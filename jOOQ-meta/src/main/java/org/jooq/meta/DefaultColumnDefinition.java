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

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import org.jooq.meta.jaxb.SyntheticIdentityType;
import org.jooq.meta.jaxb.SyntheticReadonlyColumnType;
import org.jooq.tools.JooqLogger;

/**
 * A base implementation for column definitions.
 *
 * @author Lukas Eder
 */
public class DefaultColumnDefinition
    extends AbstractTypedElementDefinition<TableDefinition>
    implements ColumnDefinition {

    private static final JooqLogger              log = JooqLogger.getLogger(DefaultColumnDefinition.class);
    private final int                            position;
    private final boolean                        identity;
    private final boolean                        readonly;
    private transient List<EmbeddableDefinition> replacedByEmbeddables;

    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        boolean identity,
        String comment
    ) {
        this(table, name, position, type, identity, false, comment);
    }

    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        boolean identity,
        boolean readonly,
        String comment
    ) {
        super(table, name, position, type, comment);

        this.position = position;
        this.identity = identity || isSyntheticIdentity(this);
        this.readonly = readonly || isSyntheticReadonlyColumn(this, this.identity);

        // [#6222] Copy the column's identity flag to the data type definition
        if (type instanceof DefaultDataTypeDefinition) { DefaultDataTypeDefinition dd = (DefaultDataTypeDefinition) type;
            dd.identity(this.identity);
            dd.readonly(this.readonly);
        }
    }

    @SuppressWarnings("unused")
    private static boolean isSyntheticIdentity(DefaultColumnDefinition column) {
        AbstractDatabase db = (AbstractDatabase) column.getDatabase();

        for (SyntheticIdentityType id : db.getConfiguredSyntheticIdentities()) {
            for (TableDefinition t : db.filter(singletonList(column.getContainer()), id.getTables())) {
                for (ColumnDefinition c : db.filter(singletonList(column), id.getFields())) {
                    log.info("Synthetic identity", column.getQualifiedName());
                    db.markUsed(id);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isSyntheticReadonlyColumn(DefaultColumnDefinition column, boolean identity) {

















        return false;
    }

    @Override
    public final int getPosition() {
        return position;
    }

    @Override
    public final UniqueKeyDefinition getPrimaryKey() {
        return getDatabase().getRelations().getPrimaryKey(this);
    }

    @Override
    public final List<UniqueKeyDefinition> getUniqueKeys() {
        return getDatabase().getRelations().getUniqueKeys(this);
    }

    @Override
    public final List<UniqueKeyDefinition> getKeys() {
        return getDatabase().getRelations().getKeys(this);
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys() {
        return getDatabase().getRelations().getForeignKeys(this);
    }

    @Override
    public final boolean isIdentity() {
        return identity;
    }

    @Override
    public final boolean isReadonly() {
        return readonly;
    }

















}
