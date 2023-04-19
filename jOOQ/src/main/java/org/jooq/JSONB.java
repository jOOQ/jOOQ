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
import java.util.Objects;

import org.jooq.tools.json.JSONParser;
import org.jooq.tools.json.JSONValue;
import org.jooq.tools.json.ParseException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A JSON wrapper type for JSONB data obtained from the database.
 * <p>
 * The wrapper represents JSONB {@link #data()} in serialised string form. A
 * <code>CAST(NULL AS JSONB)</code> value is represented by a <code>null</code>
 * reference of type {@link JSONB}, not as <code>data() == null</code>. This is
 * consistent with jOOQ's general way of returning <code>NULL</code> from
 * {@link Result} and {@link Record} methods.
 * <p>
 * Unlike the purely text based, formatting-preserving {@link JSON} data type,
 * the {@link JSONB} type uses a normalised representation of the JSON content,
 * meaning that e.g. the following two documents are equal, despite their
 * different object attribute order and formatting:
 * <p>
 * <ul>
 * <li><code>{"a":1,"b":2}</code></li>
 * <li><code>{"b": 2, "a": 1}</code></li>
 * </ul>
 * <p>
 * This impacts the behaviour (and performance!) of
 * <ul>
 * <li>{@link #equals(Object)}</li>
 * <li>{@link #hashCode()}</li>
 * <li>{@link #toString()}</li>
 * </ul>
 * <p>
 * The {@link #data()} content, however, is not normalised.
 */
public final class JSONB implements Serializable {

    private final String     data;
    private transient Object parsed;

    private JSONB(String data) {
        this.data = String.valueOf(data);
    }

    @NotNull
    public final String data() {
        return data;
    }

    /**
     * Create a new {@link JSONB} instance from string data input.
     */
    @NotNull
    public static final JSONB valueOf(String data) {
        return new JSONB(data);
    }

    /**
     * Create a new {@link JSONB} instance from string data input.
     * <p>
     * This is the same as {@link #valueOf(String)}, but it can be static
     * imported.
     */
    @NotNull
    public static final JSONB jsonb(String data) {
        return new JSONB(data);
    }

    /**
     * Create a new {@link JSONB} instance from string data input, or
     * <code>null</code> if the input is <code>null</code>.
     */
    @Nullable
    public static final JSONB jsonbOrNull(String data) {
        return data == null ? null : jsonb(data);
    }

    private final Object parsed() {
        if (parsed == null) {
            try {
                parsed = new JSONParser().parse(data);
            }
            catch (ParseException e) {
                parsed = data;
            }
        }

        return parsed;
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>
     * <h3>{@link JSONB} specifics:</h3>
     * <p>
     * The {@link JSONB} type uses a normalised representation of the JSON
     * content, meaning that two equivalent JSON documents are considered equal
     * (see {@link JSONB} for details). This impacts both behaviour and
     * performance!
     */
    @Override
    public int hashCode() {
        Object p = parsed();
        return p == null ? 0 : p.hashCode();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <h3>{@link JSONB} specifics:</h3>
     * <p>
     * The {@link JSONB} type uses a normalised representation of the JSON
     * content, meaning that two equivalent JSON documents are considered equal
     * (see {@link JSONB} for details). This impacts both behaviour and
     * performance!
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof JSONB j)
            return Objects.equals(parsed(), (j.parsed()));
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <h3>{@link JSONB} specifics:</h3>
     * <p>
     * The {@link JSONB} type uses a normalised representation of the JSON
     * content, meaning that two equivalent JSON documents are considered equal
     * (see {@link JSONB} for details). This impacts both behaviour and
     * performance!
     */
    @Override
    public String toString() {
        return JSONValue.toJSONString(parsed());
    }
}
