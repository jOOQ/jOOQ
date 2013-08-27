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

import java.util.Map;

/**
 * A context object for {@link Record} manipulation passed to
 * registered {@link RecordListener}'s.
 *
 * @author Lukas Eder
 */
public interface RecordContext {

    /**
     * Get all custom data from this <code>RecordContext</code>.
     * <p>
     * This is custom data that was previously set to the record context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * between events received by a {@link RecordListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only matches
     * that of a single record manipulation.
     *
     * @return The custom data. This is never <code>null</code>
     * @see RecordListener
     */
    Map<Object, Object> data();

    /**
     * Get some custom data from this <code>RecordContext</code>.
     * <p>
     * This is custom data that was previously set to the record context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * between events received by an {@link RecordListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only matches
     * that of a single query execution.
     *
     * @param key A key to identify the custom data
     * @return The custom data or <code>null</code> if no such data is contained
     *         in this <code>RecordContext</code>
     * @see RecordListener
     */
    Object data(Object key);

    /**
     * Set some custom data to this <code>RecordContext</code>.
     * <p>
     * This is custom data that was previously set to the record context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * between events received by an {@link RecordListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only matches
     * that of a single query execution.
     *
     * @param key A key to identify the custom data
     * @param value The custom data or <code>null</code> to unset the custom
     *            data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     * @see RecordListener
     */
    Object data(Object key, Object value);

    /**
     * The configuration wrapped by this context.
     */
    Configuration configuration();

    /**
     * The type of database interaction that is being executed.
     * <p>
     * Unlike {@link ExecuteContext#type()}, this can only result in any of
     * these:
     * <ul>
     * <li>{@link ExecuteType#BATCH} when calling
     * {@link DSLContext#batchStore(UpdatableRecord...) batchStore()},
     * {@link DSLContext#batchInsert(UpdatableRecord...) batchInsert()},
     * {@link DSLContext#batchUpdate(UpdatableRecord...) batchUpdate()},
     * {@link DSLContext#batchDelete(UpdatableRecord...) batchDelete()}.</li>
     * <li>{@link ExecuteType#READ} when calling
     * {@link UpdatableRecord#refresh() refresh()}</li>
     * <li>{@link ExecuteType#WRITE} when calling
     * {@link UpdatableRecord#store() store()}, {@link UpdatableRecord#insert()
     * insert()}, {@link UpdatableRecord#update() update()},
     * {@link UpdatableRecord#delete() delete()}.</li>
     * </ul>
     *
     * @see ExecuteType
     */
    ExecuteType type();

    /**
     * The <code>Record</code> that is being manipulated.
     *
     * @return The <code>Record</code> being manipulated. This is never
     *         <code>null</code>
     */
    Record record();

    /**
     * The <code>Record</code>(s) that are being manipulated in batch
     * mode.
     * <p>
     * If a single <code>Record</code> is being manipulated in
     * non-batch mode, this will return an array of length <code>1</code>,
     * containing that <code>Record</code>.
     *
     * @return The <code>Record</code>(s) being manipulated. This is
     *         never <code>null</code>
     */
    Record[] batchRecords();
}
