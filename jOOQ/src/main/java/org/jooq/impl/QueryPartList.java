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

import static java.util.Arrays.asList;
import static org.jooq.impl.Utils.visitAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class QueryPartList<T extends QueryPart> extends AbstractQueryPart implements List<T> {

    private static final long serialVersionUID = -2936922742534009564L;
    private final List<T>     wrappedList;

    QueryPartList() {
        this((Collection<T>) null);
    }

    QueryPartList(Collection<? extends T> wrappedList) {
        super();

        this.wrappedList = new ArrayList<T>();

        if (wrappedList != null) {
            addAll(wrappedList);
        }
    }

    QueryPartList(T... wrappedList) {
        this(asList(wrappedList));
    }

    @Override
    public final void toSQL(RenderContext context) {

        // Some lists render different SQL when empty
        if (isEmpty()) {
//            if (clause != null && clause != DUMMY)
//                context.start(clause);

            toSQLEmptyList(context);

//            if (clause != null && clause != DUMMY)
//                context.end(clause);
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

//                if (clause != null && clause != DUMMY)
//                    context.start(clause);

                context.visit(queryPart);

//                if (clause != null && clause != DUMMY)
//                    context.end(clause);

                separator = ", ";
            }

            if (indent)
                context.formatIndentEnd();
        }
    }

    @Override
    public final void bind(BindContext context) {
        visitAll(context, wrappedList);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
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
