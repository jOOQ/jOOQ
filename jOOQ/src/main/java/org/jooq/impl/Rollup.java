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
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.Keywords.F_ROLLUP;
import static org.jooq.impl.Keywords.K_WITH_ROLLUP;
import static org.jooq.impl.Names.N_ROLLUP;
import static org.jooq.impl.SQLDataType.OTHER;

import org.jooq.Context;
import org.jooq.FieldOrRow;

/**
 * @author Lukas Eder
 */
final class Rollup extends AbstractField<Object> {

    /**
     * Generated UID
     */
    private static final long         serialVersionUID = -5820608758939548704L;
    private QueryPartList<FieldOrRow> arguments;

    Rollup(FieldOrRow... arguments) {
        super(N_ROLLUP, OTHER);

        this.arguments = new QueryPartList<>(arguments);
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {



            case CUBRID:
            case MARIADB:
            case MYSQL:
                ctx.visit(new MySQLWithRollup());
                break;

            default:
                ctx.visit(F_ROLLUP).sql('(').visit(arguments).sql(')');
                break;
        }
    }

    final class MySQLWithRollup extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2814185308000330197L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.visit(arguments)
               .formatSeparator()
               .visit(K_WITH_ROLLUP);
        }
    }
}
