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

package org.jooq.util.mysql;

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
import org.jooq.util.Database;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.mysql.information_schema.tables.Columns;

/**
 * @author Lukas Eder
 */
public class MySQLTableDefinition extends AbstractTableDefinition {

    public MySQLTableDefinition(Database database, String name, String comment) {
		super(database, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

		for (Record record : create().select(
		        Columns.COLUMN_NAME,
		        Columns.ORDINAL_POSITION,
		        Columns.DATA_TYPE,
		        Columns.NUMERIC_PRECISION,
		        Columns.NUMERIC_SCALE,
		        Columns.COLUMN_COMMENT,
		        Columns.EXTRA)
		    .from(COLUMNS)
		    .where(TABLE_SCHEMA.equal(getSchemaName()))
		    .and(TABLE_NAME.equal(getName()))
		    .orderBy(ORDINAL_POSITION)
		    .fetch()) {

		    DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                record.getValue(Columns.DATA_TYPE),
                record.getValue(Columns.NUMERIC_PRECISION),
                record.getValue(Columns.NUMERIC_SCALE),
                getName() + "_" + record.getValue(Columns.COLUMN_NAME));

			ColumnDefinition column = new DefaultColumnDefinition(
				getDatabase().getTable(getName()),
			    record.getValue(Columns.COLUMN_NAME),
			    record.getValueAsInteger(Columns.ORDINAL_POSITION),
			    type,
			    "auto_increment".equalsIgnoreCase(record.getValue(Columns.EXTRA)),
			    record.getValue(Columns.COLUMN_COMMENT));

			result.add(column);
		}

		return result;
	}
}
