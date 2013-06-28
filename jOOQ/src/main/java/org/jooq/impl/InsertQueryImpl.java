/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Merge;
import org.jooq.MergeNotMatchedStep;
import org.jooq.MergeOnConditionStep;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class InsertQueryImpl<R extends Record> extends AbstractStoreQuery<R> implements InsertQuery<R> {

    private static final long             serialVersionUID = 4466005417945353842L;

    private final FieldMapForUpdate       updateMap;
    private final FieldMapsForInsert      insertMaps;
    private boolean                       onDuplicateKeyUpdate;
    private boolean                       onDuplicateKeyIgnore;

    InsertQueryImpl(Configuration configuration, Table<R> into) {
        super(configuration, into);

        updateMap = new FieldMapForUpdate();
        insertMaps = new FieldMapsForInsert();
    }

    @Override
    public final void setRecord(R record) {
        for (Field<?> field : record.fields()) {
            addValue(record, field);
        }
    }

    @Override
    public final void newRecord() {
        insertMaps.newRecord();
    }

    @Override
    protected final FieldMapForInsert getValues() {
        return insertMaps.getMap();
    }

    @Override
    public final void addRecord(R record) {
        newRecord();
        setRecord(record);
    }

    @Override
    public final void onDuplicateKeyUpdate(boolean flag) {
        this.onDuplicateKeyIgnore = false;
        this.onDuplicateKeyUpdate = flag;
    }

    @Override
    public final void onDuplicateKeyIgnore(boolean flag) {
        this.onDuplicateKeyUpdate = false;
        this.onDuplicateKeyIgnore = flag;
    }

    @Override
    public final <T> void addValueForUpdate(Field<T> field, T value) {
        updateMap.put(field, Utils.field(value, field));
    }

    @Override
    public final <T> void addValueForUpdate(Field<T> field, Field<T> value) {
        updateMap.put(field, Utils.field(value, field));
    }

    @Override
    public final void addValuesForUpdate(Map<? extends Field<?>, ?> map) {
        updateMap.set(map);
    }

    @Override
    public final void addValues(Map<? extends Field<?>, ?> map) {
        insertMaps.getMap().set(map);
    }

    @Override
    public final void toSQL(RenderContext context) {

        // ON DUPLICATE KEY UPDATE clause
        // ------------------------------
        if (onDuplicateKeyUpdate) {
            switch (context.configuration().dialect().family()) {

                // MySQL has a nice syntax for this
                case CUBRID:
                case MARIADB:
                case MYSQL: {
                    toSQLInsert(context);
                    context.formatSeparator()
                           .keyword("on duplicate key update ")
                           .sql(updateMap);

                    break;
                }

                // Some dialects can't really handle this clause. Simulation
                // should be done in two steps
                case H2: {
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + context.configuration().dialect());
                }

                // Some databases allow for simulating this clause using a
                // MERGE statement
                case DB2:
                case HSQLDB:
                case ORACLE:
                case SQLSERVER:
                case SYBASE: {
                    context.sql(toMerge(context.configuration()));
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + context.configuration().dialect());
            }
        }

        // ON DUPLICATE KEY IGNORE clause
        // ------------------------------
        else if (onDuplicateKeyIgnore) {
            switch (context.configuration().dialect().family()) {

                // MySQL has a nice, native syntax for this
                case MARIADB:
                case MYSQL: {
                    toSQLInsert(context);
                    break;
                }

                // CUBRID can simulate this using ON DUPLICATE KEY UPDATE
                case CUBRID: {
                    FieldMapForUpdate update = new FieldMapForUpdate();
                    Field<?> field = getInto().field(0);
                    update.put(field, field);

                    toSQLInsert(context);
                    context.formatSeparator()
                           .keyword("on duplicate key update ")
                           .sql(update);

                    break;
                }

                // Some dialects can't really handle this clause. Simulation
                // should be done in two steps
                case H2: {
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY IGNORE clause cannot be simulated for " + context.configuration().dialect());
                }

                // Some databases allow for simulating this clause using a
                // MERGE statement
                case DB2:
                case HSQLDB:
                case ORACLE:
                case SQLSERVER:
                case SYBASE: {
                    context.sql(toMerge(context.configuration()));
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY IGNORE clause cannot be simulated for " + context.configuration().dialect());
            }
        }

        // Default mode
        // ------------
        else {
            toSQLInsert(context);
        }
    }

    @Override
    public final void bind(BindContext context) {

        // ON DUPLICATE KEY UPDATE clause
        // ------------------------------
        if (onDuplicateKeyUpdate) {
            switch (context.configuration().dialect().family()) {

                // MySQL has a nice syntax for this
                case CUBRID:
                case MARIADB:
                case MYSQL: {
                    bindInsert(context);
                    break;
                }

                // Some dialects can't really handle this clause. Simulation
                // is done in two steps
                case H2: {
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + context.configuration().dialect());
                }

                // Some databases allow for simulating this clause using a
                // MERGE statement
                case DB2:
                case HSQLDB:
                case ORACLE:
                case SQLSERVER:
                case SYBASE: {
                    context.bind(toMerge(context.configuration()));
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + context.configuration().dialect());
            }
        }

        // ON DUPLICATE KEY IGNORE clause
        // ------------------------------
        else if (onDuplicateKeyIgnore) {
            switch (context.configuration().dialect().family()) {

                // MySQL has a nice, native syntax for this
                case MARIADB:
                case MYSQL: {
                    bindInsert(context);
                    break;
                }

                // CUBRID can simulate this using ON DUPLICATE KEY UPDATE
                case CUBRID: {
                    bindInsert(context);
                    break;
                }

                // Some dialects can't really handle this clause. Simulation
                // is done in two steps
                case H2: {
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY IGNORE clause cannot be simulated for " + context.configuration().dialect());
                }

                // Some databases allow for simulating this clause using a
                // MERGE statement
                case DB2:
                case HSQLDB:
                case ORACLE:
                case SQLSERVER:
                case SYBASE: {
                    context.bind(toMerge(context.configuration()));
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY IGNORE clause cannot be simulated for " + context.configuration().dialect());
            }
        }

        // Default mode
        // ------------
        else {
            bindInsert(context);
        }
    }

    private final void toSQLInsert(RenderContext context) {
        context.keyword("insert ")
               // [#1295] MySQL natively supports the IGNORE keyword
               .keyword((onDuplicateKeyIgnore && asList(MARIADB, MYSQL).contains(context.configuration().dialect())) ? "ignore " : "")
               .keyword("into ")
               .sql(getInto())
               .sql(" ")
               .sql(insertMaps);

        toSQLReturning(context);
    }

    private final void bindInsert(BindContext context) {
        context.bind(getInto())
               .bind(insertMaps)
               .bind(updateMap);

        bindReturning(context);
    }

    @SuppressWarnings("unchecked")
    private final Merge<R> toMerge(Configuration configuration) {
        Table<R> into = getInto();

        if (into.getPrimaryKey() != null) {
            Condition condition = null;
            List<Field<?>> key = new ArrayList<Field<?>>();

            for (Field<?> f : into.getPrimaryKey().getFields()) {
                Field<Object> field = (Field<Object>) f;
                Field<Object> value = (Field<Object>) insertMaps.getMap().get(field);

                key.add(value);
                Condition other = field.equal(value);

                if (condition == null) {
                    condition = other;
                }
                else {
                    condition = condition.and(other);
                }
            }

            MergeOnConditionStep<R> on =
            create(configuration).mergeInto(into)
                                 .usingDual()
                                 .on(condition);

            // [#1295] Use UPDATE clause only when with ON DUPLICATE KEY UPDATE,
            // not with ON DUPLICATE KEY IGNORE
            MergeNotMatchedStep<R> notMatched = on;
            if (onDuplicateKeyUpdate) {
                notMatched = on.whenMatchedThenUpdate()
                               .set(updateMap);
            }

            return notMatched.whenNotMatchedThenInsert(insertMaps.getMap().keySet())
                             .values(insertMaps.getMap().values());
        }
        else {
            throw new IllegalStateException("The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be simulated when inserting into non-updatable tables : " + getInto());
        }
    }

    @Override
    public final boolean isExecutable() {
        return insertMaps.isExecutable();
    }
}
