/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.sql;

import org.jooq.SQL;

/**
 * An internal {@link SQL} cache.
 *
 * @author Lukas Eder
 */
final class Operators {

    // For a list of standardised HTML entity names, see https://dev.w3.org/html5/html-author/charref
    static final SQL OP_AMP    = sql("&");
    static final SQL OP_AST    = sql("*");
    static final SQL OP_COMMAT = sql("@");
    static final SQL OP_DOLLAR = sql("$");
    static final SQL OP_EQUALS = sql("=");
    static final SQL OP_EXCL   = sql("!");
    static final SQL OP_GT     = sql(">");
    static final SQL OP_HAT    = sql("^");
    static final SQL OP_LT     = sql("<");
    static final SQL OP_NUM    = sql("#");
    static final SQL OP_PERCNT = sql("%");
    static final SQL OP_PLUS   = sql("+");
    static final SQL OP_QUEST  = sql("?");
    static final SQL OP_SOL    = sql("/");
    static final SQL OP_VERBAR = sql("|");

}
