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
package org.jooq;

import java.util.EventListener;

/**
 * A listener for manipulation events on {@link UpdatableRecord}s.
 * <p>
 * Users may want to centrally inject custom behaviour when manipulating their
 * {@link UpdatableRecord} objects, performing CRUD. This service provider
 * allows to hook in callback method implementations for before or after any of
 * these methods:
 * <ul>
 * <li>{@link UpdatableRecord#store()}</li>
 * <li>{@link UpdatableRecord#insert()}</li>
 * <li>{@link UpdatableRecord#update()}</li>
 * <li>{@link UpdatableRecord#delete()}</li>
 * <li>{@link UpdatableRecord#refresh()}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface RecordListener extends EventListener {

    /**
     * Called before storing an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to storing. Note that modifying the record's primary key value may
     * influence whether storing results in an <code>INSERT</code> or
     * <code>UPDATE</code> statement.
     * <p>
     * A store event will generate a nested {@link #insertStart(RecordContext)}
     * or {@link #updateStart(RecordContext)} event.
     *
     * @see UpdatableRecord#store()
     */
    void storeStart(RecordContext ctx);

    /**
     * Called after storing an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after storing. Note that modifying the record's primary key value may
     * influence whether storing results in an <code>INSERT</code> or
     * <code>UPDATE</code> statement.
     * <p>
     * A store event will generate a nested {@link #insertEnd(RecordContext)}
     * or {@link #updateEnd(RecordContext)} event.
     *
     * @see UpdatableRecord#store()
     */
    void storeEnd(RecordContext ctx);

    /**
     * Called before inserting an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to inserting.
     *
     * @see UpdatableRecord#insert()
     */
    void insertStart(RecordContext ctx);

    /**
     * Called after inserting an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after inserting.
     *
     * @see UpdatableRecord#insert()
     */
    void insertEnd(RecordContext ctx);

    /**
     * Called before updating an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to updating.
     *
     * @see UpdatableRecord#update()
     */
    void updateStart(RecordContext ctx);

    /**
     * Called after updating an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after updating.
     *
     * @see UpdatableRecord#update()
     */
    void updateEnd(RecordContext ctx);

    /**
     * Called before deleting an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to deleting.
     *
     * @see UpdatableRecord#delete()
     */
    void deleteStart(RecordContext ctx);

    /**
     * Called after deleting an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after deleting.
     *
     * @see UpdatableRecord#delete()
     */
    void deleteEnd(RecordContext ctx);

    /**
     * Called before loading an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to loading.
     */
    void loadStart(RecordContext ctx);

    /**
     * Called after loading an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after loading.
     */
    void loadEnd(RecordContext ctx);

    /**
     * Called before refreshing an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to refreshing.
     *
     * @see UpdatableRecord#refresh()
     */
    void refreshStart(RecordContext ctx);

    /**
     * Called after refreshing an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after refreshing.
     *
     * @see UpdatableRecord#refresh()
     */
    void refreshEnd(RecordContext ctx);
}
