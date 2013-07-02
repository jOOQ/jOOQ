/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name of the "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util.access;

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
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;

/**
 * Supported data types for the {@link SQLDialect#ACCESS} dialect
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms714540(v=vs.85).aspx">http://msdn.microsoft.com/en-us/library/windows/desktop/ms714540(v=vs.85).aspx</a>
 * @author Lukas Eder
 */
public class AccessDataType {

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<Byte>        TINYINT               = new DefaultDataType<Byte>(SQLDialect.ACCESS, SQLDataType.TINYINT, "tinyint");
    public static final DataType<Short>       SMALLINT              = new DefaultDataType<Short>(SQLDialect.ACCESS, SQLDataType.SMALLINT, "smallint");
    public static final DataType<Integer>     INT                   = new DefaultDataType<Integer>(SQLDialect.ACCESS, SQLDataType.INTEGER, "int");
    public static final DataType<Integer>     INTEGER               = new DefaultDataType<Integer>(SQLDialect.ACCESS, SQLDataType.INTEGER, "integer");
    public static final DataType<Long>        BIGINT                = new DefaultDataType<Long>(SQLDialect.ACCESS, SQLDataType.BIGINT, "bigint");
    public static final DataType<Double>      DOUBLE                = new DefaultDataType<Double>(SQLDialect.ACCESS, SQLDataType.DOUBLE, "double");
    public static final DataType<Double>      DOUBLEPRECISION       = new DefaultDataType<Double>(SQLDialect.ACCESS, SQLDataType.DOUBLE, "double precision");
    public static final DataType<Double>      FLOAT                 = new DefaultDataType<Double>(SQLDialect.ACCESS, SQLDataType.FLOAT, "float");
    public static final DataType<Float>       REAL                  = new DefaultDataType<Float>(SQLDialect.ACCESS, SQLDataType.REAL, "real");
    public static final DataType<Boolean>     BOOLEAN               = new DefaultDataType<Boolean>(SQLDialect.ACCESS, SQLDataType.BOOLEAN, "boolean");
    public static final DataType<Boolean>     BIT                   = new DefaultDataType<Boolean>(SQLDialect.ACCESS, SQLDataType.BIT, "bit");
    public static final DataType<BigDecimal>  DECIMAL               = new DefaultDataType<BigDecimal>(SQLDialect.ACCESS, SQLDataType.DECIMAL, "decimal");
    public static final DataType<BigDecimal>  NUMERIC               = new DefaultDataType<BigDecimal>(SQLDialect.ACCESS, SQLDataType.NUMERIC, "numeric");
    public static final DataType<String>      VARCHAR               = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.VARCHAR, "varchar", "varchar(32672)");
    public static final DataType<String>      LONGVARCHAR           = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.LONGVARCHAR, "longvarchar");
    public static final DataType<String>      CHAR                  = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.CHAR, "char");
    public static final DataType<String>      CHARACTER             = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.CHAR, "character");
    public static final DataType<String>      CHARACTERVARYING      = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.VARCHAR, "character varying", "character varying(32672)");
    public static final DataType<String>      CLOB                  = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.CLOB, "clob");
    public static final DataType<String>      CHARLARGEOBJECT       = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.CLOB, "char large object", "clob");
    public static final DataType<String>      CHARACTERLARGEOBJECT  = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.CLOB, "character large object", "clob");
    public static final DataType<Date>        DATE                  = new DefaultDataType<Date>(SQLDialect.ACCESS, SQLDataType.DATE, "date");
    public static final DataType<Time>        TIME                  = new DefaultDataType<Time>(SQLDialect.ACCESS, SQLDataType.TIME, "time");
    public static final DataType<Timestamp>   TIMESTAMP             = new DefaultDataType<Timestamp>(SQLDialect.ACCESS, SQLDataType.TIMESTAMP, "timestamp");
    public static final DataType<Timestamp>   DATETIME              = new DefaultDataType<Timestamp>(SQLDialect.ACCESS, SQLDataType.TIMESTAMP, "datetime");
    public static final DataType<byte[]>      LONGVARBINARY         = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.LONGVARBINARY, "longvarbinary");
    public static final DataType<byte[]>      VARBINARY             = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.VARBINARY, "varbinary", "varbinary(32672)");
    public static final DataType<byte[]>      BINARY                = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.BINARY, "binary");
    public static final DataType<byte[]>      BLOB                  = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.BLOB, "blob");
    public static final DataType<byte[]>      BINARYLARGEOBJECT     = new DefaultDataType<byte[]>(SQLDialect.ACCESS, SQLDataType.BLOB, "binary large object", "blob");
    public static final DataType<Object>      OTHER                 = new DefaultDataType<Object>(SQLDialect.ACCESS, SQLDataType.OTHER, "other");
    public static final DataType<YearToMonth> INTERVALYEARTOMONTH   = new DefaultDataType<YearToMonth>(SQLDialect.ACCESS, SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
    public static final DataType<DayToSecond> INTERVALDAYTOSECOND   = new DefaultDataType<DayToSecond>(SQLDialect.ACCESS, SQLDataType.INTERVALDAYTOSECOND, "interval day to second");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDialect.ACCESS, SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final DataType<String>   __NCHAR               = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.NCHAR, "char");
    protected static final DataType<String>   __NCLOB               = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.NCLOB, "clob");
    protected static final DataType<String>   __LONGNVARCHAR        = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.LONGNVARCHAR, "longvarchar");
    protected static final DataType<String>   __NVARCHAR            = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.NVARCHAR, "varchar", "varchar(32672)");
    protected static final DataType<UByte>    __TINYINTUNSIGNED     = new DefaultDataType<UByte>(SQLDialect.ACCESS, SQLDataType.TINYINTUNSIGNED, "smallint");
    protected static final DataType<UShort>   __SMALLINTUNSIGNED    = new DefaultDataType<UShort>(SQLDialect.ACCESS, SQLDataType.SMALLINTUNSIGNED, "int");
    protected static final DataType<UInteger> __INTEGERUNSIGNED     = new DefaultDataType<UInteger>(SQLDialect.ACCESS, SQLDataType.INTEGERUNSIGNED, "bigint");
    protected static final DataType<ULong>    __BIGINTUNSIGNED      = new DefaultDataType<ULong>(SQLDialect.ACCESS, SQLDataType.BIGINTUNSIGNED, "decimal");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final DataType<BigInteger> __BIGINTEGER       = new DefaultDataType<BigInteger>(SQLDialect.ACCESS, SQLDataType.DECIMAL_INTEGER, "decimal");
    protected static final DataType<UUID>       __UUID             = new DefaultDataType<UUID>(SQLDialect.ACCESS, SQLDataType.UUID, "varchar", "varchar(36)");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final DataType<String>         VARCHARIGNORECASE = new DefaultDataType<String>(SQLDialect.ACCESS, SQLDataType.VARCHAR, "varchar_ignorecase", "varchar_ignorecase(32672)");
    public static final DataType<Object>         OBJECT            = new DefaultDataType<Object>(SQLDialect.ACCESS, SQLDataType.OTHER, "object");
    public static final DataType<Result<Record>> ROW               = new DefaultDataType<Result<Record>>(SQLDialect.ACCESS, SQLDataType.RESULT, "row");

    private AccessDataType() {}
}
