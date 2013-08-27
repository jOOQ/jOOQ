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
import static org.jooq.Clause.CONDITION_IS_NOT_NULL;
import static org.jooq.Clause.CONDITION_IS_NULL;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.Row;

/**
 * @author Lukas Eder
 */
class RowIsNull extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1806139685201770706L;
    private static final Clause[] CLAUSES_NULL     = { CONDITION, CONDITION_IS_NULL };
    private static final Clause[] CLAUSES_NOT_NULL = { CONDITION, CONDITION_IS_NOT_NULL };

    private final Row             row;
    private final boolean         isNull;

    RowIsNull(Row row, boolean isNull) {
        this.row = row;
        this.isNull = isNull;
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

        // CUBRID 9.0.0 and HSQLDB have buggy implementations of the NULL predicate.
        // Let's wait for them to be fixed
        if (asList(CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.dialect().family())) {
            List<Condition> conditions = new ArrayList<Condition>();

            for (Field<?> field : row.fields()) {
                conditions.add(isNull ? field.isNull() : field.isNotNull());
            }

            Condition result = new CombinedCondition(Operator.AND, conditions);
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
        private static final long serialVersionUID = -2977241780111574353L;

        @Override
        public final void toSQL(RenderContext context) {
            context.visit(row)
                   .sql(" ")
                   .keyword(isNull ? "is null" : "is not null");
        }

        @Override
        public final void bind(BindContext context) {
            context.visit(row);
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return isNull ? CLAUSES_NULL : CLAUSES_NOT_NULL;
        }
    }
}