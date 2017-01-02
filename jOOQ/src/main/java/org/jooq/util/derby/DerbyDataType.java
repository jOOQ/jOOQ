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

package org.jooq.util.derby;

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
 * Supported data types for the {@link SQLDialect#DERBY} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://db.apache.org/derby/docs/10.7/ref/crefsqlj31068.html">http://db.apache.org/derby/docs/10.7/ref/crefsqlj31068.html</a>
 */
public class DerbyDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Short>      SMALLINT                   = new DefaultDataType<Short>(SQLDialect.DERBY, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>    INT                        = new DefaultDataType<Integer>(SQLDialect.DERBY, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>    INTEGER                    = new DefaultDataType<Integer>(SQLDialect.DERBY, SQLDataType.INTEGER, "integer");
    public static final DataType<Long>       BIGINT                     = new DefaultDataType<Long>(SQLDialect.DERBY, SQLDataType.BIGINT, "bigint");
    public static final DataType<Double>     DOUBLE                     = new DefaultDataType<Double>(SQLDialect.DERBY, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>     DOUBLEPRECISION            = new DefaultDataType<Double>(SQLDialect.DERBY, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>     FLOAT                      = new DefaultDataType<Double>(SQLDialect.DERBY, SQLDataType.FLOAT, "float");
    public static final DataType<Float>      REAL                       = new DefaultDataType<Float>(SQLDialect.DERBY, SQLDataType.REAL, "real");
    public static final DataType<BigDecimal> DECIMAL                    = new DefaultDataType<BigDecimal>(SQLDialect.DERBY, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal> DEC                        = new DefaultDataType<BigDecimal>(SQLDialect.DERBY, SQLDataType.DECIMAL, "dec");
    public static final DataType<BigDecimal> NUMERIC                    = new DefaultDataType<BigDecimal>(SQLDialect.DERBY, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>     VARCHAR                    = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.VARCHAR, "varchar", "varchar(32672)");
    public static final DataType<String>     LONGVARCHAR                = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.LONGVARCHAR, "long varchar");
    public static final DataType<String>     CHAR                       = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.CHAR, "char", "varchar(32672)");
    public static final DataType<String>     CHARACTER                  = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.CHAR, "character", "varchar(32672)");
    public static final DataType<String>     CLOB                       = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.CLOB, "clob");
    public static final DataType<String>     CHARACTERLARGEOBJECT       = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.CLOB, "character large object");
    public static final DataType<String>     CHARVARYING                = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.VARCHAR, "char varying", "char varying(32672)");
    public static final DataType<String>     CHARACTERVARYING           = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.VARCHAR, "character varying", "character varying(32672)");
    public static final DataType<Boolean>    BOOLEAN                    = new DefaultDataType<Boolean>(SQLDialect.DERBY, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Date>       DATE                       = new DefaultDataType<Date>(SQLDialect.DERBY, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                       = new DefaultDataType<Time>(SQLDialect.DERBY, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  TIMESTAMP                  = new DefaultDataType<Timestamp>(SQLDialect.DERBY, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>     BLOB                       = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>     BINARYLARGEOBJECT          = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.BLOB, "binary large object");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.DERBY, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>     __BINARY                = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.BINARY, "blob");
    protected static final DataType<Boolean>    __BIT                   = new DefaultDataType<Boolean>(SQLDialect.DERBY, SQLDataType.BIT, "boolean");
    protected static final DataType<byte[]>     __LONGVARBINARY         = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.LONGVARBINARY, "blob");
    protected static final DataType<String>     __NCHAR                 = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.NCHAR, "char", "varchar(32672)");
    protected static final DataType<String>     __NCLOB                 = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>     __LONGNVARCHAR          = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.LONGNVARCHAR, "long varchar");
    protected static final DataType<String>     __NVARCHAR              = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
    protected static final DataType<Byte>       __TINYINT               = new DefaultDataType<Byte>(SQLDialect.DERBY, SQLDataType.TINYINT, "smallint");
    protected static final DataType<byte[]>     __VARBINARY             = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.VARBINARY, "blob");
    protected static final DataType<UByte>      __TINYINTUNSIGNED       = new DefaultDataType<UByte>(SQLDialect.DERBY, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED      = new DefaultDataType<UShort>(SQLDialect.DERBY, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED       = new DefaultDataType<UInteger>(SQLDialect.DERBY, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>      __BIGINTUNSIGNED        = new DefaultDataType<ULong>(SQLDialect.DERBY, SQLDataType.BIGINTUNSIGNED, "decimal", "decimal(20)");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER            = new DefaultDataType<BigInteger>(SQLDialect.DERBY, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal(31)");
    protected static final DataType<UUID>       __UUID                  = new DefaultDataType<UUID>(SQLDialect.DERBY, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<byte[]>     CHARFORBITDATA             = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.BINARY, "char for bit data");
    public static final DataType<byte[]>     CHARACTERFORBITDATA        = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.BINARY, "character for bit data");
    public static final DataType<byte[]>     LONGVARCHARFORBITDATA      = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.BINARY, "long varchar for bit data");
    public static final DataType<byte[]>     VARCHARFORBITDATA          = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.VARBINARY, "varchar for bit data", "varchar(32672) for bit data");
    public static final DataType<byte[]>     CHARVARYINGFORBITDATA      = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.VARBINARY, "char varying for bit data", "char varying(32672) for bit data");
    public static final DataType<byte[]>     CHARACTERVARYINGFORBITDATA = new DefaultDataType<byte[]>(SQLDialect.DERBY, SQLDataType.VARBINARY, "character varying for bit data", "character varying (32672) for bit data");
    public static final DataType<String>     ORGAPACHEDERBYCATALOGTYPEDESCRIPTOR
                                                                        = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.CLOB, "org.apache.derby.catalog.TypeDescriptor");
    public static final DataType<String>     ORGAPACHEDERBYCATALOGINDEXDESCRIPTOR
                                                                        = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.CLOB, "org.apache.derby.catalog.IndexDescriptor");
    public static final DataType<String>     JAVAIOSERIALIZABLE         = new DefaultDataType<String>(SQLDialect.DERBY, SQLDataType.CLOB, "java.io.Serializable");
}
