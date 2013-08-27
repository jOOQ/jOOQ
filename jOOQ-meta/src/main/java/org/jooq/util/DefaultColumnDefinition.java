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

import java.util.List;

/**
 * A base implementation for column definitions.
 *
 * @author Lukas Eder
 */
public class DefaultColumnDefinition
    extends AbstractTypedElementDefinition<TableDefinition>
    implements ColumnDefinition {

    private final int                  position;
    private final boolean              isIdentity;

    private boolean                    primaryKeyLoaded;
    private UniqueKeyDefinition        primaryKey;
    private List<UniqueKeyDefinition>  uniqueKeys;
    private boolean                    foreignKeyLoaded;
    private List<ForeignKeyDefinition> foreignKey;

    public DefaultColumnDefinition(TableDefinition table, String name, int position, DataTypeDefinition type,
        boolean isIdentity, String comment) {

        super(table, name, position, type, comment);

        this.position = position;
        this.isIdentity = isIdentity;
    }

    @Override
    public final int getPosition() {
        return position;
    }

    @Override
    public final UniqueKeyDefinition getPrimaryKey() {
        if (!primaryKeyLoaded) {
            primaryKeyLoaded = true;
            primaryKey = getDatabase().getRelations().getPrimaryKey(this);
        }

        return primaryKey;
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys() {
        if (uniqueKeys == null) {
            uniqueKeys = getDatabase().getRelations().getUniqueKeys(this);
        }

        return uniqueKeys;
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys() {
        if (!foreignKeyLoaded) {
            foreignKeyLoaded = true;
            foreignKey = getDatabase().getRelations().getForeignKeys(this);
        }

        return foreignKey;
    }

    @Override
    public final boolean isIdentity() {
        return isIdentity;
    }

    @Override
    public final boolean isNullable() {
        return getType().isNullable();
    }
}
