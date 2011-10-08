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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jooq.SQLDialectNotSupportedException;

/**
 * Utility methods for type conversions
 *
 * @author Lukas Eder
 */
final class TypeUtils {

    static final Set<String> TRUE_VALUES;
    static final Set<String> FALSE_VALUES;

    static {
        TRUE_VALUES = new HashSet<String>();
        FALSE_VALUES = new HashSet<String>();

        TRUE_VALUES.add("1");
        TRUE_VALUES.add("y");
        TRUE_VALUES.add("Y");
        TRUE_VALUES.add("yes");
        TRUE_VALUES.add("YES");
        TRUE_VALUES.add("true");
        TRUE_VALUES.add("TRUE");
        TRUE_VALUES.add("on");
        TRUE_VALUES.add("ON");
        TRUE_VALUES.add("enabled");
        TRUE_VALUES.add("ENABLED");

        FALSE_VALUES.add("0");
        FALSE_VALUES.add("n");
        FALSE_VALUES.add("N");
        FALSE_VALUES.add("no");
        FALSE_VALUES.add("NO");
        FALSE_VALUES.add("false");
        FALSE_VALUES.add("FALSE");
        FALSE_VALUES.add("off");
        FALSE_VALUES.add("OFF");
        FALSE_VALUES.add("disabled");
        FALSE_VALUES.add("DISABLED");
    }

    @SuppressWarnings("unchecked")
    static Object[] convertArray(Object[] from, Class<?> toClass) {
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
     * Convert an object to a type. These are the conversion rules:
     * <ul>
     * <li><code>null</code> is always converted to <code>null</code>,
     * regardless of the target type.</li>
     * <li>Identity conversion is always possible</li>
     * <li>All types can be converted to <code>String</code></li>
     * <li>All types can be converted to <code>Object</code></li>
     * <li>All <code>Number</code> types can be converted to other
     * <code>Number</code> types</li>
     * <li>All <code>Number</code> or <code>String</code> types can be converted
     * to <code>Boolean</code>. Possible (case-insensitive) values for
     * <code>true</code>:
     * <ul>
     * <li><code>1</code></li>
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
     * <li><code>n</code></li>
     * <li><code>no</code></li>
     * <li><code>false</code></li>
     * <li><code>off</code></li>
     * <li><code>disabled</code></li>
     * </ul>
     * <p>
     * All other values evaluate to <code>null</code></li>
     * <li>All <code>Date</code> types can be converted into each other</li>
     * <li><code>byte[]</code> can be converted into <code>String</code>, using
     * the platform's default charset</li>
     * <li><code>Object[]</code> can be converted into any other array type, if
     * array elements can be converted, too</li>
     * <li><b>All other combinations that are not listed above will result in an
     * undisclosed unchecked exception.</b></li>
     * </ul>
     *
     * @param from The object to convert
     * @param toClass The target type
     * @return The converted object
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object from, Class<? extends T> toClass) {
        if (from == null) {
            return null;
        }
        else {
            final Class<?> fromClass = from.getClass();

            if (toClass == fromClass) {
                return (T) from;
            }
            else if (fromClass == byte[].class) {
                return convert(new String((byte[]) from), toClass);
            }
            else if (fromClass.isArray()) {
                return (T) convertArray((Object[]) from, toClass);
            }

            // All types can be converted into String
            else if (toClass == String.class) {
                return (T) from.toString();
            }

            // All types can be converted into Object
            else if (toClass == Object.class) {
                return (T) from;
            }

            // Various number types are converted between each other via String
            else if (toClass == Byte.class) {
                return (T) Byte.valueOf(new BigDecimal(from.toString().trim()).byteValue());
            }
            else if (toClass == Short.class) {
                return (T) Short.valueOf(new BigDecimal(from.toString().trim()).shortValue());
            }
            else if (toClass == Integer.class) {
                return (T) Integer.valueOf(new BigDecimal(from.toString().trim()).intValue());
            }
            else if (toClass == Long.class) {
                return (T) Long.valueOf(new BigDecimal(from.toString().trim()).longValue());
            }
            else if (toClass == Float.class) {
                return (T) Float.valueOf(from.toString().trim());
            }
            else if (toClass == Double.class) {
                return (T) Double.valueOf(from.toString().trim());
            }
            else if (toClass == BigDecimal.class) {
                return (T) new BigDecimal(from.toString().trim());
            }
            else if (toClass == BigInteger.class) {
                return (T) new BigDecimal(from.toString().trim()).toBigInteger();
            }
            else if (toClass == Boolean.class) {
                String s = from.toString().toLowerCase().trim();

                if (TRUE_VALUES.contains(s)) {
                    return (T) Boolean.TRUE;
                }
                else if (FALSE_VALUES.contains(s)) {
                    return (T) Boolean.FALSE;
                }
                else {
                    return null;
                }
            }

            // Date types can be converted among each other
            else if (toClass == Date.class && java.util.Date.class.isAssignableFrom(fromClass)) {
                return (T) new Date(((java.util.Date) from).getTime());
            }
            else if (toClass == Time.class && java.util.Date.class.isAssignableFrom(fromClass)) {
                return (T) new Time(((java.util.Date) from).getTime());
            }
            else if (toClass == Timestamp.class && java.util.Date.class.isAssignableFrom(fromClass)) {
                return (T) new Timestamp(((java.util.Date) from).getTime());
            }
        }

        throw new SQLDialectNotSupportedException("Cannot convert from " + from + " to " + toClass);
    }

    /**
     * Convert a list of objects to a list of <code>T</code>, using
     * {@link #convert(Object, Class)}
     *
     * @param list The list of objects
     * @param type The target type
     * @return The list of converted objects
     */
    public static <T> List<T> convert(List<?> list, Class<? extends T> type) {
        List<T> result = new ArrayList<T>();

        for (Object o : list) {
            result.add(convert(o, type));
        }

        return result;
    }

    private TypeUtils() {}
}
