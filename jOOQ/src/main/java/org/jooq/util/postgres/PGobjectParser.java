/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util.postgres;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser for Postgres PGobject anonymous types
 *
 * @author Lukas Eder
 */
public class PGobjectParser {

    /**
     * Tokenize a PGObject input string
     */
    @SuppressWarnings("null")
    public List<String> parse(String input) {
        List<String> values = new ArrayList<String>();
        int i = 0;
        int state = 0;
        StringBuilder sb = null;

        while (i < input.length()) {
            char c = input.charAt(i);

            switch (state) {
                // Initial state
                case 0:

                    // Consume the opening parenthesis
                    if (c == '(') {
                        state = 1;
                    }

                    break;

                // Before a new value
                case 1:
                    sb = new StringBuilder();

                    // Consume "empty"
                    if (c == ',') {
                        values.add(null);
                        state = 1;
                    }

                    // Consume "empty"
                    else if (c == ')') {
                        values.add(null);
                        state = 5;
                    }

                    // Consume the opening quote
                    else if (c == '"') {
                        state = 2;
                    }

                    // Consume "null"
                    else if ((c == 'n' || c == 'N') && (i + 4 < input.length())
                        && input.substring(i, i + 4).equalsIgnoreCase("null")) {
                        values.add(null);
                        i += 3;
                        state = 4;
                    }

                    // Consume a character
                    else {
                        sb.append(c);
                        state = 3;
                    }

                    break;

                // A "value" is being created
                case 2:

                    // Consume a quote
                    if (c == '"') {

                        // Consume an escaped quote
                        if (input.charAt(i + 1) == '"') {
                            sb.append(c);
                            i++;
                        }

                        // Consume the closing quote
                        else {
                            values.add(sb.toString());
                            state = 4;
                        }
                    }

                    // Consume any other character
                    else {
                        sb.append(c);
                    }

                    break;

                // A value is being created
                case 3:

                    // Consume the closing parenthesis
                    if (c == ')') {
                        values.add(sb.toString());
                        state = 5;
                    }

                    // Consume the value separator
                    else if (c == ',') {
                        values.add(sb.toString());
                        state = 1;
                    }

                    // Consume any other character
                    else {
                        sb.append(c);
                    }

                    break;

                // A value was just added
                case 4:

                    // Consume the closing parenthesis
                    if (c == ')') {
                        state = 5;
                    }

                    // Consume the value separator
                    else if (c == ',') {
                        state = 1;
                    }

                    break;
            }

            // Consume next character
            i++;
        }

        return values;
    }
}
