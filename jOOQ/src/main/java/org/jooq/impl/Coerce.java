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
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;

/**
 * @author Lukas Eder
 */
final class Coerce<T> extends AbstractField<T> {

    final AbstractField<?> field;

    public Coerce(Field<?> field, DataType<T> type) {
        super(field.getQualifiedName(), type);

        this.field = (AbstractField<?>) Tools.uncoerce(field);
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
    boolean isPossiblyNullable() {
        return field.isPossiblyNullable();
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
}
