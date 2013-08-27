/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.fieldByName;

import java.util.ArrayList;
import java.util.List;

import org.jooq.ArrayRecord;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.UDTRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.util.h2.H2DataType;

/**
 * An unnested array
 *
 * @author Lukas Eder
 */
class ArrayTable extends AbstractTable<Record> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 2380426377794577041L;

    private final Field<?>       array;
    private final Fields<Record> field;
    private final String         alias;

    ArrayTable(Field<?> array) {
        this(array, "array_table");
    }

    @SuppressWarnings({ "unchecked" })
    ArrayTable(Field<?> array, String alias) {
        super(alias);

        Class<?> arrayType;

        // TODO [#523] Solve this in a more object-oriented way...
        if (array.getDataType().getType().isArray()) {
            arrayType = array.getDataType().getType().getComponentType();
        }

        // [#1110] Keep track of element type information of Oracle VARRAY / TABLE types
        else if (array instanceof ArrayConstant) {
            arrayType = array.getDataType().getType();
        }

        // [#1111] Keep track of element type information of Oracle
        // VARRAY / TABLE types returned from functions
        else if (ArrayRecord.class.isAssignableFrom(array.getDataType().getType())) {
            // TODO [#523] This information should be available in ARRAY meta-data
            ArrayRecord<?> dummy = Utils.newArrayRecord((Class<ArrayRecord<?>>) array.getDataType().getType(), new DefaultConfiguration());
            arrayType = dummy.getDataType().getType();
        }

        // Is this case possible?
        else {
            arrayType = Object.class;
        }

        this.array = array;
        this.alias = alias;
        this.field = init(alias, arrayType);

        init(alias, arrayType);
    }

    @SuppressWarnings("unused")
    ArrayTable(Field<?> array, String alias, String[] fieldAliases) {
        super(alias);

        throw new UnsupportedOperationException("This constructor is not yet implemented");
    }

    private static final Fields<Record> init(String alias, Class<?> arrayType) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        // [#1114] VARRAY/TABLE of OBJECT have more than one field
        if (UDTRecord.class.isAssignableFrom(arrayType)) {
            try {
                UDTRecord<?> record = (UDTRecord<?>) arrayType.newInstance();
                for (Field<?> f : record.fields()) {
                    result.add(fieldByName(f.getDataType(), alias, f.getName()));
                }
            }
            catch (Exception e) {
                throw new DataTypeException("Bad UDT Type : " + arrayType, e);
            }
        }

        // Simple array types have a synthetic field called "COLUMN_VALUE"
        else {
            result.add(fieldByName(DSL.getDataType(arrayType), alias, "COLUMN_VALUE"));
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
    public final void toSQL(RenderContext ctx) {
        ctx.visit(table(ctx.configuration()));
    }

    @Override
    public final void bind(BindContext ctx) {
        ctx.visit(table(ctx.configuration()));
    }

    private final Table<Record> table(Configuration configuration) {
        switch (configuration.dialect().family()) {
            case ORACLE: {
                if (array.getDataType().getType().isArray()) {
                    return simulate().as(alias);
                }
                else {
                    return new OracleArrayTable().as(alias);
                }
            }

            case H2: {
                return new H2ArrayTable().as(alias);
            }

            // [#756] These dialects need special care when aliasing unnested
            // arrays
            case HSQLDB:
            case POSTGRES: {
                return new PostgresHSQLDBTable().as(alias);
            }

            // Other dialects can simulate unnested arrays using UNION ALL
            default: {
                if (array.getDataType().getType().isArray() && array instanceof Param) {
                    return simulate();
                }

                else {
                    throw new SQLDialectNotSupportedException("ARRAY TABLE is not supported for " + configuration.dialect());
                }
            }
        }
    }

    private class PostgresHSQLDBTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 6989279597964488457L;

        @Override
        public void toSQL(RenderContext context) {
            context.sql("(").keyword("select").sql(" * ")
                   .keyword("from").sql(" ").keyword("unnest").sql("(").visit(array).sql(") ")
                   .keyword("as").sql(" ").literal(alias)
                   .sql("(").literal("COLUMN_VALUE").sql("))");
        }
    }

    private class H2ArrayTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8679404596822098711L;

        @Override
        public void toSQL(RenderContext context) {
            context.keyword("table(").sql("COLUMN_VALUE ");

            // If the array type is unknown (e.g. because it's returned from
            // a stored function
            // Then the best choice for arbitrary types is varchar
            if (array.getDataType().getType() == Object[].class) {
                context.keyword(H2DataType.VARCHAR.getTypeName());
            }
            else {
                context.keyword(array.getDataType().getTypeName());
            }

            context.sql(" = ").visit(array).sql(")");
        }
    }

    private class OracleArrayTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 1716687061980551706L;

        @Override
        public void toSQL(RenderContext context) {
            context.keyword("table (").visit(array).sql(")");
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
        public final void bind(BindContext context) throws DataAccessException {
            context.visit(array);
        }

        @Override
        final Fields<Record> fields0() {
            return ArrayTable.this.fields0();
        }
    }

    @SuppressWarnings("unchecked")
    private final ArrayTableSimulation simulate() {
        return new ArrayTableSimulation(((Param<Object[]>) array).getValue(), alias);
    }

    @Override
    final Fields<Record> fields0() {
        return field;
    }
}
