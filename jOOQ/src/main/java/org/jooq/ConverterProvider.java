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

/**
 * A <code>ConverterProvider</code> providers {@link Converter} implementations
 * for any combination of types <code>&lt;T></code> and <code>&lt;U></code>.
 *
 * @author Lukas Eder
 * @deprecated - This API is still EXPERIMENTAL. Do not use yet
 */
@Deprecated

@FunctionalInterface

public interface ConverterProvider {

    /**
     * Provide a converter that can convert between <code>&lt;T></code> and
     * <code>&lt;U></code> types.
     */
    <T, U> Converter<T, U> provide(Class<T> tType, Class<U> uType);
}
