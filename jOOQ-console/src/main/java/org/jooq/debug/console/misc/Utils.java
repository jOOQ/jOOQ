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
package org.jooq.debug.console.misc;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;

/**
 * @author Christopher Deckers & others
 */
public class Utils {

	private Utils() {}

    public static enum DurationFormatPrecision {
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND,
    }

    public static String formatDuration(long duration) {
        return formatDuration(duration, DurationFormatPrecision.MILLISECOND);
    }

    public static String formatDuration(long duration, DurationFormatPrecision precision) {
        StringBuilder sb = new StringBuilder();
        if (duration < 0) {
            duration = -duration;
            sb.append("-");
        }
        if (duration / (24 * 1000 * 60 * 60) != 0 || precision == DurationFormatPrecision.DAY) {
            sb.append(duration / (24 * 1000 * 60 * 60)).append("d ");
            if (precision == DurationFormatPrecision.DAY) {
                return sb.toString().trim();
            }
        }
        if (duration / (1000 * 60 * 60) != 0 || precision == DurationFormatPrecision.HOUR) {
            sb.append((duration / (1000 * 60 * 60)) % 24).append("h ");
            if (precision == DurationFormatPrecision.HOUR) {
                return sb.toString().trim();
            }
        }
        if (duration / (1000 * 60) != 0 || precision == DurationFormatPrecision.MINUTE) {
            sb.append((duration / (1000 * 60)) % 60).append("min ");
            if (precision == DurationFormatPrecision.MINUTE) {
                return sb.toString().trim();
            }
        }
        if (duration / 1000 != 0 || precision == DurationFormatPrecision.SECOND) {
            sb.append((duration / 1000) % 60).append("s ");
            if (precision == DurationFormatPrecision.SECOND) {
                return sb.toString().trim();
            }
        }
        sb.append(duration % 1000).append("ms");
        return sb.toString();
    }

    public static String formatDateTimeTZ(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(date);
    }

    public static String formatDateTimeGMT(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(date);
    }

    public static String formatDateGMT(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    public static boolean equals(Object o1, Object o2) {
        return (o1 == o2) || (o1 != null && o1.equals(o2));
    }

    public static String getFormattedSql(String text) {
        if(text == null) {
            return null;
        }
        String originalText = text;
        text = text.trim();
        if(text.length() == 0) {
            return originalText;
        }
        int startIndex = originalText.indexOf(text);
        int endIndex = startIndex + text.length();
        text = text.replaceAll("[\\t\\n\\x0B\\f\\r]+", " ");
        String textIndent = "  ";
        StringBuilder sb = new StringBuilder();
        int charCount = text.length();
        char quoteStart = 0;
        boolean isLineStart = false;
        String currentIndent = "";
        boolean isWritingKeyword = isSqlKeywordStart(text.trim());
        Stack<Boolean> isParenthesisNewLineStack = new Stack<Boolean>();
        for(int i = 0; i<charCount; i++) {
            char c = text.charAt(i);
            if(quoteStart != 0) {
                if(c == quoteStart) {
                    quoteStart = 0;
                }
                sb.append(c);
            } else {
                switch(c) {
                    case '"':
                    case '\'': {
                        isWritingKeyword = false;
                        if(isLineStart) {
                            isLineStart = false;
                            sb.append(currentIndent);
                        }
                        quoteStart = c;
                        sb.append(c);
                        break;
                    }
                    case '(': {
                        isWritingKeyword = false;
                        boolean isNewLine = false;
                        if(text.length() <= i + 1 || !text.substring(i + 1).matches("\\s*(\\w+|[^\\w\\s])\\s*\\).*")) {
//                        if(text.length() > i + 1 && isKeywordStart(text.substring(i + 1).trim())) {
                            while(text.length() > i + 1 && text.charAt(i + 1) == ' ') {
                                i++;
                            }
                            isNewLine = true;
                            if(isLineStart) {
                                isLineStart = false;
                                sb.append(currentIndent);
                            }
                            sb.append(c);
                            sb.append('\n');
                            isLineStart = true;
                            currentIndent += textIndent;
                        } else {
                            sb.append(c);
                        }
                        isParenthesisNewLineStack.push(isNewLine);
                        break;
                    }
                    case ')': {
                        isWritingKeyword = false;
                        if(!isParenthesisNewLineStack.isEmpty()) {
                            boolean isNewLine = isParenthesisNewLineStack.pop();
                            if(isNewLine) {
                                if(isLineStart) {
                                    isLineStart = false;
                                } else {
                                    sb.append('\n');
                                }
                                int length = currentIndent.length();
                                length -= textIndent.length();
                                if(length >= 0) {
                                    currentIndent = currentIndent.substring(0, length);
                                }
                                sb.append(currentIndent);
                            }
                        }
                        sb.append(c);
                        break;
                    }
                    case ' ': {
                        boolean isFirstKeyWord = !isWritingKeyword;
                        isWritingKeyword = text.length() > i + 1 && isSqlKeywordStart(text.substring(i + 1).trim());
                        if(!isLineStart && isWritingKeyword && isFirstKeyWord) {
                            while(text.length() > i + 1 && text.charAt(i + 1) == ' ') {
                                i++;
                            }
                            sb.append('\n');
                            isLineStart = true;
                        } else {
                            if(text.length() > i + 1 && text.charAt(i + 1) != ' ') {
                                sb.append(c);
                            }
                        }
                        break;
                    }
                    case ',': {
                        isWritingKeyword = false;
                        if(isLineStart) {
                            isLineStart = false;
                            sb.append(currentIndent);
                        }
                        sb.append(c);
                        if(text.length() > i + 1 && text.charAt(i + 1) != ' ') {
                            sb.append(' ');
                        }
                        break;
                    }
                    case '[':
                    case ']':
                        isWritingKeyword = false;
                        // Fall through
                    default: {
                        if(isLineStart) {
                            isLineStart = false;
                            sb.append(currentIndent);
                        }
                        sb.append(c);
                        break;
                    }
                }
            }
        }
        String newContent = sb.toString().replaceAll(" +\n", "\n");
        String newText = originalText.substring(0, startIndex) + newContent + originalText.substring(endIndex);
        return newText;
    }

    private static final Set<String> keywordSet;

    static {
        keywordSet = new HashSet<String>();
        keywordSet.addAll(Arrays.<String>asList(
            "UNION",
            "CROSS",
            "INNER",
            "JOIN",
            "ORDER",
            "GROUP",
            "BY",
            "HAVING",
            "ON",
            "WITH",
            "INSERT",
            "DELETE",
            "SELECT",
            "UPDATE",
            "VALUES",
            "LEFT",
            "OUTER",
            "FROM",
            "WHERE",
            "AND",
            "OR",
            "SET"
        ));
    }

    private static boolean isSqlKeywordStart(String s) {
        s = s.toUpperCase(Locale.ENGLISH);
        int index = s.indexOf(' ');
        if(index < 1) {
            return false;
        }
        return keywordSet.contains(s.substring(0, index));
    }

}
