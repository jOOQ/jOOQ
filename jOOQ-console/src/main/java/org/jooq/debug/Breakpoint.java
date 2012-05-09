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

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class Breakpoint implements Serializable {

    private int id;
    private StatementMatcher statementMatcher;
//    private Integer hitCount;
    private boolean isBreaking;
    private StatementProcessor beforeExecutionProcessor;
    private StatementProcessor replacementExecutionProcessor;
    private StatementProcessor afterExecutionProcessor;

    public Breakpoint(int id, StatementMatcher statementMatcher, boolean isBreaking, StatementProcessor beforeExecutionProcessor, StatementProcessor replacementExecutionProcessor, StatementProcessor afterExecutionProcessor) {
        this.id = id;
        this.statementMatcher = statementMatcher;
        this.isBreaking = isBreaking;
        this.beforeExecutionProcessor = beforeExecutionProcessor;
        this.replacementExecutionProcessor = replacementExecutionProcessor;
        this.afterExecutionProcessor = afterExecutionProcessor;
    }

    public int getID() {
        return id;
    }

    public StatementMatcher getStatementMatcher() {
        return statementMatcher;
    }

    public boolean matches(StatementInfo statementInfo) {
        return statementMatcher != null && statementMatcher.matches(statementInfo);
    }

    public boolean isBreaking() {
        return isBreaking;
    }

    public StatementProcessor getBeforeExecutionProcessor() {
        return beforeExecutionProcessor;
    }

    public StatementProcessor getReplacementExecutionProcessor() {
        return replacementExecutionProcessor;
    }

    public StatementProcessor getAfterExecutionProcessor() {
        return afterExecutionProcessor;
    }

}
