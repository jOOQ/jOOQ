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

package org.jooq.util.postgres;

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
 * Supported data types for the {@link SQLDialect#POSTGRES} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://www.postgresql.org/docs/9.0/interactive/datatype.html">http://www.postgresql.org/docs/9.0/interactive/datatype.html</a>
 */
public class PostgresDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                        serialVersionUID         = -5677365115109672781L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final PostgresDataType<Short>        SMALLINT                 = new PostgresDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final PostgresDataType<Short>        INT2                     = new PostgresDataType<Short>(SQLDataType.SMALLINT, "int2");
    public static final PostgresDataType<Integer>      INT                      = new PostgresDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final PostgresDataType<Integer>      INTEGER                  = new PostgresDataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final PostgresDataType<Integer>      INT4                     = new PostgresDataType<Integer>(SQLDataType.INTEGER, "int4");
    public static final PostgresDataType<Long>         BIGINT                   = new PostgresDataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final PostgresDataType<Long>         INT8                     = new PostgresDataType<Long>(SQLDataType.BIGINT, "int8");
    public static final PostgresDataType<Double>       DOUBLEPRECISION          = new PostgresDataType<Double>(SQLDataType.DOUBLE, "double precision");
    public static final PostgresDataType<Double>       FLOAT8                   = new PostgresDataType<Double>(SQLDataType.FLOAT, "float8");
    public static final PostgresDataType<Float>        REAL                     = new PostgresDataType<Float>(SQLDataType.REAL, "real");
    public static final PostgresDataType<Float>        FLOAT4                   = new PostgresDataType<Float>(SQLDataType.REAL, "float4");
    public static final PostgresDataType<Boolean>      BOOLEAN                  = new PostgresDataType<Boolean>(SQLDataType.BOOLEAN, "boolean");
    public static final PostgresDataType<Boolean>      BOOL                     = new PostgresDataType<Boolean>(SQLDataType.BOOLEAN, "bool");
    public static final PostgresDataType<BigDecimal>   NUMERIC                  = new PostgresDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric");
    public static final PostgresDataType<BigDecimal>   DECIMAL                  = new PostgresDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final PostgresDataType<String>       VARCHAR                  = new PostgresDataType<String>(SQLDataType.VARCHAR, "varchar");
    public static final PostgresDataType<String>       CHARACTERVARYING         = new PostgresDataType<String>(SQLDataType.VARCHAR, "character varying");
    public static final PostgresDataType<String>       CHAR                     = new PostgresDataType<String>(SQLDataType.CHAR, "char");
    public static final PostgresDataType<String>       CHARACTER                = new PostgresDataType<String>(SQLDataType.CHAR, "character");
    public static final PostgresDataType<String>       TEXT                     = new PostgresDataType<String>(SQLDataType.CLOB, "text");
    public static final PostgresDataType<Date>         DATE                     = new PostgresDataType<Date>(SQLDataType.DATE, "date");
    public static final PostgresDataType<Time>         TIME                     = new PostgresDataType<Time>(SQLDataType.TIME, "time");
    public static final PostgresDataType<Timestamp>    TIMESTAMP                = new PostgresDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp");
    public static final PostgresDataType<byte[]>       BYTEA                    = new PostgresDataType<byte[]>(SQLDataType.BLOB, "bytea");
    public static final PostgresDataType<YearToMonth>  INTERVALYEARTOMONTH      = new PostgresDataType<YearToMonth>(SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final PostgresDataType<DayToSecond>  INTERVALDAYTOSECOND      = new PostgresDataType<DayToSecond>(SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final PostgresDataType<byte[]>     __BINARY                = new PostgresDataType<byte[]>(SQLDataType.BINARY, "bytea");
    protected static final PostgresDataType<Boolean>    __BIT                   = new PostgresDataType<Boolean>(SQLDataType.BIT, "boolean");
    protected static final PostgresDataType<byte[]>     __LONGVARBINARY         = new PostgresDataType<byte[]>(SQLDataType.LONGVARBINARY, "bytea");
    protected static final PostgresDataType<String>     __LONGVARCHAR           = new PostgresDataType<String>(SQLDataType.LONGVARCHAR, "varchar");
    protected static final PostgresDataType<String>     __NCHAR                 = new PostgresDataType<String>(SQLDataType.NCHAR, "char");
    protected static final PostgresDataType<String>     __NCLOB                 = new PostgresDataType<String>(SQLDataType.NCLOB, "text");
    protected static final PostgresDataType<String>     __LONGNVARCHAR          = new PostgresDataType<String>(SQLDataType.LONGNVARCHAR, "varchar");
    protected static final PostgresDataType<String>     __NVARCHAR              = new PostgresDataType<String>(SQLDataType.NVARCHAR, "varchar");
    protected static final PostgresDataType<Byte>       __TINYINT               = new PostgresDataType<Byte>(SQLDataType.TINYINT, "smallint");
    protected static final PostgresDataType<byte[]>     __VARBINARY             = new PostgresDataType<byte[]>(SQLDataType.VARBINARY, "bytea");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final PostgresDataType<BigInteger> __BIGINTEGER            = new PostgresDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final PostgresDataType<Integer>    SERIAL                     = new PostgresDataType<Integer>(SQLDataType.INTEGER, "serial");
    public static final PostgresDataType<Integer>    SERIAL4                    = new PostgresDataType<Integer>(SQLDataType.INTEGER, "serial4");
    public static final PostgresDataType<Long>       BIGSERIAL                  = new PostgresDataType<Long>(SQLDataType.BIGINT, "bigserial");
    public static final PostgresDataType<Long>       SERIAL8                    = new PostgresDataType<Long>(SQLDataType.BIGINT, "serial8");
    public static final PostgresDataType<BigDecimal> MONEY                      = new PostgresDataType<BigDecimal>(SQLDataType.DECIMAL, "money");
    public static final PostgresDataType<String>     BITVARYING                 = new PostgresDataType<String>(SQLDataType.VARCHAR, "bit varying");
    public static final PostgresDataType<String>     VARBIT                     = new PostgresDataType<String>(SQLDataType.VARCHAR, "varbit");
    public static final PostgresDataType<String>     BIT                        = new PostgresDataType<String>(SQLDataType.CHAR, "bit");
    public static final PostgresDataType<String>     BPCHAR                     = new PostgresDataType<String>(SQLDataType.CHAR, "bpchar");
    public static final PostgresDataType<Time>       TIMEWITHOUTTIMEZONE        = new PostgresDataType<Time>(SQLDataType.TIME, "time without time zone");
    public static final PostgresDataType<Time>       TIMEWITHTIMEZONE           = new PostgresDataType<Time>(SQLDataType.TIME, "time with time zone");
    public static final PostgresDataType<Time>       TIMETZ                     = new PostgresDataType<Time>(SQLDataType.TIME, "timetz");
    public static final PostgresDataType<Timestamp>  TIMESTAMPWITHOUTTIMEZONE   = new PostgresDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp without time zone");
    public static final PostgresDataType<Timestamp>  TIMESTAMPWITHTIMEZONE      = new PostgresDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp with time zone");
    public static final PostgresDataType<Timestamp>  TIMESTAMPTZ                = new PostgresDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamptz");
    public static final PostgresDataType<Result<Record>> REFCURSOR              = new PostgresDataType<Result<Record>>(SQLDataType.RESULT, "refcursor");

    // Meta-table types
    public static final PostgresDataType<Long>       OID                        = new PostgresDataType<Long>(SQLDataType.BIGINT, "oid");
    public static final PostgresDataType<Long>       OIDVECTOR                  = new PostgresDataType<Long>(SQLDataType.BIGINT, "oidvector");
    public static final PostgresDataType<Long>       XID                        = new PostgresDataType<Long>(SQLDataType.BIGINT, "xid");
    public static final PostgresDataType<Long>       TID                        = new PostgresDataType<Long>(SQLDataType.BIGINT, "tid");
    public static final PostgresDataType<Long>       CID                        = new PostgresDataType<Long>(SQLDataType.BIGINT, "cid");
    public static final PostgresDataType<String>     ACLITEM                    = new PostgresDataType<String>(SQLDataType.VARCHAR, "aclitem");
    public static final PostgresDataType<String>     NAME                       = new PostgresDataType<String>(SQLDataType.VARCHAR, "name");
    public static final PostgresDataType<String>     REGPROC                    = new PostgresDataType<String>(SQLDataType.VARCHAR, "regproc");


    private PostgresDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.POSTGRES, sqlDataType, sqlDataType.getType(), typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.POSTGRES, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.POSTGRES, typeName);
    }
}
