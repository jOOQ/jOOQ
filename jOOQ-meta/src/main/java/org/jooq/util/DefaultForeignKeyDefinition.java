/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultForeignKeyDefinition extends AbstractDefinition implements ForeignKeyDefinition {

    private final List<ColumnDefinition> keyColumns;
    private final TableDefinition        table;
    private final UniqueKeyDefinition    uniqueKey;

    public DefaultForeignKeyDefinition(SchemaDefinition schema, String name, TableDefinition table,
        UniqueKeyDefinition uniqueKey) {

        super(schema.getDatabase(), schema, name, null);

        this.keyColumns = new ArrayList<ColumnDefinition>();
        this.table = table;
        this.uniqueKey = uniqueKey;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<Definition>();

        result.addAll(getSchema().getDefinitionPath());
        result.add(this);

        return result;
    }

    @Override
    public TableDefinition getKeyTable() {
        return table;
    }

    @Override
    public List<ColumnDefinition> getKeyColumns() {
        return keyColumns;
    }

    @Override
    public UniqueKeyDefinition getReferencedKey() {
        return uniqueKey;
    }

    @Override
    public TableDefinition getReferencedTable() {
        return uniqueKey.getTable();
    }

    @Override
    public List<ColumnDefinition> getReferencedColumns() {
        return uniqueKey.getKeyColumns();
    }

    @Override
    public int countSimilarReferences() {
        Set<String> keys = new HashSet<String>();

        for (ForeignKeyDefinition key : getDatabase().getRelations().getForeignKeys(table)) {
            if (key.getReferencedTable().equals(getReferencedTable())) {
                keys.add(key.getName());
            }
        }

        return keys.size();
    }
}
