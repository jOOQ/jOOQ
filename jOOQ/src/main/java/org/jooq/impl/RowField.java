/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: http://www.jooq.org/licenses
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

import static org.jooq.Converter.fromNullable;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DefaultBinding.binding;
import static org.jooq.impl.DefaultBinding.DefaultRecordBinding.pgNewRecord;
import static org.jooq.impl.Keywords.K_ROW;
import static org.jooq.impl.Names.N_ROW;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.row0;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;

import java.util.Set;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class RowField<ROW extends Row, REC extends Record> extends AbstractField<REC> {

    static final Set<SQLDialect>   NO_NATIVE_SUPPORT = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);

    private final ROW              row;

    RowField(ROW row) {
        this(row, N_ROW);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    RowField(final ROW row, Name as) {
        super(as, new RecordDataType<>(row), CommentImpl.NO_COMMENT, binding(fromNullable(
            Object.class,
            (Class<REC>) Tools.recordType(row.size()),

            // [#7100] In non-PostgreSQL style dialects, RowField is emulated,
            // and at conversion time, we already have a synthetic Record[N],
            // so no further conversion is required.
            t -> (REC) (
                  t instanceof InternalRecord
                ? t
                : pgNewRecord(Record.class, (AbstractRow) row, t)
            )
        )));

        this.row = row;
    }

    @SuppressWarnings("unchecked")
    AbstractRow<REC> emulatedFields(Configuration configuration) {
        return (AbstractRow<REC>) row0(map(row.fields(), x -> x.as(getUnqualifiedName().unquotedName() + configuration.settings().getNamePathSeparator() + x.getName()), Field[]::new));
    }

    ROW row() {
        return row;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (NO_NATIVE_SUPPORT.contains(ctx.dialect()))
            ctx.data(DATA_LIST_ALREADY_INDENTED, true, c -> c.visit(new SelectFieldList<>(emulatedFields(ctx.configuration()).fields.fields)));





        // [#11812] RowField is mainly used for projections, in case of which an
        //          explicit ROW keyword helps disambiguate (1) from ROW(1)
        else
            ctx.visit(K_ROW).sql(' ').visit(row);
    }

    @Override
    public Field<REC> as(Name alias) {
        return new RowField<>(row, alias);
    }

    @Override
    public boolean declaresFields() {
        return true;
    }
}
