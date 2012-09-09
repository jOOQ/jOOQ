/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                             Christopher Deckers, chrriis@gmail.com
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

import org.jooq.debug.Breakpoint;
import org.jooq.debug.BreakpointHit;
import org.jooq.debug.BreakpointHitHandler;
import org.jooq.debug.LocalDebugger;
import org.jooq.debug.LocalStatementExecutor;
import org.jooq.debug.LoggingListener;
import org.jooq.debug.ResultSetLog;
import org.jooq.debug.StatementExecution;
import org.jooq.debug.StatementExecutor;
import org.jooq.debug.StatementLog;
import org.jooq.debug.StatementMatcher;
import org.jooq.debug.console.DatabaseDescriptor;
import org.jooq.debug.console.remote.ClientDebugger.CMC_logQueries;
import org.jooq.debug.console.remote.ClientDebugger.CMC_logResultSet;
import org.jooq.debug.console.remote.ClientDebugger.CMC_processBreakpointAfterExecutionHit;
import org.jooq.debug.console.remote.ClientDebugger.CMC_processBreakpointBeforeExecutionHit;
import org.jooq.debug.console.remote.messaging.CommandMessage;
import org.jooq.debug.console.remote.messaging.CommunicationInterface;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
class ServerDebugger extends LocalDebugger {

    public ServerDebugger(DatabaseDescriptor databaseDescriptor) {
        super(databaseDescriptor);
    }

    private CommunicationInterface comm;

    void setCommunicationInterface(CommunicationInterface communicationInterface) {
        this.comm = communicationInterface;
    }

    private void setLoggingActive(boolean isActive) {
        if(isActive) {
            setLoggingListener(new LoggingListener() {
                @Override
                public void logQueries(StatementLog statementLog) {
                    comm.asyncSend((CommandMessage<?>) new CMC_logQueries(statementLog));
                }
                @Override
                public void logResultSet(int dataId, ResultSetLog resultSetLog) {
                    comm.asyncSend((CommandMessage<?>) new CMC_logResultSet(dataId, resultSetLog));
                }
            });
        } else {
            setLoggingListener(null);
        }
    }

    static class CMS_setLoggingActive extends ServerDebuggerCommandMessage<Serializable> {
        private final boolean isActive;

        CMS_setLoggingActive(boolean isActive) {
            this.isActive = isActive;
        }

        @Override
        public Serializable run() {
            getDebugger().setLoggingActive(isActive);
            return null;
        }
    }

    private void setBreakpointHitHandlerActive(boolean isActive) {
        if(isActive) {
            setBreakpointHitHandler(new BreakpointHitHandler() {
                @Override
                public void processBreakpointBeforeExecutionHit(BreakpointHit hit) {
                    BreakpointHit modifiedBreakpointHit = comm.syncSend(new CMC_processBreakpointBeforeExecutionHit(hit));
                    if(modifiedBreakpointHit != null) {
                        hit.setExecutionType(modifiedBreakpointHit.getExecutionType(), modifiedBreakpointHit.getSql());
                    }
                }
                @Override
                public void processBreakpointAfterExecutionHit(BreakpointHit hit) {
                    comm.syncSend(new CMC_processBreakpointAfterExecutionHit(hit));
                }
            });
        } else {
            setBreakpointHitHandler(null);
        }
    }

    static class CMS_setLoggingStatementMatchers extends ServerDebuggerCommandMessage<Serializable> {
        private final StatementMatcher[] matchers;

        CMS_setLoggingStatementMatchers(StatementMatcher[] matchers) {
            this.matchers = matchers;
        }

        @Override
        public Serializable run() {
            getDebugger().setLoggingStatementMatchers(matchers);
            return null;
        }
    }

    static class CMS_setBreakpoints extends ServerDebuggerCommandMessage<Serializable> {
        private final Breakpoint[] breakpoints;

        CMS_setBreakpoints(Breakpoint[] breakpoints) {
            this.breakpoints = breakpoints;
        }

        @Override
        public Serializable run() {
            if (breakpoints != null) {
                for (Breakpoint breakpoint : breakpoints) {
                    // Serialization has a cache, assuming objects are
                    // immutable. We have to reset our internal states.
                    breakpoint.reset();
                }
            }
            getDebugger().setBreakpoints(breakpoints);
            return null;
        }
    }

    static class CMS_addBreakpoint extends ServerDebuggerCommandMessage<Serializable> {
        private final Breakpoint breakpoint;

        CMS_addBreakpoint(Breakpoint breakpoint) {
            this.breakpoint = breakpoint;
        }

        @Override
        public Serializable run() {
            // Serialization has a cache, assuming objects are immutable. We
            // have to reset our internal states.
            breakpoint.reset();
            getDebugger().addBreakpoint(breakpoint);
            return null;
        }
    }

    static class CMS_modifyBreakpoint extends ServerDebuggerCommandMessage<Serializable> {
        private final Breakpoint breakpoint;

        CMS_modifyBreakpoint(Breakpoint breakpoint) {
            this.breakpoint = breakpoint;
        }

        @Override
        public Serializable run() {
            // Serialization has a cache, assuming objects are immutable. We
            // have to reset our internal states.
            breakpoint.reset();
            getDebugger().modifyBreakpoint(breakpoint);
            return null;
        }
    }

    static class CMS_removeBreakpoint extends ServerDebuggerCommandMessage<Serializable> {
        private final Breakpoint breakpoint;

        CMS_removeBreakpoint(Breakpoint breakpoint) {
            this.breakpoint = breakpoint;
        }

        @Override
        public Serializable run() {
            getDebugger().removeBreakpoint(breakpoint);
            return null;
        }
    }

    static class CMS_setBreakpointHitHandlerActive extends ServerDebuggerCommandMessage<Serializable> {
        private final boolean isActive;

        CMS_setBreakpointHitHandlerActive(boolean isActive) {
            this.isActive = isActive;
        }

        @Override
        public Serializable run() {
            getDebugger().setBreakpointHitHandlerActive(isActive);
            return null;
        }
    }

    static class CMS_isExecutionSupported extends ServerDebuggerCommandMessage<Boolean> {
        @Override
        public Boolean run() {
            return getDebugger().isExecutionSupported();
        }
    }

    private Map<Integer, StatementExecutor> idToStatementExecutorMap = new HashMap<Integer, StatementExecutor>();

    private void createStatementExecutor(int id, Long breakpointHitThreadID) {
        LocalStatementExecutor statementExecutor;
        if(breakpointHitThreadID == null) {
            statementExecutor = createStatementExecutor();
        } else {
            statementExecutor = createBreakpointHitStatementExecutor(breakpointHitThreadID);
        }
        synchronized (idToStatementExecutorMap) {
            idToStatementExecutorMap.put(id, statementExecutor);
        }
    }

    private StatementExecutor getStatementExecutor(int id) {
        synchronized (idToStatementExecutorMap) {
            return idToStatementExecutorMap.get(id);
        }
    }

    private StatementExecutor removeStatementExecutor(int id) {
        synchronized (idToStatementExecutorMap) {
            return idToStatementExecutorMap.remove(id);
        }
    }

    static class CMS_createServerStatementExecutor extends ServerDebuggerCommandMessage<Serializable> {
        private final int  id;
        private final Long breakpointHitThreadID;

        CMS_createServerStatementExecutor(int id, Long breakpointHitThreadID) {
            this.id = id;
            this.breakpointHitThreadID = breakpointHitThreadID;
        }

        @Override
        public Serializable run() {
            getDebugger().createStatementExecutor(id, breakpointHitThreadID);
            return null;
        }
    }

    static class CMS_doStatementExecutorExecution extends ServerDebuggerCommandMessage<StatementExecution> {
        private final int    id;
        private final String sql;
        private final int    maxRSRowsParsing;
        private final int    retainParsedRSDataRowCountThreshold;

        CMS_doStatementExecutorExecution(int id, String sql, int maxRSRowsParsing,
            int retainParsedRSDataRowCountThreshold) {
            this.id = id;
            this.sql = sql;
            this.maxRSRowsParsing = maxRSRowsParsing;
            this.retainParsedRSDataRowCountThreshold = retainParsedRSDataRowCountThreshold;
        }

        @Override
        public StatementExecution run() {
            StatementExecution statementExecution = getDebugger().getStatementExecutor(id).execute(sql, maxRSRowsParsing, retainParsedRSDataRowCountThreshold);
            return new ClientStatementExecution(statementExecution);
        }
    }

    static class CMS_stopStatementExecutorExecution extends ServerDebuggerCommandMessage<Serializable> {
        private final int id;

        CMS_stopStatementExecutorExecution(int id) {
            this.id = id;
        }

        @Override
        public Serializable run() {
            getDebugger().removeStatementExecutor(id).stopExecution();
            return null;
        }
    }

    static class CMS_getStatementExecutorTableNames extends ServerDebuggerCommandMessage<String[]> {
        private final int id;

        CMS_getStatementExecutorTableNames(int id) {
            this.id = id;
        }

        @Override
        public String[] run() {
            return getDebugger().getStatementExecutor(id).getTableNames();
        }
    }

    static class CMS_getStatementExecutorTableColumnNames extends ServerDebuggerCommandMessage<String[]> {
        private final int id;

        CMS_getStatementExecutorTableColumnNames(int id) {
            this.id = id;
        }

        @Override
        public String[] run() {
            return getDebugger().getStatementExecutor(id).getTableColumnNames();
        }
    }

    void cleanup() {
        synchronized (idToStatementExecutorMap) {
            for(StatementExecutor executor: idToStatementExecutorMap.values()) {
                executor.stopExecution();
            }
            idToStatementExecutorMap.clear();
        }
    }

    private Map<Long, List<StatementExecutor>> threadIDToStatementExecutorList = new HashMap<Long, List<StatementExecutor>>();

    @Override
    public LocalStatementExecutor createBreakpointHitStatementExecutor(long threadID) {
        LocalStatementExecutor statementExecutor = super.createBreakpointHitStatementExecutor(threadID);
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

    @Override
    protected void performThreadDataCleanup(long threadID) {
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