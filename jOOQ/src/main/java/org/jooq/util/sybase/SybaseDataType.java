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
package org.jooq.util.sybase;

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
 * Supported data types for the {@link SQLDialect#SYBASE} dialect
 *
 * @see <a href="http://infocenter.sybase.com/help/topic/com.sybase.help.sqlanywhere.12.0.0/dbreference/rf-datatypes.html">http://infocenter.sybase.com/help/topic/com.sybase.help.sqlanywhere.12.0.0/dbreference/rf-datatypes.html</a>
 * @author Espen Stromsnes
 */
public class SybaseDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<UByte>      UNSIGNEDTINYINT            = new DefaultDataType<UByte>(SQLDialect.SYBASE, SQLDataType.TINYINTUNSIGNED, "unsigned tinyint");
    public static final DataType<UByte>      TINYINT                    = new DefaultDataType<UByte>(SQLDialect.SYBASE, SQLDataType.TINYINTUNSIGNED, "tinyint");
    public static final DataType<Short>      SMALLINT                   = new DefaultDataType<Short>(SQLDialect.SYBASE, SQLDataType.SMALLINT, "smallint");
    public static final DataType<UShort>     UNSIGNEDSMALLLINT          = new DefaultDataType<UShort>(SQLDialect.SYBASE, SQLDataType.SMALLINTUNSIGNED, "unsigned smallint");
    public static final DataType<Integer>    INT                        = new DefaultDataType<Integer>(SQLDialect.SYBASE, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER                    = new DefaultDataType<Integer>(SQLDialect.SYBASE, SQLDataType.INTEGER, "integer");
    public static final DataType<UInteger>   UNSIGNEDINT                = new DefaultDataType<UInteger>(SQLDialect.SYBASE, SQLDataType.INTEGERUNSIGNED, "unsigned int");
    public static final DataType<Long>       BIGINT                     = new DefaultDataType<Long>(SQLDialect.SYBASE, SQLDataType.BIGINT, "bigint");
    public static final DataType<ULong>      UNSIGNEDBIGINT             = new DefaultDataType<ULong>(SQLDialect.SYBASE, SQLDataType.BIGINTUNSIGNED, "unsigned bigint");
    public static final DataType<Double>     DOUBLE                     = new DefaultDataType<Double>(SQLDialect.SYBASE, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     FLOAT                      = new DefaultDataType<Double>(SQLDialect.SYBASE, SQLDataType.FLOAT, "float");
    public static final DataType<Float>      REAL                       = new DefaultDataType<Float>(SQLDialect.SYBASE, SQLDataType.REAL, "real");
    public static final DataType<BigDecimal> DECIMAL                    = new DefaultDataType<BigDecimal>(SQLDialect.SYBASE, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> NUMERIC                    = new DefaultDataType<BigDecimal>(SQLDialect.SYBASE, SQLDataType.NUMERIC, "numeric");
    public static final DataType<Boolean>    BIT                        = new DefaultDataType<Boolean>(SQLDialect.SYBASE, SQLDataType.BIT, "bit");
    public static final DataType<String>     VARCHAR                    = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     CHAR                       = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.CHAR, "char");
    public static final DataType<String>     LONGNVARCHAR               = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.LONGNVARCHAR, "long nvarchar");
    public static final DataType<String>     LONGVARCHAR                = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.LONGVARCHAR, "long varchar");
    public static final DataType<String>     NCHAR                      = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NTEXT                      = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.NCLOB, "ntext");
    public static final DataType<String>     NVARCHAR                   = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>     TEXT                       = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.CLOB, "text");
    public static final DataType<Date>       DATE                       = new DefaultDataType<Date>(SQLDialect.SYBASE, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                       = new DefaultDataType<Time>(SQLDialect.SYBASE, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  DATETIME                   = new DefaultDataType<Timestamp>(SQLDialect.SYBASE, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<Timestamp>  TIMESTAMP                  = new DefaultDataType<Timestamp>(SQLDialect.SYBASE, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>     BINARY                     = new DefaultDataType<byte[]>(SQLDialect.SYBASE, SQLDataType.BINARY, "binary");
    public static final DataType<byte[]>     LONGBINARY                 = new DefaultDataType<byte[]>(SQLDialect.SYBASE, SQLDataType.LONGVARBINARY, "long binary");
    public static final DataType<byte[]>     VARBINARY                  = new DefaultDataType<byte[]>(SQLDialect.SYBASE, SQLDataType.VARBINARY, "varbinary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.SYBASE, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>  __BLOB                     = new DefaultDataType<byte[]>(SQLDialect.SYBASE, SQLDataType.BLOB, "binary");
    protected static final DataType<Boolean> __BOOLEAN                  = new DefaultDataType<Boolean>(SQLDialect.SYBASE, SQLDataType.BOOLEAN, "bit");
    protected static final DataType<Byte>    __BYTE                     = new DefaultDataType<Byte>(SQLDialect.SYBASE, SQLDataType.TINYINT, "tinyint");
    protected static final DataType<Byte>    __BYTESIGNED               = new DefaultDataType<Byte>(SQLDialect.SYBASE, SQLDataType.TINYINT, "signed tinyint");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER            = new DefaultDataType<BigInteger>(SQLDialect.SYBASE, SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<BigDecimal> MONEY                      = new DefaultDataType<BigDecimal>(SQLDialect.SYBASE, SQLDataType.DECIMAL, "money");
    public static final DataType<BigDecimal> SMALLMONEY                 = new DefaultDataType<BigDecimal>(SQLDialect.SYBASE, SQLDataType.DECIMAL, "smallmoney");
    public static final DataType<String>     UNIQUEIDENTIFIERSTR        = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.VARCHAR, "uniqueidentifierstr");
    public static final DataType<String>     XML                        = new DefaultDataType<String>(SQLDialect.SYBASE, SQLDataType.VARCHAR, "xml");
    public static final DataType<UUID>       UNIQUEIDENTIFIER           = new DefaultDataType<UUID>(SQLDialect.SYBASE, SQLDataType.UUID, "uniqueidentifier");
    public static final DataType<Timestamp>  DATETIMEOFFSET             = new DefaultDataType<Timestamp>(SQLDialect.SYBASE, SQLDataType.TIMESTAMP, "datetimeoffset");
    public static final DataType<Timestamp>  SMALLDATETIME              = new DefaultDataType<Timestamp>(SQLDialect.SYBASE, SQLDataType.TIMESTAMP, "smalldatetime");
    public static final DataType<Timestamp>  TIMESTAMPWITHTIMEZONE      = new DefaultDataType<Timestamp>(SQLDialect.SYBASE, SQLDataType.TIMESTAMP, "timestampwithtimezone");
    public static final DataType<byte[]>     IMAGE                      = new DefaultDataType<byte[]>(SQLDialect.SYBASE, SQLDataType.BINARY, "image");
    public static final DataType<byte[]>     VARBIT                     = new DefaultDataType<byte[]>(SQLDialect.SYBASE, SQLDataType.VARBINARY, "varbit");
    public static final DataType<byte[]>     LONGVARBIT                 = new DefaultDataType<byte[]>(SQLDialect.SYBASE, SQLDataType.LONGVARBINARY, "longvarbit");
}
