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

/**
 * A model type for a tuple.
 * <p>
 * Note: Not all databases support tuples, but many tuple operations can be
 * simulated on all databases. See relevant tuple method Javadocs for details.
 *
 * @author Lukas Eder
 */
public interface Tuple extends QueryPart {

    /**
     * Get the degree of this tuple
     */
    int getDegree();

    /**
     * Get a field at a given index
     */
    Field<?> getField(int index);

//    /**
//     * Compare this tuple with a subselect for equality
//     * <p>
//     * Note that the subquery must return a table of the same degree as this
//     * tuple. This is not checked by jOOQ and will result in syntax errors in
//     * the database, if not used correctly.
//     */
//    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
//    Condition equal(Select<?> select);
//
//    /**
//     * Compare this tuple with a subselect for equality
//     * <p>
//     * Note that the subquery must return a table of the same degree as this
//     * tuple. This is not checked by jOOQ and will result in syntax errors in
//     * the database, if not used correctly.
//     */
//    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
//    Condition eq(Select<?> select);
//
//    /**
//     * Compare this tuple with a subselect for equality
//     * <p>
//     * Note that the subquery must return a table of the same degree as this
//     * tuple. This is not checked by jOOQ and will result in syntax errors in
//     * the database, if not used correctly.
//     */
//    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
//    Condition notEqual(Select<?> select);
//
//    /**
//     * Compare this tuple with a subselect for equality
//     * <p>
//     * Note that the subquery must return a table of the same degree as this
//     * tuple. This is not checked by jOOQ and will result in syntax errors in
//     * the database, if not used correctly.
//     */
//    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
//    Condition ne(Select<?> select);

    /**
     * Compare this tuple with a subselect for equality
     * <p>
     * Note that the subquery must return a table of the same degree as this
     * tuple. This is not checked by jOOQ and will result in syntax errors in
     * the database, if not used correctly.
     */
    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition in(Select<?> select);

    /**
     * Compare this tuple with a subselect for non-equality
     * <p>
     * Note that the subquery must return a table of the same degree as this
     * tuple. This is not checked by jOOQ and will result in syntax errors in
     * the database, if not used correctly.
     */
    @Support({ CUBRID, DB2, HSQLDB, MYSQL, ORACLE, POSTGRES })
    Condition notIn(Select<?> select);

}
