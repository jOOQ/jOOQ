/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 * and Maintenance Agreement for more details: http://www.jooq.org/eula
 */

package org.jooq.util.sqlserver;

import static org.jooq.impl.DSL.field;
import static org.jooq.util.sqlserver.information_schema.Tables.COLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Field;
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
public class SQLServerTableDefinition extends AbstractTableDefinition {

	public SQLServerTableDefinition(SchemaDefinition schema, String name, String comment) {
		super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();
        Field<Integer> identity = field("c.is_identity", Integer.class);

        for (Record record : create()
            .select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,
                COLUMNS.DATA_TYPE,
                COLUMNS.IS_NULLABLE,
                COLUMNS.COLUMN_DEFAULT,
                COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                COLUMNS.NUMERIC_PRECISION,
                COLUMNS.NUMERIC_SCALE,
                identity)
            .from(COLUMNS)
                .join("sys.schemas s")
                .on(COLUMNS.TABLE_SCHEMA.equal(field("s.name", String.class)))
                // sys.objects is used rather than sys.tables, to include tables AND views
                .join("sys.objects t")
                .on("t.type in ('U', 'V')")
                .and("t.schema_id = s.schema_id")
                .and(COLUMNS.TABLE_NAME.equal(field("t.name", String.class)))
                .join("sys.columns c")
                .on("c.object_id = t.object_id")
                .and(COLUMNS.COLUMN_NAME.equal(field("c.name", String.class)))
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
                .and(COLUMNS.TABLE_NAME.equal(getName()))
            .orderBy(COLUMNS.ORDINAL_POSITION)
            .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(COLUMNS.DATA_TYPE),
                record.getValue(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(COLUMNS.NUMERIC_PRECISION),
                record.getValue(COLUMNS.NUMERIC_SCALE),
                record.getValue(COLUMNS.IS_NULLABLE, boolean.class),
                record.getValue(COLUMNS.COLUMN_DEFAULT) != null,
                ""
            );

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.getValue(COLUMNS.COLUMN_NAME),
			    record.getValue(COLUMNS.ORDINAL_POSITION, int.class),
			    type,
			    1 == record.getValue(identity),
			    null);
			result.add(column);
		}

		return result;
	}
}
