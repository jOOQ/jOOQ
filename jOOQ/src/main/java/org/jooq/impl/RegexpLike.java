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

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class RegexpLike extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 3162855665213654276L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Field<?>        search;
    private final Field<String>   pattern;

    RegexpLike(Field<?> search, Field<String> pattern) {
        this.search = search;
        this.pattern = pattern;
    }

    @Override
    public final void toSQL(RenderContext context) {
        switch (context.configuration().dialect().family()) {

            // [#620] These databases are compatible with the MySQL syntax
            case CUBRID:
            case H2:
            case MARIADB:
            case MYSQL:
            case SQLITE:
            case SYBASE: {
                context.visit(search)
                       .sql(" ")
                       .keyword("regexp")
                       .sql(" ")
                       .visit(pattern);

                break;
            }

            // [#620] HSQLDB has its own syntax
            case HSQLDB: {

                // [#1570] TODO: Replace this by SQL.condition(String, QueryPart...)
                context.visit(DSL.condition("{regexp_matches}({0}, {1})", search, pattern));
                break;
            }

            // [#620] Postgres has its own syntax
            case POSTGRES: {

                // [#1570] TODO: Replace this by SQL.condition(String, QueryPart...)
                context.visit(DSL.condition("{0} ~ {1}", search, pattern));
                break;
            }

            // [#620] Oracle has its own syntax
            case ORACLE: {

                // [#1570] TODO: Replace this by SQL.condition(String, QueryPart...)
                context.visit(DSL.condition("{regexp_like}({0}, {1})", search, pattern));
                break;
            }

            // Render the SQL standard for those databases that do not support
            // regular expressions
            case ASE:
            case DB2:
            case DERBY:
            case FIREBIRD:
            case INGRES:
            case SQLSERVER:
            default: {
                context.visit(search)
                       .sql(" ")
                       .keyword("like_regex")
                       .sql(" ")
                       .visit(pattern);

                break;
            }
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(search).visit(pattern);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
