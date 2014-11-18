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
import static org.jooq.util.firebird.rdb.Tables.RDB$PROCEDURE_PARAMETERS;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.firebird.rdb.tables.Rdb$fields;
import org.jooq.util.firebird.rdb.tables.Rdb$procedureParameters;

/**
 * @author Lukas Eder
 */
public class FirebirdRoutineDefinition extends AbstractRoutineDefinition {

    public FirebirdRoutineDefinition(SchemaDefinition schema, String name) {
        super(schema, null, name, null, null, false);
    }

    @Override
    protected void init0() throws SQLException {
        Rdb$procedureParameters p = RDB$PROCEDURE_PARAMETERS.as("p");
        Rdb$fields f = RDB$FIELDS.as("f");
        int i = 0;

        for (Record record : create()
                .select(
                    p.RDB$PARAMETER_NUMBER,
                    p.RDB$PARAMETER_TYPE,
                    p.RDB$PARAMETER_NAME.trim().as(p.RDB$PARAMETER_NAME),
                    FIELD_TYPE(f).as("FIELD_TYPE"),
                    CHARACTER_LENGTH(f).as("CHARACTER_LENGTH"),
                    f.RDB$FIELD_PRECISION,
                    FIELD_SCALE(f).as("FIELD_SCALE"),
                    DSL.bitOr(p.RDB$NULL_FLAG.nvl((short) 0), f.RDB$NULL_FLAG.nvl((short) 0)).as(p.RDB$NULL_FLAG),
                    p.RDB$DEFAULT_SOURCE)
                .from(p)
                .leftOuterJoin(f).on(p.RDB$FIELD_SOURCE.eq(f.RDB$FIELD_NAME))
                .where(p.RDB$PROCEDURE_NAME.eq(getName()))
                .orderBy(
                    p.RDB$PARAMETER_TYPE.desc(),
                    p.RDB$PARAMETER_NUMBER.asc())) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue("FIELD_TYPE", String.class),
                record.getValue("CHARACTER_LENGTH", short.class),
                record.getValue(f.RDB$FIELD_PRECISION),
                record.getValue("FIELD_SCALE", Integer.class),
                record.getValue(p.RDB$NULL_FLAG) == 0,
                record.getValue(p.RDB$DEFAULT_SOURCE) != null
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.getValue(p.RDB$PARAMETER_NAME),
                i++,
                type
            );

            addParameter(record.getValue(p.RDB$PARAMETER_TYPE, int.class).equals(0) ? InOutDefinition.IN : InOutDefinition.OUT, parameter);
        }

    }
}
