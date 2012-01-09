/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import java.util.Collections;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
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
    private static final long serialVersionUID = 2380426377794577041L;

    private final Field<?>          array;
    private final FieldList         field;
    private final String            alias;

    ArrayTable(Field<?> array) {
        this(array, "array_table");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    ArrayTable(Field<?> array, String alias) {
        super(alias);

        Class<?> arrayType;
        if (array.getDataType().getType().isArray()) {
            arrayType = array.getDataType().getType().getComponentType();
        }
        else {
            // [#523] TODO use ArrayRecord meta data instead
            arrayType = Object.class;
        }

        this.array = array;
        this.alias = alias;
        this.field = new FieldList();
        this.field.add(new Qualifier(Factory.getDataType(arrayType), alias, "COLUMN_VALUE"));
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
    public final boolean declaresTables() {

        // Always true, because unnested tables are always aliased
        return true;
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(table(context));
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(table(context));
    }

    private final Table<Record> table(Configuration configuration) {
        switch (configuration.getDialect()) {
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
                    throw new SQLDialectNotSupportedException("ARRAY TABLE is not supported for " + configuration.getDialect());
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
            context.sql("(select * from unnest(")
                   .sql(array)
                   .sql(") as ")
                   .literal(alias)
                   .sql("(")
                   .literal("COLUMN_VALUE")
                   .sql("))");
        }
    }

    private class H2ArrayTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8679404596822098711L;

        @Override
        public void toSQL(RenderContext context) {
            context.sql("table(COLUMN_VALUE ");

            // If the array type is unknown (e.g. because it's returned from
            // a stored function
            // Then the best choice for arbitrary types is varchar
            if (array.getDataType().getType() == Object[].class) {
                context.sql(H2DataType.VARCHAR.getTypeName());
            }
            else {
                context.sql(array.getDataType().getTypeName());
            }

            context.sql(" = ").sql(array).sql(")");
        }
    }

    private class OracleArrayTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 1716687061980551706L;

        @Override
        public void toSQL(RenderContext context) {
            context.sql("table (").sql(array).sql(")");
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
        public Class<? extends Record> getRecordType() {
            return RecordImpl.class;
        }

        @Override
        public Table<Record> as(String as) {
            return new TableAlias<Record>(this, as);
        }

        @Override
        public void bind(BindContext context) throws DataAccessException {
            context.bind(array);
        }

        @Override
        protected FieldList getFieldList() {
            return ArrayTable.this.getFieldList();
        }

        @Override
        protected List<Attachable> getAttachables0() {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    private final ArrayTableSimulation simulate() {
        return new ArrayTableSimulation(((Param<Object[]>) array).getValue(), alias);
    }

    @Override
    protected final FieldList getFieldList() {
        return field;
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        return Collections.emptyList();
    }
}
