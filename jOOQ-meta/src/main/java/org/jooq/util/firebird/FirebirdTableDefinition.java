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
package org.jooq.util.firebird;

import static org.jooq.util.firebird.FirebirdDatabase.CHARACTER_LENGTH;
import static org.jooq.util.firebird.FirebirdDatabase.FIELD_SCALE;
import static org.jooq.util.firebird.FirebirdDatabase.FIELD_TYPE;
import static org.jooq.util.firebird.rdb.Tables.RDB$FIELDS;
import static org.jooq.util.firebird.rdb.Tables.RDB$RELATION_FIELDS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.firebird.rdb.tables.Rdb$fields;
import org.jooq.util.firebird.rdb.tables.Rdb$relationFields;

/**
 * @author Sugiharto Lim - Initial contribution
 */
public class FirebirdTableDefinition extends AbstractTableDefinition {

    public FirebirdTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        Rdb$relationFields r = RDB$RELATION_FIELDS.as("r");
        Rdb$fields f = RDB$FIELDS.as("f");

        // Inspiration for the below query was taken from jaybird's
        // DatabaseMetaData implementation
        for (Record record : create()
                .select(
                    r.RDB$FIELD_NAME.trim(),
                    r.RDB$DESCRIPTION,
                    r.RDB$DEFAULT_VALUE,
                    DSL.bitOr(r.RDB$NULL_FLAG.nvl((short) 0), f.RDB$NULL_FLAG.nvl((short) 0)).as(r.RDB$NULL_FLAG),
                    r.RDB$DEFAULT_SOURCE,
                    r.RDB$FIELD_POSITION,

                    // [#3342] FIELD_LENGTH should be ignored for LOBs
                    CHARACTER_LENGTH(f).as("CHARACTER_LENGTH"),
                    f.RDB$FIELD_PRECISION,
                    FIELD_SCALE(f).as("FIELD_SCALE"),
                    FIELD_TYPE(f).as("FIELD_TYPE"),
                    f.RDB$FIELD_SUB_TYPE)
                .from(r)
                .leftOuterJoin(f).on(r.RDB$FIELD_SOURCE.eq(f.RDB$FIELD_NAME))
                .where(r.RDB$RELATION_NAME.eq(getName()))
                .orderBy(r.RDB$FIELD_POSITION)
                .fetch()) {

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    getSchema(),
                    record.get("FIELD_TYPE", String.class),
                    record.get("CHARACTER_LENGTH", short.class),
                    record.get(f.RDB$FIELD_PRECISION),
                    record.get("FIELD_SCALE", Integer.class),
                    record.get(r.RDB$NULL_FLAG) == 0,
                    record.get(r.RDB$DEFAULT_SOURCE)
            );

            ColumnDefinition column = new DefaultColumnDefinition(
                    getDatabase().getTable(getSchema(), getName()),
                    record.get(r.RDB$FIELD_NAME.trim()),
                    record.get(r.RDB$FIELD_POSITION),
                    type,
                    false,
                    null
            );

            result.add(column);
        }

        return result;
    }
}
