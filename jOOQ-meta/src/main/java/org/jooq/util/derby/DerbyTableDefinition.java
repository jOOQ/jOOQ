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

package org.jooq.util.derby;

import static org.jooq.impl.DSL.inline;
import static org.jooq.util.derby.sys.tables.Syscolumns.SYSCOLUMNS;

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
import org.jooq.util.derby.sys.tables.Syscolumns;

/**
 * @author Lukas Eder
 */
public class DerbyTableDefinition extends AbstractTableDefinition {

    private final String         tableid;

    public DerbyTableDefinition(SchemaDefinition schema, String name, String tableid) {
		super(schema, name, "");

		this.tableid = tableid;
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                Syscolumns.COLUMNNAME,
                Syscolumns.COLUMNNUMBER,
                Syscolumns.COLUMNDATATYPE,
                Syscolumns.COLUMNDEFAULT,
                Syscolumns.AUTOINCREMENTINC)
            .from(SYSCOLUMNS)
            // [#1241] Suddenly, bind values didn't work any longer, here...
            .where(Syscolumns.REFERENCEID.equal(inline(tableid)))
            .orderBy(Syscolumns.COLUMNNUMBER)
            .fetch()) {

            String typeName = record.getValue(Syscolumns.COLUMNDATATYPE, String.class);
            Number precision = parsePrecision(typeName);
            Number scale = parseScale(typeName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                typeName,
                precision,
                precision,
                scale,
                !parseNotNull(typeName),
                record.getValue(Syscolumns.COLUMNDEFAULT) != null
            );

			ColumnDefinition column = new DefaultColumnDefinition(
				getDatabase().getTable(getSchema(), getName()),
			    record.getValue(Syscolumns.COLUMNNAME),
			    record.getValue(Syscolumns.COLUMNNUMBER),
			    type,
                null != record.getValue(Syscolumns.AUTOINCREMENTINC),
                null
            );

			result.add(column);
		}

		return result;
	}
}
