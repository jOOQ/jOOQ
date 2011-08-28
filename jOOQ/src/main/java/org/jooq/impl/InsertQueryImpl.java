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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.Merge;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
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
    private final FieldList          returning;
    private R                        returned;
    private boolean                  onDuplicateKeyUpdate;

    InsertQueryImpl(Configuration configuration, Table<R> into) {
        super(configuration, into);

        updateMap = new FieldMapForUpdate();
        insertMaps = new FieldMapsForInsert();
        returning = new FieldList();
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
    public final void toSQL(RenderContext context) {
        if (!onDuplicateKeyUpdate) {
            toSQLInsert(context);
        }

        // ON DUPLICATE KEY UPDATE clause
        else {
            switch (context.getDialect()) {

                // MySQL has a nice syntax for this
                case MYSQL: {
                    toSQLInsert(context);
                    context.sql(" on duplicate key update ");
                    context.sql(updateMap);

                    break;
                }

                // Some dialects can't really handle this clause. Simulation
                // should be done in two steps
                case H2: {
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + context.getDialect());
                }

                // Some databases allow for simulating this clause using a
                // MERGE statement
                case DB2:
                case HSQLDB:
                case ORACLE:
                case SQLSERVER:
                case SYBASE: {
                    context.sql(toMerge());
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + context.getDialect());
            }
        }
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        if (!onDuplicateKeyUpdate) {
            bindInsert(context);
        }

        // ON DUPLICATE KEY UPDATE clause
        else {
            switch (context.getDialect()) {

                // MySQL has a nice syntax for this
                case MYSQL: {
                    bindInsert(context);
                    break;
                }

                // Some dialects can't really handle this clause. Simulation
                // is done in two steps
                case H2: {
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + context.getDialect());
                }

                // Some databases allow for simulating this clause using a
                // MERGE statement
                case DB2:
                case HSQLDB:
                case ORACLE:
                case SQLSERVER:
                case SYBASE: {
                    context.bind(toMerge());
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be simulated for " + context.getDialect());
            }
        }
    }

    private final void toSQLInsert(RenderContext context) {
        context.sql("insert into ")
               .sql(getInto())
               .sql(" ")
               .sql(insertMaps);

        if (!returning.isEmpty()) {
            switch (context.getDialect()) {
                case POSTGRES:
                    context.sql(" returning ").sql(returning);
                    break;

                default:
                    // Other dialects don't render a RETURNING clause, but
                    // use JDBC's Statement.RETURN_GENERATED_KEYS mode instead
            }
        }
    }

    private final void bindInsert(BindContext context) throws SQLException {
        context.bind(getInto())
               .bind(insertMaps)
               .bind(updateMap);

        switch (context.getDialect()) {
            case POSTGRES:
                context.bind((QueryPart) returning);
                break;

            default:
                // Other dialects don't bind a RETURNING clause, but
                // use JDBC's Statement.RETURN_GENERATED_KEYS mode instead
        }
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
    protected final boolean isExecutable() {
        return insertMaps.isExecutable();
    }

    @Override
    protected final PreparedStatement prepare(Configuration configuration, String sql) throws SQLException {
        if (returning.isEmpty()) {
            return super.prepare(configuration, sql);
        }
        else {
            Connection connection = configuration.getConnection();

            switch (configuration.getDialect()) {
                // Some JDBC drivers do not support generated keys altogether
                case INGRES:
                case SYBASE:
                case SQLITE: {
                    return super.prepare(configuration, sql);
                }

                // Postgres uses the RETURNING clause in SQL
                case POSTGRES:
                    return super.prepare(configuration, sql);

                // Some dialects can only return AUTO_INCREMENT values
                // Other values have to be fetched in a second step
                case DERBY:
                case H2:
                case MYSQL:
                    return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                case SQLSERVER:
                    return connection.prepareStatement(sql, new String[] { "id_generated" });

                // The default is to return all requested fields directly
                default: {
                    List<String> names = new ArrayList<String>();

                    for (Field<?> field : returning) {
                        names.add(field.getName());
                    }

                    return connection.prepareStatement(sql, names.toArray(new String[names.size()]));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final int execute(Configuration configuration, PreparedStatement statement) throws SQLException {
        if (returning.isEmpty()) {
            return super.execute(configuration, statement);
        }
        else {
            int result = 1;
            ResultSet rs;

            switch (configuration.getDialect()) {

                // Some JDBC drivers do not support generated keys altogether
                case INGRES:
                case SYBASE:
                case SQLITE: {
                    return super.execute(configuration, statement);
                }

                // Some dialects can only retrieve "identity" (AUTO_INCREMENT) values
                // Additional values have to be fetched explicitly
                case DERBY:
                case H2:
                case MYSQL:
                case SQLSERVER: {
                    result = statement.executeUpdate();
                    rs = statement.getGeneratedKeys();

                    if (rs.next() && getInto() instanceof UpdatableTable) {
                        UpdatableTable<R> updatable = (UpdatableTable<R>) getInto();

                        // This shouldn't be null, as relevant dialects should
                        // return empty generated keys ResultSet
                        if (updatable.getIdentity() != null) {
                            Field<Number> id = (Field<Number>) updatable.getIdentity().getField();
                            Number value = id.getDataType().convert(rs.getObject(1));
                            returned = JooqUtil.newRecord(updatable, configuration);

                            // Only the IDENTITY value was requested. No need for an
                            // additional query
                            if (returning.size() == 1 && returning.get(0).equals(id)) {
                                ((AbstractRecord) returned).setValue(id, new Value<Number>(value));
                            }

                            // Other values are requested, too. Run another query
                            else {
                                Record record =
                                create(configuration).select(returning)
                                                     .from(updatable)
                                                     .where(id.equal(value))
                                                     .fetchOne();

                                for (Field<?> field : returning) {
                                    setValue(record, field);
                                }
                            }
                        }
                    }

                    return result;
                }

                // Postgres can execute the INSERT .. RETURNING clause like
                // a select clause. JDBC support is not implemented
                case POSTGRES: {
                    rs = statement.executeQuery();

                    break;
                }

               // These dialects have full JDBC support
                case DB2:
                case HSQLDB:
                case ORACLE:
                default: {
                    result = statement.executeUpdate();
                    rs = statement.getGeneratedKeys();

                    break;
                }
            }

            CursorImpl<R> cursor = new CursorImpl<R>(configuration, returning, rs, statement, getInto().getRecordType());
            returned = cursor.fetchOne();
            return result;
        }
    }

    /**
     * Generic type-safe utility method
     */
    private final <T> void setValue(Record record, Field<T> field) {
        ((AbstractRecord) returned).setValue(field, new Value<T>(record.getValue(field)));
    }

    @Override
    public final void setReturning() {
        setReturning(getInto().getFields());
    }

    @Override
    public final void setReturning(Identity<R, ? extends Number> identity) {
        if (identity != null) {
            setReturning(identity.getField());
        }
    }

    @Override
    public final void setReturning(Field<?>... fields) {
        setReturning(Arrays.asList(fields));
    }

    @Override
    public final void setReturning(Collection<? extends Field<?>> fields) {
        returning.clear();
        returning.addAll(fields);
    }

    @Override
    public final R getReturned() {
        return returned;
    }
}
