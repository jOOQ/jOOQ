/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import java.util.EnumMap;

import org.jooq.SQLDialect;

/**
 * An internal utility class for SQL identifiers.
 *
 * @author Lukas Eder
 */
final class Identifiers {

    /**
     * The structure is:
     * <p>
     * <pre>
     * SQLDialect -> {
     *     { main         start delimiter, alternative         start delimiter, ... },
     *     { main         end   delimiter, alternative         end   delimiter, ... },
     *     { main escaped end   delimiter, alternative escaped end   delimiter, ... },
     *       ...
     * }
     * </pre>
     */
    static final EnumMap<SQLDialect, String[][]> QUOTES;
    static final int                             QUOTE_START_DELIMITER       = 0;
    static final int                             QUOTE_END_DELIMITER         = 1;
    static final int                             QUOTE_END_DELIMITER_ESCAPED = 2;

    static {
        QUOTES = new EnumMap<SQLDialect, String[][]>(SQLDialect.class);

        for (SQLDialect family : SQLDialect.families()) {
            switch (family) {

                // MySQL supports backticks and double quotes
                case MARIADB:
                case MYSQL:
                    QUOTES.put(family, new String[][] {
                        { "`" , "\""},
                        { "`" , "\""},
                        { "``", "\"\"" }
                    });
                    break;















                // Most dialects implement the SQL standard, using double quotes







                case CUBRID:
                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB:
                case POSTGRES:
                case SQLITE:
                default:
                    QUOTES.put(family, new String[][] {
                        { "\""},
                        { "\""},
                        { "\"\"" }
                    });
                    break;
            }
        }
    }
}
