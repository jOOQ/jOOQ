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

import java.util.Collections;
import java.util.List;

/**
 * Abstract base implementation for {@link UDTDefinition}'s
 *
 * @author Lukas Eder
 */
public abstract class AbstractUDTDefinition
extends
    AbstractElementContainerDefinition<AttributeDefinition>
implements
    UDTDefinition {

    private List<RoutineDefinition> routines;

    public AbstractUDTDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, null, name, comment);
    }

    public AbstractUDTDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, String comment) {
        super(schema, pkg, name, comment);
    }

    @Override
    public final List<AttributeDefinition> getAttributes() {
        return getElements();
    }

    @Override
    public final AttributeDefinition getAttribute(String attributeName) {
        return getElement(attributeName);
    }

    @Override
    public final AttributeDefinition getAttribute(int attributeIndex) {
        return getElement(attributeIndex);
    }

    @Override
    public final List<RoutineDefinition> getRoutines() {
        if (routines == null) {
            routines = getRoutines0();
        }

        return routines;
    }

    protected abstract List<RoutineDefinition> getRoutines0();

    @Override
    public List<AttributeDefinition> getConstants() {
        return Collections.emptyList();
    }
}
