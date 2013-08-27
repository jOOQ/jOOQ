/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Configuration;
import org.jooq.Context;
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
    private transient QueryPartInternal sqliteCondition;
    private transient QueryPartInternal compareCondition;
    private transient QueryPartInternal caseExpression;

    IsDistinctFrom(Field<T> lhs, Field<T> rhs, Comparator comparator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.comparator = comparator;
    }

    @Override
    public final void toSQL(RenderContext ctx) {
        delegate(ctx.configuration()).toSQL(ctx);
    }

    @Override
    public final void bind(BindContext ctx) {
        delegate(ctx.configuration()).bind(ctx);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return delegate(ctx.configuration()).clauses(ctx);
    }

    /**
     * Get a delegate <code>CompareCondition</code>, in case the context
     * {@link SQLDialect} natively supports the <code>IS DISTINCT FROM</code>
     * clause.
     */
    private final QueryPartInternal delegate(Configuration configuration) {

        // These dialects need to simulate the IS DISTINCT FROM predicate
        if (asList(ASE, CUBRID, DB2, DERBY, INGRES, ORACLE, SQLSERVER, SYBASE).contains(configuration.dialect().family())) {
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
                    mySQLCondition = (QueryPartInternal) condition("{not}({0} <=> {1})", lhs, rhs);
                }
                else {
                    mySQLCondition = (QueryPartInternal) condition("{0} <=> {1}", lhs, rhs);
                }
            }

            return mySQLCondition;
        }

        // SQLite knows the IS / IS NOT predicate
        else if (SQLITE == configuration.dialect()) {
            if (sqliteCondition == null) {
                if (comparator == Comparator.IS_DISTINCT_FROM) {
                    sqliteCondition = (QueryPartInternal) condition("{0} {is not} {1}", lhs, rhs);
                }
                else {
                    sqliteCondition = (QueryPartInternal) condition("{0} {is} {1}", lhs, rhs);
                }
            }

            return sqliteCondition;
        }

        // These dialects natively support the IS DISTINCT FROM predicate:
        // H2, Postgres
        else {
            if (compareCondition == null) {
                compareCondition = new CompareCondition(lhs, rhs, comparator);
            }

            return compareCondition;
        }
    }
}
