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

import static org.jooq.impl.Factory.function;
import static org.jooq.impl.Factory.table;
import static org.jooq.impl.Factory.val;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Package;
import org.jooq.Parameter;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Routine;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.UDTField;
import org.jooq.UDTRecord;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;

/**
 * A common base class for stored procedures
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public abstract class AbstractRoutine<T> extends AbstractSchemaProviderQueryPart implements Routine<T> {

    /**
     * Generated UID
     */
    private static final long                 serialVersionUID = 6330037113167106443L;
    private static final JooqLogger           log              = JooqLogger.getLogger(AbstractRoutine.class);

    private final Package                     pkg;
    private final List<Parameter<?>>          allParameters;
    private final List<Parameter<?>>          inParameters;
    private final List<Parameter<?>>          outParameters;
    private final Map<Parameter<?>, Field<?>> inValues;
    private final DataType<T>                 type;

    private final AttachableImpl              attachable;
    private final Map<Parameter<?>, Object>   results;
    private final Map<Parameter<?>, Integer>  parameterIndexes;

    private Parameter<T>                      returnParameter;
    private boolean                           overloaded;
    private transient Field<T>                function;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    protected AbstractRoutine(SQLDialect dialect, String name, Schema schema) {
        this(dialect, name, schema, null, null);
    }

    protected AbstractRoutine(SQLDialect dialect, String name, Schema schema, Package pkg) {
        this(dialect, name, schema, pkg, null);
    }

    protected AbstractRoutine(SQLDialect dialect, String name, Schema schema, DataType<T> type) {
        this(dialect, name, schema, null, type);
    }

    protected AbstractRoutine(SQLDialect dialect, String name, Schema schema, Package pkg, DataType<T> type) {
        super(name, schema);

        this.attachable = new AttachableImpl(this, Factory.getNewFactory(dialect));
        this.parameterIndexes = new HashMap<Parameter<?>, Integer>();

        this.pkg = pkg;
        this.allParameters = new ArrayList<Parameter<?>>();
        this.inParameters = new ArrayList<Parameter<?>>();
        this.outParameters = new ArrayList<Parameter<?>>();
        this.inValues = new HashMap<Parameter<?>, Field<?>>();
        this.results = new HashMap<Parameter<?>, Object>();
        this.type = type;
    }

    // ------------------------------------------------------------------------
    // Initialise a routine call
    // ------------------------------------------------------------------------

    protected final void setNumber(Parameter<? extends Number> parameter, Number value) {
        setValue(parameter, Convert.convert(value, parameter.getType()));
    }

    protected final void setNumber(Parameter<? extends Number> parameter, Field<? extends Number> value) {
        setField(parameter, value);
    }

    protected final void setValue(Parameter<?> parameter, Object value) {
        setField(parameter, val(value, parameter.getDataType()));
    }

    /*
     * #326 - Avoid overloading setValue()
     */
    protected final void setField(Parameter<?> parameter, Field<?> value) {
        // Be sure null is correctly represented as a null field
        if (value == null) {
            setField(parameter, val(null, parameter.getDataType()));
        }

        // Add the field to the in-values
        else {
            inValues.put(parameter, value);
        }
    }

    // ------------------------------------------------------------------------
    // Call the routine
    // ------------------------------------------------------------------------

    @Override
    public final void attach(Configuration configuration) {
        attachable.attach(configuration);
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        List<Attachable> result = new ArrayList<Attachable>();

        result.addAll(getAttachables(pkg));
        result.addAll(getAttachables(allParameters));
        result.addAll(getAttachables(inValues.keySet()));
        result.addAll(getAttachables(inValues.values()));

        return result;
    }

    @Override
    public final int execute(Configuration configuration) {

        // Ensure that all depending Attachables are attached
        attach(configuration);
        return execute();
    }

    @Override
    public final int execute() {
        // Procedures (no return value) are always executed as CallableStatement
        if (type == null) {
            return executeCallableStatement();
        }
        else {
            switch (attachable.getConfiguration().getDialect()) {

                // [#852] Some RDBMS don't allow for using JDBC procedure escape
                // syntax for functions. Select functions from DUAL instead
                case HSQLDB:

                	// [#692] HSQLDB cannot SELECT f() FROM [...] when f()
                	// returns a cursor. Instead, SELECT * FROM table(f()) works
                    if (SQLDataType.RESULT.equals(type.getSQLDataType())) {
                        return executeSelectFrom();
                    }

                    // Fall through
                    else {
                    }

                case DB2:
                case H2:

                // Sybase CallableStatement.wasNull() doesn't work :-(
                case SYBASE:
                    return executeSelect();

                // [#773] If JDBC escape syntax is available for functions, use
                // it to prevent transactional issues when functions issue
                // DML statements
                default:
                    return executeCallableStatement();
            }
        }
    }

    private final int executeSelectFrom() {
        Factory create = create(attachable);
        Result<?> result = create.selectFrom(table(asField())).fetch();
        results.put(returnParameter, result);
        return 0;
    }

    private final int executeSelect() {
        final Field<T> field = asField();
        results.put(returnParameter, create(attachable).select(field).fetchOne(field));
        return 0;
    }

    private final int executeCallableStatement() {
        StopWatch watch = new StopWatch();

        Configuration configuration = attachable.getConfiguration();
        CallableStatement statement = null;
        String sql = null;
        try {
            Connection connection = configuration.getConnection();

            sql = create(configuration).render(this);
            watch.splitTrace("SQL rendered");

            if (log.isDebugEnabled())
                log.debug("Executing routine", create(configuration).renderInlined(this));
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

            watch.splitTrace("Routine called");

            fetchOutParameters(configuration, statement);
            watch.splitTrace("OUT params fetched");

            return 0;
        }
        catch (SQLException exc) {
            throw translate("AbstractRoutine.executeCallableStatement", sql, exc);
        }
        finally {
            Util.safeClose(statement);
            watch.splitDebug("Routine executed");
        }
    }

    @Override
    public final void bind(BindContext context) {
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

    private final void toSQLQualifiedName(RenderContext context) {
        if (getMappedSchema(context, getSchema()) != null) {
            context.sql(getMappedSchema(context, getSchema()));
            context.sql(".");
        }

        if (getPackage() != null) {
            context.sql(getPackage());
            context.sql(".");
        }

        context.literal(getName());
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
                            UDTRecord<?> record = Util.newRecord((Class<? extends UDTRecord<?>>) parameter.getType());
                            statement.registerOutParameter(index, Types.STRUCT, record.getSQLTypeName());
                        }

                        else if (sqlType == Types.ARRAY) {
                            ArrayRecord<?> record = Util.newArrayRecord(
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

    // ------------------------------------------------------------------------
    // Fetch routine results
    // ------------------------------------------------------------------------

    @Override
    public final T getReturnValue() {
        if (returnParameter != null) {
            return getValue(returnParameter);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected final <Z> Z getValue(Parameter<Z> parameter) {
        return (Z) results.get(parameter);
    }

    protected final Map<Parameter<?>, Field<?>> getInValues() {
        return inValues;
    }

    // ------------------------------------------------------------------------
    // Access to routine configuration objects
    // ------------------------------------------------------------------------

    @Override
    public final List<Parameter<?>> getOutParameters() {
        return Collections.unmodifiableList(outParameters);
    }

    @Override
    public final List<Parameter<?>> getInParameters() {
        return Collections.unmodifiableList(inParameters);
    }

    @Override
    public final List<Parameter<?>> getParameters() {
        return Collections.unmodifiableList(allParameters);
    }

    @Override
    public final Package getPackage() {
        return pkg;
    }

    protected final Parameter<?> getReturnParameter() {
        return returnParameter;
    }

    protected final void setOverloaded(boolean overloaded) {
        this.overloaded = overloaded;
    }

    protected final boolean isOverloaded() {
        return overloaded;
    }

    protected final void addInParameter(Parameter<?> parameter) {
        inParameters.add(parameter);
        allParameters.add(parameter);

        // IN parameters are initialised with null by default
        inValues.put(parameter, val(null, parameter.getDataType()));
    }

    protected final void addInOutParameter(Parameter<?> parameter) {
        addInParameter(parameter);
        outParameters.add(parameter);
    }

    protected final void addOutParameter(Parameter<?> parameter) {
        outParameters.add(parameter);
        allParameters.add(parameter);
    }

    protected final void setReturnParameter(Parameter<T> parameter) {
        returnParameter = parameter;
        allParameters.add(parameter);
    }

    public final Field<T> asField() {
        if (function == null) {
            Field<?>[] array = new Field<?>[getInParameters().size()];

            int i = 0;
            for (Parameter<?> p : getInParameters()) {

                // Disambiguate overloaded function signatures
                if (SQLDialect.POSTGRES == attachable.getDialect() && isOverloaded()) {
                    array[i] = getInValues().get(p).cast(p.getType());
                }
                else {
                    array[i] = getInValues().get(p);
                }

                i++;
            }

            RenderContext local = create(attachable).renderContext();
            toSQLQualifiedName(local);

            function = function(local.render(), type, array);
        }

        return function;
    }

    public final Field<T> asField(String alias) {
        return asField().as(alias);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <T> Parameter<T> createParameter(String name, DataType<T> type) {
        return new ParameterImpl<T>(name, type);
    }
}
