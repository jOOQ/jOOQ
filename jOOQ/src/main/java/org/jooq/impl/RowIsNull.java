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

import static java.util.Arrays.asList;
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_IS_NOT_NULL;
import static org.jooq.Clause.CONDITION_IS_NULL;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import java.util.ArrayList;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.Row;

/**
 * @author Lukas Eder
 */
final class RowIsNull extends AbstractCondition {

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
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private final QueryPartInternal delegate(Configuration configuration) {

        // CUBRID 9.0.0 and HSQLDB have buggy implementations of the NULL predicate.
        // Informix doesn't implement the RVE IS NULL predicate.
        if (asList(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, SQLITE).contains(configuration.family())) {
            List<Condition> conditions = new ArrayList<Condition>();

            for (Field<?> field : row.fields()) {
                conditions.add(isNull ? field.isNull() : field.isNotNull());
            }

            Condition result = DSL.and(conditions);
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
        public final void accept(Context<?> ctx) {
            ctx.visit(row)
               .sql(' ')
               .keyword(isNull ? "is null" : "is not null");
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return isNull ? CLAUSES_NULL : CLAUSES_NOT_NULL;
        }
    }
}