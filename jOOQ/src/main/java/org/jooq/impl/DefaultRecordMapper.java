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

import static org.jooq.impl.Utils.getAnnotatedGetter;
import static org.jooq.impl.Utils.getAnnotatedMembers;
import static org.jooq.impl.Utils.getAnnotatedSetters;
import static org.jooq.impl.Utils.getMatchingGetter;
import static org.jooq.impl.Utils.getMatchingMembers;
import static org.jooq.impl.Utils.getMatchingSetters;
import static org.jooq.impl.Utils.getPropertyName;
import static org.jooq.impl.Utils.hasColumnAnnotations;
import static org.jooq.tools.reflect.Reflect.accessible;

import java.beans.ConstructorProperties;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.exception.MappingException;
import org.jooq.tools.Convert;
import org.jooq.tools.reflect.Reflect;

/**
 * This is the default implementation for <code>RecordMapper</code> types.
 * <p>
 * The mapping algorithm is this:
 * <p>
 * <h5>If <code>&lt;E></code> is an array type:</h5>
 * <p>
 * The resulting array is of the nature described in {@link Record#intoArray()}.
 * Arrays more specific than <code>Object[]</code> can be specified as well,
 * e.g. <code>String[]</code>. If conversion to the element type of more
 * specific arrays fails, a {@link MappingException} is thrown, wrapping
 * conversion exceptions.
 * <p>
 * <h5>If a default constructor is available and any JPA {@link Column}
 * annotations are found on the provided <code>&lt;E></code>, only those are
 * used:</h5>
 * <p>
 * <ul>
 * <li>If <code>&lt;E></code> contains public single-argument instance methods
 * annotated with <code>Column</code>, those methods are invoked</li>
 * <li>If <code>&lt;E></code> contains public no-argument instance methods
 * starting with <code>getXXX</code> or <code>isXXX</code>, annotated with
 * <code>Column</code>, then matching public <code>setXXX()</code> instance
 * methods are invoked</li>
 * <li>If <code>&lt;E></code> contains public instance member fields annotated
 * with <code>Column</code>, those members are set</li>
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
 * this field's value will be set on all of these:
 * <ul>
 * <li>Public single-argument instance method <code>MY_field(...)</code></li>
 * <li>Public single-argument instance method <code>myField(...)</code></li>
 * <li>Public single-argument instance method <code>setMY_field(...)</code></li>
 * <li>Public single-argument instance method <code>setMyField(...)</code></li>
 * <li>Public non-final instance member field <code>MY_field</code></li>
 * <li>Public non-final instance member field <code>myField</code></li>
 * </ul>
 * <p>
 * <h5>If no default constructor is available, but at least one constructor
 * annotated with <code>ConstructorProperties</code> is available, that one is
 * used</h5>
 * <p>
 * <ul>
 * <li>The standard JavaBeans {@link ConstructorProperties} annotation is used
 * to match constructor arguments against POJO members or getters.</li>
 * <li>If those POJO members or getters have JPA annotations, those will be used
 * according to the aforementioned rules, in order to map <code>Record</code>
 * values onto constructor arguments.</li>
 * <li>If those POJO members or getters don't have JPA annotations, the
 * aforementioned naming conventions will be used, in order to map
 * <code>Record</code> values onto constructor arguments.</li>
 * <li>When several annotated constructors are found, the first one is chosen
 * (as reported by {@link Class#getDeclaredConstructors()}</li>
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
 * (as reported by {@link Class#getDeclaredConstructors()}</li>
 * <li>When invoking the "matching" constructor, values are converted onto
 * constructor argument types</li>
 * </ul>
 * <p>
 * <h5>If the supplied type is an interface or an abstract class</h5>
 * <p>
 * Abstract types are instanciated using Java reflection {@link Proxy}
 * mechanisms. The returned proxy will wrap a {@link HashMap} containing
 * properties mapped by getters and setters of the supplied type. Methods (even
 * JPA-annotated ones) other than standard POJO getters and setters are not
 * supported. Details can be seen in {@link Reflect#as(Class)}.
 * <p>
 * <h5>Other restrictions</h5>
 * <p>
 * <ul>
 * <li><code>&lt;E></code> must provide a default or a "matching" constructor.
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
     * The record type
     */
    private final Field<?>[]         fields;

    /**
     * The target type
     */
    private final Class<? extends E> type;

    /**
     * An optional target instance to use instead of creating new instances
     */
    private transient E              instance;

    /**
     * A delegate mapper created from type information in <code>type</code>
     */
    private RecordMapper<R, E>       delegate;

    public DefaultRecordMapper(RecordType<R> rowType, Class<? extends E> type) {
        this(rowType, type, null);
    }

    DefaultRecordMapper(RecordType<R> rowType, Class<? extends E> type, E instance) {
        this.fields = rowType.fields();
        this.type = type;
        this.instance = instance;

        init();
    }

    private final void init() {

        // Arrays can be mapped easily
        if (type.isArray()) {
            delegate = new ArrayMapper();
            return;
        }

        // [#1470] Return a proxy if the supplied type is an interface
        if (Modifier.isAbstract(type.getModifiers())) {
            delegate = new ProxyMapper();
            return;
        }

        // [#1340] Allow for using non-public default constructors
        try {
            delegate = new MutablePOJOMapper(type.getDeclaredConstructor());
            return;
        }
        catch (NoSuchMethodException ignore) {}

        // [#1336] If no default constructor is present, check if there is a
        // "matching" constructor with the same number of fields as this record
        Constructor<E>[] constructors = (Constructor<E>[]) type.getDeclaredConstructors();

        // [#1837] If any java.beans.ConstructorProperties annotations are
        // present use those rather than matching constructors by the number of
        // arguments
        for (Constructor<E> constructor : constructors) {
            ConstructorProperties properties = constructor.getAnnotation(ConstructorProperties.class);

            if (properties != null) {
                delegate = new ImmutablePOJOMapperWithConstructorProperties(constructor, properties);
                return;
            }
        }

        // [#1837] Without ConstructorProperties, match constructors by matching
        // argument length
        for (Constructor<E> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            // Match the first constructor by parameter length
            if (parameterTypes.length == fields.length) {
                delegate = new ImmutablePOJOMapper(constructor, parameterTypes);
                return;
            }
        }

        throw new MappingException("No matching constructor found on type " + type + " for record " + this);
    }

    @Override
    public final E map(R record) {
        if (record == null) {
            return null;
        }

        try {
            return delegate.map(record);
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

        @Override
        public final E map(R record) {
            int size = record.size();
            Class<?> componentType = type.getComponentType();
            Object[] result = (Object[]) (instance != null
                ? instance
                : Array.newInstance(componentType, size));

            // Just as in Collection.toArray(Object[]), return a new array in case
            // sizes don't match
            if (size > result.length) {
                result = (Object[]) Array.newInstance(componentType, size);
            }

            for (int i = 0; i < size; i++) {
                result[i] = Convert.convert(record.getValue(i), componentType);
            }

            return (E) result;
        }
    }

    /**
     * Convert a record into an hash map proxy of a given type.
     * <p>
     * This is done for types that are not instanciable
     */
    private class ProxyMapper implements RecordMapper<R, E> {

        private final MutablePOJOMapper localDelegate;

        ProxyMapper() {
            this.localDelegate = new MutablePOJOMapper(null);
        }

        @Override
        public final E map(R record) {
            E previous = instance;

            try {
                instance = Reflect.on(HashMap.class).create().as(type);
                return localDelegate.map(record);
            }
            finally {
                instance = previous;
            }
        }
    }

    /**
     * Convert a record into a mutable POJO type
     * <p>
     * jOOQ's understanding of a mutable POJO is a Java type that has a default
     * constructor
     */
    private class MutablePOJOMapper implements RecordMapper<R, E> {

        private final Constructor<? extends E>         constructor;
        private final boolean                          useAnnotations;
        private final List<java.lang.reflect.Field>[]  members;
        private final List<java.lang.reflect.Method>[] methods;

        MutablePOJOMapper(Constructor<? extends E> constructor) {
            this.constructor = accessible(constructor);
            this.useAnnotations = hasColumnAnnotations(type);
            this.members = new List[fields.length];
            this.methods = new List[fields.length];

            for (int i = 0; i < fields.length; i++) {
                Field<?> field = fields[i];

                // Annotations are available and present
                if (useAnnotations) {
                    members[i] = getAnnotatedMembers(type, field.getName());
                    methods[i] = getAnnotatedSetters(type, field.getName());
                }

                // No annotations are present
                else {
                    members[i] = getMatchingMembers(type, field.getName());
                    methods[i] = getMatchingSetters(type, field.getName());
                }
            }
        }

        @Override
        public final E map(R record) {
            try {
                E result = instance != null ? instance : constructor.newInstance();

                for (int i = 0; i < fields.length; i++) {
                    for (java.lang.reflect.Field member : members[i]) {

                        // [#935] Avoid setting final fields
                        if ((member.getModifiers() & Modifier.FINAL) == 0) {
                            map(record, result, member, i);
                        }
                    }

                    for (java.lang.reflect.Method method : methods[i]) {
                        method.invoke(result, record.getValue(i, method.getParameterTypes()[0]));
                    }
                }

                return result;
            }
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record to " + type, e);
            }
        }

        private final void map(Record record, Object result, java.lang.reflect.Field member, int index) throws IllegalAccessException {
            Class<?> mType = member.getType();

            if (mType.isPrimitive()) {
                if (mType == byte.class) {
                    member.setByte(result, record.getValue(index, byte.class));
                }
                else if (mType == short.class) {
                    member.setShort(result, record.getValue(index, short.class));
                }
                else if (mType == int.class) {
                    member.setInt(result, record.getValue(index, int.class));
                }
                else if (mType == long.class) {
                    member.setLong(result, record.getValue(index, long.class));
                }
                else if (mType == float.class) {
                    member.setFloat(result, record.getValue(index, float.class));
                }
                else if (mType == double.class) {
                    member.setDouble(result, record.getValue(index, double.class));
                }
                else if (mType == boolean.class) {
                    member.setBoolean(result, record.getValue(index, boolean.class));
                }
                else if (mType == char.class) {
                    member.setChar(result, record.getValue(index, char.class));
                }
            }
            else {
                member.set(result, record.getValue(index, mType));
            }
        }
    }

    /**
     * Convert a record into an "immutable" POJO (final fields, "matching"
     * constructor).
     */
    private class ImmutablePOJOMapper implements RecordMapper<R, E> {

        private final Constructor<E> constructor;
        private final Class<?>[]     parameterTypes;

        public ImmutablePOJOMapper(Constructor<E> constructor, Class<?>[] parameterTypes) {
            this.constructor = accessible(constructor);
            this.parameterTypes = parameterTypes;
        }

        @Override
        public final E map(R record) {
            try {
                Object[] converted = Convert.convert(record.intoArray(), parameterTypes);
                return constructor.newInstance(converted);
            }
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record to " + type, e);
            }
        }
    }

    /**
     * Create an immutable POJO given a constructor and its associated JavaBeans
     * {@link ConstructorProperties}
     */
    private class ImmutablePOJOMapperWithConstructorProperties implements RecordMapper<R, E> {

        private final Constructor<E>                  constructor;
        private final Class<?>[]                      parameterTypes;
        private final Object[]                        parameterValues;
        private final List<String>                    propertyNames;
        private final boolean                         useAnnotations;
        private final List<java.lang.reflect.Field>[] members;
        private final java.lang.reflect.Method[]      methods;

        ImmutablePOJOMapperWithConstructorProperties(Constructor<E> constructor, ConstructorProperties properties) {
            this.constructor = constructor;
            this.propertyNames = Arrays.asList(properties.value());
            this.useAnnotations = hasColumnAnnotations(type);
            this.parameterTypes = constructor.getParameterTypes();
            this.parameterValues = new Object[parameterTypes.length];
            this.members = new List[fields.length];
            this.methods = new Method[fields.length];

            for (int i = 0; i < fields.length; i++) {
                Field<?> field = fields[i];

                // Annotations are available and present
                if (useAnnotations) {
                    members[i] = getAnnotatedMembers(type, field.getName());
                    methods[i] = getAnnotatedGetter(type, field.getName());
                }

                // No annotations are present
                else {
                    members[i] = getMatchingMembers(type, field.getName());
                    methods[i] = getMatchingGetter(type, field.getName());
                }
            }
        }

        @Override
        public final E map(R record) {
            try {
                for (int i = 0; i < fields.length; i++) {
                    for (java.lang.reflect.Field member : members[i]) {
                        int index = propertyNames.indexOf(member.getName());

                        if (index >= 0) {
                            parameterValues[index] = record.getValue(i);
                        }
                    }

                    if (methods[i] != null) {
                        String name = getPropertyName(methods[i].getName());
                        int index = propertyNames.indexOf(name);

                        if (index >= 0) {
                            parameterValues[index] = record.getValue(i);
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
}
