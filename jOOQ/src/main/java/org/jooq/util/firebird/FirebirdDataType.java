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

package org.jooq.util.firebird;

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
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;

/**
 * Supported data types for the {@link SQLDialect#FIREBIRD} dialect
 *
 * @author Lukas Eder
 */
public class FirebirdDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>       SMALLINT              = new DefaultDataType<Short>(SQLDialect.FIREBIRD, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>     INTEGER               = new DefaultDataType<Integer>(SQLDialect.FIREBIRD, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>     INT                   = new DefaultDataType<Integer>(SQLDialect.FIREBIRD, SQLDataType.INTEGER, "int");
    public static final DataType<Long>        BIGINT                = new DefaultDataType<Long>(SQLDialect.FIREBIRD, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>        INT64                 = new DefaultDataType<Long>(SQLDialect.FIREBIRD, SQLDataType.BIGINT, "int64");
    public static final DataType<Double>      DOUBLEPRECISION       = new DefaultDataType<Double>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>      DOUBLE                = new DefaultDataType<Double>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>      D_FLOAT               = new DefaultDataType<Double>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "d_float");
    public static final DataType<Float>       FLOAT                 = new DefaultDataType<Float>(SQLDialect.FIREBIRD, SQLDataType.REAL, "float");
    public static final DataType<Boolean>     BOOLEAN               = new DefaultDataType<Boolean>(SQLDialect.FIREBIRD, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<BigDecimal>  DECIMAL               = new DefaultDataType<BigDecimal>(SQLDialect.FIREBIRD, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal>  NUMERIC               = new DefaultDataType<BigDecimal>(SQLDialect.FIREBIRD, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>      VARCHAR               = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.VARCHAR, "varchar", "varchar(4000)");
    public static final DataType<String>      CHARACTERVARYING      = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.VARCHAR, "character varying", "varchar(4000)");
    public static final DataType<String>      CHAR                  = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.CHAR, "char");
    public static final DataType<String>      CHARACTER             = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.CHAR, "character");
    public static final DataType<String>      CLOB                  = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.CLOB, "blob sub_type text");
    public static final DataType<Date>        DATE                  = new DefaultDataType<Date>(SQLDialect.FIREBIRD, SQLDataType.DATE, "date");
    public static final DataType<Time>        TIME                  = new DefaultDataType<Time>(SQLDialect.FIREBIRD, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>   TIMESTAMP             = new DefaultDataType<Timestamp>(SQLDialect.FIREBIRD, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>      BLOB                  = new DefaultDataType<byte[]>(SQLDialect.FIREBIRD, SQLDataType.BLOB, "blob");

    // TODO Below are HSQLDB data types. Fix this

    public static final DataType<Boolean>     BIT                   = new DefaultDataType<Boolean>(SQLDialect.FIREBIRD, SQLDataType.BIT, "bit");
    public static final DataType<Object>      OTHER                 = new DefaultDataType<Object>(SQLDialect.FIREBIRD, SQLDataType.OTHER, "other");
    public static final DataType<YearToMonth> INTERVALYEARTOMONTH   = new DefaultDataType<YearToMonth>(SQLDialect.FIREBIRD, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond> INTERVALDAYTOSECOND   = new DefaultDataType<DayToSecond>(SQLDialect.FIREBIRD, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.FIREBIRD, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>   __BINARY              = new DefaultDataType<byte[]>(SQLDialect.FIREBIRD, SQLDataType.BINARY, "blob");
    protected static final DataType<Double>   __FLOAT               = new DefaultDataType<Double>(SQLDialect.FIREBIRD, SQLDataType.FLOAT, "double precision");
    protected static final DataType<String>   __LONGNVARCHAR        = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.LONGNVARCHAR, "blob sub_type text");
    protected static final DataType<byte[]>   __LONGVARBINARY       = new DefaultDataType<byte[]>(SQLDialect.FIREBIRD, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<String>   __LONGVARCHAR         = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.LONGVARCHAR, "varchar", "varchar(4000)");
    protected static final DataType<String>   __NCHAR               = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.NCHAR, "char");
    protected static final DataType<String>   __NCLOB               = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>   __NVARCHAR            = new DefaultDataType<String>(SQLDialect.FIREBIRD, SQLDataType.NVARCHAR, "varchar", "varchar(4000)");
    protected static final DataType<Byte>     __TINYINT             = new DefaultDataType<Byte>(SQLDialect.FIREBIRD, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>   __VARBINARY           = new DefaultDataType<byte[]>(SQLDialect.FIREBIRD, SQLDataType.VARBINARY, "blob");
    protected static final DataType<UByte>    __TINYINTUNSIGNED     = new DefaultDataType<UByte>(SQLDialect.FIREBIRD, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED    = new DefaultDataType<UShort>(SQLDialect.FIREBIRD, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED     = new DefaultDataType<UInteger>(SQLDialect.FIREBIRD, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED      = new DefaultDataType<ULong>(SQLDialect.FIREBIRD, SQLDataType.BIGINTUNSIGNED, "decimal", "varchar(20)"); // There are no large numbers in firebird...?

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER        = new DefaultDataType<BigInteger>(SQLDialect.FIREBIRD, SQLDataType.DECIMAL_INTEGER, "decimal");
    protected static final DataType<UUID>       __UUID              = new DefaultDataType<UUID>(SQLDialect.FIREBIRD, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------
}
