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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
 * <p>
 * Some JDBC drivers (e.g. pgjdbc) can produce the JSON document as UTF-8
 * encoded bytes. In that case, the {@link String} {@link #data()} is decoded
 * only when it is first requested, and parsers that work on UTF-8 bytes can
 * consume the document through {@link #inputStream()} or {@link #bytes()}
 * without an intermediate {@link String}.
 */
public final class JSON implements Data {

    /**
     * [#20504] Either a {@link String} or a UTF-8 encoded <code>byte[]</code>.
     * <p>
     * A <code>byte[]</code> is replaced by its decoded {@link String} once
     * {@link #data()} is called, to avoid holding both representations in
     * memory. Concurrent calls may decode the same <code>byte[]</code> more
     * than once (a benign data race, like in {@link String#hashCode()}).
     */
    private Object data;

    private JSON(String data) {
        this.data = String.valueOf(data);
    }

    private JSON(byte[] data) {
        this.data = data;
    }

    @Override
    @NotNull
    public final String data() {
        Object d = data;

        if (d instanceof String s)
            return s;

        String s = new String((byte[]) d, UTF_8);
        data = s;
        return s;
    }

    /**
     * Get the UTF-8 encoded {@link #data()} of this JSON document.
     * <p>
     * The returned array is a copy, which can be modified freely. Use
     * {@link #inputStream()} to read the bytes without copying them, if this
     * {@link JSON} value was produced from bytes.
     */
    public final byte @NotNull [] bytes() {
        Object d = data;

        if (d instanceof byte[] b)
            return b.clone();
        else
            return ((String) d).getBytes(UTF_8);
    }

    /**
     * Get the UTF-8 encoded {@link #data()} of this JSON document as an
     * {@link InputStream}.
     * <p>
     * If this {@link JSON} value was produced from bytes (e.g. by the
     * PostgreSQL JDBC driver), and its {@link #data()} hasn't been requested
     * yet, then the stream reads the bytes directly, without decoding them to
     * a {@link String} or copying them. Otherwise, the {@link String}
     * {@link #data()} is encoded first.
     */
    @NotNull
    public final InputStream inputStream() {
        Object d = data;

        if (d instanceof byte[] b)
            return new ImmutableBytesInputStream(b);
        else
            return new ByteArrayInputStream(((String) d).getBytes(UTF_8));
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
     * Create a new {@link JSON} instance from UTF-8 encoded byte data input,
     * or <code>null</code> if the input is <code>null</code>.
     * <p>
     * The bytes are decoded to a {@link String} lazily, when {@link #data()}
     * is first called. The array is not copied, so the caller must not modify
     * it after calling this method.
     */
    @Nullable
    public static final JSON jsonFromBytes(byte[] data) {
        return data == null ? null : new JSON(data);
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
        return data().hashCode();
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
            return data().equals(j.data());
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
        return data();
    }
}
