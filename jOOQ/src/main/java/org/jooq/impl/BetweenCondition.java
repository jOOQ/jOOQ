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
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DSL.val;

import org.jooq.BetweenAndStep;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class BetweenCondition<T> extends AbstractCondition implements BetweenAndStep<T> {

    private static final long serialVersionUID = -4666251100802237878L;

    private final boolean     symmetric;
    private final boolean     not;
    private final Field<T>    field;
    private final Field<T>    minValue;
    private Field<T>          maxValue;

    BetweenCondition(Field<T> field, Field<T> minValue, boolean not, boolean symmetric) {
        this.field = field;
        this.minValue = minValue;
        this.not = not;
        this.symmetric = symmetric;
    }

    @Override
    public final Condition and(T value) {
        return and(val(value));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final Condition and(Field f) {
        if (maxValue == null) {
            this.maxValue = f;
            return this;
        }
        else {
            return super.and(f);
        }
    }

    @Override
    public final void bind(BindContext ctx) {
        delegate(ctx.configuration()).bind(ctx);
    }

    @Override
    public final void toSQL(RenderContext ctx) {
        delegate(ctx.configuration()).toSQL(ctx);
    }

    private final QueryPartInternal delegate(Configuration configuration) {
        if (symmetric && asList(ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, MARIADB, MYSQL, ORACLE, SQLSERVER, SQLITE, SYBASE).contains(configuration.dialect().family())) {
            if (not) {
                return (QueryPartInternal) field.notBetween(minValue, maxValue).and(field.notBetween(maxValue, minValue));
            }
            else {
                return (QueryPartInternal) field.between(minValue, maxValue).or(field.between(maxValue, minValue));
            }
        }
        else {
            return new Native();
        }
    }

    private class Native extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2915703568738921575L;

        @Override
        public final void toSQL(RenderContext context) {
            context.sql(field)
                   .keyword(not ? " not" : "")
                   .keyword(" between ")
                   .keyword(symmetric ? "symmetric " : "")
                   .sql(minValue)
                   .keyword(" and ")
                   .sql(maxValue);
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(field).bind(minValue).bind(maxValue);
        }
    }
}
