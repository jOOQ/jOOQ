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

package org.jooq.util.hsqldb;

import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.hsqldb.information_schema.Tables.COLUMNS;
import static org.jooq.util.hsqldb.information_schema.Tables.ELEMENT_TYPES;

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

/**
 * @author Lukas Eder
 */
public class HSQLDBTableDefinition extends AbstractTableDefinition {

	public HSQLDBTableDefinition(SchemaDefinition schema, String name, String comment) {
		super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,
                nvl(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER,
                    nvl2(COLUMNS.INTERVAL_TYPE,
                        concat(COLUMNS.DATA_TYPE, val(" "), COLUMNS.INTERVAL_TYPE),
                        COLUMNS.DATA_TYPE)).as("datatype"),
                COLUMNS.IDENTITY_GENERATION,
                COLUMNS.IS_NULLABLE,
                COLUMNS.COLUMN_DEFAULT,
                COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                COLUMNS.NUMERIC_PRECISION,
                COLUMNS.NUMERIC_SCALE,
                COLUMNS.UDT_NAME)
            .from(COLUMNS)
            .leftOuterJoin(ELEMENT_TYPES)
            .on(COLUMNS.TABLE_SCHEMA.equal(ELEMENT_TYPES.OBJECT_SCHEMA))
            .and(COLUMNS.TABLE_NAME.equal(ELEMENT_TYPES.OBJECT_NAME))
            .and(COLUMNS.DTD_IDENTIFIER.equal(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER))
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(COLUMNS.TABLE_NAME.equal(getName()))
            .orderBy(COLUMNS.ORDINAL_POSITION)
            .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get("datatype", String.class),
                record.get(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.get(COLUMNS.NUMERIC_PRECISION),
                record.get(COLUMNS.NUMERIC_SCALE),
                record.get(COLUMNS.IS_NULLABLE, boolean.class),
                record.get(COLUMNS.COLUMN_DEFAULT),
                record.get(COLUMNS.UDT_NAME)
            );

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.get(COLUMNS.COLUMN_NAME),
			    record.get(COLUMNS.ORDINAL_POSITION, int.class),
			    type,
			    null != record.get(COLUMNS.IDENTITY_GENERATION),
			    null
		    );

			result.add(column);
		}

		return result;
	}
}
