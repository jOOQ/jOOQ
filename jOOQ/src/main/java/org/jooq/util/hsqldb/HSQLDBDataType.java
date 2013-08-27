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

package org.jooq.util.hsqldb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.Result;
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
 * Supported data types for the {@link SQLDialect#HSQLDB} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://hsqldb.org/doc/guide/ch09.html#datatypes-section">http://hsqldb.org/doc/guide/ch09.html#datatypes-section</a>
 * @see <a href="http://hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html#sqlgeneral_types_ops-sect">http://hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html#sqlgeneral_types_ops-sect</a>
 */
public class HSQLDBDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>        TINYINT               = new DefaultDataType<Byte>(SQLDialect.HSQLDB, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>       SMALLINT              = new DefaultDataType<Short>(SQLDialect.HSQLDB, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>     INT                   = new DefaultDataType<Integer>(SQLDialect.HSQLDB, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>     INTEGER               = new DefaultDataType<Integer>(SQLDialect.HSQLDB, SQLDataType.INTEGER, "integer");
    public static final DataType<Long>        BIGINT                = new DefaultDataType<Long>(SQLDialect.HSQLDB, SQLDataType.BIGINT, "bigint");
    public static final DataType<Double>      DOUBLE                = new DefaultDataType<Double>(SQLDialect.HSQLDB, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>      DOUBLEPRECISION       = new DefaultDataType<Double>(SQLDialect.HSQLDB, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>      FLOAT                 = new DefaultDataType<Double>(SQLDialect.HSQLDB, SQLDataType.FLOAT, "float");
    public static final DataType<Float>       REAL                  = new DefaultDataType<Float>(SQLDialect.HSQLDB, SQLDataType.REAL, "real");
    public static final DataType<Boolean>     BOOLEAN               = new DefaultDataType<Boolean>(SQLDialect.HSQLDB, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Boolean>     BIT                   = new DefaultDataType<Boolean>(SQLDialect.HSQLDB, SQLDataType.BIT, "bit");
    public static final DataType<BigDecimal>  DECIMAL               = new DefaultDataType<BigDecimal>(SQLDialect.HSQLDB, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal>  NUMERIC               = new DefaultDataType<BigDecimal>(SQLDialect.HSQLDB, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>      VARCHAR               = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.VARCHAR, "varchar", "varchar(32672)");
    public static final DataType<String>      LONGVARCHAR           = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.LONGVARCHAR, "longvarchar");
    public static final DataType<String>      CHAR                  = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.CHAR, "char");
    public static final DataType<String>      CHARACTER             = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.CHAR, "character");
    public static final DataType<String>      CHARACTERVARYING      = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.VARCHAR, "character varying", "character varying(32672)");
    public static final DataType<String>      CLOB                  = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.CLOB, "clob");
    public static final DataType<String>      CHARLARGEOBJECT       = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.CLOB, "char large object", "clob");
    public static final DataType<String>      CHARACTERLARGEOBJECT  = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.CLOB, "character large object", "clob");
    public static final DataType<Date>        DATE                  = new DefaultDataType<Date>(SQLDialect.HSQLDB, SQLDataType.DATE, "date");
    public static final DataType<Time>        TIME                  = new DefaultDataType<Time>(SQLDialect.HSQLDB, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>   TIMESTAMP             = new DefaultDataType<Timestamp>(SQLDialect.HSQLDB, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<Timestamp>   DATETIME              = new DefaultDataType<Timestamp>(SQLDialect.HSQLDB, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<byte[]>      LONGVARBINARY         = new DefaultDataType<byte[]>(SQLDialect.HSQLDB, SQLDataType.LONGVARBINARY, "longvarbinary");
    public static final DataType<byte[]>      VARBINARY             = new DefaultDataType<byte[]>(SQLDialect.HSQLDB, SQLDataType.VARBINARY, "varbinary", "varbinary(32672)");
    public static final DataType<byte[]>      BINARY                = new DefaultDataType<byte[]>(SQLDialect.HSQLDB, SQLDataType.BINARY, "binary");
    public static final DataType<byte[]>      BLOB                  = new DefaultDataType<byte[]>(SQLDialect.HSQLDB, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>      BINARYLARGEOBJECT     = new DefaultDataType<byte[]>(SQLDialect.HSQLDB, SQLDataType.BLOB, "binary large object", "blob");
    public static final DataType<Object>      OTHER                 = new DefaultDataType<Object>(SQLDialect.HSQLDB, SQLDataType.OTHER, "other");
    public static final DataType<YearToMonth> INTERVALYEARTOMONTH   = new DefaultDataType<YearToMonth>(SQLDialect.HSQLDB, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond> INTERVALDAYTOSECOND   = new DefaultDataType<DayToSecond>(SQLDialect.HSQLDB, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.HSQLDB, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<String>   __NCHAR               = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.NCHAR, "char");
    protected static final DataType<String>   __NCLOB               = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>   __LONGNVARCHAR        = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.LONGNVARCHAR, "longvarchar");
    protected static final DataType<String>   __NVARCHAR            = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
    protected static final DataType<UByte>    __TINYINTUNSIGNED     = new DefaultDataType<UByte>(SQLDialect.HSQLDB, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED    = new DefaultDataType<UShort>(SQLDialect.HSQLDB, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED     = new DefaultDataType<UInteger>(SQLDialect.HSQLDB, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED      = new DefaultDataType<ULong>(SQLDialect.HSQLDB, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER       = new DefaultDataType<BigInteger>(SQLDialect.HSQLDB, SQLDataType.DECIMAL_INTEGER, "decimal");
    protected static final DataType<UUID>       __UUID             = new DefaultDataType<UUID>(SQLDialect.HSQLDB, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<String>         VARCHARIGNORECASE = new DefaultDataType<String>(SQLDialect.HSQLDB, SQLDataType.VARCHAR, "varchar_ignorecase", "varchar_ignorecase(32672)");
    public static final DataType<Object>         OBJECT            = new DefaultDataType<Object>(SQLDialect.HSQLDB, SQLDataType.OTHER, "object");
    public static final DataType<Result<Record>> ROW               = new DefaultDataType<Result<Record>>(SQLDialect.HSQLDB, SQLDataType.RESULT, "row");
}
