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

import java.util.Map;
import java.util.Map.Entry;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class MapCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6320436041406801993L;

    private final Map<Field<?>, ?>   map;

    MapCondition(Map<Field<?>, ?> map) {
        this.map = map;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void accept(Context<?> ctx) {
        ConditionProviderImpl condition = new ConditionProviderImpl();

        for (Entry<Field<?>, ?> entry : map.entrySet()) {
            Field f1 = entry.getKey();
            Field f2 = Tools.field(entry.getValue(), f1);

            condition.addConditions(f1.eq(f2));
        }

        ctx.visit(condition);
    }
}
