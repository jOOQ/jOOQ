/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util.firebird;

import static org.jooq.impl.Factory.decode;
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
                    record.getValue("FIELD_SCALE", Integer.class));

            ColumnDefinition column = new DefaultColumnDefinition(
                    getDatabase().getTable(getSchema(), getName()),
                    record.getValue(r.RDB$FIELD_NAME.trim()),
                    record.getValue(r.RDB$FIELD_POSITION),
                    type,
                    record.getValue(r.RDB$NULL_FLAG, (short) 0) == 0,
                    false,
                    null);

            result.add(column);
        }

        return result;
    }
}
