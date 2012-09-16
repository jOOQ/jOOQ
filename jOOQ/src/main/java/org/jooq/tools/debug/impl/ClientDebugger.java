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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.tools.debug.Breakpoint;
import org.jooq.tools.debug.BreakpointListener;
import org.jooq.tools.debug.Debugger;
import org.jooq.tools.debug.HitContext;
import org.jooq.tools.debug.Logger;
import org.jooq.tools.debug.LoggerListener;
import org.jooq.tools.debug.Matcher;
import org.jooq.tools.debug.QueryExecutor;
import org.jooq.tools.debug.QueryLog;
import org.jooq.tools.debug.ResultLog;
import org.jooq.tools.debug.Step;
import org.jooq.tools.debug.impl.Message.NoResult;

/**
 * The client side of client-server debugging
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
class ClientDebugger extends AbstractDebuggerObject implements Debugger {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1867377595857961400L;

    private Communication     comm;
    private List<Matcher>     matchers         = new ArrayList<Matcher>();

    public ClientDebugger(String ip, int port) throws Exception {
        comm = new ClientCommunication(this, port, ip);
    }

    Communication getCommunicationInterface() {
        return comm;
    }

    @Override
    void apply() {}

    @Override
    public void remove() {
        comm.close();
    }

    @Override
    public synchronized Matcher newMatcher() {
        final MatcherImpl result = new MatcherImpl();
        result.setDelegate(new AbstractDebuggerObject() {

            /**
             * Generated UID
             */
            private static final long serialVersionUID = 549633622817329607L;

            @Override
            public void remove() {
                synchronized (ClientDebugger.this) {
                    matchers.remove(result);
                }

                comm.asyncSend(new CMC_removeMatcher(result.getId()));
            }

            @Override
            void apply() {
                comm.asyncSend(new CMC_setMatcher(result));
            }
        });

        synchronized (ClientDebugger.this) {
            matchers.add(result);
        }

        return result;
    }

    @Override
    public synchronized Matcher[] matchers() {
        return matchers.toArray(new Matcher[matchers.size()]);
    }

    static class CMS_logQuery extends CommandMessage<NoResult> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 5517468629186256571L;

        private final UUID        logger;
        private final QueryLog    log;

        CMS_logQuery(UUID logger, QueryLog log) {
            this.logger = logger;
            this.log = log;
        }

        @Override
        public NoResult run(MessageContext context) {
            for (Matcher m : context.getClientDebugger().matchers) {
                for (Logger l : m.loggers()) {
                    if (l.getId().equals(logger)) {
                        LoggerListener listener = l.listener();

                        if (listener != null) {
                            listener.logQuery(log);
                        }
                    }
                }
            }

            return null;
        }
    }

    static class CMS_logResult extends CommandMessage<NoResult> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -5103500454940646628L;

        private final UUID        logger;
        private final ResultLog   log;

        CMS_logResult(UUID logger, ResultLog log) {
            this.logger = logger;
            this.log = log;
        }

        @Override
        public NoResult run(MessageContext context) {
            for (Matcher m : context.getClientDebugger().matchers) {
                for (Logger l : m.loggers()) {
                    if (l.getId().equals(logger)) {
                        LoggerListener listener = l.listener();

                        if (listener != null) {
                            listener.logResult(log);
                        }
                    }
                }
            }

            return null;
        }
    }

    static class CMS_step extends CommandMessage<Step> {

        /**
         * Generated UID
         */
        private static final long       serialVersionUID = 3392847306059043330L;

        private final boolean           before;
        private final UUID              breakpoint;
        private final HitContextImpl    hitContext;
        private transient QueryExecutor executor;

        CMS_step(boolean before, UUID breakpoint, HitContextImpl hitContext) {
            this.before = before;
            this.breakpoint = breakpoint;
            this.hitContext = hitContext;
        }

        @Override
        public Step run(MessageContext context) throws Exception {
            initialiseHitContext(context);

            for (Matcher m : context.getClientDebugger().matchers) {
                for (Breakpoint b : m.breakpoints()) {
                    if (b.getId().equals(breakpoint)) {
                        BreakpointListener listener = b.listener();

                        if (listener != null) {
                            if (before) {
                                return listener.before(hitContext);
                            }
                            else {
                                return listener.after(hitContext);
                            }
                        }
                    }
                }
            }

            return Step.STEP;
        }

        private void initialiseHitContext(final MessageContext context) {
            if (executor == null) {
                executor = new QueryExecutor() {

                    @Override
                    public <R extends Record> Result<R> fetch(ResultQuery<R> query) {
                        return context.getClientDebugger().getCommunicationInterface().syncSend(new CMC_fetch<R>(query));
                    }

                    @Override
                    public int execute(Query query) {
                        return context.getClientDebugger().getCommunicationInterface().syncSend(new CMC_execute(query));
                    }
                };
            }

            hitContext.executor(executor);
        }
    }

    static class CMC_fetch<R extends Record> extends CommandMessage<Result<R>> {

        /**
         * Generated UID
         */
        private static final long    serialVersionUID = -9163573787750356644L;

        private final ResultQuery<R> query;

        CMC_fetch(ResultQuery<R> query) {
            this.query = query;
        }

        @Override
        public Result<R> run(MessageContext context) {
            return DebugListener.BREAKPOINT_EXECUTORS.get().fetch(query);
        }
    }

    static class CMC_execute extends CommandMessage<Integer> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -4323062835006905610L;

        private final Query       query;

        CMC_execute(Query query) {
            this.query = query;
        }

        @Override
        public Integer run(MessageContext context) {
            return DebugListener.BREAKPOINT_EXECUTORS.get().execute(query);
        }
    }

    static class CMC_setMatcher extends CommandMessage<NoResult> {

        /**
         * Generated UID
         */
        private static final long            serialVersionUID = -7927241101645290722L;

        private final Matcher                matcher;
        private transient LoggerListener     loggerListener;
        private transient BreakpointListener breakpointListener;

        CMC_setMatcher(Matcher matcher) {
            // TODO: This seems to be needed. Is there some weird deserialisation cache?
            this.matcher = Communication.deepClone(matcher);
        }

        @Override
        public NoResult run(final MessageContext context) {
            initialiseLogger(context);
            initialiseBreakpoint(context);

            context.getServerDebugger().setMatcher(matcher);
            return null;
        }

        private void initialiseLogger(final MessageContext context) {
            for (final Logger logger : matcher.loggers()) {
                if (loggerListener == null) {
                    loggerListener = new LoggerListener() {

                        @Override
                        public void logQuery(QueryLog log) {
                            context.getServerDebugger().getCommunicationInterface().asyncSend(new CMS_logQuery(logger.getId(), log));
                        }

                        @Override
                        public void logResult(ResultLog log) {
                            context.getServerDebugger().getCommunicationInterface().asyncSend(new CMS_logResult(logger.getId(), log));
                        }
                    };
                }

                logger.listener(loggerListener);
            }
        }

        private void initialiseBreakpoint(final MessageContext context) {
            for (final Breakpoint breakpoint : matcher.breakpoints()) {
                if (breakpointListener == null) {
                    breakpointListener = new BreakpointListener() {

                        @Override
                        public Step before(HitContext hitContext) {
                            return context.getServerDebugger().getCommunicationInterface().syncSend(new CMS_step(true, breakpoint.getId(), (HitContextImpl) hitContext));
                        }

                        @Override
                        public Step after(HitContext hitContext) {
                            return context.getServerDebugger().getCommunicationInterface().syncSend(new CMS_step(false, breakpoint.getId(), (HitContextImpl) hitContext));
                        }
                    };
                }

                breakpoint.listener(breakpointListener);
            }
        }
    }

    static class CMC_removeMatcher extends CommandMessage<NoResult> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2063445258732465943L;

        private final UUID        matcher;

        CMC_removeMatcher(UUID matcher) {
            this.matcher = matcher;
        }

        @Override
        public NoResult run(MessageContext context) {
            for (Matcher m : context.getClientDebugger().matchers) {
                if (m.getId().equals(matcher)) {
                    context.getServerDebugger().removeMatcher(m);
                }
            }

            return null;
        }
    }
}
