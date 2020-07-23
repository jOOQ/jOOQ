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

import java.sql.SQLException;

import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Converters;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Nullability;
import org.jooq.SQLDialect;

/**
 * A <code>DataType</code> used for converted types using {@link Converter}
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class ConvertedDataType<T, U> extends AbstractDataType<U> {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = -2321926692580974126L;

    private final AbstractDataType<T>   delegate;
    private final Binding<? super T, U> binding;

    ConvertedDataType(AbstractDataType<T> delegate, Binding<? super T, U> binding) {
        super(delegate.getQualifiedName(), delegate.getCommentPart());

        this.delegate = delegate;
        this.binding = binding;
    }

    @Override
    AbstractDataType<U> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability newNullability,
        Collation newCollation,
        CharacterSet newCharacterSet,
        boolean newIdentity,
        Field<U> newDefaultValue
    ) {
        return (AbstractDataType) delegate.construct(
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

        // [#3200] Try to convert arbitrary objects to T
        else
            return ((Converter<T, U>) getConverter()).from(delegate.convert(object));
    }

    @Override
    public final <X> DataType<X> asConvertedDataType(Converter<? super U, X> converter) {
        return super.asConvertedDataType(new ChainedConverterBinding(getBinding(), converter));
    }

    /**
     * A binding that chains a new converter to an existing binding / converter.
     *
     * @author Lukas Eder
     */
    private static class ChainedConverterBinding<T, U1, U2> implements Binding<T, U2> {

        /**
         * Generated UID
         */
        private static final long       serialVersionUID = -5120352678786683423L;

        private final Binding<T, U1>    delegate;
        private final Converter<U1, U2> suffix;
        private final Converter<T, U2>  chained;

        ChainedConverterBinding(Binding<T, U1> delegate, Converter<U1, U2> converter) {
            this.delegate = delegate;
            this.suffix = converter;
            this.chained = Converters.of(delegate.converter(), converter);
        }

        @Override
        public Converter<T, U2> converter() {
            return chained;
        }

        @Override
        public void sql(BindingSQLContext<U2> ctx) throws SQLException {
            delegate.sql(ctx.convert(suffix));
        }

        @Override
        public void register(BindingRegisterContext<U2> ctx) throws SQLException {
            delegate.register(ctx.convert(suffix));
        }

        @Override
        public void set(BindingSetStatementContext<U2> ctx) throws SQLException {
            delegate.set(ctx.convert(suffix));
        }

        @Override
        public void set(BindingSetSQLOutputContext<U2> ctx) throws SQLException {
            delegate.set(ctx.convert(suffix));
        }

        @Override
        public void get(BindingGetResultSetContext<U2> ctx) throws SQLException {
            delegate.get(ctx.convert(suffix));
        }

        @Override
        public void get(BindingGetStatementContext<U2> ctx) throws SQLException {
            delegate.get(ctx.convert(suffix));
        }

        @Override
        public void get(BindingGetSQLInputContext<U2> ctx) throws SQLException {
            delegate.get(ctx.convert(suffix));
        }
    }
}