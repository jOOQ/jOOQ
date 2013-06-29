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

import static org.jooq.impl.Utils.fieldArray;
import static org.jooq.util.sqlite.SQLiteDSL.rowid;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * A default implementation for store queries.
 *
 * @author Lukas Eder
 */
abstract class AbstractStoreQuery<R extends Record> extends AbstractQuery implements StoreQuery<R> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = 6864591335823160569L;

    private final Table<R>                into;
    private final QueryPartList<Field<?>> returning;
    private Result<R>                     returned;

    AbstractStoreQuery(Configuration configuration, Table<R> into) {
        super(configuration);

        this.into = into;
        this.returning = new QueryPartList<Field<?>>();
    }

    protected abstract Map<Field<?>, Field<?>> getValues();

    final Table<R> getInto() {
        return into;
    }

    final <T> void addValue(R record, Field<T> field) {
        addValue(field, record.getValue(field));
    }

    @Override
    public final <T> void addValue(Field<T> field, T value) {
        getValues().put(field, Utils.field(value, field));
    }

    @Override
    public final <T> void addValue(Field<T> field, Field<T> value) {
        getValues().put(field, Utils.field(value, field));
    }

    @Override
    public final void setReturning() {
        setReturning(getInto().fields());
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
            returned = new ResultImpl<R>(configuration(), returning);
        }

        return returned;
    }

    final void toSQLReturning(RenderContext context) {
        if (!returning.isEmpty()) {
            switch (context.configuration().dialect()) {
                case FIREBIRD:
                case POSTGRES:
                    context.formatSeparator()
                           .keyword("returning ")
                           .sql(returning);
                    break;

                default:
                    // Other dialects don't render a RETURNING clause, but
                    // use JDBC's Statement.RETURN_GENERATED_KEYS mode instead
            }
        }
    }

    final void bindReturning(BindContext context) {
        switch (context.configuration().dialect()) {
            case FIREBIRD:
            case POSTGRES:
                context.bind((QueryPart) returning);
                break;

            default:
                // Other dialects don't bind a RETURNING clause, but
                // use JDBC's Statement.RETURN_GENERATED_KEYS mode instead
        }
    }

    @Override
    protected final void prepare(ExecuteContext ctx) throws SQLException {
        Connection connection = ctx.connection();

        // Just in case, always set Sybase ASE statement mode to return
        // Generated keys if client code wants to SELECT @@identity afterwards
        if (ctx.configuration().dialect() == SQLDialect.ASE) {
            ctx.statement(connection.prepareStatement(ctx.sql(), Statement.RETURN_GENERATED_KEYS));
            return;
        }

        // Normal statement preparing if no values should be returned
        else if (returning.isEmpty()) {
            super.prepare(ctx);
            return;
        }

        // Values should be returned from the INSERT
        else {
            switch (ctx.configuration().dialect().family()) {

                // Postgres uses the RETURNING clause in SQL
                case FIREBIRD:
                case POSTGRES:
                // SQLite will select last_insert_rowid() after the INSER
                case SQLITE:
                // Sybase will select @@identity after the INSERT
                case CUBRID:
                case SYBASE:
                    super.prepare(ctx);
                    return;

                // Some dialects can only return AUTO_INCREMENT values
                // Other values have to be fetched in a second step
                // [#1260] TODO CUBRID supports this, but there's a JDBC bug
                case ASE:
                case DERBY:
                case H2:
                case INGRES:
                case MARIADB:
                case MYSQL:
                case SQLSERVER:
                    ctx.statement(connection.prepareStatement(ctx.sql(), Statement.RETURN_GENERATED_KEYS));
                    return;

                // The default is to return all requested fields directly
                case DB2:
                case HSQLDB:
                case ORACLE:
                default: {
                    List<String> names = new ArrayList<String>();

                    for (Field<?> field : returning) {
                        names.add(field.getName());
                    }

                    ctx.statement(connection.prepareStatement(ctx.sql(), names.toArray(new String[names.size()])));
                    return;
                }
            }
        }
    }

    @Override
    protected final int execute(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        if (returning.isEmpty()) {
            return super.execute(ctx, listener);
        }
        else {
            int result = 1;
            ResultSet rs;
            switch (ctx.configuration().dialect().family()) {

                // SQLite can select _rowid_ after the insert
                case SQLITE: {
                    listener.executeStart(ctx);
                    result = ctx.statement().executeUpdate();
                    listener.executeEnd(ctx);

                    DSLContext create = DSL.using(ctx.configuration());
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
                case CUBRID:
                case SYBASE: {
                    listener.executeStart(ctx);
                    result = ctx.statement().executeUpdate();
                    listener.executeEnd(ctx);

                    selectReturning(ctx.configuration(), create(ctx.configuration()).lastID());
                    return result;
                }

                // Some dialects can only retrieve "identity" (AUTO_INCREMENT) values
                // Additional values have to be fetched explicitly
                // [#1260] TODO CUBRID supports this, but there's a JDBC bug
                case ASE:
                case DERBY:
                case H2:
                case INGRES:
                case MARIADB:
                case MYSQL:
                case SQLSERVER: {
                    listener.executeStart(ctx);
                    result = ctx.statement().executeUpdate();
                    listener.executeEnd(ctx);

                    rs = ctx.statement().getGeneratedKeys();

                    try {
                        List<Object> list = new ArrayList<Object>();

                        // Some JDBC drivers seem to illegally return null
                        // from getGeneratedKeys() sometimes
                        if (rs != null) {
                            while (rs.next()) {
                                list.add(rs.getObject(1));
                            }
                        }

                        selectReturning(ctx.configuration(), list.toArray());
                        return result;
                    }
                    finally {
                        JDBCUtils.safeClose(rs);
                    }
                }

                // Firebird and Postgres can execute the INSERT .. RETURNING
                // clause like a select clause. JDBC support is not implemented
                // in the Postgres JDBC driver
                case FIREBIRD:
                case POSTGRES: {
                    listener.executeStart(ctx);
                    rs = ctx.statement().executeQuery();
                    listener.executeEnd(ctx);

                    break;
                }

                // These dialects have full JDBC support
                case DB2:
                case HSQLDB:
                case ORACLE:
                default: {
                    listener.executeStart(ctx);
                    result = ctx.statement().executeUpdate();
                    listener.executeEnd(ctx);

                    rs = ctx.statement().getGeneratedKeys();
                    break;
                }
            }

            ExecuteContext ctx2 = new DefaultExecuteContext(ctx.configuration());
            ExecuteListener listener2 = new ExecuteListeners(ctx2);

            ctx2.resultSet(rs);
            returned = new CursorImpl<R>(ctx2, listener2, fieldArray(returning), null, false, true).fetch().into(getInto());
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
                if (returning.size() == 1 && new Fields<Record>(returning).field(field) != null) {
                    for (Number id : ids) {
                        R typed = Utils.newRecord(into, configuration);
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
}
