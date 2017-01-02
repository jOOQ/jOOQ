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

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.FIREBIRD_3_0;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...

import java.util.Collection;

import javax.annotation.Generated;

/**
 * This type is used for the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.mergeInto(table)
 *       .using(select)
 *       .on(condition)
 *       .whenMatchedThenUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .whenNotMatchedThenInsert(field1, field2)
 *       .values(value1, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface MergeNotMatchedStep<R extends Record> extends MergeFinalStep<R> {

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement.
     * <p>
     * Unlike the {@link #whenNotMatchedThenInsert(Field...)} and
     * {@link #whenNotMatchedThenInsert(Collection)} methods, this will give
     * access to a MySQL-like API allowing for
     * <code>INSERT SET a = x, b = y</code> syntax.
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    MergeNotMatchedSetStep<R> whenNotMatchedThenInsert();

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1> MergeNotMatchedValuesStep1<R, T1> whenNotMatchedThenInsert(Field<T1> field1);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2> MergeNotMatchedValuesStep2<R, T1, T2> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3> MergeNotMatchedValuesStep3<R, T1, T2, T3> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4> MergeNotMatchedValuesStep4<R, T1, T2, T3, T4> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5> MergeNotMatchedValuesStep5<R, T1, T2, T3, T4, T5> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6> MergeNotMatchedValuesStep6<R, T1, T2, T3, T4, T5, T6> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7> MergeNotMatchedValuesStep7<R, T1, T2, T3, T4, T5, T6, T7> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8> MergeNotMatchedValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> MergeNotMatchedValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> MergeNotMatchedValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> MergeNotMatchedValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> MergeNotMatchedValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> MergeNotMatchedValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> MergeNotMatchedValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> MergeNotMatchedValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> MergeNotMatchedValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> MergeNotMatchedValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> MergeNotMatchedValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> MergeNotMatchedValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> MergeNotMatchedValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> MergeNotMatchedValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> MergeNotMatchedValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

    /**
     * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    MergeNotMatchedValuesStepN<R> whenNotMatchedThenInsert(Field<?>... fields);

    /**
     * Add the <code>WHEN MATCHED THEN UPDATE</code> clause to the
     * <code>MERGE</code> statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    MergeNotMatchedValuesStepN<R> whenNotMatchedThenInsert(Collection<? extends Field<?>> fields);
}
