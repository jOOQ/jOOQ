/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
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

            String name = record.get("name", String.class);
            String dataType = record.get("type", String.class)
                                    .replaceAll("\\(\\d+(\\s*,\\s*\\d+)?\\)", "");
            Number precision = parsePrecision(record.get("type", String.class));
            Number scale = parseScale(record.get("type", String.class));

            // SQLite identities are primary keys whose tables are mentioned in
            // sqlite_sequence
            int pk = record.get("pk", int.class);
            boolean identity = pk > 0 && existsSqliteSequence() && create()
                .fetchOne("select count(*) from sqlite_sequence where name = ?", getName())
                .get(0, Boolean.class);

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                precision,
                precision,
                scale,
                !record.get("notnull", boolean.class),
                record.get("dflt_value", String.class)
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
