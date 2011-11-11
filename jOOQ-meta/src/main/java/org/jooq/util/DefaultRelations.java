/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

public class DefaultRelations extends AbstractDefinition implements Relations {

    private static final JooqLogger                          log                 = JooqLogger.getLogger(DefaultRelations.class);

    private Map<String, ForeignKeyDefinition>                foreignKeys         = new LinkedHashMap<String, ForeignKeyDefinition>();
    private Map<String, UniqueKeyDefinition>                 primaryKeys         = new LinkedHashMap<String, UniqueKeyDefinition>();
    private Map<String, UniqueKeyDefinition>                 uniqueKeys          = new LinkedHashMap<String, UniqueKeyDefinition>();

    private Map<ColumnDefinition, ForeignKeyDefinition>      foreignKeysByColumn = new LinkedHashMap<ColumnDefinition, ForeignKeyDefinition>();
    private Map<ColumnDefinition, UniqueKeyDefinition>       primaryKeysByColumn = new LinkedHashMap<ColumnDefinition, UniqueKeyDefinition>();
    private Map<ColumnDefinition, List<UniqueKeyDefinition>> uniqueKeysByColumn  = new LinkedHashMap<ColumnDefinition, List<UniqueKeyDefinition>>();

	public DefaultRelations(Database database) {
		super(database, "", "");
	}

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
        UniqueKeyDefinition key = uniqueKeys.get(keyName);

        if (key == null) {
            key = new DefaultUniqueKeyDefinition(getDatabase(), keyName, column.getContainer());
            uniqueKeys.put(keyName, key);

            if (isPK) {
                primaryKeys.put(keyName, key);
            }
        }

        return key;
    }

    public void addForeignKey(String foreignKeyName, String uniqueKeyName, ColumnDefinition column) {
        if (log.isDebugEnabled()) {
            log.debug("Adding foreign key", foreignKeyName + " (" + column + ") referencing " + uniqueKeyName);
        }

        ForeignKeyDefinition foreignKey = foreignKeys.get(foreignKeyName);

        if (foreignKey == null) {
            UniqueKeyDefinition uniqueKey = uniqueKeys.get(uniqueKeyName);

            // If the unique key is not loaded, ignore this foreign key
            if (uniqueKey != null) {
                foreignKey = new DefaultForeignKeyDefinition(getDatabase(), foreignKeyName, column.getContainer(), uniqueKey);
                foreignKeys.put(foreignKeyName, foreignKey);

                uniqueKey.getForeignKeys().add(foreignKey);
            }
        }

        if (foreignKey != null) {
            foreignKey.getKeyColumns().add(column);
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
}
