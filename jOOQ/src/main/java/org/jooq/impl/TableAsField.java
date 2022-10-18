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

// ...
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
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_ROW;
import static org.jooq.impl.Tools.unalias;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;

import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.SQLDialect;
import org.jooq.Table;
// ...

/**
 * @author Lukas Eder
 */
final class TableAsField<R extends Record>
extends
    AbstractRowAsField<R>
implements
    QOM.TableAsField<R>,
    ScopeMappableWrapper<TableAsField<R>, Table<R>>
{

    final Table<R> table;

    TableAsField(Table<R> table) {
        this(table, table.getQualifiedName());
    }

    TableAsField(final Table<R> table, Name as) {
        super(as, new RecordDataType<>(
            ((AbstractTable<R>) table).fieldsRow(),
            (Class<R>) table.getRecordType(),
            table.getName()
        ));

        this.table = table;
    }

    @Override
    final Table<R> fields0() {
        return table;
    }

    @SuppressWarnings("unchecked")
    @Override
    final Class<R> getRecordType() {
        return (Class<R>) table.getRecordType();
    }

    @Override
    final void acceptDefault(Context<?> ctx) {
        if (RowAsField.NO_NATIVE_SUPPORT.contains(ctx.dialect()))
            ctx.data(DATA_LIST_ALREADY_INDENTED, true, c -> c.visit(new SelectFieldList<>(emulatedFields(ctx.configuration()).fields.fields)));




        // [#4727] [#13664] [#14100] In the first versions of jOOQ 3.17, there
        // used to be native implementations for this feature here, but they
        // produced a significant amount of problems, most importantly #14100,
        // where we relied on code generation column order to match production
        // metadata column order, something which we should never rely upon.
        // Hence, even in the presence of native support (e.g. PostgreSQL), we
        // are now emulating the feature.
        else
            ctx.visit(new RowAsField<>(table.fieldsRow(), getQualifiedName()));
    }

    @Override
    public Field<R> as(Name alias) {
        return new TableAsField<>(table, alias);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<R> $table() {
        return table;
    }















    // -------------------------------------------------------------------------
    // XXX: ScopeMappable
    // -------------------------------------------------------------------------

    @Override
    public final TableAsField<R> wrap(Table<R> wrapped) {
        return new TableAsField<>(wrapped, getQualifiedName());
    }
}
