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

package org.jooq.util.hana;

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
 * Supported data types for the {@link SQLDialect#HANA} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://help.sap.com/saphelp_hanaplatform/helpdata/en/20/d58a5f75191014b2fe92141b7df228/content.htm#loio20d58a5f75191014b2fe92141b7df228__sql_create_table_1create_table_contents_source">http://help.sap.com/saphelp_hanaplatform/helpdata/en/20/d58a5f75191014b2fe92141b7df228/content.htm#loio20d58a5f75191014b2fe92141b7df228__sql_create_table_1create_table_contents_source</a>
 */
public class HanaDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>        TINYINT               = new DefaultDataType<Byte>(SQLDialect.HANA, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>       SMALLINT              = new DefaultDataType<Short>(SQLDialect.HANA, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>     INTEGER               = new DefaultDataType<Integer>(SQLDialect.HANA, SQLDataType.INTEGER, "integer");
    public static final DataType<Long>        BIGINT                = new DefaultDataType<Long>(SQLDialect.HANA, SQLDataType.BIGINT, "bigint");
    public static final DataType<Double>      DOUBLE                = new DefaultDataType<Double>(SQLDialect.HANA, SQLDataType.DOUBLE, "double");
    public static final DataType<Float>       REAL                  = new DefaultDataType<Float>(SQLDialect.HANA, SQLDataType.REAL, "real");
    public static final DataType<BigDecimal>  DECIMAL               = new DefaultDataType<BigDecimal>(SQLDialect.HANA, SQLDataType.DECIMAL, "decimal");
    public static final DataType<String>      VARCHAR               = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>      TEXT                  = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.VARCHAR, "text");
    public static final DataType<String>      NVARCHAR              = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.NVARCHAR, "nvarchar");
    public static final DataType<String>      CLOB                  = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.CLOB, "clob");
    public static final DataType<Date>        DATE                  = new DefaultDataType<Date>(SQLDialect.HANA, SQLDataType.DATE, "date");
    public static final DataType<Time>        TIME                  = new DefaultDataType<Time>(SQLDialect.HANA, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>   TIMESTAMP             = new DefaultDataType<Timestamp>(SQLDialect.HANA, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<byte[]>      VARBINARY             = new DefaultDataType<byte[]>(SQLDialect.HANA, SQLDataType.VARBINARY, "varbinary");
    public static final DataType<byte[]>      BLOB                  = new DefaultDataType<byte[]>(SQLDialect.HANA, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>      BINARYLARGEOBJECT     = new DefaultDataType<byte[]>(SQLDialect.HANA, SQLDataType.BLOB, "binary large object", "blob");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.HANA, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<Boolean>  __BIT                 = new DefaultDataType<Boolean>(SQLDialect.HANA, SQLDataType.BIT, "bit");
    protected static final DataType<byte[]>   __BINARY              = new DefaultDataType<byte[]>(SQLDialect.HANA, SQLDataType.BINARY, "binary");
    protected static final DataType<Boolean>  __BOOLEAN             = new DefaultDataType<Boolean>(SQLDialect.HANA, SQLDataType.BOOLEAN, "boolean");
    protected static final DataType<String>   __CHAR                = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.CHAR, "char");
    protected static final DataType<Double>   __FLOAT               = new DefaultDataType<Double>(SQLDialect.HANA, SQLDataType.FLOAT, "double");
    protected static final DataType<String>   __NCHAR               = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.NCHAR, "char");
    protected static final DataType<String>   __NCLOB               = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.NCLOB, "clob");
    protected static final DataType<BigDecimal> __NUMERIC           = new DefaultDataType<BigDecimal>(SQLDialect.HANA, SQLDataType.NUMERIC, "numeric");
    protected static final DataType<String>   __LONGVARCHAR         = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.LONGVARCHAR, "longvarchar");
    protected static final DataType<String>   __LONGNVARCHAR        = new DefaultDataType<String>(SQLDialect.HANA, SQLDataType.LONGNVARCHAR, "longvarchar");
    protected static final DataType<UByte>    __TINYINTUNSIGNED     = new DefaultDataType<UByte>(SQLDialect.HANA, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED    = new DefaultDataType<UShort>(SQLDialect.HANA, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED     = new DefaultDataType<UInteger>(SQLDialect.HANA, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED      = new DefaultDataType<ULong>(SQLDialect.HANA, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER       = new DefaultDataType<BigInteger>(SQLDialect.HANA, SQLDataType.DECIMAL_INTEGER, "decimal");
    protected static final DataType<UUID>       __UUID             = new DefaultDataType<UUID>(SQLDialect.HANA, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

}
