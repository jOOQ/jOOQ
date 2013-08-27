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

package org.jooq.util.cubrid;

import static org.jooq.util.cubrid.dba.Tables.DB_ATTRIBUTE;
import static org.jooq.util.cubrid.dba.Tables.DB_SERIAL;

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
public class CUBRIDTableDefinition extends AbstractTableDefinition {

    public CUBRIDTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

		 for (Record record : create()
		         .select(
        		     DB_ATTRIBUTE.ATTR_NAME,
		             DB_ATTRIBUTE.DEF_ORDER,
		             DB_ATTRIBUTE.DATA_TYPE,
		             DB_ATTRIBUTE.PREC,
		             DB_ATTRIBUTE.SCALE,
		             DB_ATTRIBUTE.IS_NULLABLE,
		             DB_ATTRIBUTE.DEFAULT_VALUE,
		             DB_SERIAL.NAME)
		        .from(DB_ATTRIBUTE)
		        .leftOuterJoin(DB_SERIAL).on(
		            DB_ATTRIBUTE.ATTR_NAME.equal(DB_SERIAL.ATT_NAME).and(
	                DB_ATTRIBUTE.CLASS_NAME.equal(DB_SERIAL.CLASS_NAME)))
		        .where(DB_ATTRIBUTE.CLASS_NAME.equal(getName()))
		        .orderBy(DB_ATTRIBUTE.DEF_ORDER)
		        .fetch()) {

		    String dataType = record.getValue(DB_ATTRIBUTE.DATA_TYPE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                record.getValue(DB_ATTRIBUTE.PREC),
                record.getValue(DB_ATTRIBUTE.PREC),
                record.getValue(DB_ATTRIBUTE.SCALE),
                record.getValue(DB_ATTRIBUTE.IS_NULLABLE, boolean.class),
                record.getValue(DB_ATTRIBUTE.DEFAULT_VALUE) != null,
                getName() + "_" + record.getValue(DB_ATTRIBUTE.ATTR_NAME)
            );

			ColumnDefinition column = new DefaultColumnDefinition(
				getDatabase().getTable(getSchema(), getName()),
			    record.getValue(DB_ATTRIBUTE.ATTR_NAME),
			    record.getValue(DB_ATTRIBUTE.DEF_ORDER),
			    type,
			    record.getValue(DB_SERIAL.NAME) != null,
			    null
		    );

			result.add(column);
		}

		return result;
	}
}
