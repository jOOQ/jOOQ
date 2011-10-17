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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Package;
import org.jooq.Parameter;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.UDTRecord;

/**
 * A common base class for stored procedures
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 * @deprecated - 1.6.8 [#852] - The stored procedure / stored function
 *             distinction has been reviewed in jOOQ. The 12 currently supported
 *             RDBMS have such a distinct idea of what is a procedure and what
 *             is a function that it makes no longer sense to distinguish them
 *             generally, in jOOQ. See <a
 *             href="https://sourceforge.net/apps/trac/jooq/ticket/852"
 *             >https://sourceforge.net/apps/trac/jooq/ticket/852</a> for more
 *             details.
 */
@Deprecated
public class StoredProcedureImpl extends AbstractStoredProcedure {

    private static final long                serialVersionUID = -8046199737354507547L;
    private static final JooqLogger          log              = JooqLogger.getLogger(StoredProcedureImpl.class);

    private Map<Parameter<?>, Object>        results;
    private final Map<Parameter<?>, Integer> parameterIndexes;

    public StoredProcedureImpl(SQLDialect dialect, String name, Schema schema) {
        this(Factory.getNewFactory(dialect), name, schema, null);
    }

    public StoredProcedureImpl(SQLDialect dialect, String name, Schema schema, Package pkg) {
        this(Factory.getNewFactory(dialect), name, schema, pkg);
    }

    public StoredProcedureImpl(Configuration configuration, String name, Schema schema) {
        this(configuration, name, schema, null);
    }

    public StoredProcedureImpl(Configuration configuration, String name, Schema schema, Package pkg) {
        super(configuration, name, schema, pkg);

        this.parameterIndexes = new HashMap<Parameter<?>, Integer>();
    }

    @Override
    protected final List<Attachable> getAttachables2() {
        return Collections.emptyList();
    }

    @Override
    public final int execute() throws SQLException {
        StopWatch watch = new StopWatch();

        CallableStatement statement = null;
        try {
            results = new HashMap<Parameter<?>, Object>();
            Configuration configuration = attachable.getConfiguration();
            Connection connection = configuration.getConnection();

            String sql = create(configuration).render(this);
            watch.splitTrace("SQL rendered");

            if (log.isDebugEnabled())
                log.debug("Executing procedure", create(configuration).renderInlined(this));
            if (log.isTraceEnabled())
                log.trace("Preparing statement", sql);

            statement = connection.prepareCall(sql);
            watch.splitTrace("Statement prepared");

            create(configuration).bind(this, statement);
            watch.splitTrace("Variables bound");

            registerOutParameters(configuration, statement);
            watch.splitTrace("OUT params registered");

            // Postgres requires two separate queries running in the same
            // transaction to be executed when fetching refcursor types
            boolean autoCommit = connection.getAutoCommit();
            if (autoCommit && configuration.getDialect() == SQLDialect.POSTGRES) {
                connection.setAutoCommit(false);
            }

            statement.execute();

            if (autoCommit && configuration.getDialect() == SQLDialect.POSTGRES) {
                connection.setAutoCommit(autoCommit);
            }

            watch.splitTrace("Procedure called");

            fetchOutParameters(configuration, statement);
            watch.splitTrace("OUT params fetched");

            return 0;
        }
        finally {
            JooqUtil.safeClose(statement);
            watch.splitDebug("Procedure executed");
        }
    }

    private final void fetchOutParameters(Configuration configuration, CallableStatement statement) throws SQLException {
        for (Parameter<?> parameter : getParameters()) {
            int index = parameterIndexes.get(parameter);

            if (parameter.equals(getReturnParameter()) ||
                getOutParameters().contains(parameter)) {

                results.put(parameter, FieldTypeHelper.getFromStatement(
                    configuration, statement, parameter.getType(), index));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private final void registerOutParameters(Configuration configuration, CallableStatement statement) throws SQLException {

        // Register all out / inout parameters according to their position
        // Note that some RDBMS do not support binding by name very well
        for (Parameter<?> parameter : getParameters()) {
            if (parameter.equals(getReturnParameter()) ||
                getOutParameters().contains(parameter)) {

                int index = parameterIndexes.get(parameter);
                int sqlType = parameter.getDataType().getDataType(configuration).getSQLType();

                switch (configuration.getDialect()) {

                    // For some user defined types Oracle needs to bind
                    // also the type name
                    case ORACLE: {
                        if (sqlType == Types.STRUCT) {
                            UDTRecord<?> record = JooqUtil.newRecord((Class<? extends UDTRecord<?>>) parameter.getType());
                            statement.registerOutParameter(index, Types.STRUCT, record.getSQLTypeName());
                        }

                        else if (sqlType == Types.ARRAY) {
                            ArrayRecord<?> record = JooqUtil.newArrayRecord(
                                (Class<? extends ArrayRecord<?>>) parameter.getType(), configuration);
                            statement.registerOutParameter(index, Types.ARRAY, record.getName());
                        }

                        // The default behaviour is not to register a type
                        // mapping
                        else {
                            statement.registerOutParameter(index, sqlType);
                        }

                        break;
                    }

                    default: {
                        statement.registerOutParameter(index, sqlType);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        for (Parameter<?> parameter : getParameters()) {
            int index = context.peekIndex();
            parameterIndexes.put(parameter, index);

            if (getInValues().get(parameter) != null) {
                context.bind(getInValues().get(parameter));

                // [#391] This happens when null literals are used as IN/OUT
                // parameters. They're not bound as in value, but they need to
                // be registered as OUT parameter
                if (index == context.peekIndex() && getOutParameters().contains(parameter)) {
                    context.nextIndex();
                }
            }

            // Skip one index for OUT parameters
            else {
                context.nextIndex();
            }
        }
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql("{ ");

        if (getReturnParameter() != null) {
            context.sql("? = ");
        }

        context.sql("call ");
        toSQLQualifiedName(context);
        context.sql("(");

        String separator = "";
        for (Parameter<?> parameter : getParameters()) {
            context.sql(separator);

            // The return value has already been written
            if (parameter.equals(getReturnParameter())) {
                continue;
            }

            // OUT and IN OUT parameters are always written as a '?' bind variable
            else if (getOutParameters().contains(parameter)) {
                context.sql("?");
            }

            // IN parameters are rendered normally
            else {
                Field<?> value = getInValues().get(parameter);

                // Disambiguate overloaded procedure signatures
                if (SQLDialect.POSTGRES == context.getDialect() && isOverloaded()) {
                    value = value.cast(parameter.getType());
                }

                context.sql(value);
            }

            separator = ", ";
        }

        context.sql(") }");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T getValue(Parameter<T> parameter) {
        return (T) results.get(parameter);
    }
}
