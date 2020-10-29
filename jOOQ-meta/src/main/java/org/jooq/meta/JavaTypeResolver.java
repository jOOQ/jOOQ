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
package org.jooq.meta;

/**
 * A function that can be injected into jOOQ-meta elements to resolve Java types
 * from {@link DataTypeDefinition}.
 * <p>
 * This inversion of control is necessary to inject jOOQ-codegen behaviour into
 * jOOQ-meta. It might become obsolete once we merge the two modules again.
 *
 * @author Lukas Eder
 */
public interface JavaTypeResolver {

    /**
     * Resolve a Java type from a {@link DataTypeDefinition}.
     */
    String resolve(DataTypeDefinition type);

    /**
     * Get a language dependent class literal for a type.
     * <p>
     * <table>
     * <tr><th>Language</th><th>Output for <code>String</code></th></tr>
     * <tr><td>Java</td><td><code>String.class</code></td></tr>
     * <tr><td>Scala</td><td><code>classOf[String]</code></td></tr>
     * <tr><td>Kotlin</td><td><code>String::class.java</code></td></tr>
     * </table>
     */
    String classLiteral(String type);

    /**
     * Get a language dependent constructor call for a type.
     * <p>
     * <table>
     * <tr><th>Language</th><th>Output for <code>String</code></th></tr>
     * <tr><td>Java</td><td><code>new EnumConverter&lt;A, B&gt;</code></td></tr>
     * <tr><td>Scala</td><td><code>new EnumConverter[A, B]</code></td></tr>
     * <tr><td>Kotlin</td><td><code>EnumConverter&lt;A, B&gt;</code></td></tr>
     * </table>
     */
    String constructorCall(String type);

    /**
     * Get the unqualified type reference and add an import for the qualified
     * type, if necessary.
     */
    String ref(String type);

    /**
     * Get the unqualified type reference and add an import for the qualified
     * type, if necessary.
     */
    String ref(Class<?> type);
}
