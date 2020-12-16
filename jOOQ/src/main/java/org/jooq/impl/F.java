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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.Internal;

/**
 * An internal alternative to the JDK's functional interface types, ready for
 * use on Java 6+.
 *
 * @deprecated - Do not use these in client code as they are subject for removal
 *             in future jOOQ versions.
 */
@Deprecated
public final class F {

    @FunctionalInterface
    @Internal
    @Deprecated
    public interface F0<R> extends Supplier<R> {

        @Override

        R get();
    }

    @FunctionalInterface
    @Internal
    @Deprecated
    public interface F1<T1, R> extends Function<T1, R> {

        @Override

        R apply(T1 t1);
    }

    @FunctionalInterface
    @Internal
    @Deprecated
    public interface F2<T1, T2, R> extends BiFunction<T1, T2, R> {

        @Override

        R apply(T1 t1, T2 t2);
    }

    @FunctionalInterface
    @Internal
    @Deprecated
    public interface A0 extends Runnable {
        @Override
        void run();
    }

    @FunctionalInterface
    @Internal
    @Deprecated
    public interface A1<T1> extends Consumer<T1> {

        @Override

        void accept(T1 t1);
    }

    @FunctionalInterface
    @Internal
    @Deprecated
    public interface A2<T1, T2> extends BiConsumer<T1, T2> {

        @Override

        void accept(T1 t1, T2 t2);
    }
}
