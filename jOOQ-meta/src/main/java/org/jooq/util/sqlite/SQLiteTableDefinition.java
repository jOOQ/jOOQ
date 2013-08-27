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
package org.jooq.util.sqlite;

import static org.jooq.util.sqlite.sqlite_master.SQLiteMaster.SQLITE_MASTER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.sqlite.sqlite_master.SQLiteMaster;

/**
 * SQLite table definition
 *
 * @author Lukas Eder
 */
public class SQLiteTableDefinition extends AbstractTableDefinition {

    private static Boolean existsSqliteSequence;

    public SQLiteTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        int position = 0;
        for (Record record : create().fetch("pragma table_info('" + getName() + "')")) {
            position++;

            String name = record.getValue("name", String.class);
            String dataType = record.getValue("type", String.class)
                                    .replaceAll("\\(\\d+\\)", "");
            Number precision = parsePrecision(record.getValue("type", String.class));
            Number scale = parseScale(record.getValue("type", String.class));

            // SQLite identities are primary keys whose tables are mentioned in
            // sqlite_sequence
            boolean pk = record.getValue("pk", Boolean.class);
            boolean identity = pk && existsSqliteSequence() && create()
                .fetchOne("select count(*) from sqlite_sequence where name = ?", getName())
                .getValue(0, Boolean.class);

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                precision,
                precision,
                scale,
                !record.getValue("notnull", boolean.class),
                record.getValue("dflt_value") != null
            );

            ColumnDefinition column = new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                name,
                position,
                type,
                identity,
                null
            );

            result.add(column);
        }

        return result;
    }

    private boolean existsSqliteSequence() {
        if (existsSqliteSequence == null) {
            existsSqliteSequence = create()
                .selectCount()
                .from(SQLITE_MASTER)
                .where(SQLiteMaster.NAME.lower().eq("sqlite_sequence"))
                .fetchOne(0, boolean.class);
        }

        return existsSqliteSequence;
    }
}
