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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QueryPart;
// ...
// ...
import org.jooq.tools.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Eder
 */
final class Coerce<T>
extends
    AbstractField<T>
implements
    AutoAlias<Field<T>>,
    NamedCheckField<T>,
    QOM.Coerce<T>
{

    final AbstractField<?> field;

    public Coerce(Field<?> field, DataType<T> type) {
        super(field.getQualifiedName(), type);

        this.field = (AbstractField<?>) Tools.uncoerce(field);
    }

    @Override
    public final boolean hasName(Context<?> ctx) {
        return Tools.hasName(ctx, field);
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(field);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return field.clauses(ctx);
    }

    @Override
    final boolean isNullable() {
        return field.isNullable();
    }

    @Override
    public final Field<T> as(Name alias) {
        return field.as(alias).coerce(getDataType());
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return field.rendersContent(ctx);
    }

    @Override
    public final boolean declaresFields() {
        return field.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return field.declaresTables();
    }

    @Override
    public final boolean declaresWindows() {
        return field.declaresWindows();
    }

    @Override
    public final boolean declaresCTE() {
        return field.declaresCTE();
    }










    @Override
    public final boolean generatesCast() {
        return field.generatesCast();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> autoAlias(Context<?> ctx, Field<T> f) {
        if (field instanceof AutoAlias)
            return ((AutoAlias<Field<T>>) field).autoAlias(ctx, (Field<T>) field).coerce(getDataType());
        else
            return f;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $aliased() {
        return field.$aliased().coerce(getDataType());
    }

    @Override
    public final Name $alias() {
        return field.$alias();
    }

    @Override
    public final Field<?> $field() {
        return field;
    }


























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Coerce<?> o) {
            return
                Objects.equals($field(), o.$field())
            ;
        }
        else
            return super.equals(that);
    }
}
