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

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Record;

/**
 * @author Lukas Eder
 */
final class RecordCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 56430809227808954L;

    private final Record      record;

    RecordCondition(Record record) {
        this.record = record;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void accept(Context<?> ctx) {
        ConditionProviderImpl condition = new ConditionProviderImpl();

        int size = record.size();
        for (int i = 0; i < size; i++) {
            Object value = record.get(i);

            if (value != null) {
                Field f1 = record.field(i);
                Field f2 = DSL.val(value, f1.getDataType());

                condition.addConditions(f1.eq(f2));
            }
        }

        ctx.visit(condition);
    }
}
