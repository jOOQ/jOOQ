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
 *       .insertInto(table, field1, field2, field3, field4)
 *       .values(field1, field2, field3, field4)
 *       .values(field1, field2, field3, field4)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface InsertValuesStep4<R extends Record, T1, T2, T3, T4> extends InsertOnDuplicateStep<R> {

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep4<R, T1, T2, T3, T4> values(T1 value1, T2 value2, T3 value3, T4 value4);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep4<R, T1, T2, T3, T4> values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep4<R, T1, T2, T3, T4> values(Collection<?> values);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep4<R, T1, T2, T3, T4> values(Row4<T1, T2, T3, T4> values);

    /**
     * Add a single row of values to the insert statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep4<R, T1, T2, T3, T4> values(Record4<T1, T2, T3, T4> values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     *
     * @see Rows#toRowArray(Function, Function, Function, Function)
     */
    @NotNull @CheckReturnValue
    @Support
    @SuppressWarnings("unchecked")
    InsertValuesStep4<R, T1, T2, T3, T4> valuesOfRows(Row4<T1, T2, T3, T4>... values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     *
     * @see Rows#toRowList(Function, Function, Function, Function)
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep4<R, T1, T2, T3, T4> valuesOfRows(Collection<? extends Row4<T1, T2, T3, T4>> values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     */
    @NotNull @CheckReturnValue
    @Support
    @SuppressWarnings("unchecked")
    InsertValuesStep4<R, T1, T2, T3, T4> valuesOfRecords(Record4<T1, T2, T3, T4>... values);

    /**
     * Add multiple rows of values to the insert statement.
     * <p>
     * This is equivalent to calling the other values clauses multiple times, but
     * allows for dynamic construction of row arrays.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStep4<R, T1, T2, T3, T4> valuesOfRecords(Collection<? extends Record4<T1, T2, T3, T4>> values);

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement
     * <p>
     * This variant of the <code>INSERT .. SELECT</code> statement expects a
     * select returning exactly as many fields as specified previously in the
     * <code>INTO</code> clause:
     * {@link DSLContext#insertInto(Table, Field, Field, Field, Field)}
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnDuplicateStep<R> select(Select<? extends Record4<T1, T2, T3, T4>> select);
}
