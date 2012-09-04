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

import java.util.Collection;

/**
 * A model type for a row value expression with degree <code>5</code>
 * <p>
 * Note: Not all databases support row value expressions, but many row value expression operations can be
 * simulated on all databases. See relevant row value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
public interface Row5<T1, T2, T3, T4, T5> extends Row {

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field
     */
    Field<T1> field1();

    /**
     * Get the second field
     */
    Field<T2> field2();

    /**
     * Get the third field
     */
    Field<T3> field3();

    /**
     * Get the fourth field
     */
    Field<T4> field4();

    /**
     * Get the fifth field
     */
    Field<T5> field5();

    // ------------------------------------------------------------------------
    // Row DSL API
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for equality
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition equal(Row5<T1, T2, T3, T4, T5> row);

    /**
     * Compare this row value expression with another row value expression for equality
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

    /**
     * Compare this row value expression with another row value expression for equality
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5);

    /**
     * Compare this row value expression with another row value expression for equality
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition eq(Row5<T1, T2, T3, T4, T5> row);

    /**
     * Compare this row value expression with another row value expression for equality
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

    /**
     * Compare this row value expression with another row value expression for equality
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5);

    /**
     * Compare this row value expression with another row value expression for non-equality
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition notEqual(Row5<T1, T2, T3, T4, T5> row);

    /**
     * Compare this row value expression with another row value expression for non-equality
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

    /**
     * Compare this row value expression with another row value expression for non-equality
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5);

    /**
     * Compare this row value expression with another row value expression for non-equality
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition ne(Row5<T1, T2, T3, T4, T5> row);

    /**
     * Compare this row value expression with another row value expression for non-equality
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

    /**
     * Compare this row value expression with another row value expression for non-equality
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5);

    /**
     * Compare this row value expression with a set of row value expressions for equality
     * <p>
     * Row IN predicates can be simulated in those databases that do not
     * support such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code>
     * is equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>,
     * which is equivalent to
     * <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
     */
    @Support
    Condition in(Collection<? extends Row5<T1, T2, T3, T4, T5>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for equality
     * <p>
     * Row IN predicates can be simulated in those databases that do not
     * support such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code>
     * is equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>,
     * which is equivalent to
     * <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
     */
    @Support
    Condition in(Row5<T1, T2, T3, T4, T5>... rows);

    /**
     * Compare this row value expression with a set of row value expressions for equality
     * <p>
     * Row NOT IN predicates can be simulated in those databases that do not
     * support such predicates natively:
     * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
     * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
     * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
     */
    @Support
    Condition notIn(Collection<? extends Row5<T1, T2, T3, T4, T5>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for equality
     * <p>
     * Row NOT IN predicates can be simulated in those databases that do not
     * support such predicates natively:
     * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
     * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
     * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
     */
    @Support
    Condition notIn(Row5<T1, T2, T3, T4, T5>... rows);
}
