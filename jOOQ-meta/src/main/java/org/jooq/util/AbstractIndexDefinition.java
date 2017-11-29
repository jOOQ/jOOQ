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
package org.jooq.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukas Eder
 */
public abstract class AbstractIndexDefinition extends AbstractDefinition implements IndexDefinition {

    private final TableDefinition       table;
    private final boolean               unique;
    private List<IndexColumnDefinition> indexColumns;

    public AbstractIndexDefinition(SchemaDefinition schema, String name, TableDefinition table, boolean unique) {
        super(schema.getDatabase(), schema, name, "");

        this.table = table;
        this.unique = unique;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<Definition>();

        switch (getDialect().family()) {






            default:
                result.addAll(getSchema().getDefinitionPath());
        }

        result.add(this);
        return result;
    }

    @Override
    public TableDefinition getTable() {
        return table;
    }

    @Override
    public List<IndexColumnDefinition> getIndexColumns() {
        if (indexColumns == null) {
            indexColumns = getIndexColumns0();
        }

        return indexColumns;
    }

    protected abstract List<IndexColumnDefinition> getIndexColumns0();

    @Override
    public boolean isUnique() {
        return unique;
    }
}
