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
package org.jooq;

import java.util.EventListener;
import java.util.function.Consumer;

import org.jooq.impl.CallbackRecordListener;

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
 * <p>
 * A <code>RecordListener</code> does not act as a client-side trigger. As such,
 * it does not affect any bulk DML statements (e.g. a
 * {@link DSLContext#update(Table)}), whose affected records are not available
 * to clients. For those purposes, use a server-side trigger
 * {@link DSLContext#createTrigger(Name)} if records should be changed, or a
 * {@link VisitListener} if the SQL query should be changed, independently of
 * data.
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
     * Called before merging an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to merging.
     *
     * @see UpdatableRecord#merge()
     */
    void mergeStart(RecordContext ctx);

    /**
     * Called after merging an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after merging.
     *
     * @see UpdatableRecord#merge()
     */
    void mergeEnd(RecordContext ctx);

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

    /**
     * Create a {@link RecordListener} with a
     * {@link #onStoreStart(Consumer)} implementation.
     */
    static CallbackRecordListener onStoreStart(Consumer<? super RecordContext> onStoreStart) {
        return new CallbackRecordListener().onStoreStart(onStoreStart);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onStoreEnd(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onStoreEnd(Consumer<? super RecordContext> onStoreEnd) {
        return new CallbackRecordListener().onStoreEnd(onStoreEnd);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onInsertStart(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onInsertStart(Consumer<? super RecordContext> onInsertStart) {
        return new CallbackRecordListener().onInsertStart(onInsertStart);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onInsertEnd(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onInsertEnd(Consumer<? super RecordContext> onInsertEnd) {
        return new CallbackRecordListener().onInsertEnd(onInsertEnd);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onUpdateStart(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onUpdateStart(Consumer<? super RecordContext> onUpdateStart) {
        return new CallbackRecordListener().onUpdateStart(onUpdateStart);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onUpdateEnd(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onUpdateEnd(Consumer<? super RecordContext> onUpdateEnd) {
        return new CallbackRecordListener().onUpdateEnd(onUpdateEnd);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onMergeStart(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onMergeStart(Consumer<? super RecordContext> onMergeStart) {
        return new CallbackRecordListener().onMergeStart(onMergeStart);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onMergeEnd(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onMergeEnd(Consumer<? super RecordContext> onMergeEnd) {
        return new CallbackRecordListener().onMergeEnd(onMergeEnd);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onDeleteStart(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onDeleteStart(Consumer<? super RecordContext> onDeleteStart) {
        return new CallbackRecordListener().onDeleteStart(onDeleteStart);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onDeleteEnd(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onDeleteEnd(Consumer<? super RecordContext> onDeleteEnd) {
        return new CallbackRecordListener().onDeleteEnd(onDeleteEnd);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onLoadStart(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onLoadStart(Consumer<? super RecordContext> onLoadStart) {
        return new CallbackRecordListener().onLoadStart(onLoadStart);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onLoadEnd(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onLoadEnd(Consumer<? super RecordContext> onLoadEnd) {
        return new CallbackRecordListener().onLoadEnd(onLoadEnd);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onRefreshStart(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onRefreshStart(Consumer<? super RecordContext> onRefreshStart) {
        return new CallbackRecordListener().onRefreshStart(onRefreshStart);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onRefreshEnd(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onRefreshEnd(Consumer<? super RecordContext> onRefreshEnd) {
        return new CallbackRecordListener().onRefreshEnd(onRefreshEnd);
    }

    /**
     * Create a {@link RecordListener} with a {@link #onException(Consumer)}
     * implementation.
     */
    static CallbackRecordListener onException(Consumer<? super RecordContext> onException) {
        return new CallbackRecordListener().onException(onException);
    }
}
