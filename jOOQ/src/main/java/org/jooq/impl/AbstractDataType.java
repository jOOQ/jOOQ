/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.Internal.arrayType;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.CLOB;
import static org.jooq.impl.SQLDataType.NCHAR;
import static org.jooq.impl.SQLDataType.NCLOB;
import static org.jooq.impl.SQLDataType.NVARCHAR;
import static org.jooq.impl.Tools.NO_SUPPORT_BINARY_TYPE_LENGTH;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

// ...
import org.jooq.Binding;
import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Comment;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Domain;
import org.jooq.EmbeddableRecord;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.LanguageContext;
import org.jooq.Name;
import org.jooq.Nullability;
// ...
import org.jooq.QualifiedRecord;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.XML;
import org.jooq.types.Interval;
import org.jooq.types.UNumber;

import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
abstract class AbstractDataType<T> extends AbstractNamed implements DataType<T> {
    static final Set<SQLDialect> NO_SUPPORT_TIMESTAMP_PRECISION = SQLDialect.supportedBy(DERBY, FIREBIRD);

    AbstractDataType(Name name, Comment comment) {
        super(name, comment);
    }

    @Override
    public abstract DataType<T> nullability(Nullability n);

    @Override
    public final DataType<T> nullable(boolean n) {
        return nullability(Nullability.of(n));
    }

    @Override
    public final boolean nullable() {
        return nullability().nullable();
    }

    @Override
    public final DataType<T> null_() {
        return nullable(true);
    }

    @Override
    public final DataType<T> notNull() {
        return nullable(false);
    }

    @Override
    public abstract DataType<T> collation(Collation c);

    @Override
    public abstract DataType<T> characterSet(CharacterSet c);

    @Override
    public abstract DataType<T> identity(boolean i);

    @Override
    public final DataType<T> defaultValue(T d) {
        return default_(d);
    }

    @Override
    public final DataType<T> defaultValue(Field<T> d) {
        return default_(d);
    }

    @Override
    public final Field<T> defaultValue() {
        return default_();
    }

    @Override
    public final DataType<T> default_(T d) {
        return default_(Tools.field(d, this));
    }

    @Override
    public abstract DataType<T> default_(Field<T> d);

    @Override
    @Deprecated
    public final DataType<T> defaulted(boolean d) {
        return defaultValue(d ? Tools.field(null, this) : null);
    }

    @Override
    public final boolean defaulted() {
        return defaultValue() != null;
    }

    @Override
    public final int precision() {
        Integer precision = precision0();
        return precision == null ? 0 : precision;
    }

    @Override
    public final DataType<T> precision(int p) {
        return precision0(p, scale());
    }

    @Override
    public final DataType<T> precision(int p, int s) {
        return precision0(p, s);
    }

    final AbstractDataType<T> precision0(Integer p, Integer s) {
        if (eq(precision0(), p) && eq(scale0(), s))
            return this;

        // [#4120] LOB types are not allowed to have precision
        else if (isLob())
            return this;
        else
            return precision1(p, s);
    }

    abstract AbstractDataType<T> precision1(Integer p, Integer s);

    @Override
    public final boolean hasPrecision() {
        Class<?> tType = tType0();

        return tType == BigInteger.class
            || tType == BigDecimal.class
            || tType == Timestamp.class
            || tType == Time.class
            || tType == LocalDateTime.class
            || tType == LocalTime.class
            || tType == OffsetDateTime.class
            || tType == OffsetTime.class
            || tType == Instant.class
        ;
    }

    @Override
    public final boolean precisionDefined() {
        return precision0() != null && hasPrecision();
    }

    @Override
    public final int scale() {
        Integer scale = scale0();
        return scale == null ? 0 : scale;
    }

    @Override
    public final DataType<T> scale(int s) {
        return scale0(s);
    }

    final AbstractDataType<T> scale0(Integer s) {
        if (eq(scale0(), s))
            return this;

        // [#4120] LOB types are not allowed to have scale
        else if (isLob())
            return this;
        else
            return scale1(s);
    }

    abstract AbstractDataType<T> scale1(Integer s);

    @Override
    public final boolean hasScale() {
        return tType0() == BigDecimal.class;
    }

    @Override
    public final boolean scaleDefined() {
        return scale0() != null && hasScale();
    }

    @Override
    public final DataType<T> length(int l) {
        return length0(l);
    }

    final AbstractDataType<T> length0(Integer l) {
        if (eq(length0(), l))
            return this;

        // [#4120] LOB types are not allowed to have length
        else if (isLob())
            return this;
        else
            return length1(l);
    }

    abstract AbstractDataType<T> length1(Integer l);

    @Override
    public final int length() {
        Integer length = length0();
        return length == null ? 0 : length;
    }

    @Override
    public final boolean hasLength() {
        Class<?> tType = tType0();
        return (tType == byte[].class || tType == String.class) && !isLob();
    }

    @Override
    public final boolean lengthDefined() {
        return length0() != null && hasLength();
    }

    @Override
    public /* final */ int getSQLType() {
        return getSQLType(DSL.using(getDialect()).configuration());
    }

    @Override
    public final int getSQLType(Configuration configuration) {
        // TODO [#1227] There is some confusion with these types, especially
        // when it comes to byte[] which can be mapped to BLOB, BINARY, VARBINARY
        Class<?> tType = tType0();

        if (tType == Blob.class)
            return Types.BLOB;
        else if (tType == Boolean.class)
            return Types.BOOLEAN;
        else if (tType == BigInteger.class)
            return Types.BIGINT;
        else if (tType == BigDecimal.class)
            return Types.DECIMAL;
        else if (tType == Byte.class)
            return Types.TINYINT;
        else if (tType == byte[].class)
            return Types.BLOB;
        else if (tType == Clob.class)
            return Types.CLOB;
        else if (Tools.isDate(tType)) {
            switch (configuration.family()) {






                default:
                    return Types.DATE;
            }
        }
        else if (tType == Double.class)
            return Types.DOUBLE;
        else if (tType == Float.class)
            return Types.FLOAT;
        else if (tType == Integer.class)
            return Types.INTEGER;
        else if (tType == Long.class)
            return Types.BIGINT;
        else if (tType == Short.class)
            return Types.SMALLINT;
        else if (tType == String.class)
            return Types.VARCHAR;
        else if (Tools.isTime(tType))
            return Types.TIME;
        else if (Tools.isTimestamp(tType))
            return Types.TIMESTAMP;

        // [#5779] Few JDBC drivers support the JDBC 4.2 TIME[STAMP]_WITH_TIMEZONE types.
        else if (tType == OffsetTime.class)
            return Types.VARCHAR;
        else if (tType == OffsetDateTime.class)
            return Types.VARCHAR;
        else if (tType == Instant.class)
            return Types.VARCHAR;

        // The type byte[] is handled earlier.
        else if (tType.isArray())
            return Types.ARRAY;














        else if (EnumType.class.isAssignableFrom(tType))
            return Types.VARCHAR;
        else if (QualifiedRecord.class.isAssignableFrom(tType))
            return Types.STRUCT;
        else if (Result.class.isAssignableFrom(tType)) {
            switch (configuration.family()) {

                case H2:
                    return -10; // OracleTypes.CURSOR;



                case POSTGRES:
                default:
                    return Types.OTHER;
            }
        }
        else
            return Types.OTHER;
    }

    @Override
    public /* non-final */ Domain<T> getDomain() {
        return null;
    }

    @Override
    public final Converter<?, T> getConverter() {
        return getBinding().converter();
    }

    @Override
    public final String getTypeName() {
        return typeName0();
    }

    @Override
    public /* non-final */ String getTypeName(Configuration configuration) {
        return getDataType(configuration).getTypeName();
    }

    @Override
    public final String getCastTypeName() {
        return getCastTypeName0(SQLDialect.DEFAULT);
    }

    private String getCastTypeName0(SQLDialect dialect) {

        // [#9958] We should be able to avoid checking for x > 0, but there may
        //         be a lot of data types constructed with a 0 value instead of
        //         a null value, historically, so removing this check would
        //         introduce a lot of regressions!
        if (lengthDefined() && length() > 0) {
            if (isBinary() && NO_SUPPORT_BINARY_TYPE_LENGTH.contains(dialect))
                return castTypeName0();




            else
                return castTypePrefix0() + "(" + length() + ")" + castTypeSuffix0();
        }
        else if (precisionDefined() && (isTimestamp() || precision() > 0)) {

            // [#8029] Not all dialects support precision on timestamp
            // syntax, possibly despite there being explicit or implicit
            // precision support in DDL.
            if (isTimestamp() && NO_SUPPORT_TIMESTAMP_PRECISION.contains(dialect))
                return castTypePrefix0() + castTypeSuffix0();








            else if (scaleDefined() && scale() > 0)
                return castTypePrefix0() + "(" + precision() + ", " + scale() + ")" + castTypeSuffix0();
            else
                return castTypePrefix0() + "(" + precision() + ")" + castTypeSuffix0();
        }
        else
            return castTypeName0();
    }

    @Override
    public /* non-final */ String getCastTypeName(Configuration configuration) {
        return ((AbstractDataType<T>) getDataType(configuration)).getCastTypeName0(configuration.dialect());
    }

    @Override
    public final Class<T[]> getArrayType() {
        return arrayType(getType());
    }

    @Override
    public final DataType<T[]> getArrayDataType() {
        return new ArrayDataType<>(this);
    }

    @Override
    public /* non-final */ Class<?> getArrayComponentType() {
        return null;
    }

    @Override
    public /* non-final */ DataType<?> getArrayComponentDataType() {
        return null;
    }

    @Override
    public /* non-final */ Row getRow() {
        return null;
    }

    @Override
    public /* non-final */ Class<? extends Record> getRecordType() {
        return null;
    }









    @Override
    public final <E extends EnumType> DataType<E> asEnumDataType(Class<E> enumDataType) {
        // TODO: Make EnumTypes implement Named
        String enumTypeName = Tools.enums(enumDataType)[0].getName();
        return new DefaultDataType<>(getDialect(), (DataType<E>) null, enumDataType, unquotedName(enumTypeName), enumTypeName, enumTypeName, precision0(), scale0(), length0(), nullability(), (Field) defaultValue());
    }

    @Override
    public /* non-final */ <U> DataType<U> asConvertedDataType(Converter<? super T, U> converter) {
        return asConvertedDataType(DefaultBinding.newBinding(converter, this, null));
    }

    @Override
    public /* non-final */ <U> DataType<U> asConvertedDataType(Binding<? super T, U> newBinding) {
        if (getBinding() == newBinding)
            return (DataType<U>) this;

        if (newBinding == null)
            newBinding = (Binding<? super T, U>) DefaultBinding.binding(this);

        return new ConvertedDataType<>((AbstractDataTypeX) this, newBinding);
    }

    @Override
    public /* final */ T convert(Object object) {

        // [#1441] Avoid unneeded type conversions to improve performance
        if (object == null)
            return null;
        else if (object.getClass() == getType())
            return (T) object;
        else
            return Convert.convert(object, getType());
    }

    @Override
    public final T[] convert(Object... objects) {
        return Convert.convertArray(objects, getConverter());
    }

    @Override
    public final List<T> convert(Collection<?> objects) {
        return Convert.convert(objects, getConverter());
    }

    @Override
    public final boolean isNumeric() {
        return Number.class.isAssignableFrom(tType0()) && !isInterval();
    }

    @Override
    public final boolean isInteger() {
        Class<?> tType = tType0();
        return UNumber.class.isAssignableFrom(tType)
            || Byte.class == tType
            || Short.class == tType
            || Integer.class == tType
            || Long.class == tType
        ;
    }

    @Override
    public final boolean isString() {
        return tType0() == String.class;
    }

    @Override
    public final boolean isNString() {
        DataType<T> t = getSQLDataType();
        return t == NCHAR
            || t == NCLOB
            || t == NVARCHAR

            // [#9540] [#10368] In case the constant literals haven't been initialised yet
            || NCHAR == null && "nchar".equals(t.getTypeName())
            || NCLOB == null && "nclob".equals(t.getTypeName())
            || NVARCHAR == null && "nvarchar".equals(t.getTypeName());
    }

    @Override
    public final boolean isDateTime() {
        Class<?> tType = tType0();
        return java.util.Date.class.isAssignableFrom(tType)
            || java.time.temporal.Temporal.class.isAssignableFrom(tType)
        ;
    }

    @Override
    public final boolean isDate() {
        Class<?> tType = tType0();
        return java.sql.Date.class.isAssignableFrom(tType)
            || java.time.LocalDate.class.isAssignableFrom(tType)
        ;
    }

    @Override
    public final boolean isTimestamp() {
        Class<?> tType = tType0();
        return java.sql.Timestamp.class.isAssignableFrom(tType)
            || java.time.LocalDateTime.class.isAssignableFrom(tType)
        ;
    }

    @Override
    public final boolean isTime() {
        Class<?> tType = tType0();
        return java.sql.Time.class.isAssignableFrom(tType)
            || java.time.LocalTime.class.isAssignableFrom(tType)
        ;
    }

    @Override
    public final boolean isTemporal() {
        return isDateTime() || isInterval();
    }

    @Override
    public final boolean isInterval() {
        return Interval.class.isAssignableFrom(tType0());
    }

    @Override
    public final boolean isLob() {
        DataType<T> t = getSQLDataType();

        if (t == this)
            return getTypeName().endsWith("lob");
        else
            return t == BLOB
                || t == CLOB
                || t == NCLOB

                // [#9540] [#10368] In case the constant literals haven't been initialised yet
                || BLOB == null && "blob".equals(t.getTypeName())
                || CLOB == null && "clob".equals(t.getTypeName())
                || NCLOB == null && "nclob".equals(t.getTypeName());
    }

    @Override
    public final boolean isBinary() {
        return tType0() == byte[].class;
    }

    @Override
    public final boolean isArray() {
        Class<?> tType = tType0();
        return 
            (!isBinary() && tType.isArray());
    }

    @Override
    public final boolean isEmbeddable() {
        return EmbeddableRecord.class.isAssignableFrom(tType0());
    }

    @Override
    public final boolean isUDT() {
        return QualifiedRecord.class.isAssignableFrom(tType0());
    }

    @Override
    public /* non-final */ boolean isRecord() {
        return Record.class.isAssignableFrom(tType0());
    }

    @Override
    public /* non-final */ boolean isMultiset() {
        return this instanceof MultisetDataType;
    }

    @Override
    public final boolean isEnum() {
        return EnumType.class.isAssignableFrom(tType0());
    }

    @Override
    public final boolean isJSON() {
        Class<?> tType = tType0();
        return tType == JSON.class || tType == JSONB.class;
    }

    @Override
    public final boolean isXML() {
        return tType0() == XML.class;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {















            default:
                ctx.visit(getQualifiedName());
                break;
        }
    }

    private static final boolean eq(Integer i1, Integer i2) {
        return (i1 == i2) || (i1 != null && i2 != null && i1.intValue() == i2.intValue());
    }

    abstract String typeName0();
    abstract String castTypePrefix0();
    abstract String castTypeSuffix0();
    abstract String castTypeName0();
    abstract Class<?> tType0();
    abstract Class<T> uType0();
    abstract Integer precision0();
    abstract Integer scale0();
    abstract Integer length0();

    // ------------------------------------------------------------------------
    // The Object API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return getCastTypeName() + (isArray() ? " array" : "") + " /* " + getType().getName() + " */";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDialect() == null) ? 0 : getDialect().hashCode());
        result = prime * result + length();
        result = prime * result + precision();
        result = prime * result + scale();
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((tType0() == null) ? 0 : tType0().hashCode());
        result = prime * result + ((typeName0() == null) ? 0 : typeName0().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AbstractDataType))
            return false;
        AbstractDataType<?> other = (AbstractDataType<?>) obj;
        if (getDialect() != other.getDialect())
            return false;
        if (!eq(length0(), other.length0()))
            return false;
        if (!eq(precision0(), other.precision0()))
            return false;
        if (!eq(scale0(), other.scale0()))
            return false;
        if (getType() == null) {
            if (other.getType() != null)
                return false;
        }
        else if (!getType().equals(other.getType()))
            return false;
        if (tType0() == null) {
            if (other.tType0() != null)
                return false;
        }
        else if (!tType0().equals(other.tType0()))
            return false;
        if (typeName0() == null) {
            if (other.typeName0() != null)
                return false;
        }
        else if (!typeName0().equals(other.typeName0()))
            return false;
        return true;
    }
}
