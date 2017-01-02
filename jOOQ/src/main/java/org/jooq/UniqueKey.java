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

import java.util.List;

/**
 * A <code>UniqueKey</code> is an object representing a <code>UNIQUE KEY</code>
 * or a <code>PRIMARY KEY</code>. It can be referenced by a {@link ForeignKey}
 *
 * @param <R> The <code>KEY</code>'s owner table record
 * @author Lukas Eder
 */
public interface UniqueKey<R extends Record> extends Key<R> {

    /**
     * A list of all <code>ForeignKeys</code>, referencing this
     * <code>UniqueKey</code>
     */
    List<ForeignKey<?, R>> getReferences();

    /**
     * Whether this is the table's primary key.
     */
    boolean isPrimary();

}
