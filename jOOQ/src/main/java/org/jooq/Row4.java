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

import static org.jooq.SQLDialect.*;

import java.util.Collection;
import java.util.function.Function;

import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;

/**
 * A row value expression.
 * <p>
 * Row value expressions are mainly useful for use in predicates, when comparing
 * several values in one go, which can be more elegant than expanding the row
 * value expression predicate in other equivalent syntaxes. This is especially
 * true for non-equality predicates. For instance, the following two predicates
 * are equivalent in SQL:
 * <p>
 * <code><pre>
 * (A, B) &gt; (X, Y)
 * (A &gt; X) OR (A = X AND B &gt; Y)
 * </pre></code>
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select()
 *    .from(CUSTOMER)
 *    .where(row(CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME).in(
 *        select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME).from(ACTOR)
 *    ))
 *    .fetch();
 * </pre></code>
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be emulated on all databases. See relevant row
 * value expression method Javadocs for details.
 * <p>
 * Instances can be created using {@link DSL#row(Object...)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Row4<T1, T2, T3, T4> extends Row, SelectField<Record4<T1, T2, T3, T4>> {

    // ------------------------------------------------------------------------
    // Mapping convenience methods
    // ------------------------------------------------------------------------

    /**
     * A convenience method to define a local {@link Record4} to custom type
     * {@link RecordMapper} that can be used when projecting {@link Row} types in
     * <code>SELECT</code> or <code>RETURNING</code> clauses.
     * <p>
     * EXPERIMENTAL. Unlike {@link #mapping(Class, Function4)}, this method
     * attempts to work without an explicit {@link Class} reference for the underlying
     * {@link Converter#toType()}. There may be some edge cases where this doesn't
     * work. Please report any bugs here:
     * <a href="https://github.com/jOOQ/jOOQ/issues/new/choose">https://github.com/jOOQ/jOOQ/issues/new/choose</a>
     * <p>
     * Known issues include:
     * <p>
     * <ul>
     * <li>When nesting rows in arrays, the class literal is required for reflective array creation.</li>
     * </ul>
     */
    @NotNull
    @Internal
    <U> SelectField<U> mapping(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends U> function);

    /**
     * A convenience method to define a local {@link Record4} to custom type
     * {@link RecordMapper} that can be used when projecting {@link Row} types in
     * <code>SELECT</code> or <code>RETURNING</code> clauses.
     */
    @NotNull
    <U> SelectField<U> mapping(Class<U> uType, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends U> function);

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    @NotNull
    Field<T1> field1();

    /**
     * Get the second field.
     */
    @NotNull
    Field<T2> field2();

    /**
     * Get the third field.
     */
    @NotNull
    Field<T3> field3();

    /**
     * Get the fourth field.
     */
    @NotNull
    Field<T4> field4();

    // ------------------------------------------------------------------------
    // Generic comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Row4)
     * @see #notEqual(Row4)
     * @see #lessThan(Row4)
     * @see #lessOrEqual(Row4)
     * @see #greaterThan(Row4)
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition compare(Comparator comparator, Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Record4)
     * @see #notEqual(Record4)
     * @see #lessThan(Record4)
     * @see #lessOrEqual(Record4)
     * @see #greaterThan(Record4)
     * @see #greaterOrEqual(Record4)
     */
    @NotNull
    @Support
    Condition compare(Comparator comparator, Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Row4)
     * @see #notEqual(Row4)
     * @see #lessThan(Row4)
     * @see #lessOrEqual(Row4)
     * @see #greaterThan(Row4)
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition compare(Comparator comparator, T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Row4)
     * @see #notEqual(Row4)
     * @see #lessThan(Row4)
     * @see #lessOrEqual(Row4)
     * @see #greaterThan(Row4)
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition compare(Comparator comparator, Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Select)
     * @see #notEqual(Select)
     * @see #lessThan(Select)
     * @see #lessOrEqual(Select)
     * @see #greaterThan(Select)
     * @see #greaterOrEqual(Select)
     */
    @NotNull
    @Support
    Condition compare(Comparator comparator, Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Select)
     * @see #notEqual(Select)
     * @see #lessThan(Select)
     * @see #lessOrEqual(Select)
     * @see #greaterThan(Select)
     * @see #greaterOrEqual(Select)
     */
    @NotNull
    @Support
    Condition compare(Comparator comparator, QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     * <p>
     * Row equality comparison predicates can be emulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @NotNull
    @Support
    Condition equal(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition equal(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition equal(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition equal(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition equal(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition eq(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition eq(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition eq(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row4)
     */
    @NotNull
    @Support
    Condition eq(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition eq(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     * <p>
     * Row non-equality comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) &lt;&gt; (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @NotNull
    @Support
    Condition notEqual(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition notEqual(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition notEqual(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition notEqual(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition ne(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition ne(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition ne(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row4)
     */
    @NotNull
    @Support
    Condition ne(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition ne(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isDistinctFrom(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isDistinctFrom(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isDistinctFrom(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isDistinctFrom(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isDistinctFrom(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isNotDistinctFrom(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isNotDistinctFrom(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isNotDistinctFrom(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isNotDistinctFrom(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with another row value expression for
     * distinctness.
     */
    @NotNull
    @Support
    Condition isNotDistinctFrom(Select<? extends Record4<T1, T2, T3, T4>> select);

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) &lt; (1, 2, 3)</code> is equivalent to
     * <code>A &lt; 1 OR (A = 1 AND B &lt; 2) OR (A = 1 AND B = 2 AND C &lt; 3)</code>
     */
    @NotNull
    @Support
    Condition lessThan(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lessThan(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lessThan(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition lessThan(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lt(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lt(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lt(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row4)
     */
    @NotNull
    @Support
    Condition lt(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition lt(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) &lt;= (1, 2)</code> is equivalent to
     * <code>A &lt; 1 OR (A = 1 AND B &lt; 2) OR (A = 1 AND B = 2)</code>
     */
    @NotNull
    @Support
    Condition lessOrEqual(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition lessOrEqual(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition lessOrEqual(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition lessOrEqual(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition le(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition le(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition le(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition le(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition le(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) &gt; (1, 2, 3)</code> is equivalent to
     * <code>A &gt; 1 OR (A = 1 AND B &gt; 2) OR (A = 1 AND B = 2 AND C &gt; 3)</code>
     */
    @NotNull
    @Support
    Condition greaterThan(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition greaterThan(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition greaterThan(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition greaterThan(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition gt(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition gt(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition gt(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row4)
     */
    @NotNull
    @Support
    Condition gt(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition gt(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) &gt;= (1, 2)</code> is equivalent to
     * <code>A &gt; 1 OR (A = 1 AND B &gt; 2) OR (A = 1 AND B = 2)</code>
     */
    @NotNull
    @Support
    Condition greaterOrEqual(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition greaterOrEqual(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition greaterOrEqual(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition greaterOrEqual(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition ge(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition ge(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition ge(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @NotNull
    @Support
    Condition ge(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @NotNull
    @Support
    Condition ge(QuantifiedSelect<? extends Record4<T1, T2, T3, T4>> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> between(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> between(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> between(Row4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> between(Record4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN B AND C</code> is equivalent to the
     * expression <code>A &gt;= B AND A &lt;= C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @NotNull
    @Support
    Condition between(Row4<T1, T2, T3, T4> minValue,
                      Row4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(Row4, Row4)
     */
    @NotNull
    @Support
    Condition between(Record4<T1, T2, T3, T4> minValue,
                      Record4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> betweenSymmetric(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> betweenSymmetric(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> betweenSymmetric(Row4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> betweenSymmetric(Record4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN SYMMETRIC B AND C</code> is equivalent to
     * the expression <code>(A &gt;= B AND A &lt;= C) OR (A &gt;= C AND A &lt;= B)</code>
     * for those SQL dialects that do not properly support the
     * <code>BETWEEN</code> predicate for row value expressions
     */
    @NotNull
    @Support
    Condition betweenSymmetric(Row4<T1, T2, T3, T4> minValue,
                               Row4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    Condition betweenSymmetric(Record4<T1, T2, T3, T4> minValue,
                               Record4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetween(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetween(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetween(Row4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetween(Record4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN B AND C</code> is equivalent to the
     * expression <code>A &lt; B OR A &gt; C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @NotNull
    @Support
    Condition notBetween(Row4<T1, T2, T3, T4> minValue,
                         Row4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(Row4, Row4)
     */
    @NotNull
    @Support
    Condition notBetween(Record4<T1, T2, T3, T4> minValue,
                         Record4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetweenSymmetric(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetweenSymmetric(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetweenSymmetric(Row4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetweenSymmetric(Record4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN SYMMETRIC B AND C</code> is equivalent
     * to the expression <code>(A &lt; B OR A &gt; C) AND (A &lt; C OR A &gt; B)</code> for
     * those SQL dialects that do not properly support the <code>BETWEEN</code>
     * predicate for row value expressions
     */
    @NotNull
    @Support
    Condition notBetweenSymmetric(Row4<T1, T2, T3, T4> minValue,
                                  Row4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @NotNull
    @Support
    Condition notBetweenSymmetric(Record4<T1, T2, T3, T4> minValue,
                                  Record4<T1, T2, T3, T4> maxValue);

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Row IN predicates can be emulated in those databases that do not support
     * such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code> is
     * equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>, which
     * is equivalent to <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>IN</code> predicates on temporary tables</li>
     * <li><code>IN</code> predicates on unnested array bind variables</li>
     * </ul>
     *
     * @see Rows#toRowList(Function, Function, Function, Function)
     */
    @NotNull
    @Support
    Condition in(Collection<? extends Row4<T1, T2, T3, T4>> rows);

    /**
     * Compare this row value expression with a set of records for
     * equality.
     * <p>
     * Row IN predicates can be emulated in those databases that do not support
     * such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code> is
     * equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>, which
     * is equivalent to <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>IN</code> predicates on temporary tables</li>
     * <li><code>IN</code> predicates on unnested array bind variables</li>
     * </ul>
     */
    @NotNull
    @Support
    Condition in(Result<? extends Record4<T1, T2, T3, T4>> result);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>IN</code> predicates on temporary tables</li>
     * <li><code>IN</code> predicates on unnested array bind variables</li>
     * </ul>
     *
     * @see #in(Collection)
     * @see Rows#toRowArray(Function, Function, Function, Function)
     */
    @SuppressWarnings("unchecked")
    @NotNull
    @Support
    Condition in(Row4<T1, T2, T3, T4>... rows);

    /**
     * Compare this row value expression with a set of records for equality.
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>IN</code> predicates on temporary tables</li>
     * <li><code>IN</code> predicates on unnested array bind variables</li>
     * </ul>
     *
     * @see #in(Collection)
     */
    @SuppressWarnings("unchecked")
    @NotNull
    @Support
    Condition in(Record4<T1, T2, T3, T4>... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @NotNull
    @Support
    Condition in(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Row NOT IN predicates can be emulated in those databases that do not
     * support such predicates natively:
     * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
     * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
     * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>NOT IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>NOT IN</code> predicates on temporary tables</li>
     * <li><code>NOT IN</code> predicates on unnested array bind variables</li>
     * </ul>
     *
     * @see Rows#toRowList(Function, Function, Function, Function)
     */
    @NotNull
    @Support
    Condition notIn(Collection<? extends Row4<T1, T2, T3, T4>> rows);

    /**
     * Compare this row value expression with a set of records for
     * equality.
     * <p>
     * Row NOT IN predicates can be emulated in those databases that do not
     * support such predicates natively:
     * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
     * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
     * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>NOT IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>NOT IN</code> predicates on temporary tables</li>
     * <li><code>NOT IN</code> predicates on unnested array bind variables</li>
     * </ul>
     */
    @NotNull
    @Support
    Condition notIn(Result<? extends Record4<T1, T2, T3, T4>> result);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>NOT IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>NOT IN</code> predicates on temporary tables</li>
     * <li><code>NOT IN</code> predicates on unnested array bind variables</li>
     * </ul>
     *
     * @see #notIn(Collection)
     * @see Rows#toRowArray(Function, Function, Function, Function)
     */
    @SuppressWarnings("unchecked")
    @NotNull
    @Support
    Condition notIn(Row4<T1, T2, T3, T4>... rows);

    /**
     * Compare this row value expression with a set of records for non-equality.
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>NOT IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>NOT IN</code> predicates on temporary tables</li>
     * <li><code>NOT IN</code> predicates on unnested array bind variables</li>
     * </ul>
     *
     * @see #notIn(Collection)
     */
    @SuppressWarnings("unchecked")
    @NotNull
    @Support
    Condition notIn(Record4<T1, T2, T3, T4>... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @NotNull
    @Support
    Condition notIn(Select<? extends Record4<T1, T2, T3, T4>> select);











































}
