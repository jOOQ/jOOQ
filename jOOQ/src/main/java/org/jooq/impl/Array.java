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

// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.Keywords.K_ARRAY;
import static org.jooq.impl.Keywords.K_INT;
import static org.jooq.impl.Names.N_ARRAY;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;

/**
 * @author Lukas Eder
 */
final class Array<T> extends AbstractField<T[]> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -6629785423729163857L;

    private final Fields<Record> fields;

    Array(Collection<? extends Field<T>> fields) {
        super(N_ARRAY, type(fields));

        this.fields = new Fields<>(fields);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <T> DataType<T[]> type(Collection<? extends Field<T>> fields) {
        if (fields == null || fields.isEmpty())
            return (DataType) SQLDataType.OTHER.getArrayDataType();
        else
            return fields.iterator().next().getDataType().getArrayDataType();
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {





            case H2:
            case HSQLDB:
            case POSTGRES:
            default:
                ctx.visit(K_ARRAY)
                   .sql('[')
                   .visit(fields)
                   .sql(']');

                if (fields.fields.length == 0 && ( ctx.family() == POSTGRES))
                    ctx.sql("::").visit(K_INT).sql("[]");

                break;
        }
    }
}
