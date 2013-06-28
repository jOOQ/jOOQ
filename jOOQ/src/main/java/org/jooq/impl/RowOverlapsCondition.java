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
import org.jooq.Configuration;
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
    private static final long  serialVersionUID = 85887551884667824L;

    private final Row2<T1, T2> left;
    private final Row2<T1, T2> right;

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
            context.sql("(")
                   .sql(left)
                   .keyword(" overlaps ")
                   .sql(right)
                   .sql(")");
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(left).bind(right);
        }
    }
}