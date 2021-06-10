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

package org.jooq.util.hsqldb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.JSON;
import org.jooq.JSONB;
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
import org.jooq.types.YearToSecond;

/**
 * Supported data types for the {@link SQLDialect#HSQLDB} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://hsqldb.org/doc/guide/ch09.html#datatypes-section">http://hsqldb.org/doc/guide/ch09.html#datatypes-section</a>
 * @see <a href="http://hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html#sqlgeneral_types_ops-sect">http://hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html#sqlgeneral_types_ops-sect</a>
 * @deprecated - 3.11.0 - [#7375] - This type is part of jOOQ's internal API. Do
 *             not reference this type directly from client code. Referencing
 *             this type before the {@link SQLDataType} class has been
 *             initialised may lead to deadlocks! See <a href=
 *             "https://github.com/jOOQ/jOOQ/issues/3777">https://github.com/jOOQ/jOOQ/issues/3777</a>
 *             for details.
 *             <p>
 *             Use the corresponding {@link SQLDataType} instead.
 */
@Deprecated(forRemoval = true, since = "3.11")
public class HSQLDBDataType {

    private static final SQLDialect FAMILY = SQLDialect.HSQLDB;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>        TINYINT                  = new DefaultDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>       SMALLINT                 = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>     INT                      = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>     INTEGER                  = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
    public static final DataType<Long>        BIGINT                   = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
    public static final DataType<Double>      DOUBLE                   = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>      DOUBLEPRECISION          = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>      FLOAT                    = new DefaultDataType<>(FAMILY, SQLDataType.FLOAT, "float");
    public static final DataType<Float>       REAL                     = new DefaultDataType<>(FAMILY, SQLDataType.REAL, "real");
    public static final DataType<Boolean>     BOOLEAN                  = new DefaultDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Boolean>     BIT                      = new DefaultDataType<>(FAMILY, SQLDataType.BIT, "bit");
    public static final DataType<BigDecimal>  DECIMAL                  = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
    public static final DataType<BigDecimal>  NUMERIC                  = new DefaultDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
    public static final DataType<String>      VARCHAR                  = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)", "varchar(32672)");
    public static final DataType<String>      LONGVARCHAR              = new DefaultDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "longvarchar(l)");
    public static final DataType<String>      CHAR                     = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
    public static final DataType<String>      CHARACTER                = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
    public static final DataType<String>      CHARACTERVARYING         = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)", "character varying(32672)");
    public static final DataType<String>      CLOB                     = new DefaultDataType<>(FAMILY, SQLDataType.CLOB, "clob");
    public static final DataType<String>      CHARLARGEOBJECT          = new DefaultDataType<>(FAMILY, SQLDataType.CLOB, "char large object", "clob");
    public static final DataType<String>      CHARACTERLARGEOBJECT     = new DefaultDataType<>(FAMILY, SQLDataType.CLOB, "character large object", "clob");
    public static final DataType<Date>        DATE                     = new DefaultDataType<>(FAMILY, SQLDataType.DATE, "date");
    public static final DataType<Time>        TIME                     = new DefaultDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
    public static final DataType<Time>        TIMEWITHOUTTIMEZONE      = new DefaultDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
    public static final DataType<OffsetTime>  TIMEWITHTIMEZONE         = new DefaultDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
    public static final DataType<Timestamp>   TIMESTAMP                = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
    public static final DataType<Timestamp>   TIMESTAMPWITHOUTTIMEZONE = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
    public static final DataType<Timestamp>   DATETIME                 = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime(p)");
    public static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
    public static final DataType<Instant>     INSTANT                  = new DefaultDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
    public static final DataType<byte[]>       LONGVARBINARY            = new DefaultDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "longvarbinary(l)");
    public static final DataType<byte[]>       VARBINARY                = new DefaultDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary(l)", "varbinary(32672)");
    public static final DataType<byte[]>       BINARY                   = new DefaultDataType<>(FAMILY, SQLDataType.BINARY, "binary(l)");
    public static final DataType<byte[]>       BLOB                     = new DefaultDataType<>(FAMILY, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>       BINARYLARGEOBJECT        = new DefaultDataType<>(FAMILY, SQLDataType.BLOB, "binary large object", "blob");
    public static final DataType<Object>       OTHER                    = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "other");
    public static final DataType<YearToSecond> INTERVAL                 = new DefaultDataType<>(FAMILY, SQLDataType.INTERVAL, "interval");
    public static final DataType<YearToMonth>  INTERVALYEARTOMONTH      = new DefaultDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond>  INTERVALDAYTOSECOND      = new DefaultDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second", "interval day(9) to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.HSQLDB, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<String>   __NCHAR                  = new DefaultDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
    protected static final DataType<String>   __NCLOB                  = new DefaultDataType<>(FAMILY, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>   __LONGNVARCHAR           = new DefaultDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "longvarchar(l)");
    protected static final DataType<String>   __NVARCHAR               = new DefaultDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)", "varchar(32672)");
    protected static final DataType<UByte>    __TINYINTUNSIGNED        = new DefaultDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED       = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED        = new DefaultDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED         = new DefaultDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal(p, s)");
    protected static final DataType<JSON>     __JSON                   = new DefaultDataType<>(FAMILY, SQLDataType.JSON, "clob");
    protected static final DataType<JSONB>    __JSONB                  = new DefaultDataType<>(FAMILY, SQLDataType.JSONB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER           = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<UUID>           UUID                  = new DefaultDataType<>(FAMILY, SQLDataType.UUID, "uuid");
    public static final DataType<String>         VARCHARIGNORECASE     = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar_ignorecase(l)", "varchar_ignorecase(32672)");
    public static final DataType<Object>         OBJECT                = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "object");
    public static final DataType<Result<Record>> ROW                   = new DefaultDataType<>(FAMILY, SQLDataType.RESULT, "row");
}
