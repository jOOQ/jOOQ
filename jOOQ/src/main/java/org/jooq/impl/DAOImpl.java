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
import static java.util.Collections.singletonList;
import static org.jooq.impl.DSL.using;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DAO;
import org.jooq.Field;
import org.jooq.RecordMapper;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;

/**
 * A common base implementation for generated DAO's.
 *
 * @author Lukas Eder
 */
public abstract class DAOImpl<R extends UpdatableRecord<R>, P, T> implements DAO<R, P, T> {

    private final Table<R>     table;
    private final Class<P>     type;
    private RecordMapper<R, P> mapper;
    private Configuration      configuration;

    // -------------------------------------------------------------------------
    // XXX: Constructors and initialisation
    // -------------------------------------------------------------------------

    protected DAOImpl(Table<R> table, Class<P> type) {
        this(table, type, null);
    }

    protected DAOImpl(Table<R> table, Class<P> type, Configuration configuration) {
        this.table = table;
        this.type = type;

        setConfiguration(configuration);
    }

    /**
     * Inject a configuration.
     * <p>
     * This method is maintained to be able to configure a <code>DAO</code>
     * using Spring. It is not exposed in the public API.
     */
    public final void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        this.mapper = Utils.configuration(configuration).recordMapperProvider().provide(table.recordType(), type);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method to provide custom implementations.
     */
    @Override
    public /* non-final */ RecordMapper<R, P> mapper() {
        return mapper;
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
            using(configuration).batchInsert(records(objects, false)).execute();
        }

        // Execute a regular INSERT
        else if (objects.size() == 1) {
            records(objects, false).get(0).insert();
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
            using(configuration).batchUpdate(records(objects, true)).execute();
        }

        // Execute a regular UPDATE
        else if (objects.size() == 1) {
            records(objects, true).get(0).update();
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
            using(configuration).delete(table).where(equal(pk, ids)).execute();
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
            return using(configuration)
                     .selectCount()
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
        return using(configuration)
                 .selectCount()
                 .from(table)
                 .fetchOne(0, Long.class);
    }

    @Override
    public final List<P> findAll() {
        return using(configuration)
                 .selectFrom(table)
                 .fetch()
                 .map(mapper());
    }

    @Override
    public final P findById(T id) {
        Field<?> pk = pk();
        R record = null;

        if (pk != null) {
            record = using(configuration)
                        .selectFrom(table)
                        .where(equal(pk, id))
                        .fetchOne();
        }

        return mapper().map(record);
    }

    @Override
    public final <Z> List<P> fetch(Field<Z> field, Z... values) {
        return using(configuration)
                 .selectFrom(table)
                 .where(field.in(values))
                 .fetch()
                 .map(mapper());
    }

    @Override
    public final <Z> P fetchOne(Field<Z> field, Z value) {
        R record = using(configuration)
                     .selectFrom(table)
                     .where(field.equal(value))
                     .fetchOne();

        return mapper().map(record);
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
        UniqueKey<?> key = table.getPrimaryKey();

        if (key != null) {
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
            R record = using(configuration).newRecord(table, object);

            if (forUpdate && pk != null) {
                ((AbstractRecord) record).getValue0(pk).setChanged(false);
            }

            result.add(record);
        }

        return result;
    }
}
