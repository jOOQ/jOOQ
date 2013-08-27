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
