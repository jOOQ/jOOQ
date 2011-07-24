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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Package;
import org.jooq.Parameter;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.UDTRecord;

/**
 * @author Lukas Eder
 */
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

            String sql = toSQLReference(configuration);
            watch.splitTrace("SQL rendered");

            if (log.isDebugEnabled()) log.debug("Executing query", toSQLReference(configuration, true));
            if (log.isTraceEnabled()) log.trace("Preparing statement", sql);

            statement = connection.prepareCall(sql);
            watch.splitTrace("Statement prepared");

            bindReference(configuration, statement);
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
    public final int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
        int index = initialIndex;

        for (Parameter<?> parameter : getParameters()) {
            parameterIndexes.put(parameter, index);

            if (getInValues().get(parameter) != null) {
                int newIndex = internal(getInValues().get(parameter)).bindReference(configuration, stmt, index);

                // [#391] This happens when null literals are used as IN/OUT
                // parameters. They're not bound as in value, but they need to
                // be registered as OUT parameter
                if (newIndex == index && getOutParameters().contains(parameter)) {
                    newIndex++;
                }

                index = newIndex;
            }

            // Skip one index.
            else {
                index++;
            }
        }

        return index;
    }

    @Override
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();

        sb.append("{ ");

        if (getReturnParameter() != null) {
            sb.append("? = ");
        }

        sb.append("call ");
        sb.append(toSQLQualifiedName(configuration));
        sb.append("(");

        String separator = "";
        for (Parameter<?> parameter : getParameters()) {
            sb.append(separator);

            // The return value has already been written
            if (parameter.equals(getReturnParameter())) {
                continue;
            }

            // OUT and IN OUT parameters are always written as a '?' bind variable
            else if (getOutParameters().contains(parameter)) {
                sb.append("?");
            }

            // IN parameters are rendered normally
            else {
                Field<?> value = getInValues().get(parameter);

                // Disambiguate overloaded procedure signatures
                if (SQLDialect.POSTGRES == configuration.getDialect() && isOverloaded()) {
                    value = value.cast(parameter.getType());
                }

                sb.append(internal(value).toSQLReference(configuration, inlineParameters));
            }

            separator = ", ";
        }

        sb.append(") }");
        return sb.toString();

    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T getValue(Parameter<T> parameter) {
        return (T) results.get(parameter);
    }
}
