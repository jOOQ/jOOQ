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

import java.util.List;

/**
 * A base implementation for column definitions.
 *
 * @author Lukas Eder
 */
public class DefaultColumnDefinition
    extends AbstractTypedElementDefinition<TableDefinition>
    implements ColumnDefinition {

    private final int                 position;
    private final DataTypeDefinition  underlying;
    private final boolean             isIdentity;
    private final boolean             nullable;

    private DataTypeDefinition        type;
    private boolean                   primaryKeyLoaded;
    private UniqueKeyDefinition       primaryKey;
    private List<UniqueKeyDefinition> uniqueKeys;
    private boolean                   foreignKeyLoaded;
    private ForeignKeyDefinition      foreignKey;

    /**
     * @deprecated - 2.1.0 - Use the other constructor instead
     */
    @Deprecated
    public DefaultColumnDefinition(TableDefinition table, String name, int position, DataTypeDefinition type,
        boolean isIdentity, String comment) {
        this(table, name, position, type, true, isIdentity, comment);
    }

    public DefaultColumnDefinition(TableDefinition table, String name, int position, DataTypeDefinition type,
        boolean nullable, boolean isIdentity, String comment) {

        super(table, name, position, type, comment);

        this.position = position;
        this.underlying = type;
        this.isIdentity = isIdentity;
        this.nullable = nullable;
    }

    @Override
    public final int getPosition() {
        return position;
    }

    @Override
    public final DataTypeDefinition getType() {

        // Lazy initialise
        if (type == null) {
            ForeignKeyDefinition fk = getDatabase().getRelations().getForeignKey(this);

            // If this is a foreign key to a master data type
            if (fk != null) {
                TableDefinition referencedTable = fk.getReferencedTable();

                if (referencedTable instanceof MasterDataTableDefinition) {
                    type = new MasterDataTypeDefinition(referencedTable, underlying);
                }
            }

            // Else...
            if (type == null) {
                type = super.getType();
            }
        }

        return type;
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
    public final ForeignKeyDefinition getForeignKey() {
        if (!foreignKeyLoaded) {
            foreignKeyLoaded = true;
            foreignKey = getDatabase().getRelations().getForeignKey(this);
        }

        return foreignKey;
    }

    @Override
    public final boolean isIdentity() {
        return isIdentity;
    }

    @Override
    public final boolean isNullable() {
        return nullable;
    }
}
