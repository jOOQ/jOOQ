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

import static org.jooq.impl.DSL.cardinality;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.Names.N_ARRAY_GET;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.tools.StringUtils.defaultIfNull;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class ArrayGet<T> extends AbstractField<T> {
    private final Field<T[]>     field;
    private final Field<Integer> index;

    @SuppressWarnings("unchecked")
    ArrayGet(Field<T[]> field, Field<Integer> index) {
        super(N_ARRAY_GET, (DataType<T>) defaultIfNull(field.getDataType().getArrayComponentDataType(), OTHER));

        this.field = field;
        this.index = index;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            case H2:
                ctx.visit(N_ARRAY_GET).sql('(').visit(field).sql(", ").visit(index).sql(')');
                break;

            case HSQLDB:
                ctx.visit(when(cardinality(field).ge(index), new Standard()));
                break;

            default:
                ctx.visit(new Standard());
                break;
        }
    }

    private class Standard extends AbstractField<T> {

        Standard() {
            super(ArrayGet.this.getQualifiedName(), ArrayGet.this.getDataType());
        }

        @Override
        public void accept(Context<?> ctx) {
            ctx.sql('(').visit(field).sql(')').sql('[').visit(index).sql(']');
        }
    }
}
