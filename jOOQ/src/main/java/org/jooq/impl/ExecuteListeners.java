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

import static java.lang.Boolean.FALSE;
import static org.jooq.impl.Tools.EMPTY_EXECUTE_LISTENER;

import java.util.ArrayList;
import java.util.List;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.tools.LoggerListener;
import org.jooq.tools.StopWatchListener;

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
    private static final long       serialVersionUID = 7399239846062763212L;

    private final ExecuteListener[] listeners;

    // In some setups, these two events may get mixed up chronologically by the
    // Cursor. Postpone fetchEnd event until after resultEnd event, if there is
    // an open Result
    private boolean                 resultStart;
    private boolean                 fetchEnd;

    ExecuteListeners(ExecuteContext ctx) {
        listeners = listeners(ctx);

        start(ctx);
    }

    /**
     * Provide delegate listeners from an <code>ExecuteContext</code>
     */
    private static ExecuteListener[] listeners(ExecuteContext ctx) {
        List<ExecuteListener> result = new ArrayList<ExecuteListener>();

        for (ExecuteListenerProvider provider : ctx.configuration().executeListenerProviders())

            // Could be null after deserialisation
            if (provider != null)
                result.add(provider.provide());

        if (!FALSE.equals(ctx.configuration().settings().isExecuteLogging())) {
            result.add(new LoggerListener());
            result.add(new StopWatchListener());
        }

        return result.toArray(EMPTY_EXECUTE_LISTENER);
    }

    @Override
    public final void start(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.start(ctx);
        }
    }

    @Override
    public final void renderStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.renderStart(ctx);
        }
    }

    @Override
    public final void renderEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.renderEnd(ctx);
        }
    }

    @Override
    public final void prepareStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.prepareStart(ctx);
        }
    }

    @Override
    public final void prepareEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.prepareEnd(ctx);
        }
    }

    @Override
    public final void bindStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.bindStart(ctx);
        }
    }

    @Override
    public final void bindEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.bindEnd(ctx);
        }
    }

    @Override
    public final void executeStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.executeStart(ctx);
        }
    }

    @Override
    public final void executeEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.executeEnd(ctx);
        }
    }

    @Override
    public final void fetchStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.fetchStart(ctx);
        }
    }

    @Override
    public final void outStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.outStart(ctx);
        }
    }

    @Override
    public final void outEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.outEnd(ctx);
        }
    }

    @Override
    public final void resultStart(ExecuteContext ctx) {
        resultStart = true;

        for (ExecuteListener listener : listeners) {
            listener.resultStart(ctx);
        }
    }

    @Override
    public final void recordStart(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.recordStart(ctx);
        }
    }

    @Override
    public final void recordEnd(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.recordEnd(ctx);
        }
    }

    @Override
    public final void resultEnd(ExecuteContext ctx) {
        resultStart = false;

        for (ExecuteListener listener : listeners) {
            listener.resultEnd(ctx);
        }

        if (fetchEnd) {
            fetchEnd(ctx);
        }
    }

    @Override
    public final void fetchEnd(ExecuteContext ctx) {
        if (resultStart) {
            fetchEnd = true;
        }
        else {
            for (ExecuteListener listener : listeners) {
                listener.fetchEnd(ctx);
            }
        }
    }

    @Override
    public final void end(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.end(ctx);
        }
    }

    @Override
    public final void exception(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.exception(ctx);
        }
    }

    @Override
    public final void warning(ExecuteContext ctx) {
        for (ExecuteListener listener : listeners) {
            listener.warning(ctx);
        }
    }
}
