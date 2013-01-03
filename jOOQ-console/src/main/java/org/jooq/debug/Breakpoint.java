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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A breakpoint that can be set onto a {@link Debugger}
 * <p>
 * Breakpoints can be set onto a {@link Debugger} in order to make the debugger
 * break on certain events
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
public class Breakpoint implements Serializable {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = 7419556364234031622L;

    private final UUID              id;
    private final QueryMatcher      matcher;
    private final Integer           hitCount;
    private final boolean           isBreaking;
    private final QueryProcessor    before;
    private final QueryProcessor    replace;
    private final QueryProcessor    after;

    private transient AtomicInteger currentHitCount;

    /**
     * Create a new, empty breakpoint
     */
    public Breakpoint() {
        this(null, null, null, true, null, null, null);
    }

    /**
     * Create an initialised breakpoint
     */
    public Breakpoint(UUID id, Integer hitCount, QueryMatcher matcher, boolean isBreaking, QueryProcessor before,
        QueryProcessor replace, QueryProcessor after) {

        if (id == null) {
            this.id = UUID.randomUUID();
        }
        else {
            this.id = id;
        }

        this.hitCount = hitCount;

        if (hitCount != null) {
            currentHitCount = new AtomicInteger(hitCount);
        }

        this.matcher = matcher;
        this.isBreaking = isBreaking;
        this.before = before;
        this.replace = replace;
        this.after = after;
    }

    /**
     * The breakpoints unique ID, which is used to identify it across remote
     * systems.
     */
    public UUID getID() {
        return id;
    }

    /**
     * A matcher used to match queries on which to break
     */
    public QueryMatcher getMatcher() {
        return matcher;
    }

    public boolean matches(QueryInfo queryInfo) {

        // No need to match if hit count was already reached.
        if (hitCount != null && currentHitCount.get() <= 0) {
            return false;
        }

        boolean hasMatcher = false;
        if (matcher != null) {
            if (!matcher.matches(queryInfo)) {
                return false;
            }
            hasMatcher = true;
        }

        if (hitCount != null) {
            int currentHitCount_ = currentHitCount.decrementAndGet();
            if (currentHitCount_ > 0) {
                return false;
            }
            if (currentHitCount_ < 0) {
                currentHitCount.set(0);
                return false;
            }
            hasMatcher = true;
        }

        return hasMatcher;
    }

    /**
     * Reset internal state like hit count tracking.
     *
     * @deprecated - That feels like a workaround... Maybe there's a better way
     *             to do this?
     */
    @Deprecated
    public void reset() {
        if (hitCount != null) {
            currentHitCount = new AtomicInteger(hitCount);
        }
    }

    /**
     * The breakpoint's hit count
     */
    public Integer getHitCount() {
        return hitCount;
    }

    /**
     * Whether the breakpoint is breaking
     */
    public boolean isBreaking() {
        return isBreaking;
    }

    public QueryProcessor getBeforeExecutionProcessor() {
        return before;
    }

    public QueryProcessor getReplacementExecutionProcessor() {
        return replace;
    }

    public QueryProcessor getAfterExecutionProcessor() {
        return after;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Breakpoint) {
            return id.equals(((Breakpoint) obj).id);
        }

        return false;
    }
}
