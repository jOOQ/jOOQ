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

import org.jooq.impl.DSL;

/**
 * The {@link Formatter} SPI parameter object.
 *
 * @author Lukas Eder
 */
public interface FormatterContext extends Scope {

    /**
     * The {@link DataType} that is being formatted.
     */
    DataType<?> type();

    /**
     * Whether the formatting request is made in a
     * {@link DSL#multiset(TableLike)} context.
     */
    boolean multiset();

    /**
     * The input expression that is being formatted.
     */
    Field<?> field();

    /**
     * Pass the formatted output expression to the context.
     *
     * @param formatted The output expression.
     */
    void field(Field<?> formatted);
}
