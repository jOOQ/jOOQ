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

// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * UDT definition.
 * <p>
 * Instances of this type cannot be created directly. They are available from
 * generated code.
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface UDT<R extends UDTRecord<R>> extends RecordQualifier<R> {

    /**
     * Whether this data type can be used from SQL statements.
     */
    boolean isSQLUsable();

    /**
     * Whether this data type is a synthetic, structural UDT type.
     * <p>
     * This is <code>true</code> for example:
     * <ul>
     * <li>For Oracle <code>TAB%ROWTYPE</code> references, which are synthetic
     * PL/SQL RECORD types in PL/SQL.</li>
     * </ul>
     */
    boolean isSynthetic();

    /**
     * Get the supertype of this {@link UDT}, or <code>null</code> if this type
     * has no supertype.
     */
    @Nullable
    UDT<?> getSupertype();

    /**
     * Get the subtypes of this {@link UDT} or an empty list, if there are no
     * known subtypes.
     */
    @NotNull
    List<UDT<?>> getSubtypes();

    /**
     * Check if this type is a supertype or the same type as another {@link UDT} type.
     */
    boolean isAssignableFrom(UDT<?> other);

    /**
     * Create a constructor call for UDTs, with all UDT fields set to their
     * default (<code>NULL</code>).
     */
    @Support({ DUCKDB, POSTGRES, YUGABYTEDB })
    @NotNull
    Field<R> construct();

    /**
     * Create a constructor call for UDTs.
     */
    @Support({ DUCKDB, POSTGRES, YUGABYTEDB })
    @NotNull
    Field<R> construct(Field<?>... args);
}
