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
package org.jooq.tools.debug.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jooq.ExecuteContext;
import org.jooq.tools.debug.Breakpoint;
import org.jooq.tools.debug.Logger;
import org.jooq.tools.debug.Matcher;
import org.jooq.tools.debug.Processor;

/**
 * A default implementation for the {@link Matcher} type
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
class MatcherImpl extends AbstractDebuggerObject implements Matcher {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 8299944904763983748L;

    private List<LoggerImpl>     loggers          = new ArrayList<LoggerImpl>();
    private List<ProcessorImpl>  processors       = new ArrayList<ProcessorImpl>();
    private List<BreakpointImpl> breakpoints      = new ArrayList<BreakpointImpl>();

    private Pattern              matchThreadName;
    private Pattern              matchSQL;
    private Set<Integer>         matchCount;
    private transient int        matchCounter;

    @Override
    public Logger newLogger() {
        return delegate(this, new LoggerImpl(), loggers);
    }

    @Override
    public synchronized Logger[] loggers() {
        return loggers.toArray(new Logger[loggers.size()]);
    }

    @Override
    public synchronized Processor newProcessor() {
        return delegate(this, new ProcessorImpl(), processors);
    }

    @Override
    public synchronized Processor[] processors() {
        return processors.toArray(new Processor[processors.size()]);
    }

    @Override
    public synchronized Breakpoint newBreakpoint() {
        return delegate(this, new BreakpointImpl(), breakpoints);
    }

    @Override
    public synchronized Breakpoint[] breakpoints() {
        return breakpoints.toArray(new Breakpoint[breakpoints.size()]);
    }

    @Override
    public void matchThreadName(String regex) {
        if (regex != null) {
            this.matchThreadName = Pattern.compile(regex);
        }
        else {
            this.matchThreadName = null;
        }

        apply();
    }

    @Override
    public void matchSQL(String regex) {
        if (regex != null) {
            this.matchSQL = Pattern.compile(regex);
        }
        else {
            this.matchSQL = null;
        }

        apply();
    }

    @Override
    public void matchCount(int... count) {
        if (count == null || count.length == 0) {
            matchCount = null;
        }
        else {
            matchCount = new HashSet<Integer>();

            for (int i : count) {
                matchCount.add(i);
            }
        }

        apply();
    }

    boolean matches(ExecuteContext ctx) {
        if (matchThreadName != null) {
            if (!matchThreadName.matcher(Thread.currentThread().getName()).matches()) {
                return false;
            }
        }

        if (matchSQL != null) {
            boolean matchFound = false;

            for (String sql : ctx.batchSQL()) {
                if (matchSQL.matcher(sql).matches()) {
                    matchFound = true;
                    break;
                }
            }

            if (!matchFound) {
                return false;
            }
        }

        if (matchCount != null) {
            synchronized (matchCount) {
                matchCounter += 1;

                if (!matchCount.contains(matchCounter)) {
                    return false;
                }
            }
        }

        return true;
    }
}
