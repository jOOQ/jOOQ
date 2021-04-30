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
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;

/**
 * @author Lukas Eder
 * @deprecated - 3.14.0 - [#9492] [#10312] - A compatibility implementation for
 *             converted data types, which registers itself in the static type
 *             registry for legacy reasons.
 */
@Deprecated
final class LegacyConvertedDataType<T, U> extends DefaultDataType<U> {

    private final DataType<T>           delegate;

    @SuppressWarnings("unchecked")
    LegacyConvertedDataType(AbstractDataType<T> delegate, Binding<? super T, U> binding) {
        super(
            null,
            binding.converter().toType(),
            binding,
            delegate.getQualifiedName(),
            delegate.getTypeName(),
            delegate.getCastTypeName(),
            delegate.precisionDefined() ? delegate.precision() : null,
            delegate.scaleDefined() ? delegate.scale() : null,
            delegate.lengthDefined() ? delegate.length() : null,
            delegate.nullability(),
            (Field<U>) delegate.defaultValue()
        );

        this.delegate = delegate;
    }

    @Override
    public int getSQLType() {
        return delegate.getSQLType();
    }

    @Override
    public String getTypeName(Configuration configuration) {
        return delegate.getTypeName(configuration);
    }

    @Override
    public String getCastTypeName(Configuration configuration) {
        return delegate.getCastTypeName(configuration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public U convert(Object object) {
        if (getConverter().toType().isInstance(object))
            return (U) object;

        // [#3200] Try to convert arbitrary objects to T
        else
            return ((Converter<T, U>) getConverter()).from(delegate.convert(object));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <X> DataType<X> asConvertedDataType(Converter<? super U, X> converter) {
        return super.asConvertedDataType(new ChainedConverterBinding(getBinding(), converter));
    }
}
