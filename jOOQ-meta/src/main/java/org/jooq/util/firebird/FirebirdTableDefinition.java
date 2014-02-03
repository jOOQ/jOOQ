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

import static org.jooq.impl.DSL.decode;
import static org.jooq.util.firebird.rdb.Tables.RDB$FIELDS;
import static org.jooq.util.firebird.rdb.Tables.RDB$RELATION_FIELDS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
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
        for (Record record : create().select(
                r.RDB$FIELD_NAME.trim(),
                r.RDB$DESCRIPTION,
                r.RDB$DEFAULT_VALUE,
                r.RDB$NULL_FLAG.nvl((short) 0),
                r.RDB$DEFAULT_SOURCE,
                r.RDB$FIELD_POSITION,
                f.RDB$FIELD_LENGTH,
                f.RDB$FIELD_PRECISION,
                f.RDB$FIELD_SCALE.neg().as("FIELD_SCALE"),

                // FIELD_TYPE
                decode().value(f.RDB$FIELD_TYPE)
                        .when((short) 7, decode()
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 1), "NUMERIC")
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 0)
                             .and(f.RDB$FIELD_SCALE.lt((short) 0)), "NUMERIC")
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 2), "DECIMAL")
                            .otherwise("SMALLINT"))
                        .when((short) 8, decode()
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 1), "NUMERIC")
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 0)
                             .and(f.RDB$FIELD_SCALE.lt((short) 0)), "NUMERIC")
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 2), "DECIMAL")
                            .otherwise("INTEGER"))
                        .when((short) 9, "QUAD")
                        .when((short) 10, "FLOAT")
                        .when((short) 11, "D_FLOAT")
                        .when((short) 12, "DATE")
                        .when((short) 13, "TIME")
                        .when((short) 14, "CHAR")
                        .when((short) 16, decode()
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 1), "NUMERIC")
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 0)
                             .and(f.RDB$FIELD_SCALE.lt((short) 0)), "NUMERIC")
                            .when(f.RDB$FIELD_SUB_TYPE.eq((short) 2), "DECIMAL")
                            .otherwise("BIGINT"))
                        .when((short) 27, "DOUBLE")
                        .when((short) 35, "TIMESTAMP")
                        .when((short) 37, "VARCHAR")
                        .when((short) 40, "CSTRING")
                        .when((short) 261, decode().value(f.RDB$FIELD_SUB_TYPE)
                            .when((short) 0, "BLOB")
                            .when((short) 1, "BLOB SUB_TYPE TEXT")
                            .otherwise("BLOB"))
                        .otherwise("UNKNOWN").as("FIELD_TYPE"),
                    f.RDB$FIELD_SUB_TYPE)
                .from(r)
                .leftOuterJoin(f).on(r.RDB$FIELD_SOURCE.equal(f.RDB$FIELD_NAME))
                .where(r.RDB$RELATION_NAME.equal(getName()))
                .orderBy(r.RDB$FIELD_POSITION)
                .fetch()) {

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    getSchema(),
                    record.getValue("FIELD_TYPE", String.class),
                    record.getValue(f.RDB$FIELD_LENGTH),
                    record.getValue(f.RDB$FIELD_PRECISION),
                    record.getValue("FIELD_SCALE", Integer.class),
                    record.getValue(r.RDB$NULL_FLAG.nvl((short) 0)) == 0,
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
