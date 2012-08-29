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
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;

import java.util.Collection;

/**
 * A model class for a tuple with arity <code>6</code>
 *
 * @author Lukas Eder
 */
public interface Tuple6<T1, T2, T3, T4, T5, T6> extends QueryPart {

    /**
     * Compare this tuple with another tuple for equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition equal(Tuple6<T1, T2, T3, T4, T5, T6> tuple);

    /**
     * Compare this tuple with another tuple for equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this tuple with another tuple for equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this tuple with another tuple for equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition eq(Tuple6<T1, T2, T3, T4, T5, T6> tuple);

    /**
     * Compare this tuple with another tuple for equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this tuple with another tuple for equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this tuple with another tuple for non-equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notEqual(Tuple6<T1, T2, T3, T4, T5, T6> tuple);

    /**
     * Compare this tuple with another tuple for non-equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this tuple with another tuple for non-equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this tuple with another tuple for non-equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition ne(Tuple6<T1, T2, T3, T4, T5, T6> tuple);

    /**
     * Compare this tuple with another tuple for non-equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    /**
     * Compare this tuple with another tuple for non-equality
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6);

    /**
     * Compare this tuple with a set of tuples for equality
     */
    @Support({ CUBRID, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition in(Collection<? extends Tuple6<T1, T2, T3, T4, T5, T6>> tuples);

    /**
     * Compare this tuple with a set of tuples for equality
     */
    @Support({ CUBRID, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition in(Tuple6<T1, T2, T3, T4, T5, T6>... tuples);

    /**
     * Compare this tuple with a subselect for equality
     * <p>
     * Note that the subquery must return a table of the same arity as this
     * tuple. This is not checked by jOOQ and will result in syntax errors in
     * the database, if not used correctly.
     */
    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition in(Select<?> select);

    /**
     * Compare this tuple with a set of tuples for equality
     */
    @Support({ CUBRID, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notIn(Collection<? extends Tuple6<T1, T2, T3, T4, T5, T6>> tuples);

    /**
     * Compare this tuple with a set of tuples for equality
     */
    @Support({ CUBRID, H2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notIn(Tuple6<T1, T2, T3, T4, T5, T6>... tuples);

    /**
     * Compare this tuple with a subselect for equality
     * <p>
     * Note that the subquery must return a table of the same arity as this
     * tuple. This is not checked by jOOQ and will result in syntax errors in
     * the database, if not used correctly.
     */
    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notIn(Select<?> select);
}
