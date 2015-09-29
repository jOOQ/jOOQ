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

/* [pro] */

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.Binding;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Schema;

/**
 * A common base class for Oracle ARRAY types
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class ArrayRecordImpl<T> extends AbstractStore implements ArrayRecord<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -908937248705184108L;

    private final Schema      schema;
    private final DataType<T> baseType;
    private final DataType<?> type;
    private final String      name;
    private final List<T>     list;

    /**
     * Create an empty array record
     *
     * @deprecated - 3.4.0 - [#3126] - Use the
     *             {@link #ArrayRecordImpl(Schema, String, DataType)}
     *             constructor instead
     */
    @SuppressWarnings("unused")
    @Deprecated
    protected ArrayRecordImpl(Schema schema, String name, DataType<T> type, Configuration configuration) {
        this(schema, name, type, (Converter<?, T>) null, null);
    }

    /**
     * Create an empty array record
     *
     * @deprecated - 3.4.0 - [#3126] - Use the
     *             {@link #ArrayRecordImpl(Schema, String, DataType, Converter)}
     *             constructor instead.
     */
    @SuppressWarnings("unused")
    @Deprecated
    protected <X> ArrayRecordImpl(Schema schema, String name, DataType<X> type, Configuration configuration, Converter<X, T> converter) {
        this(schema, name, type, converter, null);
    }

    /**
     * Create an empty array record
     *
     * @deprecated - 3.4.0 - [#3126] - Use the
     *             {@link #ArrayRecordImpl(Schema, String, DataType, Converter)}
     *             constructor instead.
     */
    @SuppressWarnings("unused")
    @Deprecated
    protected <X, Y> ArrayRecordImpl(Schema schema, String name, DataType<X> type, Configuration configuration, Binding<X, Y> binding) {
        this(schema, name, type, (Converter<Y, T>) null, binding);
    }

    /**
     * Create an empty array record
     *
     * @deprecated - 3.4.0 - [#3126] - Use the
     *             {@link #ArrayRecordImpl(Schema, String, DataType, Converter)}
     *             constructor instead.
     */
    @SuppressWarnings("unused")
    @Deprecated
    protected <X, Y> ArrayRecordImpl(Schema schema, String name, DataType<X> type, Configuration configuration, Converter<Y, T> converter, Binding<X, Y> binding) {
        this(schema, name, type, converter, binding);
    }

    /**
     * Create an empty array record
     */
    protected ArrayRecordImpl(Schema schema, String name, DataType<T> type) {
        this(schema, name, type, (Converter<?, T>) null, null);
    }

    /**
     * Create an empty array record
     */
    protected <X> ArrayRecordImpl(Schema schema, String name, DataType<X> type, Converter<X, T> converter) {
        this(schema, name, type, converter, null);
    }

    /**
     * Create an empty array record
     */
    protected <X, Y> ArrayRecordImpl(Schema schema, String name, DataType<X> type, Binding<X, Y> binding) {
        this(schema, name, type, (Converter<Y, T>) null, binding);
    }

    /**
     * Create an empty array record
     */
    @SuppressWarnings({ "unchecked" })
    protected <X, Y> ArrayRecordImpl(Schema schema, String name, DataType<X> type, Converter<Y, T> converter, Binding<X, Y> binding) {
        super(null);

        this.schema = schema;
        this.name = name;
        this.baseType = converter == null && binding == null
            ? (DataType<T>) type
            : type.asConvertedDataType(DefaultBinding.newBinding(converter, type, binding));

        // Array data type initialisation
        this.type = baseType.asArrayDataType(getClass());
        this.list = new ArrayList<T>();
    }

    // -------------------------------------------------------------------------
    // The Attachable API
    // -------------------------------------------------------------------------

    @Override
    final List<Attachable> getAttachables() {
        List<Attachable> result = new ArrayList<Attachable>();

        if (Attachable.class.isAssignableFrom(baseType.getType())) {
            for (T element : get()) {
                result.add((Attachable) element);
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // The ArrayRecord API
    // -------------------------------------------------------------------------

    @Override
    final T getValue(int index) {
        return get()[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public final T[] get() {
        return toArray((T[]) Array.newInstance(baseType.getType(), size()));
    }

    @Override
    @Deprecated
    public final List<T> getList() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public final void set(T... array) {
        if (array == null)
            clear();
        else
            set(Arrays.asList(array));
    }

    @Override
    @Deprecated
    public final void set(java.sql.Array array) throws SQLException {
        DefaultBinding.set(configuration(), this, array);
    }

    @Override
    @Deprecated
    public final void set(Collection<? extends T> collection) {
        clear();
        addAll(collection);
    }

    @Override
    @Deprecated
    public final void setList(List<? extends T> list) {
        set(list);
    }

    @Override
    public final Schema getSchema() {
        return schema;
    }

    @Override
    public final String getName() {

        // [#1179] [#4306] With jOOQ 3.7.0 This is no longer a qualified name.
        return name;
    }

    @Override
    public final DataType<T> getDataType() {
        return baseType;
    }

    @Override
    public final DataType<?> getArrayType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";

        result.append(Utils.getMappedArrayName(configuration(), this));
        result.append("(");

        for (T t : list) {
            result.append(separator);
            result.append(create().render(DSL.inline(t)));

            separator = ", ";
        }

        result.append(")");
        return result.toString();
    }

    // -------------------------------------------------------------------------
    // XXX List methods
    // -------------------------------------------------------------------------

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public final boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public final Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public final Object[] toArray() {
        return list.toArray();
    }

    @Override
    public final <Z> Z[] toArray(Z[] a) {
        return list.toArray(a);
    }

    @Override
    public final boolean add(T e) {
        return list.add(e);
    }

    @Override
    public final boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public final boolean addAll(int index, Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    @Override
    public final boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public final int hashCode() {
        return list.hashCode();
    }

    @Override
    public final T get(int index) {
        return list.get(index);
    }

    @Override
    public final T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public final void add(int index, T element) {
        list.add(index, element);
    }

    @Override
    public final T remove(int index) {
        return list.remove(index);
    }

    @Override
    public final int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public final int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public final ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public final ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public final List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
/* [/pro] */