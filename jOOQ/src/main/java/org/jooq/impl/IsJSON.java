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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Keywords.K_IS_JSON;
import static org.jooq.impl.Keywords.K_IS_NOT_JSON;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.INTEGER;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class IsJSON extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 5667086904534664235L;
    private final Field<?>    field;
    private final boolean     isJSON;

    IsJSON(Field<?> field, boolean isJSON) {
        this.field = field;
        this.isJSON = isJSON;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case MYSQL:
                ctx.visit(function("json_valid", BOOLEAN, field));
                break;








            default:
                ctx.visit(field).sql(' ').visit(isJSON ? K_IS_JSON : K_IS_NOT_JSON);
                break;
        }
    }
}
