/*
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jooq.Context;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
abstract class AbstractQueryPartMap<K extends QueryPart, V extends QueryPart>
extends AbstractQueryPart
implements Map<K, V> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -8751499214223081415L;

    private final Map<K, V> map;

    AbstractQueryPartMap() {
        map = new LinkedHashMap<K, V>();
    }

    // -------------------------------------------------------------------------
    // The QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public abstract void accept(Context<?> context);

    // -------------------------------------------------------------------------
    // The Map API
    // -------------------------------------------------------------------------

    @Override
    public final int size() {
        return map.size();
    }

    @Override
    public final boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public final boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public final boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public final V get(Object key) {
        return map.get(key);
    }

    @Override
    public final V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public final V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public final void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public final void clear() {
        map.clear();
    }

    @Override
    public final Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public final Collection<V> values() {
        return map.values();
    }

    @Override
    public final Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
