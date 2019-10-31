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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Keywords.F_SCHEMA_NAME;
import static org.jooq.impl.Keywords.F_USER;
import static org.jooq.impl.Keywords.K_CURRENT;
import static org.jooq.impl.Keywords.K_CURRENT_SCHEMA;
import static org.jooq.impl.Keywords.K_DATABASE;
import static org.jooq.impl.Keywords.K_SCHEMA;
import static org.jooq.impl.Names.N_CURRENT_SCHEMA;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.Context;

/**
 * @author Lukas Eder
 */
final class CurrentSchema extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    CurrentSchema() {
        super(N_CURRENT_SCHEMA, VARCHAR);
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {



















            case CUBRID:
            case FIREBIRD:
            case SQLITE:
                ctx.visit(inline(""));
                break;

            case DERBY:
                ctx.visit(K_CURRENT).sql(' ').visit(K_SCHEMA);
                break;

            case H2:
                ctx.visit(K_SCHEMA).sql("()");
                break;





            case MARIADB:
            case MYSQL:
                ctx.visit(K_DATABASE).sql("()");
                break;





            case HSQLDB:
            case POSTGRES:
                ctx.visit(K_CURRENT_SCHEMA);
                break;
            default:
                ctx.visit(K_CURRENT_SCHEMA).sql("()");
                break;
        }
    }
}
