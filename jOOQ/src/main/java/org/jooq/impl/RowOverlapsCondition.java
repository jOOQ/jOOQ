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
import static org.jooq.Clause.CONDITION_OVERLAPS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.Row2;

/**
 * @author Lukas Eder
 */
class RowOverlapsCondition<T1, T2> extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 85887551884667824L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_OVERLAPS };

    private final Row2<T1, T2>    left;
    private final Row2<T1, T2>    right;

    RowOverlapsCondition(Row2<T1, T2> left, Row2<T1, T2> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public final void toSQL(RenderContext context) {
        delegate(context.configuration()).toSQL(context);
    }

    @Override
    public final void bind(BindContext context) {
        delegate(context.configuration()).bind(context);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return delegate(ctx.configuration()).clauses(ctx);
    }

    private final QueryPartInternal delegate(Configuration configuration) {
        Field<T1> left1 = left.field1();
        Field<T2> left2 = left.field2();
        Field<T1> right1 = right.field1();
        Field<T2> right2 = right.field2();

        DataType<?> type0 = left1.getDataType();
        DataType<?> type1 = left2.getDataType();

        // The SQL standard only knows temporal OVERLAPS predicates:
        // (DATE, DATE)     OVERLAPS (DATE, DATE)
        // (DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)
        boolean standardOverlaps = type0.isDateTime() && type1.isTemporal();
        boolean intervalOverlaps = type0.isDateTime() && (type1.isInterval() || type1.isNumeric());

        // The non-standard OVERLAPS predicate is always simulated
        if (!standardOverlaps || asList(ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, INGRES, MARIADB, MYSQL, SQLSERVER, SQLITE, SYBASE).contains(configuration.dialect().family())) {

            // Interval OVERLAPS predicates need some additional arithmetic
            if (intervalOverlaps) {
                return (QueryPartInternal)
                       right1.le(left1.add(left2)).and(
                       left1.le(right1.add(right2)));
            }

            // All other OVERLAPS predicates can be simulated simply
            else {
                return (QueryPartInternal)
                       right1.le(left2.cast(right1)).and(
                       left1.le(right2.cast(left1)));
            }
        }

        // These dialects seem to have trouble with INTERVAL OVERLAPS predicates
        else if (intervalOverlaps && asList(HSQLDB).contains(configuration.dialect())) {
                return (QueryPartInternal)
                        right1.le(left1.add(left2)).and(
                        left1.le(right1.add(right2)));
        }

        // Everyone else can handle OVERLAPS (Postgres, Oracle)
        else {
            return new Native();
        }
    }

    private class Native extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -1552476981094856727L;

        @Override
        public final void toSQL(RenderContext context) {
            context.sql("(").visit(left)
                   .sql(" ").keyword("overlaps")
                   .sql(" ").visit(right)
                   .sql(")");
        }

        @Override
        public final void bind(BindContext context) {
            context.visit(left).visit(right);
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }
    }
}