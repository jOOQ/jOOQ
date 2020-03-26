/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.nCopies;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.getAnnotatedGetter;
import static org.jooq.impl.Tools.getAnnotatedMembers;
import static org.jooq.impl.Tools.getAnnotatedSetters;
import static org.jooq.impl.Tools.getMatchingGetter;
import static org.jooq.impl.Tools.getMatchingMembers;
import static org.jooq.impl.Tools.getMatchingSetters;
import static org.jooq.impl.Tools.getPropertyName;
import static org.jooq.impl.Tools.hasColumnAnnotations;
import static org.jooq.tools.reflect.Reflect.accessible;

import java.beans.ConstructorProperties;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.conf.Settings;
import org.jooq.exception.MappingException;
import org.jooq.tools.Convert;
import org.jooq.tools.StringUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;

/**
 * This is the default implementation for <code>RecordMapper</code> types.
 * <p>
 * The mapping algorithm is this:
 * <p>
 * <h5>If <code>&lt;E&gt;</code> is an array type:</h5>
 * <p>
 * The resulting array is of the nature described in {@link Record#intoArray()}.
 * Arrays more specific than <code>Object[]</code> can be specified as well,
 * e.g. <code>String[]</code>. If conversion to the element type of more
 * specific arrays fails, a {@link MappingException} is thrown, wrapping
 * conversion exceptions.
 * <p>
 * <h5>If <code>&lt;E&gt;</code> is a field "value type" and <code>&lt;R&gt;</code>
 * has exactly one column:</h5>
 * <p>
 * Any Java type available from {@link SQLDataType} qualifies as a well-known
 * "value type" that can be converted from a single-field {@link Record1}. The
 * following rules apply:
 * <p>
 * <ul>
 * <li>If <code>&lt;E&gt;</code> is a reference type like {@link String},
 * {@link Integer}, {@link Long}, {@link Timestamp}, etc., then converting from
 * <code>&lt;R&gt;</code> to <code>&lt;E&gt;</code> is mere convenience for calling
 * {@link Record#getValue(int, Class)} with <code>fieldIndex = 0</code></li>
 * <li>If <code>&lt;E&gt;</code> is a primitive type, the mapping result will be
 * the corresponding wrapper type. <code>null</code> will map to the primitive
 * type's initialisation value, e.g. <code>0</code> for <code>int</code>,
 * <code>0.0</code> for <code>double</code>, <code>false</code> for
 * <code>boolean</code>.</li>
 * </ul>
 * <h5>If a default constructor is available and any JPA {@link Column}
 * annotations are found on the provided <code>&lt;E&gt;</code>, only those are
 * used:</h5>
 * <p>
 * <ul>
 * <li>If <code>&lt;E&gt;</code> contains single-argument instance methods of any
 * visibility annotated with <code>Column</code>, those methods are invoked</li>
 * <li>If <code>&lt;E&gt;</code> contains no-argument instance methods of any
 * visibility starting with <code>getXXX</code> or <code>isXXX</code>, annotated
 * with <code>Column</code>, then matching <code>setXXX()</code> instance
 * methods of any visibility are invoked</li>
 * <li>If <code>&lt;E&gt;</code> contains instance member fields of any visibility
 * annotated with <code>Column</code>, those members are set</li>
 * </ul>
 * Additional rules:
 * <ul>
 * <li>The same annotation can be re-used for several methods/members</li>
 * <li>{@link Column#name()} must match {@link Field#getName()}. All other
 * annotation attributes are ignored</li>
 * <li>Static methods / member fields are ignored</li>
 * <li>Final member fields are ignored</li>
 * </ul>
 * <p>
 * <h5>If a default constructor is available and if there are no JPA
 * <code>Column</code> annotations, or jOOQ can't find the
 * <code>javax.persistence</code> API on the classpath, jOOQ will map
 * <code>Record</code> values by naming convention:</h5>
 * <p>
 * If {@link Field#getName()} is <code>MY_field</code> (case-sensitive!), then
 * this field's value will be set on all of these (regardless of visibility):
 * <ul>
 * <li>Single-argument instance method <code>MY_field(...)</code></li>
 * <li>Single-argument instance method <code>myField(...)</code></li>
 * <li>Single-argument instance method <code>setMY_field(...)</code></li>
 * <li>Single-argument instance method <code>setMyField(...)</code></li>
 * <li>Non-final instance member field <code>MY_field</code></li>
 * <li>Non-final instance member field <code>myField</code></li>
 * </ul>
 * <p>
 * If {@link Field#getName()} is <code>MY_field.MY_nested_field</code>
 * (case-sensitive!), then this field's value will be considered a nested value
 * <code>MY_nested_field</code>, which is set on a nested POJO that is passed to
 * all of these (regardless of visibility):
 * <ul>
 * <li>Single-argument instance method <code>MY_field(...)</code></li>
 * <li>Single-argument instance method <code>myField(...)</code></li>
 * <li>Single-argument instance method <code>setMY_field(...)</code></li>
 * <li>Single-argument instance method <code>setMyField(...)</code></li>
 * <li>Non-final instance member field <code>MY_field</code></li>
 * <li>Non-final instance member field <code>myField</code></li>
 * </ul>
 * <p>
 * <h5>If no default constructor is available, but at least one constructor
 * annotated with <code>ConstructorProperties</code> is available, that one is
 * used</h5>
 * <p>
 * <ul>
 * <li>The standard JavaBeans {@link ConstructorProperties} annotation is used
 * to match constructor arguments against POJO members or getters.</li>
 * <li>If the property names provided to the constructor match the record's
 * columns via the aforementioned naming conventions, that information is used.
 * </li>
 * <li>If those POJO members or getters have JPA annotations, those will be used
 * according to the aforementioned rules, in order to map <code>Record</code>
 * values onto constructor arguments.</li>
 * <li>If those POJO members or getters don't have JPA annotations, the
 * aforementioned naming conventions will be used, in order to map
 * <code>Record</code> values onto constructor arguments.</li>
 * <li>When several annotated constructors are found, the first one is chosen,
 * randomly.</li>
 * <li>When invoking the annotated constructor, values are converted onto
 * constructor argument types</li>
 * </ul>
 * <p>
 * <h5>If no default constructor is available, but at least one "matching"
 * constructor is available, that one is used</h5>
 * <p>
 * <ul>
 * <li>A "matching" constructor is one with exactly as many arguments as this
 * record holds fields</li>
 * <li>When several "matching" constructors are found, the first one is chosen
 * (as reported by {@link Class#getDeclaredConstructors()}). This choice is
 * non-deterministic as neither the JVM nor the JDK guarantee any order of
 * methods or constructors.</li>
 * <li>When {@link Settings#isMapConstructorParameterNames()} is turned on, and
 * parameter names are available through reflection on
 * {@link Executable#getParameters()}, then values are mapped by name, otherwise
 * by index. (see #4627)</li>
 * <li>When invoking the "matching" constructor, values are converted onto
 * constructor argument types</li>
 * </ul>
 * <p>
 * <h5>If no default constructor is available, no "matching" constructor is
 * available, but {@link Settings#isMapConstructorParameterNames()} is turned
 * on, and parameter names are available through reflection on
 * {@link Executable#getParameters()}, the first constructor is used</h5>
 * <p>
 * <ul>
 * <li>The first constructor is chosen (as reported by
 * {@link Class#getDeclaredConstructors()}). This choice is non-deterministic as
 * neither the JVM nor the JDK guarantee any order of methods or
 * constructors.</li>
 * <li>When invoking that constructor, values are converted onto constructor
 * argument types</li>
 * </ul>
 * <p>
 * <h5>If the supplied type is an interface or an abstract class</h5>
 * <p>
 * Abstract types are instantiated using Java reflection {@link Proxy}
 * mechanisms. The returned proxy will wrap a {@link HashMap} containing
 * properties mapped by getters and setters of the supplied type. Methods (even
 * JPA-annotated ones) other than standard POJO getters and setters are not
 * supported. Details can be seen in {@link Reflect#as(Class)}.
 * <p>
 * <h5>Other restrictions</h5>
 * <p>
 * <ul>
 * <li><code>&lt;E&gt;</code> must provide a default or a "matching" constructor.
 * Non-public default constructors are made accessible using
 * {@link Constructor#setAccessible(boolean)}</li>
 * <li>primitive types are supported. If a value is <code>null</code>, this will
 * result in setting the primitive type's default value (zero for numbers, or
 * <code>false</code> for booleans). Hence, there is no way of distinguishing
 * <code>null</code> and <code>0</code> in that case.</li>
 * </ul>
 * <p>
 * This mapper is returned by the {@link DefaultRecordMapperProvider}. You can
 * override this behaviour by specifying your own custom
 * {@link RecordMapperProvider} in {@link Configuration#recordMapperProvider()}
 *
 * @author Lukas Eder
 * @see RecordMapper
 * @see DefaultRecordMapperProvider
 * @see Configuration
 */
@SuppressWarnings("unchecked")
public class DefaultRecordMapper<R extends Record, E> implements RecordMapper<R, E> {

    /**
     * The record type.
     */
    private final Field<?>[]         fields;
    private final RecordType<R>      rowType;

    /**
     * The target type.
     */
    private final Class<? extends E> type;

    /**
     * The configuration in whose context this {@link RecordMapper} operates.
     * <p>
     * This configuration can be used for caching reflection information.
     */
    private final Configuration      configuration;

    /**
     * A delegate mapper created from type information in <code>type</code>.
     */
    private RecordMapper<R, E>       delegate;

    /**
     * A set of field name prefixes that may defined the behaviour of nested
     * record mappers.
     */
    private transient Set<String>    prefixes;

    /**
     * Create a new <code>DefaultRecordMapper</code>.
     * <p>
     * This constructor uses a new {@link DefaultConfiguration} internally to
     * cache various reflection methods. For better performance, use
     * {@link #DefaultRecordMapper(RecordType, Class, Configuration)} instead.
     */
    public DefaultRecordMapper(RecordType<R> rowType, Class<? extends E> type) {
        this(rowType, type, null, null);
    }

    /**
     * Create a new <code>DefaultRecordMapper</code>.
     */
    public DefaultRecordMapper(RecordType<R> rowType, Class<? extends E> type, Configuration configuration) {
        this(rowType, type, null, configuration);
    }

    DefaultRecordMapper(RecordType<R> rowType, Class<? extends E> type, E instance, Configuration configuration) {
        this.rowType = rowType;
        this.fields = rowType.fields();
        this.type = type;
        this.configuration = configuration != null ? configuration : new DefaultConfiguration();

        init(instance);
    }

    private final void init(E instance) {

        // Arrays can be mapped easily
        if (type.isArray()) {
            delegate = new ArrayMapper(instance);
            return;
        }


        if (Stream.class.isAssignableFrom(type)) {
            RecordMapper<R, Object[]> local = configuration.recordMapperProvider().provide(rowType, Object[].class);
            delegate = r -> (E) Stream.of(local.map(r));
            return;
        }


        // [#3212] [#5154] "Value types" can be mapped from single-field Record1
        //                 types for convenience
        if (type.isPrimitive()
                || DefaultDataType.types().contains(type)
                || Enum.class.isAssignableFrom(type)) {
            delegate = new ValueTypeMapper();
            return;
        }

        // [#1470] Return a proxy if the supplied type is an interface
        if (Modifier.isAbstract(type.getModifiers())) {
            delegate = new ProxyMapper();
            return;
        }

        // [#2989] [#2836] Records are mapped
        if (AbstractRecord.class.isAssignableFrom(type)) {
            delegate = (RecordMapper<R, E>) new RecordToRecordMapper();
            return;
        }

        // [#1340] Allow for using non-public default constructors
        try {
            delegate = new MutablePOJOMapper(new ConstructorCall<>(accessible(type.getDeclaredConstructor())), instance);
            return;
        }
        catch (NoSuchMethodException ignore) {}

        // [#1336] If no default constructor is present, check if there is a
        // "matching" constructor with the same number of fields as this record
        Constructor<E>[] constructors = (Constructor<E>[]) type.getDeclaredConstructors();

        // [#6868] Prefer public constructors
        Arrays.sort(constructors, new Comparator<Constructor<E>>() {
            @Override
            public int compare(Constructor<E> c1, Constructor<E> c2) {
                return (c2.getModifiers() & Modifier.PUBLIC) - (c1.getModifiers() & Modifier.PUBLIC);
            }
        });

        // [#1837] If any java.beans.ConstructorProperties annotations are
        // present use those rather than matching constructors by the number of
        // arguments
        for (Constructor<E> constructor : constructors) {
            ConstructorProperties properties = constructor.getAnnotation(ConstructorProperties.class);

            if (properties != null) {
                delegate = new ImmutablePOJOMapperWithParameterNames(constructor, Arrays.asList(properties.value()));
                return;
            }
        }

        // [#7324] Map immutable Kotlin classes by parameter names if kotlin-reflect is on the classpath
        if (Tools.isKotlinAvailable() && !FALSE.equals(configuration.settings().isMapConstructorParameterNamesInKotlin())) {
            try {
                Reflect jvmClassMappingKt = Tools.ktJvmClassMapping();
                Reflect kClasses = Tools.ktKClasses();
                Reflect kTypeParameter = Tools.ktKTypeParameter();

                Object klass = jvmClassMappingKt.call("getKotlinClass", type).get();
                Reflect primaryConstructor = kClasses.call("getPrimaryConstructor", klass);

                // It is a Kotlin class
                if (primaryConstructor.get() != null) {
                    List<?> parameters = primaryConstructor.call("getParameters").get();
                    Class<?> klassType = Tools.ktKClass().type();
                    Method getJavaClass = jvmClassMappingKt.type().getMethod("getJavaClass", klassType);

                    List<String> parameterNames = new ArrayList<>(parameters.size());
                    Class<?>[] parameterTypes = new Class[parameters.size()];

                    for (int i = 0; i < parameterTypes.length; i++) {
                        Reflect parameter = Reflect.on(parameters.get(i));
                        Object typeClassifier = parameter.call("getType").call("getClassifier").get();
                        String name = parameter.call("getName").<String>get();

                        // [#8578] If the constructor parameter is a KTypeParameter, we need an additional step to
                        //         extract the first upper bounds' classifier, which (hopefully) is a KClass
                        parameterTypes[i] = (Class<?>) getJavaClass.invoke(
                            jvmClassMappingKt.get(),
                            (kTypeParameter.type().isInstance(typeClassifier)
                                ? Reflect.on(typeClassifier).call("getUpperBounds").call("get", 0).call("getClassifier").get()
                                : typeClassifier)
                        );

                        // [#8004] Clean up kotlin field name for boolean types
                        String typeName = parameterTypes[i].getName();

                        if (name.startsWith("is") &&
                            (boolean.class.getName().equalsIgnoreCase(typeName) || Boolean.class.getName().equals(typeName)))
                            name = getPropertyName(name);

                        parameterNames.add(name);
                    }

                    Constructor<E> javaConstructor = (Constructor<E>) this.type.getConstructor(parameterTypes);
                    delegate = new ImmutablePOJOMapperWithParameterNames(javaConstructor, parameterNames);
                    return;
                }
            }
            catch (ReflectException ignore) {}
            catch (NoSuchMethodException ignore) {}
            catch (IllegalAccessException ignore) {}
            catch (InvocationTargetException ignore) {}
        }


        boolean mapConstructorParameterNames = TRUE.equals(configuration.settings().isMapConstructorParameterNames());


        // [#1837] Without ConstructorProperties, match constructors by matching
        //         argument length
        // [#6598] Try prefixes first (for nested POJOs), and then field.length
        //         (for a flat POJO)
        for (boolean supportsNesting : new boolean[] { true, false }) {
            for (Constructor<E> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();

                // Match the first constructor by parameter length
                if (parameterTypes.length == (supportsNesting ? prefixes().size() : fields.length)) {


                    // [#4627] use parameter names from byte code if available
                    if (mapConstructorParameterNames) {
                        Parameter[] parameters = constructor.getParameters();

                        if (parameters != null && parameters.length > 0)
                            delegate = new ImmutablePOJOMapperWithParameterNames(constructor, collectParameterNames(parameters));
                    }


                    if (delegate == null)
                        delegate = new ImmutablePOJOMapper(constructor, parameterTypes, supportsNesting);

                    return;
                }
            }
        }


        // [#4627] if there is no exact match in terms of the number of parameters,
        // but using parameter annotations is allowed and those are in fact present,
        // use the first available constructor (thus the choice is undeterministic)
        if (mapConstructorParameterNames) {
            Constructor<E> constructor = constructors[0];
            Parameter[] parameters = constructor.getParameters();

            if (parameters != null && parameters.length > 0) {
                delegate = new ImmutablePOJOMapperWithParameterNames(constructor, collectParameterNames(parameters));
                return;
            }
        }


        throw new MappingException("No matching constructor found on type " + type + " for row type " + rowType);
    }


    private List<String> collectParameterNames(Parameter[] parameters) {
        return Arrays.stream(parameters).map(Parameter::getName).collect(Collectors.toList());
    }


    @Override
    public final E map(R record) {
        if (record == null) {
            return null;
        }

        try {
            return attach(delegate.map(record), record);
        }

        // Pass MappingExceptions on to client code
        catch (MappingException e) {
            throw e;
        }

        // All other reflection exceptions are intercepted
        catch (Exception e) {
            throw new MappingException("An error ocurred when mapping record to " + type, e);
        }
    }

    /**
     * Convert a record into an array of a given type.
     * <p>
     * The supplied type is usually <code>Object[]</code>, but in some cases, it
     * may make sense to supply <code>String[]</code>, <code>Integer[]</code>
     * etc.
     */
    private class ArrayMapper implements RecordMapper<R, E> {

        private final E instance;

        ArrayMapper(E instance) {
            this.instance = instance;
        }

        @Override
        public final E map(R record) {
            int size = record.size();
            Class<?> componentType = type.getComponentType();
            Object[] result = (Object[]) (instance != null
                ? instance
                : Array.newInstance(componentType, size));

            // Just as in Collection.toArray(Object[]), return a new array in case
            // sizes don't match
            if (size > result.length)
                result = (Object[]) Array.newInstance(componentType, size);

            for (int i = 0; i < size; i++)
                result[i] = Convert.convert(record.get(i), componentType);

            return (E) result;
        }
    }

    private class ValueTypeMapper implements RecordMapper<R, E> {

        @Override
        public final E map(R record) {
            int size = record.size();
            if (size != 1)
                throw new MappingException("Cannot map multi-column record of degree " + size + " to value type " + type);

            return record.get(0, type);
        }
    }

    /**
     * Convert a record into an hash map proxy of a given type.
     * <p>
     * This is done for types that are not instanciable
     */
    private class ProxyMapper implements RecordMapper<R, E> {


        private Constructor<Lookup>     constructor;

        private final MutablePOJOMapper pojomapper;

        ProxyMapper() {
            this.pojomapper = new MutablePOJOMapper(new Callable<E>() {
                @Override
                public E call() throws Exception {
                    return proxy();
                }
            }, null);
        }

        @Override
        public final E map(R record) {
            return pojomapper.map(record);
        }

        private E proxy() {
            final Object[] result = new Object[1];
            final Map<String, Object> map = new HashMap<>();
            final InvocationHandler handler = new InvocationHandler() {

                @SuppressWarnings("null")
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) {
                    String name = method.getName();

                    int length = (args == null ? 0 : args.length);

                    if (length == 0 && name.startsWith("get"))
                        return map.get(name.substring(3));
                    else if (length == 0 && name.startsWith("is"))
                        return map.get(name.substring(2));
                    else if (length == 1 && name.startsWith("set"))
                        map.put(name.substring(3), args[0]);


                    // [#5442] Default methods should be invoked to run client implementation
                    else if (method.isDefault())
                        try {
                            if (constructor == null)
                                constructor = accessible(Lookup.class.getDeclaredConstructor(Class.class, int.class));

                            Class<?> declaringClass = method.getDeclaringClass();
                            return constructor
                                .newInstance(declaringClass, Lookup.PRIVATE)
                                .unreflectSpecial(method, declaringClass)
                                .bindTo(result[0])
                                .invokeWithArguments(args);
                        }
                        catch (Throwable e) {
                            throw new MappingException("Cannot invoke default method", e);
                        }


                    return null;
                }
            };

            result[0] = Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, handler);
            return (E) result[0];
        }
    }

    /**
     * Convert a record into another record type.
     */
    private class RecordToRecordMapper implements RecordMapper<R, AbstractRecord> {

        @Override
        public final AbstractRecord map(R record) {
            try {
                if (record instanceof AbstractRecord) {
                    return ((AbstractRecord) record).intoRecord((Class<AbstractRecord>) type);
                }

                throw new MappingException("Cannot map record " + record + " to type " + type);
            }
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record to " + type, e);
            }
        }
    }

    private static final class ConstructorCall<E> implements Callable<E> {
        private final Constructor<? extends E> constructor;

        ConstructorCall(Constructor<? extends E> constructor) {
            this.constructor = constructor;
        }

        @Override
        public E call() throws Exception {
            return constructor.newInstance();
        }
    }

    /**
     * Convert a record into a mutable POJO type
     * <p>
     * jOOQ's understanding of a mutable POJO is a Java type that has a default
     * constructor
     */
    private class MutablePOJOMapper implements RecordMapper<R, E> {

        private final Callable<E>                                constructor;
        private final boolean                                    useAnnotations;
        private final List<java.lang.reflect.Field>[]            members;
        private final List<java.lang.reflect.Method>[]           methods;
        private final Map<String, List<RecordMapper<R, Object>>> nested;
        private final E                                          instance;

        MutablePOJOMapper(Callable<E> constructor, E instance) {
            this.constructor = constructor;
            this.useAnnotations = hasColumnAnnotations(configuration, type);
            this.members = new List[fields.length];
            this.methods = new List[fields.length];
            this.nested = new HashMap<>();
            this.instance = instance;

            Map<String, Field<?>[]> nestedFields = new HashMap<>();
            for (int i = 0; i < fields.length; i++) {
                Field<?> field = fields[i];
                String name = field.getName();

                // Annotations are available and present
                if (useAnnotations) {
                    members[i] = getAnnotatedMembers(configuration, type, name);
                    methods[i] = getAnnotatedSetters(configuration, type, name);
                }

                // No annotations are present
                else {
                    int dot = name.indexOf('.');

                    // A nested mapping is applied
                    if (dot > -1) {
                        String prefix = name.substring(0, dot);

                        Field<?>[] f = nestedFields.get(prefix);
                        if (f == null) {
                            f = nCopies(fields.length, field("")).toArray(EMPTY_FIELD);
                            nestedFields.put(prefix, f);
                        }

                        f[i] = field(name(name.substring(prefix.length() + 1)), field.getDataType());

                        members[i] = Collections.emptyList();
                        methods[i] = Collections.emptyList();
                    }

                    // A top-level mapping is applied
                    else {
                        members[i] = getMatchingMembers(configuration, type, name);
                        methods[i] = getMatchingSetters(configuration, type, name);
                    }
                }
            }

            for (Entry<String, Field<?>[]> entry : nestedFields.entrySet()) {
                String prefix = entry.getKey();
                List<RecordMapper<R, Object>> list = new ArrayList<>();

                for (java.lang.reflect.Field member : getMatchingMembers(configuration, type, prefix)) {
                    list.add(configuration
                        .recordMapperProvider()
                        .provide(new Fields<>(entry.getValue()), member.getType())
                    );
                }

                for (Method method : getMatchingSetters(configuration, type, prefix)) {
                    list.add(configuration
                        .recordMapperProvider()
                        .provide(new Fields<>(entry.getValue()), method.getParameterTypes()[0])
                    );
                }

                nested.put(prefix, list);
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public final E map(R record) {
            try {
                E result = instance != null ? instance : constructor.call();

                for (int i = 0; i < fields.length; i++) {
                    for (java.lang.reflect.Field member : members[i])

                        // [#935] Avoid setting final fields
                        if ((member.getModifiers() & Modifier.FINAL) == 0)
                            map(record, result, member, i);

                    for (java.lang.reflect.Method method : methods[i]) {
                        Class<?> mType = method.getParameterTypes()[0];
                        Object value = record.get(i, mType);

                        // [#3082] Map nested collection types
                        if (value instanceof Collection && List.class.isAssignableFrom(mType)) {
                            Class componentType = (Class) ((ParameterizedType) method.getGenericParameterTypes()[0]).getActualTypeArguments()[0];
                            method.invoke(result, Convert.convert((Collection) value, componentType));
                        }

                        // Default reference types (including arrays)
                        else {
                            method.invoke(result, record.get(i, mType));
                        }
                    }
                }

                for (Entry<String, List<RecordMapper<R, Object>>> entry : nested.entrySet()) {
                    String prefix = entry.getKey();

                    for (RecordMapper<R, Object> mapper : entry.getValue()) {
                        Object value = mapper.map(record);

                        for (java.lang.reflect.Field member : getMatchingMembers(configuration, type, prefix)) {

                            // [#935] Avoid setting final fields
                            if ((member.getModifiers() & Modifier.FINAL) == 0)
                                map(value, result, member);
                        }

                        for (Method method : getMatchingSetters(configuration, type, prefix))
                            method.invoke(result, value);
                    }
                }

                return result;
            }
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record to " + type, e);
            }
        }

        @SuppressWarnings("rawtypes")
        private final void map(Record record, Object result, java.lang.reflect.Field member, int index) throws IllegalAccessException {
            Class<?> mType = member.getType();

            if (mType.isPrimitive()) {
                if (mType == byte.class)
                    map(record.get(index, byte.class), result, member);
                else if (mType == short.class)
                    map(record.get(index, short.class), result, member);
                else if (mType == int.class)
                    map(record.get(index, int.class), result, member);
                else if (mType == long.class)
                    map(record.get(index, long.class), result, member);
                else if (mType == float.class)
                    map(record.get(index, float.class), result, member);
                else if (mType == double.class)
                    map(record.get(index, double.class), result, member);
                else if (mType == boolean.class)
                    map(record.get(index, boolean.class), result, member);
                else if (mType == char.class)
                    map(record.get(index, char.class), result, member);
            }

            else {
                Object value = record.get(index, mType);

                // [#3082] Map nested collection types
                if (value instanceof Collection && List.class.isAssignableFrom(mType)) {
                    Class componentType = (Class) ((ParameterizedType) member.getGenericType()).getActualTypeArguments()[0];
                    member.set(result, Convert.convert((Collection) value, componentType));
                }

                // Default reference types (including arrays)
                else {
                    map(value, result, member);
                }
            }
        }

        private final void map(Object value, Object result, java.lang.reflect.Field member) throws IllegalAccessException {
            Class<?> mType = member.getType();

            if (mType.isPrimitive()) {
                if (mType == byte.class)
                    member.setByte(result, (Byte) value);
                else if (mType == short.class)
                    member.setShort(result, (Short) value);
                else if (mType == int.class)
                    member.setInt(result, (Integer) value);
                else if (mType == long.class)
                    member.setLong(result, (Long) value);
                else if (mType == float.class)
                    member.setFloat(result, (Float) value);
                else if (mType == double.class)
                    member.setDouble(result, (Double) value);
                else if (mType == boolean.class)
                    member.setBoolean(result, (Boolean) value);
                else if (mType == char.class)
                    member.setChar(result, (Character) value);
            }
            else {
                member.set(result, value);
            }
        }
    }

    /**
     * Convert a record into an "immutable" POJO (final fields, "matching"
     * constructor).
     */
    private class ImmutablePOJOMapper implements RecordMapper<R, E> {

        private final Constructor<E>                 constructor;
        private final Class<?>[]                     parameterTypes;
        private final boolean                        nested;
        private final int[]                          nonNestedIndexLookup;
        private final List<Integer>[]                nestedIndexLookup;
        private final List<Field<?>>[]               nestedMappedFields;
        private final RecordMapper<Record, Object>[] nestedMappers;

        public ImmutablePOJOMapper(Constructor<E> constructor, Class<?>[] parameterTypes, boolean supportsNesting) {
            this.constructor = accessible(constructor);
            this.parameterTypes = parameterTypes;
            this.nestedMappedFields = new List[prefixes().size()];
            this.nestedMappers = new RecordMapper[prefixes().size()];
            this.nestedIndexLookup = new List[prefixes().size()];
            this.nonNestedIndexLookup = new int[prefixes().size()];

            int i = -1;
            boolean hasNestedFields = false;

            if (supportsNesting) {

                prefixLoop:
                for (String prefix : prefixes()) {
                    ++i;

                    for (int j = 0; j < fields.length; j++) {
                        if (fields[j].getName().equals(prefix)) {
                            nonNestedIndexLookup[i] = j;
                            continue prefixLoop;
                        }
                        else if (fields[j].getName().startsWith(prefix + ".")) {
                            hasNestedFields = true;

                            if (nestedMappedFields[i] == null)
                                nestedMappedFields[i] = new ArrayList<>();
                            if (nestedIndexLookup[i] == null)
                                nestedIndexLookup[i] = new ArrayList<>();

                            nestedMappedFields[i].add(field(
                                name(fields[j].getName().substring(prefix.length() + 1)),
                                fields[j].getDataType()
                            ));

                            nestedIndexLookup[i].add(j);
                        }
                    }

                    if (nestedMappedFields[i] != null) {
                        nestedMappers[i] = configuration
                            .recordMapperProvider()
                            .provide((RecordType) new Fields<>(nestedMappedFields[i]), parameterTypes[i]);
                    }
                }
            }

            this.nested = hasNestedFields;
        }

        @Override
        public final E map(R record) {
            try {
                return constructor.newInstance(nested
                    ? mapNested(record)
                    : Convert.convert(record.intoArray(), parameterTypes)
                );
            }
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record to " + type, e);
            }
        }

        private final Object[] mapNested(R record) {
            Object[] converted = new Object[prefixes().size()];

            for (int i = 0; i < converted.length; i++) {
                if (nestedMappers[i] == null) {
                    converted[i] = record.get(nonNestedIndexLookup[i], parameterTypes[i]);
                }
                else {
                    RecordImplN rec = new RecordImplN(nestedMappedFields[i]);

                    for (int index = 0; index < nestedIndexLookup[i].size(); index++)
                        rec.set(index, record.get(nestedIndexLookup[i].get(index)));

                    converted[i] = nestedMappers[i].map(rec);
                }
            }

            return converted;
        }
    }

    /**
     * Create an immutable POJO given a constructor and its associated JavaBeans
     * {@link ConstructorProperties}
     */
    private class ImmutablePOJOMapperWithParameterNames implements RecordMapper<R, E> {

        private final Constructor<E>                  constructor;
        private final Class<?>[]                      parameterTypes;
        private final List<String>                    propertyNames;
        private final boolean                         useAnnotations;
        private final List<java.lang.reflect.Field>[] members;
        private final java.lang.reflect.Method[]      methods;
        private final Integer[]                       propertyIndexes;

        ImmutablePOJOMapperWithParameterNames(Constructor<E> constructor, List<String> propertyNames) {
            this.constructor = constructor;
            this.propertyNames = propertyNames;
            this.useAnnotations = hasColumnAnnotations(configuration, type);
            this.parameterTypes = constructor.getParameterTypes();
            this.members = new List[fields.length];
            this.methods = new Method[fields.length];
            this.propertyIndexes = new Integer[fields.length];

            for (int i = 0; i < fields.length; i++) {
                Field<?> field = fields[i];
                String name = field.getName();
                String nameLC = StringUtils.toCamelCaseLC(name);


                // Annotations are available and present
                if (useAnnotations) {
                    members[i] = getAnnotatedMembers(configuration, type, name);
                    methods[i] = getAnnotatedGetter(configuration, type, name);
                }

                // No annotations are present
                else {
                    members[i] = getMatchingMembers(configuration, type, name);
                    methods[i] = getMatchingGetter(configuration, type, name);
                }

                // [#3911] Liberal interpretation of the @ConstructorProperties specs:
                // We also accept properties that don't have a matching getter or member
                for (int j = 0; j < propertyNames.size(); j++) {
                    if (name.equals(propertyNames.get(j)) || nameLC.equals(propertyNames.get(j))) {
                        propertyIndexes[i] = j;
                        break;
                    }
                }
            }
        }

        @Override
        public final E map(R record) {
            try {
                Object[] parameterValues = new Object[parameterTypes.length];

                for (int i = 0; i < fields.length; i++) {
                    if (propertyIndexes[i] != null) {
                        parameterValues[propertyIndexes[i]] = record.get(i);
                    }
                    else {
                        for (java.lang.reflect.Field member : members[i]) {
                            int index = propertyNames.indexOf(member.getName());

                            if (index >= 0)
                                parameterValues[index] = record.get(i);
                        }

                        if (methods[i] != null) {
                            String name = getPropertyName(methods[i].getName());
                            int index = propertyNames.indexOf(name);

                            if (index >= 0)
                                parameterValues[index] = record.get(i);
                        }
                    }
                }

                Object[] converted = Convert.convert(parameterValues, parameterTypes);
                return accessible(constructor).newInstance(converted);
            }
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record to " + type, e);
            }
        }
    }

    private static <E> E attach(E attachable, Record record) {
        // [#2869] Attach the mapped outcome if it is Attachable and if the context's
        // Settings.attachRecords flag is set
        if (attachable instanceof Attachable)
            if (Tools.attachRecords(record.configuration()))
                ((Attachable) attachable).attach(record.configuration());

        return attachable;
    }

    private final Set<String> prefixes() {
        if (prefixes == null) {
            prefixes = new LinkedHashSet<>();

            for (Field<?> field : fields) {
                String name = field.getName();
                int dot = name.indexOf('.');
                prefixes.add(dot > -1 ? name.substring(0, dot) : name);
            }
        }

        return prefixes;
    }
}
