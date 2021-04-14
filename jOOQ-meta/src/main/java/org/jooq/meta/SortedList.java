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
package org.jooq.meta;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * A list that keeps its contents sorted.
 * <p>
 * Since the sort behaviour is stable, {@link List#add(Object)} and other single
 * item methods can sort the list in O(N). However, large additions of M
 * elements should still be done using {@link List#addAll(Collection)} to sort
 * the list in O(N log N), instead of O(M*N), which is quadradic in the worst
 * case. Removals don't require sorting.
 */
final class SortedList<E> extends AbstractList<E> {

    final List<E>               delegate;
    final Comparator<? super E> comparator;

    SortedList(List<E> delegate, Comparator<? super E> comparator) {
        this.delegate = delegate;
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean add(E e) {
        return sort(delegate.add(e));
    }

    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return sort(delegate.addAll(c));
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return sort(delegate.addAll(index, c));
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public E get(int index) {
        return delegate.get(index);
    }

    @Override
    public E set(int index, E element) {
        return sort(delegate.set(index, element));
    }

    @Override
    public void add(int index, E element) {
        delegate.add(index, element);
        sort(comparator);
    }

    @Override
    public E remove(int index) {
        return delegate.remove(index);
    }

    private <T> T sort(T andReturn) {
        delegate.sort(comparator);
        return andReturn;
    }
}
