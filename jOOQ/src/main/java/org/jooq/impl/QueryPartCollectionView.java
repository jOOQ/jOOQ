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
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.Statement;

/**
 * A {@link List} view, delegating all calls to a wrapped list, but acting like
 * a {@link QueryPart}.
 *
 * @author Lukas Eder
 */
class QueryPartCollectionView<T extends QueryPart> extends AbstractQueryPart implements Collection<T> {

    private static final long serialVersionUID = -2936922742534009564L;
    final Collection<T>       wrapped;
    int                       indentSize;

    static <T extends QueryPart> QueryPartCollectionView<T> wrap(Collection<T> wrapped) {
        return new QueryPartCollectionView<>(wrapped);
    }

    QueryPartCollectionView(Collection<T> wrapped) {
        this.wrapped = wrapped;
        this.indentSize = 2;
    }

    /**
     * Whether to indent this list, and after what size indentation is applied.
     */
    QueryPartCollectionView<T> indentSize(int newIndentSize) {
        this.indentSize = newIndentSize <= 0 ? Integer.MAX_VALUE : newIndentSize;
        return this;
    }

    Collection<T> wrapped() {
        return wrapped;
    }

    @Override
    public /* non-final */ void accept(Context<?> ctx) {
        int size = size();
        boolean format = size >= indentSize;

        if (ctx.separatorRequired())
            if (format)
                ctx.formatSeparator();
            else
                ctx.sql(' ');

        // Some lists render different SQL when empty
        if (size == 0) {
            toSQLEmptyList(ctx);
        }

        else {
            boolean indent = format && !TRUE.equals(ctx.data(DATA_LIST_ALREADY_INDENTED));

            if (indent)
                ctx.formatIndentStart();

            int i = 0;
            for (T part : this) {
                if (i++ > 0) {

                    // [#3607] Procedures and functions are not separated by comma
                    if (!(part instanceof Statement))
                        ctx.sql(',');

                    if (format)
                        ctx.formatSeparator();
                    else
                        ctx.sql(' ');
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
        return wrapped.size();
    }

    @Override
    public final boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public final boolean contains(Object o) {
        return wrapped.contains(o);
    }

    @Override
    public final Iterator<T> iterator() {
        return wrapped.iterator();
    }

    @Override
    public final Object[] toArray() {
        return wrapped.toArray();
    }

    @Override
    public final <E> E[] toArray(E[] a) {
        return wrapped.toArray(a);
    }

    @Override
    public final boolean add(T e) {
        if (e != null) {
            return wrapped.add(e);
        }

        return false;
    }

    @Override
    public final boolean remove(Object o) {
        return wrapped.remove(o);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return wrapped.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends T> c) {
        return wrapped.addAll(removeNulls(c));
    }

    final Collection<? extends T> removeNulls(Collection<? extends T> c) {

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
        return wrapped.removeAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        return wrapped.retainAll(c);
    }

    @Override
    public final void clear() {
        wrapped.clear();
    }
}
