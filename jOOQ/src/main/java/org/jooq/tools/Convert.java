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
 */
package org.jooq.tools;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static org.jooq.types.Unsigned.ubyte;
import static org.jooq.types.Unsigned.uint;
import static org.jooq.types.Unsigned.ulong;
import static org.jooq.types.Unsigned.ushort;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Date;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

// ...
import org.jooq.Converter;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.exception.DataTypeException;
import org.jooq.tools.jdbc.MockArray;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

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
 */
public final class Convert {

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

    static {
        Set<String> trueValues = new HashSet<String>();
        Set<String> falseValues = new HashSet<String>();

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
    }

    /**
     * Convert an array of values to a matching data type
     * <p>
     * This converts <code>values[i]</code> to <code>fields[i].getType()</code>
     */
    public static final Object[] convert(Object[] values, Field<?>[] fields) {

        // [#1005] Convert values from the <code>VALUES</code> clause to appropriate
        // values as specified by the <code>INTO</code> clause's column list.
        if (values != null) {
            Object[] result = new Object[values.length];

            for (int i = 0; i < values.length; i++) {

                // TODO [#1008] Should fields be cast? Check this with
                // appropriate integration tests
                if (values[i] instanceof Field<?>) {
                    result[i] = values[i];
                }
                else {
                    result[i] = convert(values[i], fields[i].getType());
                }
            }

            return result;
        }
        else {
            return null;
        }
    }

    /**
     * Convert an array of values to a matching data type
     * <p>
     * This converts <code>values[i]</code> to <code>types[i]</code>
     */
    public static final Object[] convert(Object[] values, Class<?>[] types) {

        // [#1005] Convert values from the <code>VALUES</code> clause to appropriate
        // values as specified by the <code>INTO</code> clause's column list.
        if (values != null) {
            Object[] result = new Object[values.length];

            for (int i = 0; i < values.length; i++) {

                // TODO [#1008] Should fields be cast? Check this with
                // appropriate integration tests
                if (values[i] instanceof Field<?>) {
                    result[i] = values[i];
                }
                else {
                    result[i] = convert(values[i], types[i]);
                }
            }

            return result;
        }
        else {
            return null;
        }
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
        if (from == null) {
            return null;
        }
        else {
            Object[] arrayOfT = convertArray(from, converter.fromType());
            Object[] arrayOfU = (Object[]) Array.newInstance(converter.toType(), from.length);

            for (int i = 0; i < arrayOfT.length; i++) {
                arrayOfU[i] = convert(arrayOfT[i], converter);
            }

            return (U[]) arrayOfU;
        }
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
        if (from == null) {
            return null;
        }
        else if (!toClass.isArray()) {
            return convertArray(from, Array.newInstance(toClass, 0).getClass());
        }
        else if (toClass == from.getClass()) {
            return from;
        }
        else {
            final Class<?> toComponentType = toClass.getComponentType();

            if (from.length == 0) {
                return Arrays.copyOf(from, from.length, (Class<? extends Object[]>) toClass);
            }
            else if (from[0] != null && from[0].getClass() == toComponentType) {
                return Arrays.copyOf(from, from.length, (Class<? extends Object[]>) toClass);
            }
            else {
                final Object[] result = (Object[]) Array.newInstance(toComponentType, from.length);

                for (int i = 0; i < from.length; i++) {
                    result[i] = convert(from[i], toComponentType);
                }

                return result;
            }
        }
    }

    /**
     * Convert an object to a type.
     *
     * @param from The source object
     * @param converter The data type converter
     * @return The target type object
     * @throws DataTypeException - When the conversion is not possible
     */
    public static final <U> U convert(Object from, Converter<?, ? extends U> converter) throws DataTypeException {
        return convert0(from, converter);
    }

    /**
     * Conversion type-safety
     */
    private static final <T, U> U convert0(Object from, Converter<T, ? extends U> converter) throws DataTypeException {
        ConvertAll<T> all = new ConvertAll<T>(converter.fromType());
        return converter.from(all.from(from));
    }

    /**
     * Convert an object to a type. These are the conversion rules:
     * <ul>
     * <li><code>null</code> is always converted to <code>null</code>,
     * regardless of the target type.</li>
     * <li>Identity conversion (converting a value to its own type) is always
     * possible.</li>
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
     * <li>Primitive target types behave like their wrapper types, except that
     * <code>null</code> is converted into the initialisation value (e.g.
     * <code>0</code> for <code>int</code>, <code>false</code> for
     * <code>boolean</code>)</li>
     * <li><code>byte[]</code> can be converted into <code>String</code>, using
     * the platform's default charset</li>
     * <li><code>Object[]</code> can be converted into any other array type, if
     * array elements can be converted, too</li>
     * <li><b>All other combinations that are not listed above will result in a
     * {@link DataTypeException}</b></li>
     * </ul>
     *
     * @param from The object to convert
     * @param toClass The target type
     * @return The converted object
     * @throws DataTypeException - When the conversion is not possible
     */
    public static final <T> T convert(Object from, Class<? extends T> toClass) throws DataTypeException {
        return convert(from, new ConvertAll<T>(toClass));
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
        return convert(collection, new ConvertAll<T>(type));
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
        ConvertAll<T> all = new ConvertAll<T>(converter.fromType());
        List<U> result = new ArrayList<U>(collection.size());

        for (Object o : collection) {
            result.add(convert(all.from(o), converter));
        }

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

        /**
         * Generated UID
         */
        private static final long        serialVersionUID = 2508560107067092501L;

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
                    if (toClass == char.class) {
                        return (U) Character.valueOf((char) 0);
                    }

                    // All others can be converted from (int) 0
                    else {
                        return convert(0, toClass);
                    }
                }


                else if (toClass == Optional.class) {
                    return (U) Optional.empty();
                }


                else {
                    return null;
                }
            }
            else {
                final Class<?> fromClass = from.getClass();

                // No conversion
                if (toClass == fromClass) {
                    return (U) from;
                }

                // [#2535] Simple up-casting can be done early
                // [#1155] ... up-casting includes (toClass == Object.class)
                else if (toClass.isAssignableFrom(fromClass)) {
                    return (U) from;
                }

                // Regular checks
                else if (fromClass == byte[].class) {

                    // [#5824] UUID's most significant bits in byte[] are first
                    if (toClass == UUID.class) {
                        ByteBuffer b = ByteBuffer.wrap((byte[]) from);
                        long mostSigBits = b.getLong();
                        long leastSigBits = b.getLong();
                        return (U) new UUID(mostSigBits, leastSigBits);
                    }

                    // [#5569] Binary data is expected to be in JVM's default encoding
                    else {
                        return convert(new String((byte[]) from), toClass);
                    }
                }
                else if (fromClass.isArray()) {

                    // [#3443] Conversion from Object[] to JDBC Array
                    if (toClass == java.sql.Array.class) {
                        return (U) new MockArray(null, (Object[]) from, fromClass);
                    }
                    else {
                        return (U) convertArray((Object[]) from, toClass);
                    }
                }


                else if (toClass == Optional.class) {
                    return (U) Optional.of(from);
                }


                // All types can be converted into String
                else if (toClass == String.class) {
                    if (from instanceof EnumType) {
                        return (U) ((EnumType) from).getLiteral();
                    }

                    return (U) from.toString();
                }

                // [#5569] It should be possible, at least, to convert an empty string to an empty (var)binary.
                else if (toClass == byte[].class) {

                    // [#5824] UUID's most significant bits in byte[] are first
                    if (from instanceof UUID) {
                        ByteBuffer b = ByteBuffer.wrap(new byte[16]);
                        b.putLong(((UUID) from).getMostSignificantBits());
                        b.putLong(((UUID) from).getLeastSignificantBits());
                        return (U)b.array();
                    }
                    else {
                        return (U) from.toString().getBytes();
                    }
                }

                // Various number types are converted between each other via String
                else if (toClass == Byte.class || toClass == byte.class) {
                    if (Number.class.isAssignableFrom(fromClass)) {
                        return (U) Byte.valueOf(((Number) from).byteValue());
                    }

                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? Byte.valueOf((byte) 1) : Byte.valueOf((byte) 0));
                    }

                    try {
                        return (U) Byte.valueOf(new BigDecimal(from.toString().trim()).byteValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == Short.class || toClass == short.class) {
                    if (Number.class.isAssignableFrom(fromClass)) {
                        return (U) Short.valueOf(((Number) from).shortValue());
                    }

                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? Short.valueOf((short) 1) : Short.valueOf((short) 0));
                    }

                    try {
                        return (U) Short.valueOf(new BigDecimal(from.toString().trim()).shortValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == Integer.class || toClass == int.class) {
                    if (Number.class.isAssignableFrom(fromClass)) {
                        return (U) Integer.valueOf(((Number) from).intValue());
                    }

                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? Integer.valueOf(1) : Integer.valueOf(0));
                    }

                    try {
                        return (U) Integer.valueOf(new BigDecimal(from.toString().trim()).intValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == Long.class || toClass == long.class) {
                    if (Number.class.isAssignableFrom(fromClass)) {
                        return (U) Long.valueOf(((Number) from).longValue());
                    }

                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? Long.valueOf(1L) : Long.valueOf(0L));
                    }

                    if (java.util.Date.class.isAssignableFrom(fromClass)) {
                        return (U) Long.valueOf(((java.util.Date) from).getTime());
                    }


                    if (Temporal.class.isAssignableFrom(fromClass)) {
                        return (U) Long.valueOf(millis((Temporal) from));
                    }


                    try {
                        return (U) Long.valueOf(new BigDecimal(from.toString().trim()).longValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }

                // ... this also includes unsigned number types
                else if (toClass == UByte.class) {
                    try {
                        if (Number.class.isAssignableFrom(fromClass)) {
                            return (U) ubyte(((Number) from).shortValue());
                        }

                        if (fromClass == Boolean.class || fromClass == boolean.class) {
                            return (U) (((Boolean) from) ? ubyte(1) : ubyte(0));
                        }

                        return (U) ubyte(new BigDecimal(from.toString().trim()).shortValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == UShort.class) {
                    try {
                        if (Number.class.isAssignableFrom(fromClass)) {
                            return (U) ushort(((Number) from).intValue());
                        }

                        if (fromClass == Boolean.class || fromClass == boolean.class) {
                            return (U) (((Boolean) from) ? ushort(1) : ushort(0));
                        }

                        return (U) ushort(new BigDecimal(from.toString().trim()).intValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == UInteger.class) {
                    try {
                        if (Number.class.isAssignableFrom(fromClass)) {
                            return (U) uint(((Number) from).longValue());
                        }

                        if (fromClass == Boolean.class || fromClass == boolean.class) {
                            return (U) (((Boolean) from) ? uint(1) : uint(0));
                        }

                        return (U) uint(new BigDecimal(from.toString().trim()).longValue());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == ULong.class) {
                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? ulong(1) : ulong(0));
                    }

                    if (java.util.Date.class.isAssignableFrom(fromClass)) {
                        return (U) ulong(((java.util.Date) from).getTime());
                    }


                    if (Temporal.class.isAssignableFrom(fromClass)) {
                        return (U) ulong(millis((Temporal) from));
                    }


                    try {
                        return (U) ulong(new BigDecimal(from.toString().trim()).toBigInteger().toString());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }

                // ... and floating point / fixed point types
                else if (toClass == Float.class || toClass == float.class) {
                    if (Number.class.isAssignableFrom(fromClass)) {
                        return (U) Float.valueOf(((Number) from).floatValue());
                    }

                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? Float.valueOf(1.0f) : Float.valueOf(0.0f));
                    }

                    try {
                        return (U) Float.valueOf(from.toString().trim());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == Double.class || toClass == double.class) {
                    if (Number.class.isAssignableFrom(fromClass)) {
                        return (U) Double.valueOf(((Number) from).doubleValue());
                    }

                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? Double.valueOf(1.0) : Double.valueOf(0.0));
                    }

                    try {
                        return (U) Double.valueOf(from.toString().trim());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == BigDecimal.class) {
                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? BigDecimal.ONE : BigDecimal.ZERO);
                    }

                    try {
                        return (U) new BigDecimal(from.toString().trim());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == BigInteger.class) {
                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? BigInteger.ONE : BigInteger.ZERO);
                    }

                    try {
                        return (U) new BigDecimal(from.toString().trim()).toBigInteger();
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
                else if (toClass == Boolean.class || toClass == boolean.class) {
                    String s = from.toString().toLowerCase().trim();

                    if (TRUE_VALUES.contains(s)) {
                        return (U) Boolean.TRUE;
                    }
                    else if (FALSE_VALUES.contains(s)) {
                        return (U) Boolean.FALSE;
                    }
                    else {
                        return (U) (toClass == Boolean.class ? null : false);
                    }
                }
                else if (toClass == Character.class || toClass == char.class) {
                    if (fromClass == Boolean.class || fromClass == boolean.class) {
                        return (U) (((Boolean) from) ? Character.valueOf('1') : Character.valueOf('0'));
                    }

                    if (from.toString().length() < 1) {
                        return null;
                    }

                    return (U) Character.valueOf(from.toString().charAt(0));
                }

                // URI types can be converted from strings
                else if ((fromClass == String.class) && toClass == URI.class) {
                    try {
                        return (U) new URI(from.toString());
                    }
                    catch (URISyntaxException e) {
                        return null;
                    }
                }

                // URI types can be converted from strings
                else if ((fromClass == String.class) && toClass == URL.class) {
                    try {
                        return (U) new URI(from.toString()).toURL();
                    }
                    catch (Exception e) {
                        return null;
                    }
                }

                // File types can be converted from strings
                else if ((fromClass == String.class) && toClass == File.class) {
                    try {
                        return (U) new File(from.toString());
                    }
                    catch (Exception e) {
                        return null;
                    }
                }

                // Date types can be converted among each other
                else if (java.util.Date.class.isAssignableFrom(fromClass)) {
                    return toDate(((java.util.Date) from).getTime(), toClass);
                }


                else if (Temporal.class.isAssignableFrom(fromClass)) {
                    return toDate(convert(from, Long.class), toClass);
                }


                // Long may also be converted into a date type
                else if ((fromClass == Long.class || fromClass == long.class) && java.util.Date.class.isAssignableFrom(toClass)) {
                    return toDate((Long) from, toClass);
                }


                else if ((fromClass == Long.class || fromClass == long.class) && Temporal.class.isAssignableFrom(toClass)) {
                    return toDate((Long) from, toClass);
                }


                // [#1501] Strings can be converted to java.sql.Date
                else if ((fromClass == String.class) && toClass == java.sql.Date.class) {
                    try {
                        return (U) java.sql.Date.valueOf((String) from);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#1501] Strings can be converted to java.sql.Date
                else if ((fromClass == String.class) && toClass == java.sql.Time.class) {
                    try {
                        return (U) java.sql.Time.valueOf((String) from);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#1501] Strings can be converted to java.sql.Date
                else if ((fromClass == String.class) && toClass == java.sql.Timestamp.class) {
                    try {
                        return (U) java.sql.Timestamp.valueOf((String) from);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }


                else if ((fromClass == String.class) && toClass == LocalDate.class) {

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

                else if ((fromClass == String.class) && toClass == LocalTime.class) {

                    // Try "lenient" ISO date formats first
                    try {
                        return (U) java.sql.Time.valueOf((String) from).toLocalTime();
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) LocalTime.parse((String) from);
                        }
                        catch (DateTimeParseException e2) {
                            return null;
                        }
                    }
                }

                else if ((fromClass == String.class) && toClass == OffsetTime.class) {

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

                else if ((fromClass == String.class) && toClass == LocalDateTime.class) {

                    // Try "lenient" ISO date formats first
                    try {
                        return (U) java.sql.Timestamp.valueOf((String) from).toLocalDateTime();
                    }
                    catch (IllegalArgumentException e1) {
                        try {
                            return (U) LocalDateTime.parse((String) from);
                        }
                        catch (DateTimeParseException e2) {
                            return null;
                        }
                    }
                }

                else if ((fromClass == String.class) && toClass == OffsetDateTime.class) {

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

                else if ((fromClass == String.class) && toClass == Instant.class) {

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


                // [#1448] Some users may find it useful to convert string
                // literals to Enum values without a Converter
                else if ((fromClass == String.class) && java.lang.Enum.class.isAssignableFrom(toClass)) {
                    try {
                        return (U) java.lang.Enum.valueOf((Class) toClass, (String) from);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#1624] UUID data types can be read from Strings
                else if ((fromClass == String.class) && toClass == UUID.class) {
                    try {
                        return (U) parseUUID((String) from);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                }

                // [#3023] Record types can be converted using the supplied Configuration's
                // RecordMapperProvider
                else if (Record.class.isAssignableFrom(fromClass)) {
                    Record record = (Record) from;
                    return record.into(toClass);
                }








































                // TODO [#2520] When RecordUnmappers are supported, they should also be considered here
            }

            throw fail(from, toClass);
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
            if (toClass == Date.class) {
                return (X) new Date(time);
            }
            else if (toClass == Time.class) {
                return (X) new Time(time);
            }
            else if (toClass == Timestamp.class) {
                return (X) new Timestamp(time);
            }
            else if (toClass == java.util.Date.class) {
                return (X) new java.util.Date(time);
            }
            else if (toClass == Calendar.class) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                return (X) calendar;
            }


            else if (toClass == LocalDate.class) {
                return (X) new Date(time).toLocalDate();
            }
            else if (toClass == LocalTime.class) {
                return (X) new Time(time).toLocalTime();
            }
            else if (toClass == OffsetTime.class) {
                return (X) new Time(time).toLocalTime().atOffset(OffsetTime.now().getOffset());
            }
            else if (toClass == LocalDateTime.class) {
                return (X) new Timestamp(time).toLocalDateTime();
            }
            else if (toClass == OffsetDateTime.class) {
                return (X) new Timestamp(time).toLocalDateTime().atOffset(OffsetDateTime.now().getOffset());
            }
            else if (toClass == Instant.class) {
                return (X) Instant.ofEpochMilli(time);
            }


            throw fail(time, toClass);
        }


        private static final long millis(Temporal temporal) {

            // java.sql.* temporal types:
            if (temporal instanceof LocalDate) {
                return Date.valueOf((LocalDate) temporal).getTime();
            }
            else if (temporal instanceof LocalTime) {
                return Time.valueOf((LocalTime) temporal).getTime();
            }
            else if (temporal instanceof LocalDateTime) {
                return Timestamp.valueOf((LocalDateTime) temporal).getTime();
            }

            // OffsetDateTime
            else if (temporal.isSupported(INSTANT_SECONDS)) {
                return 1000 * temporal.getLong(INSTANT_SECONDS) + temporal.getLong(MILLI_OF_SECOND);
            }

            // OffsetTime
            else if (temporal.isSupported(MILLI_OF_DAY)) {
                return temporal.getLong(MILLI_OF_DAY);
            }

            throw fail(temporal, Long.class);
        }


        /**
         * Some databases do not implement the standard very well. Specifically,
         * {@link SQLDialect#SYBASE} seems to omit hyphens
         */
        private static final UUID parseUUID(String string) {
            if (string == null) {
                return null;
            }
            else if (string.contains("-")) {
                return UUID.fromString(string);
            }
            else {
                return UUID.fromString(UUID_PATTERN.matcher(string).replaceAll("$1-$2-$3-$4-$5"));
            }
        }

        private static DataTypeException fail(Object from, Class<?> toClass) {
            return new DataTypeException("Cannot convert from " + from + " (" + from.getClass() + ") to " + toClass);
        }
    }
}
