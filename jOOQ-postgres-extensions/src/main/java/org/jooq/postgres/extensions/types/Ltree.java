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
package org.jooq.postgres.extensions.types;

import static java.util.Objects.requireNonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A data type representing the PostgreSQL <code>ltree</code> type.
 *
 * @author Lukas Eder
 */
public final class Ltree {

    private final String data;

    private Ltree(String data) {
        requireNonNull(data);

        this.data = data;
    }

    @NotNull
    public final String data() {
        return data;
    }

    /**
     * Create a new {@link Ltree} instance from string data input.
     */
    @NotNull
    public static final Ltree valueOf(String data) {
        return new Ltree(data);
    }

    /**
     * Create a new {@link Ltree} instance from string data input.
     * <p>
     * This is the same as {@link #valueOf(String)}, but it can be static imported.
     */
    @NotNull
    public static final Ltree ltree(String data) {
        return new Ltree(data);
    }

    /**
     * Create a new {@link Ltree} instance from string data input, or
     * <code>null</code> if the input is <code>null</code>.
     */
    @Nullable
    public static final Ltree ltreeOrNull(String data) {
        return data == null ? null : new Ltree(data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Ltree)
            return data.equals(((Ltree) obj).data);
        return false;
    }

    @Override
    public String toString() {
        return data;
    }
}
