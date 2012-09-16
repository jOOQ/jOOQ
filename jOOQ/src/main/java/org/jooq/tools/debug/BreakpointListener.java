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
package org.jooq.tools.debug;

/**
 * A debugger <code>BreakpointListener</code> is used to react to breakpoint hit
 * events.
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public interface BreakpointListener {

    /**
     * A callback method for breakpoint hit events that are triggered before
     * query execution.
     * <p>
     * When a breakpoint generates a hit event before query execution, this
     * method is called by the jOOQ debugger. Debugger client code may suspend
     * query execution indefinitely by waiting within this method, e.g. for UI
     * interaction. The {@link HitContext} argument can be used to execute test
     * or patch queries in the context of the breakpoint's transaction. The
     * method result is used to indicate to the debugger how to proceed with the
     * query's execution.
     * <p>
     * Note that throwing an exception within this method has similar effects as
     * returning {@link Step#FAIL}
     *
     * @param context The context of the breakpoint hit
     * @return The result produced by this breakpoint hit
     * @throws Exception Any exception that should be thrown by the debugger
     */
    Step before(HitContext context) throws Exception;

    /**
     * A callback method for breakpoint hit events that are triggered after
     * query execution.
     * <p>
     * When a breakpoint generates a hit event after query execution, this
     * method is called by the jOOQ debugger. Debugger client code may suspend
     * query execution indefinitely by waiting within this method, e.g. for UI
     * interaction. The {@link HitContext} argument can be used to execute test
     * or patch queries in the context of the breakpoint's transaction. The
     * method result is used to indicate to the debugger how to proceed with the
     * query's execution.
     * <p>
     * Note that throwing an exception within this method has similar effects as
     * returning {@link Step#FAIL}
     * <p>
     * Note that this event is not generated, if the previous event handled by
     * {@link #before(HitContext)} returns {@link Step#SKIP} or
     * {@link Step#FAIL}
     *
     * @param context The context of the breakpoint hit
     * @return The result produced by this breakpoint hit
     * @throws Exception Any exception that should be thrown by the debugger
     */
    Step after(HitContext context) throws Exception;
}
