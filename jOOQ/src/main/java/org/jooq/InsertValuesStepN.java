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

import java.util.Collection;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <code><pre>
 * using(configuration)
 *       .insertInto(table, fields)
 *       .values(fields)
 *       .values(fields)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface InsertValuesStepN<R extends Record> extends InsertOnDuplicateStep<R> {

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> values(Object... values);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> values(Field<?>... values);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> values(Collection<?> values);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> values(RowN values);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> values(Record values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     *
     * @see Rows#toRowArray(Function, Function)
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> valuesOfRows(RowN... values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     *
     * @see Rows#toRowList(Function, Function)
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> valuesOfRows(Collection<? extends RowN> values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> valuesOfRecords(Record... values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> valuesOfRecords(Collection<? extends Record> values);

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement
     * <p>
     * This variant of the <code>INSERT .. SELECT</code> statement expects a
     * select returning exactly as many fields as specified previously in the
     * <code>INTO</code> clause:
     * {@link DSLContext#insertInto(Table)}
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnDuplicateStep<R> select(Select<? extends Record> select);
}
