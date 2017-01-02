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

package org.jooq.util.mariadb;

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
 * Supported data types for the {@link SQLDialect#MARIADB} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://dev.mysql.com/doc/refman/5.5/en/data-types.html">http://dev.mysql.com/doc/refman/5.5/en/data-types.html</a>
 * @see <a href="http://dev.mysql.com/doc/refman/5.5/en/cast-functions.html#function_cast">http://dev.mysql.com/doc/refman/5.5/en/cast-functions.html#function_cast</a>
 */
public class MariaDBDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>       TINYINT            = new DefaultDataType<Byte>(SQLDialect.MARIADB, SQLDataType.TINYINT, "tinyint", "signed");
    public static final DataType<UByte>      TINYINTUNSIGNED    = new DefaultDataType<UByte>(SQLDialect.MARIADB, SQLDataType.TINYINTUNSIGNED, "tinyint unsigned", "unsigned");
    public static final DataType<Short>      SMALLINT           = new DefaultDataType<Short>(SQLDialect.MARIADB, SQLDataType.SMALLINT, "smallint", "signed");
    public static final DataType<UShort>     SMALLINTUNSIGNED   = new DefaultDataType<UShort>(SQLDialect.MARIADB, SQLDataType.SMALLINTUNSIGNED, "smallint unsigned", "unsigned");
    public static final DataType<Integer>    INT                = new DefaultDataType<Integer>(SQLDialect.MARIADB, SQLDataType.INTEGER, "int", "signed");
    public static final DataType<UInteger>   INTUNSIGNED        = new DefaultDataType<UInteger>(SQLDialect.MARIADB, SQLDataType.INTEGERUNSIGNED, "int unsigned", "unsigned");
    public static final DataType<Integer>    MEDIUMINT          = new DefaultDataType<Integer>(SQLDialect.MARIADB, SQLDataType.INTEGER, "mediumint", "signed");
    public static final DataType<UInteger>   MEDIUMINTUNSIGNED  = new DefaultDataType<UInteger>(SQLDialect.MARIADB, SQLDataType.INTEGERUNSIGNED, "mediumint unsigned", "unsigned");
    public static final DataType<Integer>    INTEGER            = new DefaultDataType<Integer>(SQLDialect.MARIADB, SQLDataType.INTEGER, "integer", "signed");
    public static final DataType<UInteger>   INTEGERUNSIGNED    = new DefaultDataType<UInteger>(SQLDialect.MARIADB, SQLDataType.INTEGERUNSIGNED, "integer unsigned", "unsigned");
    public static final DataType<Long>       BIGINT             = new DefaultDataType<Long>(SQLDialect.MARIADB, SQLDataType.BIGINT, "bigint", "signed");
    public static final DataType<ULong>      BIGINTUNSIGNED     = new DefaultDataType<ULong>(SQLDialect.MARIADB, SQLDataType.BIGINTUNSIGNED, "bigint unsigned", "unsigned");
    public static final DataType<Double>     DOUBLE             = new DefaultDataType<Double>(SQLDialect.MARIADB, SQLDataType.DOUBLE, "double", "decimal");
    public static final DataType<Double>     FLOAT              = new DefaultDataType<Double>(SQLDialect.MARIADB, SQLDataType.FLOAT, "float", "decimal");
    public static final DataType<Float>      REAL               = new DefaultDataType<Float>(SQLDialect.MARIADB, SQLDataType.REAL, "real", "decimal");
    public static final DataType<Boolean>    BOOLEAN            = new DefaultDataType<Boolean>(SQLDialect.MARIADB, SQLDataType.BOOLEAN, "boolean", "unsigned");
    public static final DataType<Boolean>    BOOL               = new DefaultDataType<Boolean>(SQLDialect.MARIADB, SQLDataType.BOOLEAN, "bool", "unsigned");
    public static final DataType<Boolean>    BIT                = new DefaultDataType<Boolean>(SQLDialect.MARIADB, SQLDataType.BIT, "bit", "unsigned");
    public static final DataType<BigDecimal> DECIMAL            = new DefaultDataType<BigDecimal>(SQLDialect.MARIADB, SQLDataType.DECIMAL, "decimal", "decimal");
    public static final DataType<BigDecimal> DEC                = new DefaultDataType<BigDecimal>(SQLDialect.MARIADB, SQLDataType.DECIMAL, "dec", "decimal");
    public static final DataType<String>     VARCHAR            = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.VARCHAR, "varchar", "char");
    public static final DataType<String>     CHAR               = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.CHAR, "char", "char");
    public static final DataType<String>     TEXT               = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.CLOB, "text", "char");
    public static final DataType<byte[]>     BLOB               = new DefaultDataType<byte[]>(SQLDialect.MARIADB, SQLDataType.BLOB, "blob", "binary");
    public static final DataType<byte[]>     BINARY             = new DefaultDataType<byte[]>(SQLDialect.MARIADB, SQLDataType.BINARY, "binary", "binary");
    public static final DataType<byte[]>     VARBINARY          = new DefaultDataType<byte[]>(SQLDialect.MARIADB, SQLDataType.VARBINARY, "varbinary", "binary");
    public static final DataType<Date>       DATE               = new DefaultDataType<Date>(SQLDialect.MARIADB, SQLDataType.DATE, "date", "date");
    public static final DataType<Time>       TIME               = new DefaultDataType<Time>(SQLDialect.MARIADB, SQLDataType.TIME, "time", "time");
    public static final DataType<Timestamp>  TIMESTAMP          = new DefaultDataType<Timestamp>(SQLDialect.MARIADB, SQLDataType.TIMESTAMP, "timestamp", "datetime");
    public static final DataType<Timestamp>  DATETIME           = new DefaultDataType<Timestamp>(SQLDialect.MARIADB, SQLDataType.TIMESTAMP, "datetime", "datetime");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.MARIADB, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<String>     __NCHAR         = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.NCHAR, "char", "char");
    protected static final DataType<String>     __NCLOB         = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.NCLOB, "clob", "char");
    protected static final DataType<String>     __LONGNVARCHAR  = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.LONGNVARCHAR, "varchar", "char");
    protected static final DataType<BigDecimal> __NUMERIC       = new DefaultDataType<BigDecimal>(SQLDialect.MARIADB, SQLDataType.NUMERIC, "decimal", "decimal");
    protected static final DataType<String>     __NVARCHAR      = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.NVARCHAR, "varchar", "char");
    protected static final DataType<String>     __LONGVARCHAR   = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.LONGVARCHAR, "varchar", "char");
    protected static final DataType<byte[]>     __LONGVARBINARY = new DefaultDataType<byte[]>(SQLDialect.MARIADB, SQLDataType.LONGVARBINARY, "varbinary", "binary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER    = new DefaultDataType<BigInteger>(SQLDialect.MARIADB, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
    protected static final DataType<UUID>       __UUID          = new DefaultDataType<UUID>(SQLDialect.MARIADB, SQLDataType.UUID, "varchar", "char");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<String>     TINYTEXT           = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.CLOB, "tinytext", "char");
    public static final DataType<String>     MEDIUMTEXT         = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.CLOB, "mediumtext", "char");
    public static final DataType<String>     LONGTEXT           = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.CLOB, "longtext", "char");
    public static final DataType<String>     ENUM               = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.VARCHAR, "enum", "char");
    public static final DataType<String>     SET                = new DefaultDataType<String>(SQLDialect.MARIADB, SQLDataType.VARCHAR, "set", "char");
    public static final DataType<byte[]>     TINYBLOB           = new DefaultDataType<byte[]>(SQLDialect.MARIADB, SQLDataType.BLOB, "tinyblob", "binary");
    public static final DataType<byte[]>     MEDIUMBLOB         = new DefaultDataType<byte[]>(SQLDialect.MARIADB, SQLDataType.BLOB, "mediumblob", "binary");
    public static final DataType<byte[]>     LONGBLOB           = new DefaultDataType<byte[]>(SQLDialect.MARIADB, SQLDataType.BLOB, "longblob", "binary");
    public static final DataType<Date>       YEAR               = new DefaultDataType<Date>(SQLDialect.MARIADB, SQLDataType.DATE, "year", "date");
}
