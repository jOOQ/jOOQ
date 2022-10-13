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

import static org.jooq.impl.Keywords.K_ARRAY;
import static org.jooq.impl.Keywords.K_UNNEST;
import static org.jooq.impl.Names.N_ARRAY_TABLE;
import static org.jooq.impl.Names.N_COLUMN_VALUE;
import static org.jooq.impl.Tools.isEmpty;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableOptions;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.QOM.UTransient;

/**
 * An unnested array
 *
 * @author Lukas Eder
 */
final class ArrayOfValues extends AbstractTable<Record> implements UNotYetImplemented {

    private final Field<?>[]         array;
    private final FieldsImpl<Record> field;
    private final Name               alias;
    private final Name[]             fieldAliases;

    ArrayOfValues(Field<?>[] array) {
        this(array, N_ARRAY_TABLE);
    }

    ArrayOfValues(Field<?>[] array, Name alias) {
        this(array, alias, null);
    }

    ArrayOfValues(Field<?>[] array, Name alias, Name[] fieldAliases) {
        super(TableOptions.expression(), alias);

        Class<?> arrayType = !isEmpty(array) ? array[0].getType() : Object.class;

        this.array = array;
        this.alias = alias;
        this.fieldAliases = isEmpty(fieldAliases) ? new Name[] { N_COLUMN_VALUE } : fieldAliases;
        this.field = ArrayTable.init(arrayType, this.alias, this.fieldAliases[0]);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImplN.class;
    }

    @Override
    public final Table<Record> as(Name as) {
        return new ArrayOfValues(array, as);
    }

    @Override
    public final Table<Record> as(Name as, Name... fields) {
        return new ArrayOfValues(array, as, fields);
    }

    @Override
    public final boolean declaresTables() {

        // Always true, because unnested tables are always aliased
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

















            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case SQLITE:
                ctx.visit(new ArrayTableEmulation(array).as(alias, fieldAliases));
                break;

            default:
                ctx.visit(new PostgresHSQLDBTable().as(alias, fieldAliases));
                break;
        }
    }

    private class PostgresHSQLDBTable extends DialectArrayTable {

        @Override
        public final void accept(Context<?> ctx) {
            ctx.visit(K_UNNEST).sql('(').visit(K_ARRAY).sql('[').visit(QueryPartListView.wrap(array)).sql(']').sql(")");
        }
    }

    private abstract class DialectArrayTable extends AbstractTable<Record> implements UTransient {

        DialectArrayTable() {
            super(TableOptions.expression(), alias);
        }

        @Override
        public final Class<? extends Record> getRecordType() {
            return RecordImplN.class;
        }

        @Override
        final FieldsImpl<Record> fields0() {
            return ArrayOfValues.this.fields0();
        }
    }

    @Override
    final FieldsImpl<Record> fields0() {
        return field;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<Record> $aliased() {
        return new ArrayOfValues(array);
    }

    @Override
    public final Name $alias() {
        return alias;
    }
}
