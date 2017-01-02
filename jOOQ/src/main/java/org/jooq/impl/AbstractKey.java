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

import java.util.Arrays;
import java.util.List;

import org.jooq.Key;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * Common base class for <code>Key</code>'s
 *
 * @author Lukas Eder
 */
abstract class AbstractKey<R extends Record> implements Key<R> {

    /**
     * Generated UID
     */
    private static final long        serialVersionUID = 8176874459141379340L;

    private final String             name;
    private final Table<R>           table;
    private final TableField<R, ?>[] fields;


    @SafeVarargs

    AbstractKey(Table<R> table, TableField<R, ?>... fields) {
        this(table, null, fields);
    }


    @SafeVarargs

    AbstractKey(Table<R> table, String name, TableField<R, ?>... fields) {
        this.table = table;
        this.name = name;
        this.fields = fields;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }

    @Override
    public final List<TableField<R, ?>> getFields() {
        return Arrays.asList(fields);
    }

    @Override
    public final TableField<R, ?>[] getFieldsArray() {
        return fields;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((table == null) ? 0 : table.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractKey<?> other = (AbstractKey<?>) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (table == null) {
            if (other.table != null)
                return false;
        }
        else if (!table.equals(other.table))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return constraint().toString();
    }
}
