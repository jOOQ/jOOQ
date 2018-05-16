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
 * A <code>QueryPart</code> to be used exclusively in <code>SELECT</code>
 * clauses.
 *
 * @author Lukas Eder
 */
public interface SelectField<T> extends SelectFieldOrAsterisk, Named {

    // ------------------------------------------------------------------------
    // API
    // ------------------------------------------------------------------------

    /**
     * The field's underlying {@link Converter}.
     * <p>
     * By default, all fields reference an identity-converter
     * <code>Converter&lt;T, T></code>. Custom data types may be obtained by a
     * custom {@link Converter} placed on the generated {@link TableField}.
     */
    Converter<?, T> getConverter();

    /**
     * The field's underlying {@link Binding}.
     */
    Binding<?, T> getBinding();

    /**
     * The Java type of the field.
     */
    Class<T> getType();

    /**
     * The type of this field (might not be dialect-specific).
     */
    DataType<T> getDataType();

    /**
     * The dialect-specific type of this field.
     */
    DataType<T> getDataType(Configuration configuration);

}
