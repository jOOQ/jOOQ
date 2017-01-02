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

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * The <code>ExecutorProvider</code> SPI can be used to provide jOOQ with custom
 * asynchronous execution behaviour.
 * <p>
 * Asynchronous operations will call back to this SPI to obtain an executor.
 * This applies, for example, to {@link ResultQuery#fetchAsync()}.
 * <p>
 * The following logic is applied when resolving the appropriate
 * <code>executor</code>:
 * <ol>
 * <li>If {@link Configuration#executorProvider()} does not return
 * <code>null</code>, then {@link #provide()} is called to obtain an
 * <code>Executor</code> for the asynchronous task.</li>
 * <li>In the jOOQ Java 8 distribution, {@link ForkJoinPool#commonPool()} is
 * used if <code>{@link ForkJoinPool#getCommonPoolParallelism()} > 1</code></li>
 * <li>A new "one thread per call" <code>Executor</code> is used in any other
 * case.</li>
 * </ol>
 * <p>
 * The SPI will not be called if an asynchronous operation explicitly overrides
 * the {@link Executor}, e.g. as is the case for
 * {@link ResultQuery#fetchAsync(Executor)}.
 *
 * @author Lukas Eder
 */

@FunctionalInterface

public interface ExecutorProvider {

    /**
     * Provide an <code>Executor</code> for the task at hand.
     */
    Executor provide();
}
