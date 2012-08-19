/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.AbstractDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.types.DayToSecond;
import org.jooq.types.YearToMonth;

/**
 * Supported data types for the {@link SQLDialect#FIREBIRD} dialect
 *
 * @author Lukas Eder
 */
public class FirebirdDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                      serialVersionUID      = -5677365115109672781L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final FirebirdDataType<Short>       SMALLINT              = new FirebirdDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final FirebirdDataType<Integer>     INTEGER               = new FirebirdDataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final FirebirdDataType<Integer>     INT                   = new FirebirdDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final FirebirdDataType<Long>        INT64                 = new FirebirdDataType<Long>(SQLDataType.BIGINT, "int64");
    public static final FirebirdDataType<Long>        BIGINT                = new FirebirdDataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final FirebirdDataType<Double>      DOUBLEPRECISION       = new FirebirdDataType<Double>(SQLDataType.DOUBLE, "double precision");
    public static final FirebirdDataType<Float>       FLOAT                 = new FirebirdDataType<Float>(SQLDataType.REAL, "float");
    public static final FirebirdDataType<Boolean>     BOOLEAN               = new FirebirdDataType<Boolean>(SQLDataType.BOOLEAN, "boolean");
    public static final FirebirdDataType<BigDecimal>  DECIMAL               = new FirebirdDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final FirebirdDataType<BigDecimal>  NUMERIC               = new FirebirdDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric");
    public static final FirebirdDataType<String>      VARCHAR               = new FirebirdDataType<String>(SQLDataType.VARCHAR, "varchar", "varchar(32672)");
    public static final FirebirdDataType<String>      CHARACTERVARYING      = new FirebirdDataType<String>(SQLDataType.VARCHAR, "character varying", "varchar(32672)");
    public static final FirebirdDataType<String>      CHAR                  = new FirebirdDataType<String>(SQLDataType.CHAR, "char");
    public static final FirebirdDataType<String>      CHARACTER             = new FirebirdDataType<String>(SQLDataType.CHAR, "character");
    public static final FirebirdDataType<String>      CLOB                  = new FirebirdDataType<String>(SQLDataType.CLOB, "blob sub_type text");
    public static final FirebirdDataType<Date>        DATE                  = new FirebirdDataType<Date>(SQLDataType.DATE, "date");
    public static final FirebirdDataType<Time>        TIME                  = new FirebirdDataType<Time>(SQLDataType.TIME, "time");
    public static final FirebirdDataType<Timestamp>   TIMESTAMP             = new FirebirdDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp");
    public static final FirebirdDataType<byte[]>      BLOB                  = new FirebirdDataType<byte[]>(SQLDataType.BLOB, "blob");

    // TODO Below are HSQLDB data types. Fix this

    public static final FirebirdDataType<Byte>        TINYINT               = new FirebirdDataType<Byte>(SQLDataType.TINYINT, "tinyint");
    public static final FirebirdDataType<Boolean>     BIT                   = new FirebirdDataType<Boolean>(SQLDataType.BIT, "bit");
    public static final FirebirdDataType<Object>      OTHER                 = new FirebirdDataType<Object>(SQLDataType.OTHER, "other");
    public static final FirebirdDataType<YearToMonth> INTERVALYEARTOMONTH   = new FirebirdDataType<YearToMonth>(SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final FirebirdDataType<DayToSecond> INTERVALDAYTOSECOND   = new FirebirdDataType<DayToSecond>(SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final FirebirdDataType<String> __NCHAR                = new FirebirdDataType<String>(SQLDataType.NCHAR, "char");
    protected static final FirebirdDataType<String> __NCLOB                = new FirebirdDataType<String>(SQLDataType.NCLOB, "clob");
    protected static final FirebirdDataType<String> __LONGNVARCHAR         = new FirebirdDataType<String>(SQLDataType.LONGNVARCHAR, "longvarchar");
    protected static final FirebirdDataType<String> __NVARCHAR             = new FirebirdDataType<String>(SQLDataType.NVARCHAR, "varchar", "varchar(32672)");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final FirebirdDataType<BigInteger> __BIGINTEGER       = new FirebirdDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final FirebirdDataType<String>         VARCHARIGNORECASE = new FirebirdDataType<String>(SQLDataType.VARCHAR, "varchar_ignorecase", "varchar_ignorecase(32672)");
    public static final FirebirdDataType<Object>         OBJECT            = new FirebirdDataType<Object>(SQLDataType.OTHER, "object");
    public static final FirebirdDataType<Result<Record>> ROW               = new FirebirdDataType<Result<Record>>(SQLDataType.RESULT, "row");


    private FirebirdDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.FIREBIRD, sqlDataType, sqlDataType.getType(), typeName);
    }

    private FirebirdDataType(SQLDataType<T> sqlDataType, String typeName, String castTypeName) {
        super(SQLDialect.FIREBIRD, sqlDataType, sqlDataType.getType(), typeName, castTypeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.FIREBIRD, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.FIREBIRD, typeName);
    }
}
