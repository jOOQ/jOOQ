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
 * A model type for a row value expression with degree <code>6</code>.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be emulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Row6<T1, T2, T3, T4, T5, T6> extends Row {

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
     * @see #equal(Row6)
     * @see #notEqual(Row6)
     * @see #lessThan(Row6)
     * @see #lessOrEqual(Row6)
     * @see #greaterThan(Row6)
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition compare(Comparator comparator, Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Record6)
     * @see #notEqual(Record6)
     * @see #lessThan(Record6)
     * @see #lessOrEqual(Record6)
     * @see #greaterThan(Record6)
     * @see #greaterOrEqual(Record6)
     */
    @Support
    Condition compare(Comparator comparator, Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Row6)
     * @see #notEqual(Row6)
     * @see #lessThan(Row6)
     * @see #lessOrEqual(Row6)
     * @see #greaterThan(Row6)
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition compare(Comparator comparator, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all
     * {@link Comparator} types are supported
     *
     * @see #equal(Row6)
     * @see #notEqual(Row6)
     * @see #lessThan(Row6)
     * @see #lessOrEqual(Row6)
     * @see #greaterThan(Row6)
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition compare(Comparator comparator, Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

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
    Condition compare(Comparator comparator, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition compare(Comparator comparator, QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition equal(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition equal(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition equal(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition equal(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition eq(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition eq(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row6)
     */
    @Support
    Condition eq(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition eq(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition notEqual(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition notEqual(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition notEqual(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition notEqual(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition ne(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition ne(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row6)
     */
    @Support
    Condition ne(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition ne(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition lessThan(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lessThan(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lessThan(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition lessThan(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lt(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lt(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row6)
     */
    @Support
    Condition lt(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition lt(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition lessOrEqual(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition lessOrEqual(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition lessOrEqual(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition lessOrEqual(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition le(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition le(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row6)
     */
    @Support
    Condition le(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition le(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition greaterThan(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition greaterThan(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition greaterThan(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition greaterThan(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition gt(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition gt(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row6)
     */
    @Support
    Condition gt(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition gt(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition greaterOrEqual(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition greaterOrEqual(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition greaterOrEqual(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition ge(Row6<T1, T2, T3, T4, T5, T6> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition ge(Record6<T1, T2, T3, T4, T5, T6> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row6)
     */
    @Support
    Condition ge(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition ge(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> between(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4, T5 minValue5, T6 minValue6);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> between(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4, Field<T5> minValue5, Field<T6> minValue6);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> between(Row6<T1, T2, T3, T4, T5, T6> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> between(Record6<T1, T2, T3, T4, T5, T6> minValue);

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
    Condition between(Row6<T1, T2, T3, T4, T5, T6> minValue,
                      Row6<T1, T2, T3, T4, T5, T6> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(Row6, Row6)
     */
    @Support
    Condition between(Record6<T1, T2, T3, T4, T5, T6> minValue,
                      Record6<T1, T2, T3, T4, T5, T6> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> betweenSymmetric(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4, T5 minValue5, T6 minValue6);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> betweenSymmetric(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4, Field<T5> minValue5, Field<T6> minValue6);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> betweenSymmetric(Row6<T1, T2, T3, T4, T5, T6> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> betweenSymmetric(Record6<T1, T2, T3, T4, T5, T6> minValue);

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
    Condition betweenSymmetric(Row6<T1, T2, T3, T4, T5, T6> minValue,
                               Row6<T1, T2, T3, T4, T5, T6> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(Row6, Row6)
     */
    @Support
    Condition betweenSymmetric(Record6<T1, T2, T3, T4, T5, T6> minValue,
                               Record6<T1, T2, T3, T4, T5, T6> maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetween(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4, T5 minValue5, T6 minValue6);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetween(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4, Field<T5> minValue5, Field<T6> minValue6);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetween(Row6<T1, T2, T3, T4, T5, T6> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetween(Record6<T1, T2, T3, T4, T5, T6> minValue);

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
    Condition notBetween(Row6<T1, T2, T3, T4, T5, T6> minValue,
                         Row6<T1, T2, T3, T4, T5, T6> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(Row6, Row6)
     */
    @Support
    Condition notBetween(Record6<T1, T2, T3, T4, T5, T6> minValue,
                         Record6<T1, T2, T3, T4, T5, T6> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetweenSymmetric(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4, T5 minValue5, T6 minValue6);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetweenSymmetric(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4, Field<T5> minValue5, Field<T6> minValue6);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetweenSymmetric(Row6<T1, T2, T3, T4, T5, T6> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(Row6, Row6)
     */
    @Support
    BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetweenSymmetric(Record6<T1, T2, T3, T4, T5, T6> minValue);

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
    Condition notBetweenSymmetric(Row6<T1, T2, T3, T4, T5, T6> minValue,
                                  Row6<T1, T2, T3, T4, T5, T6> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(Row6, Row6)
     */
    @Support
    Condition notBetweenSymmetric(Record6<T1, T2, T3, T4, T5, T6> minValue,
                                  Record6<T1, T2, T3, T4, T5, T6> maxValue);

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
    Condition in(Collection<? extends Row6<T1, T2, T3, T4, T5, T6>> rows);

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
    Condition in(Result<? extends Record6<T1, T2, T3, T4, T5, T6>> result);

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
    Condition in(Row6<T1, T2, T3, T4, T5, T6>... rows);

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
    Condition in(Record6<T1, T2, T3, T4, T5, T6>... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

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
    Condition notIn(Collection<? extends Row6<T1, T2, T3, T4, T5, T6>> rows);

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
    Condition notIn(Result<? extends Record6<T1, T2, T3, T4, T5, T6>> result);

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
    Condition notIn(Row6<T1, T2, T3, T4, T5, T6>... rows);

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
    Condition notIn(Record6<T1, T2, T3, T4, T5, T6>... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

}
