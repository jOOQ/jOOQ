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

// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...

import org.jooq.Comparator;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import java.util.Collection;

import javax.annotation.Generated;

/**
 * A model type for a row value expression with degree <code>13</code>.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be emulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> extends Row {

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    Field<T1> field1();

    /**
     * Get the second field.
     */
    Field<T2> field2();

    /**
     * Get the third field.
     */
    Field<T3> field3();

    /**
     * Get the fourth field.
     */
    Field<T4> field4();

    /**
     * Get the fifth field.
     */
    Field<T5> field5();

    /**
     * Get the sixth field.
     */
    Field<T6> field6();

    /**
     * Get the seventh field.
     */
    Field<T7> field7();

    /**
     * Get the eighth field.
     */
    Field<T8> field8();

    /**
     * Get the ninth field.
     */
    Field<T9> field9();

    /**
     * Get the tenth field.
     */
    Field<T10> field10();

    /**
     * Get the eleventh field.
     */
    Field<T11> field11();

    /**
     * Get the twelfth field.
     */
    Field<T12> field12();

    /**
     * Get the thirteenth field.
     */
    Field<T13> field13();

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
     * @see #equal(Row13)
     * @see #notEqual(Row13)
     * @see #lessThan(Row13)
     * @see #lessOrEqual(Row13)
     * @see #greaterThan(Row13)
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition compare(Comparator comparator, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Record13)
     * @see #notEqual(Record13)
     * @see #lessThan(Record13)
     * @see #lessOrEqual(Record13)
     * @see #greaterThan(Record13)
     * @see #greaterOrEqual(Record13)
     */
    @Support
    Condition compare(Comparator comparator, Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Row13)
     * @see #notEqual(Row13)
     * @see #lessThan(Row13)
     * @see #lessOrEqual(Row13)
     * @see #greaterThan(Row13)
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition compare(Comparator comparator, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Row13)
     * @see #notEqual(Row13)
     * @see #lessThan(Row13)
     * @see #lessOrEqual(Row13)
     * @see #greaterThan(Row13)
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition compare(Comparator comparator, Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

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
    @Support
    Condition compare(Comparator comparator, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support
    Condition compare(Comparator comparator, QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support
    Condition equal(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition equal(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition equal(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition equal(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition eq(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition eq(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row13)
     */
    @Support
    Condition eq(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition eq(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     * <p>
     * Row non-equality comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) &lt;> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition notEqual(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition notEqual(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition notEqual(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition notEqual(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition ne(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition ne(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row13)
     */
    @Support
    Condition ne(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition ne(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support
    Condition lessThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lessThan(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lessThan(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition lessThan(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lt(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lt(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row13)
     */
    @Support
    Condition lt(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition lt(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) &lt;= (1, 2)</code> is equivalent to
     * <code>A &lt; 1 OR (A = 1 AND B &lt; 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition lessOrEqual(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition lessOrEqual(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition lessOrEqual(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition lessOrEqual(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition le(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition le(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row13)
     */
    @Support
    Condition le(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition le(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) > (1, 2, 3)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2 AND C > 3)</code>
     */
    @Support
    Condition greaterThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition greaterThan(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition greaterThan(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition greaterThan(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition gt(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition gt(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row13)
     */
    @Support
    Condition gt(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition gt(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be emulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) >= (1, 2)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition greaterOrEqual(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition greaterOrEqual(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition greaterOrEqual(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition ge(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition ge(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row13)
     */
    @Support
    Condition ge(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Condition ge(QuantifiedSelect<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> between(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4, T5 minValue5, T6 minValue6, T7 minValue7, T8 minValue8, T9 minValue9, T10 minValue10, T11 minValue11, T12 minValue12, T13 minValue13);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> between(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4, Field<T5> minValue5, Field<T6> minValue6, Field<T7> minValue7, Field<T8> minValue8, Field<T9> minValue9, Field<T10> minValue10, Field<T11> minValue11, Field<T12> minValue12, Field<T13> minValue13);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> between(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> between(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN B AND C</code> is equivalent to the
     * expression <code>A >= B AND A &lt;= C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @Support
    Condition between(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue,
                      Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(Row13, Row13)
     */
    @Support
    Condition between(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue,
                      Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> betweenSymmetric(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4, T5 minValue5, T6 minValue6, T7 minValue7, T8 minValue8, T9 minValue9, T10 minValue10, T11 minValue11, T12 minValue12, T13 minValue13);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> betweenSymmetric(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4, Field<T5> minValue5, Field<T6> minValue6, Field<T7> minValue7, Field<T8> minValue8, Field<T9> minValue9, Field<T10> minValue10, Field<T11> minValue11, Field<T12> minValue12, Field<T13> minValue13);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> betweenSymmetric(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> betweenSymmetric(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN SYMMETRIC B AND C</code> is equivalent to
     * the expression <code>(A >= B AND A &lt;= C) OR (A >= C AND A &lt;= B)</code>
     * for those SQL dialects that do not properly support the
     * <code>BETWEEN</code> predicate for row value expressions
     */
    @Support
    Condition betweenSymmetric(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue,
                               Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(Row13, Row13)
     */
    @Support
    Condition betweenSymmetric(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue,
                               Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> notBetween(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4, T5 minValue5, T6 minValue6, T7 minValue7, T8 minValue8, T9 minValue9, T10 minValue10, T11 minValue11, T12 minValue12, T13 minValue13);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> notBetween(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4, Field<T5> minValue5, Field<T6> minValue6, Field<T7> minValue7, Field<T8> minValue8, Field<T9> minValue9, Field<T10> minValue10, Field<T11> minValue11, Field<T12> minValue12, Field<T13> minValue13);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> notBetween(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> notBetween(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN B AND C</code> is equivalent to the
     * expression <code>A &lt; B OR A > C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @Support
    Condition notBetween(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue,
                         Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(Row13, Row13)
     */
    @Support
    Condition notBetween(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue,
                         Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> notBetweenSymmetric(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4, T5 minValue5, T6 minValue6, T7 minValue7, T8 minValue8, T9 minValue9, T10 minValue10, T11 minValue11, T12 minValue12, T13 minValue13);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> notBetweenSymmetric(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4, Field<T5> minValue5, Field<T6> minValue6, Field<T7> minValue7, Field<T8> minValue8, Field<T9> minValue9, Field<T10> minValue10, Field<T11> minValue11, Field<T12> minValue12, Field<T13> minValue13);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> notBetweenSymmetric(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(Row13, Row13)
     */
    @Support
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> notBetweenSymmetric(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN SYMMETRIC B AND C</code> is equivalent
     * to the expression <code>(A &lt; B OR A > C) AND (A &lt; C OR A > B)</code> for
     * those SQL dialects that do not properly support the <code>BETWEEN</code>
     * predicate for row value expressions
     */
    @Support
    Condition notBetweenSymmetric(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue,
                                  Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(Row13, Row13)
     */
    @Support
    Condition notBetweenSymmetric(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> minValue,
                                  Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> maxValue);

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
     */
    @Support
    Condition in(Collection<? extends Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> rows);

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
    @Support
    Condition in(Result<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> result);

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
     */
    @Support
    Condition in(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... rows);

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
    @Support
    Condition in(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

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
     */
    @Support
    Condition notIn(Collection<? extends Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> rows);

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
    @Support
    Condition notIn(Result<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> result);

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
     */
    @Support
    Condition notIn(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... rows);

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
    @Support
    Condition notIn(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

}
