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

import org.jooq.ExecuteContext;
import org.jooq.Query;
import org.jooq.Result;
import org.jooq.debug.impl.DebuggerFactory;

/**
 * The jOOQ debugger API
 * <p>
 * This is the main API for hooking into jOOQ's debugging capabilities locally
 * or remotely. In order to create a <code>Debugger</code> instance, use
 * {@link DebuggerFactory#localDebugger(DatabaseDescriptor)} or
 * {@link DebuggerFactory#remoteDebugger(String, int)}
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public interface Debugger extends QueryExecutorCreator {

    /**
     * Set a logging listener to the <code>Debugger</code>
     * <p>
     * Logging listeners log {@link Query} and {@link Result} objects.
     *
     * @param listener a listener, or null to stop logging.
     */
    void setLoggingListener(LoggingListener listener);

    /**
     * Get the <code>Debugger</code>'s configured logging listeners
     */
    LoggingListener getLoggingListener();

    /**
     * Add (or modify) a breakpoint to the debugger
     * <p>
     * This method adds a breakpoint to the debugger, or modifies the breakpoint
     * if it already exists.
     */
    void addBreakpoint(Breakpoint breakpoint);

    void removeBreakpoint(Breakpoint breakpoint);

    Breakpoint[] getBreakpoints();

    void setBreakpointHitHandler(BreakpointHitHandler handler);

    BreakpointHitHandler getBreakpointHitHandler();

    String[] getExecutionContextNames();

    void processBreakpointBeforeExecutionHit(ExecuteContext ctx, BreakpointHit hit);

    void processBreakpointAfterExecutionHit(ExecuteContext ctx, BreakpointHit hit);

    QueryExecutor createBreakpointHitStatementExecutor(long threadID);

    void close();

}
