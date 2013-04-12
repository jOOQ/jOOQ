/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
