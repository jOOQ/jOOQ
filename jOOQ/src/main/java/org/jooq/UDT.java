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
 */
package org.jooq;

import java.util.stream.Stream;

/**
 * UDT definition
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface UDT<R extends UDTRecord<R>> extends QueryPart {

    /**
     * Get this UDT's fields as a {@link Row}.
     */
    Row fieldsRow();


    /**
     * Get this table's fields as a {@link Stream}.
     */
    Stream<Field<?>> fieldStream();


    /**
     * Get a specific field from this UDT.
     * <p>
     * Usually, this will return the field itself. However, if this is a row
     * from an aliased UDT, the field will be aliased accordingly.
     *
     * @see Row#field(Field)
     */
    <T> Field<T> field(Field<T> field);

    /**
     * Get a specific field from this UDT.
     *
     * @see Row#field(String)
     */
    Field<?> field(String name);

    /**
     * Get a specific field from this UDT.
     *
     * @see Row#field(Name)
     */
    Field<?> field(Name name);

    /**
     * Get a specific field from this UDT.
     *
     * @see Row#field(int)
     */
    Field<?> field(int index);

    /**
     * Get all fields from this UDT.
     *
     * @see Row#fields()
     */
    Field<?>[] fields();

    /**
     * Get all fields from this UDT, providing some fields.
     *
     * @return All available fields
     * @see Row#fields(Field...)
     */
    Field<?>[] fields(Field<?>... fields);

    /**
     * Get all fields from this UDT, providing some field names.
     *
     * @return All available fields
     * @see Row#fields(String...)
     */
    Field<?>[] fields(String... fieldNames);

    /**
     * Get all fields from this UDT, providing some field names.
     *
     * @return All available fields
     * @see Row#fields(Name...)
     */
    Field<?>[] fields(Name... fieldNames);

    /**
     * Get all fields from this UDT, providing some field indexes.
     *
     * @return All available fields
     * @see Row#fields(int...)
     */
    Field<?>[] fields(int... fieldIndexes);

    /**
     * Get the UDT catalog.
     */
    Catalog getCatalog();

    /**
     * Get the UDT schema.
     */
    Schema getSchema();

    /**
     * Get the UDT package.
     */
    Package getPackage();

    /**
     * The name of this UDT.
     */
    String getName();

    /**
     * @return The record type produced by this table.
     */
    Class<R> getRecordType();

    /**
     * Create a new {@link Record} of this UDT's type.
     *
     * @see DSLContext#newRecord(UDT)
     */
    R newRecord();

    /**
     * The UDT's data type as known to the database.
     */
    DataType<R> getDataType();

    /**
     * Whether this data type can be used from SQL statements.
     */
    boolean isSQLUsable();
}
