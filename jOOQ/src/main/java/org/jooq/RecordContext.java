/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq;

import java.util.Map;

/**
 * A context object for {@link Record} manipulation passed to registered
 * {@link RecordListener}'s.
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
     * @param value The custom data
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
     * The <code>Record</code>(s) that are being manipulated in batch mode.
     * <p>
     * If a single <code>Record</code> is being manipulated in non-batch mode,
     * this will return an array of length <code>1</code>, containing that
     * <code>Record</code>.
     *
     * @return The <code>Record</code>(s) being manipulated. This is never
     *         <code>null</code>
     */
    Record[] batchRecords();

    /**
     * The {@link Exception} being thrown or <code>null</code>.
     */
    Exception exception();

}
