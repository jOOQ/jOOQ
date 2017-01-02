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

import static org.jooq.Clause.ALTER_SCHEMA;
import static org.jooq.Clause.ALTER_SCHEMA_RENAME;
import static org.jooq.Clause.ALTER_SCHEMA_SCHEMA;
import static org.jooq.impl.DSL.name;

import org.jooq.AlterSchemaFinalStep;
import org.jooq.AlterSchemaStep;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Schema;

/**
 * @author Lukas Eder
 */
final class AlterSchemaImpl extends AbstractQuery implements

    // Cascading interface implementations for ALTER SCHEMA behaviour
    AlterSchemaStep,
    AlterSchemaFinalStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { ALTER_SCHEMA };

    private final Schema          schema;
    private final boolean         ifExists;
    private Schema                renameTo;

    AlterSchemaImpl(Configuration configuration, Schema schema) {
        this(configuration, schema, false);
    }

    AlterSchemaImpl(Configuration configuration, Schema schema, boolean ifExists) {
        super(configuration);

        this.schema = schema;
        this.ifExists = ifExists;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterSchemaImpl renameTo(Schema newName) {
        this.renameTo = newName;
        return this;
    }

    @Override
    public final AlterSchemaImpl renameTo(Name newName) {
        return renameTo(DSL.schema(newName));
    }

    @Override
    public final AlterSchemaImpl renameTo(String newName) {
        return renameTo(name(newName));
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(ALTER_SCHEMA_SCHEMA)
           .keyword("alter schema");

        if (ifExists)
            ctx.sql(' ').keyword("if exists");

        ctx.sql(' ').visit(schema)
           .end(ALTER_SCHEMA_SCHEMA)
           .formatIndentStart()
           .formatSeparator();

        if (renameTo != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_SCHEMA_RENAME)
               .qualify(false)
               .keyword("rename to").sql(' ').visit(renameTo)
               .qualify(qualify)
               .end(ALTER_SCHEMA_RENAME);
        }

        ctx.formatIndentEnd();
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
