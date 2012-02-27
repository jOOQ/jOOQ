/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.tools.JooqLogger;

public class DefaultRelations implements Relations {

    private static final JooqLogger                          log                 = JooqLogger.getLogger(DefaultRelations.class);

    private Map<Key, ForeignKeyDefinition>                   foreignKeys         = new LinkedHashMap<Key, ForeignKeyDefinition>();
    private Map<Key, UniqueKeyDefinition>                    primaryKeys         = new LinkedHashMap<Key, UniqueKeyDefinition>();
    private Map<Key, UniqueKeyDefinition>                    uniqueKeys          = new LinkedHashMap<Key, UniqueKeyDefinition>();

    private Map<ColumnDefinition, ForeignKeyDefinition>      foreignKeysByColumn = new LinkedHashMap<ColumnDefinition, ForeignKeyDefinition>();
    private Map<ColumnDefinition, UniqueKeyDefinition>       primaryKeysByColumn = new LinkedHashMap<ColumnDefinition, UniqueKeyDefinition>();
    private Map<ColumnDefinition, List<UniqueKeyDefinition>> uniqueKeysByColumn  = new LinkedHashMap<ColumnDefinition, List<UniqueKeyDefinition>>();

	public void addPrimaryKey(String keyName, ColumnDefinition column) {
	    if (log.isDebugEnabled()) {
	        log.debug("Adding primary key", keyName + " (" + column + ")");
	    }

	    UniqueKeyDefinition key = getUniqueKey(keyName, column, true);
        key.getKeyColumns().add(column);
	}

    public void addUniqueKey(String keyName, ColumnDefinition column) {
        if (log.isDebugEnabled()) {
            log.debug("Adding unique key", keyName + " (" + column + ")");
        }

        UniqueKeyDefinition key = getUniqueKey(keyName, column, false);
        key.getKeyColumns().add(column);
    }

    private UniqueKeyDefinition getUniqueKey(String keyName, ColumnDefinition column, boolean isPK) {
        UniqueKeyDefinition key = uniqueKeys.get(key(column, keyName));

        if (key == null) {
            key = new DefaultUniqueKeyDefinition(column.getSchema(), keyName, column.getContainer());
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

        // [#1134] Prevent NPE's when a foreign key references a unique key
        // from another schema
        if (uniqueKeySchema == null) {
            log.warn("Unused foreign key", foreignKeyName + " (" + foreignKeyColumn + ") referencing " + uniqueKeyName + " references a schema out of scope for jooq-meta");
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

	@Override
	public UniqueKeyDefinition getPrimaryKey(ColumnDefinition column) {
	    if (!primaryKeysByColumn.containsKey(column)) {
	        UniqueKeyDefinition key = null;

	        for (UniqueKeyDefinition primaryKey : primaryKeys.values()) {
	            if (primaryKey.getKeyColumns().contains(column)) {
	                key = primaryKey;
	                break;
	            }
	        }

	        primaryKeysByColumn.put(column, key);
	    }

	    return primaryKeysByColumn.get(column);
	}

	@Override
    public List<UniqueKeyDefinition> getUniqueKeys(ColumnDefinition column) {
	    if (!uniqueKeysByColumn.containsKey(column)) {
            List<UniqueKeyDefinition> list = new ArrayList<UniqueKeyDefinition>();

            for (UniqueKeyDefinition uniqueKey : uniqueKeys.values()) {
                if (uniqueKey.getKeyColumns().contains(column)) {
                    list.add(uniqueKey);
                }
            }

            uniqueKeysByColumn.put(column, list);
        }

        return uniqueKeysByColumn.get(column);
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
	public ForeignKeyDefinition getForeignKey(ColumnDefinition column) {
        if (!foreignKeysByColumn.containsKey(column)) {
            ForeignKeyDefinition key = null;

            for (ForeignKeyDefinition foreignKey : foreignKeys.values()) {
                if (foreignKey.getKeyColumns().contains(column)) {
                    key = foreignKey;
                    break;
                }
            }

            foreignKeysByColumn.put(column, key);
        }

        return foreignKeysByColumn.get(column);
	}

    @Override
    public List<ForeignKeyDefinition> getForeignKeys(TableDefinition table) {
        Set<ForeignKeyDefinition> result = new LinkedHashSet<ForeignKeyDefinition>();

        for (ColumnDefinition column : table.getColumns()) {
            ForeignKeyDefinition foreignKey = getForeignKey(column);

            if (foreignKey != null) {
                result.add(foreignKey);
            }
        }

        return new ArrayList<ForeignKeyDefinition>(result);
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
