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
package org.jooq.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
// ...
// ...
import org.jooq.util.cubrid.CUBRIDDataType;
// ...
import org.jooq.util.derby.DerbyDataType;
import org.jooq.util.firebird.FirebirdDataType;
import org.jooq.util.h2.H2DataType;
// ...
import org.jooq.util.hsqldb.HSQLDBDataType;
// ...
// ...
import org.jooq.util.mariadb.MariaDBDataType;
import org.jooq.util.mysql.MySQLDataType;
// ...
import org.jooq.util.postgres.PostgresDataType;
// ...
import org.jooq.util.sqlite.SQLiteDataType;
// ...
// ...
// ...



/**
 * The SQL standard data types, as described in {@link Types}.
 * <p>
 * These types are usually the ones that are referenced by generated source
 * code. Most RDBMS have an almost 1:1 mapping between their vendor-specific
 * types and the ones in this class (except Oracle). Some RDBMS also have
 * extensions, e.g. for geospacial data types. See the dialect-specific data
 * type classes for more information.
 *
 * @author Lukas Eder
 */
public final class SQLDataType {

    // -------------------------------------------------------------------------
    // String types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#VARCHAR} type.
     */
    public static final DataType<String> VARCHAR = new DefaultDataType<String>(null, String.class, "varchar");

    /**
     * The {@link Types#VARCHAR} type.
     */
    public static final DataType<String> VARCHAR(int length) {
        return VARCHAR.length(length);
    }

    /**
     * The {@link Types#CHAR} type.
     */
    public static final DataType<String> CHAR = new DefaultDataType<String>(null, String.class, "char");

    /**
     * The {@link Types#CHAR} type.
     */
    public static final DataType<String> CHAR(int length) {
        return CHAR.length(length);
    }

    /**
     * The {@link Types#LONGVARCHAR} type.
     */
    public static final DataType<String> LONGVARCHAR = new DefaultDataType<String>(null, String.class, "longvarchar");

    /**
     * The {@link Types#LONGVARCHAR} type.
     */
    public static final DataType<String> LONGVARCHAR(int length) {
        return LONGVARCHAR.length(length);
    }

    /**
     * The {@link Types#CLOB} type.
     */
    public static final DataType<String> CLOB = new DefaultDataType<String>(null, String.class, "clob");

    /**
     * The {@link Types#CLOB} type.
     */
    public static final DataType<String> CLOB(int length) {
        return CLOB.length(length);
    }

    /**
     * The {@link Types#NVARCHAR} type.
     */
    public static final DataType<String> NVARCHAR = new DefaultDataType<String>(null, String.class, "nvarchar");

    /**
     * The {@link Types#NVARCHAR} type.
     */
    public static final DataType<String> NVARCHAR(int length) {
        return NVARCHAR.length(length);
    }

    /**
     * The {@link Types#NCHAR} type.
     */
    public static final DataType<String> NCHAR = new DefaultDataType<String>(null, String.class, "nchar");

    /**
     * The {@link Types#NCHAR} type.
     */
    public static final DataType<String> NCHAR(int length) {
        return NCHAR.length(length);
    }

    /**
     * The {@link Types#LONGNVARCHAR} type.
     */
    public static final DataType<String> LONGNVARCHAR = new DefaultDataType<String>(null, String.class, "longnvarchar");

    /**
     * The {@link Types#LONGNVARCHAR} type.
     */
    public static final DataType<String> LONGNVARCHAR(int length) {
        return LONGNVARCHAR.length(length);
    }

    /**
     * The {@link Types#NCLOB} type.
     */
    public static final DataType<String> NCLOB = new DefaultDataType<String>(null, String.class, "nclob");

    /**
     * The {@link Types#NCLOB} type.
     */
    public static final DataType<String> NCLOB(int length) {
        return NCLOB.length(length);
    }

    // -------------------------------------------------------------------------
    // Boolean types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#BOOLEAN} type.
     */
    public static final DataType<Boolean> BOOLEAN = new DefaultDataType<Boolean>(null, Boolean.class, "boolean");

    /**
     * The {@link Types#BIT} type.
     */
    public static final DataType<Boolean> BIT = new DefaultDataType<Boolean>(null, Boolean.class, "bit");

    // -------------------------------------------------------------------------
    // Integer types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#TINYINT} type.
     */
    public static final DataType<Byte> TINYINT = new DefaultDataType<Byte>(null, Byte.class, "tinyint");

    /**
     * The {@link Types#SMALLINT} type.
     */
    public static final DataType<Short> SMALLINT = new DefaultDataType<Short>(null, Short.class, "smallint");

    /**
     * The {@link Types#INTEGER} type.
     */
    public static final DataType<Integer> INTEGER = new DefaultDataType<Integer>(null, Integer.class, "integer");

    /**
     * The {@link Types#BIGINT} type.
     */
    public static final DataType<Long> BIGINT = new DefaultDataType<Long>(null, Long.class, "bigint");

    /**
     * The zero-scale {@link Types#DECIMAL} type.
     */
    public static final DataType<BigInteger> DECIMAL_INTEGER = new DefaultDataType<BigInteger>(null, BigInteger.class, "decimal_integer");

    // -------------------------------------------------------------------------
    // Unsigned integer types
    // -------------------------------------------------------------------------

    /**
     * The unsigned {@link Types#TINYINT} type.
     */
    public static final DataType<UByte> TINYINTUNSIGNED = new DefaultDataType<UByte>(null, UByte.class, "tinyint unsigned");

    /**
     * The unsigned {@link Types#SMALLINT} type.
     */
    public static final DataType<UShort> SMALLINTUNSIGNED = new DefaultDataType<UShort>(null, UShort.class, "smallint unsigned");

    /**
     * The unsigned {@link Types#INTEGER} type.
     */
    public static final DataType<UInteger> INTEGERUNSIGNED = new DefaultDataType<UInteger>(null, UInteger.class, "integer unsigned");

    /**
     * The unsigned {@link Types#BIGINT} type.
     */
    public static final DataType<ULong> BIGINTUNSIGNED = new DefaultDataType<ULong>(null, ULong.class, "bigint unsigned");

    // -------------------------------------------------------------------------
    // Floating point types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#DOUBLE} type.
     */
    public static final DataType<Double> DOUBLE = new DefaultDataType<Double>(null, Double.class, "double");

    /**
     * The {@link Types#FLOAT} type.
     */
    public static final DataType<Double> FLOAT = new DefaultDataType<Double>(null, Double.class, "float");

    /**
     * The {@link Types#REAL} type.
     */
    public static final DataType<Float> REAL = new DefaultDataType<Float>(null, Float.class, "real");

    // -------------------------------------------------------------------------
    // Numeric types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#NUMERIC} type.
     */
    public static final DataType<BigDecimal> NUMERIC = new DefaultDataType<BigDecimal>(null, BigDecimal.class, "numeric");

    /**
     * The {@link Types#NUMERIC} type.
     */
    public static final DataType<BigDecimal> NUMERIC(int precision) {
        return NUMERIC.precision(precision);
    }

    /**
     * The {@link Types#NUMERIC} type.
     */
    public static final DataType<BigDecimal> NUMERIC(int precision, int scale) {
        return NUMERIC.precision(precision, scale);
    }

    /**
     * The {@link Types#DECIMAL} type.
     */
    public static final DataType<BigDecimal> DECIMAL = new DefaultDataType<BigDecimal>(null, BigDecimal.class, "decimal");

    /**
     * The {@link Types#DECIMAL} type.
     */
    public static final DataType<BigDecimal> DECIMAL(int precision) {
        return DECIMAL.precision(precision);
    }

    /**
     * The {@link Types#DECIMAL} type.
     */
    public static final DataType<BigDecimal> DECIMAL(int precision, int scale) {
        return DECIMAL.precision(precision, scale);
    }

    // -------------------------------------------------------------------------
    // Datetime types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#DATE} type.
     */
    public static final DataType<Date> DATE = new DefaultDataType<Date>(null, Date.class, "date");

    /**
     * The {@link Types#TIMESTAMP} type.
     */
    public static final DataType<Timestamp> TIMESTAMP = new DefaultDataType<Timestamp>(null, Timestamp.class, "timestamp");

    /**
     * The {@link Types#TIME} type.
     */
    public static final DataType<Time> TIME = new DefaultDataType<Time>(null, Time.class, "time");

    /**
     * The SQL standard <code>INTERVAL YEAR TO MONTH</code> data type.
     */
    public static final DataType<YearToMonth> INTERVALYEARTOMONTH = new DefaultDataType<YearToMonth>(null, YearToMonth.class, "interval year to month");

    /**
     * The SQL standard <code>INTERVAL DAY TO SECOND</code> data type.
     */
    public static final DataType<DayToSecond> INTERVALDAYTOSECOND = new DefaultDataType<DayToSecond>(null, DayToSecond.class, "interval day to second");


    // -------------------------------------------------------------------------
    // JSR310 types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#DATE} type.
     */
    public static final DataType<LocalDate> LOCALDATE = new DefaultDataType<LocalDate>(null, LocalDate.class, "date");

    /**
     * The {@link Types#TIME} type.
     */
    public static final DataType<LocalTime> LOCALTIME = new DefaultDataType<LocalTime>(null, LocalTime.class, "time");

    /**
     * The {@link Types#TIMESTAMP} type.
     */
    public static final DataType<LocalDateTime> LOCALDATETIME = new DefaultDataType<LocalDateTime>(null, LocalDateTime.class, "timestamp");

    /**
     * The {@link Types#TIME_WITH_TIMEZONE} type.
     * <p>
     * The behaviour of this data type is influenced by the JDBC driver and the
     * database that is used. Some databases support actual time zones (as in
     * {@link ZonedDateTime}), other databases support only offsets (as in
     * {@link OffsetDateTime}). Some databases retain the actual time zone
     * information that is stored and reproduce it with every fetch (e.g.
     * {@link SQLDialect#ORACLE}), others use this type as a synonym for a
     * timestamp in UTC (e.g. {@link SQLDialect#POSTGRES}), producing possibly a
     * value in the current time zone of the database or the client. Please
     * refer to your database for more information about the behaviour of this
     * data type.
     */
    public static final DataType<OffsetTime> OFFSETTIME = new DefaultDataType<OffsetTime>(null, OffsetTime.class, "time with time zone");

    /**
     * The {@link Types#TIMESTAMP_WITH_TIMEZONE} type.
     * <p>
     * The behaviour of this data type is influenced by the JDBC driver and the
     * database that is used. Some databases support actual time zones (as in
     * {@link ZonedDateTime}), other databases support only offsets (as in
     * {@link OffsetDateTime}). Some databases retain the actual time zone
     * information that is stored and reproduce it with every fetch (e.g.
     * {@link SQLDialect#ORACLE}), others use this type as a synonym for a
     * timestamp in UTC (e.g. {@link SQLDialect#POSTGRES}), producing possibly a
     * value in the current time zone of the database or the client. Please
     * refer to your database for more information about the behaviour of this
     * data type.
     */
    public static final DataType<OffsetDateTime> OFFSETDATETIME = new DefaultDataType<OffsetDateTime>(null, OffsetDateTime.class, "timestamp with time zone");

    /**
     * The {@link Types#TIME_WITH_TIMEZONE} type.
     * <p>
     * An alias for {@link #OFFSETTIME}
     */
    public static final DataType<OffsetTime> TIMEWITHTIMEZONE = OFFSETTIME;

    /**
     * The {@link Types#TIMESTAMP_WITH_TIMEZONE} type.
     * <p>
     * An alias for {@link #OFFSETDATETIME}
     */
    public static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE = OFFSETDATETIME;


    // -------------------------------------------------------------------------
    // Binary types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#BINARY} type.
     */
    public static final DataType<byte[]> BINARY = new DefaultDataType<byte[]>(null, byte[].class, "binary");

    /**
     * The {@link Types#BINARY} type.
     */
    public static final DataType<byte[]> BINARY(int length) {
        return BINARY.length(length);
    }

    /**
     * The {@link Types#VARBINARY} type.
     */
    public static final DataType<byte[]> VARBINARY = new DefaultDataType<byte[]>(null, byte[].class, "varbinary");

    /**
     * The {@link Types#VARBINARY} type.
     */
    public static final DataType<byte[]> VARBINARY(int length) {
        return VARBINARY.length(length);
    }

    /**
     * The {@link Types#LONGVARBINARY} type.
     */
    public static final DataType<byte[]> LONGVARBINARY = new DefaultDataType<byte[]>(null, byte[].class, "longvarbinary");

    /**
     * The {@link Types#LONGVARBINARY} type.
     */
    public static final DataType<byte[]> LONGVARBINARY(int length) {
        return LONGVARBINARY.length(length);
    }

    /**
     * The {@link Types#BLOB} type.
     */
    public static final DataType<byte[]> BLOB = new DefaultDataType<byte[]>(null, byte[].class, "blob");

    /**
     * The {@link Types#BLOB} type.
     */
    public static final DataType<byte[]> BLOB(int length) {
        return BLOB.length(length);
    }

    // -------------------------------------------------------------------------
    // Other types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#OTHER} type.
     */
    public static final DataType<Object> OTHER = new DefaultDataType<Object>(null, Object.class, "other");

    /**
     * The {@link Types#STRUCT} type.
     */
    public static final DataType<Record> RECORD = new DefaultDataType<Record>(null, Record.class, "record");

    /**
     * The {@link ResultSet} type.
     * <p>
     * This is not a SQL or JDBC standard. This type emulates REF CURSOR types
     * and similar constructs
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final DataType<Result<Record>> RESULT = new DefaultDataType<Result<Record>>(null, (Class) Result.class, "result");

    /**
     * The {@link UUID} type.
     * <p>
     * This is not a SQL or JDBC standard. This type handles UUID types where
     * they are supported
     */
    public static final DataType<UUID> UUID = new DefaultDataType<UUID>(null, UUID.class, "uuid");

    // -------------------------------------------------------------------------
    // Static initialisation of dialect-specific data types
    // -------------------------------------------------------------------------

    static {
        // Load all dialect-specific data types
        // TODO [#650] Make this more reliable using a data type registry

        try {













            Class.forName(CUBRIDDataType.class.getName());
            Class.forName(DerbyDataType.class.getName());
            Class.forName(FirebirdDataType.class.getName());
            Class.forName(H2DataType.class.getName());
            Class.forName(HSQLDBDataType.class.getName());
            Class.forName(MariaDBDataType.class.getName());
            Class.forName(MySQLDataType.class.getName());
            Class.forName(PostgresDataType.class.getName());
            Class.forName(SQLiteDataType.class.getName());
        } catch (Exception ignore) {}
    }

    /**
     * No instances
     */
    private SQLDataType() {}
}
