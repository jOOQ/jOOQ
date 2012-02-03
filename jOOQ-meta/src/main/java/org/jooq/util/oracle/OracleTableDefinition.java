/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq.util.oracle;

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
		        ALL_TAB_COLS.DATA_PRECISION,
		        ALL_TAB_COLS.DATA_SCALE,
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
                record.getValueAsInteger(ALL_TAB_COLS.DATA_PRECISION, 0),
                record.getValueAsInteger(ALL_TAB_COLS.DATA_SCALE, 0));

			DefaultColumnDefinition column = new DefaultColumnDefinition(
				getDatabase().getTable(getSchema(), getName()),
			    record.getValue(ALL_TAB_COLS.COLUMN_NAME),
			    record.getValueAsInteger(ALL_TAB_COLS.COLUMN_ID),
			    type,
                false,
			    record.getValue(ALL_COL_COMMENTS.COMMENTS));

			result.add(column);
		}

		return result;
	}
}
