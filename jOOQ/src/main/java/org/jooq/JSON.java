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
 * A JSON wrapper type for JSON data obtained from the database.
 * <p>
 * The wrapper represents JSON {@link #data()} in serialised string form. A
 * <code>CAST(NULL AS JSON)</code> value is represented by a <code>null</code>
 * reference of type {@link JSON}, not as <code>data() == null</code>. This is
 * consistent with jOOQ's general way of returning <code>NULL</code> from
 * {@link Result} and {@link Record} methods.
 * <p>
 * Unlike the normalising {@link JSONB} data type, the {@link JSON} type uses a
 * purely text based, formatting-preserving representation of the JSON content,
 * meaning that e.g. the following two documents are <em>not</em> equal, due to
 * their different object attribute order and formatting:
 * <p>
 * <ul>
 * <li><code>{"a":1,"b":2}</code></li>
 * <li><code>{"b": 2, "a": 1}</code></li>
 * </ul>
 * <p>
 * This impacts the behaviour of
 * <ul>
 * <li>{@link #equals(Object)}</li>
 * <li>{@link #hashCode()}</li>
 * <li>{@link #toString()}</li>
 * </ul>
 */
public final class JSON implements Serializable {

    private final String data;

    private JSON(String data) {
        this.data = String.valueOf(data);
    }

    @NotNull
    public final String data() {
        return data;
    }

    /**
     * Create a new {@link JSON} instance from string data input.
     */
    @NotNull
    public static final JSON valueOf(String data) {
        return new JSON(data);
    }

    /**
     * Create a new {@link JSON} instance from string data input.
     * <p>
     * This is the same as {@link #valueOf(String)}, but it can be static
     * imported.
     */
    @NotNull
    public static final JSON json(String data) {
        return new JSON(data);
    }

    /**
     * Create a new {@link JSON} instance from string data input, or
     * <code>null</code> if the input is <code>null</code>.
     */
    @Nullable
    public static final JSON jsonOrNull(String data) {
        return data == null ? null : json(data);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <h3>{@link JSON} specifics:</h3>
     * <p>
     * The {@link JSON} type uses a text-based, formatting-preserving
     * representation of the JSON content, meaning that two equivalent JSON
     * documents are considered not equal if their formatting or object
     * attribute ordering differs (see {@link JSON} for details).
     */
    @Override
    public int hashCode() {
        return data.hashCode();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <h3>{@link JSON} specifics:</h3>
     * <p>
     * The {@link JSON} type uses a text-based, formatting-preserving
     * representation of the JSON content, meaning that two equivalent JSON
     * documents are considered not equal if their formatting or object
     * attribute ordering differs (see {@link JSON} for details).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof JSON j)
            return data.equals(j.data);
        return false;

    }

    /**
     * {@inheritDoc}
     * <p>
     * <h3>{@link JSON} specifics:</h3>
     * <p>
     * The {@link JSON} type uses a text-based, formatting-preserving
     * representation of the JSON content, meaning that two equivalent JSON
     * documents are considered not equal if their formatting or object
     * attribute ordering differs (see {@link JSON} for details).
     */
    @Override
    public String toString() {
        return String.valueOf(data);
    }
}
