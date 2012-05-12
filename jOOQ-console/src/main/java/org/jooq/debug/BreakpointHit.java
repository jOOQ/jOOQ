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
package org.jooq.debug;

import java.io.Serializable;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class BreakpointHit implements Serializable {

    public static enum ExecutionType {
        STEP_THROUGH,
        RUN_OVER,
        RUN,
    }

    private boolean isBeforeExecution;
    private Integer breakpointID;
    private String sql;
    private long threadID;
    private String threadName;
    private StackTraceElement[] callerStackTraceElements;

    public BreakpointHit(int breakpointID, String sql, long threadID, String threadName, StackTraceElement[] callerStackTraceElements, boolean isBeforeExecution) {
        this.isBeforeExecution = isBeforeExecution;
        this.breakpointID = breakpointID;
        this.sql = sql;
        this.threadID = threadID;
        this.threadName = threadName;
        this.callerStackTraceElements = callerStackTraceElements;
    }

    public boolean isBeforeExecution() {
        return isBeforeExecution;
    }

    /**
     * @return null if the breakpoint was processed and contains an execution type.
     */
    public Integer getBreakpointID() {
        return breakpointID;
    }

    public String getSql() {
        return sql;
    }

    public long getThreadID() {
        return threadID;
    }

    public String getThreadName() {
        return threadName;
    }

    public StackTraceElement[] getCallerStackTraceElements() {
        return callerStackTraceElements;
    }

    private ExecutionType executionType = ExecutionType.RUN;

    public void setExecutionType(ExecutionType executionType, String sql) {
        this.breakpointID = null;
        this.executionType = executionType;
        this.sql = sql;
    }

    public ExecutionType getExecutionType() {
        return executionType;
    }

}
