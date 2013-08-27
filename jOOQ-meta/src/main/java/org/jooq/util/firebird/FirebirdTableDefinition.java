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
                r.RDB$NULL_FLAG,
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
                    record.getValue(r.RDB$NULL_FLAG, (short) 0) == 0,
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
