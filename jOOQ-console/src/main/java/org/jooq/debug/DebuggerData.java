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
package org.jooq.debug;

import java.io.Serializable;


/**
 * @author Christopher Deckers
 */
public class DebuggerData implements Serializable {

    private static volatile int nextID;

    private int id;
    private SqlQueryType queryType;
    private String[] queries;
    private String parameterDescription;
    private Long preparationDuration;
    private Long bindingDuration;
    private long executionDuration;
    private String threadName;
    private long threadID;
    private StackTraceElement[] callerStackTraceElements;

    public DebuggerData(SqlQueryType queryType, String[] queries, String parameterDescription, Long preparationDuration, Long bindingDuration, long executionDuration) {
        this.id = nextID++;
        Thread currentThread = Thread.currentThread();
        this.threadName = currentThread.getName();
        this.threadID = currentThread.getId();
        this.callerStackTraceElements = new Exception().getStackTrace();
        this.queryType = queryType;
        this.queries = queries;
        this.parameterDescription = parameterDescription;
        this.preparationDuration = preparationDuration;
        this.bindingDuration = bindingDuration;
        this.executionDuration = executionDuration;
    }

    public int getID() {
        return id;
    }

    public String getThreadName() {
        return threadName;
    }

    public long getThreadID() {
        return threadID;
    }

    public StackTraceElement[] getCallerStackTraceElements() {
        return callerStackTraceElements;
    }

    public SqlQueryType getQueryType() {
        return queryType;
    }

    public String[] getQueries() {
        return queries;
    }

    /**
     * @return non null if queries consist of a single prepared statement with parameters.
     */
    public String getParameterDescription() {
        return parameterDescription;
    }

    public Long getPreparedStatementPreparationDuration() {
        return preparationDuration;
    }

    public Long getPreparedStatementBindingDuration() {
        return bindingDuration;
    }

    public long getExecutionDuration() {
        return executionDuration;
    }

}
