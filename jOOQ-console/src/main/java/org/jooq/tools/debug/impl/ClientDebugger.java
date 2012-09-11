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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.ExecuteContext;
import org.jooq.tools.debug.Breakpoint;
import org.jooq.tools.debug.BreakpointHit;
import org.jooq.tools.debug.BreakpointHitHandler;
import org.jooq.tools.debug.Debugger;
import org.jooq.tools.debug.LoggingListener;
import org.jooq.tools.debug.QueryExecutor;
import org.jooq.tools.debug.QueryLog;
import org.jooq.tools.debug.ResultLog;
import org.jooq.tools.debug.impl.ServerDebugger.CMS_addBreakpoint;
import org.jooq.tools.debug.impl.ServerDebugger.CMS_isExecutionSupported;
import org.jooq.tools.debug.impl.ServerDebugger.CMS_modifyBreakpoint;
import org.jooq.tools.debug.impl.ServerDebugger.CMS_removeBreakpoint;
import org.jooq.tools.debug.impl.ServerDebugger.CMS_setBreakpointHitHandlerActive;
import org.jooq.tools.debug.impl.ServerDebugger.CMS_setLoggingActive;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
class ClientDebugger implements Debugger {

    private Communication comm;

	public ClientDebugger(String ip, int port) throws Exception {
	    comm = new ClientCommunication(this, port, ip);
	}

    Communication getCommunicationInterface() {
        return comm;
    }

    @Override
    public void close() {}

    private LoggingListener loggingListener;
    private final Object LOGGING_LISTENER_LOCK = new Object();

    @Override
    public void setLoggingListener(LoggingListener listener) {
        synchronized (LOGGING_LISTENER_LOCK) {
            if(this.loggingListener == listener) {
                return;
            }
            this.loggingListener = listener;
        }
        comm.asyncSend((CommandMessage<?>) new CMS_setLoggingActive(
            listener != null,
            listener != null ? listener.getMatchers() : null));
    }

    @Override
    public LoggingListener getLoggingListener() {
        synchronized (LOGGING_LISTENER_LOCK) {
            return loggingListener;
        }
    }

    private Breakpoint[] breakpoints;
    private final Object BREAKPOINT_LOCK = new Object();

    @Override
    public void addBreakpoint(Breakpoint breakpoint) {
        synchronized (BREAKPOINT_LOCK) {
            if(this.breakpoints == null) {
                this.breakpoints = new Breakpoint[] {breakpoint};
                comm.asyncSend((CommandMessage<?>) new CMS_addBreakpoint(breakpoint));
                return;
            }
            for(int i=0; i<breakpoints.length; i++) {
                if(breakpoints[i].getID() == breakpoint.getID()) {
                    breakpoints[i] = breakpoint;
                    comm.asyncSend((CommandMessage<?>) new CMS_modifyBreakpoint(breakpoint));
                    return;
                }
            }
            Breakpoint[] newBreakpoints = new Breakpoint[breakpoints.length + 1];
            System.arraycopy(breakpoints, 0, newBreakpoints, 0, breakpoints.length);
            newBreakpoints[breakpoints.length] = breakpoint;
            breakpoints = newBreakpoints;
        }
        comm.asyncSend((CommandMessage<?>) new CMS_addBreakpoint(breakpoint));
    }

    @Override
    public void modifyBreakpoint(Breakpoint breakpoint) {
        synchronized (BREAKPOINT_LOCK) {
            if (this.breakpoints == null) {
                addBreakpoint(breakpoint);
                return;
            }
            for (int i = 0; i < breakpoints.length; i++) {
                if (breakpoints[i].getID() == breakpoint.getID()) {
                    breakpoints[i] = breakpoint;
                    comm.asyncSend((CommandMessage<?>) new CMS_modifyBreakpoint(breakpoint));
                    return;
                }
            }
            addBreakpoint(breakpoint);
        }
    }

    @Override
    public void removeBreakpoint(Breakpoint breakpoint) {
        synchronized (BREAKPOINT_LOCK) {
            if(this.breakpoints == null) {
                return;
            }
            for(int i=0; i<breakpoints.length; i++) {
                if(breakpoints[i].getID() == breakpoint.getID()) {
                    if(breakpoints.length == 1) {
                        breakpoints = null;
                    } else {
                        Breakpoint[] newBreakpoints = new Breakpoint[breakpoints.length - 1];
                        System.arraycopy(breakpoints, 0, newBreakpoints, 0, i);
                        System.arraycopy(breakpoints, i + 1, newBreakpoints, i, newBreakpoints.length - i);
                        breakpoints = newBreakpoints;
                    }
                    comm.asyncSend((CommandMessage<?>) new CMS_removeBreakpoint(breakpoint));
                    break;
                }
            }
        }
    }

    @Override
    public Breakpoint[] getBreakpoints() {
        synchronized (BREAKPOINT_LOCK) {
            return breakpoints;
        }
    }

    private BreakpointHitHandler breakpointHitHandler;
    private final Object BREAKPOINT_HIT_HANDLER_LOCK = new Object();

    @Override
    public void setBreakpointHitHandler(BreakpointHitHandler breakpointHitHandler) {
        synchronized (BREAKPOINT_HIT_HANDLER_LOCK) {
            if(this.breakpointHitHandler == breakpointHitHandler) {
                return;
            }
            this.breakpointHitHandler = breakpointHitHandler;
        }
        comm.asyncSend((CommandMessage<?>) new CMS_setBreakpointHitHandlerActive(breakpointHitHandler != null));
    }

    @Override
    public BreakpointHitHandler getBreakpointHitHandler() {
        synchronized (BREAKPOINT_HIT_HANDLER_LOCK) {
            return breakpointHitHandler;
        }
    }

    private Boolean isEditionSupported;
    private final Object IS_EDITION_SUPPORTED_LOCK = new Object();

    @Override
    public boolean isExecutionSupported() {
        synchronized (IS_EDITION_SUPPORTED_LOCK) {
            if(isEditionSupported == null) {
                isEditionSupported = comm.syncSend(new CMS_isExecutionSupported());
            }
        }
        return Boolean.TRUE.equals(isEditionSupported);
    }

    @Override
    public QueryExecutor createQueryExecutor() {
        return new ClientStatementExecutor(this, null);
    }


    static class CMC_logQueries extends CommandMessage<Serializable> {
        private final QueryLog queryLog;

        CMC_logQueries(QueryLog queryLog) {
            this.queryLog = queryLog;
        }

        @Override
        public Serializable run(MessageContext context) {
            LoggingListener loggingListener = context.getDebugger().getLoggingListener();

            if (loggingListener != null) {
                loggingListener.logQuery(queryLog);
            }

            return null;
        }
    }

    static class CMC_logResultSet extends CommandMessage<Serializable> {
        private final ResultLog resultLog;

        CMC_logResultSet(ResultLog resultLog) {
            this.resultLog = resultLog;
        }

        @Override
        public Serializable run(MessageContext context) {
            LoggingListener loggingListener = context.getDebugger().getLoggingListener();

            if (loggingListener != null) {
                loggingListener.logResult(resultLog);
            }

            return null;
        }
    }

    static class CMC_processBreakpointBeforeExecutionHit extends CommandMessage<BreakpointHit> {
        private final BreakpointHit hit;

        CMC_processBreakpointBeforeExecutionHit(BreakpointHit hit) {
            this.hit = hit;
        }

        @Override
        public BreakpointHit run(MessageContext context) {
            BreakpointHitHandler breakpointHitHandler = context.getDebugger().getBreakpointHitHandler();

            if (breakpointHitHandler != null) {
                breakpointHitHandler.processBreakpointBeforeExecutionHit(hit);
                if (hit.getBreakpointID() != null) {
                    // The breakpoint was not processed, so we process it here.
                    hit.setExecutionType(BreakpointHit.ExecutionType.RUN, null);
                }
                return hit;
            }

            return null;
        }
    }

    static class CMC_processBreakpointAfterExecutionHit extends CommandMessage<BreakpointHit> {
        private final BreakpointHit hit;

        CMC_processBreakpointAfterExecutionHit(BreakpointHit hit) {
            this.hit = hit;
        }

        @Override
        public BreakpointHit run(MessageContext context) {
            BreakpointHitHandler breakpointHitHandler = context.getDebugger().getBreakpointHitHandler();

            if (breakpointHitHandler != null) {
                breakpointHitHandler.processBreakpointAfterExecutionHit(hit);
            }

            return null;
        }
    }

    private Map<Long, ExecuteContext> threadIDToExecuteContextMap = new HashMap<Long, ExecuteContext>();

    @Override
    public void processBreakpointBeforeExecutionHit(ExecuteContext ctx, BreakpointHit breakpointHit) {
        BreakpointHitHandler handler = getBreakpointHitHandler();
        if(handler == null) {
            return;
        }
        long threadID = breakpointHit.getThreadID();
        synchronized (threadIDToExecuteContextMap) {
            threadIDToExecuteContextMap.put(threadID, ctx);
        }
        try {
            handler.processBreakpointBeforeExecutionHit(breakpointHit);
        } finally {
            synchronized (threadIDToExecuteContextMap) {
                threadIDToExecuteContextMap.remove(threadID);
            }
            performThreadDataCleanup(threadID);
        }
    }

    @Override
    public void processBreakpointAfterExecutionHit(ExecuteContext ctx, BreakpointHit breakpointHit) {
        BreakpointHitHandler handler = getBreakpointHitHandler();
        if(handler == null) {
            return;
        }
        long threadID = breakpointHit.getThreadID();
        synchronized (threadIDToExecuteContextMap) {
            threadIDToExecuteContextMap.put(threadID, ctx);
        }
        try {
            handler.processBreakpointAfterExecutionHit(breakpointHit);
        } finally {
            synchronized (threadIDToExecuteContextMap) {
                threadIDToExecuteContextMap.remove(threadID);
            }
            performThreadDataCleanup(threadID);
        }
    }

    private Map<Long, List<QueryExecutor>> threadIDToStatementExecutorList = new HashMap<Long, List<QueryExecutor>>();

    @Override
    public QueryExecutor createBreakpointHitStatementExecutor(long threadID) {
        ClientStatementExecutor statementExecutor = new ClientStatementExecutor(this, threadID);
        synchronized (threadIDToStatementExecutorList) {
            List<QueryExecutor> list = threadIDToStatementExecutorList.get(threadID);
            if(list == null) {
                list = new ArrayList<QueryExecutor>();
                threadIDToStatementExecutorList.put(threadID, list);
            }
            list.add(statementExecutor);
        }
        return statementExecutor;
    }


    private void performThreadDataCleanup(long threadID) {
        List<QueryExecutor> list;
        synchronized (threadIDToStatementExecutorList) {
            list = threadIDToStatementExecutorList.remove(threadID);
        }
        if(list != null) {
            for(QueryExecutor queryExecutor: list) {
                queryExecutor.stopExecution();
            }
        }
    }

}
