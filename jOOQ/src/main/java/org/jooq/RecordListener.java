/*
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

    /**
     * Called when an exception occurs.
     */
    void exception(RecordContext ctx);
}
