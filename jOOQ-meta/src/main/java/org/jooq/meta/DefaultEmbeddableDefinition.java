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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukas Eder
 */
public class DefaultEmbeddableDefinition
    extends AbstractElementContainerDefinition<EmbeddableColumnDefinition>
    implements EmbeddableDefinition {

    private final List<String>                     columnNames;
    private final TableDefinition                  table;
    private final List<EmbeddableColumnDefinition> embeddableColumns;

    public DefaultEmbeddableDefinition(String name, List<String> columnNames, TableDefinition table, List<ColumnDefinition> columns) {
        super(table.getSchema(), name, "");

        this.columnNames = columnNames;
        this.table = table;
        this.embeddableColumns = new ArrayList<EmbeddableColumnDefinition>();

        for (int i = 0; i < columns.size(); i++)
            embeddableColumns.add(new DefaultEmbeddableColumnDefinition(this, columnNames.get(i), columns.get(i), i));
    }

    @Override
    public final TableDefinition getTable() {
        return table;
    }

    @Override
    protected List<EmbeddableColumnDefinition> getElements0() throws SQLException {
        return embeddableColumns;
    }

    @Override
    public final List<EmbeddableColumnDefinition> getColumns() {
        return getElements();
    }

    @Override
    public final EmbeddableColumnDefinition getColumn(String columnName) {
        return getElement(columnName);
    }

    @Override
    public final EmbeddableColumnDefinition getColumn(String columnName, boolean ignoreCase) {
        return getElement(columnName, ignoreCase);
    }

    @Override
    public final EmbeddableColumnDefinition getColumn(int columnIndex) {
        return getElement(columnIndex);
    }
}
