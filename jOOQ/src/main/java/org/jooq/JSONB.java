/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: http://www.jooq.org/licenses
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

    @Override
    public int hashCode() {
        Object p = parsed();
        return p == null ? 0 : p.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof JSONB)
            return Objects.equals(parsed(), (((JSONB) obj).parsed()));
        return false;
    }

    @Override
    public String toString() {
        return JSONValue.toJSONString(parsed());
    }
}
