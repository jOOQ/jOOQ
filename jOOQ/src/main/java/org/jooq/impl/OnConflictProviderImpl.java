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
import org.jooq.OnConflictProvider;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Timur Shaidullin
 */
final class OnConflictProviderImpl extends AbstractQueryPart implements OnConflictProvider {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4874673510043708707L;

    private Collection<OnConflict> onConflicts = new ArrayList<OnConflict>();

    public final boolean isNotEmpty() {
        return onConflicts.size() > 0;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (isNotEmpty()) {
            String separator = "";
            for (OnConflict onConflict : onConflicts) {
                ctx.sql(separator)
                   .visit(onConflict);

                separator =", ";
            }
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final void addOnConflict(Field<?> field) {
        addOnConflict(field, null, null);
    }

    @Override
    public final void addOnConflict(Field<?> field, Name collation) {
        addOnConflict(field, collation, null);
    }

    @Override
    public final void addOnConflict(Field<?> field, Keyword opclass) {
        addOnConflict(field, null, opclass);
    }

    @Override
    public final void addOnConflict(Field<?> field, Name collation, Keyword opclass) {
        addOnConflict(DSL.onConflict(field, collation, opclass));
    }

    @Override
    public final void addOnConflict(OnConflict onConflict) {
        onConflicts.add(onConflict);
    }
}
