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
package org.jooq.util.sqlite;

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
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

/**
 * Supported data types for the {@link SQLDialect#SQLITE} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://www.sqlite.org/datatype3.html">http://www.sqlite.org/datatype3.html</a>
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
public class SQLiteDataType {

    private static final SQLDialect FAMILY = SQLDialect.SQLITE;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>       TINYINT          = new DefaultDataType<>(FAMILY, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>      SMALLINT         = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>      INT2             = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINT, "int2");
    public static final DataType<Integer>    INT              = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER          = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>    MEDIUMINT        = new DefaultDataType<>(FAMILY, SQLDataType.INTEGER, "mediumint");
    public static final DataType<Long>       INT8             = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "int8");
    public static final DataType<Long>       BIGINT           = new DefaultDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
    public static final DataType<BigInteger> UNSIGNEDBIGINT   = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "unsigned big int");
    public static final DataType<Double>     DOUBLE           = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     DOUBLEPRECISION  = new DefaultDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Float>      REAL             = new DefaultDataType<>(FAMILY, SQLDataType.REAL, "real");
    public static final DataType<Float>      FLOAT            = new DefaultDataType<>(FAMILY, SQLDataType.REAL, "float");
    public static final DataType<BigDecimal> NUMERIC          = new DefaultDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric");
    public static final DataType<BigDecimal> DECIMAL          = new DefaultDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal");
    public static final DataType<String>     LONGVARCHAR      = new DefaultDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "longvarchar");
    public static final DataType<String>     CHAR             = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "char");
    public static final DataType<String>     CHARACTER        = new DefaultDataType<>(FAMILY, SQLDataType.CHAR, "character");
    public static final DataType<String>     VARCHAR          = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     VARYINGCHARACTER = new DefaultDataType<>(FAMILY, SQLDataType.VARCHAR, "varying character");
    public static final DataType<String>     NCHAR            = new DefaultDataType<>(FAMILY, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NATIVECHARACTER  = new DefaultDataType<>(FAMILY, SQLDataType.NCHAR, "native character");
    public static final DataType<String>     NVARCHAR         = new DefaultDataType<>(FAMILY, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>     CLOB             = new DefaultDataType<>(FAMILY, SQLDataType.CLOB, "clob");
    public static final DataType<String>     TEXT             = new DefaultDataType<>(FAMILY, SQLDataType.CLOB, "text");
    public static final DataType<Boolean>    BOOLEAN          = new DefaultDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Date>       DATE             = new DefaultDataType<>(FAMILY, SQLDataType.DATE, "date");
    public static final DataType<Timestamp>  DATETIME         = new DefaultDataType<>(FAMILY, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<byte[]>     LONGVARBINARY    = new DefaultDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "longvarbinary");
    public static final DataType<byte[]>     BLOB             = new DefaultDataType<>(FAMILY, SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.SQLITE, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>   __BINARY           = new DefaultDataType<>(FAMILY, SQLDataType.BINARY, "longvarbinary");
    protected static final DataType<Boolean>  __BIT              = new DefaultDataType<>(FAMILY, SQLDataType.BIT, "boolean");
    protected static final DataType<Double>   __FLOAT            = new DefaultDataType<>(FAMILY, SQLDataType.FLOAT, "double");
    protected static final DataType<String>   __NCLOB            = new DefaultDataType<>(FAMILY, SQLDataType.NCLOB, "nclob");
    protected static final DataType<String>   __LONGNVARCHAR     = new DefaultDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "nvarchar");
    protected static final DataType<Time>     __TIME             = new DefaultDataType<>(FAMILY, SQLDataType.TIME, "datetime");
    protected static final DataType<byte[]>   __VARBINARY        = new DefaultDataType<>(FAMILY, SQLDataType.VARBINARY, "longvarbinary");
    protected static final DataType<UByte>    __TINYINTUNSIGNED  = new DefaultDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED = new DefaultDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED  = new DefaultDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED   = new DefaultDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "numeric");
    protected static final DataType<JSON>     __JSON             = new DefaultDataType<>(FAMILY, SQLDataType.JSON, "clob");
    protected static final DataType<JSONB>    __JSONB            = new DefaultDataType<>(FAMILY, SQLDataType.JSONB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<UUID>     __UUID             = new DefaultDataType<>(FAMILY, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Object>     NULL                = new DefaultDataType<>(FAMILY, SQLDataType.OTHER, "null");
}
