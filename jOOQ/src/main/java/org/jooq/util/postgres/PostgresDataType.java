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

package org.jooq.util.postgres;

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
 * Supported data types for the {@link SQLDialect#POSTGRES} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://www.postgresql.org/docs/9.0/interactive/datatype.html">http://www.postgresql.org/docs/9.0/interactive/datatype.html</a>
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
public class PostgresDataType {

    private static final SQLDialect FAMILY = SQLDialect.POSTGRES;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>          SMALLINT                 = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>          INT2                     = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINT, "int2");
    public static final DataType<Integer>        INT                      = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>        INTEGER                  = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>        INT4                     = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "int4");
    public static final DataType<Long>           BIGINT                   = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>           INT8                     = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "int8");
    public static final DataType<Double>         DOUBLEPRECISION          = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>         FLOAT8                   = new DefaultDataType<>(FAMILY, SQLDataType.FLOAT, "float8");
    public static final DataType<Float>          REAL                     = new DefaultDataType<>(FAMILY, SQLDataType.REAL, "real");
    public static final DataType<Float>          FLOAT4                   = new DefaultDataType<>(FAMILY, SQLDataType.REAL, "float4");
    public static final DataType<Boolean>        BOOLEAN                  = new DefaultDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Boolean>        BOOL                     = new DefaultDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool");
    public static final DataType<BigDecimal>     NUMERIC                  = new DefaultDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
    public static final DataType<BigDecimal>     DECIMAL                  = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
    public static final DataType<String>         VARCHAR                  = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
    public static final DataType<String>         CHARACTERVARYING         = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)");
    public static final DataType<String>         CHAR                     = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
    public static final DataType<String>         CHARACTER                = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
    public static final DataType<String>         TEXT                     = new DefaultDataType<>(FAMILY, SQLDataType.CLOB, "text");
    public static final DataType<Date>           DATE                     = new DefaultDataType<>(FAMILY, SQLDataType.DATE, "date");
    public static final DataType<Time>           TIME                     = new DefaultDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
    public static final DataType<Time>           TIMEWITHOUTTIMEZONE      = new DefaultDataType<>(FAMILY, SQLDataType.TIME, "time(p) without time zone");
    public static final DataType<OffsetTime>     TIMEWITHTIMEZONE         = new DefaultDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "time(p) with time zone");
    public static final DataType<OffsetTime>     TIMETZ                   = new DefaultDataType<>(FAMILY, SQLDataType.TIMEWITHTIMEZONE, "timetz(p)");
    public static final DataType<Timestamp>      TIMESTAMP                = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
    public static final DataType<Timestamp>      TIMESTAMPWITHOUTTIMEZONE = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p) without time zone");
    public static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE    = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
    public static final DataType<OffsetDateTime> TIMESTAMPTZ              = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamptz(p)");
    public static final DataType<Instant>        INSTANT                  = new DefaultDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
    public static final DataType<byte[]>         BYTEA                    = new DefaultDataType<>(FAMILY, SQLDataType.BLOB, "bytea");
    public static final DataType<YearToSecond>   INTERVAL                 = new DefaultDataType<>(FAMILY, SQLDataType.INTERVAL, "interval");
    public static final DataType<YearToMonth>    INTERVALYEARTOMONTH      = new DefaultDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond>    INTERVALDAYTOSECOND      = new DefaultDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.POSTGRES, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>     __BINARY                = new DefaultDataType<>(FAMILY, SQLDataType.BINARY, "bytea");
    protected static final DataType<Boolean>    __BIT                   = new DefaultDataType<>(FAMILY, SQLDataType.BIT, "boolean");
    protected static final DataType<byte[]>     __LONGVARBINARY         = new DefaultDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "bytea");
    protected static final DataType<String>     __LONGVARCHAR           = new DefaultDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar(l)");
    protected static final DataType<String>     __NCHAR                 = new DefaultDataType<>(FAMILY, SQLDataType.NCHAR, "char(l)");
    protected static final DataType<String>     __NCLOB                 = new DefaultDataType<>(FAMILY, SQLDataType.NCLOB, "text");
    protected static final DataType<String>     __LONGNVARCHAR          = new DefaultDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar(l)");
    protected static final DataType<String>     __NVARCHAR              = new DefaultDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar(l)");
    protected static final DataType<Byte>       __TINYINT               = new DefaultDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>     __VARBINARY             = new DefaultDataType<>(FAMILY, SQLDataType.VARBINARY, "bytea");
    protected static final DataType<UByte>      __TINYINTUNSIGNED       = new DefaultDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED      = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED       = new DefaultDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED        = new DefaultDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal(p, s)");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER            = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Integer>    SERIAL                     = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "serial");
    public static final DataType<Integer>    SERIAL4                    = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "serial4");
    public static final DataType<Long>       BIGSERIAL                  = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "bigserial");
    public static final DataType<Long>       SERIAL8                    = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "serial8");

//  [#6852] This type was never really supported and cannot be mapped to BigDecimal automatically by the JDBC driver
//  public static final DataType<BigDecimal> MONEY                      = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL, "money");

    public static final DataType<String>     BITVARYING                 = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "bit varying(l)");
    public static final DataType<String>     VARBIT                     = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "varbit(l)");
    public static final DataType<String>     BIT                        = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "bit(l)");
    public static final DataType<String>     BPCHAR                     = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "bpchar");
    public static final DataType<Result<Record>> REFCURSOR              = new DefaultDataType<>(FAMILY, SQLDataType.RESULT, "refcursor");
    public static final DataType<Object>     ANY                        = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "any");
    public static final DataType<UUID>       UUID                       = new DefaultDataType<>(FAMILY, SQLDataType.UUID, "uuid");
    public static final DataType<JSON>       JSON                       = new DefaultDataType<>(FAMILY, SQLDataType.JSON, "json");
    public static final DataType<JSONB>      JSONB                      = new DefaultDataType<>(FAMILY, SQLDataType.JSONB, "jsonb");

    // Meta-table types
    public static final DataType<Long>       OID                        = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "oid");
    public static final DataType<Long>       OIDVECTOR                  = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "oidvector");
    public static final DataType<Long>       XID                        = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "xid");
    public static final DataType<Long>       TID                        = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "tid");
    public static final DataType<Long>       CID                        = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "cid");
    public static final DataType<String>     ACLITEM                    = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "aclitem");
    public static final DataType<String>     NAME                       = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "name");
    public static final DataType<String>     REGPROC                    = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "regproc");
    public static final DataType<Object>     VOID                       = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "void");
}
