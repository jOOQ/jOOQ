/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
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

import org.jooq.Result;
import org.jooq.tools.StopWatch;

/**
 * A debugger log object holding information about the fetched {@link Result}
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public final class ResultLog implements Serializable {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 6968225986331050805L;

    private static AtomicInteger NEXT_ID          = new AtomicInteger();

    private final int            id;
    private final int            queryLogId;

    private final long           fetchTime;
    private final int            size;

    public ResultLog(int queryLogId, long fetchTime, final int size) {
        this.queryLogId = queryLogId;
        this.id = NEXT_ID.getAndIncrement();

        this.fetchTime = fetchTime;
        this.size = size;
    }

    public final int getId() {
        return id;
    }

    public final int getQueryLogId() {
        return queryLogId;
    }

    public final long getFetchTime() {
        return fetchTime;
    }

    public final int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ResultLog[fetch=");
        sb.append(StopWatch.format(fetchTime));
        sb.append(", size=");
        sb.append(size);
        sb.append("]");

        return sb.toString();
    }
}
