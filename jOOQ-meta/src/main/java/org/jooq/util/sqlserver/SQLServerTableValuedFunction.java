/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.util.sqlserver;

import static org.jooq.util.sqlserver.information_schema.Tables.ROUTINE_COLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class SQLServerTableValuedFunction extends AbstractTableDefinition {

	private final SQLServerRoutineDefinition routine;

    public SQLServerTableValuedFunction(SchemaDefinition schema, String name, String comment) {
		super(schema, name, comment);

		routine = new SQLServerRoutineDefinition(schema, name, name, "TABLE", 0, 0, 0);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create()
            .select(
                ROUTINE_COLUMNS.COLUMN_NAME,
                ROUTINE_COLUMNS.ORDINAL_POSITION,
                ROUTINE_COLUMNS.DATA_TYPE,
                ROUTINE_COLUMNS.IS_NULLABLE,
                ROUTINE_COLUMNS.COLUMN_DEFAULT,
                ROUTINE_COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                ROUTINE_COLUMNS.NUMERIC_PRECISION,
                ROUTINE_COLUMNS.NUMERIC_SCALE)
            .from(ROUTINE_COLUMNS)
            .where(ROUTINE_COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
                .and(ROUTINE_COLUMNS.TABLE_NAME.equal(getName()))
            .orderBy(ROUTINE_COLUMNS.ORDINAL_POSITION)
            .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(ROUTINE_COLUMNS.DATA_TYPE),
                record.getValue(ROUTINE_COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(ROUTINE_COLUMNS.NUMERIC_PRECISION),
                record.getValue(ROUTINE_COLUMNS.NUMERIC_SCALE),
                record.getValue(ROUTINE_COLUMNS.IS_NULLABLE, boolean.class),
                record.getValue(ROUTINE_COLUMNS.COLUMN_DEFAULT) != null,
                ""
            );

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.getValue(ROUTINE_COLUMNS.COLUMN_NAME),
			    record.getValue(ROUTINE_COLUMNS.ORDINAL_POSITION, int.class),
			    type,
			    false,
			    null);
			result.add(column);
		}

		return result;
	}

    @Override
    protected List<ParameterDefinition> getParameters0() {
        return routine.getInParameters();
    }

    @Override
    public boolean isTableValuedFunction() {
        return true;
    }
}
