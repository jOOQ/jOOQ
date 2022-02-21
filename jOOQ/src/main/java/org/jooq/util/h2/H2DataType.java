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
package org.jooq.util.h2;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.BuiltInDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;

/**
 * Supported data types for the {@link SQLDialect#H2} dialect
 *
 * @author Lukas Eder
 * @see <a href=
 *      "http://www.h2database.com/html/datatypes.html">http://www.h2database.com/html/datatypes.html</a>
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
public class H2DataType {

    private static final SQLDialect                 FAMILY                       = SQLDialect.H2;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>              TINYINT                      = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>             SMALLINT                     = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>             INT2                         = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "int2");
    public static final DataType<Integer>           INT                          = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>           INTEGER                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>           MEDIUMINT                    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "mediumint");
    public static final DataType<Integer>           INT4                         = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int4");
    public static final DataType<Integer>           SIGNED                       = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "signed");
    public static final DataType<Boolean>           BOOLEAN                      = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Boolean>           BOOL                         = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool");
    public static final DataType<Boolean>           BIT                          = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit");
    public static final DataType<Long>              BIGINT                       = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>              INT8                         = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "int8");
    public static final DataType<BigDecimal>        DECIMAL                      = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
    public static final DataType<BigDecimal>        DEC                          = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec(p, s)");
    public static final DataType<BigDecimal>        NUMERIC                      = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric(p, s)");
    // [#10880] [#13043] NUMBER should map to DECFLOAT once supported
    public static final DataType<BigDecimal>        NUMBER                       = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "number(p, s)");
    public static final DataType<Double>            DOUBLE                       = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>            DOUBLEPRECISION              = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>            FLOAT                        = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float");
    public static final DataType<Double>            FLOAT4                       = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float4");
    public static final DataType<Double>            FLOAT8                       = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float8");
    public static final DataType<Float>             REAL                         = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
    public static final DataType<Time>              TIME                         = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
    public static final DataType<Date>              DATE                         = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
    public static final DataType<Timestamp>         TIMESTAMP                    = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
    public static final DataType<Timestamp>         DATETIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime(p)");
    public static final DataType<OffsetDateTime>    TIMESTAMPWITHTIMEZONE        = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp(p) with time zone");
    public static final DataType<Instant>           INSTANT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INSTANT, "timestamp(p) with time zone");
    public static final DataType<byte[]>            BINARY                       = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary(l)");
    public static final DataType<byte[]>            VARBINARY                    = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary(l)");
    public static final DataType<byte[]>            BINARYVARYING                = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "binary varying(l)");
    public static final DataType<byte[]>            LONGVARBINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "longvarbinary(l)");
    public static final DataType<byte[]>            BLOB                         = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>            BINARYLARGEOBJECT            = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary large object");
    public static final DataType<Object>            OTHER                        = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
    public static final DataType<Object>            JAVAOBJECT                   = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "java_object");
    public static final DataType<String>            VARCHAR                      = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
    public static final DataType<String>            VARCHAR2                     = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar2(l)");
    public static final DataType<String>            CHARVARYING                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "char varying(l)");
    public static final DataType<String>            CHARACTERVARYING             = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying(l)");
    public static final DataType<String>            CHAR                         = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
    public static final DataType<String>            CHARACTER                    = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character(l)");
    public static final DataType<String>            LONGVARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "longvarchar(l)");
    public static final DataType<String>            CLOB                         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "clob");
    public static final DataType<String>            CHARLARGEOBJECT              = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "char large object");
    public static final DataType<String>            CHARACTERLARGEOBJECT         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "character large object");
    public static final DataType<String>            NVARCHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "nvarchar(l)");
    public static final DataType<String>            NVARCHAR2                    = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "nvarchar2(l)");
    public static final DataType<String>            NATIONALCHARVARYING          = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "national char varying(l)");
    public static final DataType<String>            NATIONALCHARACTERVARYING     = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "national character varying(l)");
    public static final DataType<String>            NCHAR                        = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "nchar(l)");
    public static final DataType<String>            NATIONALCHAR                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "national char(l)");
    public static final DataType<String>            NATIONALCHARACTER            = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "national character(l)");
    public static final DataType<String>            NCLOB                        = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "nclob");
    public static final DataType<String>            NCHARLARGEOBJECT             = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "nchar large object");
    public static final DataType<String>            NATIONALCHARLARGEOBJECT      = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "national char large object");
    public static final DataType<String>            NATIONALCHARACTERLARGEOBJECT = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "national character large object");
    public static final DataType<JSON>              JSON                         = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");
    public static final DataType<JSONB>             JSONB                        = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "json");
    public static final DataType<YearToMonth>       INTERVALYEARTOMONTH          = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond>       INTERVALDAYTOSECOND          = new BuiltInDataType<>(FAMILY, SQLDataType.INTERVALDAYTOSECOND, "interval day to second", "interval day(9) to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.H2, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<String>         __LONGNVARCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "longvarchar(l)");
    protected static final DataType<Result<Record>> __RESULT                     = new BuiltInDataType<>(FAMILY, SQLDataType.RESULT, "result_set");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger>     __BIGINTEGER                 = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");
    protected static final DataType<UByte>          __TINYINTUNSIGNED            = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>         __SMALLINTUNSIGNED           = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>       __INTEGERUNSIGNED            = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>          __BIGINTUNSIGNED             = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "number(p, s)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>             YEAR                         = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "year");
    public static final DataType<Long>              IDENTITY                     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "identity");
    public static final DataType<Timestamp>         SMALLDATETIME                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "smalldatetime");
    public static final DataType<byte[]>            RAW                          = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "raw");
    public static final DataType<byte[]>            BYTEA                        = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "bytea");
    public static final DataType<byte[]>            TINYBLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "tinyblob");
    public static final DataType<byte[]>            MEDIUMBLOB                   = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "mediumblob");
    public static final DataType<byte[]>            LONGBLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "longblob");
    public static final DataType<byte[]>            IMAGE                        = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "image");
    public static final DataType<String>            VARCHAR_CASESENSITIVE        = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar_casesensitive(l)");
    public static final DataType<String>            VARCHAR_IGNORECASE           = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar_ignorecase(l)");
    public static final DataType<UUID>              UUID                         = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
    public static final DataType<String>            TINYTEXT                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "tinytext");
    public static final DataType<String>            TEXT                         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "text");
    public static final DataType<String>            MEDIUMTEXT                   = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "mediumtext");
    public static final DataType<String>            LONGTEXT                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "longtext");
    public static final DataType<String>            NTEXT                        = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "ntext");
}
