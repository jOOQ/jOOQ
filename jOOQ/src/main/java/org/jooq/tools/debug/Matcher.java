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

import org.jooq.Batch;

/**
 * A matcher object that is used for matching queries.
 * <p>
 * Queries can be matched by
 * <ul>
 * <li>Executing thread name: {@link #matchThreadName(String)}</li>
 * <li>Regular expressions matching the SQL string: {@link #matchSQL(String)}</li>
 * </ul>
 * <p>
 * On match, loggers, processors and breakpoints can be activated
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public interface Matcher extends DebuggerObject {

    /**
     * Attach a new logger to this matcher
     */
    Logger newLogger();

    /**
     * Get a copy of the attached loggers for this matcher
     */
    Logger[] loggers();

    /**
     * Attach a new processor to this matcher
     */
    Processor newProcessor();

    /**
     * Get a copy of the attached processors for this matcher
     */
    Processor[] processors();

    /**
     * Attach a new breakpoint to this matcher
     */
    Breakpoint newBreakpoint();

    /**
     * Get a copy of the attached breakpoints for this matcher
     */
    Breakpoint[] breakpoints();

    /**
     * Define a regular expression used for matching thread names
     * <p>
     * Matching is performed as such: <code><pre>
     * Thread.currentThread().getName().matches(regex)
     * </pre></code>
     *
     * @param regex A regular expression
     */
    void matchThreadName(String regex);

    /**
     * Define a regular expression used for matching SQL strings
     * <p>
     * Matching is performed as such: <code><pre>
     * sql.matches(regex)
     * </pre></code>
     * <p>
     * In case a {@link Batch} query is being executed, this matcher will match
     * the batch statement as soon as one of the queries matches.
     *
     * @param regex A regular expression
     */
    void matchSQL(String regex);
}
