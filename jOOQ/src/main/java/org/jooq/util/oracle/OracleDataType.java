/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */

package org.jooq.util.oracle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;

/**
 * Supported data types for the {@link SQLDialect#ORACLE} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://www.techonthenet.com/oracle/datatypes.php">http://www.techonthenet.com/oracle/datatypes.php</a>
 * @see <a href="http://download.oracle.com/docs/cd/B19306_01/appdev.102/b14261/datatypes.htm">http://download.oracle.com/docs/cd/B19306_01/appdev.102/b14261/datatypes.htm</a>
 */
public class OracleDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<BigDecimal>   NUMBER                 = new DefaultDataType<BigDecimal>(SQLDialect.ORACLE, SQLDataType.NUMERIC, "number");
    public static final DataType<BigDecimal>   NUMERIC                = new DefaultDataType<BigDecimal>(SQLDialect.ORACLE, SQLDataType.NUMERIC, "numeric");
    public static final DataType<BigDecimal>   DECIMAL                = new DefaultDataType<BigDecimal>(SQLDialect.ORACLE, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal>   DEC                    = new DefaultDataType<BigDecimal>(SQLDialect.ORACLE, SQLDataType.DECIMAL, "dec");
    public static final DataType<String>       VARCHAR2               = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.VARCHAR, "varchar2", "varchar2(4000)");
    public static final DataType<String>       VARCHAR                = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.VARCHAR, "varchar", "varchar2(4000)");
    public static final DataType<String>       CHAR                   = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.CHAR, "char", "varchar2(4000)");
    public static final DataType<String>       CLOB                   = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.CLOB, "clob");
    public static final DataType<String>       NVARCHAR2              = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.NVARCHAR, "nvarchar2", "varchar2(4000)");
    public static final DataType<String>       NVARCHAR               = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.NVARCHAR, "nvarchar", "varchar2(4000)");
    public static final DataType<String>       NCHAR                  = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.NCHAR, "nchar", "varchar2(4000)");
    public static final DataType<String>       NCLOB                  = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.NCLOB, "nclob");
    public static final DataType<Date>         DATE                   = new DefaultDataType<Date>(SQLDialect.ORACLE, SQLDataType.DATE, "date");
    public static final DataType<Timestamp>    TIMESTAMP              = new DefaultDataType<Timestamp>(SQLDialect.ORACLE, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>       BLOB                   = new DefaultDataType<byte[]>(SQLDialect.ORACLE, SQLDataType.BLOB, "blob");
    public static final DataType<YearToMonth>  INTERVALYEARTOMONTH    = new DefaultDataType<YearToMonth>(SQLDialect.ORACLE, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond>  INTERVALDAYTOSECOND    = new DefaultDataType<DayToSecond>(SQLDialect.ORACLE, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.ORACLE, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>    __BINARY               = new DefaultDataType<byte[]>(SQLDialect.ORACLE, SQLDataType.BINARY, "blob");
    protected static final DataType<Long>      __BIGINT               = new DefaultDataType<Long>(SQLDialect.ORACLE, SQLDataType.BIGINT, "number", "number(19)");
    protected static final DataType<Boolean>   __BIT                  = new DefaultDataType<Boolean>(SQLDialect.ORACLE, SQLDataType.BIT, "number", "number(1)");
    protected static final DataType<Boolean>   __BOOLEAN              = new DefaultDataType<Boolean>(SQLDialect.ORACLE, SQLDataType.BOOLEAN, "number", "number(1)");
    protected static final DataType<Double>    __DOUBLE               = new DefaultDataType<Double>(SQLDialect.ORACLE, SQLDataType.DOUBLE, "number");
    protected static final DataType<Double>    __FLOAT                = new DefaultDataType<Double>(SQLDialect.ORACLE, SQLDataType.FLOAT, "number");
    protected static final DataType<Integer>   __INTEGER              = new DefaultDataType<Integer>(SQLDialect.ORACLE, SQLDataType.INTEGER, "number", "number(10)");
    protected static final DataType<byte[]>    __LONGVARBINARY        = new DefaultDataType<byte[]>(SQLDialect.ORACLE, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<String>    __LONGVARCHAR          = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.LONGVARCHAR, "varchar2", "varchar2(4000)");
    protected static final DataType<String>    __LONGNVARCHAR         = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.LONGNVARCHAR, "varchar2", "varchar2(4000)");
    protected static final DataType<Float>     __REAL                 = new DefaultDataType<Float>(SQLDialect.ORACLE, SQLDataType.REAL, "number");
    protected static final DataType<Short>     __SMALLINT             = new DefaultDataType<Short>(SQLDialect.ORACLE, SQLDataType.SMALLINT, "number", "number(5)");
    protected static final DataType<Time>      __TIME                 = new DefaultDataType<Time>(SQLDialect.ORACLE, SQLDataType.TIME, "timestamp");
    protected static final DataType<Byte>      __TINYINT              = new DefaultDataType<Byte>(SQLDialect.ORACLE, SQLDataType.TINYINT, "number", "number(3)");
    protected static final DataType<byte[]>    __VARBINARY            = new DefaultDataType<byte[]>(SQLDialect.ORACLE, SQLDataType.VARBINARY, "blob");
    protected static final DataType<UByte>     __TINYINTUNSIGNED      = new DefaultDataType<UByte>(SQLDialect.ORACLE, SQLDataType.TINYINTUNSIGNED, "number", "number(3)");
    protected static final DataType<UShort>    __SMALLINTUNSIGNED     = new DefaultDataType<UShort>(SQLDialect.ORACLE, SQLDataType.SMALLINTUNSIGNED, "number", "number(5)");
    protected static final DataType<UInteger>  __INTEGERUNSIGNED      = new DefaultDataType<UInteger>(SQLDialect.ORACLE, SQLDataType.INTEGERUNSIGNED, "number", "number(10)");
    protected static final DataType<ULong>     __BIGINTUNSIGNED       = new DefaultDataType<ULong>(SQLDialect.ORACLE, SQLDataType.BIGINTUNSIGNED, "number", "number(20)");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER          = new DefaultDataType<BigInteger>(SQLDialect.ORACLE, SQLDataType.DECIMAL_INTEGER, "number");
    protected static final DataType<UUID>       __UUID                = new DefaultDataType<UUID>(SQLDialect.ORACLE, SQLDataType.UUID, "varchar2", "varchar2(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Result<Record>>  REF_CURSOR          = new DefaultDataType<Result<Record>>(SQLDialect.ORACLE, SQLDataType.RESULT, "ref cursor");

    public static final DataType<String>       LONG                   = new DefaultDataType<String>(SQLDialect.ORACLE, SQLDataType.CLOB, "long");
    public static final DataType<byte[]>       RAW                    = new DefaultDataType<byte[]>(SQLDialect.ORACLE, SQLDataType.BLOB, "raw");
    public static final DataType<byte[]>       LONGRAW                = new DefaultDataType<byte[]>(SQLDialect.ORACLE, SQLDataType.BLOB, "longraw");
    public static final DataType<byte[]>       BFILE                  = new DefaultDataType<byte[]>(SQLDialect.ORACLE, SQLDataType.BLOB, "bfile");

    // PL/SQL data types
    public static final DataType<Integer>      BINARY_INTEGER         = new DefaultDataType<Integer>(SQLDialect.ORACLE, SQLDataType.INTEGER, "binary_integer");
    public static final DataType<Integer>      PLS_INTEGER            = new DefaultDataType<Integer>(SQLDialect.ORACLE, SQLDataType.INTEGER, "pls_integer");
    public static final DataType<Integer>      NATURAL                = new DefaultDataType<Integer>(SQLDialect.ORACLE, SQLDataType.INTEGER, "natural");
    public static final DataType<Integer>      NATURALN               = new DefaultDataType<Integer>(SQLDialect.ORACLE, SQLDataType.INTEGER, "naturaln");
    public static final DataType<Integer>      POSITIVE               = new DefaultDataType<Integer>(SQLDialect.ORACLE, SQLDataType.INTEGER, "positive");
    public static final DataType<Integer>      POSITIVEN              = new DefaultDataType<Integer>(SQLDialect.ORACLE, SQLDataType.INTEGER, "positiven");
    public static final DataType<Integer>      SIGNTYPE               = new DefaultDataType<Integer>(SQLDialect.ORACLE, SQLDataType.INTEGER, "signtype");
    public static final DataType<Double>       REAL                   = new DefaultDataType<Double>(SQLDialect.ORACLE, SQLDataType.DOUBLE, "real");
    public static final DataType<Double>       DOUBLE_PRECISION       = new DefaultDataType<Double>(SQLDialect.ORACLE, SQLDataType.DOUBLE, "double_precision");
    public static final DataType<Double>       BINARY_DOUBLE          = new DefaultDataType<Double>(SQLDialect.ORACLE, SQLDataType.DOUBLE, "binary_double");
    public static final DataType<BigDecimal>   FLOAT                  = new DefaultDataType<BigDecimal>(SQLDialect.ORACLE, SQLDataType.DECIMAL, "float");
    public static final DataType<BigDecimal>   BINARY_FLOAT           = new DefaultDataType<BigDecimal>(SQLDialect.ORACLE, SQLDataType.DECIMAL, "binary_float");
    public static final DataType<BigInteger>   INTEGER                = new DefaultDataType<BigInteger>(SQLDialect.ORACLE, SQLDataType.DECIMAL_INTEGER, "integer");
    public static final DataType<BigInteger>   INT                    = new DefaultDataType<BigInteger>(SQLDialect.ORACLE, SQLDataType.DECIMAL_INTEGER, "int");
    public static final DataType<BigInteger>   SMALLINT               = new DefaultDataType<BigInteger>(SQLDialect.ORACLE, SQLDataType.DECIMAL_INTEGER, "smallint");
    public static final DataType<Boolean>      BOOLEAN                = new DefaultDataType<Boolean>(SQLDialect.ORACLE, SQLDataType.BOOLEAN, "boolean");
}
