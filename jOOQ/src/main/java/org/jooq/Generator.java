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
package org.jooq;

import java.io.Serializable;
import java.util.function.Function;

import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.impl.QOM.GenerationOption;

/**
 * A generator can be used with {@link DataType#generatedAlwaysAs(Generator)} to
 * implement dynamic, client side computed columns, i.e. computed columns with
 * {@link GenerationLocation#CLIENT}.
 * <p>
 * There are two types of client side computed columns:
 * <ul>
 * <li>{@link GenerationOption#STORED}: The computation is performed when
 * writing to a record via {@link Insert}, {@link Update}, or {@link Merge}</li>
 * <li>{@link GenerationOption#VIRTUAL}: The computation is performed when
 * reading a record via {@link Select}, or the <code>RETURNING</code> clause of
 * {@link Insert}, {@link Update}, {@link Delete}.</li>
 * </ul>
 * <p>
 * Depending on the type of client side computed column, the exact time when the
 * computation is performed may differ, practically. It is not specified, when
 * it happens, but users may assume that it happens only once per query
 * rendering and {@link Field} expression which references a {@link Generator},
 * independently of how many times the resulting expression is repeated in the
 * resulting SQL query.
 * <p>
 * This API is part of a commercial only feature. To use this feature, please
 * use the jOOQ Professional Edition or the jOOQ Enterprise Edition.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Generator<R extends Record, X extends Table<R>, T> extends Function<GeneratorContext<R, X, T>, Field<T>>, Serializable {

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
