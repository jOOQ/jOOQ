/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static java.util.Collections.emptyList;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.configuration;
import static org.jooq.impl.Tools.converterContext;
import static org.jooq.impl.Tools.getAnnotatedGetter;
import static org.jooq.impl.Tools.getAnnotatedMembers;
import static org.jooq.impl.Tools.getAnnotatedSetters;
import static org.jooq.impl.Tools.getMatchingGetter;
import static org.jooq.impl.Tools.getMatchingMembers;
import static org.jooq.impl.Tools.getMatchingSetters;
import static org.jooq.impl.Tools.getPropertyName;
import static org.jooq.impl.Tools.hasColumnAnnotations;
import static org.jooq.impl.Tools.newRecord;
import static org.jooq.impl.Tools.recordType;
import static org.jooq.impl.Tools.row0;
import static org.jooq.tools.reflect.Reflect.accessible;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.ConstructorPropertiesProvider;
import org.jooq.ContextConverter;
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.ConverterProvider;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.XML;
import org.jooq.conf.Settings;
import org.jooq.exception.MappingException;
import org.jooq.tools.StringUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;


/**
 * This is the default implementation for <code>RecordMapper</code> types, which
 * applies to {@link Record#into(Class)}, {@link Result#into(Class)}, and
 * similar calls.
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
 * <h5>If the supplied type is an interface or an abstract class</h5>
 * <p>
 * Abstract types are instantiated using Java reflection {@link Proxy}
 * mechanisms. The returned proxy will wrap a {@link HashMap} containing
 * properties mapped by getters and setters of the supplied type. Methods (even
 * JPA-annotated ones) other than standard POJO getters and setters are not
 * supported. Details can be seen in {@link Reflect#as(Class)}.
 * <p>
 * <h5>If <code>&lt;E&gt;</code> is a {@link TableRecord} type (e.g. from a
 * generated record), then its meta data are used:</h5>
 * <p>
 * Generated {@link TableRecord} types reference their corresponding generated
 * {@link Table} types, which provide {@link TableField} meta data through
 * {@link Table#fields()}. All target {@link Record#fields()} are looked up in
 * the source table via {@link Table#indexOf(Field)} and their values are
 * mapped. Excess source values and missing target values are ignored.
 * <p>
 * <h5>If <code>&lt;E&gt;</code> is a field "value type" and
 * <code>&lt;R extends Record1&lt;T1&gt;&gt;</code>, i.e. it has exactly one
 * column:</h5>
 * <p>
 * The configured {@link ConverterProvider} is used to look up a
 * {@link Converter} between <code>T1</code> and <code>E</code>. By default, the
 * {@link DefaultConverterProvider} is used, which can (among other things):
 * <ul>
 * <li>Map between built-in types</li>
 * <li>Map between {@link Record} types and custom types by delegating to the
 * {@link Record}'s attached {@link RecordMapperProvider}</li>
 * <li>Map between {@link JSON} or {@link JSONB} and custom types by delegating
 * to Jackson or Gson (if found on the classpath)</li>
 * <li>Map between {@link XML} and custom types by delegating to JAXB (if found
 * on the classpath)</li>
 * </ul>
 * If such a {@link Converter} is found, that one is used to map to
 * <code>E</code>.
 * <p>
 * <h5>If a default constructor is available and any JPA
 * {@link jakarta.persistence.Column} annotations are found on the provided
 * <code>&lt;E&gt;</code> type, and the <code>jOOQ-jpa-extensions</code> module
 * is found on the classpath and configured in
 * {@link Configuration#annotatedPojoMemberProvider()}, only those are
 * used:</h5>
 * <p>
 * <ul>
 * <li>If <code>&lt;E&gt;</code> contains single-argument instance methods of
 * any visibility annotated with <code>jakarta.persistence.Column</code>, those
 * methods are invoked</li>
 * <li>If <code>&lt;E&gt;</code> contains no-argument instance methods of any
 * visibility starting with <code>getXXX</code> or <code>isXXX</code>, annotated
 * with <code>jakarta.persistence.Column</code>, then matching
 * <code>setXXX()</code> instance methods of any visibility are invoked</li>
 * <li>If <code>&lt;E&gt;</code> contains instance member fields of any
 * visibility annotated with <code>jakarta.persistence.Column</code>, those
 * members are set</li>
 * </ul>
 * Additional rules:
 * <ul>
 * <li>The same annotation can be re-used for several methods/members</li>
 * <li>{@link jakarta.persistence.Column#name()} must match
 * {@link Field#getName()}. All other annotation attributes are ignored</li>
 * <li>Static methods / member fields are ignored</li>
 * <li>Final member fields are ignored</li>
 * </ul>
 * <p>
 * <h5>If a default constructor is available and if there are no JPA
 * <code>jakarta.persistence.Column</code> annotations, or jOOQ can't find the
 * <code>jakarta.persistence</code> API on the classpath, jOOQ will map
 * <code>Record</code> values by naming convention:</h5>
 * <p>
 * If {@link Field#getName()} is <code>MY_field</code> (case-sensitive!), then
 * this field's value will be set on all of these (regardless of visibility):
 * <ul>
 * <li>Single-argument instance method <code>MY_field(…)</code></li>
 * <li>Single-argument instance method <code>myField(…)</code></li>
 * <li>Single-argument instance method <code>setMY_field(…)</code></li>
 * <li>Single-argument instance method <code>setMyField(…)</code></li>
 * <li>Non-final instance member field <code>MY_field</code></li>
 * <li>Non-final instance member field <code>myField</code></li>
 * </ul>
 * <p>
 * If {@link Field#getName()} is <code>MY_field.MY_nested_field</code>
 * (case-sensitive!), then this field's value will be considered a nested value
 * <code>MY_nested_field</code>, which is set on a nested POJO that is passed to
 * all of these (regardless of visibility):
 * <ul>
 * <li>Single-argument instance method <code>MY_field(…)</code></li>
 * <li>Single-argument instance method <code>myField(…)</code></li>
 * <li>Single-argument instance method <code>setMY_field(…)</code></li>
 * <li>Single-argument instance method <code>setMyField(…)</code></li>
 * <li>Non-final instance member field <code>MY_field</code></li>
 * <li>Non-final instance member field <code>myField</code></li>
 * </ul>
 * <p>
 * <h5>If no default constructor is available, but at least one constructor
 * annotated with <code>ConstructorProperties</code> is available, that one is
 * used</h5>
 * <p>
 * <ul>
 * <li>The standard JavaBeans {@link java.beans.ConstructorProperties}
 * annotation is used to match constructor arguments against POJO members or
 * getters, if the default {@link ConstructorPropertiesProvider} can look up the
 * implementation from the <code>jOOQ-mapper-extensions-beans</code> module, or
 * if you provide your own.</li>
 * <li>If the property names provided to the constructor match the record's
 * columns via the aforementioned naming conventions, that information is used.
 * </li>
 * <li>If those POJO members or getters have JPA annotations, and the
 * <code>jOOQ-jpa-extensions</code> module is found on the classpath and
 * configured in {@link Configuration#annotatedPojoMemberProvider()}, those will
 * be used according to the aforementioned rules, in order to map
 * <code>Record</code> values onto constructor arguments.</li>
 * <li>If those POJO members or getters don't have JPA annotations, the
 * aforementioned naming conventions will be used, in order to map
 * <code>Record</code> values onto constructor arguments.</li>
 * <li>When several annotated constructors are found, the first one is chosen,
 * randomly.</li>
 * <li>When invoking the annotated constructor, values are converted onto
 * constructor argument types</li>
 * </ul>
 * <p>
 * <h5>If Kotlin is available and the argument class has Kotlin reflection meta
 * data available, and {@link Settings#isMapConstructorParameterNamesInKotlin()}
 * is turned on, parameter names are reflected and used.</h5>
 * <p>
 * <ul>
 * <li>The Kotlin compiler adds meta data available for reflection using Kotlin
 * reflection APIs to derive parameter names.</li>
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
 * <h5>Other restrictions</h5>
 * <p>
 * <ul>
 * <li><code>&lt;E&gt;</code> must provide a default or a "matching"
 * constructor. Non-public default constructors are made accessible using
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
    private final Field<?>[]               fields;
    private final RecordType<R>            rowType;

    /**
     * The target type.
     */
    private final Class<? extends E>       type;

    /**
     * The configuration in whose context this {@link RecordMapper} operates.
     * <p>
     * This configuration can be used for caching reflection information.
     */
    private final Configuration            configuration;
    private final String                   namePathSeparator;

    /**
     * A delegate mapper created from type information in <code>type</code>.
     */
    private RecordMapper<R, E>             delegate;

    /**
     * A set of field name prefixes that may defined the behaviour of nested
     * record mappers.
     */
    private transient Map<String, Integer> prefixes;

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
        this.configuration = configuration(configuration);
        this.namePathSeparator = this.configuration.settings().getNamePathSeparator();

        init(instance);
    }

    private final void init(E instance) {
        Boolean debugVTFL = null;
        Boolean debugVTCP = null;
        Boolean debugMutable = null;
        Boolean debugMutableConstructors = null;
        Boolean debugCPSettings = null;
        Boolean debugRC = null;
        Boolean debugRCSettings = null;
        Boolean debugKClass = null;
        Boolean debugKSettings = null;
        Boolean debugMatchDegreeFlat = null;
        Boolean debugMatchDegreeNested = null;
        Boolean debugTopLevelClass = null;
        Boolean debugStaticNestedClass = null;

        // Arrays can be mapped easily
        if (type.isArray()) {
            delegate = new ArrayMapper(instance);
            return;
        }

        if (instance == null) {
            if (Stream.class.isAssignableFrom(type)) {
                delegate = r -> (E) Stream.of(((FieldsImpl<R>) rowType).mapper(configuration, Object[].class).map(r));
                return;
            }

            // [#1470] Return a proxy if the supplied type is an interface
            // [#10071] [#11148] Primitive types are abstract! They're mapped by a ConverterProvider only later
            if (Modifier.isAbstract(type.getModifiers()) && !type.isPrimitive()) {
                delegate = new ProxyMapper();
                return;
            }
        }

        // [#2989] [#2836] Records are mapped
        if (AbstractRecord.class.isAssignableFrom(type)) {
            delegate = (RecordMapper<R, E>) new RecordToRecordMapper<>((AbstractRecord) instance);
            return;
        }

        // [#10071] Single-field Record1 types can be mapped if there is a ConverterProvider allowing for this mapping
        ContextConverter<?, E> c;
        if ((debugVTFL = fields.length == 1) && instance == null && (debugVTCP = (c = Tools.converter(configuration, instance, (Class) fields[0].getType(), type)) != null)) {
            delegate = new ValueTypeMapper(c);
            return;
        }

        // [#1340] Allow for using non-public default constructors
        try {
            MutablePOJOMapper m = instance != null
                ? new MutablePOJOMapper(null, instance)
                : new MutablePOJOMapper(new ConstructorCall<>(accessible(type.getDeclaredConstructor())), null);

            // [#10194] Check if the POJO is really mutable. There might as well
            //          be a no-args constructor for other reasons, e.g. when
            //          using an immutable Kotlin data class with defaulted parameters
            //          If the no-args constructor is the only one, take it none-theless
            if ((debugMutable = m.isMutable()) || (debugMutableConstructors = type.getDeclaredConstructors().length <= 1)) {
                delegate = m;
                return;
            }
        }
        catch (NoSuchMethodException ignore) {
            debugMutable = false;
        }

        // [#1336] If no default constructor is present, check if there is a
        // "matching" constructor with the same number of fields as this record
        Constructor<E>[] constructors = (Constructor<E>[]) type.getDeclaredConstructors();

        // [#6868] Prefer public constructors
        Arrays.sort(constructors, (c1, c2) -> (c2.getModifiers() & Modifier.PUBLIC) - (c1.getModifiers() & Modifier.PUBLIC));

        // [#1837] [#10349] [#11123] If any java.beans.ConstructorProperties annotations are
        // present use those rather than matching constructors by the number of arguments
        if (debugCPSettings = !FALSE.equals(configuration.settings().isMapConstructorPropertiesParameterNames())) {
            ConstructorPropertiesProvider cpp = configuration.constructorPropertiesProvider();

            for (Constructor<E> constructor : constructors) {
                String[] properties = cpp.properties(constructor);

                if (properties != null) {
                    delegate = new ImmutablePOJOMapper(constructor, constructor.getParameterTypes(), Arrays.asList(properties), true);
                    return;
                }
            }
        }


        // [#11778] Java 16 record types expose their component names
        if ((debugRCSettings = TRUE.equals(configuration.settings().isMapRecordComponentParameterNames())) && (debugRC = type.isRecord())) {
            RecordComponent[] rc = type.getRecordComponents();
            List<?> types = Tools.map(rc, RecordComponent::getType);

            for (Constructor<E> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();

                if (types.equals(Arrays.asList(parameterTypes))) {
                    delegate = new ImmutablePOJOMapper(
                        constructor,
                        parameterTypes,
                        Tools.map(rc, RecordComponent::getName),
                        true
                    );

                    return;
                }
            }
        }


        // [#7324] Map immutable Kotlin classes by parameter names if kotlin-reflect is on the classpath
        if ((debugKClass = Tools.isKotlinAvailable()) && (debugKSettings = !FALSE.equals(configuration.settings().isMapConstructorParameterNamesInKotlin()))) {
            try {
                Reflect jvmClassMappingKt = Tools.ktJvmClassMapping();
                Reflect kClasses = Tools.ktKClasses();
                Reflect kTypeParameter = Tools.ktKTypeParameter();

                Object klass = jvmClassMappingKt.call("getKotlinClass", type).get();
                Reflect primaryConstructor = kClasses.call("getPrimaryConstructor", klass);

                // It is a Kotlin class
                if (debugKClass = primaryConstructor.get() != null) {
                    List<?> parameters = primaryConstructor.call("getParameters").get();
                    Class<?> klassType = Tools.ktKClass().type();
                    Method getJavaClass = jvmClassMappingKt.type().getMethod("getJavaClass", klassType);

                    List<String> parameterNames = new ArrayList<>(parameters.size());
                    Class<?>[] parameterTypes = new Class[parameters.size()];

                    for (int i = 0; i < parameterTypes.length; i++) {
                        Reflect parameter = Reflect.on(parameters.get(i));
                        Object typeClassifier = parameter.call("getType").call("getClassifier").get();
                        String name = parameter.call("getName").get();

                        // [#14283] Unnest @JvmInline value classes
                        try {
                            while (Reflect.on(typeClassifier).call("isValue").<Boolean>get()) {
                                typeClassifier = kClasses
                                    .call("getPrimaryConstructor", typeClassifier)
                                    .call("getParameters")

                                    // kotlin value classes are required to have exactly 1 parameter
                                    .call("get", 0)
                                    .call("getType")
                                    .call("getClassifier")
                                    .get();
                            }
                        }

                        // [#14283] KClass.isValue() was added in kotlin 1.5 only
                        catch (ReflectException ignore) {}

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

                    Constructor<E> javaConstructor = (Constructor<E>) accessible(this.type.getDeclaredConstructor(parameterTypes));
                    delegate = new ImmutablePOJOMapper(javaConstructor, javaConstructor.getParameterTypes(), parameterNames, true);
                    return;
                }
            }
            catch (ReflectException | InvocationTargetException | IllegalAccessException | NoSuchMethodException ignore) {}
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
                    if (supportsNesting)
                        debugMatchDegreeNested = true;
                    else
                        debugMatchDegreeFlat = true;

                    // [#4627] use parameter names from byte code if available
                    if (mapConstructorParameterNames) {
                        Parameter[] parameters = constructor.getParameters();

                        if (parameters != null && parameters.length > 0)
                            delegate = new ImmutablePOJOMapper(constructor, parameterTypes, collectParameterNames(parameters), supportsNesting);
                    }

                    if (delegate == null)
                        delegate = new ImmutablePOJOMapper(constructor, parameterTypes, emptyList(), supportsNesting);

                    return;
                }
            }

            if (supportsNesting)
                debugMatchDegreeNested = false;
            else
                debugMatchDegreeFlat = false;
        }

        // [#4627] if there is no exact match in terms of the number of parameters,
        // but using parameter annotations is allowed and those are in fact present,
        // use the first available constructor (thus the choice is undeterministic)
        if (mapConstructorParameterNames) {
            Constructor<E> constructor = constructors[0];
            Parameter[] parameters = constructor.getParameters();

            if (parameters != null && parameters.length > 0) {
                delegate = new ImmutablePOJOMapper(constructor, constructor.getParameterTypes(), collectParameterNames(parameters), false);
                return;
            }
        }

        debugTopLevelClass = !type.isMemberClass();
        debugStaticNestedClass = type.isMemberClass() && Modifier.isStatic(type.getModifiers());

        throw new MappingException(
            """
            No DefaultRecordMapper strategy applies to type $type for row type $rowType. Attempted strategies include (in this order):
            - Is type an array (false)?
            - Is type a Stream (false)?
            - Does row type have only 1 column ($debugVTFL) and did ConverterProvider provide a Converter for type ($debugVTCP)?
            - Is type abstract (false)?
            - Is type a org.jooq.Record (false)?
            - Is type a mutable POJO (a POJO with setters or non-final members: $debugMutable) and has a no-args constructor ($debugMutableConstructors)?
            - Does type have a @ConstructorProperties annotated constructor (false) and is Settings.mapConstructorPropertiesParameterNames enabled ($debugCPSettings)?
            - Is type a java.lang.Record ($debugRC) and is Settings.mapRecordComponentParameterNames enabled ($debugRCSettings)?
            - Is type a kotlin class ($debugKClass) and is Settings.mapConstructorParameterNamesInKotlin enabled ($debugKSettings)?
            - Is there a constructor that matches row type's degrees with nested fields ($debugMatchDegreeNested) or flat fields ($debugMatchDegreeFlat)
            - Is the type a top level class ($debugTopLevelClass) or static nested class ($debugStaticNestedClass)?
            -   (Inner classes cannot be created via reflection)
            - Is Settings.mapConstructorParameterNames enabled ($debugMatchNames)
            """.replace("$type", type.toString())
               .replace("$rowType", rowType.toString())
               .replace("$debugVTFL", debug(debugVTFL))
               .replace("$debugVTCP", debug(debugVTCP))
               .replace("$debugCPSettings", debug(debugCPSettings))
               .replace("$debugMutableConstructors", debug(debugMutableConstructors))
               .replace("$debugMutable", debug(debugMutable))
               .replace("$debugRCSettings", debug(debugRCSettings))
               .replace("$debugRC", debug(debugRC))
               .replace("$debugKClass", debug(debugKClass))
               .replace("$debugKSettings", debug(debugKSettings))
               .replace("$debugMatchDegreeNested", debug(debugMatchDegreeNested))
               .replace("$debugMatchDegreeFlat", debug(debugMatchDegreeFlat))
               .replace("$debugMatchNames", debug(mapConstructorParameterNames))
               .replace("$debugTopLevelClass", debug(debugTopLevelClass))
               .replace("$debugStaticNestedClass", debug(debugStaticNestedClass))
        );
    }

    private static final String debug(Boolean debug) {
        return debug == null ? "check skipped" : debug.toString();
    }

    private static final List<String> collectParameterNames(Parameter[] parameters) {
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
            throw new MappingException("An error occurred when mapping record to " + type, e);
        }
    }

    private abstract class AbstractDelegateMapper<R0 extends Record, E0> implements RecordMapper<R0, E0> {

        @Override
        public String toString() {
            return getClass().getSimpleName() + " [ (" + rowType + ") -> " + type + "]";
        }
    }

    /**
     * Convert a record into an array of a given type.
     * <p>
     * The supplied type is usually <code>Object[]</code>, but in some cases, it
     * may make sense to supply <code>String[]</code>, <code>Integer[]</code>
     * etc.
     */
    private class ArrayMapper extends AbstractDelegateMapper<R, E> {

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

    private class ValueTypeMapper extends AbstractDelegateMapper<R, E> {

        private final ContextConverter<?, E> converter;

        public ValueTypeMapper(ContextConverter<?, E> converter) {
            this.converter = converter;
        }

        @Override
        public final E map(R record) {
            int size = record.size();
            if (size != 1)
                throw new MappingException("Cannot map multi-column record of degree " + size + " to value type " + type);

            return converter != null
                ? ((ContextConverter<Object, E>) converter).from(record.get(0), converterContext(record))
                : record.get(0, type);
        }
    }

    /**
     * Convert a record into an hash map proxy of a given type.
     * <p>
     * This is done for types that are not instanciable
     */
    private class ProxyMapper extends AbstractDelegateMapper<R, E> {

        private final MutablePOJOMapper pojomapper;

        ProxyMapper() {
            this.pojomapper = new MutablePOJOMapper(() -> Reflect.on(new HashMap<>()).as(type), null);
        }

        @Override
        public final E map(R record) {
            return pojomapper.map(record);
        }
    }

    /**
     * Convert a record into another record type.
     */
    private class RecordToRecordMapper<E extends AbstractRecord> extends AbstractDelegateMapper<R, AbstractRecord> {

        private final E instance;

        RecordToRecordMapper(E instance) {
            this.instance = instance;
        }

        @Override
        public final AbstractRecord map(R record) {
            try {
                if (record instanceof AbstractRecord a) {
                    if (instance != null)
                        return a.intoRecord(instance);
                    else
                        return a.intoRecord((Class<AbstractRecord>) type);
                }

                throw new MappingException("Cannot map record " + record + " to type " + type);
            }
            catch (Exception e) {
                throw new MappingException("An error occurred when mapping record to " + type, e);
            }
        }
    }

    private static final record ConstructorCall<E>(Constructor<? extends E> constructor) implements Callable<E> {
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
    private class MutablePOJOMapper extends AbstractDelegateMapper<R, E> {

        private final Callable<E>                    constructor;
        private final boolean                        useAnnotations;
        private final List<MappedMember>[]           members;
        private final List<MappedMethod>[]           methods;
        private final Map<String, NestedMappingInfo> nestedMappingInfos;
        private final E                              instance;

        MutablePOJOMapper(Callable<E> constructor, E instance) {
            this.constructor = constructor;
            this.useAnnotations = hasColumnAnnotations(configuration, type);
            this.members = new List[fields.length];
            this.methods = new List[fields.length];
            this.instance = instance;
            this.nestedMappingInfos = new HashMap<>();

            Map<String, List<Field<?>>> nestedMappedFields = null;

            for (int i = 0; i < fields.length; i++) {
                Field<?> field = fields[i];
                String name = field.getName();

                // Annotations are available and present
                if (useAnnotations) {
                    members[i] = getAnnotatedMembers(configuration, type, field, true);
                    methods[i] = getAnnotatedSetters(configuration, type, field, true);
                }

                // No annotations are present
                else {
                    int separator = name.indexOf(namePathSeparator);

                    // A nested mapping is applied
                    if (separator > -1) {
                        String prefix = name.substring(0, separator);

                        if (nestedMappedFields == null)
                            nestedMappedFields = new HashMap<>();

                        nestedMappedFields
                            .computeIfAbsent(prefix, p -> new ArrayList<>())
                            .add(field(name(name.substring(prefix.length() + 1)), field.getDataType()));

                        nestedMappingInfos
                            .computeIfAbsent(prefix, p -> new NestedMappingInfo())
                            .indexLookup
                            .add(i);

                        members[i] = Collections.emptyList();
                        methods[i] = Collections.emptyList();
                    }

                    // A top-level mapping is applied
                    else {
                        members[i] = getMatchingMembers(configuration, type, field, true);
                        methods[i] = getMatchingSetters(configuration, type, field, true);
                    }
                }
            }

            if (nestedMappedFields != null) {
                nestedMappedFields.forEach((prefix, list) -> {
                    NestedMappingInfo nestedMappingInfo = nestedMappingInfos.get(prefix);
                    nestedMappingInfo.row = Tools.row0(list);
                    nestedMappingInfo.recordDelegate = newRecord(true, configuration, recordType(nestedMappingInfo.row.size()), nestedMappingInfo.row);

                    for (MappedMember member : getMatchingMembers(configuration, type, field(prefix), true))
                        nestedMappingInfo.mappers.add(
                            nestedMappingInfo.row.fields.mapper(configuration, member.member().getType())
                        );

                    for (MappedMethod method : getMatchingSetters(configuration, type, field(prefix), true))
                        nestedMappingInfo.mappers.add(
                            nestedMappingInfo.row.fields.mapper(configuration, method.parameterTypes()[0])
                        );
                });
            }
        }

        final boolean isMutable() {
            for (List<MappedMethod> m : methods)
                if (!m.isEmpty())
                    return true;

            for (List<MappedMember> m1 : members)
                for (MappedMember m2 : m1)
                    if ((m2.member().getModifiers() & Modifier.FINAL) == 0)
                        return true;

            return false;
        }

        @Override
        public final E map(R record) {
            try {
                final E result = instance != null ? instance : constructor.call();
                final ConverterContext cc = Tools.converterContext(record);

                for (int i = 0; i < fields.length; i++) {
                    for (MappedMember member : members[i])

                        // [#935] Avoid setting final fields
                        if ((member.member().getModifiers() & Modifier.FINAL) == 0)
                            map(record, cc, result, member, i);

                    for (MappedMethod method : methods[i]) {
                        Class<?> mType = method.parameterTypes()[0];
                        Object value = method.converter() != null
                            ? ((ContextConverter<Object, Object>) method.converter()).from(record.get(i), cc)
                            : record.get(i, mType);

                        // [#3082] [#10910] Try mapping nested collection types
                        Object list = tryConvertToListOrSet(value, mType, method.genericParameterTypes()[0]);
                        if (list != null)
                            method.method().invoke(result, list);
                        else
                            method.method().invoke(result, value);
                    }
                }

                for (final Entry<String, NestedMappingInfo> entry : nestedMappingInfos.entrySet()) {
                    final String prefix = entry.getKey();

                    for (final RecordMapper<AbstractRecord, Object> mapper : entry.getValue().mappers) {
                        entry.getValue().recordDelegate.operate(rec -> {
                            List<Integer> indexes = entry.getValue().indexLookup;
                            for (int index = 0; index < indexes.size(); index++)
                                rec.set(index, record.get(indexes.get(index)));

                            Object value = mapper.map(rec);
                            for (MappedMember member : getMatchingMembers(configuration, type, field(prefix), true)) {

                                // [#935] Avoid setting final fields
                                if ((member.member().getModifiers() & Modifier.FINAL) == 0)
                                    map(value, result, member.member());
                            }

                            for (MappedMethod method : getMatchingSetters(configuration, type, field(prefix), true))
                                method.method().invoke(result, value);

                            return rec;
                        });
                    }
                }

                return result;
            }
            catch (Exception e) {
                throw new MappingException("An error occurred when mapping record to " + type, e);
            }
        }

        private final void map(
            Record record,
            ConverterContext cc,
            Object result,
            MappedMember member,
            int index
        ) throws IllegalAccessException {
            Class<?> mType = member.member().getType();

            if (mType.isPrimitive()) {
                if (mType == byte.class)
                    map(record.get(index, byte.class), result, member.member());
                else if (mType == short.class)
                    map(record.get(index, short.class), result, member.member());
                else if (mType == int.class)
                    map(record.get(index, int.class), result, member.member());
                else if (mType == long.class)
                    map(record.get(index, long.class), result, member.member());
                else if (mType == float.class)
                    map(record.get(index, float.class), result, member.member());
                else if (mType == double.class)
                    map(record.get(index, double.class), result, member.member());
                else if (mType == boolean.class)
                    map(record.get(index, boolean.class), result, member.member());
                else if (mType == char.class)
                    map(record.get(index, char.class), result, member.member());
            }

            else {
                Object value = member.converter() != null
                    ? ((ContextConverter<Object, Object>) member.converter()).from(record.get(index), cc)
                    : record.get(index, mType);

                // [#3082] [#10910] [#11213] Try mapping nested collection types
                Object list = tryConvertToListOrSet(value, mType, member.member().getGenericType());
                if (list != null)
                    member.member().set(result, list);
                else
                    map(value, result, member.member());
            }
        }

        private final Collection<?> tryConvertToListOrSet(Object value, Class<?> mType, Type genericType) {
            if (value instanceof Collection<?> c) {
                if (genericType instanceof ParameterizedType p) {
                    Class<?> componentType = (Class<?>) p.getActualTypeArguments()[0];

                    if (mType == List.class || mType == ArrayList.class)
                        return Convert.convert(c, componentType);
                    else if (mType == Set.class || mType == LinkedHashSet.class)
                        return new LinkedHashSet<>(Convert.convert(c, componentType));
                    else if (mType == HashSet.class)
                        return new HashSet<>(Convert.convert(c, componentType));
                }
            }

            return null;
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
    private class ImmutablePOJOMapper extends AbstractDelegateMapper<R, E> {

        final Constructor<E>               constructor;
        final Class<?>[]                   parameterTypes;
        final Converter<?, ?>[]            parameterConverters;
        private final boolean              nested;
        private final NestedMappingInfo[]  nestedMappingInfo;
        private final Integer[]            propertyIndexes;

        // For named parameter mappings where the mapping is governed by JPA annotations
        private final List<String>         propertyNames;
        private final boolean              useAnnotations;
        private final List<MappedMember>[] members;
        private final MappedMethod[]       methods;

        ImmutablePOJOMapper(Constructor<E> constructor, Class<?>[] parameterTypes, List<String> propertyNames, boolean supportsNesting) {
            int size = prefixes().size();

            this.constructor = accessible(constructor);
            this.parameterTypes = parameterTypes;
            this.parameterConverters = new Converter[parameterTypes.length];
            this.nestedMappingInfo = new NestedMappingInfo[size];
            this.propertyIndexes = new Integer[fields.length];
            this.propertyNames = propertyNames;
            this.useAnnotations = hasColumnAnnotations(configuration, type);
            this.members = new List[fields.length];
            this.methods = new MappedMethod[fields.length];

            if (propertyNames.isEmpty()) {
                if (!supportsNesting) {
                    for (int i = 0; i < fields.length; i++)
                        propertyIndexes[i] = i;
                }
                else {
                    for (int i = 0; i < fields.length; i++) {
                        Field<?> field = fields[i];
                        String name = field.getName();
                        int separator = name.indexOf(namePathSeparator);
                        propertyIndexes[i] = prefixes().get(separator > -1 ? name.substring(0, separator) : name);
                    }
                }
            }
            else {

                fieldLoop:
                for (int i = 0; i < fields.length; i++) {
                    Field<?> field = fields[i];
                    String name = field.getName();
                    String nameLC = StringUtils.toCamelCaseLC(name);

                    // Annotations are available and present
                    if (useAnnotations) {
                        members[i] = getAnnotatedMembers(configuration, type, field, false);
                        methods[i] = getAnnotatedGetter(configuration, type, field, true);
                    }

                    // No annotations are present
                    else {
                        members[i] = getMatchingMembers(configuration, type, field, false);
                        methods[i] = getMatchingGetter(configuration, type, field, true);
                    }

                    // [#3911] Liberal interpretation of the @ConstructorProperties specs:
                    // We also accept properties that don't have a matching getter or member
                    for (int j = 0; j < propertyNames.size(); j++) {
                        if (name.equals(propertyNames.get(j)) || nameLC.equals(propertyNames.get(j))) {
                            propertyIndexes[i] = j;
                            continue fieldLoop;
                        }
                    }

                    for (int j = 0; j < propertyNames.size(); j++) {
                        if (name.startsWith(propertyNames.get(j) + namePathSeparator)) {
                            propertyIndexes[i] = j;
                            continue fieldLoop;
                        }
                    }
                }
            }

            boolean hasNestedFields = false;
            List<Field<?>>[] nestedMappedFields = new List[size];

            if (supportsNesting) {

                prefixLoop:
                for (Entry<String, Integer> entry : prefixes().entrySet()) {
                    String prefix = entry.getKey();
                    int i = entry.getValue();

                    if (nestedMappingInfo[i] == null)
                        nestedMappingInfo[i] = new NestedMappingInfo();

                    for (int j = 0; j < fields.length; j++) {
                        if (fields[j].getName().equals(prefix)) {
                            nestedMappingInfo[i].indexLookup.add(j);
                            continue prefixLoop;
                        }
                    }

                    for (int j = 0; j < fields.length; j++) {
                        if (fields[j].getName().startsWith(prefix + namePathSeparator)) {
                            hasNestedFields = true;

                            if (nestedMappedFields[i] == null)
                                nestedMappedFields[i] = new ArrayList<>();

                            nestedMappedFields[i].add(field(
                                name(fields[j].getName().substring(prefix.length() + 1)),
                                fields[j].getDataType()
                            ));

                            nestedMappingInfo[i].indexLookup.add(j);
                        }
                    }

                    if (nestedMappedFields[i] != null) {
                        nestedMappingInfo[i].row = row0(nestedMappedFields[i].toArray(EMPTY_FIELD));
                        nestedMappingInfo[i].recordDelegate = newRecord(true, configuration, recordType(nestedMappingInfo[i].row.size()), nestedMappingInfo[i].row);
                        nestedMappingInfo[i].mappers.add(
                            nestedMappingInfo[i].row.fields.mapper(configuration, parameterTypes[propertyIndexes[nestedMappingInfo[i].indexLookup.get(0)]])
                        );
                    }
                }
            }

            if (!hasNestedFields)
                for (int i = 0; i < fields.length; i++)
                    if (propertyIndexes[i] != null)
                        parameterConverters[propertyIndexes[i]] = configuration.converterProvider().provide(fields[i].getType(), parameterTypes[propertyIndexes[i]]);

            this.nested = hasNestedFields;
        }

        @Override
        public final E map(R record) {
            try {
                return constructor.newInstance(nested ? mapNested(record) : mapNonnested(record));
            }
            catch (Exception e) {
                throw new MappingException("An error occurred when mapping record to " + type, e);
            }
        }

        private final Object[] mapNonnested(R record) {

            // [#10425] Initialise array to constructor parameter type init values
            Object[] converted = Tools.map(parameterTypes, c -> Reflect.initValue(c), Object[]::new);
            ConverterContext cc = Tools.converterContext(record);

            for (int i = 0; i < record.size(); i++)
                set(record, cc, i, converted, propertyIndexes[i]);

            return converted;
        }

        final void set(Record from, ConverterContext cc, int fromIndex, Object[] to, Integer toIndex) {

            // TODO: This logic could be applicable to mapNested() as well?
            if (toIndex != null) {
                to[toIndex] = parameterConverters[toIndex] != null
                    ? ((ContextConverter<Object, Object>) parameterConverters[toIndex]).from(from.get(fromIndex), cc)
                    : from.get(fromIndex, parameterTypes[toIndex]);
            }
            else {
                for (MappedMember member : members[fromIndex]) {
                    int index = propertyNames.indexOf(member.member().getName());

                    if (index >= 0)
                        to[index] = member.converter() != null
                            ? ((ContextConverter<Object, Object>) member.converter()).from(from.get(fromIndex), cc)
                            : from.get(fromIndex, member.member().getType());
                }

                MappedMethod m = methods[fromIndex];
                if (m != null) {
                    String name = getPropertyName(m.method().getName());
                    int index = propertyNames.indexOf(name);

                    if (index >= 0)
                        to[index] = m.converter() != null
                            ? ((ContextConverter<Object, Object>) m.converter()).from(from.get(fromIndex), cc)
                            : from.get(fromIndex, m.method().getReturnType());

                }
            }
        }

        private final Object[] mapNested(final R record) {
            Object[] converted = new Object[parameterTypes.length];

            for (int i = 0; i < nestedMappingInfo.length; i++) {
                NestedMappingInfo info = nestedMappingInfo[i];
                List<Integer> indexLookup = info.indexLookup;
                Integer j = indexLookup.get(0);
                Integer k = propertyIndexes[j];

                if (k != null) {
                    if (info.row == null)
                        converted[k] = record.get(j, parameterTypes[k]);
                    else
                        converted[k] = info.mappers.get(0).map(info.recordDelegate.operate(rec -> {
                            for (int x = 0; x < indexLookup.size(); x++)
                                rec.set(x, record.get(indexLookup.get(x)));

                            return rec;
                        }));
                }
            }

            return converted;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + " [ (" + rowType + ") -> " + constructor + "]";
        }
    }

    private static final <E> E attach(E attachable, Record record) {
        // [#2869] Attach the mapped outcome if it is Attachable and if the context's
        // Settings.attachRecords flag is set
        if (attachable instanceof Attachable a)
            if (Tools.attachRecords(record.configuration()))
                a.attach(record.configuration());

        return attachable;
    }

    private final Map<String, Integer> prefixes() {
        if (prefixes == null) {
            prefixes = new LinkedHashMap<>();
            int[] i = { 0 };

            for (Field<?> field : fields) {
                String name = field.getName();
                int separator = name.indexOf(namePathSeparator);
                prefixes.computeIfAbsent(separator > -1 ? name.substring(0, separator) : name, k -> i[0]++);
            }
        }

        return prefixes;
    }

    static class NestedMappingInfo {
        final List<RecordMapper<AbstractRecord, Object>> mappers;
        AbstractRow                                      row;
        final List<Integer>                              indexLookup;
        RecordDelegate<? extends AbstractRecord>         recordDelegate;

        NestedMappingInfo() {
            mappers = new ArrayList<>();
            indexLookup = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "NestedMappingInfo " + indexLookup + "; (" + row + ")";
        }
    }

    static final record MappedParameters(
        Class<?> parameter,
        ContextConverter<?, ?> converter
    ) {}

    static final record MappedMember(
        java.lang.reflect.Field member,
        ContextConverter<?, ?> converter
    ) {}

    static final record MappedMethod(
        java.lang.reflect.Method method,
        Class<?>[] parameterTypes,
        Type[] genericParameterTypes,
        ContextConverter<?, ?> converter
    ) {}

    @Override
    public String toString() {
        return delegate.toString();
    }
}
