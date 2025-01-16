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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

package org.jooq.util.mariadb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.SQLDialect;
import org.jooq.impl.BuiltInDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

/**
 * Supported data types for the {@link SQLDialect#MARIADB} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://dev.mysql.com/doc/refman/5.5/en/data-types.html">http://dev.mysql.com/doc/refman/5.5/en/data-types.html</a>
 * @see <a href="http://dev.mysql.com/doc/refman/5.5/en/cast-functions.html#function_cast">http://dev.mysql.com/doc/refman/5.5/en/cast-functions.html#function_cast</a>
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
public class MariaDBDataType {

    private static final SQLDialect FAMILY = SQLDialect.MARIADB;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>       TINYINT            = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint", "signed");
    public static final DataType<UByte>      TINYINTUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "tinyint unsigned", "unsigned");
    public static final DataType<Short>      SMALLINT           = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint", "signed");
    public static final DataType<UShort>     SMALLINTUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "smallint unsigned", "unsigned");
    public static final DataType<Integer>    INT                = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int", "signed");
    public static final DataType<UInteger>   INTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "int unsigned", "unsigned");
    public static final DataType<Integer>    MEDIUMINT          = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "mediumint", "signed");
    public static final DataType<UInteger>   MEDIUMINTUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "mediumint unsigned", "unsigned");
    public static final DataType<Integer>    INTEGER            = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer", "signed");
    public static final DataType<UInteger>   INTEGERUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "integer unsigned", "unsigned");
    public static final DataType<Long>       BIGINT             = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint", "signed");
    public static final DataType<ULong>      BIGINTUNSIGNED     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "bigint unsigned", "unsigned");
    public static final DataType<Double>     DOUBLE             = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double", "decimal");
    public static final DataType<Double>     FLOAT              = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float", "decimal");
    public static final DataType<Float>      REAL               = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real", "decimal");
    public static final DataType<Boolean>    BOOLEAN            = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean", "unsigned");
    public static final DataType<Boolean>    BOOL               = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "bool", "unsigned");
    public static final DataType<Boolean>    BIT                = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "bit", "unsigned");
    public static final DataType<BigDecimal> DECIMAL            = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal", "decimal");
    public static final DataType<BigDecimal> DEC                = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec", "decimal");
    public static final DataType<String>     VARCHAR            = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar", "char");
    public static final DataType<String>     CHAR               = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char", "char");
    public static final DataType<String>     TEXT               = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "text", "char");
    public static final DataType<byte[]>     BLOB               = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob", "binary");
    public static final DataType<byte[]>     BINARY             = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "binary", "binary");
    public static final DataType<byte[]>     VARBINARY          = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varbinary", "binary");
    public static final DataType<Date>       DATE               = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date", "date");
    public static final DataType<Time>       TIME               = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time", "time");
    public static final DataType<Timestamp>  TIMESTAMP          = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp", "datetime");
    public static final DataType<Timestamp>  DATETIME           = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime", "datetime");
    public static final DataType<JSON>       JSON               = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "json");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.MARIADB, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<String>     __NCHAR         = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char", "char");
    protected static final DataType<String>     __NCLOB         = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "text", "char");
    protected static final DataType<String>     __LONGNVARCHAR  = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "varchar", "char");
    protected static final DataType<BigDecimal> __NUMERIC       = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "decimal", "decimal");
    protected static final DataType<String>     __NVARCHAR      = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar", "char");
    protected static final DataType<String>     __LONGVARCHAR   = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "varchar", "char");
    protected static final DataType<byte[]>     __LONGVARBINARY = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "varbinary", "binary");

    protected static final DataType<BigDecimal> __DECIMALUNSIGNED = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal unsigned", "decimal");
    protected static final DataType<Double>     __DOUBLEUNSIGNED  = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double unsigned", "decimal");
    protected static final DataType<Double>     __FLOATUNSIGNED   = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float unsigned", "decimal");
    protected static final DataType<Float>      __REALUNSIGNED    = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real unsigned", "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER    = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
    protected static final DataType<UUID>       __UUID          = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar", "char");
    protected static final DataType<JSONB>      __JSONB         = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "json");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<String>     TINYTEXT           = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "tinytext", "char");
    public static final DataType<String>     MEDIUMTEXT         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "mediumtext", "char");
    public static final DataType<String>     LONGTEXT           = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "longtext", "char");
    public static final DataType<String>     ENUM               = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "enum", "char");
    public static final DataType<String>     SET                = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "set", "char");
    public static final DataType<byte[]>     TINYBLOB           = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "tinyblob", "binary");
    public static final DataType<byte[]>     MEDIUMBLOB         = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "mediumblob", "binary");
    public static final DataType<byte[]>     LONGBLOB           = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "longblob", "binary");
    public static final DataType<Date>       YEAR               = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "year", "date");
}
