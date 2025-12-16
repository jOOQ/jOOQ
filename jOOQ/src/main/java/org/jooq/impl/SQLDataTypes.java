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
// ...
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



































































































































    static class ClickHouseDataType {

        private static final SQLDialect FAMILY = SQLDialect.CLICKHOUSE;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Byte>        TINYINT                  = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
        static final DataType<Byte>        INT8                     = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "Int8");
        static final DataType<Short>       SMALLINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Short>       INT16                    = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "Int16");
        static final DataType<Integer>     INTEGER                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Integer>     INT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>     INT32                    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "Int32");
        static final DataType<Long>        BIGINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<Long>        INT64                    = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "Int64");
        static final DataType<Double>      DOUBLE                   = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>      DOUBLEPRECISION          = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Double>      FLOAT64                  = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "Float64");
        static final DataType<Double>      FLOAT                    = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float");
        static final DataType<Float>       REAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Float>       FLOAT32                  = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "Float32");
        static final DataType<Boolean>     BOOLEAN                  = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<Boolean>     BOOL                     = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool");
        static final DataType<BigDecimal>  DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        static final DataType<BigDecimal>  DECIMAL32                = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "Decimal32(s)");
        static final DataType<BigDecimal>  DECIMAL64                = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "Decimal64(s)");
        static final DataType<BigDecimal>  DECIMAL128               = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "Decimal128(s)");
        static final DataType<BigDecimal>  DECIMAL256               = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "Decimal256(s)");
        static final DataType<String>      STRING                   = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "String");
        static final DataType<String>      VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
        static final DataType<String>      CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<Date>        DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Date>        DATE32                   = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "Date32");
        static final DataType<Time>        TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Timestamp>   TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
        static final DataType<Timestamp>   DATETIME                 = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "DateTime");
        static final DataType<Timestamp>   DATETIME64               = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "DateTime64(p)");
        static final DataType<byte[]>      VARBINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary");
        static final DataType<JSON>        JSON                     = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "JSON");
        static final DataType<Object>      OTHER                    = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        static final DataType<UUID>        UUID                     = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "UUID");

        static final DataType<UByte>       UINT8                    = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "UInt8");
        static final DataType<UShort>      UINT16                   = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "UInt16");
        static final DataType<UInteger>    UINT32                   = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "UInt32");
        static final DataType<ULong>       UINT64                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "UInt64");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Object>      NOTHING                  = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "Nothing");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.CLICKHOUSE, SQLDataTypes
        // -------------------------------------------------------------------------


        static final DataType<Year>        __YEAR                = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");
        static final DataType<String>      __CLOB                = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "varchar");
        static final DataType<byte[]>      __BLOB                = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "varbinary");
        static final DataType<byte[]>      __BINARY              = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "varbinary");
        static final DataType<JSONB>       __JSONB               = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "JSON");
        static final DataType<BigDecimal>  __NUMERIC             = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal(p, s)");
        static final DataType<String>      __LONGVARCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "String");
        static final DataType<String>      __NCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
        static final DataType<String>      __NVARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)");
        static final DataType<String>      __LONGNVARCHAR        = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "String");
        static final DataType<String>      __NCLOB               = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "varchar");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER           = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

    }

    static class DuckDBDataType {

        private static final SQLDialect FAMILY = SQLDialect.DUCKDB;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Byte>         TINYINT                  = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
        static final DataType<Short>        SMALLINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Integer>      INT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>      INTEGER                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Long>         BIGINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<Double>       DOUBLE                   = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>       DOUBLEPRECISION          = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Double>       FLOAT                    = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float");
        static final DataType<Float>        REAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Boolean>      BOOLEAN                  = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<Boolean>      BIT                      = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit");
        static final DataType<BigDecimal>   DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        static final DataType<BigDecimal>   NUMERIC                  = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
        static final DataType<String>       VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
        static final DataType<String>       CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<String>       CHARACTER                = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
        static final DataType<String>       CHARACTERVARYING         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)");
        static final DataType<Date>         DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>         TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Time>         TIMEWITHOUTTIMEZONE      = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
        static final DataType<OffsetTime>   TIMEWITHTIMEZONE         = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
        static final DataType<Timestamp>    TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP(6), "timestamp(p)");
        static final DataType<Timestamp>    TIMESTAMP_S              = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP(0), "timestamp_s");
        static final DataType<Timestamp>    TIMESTAMP_MS             = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP(3), "timestamp_ms");
        static final DataType<Timestamp>    TIMESTAMP_NS             = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP(9), "timestamp_ns");
        static final DataType<Timestamp>    TIMESTAMPWITHOUTTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
        static final DataType<Timestamp>    DATETIME                 = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime(p)");
        static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE  = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
        static final DataType<Instant>      INSTANT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
        static final DataType<byte[]>       VARBINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary(l)");
        static final DataType<byte[]>       BINARY                   = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary(l)");
        static final DataType<byte[]>       BLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
        static final DataType<byte[]>       BINARYLARGEOBJECT        = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary large object", "blob");
        static final DataType<Object>       OTHER                    = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        static final DataType<Object>       NULL                     = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "null");
        static final DataType<YearToSecond> INTERVAL                 = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVAL, "interval");
        static final DataType<YearToMonth>  INTERVALYEARTOMONTH      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
        static final DataType<DayToSecond>  INTERVALDAYTOSECOND      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");
        static final DataType<JSON>         JSON                     = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");
        static final DataType<JSONB>        JSONB                    = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "json");
        static final DataType<UByte>        UTINYINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "utinyint");
        static final DataType<UShort>       USMALLINT                = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "usmallint");
        static final DataType<UInteger>     UINTEGER                 = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "uinteger");
        static final DataType<ULong>        UBIGINT                  = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "ubigint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.DUCKDB, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<String>    __NCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
        static final DataType<String>    __NCLOB                  = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "string");
        static final DataType<String>    __CLOB                   = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "string");
        static final DataType<String>    __LONGNVARCHAR           = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar(l)");
        static final DataType<String>    __LONGVARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar(l)");
        static final DataType<byte[]>    __LONGVARBINARY          = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "varbinary(l)");
        static final DataType<String>    __NVARCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)");
        static final DataType<Year>      __YEAR                   = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER           = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<UUID>           UUID                  = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
        static final DataType<Object>         OBJECT                = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "object");
        static final DataType<Result<Record>> ROW                   = new BuiltInDataType<>(FAMILY, SQLDataType.RESULT, "row");
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
