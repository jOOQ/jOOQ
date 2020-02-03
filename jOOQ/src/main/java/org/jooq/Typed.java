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
 * A marker interface for all query parts that have a {@link DataType}.
 * <p>
 * While there is no requirement for implementations to also implement
 * {@link Named}, a lot of implementations do.
 *
 * @author Lukas Eder
 */
public interface Typed<T> extends QueryPart {

    /**
     * The object's underlying {@link Converter}.
     * <p>
     * By default, all typed objects reference an identity-converter
     * <code>Converter&lt;T, T&gt;</code>. If an implementation is generated,
     * custom data types may be obtained by a custom {@link Converter} placed on
     * the generated object.
     */
    Converter<?, T> getConverter();

    /**
     * The object's underlying {@link Binding}.
     */
    Binding<?, T> getBinding();

    /**
     * The Java type of the object.
     */
    Class<T> getType();

    /**
     * The type of this object (might not be dialect-specific).
     */
    DataType<T> getDataType();

    /**
     * The dialect-specific type of this object.
     */
    DataType<T> getDataType(Configuration configuration);

}
