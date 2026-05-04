/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.impl.Keywords.K_RETURNING;
import static org.jooq.impl.Names.N_JSON;
import static org.jooq.impl.Names.N_JSON_EXTRACT;
import static org.jooq.impl.Names.N_JSON_QUERY;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.impl.QOM.UTransient;

/**
 * Put some text content into a JSON context.
 * <p>
 * Some dialects require constant setting of a JSON context to text content that
 * contains JSON because they cannot really distinguish between JSON and TEXT as
 * types. This transient querypart takes care of the transformation, of avoiding
 * double transformation, or just passes through to the actual content if JSON
 * is supported as a type.
 *
 * @author Lukas Eder
 */
final class JSONFromText<T>
extends
    AbstractField<T>
implements
    SimpleQueryPart,
    UTransient
{

    final Field<T> content;

    JSONFromText(Field<T> content) {
        super(N_JSON, content.getDataType());

        this.content = content instanceof JSONFromText<T> j
            ? j.content
            : content;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case MARIADB:
            case MYSQL:
            case SQLITE:
                ctx.visit(function(N_JSON_EXTRACT, getDataType(), content, inline("$")));
                break;

            default:
                ctx.visit(content);
                break;
        }
    }
}
