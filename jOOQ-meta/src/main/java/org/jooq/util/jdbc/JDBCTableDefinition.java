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
 */

package org.jooq.util.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Table;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class JDBCTableDefinition extends AbstractTableDefinition {

	private final Table<?> table;

    public JDBCTableDefinition(SchemaDefinition schema, Table<?> table) {
		super(schema, table.getName(), "");

		this.table = table;
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

		int ordinal = 0;
        for (Field<?> field : table.fields()) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                field.getDataType().getTypeName(),
                field.getDataType().length(),
                field.getDataType().precision(),
                field.getDataType().scale(),
                field.getDataType().nullable(),
                create().renderInlined(field.getDataType().defaultValue()),
                (Name) null
            );

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    field.getName(),
			    ordinal,
			    type,
			    false,
			    null
		    );

			result.add(column);

			ordinal++;
		}

		return result;
	}
}
