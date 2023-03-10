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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
import org.jooq.SQLDialect;

/**
 * A wrapper for dialect specific, internal data types.
 * <p>
 * Starting with jOOQ 3.11.0 [#7375], these were made part of jOOQ's internals.
 * They will all be moved here, eventually.
 */
final class SQLDataTypes {

    private SQLDataTypes() {}

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
        static final DataType<String>      VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)", "varchar(32672)");
        static final DataType<String>      CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
        static final DataType<Date>        DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
        static final DataType<Time>        TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
        static final DataType<Time>        TIMEWITHOUTTIMEZONE      = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
        static final DataType<OffsetTime>  TIMEWITHTIMEZONE         = new BuiltInDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
        static final DataType<Timestamp>   TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
        static final DataType<Timestamp>   TIMESTAMPWITHOUTTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
        static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
        static final DataType<Instant>     INSTANT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
        static final DataType<byte[]>      VARBINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary(l)", "varbinary(32672)");
        static final DataType<JSON>        JSON                     = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");
        static final DataType<Object>      OTHER                    = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
        static final DataType<UUID>        UUID                     = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");

        // -------------------------------------------------------------------------
        // Compatibility types for supported SQLDialect.TRINO, SQLDataTypes
        // -------------------------------------------------------------------------

        static final DataType<Year>        __YEAR                = new BuiltInDataType<>(FAMILY, SQLDataType.YEAR, "smallint");
        static final DataType<String>      __CLOB                = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "varchar");
        static final DataType<byte[]>      __BLOB                = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "varbinary");
        static final DataType<JSONB>       __JSONB               = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "json");
        static final DataType<BigDecimal>  __NUMERIC             = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal(p, s)");

        // -------------------------------------------------------------------------
        // Compatibility types for supported Java types
        // -------------------------------------------------------------------------

        static final DataType<BigInteger> __BIGINTEGER           = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

    }

}
