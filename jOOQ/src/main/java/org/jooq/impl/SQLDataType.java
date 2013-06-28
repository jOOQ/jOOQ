/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
 * . Neither the name "jOOQ" nor the names of its contributors may be
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
package org.jooq.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.UUID;

import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.util.ase.ASEDataType;
import org.jooq.util.cubrid.CUBRIDDataType;
import org.jooq.util.db2.DB2DataType;
import org.jooq.util.derby.DerbyDataType;
import org.jooq.util.firebird.FirebirdDataType;
import org.jooq.util.h2.H2DataType;
import org.jooq.util.hsqldb.HSQLDBDataType;
import org.jooq.util.ingres.IngresDataType;
import org.jooq.util.mariadb.MariaDBDataType;
import org.jooq.util.mysql.MySQLDataType;
import org.jooq.util.oracle.OracleDataType;
import org.jooq.util.postgres.PostgresDataType;
import org.jooq.util.sqlite.SQLiteDataType;
import org.jooq.util.sqlserver.SQLServerDataType;
import org.jooq.util.sybase.SybaseDataType;



/**
 * The SQL standard data types, as described in {@link Types}.
 * <p>
 * These types are usually the ones that are referenced by generated source
 * code. Most RDBMS have an almost 1:1 mapping between their vendor-specific
 * types and the ones in this class (except Oracle). Some RDBMS also have
 * extensions, e.g. for geospacial data types. See the dialect-specific data
 * type classes for more information.
 *
 * @author Lukas Eder
 */
public final class SQLDataType {

    // -------------------------------------------------------------------------
    // String types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#VARCHAR} type
     */
    public static final DataType<String> VARCHAR = new DefaultDataType<String>(null, String.class, "varchar");

    /**
     * The {@link Types#CHAR} type
     */
    public static final DataType<String> CHAR = new DefaultDataType<String>(null, String.class, "char");

    /**
     * The {@link Types#LONGVARCHAR} type
     */
    public static final DataType<String> LONGVARCHAR = new DefaultDataType<String>(null, String.class, "longvarchar");

    /**
     * The {@link Types#CLOB} type
     */
    public static final DataType<String> CLOB = new DefaultDataType<String>(null, String.class, "clob");

    /**
     * The {@link Types#NVARCHAR} type
     */
    public static final DataType<String> NVARCHAR = new DefaultDataType<String>(null, String.class, "nvarchar");

    /**
     * The {@link Types#NCHAR} type
     */
    public static final DataType<String> NCHAR = new DefaultDataType<String>(null, String.class, "nchar");

    /**
     * The {@link Types#LONGNVARCHAR} type
     */
    public static final DataType<String> LONGNVARCHAR = new DefaultDataType<String>(null, String.class, "longnvarchar");

    /**
     * The {@link Types#NCLOB} type
     */
    public static final DataType<String> NCLOB = new DefaultDataType<String>(null, String.class, "nclob");

    // -------------------------------------------------------------------------
    // Boolean types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#BOOLEAN} type
     */
    public static final DataType<Boolean> BOOLEAN = new DefaultDataType<Boolean>(null, Boolean.class, "boolean");

    /**
     * The {@link Types#BIT} type
     */
    public static final DataType<Boolean> BIT = new DefaultDataType<Boolean>(null, Boolean.class, "bit");

    // -------------------------------------------------------------------------
    // Integer types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#TINYINT} type
     */
    public static final DataType<Byte> TINYINT = new DefaultDataType<Byte>(null, Byte.class, "tinyint");

    /**
     * The {@link Types#SMALLINT} type
     */
    public static final DataType<Short> SMALLINT = new DefaultDataType<Short>(null, Short.class, "smallint");

    /**
     * The {@link Types#INTEGER} type
     */
    public static final DataType<Integer> INTEGER = new DefaultDataType<Integer>(null, Integer.class, "integer");

    /**
     * The {@link Types#BIGINT} type
     */
    public static final DataType<Long> BIGINT = new DefaultDataType<Long>(null, Long.class, "bigint");

    /**
     * The zero-scale {@link Types#DECIMAL} type
     */
    public static final DataType<BigInteger> DECIMAL_INTEGER = new DefaultDataType<BigInteger>(null, BigInteger.class, "decimal_integer");

    // -------------------------------------------------------------------------
    // Unsigned integer types
    // -------------------------------------------------------------------------

    /**
     * The unsigned {@link Types#TINYINT} type
     */
    public static final DataType<UByte> TINYINTUNSIGNED = new DefaultDataType<UByte>(null, UByte.class, "tinyintunsigned");

    /**
     * The unsigned {@link Types#SMALLINT} type
     */
    public static final DataType<UShort> SMALLINTUNSIGNED = new DefaultDataType<UShort>(null, UShort.class, "smallintunsigned");

    /**
     * The unsigned {@link Types#INTEGER} type
     */
    public static final DataType<UInteger> INTEGERUNSIGNED = new DefaultDataType<UInteger>(null, UInteger.class, "integerunsigned");

    /**
     * The unsigned {@link Types#BIGINT} type
     */
    public static final DataType<ULong> BIGINTUNSIGNED = new DefaultDataType<ULong>(null, ULong.class, "bigintunsigned");

    // -------------------------------------------------------------------------
    // Floating point types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#DOUBLE} type
     */
    public static final DataType<Double> DOUBLE = new DefaultDataType<Double>(null, Double.class, "double");

    /**
     * The {@link Types#FLOAT} type
     */
    public static final DataType<Double> FLOAT = new DefaultDataType<Double>(null, Double.class, "float");

    /**
     * The {@link Types#REAL} type
     */
    public static final DataType<Float> REAL = new DefaultDataType<Float>(null, Float.class, "real");

    // -------------------------------------------------------------------------
    // Numeric types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#NUMERIC} type
     */
    public static final DataType<BigDecimal> NUMERIC = new DefaultDataType<BigDecimal>(null, BigDecimal.class, "numeric");

    /**
     * The {@link Types#DECIMAL} type
     */
    public static final DataType<BigDecimal> DECIMAL = new DefaultDataType<BigDecimal>(null, BigDecimal.class, "decimal");

    // -------------------------------------------------------------------------
    // Datetime types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#DATE} type
     */
    public static final DataType<Date> DATE = new DefaultDataType<Date>(null, Date.class, "date");

    /**
     * The {@link Types#TIMESTAMP} type
     */
    public static final DataType<Timestamp> TIMESTAMP = new DefaultDataType<Timestamp>(null, Timestamp.class, "timestamp");

    /**
     * The {@link Types#TIME} type
     */
    public static final DataType<Time> TIME = new DefaultDataType<Time>(null, Time.class, "time");

    /**
     * The SQL standard <code>INTERVAL YEAR TO MONTH</code> data type
     */
    public static final DataType<YearToMonth> INTERVALYEARTOMONTH = new DefaultDataType<YearToMonth>(null, YearToMonth.class, "interval year to month");

    /**
     * The SQL standard <code>INTERVAL DAY TO SECOND</code> data type
     */
    public static final DataType<DayToSecond> INTERVALDAYTOSECOND = new DefaultDataType<DayToSecond>(null, DayToSecond.class, "interval day to second");

    // -------------------------------------------------------------------------
    // Binary types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#BINARY} type
     */
    public static final DataType<byte[]> BINARY = new DefaultDataType<byte[]>(null, byte[].class, "binary");

    /**
     * The {@link Types#VARBINARY} type
     */
    public static final DataType<byte[]> VARBINARY = new DefaultDataType<byte[]>(null, byte[].class, "varbinary");

    /**
     * The {@link Types#LONGVARBINARY} type
     */
    public static final DataType<byte[]> LONGVARBINARY = new DefaultDataType<byte[]>(null, byte[].class, "longvarbinary");

    /**
     * The {@link Types#BLOB} type
     */
    public static final DataType<byte[]> BLOB = new DefaultDataType<byte[]>(null, byte[].class, "blob");

    // -------------------------------------------------------------------------
    // Other types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#OTHER} type
     */
    public static final DataType<Object> OTHER = new DefaultDataType<Object>(null, Object.class, "other");

    /**
     * The {@link ResultSet} type
     * <p>
     * This is not a SQL or JDBC standard. This type simulates REF CURSOR types
     * and similar constructs
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final DataType<Result<Record>> RESULT = new DefaultDataType<Result<Record>>(null, (Class) Result.class, "result");

    /**
     * The {@link UUID} type
     * <p>
     * This is not a SQL or JDBC standard. This type handles UUID types where
     * they are supported
     */
    public static final DataType<UUID> UUID = new DefaultDataType<UUID>(null, UUID.class, "uuid");

    // -------------------------------------------------------------------------
    // Static initialisation of dialect-specific data types
    // -------------------------------------------------------------------------

    static {
        // Load all dialect-specific data types
        // TODO [#650] Make this more reliable using a data type registry

        try {
            Class.forName(ASEDataType.class.getName());
            Class.forName(CUBRIDDataType.class.getName());
            Class.forName(DB2DataType.class.getName());
            Class.forName(DerbyDataType.class.getName());
            Class.forName(FirebirdDataType.class.getName());
            Class.forName(H2DataType.class.getName());
            Class.forName(HSQLDBDataType.class.getName());
            Class.forName(IngresDataType.class.getName());
            Class.forName(MariaDBDataType.class.getName());
            Class.forName(MySQLDataType.class.getName());
            Class.forName(OracleDataType.class.getName());
            Class.forName(PostgresDataType.class.getName());
            Class.forName(SQLiteDataType.class.getName());
            Class.forName(SQLServerDataType.class.getName());
            Class.forName(SybaseDataType.class.getName());
        } catch (Exception ignore) {}
    }

    /**
     * No instances
     */
    private SQLDataType() {}
}
