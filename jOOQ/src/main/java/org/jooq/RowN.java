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

/**
 * A model type for a row value expression with degree <code>N > 8</code>
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
public interface RowN extends Row {

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
    Condition equal(RowN row);

    /**
     * Compare this row value expression with a record for equality
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for equality
     *
     * @see #equal(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition equal(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(RowN row);

    /**
     * Compare this row value expression with a record for equality
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * equality
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for equality
     *
     * @see #equal(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition eq(Select<? extends Record> select);

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
    Condition notEqual(RowN row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(RowN row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition ne(Select<? extends Record> select);

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
    Condition lessThan(RowN row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #lessThan(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition lessThan(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(RowN row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #lessThan(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition lt(Select<? extends Record> select);

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
    Condition lessOrEqual(RowN row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition lessOrEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(RowN row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #lessOrEqual(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition le(Select<? extends Record> select);

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
    Condition greaterThan(RowN row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #greaterThan(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition greaterThan(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(RowN row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #greaterThan(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition gt(Select<? extends Record> select);

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
    Condition greaterOrEqual(RowN row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition greaterOrEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(RowN row);

    /**
     * Compare this row value expression with a record for order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Field<?>... fields);

    /**
     * Compare this row value expression with a subselect for order
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support({ CUBRID, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition ge(Select<? extends Record> select);

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
    Condition in(Collection<? extends RowN> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(RowN... rows);

    /**
     * Compare this row value expression with a set of records for equality
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Record... records);

    /**
     * Compare this row value expression with a subselect for equality
     *
     * @see #in(Collection)
     */
    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition in(Select<? extends Record> select);

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
    Condition notIn(Collection<? extends RowN> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(RowN... rows);

    /**
     * Compare this row value expression with a set of records for non-equality
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Record... records);

    /**
     * Compare this row value expression with a subselect for non-equality
     *
     * @see #notIn(Collection)
     */
    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notIn(Select<? extends Record> select);
}
