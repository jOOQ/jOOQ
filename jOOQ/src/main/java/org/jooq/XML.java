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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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
package org.jooq;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An XML wrapper type for XML data obtained from the database.
 * <p>
 * The wrapper represents XML {@link #data()} in serialised string form. A
 * <code>CAST(NULL AS XML)</code> value is represented by a <code>null</code>
 * reference of type {@link XML}, not as <code>data() == null</code>. This is
 * consistent with jOOQ's general way of returning <code>NULL</code> from
 * {@link Result} and {@link Record} methods.
 */
public final class XML implements Serializable {

    private final String data;

    private XML(String data) {
        this.data = String.valueOf(data);
    }

    @NotNull
    public final String data() {
        return data;
    }

    /**
     * Create a new {@link XML} instance from string data input.
     */
    @NotNull
    public static final XML valueOf(String data) {
        return new XML(data);
    }

    /**
     * Create a new {@link XML} instance from string data input.
     * <p>
     * This is the same as {@link #valueOf(String)}, but it can be static
     * imported.
     */
    @NotNull
    public static final XML xml(String data) {
        return new XML(data);
    }

    /**
     * Create a new {@link XML} instance from string data input, or
     * <code>null</code> if the input is <code>null</code>.
     */
    @Nullable
    public static final XML xmlOrNull(String data) {
        return data == null ? null : xml(data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof XML x)
            return data.equals(x.data);
        return false;

    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }
}
