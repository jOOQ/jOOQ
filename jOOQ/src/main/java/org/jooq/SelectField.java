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
package org.jooq;

import java.util.function.Function;

import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;

/**
 * A <code>QueryPart</code> to be used exclusively in <code>SELECT</code>
 * clauses.
 * <p>
 * Instances of this type cannot be created directly, only of its subtypes.
 *
 * @author Lukas Eder
 */
public non-sealed interface SelectField<T> extends SelectFieldOrAsterisk, Named, Typed<T> {

    // ------------------------------------------------------------------------
    // Aliasing
    // ------------------------------------------------------------------------

    /**
     * Create an alias for this field.
     * <p>
     * Note that the case-sensitivity of the returned field depends on
     * {@link Settings#getRenderQuotedNames()}. By default, field aliases are
     * quoted, and thus case-sensitive in many SQL dialects!
     *
     * @param alias The alias name
     * @return The field alias
     */
    @NotNull
    @Support
    SelectField<T> as(String alias);

    /**
     * Create an alias for this field.
     * <p>
     * Note that the case-sensitivity of the returned field depends on
     * {@link Settings#getRenderQuotedNames()} and the {@link Name}. By default,
     * field aliases are quoted, and thus case-sensitive in many SQL dialects -
     * use {@link DSL#unquotedName(String...)} for case-insensitive aliases.
     * <p>
     * If the argument {@link Name#getName()} is qualified, then the
     * {@link Name#last()} part will be used.
     *
     * @param alias The alias name
     * @return The field alias
     */
    @NotNull
    @Support
    SelectField<T> as(Name alias);

    /**
     * Create an alias for this field based on another field's name.
     *
     * @param otherField The other field whose name this field is aliased with.
     * @return The field alias.
     */
    @NotNull
    @Support
    SelectField<T> as(Field<?> otherField);

    // ------------------------------------------------------------------------
    // Ad-hoc converters
    // ------------------------------------------------------------------------

    /**
     * Apply an ad-hoc data type {@link Binding} to this field expression.
     * <p>
     * Rather than attaching data type bindings at code generation time, or
     * creating cumbersome expressions using
     * {@link DataType#asConvertedDataType(Binding)}, this method allows for
     * creating a derived field expression with an ad-hoc data type binding for
     * single query usage.
     *
     * @param <U> The user type.
     * @param binding The binding to be applied on any operations using this
     *            field.
     * @return A derived field expression using a new binding.
     */
    @NotNull
    <U> SelectField<U> convert(Binding<T, U> binding);

    /**
     * Apply an ad-hoc data type {@link Converter} to this field expression.
     * <p>
     * Rather than attaching data type converters at code generation time, or
     * creating cumbersome expressions using
     * {@link DataType#asConvertedDataType(Converter)}, this method allows for
     * creating a derived field expression with an ad-hoc data type converter for
     * single query usage.
     *
     * @param <U> The user type.
     * @param converter The converter to be applied on any operations using this
     *            field.
     * @return A derived field expression using a new converter.
     */
    @NotNull
    <U> SelectField<U> convert(Converter<T, U> converter);

    /**
     * Apply an ad-hoc data type {@link Converter} to this field expression.
     * <p>
     * Rather than attaching data type converters at code generation time, or
     * creating cumbersome expressions using
     * {@link DataType#asConvertedDataType(Class, Function, Function)}, this
     * method allows for creating a derived field expression with an ad-hoc data
     * type converter for single query usage.
     *
     * @param <U> The user type.
     * @param converter The converter to be applied on any operations using this
     *            field.
     * @return A derived field expression using a new converter.
     */
    @NotNull
    <U> SelectField<U> convert(
        Class<U> toType,
        Function<? super T, ? extends U> from,
        Function<? super U, ? extends T> to
    );

    /**
     * Apply an ad-hoc read-only data type {@link Converter} to this field
     * expression.
     * <p>
     * Rather than attaching data type converters at code generation time, or
     * creating cumbersome expressions using
     * {@link DataType#asConvertedDataTypeFrom(Class, Function)}, this method
     * allows for creating a derived field expression with an ad-hoc data type
     * converter for single query usage.
     *
     * @param <U> The user type.
     * @param converter The read-only converter to be applied on any operations
     *            using this field.
     * @return A derived field expression using a new converter.
     */
    @NotNull
    <U> SelectField<U> convertFrom(Class<U> toType, Function<? super T, ? extends U> from);

    /**
     * Apply an ad-hoc read-only data type {@link Converter} to this field
     * expression.
     * <p>
     * Rather than attaching data type converters at code generation time, or
     * creating cumbersome expressions using
     * {@link DataType#asConvertedDataTypeFrom(Class, Function)}, this method
     * allows for creating a derived field expression with an ad-hoc data type
     * converter for single query usage.
     * <p>
     * Unlike {@link #convertFrom(Class, Function)}, this method attempts to
     * work without an explicit {@link Class} reference for the underlying
     * {@link Converter#toType()}. There may be some edge cases where this
     * doesn't work, e.g. when nesting rows in arrays, the class literal is
     * required for reflective array creation.
     *
     * @param <U> The user type.
     * @param converter The read-only converter to be applied on any operations
     *            using this field.
     * @return A derived field expression using a new converter.
     */
    @NotNull
    <U> SelectField<U> convertFrom(Function<? super T, ? extends U> from);

    /**
     * Apply an ad-hoc write-only data type {@link Converter} to this field
     * expression.
     * <p>
     * Rather than attaching data type converters at code generation time, or
     * creating cumbersome expressions using
     * {@link DataType#asConvertedDataTypeTo(Class, Function)}, this method
     * allows for creating a derived field expression with an ad-hoc data type
     * converter for single query usage.
     *
     * @param <U> The user type.
     * @param converter The write-only converter to be applied on any operations
     *            using this field.
     * @return A derived field expression using a new converter.
     */
    @NotNull
    <U> SelectField<U> convertTo(Class<U> toType, Function<? super U, ? extends T> to);

    /**
     * Apply an ad-hoc write-only data type {@link Converter} to this field
     * expression.
     * <p>
     * Rather than attaching data type converters at code generation time, or
     * creating cumbersome expressions using
     * {@link DataType#asConvertedDataTypeTo(Class, Function)}, this method
     * allows for creating a derived field expression with an ad-hoc data type
     * converter for single query usage.
     * <p>
     * Unlike {@link #convertTo(Class, Function)}, this method attempts to work
     * without an explicit {@link Class} reference for the underlying
     * {@link Converter#toType()}. There may be some edge cases where this
     * doesn't work, e.g. when nesting rows in arrays, the class literal is
     * required for reflective array creation.
     *
     * @param <U> The user type.
     * @param converter The write-only converter to be applied on any operations
     *            using this field.
     * @return A derived field expression using a new converter.
     */
    @NotNull
    <U> SelectField<U> convertTo(Function<? super U, ? extends T> to);

}
