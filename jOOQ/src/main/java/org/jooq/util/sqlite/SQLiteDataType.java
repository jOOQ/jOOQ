/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.util.sqlite;

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
 * Supported data types for the {@link SQLDialect#SQLITE} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://www.sqlite.org/datatype3.html">http://www.sqlite.org/datatype3.html</a>
 */
public class SQLiteDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>       TINYINT          = new DefaultDataType<Byte>(SQLDialect.SQLITE, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>      SMALLINT         = new DefaultDataType<Short>(SQLDialect.SQLITE, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>      INT2             = new DefaultDataType<Short>(SQLDialect.SQLITE, SQLDataType.SMALLINT, "int2");
    public static final DataType<Integer>    INT              = new DefaultDataType<Integer>(SQLDialect.SQLITE, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER          = new DefaultDataType<Integer>(SQLDialect.SQLITE, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>    MEDIUMINT        = new DefaultDataType<Integer>(SQLDialect.SQLITE, SQLDataType.INTEGER, "mediumint");
    public static final DataType<Long>       INT8             = new DefaultDataType<Long>(SQLDialect.SQLITE, SQLDataType.BIGINT, "int8");
    public static final DataType<Long>       BIGINT           = new DefaultDataType<Long>(SQLDialect.SQLITE, SQLDataType.BIGINT, "bigint");
    public static final DataType<BigInteger> UNSIGNEDBIGINT   = new DefaultDataType<BigInteger>(SQLDialect.SQLITE, SQLDataType.DECIMAL_INTEGER, "unsigned big int");
    public static final DataType<Double>     DOUBLE           = new DefaultDataType<Double>(SQLDialect.SQLITE, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     DOUBLEPRECISION  = new DefaultDataType<Double>(SQLDialect.SQLITE, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Float>      REAL             = new DefaultDataType<Float>(SQLDialect.SQLITE, SQLDataType.REAL, "real");
    public static final DataType<Float>      FLOAT            = new DefaultDataType<Float>(SQLDialect.SQLITE, SQLDataType.REAL, "float");
    public static final DataType<BigDecimal> NUMERIC          = new DefaultDataType<BigDecimal>(SQLDialect.SQLITE, SQLDataType.NUMERIC, "numeric");
    public static final DataType<BigDecimal> DECIMAL          = new DefaultDataType<BigDecimal>(SQLDialect.SQLITE, SQLDataType.DECIMAL, "decimal");
    public static final DataType<String>     LONGVARCHAR      = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.LONGVARCHAR, "longvarchar");
    public static final DataType<String>     CHAR             = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.CHAR, "char");
    public static final DataType<String>     CHARACTER        = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.CHAR, "character");
    public static final DataType<String>     VARCHAR          = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     VARYINGCHARACTER = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.VARCHAR, "varying character");
    public static final DataType<String>     NCHAR            = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NATIVECHARACTER  = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.NCHAR, "native character");
    public static final DataType<String>     NVARCHAR         = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>     CLOB             = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.CLOB, "clob");
    public static final DataType<String>     TEXT             = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.CLOB, "text");
    public static final DataType<Boolean>    BOOLEAN          = new DefaultDataType<Boolean>(SQLDialect.SQLITE, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Date>       DATE             = new DefaultDataType<Date>(SQLDialect.SQLITE, SQLDataType.DATE, "date");
    public static final DataType<Timestamp>  DATETIME         = new DefaultDataType<Timestamp>(SQLDialect.SQLITE, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<byte[]>     LONGVARBINARY    = new DefaultDataType<byte[]>(SQLDialect.SQLITE, SQLDataType.LONGVARBINARY, "longvarbinary");
    public static final DataType<byte[]>     BLOB             = new DefaultDataType<byte[]>(SQLDialect.SQLITE, SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.SQLITE, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>   __BINARY           = new DefaultDataType<byte[]>(SQLDialect.SQLITE, SQLDataType.BINARY, "longvarbinary");
    protected static final DataType<Boolean>  __BIT              = new DefaultDataType<Boolean>(SQLDialect.SQLITE, SQLDataType.BIT, "boolean");
    protected static final DataType<Double>   __FLOAT            = new DefaultDataType<Double>(SQLDialect.SQLITE, SQLDataType.FLOAT, "double");
    protected static final DataType<String>   __NCLOB            = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.NCLOB, "nclob");
    protected static final DataType<String>   __LONGNVARCHAR     = new DefaultDataType<String>(SQLDialect.SQLITE, SQLDataType.LONGNVARCHAR, "nvarchar");
    protected static final DataType<Time>     __TIME             = new DefaultDataType<Time>(SQLDialect.SQLITE, SQLDataType.TIME, "datetime");
    protected static final DataType<byte[]>   __VARBINARY        = new DefaultDataType<byte[]>(SQLDialect.SQLITE, SQLDataType.VARBINARY, "longvarbinary");
    protected static final DataType<UByte>    __TINYINTUNSIGNED  = new DefaultDataType<UByte>(SQLDialect.SQLITE, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED = new DefaultDataType<UShort>(SQLDialect.SQLITE, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED  = new DefaultDataType<UInteger>(SQLDialect.SQLITE, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED   = new DefaultDataType<ULong>(SQLDialect.SQLITE, SQLDataType.BIGINTUNSIGNED, "numeric");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<UUID>     __UUID             = new DefaultDataType<UUID>(SQLDialect.SQLITE, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Object>     NULL                = new DefaultDataType<Object>(SQLDialect.SQLITE, SQLDataType.OTHER, "null");
}
