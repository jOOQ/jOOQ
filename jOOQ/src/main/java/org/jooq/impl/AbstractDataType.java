/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jooq.ArrayRecord;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.MasterDataType;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SQLDialectNotSupportedException;
import org.jooq.UDTRecord;

/**
 * A common base class for data types.
 * <p>
 * This also acts as a static data type registry for jOOQ internally.
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
public abstract class AbstractDataType<T> implements DataType<T> {

    /**
     * Generated UID
     */
    private static final long                            serialVersionUID = 4155588654449505119L;
    private static final Map<String, DataType<?>>[]      typesByTypeName;
    private static final Map<Class<?>, DataType<?>>[]    typesByType;
    private static final Map<DataType<?>, DataType<?>>[] typesBySQLDataType;
    private static final Map<Class<?>, DataType<?>>      sqlDataTypesByType;

    private final SQLDialect                             dialect;

    private final SQLDataType<T>                         sqlDataType;
    private final Class<? extends T>                     type;
    private final String                                 castTypeName;
    private final String                                 typeName;

    private final Class<? extends T[]>                   arrayType;

    private final boolean                                hasPrecisionAndScale;

    static {
        typesBySQLDataType = new Map[SQLDialect.values().length];
        typesByTypeName = new Map[SQLDialect.values().length];
        typesByType = new Map[SQLDialect.values().length];

        for (SQLDialect dialect : SQLDialect.values()) {
            typesBySQLDataType[dialect.ordinal()] = new LinkedHashMap<DataType<?>, DataType<?>>();
            typesByTypeName[dialect.ordinal()] = new LinkedHashMap<String, DataType<?>>();
            typesByType[dialect.ordinal()] = new LinkedHashMap<Class<?>, DataType<?>>();
        }

        sqlDataTypesByType = new LinkedHashMap<Class<?>, DataType<?>>();
    }

    protected AbstractDataType(SQLDialect dialect, SQLDataType<T> sqldatatype, Class<? extends T> type, String typeName) {
        this(dialect, sqldatatype, type, typeName, typeName, false);
    }

    protected AbstractDataType(SQLDialect dialect, SQLDataType<T> sqldatatype, Class<? extends T> type, String typeName, String castTypeName) {
        this(dialect, sqldatatype, type, typeName, castTypeName, false);
    }

    protected AbstractDataType(SQLDialect dialect, SQLDataType<T> sqldatatype, Class<? extends T> type, String typeName, boolean hasPrecisionAndScale) {
        this(dialect, sqldatatype, type, typeName, typeName, hasPrecisionAndScale);
    }

    protected AbstractDataType(SQLDialect dialect, SQLDataType<T> sqldatatype, Class<? extends T> type, String typeName, String castTypeName, boolean hasPrecisionAndScale) {
        this.dialect = dialect;
        this.sqlDataType = sqldatatype;
        this.type = type;
        this.typeName = typeName;
        this.castTypeName = castTypeName;
        this.hasPrecisionAndScale = hasPrecisionAndScale;
        this.arrayType = (Class<? extends T[]>) Array.newInstance(type, 0).getClass();

        // Dialect-specific data types
        if (dialect != null) {
            if (typesByTypeName[dialect.ordinal()].get(FieldTypeHelper.normalise(typeName)) == null) {
                typesByTypeName[dialect.ordinal()].put(FieldTypeHelper.normalise(typeName), this);
            }

            if (typesByType[dialect.ordinal()].get(type) == null) {
                typesByType[dialect.ordinal()].put(type, this);
            }

            if (typesBySQLDataType[dialect.ordinal()].get(sqldatatype) == null) {
                typesBySQLDataType[dialect.ordinal()].put(sqldatatype, this);
            }
        }

        // Global data types
        else {
            if (sqlDataTypesByType.get(type) == null) {
                sqlDataTypesByType.put(type, this);
            }
        }
    }

    @Override
    public final SQLDataType<T> getSQLDataType() {
        return sqlDataType;
    }

    @Override
    public final DataType<T> getDataType(Configuration configuration) {

        // If this is a SQLDataType find the most suited dialect-specific
        // data type
        if (getDialect() == null) {
            DataType<?> dataType = typesBySQLDataType[configuration.getDialect().ordinal()].get(this);

            if (dataType != null) {
                return (DataType<T>) dataType;
            }
        }

        // If this is already the dialect's specific data type, return this
        else if (getDialect() == configuration.getDialect()) {
            return this;
        }

        // If the SQL data type is not available stick with this data type
        else if (getSQLDataType() == null) {
            return this;
        }

        // If this is another dialect's specific data type, recurse
        else {
            getSQLDataType().getDataType(configuration);
        }

        return this;
    }

    @Override
    public final int getSQLType() {
        if (type == Blob.class) {
            return Types.BLOB;
        }
        else if (type == Boolean.class) {
            return Types.BOOLEAN;
        }
        else if (type == BigInteger.class) {
            return Types.BIGINT;
        }
        else if (type == BigDecimal.class) {
            return Types.DECIMAL;
        }
        else if (type == Byte.class) {
            return Types.TINYINT;
        }
        else if (type == byte[].class) {
            return Types.BLOB;
        }
        else if (type == Clob.class) {
            return Types.CLOB;
        }
        else if (type == Date.class) {
            return Types.DATE;
        }
        else if (type == Double.class) {
            return Types.DOUBLE;
        }
        else if (type == Float.class) {
            return Types.FLOAT;
        }
        else if (type == Integer.class) {
            return Types.INTEGER;
        }
        else if (type == Long.class) {
            return Types.BIGINT;
        }
        else if (type == Short.class) {
            return Types.SMALLINT;
        }
        else if (type == String.class) {
            return Types.VARCHAR;
        }
        else if (type == Time.class) {
            return Types.TIME;
        }
        else if (type == Timestamp.class) {
            return Types.TIMESTAMP;
        }

        // The type byte[] is handled earlier.
        else if (type.isArray()) {
            return Types.ARRAY;
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            return Types.ARRAY;
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            return Types.VARCHAR;
        }
        else if (MasterDataType.class.isAssignableFrom(type)) {
            return Types.BIGINT;
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            return Types.STRUCT;
        }
        else if (Result.class.isAssignableFrom(type)) {
            switch (dialect) {
                case ORACLE:
                case H2:
                    return -10; // OracleTypes.CURSOR;

                case POSTGRES:
                default:
                    return Types.OTHER;
            }
        }
        else {
            return Types.OTHER;
        }
    }

    @Override
    public final Class<? extends T> getType() {
        return type;
    }

    @Override
    public final Class<?> getType(int precision, int scale) {
        if (hasPrecisionAndScale) {
            return FieldTypeHelper.getClass(Types.NUMERIC, precision, scale);
        }

        // If no precise type could be guessed, take the default
        return getType();
    }

    @Override
    public final Class<? extends T[]> getArrayType() {
        return arrayType;
    }

    @Override
    public final String getTypeName() {
        return typeName;
    }

    @Override
    public String getTypeName(Configuration configuration) {
        return getDataType(configuration).getTypeName();
    }

    @Override
    public final String getCastTypeName() {
        return castTypeName;
    }

    @Override
    public final String getCastTypeName(Configuration configuration, int precision, int scale) {

        // Remove existing precision / scale information, first
        String result = getCastTypeName(configuration).replaceAll("\\([^\\)]*\\)", "");

        if (precision != 0) {
            if (scale != 0) {
                result += "(" + precision + ", " + scale + ")";
            }
            else {
                result += "(" + precision + ")";
            }
        }

        return result;
    }

    @Override
    public String getCastTypeName(Configuration configuration) {
        return getDataType(configuration).getCastTypeName();
    }

    @Override
    public final DataType<T[]> getArrayDataType() {
        return new ArrayDataType<T>(this);
    }

    @Override
    public final <A extends ArrayRecord<T>> DataType<A> asArrayDataType(Class<A> arrayDataType) {
        return new DefaultDataType<A>(dialect, arrayDataType, typeName, castTypeName);
    }

    @Override
    public final <M extends MasterDataType<T>> DataType<M> asMasterDataType(Class<M> masterDataType) {
        return new DefaultDataType<M>(dialect, masterDataType, typeName, castTypeName);
    }

    @Override
    public final <E extends EnumType> DataType<E> asEnumDataType(Class<E> enumDataType) {
        return new DefaultDataType<E>(dialect, enumDataType, typeName, castTypeName);
    }

    @Override
    public final SQLDialect getDialect() {
        return dialect;
    }

    @Override
    public final T convert(Object object) {
        return TypeUtils.convert(object, type);
    }

    protected static DataType<Object> getDefaultDataType(SQLDialect dialect, String typeName) {
        return new DefaultDataType<Object>(dialect, Object.class, typeName, typeName);
    }

    @SuppressWarnings("deprecation")
    protected static DataType<?> getDataType(SQLDialect dialect, String typeName) {
        DataType<?> result = typesByTypeName[dialect.ordinal()].get(FieldTypeHelper.normalise(typeName));

        // UDT data types and others are registered using SQL99
        if (result == null) {
            result = typesByTypeName[SQLDialect.SQL99.ordinal()].get(FieldTypeHelper.normalise(typeName));
        }

        if (result == null) {
            // #366 Don't log a warning here. The warning is logged when
            // catching the exception in jOOQ-codegen
            throw new SQLDialectNotSupportedException("Type " + typeName + " is not supported in dialect " + dialect, false);
        }

        return result;
    }

    protected static <T> DataType<T> getDataType(SQLDialect dialect, Class<? extends T> type) {

        // Recurse for arrays
        if (byte[].class != type && type.isArray()) {
            return (DataType<T>) getDataType(dialect, type.getComponentType()).getArrayDataType();
        }

        // Base types are registered statically
        else {
            DataType<?> result = null;

            if (dialect != null) {
                result = typesByType[dialect.ordinal()].get(type);
            }

            if (result == null) {

                // jOOQ data types are handled here
                if (EnumType.class.isAssignableFrom(type) ||
                    UDTRecord.class.isAssignableFrom(type) ||
                    MasterDataType.class.isAssignableFrom(type)) {

                    for (SQLDialect d : SQLDialect.values()) {
                        result = typesByType[d.ordinal()].get(type);

                        if (result != null) {
                            break;
                        }
                    }
                }
            }

            if (result == null) {

                // Object has a default fallback, if it is not registered explicitly
                if (type == Object.class) {
                    return new DefaultDataType<T>(dialect, (Class<? extends T>) Object.class, "", "");
                }

                // Default fallback types
                else if (sqlDataTypesByType.get(type) != null) {
                    return (DataType<T>) sqlDataTypesByType.get(type);
                }

                // All other data types are illegal
                else {
                    throw new SQLDialectNotSupportedException("Type " + type + " is not supported in dialect " + dialect);
                }
            }

            return (DataType<T>) result;
        }
    }

    @Override
    public final boolean isNumeric() {
        return Number.class.isAssignableFrom(type);
    }

    @Override
    public final boolean isString() {
        return type == String.class;
    }

    @Override
    public final boolean isTemporal() {
        return java.util.Date.class.isAssignableFrom(type);
    }

    @Override
    public final boolean isBinary() {
        return type == byte[].class;
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + " [" + type + ", " + typeName + "]";
    }
}
