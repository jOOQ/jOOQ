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
import org.jooq.Decfloat;
import org.jooq.Geography;
import org.jooq.Geometry;
import org.jooq.JSON;
import org.jooq.JSONB;
// ...
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.XML;
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
        static final DataType<Geometry>    GEOMETRY                 = new BuiltInDataType<>(FAMILY, SQLDataType.GEOMETRY, "Geometry");

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


    static class CUBRIDDataType {

        private static final SQLDialect FAMILY = SQLDialect.CUBRID;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Integer>    INT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>    INTEGER                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Short>      SHORT                    = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "short");
        static final DataType<Short>      SMALLINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Long>       BIGINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<BigDecimal> DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal");
        static final DataType<BigDecimal> DEC                      = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec");
        static final DataType<BigDecimal> NUMERIC                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "numeric");
        static final DataType<Float>      FLOAT                    = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "float");
        static final DataType<Float>      REAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Double>     DOUBLE                   = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>     DOUBLEPRECISION          = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");

        static final DataType<String>     VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar");
        static final DataType<String>     CHARVARYING              = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "char varying");
        static final DataType<String>     CHARACTERVARYING         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying");
        static final DataType<String>     CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char", "varchar");
        static final DataType<String>     CHARACTER                = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character", "varchar");
        static final DataType<String>     STRING                   = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "string");
        static final DataType<String>     NCHAR                    = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "nchar");
        static final DataType<String>     CLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "clob");

        static final DataType<Date>       DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>       TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time");
        static final DataType<Timestamp>  DATETIME                 = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime");
        static final DataType<Timestamp>  TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp");

        static final DataType<byte[]>     BITVARYING               = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "bit varying");
        static final DataType<byte[]>     VARBIT                   = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbit");
        static final DataType<byte[]>     BIT                      = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "bit");
        static final DataType<byte[]>     BLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.CUBRID, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<Boolean>    __BOOL                = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bit", "bit(1)");
        static final DataType<Boolean>    __BIT                 = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit", "bit(1)");
        static final DataType<String>     __LONGNVARCHAR        = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar");
        static final DataType<String>     __NCLOB               = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "clob");
        static final DataType<String>     __NVARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar");
        static final DataType<String>     __LONGVARCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar");
        static final DataType<byte[]>     __LONGVARBINARY       = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "blob");
        static final DataType<Byte>       __TINYINT             = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
        static final DataType<Double>     __FLOAT               = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<BigDecimal> __NUMERIC             = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal");
        static final DataType<UByte>      __TINYINTUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>     __SMALLINTUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger>   __INTEGERUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>      __BIGINTUNSIGNED      = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal");
        static final DataType<Year>       __YEAR                = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER          = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
        static final DataType<UUID>       __UUID                = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Double> MONETARY                     = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "monetary");
        static final DataType<String> ENUM                         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "enum", "varchar");

        // These types are not yet formally supported
        static final DataType<Object> OBJECT                       = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "object");
        static final DataType<Object> OID                          = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "oid");
        static final DataType<Object> ELO                          = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "elo");
        static final DataType<Object> MULTISET                     = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "multiset");
        static final DataType<Object> SEQUENCE                     = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "sequence");
        static final DataType<Object> SET                          = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "set");
    }


    static class DerbyDataType {

        private static final SQLDialect FAMILY = SQLDialect.DERBY;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Short>      SMALLINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Integer>    INT                        = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>    INTEGER                    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Long>       BIGINT                     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<Double>     DOUBLE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>     DOUBLEPRECISION            = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Double>     FLOAT                      = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float");
        static final DataType<Float>      REAL                       = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<BigDecimal> DECIMAL                    = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal");
        static final DataType<BigDecimal> DEC                        = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec");
        static final DataType<BigDecimal> NUMERIC                    = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric");
        static final DataType<String>     VARCHAR                    = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar", "varchar(32672)");
        static final DataType<String>     LONGVARCHAR                = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "long varchar");
        static final DataType<String>     CHAR                       = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char", "char(1)");
        static final DataType<String>     CHARACTER                  = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character", "character(1)");
        static final DataType<String>     CLOB                       = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "clob");
        static final DataType<String>     CHARACTERLARGEOBJECT       = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "character large object");
        static final DataType<String>     CHARVARYING                = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "char varying", "char varying(32672)");
        static final DataType<String>     CHARACTERVARYING           = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying", "character varying(32672)");
        static final DataType<Boolean>    BOOLEAN                    = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<Date>       DATE                       = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>       TIME                       = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time");
        static final DataType<Timestamp>  TIMESTAMP                  = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp");
        static final DataType<byte[]>     BLOB                       = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
        static final DataType<byte[]>     BINARYLARGEOBJECT          = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary large object");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.DERBY, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<byte[]>     __BINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "blob");
        static final DataType<Boolean>    __BIT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "boolean");
        static final DataType<byte[]>     __LONGVARBINARY         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "blob");
        static final DataType<String>     __NCHAR                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char", "char(1)");
        static final DataType<String>     __NCLOB                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "clob");
        static final DataType<String>     __LONGNVARCHAR          = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "long varchar");
        static final DataType<String>     __NVARCHAR              = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
        static final DataType<Byte>       __TINYINT               = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
        static final DataType<byte[]>     __VARBINARY             = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "blob");
        static final DataType<UByte>      __TINYINTUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>     __SMALLINTUNSIGNED      = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger>   __INTEGERUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>      __BIGINTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal", "decimal(20)");
        static final DataType<JSON>       __JSON                  = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "clob");
        static final DataType<JSONB>      __JSONB                 = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "blob");
        static final DataType<Year>       __YEAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER            = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal(31)");
        static final DataType<UUID>       __UUID                  = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar", "varchar(36)");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<byte[]>     CHARFORBITDATA             = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "char for bit data");
        static final DataType<byte[]>     CHARACTERFORBITDATA        = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "character for bit data");
        static final DataType<byte[]>     LONGVARCHARFORBITDATA      = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "long varchar for bit data");
        static final DataType<byte[]>     VARCHARFORBITDATA          = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varchar for bit data", "varchar(32672) for bit data");
        static final DataType<byte[]>     CHARVARYINGFORBITDATA      = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "char varying for bit data", "char varying(32672) for bit data");
        static final DataType<byte[]>     CHARACTERVARYINGFORBITDATA = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "character varying for bit data", "character varying (32672) for bit data");
        static final DataType<String>     ORGAPACHEDERBYCATALOGTYPEDESCRIPTOR
                                                                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "org.apache.derby.catalog.TypeDescriptor");
        static final DataType<String>     ORGAPACHEDERBYCATALOGINDEXDESCRIPTOR
                                                                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "org.apache.derby.catalog.IndexDescriptor");
        static final DataType<String>     JAVAIOSERIALIZABLE         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "java.io.Serializable");
    }


    static class FirebirdDataType {

        private static final SQLDialect FAMILY = SQLDialect.FIREBIRD;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Short>          SMALLINT              = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Integer>        INTEGER               = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Integer>        INT                   = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Long>           BIGINT                = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<Long>           INT64                 = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "int64");
        static final DataType<Double>         DOUBLEPRECISION       = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Double>         DOUBLE                = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>         D_FLOAT               = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "d_float");
        static final DataType<Float>          FLOAT                 = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "float");
        static final DataType<Boolean>        BOOLEAN               = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<BigDecimal>     DECIMAL               = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal");
        static final DataType<BigDecimal>     NUMERIC               = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric");
        static final DataType<String>         VARCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar", "varchar(4000)");
        static final DataType<String>         CHARACTERVARYING      = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying", "varchar(4000)");
        static final DataType<String>         CHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char");
        static final DataType<String>         CHARACTER             = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character");
        static final DataType<String>         CLOB                  = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "blob sub_type text");
        static final DataType<Date>           DATE                  = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>           TIME                  = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time");
        static final DataType<Timestamp>      TIMESTAMP             = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp");
        static final DataType<byte[]>         BLOB                  = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
        static final DataType<Decfloat>       DECFLOAT              = new BuiltInDataType<>(FAMILY, SQLDataType.DECFLOAT, "decfloat");
        static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp with time zone");
        static final DataType<Instant>        INSTANT               = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp with time zone");
        static final DataType<OffsetTime>     TIMEWITHTIMEZONE      = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time with time zone");

        // TODO Below are HSQLDB data types. Fix this

        static final DataType<Boolean>      BIT                  = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit");
        static final DataType<Object>       OTHER                = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        static final DataType<YearToSecond> INTERVAL             = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVAL, "interval");
        static final DataType<YearToMonth>  INTERVALYEARTOMONTH  = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
        static final DataType<DayToSecond>  INTERVALDAYTOSECOND  = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.FIREBIRD, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<byte[]>   __BINARY              = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "blob");
        static final DataType<Double>   __FLOAT               = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "double precision");
        static final DataType<String>   __LONGNVARCHAR        = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "blob sub_type text");
        static final DataType<byte[]>   __LONGVARBINARY       = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "blob");
        static final DataType<String>   __LONGVARCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar", "varchar(4000)");
        static final DataType<String>   __NCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char");
        static final DataType<String>   __NCLOB               = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "blob sub_type text");
        static final DataType<String>   __NVARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar", "varchar(4000)");
        static final DataType<Byte>     __TINYINT             = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
        static final DataType<byte[]>   __VARBINARY           = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "blob");
        static final DataType<UByte>    __TINYINTUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>   __SMALLINTUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger> __INTEGERUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>    __BIGINTUNSIGNED      = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal", "varchar(20)"); // There are no large numbers in firebird...?
        static final DataType<JSON>     __JSON                = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "blob sub_type text");
        static final DataType<JSONB>    __JSONB               = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "blob");
        static final DataType<Year>     __YEAR                = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER        = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal");
        static final DataType<UUID>       __UUID              = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar", "varchar(36)");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<byte[]>      BLOBSUBTYPEBINARY  = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob sub_type binary");
        static final DataType<byte[]>      BLOBSUBTYPE0       = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob sub_type 0");
        static final DataType<String>      BLOBSUBTYPETEXT    = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "blob sub_type text");
        static final DataType<String>      BLOBSUBTYPE1       = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "blob sub_type 1");
    }


    static class H2DataType {

        private static final SQLDialect                 FAMILY                       = SQLDialect.H2;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Byte>              TINYINT                      = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
        static final DataType<Short>             SMALLINT                     = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Short>             INT2                         = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "int2");
        static final DataType<Integer>           INT                          = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>           INTEGER                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Integer>           MEDIUMINT                    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "mediumint");
        static final DataType<Integer>           INT4                         = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int4");
        static final DataType<Integer>           SIGNED                       = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "signed");
        static final DataType<Boolean>           BOOLEAN                      = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<Boolean>           BOOL                         = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool");
        static final DataType<Boolean>           BIT                          = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit");
        static final DataType<Long>              BIGINT                       = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<Long>              INT8                         = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "int8");
        static final DataType<BigDecimal>        DECIMAL                      = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        static final DataType<BigDecimal>        DEC                          = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec(p, s)");
        static final DataType<BigDecimal>        NUMERIC                      = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
        // [#10880] [#13043] NUMBER should map to DECFLOAT once supported
        static final DataType<BigDecimal>        NUMBER                       = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "number(p, s)");
        static final DataType<Double>            DOUBLE                       = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>            DOUBLEPRECISION              = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Double>            FLOAT                        = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float");
        static final DataType<Double>            FLOAT4                       = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float4");
        static final DataType<Double>            FLOAT8                       = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float8");
        static final DataType<Float>             REAL                         = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Time>              TIME                         = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Date>              DATE                         = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Timestamp>         TIMESTAMP                    = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
        static final DataType<Timestamp>         DATETIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime(p)");
        static final DataType<OffsetDateTime>    TIMESTAMPWITHTIMEZONE        = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
        static final DataType<Instant>           INSTANT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
        static final DataType<byte[]>            VARBINARY                    = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary(l)");
        static final DataType<byte[]>            BINARY                       = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary(l)");
        static final DataType<byte[]>            BINARYVARYING                = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "binary varying(l)");
        static final DataType<byte[]>            LONGVARBINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "longvarbinary(l)");
        static final DataType<byte[]>            BLOB                         = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
        static final DataType<byte[]>            BINARYLARGEOBJECT            = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary large object");
        static final DataType<Object>            OTHER                        = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        static final DataType<Object>            JAVAOBJECT                   = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "java_object");
        static final DataType<String>            VARCHAR                      = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
        static final DataType<String>            VARCHAR2                     = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar2(l)");
        static final DataType<String>            CHARVARYING                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "char varying(l)");
        static final DataType<String>            CHARACTERVARYING             = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)");
        static final DataType<String>            CHAR                         = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<String>            CHARACTER                    = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
        static final DataType<String>            LONGVARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "longvarchar(l)");
        static final DataType<String>            LONGNVARCHAR                 = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "longnvarchar(l)");
        static final DataType<String>            CLOB                         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "clob");
        static final DataType<String>            CHARLARGEOBJECT              = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "char large object");
        static final DataType<String>            CHARACTERLARGEOBJECT         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "character large object");
        static final DataType<String>            NVARCHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "nvarchar(l)");
        static final DataType<String>            NVARCHAR2                    = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "nvarchar2(l)");
        static final DataType<String>            NATIONALCHARVARYING          = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "national char varying(l)");
        static final DataType<String>            NATIONALCHARACTERVARYING     = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "national character varying(l)");
        static final DataType<String>            NCHAR                        = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "nchar(l)");
        static final DataType<String>            NATIONALCHAR                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "national char(l)");
        static final DataType<String>            NATIONALCHARACTER            = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "national character(l)");
        static final DataType<String>            NCLOB                        = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "nclob");
        static final DataType<String>            NCHARLARGEOBJECT             = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "nchar large object");
        static final DataType<String>            NATIONALCHARLARGEOBJECT      = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "national char large object");
        static final DataType<String>            NATIONALCHARACTERLARGEOBJECT = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "national character large object");
        static final DataType<JSON>              JSON                         = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");
        static final DataType<JSONB>             JSONB                        = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "json");
        static final DataType<YearToMonth>       INTERVALYEARTOMONTH          = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
        static final DataType<DayToSecond>       INTERVALDAYTOSECOND          = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second", "interval day(9) to second");
        static final DataType<Decfloat>          DECFLOAT                     = new BuiltInDataType<>(FAMILY, SQLDataType.DECFLOAT, "decfloat");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.H2, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<Result<Record>> __RESULT                     = new BuiltInDataType<>(FAMILY, SQLDataType.RESULT, "result_set");
        static final DataType<Year>           __YEAR                       = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger>     __BIGINTEGER                 = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");
        static final DataType<UByte>          __TINYINTUNSIGNED            = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>         __SMALLINTUNSIGNED           = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger>       __INTEGERUNSIGNED            = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>          __BIGINTUNSIGNED             = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "number(p, s)");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Long>              IDENTITY                     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "identity");
        static final DataType<Timestamp>         SMALLDATETIME                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "smalldatetime");
        static final DataType<byte[]>            RAW                          = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "raw");
        static final DataType<byte[]>            BYTEA                        = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "bytea");
        static final DataType<byte[]>            TINYBLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "tinyblob");
        static final DataType<byte[]>            MEDIUMBLOB                   = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "mediumblob");
        static final DataType<byte[]>            LONGBLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "longblob");
        static final DataType<byte[]>            IMAGE                        = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "image");
        static final DataType<String>            VARCHAR_CASESENSITIVE        = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar_casesensitive(l)");
        static final DataType<String>            VARCHAR_IGNORECASE           = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar_ignorecase(l)");
        static final DataType<UUID>              UUID                         = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
        static final DataType<String>            TINYTEXT                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "tinytext");
        static final DataType<String>            TEXT                         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "text");
        static final DataType<String>            MEDIUMTEXT                   = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "mediumtext");
        static final DataType<String>            LONGTEXT                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "longtext");
        static final DataType<String>            NTEXT                        = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "ntext");
    }


    static class HSQLDBDataType {

        private static final SQLDialect FAMILY = SQLDialect.HSQLDB;

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
        static final DataType<Boolean>     BIT                      = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit");
        static final DataType<BigDecimal>  DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        static final DataType<BigDecimal>  NUMERIC                  = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
        static final DataType<String>      VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)", "varchar(32672)");
        static final DataType<String>      LONGVARCHAR              = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "longvarchar(l)");
        static final DataType<String>      CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<String>      CHARACTER                = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
        static final DataType<String>      CHARACTERVARYING         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)", "character varying(32672)");
        static final DataType<String>      CLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "clob");
        static final DataType<String>      CHARLARGEOBJECT          = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "char large object", "clob");
        static final DataType<String>      CHARACTERLARGEOBJECT     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "character large object", "clob");
        static final DataType<Date>        DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>        TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Time>        TIMEWITHOUTTIMEZONE      = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
        static final DataType<OffsetTime>  TIMEWITHTIMEZONE         = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
        static final DataType<Timestamp>   TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
        static final DataType<Timestamp>   TIMESTAMPWITHOUTTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
        static final DataType<Timestamp>   DATETIME                 = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime(p)");
        static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
        static final DataType<Instant>     INSTANT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
        static final DataType<byte[]>       LONGVARBINARY            = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "longvarbinary(l)");
        static final DataType<byte[]>       VARBINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary(l)", "varbinary(32672)");
        static final DataType<byte[]>       BINARY                   = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary(l)");
        static final DataType<byte[]>       BLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
        static final DataType<byte[]>       BINARYLARGEOBJECT        = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary large object", "blob");
        static final DataType<Object>       OTHER                    = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        static final DataType<YearToSecond> INTERVAL                 = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVAL, "interval");
        static final DataType<YearToMonth>  INTERVALYEARTOMONTH      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
        static final DataType<DayToSecond>  INTERVALDAYTOSECOND      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second", "interval day(9) to second");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.HSQLDB, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<String>   __NCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
        static final DataType<String>   __NCLOB                  = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "clob");
        static final DataType<String>   __LONGNVARCHAR           = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "longvarchar(l)");
        static final DataType<String>   __NVARCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)", "varchar(32672)");
        static final DataType<UByte>    __TINYINTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>   __SMALLINTUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger> __INTEGERUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>    __BIGINTUNSIGNED         = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal(p, s)");
        static final DataType<JSON>     __JSON                   = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "clob");
        static final DataType<JSONB>    __JSONB                  = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "blob");
        static final DataType<Year>     __YEAR                   = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER           = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<UUID>           UUID                  = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
        static final DataType<String>         VARCHARIGNORECASE     = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar_ignorecase(l)", "varchar_ignorecase(32672)");
        static final DataType<Object>         OBJECT                = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "object");
        static final DataType<Result<Record>> ROW                   = new BuiltInDataType<>(FAMILY, SQLDataType.RESULT, "row");
    }


    static class IgniteDataType {

        private static final SQLDialect FAMILY = SQLDialect.IGNITE;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Byte>            TINYINT              = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
        static final DataType<Short>           SMALLINT             = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Integer>         INT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>         INTEGER              = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Boolean>         BOOLEAN              = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<Long>            BIGINT               = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<BigDecimal>      DECIMAL              = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        static final DataType<BigDecimal>      NUMERIC              = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal(p, s)");
        static final DataType<Double>          DOUBLE               = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>          FLOAT                = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "double");
        static final DataType<Float>           REAL                 = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Time>            TIME                 = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Date>            DATE                 = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Timestamp>       TIMESTAMP            = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
        static final DataType<byte[]>          BINARY               = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary(l)");
        static final DataType<byte[]>          VARBINARY            = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "binary(l)");
        static final DataType<byte[]>          LONGVARBINARY        = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "binary(l)");
        static final DataType<byte[]>          BLOB                 = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary");
        static final DataType<Object>          OTHER                = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        static final DataType<String>          VARCHAR              = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
        static final DataType<String>          CHAR                 = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<String>          CLOB                 = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "varchar");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER       = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");
        static final DataType<UByte>      __TINYINTUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>     __SMALLINTUNSIGNED = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger>   __INTEGERUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>      __BIGINTUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "number(p, s)");
        static final DataType<Year>       __YEAR             = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<UUID>       UUID                  = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
    }


    static class MariaDBDataType {

        private static final SQLDialect FAMILY = SQLDialect.MARIADB;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Byte>       TINYINT            = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint", "signed");
        static final DataType<UByte>      TINYINTUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "tinyint unsigned", "unsigned");
        static final DataType<Short>      SMALLINT           = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint", "signed");
        static final DataType<UShort>     SMALLINTUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "smallint unsigned", "unsigned");
        static final DataType<Integer>    INT                = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int", "signed");
        static final DataType<UInteger>   INTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "int unsigned", "unsigned");
        static final DataType<Integer>    MEDIUMINT          = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "mediumint", "signed");
        static final DataType<UInteger>   MEDIUMINTUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "mediumint unsigned", "unsigned");
        static final DataType<Integer>    INTEGER            = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer", "signed");
        static final DataType<UInteger>   INTEGERUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "integer unsigned", "unsigned");
        static final DataType<Long>       BIGINT             = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint", "signed");
        static final DataType<ULong>      BIGINTUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "bigint unsigned", "unsigned");
        static final DataType<Double>     DOUBLE             = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double", "decimal");
        static final DataType<Double>     FLOAT              = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float", "decimal");
        static final DataType<Float>      REAL               = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real", "decimal");
        static final DataType<Boolean>    BOOLEAN            = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean", "unsigned");
        static final DataType<Boolean>    BOOL               = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool", "unsigned");
        static final DataType<Boolean>    BIT                = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit", "unsigned");
        static final DataType<BigDecimal> DECIMAL            = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal", "decimal");
        static final DataType<BigDecimal> DEC                = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec", "decimal");
        static final DataType<String>     VARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar", "char");
        static final DataType<String>     CHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char", "char");
        static final DataType<String>     TEXT               = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "text", "char");
        static final DataType<byte[]>     BLOB               = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob", "binary");
        static final DataType<byte[]>     BINARY             = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary", "binary");
        static final DataType<byte[]>     VARBINARY          = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary", "binary");
        static final DataType<Date>       DATE               = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date", "date");
        static final DataType<Time>       TIME               = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time", "time");
        static final DataType<Timestamp>  DATETIME           = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime", "datetime");
        static final DataType<Timestamp>  TIMESTAMP          = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp", "datetime");
        static final DataType<JSON>       JSON               = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.MARIADB, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<String>     __NCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char", "char");
        static final DataType<String>     __NCLOB         = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "text", "char");
        static final DataType<String>     __LONGNVARCHAR  = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar", "char");
        static final DataType<BigDecimal> __NUMERIC       = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal", "decimal");
        static final DataType<String>     __NVARCHAR      = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar", "char");
        static final DataType<String>     __LONGVARCHAR   = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar", "char");
        static final DataType<byte[]>     __LONGVARBINARY = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "varbinary", "binary");

        static final DataType<BigDecimal> __DECIMALUNSIGNED = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal unsigned", "decimal");
        static final DataType<Double>     __DOUBLEUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double unsigned", "decimal");
        static final DataType<Double>     __FLOATUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float unsigned", "decimal");
        static final DataType<Float>      __REALUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real unsigned", "decimal");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER    = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
        static final DataType<UUID>       __UUID          = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar", "char");
        static final DataType<JSONB>      __JSONB         = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "json");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<String>     TINYTEXT           = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "tinytext", "char");
        static final DataType<String>     MEDIUMTEXT         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "mediumtext", "char");
        static final DataType<String>     LONGTEXT           = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "longtext", "char");
        static final DataType<String>     ENUM               = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "enum", "char");
        static final DataType<String>     SET                = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "set", "char");
        static final DataType<byte[]>     TINYBLOB           = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "tinyblob", "binary");
        static final DataType<byte[]>     MEDIUMBLOB         = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "mediumblob", "binary");
        static final DataType<byte[]>     LONGBLOB           = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "longblob", "binary");
        static final DataType<Year>       YEAR               = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "year", "signed");
    }


    static class MySQLDataType {

        private static final SQLDialect FAMILY = SQLDialect.MYSQL;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Byte>       TINYINT            = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint", "signed");
        static final DataType<UByte>      TINYINTUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "tinyint unsigned", "unsigned");
        static final DataType<Short>      SMALLINT           = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint", "signed");
        static final DataType<UShort>     SMALLINTUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "smallint unsigned", "unsigned");
        static final DataType<Integer>    INT                = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int", "signed");
        static final DataType<UInteger>   INTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "int unsigned", "unsigned");
        static final DataType<Integer>    MEDIUMINT          = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "mediumint", "signed");
        static final DataType<UInteger>   MEDIUMINTUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "mediumint unsigned", "unsigned");
        static final DataType<Integer>    INTEGER            = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer", "signed");
        static final DataType<UInteger>   INTEGERUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "integer unsigned", "unsigned");
        static final DataType<Long>       BIGINT             = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint", "signed");
        static final DataType<ULong>      BIGINTUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "bigint unsigned", "unsigned");
        static final DataType<Double>     DOUBLE             = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double", "decimal");
        static final DataType<Double>     FLOAT              = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float", "decimal");
        static final DataType<Float>      REAL               = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real", "decimal");
        static final DataType<Boolean>    BOOLEAN            = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean", "unsigned");
        static final DataType<Boolean>    BOOL               = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool", "unsigned");
        static final DataType<Boolean>    BIT                = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit", "unsigned");
        static final DataType<BigDecimal> DECIMAL            = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal", "decimal");
        static final DataType<BigDecimal> DEC                = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec", "decimal");
        static final DataType<String>     VARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar", "char");
        static final DataType<String>     CHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char", "char");
        static final DataType<String>     TEXT               = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "text", "char");
        static final DataType<byte[]>     BLOB               = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob", "binary");
        static final DataType<byte[]>     BINARY             = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary", "binary");
        static final DataType<byte[]>     VARBINARY          = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary", "binary");
        static final DataType<Date>       DATE               = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date", "date");
        static final DataType<Time>       TIME               = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time", "time");
        static final DataType<Timestamp>  DATETIME           = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime", "datetime");
        static final DataType<Timestamp>  TIMESTAMP          = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp", "datetime");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.MYSQL, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<String>     __NCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char", "char");
        static final DataType<String>     __NCLOB         = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "text", "char");
        static final DataType<String>     __LONGNVARCHAR  = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar", "char");
        static final DataType<BigDecimal> __NUMERIC       = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal", "decimal");
        static final DataType<String>     __NVARCHAR      = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar", "char");
        static final DataType<String>     __LONGVARCHAR   = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar", "char");
        static final DataType<byte[]>     __LONGVARBINARY = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "varbinary", "binary");

        static final DataType<BigDecimal> __DECIMALUNSIGNED = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal unsigned", "decimal");
        static final DataType<Double>     __DOUBLEUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double unsigned", "decimal");
        static final DataType<Double>     __FLOATUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float unsigned", "decimal");
        static final DataType<Float>      __REALUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real unsigned", "decimal");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<String>     TINYTEXT           = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "tinytext", "char");
        static final DataType<String>     MEDIUMTEXT         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "mediumtext", "char");
        static final DataType<String>     LONGTEXT           = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "longtext", "char");
        static final DataType<String>     ENUM               = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "enum", "char");
        static final DataType<String>     SET                = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "set", "char");
        static final DataType<byte[]>     TINYBLOB           = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "tinyblob", "binary");
        static final DataType<byte[]>     MEDIUMBLOB         = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "mediumblob", "binary");
        static final DataType<byte[]>     LONGBLOB           = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "longblob", "binary");
        static final DataType<Year>       YEAR               = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "year", "signed");
        static final DataType<JSON>       JSON               = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER    = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
        static final DataType<UUID>       __UUID          = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar", "char");
        static final DataType<JSONB>      __JSONB         = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "json");
    }


    static class PostgresDataType {

        private static final SQLDialect FAMILY = SQLDialect.POSTGRES;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Short>          SMALLINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Short>          INT2                     = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "int2");
        static final DataType<Integer>        INT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>        INTEGER                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Integer>        INT4                     = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int4");
        static final DataType<Long>           BIGINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<Long>           INT8                     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "int8");
        static final DataType<Double>         DOUBLEPRECISION          = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Double>         FLOAT8                   = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float8");
        static final DataType<Float>          REAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Float>          FLOAT4                   = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "float4");
        static final DataType<Boolean>        BOOLEAN                  = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<Boolean>        BOOL                     = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool");
        static final DataType<BigDecimal>     NUMERIC                  = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
        static final DataType<BigDecimal>     DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        static final DataType<String>         VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
        static final DataType<String>         CHARACTERVARYING         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)");
        static final DataType<String>         CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<String>         CHARACTER                = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
        static final DataType<String>         TEXT                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "text");
        static final DataType<Date>           DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>           TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Time>           TIMEWITHOUTTIMEZONE      = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
        static final DataType<OffsetTime>     TIMEWITHTIMEZONE         = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
        static final DataType<OffsetTime>     TIMETZ                   = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "timetz(p)");
        static final DataType<Timestamp>      TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
        static final DataType<Timestamp>      TIMESTAMPWITHOUTTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
        static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE    = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
        static final DataType<OffsetDateTime> TIMESTAMPTZ              = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamptz(p)");
        static final DataType<Instant>        INSTANT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
        static final DataType<byte[]>         BYTEA                    = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "bytea");
        static final DataType<YearToSecond>   INTERVAL                 = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVAL, "interval");
        static final DataType<YearToMonth>    INTERVALYEARTOMONTH      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
        static final DataType<DayToSecond>    INTERVALDAYTOSECOND      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.POSTGRES, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<byte[]>     __BINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "bytea");
        static final DataType<Boolean>    __BIT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "boolean");
        static final DataType<byte[]>     __LONGVARBINARY         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "bytea");
        static final DataType<String>     __LONGVARCHAR           = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar(l)");
        static final DataType<String>     __NCHAR                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
        static final DataType<String>     __NCLOB                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "text");
        static final DataType<String>     __LONGNVARCHAR          = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar(l)");
        static final DataType<String>     __NVARCHAR              = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)");
        static final DataType<Byte>       __TINYINT               = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
        static final DataType<byte[]>     __VARBINARY             = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "bytea");
        static final DataType<UByte>      __TINYINTUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>     __SMALLINTUNSIGNED      = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger>   __INTEGERUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>      __BIGINTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal(p, s)");
        static final DataType<Year>       __YEAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER            = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Short>      SMALLSERIAL                = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallserial");
        static final DataType<Short>      SERIAL2                    = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "serial2");
        static final DataType<Integer>    SERIAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "serial");
        static final DataType<Integer>    SERIAL4                    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "serial4");
        static final DataType<Long>       BIGSERIAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigserial");
        static final DataType<Long>       SERIAL8                    = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "serial8");

    //  [#6852] This type was never really supported and cannot be mapped to BigDecimal automatically by the JDBC driver
    //  public static final DataType<BigDecimal> MONEY                      = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "money");

        static final DataType<String>     BITVARYING                 = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "bit varying(l)");
        static final DataType<String>     VARBIT                     = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varbit(l)");
        static final DataType<String>     BIT                        = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "bit(l)");
        static final DataType<String>     BPCHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "bpchar");
        static final DataType<Record>     RECORD                     = new BuiltInDataType<>(FAMILY, SQLDataType.RECORD, "record");
        static final DataType<Result<Record>> REFCURSOR              = new BuiltInDataType<>(FAMILY, SQLDataType.RESULT, "refcursor");
        static final DataType<Object>     ANY                        = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "any");
        static final DataType<UUID>       UUID                       = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
        static final DataType<JSON>       JSON                       = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");
        static final DataType<JSONB>      JSONB                      = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "jsonb");

        // Meta-table types
        static final DataType<Long>       OID                        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "oid");
        static final DataType<Long>       OIDVECTOR                  = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "oidvector");
        static final DataType<Long>       XID                        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "xid");
        static final DataType<Long>       TID                        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "tid");
        static final DataType<Long>       CID                        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "cid");
        static final DataType<String>     ACLITEM                    = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "aclitem");
        static final DataType<String>     NAME                       = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "name");
        static final DataType<String>     REGPROC                    = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "regproc");
        static final DataType<Object>     VOID                       = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "void");
    }


    static class SQLiteDataType {

        private static final SQLDialect FAMILY = SQLDialect.SQLITE;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Byte>       TINYINT          = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
        static final DataType<Short>      SMALLINT         = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Short>      INT2             = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "int2");
        static final DataType<Integer>    INT              = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>    INTEGER          = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Integer>    MEDIUMINT        = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "mediumint");
        static final DataType<Long>       INT8             = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "int8");
        static final DataType<Long>       BIGINT           = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<BigInteger> UNSIGNEDBIGINT   = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "unsigned big int");
        static final DataType<Double>     DOUBLE           = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
        static final DataType<Double>     DOUBLEPRECISION  = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Float>      REAL             = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Float>      FLOAT            = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "float");
        static final DataType<BigDecimal> NUMERIC          = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric");
        static final DataType<BigDecimal> DECIMAL          = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal");
        static final DataType<String>     LONGVARCHAR      = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "longvarchar");
        static final DataType<String>     CHAR             = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char");
        static final DataType<String>     CHARACTER        = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character");
        static final DataType<String>     VARCHAR          = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar");
        static final DataType<String>     VARYINGCHARACTER = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varying character");
        static final DataType<String>     NCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "nchar");
        static final DataType<String>     NATIVECHARACTER  = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "native character");
        static final DataType<String>     NVARCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "nvarchar");
        static final DataType<String>     CLOB             = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "clob");
        static final DataType<String>     TEXT             = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "text");
        static final DataType<Boolean>    BOOLEAN          = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<Date>       DATE             = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Timestamp>  DATETIME         = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime");
        static final DataType<byte[]>     BLOB             = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
        static final DataType<byte[]>     LONGVARBINARY    = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "blob");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.SQLITE, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<byte[]>   __BINARY           = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "blob");
        static final DataType<Boolean>  __BIT              = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "boolean");
        static final DataType<Double>   __FLOAT            = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "double");
        static final DataType<String>   __NCLOB            = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "nclob");
        static final DataType<String>   __LONGNVARCHAR     = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "nvarchar");
        static final DataType<Time>     __TIME             = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "datetime");
        static final DataType<byte[]>   __VARBINARY        = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "blob");
        static final DataType<UByte>    __TINYINTUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>   __SMALLINTUNSIGNED = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger> __INTEGERUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>    __BIGINTUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "numeric");
        static final DataType<JSON>     __JSON             = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "clob");
        static final DataType<JSONB>    __JSONB            = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "blob");
        static final DataType<Year>     __YEAR             = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<UUID>     __UUID             = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Object>     NULL                = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "null");
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


    static class YugabyteDBDataType {

        private static final SQLDialect FAMILY = SQLDialect.YUGABYTEDB;

        // -------------------------------------------------------------------------
        // Default SQL data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Short>          SMALLINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
        static final DataType<Short>          INT2                     = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "int2");
        static final DataType<Integer>        INT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
        static final DataType<Integer>        INTEGER                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
        static final DataType<Integer>        INT4                     = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int4");
        static final DataType<Long>           BIGINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
        static final DataType<Long>           INT8                     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "int8");
        static final DataType<Double>         DOUBLEPRECISION          = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
        static final DataType<Double>         FLOAT8                   = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float8");
        static final DataType<Float>          REAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
        static final DataType<Float>          FLOAT4                   = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "float4");
        static final DataType<Boolean>        BOOLEAN                  = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
        static final DataType<Boolean>        BOOL                     = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool");
        static final DataType<BigDecimal>     NUMERIC                  = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
        static final DataType<BigDecimal>     DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
        static final DataType<String>         VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
        static final DataType<String>         CHARACTERVARYING         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)");
        static final DataType<String>         CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<String>         CHARACTER                = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
        static final DataType<String>         TEXT                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "text");
        static final DataType<Date>           DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>           TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Time>           TIMEWITHOUTTIMEZONE      = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
        static final DataType<OffsetTime>     TIMEWITHTIMEZONE         = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
        static final DataType<OffsetTime>     TIMETZ                   = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "timetz(p)");
        static final DataType<Timestamp>      TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
        static final DataType<Timestamp>      TIMESTAMPWITHOUTTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
        static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE    = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
        static final DataType<OffsetDateTime> TIMESTAMPTZ              = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamptz(p)");
        static final DataType<Instant>        INSTANT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
        static final DataType<byte[]>         BYTEA                    = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "bytea");
        static final DataType<YearToSecond>   INTERVAL                 = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVAL, "interval");
        static final DataType<YearToMonth>    INTERVALYEARTOMONTH      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
        static final DataType<DayToSecond>    INTERVALDAYTOSECOND      = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.POSTGRES, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<byte[]>     __BINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "bytea");
        static final DataType<Boolean>    __BIT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "boolean");
        static final DataType<byte[]>     __LONGVARBINARY         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "bytea");
        static final DataType<String>     __LONGVARCHAR           = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar(l)");
        static final DataType<String>     __NCHAR                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
        static final DataType<String>     __NCLOB                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "text");
        static final DataType<String>     __LONGNVARCHAR          = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar(l)");
        static final DataType<String>     __NVARCHAR              = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)");
        static final DataType<Byte>       __TINYINT               = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
        static final DataType<byte[]>     __VARBINARY             = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "bytea");
        static final DataType<UByte>      __TINYINTUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
        static final DataType<UShort>     __SMALLINTUNSIGNED      = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
        static final DataType<UInteger>   __INTEGERUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
        static final DataType<ULong>      __BIGINTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal(p, s)");
        static final DataType<Year>       __YEAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER            = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

        // -------------------------------------------------------------------------
        // Dialect-specific data types and synonyms thereof
        // -------------------------------------------------------------------------

        static final DataType<Short>      SMALLSERIAL                = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallserial");
        static final DataType<Short>      SERIAL2                    = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "serial2");
        static final DataType<Integer>    SERIAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "serial");
        static final DataType<Integer>    SERIAL4                    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "serial4");
        static final DataType<Long>       BIGSERIAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigserial");
        static final DataType<Long>       SERIAL8                    = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "serial8");

    //  [#6852] This type was never really supported and cannot be mapped to BigDecimal automatically by the JDBC driver
    //  public static final DataType<BigDecimal> MONEY                      = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "money");

        static final DataType<String>     BITVARYING                 = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "bit varying(l)");
        static final DataType<String>     VARBIT                     = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varbit(l)");
        static final DataType<String>     BIT                        = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "bit(l)");
        static final DataType<String>     BPCHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "bpchar");
        static final DataType<Result<Record>> REFCURSOR              = new BuiltInDataType<>(FAMILY, SQLDataType.RESULT, "refcursor");
        static final DataType<Object>     ANY                        = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "any");
        static final DataType<UUID>       UUID                       = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
        static final DataType<JSON>       JSON                       = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");
        static final DataType<JSONB>      JSONB                      = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "jsonb");

        // Meta-table types
        static final DataType<Long>       OID                        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "oid");
        static final DataType<Long>       OIDVECTOR                  = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "oidvector");
        static final DataType<Long>       XID                        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "xid");
        static final DataType<Long>       TID                        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "tid");
        static final DataType<Long>       CID                        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "cid");
        static final DataType<String>     ACLITEM                    = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "aclitem");
        static final DataType<String>     NAME                       = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "name");
        static final DataType<String>     REGPROC                    = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "regproc");
        static final DataType<Object>     VOID                       = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "void");
    }
}
