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

import static java.lang.Boolean.FALSE;
import static org.jooq.conf.InvocationOrder.REVERSE;
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

    /**
     * Generated UID
     */
    private static final long            serialVersionUID       = 7399239846062763212L;
    private static final ExecuteListener EMPTY_LISTENER         = new DefaultExecuteListener();
    private static final JooqLogger      LOGGER_LISTENER_LOGGER = JooqLogger.getLogger(LoggerListener.class);

    private final ExecuteListener[][]    listeners;

    // In some setups, these two events may get mixed up chronologically by the
    // Cursor. Postpone fetchEnd event until after resultEnd event, if there is
    // an open Result
    private boolean                      resultStart;
    private boolean                      fetchEnd;

    /**
     * Initialise the provided {@link ExecuteListener} set and return a wrapper.
     */
    static ExecuteListener get(ExecuteContext ctx) {
        ExecuteListener[][] listeners = listeners(ctx);

        if (listeners == null)
            return EMPTY_LISTENER;
        else
            return new ExecuteListeners(listeners);
    }

    /**
     * Initialise the provided {@link ExecuteListener} set and return a wrapper.
     * <p>
     * Call this if the {@link ExecuteListener#start(ExecuteContext)} event
     * should be triggered eagerly.
     */
    static ExecuteListener getAndStart(ExecuteContext ctx) {
        ExecuteListener result = get(ctx);
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

    private ExecuteListeners(ExecuteListener[][] listeners) {
        this.listeners = listeners;
    }

    @Override
    public final void start(ExecuteContext ctx) {
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
        if (ctx instanceof DefaultExecuteContext)
            ((DefaultExecuteContext) ctx).incrementStatementExecutionCount();

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
    }

    @Override
    public final void recordStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[0])
            listener.recordStart(ctx);
    }

    @Override
    public final void recordEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners[1])
            listener.recordEnd(ctx);
    }

    @Override
    public final void resultEnd(ExecuteContext ctx) {
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
