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
 * Static utility methods pertaining to {@code int} primitives, that are not already found in either
 * {@link Integer} or {@link Arrays}.
 *
 * <p>See the Guava User Guide article on <a
 * href="https://github.com/google/guava/wiki/PrimitivesExplained">primitive utilities</a>.
 *
 * @author Kevin Bourrillion
 * @since 1.0
 */
public final class Ints {
    private Ints() {}

    public static Integer tryParse(String string) {
        return tryParse(string, 0, string.length());
    }

    // adapted from com.google.common.primitives.Longs#tryParse()
    // modified for performance and to allow parsing numbers with leading '+'
    public static Integer tryParse(String string, int begin, int end) {
        if (begin < 0 || end > string.length() || end - begin < 1)
            return null;

        int radix = 10;
        char firstChar = string.charAt(begin);
        boolean negative = firstChar == '-';
        int index = negative || firstChar == '+' ? begin + 1 : begin;
        if (index == end)
            return null;

        int digit = Character.digit(string.charAt(index++), 10);
        if (digit < 0 || digit >= radix)
            return null;

        int accum = -digit;

        int cap = Integer.MIN_VALUE / radix;

        while (index < end) {
            digit = Character.digit(string.charAt(index++), 10);
            if (digit < 0 || digit >= radix || accum < cap)
                return null;

            accum *= radix;
            if (accum < Integer.MIN_VALUE + digit)
                return null;

            accum -= digit;
        }

        if (negative)
            return accum;
        else if (accum == Integer.MIN_VALUE)
            return null;
        else
            return -accum;
    }


}
