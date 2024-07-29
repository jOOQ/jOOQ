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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import java.util.List;

/**
 * @author Lukas Eder
 */
public class DefaultManyToManyKeyDefinition extends AbstractConstraintDefinition implements ManyToManyKeyDefinition {

    private final ForeignKeyDefinition foreignKey1;
    private final UniqueKeyDefinition  uniqueKey;
    private final ForeignKeyDefinition foreignKey2;

    public DefaultManyToManyKeyDefinition(
        ForeignKeyDefinition foreignKey1,
        UniqueKeyDefinition uniqueKey,
        ForeignKeyDefinition foreignKey2
    ) {
        super(foreignKey2.getSchema(), foreignKey2.getTable(), foreignKey2.getName(), foreignKey2.enforced());

        this.foreignKey1 = foreignKey1;
        this.uniqueKey = uniqueKey;
        this.foreignKey2 = foreignKey2;
    }

    @Override
    public UniqueKeyDefinition getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public List<ColumnDefinition> getKeyColumns() {
        return uniqueKey.getKeyColumns();
    }

    @Override
    public ForeignKeyDefinition getForeignKey1() {
        return foreignKey1;
    }

    @Override
    public ForeignKeyDefinition getForeignKey2() {
        return foreignKey2;
    }
}
