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
import org.jooq.Condition;
import org.jooq.Configuration;
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
    private static final long serialVersionUID = -1806139685201770706L;

    private final Row         row;
    private final boolean     isNull;

    RowIsNull(Row row, boolean isNull) {
        this.row = row;
        this.isNull = isNull;
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
            context.sql(row)
                   .keyword(isNull ? " is null" : " is not null");
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(row);
        }
    }
}