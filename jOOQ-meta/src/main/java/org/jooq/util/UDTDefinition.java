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
     * The UDT's package. <code>null</code> if the UDT is not in a package
     */
    PackageDefinition getPackage();

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
}
