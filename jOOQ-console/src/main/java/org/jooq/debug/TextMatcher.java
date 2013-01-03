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
import java.util.regex.Pattern;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class TextMatcher implements Serializable {

    public static enum TextMatchingType {
        STARTS_WITH("Starts with"),
        CONTAINS("Contains"),
        EQUALS("Equals"),
        MATCHES_REG_EXP("Matches Reg. Exp."),
        ;
        private String name;
        TextMatchingType(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return name;
        }
    }

    private TextMatchingType type;
    private String text;
    private boolean isCaseSensitive;

    private Pattern pattern;

    public TextMatcher(TextMatchingType type, String text, boolean isCaseSensitive) {
        this.type = type;
        this.text = text;
        this.isCaseSensitive = isCaseSensitive;
        switch (type) {
            case CONTAINS: {
                String patternText = ".*\\Q" + text.replace("\\E", "\\\\E").replace("\\Q", "\\\\Q") + "\\E.*\\Q";
                pattern = Pattern.compile(patternText, isCaseSensitive? Pattern.DOTALL: Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                break;
            }
            case MATCHES_REG_EXP: {
                pattern = Pattern.compile(text, isCaseSensitive? Pattern.DOTALL: Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                break;
            }
        }
    }

    public boolean matches(String... texts) {
        if(text == null) {
            return false;
        }
        switch(type) {
            case STARTS_WITH: {
                for(String sql: texts) {
                    if(isCaseSensitive) {
                        if(sql.startsWith(text)) {
                            return true;
                        }
                    } else if(sql.length() >= text.length()) {
                        // Let's avoid creating new String instance.
                        if(sql.substring(0, text.length()).equalsIgnoreCase(text)) {
                            return true;
                        }
                    }
                }
                return false;
            }
            case EQUALS: {
                for(String sql: texts) {
                    if(isCaseSensitive) {
                        if(sql.equals(text)) {
                            return true;
                        }
                    } else {
                        if(sql.equalsIgnoreCase(text)) {
                            return true;
                        }
                    }
                }
                return false;
            }
            case CONTAINS: {
                for(String sql: texts) {
                    if(pattern.matcher(sql).matches()) {
                        return true;
                    }
                }
                return false;
            }
            case MATCHES_REG_EXP: {
                for(String sql: texts) {
                    if(pattern.matcher(sql).matches()) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public TextMatchingType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

}
