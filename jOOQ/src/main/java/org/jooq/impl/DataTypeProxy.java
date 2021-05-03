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

import static org.jooq.tools.StringUtils.defaultIfNull;

import org.jooq.Binding;
import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Nullability;
import org.jooq.SQLDialect;
import org.jooq.tools.StringUtils;

/**
 * A mutable proxy for a temporary {@link DataType}, which can be replaced by a
 * more specific data type once it is known.
 *
 * @author Lukas Eder
 */
final class DataTypeProxy<T> extends AbstractDataType<T> {
    private AbstractDataType<T> type;
    private final Integer       overridePrecision;
    private final Integer       overrideScale;
    private final Integer       overrideLength;
    private final Nullability   overrideNullability;
    private final Collation     overrideCollation;
    private final CharacterSet  overrideCharacterSet;
    private final Boolean       overrideIdentity;
    private final Field<T>      overrideDefaultValue;

    DataTypeProxy(AbstractDataType<T> type) {
        this(type, null, null, null, null, null, null, null, null);
    }

    private DataTypeProxy(
        AbstractDataType<T> type,
        Integer overridePrecision,
        Integer overrideScale,
        Integer overrideLength,
        Nullability overrideNullability,
        Collation overrideCollation,
        CharacterSet overrideCharacterSet,
        Boolean overrideIdentity,
        Field<T> overrideDefaultValue
    ) {
        super(type.getQualifiedName(), type.getCommentPart());

        this.type = type;
        this.overridePrecision = overridePrecision;
        this.overrideScale = overrideScale;
        this.overrideLength = overrideLength;
        this.overrideNullability = overrideNullability;
        this.overrideCollation = overrideCollation;
        this.overrideCharacterSet = overrideCharacterSet;
        this.overrideIdentity = overrideIdentity;
        this.overrideDefaultValue = overrideDefaultValue;
    }

    final AbstractDataType<T> type() {
        return type;
    }

    final void type(AbstractDataType<T> t) {
        this.type = t;
    }

    @Override
    public final Name getQualifiedName() {
        return type.getQualifiedName();
    }

    @Override
    public final DataType<T> getSQLDataType() {
        return type.getSQLDataType();
    }

    @Override
    public final DataType<T> getDataType(Configuration configuration) {
        return type.getDataType(configuration);
    }

    @Override
    public final Binding<?, T> getBinding() {
        return type.getBinding();
    }

    @Override
    public final Class<T> getType() {
        return type.getType();
    }

    @Override
    public final SQLDialect getDialect() {
        return type.getDialect();
    }

    @Override
    public final Nullability nullability() {
        return defaultIfNull(overrideNullability, type.nullability());
    }

    @Override
    public final DataType<T> nullability(Nullability n) {
        return new DataTypeProxy<>(
            this,
            overridePrecision,
            overrideScale,
            overrideLength,
            n,
            overrideCollation,
            overrideCharacterSet,
            overrideIdentity,
            overrideDefaultValue
        );
    }

    @Override
    public final Collation collation() {
        return defaultIfNull(overrideCollation, type.collation());
    }

    @Override
    public final DataType<T> collation(Collation c) {
        return new DataTypeProxy<>(
            this,
            overridePrecision,
            overrideScale,
            overrideLength,
            overrideNullability,
            c,
            overrideCharacterSet,
            overrideIdentity,
            overrideDefaultValue
        );
    }

    @Override
    public final CharacterSet characterSet() {
        return defaultIfNull(overrideCharacterSet, type.characterSet());
    }

    @Override
    public final DataType<T> characterSet(CharacterSet c) {
        return new DataTypeProxy<>(
            this,
            overridePrecision,
            overrideScale,
            overrideLength,
            overrideNullability,
            overrideCollation,
            c,
            overrideIdentity,
            overrideDefaultValue
        );
    }

    @Override
    public final boolean identity() {
        return defaultIfNull(overrideIdentity, type.identity());
    }

    @Override
    public final DataType<T> identity(boolean i) {
        return new DataTypeProxy<>(
            this,
            overridePrecision,
            overrideScale,
            overrideLength,
            overrideNullability,
            overrideCollation,
            overrideCharacterSet,
            i,
            overrideDefaultValue
        );
    }

    @Override
    public final Field<T> default_() {
        return defaultIfNull(overrideDefaultValue, type.default_());
    }

    @Override
    public final DataType<T> default_(Field<T> d) {
        return new DataTypeProxy<>(
            this,
            overridePrecision,
            overrideScale,
            overrideLength,
            overrideNullability,
            overrideCollation,
            overrideCharacterSet,
            overrideIdentity,
            d
        );
    }

    @Override
    final String typeName0() {
        return type.typeName0();
    }

    @Override
    final String castTypePrefix0() {
        return type.castTypePrefix0();
    }

    @Override
    final String castTypeSuffix0() {
        return type.castTypeSuffix0();
    }

    @Override
    final String castTypeName0() {
        return type.castTypeName0();
    }

    @Override
    final Class<?> tType0() {
        return type.tType0();
    }

    @Override
    final Class<T> uType0() {
        return type.uType0();
    }

    @Override
    final Integer precision0() {
        return defaultIfNull(overridePrecision, type.precision0());
    }

    @Override
    final AbstractDataType<T> precision1(Integer p, Integer s) {
        return new DataTypeProxy<>(
            this,
            p,
            s,
            overrideLength,
            overrideNullability,
            overrideCollation,
            overrideCharacterSet,
            overrideIdentity,
            overrideDefaultValue
        );
    }

    @Override
    final Integer scale0() {
        return defaultIfNull(overrideScale, type.scale0());
    }

    @Override
    final AbstractDataType<T> scale1(Integer s) {
        return new DataTypeProxy<>(
            this,
            overridePrecision,
            s,
            overrideLength,
            overrideNullability,
            overrideCollation,
            overrideCharacterSet,
            overrideIdentity,
            overrideDefaultValue
        );
    }

    @Override
    final Integer length0() {
        return defaultIfNull(overrideLength, type.length0());
    }

    @Override
    final AbstractDataType<T> length1(Integer l) {
        return new DataTypeProxy<>(
            this,
            overridePrecision,
            overrideScale,
            l,
            overrideNullability,
            overrideCollation,
            overrideCharacterSet,
            overrideIdentity,
            overrideDefaultValue
        );
    }
}