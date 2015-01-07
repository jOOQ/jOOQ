/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util.access;

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
 * Supported data types for the {@link SQLDialect#ACCESS} dialect family.
 * <p>
 * This includes the
 * <ul>
 * <li>{@link org.jooq.SQLDialect#ACCESS2013} dialect</li>
 * </ul>
 *
 * @author Lukas Eder
 * @see <a
 *      href="http://msdn.microsoft.com/en-us/library/office/ff193793.aspx">http://msdn.microsoft.com/en-us/library/office/ff193793.aspx</a>
 */
public class AccessDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<byte[]>     BINARY           = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.BINARY, "binary");
    public static final DataType<Boolean>    BIT              = new DefaultDataType<Boolean>(SQLDialect.ACCESS, SQLDataType.BIT, "bit");
    public static final DataType<UByte>      TINYINT          = new DefaultDataType<UByte>(SQLDialect.ACCESS, SQLDataType.TINYINTUNSIGNED, "tinyint");
    public static final DataType<Float>      REAL             = new DefaultDataType<Float>(SQLDialect.ACCESS, SQLDataType.REAL, "real");
    public static final DataType<Double>     FLOAT            = new DefaultDataType<Double>(SQLDialect.ACCESS, SQLDataType.FLOAT, "float");
    public static final DataType<Short>      SMALLINT         = new DefaultDataType<Short>(SQLDialect.ACCESS, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>    INTEGER          = new DefaultDataType<Integer>(SQLDialect.ACCESS, SQLDataType.INTEGER, "integer");
    public static final DataType<BigDecimal> NUMERIC          = new DefaultDataType<BigDecimal>(SQLDialect.ACCESS, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>     TEXT             = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.VARCHAR, "text");
    public static final DataType<String>     LONGTEXT         = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.CLOB, "longtext");
    public static final DataType<String>     LONGCHAR         = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.CLOB, "longchar");
    public static final DataType<String>     CHAR             = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.CHAR, "character");
    public static final DataType<Timestamp>  DATETIME         = new DefaultDataType<Timestamp>(SQLDialect.ACCESS, SQLDataType.TIMESTAMP, "datetime");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.ACCESS, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<byte[]>     __VARBINARY   = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.VARBINARY, "binary");
    protected static final DataType<Long>       __BIGINT      = new DefaultDataType<Long>(SQLDialect.ACCESS, SQLDataType.BIGINT, "numeric");
    protected static final DataType<BigDecimal> __DECIMAL     = new DefaultDataType<BigDecimal>(SQLDialect.ACCESS, SQLDataType.DECIMAL, "numeric");
    protected static final DataType<Date>       __DATE        = new DefaultDataType<Date>(SQLDialect.ACCESS, SQLDataType.DATE, "datetime");
    protected static final DataType<Time>       __TIME        = new DefaultDataType<Time>(SQLDialect.ACCESS, SQLDataType.TIME, "datetime");
    protected static final DataType<String>     __VARCHAR     = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.VARCHAR, "text");
    protected static final DataType<String>     __NVARCHAR    = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.NVARCHAR, "text");
    protected static final DataType<String>     __NCHAR       = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.NCHAR, "text");
    protected static final DataType<String>     __NTEXT       = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.NCLOB, "longtext");


    protected static final DataType<byte[]>  __BLOB           = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.BLOB, "binary");
    protected static final DataType<Boolean> __BOOLEAN        = new DefaultDataType<Boolean>(SQLDialect.ACCESS, SQLDataType.BOOLEAN, "bit");
    protected static final DataType<Double>  __DOUBLE         = new DefaultDataType<Double>(SQLDialect.ACCESS, SQLDataType.DOUBLE, "float");
    protected static final DataType<byte[]>  __LONGVARBINARY  = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.LONGVARBINARY, "binary");
    protected static final DataType<String>  __LONGVARCHAR    = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.LONGVARCHAR, "longtext");
    protected static final DataType<String>  __NCLOB          = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.NCLOB, "longtext");
    protected static final DataType<String>  __LONGNVARCHAR   = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.LONGNVARCHAR, "longtext");
    protected static final DataType<Byte>    __BYTE           = new DefaultDataType<Byte>(SQLDialect.ACCESS, SQLDataType.TINYINT, "signed tinyint", "smallint");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER       = new DefaultDataType<BigInteger>(SQLDialect.ACCESS, SQLDataType.DECIMAL_INTEGER, "numeric");
    protected static final DataType<UShort>     __SMALLINTUNSIGNED = new DefaultDataType<UShort>(SQLDialect.ACCESS, SQLDataType.SMALLINTUNSIGNED, "integer");
    protected static final DataType<UInteger>   __INTEGERUNSIGNED  = new DefaultDataType<UInteger>(SQLDialect.ACCESS, SQLDataType.INTEGERUNSIGNED, "numeric");
    protected static final DataType<ULong>      __BIGINTUNSIGNED   = new DefaultDataType<ULong>(SQLDialect.ACCESS, SQLDataType.BIGINTUNSIGNED, "numeric");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Integer>    COUNTER          = new DefaultDataType<Integer>(SQLDialect.ACCESS, SQLDataType.INTEGER, "counter", "integer");
    public static final DataType<BigDecimal> MONEY            = new DefaultDataType<BigDecimal>(SQLDialect.ACCESS, SQLDataType.DECIMAL, "money");
    public static final DataType<byte[]>     IMAGE            = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.BINARY, "image");
    public static final DataType<UUID>       UNIQUEIDENTIFIER = new DefaultDataType<UUID>(SQLDialect.ACCESS, SQLDataType.UUID, "uniqueidentifier");
}
