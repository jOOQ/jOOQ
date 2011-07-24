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

package org.jooq.util.oracle;

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

/**
 * Supported data types for the {@link SQLDialect#ORACLE} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://www.techonthenet.com/oracle/datatypes.php">http://www.techonthenet.com/oracle/datatypes.php</a>
 * @see <a href="http://download.oracle.com/docs/cd/B19306_01/appdev.102/b14261/datatypes.htm">http://download.oracle.com/docs/cd/B19306_01/appdev.102/b14261/datatypes.htm</a>
 */
public class OracleDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                      serialVersionUID       = -5677365115109672781L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final OracleDataType<BigDecimal> NUMBER                 = new OracleDataType<BigDecimal>(SQLDataType.NUMERIC, "number", true);
    public static final OracleDataType<BigDecimal> NUMERIC                = new OracleDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric", true);
    public static final OracleDataType<BigDecimal> DECIMAL                = new OracleDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal", true);
    public static final OracleDataType<BigDecimal> DEC                    = new OracleDataType<BigDecimal>(SQLDataType.DECIMAL, "dec", true);
    public static final OracleDataType<String>     VARCHAR2               = new OracleDataType<String>(SQLDataType.VARCHAR, "varchar2", "varchar2(4000)");
    public static final OracleDataType<String>     VARCHAR                = new OracleDataType<String>(SQLDataType.VARCHAR, "varchar", "varchar2(4000)");
    public static final OracleDataType<String>     CHAR                   = new OracleDataType<String>(SQLDataType.CHAR, "char", "varchar2(4000)");
    public static final OracleDataType<String>     CLOB                   = new OracleDataType<String>(SQLDataType.CLOB, "clob");
    public static final OracleDataType<String>     NVARCHAR2              = new OracleDataType<String>(SQLDataType.NVARCHAR, "nvarchar2", "varchar2(4000)");
    public static final OracleDataType<String>     NVARCHAR               = new OracleDataType<String>(SQLDataType.NVARCHAR, "nvarchar", "varchar2(4000)");
    public static final OracleDataType<String>     NCHAR                  = new OracleDataType<String>(SQLDataType.NCHAR, "nchar", "varchar2(4000)");
    public static final OracleDataType<String>     NCLOB                  = new OracleDataType<String>(SQLDataType.NCLOB, "nclob");
    public static final OracleDataType<Date>       DATE                   = new OracleDataType<Date>(SQLDataType.DATE, "date");
    public static final OracleDataType<Timestamp>  TIMESTAMP              = new OracleDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp");
    public static final OracleDataType<byte[]>     BLOB                   = new OracleDataType<byte[]>(SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final OracleDataType<byte[]>  __BINARY               = new OracleDataType<byte[]>(SQLDataType.BINARY, "blob");
    protected static final OracleDataType<Long>    __BIGINT               = new OracleDataType<Long>(SQLDataType.BIGINT, "number", "number(19)");
    protected static final OracleDataType<Boolean> __BIT                  = new OracleDataType<Boolean>(SQLDataType.BIT, "number", "number(1)");
    protected static final OracleDataType<Boolean> __BOOLEAN              = new OracleDataType<Boolean>(SQLDataType.BOOLEAN, "number", "number(1)");
    protected static final OracleDataType<Double>  __DOUBLE               = new OracleDataType<Double>(SQLDataType.DOUBLE, "number");
    protected static final OracleDataType<Double>  __FLOAT                = new OracleDataType<Double>(SQLDataType.FLOAT, "number");
    protected static final OracleDataType<Integer> __INTEGER              = new OracleDataType<Integer>(SQLDataType.INTEGER, "number", "number(10)");
    protected static final OracleDataType<byte[]>  __LONGVARBINARY        = new OracleDataType<byte[]>(SQLDataType.LONGVARBINARY, "blob");
    protected static final OracleDataType<String>  __LONGVARCHAR          = new OracleDataType<String>(SQLDataType.LONGVARCHAR, "varchar2", "varchar2(4000)");
    protected static final OracleDataType<String>  __LONGNVARCHAR         = new OracleDataType<String>(SQLDataType.LONGNVARCHAR, "varchar2", "varchar2(4000)");
    protected static final OracleDataType<Float>   __REAL                 = new OracleDataType<Float>(SQLDataType.REAL, "number");
    protected static final OracleDataType<Short>   __SMALLINT             = new OracleDataType<Short>(SQLDataType.SMALLINT, "number", "number(5)");
    protected static final OracleDataType<Time>    __TIME                 = new OracleDataType<Time>(SQLDataType.TIME, "timestamp");
    protected static final OracleDataType<Byte>    __TINYINT              = new OracleDataType<Byte>(SQLDataType.TINYINT, "number", "number(3)");
    protected static final OracleDataType<byte[]>  __VARBINARY            = new OracleDataType<byte[]>(SQLDataType.VARBINARY, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final OracleDataType<BigInteger> __BIGINTEGER        = new OracleDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "number");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final OracleDataType<Result<Record>> REF_CURSOR         = new OracleDataType<Result<Record>>(SQLDataType.RESULT, "ref cursor");

    public static final OracleDataType<String>     LONG                   = new OracleDataType<String>(SQLDataType.CLOB, "long");
    public static final OracleDataType<byte[]>     RAW                    = new OracleDataType<byte[]>(SQLDataType.BLOB, "raw");
    public static final OracleDataType<byte[]>     LONGRAW                = new OracleDataType<byte[]>(SQLDataType.BLOB, "longraw");
    public static final OracleDataType<byte[]>     BFILE                  = new OracleDataType<byte[]>(SQLDataType.BLOB, "bfile");

    // PL/SQL data types
    public static final OracleDataType<Integer>    BINARY_INTEGER         = new OracleDataType<Integer>(SQLDataType.INTEGER, "binary_integer");
    public static final OracleDataType<Integer>    PLS_INTEGER            = new OracleDataType<Integer>(SQLDataType.INTEGER, "pls_integer");
    public static final OracleDataType<Integer>    NATURAL                = new OracleDataType<Integer>(SQLDataType.INTEGER, "natural");
    public static final OracleDataType<Integer>    NATURALN               = new OracleDataType<Integer>(SQLDataType.INTEGER, "naturaln");
    public static final OracleDataType<Integer>    POSITIVE               = new OracleDataType<Integer>(SQLDataType.INTEGER, "positive");
    public static final OracleDataType<Integer>    POSITIVEN              = new OracleDataType<Integer>(SQLDataType.INTEGER, "positiven");
    public static final OracleDataType<Integer>    SIGNTYPE               = new OracleDataType<Integer>(SQLDataType.INTEGER, "signtype");
    public static final OracleDataType<Double>     REAL                   = new OracleDataType<Double>(SQLDataType.DOUBLE, "real");
    public static final OracleDataType<Double>     DOUBLE_PRECISION       = new OracleDataType<Double>(SQLDataType.DOUBLE, "double_precision");
    public static final OracleDataType<Double>     BINARY_DOUBLE          = new OracleDataType<Double>(SQLDataType.DOUBLE, "binary_double");
    public static final OracleDataType<BigDecimal> FLOAT                  = new OracleDataType<BigDecimal>(SQLDataType.DECIMAL, "float");
    public static final OracleDataType<BigDecimal> BINARY_FLOAT           = new OracleDataType<BigDecimal>(SQLDataType.DECIMAL, "binary_float");
    public static final OracleDataType<BigInteger> INTEGER                = new OracleDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "integer");
    public static final OracleDataType<BigInteger> INT                    = new OracleDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "int");
    public static final OracleDataType<BigInteger> SMALLINT               = new OracleDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "smallint");
    public static final OracleDataType<Boolean>    BOOLEAN                = new OracleDataType<Boolean>(SQLDataType.BOOLEAN, "boolean");

    private OracleDataType(SQLDataType<T> sqlDataType, String typeName) {
        this(sqlDataType, typeName, false);
    }

    private OracleDataType(SQLDataType<T> sqlDataType, String typeName, String castTypeName) {
        this(sqlDataType, typeName, castTypeName, false);
    }

    private OracleDataType(SQLDataType<T> sqlDataType, String typeName, boolean hasPrecisionAndScale) {
        super(SQLDialect.ORACLE, sqlDataType, sqlDataType.getType(), typeName, hasPrecisionAndScale);
    }

    private OracleDataType(SQLDataType<T> sqlDataType, String typeName, String castTypeName, boolean hasPrecisionAndScale) {
        super(SQLDialect.ORACLE, sqlDataType, sqlDataType.getType(), typeName, castTypeName, hasPrecisionAndScale);
    }

    /**
     * @deprecated - 1.6.3 - Do not reuse
     */
    @Deprecated
    public static DataType<?> getDataType(String typeName) {
        return getDataType(SQLDialect.ORACLE, typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.ORACLE, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.ORACLE, typeName);
    }
}
