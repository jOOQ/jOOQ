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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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

package org.jooq.meta.trino;

import static org.jooq.meta.hsqldb.information_schema.Tables.COLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class TrinoTableDefinition extends AbstractTableDefinition {

    public TrinoTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    public TrinoTableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType, String source) {
        super(schema, name, comment, tableType, source);
    }

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,
                COLUMNS.DATA_TYPE,
                COLUMNS.COLUMN_DEFAULT,
                COLUMNS.IS_NULLABLE)
            .from(COLUMNS)
            .where(COLUMNS.TABLE_SCHEMA.eq(getSchema().getName()))
                .and(COLUMNS.TABLE_NAME.eq(getName()))
            .orderBy(COLUMNS.ORDINAL_POSITION)
        ) {
            String typeName = record.get(COLUMNS.DATA_TYPE);
            Number precision = parsePrecision(typeName);
            Number scale = parseScale(typeName);


            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                typeName,
                precision,
                precision,
                scale,
                record.get(COLUMNS.IS_NULLABLE, boolean.class),
                record.get(COLUMNS.COLUMN_DEFAULT)
            );

            result.add(new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.get(COLUMNS.COLUMN_NAME),
                result.size() + 1,
			    type,
			    false,
			    ""
		    ));
		}

		return result;
	}
}
