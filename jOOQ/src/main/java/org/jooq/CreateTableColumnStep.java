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

import java.util.Collection;

/**
 * A {@link Query} that can create tables.
 *
 * @author Lukas Eder
 */
public interface CreateTableColumnStep extends CreateTableConstraintStep {

    /**
     * Add a column to the column list of the <code>CREATE TABLE</code>
     * statement.
     * <p>
     * This is the same as calling {@link #column(Field, DataType)} with
     * {@link Field#getDataType()} as the argument data type.
     */
    @Support
    CreateTableColumnStep column(Field<?> field);

    /**
     * Add a column to the column list of the <code>CREATE TABLE</code> statement.
     */
    @Support
    <T> CreateTableColumnStep column(Field<T> field, DataType<T> type);

    /**
     * Add a column to the column list of the <code>CREATE TABLE</code> statement.
     */
    @Support
    CreateTableColumnStep column(Name field, DataType<?> type);

    /**
     * Add a column to the column list of the <code>CREATE TABLE</code> statement.
     */
    @Support
    CreateTableColumnStep column(String field, DataType<?> type);

    /**
     * Add several columns to the column list of the <code>CREATE TABLE</code>
     * statement.
     * <p>
     * This is the same as calling {@link #column(Field, DataType)} for each
     * column, with {@link Field#getDataType()} as the argument data type.
     */
    @Support
    CreateTableColumnStep columns(Field<?>... fields);

    /**
     * Add several columns to the column list of the <code>CREATE TABLE</code>
     * statement.
     * <p>
     * This is the same as calling {@link #column(Field, DataType)} for each
     * column, with {@link Field#getDataType()} as the argument data type.
     */
    @Support
    CreateTableColumnStep columns(Collection<? extends Field<?>> fields);
}
