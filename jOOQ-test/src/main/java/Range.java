/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */


import java.io.Serializable;

/**
 * A type modelling a range of discretely enumerable values.
 *
 * @author Lukas Eder
 */
public class Range<T extends Comparable<T>> implements Serializable, Comparable<Range<T>> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2122093758769942367L;

    private final T lower;
    private final T upper;

    private Range(T lower, T upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public static final <T extends Comparable<T>> Range<T> range(T lower, T upper) {
        return new Range<T>(lower, upper);
    }

    public static final <T extends Comparable<T>> Range<T> infinite() {
        return range(null, null);
    }

    public static final <T extends Comparable<T>> Range<T> above(T lower) {
        return range(lower, null);
    }

    public static final <T extends Comparable<T>> Range<T> below(T upper) {
        return range(null, upper);
    }

    public final T lower() {
        return lower;
    }

    public final T upper() {
        return upper;
    }

    @Override
    public int compareTo(Range<T> o) {
        if (lower == null && o.lower == null)
            return 0;
        if (lower == null && o.lower != null)
            return -1;
        if (lower != null && o.lower == null)
            return 1;

        int result = lower.compareTo(o.lower);
        if (result != 0)
            return result;

        if (upper == null && o.upper == null)
            return 0;
        if (upper == null && o.upper != null)
            return -1;
        if (upper != null && o.upper == null)
            return 1;

        return upper.compareTo(o.upper);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (lower == null)
            sb.append('(');
        else
            sb.append('[').append(lower);

        sb.append(',');

        if (upper != null)
            sb.append(upper);

        sb.append(")");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lower == null) ? 0 : lower.hashCode());
        result = prime * result + ((upper == null) ? 0 : upper.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        @SuppressWarnings("unchecked")
        Range<T> other = (Range<T>) obj;
        if (lower == null) {
            if (other.lower != null)
                return false;
        }
        else if (!lower.equals(other.lower))
            return false;
        if (upper == null) {
            if (other.upper != null)
                return false;
        }
        else if (!upper.equals(other.upper))
            return false;
        return true;
    }
}
