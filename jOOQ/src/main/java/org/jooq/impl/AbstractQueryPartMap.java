/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
