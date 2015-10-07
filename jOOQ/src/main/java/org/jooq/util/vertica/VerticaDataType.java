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

package org.jooq.util.vertica;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

/**
 * Supported data types for the {@link SQLDialect#VERTICA} dialect
 *
 * @author Lukas Eder
 * @see <a href="http://my.vertica.com/docs/7.1.x/PDF/HP_Vertica_7.1.x_SQL_Reference_Manual.pdf">http://my.vertica.com/docs/7.1.x/PDF/HP_Vertica_7.1.x_SQL_Reference_Manual.pdf</a>
 */
public class VerticaDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Long>        TINYINT               = new DefaultDataType<Long>(SQLDialect.VERTICA, SQLDataType.BIGINT, "tinyint");
    public static final DataType<Long>        SMALLINT              = new DefaultDataType<Long>(SQLDialect.VERTICA, SQLDataType.BIGINT, "smallint");
    public static final DataType<Long>        INT                   = new DefaultDataType<Long>(SQLDialect.VERTICA, SQLDataType.BIGINT, "int");
    public static final DataType<Long>        INTEGER               = new DefaultDataType<Long>(SQLDialect.VERTICA, SQLDataType.BIGINT, "integer");
    public static final DataType<Long>        BIGINT                = new DefaultDataType<Long>(SQLDialect.VERTICA, SQLDataType.BIGINT, "bigint");
    public static final DataType<Double>      DOUBLEPRECISION       = new DefaultDataType<Double>(SQLDialect.VERTICA, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>      FLOAT                 = new DefaultDataType<Double>(SQLDialect.VERTICA, SQLDataType.FLOAT, "float");
    public static final DataType<Float>       REAL                  = new DefaultDataType<Float>(SQLDialect.VERTICA, SQLDataType.REAL, "real");
    public static final DataType<Boolean>     BOOLEAN               = new DefaultDataType<Boolean>(SQLDialect.VERTICA, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<BigDecimal>  DECIMAL               = new DefaultDataType<BigDecimal>(SQLDialect.VERTICA, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal>  NUMERIC               = new DefaultDataType<BigDecimal>(SQLDialect.VERTICA, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>      VARCHAR               = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.VARCHAR, "varchar");
    public static final DataType<String>      LONGVARCHAR           = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.LONGVARCHAR, "long varchar");
    public static final DataType<String>      CHAR                  = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.CHAR, "char");
    public static final DataType<Date>        DATE                  = new DefaultDataType<Date>(SQLDialect.VERTICA, SQLDataType.DATE, "date");
    public static final DataType<Time>        TIME                  = new DefaultDataType<Time>(SQLDialect.VERTICA, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>   TIMESTAMP             = new DefaultDataType<Timestamp>(SQLDialect.VERTICA, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<Timestamp>   DATETIME              = new DefaultDataType<Timestamp>(SQLDialect.VERTICA, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<Timestamp>   SMALLDATETIME         = new DefaultDataType<Timestamp>(SQLDialect.VERTICA, SQLDataType.TIMESTAMP, "smalldatetime");
    public static final DataType<byte[]>      VARBINARY             = new DefaultDataType<byte[]>(SQLDialect.VERTICA, SQLDataType.VARBINARY, "varbinary");
    public static final DataType<byte[]>      BINARY                = new DefaultDataType<byte[]>(SQLDialect.VERTICA, SQLDataType.BINARY, "binary");
    public static final DataType<byte[]>      LONGVARBINARY         = new DefaultDataType<byte[]>(SQLDialect.VERTICA, SQLDataType.LONGVARBINARY, "long varbinary");

    public static final DataType<Object>      OTHER                 = new DefaultDataType<Object>(SQLDialect.VERTICA, SQLDataType.OTHER, "other");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.VERTICA, SQLDataTypes
    // -------------------------------------------------------------------------


    protected static final DataType<Byte>     __TINYINT             = new DefaultDataType<Byte>(SQLDialect.VERTICA, SQLDataType.TINYINT, "tinyint");
    protected static final DataType<Short>    __SMALLINT            = new DefaultDataType<Short>(SQLDialect.VERTICA, SQLDataType.SMALLINT, "smallint");
    protected static final DataType<Integer>  __INT                 = new DefaultDataType<Integer>(SQLDialect.VERTICA, SQLDataType.INTEGER, "int");
    protected static final DataType<Boolean>  __BIT                 = new DefaultDataType<Boolean>(SQLDialect.VERTICA, SQLDataType.BIT, "boolean");
    protected static final DataType<byte[]>   __BLOB                = new DefaultDataType<byte[]>(SQLDialect.VERTICA, SQLDataType.BLOB, "long varbinary");
    protected static final DataType<String>   __CLOB                = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.CLOB, "long varchar");
    protected static final DataType<String>   __CHARLARGEOBJECT     = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.CLOB, "long varchar");
    protected static final DataType<String>   __CHARACTERLARGEOBJECT= new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.CLOB, "long varchar");
    protected static final DataType<String>   __NCHAR               = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.NCHAR, "char");
    protected static final DataType<String>   __NCLOB               = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>   __LONGNVARCHAR        = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.LONGNVARCHAR, "long varchar");
    protected static final DataType<String>   __NVARCHAR            = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
    protected static final DataType<UByte>    __TINYINTUNSIGNED     = new DefaultDataType<UByte>(SQLDialect.VERTICA, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED    = new DefaultDataType<UShort>(SQLDialect.VERTICA, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED     = new DefaultDataType<UInteger>(SQLDialect.VERTICA, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED      = new DefaultDataType<ULong>(SQLDialect.VERTICA, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER       = new DefaultDataType<BigInteger>(SQLDialect.VERTICA, SQLDataType.DECIMAL_INTEGER, "decimal");
    protected static final DataType<UUID>       __UUID             = new DefaultDataType<UUID>(SQLDialect.VERTICA, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<byte[]>         BYTEA             = new DefaultDataType<byte[]>(SQLDialect.VERTICA, SQLDataType.VARBINARY, "bytea");
    public static final DataType<byte[]>         RAW               = new DefaultDataType<byte[]>(SQLDialect.VERTICA, SQLDataType.VARBINARY, "raw");

    public static final DataType<String>         VARCHARIGNORECASE = new DefaultDataType<String>(SQLDialect.VERTICA, SQLDataType.VARCHAR, "varchar_ignorecase");
    public static final DataType<Object>         OBJECT            = new DefaultDataType<Object>(SQLDialect.VERTICA, SQLDataType.OTHER, "object");
    public static final DataType<Result<Record>> ROW               = new DefaultDataType<Result<Record>>(SQLDialect.VERTICA, SQLDataType.RESULT, "row");
}
