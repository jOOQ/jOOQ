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
 *       .insertInto(table, field1, field2)
 *       .values(field1, field2)
 *       .values(field1, field2)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface InsertValuesStep2<R extends Record, T1, T2> extends InsertOnDuplicateStep<R> {

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep2<R, T1, T2> values(T1 value1, T2 value2);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep2<R, T1, T2> values(Field<T1> value1, Field<T2> value2);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep2<R, T1, T2> values(Collection<?> values);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep2<R, T1, T2> values(Row2<T1, T2> values);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep2<R, T1, T2> values(Record2<T1, T2> values);

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
    @SuppressWarnings("unchecked")
    InsertValuesStep2<R, T1, T2> valuesOfRows(Row2<T1, T2>... values);

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
    InsertValuesStep2<R, T1, T2> valuesOfRows(Collection<? extends Row2<T1, T2>> values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     */
    @NotNull @CheckReturnValue
    @Support
    @SuppressWarnings("unchecked")
    InsertValuesStep2<R, T1, T2> valuesOfRecords(Record2<T1, T2>... values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep2<R, T1, T2> valuesOfRecords(Collection<? extends Record2<T1, T2>> values);

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement
     * <p>
     * This variant of the <code>INSERT .. SELECT</code> statement expects a
     * select returning exactly as many fields as specified previously in the
     * <code>INTO</code> clause:
     * {@link DSLContext#insertInto(Table, Field, Field)}
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnDuplicateStep<R> select(Select<? extends Record2<T1, T2>> select);
}
