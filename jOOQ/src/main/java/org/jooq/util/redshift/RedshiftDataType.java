/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.util.redshift;

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
 * Supported data types for the {@link SQLDialect#REDSHIFT} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://docs.aws.amazon.com/redshift/latest/dg/c_Supported_data_types.html">http://docs.aws.amazon.com/redshift/latest/dg/c_Supported_data_types.html</a>
 */
public class RedshiftDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>        SMALLINT                 = new DefaultDataType<Short>(SQLDialect.REDSHIFT, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>        INT2                     = new DefaultDataType<Short>(SQLDialect.REDSHIFT, SQLDataType.SMALLINT, "int2");
    public static final DataType<Integer>      INT                      = new DefaultDataType<Integer>(SQLDialect.REDSHIFT, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>      INTEGER                  = new DefaultDataType<Integer>(SQLDialect.REDSHIFT, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>      INT4                     = new DefaultDataType<Integer>(SQLDialect.REDSHIFT, SQLDataType.INTEGER, "int4");
    public static final DataType<Long>         BIGINT                   = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>         INT8                     = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "int8");
    public static final DataType<Double>       DOUBLEPRECISION          = new DefaultDataType<Double>(SQLDialect.REDSHIFT, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>       FLOAT8                   = new DefaultDataType<Double>(SQLDialect.REDSHIFT, SQLDataType.FLOAT, "float8");
    public static final DataType<Float>        REAL                     = new DefaultDataType<Float>(SQLDialect.REDSHIFT, SQLDataType.REAL, "real");
    public static final DataType<Float>        FLOAT4                   = new DefaultDataType<Float>(SQLDialect.REDSHIFT, SQLDataType.REAL, "float4");
    public static final DataType<Boolean>      BOOLEAN                  = new DefaultDataType<Boolean>(SQLDialect.REDSHIFT, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Boolean>      BOOL                     = new DefaultDataType<Boolean>(SQLDialect.REDSHIFT, SQLDataType.BOOLEAN, "bool");
    public static final DataType<BigDecimal>   NUMERIC                  = new DefaultDataType<BigDecimal>(SQLDialect.REDSHIFT, SQLDataType.NUMERIC, "numeric");
    public static final DataType<BigDecimal>   DECIMAL                  = new DefaultDataType<BigDecimal>(SQLDialect.REDSHIFT, SQLDataType.DECIMAL, "decimal");
    public static final DataType<String>       VARCHAR                  = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>       CHARACTERVARYING         = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.VARCHAR, "character varying");
    public static final DataType<String>       CHAR                     = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.CHAR, "char");
    public static final DataType<String>       CHARACTER                = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.CHAR, "character");
    public static final DataType<String>       TEXT                     = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.CLOB, "text");
    public static final DataType<Date>         DATE                     = new DefaultDataType<Date>(SQLDialect.REDSHIFT, SQLDataType.DATE, "date");
    public static final DataType<Time>         TIME                     = new DefaultDataType<Time>(SQLDialect.REDSHIFT, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>    TIMESTAMP                = new DefaultDataType<Timestamp>(SQLDialect.REDSHIFT, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>       BYTEA                    = new DefaultDataType<byte[]>(SQLDialect.REDSHIFT, SQLDataType.BLOB, "bytea");
    public static final DataType<YearToMonth>  INTERVALYEARTOMONTH      = new DefaultDataType<YearToMonth>(SQLDialect.REDSHIFT, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond>  INTERVALDAYTOSECOND      = new DefaultDataType<DayToSecond>(SQLDialect.REDSHIFT, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.REDSHIFT, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>     __BINARY                = new DefaultDataType<byte[]>(SQLDialect.REDSHIFT, SQLDataType.BINARY, "bytea");
    protected static final DataType<Boolean>    __BIT                   = new DefaultDataType<Boolean>(SQLDialect.REDSHIFT, SQLDataType.BIT, "boolean");
    protected static final DataType<byte[]>     __LONGVARBINARY         = new DefaultDataType<byte[]>(SQLDialect.REDSHIFT, SQLDataType.LONGVARBINARY, "bytea");
    protected static final DataType<String>     __LONGVARCHAR           = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.LONGVARCHAR, "varchar");
    protected static final DataType<String>     __NCHAR                 = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.NCHAR, "char");
    protected static final DataType<String>     __NCLOB                 = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.NCLOB, "text");
    protected static final DataType<String>     __LONGNVARCHAR          = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.LONGNVARCHAR, "varchar");
    protected static final DataType<String>     __NVARCHAR              = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.NVARCHAR, "varchar");
    protected static final DataType<Byte>       __TINYINT               = new DefaultDataType<Byte>(SQLDialect.REDSHIFT, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>     __VARBINARY             = new DefaultDataType<byte[]>(SQLDialect.REDSHIFT, SQLDataType.VARBINARY, "bytea");
    protected static final DataType<UByte>      __TINYINTUNSIGNED       = new DefaultDataType<UByte>(SQLDialect.REDSHIFT, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED      = new DefaultDataType<UShort>(SQLDialect.REDSHIFT, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED       = new DefaultDataType<UInteger>(SQLDialect.REDSHIFT, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED        = new DefaultDataType<ULong>(SQLDialect.REDSHIFT, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER            = new DefaultDataType<BigInteger>(SQLDialect.REDSHIFT, SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Integer>    SERIAL                     = new DefaultDataType<Integer>(SQLDialect.REDSHIFT, SQLDataType.INTEGER, "serial");
    public static final DataType<Integer>    SERIAL4                    = new DefaultDataType<Integer>(SQLDialect.REDSHIFT, SQLDataType.INTEGER, "serial4");
    public static final DataType<Long>       BIGSERIAL                  = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "bigserial");
    public static final DataType<Long>       SERIAL8                    = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "serial8");
    public static final DataType<BigDecimal> MONEY                      = new DefaultDataType<BigDecimal>(SQLDialect.REDSHIFT, SQLDataType.DECIMAL, "money");
    public static final DataType<String>     BITVARYING                 = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.VARCHAR, "bit varying");
    public static final DataType<String>     VARBIT                     = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.VARCHAR, "varbit");
    public static final DataType<String>     BIT                        = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.CHAR, "bit");
    public static final DataType<String>     BPCHAR                     = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.CHAR, "bpchar");
    public static final DataType<Time>       TIMEWITHOUTTIMEZONE        = new DefaultDataType<Time>(SQLDialect.REDSHIFT, SQLDataType.TIME, "time without time zone");
    public static final DataType<Time>       TIMEWITHTIMEZONE           = new DefaultDataType<Time>(SQLDialect.REDSHIFT, SQLDataType.TIME, "time with time zone");
    public static final DataType<Time>       TIMETZ                     = new DefaultDataType<Time>(SQLDialect.REDSHIFT, SQLDataType.TIME, "timetz");
    public static final DataType<Timestamp>  TIMESTAMPWITHOUTTIMEZONE   = new DefaultDataType<Timestamp>(SQLDialect.REDSHIFT, SQLDataType.TIMESTAMP, "timestamp without time zone");
    public static final DataType<Timestamp>  TIMESTAMPWITHTIMEZONE      = new DefaultDataType<Timestamp>(SQLDialect.REDSHIFT, SQLDataType.TIMESTAMP, "timestamp with time zone");
    public static final DataType<Timestamp>  TIMESTAMPTZ                = new DefaultDataType<Timestamp>(SQLDialect.REDSHIFT, SQLDataType.TIMESTAMP, "timestamptz");
    public static final DataType<Result<Record>> REFCURSOR              = new DefaultDataType<Result<Record>>(SQLDialect.REDSHIFT, SQLDataType.RESULT, "refcursor");
    public static final DataType<Object>     ANY                        = new DefaultDataType<Object>(SQLDialect.REDSHIFT, SQLDataType.OTHER, "any");
    public static final DataType<UUID>       UUID                       = new DefaultDataType<UUID>(SQLDialect.REDSHIFT, SQLDataType.UUID, "uuid");
    public static final DataType<Object>     JSON                       = new DefaultDataType<Object>(SQLDialect.REDSHIFT, SQLDataType.OTHER, "json");

    // Meta-table types
    public static final DataType<Long>       OID                        = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "oid");
    public static final DataType<Long>       OIDVECTOR                  = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "oidvector");
    public static final DataType<Long>       XID                        = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "xid");
    public static final DataType<Long>       TID                        = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "tid");
    public static final DataType<Long>       CID                        = new DefaultDataType<Long>(SQLDialect.REDSHIFT, SQLDataType.BIGINT, "cid");
    public static final DataType<String>     ACLITEM                    = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.VARCHAR, "aclitem");
    public static final DataType<String>     NAME                       = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.VARCHAR, "name");
    public static final DataType<String>     REGPROC                    = new DefaultDataType<String>(SQLDialect.REDSHIFT, SQLDataType.VARCHAR, "regproc");
}
