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
package org.jooq.util.informix;

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
 * Supported data types for the {@link SQLDialect#INFORMIX} dialect
 *
 * @see <a href="http://publib.boulder.ibm.com/infocenter/idshelp/v10/index.jsp?topic=/com.ibm.sqlr.doc/sqlrmst99.htm">http://publib.boulder.ibm.com/infocenter/idshelp/v10/index.jsp?topic=/com.ibm.sqlr.doc/sqlrmst99.htm</a>
 * @author Lukas Eder
 */
public class InformixDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Boolean>    BOOLEAN            = new DefaultDataType<Boolean>(SQLDialect.INFORMIX, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Short>      SMALLINT           = new DefaultDataType<Short>(SQLDialect.INFORMIX, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>    INTEGER            = new DefaultDataType<Integer>(SQLDialect.INFORMIX, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>    INT                = new DefaultDataType<Integer>(SQLDialect.INFORMIX, SQLDataType.INTEGER, "int");
    public static final DataType<Long>       BIGINT             = new DefaultDataType<Long>(SQLDialect.INFORMIX, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>       INT8               = new DefaultDataType<Long>(SQLDialect.INFORMIX, SQLDataType.BIGINT, "int8");
    public static final DataType<Float>      SMALLFLOAT         = new DefaultDataType<Float>(SQLDialect.INFORMIX, SQLDataType.REAL, "smallfloat");
    public static final DataType<Float>      REAL               = new DefaultDataType<Float>(SQLDialect.INFORMIX, SQLDataType.REAL, "real");
    public static final DataType<Double>     FLOAT              = new DefaultDataType<Double>(SQLDialect.INFORMIX, SQLDataType.DOUBLE, "float");
    public static final DataType<Double>     DOUBLE_PRECISION   = new DefaultDataType<Double>(SQLDialect.INFORMIX, SQLDataType.DOUBLE, "double precision");
    public static final DataType<BigDecimal> DECIMAL            = new DefaultDataType<BigDecimal>(SQLDialect.INFORMIX, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> DEC                = new DefaultDataType<BigDecimal>(SQLDialect.INFORMIX, SQLDataType.DECIMAL, "dec");
    public static final DataType<BigDecimal> NUMERIC            = new DefaultDataType<BigDecimal>(SQLDialect.INFORMIX, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>     LVARCHAR           = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.VARCHAR, "lvarchar");
    public static final DataType<String>     VARCHAR            = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.VARCHAR, "varchar", "lvarchar(254)");
    public static final DataType<String>     CHAR               = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.CHAR, "char");
    public static final DataType<String>     CHARACTER          = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.CHAR, "character");
    public static final DataType<String>     LONGVARCHAR        = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.LONGVARCHAR, "lvarchar", "clob");
    public static final DataType<String>     NCHAR              = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NVARCHAR           = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.NVARCHAR, "nvarchar", "nvarchar(254)");
    public static final DataType<String>     TEXT               = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.CLOB, "text");
    public static final DataType<String>     CLOB               = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.CLOB, "clob");
    public static final DataType<byte[]>     BYTE               = new DefaultDataType<byte[]>(SQLDialect.INFORMIX, SQLDataType.VARBINARY, "byte");
    public static final DataType<byte[]>     BLOB               = new DefaultDataType<byte[]>(SQLDialect.INFORMIX, SQLDataType.BLOB, "blob");
    public static final DataType<Date>       DATE               = new DefaultDataType<Date>(SQLDialect.INFORMIX, SQLDataType.DATE, "date");
    public static final DataType<Timestamp>  DATETIME           = new DefaultDataType<Timestamp>(SQLDialect.INFORMIX, SQLDataType.TIMESTAMP, "datetime", "datetime year to fraction");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.INFORMIX, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>     __BINARY        = new DefaultDataType<byte[]>(SQLDialect.INFORMIX, SQLDataType.BINARY, "blob");
    protected static final DataType<Boolean>    __BIT           = new DefaultDataType<Boolean>(SQLDialect.INFORMIX, SQLDataType.BIT, "boolean");
    protected static final DataType<byte[]>     __LONGVARBINARY = new DefaultDataType<byte[]>(SQLDialect.INFORMIX, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<String>     __NCLOB         = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>     __LONGNVARCHAR  = new DefaultDataType<String>(SQLDialect.INFORMIX, SQLDataType.LONGNVARCHAR, "long varchar", "clob");
    protected static final DataType<BigDecimal> __NUMERIC       = new DefaultDataType<BigDecimal>(SQLDialect.INFORMIX, SQLDataType.NUMERIC, "decimal", "decimal");
    protected static final DataType<Byte>       __TINYINT       = new DefaultDataType<Byte>(SQLDialect.INFORMIX, SQLDataType.TINYINT, "smallint");
    protected static final DataType<Time>       __TIME          = new DefaultDataType<Time>(SQLDialect.INFORMIX, SQLDataType.TIME, "datetime", "datetime hour to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER    = new DefaultDataType<BigInteger>(SQLDialect.INFORMIX, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal(31)");
    protected static final DataType<UUID>       __UUID          = new DefaultDataType<UUID>(SQLDialect.INFORMIX, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Integer>    SERIAL             = new DefaultDataType<Integer>(SQLDialect.INFORMIX, SQLDataType.INTEGER, "serial");
    public static final DataType<Long>       SERIAL8            = new DefaultDataType<Long>(SQLDialect.INFORMIX, SQLDataType.BIGINT, "serial8");
    public static final DataType<BigDecimal> MONEY              = new DefaultDataType<BigDecimal>(SQLDialect.INFORMIX, SQLDataType.DECIMAL, "money");
}
