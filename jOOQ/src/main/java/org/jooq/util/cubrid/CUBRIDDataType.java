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

package org.jooq.util.cubrid;

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
 * Supported data types for the {@link SQLDialect#CUBRID} dialect
 *
 * @author Lukas Eder
 */
public class CUBRIDDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                     serialVersionUID = -5677365115109672781L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final CUBRIDDataType<Integer>    INT                      = new CUBRIDDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final CUBRIDDataType<Integer>    INTEGER                  = new CUBRIDDataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final CUBRIDDataType<Short>      SHORT                    = new CUBRIDDataType<Short>(SQLDataType.SMALLINT, "short");
    public static final CUBRIDDataType<Short>      SMALLINT                 = new CUBRIDDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final CUBRIDDataType<Long>       BIGINT                   = new CUBRIDDataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final CUBRIDDataType<BigDecimal> DECIMAL                  = new CUBRIDDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final CUBRIDDataType<BigDecimal> DEC                      = new CUBRIDDataType<BigDecimal>(SQLDataType.DECIMAL, "dec");
    public static final CUBRIDDataType<BigDecimal> NUMERIC                  = new CUBRIDDataType<BigDecimal>(SQLDataType.DECIMAL, "numeric");
    public static final CUBRIDDataType<Float>      FLOAT                    = new CUBRIDDataType<Float>(SQLDataType.REAL, "float");
    public static final CUBRIDDataType<Float>      REAL                     = new CUBRIDDataType<Float>(SQLDataType.REAL, "real");
    public static final CUBRIDDataType<Double>     DOUBLE                   = new CUBRIDDataType<Double>(SQLDataType.DOUBLE, "double");
    public static final CUBRIDDataType<Double>     DOUBLEPRECISION          = new CUBRIDDataType<Double>(SQLDataType.DOUBLE, "double precision");

    public static final CUBRIDDataType<String>     VARCHAR                  = new CUBRIDDataType<String>(SQLDataType.VARCHAR, "varchar");
    public static final CUBRIDDataType<String>     CHARVARYING              = new CUBRIDDataType<String>(SQLDataType.VARCHAR, "char varying");
    public static final CUBRIDDataType<String>     CHARACTERVARYING         = new CUBRIDDataType<String>(SQLDataType.VARCHAR, "character varying");
    public static final CUBRIDDataType<String>     CHAR                     = new CUBRIDDataType<String>(SQLDataType.CHAR, "char", "varchar");
    public static final CUBRIDDataType<String>     CHARACTER                = new CUBRIDDataType<String>(SQLDataType.CHAR, "character", "varchar");
    public static final CUBRIDDataType<String>     STRING                   = new CUBRIDDataType<String>(SQLDataType.VARCHAR, "string");
    public static final CUBRIDDataType<String>     NCHAR                    = new CUBRIDDataType<String>(SQLDataType.NCHAR, "nchar");
    public static final CUBRIDDataType<String>     NCHARVARYING             = new CUBRIDDataType<String>(SQLDataType.NVARCHAR, "nchar varying");
    public static final CUBRIDDataType<String>     NATIONALCHARVARYING      = new CUBRIDDataType<String>(SQLDataType.NVARCHAR, "national char varying");
    public static final CUBRIDDataType<String>     NATIONALCHARACTERVARYING = new CUBRIDDataType<String>(SQLDataType.NVARCHAR, "national character varying");
    public static final CUBRIDDataType<String>     VARNCHAR                 = new CUBRIDDataType<String>(SQLDataType.NVARCHAR, "varnchar");
    public static final CUBRIDDataType<String>     CLOB                     = new CUBRIDDataType<String>(SQLDataType.CLOB, "clob");

    public static final CUBRIDDataType<Date>       DATE                     = new CUBRIDDataType<Date>(SQLDataType.DATE, "date");
    public static final CUBRIDDataType<Time>       TIME                     = new CUBRIDDataType<Time>(SQLDataType.TIME, "time");
    public static final CUBRIDDataType<Timestamp>  DATETIME                 = new CUBRIDDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime");
    public static final CUBRIDDataType<Timestamp>  TIMESTAMP                = new CUBRIDDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp");

    public static final CUBRIDDataType<byte[]>     BITVARYING               = new CUBRIDDataType<byte[]>(SQLDataType.VARBINARY, "bitvarying");
    public static final CUBRIDDataType<byte[]>     VARBIT                   = new CUBRIDDataType<byte[]>(SQLDataType.VARBINARY, "varbit");
    public static final CUBRIDDataType<byte[]>     BIT                      = new CUBRIDDataType<byte[]>(SQLDataType.BINARY, "bit");
    public static final CUBRIDDataType<byte[]>     BLOB                     = new CUBRIDDataType<byte[]>(SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final CUBRIDDataType<Boolean>    __BOOL                = new CUBRIDDataType<Boolean>(SQLDataType.BOOLEAN, "bit", "bit(1)");
    protected static final CUBRIDDataType<Boolean>    __BIT                 = new CUBRIDDataType<Boolean>(SQLDataType.BIT, "bit", "bit(1)");
    protected static final CUBRIDDataType<String>     __LONGNVARCHAR        = new CUBRIDDataType<String>(SQLDataType.LONGNVARCHAR, "nvarchar");
    protected static final CUBRIDDataType<String>     __NCLOB               = new CUBRIDDataType<String>(SQLDataType.NCLOB, "clob");
    protected static final CUBRIDDataType<String>     __LONGVARCHAR         = new CUBRIDDataType<String>(SQLDataType.LONGVARCHAR, "varchar");
    protected static final CUBRIDDataType<byte[]>     __LONGVARBINARY       = new CUBRIDDataType<byte[]>(SQLDataType.LONGVARBINARY, "blob");
    protected static final CUBRIDDataType<Byte>       __TINYINT             = new CUBRIDDataType<Byte>(SQLDataType.TINYINT, "smallint");
    protected static final CUBRIDDataType<Double>     __FLOAT               = new CUBRIDDataType<Double>(SQLDataType.DOUBLE, "double");
    protected static final CUBRIDDataType<BigDecimal> __NUMERIC             = new CUBRIDDataType<BigDecimal>(SQLDataType.NUMERIC, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final CUBRIDDataType<BigInteger> __BIGINTEGER = new CUBRIDDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final CUBRIDDataType<Double> MONETARY             = new CUBRIDDataType<Double>(SQLDataType.DOUBLE, "monetary");

    // These types are not yet formally supported
    public static final CUBRIDDataType<Object> OBJECT               = new CUBRIDDataType<Object>(SQLDataType.OTHER, "object");
    public static final CUBRIDDataType<Object> OID                  = new CUBRIDDataType<Object>(SQLDataType.OTHER, "oid");
    public static final CUBRIDDataType<Object> ELO                  = new CUBRIDDataType<Object>(SQLDataType.OTHER, "elo");
    public static final CUBRIDDataType<Object> MULTISET             = new CUBRIDDataType<Object>(SQLDataType.OTHER, "multiset");
    public static final CUBRIDDataType<Object> SEQUENCE             = new CUBRIDDataType<Object>(SQLDataType.OTHER, "sequence");
    public static final CUBRIDDataType<Object> SET                  = new CUBRIDDataType<Object>(SQLDataType.OTHER, "set");

    private CUBRIDDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.CUBRID, sqlDataType, sqlDataType.getType(), typeName);
    }

    private CUBRIDDataType(SQLDataType<T> sqlDataType, String typeName, String castTypeName) {
        super(SQLDialect.CUBRID, sqlDataType, sqlDataType.getType(), typeName, castTypeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.CUBRID, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.CUBRID, typeName);
    }
}
