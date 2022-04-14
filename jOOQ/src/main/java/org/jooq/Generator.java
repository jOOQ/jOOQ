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
package org.jooq;

import java.io.Serializable;
import java.util.function.Function;

/**
 * A generator can be used with {@link DataType#generatedAlwaysAs(Generator)} to
 * implement dynamic, client side computed columns.
 * <p>
 * This API is part of a commercial only feature. To use this feature, please
 * use the jOOQ Professional Edition or the jOOQ Enterprise Edition.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Generator<T> extends Function<GeneratorContext<T>, Field<T>>, Serializable {

    /**
     * Whether this generator supports a given statement type.
     * <p>
     * Implementations may choose to deactivate themselves for some statement
     * types, e.g. if they want to be invoked only for
     * {@link GeneratorStatementType#INSERT}.
     *
     * @param statementType The statement type.
     */
    default boolean supports(GeneratorStatementType statementType) {
        return true;
    }
}
