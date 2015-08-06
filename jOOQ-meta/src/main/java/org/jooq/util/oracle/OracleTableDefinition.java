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

package org.jooq.util.oracle;

import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.inline;
import static org.jooq.util.oracle.sys.Tables.ALL_COL_COMMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_TAB_COLS;

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
import org.jooq.util.oracle.OracleDatabase.TypeInfo;

/**
 * @author Lukas Eder
 */
public class OracleTableDefinition extends AbstractTableDefinition {

	public OracleTableDefinition(SchemaDefinition schema, String name, String comment) {
		super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

		for (Record record : create().select(
		        ALL_TAB_COLS.DATA_TYPE_OWNER,
		        ALL_TAB_COLS.DATA_TYPE,
		        decode(ALL_TAB_COLS.DATA_TYPE.upper(),
		            "CLOB", inline(0),
		            "BLOB", inline(0),
		            ALL_TAB_COLS.CHAR_LENGTH).as("char_length"),
		        ALL_TAB_COLS.DATA_PRECISION,
		        ALL_TAB_COLS.DATA_SCALE,
		        ALL_TAB_COLS.NULLABLE,
		        ALL_TAB_COLS.DATA_DEFAULT,
		        ALL_TAB_COLS.COLUMN_NAME,
		        ALL_TAB_COLS.COLUMN_ID,
		        ALL_COL_COMMENTS.COMMENTS)
		    .from(ALL_TAB_COLS)
		    .join(ALL_COL_COMMENTS)
		    .on(ALL_TAB_COLS.OWNER.equal(ALL_COL_COMMENTS.OWNER),
		        ALL_TAB_COLS.TABLE_NAME.equal(ALL_COL_COMMENTS.TABLE_NAME),
		        ALL_TAB_COLS.COLUMN_NAME.equal(ALL_COL_COMMENTS.COLUMN_NAME))
	        .where(ALL_TAB_COLS.OWNER.equal(getSchema().getName()))
	        .and(ALL_TAB_COLS.TABLE_NAME.equal(getName()))
	        .orderBy(ALL_TAB_COLS.COLUMN_ID)
	        .fetch()) {

		    // [#3711] Check if the reported type is really a synonym for another type
            TypeInfo info = ((OracleDatabase) getDatabase()).getTypeInfo(
                getSchema(),
                record.getValue(ALL_TAB_COLS.DATA_TYPE_OWNER),
                record.getValue(ALL_TAB_COLS.DATA_TYPE));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                info.schema,
                record.getValue(ALL_TAB_COLS.DATA_TYPE),
                record.getValue("char_length", int.class),
                record.getValue(ALL_TAB_COLS.DATA_PRECISION, int.class),
                record.getValue(ALL_TAB_COLS.DATA_SCALE, int.class),
                record.getValue(ALL_TAB_COLS.NULLABLE, boolean.class),
                record.getValue(ALL_TAB_COLS.DATA_DEFAULT) != null,
                info.name
            );

			DefaultColumnDefinition column = new DefaultColumnDefinition(
				getDatabase().getTable(getSchema(), getName()),
			    record.getValue(ALL_TAB_COLS.COLUMN_NAME),
			    record.getValue(ALL_TAB_COLS.COLUMN_ID, int.class),
			    type,
                false,
			    record.getValue(ALL_COL_COMMENTS.COMMENTS)
		    );

			result.add(column);
		}

		return result;
	}
}
