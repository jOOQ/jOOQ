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

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Schema;
import org.jooq.tools.Convert;

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
    private final DataType<T> type;
    private final String      name;
    private T[]               array;

    /**
     * Create an empty array record
     */
    @SuppressWarnings("unchecked")
    protected ArrayRecordImpl(Schema schema, String name, DataType<T> type, Configuration configuration) {
        super(configuration);

        this.schema = schema;
        this.name = name;
        this.type = type;

        // Array data type initialisation
        type.asArrayDataType(getClass());
    }

    /**
     * Create an empty array record
     */
    protected ArrayRecordImpl(Schema schema, String name, DataType<T> type) {
        this(schema, name, type, null);
    }

    // -------------------------------------------------------------------------
    // The Attachable API
    // -------------------------------------------------------------------------

    @Override
    final List<Attachable> getAttachables() {
        List<Attachable> result = new ArrayList<Attachable>();

        if (Attachable.class.isAssignableFrom(type.getType())) {
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
    public final T[] get() {
        if (array == null) {
            return (T[]) Array.newInstance(type.getType(), 0);
        }
        else {
            return array;
        }
    }

    @Override
    public final List<T> getList() {
        if (array == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(array);
        }
    }

    @Override
    public final void set(T... array) {
        this.array = array;
    }

    @Override
    public final void set(java.sql.Array array) throws SQLException {
        if (array == null) {
            this.array = null;
        }
        else {
            Object o;

            // [#1179 #1376 #1377] This is needed to load TABLE OF OBJECT
            // [#884] TODO: This name is used in inlined SQL. It should be
            // correctly escaped and schema mapped!
            o = array.getArray(DataTypes.udtRecords());
            this.array = Convert.convert(o, type.getArrayType());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void setList(List<? extends T> list) {
        if (list == null) {
            array = null;
        }
        else {
            array = list.toArray((T[]) Array.newInstance(type.getType(), 0));
        }
    }

    @Override
    public final int size() {
        return get().length;
    }

    @Override
    public final String getName() {

        // [#1179] When Schema is present, the name is not fully qualified
        if (schema != null) {
            return schema.getName() + "." + name;
        }

        // When Schema is absent, the name is fully qualified (deprecated, pre 2.0.5)
        else {
            return name;
        }
    }

    @Override
    public final DataType<T> getDataType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";

        result.append(getName());
        result.append("(");

        if (array != null) {
            for (T t : array) {
                result.append(separator);
                result.append(t);

                separator = ", ";
            }
        }

        result.append(")");
        return result.toString();
    }

    @Override
    public final Iterator<T> iterator() {
        return getList().iterator();
    }
}
