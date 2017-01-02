/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
