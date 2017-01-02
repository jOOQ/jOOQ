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
 */
package org.jooq.impl;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteEventHandler;
import org.jooq.ExecuteListener;

/**
 * An {@link ExecuteListener} that allows for functional composition.
 * <p>
 * For example: <code><pre>
 * ExecuteListener listener = new CallbackExecuteListener()
 *   .onExecuteStart(ctx -> something())
 *   .onExecuteEnd(ctx -> something());
 * </pre></code>
 *
 * @author Lukas Eder
 */
public final class CallbackExecuteListener implements ExecuteListener {

    /**
     * Generated UID
     */
    private static final long         serialVersionUID = -4135358887698253754L;

    private final ExecuteEventHandler onStart;
    private final ExecuteEventHandler onEnd;
    private final ExecuteEventHandler onRenderStart;
    private final ExecuteEventHandler onRenderEnd;
    private final ExecuteEventHandler onPrepareStart;
    private final ExecuteEventHandler onPrepareEnd;
    private final ExecuteEventHandler onBindStart;
    private final ExecuteEventHandler onBindEnd;
    private final ExecuteEventHandler onExecuteStart;
    private final ExecuteEventHandler onExecuteEnd;
    private final ExecuteEventHandler onOutStart;
    private final ExecuteEventHandler onOutEnd;
    private final ExecuteEventHandler onFetchStart;
    private final ExecuteEventHandler onResultStart;
    private final ExecuteEventHandler onRecordStart;
    private final ExecuteEventHandler onRecordEnd;
    private final ExecuteEventHandler onResultEnd;
    private final ExecuteEventHandler onFetchEnd;
    private final ExecuteEventHandler onException;
    private final ExecuteEventHandler onWarning;

    public CallbackExecuteListener() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    private CallbackExecuteListener(
        ExecuteEventHandler onStart,
        ExecuteEventHandler onEnd,
        ExecuteEventHandler onRenderStart,
        ExecuteEventHandler onRenderEnd,
        ExecuteEventHandler onPrepareStart,
        ExecuteEventHandler onPrepareEnd,
        ExecuteEventHandler onBindStart,
        ExecuteEventHandler onBindEnd,
        ExecuteEventHandler onExecuteStart,
        ExecuteEventHandler onExecuteEnd,
        ExecuteEventHandler onOUtStart,
        ExecuteEventHandler onOUtEnd,
        ExecuteEventHandler onFetchStart,
        ExecuteEventHandler onResultStart,
        ExecuteEventHandler onRecordStart,
        ExecuteEventHandler onRecordEnd,
        ExecuteEventHandler onResultEnd,
        ExecuteEventHandler onFetchEnd,
        ExecuteEventHandler onException,
        ExecuteEventHandler onWarning
    ) {
        this.onStart = onStart;
        this.onEnd = onEnd;
        this.onRenderStart = onRenderStart;
        this.onRenderEnd = onRenderEnd;
        this.onPrepareStart = onPrepareStart;
        this.onPrepareEnd = onPrepareEnd;
        this.onBindStart = onBindStart;
        this.onBindEnd = onBindEnd;
        this.onExecuteStart = onExecuteStart;
        this.onExecuteEnd = onExecuteEnd;
        this.onOutStart = onOUtStart;
        this.onOutEnd = onOUtEnd;
        this.onFetchStart = onFetchStart;
        this.onResultStart = onResultStart;
        this.onRecordStart = onRecordStart;
        this.onRecordEnd = onRecordEnd;
        this.onResultEnd = onResultEnd;
        this.onFetchEnd = onFetchEnd;
        this.onException = onException;
        this.onWarning = onWarning;
    }

    @Override
    public final void start(ExecuteContext ctx) {
        if (onStart != null)
            onStart.fire(ctx);
    }

    @Override
    public final void renderStart(ExecuteContext ctx) {
        if (onRenderStart != null)
            onRenderStart.fire(ctx);
    }

    @Override
    public final void renderEnd(ExecuteContext ctx) {
        if (onRenderEnd != null)
            onRenderEnd.fire(ctx);
    }

    @Override
    public final void prepareStart(ExecuteContext ctx) {
        if (onPrepareStart != null)
            onPrepareStart.fire(ctx);
    }

    @Override
    public final void prepareEnd(ExecuteContext ctx) {
        if (onPrepareEnd != null)
            onPrepareEnd.fire(ctx);
    }

    @Override
    public final void bindStart(ExecuteContext ctx) {
        if (onBindStart != null)
            onBindStart.fire(ctx);
    }

    @Override
    public final void bindEnd(ExecuteContext ctx) {
        if (onBindEnd != null)
            onBindEnd.fire(ctx);
    }

    @Override
    public final void executeStart(ExecuteContext ctx) {
        if (onExecuteStart != null)
            onExecuteStart.fire(ctx);
    }

    @Override
    public final void executeEnd(ExecuteContext ctx) {
        if (onExecuteEnd != null)
            onExecuteEnd.fire(ctx);
    }

    @Override
    public final void outStart(ExecuteContext ctx) {
        if (onOutStart != null)
            onOutStart.fire(ctx);
    }

    @Override
    public final void outEnd(ExecuteContext ctx) {
        if (onOutEnd != null)
            onOutEnd.fire(ctx);
    }

    @Override
    public final void fetchStart(ExecuteContext ctx) {
        if (onFetchStart != null)
            onFetchStart.fire(ctx);
    }

    @Override
    public final void resultStart(ExecuteContext ctx) {
        if (onResultStart != null)
            onResultStart.fire(ctx);
    }

    @Override
    public final void recordStart(ExecuteContext ctx) {
        if (onRecordStart != null)
            onRecordStart.fire(ctx);
    }

    @Override
    public final void recordEnd(ExecuteContext ctx) {
        if (onRecordEnd != null)
            onRecordEnd.fire(ctx);
    }

    @Override
    public final void resultEnd(ExecuteContext ctx) {
        if (onResultEnd != null)
            onResultEnd.fire(ctx);
    }

    @Override
    public final void fetchEnd(ExecuteContext ctx) {
        if (onFetchEnd != null)
            onFetchEnd.fire(ctx);
    }

    @Override
    public final void end(ExecuteContext ctx) {
        if (onEnd != null)
            onEnd.fire(ctx);
    }

    @Override
    public final void exception(ExecuteContext ctx) {
        if (onException != null)
            onException.fire(ctx);
    }

    @Override
    public final void warning(ExecuteContext ctx) {
        if (onWarning != null)
            onWarning.fire(ctx);
    }

    public final CallbackExecuteListener onStart(ExecuteEventHandler newOnStart) {
        return new CallbackExecuteListener(
            newOnStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onRenderStart(ExecuteEventHandler newOnRenderStart) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            newOnRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onRenderEnd(ExecuteEventHandler newOnRenderEnd) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            newOnRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onPrepareStart(ExecuteEventHandler newOnPrepareStart) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            newOnPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onPrepareEnd(ExecuteEventHandler newOnPrepareEnd) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            newOnPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onBindStart(ExecuteEventHandler newOnBindStart) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            newOnBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onBindEnd(ExecuteEventHandler newOnBindEnd) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            newOnBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onExecuteStart(ExecuteEventHandler newOnExecuteStart) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            newOnExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onExecuteEnd(ExecuteEventHandler newOnExecuteEnd) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            newOnExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onOutStart(ExecuteEventHandler newOnOutStart) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            newOnOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onOutEnd(ExecuteEventHandler newOnOutEnd) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            newOnOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onFetchStart(ExecuteEventHandler newOnFetchStart) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            newOnFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onResultStart(ExecuteEventHandler newOnResultStart) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            newOnResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onRecordStart(ExecuteEventHandler newOnRecordStart) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            newOnRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onRecordEnd(ExecuteEventHandler newOnRecordEnd) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            newOnRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onResultEnd(ExecuteEventHandler newOnResultEnd) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            newOnResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onFetchEnd(ExecuteEventHandler newOnFetchEnd) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            newOnFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onEnd(ExecuteEventHandler newOnEnd) {
        return new CallbackExecuteListener(
            onStart,
            newOnEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            onWarning
        );
    }

    public final CallbackExecuteListener onException(ExecuteEventHandler newOnException) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            newOnException,
            onWarning
        );
    }

    public final CallbackExecuteListener onWarning(ExecuteEventHandler newOnWarning) {
        return new CallbackExecuteListener(
            onStart,
            onEnd,
            onRenderStart,
            onRenderEnd,
            onPrepareStart,
            onPrepareEnd,
            onBindStart,
            onBindEnd,
            onExecuteStart,
            onExecuteEnd,
            onOutStart,
            onOutEnd,
            onFetchStart,
            onResultStart,
            onRecordStart,
            onRecordEnd,
            onResultEnd,
            onFetchEnd,
            onException,
            newOnWarning
        );
    }
}
