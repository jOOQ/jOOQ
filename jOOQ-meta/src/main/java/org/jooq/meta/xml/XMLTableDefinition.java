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
package org.jooq.meta.xml;

import static java.lang.Boolean.TRUE;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.QOM.GenerationOption.STORED;
import static org.jooq.impl.QOM.GenerationOption.VIRTUAL;
import static org.jooq.meta.xml.XMLDatabase.unbox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.Table;

/**
 * @author Lukas Eder
 */
public class XMLTableDefinition extends AbstractTableDefinition {

    private final InformationSchema info;
    private final Table             table;

    public XMLTableDefinition(SchemaDefinition schema, InformationSchema info, Table table) {
        this(schema, info, table, "");
    }

    public XMLTableDefinition(SchemaDefinition schema, InformationSchema info, Table table, String comment) {
        this(schema, info, table, comment, TableType.TABLE, null);
    }

    public XMLTableDefinition(SchemaDefinition schema, InformationSchema info, Table table, String comment, TableType tableType, String source) {
        super(schema, table.getTableName(), comment, tableType, source);

        this.info = info;
        this.table = table;
    }

    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        for (Column column : ((XMLDatabase) getDatabase()).getColumnsByTableName(
            name(table.getTableCatalog(), table.getTableSchema(), table.getTableName())
        )) {
            SchemaDefinition schema = getDatabase().getSchema(column.getTableSchema());

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
                column.getDataType(),
                unbox(column.getCharacterMaximumLength()),
                unbox(column.getNumericPrecision()),
                unbox(column.getNumericScale()),
                column.isIsNullable(),
                column.getColumnDefault()
            )
                .generatedAlwaysAs(TRUE.equals(column.isIsGenerated()) ? column.getGenerationExpression() : null)
                .generationOption(TRUE.equals(column.isIsGenerated())
                    ? "STORED".equalsIgnoreCase(column.getGenerationOption())
                        ? STORED
                        : "VIRTUAL".equalsIgnoreCase(column.getGenerationOption())
                        ? VIRTUAL
                        : null
                    : null);

            result.add(new DefaultColumnDefinition(
                this,
                column.getColumnName(),
                unbox(column.getOrdinalPosition()),
                type,
                column.getIdentityGeneration() != null,
                TRUE.equals(column.isReadonly()),
                column.getComment()
            ));
        }

        return result;
    }
}
