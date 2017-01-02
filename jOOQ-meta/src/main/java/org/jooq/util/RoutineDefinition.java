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
 * An interface defining a stored routine in a database.
 *
 * @author Lukas Eder
 */
public interface RoutineDefinition extends Definition {

    /**
     * @return The routine's package. <code>null</code> if the routine is not in
     *         a package
     */
    PackageDefinition getPackage();

    /**
     * A list of IN or INOUT parameter column definitions
     */
    List<ParameterDefinition> getInParameters();

    /**
     * A list of OUT or INOUT parameter column definitions
     */
    List<ParameterDefinition> getOutParameters();

    /**
     * A list of all IN, OUT, and INOUT parameter column definitions
     */
    List<ParameterDefinition> getAllParameters();

    /**
     * @return The return value column definition
     */
    ParameterDefinition getReturnValue();

    /**
     * @return The return value simple Java type
     */
    DataTypeDefinition getReturnType();

    /**
     * @return Whether this routine can be used in SQL (a function without OUT
     *         parameters)
     */
    boolean isSQLUsable();

    /**
     * @return Whether this routine is an aggregate function
     */
    boolean isAggregate();

}
