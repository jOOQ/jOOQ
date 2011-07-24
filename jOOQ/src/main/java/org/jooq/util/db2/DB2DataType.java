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
package org.jooq.util.db2;

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
 * Supported data types for the {@link SQLDialect#DB2} dialect
 *
 * @see <a href="http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.ibm.db2.udb.apdv.java.doc/doc/rjvjdata.htm">http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.ibm.db2.udb.apdv.java.doc/doc/rjvjdata.htm</a>
 * @author Espen Stromsnes
 */
public class DB2DataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                   serialVersionUID   = -5677365115109672781L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DB2DataType<Short>      SMALLINT    = new DB2DataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final DB2DataType<Integer>    INTEGER     = new DB2DataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final DB2DataType<Long>       BIGINT      = new DB2DataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final DB2DataType<Float>      REAL        = new DB2DataType<Float>(SQLDataType.REAL, "real");
    public static final DB2DataType<Double>     DOUBLE      = new DB2DataType<Double>(SQLDataType.DOUBLE, "double");
    public static final DB2DataType<BigDecimal> DECIMAL     = new DB2DataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final DB2DataType<BigDecimal> DECFLOAT    = new DB2DataType<BigDecimal>(SQLDataType.DECIMAL, "decfloat");
    public static final DB2DataType<String>     VARCHAR     = new DB2DataType<String>(SQLDataType.VARCHAR, "varchar", "varchar(32672)");
    public static final DB2DataType<String>     CHAR        = new DB2DataType<String>(SQLDataType.CHAR, "char");
    public static final DB2DataType<String>     CHARACTER   = new DB2DataType<String>(SQLDataType.CHAR, "character");
    public static final DB2DataType<String>     LONGVARCHAR = new DB2DataType<String>(SQLDataType.LONGVARCHAR, "long varchar");
    public static final DB2DataType<String>     CLOB        = new DB2DataType<String>(SQLDataType.CLOB, "clob");
    public static final DB2DataType<byte[]>     BLOB        = new DB2DataType<byte[]>(SQLDataType.BLOB, "blob");
    public static final DB2DataType<Date>       DATE        = new DB2DataType<Date>(SQLDataType.DATE, "date");
    public static final DB2DataType<Time>       TIME        = new DB2DataType<Time>(SQLDataType.TIME, "time");
    public static final DB2DataType<Timestamp>  TIMESTAMP   = new DB2DataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DB2DataType<byte[]>     __BINARY        = new DB2DataType<byte[]>(SQLDataType.BINARY, "blob");
    protected static final DB2DataType<Boolean>    __BIT           = new DB2DataType<Boolean>(SQLDataType.BIT, "smallint");
    protected static final DB2DataType<Boolean>    __BOOLEAN       = new DB2DataType<Boolean>(SQLDataType.BOOLEAN, "smallint");
    protected static final DB2DataType<byte[]>     __LONGVARBINARY = new DB2DataType<byte[]>(SQLDataType.LONGVARBINARY, "blob");
    protected static final DB2DataType<String>     __NCHAR         = new DB2DataType<String>(SQLDataType.NCHAR, "char");
    protected static final DB2DataType<String>     __NCLOB         = new DB2DataType<String>(SQLDataType.NCLOB, "clob");
    protected static final DB2DataType<String>     __LONGNVARCHAR  = new DB2DataType<String>(SQLDataType.LONGNVARCHAR, "long varchar");
    protected static final DB2DataType<BigDecimal> __NUMERIC       = new DB2DataType<BigDecimal>(SQLDataType.NUMERIC, "decimal", "decimal");
    protected static final DB2DataType<String>     __NVARCHAR      = new DB2DataType<String>(SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
    protected static final DB2DataType<Byte>       __TINYINT       = new DB2DataType<Byte>(SQLDataType.TINYINT, "smallint");
    protected static final DB2DataType<byte[]>     __VARBINARY     = new DB2DataType<byte[]>(SQLDataType.VARBINARY, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DB2DataType<BigInteger> __BIGINTEGER = new DB2DataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DB2DataType<String> XML               = new DB2DataType<String>(SQLDataType.CLOB, "xml");
    public static final DB2DataType<String> DBCLOB            = new DB2DataType<String>(SQLDataType.CLOB, "dbclob");
    public static final DB2DataType<String> GRAPHIC           = new DB2DataType<String>(SQLDataType.CLOB, "graphic");
    public static final DB2DataType<String> VARGRAPHIC        = new DB2DataType<String>(SQLDataType.CLOB, "vargraphic");
    public static final DB2DataType<byte[]> CHARFORBITDATA    = new DB2DataType<byte[]>(SQLDataType.BLOB, "char for bit data");
    public static final DB2DataType<byte[]> VARCHARFORBITDATA = new DB2DataType<byte[]>(SQLDataType.BLOB, "varchar(32672) for bit data");
    public static final DB2DataType<byte[]> ROWID             = new DB2DataType<byte[]>(SQLDataType.BLOB, "rowid");


    private DB2DataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.DB2, sqlDataType, sqlDataType.getType(), typeName);
    }

    private DB2DataType(SQLDataType<T> sqlDataType, String typeName, String castTypeName) {
        super(SQLDialect.DB2, sqlDataType, sqlDataType.getType(), typeName, castTypeName);
    }

    /**
     * @deprecated - 1.6.3 - Do not reuse
     */
    @Deprecated
    public static DataType<?> getDataType(String typeName) {
        return getDataType(SQLDialect.DB2, typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.DB2, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.DB2, typeName);
    }
}
