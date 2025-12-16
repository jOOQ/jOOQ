/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;

/**
 * A wrapper for dialect specific, internal data types.
 * <p>
 * Starting with jOOQ 3.11.0 [#7375], these were made part of jOOQ's internals.
 * They will all be moved here, eventually.
 */
final class SQLDataTypes {

    private SQLDataTypes() {}

    static class DuckDBDataType {

        private static final SQLDialect FAMILY = SQLDialect.DUCKDB;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        public static final DataType<Byte>         TINYINT                  = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
        public static final DataType<Short>        SMALLINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        public static final DataType<Integer>      INT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        public static final DataType<Integer>      INTEGER                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        public static final DataType<Long>         BIGINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        public static final DataType<Double>       DOUBLE                   = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        public static final DataType<Double>       DOUBLEPRECISION          = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        public static final DataType<Double>       FLOAT                    = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float");
        public static final DataType<Float>        REAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        public static final DataType<Boolean>      BOOLEAN                  = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        public static final DataType<Boolean>      BIT                      = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit");
        public static final DataType<BigDecimal>   DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        public static final DataType<BigDecimal>   NUMERIC                  = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
        public static final DataType<String>       VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
        public static final DataType<String>       LONGVARCHAR              = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "longvarchar(l)");
        public static final DataType<String>       CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        public static final DataType<String>       CHARACTER                = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
        public static final DataType<String>       CHARACTERVARYING         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)");
        public static final DataType<Date>         DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        public static final DataType<Time>         TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        public static final DataType<Time>         TIMEWITHOUTTIMEZONE      = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
        public static final DataType<OffsetTime>   TIMEWITHTIMEZONE         = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
        public static final DataType<Timestamp>    TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP(6), "timestamp(p)");
        public static final DataType<Timestamp>    TIMESTAMP_S              = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP(0), "timestamp_s");
        public static final DataType<Timestamp>    TIMESTAMP_MS             = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP(3), "timestamp_ms");
        public static final DataType<Timestamp>    TIMESTAMP_NS             = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP(9), "timestamp_ns");
        public static final DataType<Timestamp>    TIMESTAMPWITHOUTTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
        public static final DataType<Timestamp>    DATETIME                 = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime(p)");
        public static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE  = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
        public static final DataType<Instant>      INSTANT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
        public static final DataType<byte[]>       LONGVARBINARY            = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "longvarbinary(l)");
        public static final DataType<byte[]>       VARBINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary(l)");
        public static final DataType<byte[]>       BINARY                   = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary(l)");
        public static final DataType<byte[]>       BLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
        public static final DataType<byte[]>       BINARYLARGEOBJECT        = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary large object", "blob");
        public static final DataType<Object>       OTHER                    = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        public static final DataType<YearToSecond> INTERVAL                 = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVAL, "interval");
        public static final DataType<YearToMonth>  INTERVALYEARTOMONTH      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
        public static final DataType<DayToSecond>  INTERVALDAYTOSECOND      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.HSQLDB, SQLDataTypes
        // -------------------------------------------------------------------------

        protected static final DataType<String>    __NCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
        protected static final DataType<String>    __NCLOB                  = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "string");
        protected static final DataType<String>    __CLOB                   = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "string");
        protected static final DataType<String>    __LONGNVARCHAR           = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "longvarchar(l)");
        protected static final DataType<String>    __NVARCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)");
        protected static final DataType<UByte>     __TINYINTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        protected static final DataType<UShort>    __SMALLINTUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        protected static final DataType<UInteger>  __INTEGERUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        protected static final DataType<ULong>     __BIGINTUNSIGNED         = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal(p, s)");
        protected static final DataType<JSON>      __JSON                   = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "clob");
        protected static final DataType<JSONB>     __JSONB                  = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "blob");
        protected static final DataType<Year>      __YEAR                   = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        protected static final DataType<BigInteger> __BIGINTEGER           = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        public static final DataType<UUID>           UUID                  = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
        public static final DataType<Object>         OBJECT                = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "object");
        public static final DataType<Result<Record>> ROW                   = new BuiltInDataType<>(FAMILY, SQLDataType.RESULT, "row");
    }


    static class TrinoDataType {

        private static final SQLDialect FAMILY = SQLDialect.TRINO;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Byte>        TINYINT                  = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
        static final DataType<Short>       SMALLINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Integer>     INT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>     INTEGER                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Long>        BIGINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<Double>      DOUBLE                   = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>      DOUBLEPRECISION          = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Double>      FLOAT                    = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float");
        static final DataType<Float>       REAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Boolean>     BOOLEAN                  = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<BigDecimal>  DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        static final DataType<String>      VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
        static final DataType<String>      CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<Date>        DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>        TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Time>        TIMEWITHOUTTIMEZONE      = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
        static final DataType<OffsetTime>  TIMEWITHTIMEZONE         = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
        static final DataType<Timestamp>   TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
        static final DataType<Timestamp>   TIMESTAMPWITHOUTTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
        static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
        static final DataType<Instant>     INSTANT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
        static final DataType<byte[]>      VARBINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary");
        static final DataType<JSON>        JSON                     = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");
        static final DataType<Object>      OTHER                    = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        static final DataType<UUID>        UUID                     = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.TRINO, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<Year>        __YEAR                = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");
        static final DataType<String>      __CLOB                = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "varchar");
        static final DataType<byte[]>      __BLOB                = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "varbinary");
        static final DataType<byte[]>      __BINARY              = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "varbinary");
        static final DataType<byte[]>      __LONGVARBINARY       = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "varbinary");
        static final DataType<JSONB>       __JSONB               = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "json");
        static final DataType<BigDecimal>  __NUMERIC             = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal(p, s)");
        static final DataType<String>      __LONGVARCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar");
        static final DataType<String>      __NCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
        static final DataType<String>      __NVARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)");
        static final DataType<String>      __LONGNVARCHAR        = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar");
        static final DataType<String>      __NCLOB               = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "varchar");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER           = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

    }
}
