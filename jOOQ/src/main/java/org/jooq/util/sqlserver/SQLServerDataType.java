/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.impl.AbstractDataType;
import org.jooq.impl.SQLDataType;

/**
 * Supported data types for the {@link SQLDialect#SQLSERVER} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://msdn.microsoft.com/en-us/library/aa258271%28v=sql.80%29.aspx">http://msdn.microsoft.com/en-us/library/aa258271%28v=sql.80%29.aspx</a>
 */
public class SQLServerDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                         serialVersionUID = -5677365115109672781L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final SQLServerDataType<Byte>       TINYINT          = new SQLServerDataType<Byte>(SQLDataType.TINYINT, "tinyint");
    public static final SQLServerDataType<Short>      SMALLINT         = new SQLServerDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final SQLServerDataType<Integer>    INT              = new SQLServerDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final SQLServerDataType<Long>       BIGINT           = new SQLServerDataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final SQLServerDataType<Double>     FLOAT            = new SQLServerDataType<Double>(SQLDataType.FLOAT, "float");
    public static final SQLServerDataType<Float>      REAL             = new SQLServerDataType<Float>(SQLDataType.REAL, "real");
    public static final SQLServerDataType<BigDecimal> NUMERIC          = new SQLServerDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric");
    public static final SQLServerDataType<BigDecimal> DECIMAL          = new SQLServerDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final SQLServerDataType<Boolean>    BIT              = new SQLServerDataType<Boolean>(SQLDataType.BIT, "bit");
    public static final SQLServerDataType<Date>       DATE             = new SQLServerDataType<Date>(SQLDataType.DATE, "date");
    public static final SQLServerDataType<Timestamp>  DATETIME         = new SQLServerDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime");
    public static final SQLServerDataType<Time>       TIME             = new SQLServerDataType<Time>(SQLDataType.TIME, "time");
    public static final SQLServerDataType<String>     VARCHAR          = new SQLServerDataType<String>(SQLDataType.VARCHAR, "varchar");
    public static final SQLServerDataType<String>     CHAR             = new SQLServerDataType<String>(SQLDataType.CHAR, "char");
    public static final SQLServerDataType<String>     TEXT             = new SQLServerDataType<String>(SQLDataType.CLOB, "text");
    public static final SQLServerDataType<String>     NVARCHAR         = new SQLServerDataType<String>(SQLDataType.NVARCHAR, "nvarchar");
    public static final SQLServerDataType<String>     NCHAR            = new SQLServerDataType<String>(SQLDataType.NCHAR, "nchar");
    public static final SQLServerDataType<String>     NTEXT            = new SQLServerDataType<String>(SQLDataType.NCLOB, "ntext");
    public static final SQLServerDataType<byte[]>     VARBINARY        = new SQLServerDataType<byte[]>(SQLDataType.VARBINARY, "varbinary", "varbinary(max)");
    public static final SQLServerDataType<byte[]>     BINARY           = new SQLServerDataType<byte[]>(SQLDataType.BINARY, "binary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final SQLServerDataType<byte[]>  __BLOB           = new SQLServerDataType<byte[]>(SQLDataType.BLOB, "binary");
    protected static final SQLServerDataType<Boolean> __BOOLEAN        = new SQLServerDataType<Boolean>(SQLDataType.BOOLEAN, "bit");
    protected static final SQLServerDataType<Double>  __DOUBLE         = new SQLServerDataType<Double>(SQLDataType.DOUBLE, "float");
    protected static final SQLServerDataType<byte[]>  __LONGVARBINARY  = new SQLServerDataType<byte[]>(SQLDataType.LONGVARBINARY, "varbinary", "varbinary(max)");
    protected static final SQLServerDataType<String>  __LONGVARCHAR    = new SQLServerDataType<String>(SQLDataType.LONGVARCHAR, "varchar");
    protected static final SQLServerDataType<String>  __NCLOB          = new SQLServerDataType<String>(SQLDataType.NCLOB, "text");
    protected static final SQLServerDataType<String>  __LONGNVARCHAR   = new SQLServerDataType<String>(SQLDataType.LONGNVARCHAR, "varchar");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final SQLServerDataType<BigInteger> __BIGINTEGER  = new SQLServerDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "numeric");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final SQLServerDataType<Timestamp>  SMALLDATETIME    = new SQLServerDataType<Timestamp>(SQLDataType.TIMESTAMP, "smalldatetime");
    public static final SQLServerDataType<Timestamp>  DATETIME2        = new SQLServerDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime2");
    public static final SQLServerDataType<Timestamp>  DATETIMEOFFSET   = new SQLServerDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetimeoffset");
    public static final SQLServerDataType<BigDecimal> MONEY            = new SQLServerDataType<BigDecimal>(SQLDataType.DECIMAL, "money");
    public static final SQLServerDataType<BigDecimal> SMALLMONEY       = new SQLServerDataType<BigDecimal>(SQLDataType.DECIMAL, "smallmoney");
    public static final SQLServerDataType<byte[]>     IMAGE            = new SQLServerDataType<byte[]>(SQLDataType.BINARY, "image");
    public static final SQLServerDataType<String>     UNIQUEIDENTIFIER = new SQLServerDataType<String>(SQLDataType.VARCHAR, "uniqueidentifier");


    private SQLServerDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.SQLSERVER, sqlDataType, sqlDataType.getType(), typeName);
    }

    private SQLServerDataType(SQLDataType<T> sqlDataType, String typeName, String castTypeName) {
        super(SQLDialect.SQLSERVER, sqlDataType, sqlDataType.getType(), typeName, castTypeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.SQLSERVER, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.SQLSERVER, typeName);
    }
}
