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

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Keyword;
import org.jooq.Name;
import org.jooq.OnConflict;

import static org.jooq.impl.Keywords.K_COLLATE;

/**
 * @author Timur Shaidullin
 */
final class OnConflictImpl extends AbstractQueryPart implements OnConflict {

    private Field<?> field;
    private Name     collation;
    private Keyword  opclass;

    OnConflictImpl(Field<?> field, Keyword opclass) {
        this(field, null, opclass);
    }

    OnConflictImpl(Field<?> field, Name collation) {
        this(field, collation, null);
    }

    OnConflictImpl(Field<?> field, Name collation, Keyword opclass) {
        this.field = field;
        this.collation = collation;
        this.opclass = opclass;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(field);

        if (collation != null)
            ctx
               .sql(' ')
               .visit(K_COLLATE)
               .sql(' ')
               .visit(collation);

        if (opclass != null)
            ctx
               .sql(' ')
               .visit(opclass);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
