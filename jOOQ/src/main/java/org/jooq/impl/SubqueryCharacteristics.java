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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.impl;

/**
 * Subquery characteristics.
 *
 * @author Lukas Eder
 */
final class SubqueryCharacteristics {

    // -------------------------------------------------------------------------
    // Different types of subqueries (mutually exclusive)
    // -------------------------------------------------------------------------

    static final int DERIVED_TABLE = 0x0001;
    static final int SET_OPERATION = 0x0002;
    static final int SCALAR        = 0x0004;

    // -------------------------------------------------------------------------
    // Additional characteristics of subqueries and their context
    // -------------------------------------------------------------------------

    static final int PREDICAND     = 0x0100;
    static final int CORRELATED    = 0x0200;

}
