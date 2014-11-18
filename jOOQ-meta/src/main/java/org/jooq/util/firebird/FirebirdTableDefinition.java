/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
                    record.getValue("FIELD_TYPE", String.class),
                    record.getValue("CHARACTER_LENGTH", short.class),
                    record.getValue(f.RDB$FIELD_PRECISION),
                    record.getValue("FIELD_SCALE", Integer.class),
                    record.getValue(r.RDB$NULL_FLAG) == 0,
                    record.getValue(r.RDB$DEFAULT_SOURCE) != null
            );

            ColumnDefinition column = new DefaultColumnDefinition(
                    getDatabase().getTable(getSchema(), getName()),
                    record.getValue(r.RDB$FIELD_NAME.trim()),
                    record.getValue(r.RDB$FIELD_POSITION),
                    type,
                    false,
                    null
            );

            result.add(column);
        }

        return result;
    }
}
