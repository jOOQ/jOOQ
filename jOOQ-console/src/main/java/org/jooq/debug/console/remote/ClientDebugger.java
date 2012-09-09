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
package org.jooq.debug.console.remote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.ExecuteContext;
import org.jooq.debug.Breakpoint;
import org.jooq.debug.BreakpointHit;
import org.jooq.debug.BreakpointHitHandler;
import org.jooq.debug.Debugger;
import org.jooq.debug.LoggingListener;
import org.jooq.debug.ResultSetLog;
import org.jooq.debug.StatementExecutor;
import org.jooq.debug.StatementLog;
import org.jooq.debug.StatementMatcher;
import org.jooq.debug.console.remote.ServerDebugger.CMS_addBreakpoint;
import org.jooq.debug.console.remote.ServerDebugger.CMS_isExecutionSupported;
import org.jooq.debug.console.remote.ServerDebugger.CMS_modifyBreakpoint;
import org.jooq.debug.console.remote.ServerDebugger.CMS_removeBreakpoint;
import org.jooq.debug.console.remote.ServerDebugger.CMS_setBreakpointHitHandlerActive;
import org.jooq.debug.console.remote.ServerDebugger.CMS_setBreakpoints;
import org.jooq.debug.console.remote.ServerDebugger.CMS_setLoggingActive;
import org.jooq.debug.console.remote.ServerDebugger.CMS_setLoggingStatementMatchers;
import org.jooq.debug.console.remote.messaging.CommandMessage;
import org.jooq.debug.console.remote.messaging.CommunicationInterface;
import org.jooq.debug.console.remote.messaging.CommunicationInterfaceFactory;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class ClientDebugger implements Debugger {

    private CommunicationInterface comm;

	public ClientDebugger(String ip, int port) throws Exception {
	    comm = CommunicationInterface.openClientCommunicationChannel(new CommunicationInterfaceFactory() {
            @Override
            public CommunicationInterface createCommunicationInterface(int port_) {
                return new DebuggerCommmunicationInterface(ClientDebugger.this, port_);
            }
        }, ip, port);
	}

    CommunicationInterface getCommunicationInterface() {
        return comm;
    }

    private LoggingListener loggingListener;
    private final Object LOGGING_LISTENER_LOCK = new Object();

    @Override
    public void setLoggingListener(LoggingListener loggingListener) {
        synchronized (LOGGING_LISTENER_LOCK) {
            if(this.loggingListener == loggingListener) {
                return;
            }
            this.loggingListener = loggingListener;
        }
        comm.asyncSend((CommandMessage<?>) new CMS_setLoggingActive(loggingListener != null));
    }

    @Override
    public LoggingListener getLoggingListener() {
        synchronized (LOGGING_LISTENER_LOCK) {
            return loggingListener;
        }
    }

    private StatementMatcher[] loggingStatementMatchers;
    private final Object LOGGING_STATEMENT_MATCHERS_LOCK = new Object();

    @Override
    public void setLoggingStatementMatchers(StatementMatcher[] matchers) {
        synchronized (LOGGING_STATEMENT_MATCHERS_LOCK) {
            this.loggingStatementMatchers = matchers;
        }
        comm.asyncSend((CommandMessage<?>) new CMS_setLoggingStatementMatchers(matchers));
    }

    @Override
    public StatementMatcher[] getLoggingStatementMatchers() {
        synchronized (LOGGING_STATEMENT_MATCHERS_LOCK) {
            return loggingStatementMatchers;
        }
    }

    private Breakpoint[] breakpoints;
    private final Object BREAKPOINT_LOCK = new Object();

    @Override
    public void setBreakpoints(Breakpoint[] breakpoints) {
        if(breakpoints != null && breakpoints.length == 0) {
            breakpoints = null;
        }
        synchronized (BREAKPOINT_LOCK) {
            this.breakpoints = breakpoints;
        }
        comm.asyncSend((CommandMessage<?>) new CMS_setBreakpoints(breakpoints));
    }

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
    public StatementExecutor createStatementExecutor() {
        return new ClientStatementExecutor(this, null);
    }


    static class CMC_logQueries extends ClientDebuggerCommandMessage<Serializable> {
        private final StatementLog statementLog;

        CMC_logQueries(StatementLog statementLog) {
            this.statementLog = statementLog;
        }

        @Override
        public Serializable run() {
            LoggingListener loggingListener = getDebugger().getLoggingListener();

            if (loggingListener != null) {
                loggingListener.logQueries(statementLog);
            }

            return null;
        }
    }

    static class CMC_logResultSet extends ClientDebuggerCommandMessage<Serializable> {
        private final int          dataId;
        private final ResultSetLog resultSetLog;

        CMC_logResultSet(int dataId, ResultSetLog resultSetLog) {
            this.dataId = dataId;
            this.resultSetLog = resultSetLog;
        }

        @Override
        public Serializable run() {
            LoggingListener loggingListener = getDebugger().getLoggingListener();

            if (loggingListener != null) {
                loggingListener.logResultSet(dataId, resultSetLog);
            }

            return null;
        }
    }

    static class CMC_processBreakpointBeforeExecutionHit extends ClientDebuggerCommandMessage<BreakpointHit> {
        private final BreakpointHit hit;

        CMC_processBreakpointBeforeExecutionHit(BreakpointHit hit) {
            this.hit = hit;
        }

        @Override
        public BreakpointHit run() {
            BreakpointHitHandler breakpointHitHandler = getDebugger().getBreakpointHitHandler();

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

    static class CMC_processBreakpointAfterExecutionHit extends ClientDebuggerCommandMessage<BreakpointHit> {
        private final BreakpointHit hit;

        CMC_processBreakpointAfterExecutionHit(BreakpointHit hit) {
            this.hit = hit;
        }

        @Override
        public BreakpointHit run() {
            BreakpointHitHandler breakpointHitHandler = getDebugger().getBreakpointHitHandler();

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

    private Map<Long, List<StatementExecutor>> threadIDToStatementExecutorList = new HashMap<Long, List<StatementExecutor>>();

    @Override
    public StatementExecutor createBreakpointHitStatementExecutor(long threadID) {
        ClientStatementExecutor statementExecutor = new ClientStatementExecutor(this, threadID);
        synchronized (threadIDToStatementExecutorList) {
            List<StatementExecutor> list = threadIDToStatementExecutorList.get(threadID);
            if(list == null) {
                list = new ArrayList<StatementExecutor>();
                threadIDToStatementExecutorList.put(threadID, list);
            }
            list.add(statementExecutor);
        }
        return statementExecutor;
    }


    private void performThreadDataCleanup(long threadID) {
        List<StatementExecutor> list;
        synchronized (threadIDToStatementExecutorList) {
            list = threadIDToStatementExecutorList.remove(threadID);
        }
        if(list != null) {
            for(StatementExecutor statementExecutor: list) {
                statementExecutor.stopExecution();
            }
        }
    }

}
