/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.util;

import static java.util.Collections.singletonList;

import java.util.List;

import org.jooq.tools.JooqLogger;

/**
 * A base implementation for column definitions.
 *
 * @author Lukas Eder
 */
public class DefaultColumnDefinition
    extends AbstractTypedElementDefinition<TableDefinition>
    implements ColumnDefinition {

    private static final JooqLogger log = JooqLogger.getLogger(DefaultColumnDefinition.class);
    private final int               position;
    private final boolean           isIdentity;

    public DefaultColumnDefinition(TableDefinition table, String name, int position, DataTypeDefinition type,
        boolean isIdentity, String comment) {

        super(table, name, position, type, comment);

        this.position = position;
        this.isIdentity = isIdentity || isSyntheticIdentity(this);
    }

    private static boolean isSyntheticIdentity(DefaultColumnDefinition column) {
        AbstractDatabase db = (AbstractDatabase) column.getDatabase();
        String[] syntheticIdentities = db.getSyntheticIdentities();
        boolean match = !db.filterExcludeInclude(singletonList(column), null, syntheticIdentities, db.getFilters()).isEmpty();

        if (match)
            log.info("Synthetic Identity: " + column.getQualifiedName());

        return match;
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
    public List<UniqueKeyDefinition> getUniqueKeys() {
        return getDatabase().getRelations().getUniqueKeys(this);
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys() {
        return getDatabase().getRelations().getForeignKeys(this);
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
