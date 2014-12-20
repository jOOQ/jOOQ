/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import org.jooq.SQLDialect;

/**
 * The term dictionary enumerates standard expressions and their
 * dialect-specific variants if applicable
 *
 * @author Lukas Eder
 */
enum Term {

    ATAN2 {
        @Override
        public String translate(SQLDialect dialect) {
            /* [pro] */
            switch (dialect.family()) {
                case ASE:
                case SQLSERVER:
                    return "atn2";
            }

            /* [/pro] */
            return "atan2";
        }
    },
    BIT_LENGTH {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
                /* [pro] */
                case ASE:
                    return "8 * datalength";

                case ACCESS:
                case SQLSERVER:
                    return "8 * len";

                case ORACLE:
                    return "8 * lengthb";

                case DB2:
                case INGRES:
                case SYBASE:
                /* [/pro] */
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
                /* [pro] */
                case ACCESS:
                case SQLSERVER:
                    return "len";

                case DB2:
                case HANA:
                case INGRES:
                case ORACLE:
                case SYBASE:
                /* [/pro] */
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
                /* [pro] */
                // DB2 needs to do some rather complex XML manipulation to
                // achieve the same results. XMLAGG() itself cannot do it
                case DB2:
                    return "xmlagg";

                case ORACLE:
                    return "listagg";

                case SYBASE:
                    return "list";

                /* [/pro] */
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
                /* [pro] */
                case ACCESS:
                case SQLSERVER:
                    return "len";

                case ORACLE:
                    return "lengthb";

                case DB2:
                case INGRES:
                case SYBASE:
                /* [/pro] */
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
            /* [pro] */
            switch (dialect.family()) {
                case DB2:
                case HANA:
                    return "stddev";

                case INFORMIX:
                    return "stdev";

                case SQLSERVER:
                    return "stdevp";
            }

            /* [/pro] */
            return "stddev_pop";
        }
    },
    STDDEV_SAMP {
        @Override
        public String translate(SQLDialect dialect) {
            /* [pro] */
            switch (dialect.family()) {
                case DB2:
                case HANA:
                    return "stddev";

                case INFORMIX:
                    return "stdev";

                case SQLSERVER:
                    return "stdev";
            }

            /* [/pro] */
            return "stddev_samp";
        }
    },
    VAR_POP {
        @Override
        public String translate(SQLDialect dialect) {
            /* [pro] */
            switch (dialect.family()) {
                case DB2:
                case INFORMIX:
                    return "variance";

                case HANA:
                    return "var";

                case SQLSERVER:
                    return "varp";
            }

            /* [/pro] */
            return "var_pop";
        }
    },
    VAR_SAMP {
        @Override
        public String translate(SQLDialect dialect) {
            /* [pro] */
            switch (dialect.family()) {
                case DB2:
                case INFORMIX:
                    return "variance";

                case HANA:
                    return "var";

                case SQLSERVER:
                    return "var";
            }

            /* [/pro] */
            return "var_samp";
        }
    },

    ;

    /**
     * Translate the term to its dialect-specific variant
     */
    abstract String translate(SQLDialect dialect);
}
