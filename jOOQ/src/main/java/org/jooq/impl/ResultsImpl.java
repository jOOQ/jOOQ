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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Results;

/**
 * @author Lukas Eder
 */
class ResultsImpl implements Results, AttachableInternal {

    /**
     * Generated UID
     */
    private static final long          serialVersionUID = 1744826140354980500L;

    private Configuration              configuration;
    private final List<Result<Record>> results;

    ResultsImpl(Configuration configuration) {
        this.configuration = configuration;
        this.results = new ArrayList<Result<Record>>();
    }

    // -------------------------------------------------------------------------
    // XXX: Attachable API
    // -------------------------------------------------------------------------

    @Override
    public final void attach(Configuration c) {
        this.configuration = c;

        for (Result<?> result : results)
            if (result != null)
                result.attach(c);
    }

    @Override
    public final void detach() {
        attach(null);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    // -------------------------------------------------------------------------
    // XXX Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String separator = "";
        for (Result<?> result : this) {
            sb.append(separator)
              .append(result);

            separator = "\n";
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return results.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ResultsImpl) {
            ResultsImpl other = (ResultsImpl) obj;
            return results.equals(other.results);
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // XXX: List API
    // -------------------------------------------------------------------------

    @Override
    public final int size() {
        return results.size();
    }

    @Override
    public final boolean isEmpty() {
        return results.isEmpty();
    }

    @Override
    public final boolean contains(Object o) {
        return results.contains(o);
    }

    @Override
    public final Iterator<Result<Record>> iterator() {
        return results.iterator();
    }

    @Override
    public final Object[] toArray() {
        return results.toArray();
    }

    @Override
    public final <T> T[] toArray(T[] a) {
        return results.toArray(a);
    }

    @Override
    public final boolean add(Result<Record> e) {
        return results.add(e);
    }

    @Override
    public final boolean remove(Object o) {
        return results.remove(o);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return results.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends Result<Record>> c) {
        return results.addAll(c);
    }

    @Override
    public final boolean addAll(int index, Collection<? extends Result<Record>> c) {
        return results.addAll(index, c);
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return results.removeAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        return results.retainAll(c);
    }

    @Override
    public final void clear() {
        results.clear();
    }

    @Override
    public final Result<Record> get(int index) {
        return results.get(index);
    }

    @Override
    public final Result<Record> set(int index, Result<Record> element) {
        return results.set(index, element);
    }

    @Override
    public final void add(int index, Result<Record> element) {
        results.add(index, element);
    }

    @Override
    public final Result<Record> remove(int index) {
        return results.remove(index);
    }

    @Override
    public final int indexOf(Object o) {
        return results.indexOf(o);
    }

    @Override
    public final int lastIndexOf(Object o) {
        return results.lastIndexOf(o);
    }

    @Override
    public final ListIterator<Result<Record>> listIterator() {
        return results.listIterator();
    }

    @Override
    public final ListIterator<Result<Record>> listIterator(int index) {
        return results.listIterator(index);
    }

    @Override
    public final List<Result<Record>> subList(int fromIndex, int toIndex) {
        return results.subList(fromIndex, toIndex);
    }
}
