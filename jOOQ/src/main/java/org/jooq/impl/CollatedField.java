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

import static org.jooq.impl.Keywords.K_COLLATE;

import org.jooq.Binding;
import org.jooq.Collation;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class CollatedField extends AbstractField<String> {

    private static final long serialVersionUID = -3996395622492844215L;
    private final Field<?>    field;
    private final Collation   collation;

    CollatedField(Field<?> field, Collation collation) {
        super(field.getQualifiedName(), type(field), DSL.comment(field.getComment()), binding(field));

        this.field = field;
        this.collation = collation;
    }

    @SuppressWarnings("unchecked")
    private static final Binding<?, String> binding(Field<?> field) {
        return field.getType() == String.class ? (Binding<?, String>) field.getBinding() : SQLDataType.VARCHAR.getBinding();
    }

    @SuppressWarnings("unchecked")
    private static final DataType<String> type(Field<?> field) {
        return field.getType() == String.class ? (DataType<String>) field.getDataType() : SQLDataType.VARCHAR;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.sql("((").visit(field).sql(") ").visit(K_COLLATE).sql(' ').visit(collation).sql(')');

    }
}
