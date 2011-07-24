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

package org.jooq.util.derby;

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
 * Supported data types for the {@link SQLDialect#DERBY} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://db.apache.org/derby/docs/10.7/ref/crefsqlj31068.html">http://db.apache.org/derby/docs/10.7/ref/crefsqlj31068.html</a>
 */
public class DerbyDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                     serialVersionUID           = 9064795517702394227L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DerbyDataType<Short>      SMALLINT                   = new DerbyDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final DerbyDataType<Integer>    INT                        = new DerbyDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final DerbyDataType<Integer>    INTEGER                    = new DerbyDataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final DerbyDataType<Long>       BIGINT                     = new DerbyDataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final DerbyDataType<Double>     DOUBLE                     = new DerbyDataType<Double>(SQLDataType.DOUBLE, "double");
    public static final DerbyDataType<Double>     DOUBLEPRECISION            = new DerbyDataType<Double>(SQLDataType.DOUBLE, "double precision");
    public static final DerbyDataType<Double>     FLOAT                      = new DerbyDataType<Double>(SQLDataType.FLOAT, "float");
    public static final DerbyDataType<Float>      REAL                       = new DerbyDataType<Float>(SQLDataType.REAL, "real");
    public static final DerbyDataType<BigDecimal> DECIMAL                    = new DerbyDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final DerbyDataType<BigDecimal> DEC                        = new DerbyDataType<BigDecimal>(SQLDataType.DECIMAL, "dec");
    public static final DerbyDataType<BigDecimal> NUMERIC                    = new DerbyDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric");
    public static final DerbyDataType<String>     VARCHAR                    = new DerbyDataType<String>(SQLDataType.VARCHAR, "varchar", "varchar(32672)");
    public static final DerbyDataType<String>     LONGVARCHAR                = new DerbyDataType<String>(SQLDataType.LONGVARCHAR, "long varchar");
    public static final DerbyDataType<String>     CHAR                       = new DerbyDataType<String>(SQLDataType.CHAR, "char");
    public static final DerbyDataType<String>     CHARACTER                  = new DerbyDataType<String>(SQLDataType.CHAR, "character");
    public static final DerbyDataType<String>     CLOB                       = new DerbyDataType<String>(SQLDataType.CLOB, "clob");
    public static final DerbyDataType<String>     CHARACTERLARGEOBJECT       = new DerbyDataType<String>(SQLDataType.CLOB, "character large object");
    public static final DerbyDataType<String>     CHARVARYING                = new DerbyDataType<String>(SQLDataType.VARCHAR, "char varying", "char varying(32672)");
    public static final DerbyDataType<String>     CHARACTERVARYING           = new DerbyDataType<String>(SQLDataType.VARCHAR, "character varying", "character varying(32672)");
    public static final DerbyDataType<Boolean>    BOOLEAN                    = new DerbyDataType<Boolean>(SQLDataType.BOOLEAN, "boolean");
    public static final DerbyDataType<Date>       DATE                       = new DerbyDataType<Date>(SQLDataType.DATE, "date");
    public static final DerbyDataType<Time>       TIME                       = new DerbyDataType<Time>(SQLDataType.TIME, "time");
    public static final DerbyDataType<Timestamp>  TIMESTAMP                  = new DerbyDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp");
    public static final DerbyDataType<byte[]>     BLOB                       = new DerbyDataType<byte[]>(SQLDataType.BLOB, "blob");
    public static final DerbyDataType<byte[]>     BINARYLARGEOBJECT          = new DerbyDataType<byte[]>(SQLDataType.BLOB, "binary large object");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DerbyDataType<byte[]>     __BINARY                = new DerbyDataType<byte[]>(SQLDataType.BINARY, "blob");
    protected static final DerbyDataType<Boolean>    __BIT                   = new DerbyDataType<Boolean>(SQLDataType.BIT, "boolean");
    protected static final DerbyDataType<byte[]>     __LONGVARBINARY         = new DerbyDataType<byte[]>(SQLDataType.LONGVARBINARY, "blob");
    protected static final DerbyDataType<String>     __NCHAR                 = new DerbyDataType<String>(SQLDataType.NCHAR, "char");
    protected static final DerbyDataType<String>     __NCLOB                 = new DerbyDataType<String>(SQLDataType.NCLOB, "clob");
    protected static final DerbyDataType<String>     __LONGNVARCHAR          = new DerbyDataType<String>(SQLDataType.LONGNVARCHAR, "long varchar");
    protected static final DerbyDataType<String>     __NVARCHAR              = new DerbyDataType<String>(SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
    protected static final DerbyDataType<Byte>       __TINYINT               = new DerbyDataType<Byte>(SQLDataType.TINYINT, "smallint");
    protected static final DerbyDataType<byte[]>     __VARBINARY             = new DerbyDataType<byte[]>(SQLDataType.VARBINARY, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DerbyDataType<BigInteger> __BIGINTEGER            = new DerbyDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DerbyDataType<byte[]>     CHARFORBITDATA             = new DerbyDataType<byte[]>(SQLDataType.BLOB, "char for bit data");
    public static final DerbyDataType<byte[]>     CHARACTERFORBITDATA        = new DerbyDataType<byte[]>(SQLDataType.BLOB, "character for bit data");
    public static final DerbyDataType<byte[]>     LONGVARCHARFORBITDATA      = new DerbyDataType<byte[]>(SQLDataType.BLOB, "long varchar for bit data");
    public static final DerbyDataType<byte[]>     VARCHARFORBITDATA          = new DerbyDataType<byte[]>(SQLDataType.BLOB, "varchar for bit data", "varchar(32672) for bit data");
    public static final DerbyDataType<byte[]>     CHARVARYINGFORBITDATA      = new DerbyDataType<byte[]>(SQLDataType.BLOB, "char varying for bit data", "char varying(32672) for bit data");
    public static final DerbyDataType<byte[]>     CHARACTERVARYINGFORBITDATA = new DerbyDataType<byte[]>(SQLDataType.BLOB, "character varying for bit data", "character varying (32672) for bit data");
    public static final DerbyDataType<String>     ORGAPACHEDERBYCATALOGTYPEDESCRIPTOR
                                                                             = new DerbyDataType<String>(SQLDataType.CLOB, "org.apache.derby.catalog.TypeDescriptor");
    public static final DerbyDataType<String>     ORGAPACHEDERBYCATALOGINDEXDESCRIPTOR
                                                                             = new DerbyDataType<String>(SQLDataType.CLOB, "org.apache.derby.catalog.IndexDescriptor");
    public static final DerbyDataType<String>     JAVAIOSERIALIZABLE         = new DerbyDataType<String>(SQLDataType.CLOB, "java.io.Serializable");


    private DerbyDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.DERBY, sqlDataType, sqlDataType.getType(), typeName);
    }

    private DerbyDataType(SQLDataType<T> sqlDataType, String typeName, String castName) {
        super(SQLDialect.DERBY, sqlDataType, sqlDataType.getType(), typeName, castName);
    }

    /**
     * @deprecated - 1.6.3 - Do not reuse
     */
    @Deprecated
    public static DataType<?> getDataType(String typeName) {
        return getDataType(SQLDialect.DERBY, typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.DERBY, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.DERBY, typeName);
    }
}
