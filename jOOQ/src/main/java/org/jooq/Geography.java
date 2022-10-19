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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper type for spatial data obtained from the database.
 * <p>
 * The wrapper represents spatial {@link #data()} in serialised string form
 * either as a well known text (WKT) or well known binary in hex format (WKB),
 * depending on your dialect's default behaviour. A
 * <code>CAST(NULL AS GEOGRAPHY)</code> value is represented by a
 * <code>null</code> reference of type {@link Geography}, not as
 * <code>data() == null</code>. This is consistent with jOOQ's general way of
 * returning <code>NULL</code> from {@link Result} and {@link Record} methods.
 * <p>
 * This data type is supported only by the commercial editions of jOOQ.
 */
public final class Geography implements Spatial {

    private final String data;

    private Geography(String data) {
        this.data = String.valueOf(data);
    }

    @Override
    @NotNull
    public final String data() {
        return data;
    }

    /**
     * Create a new {@link Geography} instance from string data input.
     */
    @NotNull
    public static final Geography valueOf(String data) {
        return new Geography(data);
    }

    /**
     * Create a new {@link Geography} instance from string data input.
     * <p>
     * This is the same as {@link #valueOf(String)}, but it can be static
     * imported.
     */
    @NotNull
    public static final Geography geography(String data) {
        return new Geography(data);
    }

    /**
     * Create a new {@link Geography} instance from string data input, or
     * <code>null</code> if the input is <code>null</code>.
     */
    @Nullable
    public static final Geography geographyOrNull(String data) {
        return data == null ? null : geography(data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Geography j)
            return data.equals(j.data);
        return false;

    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }
}

