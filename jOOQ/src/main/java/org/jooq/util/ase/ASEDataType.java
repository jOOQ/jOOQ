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
package org.jooq.util.ase;

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
 * Supported data types for the {@link SQLDialect#ASE} dialect
 *
 * @see <a href="http://infocenter.sybase.com/help/topic/com.sybase.infocenter.dc36271.1550/html/blocks/X31825.htm">http://infocenter.sybase.com/help/topic/com.sybase.infocenter.dc36271.1550/html/blocks/X31825.htm</a>
 * @author Lukas Eder
 */
public class ASEDataType<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long                      serialVersionUID           = -4442303192680774346L;

    // -------------------------------------------------------------------------
    // Default SQL data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final ASEDataType<Byte>       TINYINT                    = new ASEDataType<Byte>(SQLDataType.TINYINT, "tinyint");
    public static final ASEDataType<Short>      SMALLINT                   = new ASEDataType<Short>(SQLDataType.SMALLINT, "smallint");
    public static final ASEDataType<Short>      UNSIGNEDSMALLLINT          = new ASEDataType<Short>(SQLDataType.SMALLINT, "unsigned smallint");
    public static final ASEDataType<Integer>    INT                        = new ASEDataType<Integer>(SQLDataType.INTEGER, "int");
    public static final ASEDataType<Integer>    INTEGER                    = new ASEDataType<Integer>(SQLDataType.INTEGER, "integer");
    public static final ASEDataType<Integer>    UNSIGNEDINT                = new ASEDataType<Integer>(SQLDataType.INTEGER, "unsigned int");
    public static final ASEDataType<Long>       BIGINT                     = new ASEDataType<Long>(SQLDataType.BIGINT, "bigint");
    public static final ASEDataType<Long>       UNSIGNEDBIGINT             = new ASEDataType<Long>(SQLDataType.BIGINT, "unsigned bigint");
    public static final ASEDataType<Double>     DOUBLE_PRECISION           = new ASEDataType<Double>(SQLDataType.DOUBLE, "double precision");
    public static final ASEDataType<Double>     FLOAT                      = new ASEDataType<Double>(SQLDataType.FLOAT, "float");
    public static final ASEDataType<Float>      REAL                       = new ASEDataType<Float>(SQLDataType.REAL, "real");
    public static final ASEDataType<BigDecimal> DECIMAL                    = new ASEDataType<BigDecimal>(SQLDataType.DECIMAL, "decimal");
    public static final ASEDataType<BigDecimal> DEC                        = new ASEDataType<BigDecimal>(SQLDataType.DECIMAL, "dec");
    public static final ASEDataType<BigDecimal> NUMERIC                    = new ASEDataType<BigDecimal>(SQLDataType.NUMERIC, "numeric");
    public static final ASEDataType<Boolean>    BIT                        = new ASEDataType<Boolean>(SQLDataType.BIT, "bit");
    public static final ASEDataType<String>     VARCHAR                    = new ASEDataType<String>(SQLDataType.VARCHAR, "varchar");
    public static final ASEDataType<String>     CHAR                       = new ASEDataType<String>(SQLDataType.CHAR, "char");
    public static final ASEDataType<String>     LONGVARCHAR                = new ASEDataType<String>(SQLDataType.LONGVARCHAR, "text");
    public static final ASEDataType<String>     NCHAR                      = new ASEDataType<String>(SQLDataType.NCHAR, "nchar");
    public static final ASEDataType<String>     NVARCHAR                   = new ASEDataType<String>(SQLDataType.NVARCHAR, "nvarchar");
    public static final ASEDataType<String>     TEXT                       = new ASEDataType<String>(SQLDataType.CLOB, "text");
    public static final ASEDataType<Date>       DATE                       = new ASEDataType<Date>(SQLDataType.DATE, "date");
    public static final ASEDataType<Time>       TIME                       = new ASEDataType<Time>(SQLDataType.TIME, "time");
    public static final ASEDataType<Timestamp>  DATETIME                   = new ASEDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime");
    public static final ASEDataType<Timestamp>  TIMESTAMP                  = new ASEDataType<Timestamp>(SQLDataType.TIMESTAMP, "datetime");
    public static final ASEDataType<byte[]>     BINARY                     = new ASEDataType<byte[]>(SQLDataType.BINARY, "binary");
    public static final ASEDataType<byte[]>     VARBINARY                  = new ASEDataType<byte[]>(SQLDataType.VARBINARY, "varbinary");

    // -------------------------------------------------------------------------
    // Compatibility types for supported SQLDataTypes
    // -------------------------------------------------------------------------

    protected static final ASEDataType<byte[]>  __BLOB                     = new ASEDataType<byte[]>(SQLDataType.BLOB, "binary");
    protected static final ASEDataType<byte[]>  __LONGVARBINARY            = new ASEDataType<byte[]>(SQLDataType.LONGVARBINARY, "varbinary");
    protected static final ASEDataType<Boolean> __BOOLEAN                  = new ASEDataType<Boolean>(SQLDataType.BOOLEAN, "bit");
    protected static final ASEDataType<String>  __LONGNVARCHAR             = new ASEDataType<String>(SQLDataType.LONGNVARCHAR, "unitext");

    // -------------------------------------------------------------------------
    // Compatibility types for supported Java types
    // -------------------------------------------------------------------------

    protected static final ASEDataType<BigInteger> __BIGINTEGER = new ASEDataType<BigInteger>(SQLDataType.DECIMAL_INTEGER, "decimal");

    // -------------------------------------------------------------------------
    // Dialect-specific data types and synonyms thereof
    // -------------------------------------------------------------------------

    public static final ASEDataType<String>     UNICHAR               = new ASEDataType<String>(SQLDataType.NCHAR, "unichar");
    public static final ASEDataType<String>     UNITEXT               = new ASEDataType<String>(SQLDataType.NCLOB, "unitext");
    public static final ASEDataType<String>     UNIVARCHAR            = new ASEDataType<String>(SQLDataType.NVARCHAR, "univarchar");
    public static final ASEDataType<String>     SYSNAME               = new ASEDataType<String>(SQLDataType.VARCHAR, "sysname");
    public static final ASEDataType<String>     LONGSYSNAME           = new ASEDataType<String>(SQLDataType.VARCHAR, "longsysname");
    public static final ASEDataType<BigDecimal> MONEY                 = new ASEDataType<BigDecimal>(SQLDataType.DECIMAL, "money");
    public static final ASEDataType<BigDecimal> SMALLMONEY            = new ASEDataType<BigDecimal>(SQLDataType.DECIMAL, "smallmoney");
    public static final ASEDataType<Timestamp>  SMALLDATETIME         = new ASEDataType<Timestamp>(SQLDataType.TIMESTAMP, "smalldatetime");
    public static final ASEDataType<Timestamp>  BIGDATETIME           = new ASEDataType<Timestamp>(SQLDataType.TIMESTAMP, "bigdatetime");
    public static final ASEDataType<Time>       BIGTIME               = new ASEDataType<Time>(SQLDataType.TIME, "bigtime");
    public static final ASEDataType<byte[]>     IMAGE                 = new ASEDataType<byte[]>(SQLDataType.BINARY, "image");


    private ASEDataType(SQLDataType<T> sqlDataType, String typeName) {
        super(SQLDialect.ASE, sqlDataType, sqlDataType.getType(), typeName);
    }

    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return getDataType(SQLDialect.ASE, type);
    }

    public static DataType<Object> getDefaultDataType(String typeName) {
        return getDefaultDataType(SQLDialect.ASE, typeName);
    }
}
