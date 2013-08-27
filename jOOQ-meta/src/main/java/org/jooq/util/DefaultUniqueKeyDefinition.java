/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultUniqueKeyDefinition extends AbstractDefinition implements UniqueKeyDefinition {

    private final List<ForeignKeyDefinition> foreignKeys;
    private final List<ColumnDefinition>     keyColumns;
    private final TableDefinition            table;
    private final boolean                    isPrimaryKey;

    public DefaultUniqueKeyDefinition(SchemaDefinition schema, String name, TableDefinition table, boolean isPrimaryKey) {
        super(schema.getDatabase(), schema, name, null);

        this.foreignKeys = new ArrayList<ForeignKeyDefinition>();
        this.keyColumns = new ArrayList<ColumnDefinition>();
        this.table = table;
        this.isPrimaryKey = isPrimaryKey;
    }

    @Override
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        return Arrays.<Definition>asList(getSchema(), this);
    }

    @Override
    public List<ColumnDefinition> getKeyColumns() {
        return keyColumns;
    }

    @Override
    public List<ForeignKeyDefinition> getForeignKeys() {
        return foreignKeys;
    }

    @Override
    public TableDefinition getTable() {
        return table;
    }
}
