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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import java.util.Objects;

import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QueryPart;
// ...
import org.jooq.Table;
// ...

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class FieldAlias<T> extends AbstractField<T> implements QOM.FieldAlias<T>, SimpleCheckQueryPart {

    private final Alias<Field<T>> alias;

    FieldAlias(Field<T> field, Name alias) {
        this(field, alias, null);
    }

    FieldAlias(Field<T> field, Name alias, Comment comment) {
        super(alias, field.getDataType(), comment);

        this.alias = new Alias<>(field, this, alias);
    }

    @Override
    public final boolean isSimple(Context<?> ctx) {
        return !ctx.declareFields();
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(alias);
    }

    @Override // Avoid AbstractField implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final Field<T> as(Name as) {
        return new FieldAlias<>(alias.wrapped(), as, getCommentPart());
    }

    @Override
    public final Field<T> comment(Comment comment) {
        return new FieldAlias<>(alias.wrapped(), alias.alias, comment);
    }

    @Override
    public final boolean declaresFields() {
        return true;
    }

    @Override
    public Name getQualifiedName() {
        return getUnqualifiedName();
    }

    /**
     * Get the aliased field wrapped by this field.
     */
    Field<T> getAliasedField() {
        if (alias != null)
            return alias.wrapped();

        return null;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $field() {
        return alias.wrapped();
    }

    @Override
    public final Field<?> $aliased() {
        return alias.wrapped();
    }

    @Override
    public final @NotNull Name $alias() {
        return getQualifiedName();
    }















    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return alias.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof FieldAlias o) {
            return Objects.equals(alias, o.alias);
        }
        else
            return super.equals(that);
    }
}
