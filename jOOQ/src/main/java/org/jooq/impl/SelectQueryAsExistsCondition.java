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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_EXISTS;
import static org.jooq.Clause.CONDITION_NOT_EXISTS;
import static org.jooq.impl.ExistsOperator.EXISTS;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.RenderContext;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
class SelectQueryAsExistsCondition extends AbstractCondition {

    private static final long     serialVersionUID   = 5678338161136603292L;
    private static final Clause[] CLAUSES_EXISTS     = { CONDITION, CONDITION_EXISTS };
    private static final Clause[] CLAUSES_EXISTS_NOT = { CONDITION, CONDITION_NOT_EXISTS };

    private final Select<?>       query;
    private final ExistsOperator  operator;

    SelectQueryAsExistsCondition(Select<?> query, ExistsOperator operator) {
        this.query = query;
        this.operator = operator;
    }

    @Override
    public final void toSQL(RenderContext context) {

        // If this is already a subquery, proceed
        if (context.subquery()) {
            context.keyword(operator.toSQL())
                   .sql(" (")
                   .formatIndentStart()
                   .formatNewLine()
                   .visit(query)
                   .formatIndentEnd()
                   .formatNewLine()
                   .sql(")");
        }
        else {
            context.keyword(operator.toSQL())
                   .sql(" (")
                   .subquery(true)
                   .formatIndentStart()
                   .formatNewLine()
                   .visit(query)
                   .formatIndentEnd()
                   .formatNewLine()
                   .subquery(false)
                   .sql(")");
        }
    }

    @Override
    public final void bind(BindContext context) {

        // If this is already a subquery, proceed
        if (context.subquery()) {
            context.visit(query);
        }
        else {
            context.subquery(true)
                   .visit(query)
                   .subquery(false);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return operator == EXISTS ? CLAUSES_EXISTS : CLAUSES_EXISTS_NOT;
    }
}
