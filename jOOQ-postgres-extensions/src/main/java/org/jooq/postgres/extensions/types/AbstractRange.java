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
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

/**
 * A data type representing the PostgreSQL <code>range</code> type.
 *
 * @author Lukas Eder
 */
public abstract class AbstractRange<T> implements Serializable {

    private final T       lower;
    private final boolean lowerIncluding;
    private final T       upper;
    private final boolean upperIncluding;

    AbstractRange(T lower, boolean lowerIncluding, T upper, boolean upperIncluding) {
        this.lower = lower;
        this.lowerIncluding = lowerIncluding;
        this.upper = upper;
        this.upperIncluding = upperIncluding;
    }

    @Nullable
    public final T lower() {
        return lower;
    }

    public final boolean lowerIncluding() {
        return lowerIncluding;
    }

    @Nullable
    public final T upper() {
        return upper;
    }

    public final boolean upperIncluding() {
        return upperIncluding;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, lowerIncluding, upper, upperIncluding);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractRange<?> other = (AbstractRange<?>) obj;
        return Objects.equals(lower, other.lower) && lowerIncluding == other.lowerIncluding
            && Objects.equals(upper, other.upper) && upperIncluding == other.upperIncluding;
    }

    @Override
    public String toString() {
        return (lowerIncluding ? "[" : "(")
             + (lower == null ? "" : "" + lower)
             + ","
             + (upper == null ? "" : "" + upper)
             + (upperIncluding ? "]" : ")");
    }
}
