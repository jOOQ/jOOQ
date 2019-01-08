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

import static org.jooq.impl.Keywords.K_ALTER;
import static org.jooq.impl.Keywords.K_CURRENT_SCHEMA;
import static org.jooq.impl.Keywords.K_SCHEMA;
import static org.jooq.impl.Keywords.K_SEARCH_PATH;
import static org.jooq.impl.Keywords.K_SESSION;
import static org.jooq.impl.Keywords.K_SET;
import static org.jooq.impl.Keywords.K_USE;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Schema;

/**
 * @author Lukas Eder
 */
final class SetSchema extends AbstractQuery {

    private static final long serialVersionUID = -3996953205762741746L;
    private final Schema      schema;

    SetSchema(Configuration configuration, Schema schema) {
        super(configuration);

        this.schema = schema;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case MARIADB:
            case MYSQL:
                ctx.visit(K_USE).sql(' ').visit(schema);
                break;




            case POSTGRES:
                ctx.visit(K_SET).sql(' ').visit(K_SEARCH_PATH).sql(" = ").visit(schema);
                break;

            case DERBY:
            case H2:
            case HSQLDB:
            default:
                ctx.visit(K_SET).sql(' ').visit(K_SCHEMA).sql(' ').visit(schema);
                break;
        }
    }
}
