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

package org.jooq.util;


/**
 * A base implementation for column definitions.
 *
 * @author Lukas Eder
 */
public class DefaultParameterDefinition
    extends AbstractTypedElementDefinition<RoutineDefinition>
    implements ParameterDefinition {

    private final boolean isDefaulted;
    private final boolean isUnnamed;

    public DefaultParameterDefinition(RoutineDefinition routine, String name, int position, DataTypeDefinition type) {
        this(routine, name, position, type, false, false);
    }

    public DefaultParameterDefinition(RoutineDefinition routine, String name, int position, DataTypeDefinition type, boolean isDefaulted) {
        this(routine, name, position, type, isDefaulted, false);
    }

    public DefaultParameterDefinition(RoutineDefinition routine, String name, int position, DataTypeDefinition type, boolean isDefaulted, boolean isUnnamed) {
        super(routine, name, position, type, null);

        this.isDefaulted = isDefaulted;
        this.isUnnamed = isUnnamed;
    }

    @Override
    public boolean isDefaulted() {
        return isDefaulted;
    }

    @Override
    public boolean isUnnamed() {
        return isUnnamed;
    }
}
