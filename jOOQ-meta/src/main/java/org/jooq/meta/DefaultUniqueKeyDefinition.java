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

import java.util.ArrayList;
import java.util.List;

import org.jooq.tools.JooqLogger;

public class DefaultUniqueKeyDefinition extends AbstractConstraintDefinition implements UniqueKeyDefinition {

    private static final JooqLogger          log = JooqLogger.getLogger(DefaultUniqueKeyDefinition.class);
    private final List<ForeignKeyDefinition> foreignKeys;
    private final List<ColumnDefinition>     keyColumns;
    private final boolean                    isPrimaryKey;
    private transient boolean                resolvedUKCalculated;
    private transient UniqueKeyDefinition    resolvedUK;

    public DefaultUniqueKeyDefinition(SchemaDefinition schema, String name, TableDefinition table, boolean isPrimaryKey) {
        this(schema, name, table, isPrimaryKey, true);
    }

    public DefaultUniqueKeyDefinition(SchemaDefinition schema, String name, TableDefinition table, boolean isPrimaryKey, boolean enforced) {
        super(schema, table, name, enforced);

        this.foreignKeys = new ArrayList<>();
        this.keyColumns = new ArrayList<>();
        this.isPrimaryKey = isPrimaryKey;
    }

    @Override
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    @Override
    public List<ColumnDefinition> getKeyColumns() {
        return keyColumns;
    }

    @Override
    public List<ForeignKeyDefinition> getForeignKeys() {
        return getDatabase().sort(foreignKeys);
    }

    @Override
    public final UniqueKeyDefinition resolveReferencedKey() {
        if (!resolvedUKCalculated) {
            resolvedUKCalculated = true;

            ForeignKeyDefinition candidate = null;
            for (ForeignKeyDefinition fk : getTable().getForeignKeys()) {
                if (keyColumns.equals(fk.getKeyColumns())) {
                    if (candidate == null) {
                        candidate = fk;
                    }
                    else {
                        log.info("Cannot resolve key", (isPrimaryKey ? "Primary" : "Unique") + " key coincides with at least two foreign keys: " + candidate + " and " + fk);
                        return null;
                    }
                }
            }

            resolvedUK = candidate == null ? this : candidate.resolveReferencedKey();
        }

        return resolvedUK;
    }
}
