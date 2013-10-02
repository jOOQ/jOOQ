/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.util.ase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;

/**
 * Supported data types for the {@link SQLDialect#ASE} dialect
 *
 * @see <a href="http://infocenter.sybase.com/help/topic/com.sybase.infocenter.dc36271.1550/html/blocks/X31825.htm">http://infocenter.sybase.com/help/topic/com.sybase.infocenter.dc36271.1550/html/blocks/X31825.htm</a>
 * @author Lukas Eder
 */
public class ASEDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>       TINYINT                    = new DefaultDataType<Byte>(SQLDialect.ASE, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>      SMALLINT                   = new DefaultDataType<Short>(SQLDialect.ASE, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>      UNSIGNEDSMALLLINT          = new DefaultDataType<Short>(SQLDialect.ASE, SQLDataType.SMALLINT, "unsigned smallint");
    public static final DataType<Integer>    INT                        = new DefaultDataType<Integer>(SQLDialect.ASE, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER                    = new DefaultDataType<Integer>(SQLDialect.ASE, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>    UNSIGNEDINT                = new DefaultDataType<Integer>(SQLDialect.ASE, SQLDataType.INTEGER, "unsigned int");
    public static final DataType<Long>       BIGINT                     = new DefaultDataType<Long>(SQLDialect.ASE, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>       UNSIGNEDBIGINT             = new DefaultDataType<Long>(SQLDialect.ASE, SQLDataType.BIGINT, "unsigned bigint");
    public static final DataType<Double>     DOUBLE_PRECISION           = new DefaultDataType<Double>(SQLDialect.ASE, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>     FLOAT                      = new DefaultDataType<Double>(SQLDialect.ASE, SQLDataType.FLOAT, "float");
    public static final DataType<Float>      REAL                       = new DefaultDataType<Float>(SQLDialect.ASE, SQLDataType.REAL, "real");
    public static final DataType<BigDecimal> DECIMAL                    = new DefaultDataType<BigDecimal>(SQLDialect.ASE, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> DEC                        = new DefaultDataType<BigDecimal>(SQLDialect.ASE, SQLDataType.DECIMAL, "dec");
    public static final DataType<BigDecimal> NUMERIC                    = new DefaultDataType<BigDecimal>(SQLDialect.ASE, SQLDataType.NUMERIC, "numeric");
    public static final DataType<Boolean>    BIT                        = new DefaultDataType<Boolean>(SQLDialect.ASE, SQLDataType.BIT, "bit");
    public static final DataType<String>     VARCHAR                    = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     CHAR                       = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.CHAR, "char");
    public static final DataType<String>     LONGVARCHAR                = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.LONGVARCHAR, "text");
    public static final DataType<String>     NCHAR                      = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NVARCHAR                   = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>     TEXT                       = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.CLOB, "text");
    public static final DataType<Date>       DATE                       = new DefaultDataType<Date>(SQLDialect.ASE, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                       = new DefaultDataType<Time>(SQLDialect.ASE, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  DATETIME                   = new DefaultDataType<Timestamp>(SQLDialect.ASE, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<Timestamp>  TIMESTAMP                  = new DefaultDataType<Timestamp>(SQLDialect.ASE, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<byte[]>     BINARY                     = new DefaultDataType<byte[]>(SQLDialect.ASE, SQLDataType.BINARY, "binary");
    public static final DataType<byte[]>     VARBINARY                  = new DefaultDataType<byte[]>(SQLDialect.ASE, SQLDataType.VARBINARY, "varbinary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>  __BLOB                     = new DefaultDataType<byte[]>(SQLDialect.ASE, SQLDataType.BLOB, "binary");
    protected static final DataType<byte[]>  __LONGVARBINARY            = new DefaultDataType<byte[]>(SQLDialect.ASE, SQLDataType.LONGVARBINARY, "varbinary");
    protected static final DataType<Boolean> __BOOLEAN                  = new DefaultDataType<Boolean>(SQLDialect.ASE, SQLDataType.BOOLEAN, "bit");
    protected static final DataType<String>  __LONGNVARCHAR             = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.LONGNVARCHAR, "unitext");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER            = new DefaultDataType<BigInteger>(SQLDialect.ASE, SQLDataType.DECIMAL_INTEGER, "decimal");
    protected static final DataType<UUID>       __UUID                  = new DefaultDataType<UUID>(SQLDialect.ASE, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<String>     UNICHAR                    = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.NCHAR, "unichar");
    public static final DataType<String>     UNITEXT                    = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.NCLOB, "unitext");
    public static final DataType<String>     UNIVARCHAR                 = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.NVARCHAR, "univarchar");
    public static final DataType<String>     SYSNAME                    = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.VARCHAR, "sysname");
    public static final DataType<String>     LONGSYSNAME                = new DefaultDataType<String>(SQLDialect.ASE, SQLDataType.VARCHAR, "longsysname");
    public static final DataType<BigDecimal> MONEY                      = new DefaultDataType<BigDecimal>(SQLDialect.ASE, SQLDataType.DECIMAL, "money");
    public static final DataType<BigDecimal> SMALLMONEY                 = new DefaultDataType<BigDecimal>(SQLDialect.ASE, SQLDataType.DECIMAL, "smallmoney");
    public static final DataType<Timestamp>  SMALLDATETIME              = new DefaultDataType<Timestamp>(SQLDialect.ASE, SQLDataType.TIMESTAMP, "smalldatetime");
    public static final DataType<Timestamp>  BIGDATETIME                = new DefaultDataType<Timestamp>(SQLDialect.ASE, SQLDataType.TIMESTAMP, "bigdatetime");
    public static final DataType<Time>       BIGTIME                    = new DefaultDataType<Time>(SQLDialect.ASE, SQLDataType.TIME, "bigtime");
    public static final DataType<byte[]>     IMAGE                      = new DefaultDataType<byte[]>(SQLDialect.ASE, SQLDataType.BINARY, "image");

    private ASEDataType() {}
}
