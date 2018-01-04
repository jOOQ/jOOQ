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

import static org.jooq.impl.DSL.inline;

import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;

/**
 * @author Lukas Eder
 */
final class CommentImpl extends AbstractQueryPart implements Comment {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -5034168783226853829L;
    private final String      comment;

    CommentImpl(String comment) {
        this.comment = comment;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(inline(comment));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final String getComment() {
        return comment;
    }
}
