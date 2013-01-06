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
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;

/**
 * Supported data types for the {@link SQLDialect#DB2} dialect
 *
 * @see <a href="http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.ibm.db2.udb.apdv.java.doc/doc/rjvjdata.htm">http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.ibm.db2.udb.apdv.java.doc/doc/rjvjdata.htm</a>
 * @author Espen Stromsnes
 */
public class DB2DataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>      SMALLINT           = new DefaultDataType<Short>(SQLDialect.DB2, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>    INTEGER            = new DefaultDataType<Integer>(SQLDialect.DB2, SQLDataType.INTEGER, "integer");
    public static final DataType<Long>       BIGINT             = new DefaultDataType<Long>(SQLDialect.DB2, SQLDataType.BIGINT, "bigint");
    public static final DataType<Float>      REAL               = new DefaultDataType<Float>(SQLDialect.DB2, SQLDataType.REAL, "real");
    public static final DataType<Double>     DOUBLE             = new DefaultDataType<Double>(SQLDialect.DB2, SQLDataType.DOUBLE, "double");
    public static final DataType<BigDecimal> DECIMAL            = new DefaultDataType<BigDecimal>(SQLDialect.DB2, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> DECFLOAT           = new DefaultDataType<BigDecimal>(SQLDialect.DB2, SQLDataType.DECIMAL, "decfloat");
    public static final DataType<String>     VARCHAR            = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.VARCHAR, "varchar", "varchar(32672)");
    public static final DataType<String>     CHAR               = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.CHAR, "char", "varchar(32672)");
    public static final DataType<String>     CHARACTER          = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.CHAR, "character", "varchar(32672)");
    public static final DataType<String>     LONGVARCHAR        = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.LONGVARCHAR, "long varchar");
    public static final DataType<String>     CLOB               = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.CLOB, "clob");
    public static final DataType<byte[]>     BLOB               = new DefaultDataType<byte[]>(SQLDialect.DB2, SQLDataType.BLOB, "blob");
    public static final DataType<Date>       DATE               = new DefaultDataType<Date>(SQLDialect.DB2, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME               = new DefaultDataType<Time>(SQLDialect.DB2, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  TIMESTAMP          = new DefaultDataType<Timestamp>(SQLDialect.DB2, SQLDataType.TIMESTAMP, "timestamp");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.DB2, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>     __BINARY        = new DefaultDataType<byte[]>(SQLDialect.DB2, SQLDataType.BINARY, "blob");
    protected static final DataType<Boolean>    __BIT           = new DefaultDataType<Boolean>(SQLDialect.DB2, SQLDataType.BIT, "smallint");
    protected static final DataType<Boolean>    __BOOLEAN       = new DefaultDataType<Boolean>(SQLDialect.DB2, SQLDataType.BOOLEAN, "smallint");
    protected static final DataType<byte[]>     __LONGVARBINARY = new DefaultDataType<byte[]>(SQLDialect.DB2, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<String>     __NCHAR         = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.NCHAR, "char", "varchar(32672)");
    protected static final DataType<String>     __NCLOB         = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>     __LONGNVARCHAR  = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.LONGNVARCHAR, "long varchar");
    protected static final DataType<BigDecimal> __NUMERIC       = new DefaultDataType<BigDecimal>(SQLDialect.DB2, SQLDataType.NUMERIC, "decimal", "decimal");
    protected static final DataType<String>     __NVARCHAR      = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
    protected static final DataType<Byte>       __TINYINT       = new DefaultDataType<Byte>(SQLDialect.DB2, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>     __VARBINARY     = new DefaultDataType<byte[]>(SQLDialect.DB2, SQLDataType.VARBINARY, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER    = new DefaultDataType<BigInteger>(SQLDialect.DB2, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal(31)");
    protected static final DataType<UUID>       __UUID          = new DefaultDataType<UUID>(SQLDialect.DB2, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<String> XML                    = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.CLOB, "xml");
    public static final DataType<String> DBCLOB                 = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.CLOB, "dbclob");
    public static final DataType<String> GRAPHIC                = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.CLOB, "graphic");
    public static final DataType<String> VARGRAPHIC             = new DefaultDataType<String>(SQLDialect.DB2, SQLDataType.CLOB, "vargraphic");
    public static final DataType<byte[]> CHARFORBITDATA         = new DefaultDataType<byte[]>(SQLDialect.DB2, SQLDataType.BLOB, "char for bit data");
    public static final DataType<byte[]> VARCHARFORBITDATA      = new DefaultDataType<byte[]>(SQLDialect.DB2, SQLDataType.BLOB, "varchar(32672) for bit data");
    public static final DataType<byte[]> ROWID                  = new DefaultDataType<byte[]>(SQLDialect.DB2, SQLDataType.BLOB, "rowid");
}
