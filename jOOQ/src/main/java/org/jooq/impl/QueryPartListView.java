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
import static java.util.Arrays.asList;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.Statement;

/**
 * A {@link List} view, delegating all calls to a wrapped list, but acting like
 * a {@link QueryPart}.
 *
 * @author Lukas Eder
 */
class QueryPartListView<T extends QueryPart> extends AbstractQueryPart implements List<T> {

    private static final long serialVersionUID = -2936922742534009564L;
    private final List<T>     wrappedList;
    private int               indentSize       = 2;

    static <T extends QueryPart> QueryPartListView<T> wrap(T[] wrappedList) {
        return new QueryPartListView<>(wrappedList);
    }

    static <T extends QueryPart> QueryPartListView<T> wrap(List<T> wrappedList) {
        return new QueryPartListView<>(wrappedList);
    }

    QueryPartListView(T[] wrappedList) {
        this(asList(wrappedList));
    }

    QueryPartListView(List<T> wrappedList) {
        this.wrappedList = wrappedList;
    }

    /**
     * Whether to indent this list, and after what size indentation is applied.
     */
    QueryPartListView<T> indentSize(int newIndentSize) {
        this.indentSize = newIndentSize;
        return this;
    }

    @Override
    public /* non-final */ void accept(Context<?> ctx) {
        int size = size();

        if (ctx.separatorRequired())
            if (size >= indentSize)
                ctx.formatSeparator();
            else
                ctx.sql(' ');

        // Some lists render different SQL when empty
        if (size == 0) {
            toSQLEmptyList(ctx);
        }

        else {
            boolean indent = (size >= indentSize) && !TRUE.equals(ctx.data(DATA_LIST_ALREADY_INDENTED));

            if (indent)
                ctx.formatIndentStart();

            for (int i = 0; i < size; i++) {
                T part = get(i);

                if (i > 0) {

                    // [#3607] Procedures and functions are not separated by comma
                    if (!(part instanceof Statement))
                        ctx.sql(',');

                    ctx.formatSeparator();
                }
                else if (indent)
                    ctx.formatNewLine();

                ctx.visit(part);
            }

            if (indent)
                ctx.formatIndentEnd().formatNewLine();
        }
    }

    /**
     * Subclasses may override this method
     */
    @SuppressWarnings("unused")
    protected void toSQLEmptyList(Context<?> context) {
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
        boolean containsNulls;

        try {
            containsNulls = c.contains(null);
        }

        // [#7991] Some immutable collections do not allow for nulls to be contained
        catch (NullPointerException ignore) {
            containsNulls = false;
        }

        if (containsNulls) {
            List<T> list = new ArrayList<>(c);
            Iterator<T> it = list.iterator();

            while (it.hasNext())
                if (it.next() == null)
                    it.remove();

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
