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

import static org.jooq.impl.Factory.val;
import static org.jooq.util.sqlite.SQLiteFactory.rowid;

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
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.UpdatableTable;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.util.sqlite.SQLiteFactory;

/**
 * @author Lukas Eder
 */
class InsertQueryImpl<R extends Record> extends AbstractStoreQuery<R> implements InsertQuery<R> {

    private static final long        serialVersionUID = 4466005417945353842L;

    private final FieldMapForUpdate  updateMap;
    private final FieldMapsForInsert insertMaps;
    private final FieldList          returning;
    private Result<R>                returned;
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
    public final <T> void addValueForUpdate(Field<T> field, T value) {
        addValueForUpdate(field, val(value));
    }

    @Override
    public final <T> void addValueForUpdate(Field<T> field, Field<T> value) {
        if (value == null) {
            updateMap.put(field, val(null, field));
        }
        else {
            updateMap.put(field, value);
        }
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
    public final void bind(BindContext context) {
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

    private final void bindInsert(BindContext context) {
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
    private final Merge<R> toMerge() {
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
        Connection connection = configuration.getConnection();

        // Just in case, always set Sybase ASE statement mode to return
        // Generated keys if client code wants to SELECT @@identity afterwards
        if (configuration.getDialect() == SQLDialect.ASE) {
            return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        }

        // Normal statement preparing if no values should be returned
        else if (returning.isEmpty()) {
            return super.prepare(configuration, sql);
        }

        // Values should be returned from the INSERT
        else {
            switch (configuration.getDialect()) {

                // Postgres uses the RETURNING clause in SQL
                case POSTGRES:
                // SQLite will select last_insert_rowid() after the INSER
                case SQLITE:
                // Sybase will select @@identity after the INSERT
                case SYBASE:
                    return super.prepare(configuration, sql);

                // Some dialects can only return AUTO_INCREMENT values
                // Other values have to be fetched in a second step
                case ASE:
                case DERBY:
                case H2:
                case INGRES:
                case MYSQL:
                case SQLSERVER:
                    return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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

    @Override
    protected final int execute(Configuration configuration, PreparedStatement statement) throws SQLException {
        if (returning.isEmpty()) {
            return super.execute(configuration, statement);
        }
        else {
            int result = 1;
            ResultSet rs;

            switch (configuration.getDialect()) {

                // SQLite can select _rowid_ after the insert
                case SQLITE: {
                    result = statement.executeUpdate();

                    SQLiteFactory create = new SQLiteFactory(configuration.getConnection());
                    returned =
                    create.select(returning)
                          .from(getInto())
                          .where(rowid().equal(rowid().getDataType().convert(create.lastID())))
                          .fetchInto(getInto());

                    return result;
                }

                // Sybase can select @@identity after the insert
                // TODO [#832] Fix this. This might be a driver issue. JDBC
                // Generated keys don't work with jconn3, but they seem to work
                // with jTDS (which is used for Sybase ASE integration)
                case SYBASE: {
                    result = statement.executeUpdate();
                    selectReturning(configuration, create(configuration).lastID());
                    return result;
                }

                // Some dialects can only retrieve "identity" (AUTO_INCREMENT) values
                // Additional values have to be fetched explicitly
                case ASE:
                case DERBY:
                case H2:
                case INGRES:
                case MYSQL:
                case SQLSERVER: {
                    result = statement.executeUpdate();
                    rs = statement.getGeneratedKeys();

                    try {
                        List<Object> list = new ArrayList<Object>();
                        while (rs.next()) {
                            list.add(rs.getObject(1));
                        }

                        selectReturning(configuration, list.toArray());
                        return result;
                    }
                    finally {
                        rs.close();
                    }
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
            returned = cursor.fetch();
            return result;
        }
    }

    /**
     * Get the returning record in those dialects that do not support fetching
     * arbitrary fields from JDBC's {@link Statement#getGeneratedKeys()} method.
     */
    @SuppressWarnings("unchecked")
    private final void selectReturning(Configuration configuration, Object... values) {
        if (values != null && values.length > 0) {
            Table<R> into = getInto();

            // This shouldn't be null, as relevant dialects should
            // return empty generated keys ResultSet
            if (into.getIdentity() != null) {
                Field<Number> field = (Field<Number>) into.getIdentity().getField();
                Number[] ids = new Number[values.length];
                for (int i = 0; i < values.length; i++) {
                    ids[i] = field.getDataType().convert(values[i]);
                }

                // Only the IDENTITY value was requested. No need for an
                // additional query
                if (returning.size() == 1 && returning.get(0).equals(field)) {
                    for (Number id : ids) {
                        R typed = Util.newRecord(into, configuration);
                        ((AbstractRecord) typed).setValue(field, new Value<Number>(id));
                        getReturnedRecords().add(typed);
                    }
                }

                // Other values are requested, too. Run another query
                else {
                    returned =
                    create(configuration).select(returning)
                                         .from(into)
                                         .where(field.in(ids))
                                         .fetchInto(into);
                }
            }
        }
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
    public final R getReturnedRecord() {
        if (getReturnedRecords().size() == 0) {
            return null;
        }

        return getReturnedRecords().get(0);
    }

    @Override
    public final Result<R> getReturnedRecords() {
        if (returned == null) {
            returned = new ResultImpl<R>(getConfiguration(), returning);
        }

        return returned;
    }
}
