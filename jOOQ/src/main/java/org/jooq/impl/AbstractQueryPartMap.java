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
package org.jooq.impl;

import static org.jooq.impl.QOM.tuple;
import static org.jooq.impl.QOM.unmodifiable;
import static org.jooq.impl.Tools.map;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jooq.Context;
import org.jooq.QueryPart;
// ...
// ...
import org.jooq.impl.QOM.Tuple2;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
abstract sealed class AbstractQueryPartMap<K extends QueryPart, V extends QueryPart>
extends
    AbstractQueryPart
implements
    QOM.UnmodifiableMap<K, V>
permits
    FieldMapForUpdate,
    QueryPartMapView
{

    private final Map<K, V> map;

    AbstractQueryPartMap() {
        this(new LinkedHashMap<>());
    }

    AbstractQueryPartMap(Map<K, V> map) {
        this.map = map;
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
    public /* non-final */ V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public final V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public final void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
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

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<Tuple2<K, V>> $tuples() {
        return unmodifiable(map(entrySet(), e -> tuple(e.getKey(), e.getValue())));
    }

    abstract Function<? super Map<K, V>, ? extends AbstractQueryPartMap<K, V>> $construct();



























}
