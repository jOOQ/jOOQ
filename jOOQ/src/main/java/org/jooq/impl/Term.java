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

import org.jooq.SQLDialect;

/**
 * The term dictionary enumerates standard expressions and their
 * dialect-specific variants if applicable
 *
 * @author Lukas Eder
 */
enum Term {

    ARRAY_AGG {
        @Override
        public String translate(SQLDialect dialect) {
            return "array_agg";
        }
    },
    ATAN2 {
        @Override
        public String translate(SQLDialect dialect) {








            return "atan2";
        }
    },
    BIT_LENGTH {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
















                case DERBY:
                case SQLITE:
                    return "8 * length";
            }

            return "bit_length";
        }
    },
    CHAR_LENGTH {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {











                case DERBY:
                case SQLITE:
                    return "length";
            }

            return "char_length";
        }
    },








    LIST_AGG {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {













                case CUBRID:
                case H2:
                case HSQLDB:
                case MARIADB:
                case MYSQL:
                case SQLITE:
                    return "group_concat";

                case POSTGRES:
                    return "string_agg";
            }

            return "listagg";
        }
    },
    MEDIAN {
        @Override
        public String translate(SQLDialect dialect) {
            return "median";
        }
    },
    OCTET_LENGTH {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {













                case DERBY:
                case SQLITE:
                    return "length";
            }

            return "octet_length";
        }
    },
    ROW_NUMBER {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
                case HSQLDB:
                    return "rownum";
            }

            return "row_number";
        }
    },
    STDDEV_POP {
        @Override
        public String translate(SQLDialect dialect) {














            return "stddev_pop";
        }
    },
    STDDEV_SAMP {
        @Override
        public String translate(SQLDialect dialect) {














            return "stddev_samp";
        }
    },
    VAR_POP {
        @Override
        public String translate(SQLDialect dialect) {














            return "var_pop";
        }
    },
    VAR_SAMP {
        @Override
        public String translate(SQLDialect dialect) {














            return "var_samp";
        }
    },

    ;

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Translate the term to its dialect-specific variant
     */
    abstract String translate(SQLDialect dialect);
}
