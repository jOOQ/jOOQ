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

package org.jooq.util.mysql;

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
 * Supported data types for the {@link SQLDialect#MYSQL} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://dev.mysql.com/doc/refman/5.5/en/data-types.html">http://dev.mysql.com/doc/refman/5.5/en/data-types.html</a>
 * @see <a href="http://dev.mysql.com/doc/refman/5.5/en/cast-functions.html#function_cast">http://dev.mysql.com/doc/refman/5.5/en/cast-functions.html#function_cast</a>
 */
public class MySQLDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                     serialVersionUID = -5677365115109672781L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final MySQLDataType<Byte>       TINYINT          = new MySQLDataType<Byte>(SQLDataType.TINYINT, "tinyint", "signed");
    public static final MySQLDataType<Short>      SMALLINT         = new MySQLDataType<Short>(SQLDataType.SMALLINT, "smallint", "signed");
    public static final MySQLDataType<Integer>    INT              = new MySQLDataType<Integer>(SQLDataType.INTEGER, "int", "signed");
    public static final MySQLDataType<Integer>    MEDIUMINT        = new MySQLDataType<Integer>(SQLDataType.INTEGER, "mediumint", "signed");
    public static final MySQLDataType<Integer>    INTEGER          = new MySQLDataType<Integer>(SQLDataType.INTEGER, "integer", "signed");
    public static final MySQLDataType<Long>       BIGINT           = new MySQLDataType<Long>(SQLDataType.BIGINT, "bigint", "signed");
    public static final MySQLDataType<Double>     DOUBLE           = new MySQLDataType<Double>(SQLDataType.DOUBLE, "double", "decimal");
    public static final MySQLDataType<Double>     FLOAT            = new MySQLDataType<Double>(SQLDataType.FLOAT, "float", "decimal");
    public static final MySQLDataType<Float>      REAL             = new MySQLDataType<Float>(SQLDataType.REAL, "real", "decimal");
    public static final MySQLDataType<Boolean>    BOOLEAN          = new MySQLDataType<Boolean>(SQLDataType.BOOLEAN, "boolean", "unsigned");
    public static final MySQLDataType<Boolean>    BOOL             = new MySQLDataType<Boolean>(SQLDataType.BOOLEAN, "bool", "unsigned");
    public static final MySQLDataType<Boolean>    BIT              = new MySQLDataType<Boolean>(SQLDataType.BIT, "bit", "unsigned");
    public static final MySQLDataType<BigDecimal> DECIMAL          = new MySQLDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal", "decimal");
    public static final MySQLDataType<BigDecimal> DEC              = new MySQLDataType<BigDecimal>(SQLDataType.DECIMAL, "dec", "decimal");
    public static final MySQLDataType<String>     VARCHAR          = new MySQLDataType<String>(SQLDataType.VARCHAR, "varchar", "char");
    public static final MySQLDataType<String>     CHAR             = new MySQLDataType<String>(SQLDataType.CHAR, "char", "char");
    public static final MySQLDataType<String>     TEXT             = new MySQLDataType<String>(SQLDataType.CLOB, "text", "char");
    public static final MySQLDataType<byte[]>     BLOB             = new MySQLDataType<byte[]>(SQLDataType.BLOB, "blob", "binary");
    public static final MySQLDataType<byte[]>     BINARY           = new MySQLDataType<byte[]>(SQLDataType.BINARY, "binary", "binary");
    public static final MySQLDataType<byte[]>     VARBINARY        = new MySQLDataType<byte[]>(SQLDataType.VARBINARY, "varbinary", "binary");
    public static final MySQLDataType<Date>       DATE             = new MySQLDataType<Date>(SQLDataType.DATE, "date", "date");
    public static final MySQLDataType<Time>       TIME             = new MySQLDataType<Time>(SQLDataType.TIME, "time", "time");
    public static final MySQLDataType<Timestamp>  TIMESTAMP        = new MySQLDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp", "datetime");
    public static final MySQLDataType<Timestamp>  DATETIME         = new MySQLDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime", "datetime");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final MySQLDataType<String>     __NCHAR         = new MySQLDataType<String>(SQLDataType.NCHAR, "char", "char");
    protected static final MySQLDataType<String>     __NCLOB         = new MySQLDataType<String>(SQLDataType.NCLOB, "clob", "char");
    protected static final MySQLDataType<String>     __LONGNVARCHAR  = new MySQLDataType<String>(SQLDataType.LONGNVARCHAR, "varchar", "char");
    protected static final MySQLDataType<BigDecimal> __NUMERIC       = new MySQLDataType<BigDecimal>(SQLDataType.NUMERIC, "decimal", "decimal");
    protected static final MySQLDataType<String>     __NVARCHAR      = new MySQLDataType<String>(SQLDataType.NVARCHAR, "varchar", "char");
    protected static final MySQLDataType<String>     __LONGVARCHAR   = new MySQLDataType<String>(SQLDataType.LONGVARCHAR, "varchar", "char");
    protected static final MySQLDataType<byte[]>     __LONGVARBINARY = new MySQLDataType<byte[]>(SQLDataType.LONGVARBINARY, "varbinary", "binary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final MySQLDataType<BigInteger> __BIGINTEGER = new MySQLDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final MySQLDataType<String>     MEDIUMTEXT       = new MySQLDataType<String>(SQLDataType.CLOB, "mediumtext", "char");
    public static final MySQLDataType<String>     LONGTEXT         = new MySQLDataType<String>(SQLDataType.CLOB, "longtext", "char");
    public static final MySQLDataType<String>     SET              = new MySQLDataType<String>(SQLDataType.VARCHAR, "set", "char");
    public static final MySQLDataType<byte[]>     TINYBLOB         = new MySQLDataType<byte[]>(SQLDataType.BLOB, "tinyblob", "binary");
    public static final MySQLDataType<byte[]>     MEDIUMBLOB       = new MySQLDataType<byte[]>(SQLDataType.BLOB, "mediumblob", "binary");
    public static final MySQLDataType<byte[]>     LONGBLOB         = new MySQLDataType<byte[]>(SQLDataType.BLOB, "longblob", "binary");
    public static final MySQLDataType<Date>       YEAR             = new MySQLDataType<Date>(SQLDataType.DATE, "year", "date");


    private MySQLDataType(SQLDataType<T> sqlDataType, String typeName, String castTypeName) {
        super(SQLDialect.MYSQL, sqlDataType, sqlDataType.getType(), typeName, castTypeName);
    }

    /**
     * @deprecated - 1.6.3 - Do not reuse
     */
    @Deprecated
    public static DataType<?> getDataType(String typeName) {
        return getDataType(SQLDialect.MYSQL, typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.MYSQL, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.MYSQL, typeName);
    }
}
