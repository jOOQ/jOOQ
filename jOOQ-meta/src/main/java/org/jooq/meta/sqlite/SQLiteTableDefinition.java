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
 *
 *
 *
 */
package org.jooq.meta.sqlite;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.meta.sqlite.sqlite_master.SQLiteMaster.SQLITE_MASTER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.sqlite.sqlite_master.SQLiteMaster;

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

    public SQLiteTableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType) {
        super(schema, name, comment, tableType);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        Field<String> fName = field(name("name"), String.class);
        Field<String> fType = field(name("type"), String.class);
        Field<Boolean> fNotnull = field(name("notnull"), boolean.class);
        Field<String> fDefaultValue = field(name("dflt_value"), String.class);
        Field<Integer> fPk = field(name("pk"), int.class);

        int position = 0;
        for (Record record : create().select(fName, fType, fNotnull, fDefaultValue, fPk)
                .from("pragma_table_info({0})", inline(getName())).fetch()) {
            position++;

            String name = record.get(fName);
            String dataType = record.get(fType)
                                    .replaceAll("\\(\\d+(\\s*,\\s*\\d+)?\\)", "");
            Number precision = parsePrecision(record.get(fType));
            Number scale = parseScale(record.get(fType));

            // SQLite identities are primary keys whose tables are mentioned in
            // sqlite_sequence
            int pk = record.get(fPk);
            boolean identity = false;

            if (pk > 0) {

                // [#6854] sqlite_sequence only contains identity information once a table contains records.
                identity |= existsSqliteSequence() && create()
                    .fetchOne("select count(*) from sqlite_sequence where name = ?", getName())
                    .get(0, Boolean.class);

                if (!identity && !create().fetchExists(selectOne().from("{0}", DSL.name(getName()))))
                    identity = create()
                        .select(SQLiteMaster.SQL)
                        .from(SQLITE_MASTER)
                        .where(SQLiteMaster.NAME.eq(getName()))
                        .fetchOneInto(String.class)
                        .matches("(?s:.*\\b" + getName() + "\\b[^,]*(?i:\\bautoincrement\\b)[^,]*.*)");
            }

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                precision,
                precision,
                scale,
                !record.get(fNotnull),
                record.get(fDefaultValue)
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
