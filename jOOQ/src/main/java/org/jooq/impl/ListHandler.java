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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;

/**
 * A handler for recursive, native {@link DSL#multiset(Select)} data structures.
 *
 * @author Lukas Eder
 */
final class ListHandler<R extends Record> {
    private final DSLContext         ctx;
    private final AbstractRow<R>     row;
    private final Class<? extends R> recordType;

    ListHandler(DSLContext ctx, AbstractRow<R> row, Class<? extends R> recordType) {
        this.ctx = ctx;
        this.row = row;
        this.recordType = recordType;
    }

    final Result<R> read(List<?> list) throws SQLException {
        Result<R> result = new ResultImpl<>(ctx.configuration(), row);

        for (Object o : list)
            result.add(newRecord(true, recordType, row, ctx.configuration()).operate(r -> {
                if (o instanceof Struct s) {
                    Object[] attributes = s.getAttributes();

                    // [#13400] Recurse for nested MULTISET or ROW types
                    for (int i = 0; i < attributes.length && i < row.size(); i++) {
                        DataType<?> t = row.field(i).getDataType();

                        if (t.isMultiset() && attributes[i] instanceof List)
                            attributes[i] = new ListHandler(ctx, (AbstractRow<?>) t.getRow(), t.getRecordType()).read((List<Struct>) attributes[i]);
                        else if (t.isRecord() && attributes[i] instanceof Struct)
                            attributes[i] = new ListHandler(ctx, (AbstractRow<?>) t.getRow(), t.getRecordType()).read(asList((Struct) attributes[i])).get(0);
                    }

                    r.fromArray(attributes);
                    r.changed(false);
                    return r;
                }
                else
                    throw new UnsupportedOperationException("No support for reading value " + o);
            }));

        return result;
    }
}

