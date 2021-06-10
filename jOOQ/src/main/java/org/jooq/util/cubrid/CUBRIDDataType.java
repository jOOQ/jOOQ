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
import org.jooq.impl.DefaultDataType;
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

    public static final DataType<Integer>    INT                      = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER                  = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
    public static final DataType<Short>      SHORT                    = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINT, "short");
    public static final DataType<Short>      SMALLINT                 = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Long>       BIGINT                   = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
    public static final DataType<BigDecimal> DECIMAL                  = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> DEC                      = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL, "dec");
    public static final DataType<BigDecimal> NUMERIC                  = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL, "numeric");
    public static final DataType<Float>      FLOAT                    = new DefaultDataType<>(FAMILY, SQLDataType.REAL, "float");
    public static final DataType<Float>      REAL                     = new DefaultDataType<>(FAMILY, SQLDataType.REAL, "real");
    public static final DataType<Double>     DOUBLE                   = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     DOUBLEPRECISION          = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");

    public static final DataType<String>     VARCHAR                  = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     CHARVARYING              = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "char varying");
    public static final DataType<String>     CHARACTERVARYING         = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying");
    public static final DataType<String>     CHAR                     = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "char", "varchar");
    public static final DataType<String>     CHARACTER                = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "character", "varchar");
    public static final DataType<String>     STRING                   = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "string");
    public static final DataType<String>     NCHAR                    = new DefaultDataType<>(FAMILY, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     CLOB                     = new DefaultDataType<>(FAMILY, SQLDataType.CLOB, "clob");

    public static final DataType<Date>       DATE                     = new DefaultDataType<>(FAMILY, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                     = new DefaultDataType<>(FAMILY, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  DATETIME                 = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<Timestamp>  TIMESTAMP                = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp");

    public static final DataType<byte[]>     BITVARYING               = new DefaultDataType<>(FAMILY, SQLDataType.VARBINARY, "bit varying");
    public static final DataType<byte[]>     VARBIT                   = new DefaultDataType<>(FAMILY, SQLDataType.VARBINARY, "varbit");
    public static final DataType<byte[]>     BIT                      = new DefaultDataType<>(FAMILY, SQLDataType.BINARY, "bit");
    public static final DataType<byte[]>     BLOB                     = new DefaultDataType<>(FAMILY, SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.CUBRID, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<Boolean>    __BOOL                = new DefaultDataType<>(FAMILY, SQLDataType.BOOLEAN, "bit", "bit(1)");
    protected static final DataType<Boolean>    __BIT                 = new DefaultDataType<>(FAMILY, SQLDataType.BIT, "bit", "bit(1)");
    protected static final DataType<String>     __LONGNVARCHAR        = new DefaultDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar");
    protected static final DataType<String>     __NCLOB               = new DefaultDataType<>(FAMILY, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>     __NVARCHAR            = new DefaultDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar");
    protected static final DataType<String>     __LONGVARCHAR         = new DefaultDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar");
    protected static final DataType<byte[]>     __LONGVARBINARY       = new DefaultDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<Byte>       __TINYINT             = new DefaultDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
    protected static final DataType<Double>     __FLOAT               = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    protected static final DataType<BigDecimal> __NUMERIC             = new DefaultDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal");
    protected static final DataType<UByte>      __TINYINTUNSIGNED     = new DefaultDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED    = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED     = new DefaultDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED      = new DefaultDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER          = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
    protected static final DataType<UUID>       __UUID                = new DefaultDataType<>(FAMILY, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Double> MONETARY                     = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "monetary");
    public static final DataType<String> ENUM                         = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "enum", "varchar");

    // These types are not yet formally supported
    public static final DataType<Object> OBJECT                       = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "object");
    public static final DataType<Object> OID                          = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "oid");
    public static final DataType<Object> ELO                          = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "elo");
    public static final DataType<Object> MULTISET                     = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "multiset");
    public static final DataType<Object> SEQUENCE                     = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "sequence");
    public static final DataType<Object> SET                          = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "set");
}
