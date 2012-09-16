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
import java.util.Collections;
import java.util.List;

import org.jooq.tools.debug.Debugger;
import org.jooq.tools.debug.Matcher;

/**
 * A local {@link Debugger} implementation
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
class LocalDebugger extends AbstractDebuggerObject implements Debugger {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7828985946284691522L;

    public LocalDebugger() {
        DebuggerRegistry.add(this);
    }

    @Override
    void apply() {}

    @Override
    public void remove() {
        DebuggerRegistry.remove(this);
    }

    private List<Matcher> matchers     = new ArrayList<Matcher>();
    private final Object  MATCHER_LOCK = new Object();

    @Override
    public Matcher newMatcher() {
        Matcher result = new MatcherImpl();
        setMatcher(result);
        return result;
    }

    void setMatcher(Matcher matcher) {
        synchronized (MATCHER_LOCK) {
            int size = matchers.size();

            for (int i = 0; i < size; i++) {
                if (matchers.get(i).equals(matcher)) {
                    matchers.set(i, matcher);
                    return;
                }
            }

            matchers.add(matcher);
        }
    }

    void removeMatcher(Matcher matcher) {
        synchronized (MATCHER_LOCK) {
            matchers.remove(matcher);
        }
    }

    @Override
    public Matcher[] matchers() {
        synchronized (MATCHER_LOCK) {
            return matchers.toArray(new Matcher[matchers.size()]);
        }
    }

    /**
     * A registry for local debuggers
     */
    static class DebuggerRegistry {

        /**
         * A list of registered debuggers
         */
        private static final List<Debugger> debuggers = new ArrayList<Debugger>();

        /**
         * Add a new debugger to the registry
         */
        private static synchronized void add(Debugger debugger) {
            debuggers.add(debugger);
        }

        /**
         * Remove a debugger from the registry
         */
        private static synchronized void remove(Debugger debugger) {
            debuggers.remove(debugger);
        }

        /**
         * Return an immutable list of all the debuggers currently registered.
         */
        static synchronized List<Debugger> get() {

            // No cost when no loggers
            if (debuggers.isEmpty()) {
                return Collections.emptyList();
            }

            // Small cost: copy collection and make it immutable.
            // Generally, no more than one or two listeners in the list.
            return Collections.unmodifiableList(new ArrayList<Debugger>(debuggers));
        }

        /**
         * No instances
         */
        private DebuggerRegistry() {}
    }
}
