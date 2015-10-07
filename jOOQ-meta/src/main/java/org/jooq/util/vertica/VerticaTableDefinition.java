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

package org.jooq.util.vertica;

import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.select;
import static org.jooq.util.vertica.v_catalog.Tables.COLUMNS;
import static org.jooq.util.vertica.v_catalog.Tables.SYSTEM_COLUMNS;
import static org.jooq.util.vertica.v_catalog.Tables.VIEW_COLUMNS;

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
public class VerticaTableDefinition extends AbstractTableDefinition {

	public VerticaTableDefinition(SchemaDefinition schema, String name, String comment) {
		super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create()
                .select(
                    COLUMNS.COLUMN_NAME,
                    COLUMNS.ORDINAL_POSITION,
                    choose(COLUMNS.DATA_TYPE)
                        .when(inline("int"), inline("bigint"))
                        .otherwise(COLUMNS.DATA_TYPE).as(COLUMNS.DATA_TYPE),
                    COLUMNS.IS_NULLABLE,
                    COLUMNS.COLUMN_DEFAULT,
                    COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                    COLUMNS.NUMERIC_PRECISION,
                    COLUMNS.NUMERIC_SCALE)
                .from(COLUMNS)
                .where(COLUMNS.TABLE_SCHEMA.eq(getSchema().getName()))
                .and(COLUMNS.TABLE_NAME.eq(getName()))
                .unionAll(
                    select(
                        VIEW_COLUMNS.COLUMN_NAME,
                        VIEW_COLUMNS.ORDINAL_POSITION,
                        VIEW_COLUMNS.DATA_TYPE,
                        inline(false),
                        inline((String) null),
                        VIEW_COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                        VIEW_COLUMNS.NUMERIC_PRECISION,
                        VIEW_COLUMNS.NUMERIC_SCALE)
                    .from(VIEW_COLUMNS)
                    .where(VIEW_COLUMNS.TABLE_SCHEMA.eq(getSchema().getName()))
                    .and(VIEW_COLUMNS.TABLE_NAME.eq(getName()))
                )
                .unionAll(
                    select(
                        SYSTEM_COLUMNS.COLUMN_NAME,
                        SYSTEM_COLUMNS.ORDINAL_POSITION,
                        SYSTEM_COLUMNS.DATA_TYPE,
                        SYSTEM_COLUMNS.IS_NULLABLE,
                        SYSTEM_COLUMNS.COLUMN_DEFAULT,
                        SYSTEM_COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                        SYSTEM_COLUMNS.NUMERIC_PRECISION,
                        SYSTEM_COLUMNS.NUMERIC_SCALE)
                    .from(SYSTEM_COLUMNS)
                    .where(SYSTEM_COLUMNS.TABLE_SCHEMA.eq(getSchema().getName()))
                    .and(SYSTEM_COLUMNS.TABLE_NAME.eq(getName()))
                )
                .orderBy(2)
                .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(COLUMNS.DATA_TYPE),
                record.getValue(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(COLUMNS.NUMERIC_PRECISION),
                record.getValue(COLUMNS.NUMERIC_SCALE),
                record.getValue(COLUMNS.IS_NULLABLE, boolean.class),
                record.getValue(COLUMNS.COLUMN_DEFAULT) != null
            );

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.getValue(COLUMNS.COLUMN_NAME),
			    record.getValue(COLUMNS.ORDINAL_POSITION),
			    type,
			    false,
			    null
		    );

			result.add(column);
		}

		return result;
	}
}
