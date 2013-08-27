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
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_IN;
import static org.jooq.Clause.CONDITION_NOT_IN;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Operator;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.Row;

/**
 * @author Lukas Eder
 */
class RowInCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long                  serialVersionUID = -1806139685201770706L;
    private static final Clause[]              CLAUSES_IN       = { CONDITION, CONDITION_IN };
    private static final Clause[]              CLAUSES_IN_NOT   = { CONDITION, CONDITION_NOT_IN };

    private final Row                          left;
    private final QueryPartList<? extends Row> right;
    private final Comparator                   comparator;

    RowInCondition(Row left, QueryPartList<? extends Row> right, Comparator comparator) {
        this.left = left;
        this.right = right;
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

    private final QueryPartInternal delegate(Configuration configuration) {
        if (asList(ASE, DB2, DERBY, FIREBIRD, INGRES, SQLSERVER, SQLITE, SYBASE).contains(configuration.dialect().family())) {
            List<Condition> conditions = new ArrayList<Condition>();

            for (Row row : right) {
                conditions.add(new RowCondition(left, row, EQUALS));
            }

            Condition result = new CombinedCondition(Operator.OR, conditions);

            if (comparator == NOT_IN) {
                result = result.not();
            }

            return (QueryPartInternal) result;
        }
        else {
            return new Native();
        }
    }

    private class Native extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -7019193803316281371L;

        @Override
        public final void toSQL(RenderContext context) {
            context.visit(left)
                   .sql(" ")
                   .keyword(comparator.toSQL())
                   .sql(" (")
                   .visit(right)
                   .sql(")");
        }

        @Override
        public final void bind(BindContext context) {
            context.visit(left).visit(right);
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return comparator == IN ? CLAUSES_IN : CLAUSES_IN_NOT;
        }
    }
}