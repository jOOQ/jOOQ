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
package org.jooq.impl;

import org.jooq.Identity;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * @author Lukas Eder
 */
final class IdentityImpl<R extends Record, T> implements Identity<R, T> {

    /**
     * Generated UID
     */
    private static final long      serialVersionUID = 162853300137140844L;

    private final Table<R>         table;
    private final TableField<R, T> field;

    IdentityImpl(Table<R> table, TableField<R, T> field) {
        this.table = table;
        this.field = field;
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }

    @Override
    public final TableField<R, T> getField() {
        return field;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Identity) {
            return toString().equals(obj.toString());
        }

        return false;
    }

    @Override
    public String toString() {
        return field.toString();
    }
}
