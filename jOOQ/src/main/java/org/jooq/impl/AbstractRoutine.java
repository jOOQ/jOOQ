/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_FUNCTION;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.Utils.consumeExceptions;
import static org.jooq.impl.Utils.settings;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.AggregateFunction;
import org.jooq.AttachableInternal;
import org.jooq.BindContext;
import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Package;
import org.jooq.Parameter;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Routine;
import org.jooq.Schema;
import org.jooq.UDTField;
import org.jooq.exception.ControlFlowSignal;
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
    private static final Clause[]             CLAUSES          = { FIELD, FIELD_FUNCTION };

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
    private List<Result<Record>>              results;
    private boolean                           overloaded;
    private boolean                           hasDefaultedParameters;

    // ------------------------------------------------------------------------
    // Call-data attributes (call-specific)
    // ------------------------------------------------------------------------

    private final Map<Parameter<?>, Field<?>> inValues;
    private final Set<Parameter<?>>           inValuesDefaulted;
    private final Set<Parameter<?>>           inValuesNonDefaulted;
    private transient Field<T>                function;

    private Configuration                     configuration;
    private final Map<Parameter<?>, Object>   outValues;
    private final Map<Parameter<?>, Integer>  parameterIndexes;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    protected AbstractRoutine(String name, Schema schema) {
        this(name, schema, null, null, null, null);
    }

    protected AbstractRoutine(String name, Schema schema, Package pkg) {
        this(name, schema, pkg, null, null, null);
    }

    protected AbstractRoutine(String name, Schema schema, DataType<T> type) {
        this(name, schema, null, type, null, null);
    }

    protected <X> AbstractRoutine(String name, Schema schema, DataType<X> type, Converter<X, T> converter) {
        this(name, schema, null, type, converter, null);
    }

    protected <X> AbstractRoutine(String name, Schema schema, DataType<X> type, Binding<X, T> binding) {
        this(name, schema, null, type, null, binding);
    }


    protected <X, Y> AbstractRoutine(String name, Schema schema, DataType<X> type, Converter<Y, T> converter, Binding<X, Y> binding) {
        this(name, schema, null, type, converter, binding);
    }

    protected AbstractRoutine(String name, Schema schema, Package pkg, DataType<T> type) {
        this(name, schema, pkg, type, null, null);
    }

    protected <X> AbstractRoutine(String name, Schema schema, Package pkg, DataType<X> type, Converter<X, T> converter) {
        this(name, schema, pkg, type, converter, null);
    }

    protected <X> AbstractRoutine(String name, Schema schema, Package pkg, DataType<X> type, Binding<X, T> binding) {
        this(name, schema, pkg, type, null, binding);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <X, Y> AbstractRoutine(String name, Schema schema, Package pkg, DataType<X> type, Converter<Y, T> converter, Binding<X, Y> binding) {
        this.parameterIndexes = new HashMap<Parameter<?>, Integer>();

        this.schema = schema;
        this.pkg = pkg;
        this.name = name;
        this.allParameters = new ArrayList<Parameter<?>>();
        this.inParameters = new ArrayList<Parameter<?>>();
        this.outParameters = new ArrayList<Parameter<?>>();
        this.results = new ArrayList<Result<Record>>();
        this.inValues = new HashMap<Parameter<?>, Field<?>>();
        this.inValuesDefaulted = new HashSet<Parameter<?>>();
        this.inValuesNonDefaulted = new HashSet<Parameter<?>>();
        this.outValues = new HashMap<Parameter<?>, Object>();
        this.type = converter == null && binding == null
            ? (DataType<T>) type
            : type.asConvertedDataType(DefaultBinding.newBinding((Converter) converter, type, binding));
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

        // [#1183] [#3533] Add the field to the in-values and mark them as non-defaulted
        else {
            inValues.put(parameter, value);
            inValuesDefaulted.remove(parameter);
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
    public final void detach() {
        attach(null);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @Override
    public final int execute(Configuration c) {

        // Ensure that all depending Attachables are attached
        Configuration previous = configuration();
        try {
            attach(c);
            return execute();
        }
        finally {
            attach(previous);
        }
    }

    @Override
    public final int execute() {
        results.clear();
        outValues.clear();

        // Procedures (no return value) are always executed as CallableStatement
        if (type == null) {
            return executeCallableStatement();
        }
        else {
            switch (configuration.dialect().family()) {

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

                case H2:
                /* [pro] xx
                xxxx xxxx

                xx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxx xxx
                xxxx xxxxxxx
                xx [/pro] */
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
        outValues.put(returnParameter, result);
        return 0;
    }

    private final int executeSelect() {
        final Field<T> field = asField();
        outValues.put(returnParameter, create(configuration).select(field).fetchOne(field));
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
            using(configuration).bindContext(ctx.statement()).visit(this);
            registerOutParameters(configuration, (CallableStatement) ctx.statement());
            listener.bindEnd(ctx);

            execute0(ctx, listener);

            /* [pro] xx
            xx xxxxxxx xxx xxxxx xx xxxxxxxxx xxxxxx xxxx xxx xxxx xxxxxxxxx
            xx xxx xxxxxxxxxx xx xxxxxxxx xx xxx xxxxxx
            xx [/pro] */

            // [#2925] Jaybird currently doesn't like fetching OUT parameters and consuming ResultSets
            //         http://tracker.firebirdsql.org/browse/JDBC-350
            if (ctx.family() != FIREBIRD)
                Utils.consumeResultSets(ctx, listener, results, null);

            fetchOutParameters(ctx);
            return 0;
        }

        // [#3427] ControlFlowSignals must not be passed on to ExecuteListners
        catch (ControlFlowSignal e) {
            throw e;
        }
        catch (RuntimeException e) {
            ctx.exception(e);
            listener.exception(ctx);
            throw ctx.exception();
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

    private final void execute0(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        try {
            listener.executeStart(ctx);

            if (ctx.statement().execute())
                ctx.resultSet(ctx.statement().getResultSet());

            listener.executeEnd(ctx);
        }

        // [#3011] [#3054] Consume additional exceptions if there are any
        catch (SQLException e) {
            consumeExceptions(ctx.configuration(), ctx.statement(), e);
            throw e;
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public void accept(Context<?> ctx) {
        if (ctx instanceof RenderContext)
            toSQL0((RenderContext) ctx);
        else
            bind0((BindContext) ctx);
    }

    final void bind0(BindContext context) {
        for (Parameter<?> parameter : getParameters()) {

            // [#1183] [#3533] Skip defaulted parameters
            if (getInParameters().contains(parameter) && inValuesDefaulted.contains(parameter)) {
                continue;
            }

            int index = context.peekIndex();
            parameterIndexes.put(parameter, index);

            if (getInValues().get(parameter) != null) {
                context.visit(getInValues().get(parameter));

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

    final void toSQL0(RenderContext context) {
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

            // [#1183] [#3533] Omit defaulted parameters
            else if (inValuesDefaulted.contains(parameter)) {
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
        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxx xx xxxxxxx x
            xxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxx
        x
        xxxx
        xx [/pro] */
        {
            context.sql(" }");
        }
    }

    private final void toSQLBegin(RenderContext context) {
        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxx xx xxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxxxxx
                   xxxxxxxxxxxxxxxxxxx
        x
        xxxx
        xx [/pro] */
        {
            context.sql("{ ");
        }
    }

    private final void toSQLAssign(RenderContext context) {
        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxx xx xxxxxxx x
            xxxxxxxxxxxxxx xx xxx
        x
        xxxx
        xx [/pro] */
        {
            context.sql("? = ");
        }
    }

    private final void toSQLCall(RenderContext context) {
        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxx xx xxxxxxx x
        x
        xxxx
        xx [/pro] */
        {
            context.sql("call ");
        }

        toSQLQualifiedName(context);
    }

    private final void toSQLOutParam(RenderContext context, Parameter<?> parameter) {
        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxx xx xxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxx xx xxx
        x

        xx [/pro] */
        context.sql("?");
    }

    private final void toSQLInParam(RenderContext context, Parameter<?> parameter, Field<?> value) {
        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxx xx xxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxx xx xxx
        x

        xx [/pro] */
        context.visit(value);
    }

    private final void toSQLQualifiedName(RenderContext context) {
        Schema mappedSchema = Utils.getMappedSchema(context.configuration(), getSchema());

        if (context.qualify()) {
            if (mappedSchema != null) {
                context.visit(mappedSchema);
                context.sql(".");
            }

            /* [pro] xx
            xx xxxxxxx xx xxx xxxxxxx xxxxxxxx xxxxxx xxxx xx xx xxxxx xxxxxxxxx
            xxxx xx xxxxxxxxxxxx xx xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxx
            x

            xx [/pro] */
            if (getPackage() != null) {
                context.visit(getPackage());
                context.sql(".");
            }
        }

        context.literal(getName());
    }

    private final void fetchOutParameters(ExecuteContext ctx) throws SQLException {
        for (Parameter<?> parameter : getParameters()) {
            if (parameter.equals(getReturnParameter()) ||
                getOutParameters().contains(parameter)) {

                fetchOutParameter(ctx, parameter);
            }
        }
    }

    private final <U> void fetchOutParameter(ExecuteContext ctx, Parameter<U> parameter) throws SQLException {
        DefaultBindingGetStatementContext<U> out = new DefaultBindingGetStatementContext<U>(
            ctx.configuration(),
            (CallableStatement) ctx.statement(),
            parameterIndexes.get(parameter)
        );

        parameter.getBinding().get(out);
        outValues.put(parameter, out.value());
    }

    private final void registerOutParameters(Configuration c, CallableStatement statement) throws SQLException {

        // Register all out / inout parameters according to their position
        // Note that some RDBMS do not support binding by name very well
        for (Parameter<?> parameter : getParameters()) {
            if (parameter.equals(getReturnParameter()) ||
                getOutParameters().contains(parameter)) {

                registerOutParameter(c, statement, parameter);
            }
        }
    }

    private final <U> void registerOutParameter(Configuration c, CallableStatement statement, Parameter<U> parameter) throws SQLException {
        parameter.getBinding().register(new DefaultBindingRegisterContext<U>(c, statement, parameterIndexes.get(parameter)));
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

    @Override
    public final List<Result<Record>> getResults() {
        return results;
    }

    @SuppressWarnings("unchecked")
    protected final <Z> Z getValue(Parameter<Z> parameter) {
        return (Z) outValues.get(parameter);
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

    private final boolean hasDefaultedParameters() {
        return hasDefaultedParameters && !inValuesDefaulted.isEmpty();
    }

    private final void addParameter(Parameter<?> parameter) {
        allParameters.add(parameter);
        hasDefaultedParameters |= parameter.isDefaulted();
    }

    protected final void addInParameter(Parameter<?> parameter) {
        addParameter(parameter);
        inParameters.add(parameter);

        // IN parameters are initialised with null by default
        inValues.put(parameter, val(null, parameter.getDataType()));

        // [#1183] [#3533] defaulted parameters are marked as such
        if (parameter.isDefaulted())
            inValuesDefaulted.add(parameter);
        else
            inValuesNonDefaulted.add(parameter);
    }


    protected final void addInOutParameter(Parameter<?> parameter) {
        addInParameter(parameter);
        outParameters.add(parameter);
    }

    protected final void addOutParameter(Parameter<?> parameter) {
        addParameter(parameter);
        outParameters.add(parameter);
    }

    protected final void setReturnParameter(Parameter<T> parameter) {
        addParameter(parameter);
        returnParameter = parameter;
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
        return createParameter(name, type, false, null, null);
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
        return createParameter(name, type, isDefaulted, null, null);
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
    protected static final <T, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, Converter<T, U> converter) {
        return createParameter(name, type, isDefaulted, converter, null);
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
    protected static final <T, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, Binding<T, U> binding) {
        return createParameter(name, type, isDefaulted, null, binding);
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
    @SuppressWarnings("unchecked")
    protected static final <T, X, U> Parameter<U> createParameter(String name, DataType<T> type, boolean isDefaulted, Converter<X, U> converter, Binding<T, X> binding) {
        final Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
        final DataType<U> actualType = converter == null && binding == null
            ? (DataType<U>) type
            : type.asConvertedDataType(actualBinding);

        return new ParameterImpl<U>(name, actualType, isDefaulted, actualBinding);
    }

    /**
     * The {@link Field} representation of this {@link Routine}
     */
    private class RoutineField extends AbstractField<T> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -5730297947647252624L;

        RoutineField() {
            super(AbstractRoutine.this.getName(),
                  AbstractRoutine.this.type);
        }

        @Override
        public void accept(Context<?> ctx) {
            RenderContext local = create(ctx).renderContext();
            toSQLQualifiedName(local);

            Field<?>[] array = new Field<?>[getInParameters().size()];

            int i = 0;
            for (Parameter<?> p : getInParameters()) {

                // Disambiguate overloaded function signatures
                if (POSTGRES == ctx.dialect() && isOverloaded()) {
                    array[i] = getInValues().get(p).cast(p.getType());
                }
                else {
                    array[i] = getInValues().get(p);
                }

                i++;
            }

            Field<T> result = function(local.render(), getDataType(), array);

            // [#3592] Decrease SQL -> PL/SQL context switches with Oracle Scalar Subquery Caching
            if (TRUE.equals(settings(ctx.configuration()).isRenderScalarSubqueriesForStoredFunctions())) {
                result = DSL.select(result).asField();
            }

            ctx.visit(result);
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
