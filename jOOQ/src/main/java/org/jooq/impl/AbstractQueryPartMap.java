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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jooq.BindContext;
import org.jooq.QueryPart;
import org.jooq.RenderContext;

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
    public abstract void toSQL(RenderContext context);

    @Override
    public abstract void bind(BindContext context);

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
