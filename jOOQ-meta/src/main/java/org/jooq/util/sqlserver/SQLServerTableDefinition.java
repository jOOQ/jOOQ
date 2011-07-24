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

package org.jooq.util.sqlserver;

import static org.jooq.util.sqlserver.information_schema.tables.Columns.COLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.sqlserver.information_schema.tables.Columns;

/**
 * @author Lukas Eder
 */
public class SQLServerTableDefinition extends AbstractTableDefinition {

	public SQLServerTableDefinition(Database database, String name, String comment) {
		super(database, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();
		Field<Integer> identity = create().field("c.is_identity", Integer.class);

        for (Record record : create().select(
                Columns.COLUMN_NAME,
                Columns.ORDINAL_POSITION,
                Columns.DATA_TYPE,
                Columns.NUMERIC_PRECISION,
                Columns.NUMERIC_SCALE,
                identity)
            .from(COLUMNS)
            .join("sys.objects o")
            .on("o.type in ('U', 'V')")
            .and(Columns.TABLE_NAME.equal(create().field("o.name", String.class)))
            .join("sys.columns c")
            .on("c.object_id = o.object_id")
            .and(Columns.COLUMN_NAME.equal(create().field("c.name", String.class)))
            .where(Columns.TABLE_SCHEMA.equal(getSchemaName()))
            .and(Columns.TABLE_NAME.equal(getName()))
            .orderBy(Columns.ORDINAL_POSITION)
            .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                record.getValue(Columns.DATA_TYPE),
                record.getValue(Columns.NUMERIC_PRECISION),
                record.getValue(Columns.NUMERIC_SCALE),
                "");

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getName()),
			    record.getValue(Columns.COLUMN_NAME),
			    record.getValueAsInteger(Columns.ORDINAL_POSITION),
			    type,
			    1 == record.getValue(identity),
			    null);
			result.add(column);
		}

		return result;
	}
}
