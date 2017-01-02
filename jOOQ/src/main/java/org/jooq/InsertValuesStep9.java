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

import javax.annotation.Generated;

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <code><pre>
 * using(configuration)
 *       .insertInto(table, field1, field2, field3, .., field8, field9)
 *       .values(valueA1, valueA2, valueA3, .., valueA8, valueA9)
 *       .values(valueB1, valueB2, valueB3, .., valueB8, valueB9)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface InsertValuesStep9<R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> extends InsertOnDuplicateStep<R> {

    /**
     * Add values to the insert statement.
     */
    @Support
    InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9);

    /**
     * Add values to the insert statement.
     */
    @Support
    InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9);

    /**
     * Add values to the insert statement.
     */
    @Support
    InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> values(Collection<?> values);

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement
     * <p>
     * This variant of the <code>INSERT .. SELECT</code> statement expects a
     * select returning exactly as many fields as specified previously in the
     * <code>INTO</code> clause:
     * {@link DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field)}
     */
    @Support
    InsertOnDuplicateStep<R> select(Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select);
}
