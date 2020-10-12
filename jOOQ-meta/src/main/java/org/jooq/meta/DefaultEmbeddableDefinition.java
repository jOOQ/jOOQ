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

import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
public class DefaultEmbeddableDefinition
    extends AbstractElementContainerDefinition<EmbeddableColumnDefinition>
    implements EmbeddableDefinition {

    private static final JooqLogger                log = JooqLogger.getLogger(DefaultEmbeddableDefinition.class);
    private final TableDefinition                  definingTable;
    private final List<String>                     definingColumnNames;
    private final String                           referencingName;
    private final String                           referencingComment;
    private final TableDefinition                  referencingTable;
    private final List<EmbeddableColumnDefinition> embeddableColumns;
    private final boolean                          replacesFields;

    @SuppressWarnings("unused")
    public DefaultEmbeddableDefinition(
        SchemaDefinition definingSchema,
        String definingName,
        String definingComment,
        TableDefinition definingTable,
        List<String> definingColumnNames,
        String referencingName,
        String referencingComment,
        TableDefinition referencingTable,
        List<ColumnDefinition> referencingColumns,
        boolean replacesFields
    ) {
        super(definingSchema, definingName, definingComment);

        this.definingColumnNames = definingColumnNames;
        this.definingTable = definingTable;
        this.referencingName = referencingName;
        this.referencingComment = referencingComment;
        this.referencingTable = referencingTable;
        this.embeddableColumns = new ArrayList<>();




        if (replacesFields) {
            log.info("Commercial feature", "Embeddables replacing fields is a commercial only feature. Please upgrade to the jOOQ Professional Edition");
            replacesFields = false;
        }

        this.replacesFields = replacesFields;

        for (int i = 0; i < referencingColumns.size(); i++)
            embeddableColumns.add(new DefaultEmbeddableColumnDefinition(this, definingColumnNames.get(i), referencingColumns.get(i), i + 1));
    }

    @Override
    public final TableDefinition getTable() {
        return getDefiningTable();
    }

    @Override
    public final TableDefinition getDefiningTable() {
        return definingTable;
    }

    @Override
    public final String getReferencingComment() {
        return referencingComment;
    }

    @Override
    public final String getReferencingName() {
        return getReferencingInputName();
    }

    @Override
    public final String getReferencingInputName() {
        return referencingName;
    }

    @Override
    public final String getReferencingOutputName() {
        return referencingName;
    }

    @Override
    public final TableDefinition getReferencingTable() {
        return referencingTable;
    }

    @Override
    protected final List<EmbeddableColumnDefinition> getElements0() throws SQLException {
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

    @Override
    public final EmbeddableColumnDefinition getReferencingColumn(String columnName) {
        return getReferencingColumn(columnName, false);
    }

    @Override
    public final EmbeddableColumnDefinition getReferencingColumn(String columnName, boolean ignoreCase) {
        if (columnName == null)
            return null;

        for (EmbeddableColumnDefinition column : getColumns())
            if ((ignoreCase && column.getReferencingColumn().getName().equalsIgnoreCase(columnName)) ||
                (!ignoreCase && column.getReferencingColumn().getName().equals(columnName)))

                return column;

        return null;
    }

    @Override
    public final boolean replacesFields() {
        return replacesFields;
    }

    @Override
    public String toString() {
        return super.toString() + " (referenced by " + getReferencingTable() + " " + getColumns() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        if (!(obj instanceof EmbeddableDefinition))
            return false;

        EmbeddableDefinition other = (EmbeddableDefinition) obj;
        return getReferencingTable().equals(other.getReferencingTable())
            && getColumns().equals(other.getColumns());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
