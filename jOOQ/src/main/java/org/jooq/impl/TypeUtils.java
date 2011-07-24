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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jooq.SQLDialectNotSupportedException;

/**
 * Utility methods for type conversions
 *
 * @author Lukas Eder
 */
final class TypeUtils {

    private static final Set<String> TRUE_VALUES;
    private static final Set<String> FALSE_VALUES;

    static {
        TRUE_VALUES = new HashSet<String>();
        FALSE_VALUES = new HashSet<String>();

        TRUE_VALUES.add("1");
        TRUE_VALUES.add("y");
        TRUE_VALUES.add("yes");
        TRUE_VALUES.add("true");
        TRUE_VALUES.add("on");
        TRUE_VALUES.add("enabled");

        FALSE_VALUES.add("0");
        FALSE_VALUES.add("n");
        FALSE_VALUES.add("no");
        FALSE_VALUES.add("false");
        FALSE_VALUES.add("off");
        FALSE_VALUES.add("disabled");
    }

    @SuppressWarnings("unchecked")
    public static Object[] convertArray(Object[] from, Class<?> toClass) {
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
                return (T) Byte.valueOf(new BigDecimal(from.toString()).byteValue());
            }
            else if (toClass == Short.class) {
                return (T) Short.valueOf(new BigDecimal(from.toString()).shortValue());
            }
            else if (toClass == Integer.class) {
                return (T) Integer.valueOf(new BigDecimal(from.toString()).intValue());
            }
            else if (toClass == Long.class) {
                return (T) Long.valueOf(new BigDecimal(from.toString()).longValue());
            }
            else if (toClass == Float.class) {
                return (T) Float.valueOf(from.toString());
            }
            else if (toClass == Double.class) {
                return (T) Double.valueOf(from.toString());
            }
            else if (toClass == BigDecimal.class) {
                return (T) new BigDecimal(from.toString());
            }
            else if (toClass == BigInteger.class) {
                return (T) new BigDecimal(from.toString()).toBigInteger();
            }
            else if (toClass == Boolean.class) {
                String s = from.toString().toLowerCase();

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

    private TypeUtils() {}
}
