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
package org.jooq.impl;

import static java.lang.Boolean.FALSE;

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
class ExecuteListeners implements ExecuteListener {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = 7399239846062763212L;

    private final List<ExecuteListener> listeners;

    // In some setups, these two events may get mixed up chronologically by the
    // Cursor. Postpone fetchEnd event until after resultEnd event, if there is
    // an open Result
    private boolean                     resultStart;
    private boolean                     fetchEnd;

    ExecuteListeners(ExecuteContext ctx) {
        listeners = listeners(ctx);

        start(ctx);
    }

    /**
     * Provide delegate listeners from an <code>ExecuteContext</code>
     */
    private static List<ExecuteListener> listeners(ExecuteContext ctx) {
        List<ExecuteListener> result = new ArrayList<ExecuteListener>();
        
        if (!FALSE.equals(ctx.configuration().settings().isExecuteLogging())) {
            result.add(new StopWatchListener());
            result.add(new LoggerListener());
        }

        for (ExecuteListenerProvider provider : ctx.configuration().executeListenerProviders()) {

            // Could be null after deserialisation
            if (provider != null) {
                result.add(provider.provide());
            }
        }

        return result;
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
}
