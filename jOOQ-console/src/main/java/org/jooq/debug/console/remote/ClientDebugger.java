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

import org.jooq.debug.Breakpoint;
import org.jooq.debug.BreakpointAfterExecutionHit;
import org.jooq.debug.BreakpointBeforeExecutionHit;
import org.jooq.debug.BreakpointHitHandler;
import org.jooq.debug.Debugger;
import org.jooq.debug.LoggingListener;
import org.jooq.debug.QueryLoggingData;
import org.jooq.debug.ResultSetLoggingData;
import org.jooq.debug.StatementExecutor;
import org.jooq.debug.StatementMatcher;
import org.jooq.debug.console.remote.messaging.CommunicationInterface;
import org.jooq.debug.console.remote.messaging.CommunicationInterfaceFactory;

/**
 * @author Christopher Deckers
 */
public class ClientDebugger implements Debugger {

    private CommunicationInterface communicationInterface;

	public ClientDebugger(String ip, int port) throws Exception {
	    communicationInterface = CommunicationInterface.openClientCommunicationChannel(new CommunicationInterfaceFactory() {
            @Override
            public CommunicationInterface createCommunicationInterface(int port_) {
                return new DebuggerCommmunicationInterface(ClientDebugger.this, port_);
            }
        }, ip, port);
	}

    private LoggingListener loggingListener;
    private final Object LOGGING_LISTENER_LOCK = new Object();

    @Override
    public void setLoggingListener(LoggingListener loggingListener) {
        synchronized (LOGGING_LISTENER_LOCK) {
            this.loggingListener = loggingListener;
        }
        new ServerDebugger.CMS_setLoggingActive().asyncExec(communicationInterface, loggingListener != null);
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
    public void setLoggingStatementMatchers(StatementMatcher[] loggingStatementMatchers) {
        synchronized (LOGGING_STATEMENT_MATCHERS_LOCK) {
            this.loggingStatementMatchers = loggingStatementMatchers;
        }
        new ServerDebugger.CMS_setLoggingStatementMatchers().asyncExec(communicationInterface, (Object)loggingStatementMatchers);
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
        new ServerDebugger.CMS_setBreakpoints().asyncExec(communicationInterface, (Object)breakpoints);
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
            this.breakpointHitHandler = breakpointHitHandler;
        }
        new ServerDebugger.CMS_setBreakpointHitHandlerActive().asyncExec(communicationInterface, breakpointHitHandler != null);
    }

    @Override
    public BreakpointHitHandler getBreakpointHitHandler() {
        synchronized (BREAKPOINT_HIT_HANDLER_LOCK) {
            return breakpointHitHandler;
        }
    }

    @Override
    public boolean isExecutionSupported() {
        // TODO: implement
        return false;
    }

    @Override
    public StatementExecutor createStatementExecutor() {
        // TODO: implement
        return null;
    }

    @SuppressWarnings("serial")
    static class CMC_logQueries extends ClientDebuggerCommandMessage {
        @Override
        public Object run(Object[] args) {
            LoggingListener loggingListener = getDebugger().getLoggingListener();
            if(loggingListener != null) {
                loggingListener.logQueries((QueryLoggingData)args[0]);
            }
            return null;
        }
    }

    @SuppressWarnings("serial")
    static class CMC_logResultSet extends ClientDebuggerCommandMessage {
        @Override
        public Object run(Object[] args) {
            LoggingListener loggingListener = getDebugger().getLoggingListener();
            if(loggingListener != null) {
                loggingListener.logResultSet((Integer)args[0], (ResultSetLoggingData)args[1]);
            }
            return null;
        }
    }

    @SuppressWarnings("serial")
    static class CMC_processBreakpointBeforeExecutionHit extends ClientDebuggerCommandMessage {
        @Override
        public Object run(Object[] args) {
            BreakpointHitHandler breakpointHitHandler = getDebugger().getBreakpointHitHandler();
            if(breakpointHitHandler != null) {
                BreakpointBeforeExecutionHit breakpointHit = (BreakpointBeforeExecutionHit)args[0];
                breakpointHitHandler.processBreakpointBeforeExecutionHit(breakpointHit);
                if(breakpointHit.getBreakpointID() != null) {
                    // The breakpoint was not processed, so we process it here.
                    breakpointHit.setExecutionType(BreakpointBeforeExecutionHit.ExecutionType.RUN, null);
                }
                return breakpointHit;
            }
            return null;
        }
    }

    @SuppressWarnings("serial")
    static class CMC_processBreakpointAfterExecutionHit extends ClientDebuggerCommandMessage {
        @Override
        public Object run(Object[] args) {
            BreakpointHitHandler breakpointHitHandler = getDebugger().getBreakpointHitHandler();
            if(breakpointHitHandler != null) {
                BreakpointAfterExecutionHit breakpointHit = (BreakpointAfterExecutionHit)args[0];
                breakpointHitHandler.processBreakpointAfterExecutionHit(breakpointHit);
            }
            return null;
        }
    }

}
