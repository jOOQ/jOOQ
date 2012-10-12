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
package org.jooq.debug.impl;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * A "sed -e" like reg exp, of the form:<br/>
     * - /regexp/flags: find and output the matches.<br/>
     * - /regexp/replacement/flags: replace the matches and output the resulting string.<br/>
     * Flags can be left empty or any combinations of the characters 'gidmsux' (g perfoms a replace all instead of just the first match. For other flags, refer to the Javadoc of Pattern).
     * It is also possible to chain the output using ';' to perform multiple replacements.<br/>
     * If the regexp contains capturing groups, a find operation would only retain those; for a replace operation, the replacement string can refer to capturing groups with a syntax like '$1'.
     */
    public static String applySedRegularExpression(String text, String regex) {
      String originalRegEx = regex;
      if(!regex.startsWith("/")) {
        throw new IllegalArgumentException("Invalid expression format: " + originalRegEx);
      }
      regex = regex.substring(1);
      StringBuilder sb = new StringBuilder();
      char[] chars = regex.toCharArray();
      int index1 = -1;
      int index2 = -1;
      for(int i=0; i<chars.length; i++) {
        char c = chars[i];
        switch(c) {
          case ';':
            text = applySedRegularExpression(sb.toString(), text, originalRegEx, index1, index2);
            index1 = -1;
            index2 = -1;
            sb = new StringBuilder();
            i++;
            if(i >= chars.length || chars[i] != '/') {
              throw new IllegalArgumentException("Invalid expression format: " + originalRegEx);
            }
            break;
          case '\\':
            i++;
            if(i >= chars.length) {
              throw new IllegalArgumentException("Invalid expression format: " + originalRegEx);
            }
            switch(chars[i]) {
              case '/': sb.append('/'); break;
              case ';': sb.append(';'); break;
              default: sb.append('\\').append(chars[i]); break;
            }
            break;
          case '/':
            if(index1 == -1) {
              index1 = sb.length();
            } else if(index2 == -1) {
              index2 = sb.length();
            } else {
              throw new IllegalArgumentException("Invalid expression format: " + originalRegEx);
            }
            break;
          default: sb.append(c); break;
        }
      }
      if(index1 == -1) {
        throw new IllegalArgumentException("Invalid expression format: " + originalRegEx);
      }
      return applySedRegularExpression(sb.toString(), text, originalRegEx, index1, index2);
    }

    private static String applySedRegularExpression(String s, String text, String originalRegEx, int index1, int index2) {
      StringBuilder sb;
      String toFind = s.substring(0, index1);
      String replacement = index2 == -1? null: s.substring(index1, index2);
      String modifiers = index2 == -1? s.substring(index1): s.substring(index2);
      boolean isGlobal = false;
      int flags = 0;
      for(int i=0; i<modifiers.length(); i++) {
        char c = modifiers.charAt(i);
        switch(c) {
          case 'g': isGlobal = true; break;
          case 'i': flags |= Pattern.CASE_INSENSITIVE; break;
          case 'd': flags |= Pattern.UNIX_LINES; break;
          case 'm': flags |= Pattern.MULTILINE; break;
          case 's': flags |= Pattern.DOTALL; break;
          case 'u': flags |= Pattern.UNICODE_CASE; break;
          case 'x': flags |= Pattern.COMMENTS; break;
          default:
            throw new IllegalArgumentException("Invalid expression format: " + originalRegEx);
        }
      }
      Matcher matcher = Pattern.compile(toFind, flags).matcher(text);
      if(replacement == null) {
        // Just returning the matches, no replacement
        int groupCount = matcher.groupCount();
        sb = new StringBuilder();
        while(matcher.find()) {
          if(groupCount > 0) {
            for(int i=0; i<groupCount; i++) {
              String group = matcher.group(i + 1);
              if(group != null) {
                sb.append(group);
              }
            }
          } else {
            String group = matcher.group();
            if(group != null) {
              sb.append(group);
            }
          }
          if(!isGlobal) {
            break;
          }
        }
        return sb.toString();
      }
      if(isGlobal) {
        return matcher.replaceAll(replacement);
      }
      return matcher.replaceFirst(replacement);
    }

}
