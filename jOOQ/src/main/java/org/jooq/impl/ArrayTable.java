/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.impl.DSL.name;

import java.util.ArrayList;
import java.util.List;

// ...
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.UDTRecord;
import org.jooq.exception.DataTypeException;
import org.jooq.util.h2.H2DataType;

/**
 * An unnested array
 *
 * @author Lukas Eder
 */
final class ArrayTable extends AbstractTable<Record> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 2380426377794577041L;

    private final Field<?>       array;
    private final Fields<Record> field;
    private final String         alias;
    private final String[]       fieldAliases;

    ArrayTable(Field<?> array) {
        this(array, "array_table");
    }

    ArrayTable(Field<?> array, String alias) {
        this(array, alias, new String[] { "COLUMN_VALUE" });
    }

    @SuppressWarnings({ "unchecked" })
    ArrayTable(Field<?> array, String alias, String[] fieldAliases) {
        super(alias);

        Class<?> arrayType;

        // TODO [#523] Solve this in a more object-oriented way...
        if (array.getDataType().getType().isArray()) {
            arrayType = array.getDataType().getType().getComponentType();
        }
















        // Is this case possible?
        else {
            arrayType = Object.class;
        }

        this.array = array;
        this.alias = alias;
        this.fieldAliases = fieldAliases;
        this.field = init(arrayType, alias, fieldAliases);
    }

    private static final Fields<Record> init(Class<?> arrayType, String alias, String[] fieldAliases) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        // [#1114] VARRAY/TABLE of OBJECT have more than one field
        if (UDTRecord.class.isAssignableFrom(arrayType)) {
            try {
                UDTRecord<?> record = (UDTRecord<?>) arrayType.newInstance();
                for (Field<?> f : record.fields()) {
                    result.add(DSL.field(name(alias, f.getName()), f.getDataType()));
                }
            }
            catch (Exception e) {
                throw new DataTypeException("Bad UDT Type : " + arrayType, e);
            }
        }

        // Simple array types have a synthetic field called "COLUMN_VALUE"
        else {
            result.add(DSL.field(name(alias, "COLUMN_VALUE"), DSL.getDataType(arrayType)));
        }

        return new Fields<Record>(result);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record> as(String as) {
        return new ArrayTable(array, as);
    }

    @Override
    public final Table<Record> as(String as, String... fieldAliases) {
        return new ArrayTable(array, as, fieldAliases);
    }

    @Override
    public final boolean declaresTables() {

        // Always true, because unnested tables are always aliased
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(table(ctx.configuration()));
    }

    private final Table<Record> table(Configuration configuration) {
        switch (configuration.family()) {








            case H2:
                return new H2ArrayTable().as(alias);

            // [#756] These dialects need special care when aliasing unnested
            // arrays
            case HSQLDB:
            case POSTGRES:
                return new PostgresHSQLDBTable().as(alias, fieldAliases);

            // Other dialects can simulate unnested arrays using UNION ALL
            default:
                if (array.getDataType().getType().isArray() && array instanceof Param)
                    return emulate();








                else
                    return DSL.table("{0}", array).as(alias);
        }
    }

    private class PostgresHSQLDBTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 6989279597964488457L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.keyword("unnest").sql('(').visit(array).sql(")");
        }
    }

    private class H2ArrayTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8679404596822098711L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.keyword("table")
               .sql('(')
               .visit(name(fieldAliases == null || fieldAliases.length == 0 ? "COLUMN_VALUE" : fieldAliases[0]))
               .sql(' ');

            // If the array type is unknown (e.g. because it's returned from
            // a stored function), then a reasonable choice for arbitrary types is varchar
            if (array.getDataType().getType() == Object[].class)
                ctx.keyword(H2DataType.VARCHAR.getTypeName());
            else
                ctx.keyword(array.getDataType().getTypeName());

            ctx.sql(" = ").visit(array).sql(')');
        }
    }
















    private abstract class DialectArrayTable extends AbstractTable<Record> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2662639259338694177L;

        DialectArrayTable() {
            super(alias);
        }

        @Override
        public final Class<? extends Record> getRecordType() {
            return RecordImpl.class;
        }

        @Override
        public final Table<Record> as(String as) {
            return new TableAlias<Record>(this, as);
        }

        @Override
        public final Table<Record> as(String as, String... fieldAliases) {
            return new TableAlias<Record>(this, as, fieldAliases);
        }

        @Override
        final Fields<Record> fields0() {
            return ArrayTable.this.fields0();
        }
    }

    @SuppressWarnings("unchecked")
    private final ArrayTableEmulation emulate() {
        return new ArrayTableEmulation(((Param<Object[]>) array).getValue(), alias);
    }

    @Override
    final Fields<Record> fields0() {
        return field;
    }
}
