/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;

import java.util.Collection;

import javax.annotation.Generated;

/**
 * A model type for a row value expression with degree <code>1</code>
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Row1<T1> extends Row {

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field
     */
    Field<T1> field1();

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * equality
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition equal(Row1<T1> row);

    /**
     * Compare this row value expression with a record for equality
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for equality
     *
     * @see #equal(Row1)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition equal(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Row1<T1> row);

    /**
     * Compare this row value expression with a record for equality
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for equality
     *
     * @see #equal(Row1)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition eq(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition notEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Row1<T1> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition ne(Select<? extends Record1<T1>> select);

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * order
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) < (1, 2, 3)</code> is equivalent to
     * <code>A < 1 OR (A = 1 AND B < 2) OR (A = 1 AND B = 2 AND C < 3)</code>
     */
    @Support
    Condition lessThan(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #lessThan(Row1)
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    Condition lessThan(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #lessThan(Row1)
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    Condition lt(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <= (1, 2)</code> is equivalent to
     * <code>A < 1 OR (A = 1 AND B < 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition lessOrEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    Condition lessOrEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #lessOrEqual(Row1)
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    Condition le(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) > (1, 2, 3)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2 AND C > 3)</code>
     */
    @Support
    Condition greaterThan(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #greaterThan(Row1)
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    Condition greaterThan(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #greaterThan(Row1)
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    Condition gt(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) >= (1, 2)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition greaterOrEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    Condition greaterOrEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    Condition ge(Select<? extends Record1<T1>> select);

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality
     * <p>
     * Row IN predicates can be simulated in those databases that do not support
     * such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code> is
     * equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>, which
     * is equivalent to <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
     */
    @Support
    Condition in(Collection<? extends Row1<T1>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Row1<T1>... rows);

    /**
     * Compare this row value expression with a set of records for equality
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Record1<T1>... record);

    /**
     * Compare this row value expression with a subselect for equality
     *
     * @see #in(Collection)
     */
    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition in(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality
     * <p>
     * Row NOT IN predicates can be simulated in those databases that do not
     * support such predicates natively:
     * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
     * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
     * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
     */
    @Support
    Condition notIn(Collection<? extends Row1<T1>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Row1<T1>... rows);

    /**
     * Compare this row value expression with a set of records for non-equality
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Record1<T1>... record);

    /**
     * Compare this row value expression with a subselect for non-equality
     *
     * @see #notIn(Collection)
     */
    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notIn(Select<? extends Record1<T1>> select);

}
