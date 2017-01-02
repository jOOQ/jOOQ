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
