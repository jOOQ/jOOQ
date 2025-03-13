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

import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static org.jooq.ContextConverter.scoped;
import static org.jooq.Decfloat.decfloat;
import static org.jooq.impl.Internal.arrayType;
import static org.jooq.impl.Internal.converterContext;
import static org.jooq.impl.Tools.configuration;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.tools.StringUtils.leftPad;
import static org.jooq.tools.reflect.Reflect.accessible;
import static org.jooq.tools.reflect.Reflect.wrapper;
import static org.jooq.types.Unsigned.ubyte;
import static org.jooq.types.Unsigned.uint;
import static org.jooq.types.Unsigned.ulong;
import static org.jooq.types.Unsigned.ushort;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

// ...
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.ConverterProvider;
import org.jooq.Data;
import org.jooq.Converters.UnknownType;
import org.jooq.Decfloat;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.JSONFormat;
import org.jooq.Param;
import org.jooq.QualifiedRecord;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.XML;
import org.jooq.XMLFormat;
import org.jooq.exception.DataTypeException;
import org.jooq.tools.Ints;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.Longs;
import org.jooq.tools.jdbc.MockArray;
import org.jooq.tools.jdbc.MockResultSet;
import org.jooq.tools.json.ContainerFactory;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONParser;
import org.jooq.tools.json.ParseException;
import org.jooq.tools.reflect.Reflect;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;
import org.jooq.util.postgres.PostgresUtils;
import org.jooq.util.xml.jaxb.InformationSchema;

import org.jetbrains.annotations.NotNull;

import jakarta.xml.bind.JAXB;

/**
 * Utility methods for type conversions
 * <p>
 * This class provides less type-safety than the general jOOQ API methods. For
 * instance, it accepts arbitrary {@link Converter} objects in methods like
 * {@link #convert(Collection, Converter)} and {@link #convert(Object, Class)},
 * trying to retrofit the <code>Object</code> argument to the type provided in
 * {@link Converter#fromType()} before performing the actual conversion.
 * <p>
 * For better future configurability via {@link ConverterProvider} etc, this
 * type has been moved to jOOQ's internals in jOOQ 3.15.0 ([#11898]) and is no
 * longer accessible to users.
 *
 * @author Lukas Eder
 */
final class Convert {

    private static final JooqLogger log          = JooqLogger.getLogger(Convert.class);

    /**
     * All string values that can be transformed into a boolean
     * <code>true</code> value.
     */
    static final Set<String> TRUE_VALUES;

    /**
     * All string values that can be transformed into a boolean <code>false</code> value.
     */
    static final Set<String> FALSE_VALUES;

    /**
     * A UUID pattern for UUIDs with or without hyphens
     */
    private static final Pattern UUID_PATTERN = Pattern.compile("(\\p{XDigit}{8})-?(\\p{XDigit}{4})-?(\\p{XDigit}{4})-?(\\p{XDigit}{4})-?(\\p{XDigit}{12})");

    static {
        Set<String> trueValues = new HashSet<>();
        Set<String> falseValues = new HashSet<>();

        trueValues.add("1");
        trueValues.add("1.0");
        trueValues.add("1E0");
        trueValues.add("1e0");
        trueValues.add("y");
        trueValues.add("Y");
        trueValues.add("yes");
        trueValues.add("YES");
        trueValues.add("true");
        trueValues.add("TRUE");
        trueValues.add("t");
        trueValues.add("T");
        trueValues.add("on");
        trueValues.add("ON");
        trueValues.add("enabled");
        trueValues.add("ENABLED");

        falseValues.add("0");
        falseValues.add("0.0");
        falseValues.add("0E0");
        falseValues.add("0e0");
        falseValues.add("n");
        falseValues.add("N");
        falseValues.add("no");
        falseValues.add("NO");
        falseValues.add("false");
        falseValues.add("FALSE");
        falseValues.add("f");
        falseValues.add("F");
        falseValues.add("off");
        falseValues.add("OFF");
        falseValues.add("disabled");
        falseValues.add("DISABLED");

        TRUE_VALUES = Collections.unmodifiableSet(trueValues);
        FALSE_VALUES = Collections.unmodifiableSet(falseValues);
    }

    private static final class _JSON {

        /**
         * The Jackson ObjectMapper or Gson instance, if available.
         */
        private static final Object JSON_MAPPER;

        /**
         * The Jackson ObjectMapper::readValue or Gson::fromJson method, if available.
         */
        private static final Method JSON_READ_METHOD;

        /**
         * The Jackson ObjectMapper::writeValueToString or Gson::toJson method, if available.
         */
        private static final Method JSON_WRITE_METHOD;

        static {
            Object jsonMapper = null;
            Method jsonReadMethod = null;
            Method jsonWriteMethod = null;

            try {
                Class<?> klass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");

                try {
                    Class<?> kotlin = Class.forName("com.fasterxml.jackson.module.kotlin.ExtensionsKt");
                    jsonMapper = kotlin.getMethod("jacksonObjectMapper").invoke(kotlin);
                    log.debug("Jackson kotlin module is available");
                }
                catch (Exception e) {
                    jsonMapper = klass.getDeclaredConstructor().newInstance();
                    log.debug("Jackson kotlin module is not available");
                }

                try {
                    Class<?> jtmc = Class.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
                    Object jtm = jtmc.getDeclaredConstructor().newInstance();
                    Reflect.on(jsonMapper).call("registerModule", jtm);
                }
                catch (Exception e) {
                    log.debug("JavaTimeModule is not available");
                }

                jsonReadMethod = klass.getMethod("readValue", String.class, Class.class);
                jsonWriteMethod = klass.getMethod("writeValueAsString", Object.class);
                log.debug("Jackson is available");
            }
            catch (Exception e1) {
                log.debug("Jackson not available", e1.getMessage());

                try {
                    Class<?> klass = Class.forName("com.google.gson.Gson");

                    jsonMapper = klass.getDeclaredConstructor().newInstance();
                    jsonReadMethod = klass.getMethod("fromJson", String.class, Class.class);
                    jsonWriteMethod = klass.getMethod("toJson", Object.class);
                    log.debug("Gson is available");
                }
                catch (Exception e2) {
                    log.debug("Gson not available", e2.getMessage());
                }
            }

            JSON_MAPPER = jsonMapper;
            JSON_READ_METHOD = jsonReadMethod;
            JSON_WRITE_METHOD = jsonWriteMethod;
        }
    }

    private static final class _XML {

        /**
         * Whether a JAXB implementation is available.
         */
        private static final boolean JAXB_AVAILABLE;

        static {
            boolean jaxbAvailable = false;

            try {
                JAXB.marshal(new InformationSchema(), new StringWriter());
                jaxbAvailable = true;
                log.debug("JAXB is available");
            }

            // [#10145] Depending on whether jOOQ is modularised or not, this can also
            //          be a NoClassDefFoundError.
            catch (Throwable t) {
                log.debug("JAXB not available", t.getMessage());
            }

            JAXB_AVAILABLE = jaxbAvailable;
        }
    }

    /**
     * Convert an array of values to a matching data type
     * <p>
     * This converts <code>values[i]</code> to <code>fields[i].getType()</code>
     */
    static final Object[] convert(Object[] values, Field<?>[] fields) {
        if (values == null)
            return null;

        // [#1005] Convert values from the <code>VALUES</code> clause to appropriate
        // values as specified by the <code>INTO</code> clause's column list.
        Object[] result = new Object[values.length];

        // TODO [#1008] Should fields be cast? Check this with
        // appropriate integration tests
        for (int i = 0; i < values.length; i++)
            if (values[i] instanceof Field<?>)
                result[i] = values[i];
            else
                result[i] = convert(values[i], fields[i].getType());

        return result;
    }

    /**
     * Convert an array of values to a matching data type
     * <p>
     * This converts <code>values[i]</code> to <code>types[i]</code>
     */
    static final Object[] convert(Object[] values, Class<?>[] types) {
        if (values == null)
            return null;

        // [#1005] Convert values from the <code>VALUES</code> clause to appropriate
        // values as specified by the <code>INTO</code> clause's column list.
        Object[] result = new Object[values.length];

        // TODO [#1008] Should fields be cast? Check this with
        // appropriate integration tests
        for (int i = 0; i < values.length; i++)
            if (values[i] instanceof Field<?>)
                result[i] = values[i];
            else
                result[i] = convert(values[i], types[i]);

        return result;
    }
    /**
     * Convert an array into another one using a converter
     * <p>
     * This uses {@link #convertArray(Object[], Class)} to convert the array to
     * an array of {@link Converter#fromType()} first, before converting that
     * array again to {@link Converter#toType()}
     *
     * @param from The array to convert
     * @param converter The data type converter
     * @return A converted array
     * @throws DataTypeException - When the conversion is not possible
     */
    @SuppressWarnings("unchecked")
    static final <U> U[] convertArray(Object[] from, Converter<?, ? extends U> converter) throws DataTypeException {
        if (from == null)
            return null;

        // [#18052] Use wrapper types here, because we guarantee a U[] array typed result (i.e. Object[])
        Object[] arrayOfT = (Object[]) convertArray(from, wrapper(converter.fromType()));
        Object[] arrayOfU = (Object[]) Array.newInstance(wrapper(converter.toType()), from.length);

        for (int i = 0; i < arrayOfT.length; i++)
            arrayOfU[i] = convert(arrayOfT[i], converter);

        return (U[]) arrayOfU;
    }

    /**
     * Convert an array into another one by these rules
     * <p>
     * <ul>
     * <li>If <code>toClass</code> is not an array class, then make it an array
     * class first</li>
     * <li>If <code>toClass</code> is an array class, then create an instance
     * from it, and convert all elements in the <code>from</code> array one by
     * one, using {@link #convert(Object, Class)}</li>
     * </ul>
     *
     * @param from The array to convert
     * @param toClass The target array type
     * @return A converted array
     * @throws DataTypeException - When the conversion is not possible
     */
    @SuppressWarnings("unchecked")
    static final Object convertArray(Object[] from, Class<?> toClass) throws DataTypeException {
        if (from == null)
            return null;
        else if (!equalArrayDegree(from.getClass(), toClass))
            return convertArray(from, arrayType(toClass));
        else if (toClass == from.getClass())
            return from;
        else {
            final Class<?> toComponentType = toClass.getComponentType();

            if (from.length == 0)
                return Arrays.copyOf(from, from.length, (Class<? extends Object[]>) toClass);
            else if (from[0] != null && from[0].getClass() == toComponentType)
                return Arrays.copyOf(from, from.length, (Class<? extends Object[]>) toClass);
            else if (toComponentType == byte.class) {
                final byte[] result = (byte[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = (byte) convert(from[i], toComponentType);

                return result;
            }
            else if (toComponentType == short.class) {
                final short[] result = (short[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = (short) convert(from[i], toComponentType);

                return result;
            }
            else if (toComponentType == int.class) {
                final int[] result = (int[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = (int) convert(from[i], toComponentType);

                return result;
            }
            else if (toComponentType == long.class) {
                final long[] result = (long[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = (long) convert(from[i], toComponentType);

                return result;
            }
            else if (toComponentType == char.class) {
                final char[] result = (char[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = (char) convert(from[i], toComponentType);

                return result;
            }
            else if (toComponentType == boolean.class) {
                final boolean[] result = (boolean[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = (boolean) convert(from[i], toComponentType);

                return result;
            }
            else if (toComponentType == float.class) {
                final float[] result = (float[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = (float) convert(from[i], toComponentType);

                return result;
            }
            else if (toComponentType == double.class) {
                final double[] result = (double[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = (double) convert(from[i], toComponentType);

                return result;
            }
            else {
                final Object[] result = (Object[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = convert(from[i], toComponentType);

                return result;
            }
        }
    }

    private static boolean equalArrayDegree(Class<?> c1, Class<?> c2) {
        Class<?> ct1 = c1.getComponentType();
        Class<?> ct2 = c2.getComponentType();

        // [#18059] The check isn't symmetric as we'll wrap only the right type
        if (ct1 == null)
            return true;

        // [#18059] binary data of type byte[] is not considered an array type
        else if (ct2 == null && ct1 == byte.class)
            return true;
        else if (ct2 == null)
            return false;
        else
            return equalArrayDegree(ct1, ct2);
    }

    static final <U> U[] convertCollection(Collection from, Class<? extends U[]> to) {
        return new ConvertAll<U[]>(to).from(from, converterContext());
    }

    /**
     * Convert an object to a type.
     *
     * @param from The source object
     * @param converter The data type converter
     * @return The target type object
     * @throws DataTypeException - When the conversion is not possible
     */
    @SuppressWarnings("unchecked")
    static final <U> U convert(Object from, Converter<?, ? extends U> converter) throws DataTypeException {

        // [#5865] [#6799] [#11099] This leads to significant performance improvements especially when
        //                          used from MockResultSet, which is likely to host IdentityConverters
        if (converter instanceof IdentityConverter)
            return (U) from;
        else
            return convert0(from, converter);
    }

    /**
     * Conversion type-safety
     */
    @SuppressWarnings("unchecked")
    private static final <T, U> U convert0(Object from, Converter<T, ? extends U> converter) throws DataTypeException {
        Class<T> fromType = converter.fromType();

        if (fromType == Object.class)
            return scoped(converter).from((T) from, converterContext());

        ConvertAll<T> convertAll = new ConvertAll<>(fromType);
        return scoped(converter).from(convertAll.from(from, converterContext()), converterContext());
    }

    /**
     * Convert an object to a type. These are the conversion rules:
     * <ul>
     * <li><code>null</code> is always converted to <code>null</code>, or the
     * primitive default value, or {@link Optional#empty()}, regardless of the
     * target type.</li>
     * <li>Identity conversion (converting a value to its own type) is always
     * possible.</li>
     * <li>Primitive types can be converted to their wrapper types and vice
     * versa</li>
     * <li>All types can be converted to <code>String</code></li>
     * <li>All types can be converted to <code>Object</code></li>
     * <li>All <code>Number</code> types can be converted to other
     * <code>Number</code> types</li>
     * <li>All <code>Number</code> or <code>String</code> types can be converted
     * to <code>Boolean</code>. Possible (case-insensitive) values for
     * <code>true</code>:
     * <ul>
     * <li><code>1</code></li>
     * <li><code>1.0</code></li>
     * <li><code>y</code></li>
     * <li><code>yes</code></li>
     * <li><code>true</code></li>
     * <li><code>on</code></li>
     * <li><code>enabled</code></li>
     * </ul>
     * <p>
     * Possible (case-insensitive) values for <code>false</code>:
     * <ul>
     * <li><code>0</code></li>
     * <li><code>0.0</code></li>
     * <li><code>n</code></li>
     * <li><code>no</code></li>
     * <li><code>false</code></li>
     * <li><code>off</code></li>
     * <li><code>disabled</code></li>
     * </ul>
     * <p>
     * All other values evaluate to <code>null</code></li>
     * <li>All {@link java.util.Date} subtypes ({@link Date}, {@link Time},
     * {@link Timestamp}), as well as most {@link Temporal} subtypes (
     * {@link LocalDate}, {@link LocalTime}, {@link LocalDateTime},
     * {@link OffsetTime}, {@link OffsetDateTime}, as well as {@link Instant})
     * can be converted into each other.</li>
     * <li>All <code>String</code> types can be converted into {@link URI},
     * {@link URL} and {@link File}</li>
     * <li><code>byte[]</code> and {@link ByteBuffer} can be converted into one
     * another</li>
     * <li><code>byte[]</code> can be converted into <code>String</code>, using
     * the platform's default charset</li>
     * <li><code>Object[]</code> can be converted into any other array type, if
     * array elements can be converted, too</li>
     * <li>All types can be converted into types containing a single argument
     * constructor whose argument is a type that can be converted to according
     * to the above rules.</li>
     * <li><b>All other combinations that are not listed above will result in a
     * {@link DataTypeException}</b></li>
     * </ul>
     *
     * @param from The object to convert
     * @param toClass The target type
     * @return The converted object
     * @throws DataTypeException - When the conversion is not possible
     */
    @SuppressWarnings("unchecked")
    static final <T> T convert(Object from, Class<? extends T> toClass) throws DataTypeException {
        if (from != null && from.getClass() == toClass)
            return (T) from;
        else
            return convert0(from, new ConvertAll<T>(toClass));
    }

    /**
     * Convert a collection of objects to a list of <code>T</code>, using
     * {@link #convert(Object, Class)}
     *
     * @param collection The list of objects
     * @param type The target type
     * @return The list of converted objects
     * @throws DataTypeException - When the conversion is not possible
     * @see #convert(Object, Class)
     */
    static final <T> List<T> convert(Collection<?> collection, Class<? extends T> type) throws DataTypeException {
        return convert(collection, new ConvertAll<>(type));
    }

    /**
     * Convert a collection of objects to a list of <code>T</code>, using
     * {@link #convert(Object, Converter)}
     *
     * @param collection The collection of objects
     * @param converter The data type converter
     * @return The list of converted objects
     * @throws DataTypeException - When the conversion is not possible
     * @see #convert(Object, Converter)
     */
    static final <U> List<U> convert(Collection<?> collection, Converter<?, ? extends U> converter) throws DataTypeException {
        return convert0(collection, converter);
    }

    /**
     * Type safe conversion
     */
    private static final <T, U> List<U> convert0(Collection<?> collection, Converter<T, ? extends U> converter) throws DataTypeException {
        ConvertAll<T> all = new ConvertAll<>(converter.fromType());
        List<U> result = new ArrayList<>(collection.size());

        for (Object o : collection)
            result.add(convert(all.from(o, converterContext()), converter));

        return result;
    }

    /**
     * No instances
     */
    private Convert() {}

    /**
     * The converter to convert them all.
     */
    private static final class ConvertAll<U> extends AbstractContextConverter<Object, U> {

        private final Class<? extends U> toClass;

        @SuppressWarnings("unchecked")
        ConvertAll(Class<? extends U> toClass) {
            super(Object.class, (Class<U>) toClass);

            this.toClass = toClass;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public U from(Object from, ConverterContext scope) {
            if (from == null) {

                // [#936] If types are converted to primitives, the result must not
                // be null. Return the default value instead
                if (toClass.isPrimitive()) {

                    // Characters default to the "zero" character
                    if (toClass == char.class)
                        return (U) Character.valueOf((char) 0);

                    // All others can be converted from (int) 0
                    else
                        return convert(0, toClass);
                }

                else if (toClass == Optional.class)
                    return (U) Optional.empty();
                else
                    return null;
            }
            else {
                final Class<?> fromClass = from.getClass();
                final Class<?> wrapperTo;
                final Class<?> wrapperFrom;

                // No conversion
                if (toClass == fromClass)
                    return (U) from;

                // [#2535] Simple up-casting can be done early
                // [#1155] ... up-casting includes (toClass == Object.class)
                else if (toClass.isAssignableFrom(fromClass))
                    return (U) from;

                // [#6790] No conversion for primitive / wrapper conversions
                else if ((wrapperTo = wrapper(toClass)) == (wrapperFrom = wrapper(fromClass)))
                    return (U) from;

                // [#12557] Anything can be unwrapped from Optional
                else if (fromClass == Optional.class)
                    return from(((Optional) from).orElse(null), scope);

                // Regular checks
                else if (fromClass == byte[].class && !toClass.isArray()) {

                    // [#5824] UUID's most significant bits in byte[] are first
                    if (toClass == UUID.class) {
                        ByteBuffer b = ByteBuffer.wrap((byte[]) from);
                        long mostSigBits = b.getLong();
                        long leastSigBits = b.getLong();
                        return (U) new UUID(mostSigBits, leastSigBits);
                    }

                    // [#11700] [#11772] R2DBC uses ByteBuffer instead of byte[]
                    else if (toClass == ByteBuffer.class)
                        return (U) ByteBuffer.wrap((byte[]) from);

                    // [#5569] Binary data is expected to be in JVM's default encoding
                    else
                        return convert(new String((byte[]) from), toClass);
                }
                else if (fromClass.isArray()) {
                    Object[] fromArray = toObjectArray(from);

                    // [#3062] [#5796] Default collections if no specific collection type was requested
                    if (Collection.class.isAssignableFrom(toClass) &&
                            toClass.isAssignableFrom(ArrayList.class))
                        return (U) new ArrayList<>(Arrays.asList(fromArray));
                    else if (Collection.class.isAssignableFrom(toClass) &&
                            toClass.isAssignableFrom(LinkedHashSet.class))
                        return (U) new LinkedHashSet<>(Arrays.asList(fromArray));

                    // [#3443] Conversion from Object[] to JDBC Array
                    else if (toClass == java.sql.Array.class)
                        return (U) new MockArray(null, fromArray, fromClass);
                    else
                        return (U) convertArray(fromArray, toClass);
                }

                // [#6454] From JDBC Array to any other type of collection
                else if (java.sql.Array.class.isAssignableFrom(fromClass)) {
                    try {
                        return (U) convertArray((Object[]) ((java.sql.Array) from).getArray(), toClass);
                    }
                    catch (SQLException e) {
                        throw new DataTypeException("Cannot convert from JDBC array: ", e);
                    }
                }

                // [#12308] Result serialised as XML or JSON string
                else if (Result.class.isAssignableFrom(fromClass) && toClass == String.class) {
                    switch (emulateMultiset(configuration((Result<?>) from))) {
                        case XML:
                            return (U) ((Result<?>) from).formatXML(XMLFormat.DEFAULT_FOR_RECORDS);

                        case JSON:
                        case JSONB:
                            return (U) ((Result<?>) from).formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);
                    }
                }
                else if (Result.class.isAssignableFrom(fromClass) && toClass == byte[].class) {
                    return (U) convert(convert(from, String.class), byte[].class);
                }

                // [#11560] Results wrapped in ResultSet
                else if (Result.class.isAssignableFrom(fromClass) && toClass == ResultSet.class) {
                    return (U) new MockResultSet((Result<?>) from);
                }

                // [#3062] Default collections if no specific collection type was requested
                else if (Collection.class.isAssignableFrom(fromClass)
                    
                    && (toClass == java.sql.Array.class || toClass.isArray())
                ) {
                    Object[] fromArray = ((Collection<?>) from).toArray();

                    // [#3443] [#10704] Conversion from Object[] to JDBC Array
                    if (toClass == java.sql.Array.class)
                        return (U) new MockArray(null, fromArray, fromClass);
                    else
                        return (U) convertArray(fromArray, toClass);
                }
                else if (toClass == Optional.class)
                    return (U) Optional.of(from);

                // All types can be converted into String
                else if (toClass == String.class) {
                    try {
                        if (from instanceof EnumType e)
                            return (U) e.getLiteral();

                        // [#17497] Avoid potentially costly Data::toString call
                        else if (from instanceof Data d)
                            return (U) d.data();

                        // [#18157] In case the driver (e.g. oracle-r2dbc) can't do the conversion itself
                        else if (from instanceof SQLXML s)
                            return (U) s.getString();

                        else
                            return (U) from.toString();
                    }
                    catch (SQLException e) {
                        throw new DataTypeException("Cannot convert to String: ", e);
                    }
                }

                // [#5569] It should be possible, at least, to convert an empty string to an empty (var)binary.
                else if (toClass == byte[].class) {

                    // [#5824] UUID's most significant bits in byte[] are first
                    if (from instanceof UUID u) {
                        ByteBuffer b = ByteBuffer.wrap(new byte[16]);
                        b.putLong(u.getMostSignificantBits());
                        b.putLong(u.getLeastSignificantBits());
                        return (U) b.array();
                    }
                    else if (from instanceof ByteBuffer b)
                        return (U) b.array();
                    else
                        return (U) from.toString().getBytes();
                }

                // Various number types are converted between each other via String
                else if (wrapperTo == Byte.class) {
                    if (Number.class.isAssignableFrom(fromClass))
                        return (U) Byte.valueOf(((Number) from).byteValue());

                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? Byte.valueOf((byte) 1) : Byte.valueOf((byte) 0));

                    try {
                        String fromString = from.toString().trim();
                        Integer asInt = Ints.tryParse(fromString);
                        return (U) Byte.valueOf(asInt != null ? asInt.byteValue() : new BigDecimal(fromString).byteValue());
                    }
                    catch (NumberFormatException e) {
                        return Reflect.initValue(toClass);
                    }
                }
                else if (wrapperTo == Short.class) {
                    if (Number.class.isAssignableFrom(fromClass))
                        return (U) Short.valueOf(((Number) from).shortValue());

                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? Short.valueOf((short) 1) : Short.valueOf((short) 0));

                    try {
                        String fromString = from.toString().trim();
                        Integer asInt = Ints.tryParse(fromString);
                        return (U) Short.valueOf(asInt != null ? asInt.shortValue() : new BigDecimal(fromString).shortValue());
                    }
                    catch (NumberFormatException e) {
                        return Reflect.initValue(toClass);
                    }
                }
                else if (wrapperTo == Integer.class) {
                    if (Number.class.isAssignableFrom(fromClass))
                        return (U) Integer.valueOf(((Number) from).intValue());

                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? Integer.valueOf(1) : Integer.valueOf(0));

                    try {
                        String fromString = from.toString().trim();
                        Integer asInt = Ints.tryParse(fromString);
                        return (U) Integer.valueOf(asInt != null ? asInt.intValue() : new BigDecimal(fromString).intValue());
                    }
                    catch (NumberFormatException e) {
                        return Reflect.initValue(toClass);
                    }
                }
                else if (wrapperTo == Long.class) {
                    if (Number.class.isAssignableFrom(fromClass))
                        return (U) Long.valueOf(((Number) from).longValue());

                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? Long.valueOf(1L) : Long.valueOf(0L));

                    if (wrapperFrom == Year.class)
                        return (U) (Long) (long) ((Year) from).getValue();

                    if (java.util.Date.class.isAssignableFrom(fromClass))
                        return (U) Long.valueOf(((java.util.Date) from).getTime());

                    if (Temporal.class.isAssignableFrom(fromClass))
                        return (U) Long.valueOf(millis((Temporal) from));

                    try {
                        String fromString = from.toString().trim();
                        Long asLong = Longs.tryParse(fromString);
                        return (U) Long.valueOf(asLong != null ? asLong.longValue() : new BigDecimal(fromString).longValue());
                    }
                    catch (NumberFormatException e) {
                        return Reflect.initValue(toClass);
                    }
                }

                // ... this also includes unsigned number types
                else if (toClass == UByte.class) {
                    try {
                        if (Number.class.isAssignableFrom(fromClass))
                            return (U) ubyte(((Number) from).shortValue());

                        if (wrapperFrom == Boolean.class)
                            return (U) (((Boolean) from) ? ubyte(1) : ubyte(0));

                        String fromString = from.toString().trim();
                        Integer asInt = Ints.tryParse(fromString);
                        return (U) ubyte(asInt != null ? asInt.shortValue() : new BigDecimal(fromString).shortValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == UShort.class) {
                    try {
                        if (Number.class.isAssignableFrom(fromClass))
                            return (U) ushort(((Number) from).intValue());

                        if (wrapperFrom == Boolean.class)
                            return (U) (((Boolean) from) ? ushort(1) : ushort(0));

                        String fromString = from.toString().trim();
                        Integer asInt = Ints.tryParse(fromString);
                        return (U) ushort(asInt != null ? asInt.intValue() : new BigDecimal(fromString).intValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == UInteger.class) {
                    try {
                        if (Number.class.isAssignableFrom(fromClass))
                            return (U) uint(((Number) from).longValue());

                        if (wrapperFrom == Boolean.class)
                            return (U) (((Boolean) from) ? uint(1) : uint(0));

                        String fromString = from.toString().trim();
                        Long asLong = Longs.tryParse(fromString);
                        return (U) uint(asLong != null ? asLong.longValue() : new BigDecimal(fromString).longValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == ULong.class) {
                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? ulong(1) : ulong(0));

                    if (java.util.Date.class.isAssignableFrom(fromClass))
                        return (U) ulong(((java.util.Date) from).getTime());

                    if (Temporal.class.isAssignableFrom(fromClass))
                        return (U) ulong(millis((Temporal) from));

                    try {
                        String fromString = from.toString().trim();
                        // tryParse() will return null in case of overflow
                        Long asLong = Longs.tryParse(fromString);
                        return asLong != null ? (U) ulong(asLong.longValue()) : (U) ulong(new BigDecimal(fromString).toBigInteger());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }

                // ... and floating point / fixed point types
                else if (wrapperTo == Float.class) {
                    if (Number.class.isAssignableFrom(fromClass))
                        return (U) Float.valueOf(((Number) from).floatValue());

                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? Float.valueOf(1.0f) : Float.valueOf(0.0f));

                    try {
                        return (U) Float.valueOf(from.toString().trim());
                    }
                    catch (NumberFormatException e) {
                        return Reflect.initValue(toClass);
                    }
                }
                else if (wrapperTo == Double.class) {
                    if (Number.class.isAssignableFrom(fromClass))
                        return (U) Double.valueOf(((Number) from).doubleValue());

                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? Double.valueOf(1.0) : Double.valueOf(0.0));

                    try {
                        return (U) Double.valueOf(from.toString().trim());
                    }
                    catch (NumberFormatException e) {
                        return Reflect.initValue(toClass);
                    }
                }
                else if (toClass == BigDecimal.class) {
                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? BigDecimal.ONE : BigDecimal.ZERO);

                    try {
                        return (U) new BigDecimal(from.toString().trim());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == BigInteger.class) {
                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? BigInteger.ONE : BigInteger.ZERO);

                    try {
                        return (U) new BigDecimal(from.toString().trim()).toBigInteger();
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == Decfloat.class) {
                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? decfloat("1") : decfloat("0"));

                    return (U) decfloat(from.toString());
                }
                else if (toClass == Year.class) {
                    if (Number.class.isAssignableFrom(wrapperFrom))
                        return (U) Year.of((((Number) from).intValue()));

                    try {
                        return (U) Year.parse(from.toString().trim());
                    }
                    catch (DateTimeParseException e) {
                        return null;
                    }
                }
                else if (wrapperTo == Boolean.class) {
                    String s = from.toString().toLowerCase().trim();

                    if (TRUE_VALUES.contains(s))
                        return (U) Boolean.TRUE;
                    else if (FALSE_VALUES.contains(s))
                        return (U) Boolean.FALSE;
                    else
                        return (U) (toClass == Boolean.class ? null : false);
                }
                else if (wrapperTo == Character.class) {
                    if (wrapperFrom == Boolean.class)
                        return (U) (((Boolean) from) ? Character.valueOf('1') : Character.valueOf('0'));

                    if (from.toString().length() < 1)
                        return Reflect.initValue(toClass);

                    return (U) Character.valueOf(from.toString().charAt(0));
                }

                // URI types can be converted from strings
                else if (fromClass == String.class && toClass == URL.class) {
                    try {
                        return (U) new URI(from.toString()).toURL();
                    }
                    catch (Exception e) {
                        return null;
                    }
                }

                // Date types can be converted among each other
                else if (java.util.Date.class.isAssignableFrom(fromClass)) {

                    // [#12225] Avoid losing precision if possible
                    if (Timestamp.class == fromClass)
                        if (LocalDateTime.class == toClass)
                            return (U) ((Timestamp) from).toLocalDateTime();
                        else
                            return toDate(((Timestamp) from).getTime(), ((Timestamp) from).getNanos(), toClass);
                    else if (Date.class == fromClass && LocalDate.class == toClass)
                        return (U) ((Date) from).toLocalDate();
                    else if (Time.class == fromClass && LocalTime.class == toClass)
                        return (U) ((Time) from).toLocalTime();
                    else
                        return toDate(((java.util.Date) from).getTime(), toClass);
                }
                else if (Temporal.class.isAssignableFrom(fromClass)) {

                    // [#12225] Avoid losing precision if possible
                    if (LocalDateTime.class == fromClass && Timestamp.class == toClass)
                        return (U) Timestamp.valueOf((LocalDateTime) from);
                    else if (LocalDateTime.class == fromClass && Temporal.class.isAssignableFrom(toClass))
                        return toDate(((LocalDateTime) from).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), ((LocalDateTime) from).getNano(), toClass);
                    else if (LocalDate.class == fromClass && Date.class == toClass)
                        return (U) Date.valueOf((LocalDate) from);
                    else if (LocalTime.class == fromClass && Time.class == toClass)
                        return (U) Time.valueOf((LocalTime) from);
                    else if (OffsetDateTime.class == fromClass && (Timestamp.class == toClass || Temporal.class.isAssignableFrom(toClass)))
                        return toDate(((OffsetDateTime) from).toInstant().toEpochMilli(), ((OffsetDateTime) from).getNano(), toClass);
                    else if (Instant.class == fromClass && (Timestamp.class == toClass || Temporal.class.isAssignableFrom(toClass)))
                        return toDate(((Instant) from).toEpochMilli(), ((Instant) from).getNano(), toClass);
                    else
                        return toDate(convert(from, Long.class), toClass);
                }

                // Long may also be converted into a date type
                else if (wrapperFrom == Long.class && java.util.Date.class.isAssignableFrom(toClass)) {
                    return toDate((Long) from, toClass);
                }
                else if (wrapperFrom == Long.class && Temporal.class.isAssignableFrom(toClass)) {
                    return toDate((Long) from, toClass);
                }

                // [#1501] Strings can be converted to java.sql.Date
                else if (fromClass == String.class && toClass == java.sql.Date.class) {
                    try {
                        return (U) java.sql.Date.valueOf(patchIso8601Date((String) from));
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#1501] Strings can be converted to java.sql.Date
                else if (fromClass == String.class && toClass == java.sql.Time.class) {
                    try {
                        return (U) java.sql.Time.valueOf(patchFractionalSeconds(patchIso8601Time((String) from)));
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#1501] Strings can be converted to java.sql.Date
                else if (fromClass == String.class && toClass == java.sql.Timestamp.class) {
                    try {
                        return (U) java.sql.Timestamp.valueOf(patchIso8601Timestamp((String) from, false));
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }
                else if (fromClass == String.class && toClass == LocalDate.class) {
                    String s = patchIso8601Date((String) from);

                    // Try "lenient" ISO date formats first
                    try {
                        return (U) java.sql.Date.valueOf(s).toLocalDate();
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) LocalDate.parse(s);
                        }
                        catch (DateTimeParseException e2) {
                            return null;
                        }
                    }
                }

                else if (fromClass == String.class && toClass == LocalTime.class) {
                    try {
                        return (U) LocalTime.parse(patchIso8601Time((String) from));
                    }
                    catch (DateTimeParseException e2) {
                        return null;
                    }
                }

                else if (fromClass == String.class && toClass == OffsetTime.class) {

                    // Try "local" ISO date formats first
                    try {
                        return (U) java.sql.Time.valueOf((String) from).toLocalTime().atOffset(OffsetTime.now().getOffset());
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) OffsetTime.parse((String) from);
                        }
                        catch (DateTimeParseException e2) {
                            return null;
                        }
                    }
                }

                else if (fromClass == String.class && toClass == LocalDateTime.class) {
                    try {
                        return (U) LocalDateTime.parse(patchIso8601Timestamp((String) from, true));
                    }
                    catch (DateTimeParseException e2) {
                        return null;
                    }
                }

                else if (fromClass == String.class && toClass == OffsetDateTime.class) {

                    // Try "local" ISO date formats first
                    try {
                        return (U) java.sql.Timestamp.valueOf((String) from).toLocalDateTime().atZone(ZoneId.systemDefault()).toOffsetDateTime();
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) OffsetDateTime.parse(patchIso8601Timestamp((String) from, true));
                        }
                        catch (DateTimeParseException e2) {
                            return null;
                        }
                    }
                }

                else if (fromClass == String.class && toClass == Instant.class) {

                    // Try "local" ISO date formats first
                    try {
                        return (U) java.sql.Timestamp.valueOf((String) from).toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant();
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) OffsetDateTime.parse(patchIso8601Timestamp((String) from, true)).toInstant();
                        }
                        catch (DateTimeParseException e2) {
                            return null;
                        }
                    }
                }

                // [#14437] [#14713] Interval conversions
                else if (fromClass == String.class && toClass == YearToMonth.class) {

                    // Try our own standard SQL implementation first
                    YearToMonth r = YearToMonth.valueOf((String) from);
                    if (r != null)
                        return (U) r;

                    // If that failed, try the H2 specific format
                    if (((String) from).startsWith("INTERVAL")) {
                        try {
                            r = ((Param<YearToMonth>) scope.dsl().parser().parseField((String) from)).getValue();
                            return (U) r;
                        }
                        catch (Exception ignore) {}
                    }

                    // If that failed, try the PostgreSQL specific formats
                    try {
                        return (U) PostgresUtils.toYearToMonth(from);
                    }
                    catch (Exception e) {
                        return null;
                    }
                }
                else if (fromClass == String.class && toClass == DayToSecond.class) {

                    // Try our own standard SQL implementation first
                    DayToSecond r = DayToSecond.valueOf((String) from);
                    if (r != null)
                        return (U) r;

                    // If that failed, try the H2 specific format
                    if (((String) from).startsWith("INTERVAL")) {
                        try {
                            r = ((Param<DayToSecond>) scope.dsl().parser().parseField((String) from)).getValue();
                            return (U) r;
                        }
                        catch (Exception ignore) {}
                    }

                    // If that failed, try the PostgreSQL specific formats
                    try {
                        return (U) PostgresUtils.toDayToSecond(from);
                    }
                    catch (Exception e) {
                        return null;
                    }
                }
                else if (fromClass == String.class && toClass == YearToSecond.class) {

                    // Try our own standard SQL implementation first
                    YearToSecond r = YearToSecond.valueOf((String) from);
                    if (r != null)
                        return (U) r;

                    // If that failed, try the PostgreSQL specific formats
                    try {
                        return (U) PostgresUtils.toYearToSecond(from);
                    }
                    catch (Exception e) {
                        return null;
                    }
                }

                // [#1448] [#6255] [#5720] To Enum conversion
                else if (java.lang.Enum.class.isAssignableFrom(toClass) && (fromClass == String.class || from instanceof Enum || from instanceof EnumType)) {
                    try {
                        String fromString =
                            (fromClass == String.class) ? (String) from
                          : from instanceof EnumType e ? e.getLiteral()
                          : ((Enum) from).name();

                        if (fromString == null)
                            return null;

                        if (EnumType.class.isAssignableFrom(toClass)) {
                            for (Object value : toClass.getEnumConstants())
                                if (fromString.equals(((EnumType) value).getLiteral()))
                                    return (U) value;

                            return null;
                        }
                        else {
                            return (U) java.lang.Enum.valueOf((Class) toClass, fromString);
                        }

                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#1624] UUID data types can be read from Strings
                else if (fromClass == String.class && toClass == UUID.class) {
                    try {
                        return (U) parseUUID((String) from);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#8943] JSON data types can be read from Strings
                else if (fromClass == String.class && toClass == JSON.class) {
                    return (U) JSON.valueOf((String) from);
                }

                // [#8943] JSONB data types can be read from Strings
                else if (fromClass == String.class && toClass == JSONB.class) {
                    return (U) JSONB.valueOf((String) from);
                }

                // [#12509] JSON data types can be read from Maps
                else if (Map.class.isAssignableFrom(fromClass) && toClass == JSON.class) {
                    return (U) JSON.valueOf(JSONObject.toJSONString((Map) from));
                }

                // [#12509] JSONB data types can be read from Maps
                else if (Map.class.isAssignableFrom(fromClass) && toClass == JSONB.class) {
                    return (U) JSONB.valueOf(JSONObject.toJSONString((Map) from));
                }

                // [#12509] JSON data types can be read from Lists
                else if (List.class.isAssignableFrom(fromClass) && toClass == JSON.class) {
                    return (U) JSON.valueOf(JSONArray.toJSONString((List) from));
                }

                // [#12509] JSONB data types can be read from Lists
                else if (List.class.isAssignableFrom(fromClass) && toClass == JSONB.class) {
                    return (U) JSONB.valueOf(JSONArray.toJSONString((List) from));
                }

                // [#12509] JSON data types can be written to Maps
                else if (fromClass == JSON.class && Map.class.isAssignableFrom(toClass)) {
                    try {
                        return require(toClass, new JSONParser().parse(((JSON) from).data(), containerFactoryForMaps(toClass)));
                    }
                    catch (ParseException e) {
                        throw new DataTypeException("Error while mapping JSON to Map", e);
                    }
                }

                // [#12509] JSON data types can be written to Maps
                else if (fromClass == JSONB.class && Map.class.isAssignableFrom(toClass)) {
                    try {
                        return require(toClass, new JSONParser().parse(((JSONB) from).data(), containerFactoryForMaps(toClass)));
                    }
                    catch (ParseException e) {
                        throw new DataTypeException("Error while mapping JSONB to Map", e);
                    }
                }

                // [#12509] JSON data types can be written to Lists
                else if (fromClass == JSON.class && List.class.isAssignableFrom(toClass)) {
                    try {
                        return require(toClass, new JSONParser().parse(((JSON) from).data(), containerFactoryForLists(toClass)));
                    }
                    catch (ParseException e) {
                        throw new DataTypeException("Error while mapping JSON to List", e);
                    }
                }

                // [#12509] JSON data types can be written to Lists
                else if (fromClass == JSONB.class && List.class.isAssignableFrom(toClass)) {
                    try {
                        return require(toClass, new JSONParser().parse(((JSONB) from).data(), containerFactoryForLists(toClass)));
                    }
                    catch (ParseException e) {
                        throw new DataTypeException("Error while mapping JSONB to List", e);
                    }
                }

                // [#10072] Out of the box Jackson JSON mapping support
                else if (fromClass == JSON.class && _JSON.JSON_MAPPER != null) {
                    try {
                        return (U) _JSON.JSON_READ_METHOD.invoke(_JSON.JSON_MAPPER, ((JSON) from).data(), toClass);
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping JSON to POJO using Jackson", e);
                    }
                }

                // [#10072] Out of the box Jackson JSON mapping support
                else if (fromClass == JSONB.class && _JSON.JSON_MAPPER != null) {
                    try {
                        return (U) _JSON.JSON_READ_METHOD.invoke(_JSON.JSON_MAPPER, ((JSONB) from).data(), toClass);
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping JSON to POJO using Jackson", e);
                    }
                }

                // [#11213] Workaround for a problem when Jackson or Gson do not know
                //          the generic List<X> type because toClass has its generics erased
                else if (Map.class.isAssignableFrom(fromClass) && _JSON.JSON_MAPPER != null) {
                    try {
                        return (U) _JSON.JSON_READ_METHOD.invoke(_JSON.JSON_MAPPER, _JSON.JSON_WRITE_METHOD.invoke(_JSON.JSON_MAPPER, from), toClass);
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping JSON to POJO using Jackson", e);
                    }
                }

                // [#10072] Out of the box JAXB mapping support
                else if (fromClass == XML.class && _XML.JAXB_AVAILABLE) {
                    try {
                        return JAXB.unmarshal(new StringReader(((XML) from).data()), toClass);
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping XML to POJO using JAXB", e);
                    }
                }

                // [#3023] Record types can be converted using the supplied Configuration's
                // RecordMapperProvider
                else if (Record.class.isAssignableFrom(fromClass)) {
                    Record record = (Record) from;
                    return record.into(toClass);
                }

                else if (Struct.class.isAssignableFrom(fromClass)) {
                    Struct struct = (Struct) from;

                    if (QualifiedRecord.class.isAssignableFrom(toClass)) {
                        try {
                            QualifiedRecord<?> record = ((QualifiedRecord<?>) toClass.getDeclaredConstructor().newInstance());
                            record.from(struct.getAttributes());
                            return (U) record;
                        }
                        catch (Exception e) {
                            throw new DataTypeException("Cannot convert from " + fromClass + " to " + toClass, e);
                        }
                    }
                }




























                else if (Collection.class.isAssignableFrom(fromClass) && Collection.class.isAssignableFrom(toClass)) {
                    return copyCollection(fromClass, (Collection<?>) from);
                }

                // TODO [#2520] When RecordUnmappers are supported, they should also be considered here

                // [#10229] Try public, single argument, applicable constructors first
                for (Constructor<?> constructor : toClass.getConstructors()) {
                    Class<?>[] types = constructor.getParameterTypes();

                    // [#11183] Prevent StackOverflowError when recursing into UDT POJOs
                    if (types.length == 1 && types[0] != toClass) {
                        try {
                            return (U) constructor.newInstance(convert(from, types[0]));
                        }

                        // Throw exception further down instead
                        catch (Exception ignore) {}
                    }
                }

                // [#10229] Try private, single argument, applicable constructors
                for (Constructor<?> constructor : toClass.getDeclaredConstructors()) {
                    Class<?>[] types = constructor.getParameterTypes();

                    // [#11183] Prevent StackOverflowError when recursing into UDT POJOs
                    if (types.length == 1 && types[0] != toClass) {
                        try {
                            return (U) accessible(constructor).newInstance(convert(from, types[0]));
                        }

                        // Throw exception further down instead
                        catch (Exception ignore) {}
                    }
                }
            }

            throw fail(from, toClass);
        }

        private final Object[] toObjectArray(Object from) {
            Object[] result;

            if (from instanceof Object[] a) {
                return a;
            }
            else if (from instanceof byte[] a) {
                result = new Byte[a.length];

                for (int i = 0; i < result.length; i++)
                    result[i] = a[i];
            }
            else if (from instanceof short[] a) {
                result = new Short[a.length];

                for (int i = 0; i < result.length; i++)
                    result[i] = a[i];
            }
            else if (from instanceof int[] a) {
                result = new Integer[a.length];

                for (int i = 0; i < result.length; i++)
                    result[i] = a[i];
            }
            else if (from instanceof long[] a) {
                result = new Long[a.length];

                for (int i = 0; i < result.length; i++)
                    result[i] = a[i];
            }
            else if (from instanceof char[] a) {
                result = new Character[a.length];

                for (int i = 0; i < result.length; i++)
                    result[i] = a[i];
            }
            else if (from instanceof boolean[] a) {
                result = new Boolean[a.length];

                for (int i = 0; i < result.length; i++)
                    result[i] = a[i];
            }
            else if (from instanceof float[] a) {
                result = new Float[a.length];

                for (int i = 0; i < result.length; i++)
                    result[i] = a[i];
            }
            else if (from instanceof double[] a) {
                result = new Double[a.length];

                for (int i = 0; i < result.length; i++)
                    result[i] = a[i];
            }
            else
                throw new IllegalArgumentException("Unsupported type: " + from);

            return result;
        }

        @SuppressWarnings("unchecked")
        private final U copyCollection(Class<?> fromClass, Collection<?> collection) {
            try {
                Collection<Object> c;

                if (!toClass.isInterface())
                    c = (Collection<Object>) toClass.newInstance();
                else if (Set.class.isAssignableFrom(toClass))
                    c = new LinkedHashSet<>();
                else
                    c = new ArrayList<>();

                c.addAll(collection);
                return (U) c;
            }
            catch (Exception e) {
                throw new DataTypeException("Cannot convert from " + fromClass + " to " + toClass, e);
            }
        }

        @SuppressWarnings("unchecked")
        private static final <T> T require(Class<? extends T> type, Object o) {
            if (o == null || type.isInstance(o))
                return (T) o;
            else
                throw new DataTypeException("Type " + type + " expected. Got: " + o.getClass());
        }

        @SuppressWarnings("rawtypes")
        private static final ContainerFactory containerFactoryForMaps(Class<?> mapClass) {
            return new ContainerFactory() {
                @Override
                public Map createObjectContainer() {
                    try {
                        if (mapClass == Map.class)
                            return new LinkedHashMap<>();
                        else
                            return (Map) mapClass.getConstructor().newInstance();
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping JSON to Map", e);
                    }
                }

                @Override
                public List createArrayContainer() {
                    return new ArrayList<>();
                }
            };
        }

        @SuppressWarnings("rawtypes")
        private static final ContainerFactory containerFactoryForLists(Class<?> listClass) {
            return new ContainerFactory() {
                @Override
                public Map createObjectContainer() {
                    return new LinkedHashMap<>();
                }

                @Override
                public List createArrayContainer() {
                    try {
                        if (listClass == List.class)
                            return new ArrayList<>();
                        else
                            return (List) listClass.getConstructor().newInstance();
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping JSON to List", e);
                    }
                }
            };
        }

        @Override
        public Object to(U to, ConverterContext scope) {
            return to;
        }

        /**
         * Convert a long timestamp (millis) to any date type.
         */
        private static <X> X toDate(long time, Class<X> toClass) {
            return toDate(time, 0, toClass);
        }

        /**
         * Convert a long timestamp (millis) with nanos adjustment to any date
         * type.
         */
        @SuppressWarnings("unchecked")
        private static <X> X toDate(long time, int nanos, Class<X> toClass) {
            if (toClass == Date.class)
                return (X) new Date(time);
            else if (toClass == Time.class)
                return (X) new Time(time);
            else if (toClass == Timestamp.class)
                return (X) toTimestamp(time, nanos);
            else if (toClass == java.util.Date.class)
                return (X) new java.util.Date(time);
            else if (toClass == Calendar.class) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                return (X) calendar;
            }
            else if (toClass == LocalDate.class)
                return (X) new Date(time).toLocalDate();
            else if (toClass == LocalTime.class)
                return (X) new Time(time).toLocalTime();
            else if (toClass == OffsetTime.class)
                return (X) new Time(time).toLocalTime().atOffset(OffsetTime.now().getOffset());
            else if (toClass == LocalDateTime.class)
                return (X) toTimestamp(time, nanos).toLocalDateTime();
            else if (toClass == OffsetDateTime.class)
                return (X) toTimestamp(time, nanos).toLocalDateTime().atZone(ZoneId.systemDefault()).toOffsetDateTime();
            else if (toClass == Instant.class)
                if (nanos == 0L)
                    return (X) Instant.ofEpochMilli(time);
                else
                    return (X) Instant.ofEpochSecond(time / 1000L, nanos);

            throw fail(time, toClass);
        }

        private static Timestamp toTimestamp(long time, int nanos) {
            if (nanos == 0L)
                return new Timestamp(time);

            Timestamp ts = new Timestamp(time / 1000L * 1000L);
            ts.setNanos(nanos);
            return ts;
        }

        private static final long millis(Temporal temporal) {

            // java.sql.* temporal types:
            if (temporal instanceof LocalDate ld)
                return Date.valueOf(ld).getTime();
            else if (temporal instanceof LocalTime lt)
                return Time.valueOf(lt).getTime();
            else if (temporal instanceof LocalDateTime ldt)
                return Timestamp.valueOf(ldt).getTime();

            // OffsetDateTime
            else if (temporal.isSupported(INSTANT_SECONDS))
                return 1000 * temporal.getLong(INSTANT_SECONDS) + temporal.getLong(MILLI_OF_SECOND);

            // OffsetTime
            else if (temporal.isSupported(MILLI_OF_DAY))
                return temporal.getLong(MILLI_OF_DAY);

            throw fail(temporal, Long.class);
        }

        /**
         * Some databases do not implement the standard very well. Specifically,
         * {@link SQLDialect#SYBASE} seems to omit hyphens
         */
        private static final UUID parseUUID(String string) {
            if (string == null)
                return null;
            else if (string.contains("-"))
                return UUID.fromString(string);
            else
                return UUID.fromString(UUID_PATTERN.matcher(string).replaceAll("$1-$2-$3-$4-$5"));
        }

        private static final DataTypeException fail(Object from, Class<?> toClass) {
            String message = "Cannot convert from " + from + " (" + from.getClass() + ") to " + toClass;

            // [#10072] [#11023] Some mappings may not have worked because of badly set up classpaths
            if ((from instanceof JSON || from instanceof JSONB) && _JSON.JSON_MAPPER == null)
                return new DataTypeException(message + ". Check your classpath to see if Jackson or Gson is available to jOOQ.");
            else if (from instanceof XML && !_XML.JAXB_AVAILABLE)
                return new DataTypeException(message + ". Check your classpath to see if JAXB is available to jOOQ.");

            // [#16872] [#16884]
            else if (UnknownType.class.isAssignableFrom(toClass))
                return new DataTypeException(message +
                    """

                    UnknownType conversion errors appear mainly when using ad-hoc converters together with
                    reflective conversion or mapping. Ad-hoc converter calls, such as when using:

                    - DataType<T>.asConvertedDataTypeFrom(Function<T, U>)
                    - Field<T>.convertFrom(Function<T, U>)
                    - Row[N].mapping(Function[N]<TN.., U>)

                    .. don't know anything about the user type <U> because it is erased by the compiler.
                    Reflective conversion or mapping tends to need a Class<U> reference. Workarounds include:

                    - Pass the Class<U> literal to an overload of the above method
                    - Avoid combining ad-hoc conversion with reflective conversion or mapping

                    If you think you've encountered a bug where the conversion should still work, please
                    report it here: https://jooq.org/bug
                    """);
            else
                return new DataTypeException(message);
        }
    }

    static final Pattern P_FRACTIONAL_SECONDS = Pattern.compile("^(\\d+:\\d+:\\d+)\\.\\d+$");

    static final String patchFractionalSeconds(String string) {

        // [#15478] java.sql.Time doesn't support them
        return string.length() > 8
             ? P_FRACTIONAL_SECONDS.matcher(string).replaceFirst("$1")
             : string;
    }

    static final String patchIso8601Time(String s) {
        int l = s.length();
        int c1 = s.indexOf(':');

        if (c1 >= 0) {
            int c2 = s.indexOf(':', c1 + 1);

            if (c2 == -1)
                return padLead2(s, c1) + ':' + padMid2(s, c1) + ":00";
            else if (l < 8 || c2 != l - 3 || c1 != l - 6)
                return padLead2(s, c1) + ':' + padMid2(s, c1, c2) + ':' + padMid2(s, c2);
        }

        // [#12158] Support Db2's 15.30.45 format
        else if ((c1 = s.indexOf('.')) >= 0) {
            return patchIso8601Time(s.replace('.', ':'));
        }

        return s;
    }

    static final String patchIso8601Timestamp(String s, boolean t) {

        // [#11485] Trino produces a non-ISO 8601 "UTC" suffix, instead of "Z"
        if (s.endsWith(" UTC"))
            s = s.replace(" UTC", "Z");

        int l = s.length();
        int d1 = s.indexOf('-');
        int d2 = s.indexOf('-', d1 + 1);
        int ss = s.indexOf(' ', d2 + 1);
        int st = s.indexOf('T', d2 + 1);
        int sx = Math.max(ss, st);
        int c1 = s.indexOf(':', sx + 1);
        int c2 = s.indexOf(':', c1 + 1);

        if (d1 == -1 || d2 == -1)
            return s;

        // [#12547] Support year numbers with more or less than 4 digits
        // [#13786] Be lenient with PostgreSQL style abbreviated time stamp literals
        else if (sx == -1)
            return padLead4(s, d1) + '-'
                 + padMid2(s, d1, d2) + '-'
                 + padMid2(s, d2)
                 + (t ? "T00:00:00" : " 00:00:00");
        else if (c2 == -1)
            return padLead4(s, d1) + '-'
                 + padMid2(s, d1, d2) + '-'
                 + padMid2(s, d2, sx)
                 + (t ? 'T' : ' ')
                 + padMid2(s, sx, c1) + ':'
                 + padMid2(s, c1) + ":00";

        // [#13786] TODO: This doesn't pad seconds in the presence of fractional seconds or time zones
        else if (t == (st == -1) || l - c2 < 3 || c2 - c1 < 3 || c1 - sx < 3 || sx - d2 < 3 || d2 - d1 < 3)
            return padLead4(s, d1) + '-'
                 + padMid2(s, d1, d2) + '-'
                 + padMid2(s, d2, sx)
                 + (t ? 'T' : ' ')
                 + padMid2(s, sx, c1) + ':'
                 + padMid2(s, c1, c2) + ':'
                 + padMid2(s, c2);
        else
            return s;
    }

    static final String patchIso8601Date(String s) {

        // [#11485] Trino produces a non-ISO 8601 "UTC" suffix, instead of "Z"
        if (s.endsWith(" UTC"))
            s = s.replace(" UTC", "Z");

        int l = s.length();
        int d1 = s.indexOf('-');
        int d2 = s.indexOf('-', d1 + 1);
        int ss = s.indexOf(' ', d2 + 1);
        int st = s.indexOf('T', d2 + 1);
        int sx = Math.max(ss, st);

        if (d1 == -1 || d2 == -1)
            return s;

        // [#12547] Support year numbers with more or less than 4 digits
        // [#13786] Be lenient with PostgreSQL style abbreviated time stamp literals
        else if (sx == -1)
            if (l - d2 < 3 || d2 - d1 < 3)
                return padLead4(s, d1) + '-'
                     + padMid2(s, d1, d2) + '-'
                     + padMid2(s, d2);
            else
                return s;

        else
            return padLead4(s, d1) + '-'
                 + padMid2(s, d1, d2) + '-'
                 + padMid2(s, d2, sx);
    }

    private static final String padLead2(String s, int i1) {
        return leftPad(s.substring(0, i1), 2, '0');
    }

    private static final String padLead4(String s, int i1) {
        return leftPad(s.substring(0, i1), 4, '0');
    }

    private static final String padMid2(String s, int i1) {
        return leftPad(s.substring(i1 + 1), 2, '0');
    }

    private static final String padMid2(String s, int i1, int i2) {
        return leftPad(s.substring(i1 + 1, i2), 2, '0');
    }
}
