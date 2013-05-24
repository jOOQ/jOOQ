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

package org.jooq.util.ingres;

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
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

/**
 * Supported data types for the {@link SQLDialect#INGRES} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://docs.ingres.com/ingres/9.3/quel-reference-guide/1240-data-types">http://docs.ingres.com/ingres/9.3/quel-reference-guide/1240-data-types</a>
 * @see <a href="http://docs.ingres.com/ingres/10.0/sql-reference-guide/2638-storage-formats-of-data-types">http://docs.ingres.com/ingres/10.0/sql-reference-guide/2638-storage-formats-of-data-types</a>
 */
public class IngresDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>       TINYINT                      = new DefaultDataType<Byte>(SQLDialect.INGRES, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Byte>       INTEGER1                     = new DefaultDataType<Byte>(SQLDialect.INGRES, SQLDataType.TINYINT, "integer1");
    public static final DataType<Byte>       I1                           = new DefaultDataType<Byte>(SQLDialect.INGRES, SQLDataType.TINYINT, "i1");
    public static final DataType<Short>      SMALLINT                     = new DefaultDataType<Short>(SQLDialect.INGRES, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>      INTEGER2                     = new DefaultDataType<Short>(SQLDialect.INGRES, SQLDataType.SMALLINT, "integer2");
    public static final DataType<Short>      I2                           = new DefaultDataType<Short>(SQLDialect.INGRES, SQLDataType.SMALLINT, "i2");
    public static final DataType<Integer>    INTEGER                      = new DefaultDataType<Integer>(SQLDialect.INGRES, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>    INTEGER4                     = new DefaultDataType<Integer>(SQLDialect.INGRES, SQLDataType.INTEGER, "integer4");
    public static final DataType<Integer>    I4                           = new DefaultDataType<Integer>(SQLDialect.INGRES, SQLDataType.INTEGER, "i4");
    public static final DataType<Long>       BIGINT                       = new DefaultDataType<Long>(SQLDialect.INGRES, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>       INTEGER8                     = new DefaultDataType<Long>(SQLDialect.INGRES, SQLDataType.BIGINT, "integer8");
    public static final DataType<Long>       I8                           = new DefaultDataType<Long>(SQLDialect.INGRES, SQLDataType.BIGINT, "i8");
    public static final DataType<Double>     FLOAT                        = new DefaultDataType<Double>(SQLDialect.INGRES, SQLDataType.FLOAT, "float");
    public static final DataType<Double>     FLOAT8                       = new DefaultDataType<Double>(SQLDialect.INGRES, SQLDataType.DOUBLE, "float8");
    public static final DataType<Double>     DOUBLEPRECISION              = new DefaultDataType<Double>(SQLDialect.INGRES, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Float>      REAL                         = new DefaultDataType<Float>(SQLDialect.INGRES, SQLDataType.REAL, "real");
    public static final DataType<Float>      FLOAT4                       = new DefaultDataType<Float>(SQLDialect.INGRES, SQLDataType.REAL, "float4");
    public static final DataType<Boolean>    BOOLEAN                      = new DefaultDataType<Boolean>(SQLDialect.INGRES, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<BigDecimal> DECIMAL                      = new DefaultDataType<BigDecimal>(SQLDialect.INGRES, SQLDataType.DECIMAL, "decimal", "decimal(38, 19)");
    public static final DataType<String>     VARCHAR                      = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     CHARACTERVARYING             = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.VARCHAR, "character varying");
    public static final DataType<String>     LONGVARCHAR                  = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.LONGVARCHAR, "long varchar");
    public static final DataType<String>     CHAR                         = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CHAR, "char");
    public static final DataType<String>     CHARACTER                    = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CHAR, "character");
    public static final DataType<String>     C                            = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CHAR, "c");
    public static final DataType<String>     CLOB                         = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CLOB, "clob");
    public static final DataType<String>     CHARLARGEOBJECT              = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CLOB, "char large object");
    public static final DataType<String>     CHARACTERLARGEOBJECT         = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CLOB, "character large object");
    public static final DataType<String>     NVARCHAR                     = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>     LONGNVARCHAR                 = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.LONGNVARCHAR, "long nvarchar");
    public static final DataType<String>     NCHAR                        = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NCLOB                        = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NCLOB, "nclob");
    public static final DataType<String>     NCHARLARGEOBJECT             = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NCLOB, "nchar large object");
    public static final DataType<String>     NATIONALCHARACTERLARGEOBJECT = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NCLOB, "national character large object");
    public static final DataType<Date>       DATE                         = new DefaultDataType<Date>(SQLDialect.INGRES, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                         = new DefaultDataType<Time>(SQLDialect.INGRES, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  TIMESTAMP                    = new DefaultDataType<Timestamp>(SQLDialect.INGRES, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>     BLOB                         = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>     BINARYLARGEOBJECT            = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.BLOB, "binary large object");
    public static final DataType<byte[]>     BINARY                       = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.BINARY, "binary");
    public static final DataType<byte[]>     VARBINARY                    = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.VARBINARY, "varbinary");
    public static final DataType<byte[]>     LONGBYTE                     = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.LONGVARBINARY, "long byte");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.INGRES, SQLDataTypes
    // -------------------------------------------------------------------------

    public static final DataType<Boolean>     BIT                         = new DefaultDataType<Boolean>(SQLDialect.INGRES, SQLDataType.BIT, "boolean");
    public static final DataType<BigDecimal>  NUMERIC                     = new DefaultDataType<BigDecimal>(SQLDialect.INGRES, SQLDataType.NUMERIC, "decimal", "decimal(38, 19)");
    protected static final DataType<UByte>    __TINYINTUNSIGNED           = new DefaultDataType<UByte>(SQLDialect.INGRES, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED          = new DefaultDataType<UShort>(SQLDialect.INGRES, SQLDataType.SMALLINTUNSIGNED, "integer");
    protected static final DataType<UInteger> __INTEGERUNSIGNED           = new DefaultDataType<UInteger>(SQLDialect.INGRES, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED            = new DefaultDataType<ULong>(SQLDialect.INGRES, SQLDataType.BIGINTUNSIGNED, "decimal", "decimal(20)");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER              = new DefaultDataType<BigInteger>(SQLDialect.INGRES, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal(38, 0)");
    protected static final DataType<UUID>       __UUID                    = new DefaultDataType<UUID>(SQLDialect.INGRES, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<String>     TEXT                         = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CLOB, "text");
    public static final DataType<Date>       ANSIDATE                     = new DefaultDataType<Date>(SQLDialect.INGRES, SQLDataType.DATE, "ansidate");
    public static final DataType<Date>       INGRESDATE                   = new DefaultDataType<Date>(SQLDialect.INGRES, SQLDataType.DATE, "ingresdate");
    public static final DataType<Time>       TIMEWITHTIMEZONE             = new DefaultDataType<Time>(SQLDialect.INGRES, SQLDataType.TIME, "time with time zone");
    public static final DataType<Time>       TIMEWITHOUTTIMEZONE          = new DefaultDataType<Time>(SQLDialect.INGRES, SQLDataType.TIME, "time without time zone");
    public static final DataType<Time>       TIMEWITHLOCALTIMEZONE        = new DefaultDataType<Time>(SQLDialect.INGRES, SQLDataType.TIME, "time with local time zone");
    public static final DataType<Timestamp>  TIMESTAMPWITHTIMEZONE        = new DefaultDataType<Timestamp>(SQLDialect.INGRES, SQLDataType.TIMESTAMP, "timestamp with time zone");
    public static final DataType<Timestamp>  TIMESTAMPWITHOUTTIMEZONE     = new DefaultDataType<Timestamp>(SQLDialect.INGRES, SQLDataType.TIMESTAMP, "timestamp without time zone");
    public static final DataType<Timestamp>  TIMESTAMPWITHLOCALTIMEZONE   = new DefaultDataType<Timestamp>(SQLDialect.INGRES, SQLDataType.TIMESTAMP, "timestamp with local time zone");
    public static final DataType<byte[]>     BYTE                         = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.BINARY, "byte");
    public static final DataType<byte[]>     VARBYTE                      = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.VARBINARY, "varbyte");
}
