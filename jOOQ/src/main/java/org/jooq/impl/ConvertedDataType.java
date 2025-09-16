/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import org.jooq.ContextConverter;
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.Converters;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Generator;
import org.jooq.Nullability;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.DefaultBinding.InternalBinding;
import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.impl.QOM.GenerationOption;

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
        boolean newHidden,
        boolean newRedacted,
        boolean newReadonly,
        Generator<?, ?, U> newGeneratedAlwaysAs,
        GenerationOption newGenerationOption,
        GenerationLocation newGenerationLocation,
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
            newHidden,
            newRedacted,
            newReadonly,
            (Generator) newGeneratedAlwaysAs,
            newGenerationOption,
            newGenerationLocation,
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
    public final DataType<U[]> getArrayDataType() {
        if (binding.arrayBinding() != null)
            return delegate.getArrayDataType().asConvertedDataType(binding.arrayBinding());
        else if (getBinding() instanceof InternalBinding)
            return delegate.getArrayDataType().asConvertedDataType(Converters.forArrays(binding.converter()));
        else
            throw new DataTypeException("Cannot create array data types from custom data types with custom bindings. Implement Binding::arrayBinding");
    }

    @Override
    public final DataType<?> getArrayComponentDataType() {
        DataType<?> d = delegate.getArrayComponentDataType();

        return d == null
             ? null
             : binding.arrayComponentBinding() != null
             ? d.asConvertedDataType((Binding) binding.arrayComponentBinding())
             : d.asConvertedDataType(Converters.forArrayComponents((Converter) binding.converter()));
    }

    @Override
    public final Class<?> getArrayComponentType() {
        DataType<?> d = getArrayComponentDataType();

        return d == null ? null : d.getType();
    }

    @Override
    public final DataType<U> getDataType(Configuration configuration) {
        return (DataType<U>) delegate.getDataType(configuration);
    }

    @Override
    public final String getCastTypeName() {
        return delegate.getCastTypeName();
    }

    @Override
    public final String getCastTypeName(Configuration configuration) {
        return delegate.getCastTypeName(configuration);
    }

    @Override
    public final String getTypeName() {
        return delegate.getTypeName();
    }

    @Override
    public final String getTypeName(Configuration configuration) {
        return delegate.getTypeName(configuration);
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
    public final boolean hidden() {
        return delegate.hidden();
    }

    @Override
    public final boolean redacted() {
        return delegate.redacted();
    }

    @Override
    public final boolean readonly() {
        return delegate.readonly();
    }

    @Override
    public final Generator<?, ?, U> generatedAlwaysAsGenerator() {
        return (Generator<?, ?, U>) delegate.generatedAlwaysAsGenerator();
    }

    @Override
    public final GenerationOption generationOption() {
        return delegate.generationOption();
    }

    @Override
    public final GenerationLocation generationLocation() {
        return delegate.generationLocation();
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
    final U convert(Object object, ConverterContext cc) {
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
        // [#16013] [#17428] Avoid repeated conversion in case of chained converters
        else
            return ((ContextConverter<T, U>) getConverter()).from((T) convert0(delegate(delegate), object, cc), cc);
    }

    @Override
    public final <X> DataType<X> asConvertedDataType(Converter<? super U, X> converter) {
        return super.asConvertedDataType(new ChainedConverterBinding(getBinding(), converter));
    }

    final DataType<T> delegate() {
        return delegate instanceof ConvertedDataType c ? c.delegate() : delegate;
    }

    static final DataType<?> delegate(DataType<?> type) {
        return type instanceof ConvertedDataType<?, ?> c ? c.delegate() : type;
    }
}

