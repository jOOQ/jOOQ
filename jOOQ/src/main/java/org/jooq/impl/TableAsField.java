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

    static final Set<SQLDialect> NO_NATIVE_SUPPORT = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);

    final Table<R>               table;

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
    final boolean noNativeSupport(Context<?> ctx) {
        return NO_NATIVE_SUPPORT.contains(ctx.dialect());
    }

    @Override
    final Table<R> fields0() {
        return table;
    }

    @Override
    final void acceptDefault(Context<?> ctx) {
        if (NO_NATIVE_SUPPORT.contains(ctx.dialect()))
            ctx.data(DATA_LIST_ALREADY_INDENTED, true, c -> c.visit(new SelectFieldList<>(emulatedFields(ctx.configuration()).fields.fields)));






        else
            ctx.qualify(false, c -> c.visit(table));
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
