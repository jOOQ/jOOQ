/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DAO;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.UpdatableTable;

/**
 * A common base implementation for generated DAO's
 *
 * @author Lukas Eder
 */
public abstract class DAOImpl<R extends UpdatableRecord<R>, P, T> implements DAO<R, P, T> {

    private final Table<R> table;
    private final Class<P> type;
    private Factory        create;

    // -------------------------------------------------------------------------
    // XXX: Constructors and initialisation
    // -------------------------------------------------------------------------

    protected DAOImpl(Table<R> table, Class<P> type) {
        this(table, type, null);
    }

    protected DAOImpl(Table<R> table, Class<P> type, Factory create) {
        this.table = table;
        this.type = type;
        this.create = create;
    }

    /**
     * Inject an attached factory
     */
    public final void setFactory(Factory create) {
        this.create = create;
    }

    // -------------------------------------------------------------------------
    // XXX: DAO API
    // -------------------------------------------------------------------------

    @Override
    public final void insert(P object) {
        insert(singletonList(object));
    }

    @Override
    public final void insert(P... objects) {
        insert(asList(objects));
    }

    @Override
    public final void insert(Collection<P> objects) {

        // Execute a batch INSERT
        if (objects.size() > 1) {
            create.batchStore(records(objects, false)).execute();
        }

        // Execute a regular INSERT
        else if (objects.size() == 1) {
            records(objects, false).get(0).store();
        }
    }

    @Override
    public final void update(P object) {
        update(singletonList(object));
    }

    @Override
    public final void update(P... objects) {
        update(asList(objects));
    }

    @Override
    public final void update(Collection<P> objects) {

        // Execute a batch UPDATE
        if (objects.size() > 1) {
            create.batchStore(records(objects, true)).execute();
        }

        // Execute a regular UPDATE
        else if (objects.size() == 1) {
            records(objects, true).get(0).store();
        }
    }

    @Override
    public final void delete(P... objects) {
        delete(asList(objects));
    }

    @Override
    public final void delete(Collection<P> objects) {
        List<T> ids = new ArrayList<T>();

        for (P object : objects) {
            ids.add(getId(object));
        }

        deleteById(ids);
    }

    @Override
    public final void deleteById(T... ids) {
        deleteById(asList(ids));
    }

    @Override
    public final void deleteById(Collection<T> ids) {
        Field<?> pk = pk();

        if (pk != null) {
            create.delete(table).where(equal(pk, ids)).execute();
        }
    }

    @Override
    public final boolean exists(P object) {
        return existsById(getId(object));
    }

    @Override
    public final boolean existsById(T id) {
        Field<?> pk = pk();

        if (pk != null) {
            return create.selectCount()
                         .from(table)
                         .where(equal(pk, id))
                         .fetchOne(0, Integer.class) > 0;
        }
        else {
            return false;
        }
    }

    @Override
    public final long count() {
        return create.selectCount()
                     .from(table)
                     .fetchOne(0, Long.class);
    }

    @Override
    public final List<P> findAll() {
        return create.selectFrom(table)
                     .fetch()
                     .into(type);
    }

    @Override
    public final P findById(T id) {
        Field<?> pk = pk();
        R record = null;

        if (pk != null) {
            record = create.selectFrom(table)
                           .where(equal(pk, id))
                           .fetchOne();
        }

        return record == null ? null : record.into(type);
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }

    @Override
    public final Class<P> getType() {
        return type;
    }

    // ------------------------------------------------------------------------
    // XXX: Template methods for generated subclasses
    // ------------------------------------------------------------------------

    protected abstract T getId(P object);

    // ------------------------------------------------------------------------
    // XXX: Private utility methods
    // ------------------------------------------------------------------------

    private final <U> Condition equal(Field<U> pk, T id) {
        return pk.equal(pk.getDataType().convert(id));
    }

    private final <U> Condition equal(Field<U> pk, Collection<T> ids) {
        if (ids.size() == 1) {
            return equal(pk, ids.iterator().next());
        }
        else {
            return pk.in(pk.getDataType().convert(ids));
        }
    }

    private final Field<?> pk() {
        if (table instanceof UpdatableTable) {
            UpdatableTable<?> updatable = (UpdatableTable<?>) table;
            UniqueKey<?> key = updatable.getMainKey();

            if (key.getFields().size() == 1) {
                return key.getFields().get(0);
            }
        }

        return null;
    }

    private final List<R> records(Collection<P> objects, boolean forUpdate) {
        List<R> result = new ArrayList<R>();
        Field<?> pk = pk();

        for (P object : objects) {
            R record = create.newRecord(table, object);

            if (forUpdate && pk != null) {
                ((AbstractRecord) record).getValue0(pk).setChanged(false);
            }

            result.add(record);
        }

        return result;
    }
}
