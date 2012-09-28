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

import org.jooq.tools.debug.impl.DebuggerAPI;

/**
 * A debugger that hooks into the lifecycle of jOOQ-executed queries
 * <p>
 * A debugger comes in two flavours:
 * <ul>
 * <li> {@link DebuggerAPI#localDebugger()}: A local debugger is a debugger that
 * is executed locally, in the same JVM as the debugger client program. This is
 * useful for Swing applications, for instance</li>
 * <li> {@link DebuggerAPI#remoteDebugger(String, int)}: A remote debugger is a
 * debugger that connects to a remote JVM with jOOQ configured to accept remote
 * debugging sessions. This is useful for server applications</li>
 * </ul>
 * <p>
 * A debugger allows to create and register {@link Matcher} objects, which can
 * be used for debugging a local or remote jOOQ application.
 * 
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public interface Debugger extends DebuggerObject {

    /**
     * Attach a new matcher to this debugger
     */
    Matcher newMatcher();

    /**
     * Get a copy of the attached matchers for this debugger
     */
    Matcher[] matchers();

    /**
     * Get a reference of an executor given a configured name
     * 
     * @return A configured <code>Executor</code> or <code>null</code> if no
     *         executor exists by that name.
     */
    Executor executor(String name);

    /**
     * Get all reference of this debugger's executors.
     * 
     * @return All configured <code>Executors</code> or an empty array, if no
     *         executors were configured.
     */
    Executor[] executors();

}
