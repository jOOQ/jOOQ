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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A {@link UDT} or {@link Table}.
 *
 * @author Lukas Eder
 */
public interface RecordQualifier<R extends Record> extends Qualified, Fields {

    /**
     * Get the UDT package if this is a {@link UDT}, or <code>null</code> if it
     * is not a UDT, or if it is a schema level UDT defined outside of a
     * package.
     */
    @Nullable
    Package getPackage();

    /**
     * The record type produced by this {@link UDT} or {@link Table}.
     */
    @NotNull
    Class<? extends R> getRecordType();

    /**
     * The {@link UDT}'s or {@link Table}'s data type as known to the database.
     */
    @NotNull
    DataType<R> getDataType();

    /**
     * Create a new {@link Record} of this {@link UDT}'s or {@link Table}'s type.
     *
     * @see DSLContext#newRecord(Table)
     */
    @NotNull
    R newRecord();

}
