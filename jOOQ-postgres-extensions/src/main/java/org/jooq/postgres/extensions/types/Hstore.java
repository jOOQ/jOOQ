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
package org.jooq.postgres.extensions.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A data type representing the PostgreSQL <code>hstore</code> type.
 *
 * @author Lukas Eder
 */
public final class Hstore implements Serializable {

    private static final long serialVersionUID = 860591239448066408L;
    private final Map<String, String> data;

    private Hstore(Map<String, String> data) {
        this.data = data == null ? new HashMap<>() : data;
    }

    @NotNull
    public final Map<String, String> data() {
        return data;
    }

    /**
     * Create a new {@link Hstore} instance from string data input.
     */
    @NotNull
    public static final Hstore valueOf(Map<String, String> data) {
        return new Hstore(data);
    }

    /**
     * Create a new {@link Hstore} instance from map data input.
     * <p>
     * This is the same as {@link #valueOf(Map)}, but it can be static imported.
     */
    @NotNull
    public static final Hstore hstore(Map<String, String> data) {
        return new Hstore(data);
    }

    /**
     * Create a new {@link Hstore} instance from string data input, or
     * <code>null</code> if the input is <code>null</code>.
     */
    @Nullable
    public static final Hstore hstoreOrNull(Map<String, String> data) {
        return data == null ? null : new Hstore(data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Hstore)
            return data.equals(((Hstore) obj).data);
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }
}
