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

package org.jooq;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A field dereferenced from a UDT path.
 * <p>
 * Instances of this type cannot be created directly. They are available from
 * generated code.
 *
 * @param <R> The record type
 * @param <T> The field type
 * @author Lukas Eder
 */
public interface UDTPathField<R extends Record, U extends UDTRecord<U>, T> extends UDTField<U, T> {

    /**
     * Get the {@link TableField} reference on this UDT path.
     * <p>
     * While this currently returns non-<code>null</code> values only, as
     * there's always a {@link UDTPathTableField} on the path, there might not
     * be in the future, e.g. when a UDT path is based on a UDT constructor or
     * UDT returning function, which jOOQ currently does not yet support.
     */
    @Nullable
    UDTPathTableField<?, ?, ?> getTableField();

    /**
     * @return The UDT path this field is contained in.
     */
    @Nullable
    RecordQualifier<R> getQualifier();

    /**
     * @return The UDT path represented by this field.
     */
    @NotNull
    RecordQualifier<U> asQualifier();

}
