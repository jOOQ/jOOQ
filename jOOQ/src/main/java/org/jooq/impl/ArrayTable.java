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

import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.Keywords.K_UNNEST;
import static org.jooq.impl.Names.N_ARRAY_TABLE;
import static org.jooq.impl.Names.N_COLUMN_VALUE;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.map;

// ...
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Param;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableOptions;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.QOM.UTransient;

/**
 * An unnested array
 *
 * @author Lukas Eder
 */
final class ArrayTable
extends
    AbstractAutoAliasTable<Record>
implements
    UNotYetImplemented
{

    private final Field<?>           array;
    private final FieldsImpl<Record> field;

    ArrayTable(Field<?> array) {
        this(array, N_ARRAY_TABLE);
    }

    ArrayTable(Field<?> array, Name alias) {

        // [#7863] TODO: Possibly resolve field aliases from UDT type
        this(array, alias, (Name[]) null);
    }

    ArrayTable(Field<?> array, Name alias, Name[] fieldAliases) {
        this(array, alias, init(arrayType(array), alias, fieldAliases(fieldAliases)[0]));
    }

    private ArrayTable(Field<?> array, Name alias, FieldsImpl<Record> fields) {
        super(alias, Tools.map(fields.fields, Field::getUnqualifiedName, Name[]::new));

        this.array = array;
        this.field = fields;
    }

    private static final Class<?> arrayType(Field<?> array) {
        Class<?> arrayType;

        if (array.getDataType().getType().isArray())
            arrayType = array.getDataType().getArrayComponentType();















        // Is this case possible?
        else
            arrayType = Object.class;

        return arrayType;
    }

    static final Name[] fieldAliases(Name[] fieldAliases) {
        return isEmpty(fieldAliases) ? new Name[] { N_COLUMN_VALUE } : fieldAliases;
    }

    static final FieldsImpl<Record> init(Class<?> arrayType, Name alias, Name fieldAlias) {

        // [#1114] [#7863] VARRAY/TABLE of OBJECT have more than one field
        if (Record.class.isAssignableFrom(arrayType)) {
            try {
                return new FieldsImpl<>(map(
                    ((Record) arrayType.getDeclaredConstructor().newInstance()).fields(),
                    f -> DSL.field(alias.append(f.getUnqualifiedName()), f.getDataType())
                ));
            }
            catch (Exception e) {
                throw new DataTypeException("Bad UDT Type : " + arrayType, e);
            }
        }

        // Simple array types have a synthetic field called "COLUMN_VALUE"
        else
            return new FieldsImpl<>(DSL.field(alias.unqualifiedName().append(fieldAlias.unqualifiedName()), DSL.getDataType(arrayType)));
    }

    @Override
    final ArrayTable construct(Name newAlias, Name[] newFieldAliases) {
        return new ArrayTable(array, newAlias, newFieldAliases);
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
        ctx.visit(table(ctx.configuration()));
    }

    private final QueryPart table(Configuration configuration) {
        boolean isArray = array.getDataType().getType().isArray();

        switch (configuration.family()) {











            // Most dialects can simulate unnested arrays using UNION ALL
















            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case SQLITE:
                if (isArray && array instanceof Param)
                    return emulateParam();

                // [#14416] While Array isn't supported everywhere, Unnest(Array) is
                else if (isArray && array instanceof Array)
                    return emulateArray();








                else
                    return DSL.table("{0}", array);

            // [#756] The standard SQL behaviour
            default:
                return new StandardUnnest();
        }
    }

    private class StandardUnnest extends DialectArrayTable {

        @Override
        public final void accept(Context<?> ctx) {
            ctx.visit(K_UNNEST).sql('(').visit(array).sql(")");
        }
    }












    private abstract class DialectArrayTable
    extends
        AbstractTable<Record>
    implements
        AutoAlias<Table<Record>>,
        UTransient
    {

        DialectArrayTable() {
            super(TableOptions.expression(), alias);
        }

        @Override
        public final boolean declaresTables() {
            return true;
        }

        @Override
        public final Class<? extends Record> getRecordType() {
            return RecordImplN.class;
        }

        @Override
        final FieldsImpl<Record> fields0() {
            return ArrayTable.this.fields0();
        }

        @Override
        public final Table<Record> autoAlias(Context<?> ctx, Table<Record> t) {
            return t.as(alias, fieldAliases);
        }
    }

    @SuppressWarnings("unchecked")
    private final QueryPart emulateParam() {
        return new ArrayTableEmulation(((Param<Object[]>) array).getValue(), fieldAliases);
    }

    @SuppressWarnings("unchecked")
    private final QueryPart emulateArray() {
        return new ArrayTableEmulation(((Array<Object>) array).fields.fields, fieldAliases);
    }
}
