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

import org.jooq.Binding;
import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Nullability;
import org.jooq.SQLDialect;

/**
 * A mutable proxy for a temporary {@link DataType}, which can be replaced by a
 * more specific data type once it is known.
 *
 * @author Lukas Eder
 */
final class DataTypeProxy<T> extends AbstractDataType<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -5283787691586689803L;
    AbstractDataType<T>       type;

    DataTypeProxy(AbstractDataType<T> type) {
        super(type.getQualifiedName(), type.getCommentPart());

        this.type = type;
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
        return type.nullability();
    }

    @Override
    public final Collation collation() {
        return type.collation();
    }

    @Override
    public final CharacterSet characterSet() {
        return type.characterSet();
    }

    @Override
    public final boolean identity() {
        return type.identity();
    }

    @Override
    public final Field<T> default_() {
        return type.default_();
    }

    @Override
    final AbstractDataType<T> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability newNullability,
        Collation newCollation,
        CharacterSet newCharacterSet,
        boolean newIdentity,
        Field<T> newDefaultValue
    ) {
        return type.construct(newPrecision, newScale, newLength, newNullability, newCollation, newCharacterSet, newIdentity, newDefaultValue);
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
    final Integer precision0() {
        return type.precision0();
    }

    @Override
    final Integer scale0() {
        return type.scale0();
    }

    @Override
    final Integer length0() {
        return type.length0();
    }
}