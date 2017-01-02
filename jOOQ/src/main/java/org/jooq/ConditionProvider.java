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

package org.jooq;

import java.util.Collection;

/**
 * A common interface for all objects holding conditions (e.g. queries)
 *
 * @author Lukas Eder
 * @deprecated - 2.6.0 [#1881] - This type will be removed from the public API,
 *             soon. Its methods will be pushed down into extending interfaces.
 *             Do not reference this type directly.
 */
@Deprecated
public interface ConditionProvider {

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with {@link Operator#AND}
     *
     * @param conditions The condition
     */
    @Support
    void addConditions(Condition... conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with {@link Operator#AND}
     *
     * @param conditions The condition
     */
    @Support
    void addConditions(Collection<? extends Condition> conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with the provided operator
     *
     * @param conditions The condition
     */
    @Support
    void addConditions(Operator operator, Condition... conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with the provided operator
     *
     * @param conditions The condition
     */
    @Support
    void addConditions(Operator operator, Collection<? extends Condition> conditions);

}
