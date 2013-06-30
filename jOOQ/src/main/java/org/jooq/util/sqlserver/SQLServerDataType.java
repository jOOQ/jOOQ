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
package org.jooq.util.sqlserver;

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
 * Supported data types for the {@link SQLDialect#SQLSERVER} dialect family.
 * <p>
 * This includes the
 * <ul>
 * <li>{@link org.jooq.SQLDialect#SQLSERVER2008} dialect</li>
 * <li>{@link org.jooq.SQLDialect#SQLSERVER2012} dialect</li>
 * </ul>
 *
 * @author Lukas Eder
 * @see <a
 *      href="http://msdn.microsoft.com/en-us/library/aa258271%28v=sql.80%29.aspx">http://msdn.microsoft.com/en-us/library/aa258271%28v=sql.80%29.aspx</a>
 */
public class SQLServerDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<UByte>      TINYINT          = new DefaultDataType<UByte>(SQLDialect.SQLSERVER, SQLDataType.TINYINTUNSIGNED, "tinyint");
    public static final DataType<Short>      SMALLINT         = new DefaultDataType<Short>(SQLDialect.SQLSERVER, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>    INT              = new DefaultDataType<Integer>(SQLDialect.SQLSERVER, SQLDataType.INTEGER, "int");
    public static final DataType<Long>       BIGINT           = new DefaultDataType<Long>(SQLDialect.SQLSERVER, SQLDataType.BIGINT, "bigint");
    public static final DataType<Double>     FLOAT            = new DefaultDataType<Double>(SQLDialect.SQLSERVER, SQLDataType.FLOAT, "float");
    public static final DataType<Float>      REAL             = new DefaultDataType<Float>(SQLDialect.SQLSERVER, SQLDataType.REAL, "real");
    public static final DataType<BigDecimal> NUMERIC          = new DefaultDataType<BigDecimal>(SQLDialect.SQLSERVER, SQLDataType.NUMERIC, "numeric");
    public static final DataType<BigDecimal> DECIMAL          = new DefaultDataType<BigDecimal>(SQLDialect.SQLSERVER, SQLDataType.DECIMAL, "decimal");
    public static final DataType<Boolean>    BIT              = new DefaultDataType<Boolean>(SQLDialect.SQLSERVER, SQLDataType.BIT, "bit");
    public static final DataType<Date>       DATE             = new DefaultDataType<Date>(SQLDialect.SQLSERVER, SQLDataType.DATE, "date");
    public static final DataType<Timestamp>  DATETIME         = new DefaultDataType<Timestamp>(SQLDialect.SQLSERVER, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<Time>       TIME             = new DefaultDataType<Time>(SQLDialect.SQLSERVER, SQLDataType.TIME, "time");
    public static final DataType<String>     VARCHAR          = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     CHAR             = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.CHAR, "char");
    public static final DataType<String>     TEXT             = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.CLOB, "text");
    public static final DataType<String>     NVARCHAR         = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>     NCHAR            = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NTEXT            = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.NCLOB, "ntext");
    public static final DataType<byte[]>     VARBINARY        = new DefaultDataType<byte[]>(SQLDialect.SQLSERVER, SQLDataType.VARBINARY, "varbinary", "varbinary(max)");
    public static final DataType<byte[]>     BINARY           = new DefaultDataType<byte[]>(SQLDialect.SQLSERVER, SQLDataType.BINARY, "binary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.SQLSERVER, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>  __BLOB           = new DefaultDataType<byte[]>(SQLDialect.SQLSERVER, SQLDataType.BLOB, "binary");
    protected static final DataType<Boolean> __BOOLEAN        = new DefaultDataType<Boolean>(SQLDialect.SQLSERVER, SQLDataType.BOOLEAN, "bit");
    protected static final DataType<Double>  __DOUBLE         = new DefaultDataType<Double>(SQLDialect.SQLSERVER, SQLDataType.DOUBLE, "float");
    protected static final DataType<byte[]>  __LONGVARBINARY  = new DefaultDataType<byte[]>(SQLDialect.SQLSERVER, SQLDataType.LONGVARBINARY, "varbinary", "varbinary(max)");
    protected static final DataType<String>  __LONGVARCHAR    = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.LONGVARCHAR, "varchar");
    protected static final DataType<String>  __NCLOB          = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.NCLOB, "text");
    protected static final DataType<String>  __LONGNVARCHAR   = new DefaultDataType<String>(SQLDialect.SQLSERVER, SQLDataType.LONGNVARCHAR, "varchar");
    protected static final DataType<Byte>    __BYTE           = new DefaultDataType<Byte>(SQLDialect.SQLSERVER, SQLDataType.TINYINT, "signed tinyint", "tinyint");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER       = new DefaultDataType<BigInteger>(SQLDialect.SQLSERVER, SQLDataType.DECIMAL_INTEGER, "numeric");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED = new DefaultDataType<UShort>(SQLDialect.SQLSERVER, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED  = new DefaultDataType<UInteger>(SQLDialect.SQLSERVER, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED   = new DefaultDataType<ULong>(SQLDialect.SQLSERVER, SQLDataType.BIGINTUNSIGNED, "numeric");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Timestamp>  SMALLDATETIME    = new DefaultDataType<Timestamp>(SQLDialect.SQLSERVER, SQLDataType.TIMESTAMP, "smalldatetime");
    public static final DataType<Timestamp>  DATETIME2        = new DefaultDataType<Timestamp>(SQLDialect.SQLSERVER, SQLDataType.TIMESTAMP, "datetime2");
    public static final DataType<Timestamp>  DATETIMEOFFSET   = new DefaultDataType<Timestamp>(SQLDialect.SQLSERVER, SQLDataType.TIMESTAMP, "datetimeoffset");
    public static final DataType<BigDecimal> MONEY            = new DefaultDataType<BigDecimal>(SQLDialect.SQLSERVER, SQLDataType.DECIMAL, "money");
    public static final DataType<BigDecimal> SMALLMONEY       = new DefaultDataType<BigDecimal>(SQLDialect.SQLSERVER, SQLDataType.DECIMAL, "smallmoney");
    public static final DataType<byte[]>     IMAGE            = new DefaultDataType<byte[]>(SQLDialect.SQLSERVER, SQLDataType.BINARY, "image");
    public static final DataType<UUID>       UNIQUEIDENTIFIER = new DefaultDataType<UUID>(SQLDialect.SQLSERVER, SQLDataType.UUID, "uniqueidentifier");
    public static final DataType<Long>       ROWVERSION       = new DefaultDataType<Long>(SQLDialect.SQLSERVER, SQLDataType.BIGINT, "rowversion");
    public static final DataType<Long>       TIMESTAMP        = new DefaultDataType<Long>(SQLDialect.SQLSERVER, SQLDataType.BIGINT, "timestamp");
}
