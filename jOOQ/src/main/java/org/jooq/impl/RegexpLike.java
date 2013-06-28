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

import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class RegexpLike extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = 3162855665213654276L;

    private final Field<?>      search;
    private final Field<String> pattern;

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
                context.sql(search)
                       .keyword(" regexp ")
                       .sql(pattern);

                break;
            }

            // [#620] HSQLDB has its own syntax
            case HSQLDB: {

                // [#1570] TODO: Replace this by SQL.condition(String, QueryPart...)
                context.sql(DSL.condition("{regexp_matches}({0}, {1})", search, pattern));
                break;
            }

            // [#620] Postgres has its own syntax
            case POSTGRES: {

                // [#1570] TODO: Replace this by SQL.condition(String, QueryPart...)
                context.sql(DSL.condition("{0} ~ {1}", search, pattern));
                break;
            }

            // [#620] Oracle has its own syntax
            case ORACLE: {

                // [#1570] TODO: Replace this by SQL.condition(String, QueryPart...)
                context.sql(DSL.condition("{regexp_like}({0}, {1})", search, pattern));
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
                context.sql(search)
                       .keyword(" like_regex ")
                       .sql(pattern);

                break;
            }
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(search).bind(pattern);
    }
}
