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
package org.jooq.impl;


import java.util.function.Consumer;

import org.jooq.RecordContext;
import org.jooq.RecordListener;

/**
 * A {@link RecordListener} that allows for functional composition.
 * <p>
 * For example: <code><pre>
 * ParseListener listener = RecordListener
 *   .onLoadStart(ctx -&gt; something())
 *   .onLoadEnd(ctx -&gt; something());
 * </pre></code>
 *
 * @author Lukas Eder
 */
public final class CallbackRecordListener implements RecordListener {

    private final Consumer<? super RecordContext> onStoreStart;
    private final Consumer<? super RecordContext> onStoreEnd;
    private final Consumer<? super RecordContext> onInsertStart;
    private final Consumer<? super RecordContext> onInsertEnd;
    private final Consumer<? super RecordContext> onUpdateStart;
    private final Consumer<? super RecordContext> onUpdateEnd;
    private final Consumer<? super RecordContext> onMergeStart;
    private final Consumer<? super RecordContext> onMergeEnd;
    private final Consumer<? super RecordContext> onDeleteStart;
    private final Consumer<? super RecordContext> onDeleteEnd;
    private final Consumer<? super RecordContext> onLoadStart;
    private final Consumer<? super RecordContext> onLoadEnd;
    private final Consumer<? super RecordContext> onRefreshStart;
    private final Consumer<? super RecordContext> onRefreshEnd;
    private final Consumer<? super RecordContext> onException;

    public CallbackRecordListener() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    private CallbackRecordListener(
        Consumer<? super RecordContext> onStoreStart,
        Consumer<? super RecordContext> onStoreEnd,
        Consumer<? super RecordContext> onInsertStart,
        Consumer<? super RecordContext> onInsertEnd,
        Consumer<? super RecordContext> onUpdateStart,
        Consumer<? super RecordContext> onUpdateEnd,
        Consumer<? super RecordContext> onMergeStart,
        Consumer<? super RecordContext> onMergeEnd,
        Consumer<? super RecordContext> onDeleteStart,
        Consumer<? super RecordContext> onDeleteEnd,
        Consumer<? super RecordContext> onLoadStart,
        Consumer<? super RecordContext> onLoadEnd,
        Consumer<? super RecordContext> onRefreshStart,
        Consumer<? super RecordContext> onRefreshEnd,
        Consumer<? super RecordContext> onException
    ) {
        this.onStoreStart = onStoreStart;
        this.onStoreEnd = onStoreEnd;
        this.onInsertStart = onInsertStart;
        this.onInsertEnd = onInsertEnd;
        this.onUpdateStart = onUpdateStart;
        this.onUpdateEnd = onUpdateEnd;
        this.onMergeStart = onMergeStart;
        this.onMergeEnd = onMergeEnd;
        this.onDeleteStart = onDeleteStart;
        this.onDeleteEnd = onDeleteEnd;
        this.onLoadStart = onLoadStart;
        this.onLoadEnd = onLoadEnd;
        this.onRefreshStart = onRefreshStart;
        this.onRefreshEnd = onRefreshEnd;
        this.onException = onException;
    }

    @Override
    public final void storeStart(RecordContext ctx) {
        if (onStoreStart != null)
            onStoreStart.accept(ctx);
    }

    @Override
    public final void storeEnd(RecordContext ctx) {
        if (onStoreEnd != null)
            onStoreEnd.accept(ctx);
    }

    @Override
    public final void insertStart(RecordContext ctx) {
        if (onInsertStart != null)
            onInsertStart.accept(ctx);
    }

    @Override
    public final void insertEnd(RecordContext ctx) {
        if (onInsertEnd != null)
            onInsertEnd.accept(ctx);
    }

    @Override
    public final void updateStart(RecordContext ctx) {
        if (onUpdateStart != null)
            onUpdateStart.accept(ctx);
    }

    @Override
    public final void updateEnd(RecordContext ctx) {
        if (onUpdateEnd != null)
            onUpdateEnd.accept(ctx);
    }

    @Override
    public final void mergeStart(RecordContext ctx) {
        if (onMergeStart != null)
            onMergeStart.accept(ctx);
    }

    @Override
    public final void mergeEnd(RecordContext ctx) {
        if (onMergeEnd != null)
            onMergeEnd.accept(ctx);
    }

    @Override
    public final void deleteStart(RecordContext ctx) {
        if (onDeleteStart != null)
            onDeleteStart.accept(ctx);
    }

    @Override
    public final void deleteEnd(RecordContext ctx) {
        if (onDeleteEnd != null)
            onDeleteEnd.accept(ctx);
    }

    @Override
    public final void loadStart(RecordContext ctx) {
        if (onLoadStart != null)
            onLoadStart.accept(ctx);
    }

    @Override
    public final void loadEnd(RecordContext ctx) {
        if (onLoadEnd != null)
            onLoadEnd.accept(ctx);
    }

    @Override
    public final void refreshStart(RecordContext ctx) {
        if (onRefreshStart != null)
            onRefreshStart.accept(ctx);
    }

    @Override
    public final void refreshEnd(RecordContext ctx) {
        if (onRefreshEnd != null)
            onRefreshEnd.accept(ctx);
    }

    @Override
    public final void exception(RecordContext ctx) {
        if (onException != null)
            onException.accept(ctx);
    }

    public final CallbackRecordListener onStoreStart(Consumer<? super RecordContext> newOnStoreStart) {
        return new CallbackRecordListener(
            newOnStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onStoreEnd(Consumer<? super RecordContext> newOnStoreEnd) {
        return new CallbackRecordListener(
            onStoreStart,
            newOnStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onInsertStart(Consumer<? super RecordContext> newOnInsertStart) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            newOnInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onInsertEnd(Consumer<? super RecordContext> newOnInsertEnd) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            newOnInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onUpdateStart(Consumer<? super RecordContext> newOnUpdateStart) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            newOnUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onUpdateEnd(Consumer<? super RecordContext> newOnUpdateEnd) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            newOnUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onMergeStart(Consumer<? super RecordContext> newOnMergeStart) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            newOnMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onMergeEnd(Consumer<? super RecordContext> newOnMergeEnd) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            newOnMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onDeleteStart(Consumer<? super RecordContext> newOnDeleteStart) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            newOnDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onDeleteEnd(Consumer<? super RecordContext> newOnDeleteEnd) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            newOnDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onLoadStart(Consumer<? super RecordContext> newOnLoadStart) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            newOnLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onLoadEnd(Consumer<? super RecordContext> newOnLoadEnd) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            newOnLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onRefreshStart(Consumer<? super RecordContext> newOnRefreshStart) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            newOnRefreshStart,
            onRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onRefreshEnd(Consumer<? super RecordContext> newOnRefreshEnd) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            newOnRefreshEnd,
            onException
        );
    }

    public final CallbackRecordListener onException(Consumer<? super RecordContext> newOnException) {
        return new CallbackRecordListener(
            onStoreStart,
            onStoreEnd,
            onInsertStart,
            onInsertEnd,
            onUpdateStart,
            onUpdateEnd,
            onMergeStart,
            onMergeEnd,
            onDeleteStart,
            onDeleteEnd,
            onLoadStart,
            onLoadEnd,
            onRefreshStart,
            onRefreshEnd,
            newOnException
        );
    }
}

