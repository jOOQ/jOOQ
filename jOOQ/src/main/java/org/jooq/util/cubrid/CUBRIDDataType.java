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

package org.jooq.util.cubrid;

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
 * Supported data types for the {@link SQLDialect#CUBRID} dialect
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
public class CUBRIDDataType {

    private static final SQLDialect FAMILY = SQLDialect.CUBRID;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Integer>    INT                      = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER                  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
    public static final DataType<Short>      SHORT                    = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "short");
    public static final DataType<Short>      SMALLINT                 = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Long>       BIGINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
    public static final DataType<BigDecimal> DECIMAL                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> DEC                      = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec");
    public static final DataType<BigDecimal> NUMERIC                  = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "numeric");
    public static final DataType<Float>      FLOAT                    = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "float");
    public static final DataType<Float>      REAL                     = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
    public static final DataType<Double>     DOUBLE                   = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     DOUBLEPRECISION          = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");

    public static final DataType<String>     VARCHAR                  = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     CHARVARYING              = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "char varying");
    public static final DataType<String>     CHARACTERVARYING         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying");
    public static final DataType<String>     CHAR                     = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char", "varchar");
    public static final DataType<String>     CHARACTER                = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character", "varchar");
    public static final DataType<String>     STRING                   = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "string");
    public static final DataType<String>     NCHAR                    = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     CLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "clob");

    public static final DataType<Date>       DATE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                     = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  DATETIME                 = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<Timestamp>  TIMESTAMP                = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp");

    public static final DataType<byte[]>     BITVARYING               = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "bit varying");
    public static final DataType<byte[]>     VARBIT                   = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbit");
    public static final DataType<byte[]>     BIT                      = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "bit");
    public static final DataType<byte[]>     BLOB                     = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.CUBRID, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<Boolean>    __BOOL                = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bit", "bit(1)");
    protected static final DataType<Boolean>    __BIT                 = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit", "bit(1)");
    protected static final DataType<String>     __LONGNVARCHAR        = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar");
    protected static final DataType<String>     __NCLOB               = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>     __NVARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar");
    protected static final DataType<String>     __LONGVARCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar");
    protected static final DataType<byte[]>     __LONGVARBINARY       = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<Byte>       __TINYINT             = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
    protected static final DataType<Double>     __FLOAT               = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    protected static final DataType<BigDecimal> __NUMERIC             = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal");
    protected static final DataType<UByte>      __TINYINTUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED      = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER          = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
    protected static final DataType<UUID>       __UUID                = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Double> MONETARY                     = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "monetary");
    public static final DataType<String> ENUM                         = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "enum", "varchar");

    // These types are not yet formally supported
    public static final DataType<Object> OBJECT                       = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "object");
    public static final DataType<Object> OID                          = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "oid");
    public static final DataType<Object> ELO                          = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "elo");
    public static final DataType<Object> MULTISET                     = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "multiset");
    public static final DataType<Object> SEQUENCE                     = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "sequence");
    public static final DataType<Object> SET                          = new BuiltInDataType<>(FAMILY, SQLDataType.OTHER, "set");
}
