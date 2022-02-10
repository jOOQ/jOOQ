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

import static org.jooq.impl.Tools.camelCase;
import static org.jooq.impl.Tools.getMappedSchema;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Schema;
// ...

/**
 * @author Lukas Eder
 */
abstract class AbstractFunction<T> extends AbstractField<T> implements QOM.Function<T> {

    private final boolean applySchemaMapping;

    AbstractFunction(Name name, DataType<T> type, boolean applySchemaMapping) {
        super(name, type);

        this.applySchemaMapping = applySchemaMapping;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {







            default: {
                acceptFunctionName(ctx, applySchemaMapping, getQualifiedName());
                ctx.sql('(').visit(arguments()).sql(')');

                break;
            }
        }
    }

    static void acceptFunctionName(Context<?> ctx, boolean applySchemaMapping, Name name) {
        if (applySchemaMapping && name.qualified()) {





            Schema mapped = getMappedSchema(ctx, DSL.schema(name.qualifier()));

            if (mapped != null)
                ctx.visit(mapped.getQualifiedName().append(name.unqualifiedName()));
            else
                ctx.visit(name.unqualifiedName());
        }
        else
            ctx.visit(name);
    }

    abstract QueryPart arguments();

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------













}
