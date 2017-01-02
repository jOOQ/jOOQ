/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.sql;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class SQLInline extends AbstractQueryPart implements SQL {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 5352233054249655126L;

    private SQL sql;

    SQLInline(QueryPart part) {
        this(sql("{0}", part));
    }

    SQLInline(SQL sql) {
        this.sql = sql;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ParamType paramType = ctx.paramType();

        ctx.paramType(INLINED)
           .visit(sql)
           .paramType(paramType);
    }

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public String toString() {
        return sql.toString();
    }
}
