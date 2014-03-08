/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.impl;

import java.io.Serializable;

/**
 * @author Lukas Eder
 */
class Value<T> implements Serializable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -9065797545428164533L;
    private T                 original;
    private T                 value;
    private boolean           isChanged;

    Value(T value) {
        this(value, value, false);
    }

    Value(T value, T original, boolean isChanged) {
        this.value = value;
        this.original = original;
        this.isChanged = isChanged;
    }

    final T getValue() {
        return value;
    }

    final T getValue(T defaultValue) {
        return value != null ? value : defaultValue;
    }

    final T getOriginal() {
        return original;
    }

    @SuppressWarnings("unchecked")
    final void intern() {

        // [#2177] Future versions of jOOQ may optimise this type check by
        // performing type-decisions outside of Value
        if (value instanceof String) {
            value = (T) ((String) value).intern();
        }
    }

    final void setValue(T val) {

        // The flag is always set to false:
        // [#945] To avoid bugs resulting from setting the same value twice
        // [#948] To allow for controlling the number of hard-parses
        //        To allow for explicitly overriding default values
        setValue(val, false);
    }

    final void setValue(T val, boolean primaryKey) {

        // [#948] Force setting of val in most cases, to allow for controlling
        // the number of necessary hard-parses, and to allow for explicitly
        // overriding default values with null
        if (!primaryKey) {
            isChanged = true;
        }

        // [#979] Avoid modifying isChanged on unchanged primary key values
        else {

            // [#945] Be sure that isChanged is never reset to false
            if (value == null) {
                isChanged = isChanged || (val != null);
            }
            else {
                isChanged = isChanged || (!value.equals(val));
            }
        }

        value = val;
    }

    final boolean isChanged() {
        return isChanged;
    }

    final void setChanged(boolean isChanged) {
        this.isChanged = isChanged;

        // [#1995] If a value is meant to be "unchanged", the "original" should
        // match the supposedly "unchanged" value.
        if (!isChanged) {
            original = value;
        }
    }

    final void reset() {
        isChanged = false;
        value = original;
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Value<?>) {
            Value<?> other = (Value<?>) obj;

            if (value == null) {
                return other.getValue() == null;
            }

            return value.equals(other.getValue());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return 0;
        }

        return value.hashCode();
    }

    @Override
    public String toString() {
        if (isChanged) {
            return "*" + value;
        }
        else {
            return "" + value;
        }
    }
}
