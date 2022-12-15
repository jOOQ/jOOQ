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

import static org.jooq.impl.Keywords.K_ARRAY;
import static org.jooq.impl.Keywords.K_UNNEST;
import static org.jooq.impl.Names.N_ARRAY_TABLE;
import static org.jooq.impl.Names.N_COLUMN_VALUE;
import static org.jooq.impl.Tools.isEmpty;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.TableOptions;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.QOM.UTransient;

/**
 * An unnested array
 *
 * @author Lukas Eder
 */
final class ArrayOfValues
extends
    AbstractAutoAliasTable<Record>
implements
    UNotYetImplemented
{

    private final Field<?>[]         array;
    private final FieldsImpl<Record> field;

    ArrayOfValues(Field<?>[] array) {
        this(array, N_ARRAY_TABLE);
    }

    ArrayOfValues(Field<?>[] array, Name alias) {
        this(array, alias, null);
    }

    ArrayOfValues(Field<?>[] array, Name alias, Name[] fieldAliases) {
        super(alias, ArrayTable.fieldAliases(fieldAliases));

        Class<?> arrayType = !isEmpty(array) ? array[0].getType() : Object.class;

        this.array = array;
        this.field = ArrayTable.init(arrayType, this.alias, this.fieldAliases[0]);
    }

    @Override
    final ArrayOfValues construct(Name newAlias, Name[] newFieldAliases) {
        return new ArrayOfValues(array, newAlias, newFieldAliases);
    }

    // -------------------------------------------------------------------------
    // XXX: Table API
    // -------------------------------------------------------------------------

    @Override
    public final Class<? extends Record> getRecordType() {
        // TODO: [#4695] Calculate the correct Record[B] type
        return RecordImplN.class;
    }

    @Override
    final FieldsImpl<Record> fields0() {
        return field;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

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
                ctx.visit(new ArrayTableEmulation(array, fieldAliases));
                break;

            default:
                ctx.visit(new StandardUnnest());
                break;
        }
    }

    private class StandardUnnest extends DialectArrayTable {

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
}
