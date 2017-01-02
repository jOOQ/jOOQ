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

import static org.jooq.conf.RenderNameStyle.AS_IS;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nullSafe;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.TableField;
import org.jooq.conf.RenderNameStyle;

/**
 * @author Lukas Eder
 */
final class Prior<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 4532570030471782063L;
    private final Field<T>    field;

    Prior(Field<T> field) {
        super("prior", nullSafe(field).getDataType());

        this.field = field;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






























            case CUBRID:
            default:
                ctx.keyword("prior").sql(' ').visit(field);
                break;
        }
    }
}
