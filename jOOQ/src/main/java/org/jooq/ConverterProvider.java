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

import org.jooq.impl.DefaultConverterProvider;

import org.jetbrains.annotations.Nullable;

/**
 * A <code>ConverterProvider</code> provides {@link Converter} implementations
 * for any combination of types <code>&lt;T&gt;</code> and
 * <code>&lt;U&gt;</code>.
 * <p>
 * <code>ConverterProvider</code> can be used together with
 * {@link RecordMapper}, e.g. when mapping {@link JSON} or {@link XML} data
 * types onto POJO types using third party libraries like Jackson, Gson, JAXB,
 * or others.
 * <p>
 * The general expectation is for a {@link ConverterProvider} to be side-effect
 * free. Two calls to {@link #provide(Class, Class)} should always produce the
 * same {@link Converter} logic and thus conversion behaviour, irrespective of
 * context, other than the {@link Configuration} that hosts the
 * {@link Configuration#converterProvider()}. This effectively means that it is
 * possible for jOOQ to cache the outcome of a {@link #provide(Class, Class)}
 * call within the context of a {@link Configuration} or any derived context,
 * such as an {@link ExecuteContext}, to greatly improve performance.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface ConverterProvider {

    /**
     * Provide a converter that can convert between <code>&lt;T&gt;</code> and
     * <code>&lt;U&gt;</code> types.
     *
     * @return The converter for <code>&lt;T, U&gt;</code>, or <code>null</code>
     *         if no such converter could be provided, in case of which jOOQ's
     *         {@link DefaultConverterProvider} applies.
     */
    @Nullable
    <T, U> Converter<T, U> provide(Class<T> tType, Class<U> uType);
}
