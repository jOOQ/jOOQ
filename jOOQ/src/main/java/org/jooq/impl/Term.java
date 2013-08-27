/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
            switch (dialect.family()) {
                case ASE:
                case SQLSERVER:
                    return "atn2";
            }

            return "atan2";
        }
    },
    BIT_LENGTH {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
                case ASE:
                    return "8 * datalength";

                case DB2:
                case DERBY:
                case INGRES:
                case SQLITE:
                case SYBASE:
                    return "8 * length";

                case SQLSERVER:
                    return "8 * len";

                case ORACLE:
                    return "8 * lengthb";
            }

            return "bit_length";
        }
    },
    CHAR_LENGTH {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
                case DB2:
                case DERBY:
                case INGRES:
                case ORACLE:
                case SQLITE:
                case SYBASE:
                    return "length";

                case SQLSERVER:
                    return "len";
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
                    return "group_concat";

                // DB2 needs to do some rather complex XML manipulation to
                // achieve the same results. XMLAGG() itself cannot do it
                case DB2:
                    return "xmlagg";

                case ORACLE:
                    return "listagg";

                case POSTGRES:
                    return "string_agg";

                case SYBASE:
                    return "list";
            }

            return "listagg";
        }
    },
    OCTET_LENGTH {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
                case DB2:
                case DERBY:
                case INGRES:
                case SQLITE:
                case SYBASE:
                    return "length";

                case SQLSERVER:
                    return "len";

                case ORACLE:
                    return "lengthb";
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
            switch (dialect.family()) {
                case DB2:
                    return "stddev";

                case SQLSERVER:
                    return "stdevp";
            }

            return "stddev_pop";
        }
    },
    STDDEV_SAMP {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
                case DB2:
                    return "stddev";

                case SQLSERVER:
                    return "stdev";
            }

            return "stddev_samp";
        }
    },
    VAR_POP {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
                case DB2:
                    return "variance";

                case SQLSERVER:
                    return "varp";
            }

            return "var_pop";
        }
    },
    VAR_SAMP {
        @Override
        public String translate(SQLDialect dialect) {
            switch (dialect.family()) {
                case DB2:
                    return "variance";

                case SQLSERVER:
                    return "var";
            }

            return "var_samp";
        }
    },

    ;

    /**
     * Translate the term to its dialect-specific variant
     */
    abstract String translate(SQLDialect dialect);
}
