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
package org.jooq.util.h2;

import static org.jooq.util.h2.information_schema.tables.Columns.COLUMNS;

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
import org.jooq.util.h2.information_schema.tables.Columns;

/**
 * H2 table definition
 *
 * @author Espen Stromsnes
 */

public class H2TableDefinition extends AbstractTableDefinition {

    public H2TableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                Columns.COLUMN_NAME,
                Columns.ORDINAL_POSITION,
                Columns.TYPE_NAME,
                Columns.CHARACTER_MAXIMUM_LENGTH,
                Columns.NUMERIC_PRECISION,
                Columns.NUMERIC_SCALE,
                Columns.IS_NULLABLE,
                Columns.COLUMN_DEFAULT.nvl2(true, false).as("default"),
                Columns.REMARKS,
                Columns.SEQUENCE_NAME)
            .from(COLUMNS)
            .where(Columns.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(Columns.TABLE_NAME.equal(getName()))
            .orderBy(Columns.ORDINAL_POSITION)
            .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(Columns.TYPE_NAME),
                record.getValue(Columns.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(Columns.NUMERIC_PRECISION),
                record.getValue(Columns.NUMERIC_SCALE),
                record.getValue(Columns.IS_NULLABLE, boolean.class),
                record.getValue("default", boolean.class));

            ColumnDefinition column = new DefaultColumnDefinition(
            	getDatabase().getTable(getSchema(), getName()),
                record.getValue(Columns.COLUMN_NAME),
                record.getValue(Columns.ORDINAL_POSITION),
                type,
                null != record.getValue(Columns.SEQUENCE_NAME),
                record.getValue(Columns.REMARKS));

            result.add(column);
        }

        return result;
    }
}
