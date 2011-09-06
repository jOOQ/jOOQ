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
package org.jooq.util.adaptiveserver;

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
 * Supported data types for the {@link SQLDialect#ADAPTIVESERVER} dialect
 *
 * @see <a href="http://infocenter.sybase.com/help/topic/com.sybase.infocenter.dc36271.1550/html/blocks/X31825.htm">http://infocenter.sybase.com/help/topic/com.sybase.infocenter.dc36271.1550/html/blocks/X31825.htm</a>
 * @author Lukas Eder
 */
public class AdaptiveServerDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                      serialVersionUID           = -4442303192680774346L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final AdaptiveServerDataType<Byte>       TINYINT                    = new AdaptiveServerDataType<Byte>(SQLDataType.TINYINT, "tinyint");
    public static final AdaptiveServerDataType<Short>      SMALLINT                   = new AdaptiveServerDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final AdaptiveServerDataType<Short>      UNSIGNEDSMALLLINT          = new AdaptiveServerDataType<Short>(SQLDataType.SMALLINT, "unsigned smallint");
    public static final AdaptiveServerDataType<Integer>    INT                        = new AdaptiveServerDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final AdaptiveServerDataType<Integer>    INTEGER                    = new AdaptiveServerDataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final AdaptiveServerDataType<Integer>    UNSIGNEDINT                = new AdaptiveServerDataType<Integer>(SQLDataType.INTEGER, "unsigned int");
    public static final AdaptiveServerDataType<Long>       BIGINT                     = new AdaptiveServerDataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final AdaptiveServerDataType<Long>       UNSIGNEDBIGINT             = new AdaptiveServerDataType<Long>(SQLDataType.BIGINT, "unsigned bigint");
    public static final AdaptiveServerDataType<Double>     DOUBLE_PRECISION           = new AdaptiveServerDataType<Double>(SQLDataType.DOUBLE, "double precision");
    public static final AdaptiveServerDataType<Double>     FLOAT                      = new AdaptiveServerDataType<Double>(SQLDataType.FLOAT, "float");
    public static final AdaptiveServerDataType<Float>      REAL                       = new AdaptiveServerDataType<Float>(SQLDataType.REAL, "real");
    public static final AdaptiveServerDataType<BigDecimal> DECIMAL                    = new AdaptiveServerDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final AdaptiveServerDataType<BigDecimal> DEC                        = new AdaptiveServerDataType<BigDecimal>(SQLDataType.DECIMAL, "dec");
    public static final AdaptiveServerDataType<BigDecimal> NUMERIC                    = new AdaptiveServerDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric");
    public static final AdaptiveServerDataType<Boolean>    BIT                        = new AdaptiveServerDataType<Boolean>(SQLDataType.BIT, "bit");
    public static final AdaptiveServerDataType<String>     VARCHAR                    = new AdaptiveServerDataType<String>(SQLDataType.VARCHAR, "varchar");
    public static final AdaptiveServerDataType<String>     CHAR                       = new AdaptiveServerDataType<String>(SQLDataType.CHAR, "char");
    public static final AdaptiveServerDataType<String>     LONGVARCHAR                = new AdaptiveServerDataType<String>(SQLDataType.LONGVARCHAR, "text");
    public static final AdaptiveServerDataType<String>     NCHAR                      = new AdaptiveServerDataType<String>(SQLDataType.NCHAR, "nchar");
    public static final AdaptiveServerDataType<String>     NVARCHAR                   = new AdaptiveServerDataType<String>(SQLDataType.NVARCHAR, "nvarchar");
    public static final AdaptiveServerDataType<String>     TEXT                       = new AdaptiveServerDataType<String>(SQLDataType.CLOB, "text");
    public static final AdaptiveServerDataType<Date>       DATE                       = new AdaptiveServerDataType<Date>(SQLDataType.DATE, "date");
    public static final AdaptiveServerDataType<Time>       TIME                       = new AdaptiveServerDataType<Time>(SQLDataType.TIME, "time");
    public static final AdaptiveServerDataType<Timestamp>  DATETIME                   = new AdaptiveServerDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime");
    public static final AdaptiveServerDataType<Timestamp>  TIMESTAMP                  = new AdaptiveServerDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime");
    public static final AdaptiveServerDataType<byte[]>     BINARY                     = new AdaptiveServerDataType<byte[]>(SQLDataType.BINARY, "binary");
    public static final AdaptiveServerDataType<byte[]>     VARBINARY                  = new AdaptiveServerDataType<byte[]>(SQLDataType.VARBINARY, "varbinary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final AdaptiveServerDataType<byte[]>  __BLOB                     = new AdaptiveServerDataType<byte[]>(SQLDataType.BLOB, "binary");
    protected static final AdaptiveServerDataType<byte[]>  __LONGVARBINARY            = new AdaptiveServerDataType<byte[]>(SQLDataType.LONGVARBINARY, "varbinary");
    protected static final AdaptiveServerDataType<Boolean> __BOOLEAN                  = new AdaptiveServerDataType<Boolean>(SQLDataType.BOOLEAN, "bit");
    protected static final AdaptiveServerDataType<String>  __LONGNVARCHAR             = new AdaptiveServerDataType<String>(SQLDataType.LONGNVARCHAR, "unitext");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final AdaptiveServerDataType<BigInteger> __BIGINTEGER = new AdaptiveServerDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final AdaptiveServerDataType<String>     UNICHAR               = new AdaptiveServerDataType<String>(SQLDataType.NCHAR, "unichar");
    public static final AdaptiveServerDataType<String>     UNITEXT               = new AdaptiveServerDataType<String>(SQLDataType.NCLOB, "unitext");
    public static final AdaptiveServerDataType<String>     UNIVARCHAR            = new AdaptiveServerDataType<String>(SQLDataType.NVARCHAR, "univarchar");
    public static final AdaptiveServerDataType<BigDecimal> MONEY                 = new AdaptiveServerDataType<BigDecimal>(SQLDataType.DECIMAL, "money");
    public static final AdaptiveServerDataType<BigDecimal> SMALLMONEY            = new AdaptiveServerDataType<BigDecimal>(SQLDataType.DECIMAL, "smallmoney");
    public static final AdaptiveServerDataType<Timestamp>  SMALLDATETIME         = new AdaptiveServerDataType<Timestamp>(SQLDataType.TIMESTAMP, "smalldatetime");
    public static final AdaptiveServerDataType<Timestamp>  BIGDATETIME           = new AdaptiveServerDataType<Timestamp>(SQLDataType.TIMESTAMP, "bigdatetime");
    public static final AdaptiveServerDataType<Time>       BIGTIME               = new AdaptiveServerDataType<Time>(SQLDataType.TIME, "bigtime");
    public static final AdaptiveServerDataType<byte[]>     IMAGE                 = new AdaptiveServerDataType<byte[]>(SQLDataType.BINARY, "image");


    private AdaptiveServerDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.ADAPTIVESERVER, sqlDataType, sqlDataType.getType(), typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.ADAPTIVESERVER, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.ADAPTIVESERVER, typeName);
    }
}
