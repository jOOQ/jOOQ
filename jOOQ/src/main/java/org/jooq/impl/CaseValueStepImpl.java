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
 */
package org.jooq.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class CaseValueStepImpl<V> implements CaseValueStep<V> {

    private final Field<V> value;

    CaseValueStepImpl(Field<V> value) {
        this.value = value;
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(V compareValue, T result) {
        return when(Tools.field(compareValue), Tools.field(result));
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(V compareValue, Field<T> result) {
        return when(Tools.field(compareValue), result);
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(V compareValue, Select<? extends Record1<T>> result) {
        return when(Tools.field(compareValue), DSL.field(result));
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(Field<V> compareValue, T result) {
        return when(compareValue, Tools.field(result));
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(Field<V> compareValue, Field<T> result) {
        return new CaseWhenStepImpl<V, T>(value, compareValue, result);
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(Field<V> compareValue, Select<? extends Record1<T>> result) {
        return when(compareValue, DSL.field(result));
    }

    @Override
    public final <T> CaseWhenStep<V, T> mapValues(Map<V, T> values) {
        Map<Field<V>, Field<T>> fields = new LinkedHashMap<Field<V>, Field<T>>();

        for (Entry<V, T> entry : values.entrySet())
            fields.put(Tools.field(entry.getKey()), Tools.field(entry.getValue()));

        return mapFields(fields);
    }

    @Override
    public final <T> CaseWhenStep<V, T> mapFields(Map<? extends Field<V>, ? extends Field<T>> fields) {
        return new CaseWhenStepImpl<V, T>(value, fields);
    }
}
