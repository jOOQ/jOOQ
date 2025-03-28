/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.impl.Tools.newRecord;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;

import org.jooq.BindingGetResultSetContext;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.ExecuteContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.impl.DefaultBinding.DefaultResultBinding;

/**
 * A handler for recursive, native {@link DSL#multiset(Select)} data structures.
 *
 * @author Lukas Eder
 */
final class ListHandler<R extends Record> {
    private final DSLContext         dsl;
    private final ExecuteContext     ctx;
    private final AbstractRow<R>     row;
    private final Class<? extends R> recordType;

    ListHandler(
        DSLContext dsl,
        ExecuteContext ctx,
        AbstractRow<R> row,
        Class<? extends R> recordType
    ) {
        this.dsl = dsl;
        this.ctx = ctx;
        this.row = row;
        this.recordType = recordType;
    }

    final Result<R> read(List<?> list) throws SQLException {
        Result<R> result = new ResultImpl<>(dsl.configuration(), row);

        for (Object o : list)
            result.add(newRecord(true, dsl.configuration(), recordType, row).operate(r -> {
                Object[] attributes =
                    o instanceof ResultSet rs
                  ? read(rs)
                  : o instanceof Struct s
                  ? s.getAttributes()
                  : o instanceof List<?> l
                  ? l.toArray()
                  : null;

                if (attributes != null) {

                    // [#13400] Recurse for nested MULTISET or ROW types
                    for (int i = 0; i < attributes.length && i < row.size(); i++) {
                        DataType<?> t = row.field(i).getDataType();

                        // [#16839] t.getRow() is null for scalar arrays, e.g. ARRAY (SELECT 1)
                        if (t.isMultiset() || t.isArray() && t.getRow() != null) {
                            if (attributes[i] instanceof List<?> l) {

                                // [#18175] We may have already recursed into a ResultSet that produces nested MULTISET
                                if (!(l instanceof Result))
                                    attributes[i] = new ListHandler(dsl, ctx, (AbstractRow<?>) t.getRow(), t.getRecordType()).read(l);
                            }
                            else if (attributes[i] instanceof Array a)
                                attributes[i] = new ListHandler(dsl, ctx, (AbstractRow<?>) t.getRow(), t.getRecordType()).read(asList((Object[]) a.getArray()));

                            // [#18175] JSON or XML MULTISET content may be nested in a ROW expression.
                            else if (attributes[i] instanceof String s)
                                attributes[i] = readMultiset(s, t);
                            else if (attributes[i] instanceof byte[] b)
                                attributes[i] = readMultiset(new String(b), t);
                        }
                        else if (t.isRecord() && (attributes[i] instanceof Struct || attributes[i] instanceof ResultSet))
                            attributes[i] = new ListHandler(dsl, ctx, (AbstractRow<?>) t.getRow(), t.getRecordType()).read(asList(attributes[i])).get(0);
                    }

                    r.fromArray(attributes);
                    r.touched(false);
                    return r;
                }
                else
                    throw new UnsupportedOperationException("No support for reading value " + o);
            }));

        return result;
    }

    @SuppressWarnings("unchecked")
    private final Object readMultiset(String s, DataType<?> t) {
        if (s == null)
            return null;
        else if (s.startsWith("<"))
            return DefaultResultBinding.readMultisetXML(dsl, (AbstractRow<Record>) t.getRow(), (Class<Record>) t.getRecordType(), s);
        else
            return DefaultResultBinding.readMultisetJSON(dsl, (AbstractRow<Record>) t.getRow(), (Class<Record>) t.getRecordType(), s);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object[] read(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Object[] result = new Object[rs.getMetaData().getColumnCount()];
            DefaultBindingGetResultSetContext c = new DefaultBindingGetResultSetContext<>(ctx, rs, 0);

            for (int i = 0; i < result.length; i++) {
                c.index(i + 1);
                c.field(row.field(i));
                row.field(i).getDataType().getBinding().get(c);
                result[i] = c.value();
            }

            return result;
        }

        return null;
    }
}

