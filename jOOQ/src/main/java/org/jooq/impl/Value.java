/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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

        // The flag is always set to true:
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
