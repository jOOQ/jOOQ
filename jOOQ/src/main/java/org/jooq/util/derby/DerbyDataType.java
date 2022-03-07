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

package org.jooq.util.derby;

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
 * Supported data types for the {@link SQLDialect#DERBY} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://db.apache.org/derby/docs/10.7/ref/crefsqlj31068.html">http://db.apache.org/derby/docs/10.7/ref/crefsqlj31068.html</a>
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
public class DerbyDataType {

    private static final SQLDialect FAMILY = SQLDialect.DERBY;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>      SMALLINT                   = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>    INT                        = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER                    = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGER, "integer");
    public static final DataType<Long>       BIGINT                     = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINT, "bigint");
    public static final DataType<Double>     DOUBLE                     = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     DOUBLEPRECISION            = new BuiltInDataType<>(FAMILY, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>     FLOAT                      = new BuiltInDataType<>(FAMILY, SQLDataType.FLOAT, "float");
    public static final DataType<Float>      REAL                       = new BuiltInDataType<>(FAMILY, SQLDataType.REAL, "real");
    public static final DataType<BigDecimal> DECIMAL                    = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> DEC                        = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL, "dec");
    public static final DataType<BigDecimal> NUMERIC                    = new BuiltInDataType<>(FAMILY, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>     VARCHAR                    = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "varchar", "varchar(32672)");
    public static final DataType<String>     LONGVARCHAR                = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARCHAR, "long varchar");
    public static final DataType<String>     CHAR                       = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "char", "char(1)");
    public static final DataType<String>     CHARACTER                  = new BuiltInDataType<>(FAMILY, SQLDataType.CHAR, "character", "character(1)");
    public static final DataType<String>     CLOB                       = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "clob");
    public static final DataType<String>     CHARACTERLARGEOBJECT       = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "character large object");
    public static final DataType<String>     CHARVARYING                = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "char varying", "char varying(32672)");
    public static final DataType<String>     CHARACTERVARYING           = new BuiltInDataType<>(FAMILY, SQLDataType.VARCHAR, "character varying", "character varying(32672)");
    public static final DataType<Boolean>    BOOLEAN                    = new BuiltInDataType<>(FAMILY, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Date>       DATE                       = new BuiltInDataType<>(FAMILY, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                       = new BuiltInDataType<>(FAMILY, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  TIMESTAMP                  = new BuiltInDataType<>(FAMILY, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>     BLOB                       = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>     BINARYLARGEOBJECT          = new BuiltInDataType<>(FAMILY, SQLDataType.BLOB, "binary large object");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.DERBY, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>     __BINARY                = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "blob");
    protected static final DataType<Boolean>    __BIT                   = new BuiltInDataType<>(FAMILY, SQLDataType.BIT, "boolean");
    protected static final DataType<byte[]>     __LONGVARBINARY         = new BuiltInDataType<>(FAMILY, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<String>     __NCHAR                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCHAR, "char", "char(1)");
    protected static final DataType<String>     __NCLOB                 = new BuiltInDataType<>(FAMILY, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>     __LONGNVARCHAR          = new BuiltInDataType<>(FAMILY, SQLDataType.LONGNVARCHAR, "long varchar");
    protected static final DataType<String>     __NVARCHAR              = new BuiltInDataType<>(FAMILY, SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
    protected static final DataType<Byte>       __TINYINT               = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>     __VARBINARY             = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "blob");
    protected static final DataType<UByte>      __TINYINTUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED      = new BuiltInDataType<>(FAMILY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED       = new BuiltInDataType<>(FAMILY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED        = new BuiltInDataType<>(FAMILY, SQLDataType.BIGINTUNSIGNED, "decimal", "decimal(20)");
    protected static final DataType<JSON>       __JSON                  = new BuiltInDataType<>(FAMILY, SQLDataType.JSON, "clob");
    protected static final DataType<JSONB>      __JSONB                 = new BuiltInDataType<>(FAMILY, SQLDataType.JSONB, "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER            = new BuiltInDataType<>(FAMILY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal(31)");
    protected static final DataType<UUID>       __UUID                  = new BuiltInDataType<>(FAMILY, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<byte[]>     CHARFORBITDATA             = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "char for bit data");
    public static final DataType<byte[]>     CHARACTERFORBITDATA        = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "character for bit data");
    public static final DataType<byte[]>     LONGVARCHARFORBITDATA      = new BuiltInDataType<>(FAMILY, SQLDataType.BINARY, "long varchar for bit data");
    public static final DataType<byte[]>     VARCHARFORBITDATA          = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "varchar for bit data", "varchar(32672) for bit data");
    public static final DataType<byte[]>     CHARVARYINGFORBITDATA      = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "char varying for bit data", "char varying(32672) for bit data");
    public static final DataType<byte[]>     CHARACTERVARYINGFORBITDATA = new BuiltInDataType<>(FAMILY, SQLDataType.VARBINARY, "character varying for bit data", "character varying (32672) for bit data");
    public static final DataType<String>     ORGAPACHEDERBYCATALOGTYPEDESCRIPTOR
                                                                        = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "org.apache.derby.catalog.TypeDescriptor");
    public static final DataType<String>     ORGAPACHEDERBYCATALOGINDEXDESCRIPTOR
                                                                        = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "org.apache.derby.catalog.IndexDescriptor");
    public static final DataType<String>     JAVAIOSERIALIZABLE         = new BuiltInDataType<>(FAMILY, SQLDataType.CLOB, "java.io.Serializable");
}
