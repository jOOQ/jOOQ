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
package org.jooq.impl;

import java.util.function.Supplier;

/**
 * A utility for lazy initialisation of constant values.
 * <p>
 * Eager static initialisation of constant values has produced numerous race
 * conditions and deadlocks in the past in user code that initialised jOOQ
 * classes in parallel. For example:
 * <ul>
 * <li><a href=
 * "https://github.com/jOOQ/jOOQ/issues/11200">https://github.com/jOOQ/jOOQ/issues/11200</a></li>
 * <li><a href=
 * "https://github.com/jOOQ/jOOQ/issues/13814">https://github.com/jOOQ/jOOQ/issues/13814</a></li>
 * <li><a href=
 * "https://github.com/jOOQ/jOOQ/issues/14769">https://github.com/jOOQ/jOOQ/issues/14769</a></li>
 * </ul>
 * <p>
 * This generic Lazy utility should delay the initialisation of the constant in
 * the absence of formal JDK support: <a href=
 * "https://openjdk.org/jeps/8209964">https://openjdk.org/jeps/8209964</a>
 *
 * @author Lukas Eder
 */
final class Lazy<T> {

    static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        return new Lazy<>(supplier);
    }

    private final Supplier<? extends T> supplier;
    private volatile T                  value;

    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    T get() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    value = supplier.get();
                }
            }
        }

        return value;
    }
}
