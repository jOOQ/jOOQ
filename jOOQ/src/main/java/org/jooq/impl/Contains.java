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
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.val;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.exception.DataAccessException;

/**
 * Abstraction for various "contains" operations
 *
 * @author Lukas Eder
 */
class Contains<T> extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 6146303086487338550L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Field<T>        lhs;
    private final Field<T>        rhs;
    private final T               value;

    Contains(Field<T> field, T value) {
        this.lhs = field;
        this.rhs = null;
        this.value = value;
    }

    Contains(Field<T> field, Field<T> rhs) {
        this.lhs = field;
        this.rhs = rhs;
        this.value = null;
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.visit(condition());
    }

    @Override
    public final void bind(BindContext context) throws DataAccessException {
        context.visit(condition());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    private final Condition condition() {

        // [#1107] Some dialects support "contains" operations for ARRAYs
        if (lhs.getDataType().isArray()) {
            return new PostgresArrayContains();
        }

        // "contains" operations on Strings
        else {
            Field<String> concat;

            if (rhs == null) {
                concat = DSL.concat(inline("%"), Utils.escapeForLike(value), inline("%"));
            }
            else {
                concat = DSL.concat(inline("%"), Utils.escapeForLike(rhs), inline("%"));
            }

            return lhs.like(concat, Utils.ESCAPE);
        }
    }

    /**
     * The Postgres array contains operator
     */
    private class PostgresArrayContains extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8083622843635168388L;

        @Override
        public final void toSQL(RenderContext context) {
            context.visit(lhs).sql(" @> ").visit(rhs());
        }

        @Override
        public final void bind(BindContext context) throws DataAccessException {
            context.visit(lhs).visit(rhs());
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }

        private final Field<T> rhs() {
            return (rhs == null) ? val(value, lhs) : rhs;
        }
    }
}
