/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
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
import org.jooq.types.YearToSecond;

/**
 * Supported data types for the {@link SQLDialect#FIREBIRD} dialect
 *
 * @author Lukas Eder
 * @deprecated - 3.11.0 - [#7375] - This type is part of jOOQ's internal API. Do
 *             not reference this type directly from client code. Referencing
 *             this type before the {@link SQLDataType} class has been
 *             initialised may lead to deadlocks! See <a href=
 *             "https://github.com/jOOQ/jOOQ/issues/3777">https://github.com/jOOQ/jOOQ/issues/3777</a>
 *             for details.
 *             <p>
 *             Use the corresponding {@link SQLDataType} instead.
 */
@Deprecated
public class FirebirdDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>       SMALLINT              = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>     INTEGER               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>     INT                   = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.INTEGER, "int");
    public static final DataType<Long>        BIGINT                = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>        INT64                 = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BIGINT, "int64");
    public static final DataType<Double>      DOUBLEPRECISION       = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>      DOUBLE                = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>      D_FLOAT               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.DOUBLE, "d_float");
    public static final DataType<Float>       FLOAT                 = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.REAL, "float");
    public static final DataType<Boolean>     BOOLEAN               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<BigDecimal>  DECIMAL               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal>  NUMERIC               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>      VARCHAR               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.VARCHAR, "varchar", "varchar(4000)");
    public static final DataType<String>      CHARACTERVARYING      = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.VARCHAR, "character varying", "varchar(4000)");
    public static final DataType<String>      CHAR                  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.CHAR, "char");
    public static final DataType<String>      CHARACTER             = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.CHAR, "character");
    public static final DataType<String>      CLOB                  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.CLOB, "blob sub_type text");
    public static final DataType<Date>        DATE                  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.DATE, "date");
    public static final DataType<Time>        TIME                  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>   TIMESTAMP             = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>      BLOB                  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BLOB, "blob");

    // TODO Below are HSQLDB data types. Fix this

    public static final DataType<Boolean>      BIT                  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BIT, "bit");
    public static final DataType<Object>       OTHER                = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.OTHER, "other");
    public static final DataType<YearToSecond> INTERVAL             = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.INTERVAL, "interval");
    public static final DataType<YearToMonth>  INTERVALYEARTOMONTH  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond>  INTERVALDAYTOSECOND  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.FIREBIRD, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>   __BINARY              = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BINARY, "blob");
    protected static final DataType<Double>   __FLOAT               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.FLOAT, "double precision");
    protected static final DataType<String>   __LONGNVARCHAR        = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.LONGNVARCHAR, "blob sub_type text");
    protected static final DataType<byte[]>   __LONGVARBINARY       = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<String>   __LONGVARCHAR         = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.LONGVARCHAR, "varchar", "varchar(4000)");
    protected static final DataType<String>   __NCHAR               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.NCHAR, "char");
    protected static final DataType<String>   __NCLOB               = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>   __NVARCHAR            = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.NVARCHAR, "varchar", "varchar(4000)");
    protected static final DataType<Byte>     __TINYINT             = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>   __VARBINARY           = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.VARBINARY, "blob");
    protected static final DataType<UByte>    __TINYINTUNSIGNED     = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED    = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED     = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED      = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BIGINTUNSIGNED, "decimal", "varchar(20)"); // There are no large numbers in firebird...?

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER        = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.DECIMAL_INTEGER, "decimal");
    protected static final DataType<UUID>       __UUID              = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>      BLOBSUBTYPEBINARY  = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BLOB, "blob sub_type binary");
    protected static final DataType<byte[]>      BLOBSUBTYPE0       = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.BLOB, "blob sub_type 0");
    protected static final DataType<String>      BLOBSUBTYPETEXT    = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.CLOB, "blob sub_type text");
    protected static final DataType<String>      BLOBSUBTYPE1       = new DefaultDataType<>(SQLDialect.FIREBIRD, SQLDataType.CLOB, "blob sub_type 1");
}
