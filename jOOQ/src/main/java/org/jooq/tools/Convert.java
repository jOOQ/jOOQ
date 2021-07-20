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
package org.jooq.tools;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static org.jooq.impl.Internal.arrayType;
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
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.xml.bind.JAXB;

// ...
import org.jooq.Converter;
import org.jooq.ConverterProvider;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.QualifiedRecord;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.XML;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.IdentityConverter;
import org.jooq.tools.jdbc.MockArray;
import org.jooq.tools.jdbc.MockResultSet;
import org.jooq.tools.reflect.Reflect;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.xml.jaxb.InformationSchema;

/**
 * Utility methods for type conversions
 * <p>
 * This class provides less type-safety than the general jOOQ API methods. For
 * instance, it accepts arbitrary {@link Converter} objects in methods like
 * {@link #convert(Collection, Converter)} and {@link #convert(Object, Class)},
 * trying to retrofit the <code>Object</code> argument to the type provided in
 * {@link Converter#fromType()} before performing the actual conversion.
 *
 * @author Lukas Eder
 * @deprecated - 3.15.0 - [#11898] This class will be removed in the future. Do
 *             not reuse. Data type conversion happens using internal
 *             {@link Converter} implementations, or {@link ConverterProvider}
 *             provided ones, or implementations attached to fields via
 *             generated code, or via {@link Field#convert(Converter)}, or
 *             {@link DataType#asConvertedDataType(Converter)}.
 */
@Deprecated(forRemoval = true, since = "3.15")
public final class Convert {

    private static final JooqLogger log          = JooqLogger.getLogger(Convert.class);

    /**
     * All string values that can be transformed into a boolean <code>true</code> value.
     */
    public static final Set<String> TRUE_VALUES;

    /**
     * All string values that can be transformed into a boolean <code>false</code> value.
     */
    public static final Set<String> FALSE_VALUES;

    /**
     * A UUID pattern for UUIDs with or without hyphens
     */
    private static final Pattern UUID_PATTERN = Pattern.compile("(\\p{XDigit}{8})-?(\\p{XDigit}{4})-?(\\p{XDigit}{4})-?(\\p{XDigit}{4})-?(\\p{XDigit}{12})");

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

    /**
     * Whether a JAXB implementation is available.
     */
    private static final boolean JAXB_AVAILABLE;

    static {
        Set<String> trueValues = new HashSet<>();
        Set<String> falseValues = new HashSet<>();

        trueValues.add("1");
        trueValues.add("1.0");
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

        Object jsonMapper = null;
        Method jsonReadMethod = null;
        Method jsonWriteMethod = null;
        boolean jaxbAvailable = false;

        try {
            Class<?> klass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");

            jsonMapper = klass.getDeclaredConstructor().newInstance();
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

    /**
     * Convert an array of values to a matching data type
     * <p>
     * This converts <code>values[i]</code> to <code>fields[i].getType()</code>
     */
    public static final Object[] convert(Object[] values, Field<?>[] fields) {
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
    public static final Object[] convert(Object[] values, Class<?>[] types) {
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
    public static final <U> U[] convertArray(Object[] from, Converter<?, ? extends U> converter) throws DataTypeException {
        if (from == null)
            return null;

        Object[] arrayOfT = convertArray(from, converter.fromType());
        Object[] arrayOfU = (Object[]) Array.newInstance(converter.toType(), from.length);

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
    public static final Object[] convertArray(Object[] from, Class<?> toClass) throws DataTypeException {
        if (from == null)
            return null;
        else if (!toClass.isArray())
            return convertArray(from, arrayType(toClass));
        else if (toClass == from.getClass())
            return from;
        else {
            final Class<?> toComponentType = toClass.getComponentType();

            if (from.length == 0)
                return Arrays.copyOf(from, from.length, (Class<? extends Object[]>) toClass);
            else if (from[0] != null && from[0].getClass() == toComponentType)
                return Arrays.copyOf(from, from.length, (Class<? extends Object[]>) toClass);
            else {
                final Object[] result = (Object[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++)
                    result[i] = convert(from[i], toComponentType);

                return result;
            }
        }
    }

    public static final <U> U[] convertCollection(Collection from, Class<? extends U[]> to){
        return new ConvertAll<U[]>(to).from(from);
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
    public static final <U> U convert(Object from, Converter<?, ? extends U> converter) throws DataTypeException {

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
            return converter.from((T) from);

        ConvertAll<T> convertAll = new ConvertAll<>(fromType);
        return converter.from(convertAll.from(from));
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
    public static final <T> T convert(Object from, Class<? extends T> toClass) throws DataTypeException {
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
    public static final <T> List<T> convert(Collection<?> collection, Class<? extends T> type) throws DataTypeException {
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
    public static final <U> List<U> convert(Collection<?> collection, Converter<?, ? extends U> converter) throws DataTypeException {
        return convert0(collection, converter);
    }

    /**
     * Type safe conversion
     */
    private static final <T, U> List<U> convert0(Collection<?> collection, Converter<T, ? extends U> converter) throws DataTypeException {
        ConvertAll<T> all = new ConvertAll<>(converter.fromType());
        List<U> result = new ArrayList<>(collection.size());

        for (Object o : collection)
            result.add(convert(all.from(o), converter));

        return result;
    }

    /**
     * No instances
     */
    private Convert() {}

    /**
     * The converter to convert them all.
     */
    private static class ConvertAll<U> implements Converter<Object, U> {

        private final Class<? extends U> toClass;

        ConvertAll(Class<? extends U> toClass) {
            this.toClass = toClass;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public U from(Object from) {
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

                // Regular checks
                else if (fromClass == byte[].class) {

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
                    Object[] fromArray = (Object[]) from;

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
                    if (from instanceof EnumType)
                        return (U) ((EnumType) from).getLiteral();

                    return (U) from.toString();
                }

                // [#5569] It should be possible, at least, to convert an empty string to an empty (var)binary.
                else if (toClass == byte[].class) {

                    // [#5824] UUID's most significant bits in byte[] are first
                    if (from instanceof UUID) {
                        ByteBuffer b = ByteBuffer.wrap(new byte[16]);
                        b.putLong(((UUID) from).getMostSignificantBits());
                        b.putLong(((UUID) from).getLeastSignificantBits());
                        return (U) b.array();
                    }
                    else if (from instanceof ByteBuffer)
                        return (U) ((ByteBuffer) from).array();
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
                    if (Timestamp.class == fromClass && LocalDateTime.class == toClass)
                        return (U) ((Timestamp) from).toLocalDateTime();
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
                    else if (LocalDate.class == fromClass && Date.class == toClass)
                        return (U) Date.valueOf((LocalDate) from);
                    else if (LocalTime.class == fromClass && Time.class == toClass)
                        return (U) Time.valueOf((LocalTime) from);
                    else
                        return toDate(convert(from, Long.class), toClass);
                }
                else if (Temporal.class.isAssignableFrom(fromClass)) {
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
                        return (U) java.sql.Date.valueOf((String) from);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#1501] Strings can be converted to java.sql.Date
                else if (fromClass == String.class && toClass == java.sql.Time.class) {
                    try {
                        return (U) java.sql.Time.valueOf(patchIso8601Time((String) from));
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

                    // Try "lenient" ISO date formats first
                    try {
                        return (U) java.sql.Date.valueOf((String) from).toLocalDate();
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) LocalDate.parse((String) from);
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
                        return (U) java.sql.Timestamp.valueOf((String) from).toLocalDateTime().atOffset(OffsetDateTime.now().getOffset());
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) OffsetDateTime.parse((String) from);
                        }
                        catch (DateTimeParseException e2) {
                            return null;
                        }
                    }
                }

                else if (fromClass == String.class && toClass == Instant.class) {

                    // Try "local" ISO date formats first
                    try {
                        return (U) java.sql.Timestamp.valueOf((String) from).toLocalDateTime().atOffset(OffsetDateTime.now().getOffset()).toInstant();
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) Instant.parse((String) from);
                        }
                        catch (DateTimeParseException e2) {
                            return null;
                        }
                    }
                }

                // [#1448] [#6255] [#5720] To Enum conversion
                else if (java.lang.Enum.class.isAssignableFrom(toClass) && (fromClass == String.class || from instanceof Enum || from instanceof EnumType)) {
                    try {
                        String fromString =
                            (fromClass == String.class) ? (String) from
                          : (from instanceof EnumType)  ? ((EnumType) from).getLiteral()
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

                // [#10072] Out of the box Jackson JSON mapping support
                else if (fromClass == JSON.class && JSON_MAPPER != null) {
                    try {
                        return (U) JSON_READ_METHOD.invoke(JSON_MAPPER, ((JSON) from).data(), toClass);
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping JSON to POJO using Jackson", e);
                    }
                }

                // [#10072] Out of the box Jackson JSON mapping support
                else if (fromClass == JSONB.class && JSON_MAPPER != null) {
                    try {
                        return (U) JSON_READ_METHOD.invoke(JSON_MAPPER, ((JSONB) from).data(), toClass);
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping JSON to POJO using Jackson", e);
                    }
                }

                // [#11213] Workaround for a problem when Jackson or Gson do not know
                //          the generic List<X> type because toClass has its generics erased
                else if (Map.class.isAssignableFrom(fromClass) && JSON_MAPPER != null) {
                    try {
                        return (U) JSON_READ_METHOD.invoke(JSON_MAPPER, JSON_WRITE_METHOD.invoke(JSON_MAPPER, from), toClass);
                    }
                    catch (Exception e) {
                        throw new DataTypeException("Error while mapping JSON to POJO using Jackson", e);
                    }
                }

                // [#10072] Out of the box JAXB mapping support
                else if (fromClass == XML.class && JAXB_AVAILABLE) {
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

        private static final String patchIso8601Time(String string) {
            // [#12158] Support Db2's 15.30.45 format
            return string.length() == 8
                 ? string.replace('.', ':')
                 : string;
        }

        private static final String patchIso8601Timestamp(String string, boolean t) {
            if (string.length() > 11)
                if (t && string.charAt(10) == ' ')
                    return string.substring(0, 10) + "T" + string.substring(11);
                else if (!t && string.charAt(10) == 'T')
                    return string.substring(0, 10) + " " + string.substring(11);

            return string;
        }

        @Override
        public Object to(U to) {
            return to;
        }

        @Override
        public Class<Object> fromType() {
            return Object.class;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<U> toType() {
            return (Class<U>) toClass;
        }

        /**
         * Convert a long timestamp to any date type
         */
        @SuppressWarnings("unchecked")
        private static <X> X toDate(long time, Class<X> toClass) {
            if (toClass == Date.class)
                return (X) new Date(time);
            else if (toClass == Time.class)
                return (X) new Time(time);
            else if (toClass == Timestamp.class)
                return (X) new Timestamp(time);
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
                return (X) new Timestamp(time).toLocalDateTime();
            else if (toClass == OffsetDateTime.class)
                return (X) new Timestamp(time).toLocalDateTime().atOffset(OffsetDateTime.now().getOffset());
            else if (toClass == Instant.class)
                return (X) Instant.ofEpochMilli(time);

            throw fail(time, toClass);
        }

        private static final long millis(Temporal temporal) {

            // java.sql.* temporal types:
            if (temporal instanceof LocalDate)
                return Date.valueOf((LocalDate) temporal).getTime();
            else if (temporal instanceof LocalTime)
                return Time.valueOf((LocalTime) temporal).getTime();
            else if (temporal instanceof LocalDateTime)
                return Timestamp.valueOf((LocalDateTime) temporal).getTime();

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

        private static DataTypeException fail(Object from, Class<?> toClass) {
            String message = "Cannot convert from " + from + " (" + from.getClass() + ") to " + toClass;

            // [#10072] [#11023] Some mappings may not have worked because of badly set up classpaths
            if ((from instanceof JSON || from instanceof JSONB) && JSON_MAPPER == null)
                return new DataTypeException(message + ". Check your classpath to see if Jackson or Gson is available to jOOQ.");
            else if (from instanceof XML && !JAXB_AVAILABLE)
                return new DataTypeException(message + ". Check your classpath to see if JAXB is available to jOOQ.");
            else
                return new DataTypeException(message);
        }
    }
}
