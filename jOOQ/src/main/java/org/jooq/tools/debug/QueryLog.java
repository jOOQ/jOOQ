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

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import org.jooq.Query;
import org.jooq.tools.StopWatch;

/**
 * A debugger log object holding information about the executed {@link Query}
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public final class QueryLog implements Serializable {

    /**
     * Generated UID
     */
    private static final long         serialVersionUID = 4788898342470338246L;

    private static AtomicInteger      NEXT_ID          = new AtomicInteger();

    private final int                 id;
    private final StackTraceElement[] stackTrace;

    private final Query               query;
    private final long                prepareTime;
    private final long                bindTime;
    private final long                executeTime;

    public QueryLog(Query query, long prepareTime, long bindTime, long executeTime) {
        this.id = NEXT_ID.getAndIncrement();
        this.stackTrace = Thread.currentThread().getStackTrace();

        this.query = query;
        this.prepareTime = prepareTime;
        this.bindTime = bindTime;
        this.executeTime = executeTime;
    }

    public final Query getQuery() {
        return query;
    }

    public final int getId() {
        return id;
    }

    public final StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public final long getPrepareTime() {
        return prepareTime;
    }

    public final long getBindTime() {
        return bindTime;
    }

    public final long getExecuteTime() {
        return executeTime;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("QueryLog[prepare=");
        sb.append(StopWatch.format(prepareTime));
        sb.append(", bind=");
        sb.append(StopWatch.format(bindTime));
        sb.append(", execute=");
        sb.append(StopWatch.format(executeTime));
        sb.append(", query=[");
        sb.append(query);
        sb.append("]]");

        return sb.toString();
    }
}
