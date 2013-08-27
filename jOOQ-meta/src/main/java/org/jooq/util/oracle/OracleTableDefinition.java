/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
		        ALL_TAB_COLS.DATA_TYPE,
		        decode(ALL_TAB_COLS.DATA_TYPE.upper(),
		            "CLOB", inline(0),
		            "BLOB", inline(0),
		            ALL_TAB_COLS.DATA_LENGTH).as("data_length"),
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

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(ALL_TAB_COLS.DATA_TYPE),
                record.getValue("data_length", int.class),
                record.getValue(ALL_TAB_COLS.DATA_PRECISION, int.class),
                record.getValue(ALL_TAB_COLS.DATA_SCALE, int.class),
                record.getValue(ALL_TAB_COLS.NULLABLE, boolean.class),
                record.getValue(ALL_TAB_COLS.DATA_DEFAULT) != null
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
