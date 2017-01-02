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
package org.jooq.util;

import java.util.ArrayList;
import java.util.Collections;
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

    private Map<Key, UniqueKeyDefinition>                                   primaryKeys      = new LinkedHashMap<Key, UniqueKeyDefinition>();
    private Map<Key, UniqueKeyDefinition>                                   uniqueKeys       = new LinkedHashMap<Key, UniqueKeyDefinition>();
    private Map<Key, ForeignKeyDefinition>                                  foreignKeys      = new LinkedHashMap<Key, ForeignKeyDefinition>();
    private Map<Key, CheckConstraintDefinition>                             checkConstraints = new LinkedHashMap<Key, CheckConstraintDefinition>();

    private transient Map<ColumnDefinition, UniqueKeyDefinition>            primaryKeysByColumn;
    private transient Map<ColumnDefinition, List<UniqueKeyDefinition>>      uniqueKeysByColumn;
    private transient Map<ColumnDefinition, List<ForeignKeyDefinition>>     foreignKeysByColumn;
    private transient Map<TableDefinition, List<CheckConstraintDefinition>> checkConstraintsByTable;

    public void addPrimaryKey(String keyName, ColumnDefinition column) {

        // [#2718] Column exclusions may hit primary key references. Ignore
        // such primary keys
        if (column == null) {
            log.info("Ignoring primary key", keyName + "(column unavailable)");
            return;
        }

	    if (log.isDebugEnabled()) {
	        log.debug("Adding primary key", keyName + " (" + column + ")");
	    }

	    UniqueKeyDefinition key = getUniqueKey(keyName, column, true);
        key.getKeyColumns().add(column);
	}

    public void addUniqueKey(String keyName, ColumnDefinition column) {

        // [#2718] Column exclusions may hit unique key references. Ignore
        // such unique keys
        if (column == null) {
            log.info("Ignoring unique key", keyName + "(column unavailable)");
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Adding unique key", keyName + " (" + column + ")");
        }

        UniqueKeyDefinition key = getUniqueKey(keyName, column, false);
        key.getKeyColumns().add(column);
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
        primaryKeys.put(mapKey, key);
        uniqueKeys.put(mapKey, key);
        log.info("Overriding primary key", "Table : " + key.getTable() +
                 ", previous key : " + ((old == null) ? "none" : old.getName()) +
                 ", new key : " + key.getName());
    }

    private UniqueKeyDefinition getUniqueKey(String keyName, ColumnDefinition column, boolean isPK) {
        UniqueKeyDefinition key = uniqueKeys.get(key(column, keyName));

        if (key == null) {
            key = new DefaultUniqueKeyDefinition(column.getSchema(), keyName, column.getContainer(), isPK);
            uniqueKeys.put(key(column, keyName), key);

            if (isPK) {
                primaryKeys.put(key(column, keyName), key);
            }
        }

        return key;
    }

    public void addForeignKey(
            String foreignKeyName,
            String uniqueKeyName,
            ColumnDefinition foreignKeyColumn,
            SchemaDefinition uniqueKeySchema) {


        // [#2718] Column exclusions may hit foreign key references. Ignore
        // such foreign keys
        if (foreignKeyColumn == null) {
            log.info("Ignoring foreign key", foreignKeyColumn + "(column unavailable)");
            return;
        }

        // [#1134] Prevent NPE's when a foreign key references a unique key
        // from another schema
        if (uniqueKeySchema == null) {
            log.info("Ignoring foreign key", foreignKeyName + " (" + foreignKeyColumn + ") referencing " + uniqueKeyName + " references a schema out of scope for jooq-meta: " + uniqueKeySchema);
            return;
        }

        log.info("Adding foreign key", foreignKeyName + " (" + foreignKeyColumn + ") referencing " + uniqueKeyName);

        ForeignKeyDefinition foreignKey = foreignKeys.get(key(foreignKeyColumn, foreignKeyName));

        if (foreignKey == null) {
            UniqueKeyDefinition uniqueKey = uniqueKeys.get(key(uniqueKeySchema, uniqueKeyName));

            // If the unique key is not loaded, ignore this foreign key
            if (uniqueKey != null) {
                foreignKey = new DefaultForeignKeyDefinition(foreignKeyColumn.getSchema(), foreignKeyName, foreignKeyColumn.getContainer(), uniqueKey);
                foreignKeys.put(key(foreignKeyColumn, foreignKeyName), foreignKey);

                uniqueKey.getForeignKeys().add(foreignKey);
            }
        }

        if (foreignKey != null) {
            foreignKey.getKeyColumns().add(foreignKeyColumn);
        }
	}

    public void addCheckConstraint(TableDefinition table, CheckConstraintDefinition constraint) {
        checkConstraints.put(key(table, constraint.getName()), constraint);
    }

	@Override
	public UniqueKeyDefinition getPrimaryKey(ColumnDefinition column) {
	    if (primaryKeysByColumn == null) {
	        primaryKeysByColumn = new LinkedHashMap<ColumnDefinition, UniqueKeyDefinition>();

	        for (UniqueKeyDefinition primaryKey : primaryKeys.values()) {
	            for (ColumnDefinition keyColumn : primaryKey.getKeyColumns()) {
	                primaryKeysByColumn.put(keyColumn, primaryKey);
	            }
	        }
	    }

	    return primaryKeysByColumn.get(column);
	}

	@Override
    public List<UniqueKeyDefinition> getUniqueKeys(ColumnDefinition column) {
	    if (uniqueKeysByColumn == null) {
	        uniqueKeysByColumn = new LinkedHashMap<ColumnDefinition, List<UniqueKeyDefinition>>();

	        for (UniqueKeyDefinition uniqueKey : uniqueKeys.values()) {
                for (ColumnDefinition keyColumn : uniqueKey.getKeyColumns()) {
                    List<UniqueKeyDefinition> list = uniqueKeysByColumn.get(keyColumn);

                    if (list == null) {
                        list = new ArrayList<UniqueKeyDefinition>();
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
        Set<UniqueKeyDefinition> result = new LinkedHashSet<UniqueKeyDefinition>();

        for (ColumnDefinition column : table.getColumns()) {
            result.addAll(getUniqueKeys(column));
        }

        return new ArrayList<UniqueKeyDefinition>(result);
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema) {
        Set<UniqueKeyDefinition> result = new LinkedHashSet<UniqueKeyDefinition>();

        for (TableDefinition table : schema.getDatabase().getTables(schema)) {
            result.addAll(getUniqueKeys(table));
        }

        return new ArrayList<UniqueKeyDefinition>(result);
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys() {
        return new ArrayList<UniqueKeyDefinition>(uniqueKeys.values());
    }

    @Override
	public List<ForeignKeyDefinition> getForeignKeys(ColumnDefinition column) {
        if (foreignKeysByColumn == null) {
            foreignKeysByColumn = new LinkedHashMap<ColumnDefinition, List<ForeignKeyDefinition>>();

            for (ForeignKeyDefinition foreignKey : foreignKeys.values()) {
                for (ColumnDefinition keyColumn : foreignKey.getKeyColumns()) {
                    List<ForeignKeyDefinition> list = foreignKeysByColumn.get(keyColumn);

                    if (list == null) {
                        list = new ArrayList<ForeignKeyDefinition>();
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
        Set<ForeignKeyDefinition> result = new LinkedHashSet<ForeignKeyDefinition>();

        for (ColumnDefinition column : table.getColumns()) {
            result.addAll(getForeignKeys(column));
        }

        return new ArrayList<ForeignKeyDefinition>(result);
    }

    @Override
    public List<CheckConstraintDefinition> getCheckConstraints(TableDefinition table) {
        if (checkConstraintsByTable == null) {
            checkConstraintsByTable = new LinkedHashMap<TableDefinition, List<CheckConstraintDefinition>>();

            for (CheckConstraintDefinition constraint : checkConstraints.values()) {
                List<CheckConstraintDefinition> list = checkConstraintsByTable.get(table);

                if (list == null) {
                    list = new ArrayList<CheckConstraintDefinition>();
                    checkConstraintsByTable.put(table, list);
                }

                list.add(constraint);
            }
        }

        List<CheckConstraintDefinition> list = checkConstraintsByTable.get(table);
        return list != null ? list : Collections.<CheckConstraintDefinition>emptyList();
    }

    private static Key key(Definition definition, String keyName) {
        return new Key(definition.getSchema(), keyName);
    }

    /**
     * A simple local wrapper for a key definition (schema + key name)
     */
    private static class Key {
        final SchemaDefinition schema;
        final String keyName;

        Key(SchemaDefinition schema, String keyName) {
            this.schema = schema;
            this.keyName = keyName;
        }

        @Override
        public String toString() {
            return "Key [schema=" + schema + ", keyName=" + keyName + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
            result = prime * result + ((schema == null) ? 0 : schema.hashCode());
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
            if (schema == null) {
                if (other.schema != null)
                    return false;
            }
            else if (!schema.equals(other.schema))
                return false;
            return true;
        }
    }
}
