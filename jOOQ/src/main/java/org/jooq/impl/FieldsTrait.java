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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.impl.FieldsImpl.internalFieldsRow0;

import java.util.stream.Stream;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.Name;

import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * An internal base implementation of {@link Fields}, implementing all the
 * convenience methods.
 *
 * @author Lukas Eder
 */
interface FieldsTrait extends Fields {

    @Internal
    default Fields internalFieldsRow() {
        return fieldsRow();
    }

    @Override
    default Fields fieldsIncludingHidden() {
        return internalFieldsRow().fieldsIncludingHidden();
    }

    @Override
    default Field<?>[] fields() {
        return internalFieldsRow().fields();
    }

    @Override
    default Stream<Field<?>> fieldStream() {
        return internalFieldsRow().fieldStream();
    }

    @Override
    default <T> Field<T> field(Field<T> field) {
        return internalFieldsRow().field(field);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String)}.
     */
    @Deprecated
    @Override
    default Field<?> field(String name) {
        return internalFieldsRow().field(name);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String, Class)}.
     */
    @Deprecated
    @Override
    default <T> Field<T> field(String name, Class<T> type) {
        return internalFieldsRow().field(name, type);
    }


    /**
     * @deprecated This method hides static import {@link DSL#field(String, DataType)}.
     */
    @Deprecated
    @Override
    default <T> Field<T> field(String name, DataType<T> dataType) {
        return internalFieldsRow().field(name, dataType);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(Name)}.
     */
    @Deprecated
    @Override
    default Field<?> field(Name name) {
        return internalFieldsRow().field(name);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(Name, Class)}.
     */
    @Deprecated
    @Override
    default <T> Field<T> field(Name name, Class<T> type) {
        return internalFieldsRow().field(name, type);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String, DataType)}.
     */
    @Deprecated
    @Override
    default <T> Field<T> field(Name name, DataType<T> dataType) {
        return internalFieldsRow().field(name, dataType);
    }

    @Override
    default Field<?> field(int index) {
        return internalFieldsRow().field(index);
    }

    @Override
    default <T> Field<T> field(int index, Class<T> type) {
        return internalFieldsRow().field(index, type);
    }

    @Override
    default <T> Field<T> field(int index, DataType<T> dataType) {
        return internalFieldsRow().field(index, dataType);
    }

    @Override
    default Field<?>[] fields(Field<?>... fields) {
        return internalFieldsRow().fields(fields);
    }

    @Override
    default Field<?>[] fields(String... names) {
        return internalFieldsRow().fields(names);
    }

    @Override
    default Field<?>[] fields(Name... names) {
        return internalFieldsRow().fields(names);
    }

    @Override
    default Field<?>[] fields(int... indexes) {
        return internalFieldsRow().fields(indexes);
    }

    @Override
    default int indexOf(Field<?> field) {
        return internalFieldsRow().indexOf(field);
    }

    @Override
    default int indexOf(String name) {
        return internalFieldsRow().indexOf(name);
    }

    @Override
    default int indexOf(Name name) {
        return internalFieldsRow().indexOf(name);
    }

    @Override
    default Class<?>[] types() {
        return internalFieldsRow0(this).types();
    }

    @Override
    default Class<?> type(int index) {
        return internalFieldsRow0(this).type(index);
    }

    @Override
    default Class<?> type(String name) {
        return internalFieldsRow0(this).type(name);
    }

    @Override
    default Class<?> type(Name name) {
        return internalFieldsRow0(this).type(name);
    }

    @Override
    default DataType<?>[] dataTypes() {
        return internalFieldsRow0(this).dataTypes();
    }

    @Override
    default DataType<?> dataType(int index) {
        return internalFieldsRow0(this).dataType(index);
    }

    @Override
    default DataType<?> dataType(String name) {
        return internalFieldsRow0(this).dataType(name);
    }

    @Override
    default DataType<?> dataType(Name name) {
        return internalFieldsRow0(this).dataType(name);
    }
}
