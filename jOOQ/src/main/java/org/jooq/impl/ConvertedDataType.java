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

import java.util.List;
import java.util.Map;

import org.jooq.Binding;
import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Nullability;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SQLDialect;

/**
 * A <code>DataType</code> used for converted types using {@link Converter}
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class ConvertedDataType<T, U> extends AbstractDataTypeX<U> {

    final AbstractDataTypeX<T>  delegate;
    final Binding<? super T, U> binding;

    ConvertedDataType(AbstractDataTypeX<T> delegate, Binding<? super T, U> binding) {
        super(delegate.getQualifiedName(), delegate.getCommentPart());

        this.delegate = delegate;
        this.binding = binding;

        // [#9492] For backwards compatibility reasons, a legacy type registers
        //         itself in the static type registry
        new LegacyConvertedDataType<>(delegate, binding);
    }

    @Override
    AbstractDataTypeX<U> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability newNullability,
        Collation newCollation,
        CharacterSet newCharacterSet,
        boolean newIdentity,
        Field<U> newDefaultValue
    ) {
        return (AbstractDataTypeX) delegate.construct(
            newPrecision,
            newScale,
            newLength,
            newNullability,
            newCollation,
            newCharacterSet,
            newIdentity,
            (Field) newDefaultValue
        ).asConvertedDataType(binding);
    }

    @Override
    public final Row getRow() {
        return delegate.getRow();
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return delegate.getRecordType();
    }

    @Override
    public final boolean isRecord() {
        return delegate.isRecord();
    }

    @Override
    public final boolean isMultiset() {
        return delegate.isMultiset();
    }

    @Override
    public final DataType<U> getSQLDataType() {
        return (DataType<U>) delegate.getSQLDataType();
    }

    @Override
    public final DataType<U> getDataType(Configuration configuration) {
        return (DataType<U>) delegate.getDataType(configuration);
    }

    @Override
    public final Binding<?, U> getBinding() {
        return binding;
    }

    @Override
    public final Class<U> getType() {
        return binding.converter().toType();
    }

    @Override
    public final SQLDialect getDialect() {
        return delegate.getDialect();
    }

    @Override
    public final Nullability nullability() {
        return delegate.nullability();
    }

    @Override
    public final Collation collation() {
        return delegate.collation();
    }

    @Override
    public final CharacterSet characterSet() {
        return delegate.characterSet();
    }

    @Override
    public final boolean identity() {
        return delegate.identity();
    }

    @Override
    public final Field<U> default_() {
        return (Field<U>) delegate.default_();
    }

    @Override
    final String typeName0() {
        return delegate.typeName0();
    }

    @Override
    final String castTypePrefix0() {
        return delegate.castTypePrefix0();
    }

    @Override
    final String castTypeSuffix0() {
        return delegate.castTypeSuffix0();
    }

    @Override
    final String castTypeName0() {
        return delegate.castTypeName0();
    }

    @Override
    final Class<?> tType0() {
        return delegate.tType0();
    }

    @Override
    final Class<U> uType0() {
        return binding.converter().toType();
    }

    @Override
    final Integer precision0() {
        return delegate.precision0();
    }

    @Override
    final Integer scale0() {
        return delegate.scale0();
    }

    @Override
    final Integer length0() {
        return delegate.length0();
    }

    @Override
    public final U convert(Object object) {
        if (getConverter().toType().isInstance(object))
            return (U) object;

        // [#12155] Avoid double conversion passes between Result and custom List<UserType>
        else if (delegate.isMultiset() && !(object instanceof Result))
            return (U) object;

        // [#12413] Avoid double conversion passes between Record and custom object types
        //          - List is what we produce when reading XML or JSON nested data in standard SQL
        //          - Map is what we produce in SQL Server (which doesn't support JSON_ARRAY)
        else if (delegate.isRecord() && !(object instanceof Record || object instanceof List || object instanceof Map))
            return (U) object;

        // [#3200] Try to convert arbitrary objects to T
        else
            return ((Converter<T, U>) getConverter()).from(delegate.convert(object));
    }

    @Override
    public final <X> DataType<X> asConvertedDataType(Converter<? super U, X> converter) {
        return super.asConvertedDataType(new ChainedConverterBinding(getBinding(), converter));
    }

    final DataType<T> delegate() {
        return delegate instanceof ConvertedDataType ? ((ConvertedDataType) delegate).delegate() : delegate;
    }
}

