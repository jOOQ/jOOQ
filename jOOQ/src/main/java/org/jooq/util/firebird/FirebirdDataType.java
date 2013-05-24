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
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
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

package org.jooq.util.firebird;

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
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;

/**
 * Supported data types for the {@link SQLDialect#FIREBIRD} dialect
 *
 * @author Lukas Eder
 */
public class FirebirdDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>       SMALLINT              = new DefaultDataType<Short>(SQLDialect.FIREBIRD, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>     INTEGER               = new DefaultDataType<Integer>(SQLDialect.FIREBIRD, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>     INT                   = new DefaultDataType<Integer>(SQLDialect.FIREBIRD, SQLDataType.INTEGER, "int");
    public static final DataType<Long>        BIGINT                = new DefaultDataType<Long>(SQLDialect.FIREBIRD, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>        INT64                 = new DefaultDataType<Long>(SQLDialect.FIREBIRD, SQLDataType.BIGINT, "int64");
    public static final DataType<Double>      DOUBLEPRECISION       = new DefaultDataType<Double>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>      DOUBLE                = new DefaultDataType<Double>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>      D_FLOAT               = new DefaultDataType<Double>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "d_float");
    public static final DataType<Float>       FLOAT                 = new DefaultDataType<Float>(SQLDialect.FIREBIRD, SQLDataType.REAL, "float");
    public static final DataType<Boolean>     BOOLEAN               = new DefaultDataType<Boolean>(SQLDialect.FIREBIRD, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<BigDecimal>  DECIMAL               = new DefaultDataType<BigDecimal>(SQLDialect.FIREBIRD, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal>  NUMERIC               = new DefaultDataType<BigDecimal>(SQLDialect.FIREBIRD, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>      VARCHAR               = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.VARCHAR, "varchar", "varchar(4000)");
    public static final DataType<String>      CHARACTERVARYING      = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.VARCHAR, "character varying", "varchar(4000)");
    public static final DataType<String>      CHAR                  = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.CHAR, "char");
    public static final DataType<String>      CHARACTER             = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.CHAR, "character");
    public static final DataType<String>      CLOB                  = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.CLOB, "blob sub_type text");
    public static final DataType<Date>        DATE                  = new DefaultDataType<Date>(SQLDialect.FIREBIRD, SQLDataType.DATE, "date");
    public static final DataType<Time>        TIME                  = new DefaultDataType<Time>(SQLDialect.FIREBIRD, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>   TIMESTAMP             = new DefaultDataType<Timestamp>(SQLDialect.FIREBIRD, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>      BLOB                  = new DefaultDataType<byte[]>(SQLDialect.FIREBIRD, SQLDataType.BLOB, "blob");

    // TODO Below are HSQLDB data types. Fix this

    public static final DataType<Boolean>     BIT                   = new DefaultDataType<Boolean>(SQLDialect.FIREBIRD, SQLDataType.BIT, "bit");
    public static final DataType<Object>      OTHER                 = new DefaultDataType<Object>(SQLDialect.FIREBIRD, SQLDataType.OTHER, "other");
    public static final DataType<YearToMonth> INTERVALYEARTOMONTH   = new DefaultDataType<YearToMonth>(SQLDialect.FIREBIRD, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond> INTERVALDAYTOSECOND   = new DefaultDataType<DayToSecond>(SQLDialect.FIREBIRD, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.FIREBIRD, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>   __BINARY              = new DefaultDataType<byte[]>(SQLDialect.FIREBIRD, SQLDataType.BINARY, "blob");
    protected static final DataType<Double>   __FLOAT               = new DefaultDataType<Double>(SQLDialect.FIREBIRD, SQLDataType.FLOAT, "double precision");
    protected static final DataType<String>   __LONGNVARCHAR        = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.LONGNVARCHAR, "blob sub_type text");
    protected static final DataType<byte[]>   __LONGVARBINARY       = new DefaultDataType<byte[]>(SQLDialect.FIREBIRD, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<String>   __LONGVARCHAR         = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.LONGVARCHAR, "varchar", "varchar(4000)");
    protected static final DataType<String>   __NCHAR               = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.NCHAR, "char");
    protected static final DataType<String>   __NCLOB               = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>   __NVARCHAR            = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.NVARCHAR, "varchar", "varchar(4000)");
    protected static final DataType<Byte>     __TINYINT             = new DefaultDataType<Byte>(SQLDialect.FIREBIRD, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>   __VARBINARY           = new DefaultDataType<byte[]>(SQLDialect.FIREBIRD, SQLDataType.VARBINARY, "blob");
    protected static final DataType<UByte>    __TINYINTUNSIGNED     = new DefaultDataType<UByte>(SQLDialect.FIREBIRD, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED    = new DefaultDataType<UShort>(SQLDialect.FIREBIRD, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED     = new DefaultDataType<UInteger>(SQLDialect.FIREBIRD, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED      = new DefaultDataType<ULong>(SQLDialect.FIREBIRD, SQLDataType.BIGINTUNSIGNED, "decimal", "varchar(20)"); // There are no large numbers in firebird...?

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER        = new DefaultDataType<BigInteger>(SQLDialect.FIREBIRD, SQLDataType.DECIMAL_INTEGER, "decimal");
    protected static final DataType<UUID>       __UUID              = new DefaultDataType<UUID>(SQLDialect.FIREBIRD, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------
}
