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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.Attachable;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Merge;
import org.jooq.SQLDialectNotSupportedException;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableTable;

/**
 * @author Lukas Eder
 */
class InsertQueryImpl<R extends TableRecord<R>> extends AbstractStoreQuery<R> implements InsertQuery<R> {

    private static final long        serialVersionUID = 4466005417945353842L;

    private final FieldMapForUpdate  updateMap;
    private final FieldMapsForInsert insertMaps;
    private boolean                  onDuplicateKeyUpdate;

    InsertQueryImpl(Configuration configuration, Table<R> into) {
        super(configuration, into);

        updateMap = new FieldMapForUpdate();
        insertMaps = new FieldMapsForInsert();
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        return getAttachables(insertMaps, updateMap);
    }

    @Override
    public final void setRecord(R record) {
        for (Field<?> field : record.getFields()) {
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
        this.onDuplicateKeyUpdate = flag;
    }

    @Override
    public final void addValueForUpdate(Field<?> field, Object value) {
        addValueForUpdate(field, val(value));
    }

    @Override
    public final void addValueForUpdate(Field<?> field, Field<?> value) {
        if (value == null) {
            addValueForUpdate(field, (Object) value);
            return;
        }

        updateMap.put(field, value);
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
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();

        if (!onDuplicateKeyUpdate) {
            toSQLInsert(configuration, inlineParameters, sb);
        }

        // ON DUPLICATE KEY UPDATE clause
        else {
            switch (configuration.getDialect()) {

                // MySQL has a nice syntax for this
                case MYSQL: {
                    toSQLInsert(configuration, inlineParameters, sb);
                    sb.append(" on duplicate key update ");
                    sb.append(internal(updateMap).toSQLReference(configuration, inlineParameters));
                    break;
                }

                // Some dialects can't really handle this clause. Simulation
                // is done in two steps
                case H2: {
                }

                // Some databases allow for simulating this clause using a
                // MERGE statement
                case DB2:
                case HSQLDB:
                case ORACLE:
                case SQLSERVER:
                case SYBASE: {
                    sb.append(internal(toMerge()).toSQLReference(configuration, inlineParameters));
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + configuration.getDialect());
            }
        }

        return sb.toString();
    }

    @Override
    public int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
        int result = initialIndex;

        if (!onDuplicateKeyUpdate) {
            result = bindInsert(configuration, stmt, result);
        }

        // ON DUPLICATE KEY UPDATE clause
        else {
            switch (configuration.getDialect()) {

                // MySQL has a nice syntax for this
                case MYSQL: {
                    result = bindInsert(configuration, stmt, result);
                    break;
                }

                // Some dialects can't really handle this clause. Simulation
                // is done in two steps
                case H2: {
                }

                // Some databases allow for simulating this clause using a
                // MERGE statement
                case DB2:
                case HSQLDB:
                case ORACLE:
                case SQLSERVER:
                case SYBASE: {
                    result = internal(toMerge()).bindReference(configuration, stmt, result);
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + configuration.getDialect());
            }
        }

        return result;
    }

    private final void toSQLInsert(Configuration configuration, boolean inlineParameters, StringBuilder sb) {
        sb.append("insert into ");
        sb.append(internal(getInto()).toSQLReference(configuration, inlineParameters));
        sb.append(" ");
        sb.append(internal(insertMaps).toSQLReference(configuration, inlineParameters));
    }

    private int bindInsert(Configuration configuration, PreparedStatement stmt, int result) throws SQLException {
        result = internal(getInto()).bindReference(configuration, stmt, result);
        result = internal(insertMaps).bindReference(configuration, stmt, result);
        result = internal(updateMap).bindReference(configuration, stmt, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private final Merge toMerge() {
        if (getInto() instanceof UpdatableTable) {
            UpdatableTable<R> into = (UpdatableTable<R>) getInto();

            Condition condition = null;
            List<Field<?>> key = new ArrayList<Field<?>>();

            for (Field<?> f : into.getMainKey().getFields()) {
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

            return create().mergeInto(into)
                           .usingDual()
                           .on(condition)
                           .whenMatchedThenUpdate()
                           .set(updateMap)
                           .whenNotMatchedThenInsert(insertMaps.getMap().keySet())
                           .values(insertMaps.getMap().values());
        }
        else {
            throw new IllegalStateException("The ON DUPLICATE KEY UPDATE clause cannot be simulated when inserting into non-updatable tables : " + getInto());
        }
    }

    @Override
    protected boolean isExecutable() {
        return insertMaps.isExecutable();
    }
}
