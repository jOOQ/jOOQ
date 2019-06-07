/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.jooq.tools;

import java.util.Arrays;

/**
 * Static utility methods pertaining to {@code long} primitives, that are not already found in
 * either {@link Long} or {@link Arrays}.
 *
 * <p>See the Guava User Guide article on <a
 * href="https://github.com/google/guava/wiki/PrimitivesExplained">primitive utilities</a>.
 *
 * @author Kevin Bourrillion
 * @since 1.0
 */
public final class Longs {
    private Longs() {}

    // adapted from com.google.common.primitives.Longs#tryParse()
    // modified for performance and to allow parsing numbers with leading '+'
    public static Long tryParse(String string) {
        if (string.isEmpty())
            return null;

        int radix = 10;
        char firstChar = string.charAt(0);
        boolean negative = firstChar == '-';
        int index = negative || firstChar == '+' ? 1 : 0;
        int length = string.length();
        if (index == length)
            return null;

        int digit = Character.digit(string.charAt(index++), 10);
        if (digit < 0 || digit >= radix)
            return null;

        long accum = -digit;

        long cap = Long.MIN_VALUE / radix;

        while (index < length) {
            digit = Character.digit(string.charAt(index++), 10);
            if (digit < 0 || digit >= radix || accum < cap)
                return null;

            accum *= radix;
            if (accum < Long.MIN_VALUE + digit)
                return null;

            accum -= digit;
        }

        if (negative)
            return accum;
        else if (accum == Long.MIN_VALUE)
            return null;
        else
            return -accum;
    }

}
