/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static org.jooq.impl.DSL.raw;

import org.jooq.SQL;

/**
 * An internal {@link SQL} cache.
 *
 * @author Lukas Eder
 */
final class Operators {

    // For a list of standardised HTML entity names, see https://dev.w3.org/html5/html-author/charref
    static final SQL OP_AMP    = raw("&");
    static final SQL OP_AST    = raw("*");
    static final SQL OP_COMMAT = raw("@");
    static final SQL OP_DOLLAR = raw("$");
    static final SQL OP_EQUALS = raw("=");
    static final SQL OP_EXCL   = raw("!");
    static final SQL OP_GT     = raw(">");
    static final SQL OP_HAT    = raw("^");
    static final SQL OP_LT     = raw("<");
    static final SQL OP_NUM    = raw("#");
    static final SQL OP_PERCNT = raw("%");
    static final SQL OP_PLUS   = raw("+");
    static final SQL OP_QUEST  = raw("?");
    static final SQL OP_SOL    = raw("/");
    static final SQL OP_VERBAR = raw("|");

}
