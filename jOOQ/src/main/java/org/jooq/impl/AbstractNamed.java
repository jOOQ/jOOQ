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

import org.jooq.Comment;
import org.jooq.Name;
import org.jooq.Named;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
abstract class AbstractNamed extends AbstractQueryPart implements Named {

    private static final long serialVersionUID = 4569512766643813958L;
    private final Name        name;
    private final Comment     comment;

    AbstractNamed(Name name, Comment comment) {
        this.name = name;
        this.comment = comment == null ? CommentImpl.NO_COMMENT : comment;
    }

    // -------------------------------------------------------------------------
    // The Named API
    // -------------------------------------------------------------------------

    @Override
    public final String getName() {
        return StringUtils.defaultIfNull(name.last(), "");
    }

    @Override
    public final Name getQualifiedName() {
        return name;
    }

    @Override
    public final Name getUnqualifiedName() {
        return name.unqualifiedName();
    }

    @Override
    public final String getComment() {
        return comment.getComment();
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return name == null ? 0 : name.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (that == null)
            return false;

        // [#2144] Non-equality can be decided early, without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof AbstractNamed)
            if (!name.equals(((AbstractNamed) that).name))
                return false;

        return super.equals(that);
    }

    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------

    static Name qualify(Named qualifier, Name name) {
        return qualifier == null || name.qualified() ? name : qualifier.getQualifiedName().append(name);
    }
}
