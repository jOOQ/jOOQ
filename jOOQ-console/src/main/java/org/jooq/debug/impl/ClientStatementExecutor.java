/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.debug.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.jooq.debug.QueryExecution;
import org.jooq.debug.QueryExecutor;
import org.jooq.debug.impl.ServerDebugger.CMS_createServerStatementExecutor;
import org.jooq.debug.impl.ServerDebugger.CMS_doStatementExecutorExecution;
import org.jooq.debug.impl.ServerDebugger.CMS_getStatementExecutorTableColumnNames;
import org.jooq.debug.impl.ServerDebugger.CMS_getStatementExecutorTableNames;
import org.jooq.debug.impl.ServerDebugger.CMS_stopStatementExecutorExecution;

/**
 * @author Christopher Deckers
 */
class ClientStatementExecutor implements QueryExecutor {

    private static AtomicInteger nextID = new AtomicInteger();

    private ClientDebugger debugger;
    private int id;

    /**
     * @param executionContextName ignored if in a breakpoint hit.
     * @param breakpointHitThreadID null if not in a breakpoint hit.
     */
    public ClientStatementExecutor(ClientDebugger debugger, String executionContextName, Long breakpointHitThreadID) {
        id = nextID.incrementAndGet();
        this.debugger = debugger;
        debugger.getCommunicationInterface().asyncSend((CommandMessage<?>) new CMS_createServerStatementExecutor(id, executionContextName, breakpointHitThreadID));
    }

    @Override
    public QueryExecution execute(String sql, int maxRSRowsParsing, int retainParsedRSDataRowCountThreshold, boolean isUpdatable) {
        return debugger.getCommunicationInterface().syncSend(new CMS_doStatementExecutorExecution(id, sql, maxRSRowsParsing, retainParsedRSDataRowCountThreshold, isUpdatable));
    }

    @Override
    public void stopExecution() {
        debugger.getCommunicationInterface().asyncSend((CommandMessage<?>) new CMS_stopStatementExecutorExecution(id));
    }

    @Override
    public String[] getTableNames() {
        String[] tableNames = debugger.getCommunicationInterface().syncSend(new CMS_getStatementExecutorTableNames(id));
        return tableNames == null? new String[0]: tableNames;
    }

    @Override
    public String[] getTableColumnNames() {
        String[] tableColumnNames = debugger.getCommunicationInterface().syncSend(new CMS_getStatementExecutorTableColumnNames(id));
        return tableColumnNames == null? new String[0]: tableColumnNames;
    }

}
