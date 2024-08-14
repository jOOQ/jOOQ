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
package org.jooq;

import java.math.BigDecimal;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper type for SQL:2016 standard <code>DECFLOAT</code> data types.
 * <p>
 * The wrapper represents DECFLOAT {@link #data()} in serialised string form. A
 * <code>CAST(NULL AS DECFLOAT)</code> value is represented by a
 * <code>null</code> reference of type {@link Decfloat}, not as
 * <code>data() == null</code>. This is consistent with jOOQ's general way of
 * returning <code>NULL</code> from {@link Result} and {@link Record} methods.
 */
public final class Decfloat extends Number implements Data {

    private final String         data;
    private transient BigDecimal coefficient;
    private transient int        exponent;
    private transient Special    special;

    private Decfloat(String data) {
        this.data = String.valueOf(data);
    }

    @Override
    @NotNull
    public final String data() {
        return data;
    }

    /**
     * Create a new {@link Decfloat} instance from string data input.
     */
    @NotNull
    public static final Decfloat valueOf(String data) {
        return new Decfloat(data);
    }

    /**
     * Create a new {@link Decfloat} instance from string data input.
     * <p>
     * This is the same as {@link #valueOf(String)}, but it can be static
     * imported.
     */
    @NotNull
    public static final Decfloat decfloat(String data) {
        return new Decfloat(data);
    }

    /**
     * Create a new {@link Decfloat} instance from string data input, or
     * <code>null</code> if the input is <code>null</code>.
     */
    @Nullable
    public static final Decfloat decfloatOrNull(String data) {
        return data == null ? null : decfloat(data);
    }

    // ------------------------------------------------------------------------
    // The Number API
    // ------------------------------------------------------------------------

    @Override
    public final double doubleValue() {
        return Double.valueOf(data);
    }

    @Override
    public final float floatValue() {
        return Float.valueOf(data);
    }

    @Override
    public final int intValue() {
        return (int) doubleValue();
    }

    @Override
    public final long longValue() {
        return (long) doubleValue();
    }

    // ------------------------------------------------------------------------
    // The Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        parse();

        if (special != null) {
            return special.hashCode();
        }
        else if (coefficient != null) {
            final int prime = 31;
            int result = 1;

            result = prime * result + ((coefficient == null) ? 0 : coefficient.hashCode());
            result = prime * result + exponent;

            return result;
        }
        else
            return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Decfloat x) {
            parse();
            x.parse();

            if (special != null && x.special != null)
                return special == x.special;
            else if (coefficient != null && x.coefficient != null)
                return Objects.equals(coefficient, x.coefficient) && exponent == x.exponent;
            else
                return Objects.equals(data, x.data);
        }
        return false;

    }

    @Override
    public String toString() {
        parse();

        if (special != null) {
            switch (special) {
                case NAN:
                    return "NaN";
                case POSITIVE_INFINITY:
                    return "Infinity";
                case NEGATIVE_INFINITY:
                    return "-Infinity";
            }
        }
        else if (coefficient != null)
            return coefficient + "E" + exponent;

        return String.valueOf(data);
    }

    private final void parse() {
        if (coefficient != null || special != null)
            return;

        switch (data) {
            case "+NaN":
            case "NaN":
                special = Special.NAN;
                break;

            case "+Infinity":
            case "Infinity":
            case "+Inf":
            case "Inf":
                special = Special.POSITIVE_INFINITY;
                break;

            case "-Infinity":
            case "-Inf":
                special = Special.NEGATIVE_INFINITY;
                break;

            default: {
                int i = data.indexOf("E");
                if (i == -1)
                    i = data.indexOf("e");

                try {
                    coefficient = new BigDecimal(data.substring(0, i)).stripTrailingZeros();
                    exponent = Integer.parseInt(data.substring(i + 1));
                }

                // [#10880] If we cannot represent the value internally, then we'll just work with data
                catch (NumberFormatException ignore) {}
                break;
            }
        }
    }

    private enum Special {
        NAN, POSITIVE_INFINITY, NEGATIVE_INFINITY
    }
}
