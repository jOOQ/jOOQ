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
package org.jooq.impl;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jooq.Scope;
import org.jooq.impl.Utils.DataKey;

/**
 * The {@link Map} implementation for use with {@link Scope#data()}.
 *
 * @author Lukas Eder
 */
class DataMap extends AbstractMap<Object, Object> {

    final EnumMap<DataKey, Object>   internal;
    Map<Object, Object>              external;
    final Set<Entry<Object, Object>> entrySet;

    DataMap() {
        internal = new EnumMap<DataKey, Object>(DataKey.class);
        entrySet = new EntrySet();
    }

    @Override
    public final int size() {
        return internal().size() + external(false).size();
    }

    @Override
    public final boolean isEmpty() {
        return internal().isEmpty() && external(false).isEmpty();
    }

    @Override
    public final boolean containsKey(Object key) {
        return delegate(key, false).containsKey(key);
    }

    @Override
    public final boolean containsValue(Object value) {
        return internal().containsValue(value) || external(false).containsValue(value);
    }

    @Override
    public final Object get(Object key) {
        return delegate(key, false).get(key);
    }

    @Override
    public final Object put(Object key, Object value) {
        return delegate(key, true).put(key, value);
    }

    @Override
    public final Object remove(Object key) {
        return delegate(key, true).remove(key);
    }

    @Override
    public final void clear() {
        internal().clear();
        external(true).clear();
    }

    @Override
    public final Set<Entry<Object, Object>> entrySet() {
        return entrySet;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final Map<Object, Object> internal() {
        return (Map) internal;
    }

    private final Map<Object, Object> external(boolean initialise) {
        if (external == null) {
            if (initialise)
                external = new HashMap<Object, Object>();
            else
                return Collections.emptyMap();
        }

        return external;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final Map<Object, Object> delegate(Object key, boolean initialise) {
        return key instanceof DataKey ? (Map) internal() : external(initialise);
    }

    private class EntrySet extends AbstractSet<Entry<Object, Object>> {
        @Override
        public final Iterator<Entry<Object, Object>> iterator() {
            return new Iterator<Entry<Object, Object>>() {
                final Iterator<Entry<Object, Object>> internalIterator = internal().entrySet().iterator();
                final Iterator<Entry<Object, Object>> externalIterator = external(false).entrySet().iterator();

                @Override
                public final boolean hasNext() {
                    return internalIterator.hasNext() || externalIterator.hasNext();
                }

                @Override
                public final Entry<Object, Object> next() {
                    return internalIterator.hasNext() ? internalIterator.next() : externalIterator.next();
                }

                @Override
                public final void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public final int size() {
            return DataMap.this.size();
        }
    }
}
