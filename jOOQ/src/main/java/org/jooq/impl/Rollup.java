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

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.FieldOrRow;
import org.jooq.QueryPart;

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
        super("rollup", SQLDataType.OTHER);

        this.arguments = new QueryPartList<FieldOrRow>(arguments);
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private final QueryPart delegate(Configuration configuration) {
        switch (configuration.family()) {
            case CUBRID:
            case MARIADB:
            case MYSQL:
                return DSL.field("{0} {with rollup}", arguments);

            default:
                return DSL.field("{rollup}({0})", Object.class, arguments);
        }
    }
}
