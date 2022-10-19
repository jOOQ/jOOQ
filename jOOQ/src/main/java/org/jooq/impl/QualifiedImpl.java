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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.impl.Tools.getMappedSchema;

import org.jooq.Catalog;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Qualified;
import org.jooq.Schema;
import org.jooq.impl.QOM.UTransient;


/**
 * A base implementation for {@link Qualified} objects that handles schema
 * mapping.
 *
 * @author Lukas Eder
 */
final class QualifiedImpl extends AbstractNamed implements Qualified, UTransient {

    QualifiedImpl(Name name) {
        super(name, CommentImpl.NO_COMMENT);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart
    // -------------------------------------------------------------------------

    static final void acceptMappedSchemaPrefix(Context<?> ctx, Schema schema) {
        Schema mappedSchema = getMappedSchema(ctx, schema);

        if (mappedSchema != null && !"".equals(mappedSchema.getName()))
            ctx.visit(mappedSchema).sql('.');
    }

    @Override
    public final void accept(Context<?> ctx) {
        acceptMappedSchemaPrefix(ctx, getSchema());
        ctx.visit(getUnqualifiedName());
    }

    // -------------------------------------------------------------------------
    // XXX: Qualified API
    // -------------------------------------------------------------------------

    @Override
    public final Catalog getCatalog() {
        Schema schema = getSchema();

        if (schema != null)
            return schema.getCatalog();
        else
            return null;
    }

    @Override
    public final Schema getSchema() {
        if (getQualifiedName().qualified())
            return new SchemaImpl(getQualifiedName().qualifier());
        else
            return null;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Schema $schema() {
        return getSchema();
    }
}
