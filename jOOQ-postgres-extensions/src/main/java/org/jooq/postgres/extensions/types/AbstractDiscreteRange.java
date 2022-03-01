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

import java.util.Objects;

import org.jooq.exception.DataTypeException;

/**
 * A data type representing the PostgreSQL <code>range</code> type for discrete ranges.
 *
 * @author Lukas Eder
 */
abstract class AbstractDiscreteRange<T, R extends AbstractDiscreteRange<T, R>> extends AbstractRange<T> {

    AbstractDiscreteRange(T lower, boolean lowerIncluding, T upper, boolean upperIncluding) {
        super(lower, lowerIncluding, upper, upperIncluding);
    }

    /**
     * Given a value t, get the next value.
     *
     * @throws DataTypeException on overflow.
     */
    abstract T next(T t) throws DataTypeException;

    /**
     * Given a value t, get the previous value.
     *
     * @throws DataTypeException on underflow.
     */
    abstract T prev(T t) throws DataTypeException;

    /**
     * Construct a new instance of this type.
     */
    abstract R construct(T lower, T upper);

    final boolean isCanonical() {
        return lowerIncluding() && !upperIncluding();
    }

    @SuppressWarnings("unchecked")
    final R canonical() {
        if (isCanonical())
            return (R) this;

        T l = lower();
        T u = upper();

        if (!lowerIncluding() && l != null)
            l = next(l);
        if (upperIncluding() && u != null)
            u = next(u);

        return construct(l, u);
    }

    @Override
    public int hashCode() {
        if (isCanonical())
            return Objects.hash(lower(), upper());
        else
            return canonical().hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        R other = (R) obj;
        boolean c1 = isCanonical();
        boolean c2 = other.isCanonical();

        R r1 = c1 ? (R) this : canonical();
        R r2 = c2 ? other : other.canonical();

        return Objects.equals(r1.lower(), r2.lower()) && Objects.equals(r1.upper(), r2.upper());
    }
}
