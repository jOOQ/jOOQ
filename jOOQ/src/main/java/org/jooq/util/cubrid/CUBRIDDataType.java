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

package org.jooq.util.cubrid;

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
 * Supported data types for the {@link SQLDialect#CUBRID} dialect
 *
 * @author Lukas Eder
 */
public class CUBRIDDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Integer>    INT                      = new DefaultDataType<Integer>(SQLDialect.CUBRID, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER                  = new DefaultDataType<Integer>(SQLDialect.CUBRID, SQLDataType.INTEGER, "integer");
    public static final DataType<Short>      SHORT                    = new DefaultDataType<Short>(SQLDialect.CUBRID, SQLDataType.SMALLINT, "short");
    public static final DataType<Short>      SMALLINT                 = new DefaultDataType<Short>(SQLDialect.CUBRID, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Long>       BIGINT                   = new DefaultDataType<Long>(SQLDialect.CUBRID, SQLDataType.BIGINT, "bigint");
    public static final DataType<BigDecimal> DECIMAL                  = new DefaultDataType<BigDecimal>(SQLDialect.CUBRID, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> DEC                      = new DefaultDataType<BigDecimal>(SQLDialect.CUBRID, SQLDataType.DECIMAL, "dec");
    public static final DataType<BigDecimal> NUMERIC                  = new DefaultDataType<BigDecimal>(SQLDialect.CUBRID, SQLDataType.DECIMAL, "numeric");
    public static final DataType<Float>      FLOAT                    = new DefaultDataType<Float>(SQLDialect.CUBRID, SQLDataType.REAL, "float");
    public static final DataType<Float>      REAL                     = new DefaultDataType<Float>(SQLDialect.CUBRID, SQLDataType.REAL, "real");
    public static final DataType<Double>     DOUBLE                   = new DefaultDataType<Double>(SQLDialect.CUBRID, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     DOUBLEPRECISION          = new DefaultDataType<Double>(SQLDialect.CUBRID, SQLDataType.DOUBLE, "double precision");

    public static final DataType<String>     VARCHAR                  = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     CHARVARYING              = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.VARCHAR, "char varying");
    public static final DataType<String>     CHARACTERVARYING         = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.VARCHAR, "character varying");
    public static final DataType<String>     CHAR                     = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.CHAR, "char", "varchar");
    public static final DataType<String>     CHARACTER                = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.CHAR, "character", "varchar");
    public static final DataType<String>     STRING                   = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.VARCHAR, "string");
    public static final DataType<String>     NCHAR                    = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     CLOB                     = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.CLOB, "clob");

    public static final DataType<Date>       DATE                     = new DefaultDataType<Date>(SQLDialect.CUBRID, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                     = new DefaultDataType<Time>(SQLDialect.CUBRID, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  DATETIME                 = new DefaultDataType<Timestamp>(SQLDialect.CUBRID, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<Timestamp>  TIMESTAMP                = new DefaultDataType<Timestamp>(SQLDialect.CUBRID, SQLDataType.TIMESTAMP, "timestamp");

    public static final DataType<byte[]>     BITVARYING               = new DefaultDataType<byte[]>(SQLDialect.CUBRID, SQLDataType.VARBINARY, "bit varying");
    public static final DataType<byte[]>     VARBIT                   = new DefaultDataType<byte[]>(SQLDialect.CUBRID, SQLDataType.VARBINARY, "varbit");
    public static final DataType<byte[]>     BIT                      = new DefaultDataType<byte[]>(SQLDialect.CUBRID, SQLDataType.BINARY, "bit");
    public static final DataType<byte[]>     BLOB                     = new DefaultDataType<byte[]>(SQLDialect.CUBRID, SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.CUBRID, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<Boolean>    __BOOL                = new DefaultDataType<Boolean>(SQLDialect.CUBRID, SQLDataType.BOOLEAN, "bit", "bit(1)");
    protected static final DataType<Boolean>    __BIT                 = new DefaultDataType<Boolean>(SQLDialect.CUBRID, SQLDataType.BIT, "bit", "bit(1)");
    protected static final DataType<String>     __LONGNVARCHAR        = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.LONGNVARCHAR, "varchar");
    protected static final DataType<String>     __NCLOB               = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>     __NVARCHAR            = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.NVARCHAR, "varchar");
    protected static final DataType<String>     __LONGVARCHAR         = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.LONGVARCHAR, "varchar");
    protected static final DataType<byte[]>     __LONGVARBINARY       = new DefaultDataType<byte[]>(SQLDialect.CUBRID, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<Byte>       __TINYINT             = new DefaultDataType<Byte>(SQLDialect.CUBRID, SQLDataType.TINYINT, "smallint");
    protected static final DataType<Double>     __FLOAT               = new DefaultDataType<Double>(SQLDialect.CUBRID, SQLDataType.DOUBLE, "double");
    protected static final DataType<BigDecimal> __NUMERIC             = new DefaultDataType<BigDecimal>(SQLDialect.CUBRID, SQLDataType.NUMERIC, "decimal");
    protected static final DataType<UByte>      __TINYINTUNSIGNED     = new DefaultDataType<UByte>(SQLDialect.CUBRID, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED    = new DefaultDataType<UShort>(SQLDialect.CUBRID, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED     = new DefaultDataType<UInteger>(SQLDialect.CUBRID, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED      = new DefaultDataType<ULong>(SQLDialect.CUBRID, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER          = new DefaultDataType<BigInteger>(SQLDialect.CUBRID, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
    protected static final DataType<UUID>       __UUID                = new DefaultDataType<UUID>(SQLDialect.CUBRID, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Double> MONETARY                     = new DefaultDataType<Double>(SQLDialect.CUBRID, SQLDataType.DOUBLE, "monetary");
    public static final DataType<String> ENUM                         = new DefaultDataType<String>(SQLDialect.CUBRID, SQLDataType.VARCHAR, "enum", "varchar");

    // These types are not yet formally supported
    public static final DataType<Object> OBJECT                       = new DefaultDataType<Object>(SQLDialect.CUBRID, SQLDataType.OTHER, "object");
    public static final DataType<Object> OID                          = new DefaultDataType<Object>(SQLDialect.CUBRID, SQLDataType.OTHER, "oid");
    public static final DataType<Object> ELO                          = new DefaultDataType<Object>(SQLDialect.CUBRID, SQLDataType.OTHER, "elo");
    public static final DataType<Object> MULTISET                     = new DefaultDataType<Object>(SQLDialect.CUBRID, SQLDataType.OTHER, "multiset");
    public static final DataType<Object> SEQUENCE                     = new DefaultDataType<Object>(SQLDialect.CUBRID, SQLDataType.OTHER, "sequence");
    public static final DataType<Object> SET                          = new DefaultDataType<Object>(SQLDialect.CUBRID, SQLDataType.OTHER, "set");
}
