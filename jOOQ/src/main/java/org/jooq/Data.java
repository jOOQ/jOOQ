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

import java.io.Serializable;
import java.sql.SQLXML;

import org.jetbrains.annotations.NotNull;

/**
 * A type holding {@link #data()}, which is a {@link String} based
 * representation of a SQL data type with no reasonable representation in the
 * JDK or in JDBC.
 * <p>
 * JDBC maps most types to JDK types, but some vendor specific types lack an
 * equivalent in the JDK, or the relevant type is unsatisfactory (such as
 * {@link SQLXML}, which is a resource). To work around such limitations, jOOQ
 * establishes the set of {@link Data} types, with these properties:
 * <p>
 * <ul>
 * <li>They contain a {@link NotNull} representation of their {@link #data()}.
 * In other words, a <code>CAST(NULL AS …)</code> value is represented by a
 * <code>null</code> reference of type {@link Data}, not as
 * <code>data() == null</code>. This is consistent with jOOQ's general way of
 * returning <code>NULL</code> from {@link Result} and {@link Record}
 * methods.</li>
 * <li>They're {@link Serializable}</li>
 * <li>They may or may not use an internal, normalised representation of their
 * {@link #data()} for {@link #equals(Object)}, {@link #hashCode()}, and
 * {@link #toString()} (e.g. {@link JSONB})</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface Data extends Serializable {

    /**
     * Get the vendor specific {@link String} representation of this
     * {@link Data} object.
     */
    @NotNull
    String data();

}
