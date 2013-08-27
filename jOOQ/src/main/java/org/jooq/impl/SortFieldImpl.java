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

import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SortField;
import org.jooq.SortOrder;

class SortFieldImpl<T> extends AbstractQueryPart implements SortField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1223739398544155873L;

    private final Field<T>    field;
    private final SortOrder   order;
    private boolean           nullsFirst;
    private boolean           nullsLast;

    SortFieldImpl(Field<T> field, SortOrder order) {
        this.field = field;
        this.order = order;
    }

    @Override
    public final String getName() {
        return field.getName();
    }

    @Override
    public final SortOrder getOrder() {
        return order;
    }

    @Override
    public final SortField<T> nullsFirst() {
        nullsFirst = true;
        nullsLast = false;
        return this;
    }

    @Override
    public final SortField<T> nullsLast() {
        nullsFirst = false;
        nullsLast = true;
        return this;
    }

    @Override
    public final void toSQL(RenderContext context) {
        if (nullsFirst || nullsLast) {
            switch (context.configuration().dialect().family()) {

                // DB2 supports NULLS FIRST/LAST only in OLAP (window) functions
                case DB2:

                // These dialects don't support this syntax at all
                case ASE:
                case CUBRID:
                case INGRES:
                case MARIADB:
                case MYSQL:
                case SQLITE:
                case SQLSERVER:
                case SYBASE: {
                    Field<Integer> ifNull = nullsFirst ? zero() : one();
                    Field<Integer> ifNotNull = nullsFirst ? one() : zero();

                    context.visit(nvl2(field, ifNotNull, ifNull))
                           .sql(", ")
                           .visit(field)
                           .sql(" ")
                           .keyword(order.toSQL());

                    break;
                }

                // DERBY, H2, HSQLDB, ORACLE, POSTGRES
                default: {
                    context.visit(field)
                           .sql(" ")
                           .keyword(order.toSQL());

                    if (nullsFirst) {
                        context.sql(" ").keyword("nulls first");
                    }
                    else {
                        context.sql(" ").keyword("nulls last");
                    }

                    break;
                }
            }
        }
        else {
            context.visit(field)
                   .sql(" ")
                   .keyword(order.toSQL());
        }
    }

    @Override
    public final void bind(BindContext context) {

        // [#1667] Some dialects simulate NULLS { FIRST | LAST } clauses. They
        // will need to bind the sort field twice
        if (nullsFirst || nullsLast) {
            switch (context.configuration().dialect().family()) {
                case DB2:
                case ASE:
                case CUBRID:
                case INGRES:
                case MARIADB:
                case MYSQL:
                case SQLITE:
                case SQLSERVER:
                case SYBASE: {
                    context.visit(field);
                }
            }
        }

        context.visit(field);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
