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
@Deprecated
public class SQLiteDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>       TINYINT          = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>      SMALLINT         = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>      INT2             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.SMALLINT, "int2");
    public static final DataType<Integer>    INT              = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER          = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>    MEDIUMINT        = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.INTEGER, "mediumint");
    public static final DataType<Long>       INT8             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.BIGINT, "int8");
    public static final DataType<Long>       BIGINT           = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.BIGINT, "bigint");
    public static final DataType<BigInteger> UNSIGNEDBIGINT   = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.DECIMAL_INTEGER, "unsigned big int");
    public static final DataType<Double>     DOUBLE           = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     DOUBLEPRECISION  = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Float>      REAL             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.REAL, "real");
    public static final DataType<Float>      FLOAT            = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.REAL, "float");
    public static final DataType<BigDecimal> NUMERIC          = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.NUMERIC, "numeric");
    public static final DataType<BigDecimal> DECIMAL          = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.DECIMAL, "decimal");
    public static final DataType<String>     LONGVARCHAR      = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.LONGVARCHAR, "longvarchar");
    public static final DataType<String>     CHAR             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.CHAR, "char");
    public static final DataType<String>     CHARACTER        = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.CHAR, "character");
    public static final DataType<String>     VARCHAR          = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     VARYINGCHARACTER = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.VARCHAR, "varying character");
    public static final DataType<String>     NCHAR            = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NATIVECHARACTER  = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.NCHAR, "native character");
    public static final DataType<String>     NVARCHAR         = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>     CLOB             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.CLOB, "clob");
    public static final DataType<String>     TEXT             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.CLOB, "text");
    public static final DataType<Boolean>    BOOLEAN          = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Date>       DATE             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.DATE, "date");
    public static final DataType<Timestamp>  DATETIME         = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<byte[]>     LONGVARBINARY    = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.LONGVARBINARY, "longvarbinary");
    public static final DataType<byte[]>     BLOB             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.BLOB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.SQLITE, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>   __BINARY           = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.BINARY, "longvarbinary");
    protected static final DataType<Boolean>  __BIT              = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.BIT, "boolean");
    protected static final DataType<Double>   __FLOAT            = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.FLOAT, "double");
    protected static final DataType<String>   __NCLOB            = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.NCLOB, "nclob");
    protected static final DataType<String>   __LONGNVARCHAR     = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.LONGNVARCHAR, "nvarchar");
    protected static final DataType<Time>     __TIME             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.TIME, "datetime");
    protected static final DataType<byte[]>   __VARBINARY        = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.VARBINARY, "longvarbinary");
    protected static final DataType<UByte>    __TINYINTUNSIGNED  = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED  = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED   = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.BIGINTUNSIGNED, "numeric");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<UUID>     __UUID             = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Object>     NULL                = new DefaultDataType<>(SQLDialect.SQLITE, SQLDataType.OTHER, "null");
}
