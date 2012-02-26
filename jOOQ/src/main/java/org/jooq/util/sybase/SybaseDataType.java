/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util.sybase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.impl.AbstractDataType;
import org.jooq.impl.SQLDataType;

/**
 * Supported data types for the {@link SQLDialect#SYBASE} dialect
 *
 * @see <a href="http://infocenter.sybase.com/help/topic/com.sybase.help.sqlanywhere.12.0.0/dbreference/rf-datatypes.html">http://infocenter.sybase.com/help/topic/com.sybase.help.sqlanywhere.12.0.0/dbreference/rf-datatypes.html</a>
 * @author Espen Stromsnes
 */
public class SybaseDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                      serialVersionUID           = -4442303192680774346L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final SybaseDataType<Byte>       TINYINT                    = new SybaseDataType<Byte>(SQLDataType.TINYINT, "tinyint");
    public static final SybaseDataType<Short>      SMALLINT                   = new SybaseDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final SybaseDataType<Short>      UNSIGNEDSMALLLINT          = new SybaseDataType<Short>(SQLDataType.SMALLINT, "unsignedsmallint");
    public static final SybaseDataType<Integer>    INT                        = new SybaseDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final SybaseDataType<Integer>    INTEGER                    = new SybaseDataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final SybaseDataType<Integer>    UNSIGNEDINT                = new SybaseDataType<Integer>(SQLDataType.INTEGER, "unsignedint");
    public static final SybaseDataType<Long>       BIGINT                     = new SybaseDataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final SybaseDataType<Long>       UNSIGNEDBIGINT             = new SybaseDataType<Long>(SQLDataType.BIGINT, "unsignedbigint");
    public static final SybaseDataType<Double>     DOUBLE                     = new SybaseDataType<Double>(SQLDataType.DOUBLE, "double");
    public static final SybaseDataType<Double>     FLOAT                      = new SybaseDataType<Double>(SQLDataType.FLOAT, "float");
    public static final SybaseDataType<Float>      REAL                       = new SybaseDataType<Float>(SQLDataType.REAL, "real");
    public static final SybaseDataType<BigDecimal> DECIMAL                    = new SybaseDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final SybaseDataType<BigDecimal> NUMERIC                    = new SybaseDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric");
    public static final SybaseDataType<Boolean>    BIT                        = new SybaseDataType<Boolean>(SQLDataType.BIT, "bit");
    public static final SybaseDataType<String>     VARCHAR                    = new SybaseDataType<String>(SQLDataType.VARCHAR, "varchar");
    public static final SybaseDataType<String>     CHAR                       = new SybaseDataType<String>(SQLDataType.CHAR, "char");
    public static final SybaseDataType<String>     LONGNVARCHAR               = new SybaseDataType<String>(SQLDataType.LONGNVARCHAR, "long nvarchar");
    public static final SybaseDataType<String>     LONGVARCHAR                = new SybaseDataType<String>(SQLDataType.LONGVARCHAR, "long varchar");
    public static final SybaseDataType<String>     NCHAR                      = new SybaseDataType<String>(SQLDataType.NCHAR, "nchar");
    public static final SybaseDataType<String>     NTEXT                      = new SybaseDataType<String>(SQLDataType.NCLOB, "ntext");
    public static final SybaseDataType<String>     NVARCHAR                   = new SybaseDataType<String>(SQLDataType.NVARCHAR, "nvarchar");
    public static final SybaseDataType<String>     TEXT                       = new SybaseDataType<String>(SQLDataType.CLOB, "text");
    public static final SybaseDataType<Date>       DATE                       = new SybaseDataType<Date>(SQLDataType.DATE, "date");
    public static final SybaseDataType<Time>       TIME                       = new SybaseDataType<Time>(SQLDataType.TIME, "time");
    public static final SybaseDataType<Timestamp>  DATETIME                   = new SybaseDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime");
    public static final SybaseDataType<Timestamp>  TIMESTAMP                  = new SybaseDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestamp");
    public static final SybaseDataType<byte[]>     BINARY                     = new SybaseDataType<byte[]>(SQLDataType.BINARY, "binary");
    public static final SybaseDataType<byte[]>     LONGBINARY                 = new SybaseDataType<byte[]>(SQLDataType.LONGVARBINARY, "long binary");
    public static final SybaseDataType<byte[]>     VARBINARY                  = new SybaseDataType<byte[]>(SQLDataType.VARBINARY, "varbinary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final SybaseDataType<byte[]>  __BLOB                     = new SybaseDataType<byte[]>(SQLDataType.BLOB, "binary");
    protected static final SybaseDataType<Boolean> __BOOLEAN                  = new SybaseDataType<Boolean>(SQLDataType.BOOLEAN, "bit");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final SybaseDataType<BigInteger> __BIGINTEGER = new SybaseDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final SybaseDataType<BigDecimal> MONEY                 = new SybaseDataType<BigDecimal>(SQLDataType.DECIMAL, "money");
    public static final SybaseDataType<BigDecimal> SMALLMONEY            = new SybaseDataType<BigDecimal>(SQLDataType.DECIMAL, "smallmoney");
    public static final SybaseDataType<String>     UNIQUEIDENTIFIERSTR   = new SybaseDataType<String>(SQLDataType.VARCHAR, "uniqueidentifierstr");
    public static final SybaseDataType<String>     XML                   = new SybaseDataType<String>(SQLDataType.VARCHAR, "xml");
    public static final SybaseDataType<String>     UNIQUEIDENTIFIER      = new SybaseDataType<String>(SQLDataType.VARCHAR, "uniqueidentifier");
    public static final SybaseDataType<Timestamp>  DATETIMEOFFSET        = new SybaseDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetimeoffset");
    public static final SybaseDataType<Timestamp>  SMALLDATETIME         = new SybaseDataType<Timestamp>(SQLDataType.TIMESTAMP, "smalldatetime");
    public static final SybaseDataType<Timestamp>  TIMESTAMPWITHTIMEZONE = new SybaseDataType<Timestamp>(SQLDataType.TIMESTAMP, "timestampwithtimezone");
    public static final SybaseDataType<byte[]>     IMAGE                 = new SybaseDataType<byte[]>(SQLDataType.BINARY, "image");
    public static final SybaseDataType<byte[]>     VARBIT                = new SybaseDataType<byte[]>(SQLDataType.VARBINARY, "varbit");
    public static final SybaseDataType<byte[]>     LONGVARBIT            = new SybaseDataType<byte[]>(SQLDataType.LONGVARBINARY, "longvarbit");


    private SybaseDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.SYBASE, sqlDataType, sqlDataType.getType(), typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.SYBASE, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.SYBASE, typeName);
    }
}
