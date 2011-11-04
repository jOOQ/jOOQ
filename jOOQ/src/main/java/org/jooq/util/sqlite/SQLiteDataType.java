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
package org.jooq.util.sqlite;

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
 * Supported data types for the {@link SQLDialect#SQLITE} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://www.sqlite.org/datatype3.html">http://www.sqlite.org/datatype3.html</a>
 */
public class SQLiteDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                      serialVersionUID = -5677365115109672781L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final SQLiteDataType<Byte>       TINYINT          = new SQLiteDataType<Byte>(SQLDataType.TINYINT, "tinyint");
    public static final SQLiteDataType<Short>      SMALLINT         = new SQLiteDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final SQLiteDataType<Short>      INT2             = new SQLiteDataType<Short>(SQLDataType.SMALLINT, "int2");
    public static final SQLiteDataType<Integer>    INT              = new SQLiteDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final SQLiteDataType<Integer>    INTEGER          = new SQLiteDataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final SQLiteDataType<Integer>    MEDIUMINT        = new SQLiteDataType<Integer>(SQLDataType.INTEGER, "mediumint");
    public static final SQLiteDataType<Long>       INT8             = new SQLiteDataType<Long>(SQLDataType.BIGINT, "int8");
    public static final SQLiteDataType<BigInteger> BIGINT           = new SQLiteDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "bigint");
    public static final SQLiteDataType<BigInteger> UNSIGNEDBIGINT   = new SQLiteDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "unsigned big int");
    public static final SQLiteDataType<Double>     DOUBLE           = new SQLiteDataType<Double>(SQLDataType.DOUBLE, "double");
    public static final SQLiteDataType<Double>     DOUBLEPRECISION  = new SQLiteDataType<Double>(SQLDataType.DOUBLE, "double precision");
    public static final SQLiteDataType<Float>      REAL             = new SQLiteDataType<Float>(SQLDataType.REAL, "real");
    public static final SQLiteDataType<Float>      FLOAT            = new SQLiteDataType<Float>(SQLDataType.REAL, "float");
    public static final SQLiteDataType<BigDecimal> NUMERIC          = new SQLiteDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric");
    public static final SQLiteDataType<BigDecimal> DECIMAL          = new SQLiteDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final SQLiteDataType<String>     LONGVARCHAR      = new SQLiteDataType<String>(SQLDataType.LONGVARCHAR, "longvarchar");
    public static final SQLiteDataType<String>     CHAR             = new SQLiteDataType<String>(SQLDataType.CHAR, "char");
    public static final SQLiteDataType<String>     CHARACTER        = new SQLiteDataType<String>(SQLDataType.CHAR, "character");
    public static final SQLiteDataType<String>     VARCHAR          = new SQLiteDataType<String>(SQLDataType.VARCHAR, "varchar");
    public static final SQLiteDataType<String>     VARYINGCHARACTER = new SQLiteDataType<String>(SQLDataType.VARCHAR, "varying character");
    public static final SQLiteDataType<String>     NCHAR            = new SQLiteDataType<String>(SQLDataType.NCHAR, "nchar");
    public static final SQLiteDataType<String>     NATIVECHARACTER  = new SQLiteDataType<String>(SQLDataType.NCHAR, "native character");
    public static final SQLiteDataType<String>     NVARCHAR         = new SQLiteDataType<String>(SQLDataType.NVARCHAR, "nvarchar");
    public static final SQLiteDataType<String>     CLOB             = new SQLiteDataType<String>(SQLDataType.CLOB, "clob");
    public static final SQLiteDataType<String>     TEXT             = new SQLiteDataType<String>(SQLDataType.CLOB, "text");
    public static final SQLiteDataType<Boolean>    BOOLEAN          = new SQLiteDataType<Boolean>(SQLDataType.BOOLEAN, "boolean");
    public static final SQLiteDataType<Date>       DATE             = new SQLiteDataType<Date>(SQLDataType.DATE, "date");
    public static final SQLiteDataType<Timestamp>  DATETIME         = new SQLiteDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime");
    public static final SQLiteDataType<byte[]>     LONGVARBINARY    = new SQLiteDataType<byte[]>(SQLDataType.LONGVARBINARY, "longvarbinary");
    public static final SQLiteDataType<byte[]>     BLOB             = new SQLiteDataType<byte[]>(SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final SQLiteDataType<byte[]>  __BINARY         = new SQLiteDataType<byte[]>(SQLDataType.BINARY, "longvarbinary");
    protected static final SQLiteDataType<Boolean> __BIT            = new SQLiteDataType<Boolean>(SQLDataType.BIT, "boolean");
    protected static final SQLiteDataType<Double>  __FLOAT          = new SQLiteDataType<Double>(SQLDataType.FLOAT, "double");
    protected static final SQLiteDataType<String>  __NCLOB          = new SQLiteDataType<String>(SQLDataType.NCLOB, "nclob");
    protected static final SQLiteDataType<String>  __LONGNVARCHAR   = new SQLiteDataType<String>(SQLDataType.LONGNVARCHAR, "nvarchar");
    protected static final SQLiteDataType<Time>    __TIME           = new SQLiteDataType<Time>(SQLDataType.TIME, "datetime");
    protected static final SQLiteDataType<byte[]>  __VARBINARY      = new SQLiteDataType<byte[]>(SQLDataType.VARBINARY, "longvarbinary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final SQLiteDataType<Object>     NULL             = new SQLiteDataType<Object>(SQLDataType.OTHER, "null");


    private SQLiteDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.SQLITE, sqlDataType, sqlDataType.getType(), typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.SQLITE, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.SQLITE, typeName);
    }
}
