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

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.jooq.Context;
import org.jooq.QueryPart;

/**
 * A {@link List} view, delegating all calls to a wrapped list, but acting like
 * a {@link QueryPart}.
 *
 * @author Lukas Eder
 */
class QueryPartListView<T extends QueryPart> extends QueryPartCollectionView<T> implements List<T> {

    private static final long serialVersionUID = -2936922742534009564L;

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
        super(wrappedList);
    }

    @Override
    QueryPartListView<T> indentSize(int newIndentSize) {
        return (QueryPartListView<T>) super.indentSize(newIndentSize);
    }

    @Override
    QueryPartListView<T> qualify(boolean newQualify) {
        return (QueryPartListView<T>) super.qualify(newQualify);
    }

    @Override
    List<T> wrapped() {
        return (List<T>) super.wrapped();
    }

    /**
     * Subclasses may override this method
     */
    @Override
    protected void toSQLEmptyList(Context<?> context) {}

    // -------------------------------------------------------------------------
    // Implementations from the List API
    // -------------------------------------------------------------------------

    @Override
    public final boolean addAll(int index, Collection<? extends T> c) {
        return wrapped().addAll(index, removeNulls(c));
    }

    @Override
    public final T get(int index) {
        return wrapped().get(index);
    }

    @Override
    public final T set(int index, T element) {
        if (element != null)
            return wrapped().set(index, element);

        return null;
    }

    @Override
    public final void add(int index, T element) {
        if (element != null)
            wrapped().add(index, element);
    }

    @Override
    public final T remove(int index) {
        return wrapped().remove(index);
    }

    @Override
    public final int indexOf(Object o) {
        return wrapped().indexOf(o);
    }

    @Override
    public final int lastIndexOf(Object o) {
        return wrapped().lastIndexOf(o);
    }

    @Override
    public final ListIterator<T> listIterator() {
        return wrapped().listIterator();
    }

    @Override
    public final ListIterator<T> listIterator(int index) {
        return wrapped().listIterator(index);
    }

    @Override
    public final List<T> subList(int fromIndex, int toIndex) {
        return wrapped().subList(fromIndex, toIndex);
    }
}
