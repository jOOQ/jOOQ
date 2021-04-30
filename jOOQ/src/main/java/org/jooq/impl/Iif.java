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

import static org.jooq.impl.Names.N_IF;
import static org.jooq.impl.Names.N_IIF;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;

/**
 * @author Lukas Eder
 */
final class Iif<T> extends AbstractField<T> {

    private final Condition   condition;
    private final Field<T>    ifTrue;
    private final Field<T>    ifFalse;

    Iif(Name name, Condition condition, Field<T> ifTrue, Field<T> ifFalse) {
        super(name, ifTrue.getDataType());

        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {


            case MARIADB:
            case MYSQL:
                ctx.visit(N_IF).sql('(').visit(condition).sql(", ").visit(ifTrue).sql(", ").visit(ifFalse).sql(')');
                break;








            default:
                ctx.visit(DSL.when(condition, ifTrue).otherwise(ifFalse));
                break;
        }
    }
}
