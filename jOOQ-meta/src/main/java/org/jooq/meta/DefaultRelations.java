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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jooq.tools.JooqLogger;

public class DefaultRelations implements Relations {

    private static final JooqLogger                                         log              = JooqLogger.getLogger(DefaultRelations.class);

    private final Map<Key, UniqueKeyDefinition>                             primaryKeys      = new LinkedHashMap<>();
    private final Map<Key, UniqueKeyDefinition>                             uniqueKeys       = new LinkedHashMap<>();
    private final Map<Key, ForeignKeyDefinition>                            foreignKeys      = new LinkedHashMap<>();
    private final Map<Key, CheckConstraintDefinition>                       checkConstraints = new LinkedHashMap<>();
    private final Set<Key>                                                  incompleteKeys   = new HashSet<>();

    private transient Map<ColumnDefinition, UniqueKeyDefinition>            primaryKeysByColumn;
    private transient Map<ColumnDefinition, List<UniqueKeyDefinition>>      uniqueKeysByColumn;
    private transient Map<ColumnDefinition, List<ForeignKeyDefinition>>     foreignKeysByColumn;
    private transient Map<TableDefinition, List<CheckConstraintDefinition>> checkConstraintsByTable;

    public void addPrimaryKey(String keyName, TableDefinition table, ColumnDefinition column) {
        Key key = key(table, keyName);

        // [#2718] Column exclusions may hit primary key references. Ignore
        // such primary keys
        if (column == null) {
            log.info("Ignoring primary key", keyName + " (column unavailable)");

            // [#7826] Prevent incomplete keys from being generated
            if (table != null) {
                incompleteKeys.add(key);
                primaryKeys.remove(key);
                uniqueKeys.remove(key);
            }

            return;
        }

        if (incompleteKeys.contains(key))
            return;

	    if (log.isDebugEnabled())
	        log.debug("Adding primary key", keyName + " (" + column + ")");

	    UniqueKeyDefinition result = getUniqueKey(keyName, table, column, true);
        result.getKeyColumns().add(column);
	}

    public void addUniqueKey(String keyName, TableDefinition table, ColumnDefinition column) {
        Key key = key(table, keyName);

        // [#2718] Column exclusions may hit unique key references. Ignore
        // such unique keys
        if (column == null) {
            log.info("Ignoring unique key", keyName + " (column unavailable)");

            // [#7826] Prevent incomplete keys from being generated
            if (table != null) {
                incompleteKeys.add(key);
                uniqueKeys.remove(key);
            }

            return;
        }

        if (incompleteKeys.contains(key))
            return;

        if (log.isDebugEnabled())
            log.debug("Adding unique key", keyName + " (" + column + ")");

        UniqueKeyDefinition result = getUniqueKey(keyName, table, column, false);
        result.getKeyColumns().add(column);
    }

    public void overridePrimaryKey(UniqueKeyDefinition key) {
        UniqueKeyDefinition old = null;

        // Remove the existing key from the column -> key mapping
        primaryKeysByColumn = null;
        uniqueKeysByColumn = null;

        // Remove the existing key from the primary key mapping (not from the unique key mapping!)
        Iterator<Entry<Key, UniqueKeyDefinition>> it = primaryKeys.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Key, UniqueKeyDefinition> entry = it.next();

            if (entry.getValue().getTable().equals(key.getTable())) {
                old = entry.getValue();
                it.remove();
                break;
            }
        }

        // Add the new primary key
        Key mapKey = key(key.getTable(), key.getName());
        if (incompleteKeys.contains(mapKey))
            return;

        primaryKeys.put(mapKey, key);
        uniqueKeys.put(mapKey, key);
        log.info("Overriding primary key", "Table : " + key.getTable() +
                 ", previous key : " + ((old == null) ? "none" : old.getName()) +
                 ", new key : " + key.getName());
    }

    private UniqueKeyDefinition getUniqueKey(String keyName, TableDefinition table, ColumnDefinition column, boolean isPK) {
        Key key = key(table, keyName);
        UniqueKeyDefinition result = uniqueKeys.get(key);

        if (result == null) {
            result = new DefaultUniqueKeyDefinition(column.getSchema(), keyName, table, isPK);
            uniqueKeys.put(key, result);

            if (isPK)
                primaryKeys.put(key, result);
        }

        return result;
    }

    public void addForeignKey(
        String foreignKeyName,
        TableDefinition foreignKeyTable,
        ColumnDefinition foreignKeyColumn,
        String uniqueKeyName,
        TableDefinition uniqueKeyTable) {


        // [#2718] Column exclusions may hit foreign key references. Ignore
        // such foreign keys
        Key key = key(foreignKeyTable, foreignKeyName);
        if (foreignKeyColumn == null) {
            log.info("Ignoring foreign key", foreignKeyColumn + " (column unavailable)");

            // [#7826] Prevent incomplete keys from being generated
            if (foreignKeyTable != null) {
                incompleteKeys.add(key);
                foreignKeys.remove(key);
            }

            return;
        }

        if (incompleteKeys.contains(key))
            return;

        // [#1134] Prevent NPE's when a foreign key references a unique key
        // from another schema
        if (uniqueKeyTable == null) {
            log.info("Ignoring foreign key", foreignKeyName + " (" + foreignKeyColumn + ") referencing " + uniqueKeyName + " references a schema out of scope for jooq-meta: " + uniqueKeyTable);
            return;
        }

        log.info("Adding foreign key", foreignKeyName + " (" + foreignKeyColumn + ") referencing " + uniqueKeyName);

        ForeignKeyDefinition foreignKey = foreignKeys.get(key);

        if (foreignKey == null) {
            UniqueKeyDefinition uniqueKey = uniqueKeys.get(key(uniqueKeyTable, uniqueKeyName));

            // If the unique key is not loaded, ignore this foreign key
            if (uniqueKey != null) {
                foreignKey = new DefaultForeignKeyDefinition(foreignKeyColumn.getSchema(), foreignKeyName, foreignKeyColumn.getContainer(), uniqueKey);
                foreignKeys.put(key, foreignKey);

                uniqueKey.getForeignKeys().add(foreignKey);
            }
        }

        if (foreignKey != null)
            foreignKey.getKeyColumns().add(foreignKeyColumn);
	}

    public void addCheckConstraint(TableDefinition table, CheckConstraintDefinition constraint) {
        checkConstraints.put(key(table, constraint.getName()), constraint);
    }

	@Override
	public UniqueKeyDefinition getPrimaryKey(ColumnDefinition column) {
	    if (primaryKeysByColumn == null) {
	        primaryKeysByColumn = new LinkedHashMap<>();

	        for (UniqueKeyDefinition primaryKey : primaryKeys.values())
	            for (ColumnDefinition keyColumn : primaryKey.getKeyColumns())
	                primaryKeysByColumn.put(keyColumn, primaryKey);
	    }

	    return primaryKeysByColumn.get(column);
	}

	@Override
    public List<UniqueKeyDefinition> getUniqueKeys(ColumnDefinition column) {
	    if (uniqueKeysByColumn == null) {
	        uniqueKeysByColumn = new LinkedHashMap<>();

	        for (UniqueKeyDefinition uniqueKey : uniqueKeys.values()) {
                for (ColumnDefinition keyColumn : uniqueKey.getKeyColumns()) {
                    List<UniqueKeyDefinition> list = uniqueKeysByColumn.get(keyColumn);

                    if (list == null) {
                        list = new ArrayList<>();
                        uniqueKeysByColumn.put(keyColumn, list);
                    }

                    list.add(uniqueKey);
                }
            }
        }

        List<UniqueKeyDefinition> list = uniqueKeysByColumn.get(column);
        return list != null ? list : Collections.<UniqueKeyDefinition>emptyList();
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys(TableDefinition table) {
        Set<UniqueKeyDefinition> result = new LinkedHashSet<>();

        for (ColumnDefinition column : table.getColumns())
            result.addAll(getUniqueKeys(column));

        return new ArrayList<>(result);
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema) {
        Set<UniqueKeyDefinition> result = new LinkedHashSet<>();

        for (TableDefinition table : schema.getDatabase().getTables(schema))
            result.addAll(getUniqueKeys(table));

        return new ArrayList<>(result);
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys() {
        return new ArrayList<>(uniqueKeys.values());
    }

    @Override
	public List<ForeignKeyDefinition> getForeignKeys(ColumnDefinition column) {
        if (foreignKeysByColumn == null) {
            foreignKeysByColumn = new LinkedHashMap<>();

            for (ForeignKeyDefinition foreignKey : foreignKeys.values()) {
                for (ColumnDefinition keyColumn : foreignKey.getKeyColumns()) {
                    List<ForeignKeyDefinition> list = foreignKeysByColumn.get(keyColumn);

                    if (list == null) {
                        list = new ArrayList<>();
                        foreignKeysByColumn.put(keyColumn, list);
                    }

                    list.add(foreignKey);
                }
            }
        }


        List<ForeignKeyDefinition> list = foreignKeysByColumn.get(column);
        return list != null ? list : Collections.<ForeignKeyDefinition>emptyList();
	}

    @Override
    public List<ForeignKeyDefinition> getForeignKeys(TableDefinition table) {
        Set<ForeignKeyDefinition> result = new LinkedHashSet<>();

        for (ColumnDefinition column : table.getColumns())
            result.addAll(getForeignKeys(column));

        return new ArrayList<>(result);
    }

    @Override
    public List<CheckConstraintDefinition> getCheckConstraints(TableDefinition table) {
        if (checkConstraintsByTable == null) {
            checkConstraintsByTable = new LinkedHashMap<>();

            for (Map.Entry<Key, CheckConstraintDefinition> entry : checkConstraints.entrySet()) {
                List<CheckConstraintDefinition> list = checkConstraintsByTable.get(entry.getKey().table);

                if (list == null) {
                    list = new ArrayList<>();
                    checkConstraintsByTable.put(entry.getKey().table, list);
                }

                list.add(entry.getValue());
            }
        }

        List<CheckConstraintDefinition> list = checkConstraintsByTable.get(table);
        return list != null ? list : Collections.<CheckConstraintDefinition>emptyList();
    }

    private static Key key(TableDefinition definition, String keyName) {
        return new Key(definition, keyName);
    }

    /**
     * A simple local wrapper for a key definition (table + key name)
     */
    private static class Key {
        final TableDefinition table;
        final String keyName;

        Key(TableDefinition table, String keyName) {
            this.table = table;
            this.keyName = keyName;
        }

        @Override
        public String toString() {
            return "Key [table=" + table + ", keyName=" + keyName + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
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
            Key other = (Key) obj;
            if (keyName == null) {
                if (other.keyName != null)
                    return false;
            }
            else if (!keyName.equals(other.keyName))
                return false;
            if (table == null) {
                if (other.table != null)
                    return false;
            }
            else if (!table.equals(other.table))
                return false;
            return true;
        }
    }
}
