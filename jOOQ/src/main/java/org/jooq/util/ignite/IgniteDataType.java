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
package org.jooq.util.ignite;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.impl.BuiltInDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

/**
 * Supported data types for the {@link SQLDialect#IGNITE} dialect
 *
 * @author Lukas Eder
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
public class IgniteDataType {

    private static final SQLDialect FAMILY = SQLDialect.IGNITE;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>            TINYINT              = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>           SMALLINT             = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>         INT                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>         INTEGER              = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
    public static final DataType<Boolean>         BOOLEAN              = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Long>            BIGINT               = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
    public static final DataType<BigDecimal>      DECIMAL              = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal(p, s)");
    public static final DataType<BigDecimal>      NUMERIC              = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal(p, s)");
    public static final DataType<Double>          DOUBLE               = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>          FLOAT                = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "double");
    public static final DataType<Float>           REAL                 = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
    public static final DataType<Time>            TIME                 = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time(p)");
    public static final DataType<Date>            DATE                 = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
    public static final DataType<Timestamp>       TIMESTAMP            = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp(p)");
    public static final DataType<byte[]>         BINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary(l)");
    public static final DataType<byte[]>         VARBINARY             = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "binary(l)");
    public static final DataType<byte[]>         LONGVARBINARY         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "binary(l)");
    public static final DataType<byte[]>         BLOB                  = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary");
    public static final DataType<Object>         OTHER                 = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "other");
    public static final DataType<String>         VARCHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar(l)");
    public static final DataType<String>         CHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char(l)");
    public static final DataType<String>         CLOB                  = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "varchar");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER       = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal(p, s)");
    protected static final DataType<UByte>      __TINYINTUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "number(p, s)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<UUID>       UUID                  = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "uuid");
}
