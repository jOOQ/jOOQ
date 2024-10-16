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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
 *
 *
 *
 */
package org.jooq.meta;

/**
 * A definition that is positioned at a 1-based index within its parent.
 *
 * @author Lukas Eder
 */
public interface PositionedDefinition extends Definition {

    /**
     * The object's 1-based position in the parent.
     * <p>
     * [#17434] While in most RDBMS, this value is 1-based and a consecutive
     * ordinal, there may be cases where a positional value is skipped, either
     * by what's reported by the RDBMS's dictionary views, or because the
     * definition is filtered by jOOQ-meta. Users shouldn't rely on the
     * positions reflecting the actually generated column/attribute/etc.
     * position.
     */
    int getPosition();

}
