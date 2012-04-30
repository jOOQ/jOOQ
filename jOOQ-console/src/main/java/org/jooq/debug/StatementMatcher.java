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
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class StatementMatcher implements Serializable {

    public static enum TextMatchingType {
        STARTS_WITH,
        CONTAINS,
        EQUALS,
        MATCHES_REG_EXP,
    }

    private String threadName;
    private TextMatchingType textMatchingType;
    private String text;
    private boolean isTextMatchingCaseSensitive;
    private Set<SqlQueryType> queryTypeSet;

    private Pattern pattern;

    /**
     * @param threadName a name or null for no matching on thread name.
     * @param textMatchingType a type or null for no text matching.
     * @param queryTypeSet some types or null for all types.
     */
    public StatementMatcher(String threadName, TextMatchingType textMatchingType, String text, boolean isTextMatchingCaseSensitive, Set<SqlQueryType> queryTypeSet) {
        this.threadName = threadName;
        this.textMatchingType = textMatchingType;
        this.text = text;
        this.isTextMatchingCaseSensitive = isTextMatchingCaseSensitive;
        this.queryTypeSet = queryTypeSet == null? null: EnumSet.copyOf(queryTypeSet);
        switch (textMatchingType) {
            case CONTAINS: {
                String patternText = ".*\\Q" + text.replace("\\E", "\\\\E").replace("\\Q", "\\\\Q") + "\\E.*\\Q";
                pattern = Pattern.compile(patternText, isTextMatchingCaseSensitive? 0: Pattern.CASE_INSENSITIVE);
                break;
            }
            case MATCHES_REG_EXP: {
                pattern = Pattern.compile(text, isTextMatchingCaseSensitive? 0: Pattern.CASE_INSENSITIVE);
                break;
            }
        }
    }

    public boolean matches(StatementInfo statementInfo) {
        if(threadName != null) {
            if(!threadName.equals(statementInfo.getThreadName())) {
                return false;
            }
        }
        if(textMatchingType != null) {
            if(text == null) {
                return false;
            }
            switch(textMatchingType) {
                case STARTS_WITH: {
                    boolean isMatching = false;
                    for(String sql: statementInfo.getQueries()) {
                        if(isTextMatchingCaseSensitive) {
                            if(sql.startsWith(text)) {
                                isMatching = true;
                                break;
                            }
                        } else if(sql.length() >= text.length()) {
                            // Let's avoid creating new String instance.
                            if(sql.substring(0, text.length()).equalsIgnoreCase(text)) {
                                isMatching = true;
                                break;
                            }
                        }
                    }
                    if(!isMatching) {
                        return false;
                    }
                    break;
                }
                case EQUALS: {
                    boolean isMatching = false;
                    for(String sql: statementInfo.getQueries()) {
                        if(isTextMatchingCaseSensitive) {
                            if(sql.equals(text)) {
                                isMatching = true;
                                break;
                            }
                        } else {
                            if(sql.equalsIgnoreCase(text)) {
                                isMatching = true;
                                break;
                            }
                        }
                    }
                    if(!isMatching) {
                        return false;
                    }
                    break;
                }
                case CONTAINS: {
                    boolean isMatching = false;
                    for(String sql: statementInfo.getQueries()) {
                        if(pattern.matcher(sql).matches()) {
                            isMatching = true;
                            break;
                        }
                    }
                    if(!isMatching) {
                        return false;
                    }
                }
                case MATCHES_REG_EXP: {
                    boolean isMatching = false;
                    for(String sql: statementInfo.getQueries()) {
                        if(pattern.matcher(sql).matches()) {
                            isMatching = true;
                            break;
                        }
                    }
                    if(!isMatching) {
                        return false;
                    }
                }
            }
        }
        if(queryTypeSet != null) {
            if(!queryTypeSet.contains(statementInfo.getQueryType())) {
                return false;
            }
        }
        return true;
    }


}
