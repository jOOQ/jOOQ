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
package org.jooq.debug;

import java.util.Locale;

/**
 * @author Christopher Deckers
 */
public enum SqlQueryType {
    SELECT,
    INSERT,
    UPDATE,
    DELETE,
    OTHER,

    ;

    public static SqlQueryType detectType(String sql) {
        String queryLC = sql.toLowerCase(Locale.ENGLISH).trim();
        if(queryLC.startsWith("insert ")) {
            return SqlQueryType.INSERT;
        }
        if(queryLC.startsWith("update ")) {
            return SqlQueryType.UPDATE;
        }
        if(queryLC.startsWith("delete ")) {
            return SqlQueryType.DELETE;
        }
        if(queryLC.startsWith("select ")) {
            return SqlQueryType.SELECT;
        }
        if(queryLC.startsWith("with ")) {
            // Let's skip the table definition to see what action is performed.
            queryLC = queryLC.replaceAll("[\\t\\n\\x0B\\f\\r]+", " ");
            queryLC = queryLC.substring("with ".length());
            boolean isFindingWithStatementEnd = true;
            while(isFindingWithStatementEnd) {
                isFindingWithStatementEnd = false;
                // Let's consider the query is properly created, and the "as" is at the right place.
                int index = queryLC.indexOf(" as ");
                if(index == -1) {
                    break;
                }
                queryLC = queryLC.substring(index + " as ".length()).trim();
                if(!queryLC.startsWith("(")) {
                    break;
                }
                int length = queryLC.length();
                boolean isQuote = false;
                int pCount = 1;
                for(int i=1; i<length; i++) {
                    char c = queryLC.charAt(i);
                    switch(c) {
                        case '\'':
                            isQuote = !isQuote;
                            break;
                        case '(':
                            if(!isQuote) {
                                pCount++;
                            }
                            break;
                        case ')':
                            if(!isQuote) {
                                pCount--;
                            }
                            break;
                    }
                    if(pCount == 0) {
                        queryLC = queryLC.substring(i + 1).trim();
                        if(queryLC.startsWith(",")) {
                            // Another table definition in the with clause, so let's do another round.
                            isFindingWithStatementEnd = true;
                            break;
                        } else {
                            // We removed the with part. Let's ask the type of this.
                            return detectType(queryLC);
                        }
                    }
                }
            }
            // If we couldn't find a proper with structure, default to OTHER.
            return SqlQueryType.OTHER;
        }
        return SqlQueryType.OTHER;
    }

}