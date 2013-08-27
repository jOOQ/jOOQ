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
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.LIKE;
import static org.jooq.Comparator.LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_LIKE;
import static org.jooq.Comparator.NOT_LIKE_IGNORE_CASE;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
class CompareCondition extends AbstractCondition {

    private static final long     serialVersionUID = -747240442279619486L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Field<?>        field1;
    private final Field<?>        field2;
    private final Comparator      comparator;
    private final Character       escape;

    CompareCondition(Field<?> field1, Field<?> field2, Comparator comparator) {
        this(field1, field2, comparator, null);
    }

    CompareCondition(Field<?> field1, Field<?> field2, Comparator comparator, Character escape) {
        this.field1 = field1;
        this.field2 = field2;
        this.comparator = comparator;
        this.escape = escape;
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(field1).visit(field2);
    }

    @Override
    public final void toSQL(RenderContext context) {
        SQLDialect dialect = context.configuration().dialect();
        Field<?> lhs = field1;
        Field<?> rhs = field2;
        Comparator op = comparator;

        // [#1159] Some dialects cannot auto-convert the LHS operand to a
        // VARCHAR when applying a LIKE predicate
        // [#293] TODO: This could apply to other operators, too
        if ((op == LIKE || op == NOT_LIKE)
                && field1.getType() != String.class
                && asList(ASE, DERBY, POSTGRES).contains(dialect)) {

            lhs = lhs.cast(String.class);
        }

        // [#1423] Only Postgres knows a true ILIKE operator. Other dialects
        // need to simulate this as LOWER(lhs) LIKE LOWER(rhs)
        else if ((op == LIKE_IGNORE_CASE || op == NOT_LIKE_IGNORE_CASE)
                && POSTGRES != dialect) {

            lhs = lhs.lower();
            rhs = rhs.lower();
            op = (op == LIKE_IGNORE_CASE ? LIKE : NOT_LIKE);
        }

        context.visit(lhs)
               .sql(" ");

        // [#1131] Some weird DB2 issue stops "LIKE" from working with a
        // concatenated search expression, if the expression is more than 4000
        // characters long
        boolean castRhs = (dialect == DB2 && rhs instanceof Concat);

                     context.keyword(op.toSQL()).sql(" ");
        if (castRhs) context.keyword("cast").sql("(");
                     context.visit(rhs);
        if (castRhs) context.sql(" ").keyword("as").sql(" ").keyword("varchar").sql("(4000))");

        if (escape != null) {
            context.sql(" ").keyword("escape").sql(" '")
                   .sql(escape)
                   .sql("'");
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
