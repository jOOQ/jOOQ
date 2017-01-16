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
 */

package org.jooq.util.postgres;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
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
 * Supported data types for the {@link SQLDialect#POSTGRES} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://www.postgresql.org/docs/9.0/interactive/datatype.html">http://www.postgresql.org/docs/9.0/interactive/datatype.html</a>
 */
public class PostgresDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>          SMALLINT                 = new DefaultDataType<Short>(SQLDialect.POSTGRES, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>          INT2                     = new DefaultDataType<Short>(SQLDialect.POSTGRES, SQLDataType.SMALLINT, "int2");
    public static final DataType<Integer>        INT                      = new DefaultDataType<Integer>(SQLDialect.POSTGRES, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>        INTEGER                  = new DefaultDataType<Integer>(SQLDialect.POSTGRES, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>        INT4                     = new DefaultDataType<Integer>(SQLDialect.POSTGRES, SQLDataType.INTEGER, "int4");
    public static final DataType<Long>           BIGINT                   = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>           INT8                     = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "int8");
    public static final DataType<Double>         DOUBLEPRECISION          = new DefaultDataType<Double>(SQLDialect.POSTGRES, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>         FLOAT8                   = new DefaultDataType<Double>(SQLDialect.POSTGRES, SQLDataType.FLOAT, "float8");
    public static final DataType<Float>          REAL                     = new DefaultDataType<Float>(SQLDialect.POSTGRES, SQLDataType.REAL, "real");
    public static final DataType<Float>          FLOAT4                   = new DefaultDataType<Float>(SQLDialect.POSTGRES, SQLDataType.REAL, "float4");
    public static final DataType<Boolean>        BOOLEAN                  = new DefaultDataType<Boolean>(SQLDialect.POSTGRES, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Boolean>        BOOL                     = new DefaultDataType<Boolean>(SQLDialect.POSTGRES, SQLDataType.BOOLEAN, "bool");
    public static final DataType<BigDecimal>     NUMERIC                  = new DefaultDataType<BigDecimal>(SQLDialect.POSTGRES, SQLDataType.NUMERIC, "numeric");
    public static final DataType<BigDecimal>     DECIMAL                  = new DefaultDataType<BigDecimal>(SQLDialect.POSTGRES, SQLDataType.DECIMAL, "decimal");
    public static final DataType<String>         VARCHAR                  = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>         CHARACTERVARYING         = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.VARCHAR, "character varying");
    public static final DataType<String>         CHAR                     = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.CHAR, "char");
    public static final DataType<String>         CHARACTER                = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.CHAR, "character");
    public static final DataType<String>         TEXT                     = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.CLOB, "text");
    public static final DataType<Date>           DATE                     = new DefaultDataType<Date>(SQLDialect.POSTGRES, SQLDataType.DATE, "date");
    public static final DataType<Time>           TIME                     = new DefaultDataType<Time>(SQLDialect.POSTGRES, SQLDataType.TIME, "time");
    public static final DataType<Time>           TIMEWITHOUTTIMEZONE      = new DefaultDataType<Time>(SQLDialect.POSTGRES, SQLDataType.TIME, "time without time zone");

    public static final DataType<OffsetTime>     TIMEWITHTIMEZONE         = new DefaultDataType<OffsetTime>(SQLDialect.POSTGRES, SQLDataType.TIMEWITHTIMEZONE, "time with time zone");
    public static final DataType<OffsetTime>     TIMETZ                   = new DefaultDataType<OffsetTime>(SQLDialect.POSTGRES, SQLDataType.TIMEWITHTIMEZONE, "timetz");

    public static final DataType<Timestamp>      TIMESTAMP                = new DefaultDataType<Timestamp>(SQLDialect.POSTGRES, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<Timestamp>      TIMESTAMPWITHOUTTIMEZONE = new DefaultDataType<Timestamp>(SQLDialect.POSTGRES, SQLDataType.TIMESTAMP, "timestamp without time zone");

    public static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE    = new DefaultDataType<OffsetDateTime>(SQLDialect.POSTGRES, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamp with time zone");
    public static final DataType<OffsetDateTime> TIMESTAMPTZ              = new DefaultDataType<OffsetDateTime>(SQLDialect.POSTGRES, SQLDataType.TIMESTAMPWITHTIMEZONE, "timestamptz");

    public static final DataType<byte[]>         BYTEA                    = new DefaultDataType<byte[]>(SQLDialect.POSTGRES, SQLDataType.BLOB, "bytea");
    public static final DataType<YearToMonth>    INTERVALYEARTOMONTH      = new DefaultDataType<YearToMonth>(SQLDialect.POSTGRES, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond>    INTERVALDAYTOSECOND      = new DefaultDataType<DayToSecond>(SQLDialect.POSTGRES, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.POSTGRES, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>     __BINARY                = new DefaultDataType<byte[]>(SQLDialect.POSTGRES, SQLDataType.BINARY, "bytea");
    protected static final DataType<Boolean>    __BIT                   = new DefaultDataType<Boolean>(SQLDialect.POSTGRES, SQLDataType.BIT, "boolean");
    protected static final DataType<byte[]>     __LONGVARBINARY         = new DefaultDataType<byte[]>(SQLDialect.POSTGRES, SQLDataType.LONGVARBINARY, "bytea");
    protected static final DataType<String>     __LONGVARCHAR           = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.LONGVARCHAR, "varchar");
    protected static final DataType<String>     __NCHAR                 = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.NCHAR, "char");
    protected static final DataType<String>     __NCLOB                 = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.NCLOB, "text");
    protected static final DataType<String>     __LONGNVARCHAR          = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.LONGNVARCHAR, "varchar");
    protected static final DataType<String>     __NVARCHAR              = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.NVARCHAR, "varchar");
    protected static final DataType<Byte>       __TINYINT               = new DefaultDataType<Byte>(SQLDialect.POSTGRES, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>     __VARBINARY             = new DefaultDataType<byte[]>(SQLDialect.POSTGRES, SQLDataType.VARBINARY, "bytea");
    protected static final DataType<UByte>      __TINYINTUNSIGNED       = new DefaultDataType<UByte>(SQLDialect.POSTGRES, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED      = new DefaultDataType<UShort>(SQLDialect.POSTGRES, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED       = new DefaultDataType<UInteger>(SQLDialect.POSTGRES, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED        = new DefaultDataType<ULong>(SQLDialect.POSTGRES, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER            = new DefaultDataType<BigInteger>(SQLDialect.POSTGRES, SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Integer>    SERIAL                     = new DefaultDataType<Integer>(SQLDialect.POSTGRES, SQLDataType.INTEGER, "serial");
    public static final DataType<Integer>    SERIAL4                    = new DefaultDataType<Integer>(SQLDialect.POSTGRES, SQLDataType.INTEGER, "serial4");
    public static final DataType<Long>       BIGSERIAL                  = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "bigserial");
    public static final DataType<Long>       SERIAL8                    = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "serial8");
    public static final DataType<BigDecimal> MONEY                      = new DefaultDataType<BigDecimal>(SQLDialect.POSTGRES, SQLDataType.DECIMAL, "money");
    public static final DataType<String>     BITVARYING                 = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.VARCHAR, "bit varying");
    public static final DataType<String>     VARBIT                     = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.VARCHAR, "varbit");
    public static final DataType<String>     BIT                        = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.CHAR, "bit");
    public static final DataType<String>     BPCHAR                     = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.CHAR, "bpchar");
    public static final DataType<Result<Record>> REFCURSOR              = new DefaultDataType<Result<Record>>(SQLDialect.POSTGRES, SQLDataType.RESULT, "refcursor");
    public static final DataType<Object>     ANY                        = new DefaultDataType<Object>(SQLDialect.POSTGRES, SQLDataType.OTHER, "any");
    public static final DataType<UUID>       UUID                       = new DefaultDataType<UUID>(SQLDialect.POSTGRES, SQLDataType.UUID, "uuid");
    public static final DataType<Object>     JSON                       = new DefaultDataType<Object>(SQLDialect.POSTGRES, SQLDataType.OTHER, "json");

    // Meta-table types
    public static final DataType<Long>       OID                        = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "oid");
    public static final DataType<Long>       OIDVECTOR                  = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "oidvector");
    public static final DataType<Long>       XID                        = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "xid");
    public static final DataType<Long>       TID                        = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "tid");
    public static final DataType<Long>       CID                        = new DefaultDataType<Long>(SQLDialect.POSTGRES, SQLDataType.BIGINT, "cid");
    public static final DataType<String>     ACLITEM                    = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.VARCHAR, "aclitem");
    public static final DataType<String>     NAME                       = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.VARCHAR, "name");
    public static final DataType<String>     REGPROC                    = new DefaultDataType<String>(SQLDialect.POSTGRES, SQLDataType.VARCHAR, "regproc");
    public static final DataType<Object>     VOID                       = new DefaultDataType<Object>(SQLDialect.POSTGRES, SQLDataType.OTHER, "void");
}
