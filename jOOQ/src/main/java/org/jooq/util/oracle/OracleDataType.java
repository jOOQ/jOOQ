/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice, *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
