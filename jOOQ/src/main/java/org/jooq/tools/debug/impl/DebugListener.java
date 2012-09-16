/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
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
package org.jooq.tools.debug.impl;

import static org.jooq.conf.ExecuteDebugging.SERVER;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.debug.Step.STEP;

import java.util.ArrayList;
import java.util.List;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Query;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.Factory;
import org.jooq.tools.debug.Action;
import org.jooq.tools.debug.Breakpoint;
import org.jooq.tools.debug.BreakpointListener;
import org.jooq.tools.debug.Debugger;
import org.jooq.tools.debug.HitContext;
import org.jooq.tools.debug.Logger;
import org.jooq.tools.debug.LoggerListener;
import org.jooq.tools.debug.Matcher;
import org.jooq.tools.debug.Processor;
import org.jooq.tools.debug.QueryExecutor;
import org.jooq.tools.debug.QueryLog;
import org.jooq.tools.debug.ResultLog;
import org.jooq.tools.debug.Step;
import org.jooq.tools.debug.impl.LocalDebugger.DebuggerRegistry;

/**
 * An {@link ExecuteListener} implementation for the jOOQ debugger API
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public class DebugListener extends DefaultExecuteListener {

    private static final ThreadLocal<Object> RECURSION_LOCK       = new ThreadLocal<Object>();
    static final ThreadLocal<QueryExecutor>  BREAKPOINT_EXECUTORS = new ThreadLocal<QueryExecutor>();

    private boolean                          hasDebuggers;

    private long                             startPrepareTime;
    private long                             endPrepareTime;

    private long                             startBindTime;
    private long                             endBindTime;

    private long                             startExecuteTime;
    private long                             endExecuteTime;

    private long                             startFetchTime;
    private long                             endFetchTime;

    private List<Matcher>                    matchers;
    private QueryLog                         log;

    @Override
    public void start(ExecuteContext ctx) {
        if (ctx.getSettings().getExecuteDebugging() == SERVER) {
            Server.initialise(ctx.getSettings().getExecuteDebuggingPort());
        }
    }

    @Override
    public void renderEnd(final ExecuteContext ctx) {
        hasDebuggers = !DebuggerRegistry.get().isEmpty();
        startPrepareTime = 0;
        endPrepareTime = 0;
        startBindTime = 0;
        endBindTime = 0;
        startExecuteTime = 0;
        endExecuteTime = 0;

        // Avoid recursion for processors and breakpoints.
        recursionSafe(new Runnable() {
            @Override
            public void run() {
                Factory create = create(ctx);

                // Process "before" processors. These processors must not trigger
                // recursive debugger reactions (e.g. further processors or breakpoints)
                for (Matcher matcher : matchers(ctx)) {
                    for (Processor processor : matcher.processors()) {
                        for (Action action : processor.before()) {
                            execute(create, action);
                        }
                    }
                }

                // Process breakpoints
                breakpoints(true, ctx);
            }

        });
    }

    @Override
    public void prepareStart(ExecuteContext ctx) {
        if (!hasDebuggers) return;
        startPrepareTime = System.nanoTime();
    }

    @Override
    public void prepareEnd(ExecuteContext ctx) {
        if (!hasDebuggers) return;
        endPrepareTime = System.nanoTime();
    }

    @Override
    public void bindStart(ExecuteContext ctx) {
        if (!hasDebuggers) return;
        startBindTime = System.nanoTime();
    }

    @Override
    public void bindEnd(ExecuteContext ctx) {
        if (!hasDebuggers) return;
        endBindTime = System.nanoTime();
    }

    @Override
    public void executeStart(ExecuteContext ctx) {
        if (!hasDebuggers) return;
        startExecuteTime = System.nanoTime();
    }

    @Override
    public void executeEnd(final ExecuteContext ctx) {
        if (!hasDebuggers) return;
        endExecuteTime = System.nanoTime();

        // Log the QueryLog, first
        for (Matcher matcher : matchers(ctx)) {
            for (Logger logger : matcher.loggers()) {
                LoggerListener listener = logger.listener();

                if (listener != null) {
                    if (logger.logQuery()) {
                        listener.logQuery(getLog(ctx));
                    }
                }
            }
        }

        // Avoid recursion for processors and breakpoints.
        recursionSafe(new Runnable() {
            @Override
            public void run() {
                Factory create = create(ctx);

                // Process "after" processors
                for (Matcher matcher : matchers(ctx)) {
                    for (Processor processor : matcher.processors()) {
                        for (Action action : processor.after()) {
                            execute(create, action);
                        }
                    }
                }

                // Process breakpoints
                breakpoints(false, ctx);
            }
        });
    }

    @Override
    public void fetchStart(ExecuteContext ctx) {
        if (!hasDebuggers) return;
        startFetchTime = System.nanoTime();
    }

    @Override
    public void fetchEnd(final ExecuteContext ctx) {
        if (!hasDebuggers) return;
        endFetchTime = System.nanoTime();

        // Log ResultLogs
        Result<?> result = ctx.result();
        if (result != null) {
            ResultLog resultLog = null;

            for (Matcher matcher : matchers(ctx)) {
                for (Logger logger : matcher.loggers()) {
                    if (logger.logResult()) {
                        LoggerListener listener = logger.listener();

                        if (listener != null) {
                            if (resultLog == null) {
                                resultLog = new ResultLog(
                                    getLog(ctx).getId(),
                                    endFetchTime - startFetchTime,
                                    result.size()
                                );
                            }

                            listener.logResult(resultLog);
                        }
                    }
                }
            }
        }
    }

    private void execute(Factory create, Action action) {
        Query query = action.query();

        // TODO [#1829] It should be possible to execute any query using Factory.execute()
        if (query instanceof ResultQuery) {
            create.fetchLazy(create.renderInlined(query)).close();
        }
        else {
            create.execute(create.renderInlined(query));
        }
    }

    private void breakpoints(boolean before, final ExecuteContext ctx) {
        matcherLoop:
        for (Matcher matcher : matchers(ctx)) {
            for (Breakpoint breakpoint : matcher.breakpoints()) {
                BreakpointListener listener = breakpoint.listener();

                if (listener != null) {
                    QueryExecutor executor = new QueryExecutorImpl(ctx);
                    BREAKPOINT_EXECUTORS.set(executor);

                    try {
                        HitContext hitContext = new HitContextImpl(ctx.query(), executor);
                        Step step = null;

                        try {
                            if (before) {
                                step = listener.before(hitContext);
                            }
                            else {
                                step = listener.after(hitContext);
                            }
                        }
                        catch (Exception e) {
                            Utils.doThrow(e);
                        }

                        listenerStep:
                        switch (defaultIfNull(step, STEP)) {
                            case STEP:
                                break listenerStep;
                            case SKIP:
                                // TODO implement this
                                break matcherLoop;
                            case FAIL:
                                throw new DataAccessException("Failing query");
                        }
                    }
                    finally {
                        BREAKPOINT_EXECUTORS.remove();
                    }
                }
            }
        }
    }

    private void recursionSafe(Runnable runnable) {
        if (RECURSION_LOCK.get() == null) {
            RECURSION_LOCK.set(new Object());

            try {
                runnable.run();
            }
            finally {
                RECURSION_LOCK.remove();
            }
        }
    }

    private List<Matcher> matchers(ExecuteContext ctx) {
        if (matchers == null) {
            matchers = new ArrayList<Matcher>();

            for (Debugger debugger : DebuggerRegistry.get()) {
                for (Matcher matcher : debugger.matchers()) {
                    if (((MatcherImpl) matcher).matches(ctx)) {
                        matchers.add(matcher);
                    }
                }
            }
        }

        return matchers;
    }

    private QueryLog getLog(ExecuteContext ctx) {
        if (log == null) {
            log = new QueryLog(
                ctx.query(),
                endPrepareTime - startPrepareTime,
                endBindTime - startBindTime,
                endExecuteTime - startExecuteTime
            );
        }

        return log;
    }

    private Factory create(ExecuteContext ctx) {
        return new Factory(ctx.getConnection(), ctx.getDialect(), ctx.getSettings());
    }
}
