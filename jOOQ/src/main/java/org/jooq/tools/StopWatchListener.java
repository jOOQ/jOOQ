/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.tools;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;

/**
 * A default {@link ExecuteListener} that just logs events to java.util.logging,
 * log4j, or slf4j using the {@link JooqLogger}
 *
 * @author Lukas Eder
 */
public class StopWatchListener implements ExecuteListener {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7399239846062763212L;

    private final StopWatch watch = new StopWatch();

    @Override
    public void start(ExecuteContext ctx) {
        watch.splitTrace("Initialising");
    }

    @Override
    public void renderStart(ExecuteContext ctx) {
        watch.splitTrace("Rendering query");
    }

    @Override
    public void renderEnd(ExecuteContext ctx) {
        watch.splitTrace("Query rendered");
    }

    @Override
    public void prepareStart(ExecuteContext ctx) {
        watch.splitTrace("Preparing statement");
    }

    @Override
    public void prepareEnd(ExecuteContext ctx) {
        watch.splitTrace("Statement prepared");
    }

    @Override
    public void bindStart(ExecuteContext ctx) {
        watch.splitTrace("Binding variables");
    }

    @Override
    public void bindEnd(ExecuteContext ctx) {
        watch.splitTrace("Variables bound");
    }

    @Override
    public void executeStart(ExecuteContext ctx) {
        watch.splitTrace("Executing query");
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        watch.splitDebug("Query executed");
    }

    @Override
    public void fetchStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching results");
    }

    @Override
    public void resultStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching result");
    }

    @Override
    public void recordStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching record");
    }

    @Override
    public void recordEnd(ExecuteContext ctx) {
        watch.splitTrace("Record fetched");
    }

    @Override
    public void resultEnd(ExecuteContext ctx) {
        watch.splitTrace("Result fetched");
    }

    @Override
    public void fetchEnd(ExecuteContext ctx) {
        watch.splitTrace("Results fetched");
    }

    @Override
    public void end(ExecuteContext ctx) {
        watch.splitDebug("Finishing");
    }

    @Override
    public void exception(ExecuteContext ctx) {
        watch.splitDebug("Exception");
    }
}
