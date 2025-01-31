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
package org.jooq.meta;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jooq.impl.QOM.ForeignKeyRule;
import org.jooq.tools.JooqLogger;

public class DefaultRelations implements Relations {

    private static final JooqLogger                                         log              = JooqLogger.getLogger(DefaultRelations.class);

    private final Map<Key, UniqueKeyDefinition>                             primaryKeys      = new LinkedHashMap<>();
    private final Map<Key, UniqueKeyDefinition>                             uniqueKeys       = new LinkedHashMap<>();
    private final Map<Key, UniqueKeyDefinition>                             keys             = new LinkedHashMap<>();
    private final Map<Key, ForeignKeyDefinition>                            foreignKeys      = new LinkedHashMap<>();
    private final Map<Key, CheckConstraintDefinition>                       checkConstraints = new LinkedHashMap<>();
    private final Set<Key>                                                  incompleteKeys   = new HashSet<>();

    private transient Map<ColumnDefinition, UniqueKeyDefinition>            primaryKeysByColumn;
    private transient Map<ColumnDefinition, List<UniqueKeyDefinition>>      uniqueKeysByColumn;
    private transient Map<ColumnDefinition, List<UniqueKeyDefinition>>      keysByColumn;
    private transient Map<ColumnDefinition, List<ForeignKeyDefinition>>     foreignKeysByColumn;
    private transient Map<TableDefinition, List<UniqueKeyDefinition>>       uniqueKeysByTable;
    private transient Map<TableDefinition, List<UniqueKeyDefinition>>       keysByTable;
    private transient Map<TableDefinition, List<ForeignKeyDefinition>>      foreignKeysByTable;
    private transient Map<TableDefinition, List<CheckConstraintDefinition>> checkConstraintsByTable;

    public void addPrimaryKey(String keyName, TableDefinition table, ColumnDefinition column) {
        addPrimaryKey(keyName, table, column, true);
    }

    public void addPrimaryKey(String keyName, TableDefinition table, ColumnDefinition column, boolean enforced) {
        Key key = key(table, keyName);

        // [#2718] Column exclusions may hit primary key references. Ignore
        // such primary keys
        if (column == null) {
            if (log.isDebugEnabled())
                log.debug("Ignoring primary key", keyName + " (column unavailable)");

            // [#7826] Prevent incomplete keys from being generated
            if (table != null) {
                incompleteKeys.add(key);
                primaryKeys.remove(key);
                keys.remove(key);
            }

            return;
        }

        if (incompleteKeys.contains(key))
            return;

	    if (log.isDebugEnabled())
	        log.debug("Adding primary key", keyName + " (" + column + ")");

	    UniqueKeyDefinition result = getUniqueKey(keyName, table, column, true, enforced);
        result.getKeyColumns().add(column);
	}

    public void addUniqueKey(String keyName, TableDefinition table, ColumnDefinition column) {
        addUniqueKey(keyName, table, column, true);
    }

    public void addUniqueKey(String keyName, TableDefinition table, ColumnDefinition column, boolean enforced) {
        Key key = key(table, keyName);

        // [#2718] Column exclusions may hit unique key references. Ignore
        // such unique keys
        if (column == null) {
            if (log.isDebugEnabled())
                log.debug("Ignoring unique key", keyName + " (column unavailable)");

            // [#7826] Prevent incomplete keys from being generated
            if (table != null) {
                incompleteKeys.add(key);
                uniqueKeys.remove(key);
                keys.remove(key);
            }

            return;
        }

        if (incompleteKeys.contains(key))
            return;

        if (log.isDebugEnabled())
            log.debug("Adding unique key", keyName + " (" + column + ")");

        UniqueKeyDefinition result = getUniqueKey(keyName, table, column, false, enforced);
        result.getKeyColumns().add(column);
    }

    public void overridePrimaryKey(UniqueKeyDefinition key) {
        UniqueKeyDefinition old = null;

        // Remove the existing key from the column -> key mapping
        primaryKeysByColumn = null;
        uniqueKeysByColumn = null;
        uniqueKeysByTable = null;
        keysByColumn = null;
        keysByTable = null;
        foreignKeysByColumn = null;
        foreignKeysByTable = null;

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
        keys.put(mapKey, key);
        uniqueKeys.remove(mapKey);

        if (old != null)
            uniqueKeys.put(mapKey, old);

        if (log.isDebugEnabled())
            log.debug("Overriding primary key", "Table : " + key.getTable() +
                     ", previous key : " + ((old == null) ? "none" : old.getName()) +
                     ", new key : " + key.getName());
    }

    private UniqueKeyDefinition getUniqueKey(String keyName, TableDefinition table, ColumnDefinition column, boolean isPK, boolean enforced) {
        Key key = key(table, keyName);
        UniqueKeyDefinition result = keys.get(key);

        if (result == null) {
            result = new DefaultUniqueKeyDefinition(column.getSchema(), keyName, table, isPK, enforced);
            keys.put(key, result);

            if (isPK)
                primaryKeys.put(key, result);
            else
                uniqueKeys.put(key, result);
        }

        return result;
    }

    public void addForeignKey(
        String foreignKeyName,
        TableDefinition foreignKeyTable,
        ColumnDefinition foreignKeyColumn,
        String uniqueKeyName,
        TableDefinition uniqueKeyTable
    ) {
        addForeignKey(foreignKeyName, foreignKeyTable, foreignKeyColumn, uniqueKeyName, uniqueKeyTable, true);
    }

    public void addForeignKey(
        String foreignKeyName,
        TableDefinition foreignKeyTable,
        ColumnDefinition foreignKeyColumn,
        String uniqueKeyName,
        TableDefinition uniqueKeyTable,
        boolean enforced
    ) {
        addForeignKey(
            foreignKeyName,
            foreignKeyTable,
            foreignKeyColumn,
            uniqueKeyName,
            uniqueKeyTable,
            enforced,
            null,
            null
        );
    }

    public void addForeignKey(
        String foreignKeyName,
        TableDefinition foreignKeyTable,
        ColumnDefinition foreignKeyColumn,
        String uniqueKeyName,
        TableDefinition uniqueKeyTable,
        boolean enforced,
        ForeignKeyRule deleteRule,
        ForeignKeyRule updateRule
    ) {
        UniqueKeyDefinition uk = keys.get(key(uniqueKeyTable, uniqueKeyName));
        Key key = key(foreignKeyTable, foreignKeyName);

        if (uk == null) {
            if (log.isDebugEnabled())
                log.debug("Ignoring foreign key", uniqueKeyName + " (unique key unavailable)");

            // [#7826] Prevent incomplete keys from being generated
            if (foreignKeyTable != null) {

                incompleteKeys.add(key);
                foreignKeys.remove(key);
            }

            return;
        }

        addForeignKey(
            foreignKeyName,
            foreignKeyTable,
            foreignKeyColumn,
            uniqueKeyName,
            uniqueKeyTable,
            getNextUkColumn(key, uk),
            enforced,
            deleteRule,
            updateRule
        );
    }

    private final Map<Key, Integer> nextUkColumnIndex = new HashMap<>();

    private ColumnDefinition getNextUkColumn(Key key, UniqueKeyDefinition uk) {
        Integer index = nextUkColumnIndex.get(key);

        if (index == null)
            nextUkColumnIndex.put(key, index = 0);
        else
            nextUkColumnIndex.put(key, index = index + 1);

        return index < uk.getKeyColumns().size() ? uk.getKeyColumns().get(index) : null;
    }

    public void addForeignKey(
        String foreignKeyName,
        TableDefinition foreignKeyTable,
        ColumnDefinition foreignKeyColumn,
        String uniqueKeyName,
        TableDefinition uniqueKeyTable,
        Integer positionInUniqueKey,
        boolean enforced
    ) {
        addForeignKey(
            foreignKeyName,
            foreignKeyTable,
            foreignKeyColumn,
            uniqueKeyName,
            uniqueKeyTable,
            positionInUniqueKey,
            enforced,
            null,
            null
        );
    }

    public void addForeignKey(
        String foreignKeyName,
        TableDefinition foreignKeyTable,
        ColumnDefinition foreignKeyColumn,
        String uniqueKeyName,
        TableDefinition uniqueKeyTable,
        Integer positionInUniqueKey,
        boolean enforced,
        ForeignKeyRule deleteRule,
        ForeignKeyRule updateRule
    ) {
        if (positionInUniqueKey == null) {
            addForeignKey(
                foreignKeyName,
                foreignKeyTable,
                foreignKeyColumn,
                uniqueKeyName,
                uniqueKeyTable,
                enforced,
                deleteRule,
                updateRule
            );
        }
        else {
            UniqueKeyDefinition uniqueKey = keys.get(key(uniqueKeyTable, uniqueKeyName));

            if (uniqueKey != null) {
                addForeignKey(
                    foreignKeyName,
                    foreignKeyTable,
                    foreignKeyColumn,
                    uniqueKeyName,
                    uniqueKeyTable,
                    uniqueKey.getKeyColumns().get(positionInUniqueKey - 1),
                    enforced,
                    deleteRule,
                    updateRule
                );
            }
        }
    }

    public void addForeignKey(
        String foreignKeyName,
        TableDefinition foreignKeyTable,
        ColumnDefinition foreignKeyColumn,
        String uniqueKeyName,
        TableDefinition uniqueKeyTable,
        ColumnDefinition uniqueKeyColumn,
        boolean enforced
    ) {
        addForeignKey(
            foreignKeyName,
            foreignKeyTable,
            foreignKeyColumn,
            uniqueKeyName,
            uniqueKeyTable,
            uniqueKeyColumn,
            enforced,
            null,
            null
        );
    }

    public void addForeignKey(
        String foreignKeyName,
        TableDefinition foreignKeyTable,
        ColumnDefinition foreignKeyColumn,
        String uniqueKeyName,
        TableDefinition uniqueKeyTable,
        ColumnDefinition uniqueKeyColumn,
        boolean enforced,
        ForeignKeyRule deleteRule,
        ForeignKeyRule updateRule
    ) {
        // [#2718] Column exclusions may hit foreign key references. Ignore
        // such foreign keys
        Key key = key(foreignKeyTable, foreignKeyName);
        if (foreignKeyColumn == null || uniqueKeyColumn == null) {
            if (log.isDebugEnabled())
                log.debug("Ignoring foreign key", foreignKeyColumn + " referencing " + uniqueKeyColumn + " (column unavailable)");

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
            if (log.isDebugEnabled())
                log.debug("Ignoring foreign key", foreignKeyName + " (" + foreignKeyColumn + ") referencing " + uniqueKeyName + " (" + uniqueKeyColumn + ") references a schema out of scope for jooq-meta: " + uniqueKeyTable);

            return;
        }

        if (log.isDebugEnabled())
            log.debug("Adding foreign key", foreignKeyName + " (" + foreignKeyColumn + ") referencing " + uniqueKeyName + " (" + uniqueKeyColumn + ")");

        ForeignKeyDefinition foreignKey = foreignKeys.get(key);

        if (foreignKey == null) {
            UniqueKeyDefinition uniqueKey = keys.get(key(uniqueKeyTable, uniqueKeyName));

            // If the unique key is not loaded, ignore this foreign key
            if (uniqueKey != null) {
                foreignKey = new DefaultForeignKeyDefinition(
                    foreignKeyColumn.getSchema(),
                    foreignKeyName,
                    foreignKeyColumn.getContainer(),
                    uniqueKey,
                    enforced,
                    deleteRule,
                    updateRule
                );

                foreignKeys.put(key, foreignKey);
                uniqueKey.getForeignKeys().add(foreignKey);
            }
        }

        if (foreignKey != null) {
            foreignKey.getKeyColumns().add(foreignKeyColumn);
            foreignKey.getReferencedColumns().add(uniqueKeyColumn);
        }
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

	        for (UniqueKeyDefinition uniqueKey : uniqueKeys.values())
                for (ColumnDefinition keyColumn : uniqueKey.getKeyColumns())
                    uniqueKeysByColumn.computeIfAbsent(keyColumn, c -> new ArrayList<>()).add(uniqueKey);

	        uniqueKeysByColumn.forEach((t, l) -> t.getDatabase().sort(l));
        }

	    return nonNull(uniqueKeysByColumn, column);
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys(TableDefinition table) {
        if (uniqueKeysByTable == null)
            uniqueKeysByTable = initByTable(uniqueKeys);

        return nonNull(uniqueKeysByTable, table);
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema) {
        Set<UniqueKeyDefinition> result = new LinkedHashSet<>();

        for (TableDefinition table : schema.getDatabase().getTables(schema))
            result.addAll(getUniqueKeys(table));

        return sort(new ArrayList<>(result));
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys() {
        return sort(new ArrayList<>(uniqueKeys.values()));
    }

    @Override
    public List<UniqueKeyDefinition> getKeys(ColumnDefinition column) {
        if (keysByColumn == null) {
            keysByColumn = new LinkedHashMap<>();

            for (UniqueKeyDefinition uniqueKey : keys.values())
                for (ColumnDefinition keyColumn : uniqueKey.getKeyColumns())
                    keysByColumn.computeIfAbsent(keyColumn, c -> new ArrayList<>()).add(uniqueKey);

            keysByColumn.forEach((t, l) -> t.getDatabase().sort(l));
        }

        return nonNull(keysByColumn, column);
    }

    @Override
    public List<UniqueKeyDefinition> getKeys(TableDefinition table) {
        if (keysByTable == null)
            keysByTable = initByTable(keys);

        return nonNull(keysByTable, table);
    }

    @Override
    public List<UniqueKeyDefinition> getKeys(SchemaDefinition schema) {
        Set<UniqueKeyDefinition> result = new LinkedHashSet<>();

        for (TableDefinition table : schema.getDatabase().getTables(schema))
            result.addAll(getKeys(table));

        return sort(new ArrayList<>(result));
    }

    @Override
    public List<UniqueKeyDefinition> getKeys() {
        return sort(new ArrayList<>(keys.values()));
    }

    @Override
	public List<ForeignKeyDefinition> getForeignKeys(ColumnDefinition column) {
        if (foreignKeysByColumn == null) {
            foreignKeysByColumn = new LinkedHashMap<>();

            for (ForeignKeyDefinition foreignKey : foreignKeys.values())
                for (ColumnDefinition keyColumn : foreignKey.getKeyColumns())
                    foreignKeysByColumn.computeIfAbsent(keyColumn, c -> new ArrayList<>()).add(foreignKey);

            foreignKeysByColumn.forEach((t, l) -> t.getDatabase().sort(l));
        }

        return nonNull(foreignKeysByColumn, column);
	}

    @Override
    public List<ForeignKeyDefinition> getForeignKeys(TableDefinition table) {
        if (foreignKeysByTable == null)
            foreignKeysByTable = initByTable(foreignKeys);

        return nonNull(foreignKeysByTable, table);
    }

    @Override
    public List<CheckConstraintDefinition> getCheckConstraints(TableDefinition table) {
        if (checkConstraintsByTable == null)
            checkConstraintsByTable = initByTable(checkConstraints);

        return nonNull(checkConstraintsByTable, table);
    }

    private static <D extends Definition> Map<TableDefinition, List<D>> initByTable(
        Map<Key, D> map
    ) {
        Map<TableDefinition, List<D>> result = new LinkedHashMap<>();
        map.forEach((k, v) -> result.computeIfAbsent(k.table, t -> new ArrayList<>()).add(v));
        result.forEach((t, l) -> t.getDatabase().sort(l));

        return result;
    }

    private static <K extends Definition, V extends Definition> List<V> nonNull(Map<K, List<V>> map, K key) {
        List<V> list = map.get(key);
        return list != null ? list : emptyList();
    }

    private static <D extends Definition> List<D> sort(List<D> list) {
        return list.isEmpty() ? list : list.get(0).getDatabase().sort(list);
    }

    private static Key key(TableDefinition definition, String keyName) {
        return new Key(definition, keyName);
    }

    /**
     * A simple local wrapper for a key definition (table + key name)
     */
    private static record Key(TableDefinition table, String keyName) {}
}
