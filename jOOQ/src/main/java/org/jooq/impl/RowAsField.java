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

// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.impl.Keywords.K_ROW;
import static org.jooq.impl.Keywords.K_TUPLE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_ROW_CONTENT;

import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Row;
import org.jooq.SQLDialect;
// ...

/**
 * @author Lukas Eder
 */
final class RowAsField<ROW extends Row, REC extends Record> extends AbstractRowAsField<REC> implements QOM.RowAsField<REC> {

    // [#11485] Trino supports this, but their JDBC driver doesn't expose Structs yet: https://github.com/trinodb/trino/issues/16479
    static final Set<SQLDialect> NO_NATIVE_SUPPORT = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE, TRINO);

    final ROW                    row;

    RowAsField(ROW row) {
        this(row, DSL.name("nested"));
    }

    RowAsField(ROW row, Name as) {
        super(as, new RecordDataType<>(row));

        this.row = row;
    }

    @Override
    final ROW fields0() {
        return row;
    }

    @SuppressWarnings("unchecked")
    @Override
    final Class<REC> getRecordType() {
        return (Class<REC>) Tools.recordType(row.size());
    }

    @Override
    final void acceptDefault(Context<?> ctx) {
        if (NO_NATIVE_SUPPORT.contains(ctx.dialect())) {
            ctx.data(DATA_LIST_ALREADY_INDENTED, true, c -> c.visit(new SelectFieldList<>(emulatedFields(ctx.configuration()).fields.fields)));
        }
        else {
            switch (ctx.family()) {












                case CLICKHOUSE:
                    ctx.data(DATA_ROW_CONTENT, true, c -> c.visit(K_TUPLE).sql(' ').visit(row));
                    break;

                // [#11812] RowField is mainly used for projections, in case of which an
                //          explicit ROW keyword helps disambiguate (1) from ROW(1)
                default:
                    ctx.data(DATA_ROW_CONTENT, true, c -> c.visit(K_ROW).sql(' ').visit(row));
                    break;
            }
        }
    }

    @Override
    public Field<REC> as(Name alias) {
        return new RowAsField<>(row, alias);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $aliased() {
        return new RowAsField<>(row);
    }

    @Override
    public final Name $alias() {
        return getQualifiedName();
    }

    @Override
    public final Row $row() {
        return row;
    }














}
