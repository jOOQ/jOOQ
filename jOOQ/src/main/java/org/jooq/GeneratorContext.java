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

import java.time.Instant;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link GeneratorContext} is an argument object that is passed to a
 * {@link Generator} when generating client side computed columns.
 * <p>
 * It is a {@link Scope} that models the short lived lifecycle of the generation
 * of the client side computed column. Its {@link #data()} map is inherited from
 * the parent {@link Context} scope, if available, or it is an empty map in case
 * no rendering {@link Context} was available.
 * <p>
 * This API is part of a commercial only feature. To use this feature, please
 * use the jOOQ Professional Edition or the jOOQ Enterprise Edition.
 *
 * @author Lukas Eder
 */
public interface GeneratorContext<R extends Record, X extends Table<R>, T> extends Scope {

    /**
     * The time, according to {@link Configuration#clock()}, when the rendering
     * {@link Context} was created, or when this {@link GeneratorContext} was
     * created in case no rendering {@link Context} was available.
     */
    @NotNull
    Instant renderTime();

    /**
     * The target table whose contents are being generated, or <code>null</code>
     * when the table is unknown / not applicable.
     */
    @Nullable
    X table();

    /**
     * The target field whose contents are being generated, or <code>null</code>
     * when the field is unknown / not applicable.
     */
    @Nullable
    Field<T> field();

    /**
     * The statement type in which the {@link Generator} is being invoked, or
     * <code>null</code> when the statement type is unknown / not applicable.
     */
    @Nullable
    GeneratorStatementType statementType();
}
