/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
import org.jooq.SQLDialect;
import org.jooq.SQLDialectNotSupportedException;
import org.jooq.util.oracle.OracleUtils;

/**
 * A common base class for Oracle ARRAY types
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class ArrayRecordImpl<T> extends AbstractStore<T> implements ArrayRecord<T> {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = -908937248705184108L;

    private final DataType<T>       type;
    private final String            name;
    private T[]                     array;

    /**
     * Create an empty array record
     */
    protected ArrayRecordImpl(String name, DataType<T> type, Configuration configuration) {
        super(configuration);

        this.name = name;
        this.type = type;
    }

    /**
     * Create an empty array record
     */
    protected ArrayRecordImpl(String name, DataType<T> type) {
        this(name, type, null);
    }

    // -------------------------------------------------------------------------
    // The Attachable API
    // -------------------------------------------------------------------------

    @Override
    public final List<Attachable> getAttachables() {
        List<Attachable> result = new ArrayList<Attachable>();

        if (Attachable.class.isAssignableFrom(type.getType())) {
            for (T element : array) {
                result.add((Attachable) element);
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // The ArrayRecord API
    // -------------------------------------------------------------------------

    @Override
    public final T getValue(int index) throws IllegalArgumentException {
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
            this.array = TypeUtils.convert(array.getArray(), type.getArrayType());
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
        return name;
    }

    @Override
    public final DataType<T> getDataType() {
        return type;
    }

    @Override
    public final java.sql.Array createArray() throws SQLException {
        SQLDialect dialect = getConfiguration().getDialect();

        switch (dialect) {
            case ORACLE:
                return OracleUtils.createArray(getConfiguration().getConnection(), this);

            default:
                throw new SQLDialectNotSupportedException(
                    "Cannot create Array for dialect : " + dialect);
        }
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + " [values=" + getList() + "]";
    }

    @Override
    public final Iterator<T> iterator() {
        return getList().iterator();
    }
}
