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

import org.jooq.Case;
import org.jooq.CaseConditionStep;
import org.jooq.CaseValueStep;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class CaseImpl implements Case {

    CaseImpl() {
    }

    @Override
    public final <V> CaseValueStep<V> value(V value) {
        return value(Tools.field(value));
    }

    @Override
    public final <V> CaseValueStep<V> value(Field<V> value) {
        return new CaseValueStepImpl<V>(value);
    }

    @Override
    public final <T> CaseConditionStep<T> when(Condition condition, T result) {
        return when(condition, Tools.field(result));
    }

    @Override
    public final <T> CaseConditionStep<T> when(Condition condition, Field<T> result) {
        return new CaseConditionStepImpl<T>(condition, result);
    }

    @Override
    public final <T> CaseConditionStep<T> when(Condition condition, Select<? extends Record1<T>> result) {
        return when(condition, DSL.field(result));
    }
}
