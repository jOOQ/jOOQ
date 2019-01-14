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
package org.jooq.impl;

import static java.lang.Boolean.TRUE;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jooq.Scope;
import org.jooq.impl.Tools.BooleanDataKey;
import org.jooq.impl.Tools.DataKey;

/**
 * The {@link Map} implementation for use with {@link Scope#data()}.
 *
 * @author Lukas Eder
 */
final class DataMap extends AbstractMap<Object, Object> {

    final EnumSet<BooleanDataKey>    internalSet;
    final EnumMap<DataKey, Object>   internalMap;
    Map<Object, Object>              externalMap;
    final Set<Entry<Object, Object>> entrySet;

    DataMap() {
        internalSet = EnumSet.noneOf(BooleanDataKey.class);
        internalMap = new EnumMap<DataKey, Object>(DataKey.class);
        entrySet = new EntrySet();
    }

    @Override
    public final int size() {
        return internalSet.size() + internalMap().size() + external(false).size();
    }

    @Override
    public final boolean isEmpty() {
        return internalSet.isEmpty() && internalMap().isEmpty() && external(false).isEmpty();
    }

    @Override
    public final boolean containsKey(Object key) {
        return key instanceof BooleanDataKey
             ? internalSet.contains(key)
             : delegate(key, false).containsKey(key);
    }

    @Override
    public final boolean containsValue(Object value) {
        if (value instanceof Boolean)
            if ((Boolean) value && internalSet.size() > 0)
                return true;
            else if (!((Boolean) value) && internalSet.size() < BooleanDataKey.values().length)
                return true;

        return internalMap().containsValue(value) || external(false).containsValue(value);
    }

    @Override
    public final Object get(Object key) {
        return key instanceof BooleanDataKey
             ? internalSet.contains(key)
                 ? (Boolean) true
                 : (Boolean) null
             : delegate(key, false).get(key);
    }

    @Override
    public final Object put(Object key, Object value) {
        return key instanceof BooleanDataKey
             ? TRUE.equals(value)
                 ? internalSet.add((BooleanDataKey) key)
                     ? (Boolean) null
                     : TRUE
                 : internalSet.remove(key)
                     ? TRUE
                     : (Boolean) null
             : delegate(key, true).put(key, value);
    }

    @Override
    public final Object remove(Object key) {
        return key instanceof BooleanDataKey
             ? internalSet.remove(key)
                 ? TRUE
                 : (Boolean) null
             : delegate(key, true).remove(key);
    }

    @Override
    public final void clear() {
        internalSet.clear();
        internalMap().clear();
        external(true).clear();
    }

    @Override
    public final Set<Entry<Object, Object>> entrySet() {
        return entrySet;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final Map<Object, Object> internalMap() {
        return (Map) internalMap;
    }

    private final Map<Object, Object> external(boolean initialise) {
        if (externalMap == null) {
            if (initialise)
                externalMap = new HashMap<Object, Object>();
            else
                return Collections.emptyMap();
        }

        return externalMap;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final Map<Object, Object> delegate(Object key, boolean initialise) {
        return key instanceof DataKey ? (Map) internalMap() : external(initialise);
    }

    private class EntrySet extends AbstractSet<Entry<Object, Object>> {
        @Override
        public final Iterator<Entry<Object, Object>> iterator() {
            return new Iterator<Entry<Object, Object>>() {
                final Iterator<BooleanDataKey>        internalSetIterator = internalSet.iterator();
                final Iterator<Entry<Object, Object>> internalMapIterator = internalMap().entrySet().iterator();
                final Iterator<Entry<Object, Object>> externalMapIterator = external(false).entrySet().iterator();

                @Override
                public final boolean hasNext() {
                    return internalSetIterator.hasNext()
                        || internalMapIterator.hasNext()
                        || externalMapIterator.hasNext();
                }

                @Override
                public final Entry<Object, Object> next() {
                    return internalSetIterator.hasNext()
                         ? new SimpleImmutableEntry<Object, Object>(internalSetIterator.next(), true)
                         : internalMapIterator.hasNext()
                         ? internalMapIterator.next()
                         : externalMapIterator.next();
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
