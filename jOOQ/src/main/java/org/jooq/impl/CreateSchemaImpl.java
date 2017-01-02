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

import static org.jooq.Clause.CREATE_SCHEMA;
import static org.jooq.Clause.CREATE_SCHEMA_NAME;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateSchemaFinalStep;
import org.jooq.Record;
import org.jooq.Schema;

/**
 * @author Lukas Eder
 */
final class CreateSchemaImpl<R extends Record> extends AbstractQuery implements

    // Cascading interface implementations for CREATE SCHEMA behaviour
    CreateSchemaFinalStep {


    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { CREATE_SCHEMA };

    private final Schema          schema;
    private final boolean         ifNotExists;

    CreateSchemaImpl(Configuration configuration, Schema schema, boolean ifNotExists) {
        super(configuration);

        this.schema = schema;
        this.ifNotExists = ifNotExists;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(CREATE_SCHEMA_NAME)
           .keyword("create schema");

        if (ifNotExists)
            ctx.sql(' ').keyword("if not exists");

        ctx.sql(' ').visit(schema)
           .end(CREATE_SCHEMA_NAME);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
