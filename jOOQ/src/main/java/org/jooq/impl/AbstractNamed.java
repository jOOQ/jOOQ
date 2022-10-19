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

import static org.jooq.impl.AbstractName.NO_NAME;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Comment;
import org.jooq.Name;
import org.jooq.Named;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
abstract class AbstractNamed extends AbstractQueryPart implements Named {

    private final Name    name;
    private final Comment comment;

    AbstractNamed(Name name, Comment comment) {
        this.name = name == null ? AbstractName.NO_NAME : name;
        this.comment = comment == null ? CommentImpl.NO_COMMENT : comment;
    }

    // -------------------------------------------------------------------------
    // The Named API
    // -------------------------------------------------------------------------

    @Override
    public final String getName() {
        return StringUtils.defaultIfNull(getQualifiedName().last(), "");
    }

    /* [#7172] For lazy initialisation reasons, some subtypes need to override this method */
    @Override
    public /* non-final */ Name getQualifiedName() {
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

    @Override
    public final Comment getCommentPart() {
        return comment;
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return getQualifiedName().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (that == null)
            return false;

        // [#2144] Non-equality can be decided early, without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof AbstractNamed n)
            if (!getQualifiedName().equals(n.getQualifiedName()))
                return false;

        return super.equals(that);
    }

    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------

    static final Name nameOrDefault(Named named) {
        return named == null ? NO_NAME : named.getUnqualifiedName();
    }

    static final Name qualify(Named qualifier, Name name) {
        // [#9820] [#11292] name == null || name.empty() are special cases that
        //                  may appear when using unnamed constraint declarations.
        //                  Their unnamedness must not be changed, nor qualified!
        return qualifier == null || name == null || name.empty() || name.qualified() ? name : qualifier.getQualifiedName().append(name);
    }

    static final <N extends Named> List<N> findAll(String name, Iterable<? extends N> in) {
        List<N> result = new ArrayList<>();

        for (N n : in)
            if (n.getName().equals(name))
                result.add(n);

        return result;
    }

    static final <N extends Named> List<N> findAll(Name name, Iterable<? extends N> in) {
        List<N> result = new ArrayList<>();

        for (N n : in)
            if (n.getQualifiedName().equals(name) || n.getUnqualifiedName().equals(name))
                result.add(n);

        return result;
    }

    static final <N extends Named> N find(String name, Iterable<? extends N> in) {
        return Tools.findAny(in, n -> n.getName().equals(name));
    }

    static final <N extends Named> N find(Name name, Iterable<? extends N> in) {
        N unqualified = null;

        for (N n : in)
            if (n.getQualifiedName().equals(name))
                return n;
            else if (unqualified == null && n.getUnqualifiedName().equals(name.unqualifiedName()))
                unqualified = n;

        return unqualified;
    }

    static final <N extends Named> N findIgnoreCase(String name, Iterable<? extends N> in) {
        return Tools.findAny(in, n -> n.getName().equalsIgnoreCase(name));
    }

    static final <N extends Named> N findIgnoreCase(Name name, Iterable<? extends N> in) {
        N unqualified = null;

        for (N n : in)
            if (n.getQualifiedName().equalsIgnoreCase(name))
                return n;
            else if (unqualified == null && n.getUnqualifiedName().equalsIgnoreCase(name.unqualifiedName()))
                unqualified = n;

        return unqualified;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Name $name() {
        return getQualifiedName();
    }
}
