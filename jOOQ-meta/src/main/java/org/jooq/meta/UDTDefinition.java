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

import java.util.List;

/**
 * A definition for a UDT
 * <p>
 * This extends {@link PackageDefinition} because Oracle internally models UDT's
 * in similar ways as packages. This is especially true for the way, member
 * procedures and functions are called.
 *
 * @author Lukas Eder
 */
public interface UDTDefinition extends PackageDefinition {

    /**
     * All attributes in the UDT
     */
    List<AttributeDefinition> getAttributes();

    /**
     * Get an attribute in this UDT by its name
     */
    AttributeDefinition getAttribute(String attributeName);

    /**
     * Get an attribute in this UDT by its index (starting at 0)
     */
    AttributeDefinition getAttribute(int attributeIndex);

    /**
     * All routines in the UDT
     */
    @Override
    List<RoutineDefinition> getRoutines();

    /**
     * Whether this UDT is a synthetic type.
     */
    @Override
    boolean isSynthetic();

    /**
     * Whether this UDT has either subtypes or supertypes or both.
     */
    boolean isInTypeHierarchy();

    /**
     * The subtypes of this UDT, if any.
     */
    List<UDTDefinition> getSubtypes();

    /**
     * The supertype of this UDT, if any.
     */
    UDTDefinition getSupertype();

    /**
     * Whether the UDT is instantiable.
     */
    boolean isInstantiable();
}
