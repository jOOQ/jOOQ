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

import static org.jooq.impl.Keywords.K_COLLATE;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.jooq.Binding;
import org.jooq.Collation;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.QueryPart;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class Collated extends AbstractField<String> implements QOM.Collated {

    private final Field<?>  field;
    private final Collation collation;

    Collated(Field<?> field, Collation collation) {
        super(field.getQualifiedName(), type(field).collation(collation), field.getCommentPart(), binding(field));

        this.field = field;
        this.collation = collation;
    }

    @SuppressWarnings("unchecked")
    private static final Binding<?, String> binding(Field<?> field) {
        return field.getDataType().isString() ? (Binding<?, String>) field.getBinding() : SQLDataType.VARCHAR.getBinding();
    }

    @SuppressWarnings("unchecked")
    private static final DataType<String> type(Field<?> field) {
        return field.getDataType().isString() ? (DataType<String>) field.getDataType() : SQLDataType.VARCHAR;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#8011] Collations are vendor-specific storage clauses, which we might need to ignore
        if (ctx.configuration().data("org.jooq.ddl.ignore-storage-clauses") == null)
            ctx.sql("((").visit(field).sql(") ").visit(K_COLLATE).sql(' ').visit(collation).sql(')');
        else
            ctx.visit(field);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $field() {
        return field;
    }

    @Override
    public final Collation $collation() {
        return collation;
    }














}
