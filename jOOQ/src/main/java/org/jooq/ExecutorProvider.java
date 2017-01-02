/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
