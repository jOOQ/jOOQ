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

import static org.jooq.impl.Keywords.K_CASE;
import static org.jooq.impl.Keywords.K_ELSE;
import static org.jooq.impl.Keywords.K_END;
import static org.jooq.impl.Keywords.K_SWITCH;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_TRUE;
import static org.jooq.impl.Keywords.K_WHEN;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.CaseWhenStep;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
// ...

final class CaseWhenStepImpl<V, T> extends AbstractField<T> implements CaseWhenStep<V, T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -3817194006479624228L;

    private final Field<V>       value;
    private final List<Field<V>> compareValues;
    private final List<Field<T>> results;
    private Field<T>             else_;

    CaseWhenStepImpl(Field<V> value, Field<V> compareValue, Field<T> result) {
        this(value, result.getDataType());

        when(compareValue, result);
    }

    CaseWhenStepImpl(Field<V> value, Map<? extends Field<V>, ? extends Field<T>> map) {
        this(value, dataType(map));

        for (Entry<? extends Field<V>, ? extends Field<T>> entry : map.entrySet())
            when(entry.getKey(), entry.getValue());
    }

    private CaseWhenStepImpl(Field<V> value, DataType<T> type) {
        super(DSL.name("case"), type);

        this.value = value;
        this.compareValues = new ArrayList<>();
        this.results = new ArrayList<>();
    }


    @SuppressWarnings("unchecked")
    private static final <T> DataType<T> dataType(Map<? extends Field<?>, ? extends Field<T>> map) {
        if (map.isEmpty())
            return (DataType<T>) SQLDataType.OTHER;
        else
            return map.entrySet().iterator().next().getValue().getDataType();
    }

    @Override
    public final Field<T> otherwise(T result) {
        return else_(result);
    }

    @Override
    public final Field<T> otherwise(Field<T> result) {
        return else_(result);
    }

    @Override
    public final Field<T> else_(T result) {
        return else_(Tools.field(result));
    }

    @Override
    public final Field<T> else_(Field<T> result) {
        this.else_ = result;

        return this;
    }

    @Override
    public final CaseWhenStep<V, T> when(V compareValue, T result) {
        return when(Tools.field(compareValue, value), Tools.field(result));
    }

    @Override
    public final CaseWhenStep<V, T> when(V compareValue, Field<T> result) {
        return when(Tools.field(compareValue, value), result);
    }

    @Override
    public final CaseWhenStep<V, T> when(Field<V> compareValue, T result) {
        return when(compareValue, Tools.field(result));
    }

    @Override
    public final CaseWhenStep<V, T> when(Field<V> compareValue, Field<T> result) {
        compareValues.add(compareValue);
        results.add(result);

        return this;
    }

    @Override
    public final CaseWhenStep<V, T> mapValues(Map<V, T> values) {
        for (Entry<V, T> entry : values.entrySet())
            when(entry.getKey(), entry.getValue());

        return this;
    }

    @Override
    public final CaseWhenStep<V, T> mapFields(Map<? extends Field<V>, ? extends Field<T>> fields) {
        for (Entry<? extends Field<V>, ? extends Field<T>> entry : fields.entrySet())
            when(entry.getKey(), entry.getValue());

        return this;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(new Native());
                break;
        }
    }








































    private class Native extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7564667836130498156L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.formatIndentLockStart()
               .visit(K_CASE);

            int size = compareValues.size();
            switch (ctx.family()) {

                // The DERBY dialect doesn't support the simple CASE clause
                case DERBY: {
                    ctx.formatIndentLockStart();

                    for (int i = 0; i < size; i++) {
                        if (i > 0)
                            ctx.formatNewLine();

                        ctx.sql(' ').visit(K_WHEN).sql(' ');
                        ctx.visit(value.equal(compareValues.get(i)));
                        ctx.sql(' ').visit(K_THEN).sql(' ');
                        ctx.visit(results.get(i));
                    }

                    break;
                }

                default: {
                    ctx.sql(' ')
                       .visit(value)
                       .formatIndentStart();

                    for (int i = 0; i < size; i++)
                        ctx.formatSeparator()
                           .visit(K_WHEN).sql(' ')
                           .visit(compareValues.get(i)).sql(' ')
                           .visit(K_THEN).sql(' ')
                           .visit(results.get(i));

                    break;
                }
            }

            if (else_ != null)
                ctx.formatSeparator()
                   .visit(K_ELSE).sql(' ')
                   .visit(else_);

            ctx.formatIndentEnd();

            if (size > 1 || else_ != null)
                ctx.formatSeparator();
            else
                ctx.sql(' ');

            ctx.visit(K_END)
               .formatIndentLockEnd();
        }
    }
}
