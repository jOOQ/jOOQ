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

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;

/**
 * A base class for generated static references.
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly.
 *
 * @author Lukas Eder
 * @deprecated - [#6875] [#7158] - 3.11.0 - Please re-generate your code
 */
@Deprecated
@org.jooq.Internal
public abstract class AbstractKeys {

    /**
     * Factory method for indexes.
     *
     * @deprecated - [#6875] [#7158] - 3.11.0 - Please re-generate your code
     */
    @Deprecated
    public static Index createIndex(String name, Table<?> table, OrderField<?>[] sortFields, boolean unique) {
        return Internal.createIndex(name, table, sortFields, unique);
    }

    /**
     * Factory method for identities.
     *
     * @deprecated - [#6875] [#7158] - 3.11.0 - Please re-generate your code
     */
    @Deprecated
    public static <R extends Record, T> Identity<R, T> createIdentity(Table<R> table, TableField<R, T> field) {
        return Internal.createIdentity(table, field);
    }

    /**
     * Factory method for unique keys.
     *
     * @deprecated - [#6875] [#7158] - 3.11.0 - Please re-generate your code
     */
    @Deprecated

    @SafeVarargs

    public static <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, TableField<R, ?>... fields) {
        return Internal.createUniqueKey(table, fields);
    }

    /**
     * Factory method for unique keys.
     *
     * @deprecated - [#6875] [#7158] - 3.11.0 - Please re-generate your code
     */
    @Deprecated

    @SafeVarargs

    public static <R extends Record> UniqueKey<R> createUniqueKey(Table<R> table, String name, TableField<R, ?>... fields) {
        return Internal.createUniqueKey(table, name, fields);
    }

    /**
     * Factory method for foreign keys.
     *
     * @deprecated - [#6875] [#7158] - 3.11.0 - Please re-generate your code
     */
    @Deprecated

    @SafeVarargs

    public static <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, TableField<R, ?>... fields) {
        return Internal.createForeignKey(key, table, fields);
    }

    /**
     * Factory method for foreign keys.
     *
     * @deprecated - [#6875] [#7158] - 3.11.0 - Please re-generate your code
     */
    @Deprecated

    @SafeVarargs

    public static <R extends Record, U extends Record> ForeignKey<R, U> createForeignKey(UniqueKey<U> key, Table<R> table, String name, TableField<R, ?>... fields) {
        return Internal.createForeignKey(key, table, name, fields);
    }
}
