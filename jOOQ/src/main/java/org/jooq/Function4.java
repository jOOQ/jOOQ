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

/**
 * A function of degree 4.
 * <p>
 * This is EXPERIMENTAL API. Future jOOQ versions may instead use the
 * functional interfaces from jOOλ, and remove these types again.
 *
 * @author Lukas Eder
 */
@Internal
@FunctionalInterface
public interface Function4<T1, T2, T3, T4, R> {

    /**
     * Applies this function to the given arguments.
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4);
}
