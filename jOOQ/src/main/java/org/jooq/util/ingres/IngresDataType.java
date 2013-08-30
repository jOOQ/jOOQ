/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */

package org.jooq.util.ingres;

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
 * Supported data types for the {@link SQLDialect#INGRES} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://docs.ingres.com/ingres/9.3/quel-reference-guide/1240-data-types">http://docs.ingres.com/ingres/9.3/quel-reference-guide/1240-data-types</a>
 * @see <a href="http://docs.ingres.com/ingres/10.0/sql-reference-guide/2638-storage-formats-of-data-types">http://docs.ingres.com/ingres/10.0/sql-reference-guide/2638-storage-formats-of-data-types</a>
 */
public class IngresDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>       TINYINT                      = new DefaultDataType<Byte>(SQLDialect.INGRES, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Byte>       INTEGER1                     = new DefaultDataType<Byte>(SQLDialect.INGRES, SQLDataType.TINYINT, "integer1");
    public static final DataType<Byte>       I1                           = new DefaultDataType<Byte>(SQLDialect.INGRES, SQLDataType.TINYINT, "i1");
    public static final DataType<Short>      SMALLINT                     = new DefaultDataType<Short>(SQLDialect.INGRES, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Short>      INTEGER2                     = new DefaultDataType<Short>(SQLDialect.INGRES, SQLDataType.SMALLINT, "integer2");
    public static final DataType<Short>      I2                           = new DefaultDataType<Short>(SQLDialect.INGRES, SQLDataType.SMALLINT, "i2");
    public static final DataType<Integer>    INTEGER                      = new DefaultDataType<Integer>(SQLDialect.INGRES, SQLDataType.INTEGER, "integer");
    public static final DataType<Integer>    INTEGER4                     = new DefaultDataType<Integer>(SQLDialect.INGRES, SQLDataType.INTEGER, "integer4");
    public static final DataType<Integer>    I4                           = new DefaultDataType<Integer>(SQLDialect.INGRES, SQLDataType.INTEGER, "i4");
    public static final DataType<Long>       BIGINT                       = new DefaultDataType<Long>(SQLDialect.INGRES, SQLDataType.BIGINT, "bigint");
    public static final DataType<Long>       INTEGER8                     = new DefaultDataType<Long>(SQLDialect.INGRES, SQLDataType.BIGINT, "integer8");
    public static final DataType<Long>       I8                           = new DefaultDataType<Long>(SQLDialect.INGRES, SQLDataType.BIGINT, "i8");
    public static final DataType<Double>     FLOAT                        = new DefaultDataType<Double>(SQLDialect.INGRES, SQLDataType.FLOAT, "float");
    public static final DataType<Double>     FLOAT8                       = new DefaultDataType<Double>(SQLDialect.INGRES, SQLDataType.DOUBLE, "float8");
    public static final DataType<Double>     DOUBLEPRECISION              = new DefaultDataType<Double>(SQLDialect.INGRES, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Float>      REAL                         = new DefaultDataType<Float>(SQLDialect.INGRES, SQLDataType.REAL, "real");
    public static final DataType<Float>      FLOAT4                       = new DefaultDataType<Float>(SQLDialect.INGRES, SQLDataType.REAL, "float4");
    public static final DataType<Boolean>    BOOLEAN                      = new DefaultDataType<Boolean>(SQLDialect.INGRES, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<BigDecimal> DECIMAL                      = new DefaultDataType<BigDecimal>(SQLDialect.INGRES, SQLDataType.DECIMAL, "decimal", "decimal(38, 19)");
    public static final DataType<String>     VARCHAR                      = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>     CHARACTERVARYING             = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.VARCHAR, "character varying");
    public static final DataType<String>     LONGVARCHAR                  = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.LONGVARCHAR, "long varchar");
    public static final DataType<String>     CHAR                         = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CHAR, "char");
    public static final DataType<String>     CHARACTER                    = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CHAR, "character");
    public static final DataType<String>     C                            = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CHAR, "c");
    public static final DataType<String>     CLOB                         = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CLOB, "clob");
    public static final DataType<String>     CHARLARGEOBJECT              = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CLOB, "char large object");
    public static final DataType<String>     CHARACTERLARGEOBJECT         = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CLOB, "character large object");
    public static final DataType<String>     NVARCHAR                     = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>     LONGNVARCHAR                 = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.LONGNVARCHAR, "long nvarchar");
    public static final DataType<String>     NCHAR                        = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NCHAR, "nchar");
    public static final DataType<String>     NCLOB                        = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NCLOB, "nclob");
    public static final DataType<String>     NCHARLARGEOBJECT             = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NCLOB, "nchar large object");
    public static final DataType<String>     NATIONALCHARACTERLARGEOBJECT = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.NCLOB, "national character large object");
    public static final DataType<Date>       DATE                         = new DefaultDataType<Date>(SQLDialect.INGRES, SQLDataType.DATE, "date");
    public static final DataType<Time>       TIME                         = new DefaultDataType<Time>(SQLDialect.INGRES, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>  TIMESTAMP                    = new DefaultDataType<Timestamp>(SQLDialect.INGRES, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>     BLOB                         = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>     BINARYLARGEOBJECT            = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.BLOB, "binary large object");
    public static final DataType<byte[]>     BINARY                       = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.BINARY, "binary");
    public static final DataType<byte[]>     VARBINARY                    = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.VARBINARY, "varbinary");
    public static final DataType<byte[]>     LONGBYTE                     = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.LONGVARBINARY, "long byte");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.INGRES, SQLDataTypes
    // -------------------------------------------------------------------------

    public static final DataType<Boolean>     BIT                         = new DefaultDataType<Boolean>(SQLDialect.INGRES, SQLDataType.BIT, "boolean");
    public static final DataType<BigDecimal>  NUMERIC                     = new DefaultDataType<BigDecimal>(SQLDialect.INGRES, SQLDataType.NUMERIC, "decimal", "decimal(38, 19)");
    protected static final DataType<UByte>    __TINYINTUNSIGNED           = new DefaultDataType<UByte>(SQLDialect.INGRES, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED          = new DefaultDataType<UShort>(SQLDialect.INGRES, SQLDataType.SMALLINTUNSIGNED, "integer");
    protected static final DataType<UInteger> __INTEGERUNSIGNED           = new DefaultDataType<UInteger>(SQLDialect.INGRES, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED            = new DefaultDataType<ULong>(SQLDialect.INGRES, SQLDataType.BIGINTUNSIGNED, "decimal", "decimal(20)");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER              = new DefaultDataType<BigInteger>(SQLDialect.INGRES, SQLDataType.DECIMAL_INTEGER, "decimal", "decimal(38, 0)");
    protected static final DataType<UUID>       __UUID                    = new DefaultDataType<UUID>(SQLDialect.INGRES, SQLDataType.UUID, "varchar");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<String>     TEXT                         = new DefaultDataType<String>(SQLDialect.INGRES, SQLDataType.CLOB, "text");
    public static final DataType<Date>       ANSIDATE                     = new DefaultDataType<Date>(SQLDialect.INGRES, SQLDataType.DATE, "ansidate");
    public static final DataType<Date>       INGRESDATE                   = new DefaultDataType<Date>(SQLDialect.INGRES, SQLDataType.DATE, "ingresdate");
    public static final DataType<Time>       TIMEWITHTIMEZONE             = new DefaultDataType<Time>(SQLDialect.INGRES, SQLDataType.TIME, "time with time zone");
    public static final DataType<Time>       TIMEWITHOUTTIMEZONE          = new DefaultDataType<Time>(SQLDialect.INGRES, SQLDataType.TIME, "time without time zone");
    public static final DataType<Time>       TIMEWITHLOCALTIMEZONE        = new DefaultDataType<Time>(SQLDialect.INGRES, SQLDataType.TIME, "time with local time zone");
    public static final DataType<Timestamp>  TIMESTAMPWITHTIMEZONE        = new DefaultDataType<Timestamp>(SQLDialect.INGRES, SQLDataType.TIMESTAMP, "timestamp with time zone");
    public static final DataType<Timestamp>  TIMESTAMPWITHOUTTIMEZONE     = new DefaultDataType<Timestamp>(SQLDialect.INGRES, SQLDataType.TIMESTAMP, "timestamp without time zone");
    public static final DataType<Timestamp>  TIMESTAMPWITHLOCALTIMEZONE   = new DefaultDataType<Timestamp>(SQLDialect.INGRES, SQLDataType.TIMESTAMP, "timestamp with local time zone");
    public static final DataType<byte[]>     BYTE                         = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.BINARY, "byte");
    public static final DataType<byte[]>     VARBYTE                      = new DefaultDataType<byte[]>(SQLDialect.INGRES, SQLDataType.VARBINARY, "varbyte");
}
