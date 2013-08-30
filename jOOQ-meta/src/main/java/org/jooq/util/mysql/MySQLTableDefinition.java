/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */

package org.jooq.util.mysql;

import static java.util.Arrays.asList;
import static org.jooq.util.mysql.information_schema.tables.Columns.COLUMNS;
import static org.jooq.util.mysql.information_schema.tables.Columns.ORDINAL_POSITION;
import static org.jooq.util.mysql.information_schema.tables.Columns.TABLE_NAME;
import static org.jooq.util.mysql.information_schema.tables.Columns.TABLE_SCHEMA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.mysql.information_schema.tables.Columns;

/**
 * @author Lukas Eder
 */
public class MySQLTableDefinition extends AbstractTableDefinition {

    public MySQLTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

		for (Record record : create().select(
        		    Columns.ORDINAL_POSITION,
    		        Columns.COLUMN_NAME,
    		        Columns.COLUMN_COMMENT,
    		        Columns.COLUMN_TYPE,
    		        Columns.DATA_TYPE,
    		        Columns.IS_NULLABLE,
    		        Columns.COLUMN_DEFAULT,
    		        Columns.CHARACTER_MAXIMUM_LENGTH,
    		        Columns.NUMERIC_PRECISION,
    		        Columns.NUMERIC_SCALE,
    		        Columns.EXTRA)
    		    .from(COLUMNS)
    		    .where(TABLE_SCHEMA.equal(getSchema().getName()))
    		    .and(TABLE_NAME.equal(getName()))
    		    .orderBy(ORDINAL_POSITION)
    		    .fetch()) {

		    String dataType = record.getValue(Columns.DATA_TYPE);

		    // [#519] Some types have unsigned versions
		    if (getDatabase().supportsUnsignedTypes()) {
    		    if (asList("tinyint", "smallint", "mediumint", "int", "bigint").contains(dataType.toLowerCase())) {
    	            if (record.getValue(Columns.COLUMN_TYPE).toLowerCase().contains("unsigned")) {
    	                dataType += "unsigned";
    	            }
    		    }
		    }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                record.getValue(Columns.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(Columns.NUMERIC_PRECISION),
                record.getValue(Columns.NUMERIC_SCALE),
                record.getValue(Columns.IS_NULLABLE, boolean.class),
                record.getValue(Columns.COLUMN_DEFAULT) != null,
                getName() + "_" + record.getValue(Columns.COLUMN_NAME)
            );

			ColumnDefinition column = new DefaultColumnDefinition(
				getDatabase().getTable(getSchema(), getName()),
			    record.getValue(Columns.COLUMN_NAME),
			    record.getValue(Columns.ORDINAL_POSITION, int.class),
			    type,
			    "auto_increment".equalsIgnoreCase(record.getValue(Columns.EXTRA)),
			    record.getValue(Columns.COLUMN_COMMENT)
		    );

			result.add(column);
		}

		return result;
	}
}
