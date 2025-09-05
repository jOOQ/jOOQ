/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static java.lang.Boolean.FALSE;
import static org.jooq.conf.InvocationOrder.REVERSE;
import static org.jooq.impl.DefaultExecuteContext.GLOBAL_EXECUTE_CONTEXT;
import static org.jooq.impl.Tools.EMPTY_EXECUTE_LISTENER;

import java.util.ArrayList;
import java.util.List;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.LoggerListener;

/**
 * A queue implementation for several {@link ExecuteListener} objects as defined
 * in {@link Settings#getExecuteListeners()}
 *
 * @author Lukas Eder
 */
final class ExecuteListeners implements ExecuteListener {

    private static final ExecuteListener EMPTY_LISTENER         = new DefaultExecuteListener();
    private static final JooqLogger      LOGGER_LISTENER_LOGGER = JooqLogger.getLogger(LoggerListener.class);




    private final ExecuteListener[][]    listeners;
    private final boolean                threadbound;

    // In some setups, these two events may get mixed up chronologically by the
    // Cursor. Postpone fetchEnd event until after resultEnd event, if there is
    // an open Result
    private boolean                      resultStart;
    private boolean                      fetchEnd;

    /**
     * Initialise the provided {@link ExecuteListener} set and return a wrapper.
     *
     * @param ctx The {@link ExecuteContext}.
     * @param threadbound Whether the {@link ExecuteContext} is thread bound,
     *            e.g. in ordinary JDBC use-cases, not reactive execution.
     */
    static final ExecuteListener get(ExecuteContext ctx, boolean threadbound) {
        ExecuteListener[][] listeners = listeners(ctx);

        if (listeners == null)
            return EMPTY_LISTENER;
        else
            return new ExecuteListeners(listeners, threadbound);
    }

    /**
     * Initialise the provided {@link ExecuteListener} set and return a wrapper.
     * <p>
     * Call this if the {@link ExecuteListener#start(ExecuteContext)} event
     * should be triggered eagerly.
     *
     * @param ctx The {@link ExecuteContext}.
     * @param threadbound Whether the {@link ExecuteContext} is thread bound,
     *            e.g. in ordinary JDBC use-cases, not reactive execution.
     */
    static final ExecuteListener getAndStart(ExecuteContext ctx, boolean threadbound) {
        ExecuteListener result = get(ctx, threadbound);
        result.start(ctx);
        return result;
    }

    /**
     * Provide delegate listeners from an <code>ExecuteContext</code>
     */
    private static final ExecuteListener[][] listeners(ExecuteContext ctx) {
        List<ExecuteListener> list = null;

        // jOOQ-internal listeners are added first, so their results are available to user-defined listeners
        // -------------------------------------------------------------------------------------------------

        // [#6580] Fetching server output may require some pre / post actions around the actual statement
        if (SettingsTools.getFetchServerOutputSize(0, ctx.settings()) > 0)
            (list = init(list)).add(new FetchServerOutputListener());

        // [#6051] The previously used StopWatchListener is no longer included by default
        if (!FALSE.equals(ctx.settings().isExecuteLogging())) {

            // [#6747] Avoid allocating the listener (and by consequence, the ExecuteListeners) if
            //         we do not DEBUG log anyway.
            if (LOGGER_LISTENER_LOGGER.isDebugEnabled())
                (list = init(list)).add(new LoggerListener());
        }









        for (ExecuteListenerProvider provider : ctx.configuration().executeListenerProviders())

            // Could be null after deserialisation
            if (provider != null)
                (list = init(list)).add(provider.provide());

        if (list == null)
            return null;

        ExecuteListener[] def = list.toArray(EMPTY_EXECUTE_LISTENER);
        ExecuteListener[] rev = null;

        return new ExecuteListener[][] {
            ctx.settings().getExecuteListenerStartInvocationOrder() != REVERSE ? def : (                     rev = Tools.reverse(def.clone())),
            ctx.settings().getExecuteListenerEndInvocationOrder()   != REVERSE ? def : (rev != null ? rev : (rev = Tools.reverse(def.clone())))
        };
    }

    private static final List<ExecuteListener> init(List<ExecuteListener> result) {
        return result == null ? new ArrayList<>() : result;
    }

    private ExecuteListeners(ExecuteListener[][] listeners, boolean threadbound) {
        this.listeners = listeners;
        this.threadbound = threadbound;
    }

    @Override
    public final void start(ExecuteContext ctx) {
        if (threadbound && ctx instanceof DefaultExecuteContext c)
            GLOBAL_EXECUTE_CONTEXT.set(c);

        for (ExecuteListener listener : listeners[0])
            listener.start(ctx);
    }

















    @Override
    public final void renderStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.renderStart(ctx);
    }

    @Override
    public final void renderEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[1])
            listener.renderEnd(ctx);
    }

    @Override
    public final void prepareStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.prepareStart(ctx);
    }

    @Override
    public final void prepareEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[1])
            listener.prepareEnd(ctx);
    }

    @Override
    public final void bindStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.bindStart(ctx);
    }

    @Override
    public final void bindEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[1])
            listener.bindEnd(ctx);
    }

    @Override
    public final void executeStart(ExecuteContext ctx) {
        if (ctx instanceof DefaultExecuteContext d)
            d.incrementStatementExecutionCount();

        for (ExecuteListener listener : listeners[0])
            listener.executeStart(ctx);
    }

    @Override
    public final void executeEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[1])
            listener.executeEnd(ctx);
    }

    @Override
    public final void fetchStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.fetchStart(ctx);
    }

    @Override
    public final void outStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.outStart(ctx);
    }

    @Override
    public final void outEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[1])
            listener.outEnd(ctx);
    }

    @Override
    public final void resultStart(ExecuteContext ctx) {
        resultStart = true;

        for (ExecuteListener listener : listeners[0])
            listener.resultStart(ctx);

        ((DefaultExecuteContext) ctx).resultLevel++;
    }

    @Override
    public final void recordStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.recordStart(ctx);

        ((DefaultExecuteContext) ctx).recordLevel++;
    }

    @Override
    public final void recordEnd(ExecuteContext ctx) {
        ((DefaultExecuteContext) ctx).recordLevel--;

        for (ExecuteListener listener : listeners[1])
            listener.recordEnd(ctx);
    }

    @Override
    public final void resultEnd(ExecuteContext ctx) {
        ((DefaultExecuteContext) ctx).resultLevel--;
        resultStart = false;

        for (ExecuteListener listener : listeners[1])
            listener.resultEnd(ctx);

        if (fetchEnd)
            fetchEnd(ctx);
    }

    @Override
    public final void fetchEnd(ExecuteContext ctx) {
        if (resultStart)
            fetchEnd = true;
        else
            for (ExecuteListener listener : listeners[1])
                listener.fetchEnd(ctx);
    }

    @Override
    public final void end(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[1])
            listener.end(ctx);

        GLOBAL_EXECUTE_CONTEXT.remove();
    }

    @Override
    public final void exception(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.exception(ctx);
    }

    @Override
    public final void warning(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.warning(ctx);
    }
}
