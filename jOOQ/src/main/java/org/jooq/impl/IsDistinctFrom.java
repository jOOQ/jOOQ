/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;

import org.jooq.BindContext;
import org.jooq.Comparator;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
class IsDistinctFrom<T> extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = 4568269684824736461L;

    private final Field<T>              lhs;
    private final Field<T>              rhs;
    private final Comparator            comparator;

    private transient QueryPartInternal mySQLCondition;
    private transient QueryPartInternal compareCondition;
    private transient QueryPartInternal caseExpression;

    IsDistinctFrom(Field<T> lhs, Field<T> rhs, Comparator comparator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.comparator = comparator;
    }

    @Override
    public final void toSQL(RenderContext context) {
        delegate(context.configuration()).toSQL(context);
    }

    @Override
    public final void bind(BindContext context) {
        delegate(context.configuration()).bind(context);
    }

    /**
     * Get a delegate <code>CompareCondition</code>, in case the context
     * {@link SQLDialect} natively supports the <code>IS DISTINCT FROM</code>
     * clause.
     */
    private final QueryPartInternal delegate(Configuration configuration) {

        // These dialects need to simulate the IS DISTINCT FROM operator
        if (asList(ASE, CUBRID, DB2, DERBY, INGRES, ORACLE, SQLSERVER, SQLITE, SYBASE).contains(configuration.dialect().family())) {
            if (caseExpression == null) {
                if (comparator == Comparator.IS_DISTINCT_FROM) {
                    caseExpression = (QueryPartInternal) decode()
                        .when(lhs.isNull().and(rhs.isNull()), zero())
                        .when(lhs.isNull().and(rhs.isNotNull()), one())
                        .when(lhs.isNotNull().and(rhs.isNull()), one())
                        .when(lhs.equal(rhs), zero())
                        .otherwise(one())
                        .equal(one());
                }
                else {
                    caseExpression = (QueryPartInternal) decode()
                        .when(lhs.isNull().and(rhs.isNull()), one())
                        .when(lhs.isNull().and(rhs.isNotNull()), zero())
                        .when(lhs.isNotNull().and(rhs.isNull()), zero())
                        .when(lhs.equal(rhs), one())
                        .otherwise(zero())
                        .equal(one());
                }
            }

            return caseExpression;
        }

        // MySQL knows the <=> operator
        else if (asList(MARIADB, MYSQL).contains(configuration.dialect())) {
            if (mySQLCondition == null) {
                if (comparator == Comparator.IS_DISTINCT_FROM) {
                    mySQLCondition = (QueryPartInternal) condition("not({0} <=> {1})", lhs, rhs);
                }
                else {
                    mySQLCondition = (QueryPartInternal) condition("{0} <=> {1}", lhs, rhs);
                }
            }

            return mySQLCondition;
        }

        // These dialects natively support the IS DISTINCT FROM operator:
        // H2, Postgres
        else {
            if (compareCondition == null) {
                compareCondition = new CompareCondition(lhs, rhs, comparator);
            }

            return compareCondition;
        }
    }
}
