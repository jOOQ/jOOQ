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
package org.jooq.debug;

import java.io.Serializable;
import java.util.UUID;

import org.jooq.Query;

/**
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public class BreakpointHit implements Serializable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -9202720101880051991L;

    private final boolean             isBeforeExecution;
    private UUID                      breakpointID;
    private String                    sql;
    private final String              parameterDescription;
    private final long                threadID;
    private final String              threadName;
    private final StackTraceElement[] stackTrace;

    private ExecutionType             executionType    = ExecutionType.RUN;

    public BreakpointHit(UUID breakpointID, String sql, String parameterDescription, long threadID, String threadName,
        StackTraceElement[] callerStackTraceElements, boolean isBeforeExecution) {
        this.isBeforeExecution = isBeforeExecution;
        this.breakpointID = breakpointID;
        this.sql = sql;
        this.parameterDescription = parameterDescription;
        this.threadID = threadID;
        this.threadName = threadName;
        this.stackTrace = callerStackTraceElements;
    }

    public boolean isBeforeExecution() {
        return isBeforeExecution;
    }

    /**
     * @return null if the breakpoint was processed and contains an execution
     *         type.
     */
    public UUID getBreakpointID() {
        return breakpointID;
    }

    /**
     * The SQL string of the {@link Query} that was suspended by this breakpoint hit
     */
    public String getSQL() {
        return sql;
    }

    /**
     * The bind values of the {@link Query} that was suspended by this breakpoint hit
     */
    public String getParameterDescription() {
        return parameterDescription;
    }

    /**
     * The ID of the thread which was suspended by this breakpoint hit
     */
    public long getThreadID() {
        return threadID;
    }

    /**
     * The name of the thread which was suspended by this breakpoint hit
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * The stacktrace of the thread which was suspended by this breakpoint hit
     */
    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    /**
     * @deprecated - {@link BreakpointHit} should be immutable. This subtle
     *             state-change has too much implicit semantics.
     */
    @Deprecated
    public void setExecutionType(ExecutionType executionType, String sql) {
        this.breakpointID = null;
        this.executionType = executionType;
        this.sql = sql;
    }

    public ExecutionType getExecutionType() {
        return executionType;
    }

}
