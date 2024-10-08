/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.meta;

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
    private final boolean           synthetic;
    private final SchemaDefinition  supertypeSchema;
    private final String            supertypeName;
    private final boolean           instantiable;

    public AbstractUDTDefinition(SchemaDefinition schema, String name, String comment) {
        this(schema, null, name, false, comment);
    }

    public AbstractUDTDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, String comment) {
        this(schema, pkg, name, false, comment);
    }

    public AbstractUDTDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, boolean synthetic, String comment) {
        this(schema, pkg, name, synthetic, comment, null, null, true);
    }

    public AbstractUDTDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, boolean synthetic, String comment, SchemaDefinition supertypeSchema, String supertypeName, boolean instantiable) {
        super(schema, pkg, name, comment);

        this.synthetic = synthetic;
        this.supertypeSchema = supertypeSchema;
        this.supertypeName = supertypeName;
        this.instantiable = instantiable;
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
        if (routines == null)
            routines = getRoutines0();

        return routines;
    }

    @Override
    public List<UDTDefinition> getUDTs() {
        return getDatabase().getUDTs(this);
    }

    protected abstract List<RoutineDefinition> getRoutines0();

    @Override
    public List<AttributeDefinition> getConstants() {
        return Collections.emptyList();
    }

    @Override
    public boolean isSynthetic() {
        return synthetic;
    }

    @Override
    public boolean isInTypeHierarchy() {
        return getSupertype() != null || !getSubtypes().isEmpty();
    }

    @Override
    public UDTDefinition getSupertype() {
        return supertypeSchema == null ? null : getDatabase().getUDT(supertypeSchema, supertypeName);
    }

    @Override
    public List<UDTDefinition> getSubtypes() {
        return getDatabase().getSubtypes(this);
    }

    @Override
    public boolean isInstantiable() {
        return instantiable;
    }
}
