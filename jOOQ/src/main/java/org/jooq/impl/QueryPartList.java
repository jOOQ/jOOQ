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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jooq.BindContext;
import org.jooq.QueryPart;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class QueryPartList<T extends QueryPart> extends AbstractQueryPart implements List<T> {

    private static final long serialVersionUID = -2936922742534009564L;
    private final List<T>     wrappedList      = new ArrayList<T>();

    QueryPartList() {
        this((Collection<T>) null);
    }

    QueryPartList(Collection<? extends T> wrappedList) {
        super();

        if (wrappedList != null) {
            addAll(wrappedList);
        }
    }

    QueryPartList(T... wrappedList) {
        this(Arrays.asList(wrappedList));
    }

    @Override
    public final void toSQL(RenderContext context) {

        // Some lists render different SQL when empty
        if (isEmpty()) {
            toSQLEmptyList(context);
        }

        else {
            String separator = "";
            boolean indent = (size() > 1);

            if (indent)
                context.formatIndentStart();

            for (T queryPart : this) {
                context.sql(separator);

                if (indent)
                    context.formatNewLine();

                context.sql(queryPart);
                separator = ", ";
            }

            if (indent)
                context.formatIndentEnd();
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(wrappedList);
    }

    /**
     * Subclasses may override this method
     */
    @SuppressWarnings("unused")
    protected void toSQLEmptyList(RenderContext context) {
    }



    // -------------------------------------------------------------------------
    // Implementations from the List API
    // -------------------------------------------------------------------------

    @Override
    public final int size() {
        return wrappedList.size();
    }

    @Override
    public final boolean isEmpty() {
        return wrappedList.isEmpty();
    }

    @Override
    public final boolean contains(Object o) {
        return wrappedList.contains(o);
    }

    @Override
    public final Iterator<T> iterator() {
        return wrappedList.iterator();
    }

    @Override
    public final Object[] toArray() {
        return wrappedList.toArray();
    }

    @Override
    public final <E> E[] toArray(E[] a) {
        return wrappedList.toArray(a);
    }

    @Override
    public final boolean add(T e) {
        if (e != null) {
            return wrappedList.add(e);
        }

        return false;
    }

    @Override
    public final boolean remove(Object o) {
        return wrappedList.remove(o);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return wrappedList.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends T> c) {
        return wrappedList.addAll(removeNulls(c));
    }

    @Override
    public final boolean addAll(int index, Collection<? extends T> c) {
        return wrappedList.addAll(index, removeNulls(c));
    }

    private final Collection<? extends T> removeNulls(Collection<? extends T> c) {

        // [#2145] Collections that contain nulls are quite rare, so it is wise
        // to add a relatively cheap defender check to avoid unnecessary loops
        if (c.contains(null)) {
            List<T> list = new ArrayList<T>(c);
            Iterator<T> it = list.iterator();

            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                }
            }

            return list;
        }
        else {
            return c;
        }
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return wrappedList.removeAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        return wrappedList.retainAll(c);
    }

    @Override
    public final void clear() {
        wrappedList.clear();
    }

    @Override
    public final T get(int index) {
        return wrappedList.get(index);
    }

    @Override
    public final T set(int index, T element) {
        if (element != null) {
            return wrappedList.set(index, element);
        }

        return null;
    }

    @Override
    public final void add(int index, T element) {
        if (element != null) {
            wrappedList.add(index, element);
        }
    }

    @Override
    public final T remove(int index) {
        return wrappedList.remove(index);
    }

    @Override
    public final int indexOf(Object o) {
        return wrappedList.indexOf(o);
    }

    @Override
    public final int lastIndexOf(Object o) {
        return wrappedList.lastIndexOf(o);
    }

    @Override
    public final ListIterator<T> listIterator() {
        return wrappedList.listIterator();
    }

    @Override
    public final ListIterator<T> listIterator(int index) {
        return wrappedList.listIterator(index);
    }

    @Override
    public final List<T> subList(int fromIndex, int toIndex) {
        return wrappedList.subList(fromIndex, toIndex);
    }
}
