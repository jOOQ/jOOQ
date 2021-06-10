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

import java.util.stream.Stream;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.Name;

/**
 * An internal base implementation of {@link Fields}, implementing all the
 * convenience methods.
 *
 * @author Lukas Eder
 */
interface FieldsTrait extends Fields {

    @Override
    default Field<?>[] fields() {
        return fieldsRow().fields();
    }

    @Override
    default Stream<Field<?>> fieldStream() {
        return fieldsRow().fieldStream();
    }

    @Override
    default <T> Field<T> field(Field<T> field) {
        return fieldsRow().field(field);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String)}.
     */
    @Deprecated
    @Override
    default Field<?> field(String name) {
        return fieldsRow().field(name);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String, Class)}.
     */
    @Deprecated
    @Override
    default <T> Field<T> field(String name, Class<T> type) {
        return fieldsRow().field(name, type);
    }


    /**
     * @deprecated This method hides static import {@link DSL#field(String, DataType)}.
     */
    @Deprecated
    @Override
    default <T> Field<T> field(String name, DataType<T> dataType) {
        return fieldsRow().field(name, dataType);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(Name)}.
     */
    @Deprecated
    @Override
    default Field<?> field(Name name) {
        return fieldsRow().field(name);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(Name, Class)}.
     */
    @Deprecated
    @Override
    default <T> Field<T> field(Name name, Class<T> type) {
        return fieldsRow().field(name, type);
    }

    /**
     * @deprecated This method hides static import {@link DSL#field(String, DataType)}.
     */
    @Deprecated
    @Override
    default <T> Field<T> field(Name name, DataType<T> dataType) {
        return fieldsRow().field(name, dataType);
    }

    @Override
    default Field<?> field(int index) {
        return fieldsRow().field(index);
    }

    @Override
    default <T> Field<T> field(int index, Class<T> type) {
        return fieldsRow().field(index, type);
    }

    @Override
    default <T> Field<T> field(int index, DataType<T> dataType) {
        return fieldsRow().field(index, dataType);
    }

    @Override
    default Field<?>[] fields(Field<?>... fields) {
        return fieldsRow().fields(fields);
    }

    @Override
    default Field<?>[] fields(String... names) {
        return fieldsRow().fields(names);
    }

    @Override
    default Field<?>[] fields(Name... names) {
        return fieldsRow().fields(names);
    }

    @Override
    default Field<?>[] fields(int... indexes) {
        return fieldsRow().fields(indexes);
    }

    @Override
    default int indexOf(Field<?> field) {
        return fieldsRow().indexOf(field);
    }

    @Override
    default int indexOf(String name) {
        return fieldsRow().indexOf(name);
    }

    @Override
    default int indexOf(Name name) {
        return fieldsRow().indexOf(name);
    }

    @Override
    default Class<?>[] types() {
        return fieldsRow().types();
    }

    @Override
    default Class<?> type(int index) {
        return fieldsRow().type(index);
    }

    @Override
    default Class<?> type(String name) {
        return fieldsRow().type(name);
    }

    @Override
    default Class<?> type(Name name) {
        return fieldsRow().type(name);
    }

    @Override
    default DataType<?>[] dataTypes() {
        return fieldsRow().dataTypes();
    }

    @Override
    default DataType<?> dataType(int index) {
        return fieldsRow().dataType(index);
    }

    @Override
    default DataType<?> dataType(String name) {
        return fieldsRow().dataType(name);
    }

    @Override
    default DataType<?> dataType(Name name) {
        return fieldsRow().dataType(name);
    }
}
