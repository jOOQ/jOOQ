/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                             Christopher Deckers, chrriis@gmail.com
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
import java.util.EnumSet;
import java.util.Set;

import org.jooq.debug.console.misc.TextMatcher;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class StatementMatcher implements Serializable {

    private boolean isActive;
    private TextMatcher threadNameTextMatcher;
    private TextMatcher statementTextMatcher;
    private Set<QueryType> queryTypeSet;

    /**
     * @param threadNameTextMatcher a text matcher for thread name or null for no text matching.
     * @param statementTextMatcher a text matcher for statement or null for no text matching.
     * @param queryTypeSet some types or null for all types.
     */
    public StatementMatcher(TextMatcher threadNameTextMatcher, TextMatcher statementTextMatcher, Set<QueryType> queryTypeSet, boolean isActive) {
        this.threadNameTextMatcher = threadNameTextMatcher;
        this.statementTextMatcher = statementTextMatcher;
        this.queryTypeSet = queryTypeSet == null? null: EnumSet.copyOf(queryTypeSet);
        this.isActive = isActive;
    }

    public boolean matches(StatementInfo statementInfo) {
        if(!isActive) {
            return false;
        }
        boolean hasMatcher = false;
        if(threadNameTextMatcher != null) {
            if(!threadNameTextMatcher.matches(statementInfo.getThreadName())) {
                return false;
            }
            hasMatcher = true;
        }
        if(statementTextMatcher != null) {
            if(!statementTextMatcher.matches(statementInfo.getQueries())) {
                return false;
            }
            hasMatcher = true;
        }
        if(queryTypeSet != null) {
            if(!queryTypeSet.contains(statementInfo.getQueryType())) {
                return false;
            }
            hasMatcher = true;
        }
        return hasMatcher;
    }

    public TextMatcher getThreadNameTextMatcher() {
        return threadNameTextMatcher;
    }

    public TextMatcher getStatementTextMatcher() {
        return statementTextMatcher;
    }

    public Set<QueryType> getQueryTypeSet() {
        return queryTypeSet;
    }

    public boolean isActive() {
        return isActive;
    }

}
