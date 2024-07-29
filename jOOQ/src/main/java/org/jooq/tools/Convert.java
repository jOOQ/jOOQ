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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.tools;

import java.io.File;
import java.net.URI;
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
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jooq.Converter;
import org.jooq.ConverterProvider;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.Internal;

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

    /**
     * All string values that can be transformed into a boolean <code>true</code> value.
     */
    public static final Set<String> TRUE_VALUES;

    /**
     * All string values that can be transformed into a boolean <code>false</code> value.
     */
    public static final Set<String> FALSE_VALUES;

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
    }

    /**
     * Convert an array of values to a matching data type
     * <p>
     * This converts <code>values[i]</code> to <code>fields[i].getType()</code>
     */
    public static final Object[] convert(Object[] values, Field<?>[] fields) {
        return Internal.convert(values, fields);
    }

    /**
     * Convert an array of values to a matching data type
     * <p>
     * This converts <code>values[i]</code> to <code>types[i]</code>
     */
    public static final Object[] convert(Object[] values, Class<?>[] types) {
        return Internal.convert(values, types);
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
    public static final <U> U[] convertArray(Object[] from, Converter<?, ? extends U> converter) throws DataTypeException {
        return Internal.convertArray(from, converter);
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
    public static final Object[] convertArray(Object[] from, Class<?> toClass) throws DataTypeException {
        return Internal.convertArray(from, toClass);
    }

    public static final <U> U[] convertCollection(Collection from, Class<? extends U[]> to){
        return Internal.convertCollection(from, to);
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
        return Internal.convert(from, converter);
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
    public static final <T> T convert(Object from, Class<? extends T> toClass) throws DataTypeException {
        return Internal.convert(from, toClass);
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
        return Internal.convert(collection, type);
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
        return Internal.convert(collection, converter);
    }
}
