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

import static org.jooq.impl.Keywords.K_SET;

// ...
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Assignment<T> extends AbstractStatement {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1567637930559064772L;
    final AssignmentTarget<T> target;
    final Field<T>            value;

    Assignment(AssignmentTarget<T> target, Field<T> value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case MARIADB:
            case MYSQL:
            case SQLSERVER:
                ctx.visit(K_SET).sql(' ').visit(target).sql(" = ").visit(value);
                break;

            case POSTGRES:
            case ORACLE:
            default:
                ctx.visit(target).sql(" := ").visit(value);
                break;
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
/* [/pro] */