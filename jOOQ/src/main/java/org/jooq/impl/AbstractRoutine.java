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

import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.AggregateFunction;
import org.jooq.ArrayRecord;
import org.jooq.AttachableInternal;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Package;
import org.jooq.Parameter;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Routine;
import org.jooq.Schema;
import org.jooq.UDTField;
import org.jooq.UDTRecord;
import org.jooq.tools.Convert;

/**
 * A common base class for stored procedures
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public abstract class AbstractRoutine<T> extends AbstractQueryPart implements Routine<T>, AttachableInternal {

    /**
     * Generated UID
     */
    private static final long                 serialVersionUID = 6330037113167106443L;

    // ------------------------------------------------------------------------
    // Meta-data attributes (the same for every call)
    // ------------------------------------------------------------------------

    private final Schema                      schema;
    private final Package                     pkg;
    private final String                      name;
    private final List<Parameter<?>>          allParameters;
    private final List<Parameter<?>>          inParameters;
    private final List<Parameter<?>>          outParameters;
    private final DataType<T>                 type;
    private Parameter<T>                      returnParameter;
    private boolean                           overloaded;

    // ------------------------------------------------------------------------
    // Call-data attributes (call-specific)
    // ------------------------------------------------------------------------

    private final Map<Parameter<?>, Field<?>> inValues;
    private final Set<Parameter<?>>           inValuesNonDefaulted;
    private transient Field<T>                function;

    private Configuration                     configuration;
    private final Map<Parameter<?>, Object>   results;
    private final Map<Parameter<?>, Integer>  parameterIndexes;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    protected AbstractRoutine(String name, Schema schema) {
        this(name, schema, null, null);
    }

    protected AbstractRoutine(String name, Schema schema, Package pkg) {
        this(name, schema, pkg, null);
    }

    protected AbstractRoutine(String name, Schema schema, DataType<T> type) {
        this(name, schema, null, type);
    }

    protected AbstractRoutine(String name, Schema schema, Package pkg, DataType<T> type) {
        this.parameterIndexes = new HashMap<Parameter<?>, Integer>();

        this.schema = schema;
        this.pkg = pkg;
        this.name = name;
        this.allParameters = new ArrayList<Parameter<?>>();
        this.inParameters = new ArrayList<Parameter<?>>();
        this.outParameters = new ArrayList<Parameter<?>>();
        this.inValues = new HashMap<Parameter<?>, Field<?>>();
        this.inValuesNonDefaulted = new HashSet<Parameter<?>>();
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

        // [#1183] Add the field to the in-values and mark them as non-defaulted
        else {
            inValues.put(parameter, value);
            inValuesNonDefaulted.add(parameter);
        }
    }

    // ------------------------------------------------------------------------
    // Call the routine
    // ------------------------------------------------------------------------

    @Override
    public final void attach(Configuration c) {
        configuration = c;
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @Override
    public final int execute(Configuration c) {

        // Ensure that all depending Attachables are attached
        attach(c);
        return execute();
    }

    @Override
    public final int execute() {
        // Procedures (no return value) are always executed as CallableStatement
        if (type == null) {
            return executeCallableStatement();
        }
        else {
            switch (configuration.dialect()) {

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
        DSLContext create = create(configuration);
        Result<?> result = create.selectFrom(table(asField())).fetch();
        results.put(returnParameter, result);
        return 0;
    }

    private final int executeSelect() {
        final Field<T> field = asField();
        results.put(returnParameter, create(configuration).select(field).fetchOne(field));
        return 0;
    }

    private final int executeCallableStatement() {
        ExecuteContext ctx = new DefaultExecuteContext(configuration, this);
        ExecuteListener listener = new ExecuteListeners(ctx);

        try {
            Connection connection = ctx.connection();

            listener.renderStart(ctx);
            // [#1520] TODO: Should the number of bind values be checked, here?
            ctx.sql(create(configuration).render(this));
            listener.renderEnd(ctx);

            listener.prepareStart(ctx);
            ctx.statement(connection.prepareCall(ctx.sql()));
            // [#1856] TODO: Add Statement flags like timeout here
            listener.prepareEnd(ctx);

            listener.bindStart(ctx);
            create(configuration).bind(this, ctx.statement());
            registerOutParameters(configuration, (CallableStatement) ctx.statement());
            listener.bindEnd(ctx);

            listener.executeStart(ctx);
            ctx.statement().execute();
            listener.executeEnd(ctx);

            fetchOutParameters(ctx);
            return 0;
        }
        catch (SQLException e) {
            ctx.sqlException(e);
            listener.exception(ctx);
            throw ctx.exception();
        }
        finally {
            Utils.safeClose(listener, ctx);
        }
    }

    @Override
    public final void bind(BindContext context) {
        for (Parameter<?> parameter : getParameters()) {

            // [#1183] Skip defaulted parameters
            if (getInParameters().contains(parameter) && !inValuesNonDefaulted.contains(parameter)) {
                continue;
            }

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
        toSQLBegin(context);

        if (getReturnParameter() != null) {
            toSQLAssign(context);
        }

        toSQLCall(context);
        context.sql("(");

        String separator = "";
        for (Parameter<?> parameter : getParameters()) {

            // The return value has already been written
            if (parameter.equals(getReturnParameter())) {
                continue;
            }

            // OUT and IN OUT parameters are always written as a '?' bind variable
            else if (getOutParameters().contains(parameter)) {
                context.sql(separator);
                toSQLOutParam(context, parameter);
            }

            // [#1183] Omit defaulted parameters
            else if (!inValuesNonDefaulted.contains(parameter)) {
                continue;
            }

            // IN parameters are rendered normally
            else {
                Field<?> value = getInValues().get(parameter);

                // Disambiguate overloaded procedure signatures
                if (POSTGRES == context.configuration().dialect() && isOverloaded()) {
                    value = value.cast(parameter.getType());
                }

                context.sql(separator);
                toSQLInParam(context, parameter, value);
            }

            separator = ", ";
        }

        context.sql(")");
        toSQLEnd(context);
    }

    private final void toSQLEnd(RenderContext context) {
        switch (context.configuration().dialect()) {
            case ORACLE:
                context.sql(";")
                       .formatIndentEnd()
                       .formatSeparator()
                       .keyword("end;");
                break;

            default:
                context.sql(" }");
                break;
        }
    }

    private final void toSQLBegin(RenderContext context) {
        switch (context.configuration().dialect()) {
            case ORACLE:
                context.keyword("begin")
                       .formatIndentStart()
                       .formatSeparator();
                break;

            default:
                context.sql("{ ");
                break;
        }
    }

    private final void toSQLAssign(RenderContext context) {
        switch (context.configuration().dialect()) {
            case ORACLE:
                context.sql("? := ");
                break;

            default:
                context.sql("? = ");
                break;
        }
    }

    private final void toSQLCall(RenderContext context) {
        switch (context.configuration().dialect()) {
            case ORACLE:
                break;

            default:
                context.sql("call ");
                break;
        }

        toSQLQualifiedName(context);
    }

    private final void toSQLOutParam(RenderContext context, Parameter<?> parameter) {
        switch (context.configuration().dialect()) {
            case ORACLE:
                context.sql(parameter);
                context.sql(" => ");
                break;

            default:
                break;
        }

        context.sql("?");
    }

    private final void toSQLInParam(RenderContext context, Parameter<?> parameter, Field<?> value) {
        switch (context.configuration().dialect()) {
            case ORACLE:
                context.sql(parameter);
                context.sql(" => ");
                break;

            default:
                break;
        }

        context.sql(value);
    }

    private final void toSQLQualifiedName(RenderContext context) {
        Schema mappedSchema = Utils.getMappedSchema(context.configuration(), getSchema());

        if (context.qualify()) {
            if (mappedSchema != null) {
                context.sql(mappedSchema);
                context.sql(".");
            }

            // [#2569] In SQL Server, routines always have to be fully qualified
            else if (getSchema() != null && context.configuration().dialect().family() == SQLSERVER) {
                context.sql(getSchema());
                context.sql(".");
            }

            if (getPackage() != null) {
                context.sql(getPackage());
                context.sql(".");
            }
        }

        context.literal(getName());
    }

    private final void fetchOutParameters(ExecuteContext ctx) throws SQLException {
        for (Parameter<?> parameter : getParameters()) {
            if (parameter.equals(getReturnParameter()) ||
                getOutParameters().contains(parameter)) {

                int index = parameterIndexes.get(parameter);
                results.put(parameter, Utils.getFromStatement(ctx, parameter.getType(), index));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private final void registerOutParameters(Configuration c, CallableStatement statement) throws SQLException {

        // Register all out / inout parameters according to their position
        // Note that some RDBMS do not support binding by name very well
        for (Parameter<?> parameter : getParameters()) {
            if (parameter.equals(getReturnParameter()) ||
                getOutParameters().contains(parameter)) {

                int index = parameterIndexes.get(parameter);
                int sqlType = parameter.getDataType().getDataType(c).getSQLType();

                switch (c.dialect()) {

                    // For some user defined types Oracle needs to bind
                    // also the type name
                    case ORACLE: {
                        if (sqlType == Types.STRUCT) {
                            UDTRecord<?> record = Utils.newRecord((Class<? extends UDTRecord<?>>) parameter.getType());
                            statement.registerOutParameter(index, Types.STRUCT, record.getSQLTypeName());
                        }

                        else if (sqlType == Types.ARRAY) {
                            ArrayRecord<?> record = Utils.newArrayRecord(
                                (Class<? extends ArrayRecord<?>>) parameter.getType(), c);
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
    public final Schema getSchema() {
        return schema;
    }

    @Override
    public final Package getPackage() {
        return pkg;
    }

    @Override
    public final String getName() {
        return name;
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

        // [#1183] non-defaulted parameters are marked as such
        if (!parameter.isDefaulted()) {
            inValuesNonDefaulted.add(parameter);
        }
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
            function = new RoutineField();
        }

        return function;
    }

    public final Field<T> asField(String alias) {
        return asField().as(alias);
    }

    public final AggregateFunction<T> asAggregateFunction() {
        Field<?>[] array = new Field<?>[getInParameters().size()];

        int i = 0;
        for (Parameter<?> p : getInParameters()) {
            array[i] = getInValues().get(p);
            i++;
        }

        // [#2393] Fully qualify custom aggregate functions.
        // TODO: Merge this code into RoutineField!
        List<String> names = new ArrayList<String>();
        if (schema != null) {
            names.add(schema.getName());
        }
        if (pkg != null) {
            names.add(pkg.getName());
        }
        names.add(name);
        return (AggregateFunction<T>) function(DSL.name(names.toArray(new String[names.size()])), type, array);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <T> Parameter<T> createParameter(String name, DataType<T> type) {
        return createParameter(name, type, false);
    }

    /**
     * Subclasses may call this method to create {@link UDTField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @param isDefaulted Whether the parameter is defaulted (see
     *            {@link Parameter#isDefaulted()}
     */
    protected static final <T> Parameter<T> createParameter(String name, DataType<T> type, boolean isDefaulted) {
        return new ParameterImpl<T>(name, type, isDefaulted);
    }

    /**
     * The {@link Field} representation of this {@link Routine}
     */
    private class RoutineField extends AbstractFunction<T> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -5730297947647252624L;

        RoutineField() {
            super(AbstractRoutine.this.getName(),
                  AbstractRoutine.this.type);
        }

        @Override
        final Field<T> getFunction0(Configuration c) {
            RenderContext local = create(c).renderContext();
            toSQLQualifiedName(local);

            Field<?>[] array = new Field<?>[getInParameters().size()];

            int i = 0;
            for (Parameter<?> p : getInParameters()) {

                // Disambiguate overloaded function signatures
                if (POSTGRES == c.dialect() && isOverloaded()) {
                    array[i] = getInValues().get(p).cast(p.getType());
                }
                else {
                    array[i] = getInValues().get(p);
                }

                i++;
            }

            return function(local.render(), getDataType(), array);
        }
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return name.hashCode();
    }
}
