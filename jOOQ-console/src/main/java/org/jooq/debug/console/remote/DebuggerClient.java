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
public class DebuggerClient implements Debugger {

    private CommunicationInterface communicationInterface;

	public DebuggerClient(String ip, int port) throws Exception {
	    communicationInterface = CommunicationInterface.openClientCommunicationChannel(new CommunicationInterfaceFactory() {
            @Override
            public CommunicationInterface createCommunicationInterface(int port_) {
                return new DebuggerCommmunicationInterface(DebuggerClient.this, port_);
            }
        }, ip, port);
	}

    private LoggingListener loggingListener;

    @Override
    public void setLoggingListener(LoggingListener loggingListener) {
        this.loggingListener = loggingListener;
        new DebuggerServer.CMS_setLoggingActive().asyncExec(communicationInterface, loggingListener != null);
    }

    @Override
    public LoggingListener getLoggingListener() {
        return loggingListener;
    }

    private StatementMatcher[] loggingStatementMatchers;

    @Override
    public void setLoggingStatementMatchers(StatementMatcher[] loggingStatementMatchers) {
        this.loggingStatementMatchers = loggingStatementMatchers;
        new DebuggerServer.CMS_setLoggingStatementMatchers().asyncExec(communicationInterface, (Object)loggingStatementMatchers);
    }

    @Override
    public StatementMatcher[] getLoggingStatementMatchers() {
        return loggingStatementMatchers;
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
            return 1;
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

}
