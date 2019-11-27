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
 *
 *
 *
 */
package org.jooq.meta;

/**
 * An interface defining a sequence
 *
 * @author Lukas Eder
 */
public interface SequenceDefinition extends TypedElementDefinition<SchemaDefinition> {

    /**
     * Get the start value for this sequence or <code>null</code>, if no such
     * value is specified.
     */
    Long getStartWith();

    /**
     * Get the increment for this sequence or <code>null</code>, if no such
     * value is specified.
     */
    Long getIncrementBy();

    /**
     * Get the minimum value for this sequence or <code>null</code>, if no such
     * value is specified.
     */
    Long getMinValue();

    /**
     * Get the maximum value for this sequence or <code>null</code>, if no such
     * value is specified.
     */
    Long getMaxValue();

    /**
     * Returns {@code true} if this sequence cycles to {@link #getMinValue()}
     * when it reaches {@link #getMaxValue()}.
     */
    boolean getCycle();

    /**
     * Get the number of sequence values to cache for this sequence or
     * <code>null</code>, if no such value is specified.
     */
    Long getCache();

}
