/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.util.postgres;


import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.util.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.util.postgres.information_schema.Tables.ROUTINES;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_PROC;

import java.sql.SQLException;
import java.util.Arrays;

import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * Postgres implementation of {@link AbstractRoutineDefinition}
 *
 * @author Lukas Eder
 */
public class PostgresRoutineDefinition extends AbstractRoutineDefinition {

    private static Boolean is94;
    private final String   specificName;

    public PostgresRoutineDefinition(Database database, Record record) {
        super(database.getSchema(record.getValue(ROUTINES.ROUTINE_SCHEMA)),
            null,
            record.getValue(ROUTINES.ROUTINE_NAME),
            null,
            record.getValue("overload", String.class),
            record.getValue(PG_PROC.PROISAGG, boolean.class));

        if (!Arrays.asList("void", "record").contains(record.getValue("data_type"))) {
            SchemaDefinition typeSchema = null;

            String schemaName = record.getValue(ROUTINES.TYPE_UDT_SCHEMA);
            if (schemaName != null)
                typeSchema = getDatabase().getSchema(schemaName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                typeSchema == null
                    ? database.getSchema(record.getValue(ROUTINES.ROUTINE_SCHEMA))
                    : typeSchema,
                record.getValue("data_type", String.class),
                record.getValue(ROUTINES.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(ROUTINES.NUMERIC_PRECISION),
                record.getValue(ROUTINES.NUMERIC_SCALE),
                null,
                null,
                record.getValue(ROUTINES.TYPE_UDT_NAME)
            );

            returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }

        specificName = record.getValue(ROUTINES.SPECIFIC_NAME);
    }

    // [#3375] This internal constructor is used for table-valued functions. It should not be used otherwise
    PostgresRoutineDefinition(Database database, String schema, String name, String specificName) {
        super(database.getSchema(schema), null, name, null, null);

        this.specificName = specificName;
    }

    @Override
    protected void init0() throws SQLException {
        for (Record record : create().select(
                PARAMETERS.PARAMETER_NAME,
                PARAMETERS.DATA_TYPE,
                PARAMETERS.CHARACTER_MAXIMUM_LENGTH,
                PARAMETERS.NUMERIC_PRECISION,
                PARAMETERS.NUMERIC_SCALE,
                PARAMETERS.UDT_NAME,
                PARAMETERS.ORDINAL_POSITION,
                PARAMETERS.PARAMETER_MODE,
                is94()
                    ? PARAMETERS.PARAMETER_DEFAULT
                    : inline((String) null).as(PARAMETERS.PARAMETER_DEFAULT))
            .from(PARAMETERS)
            .where(PARAMETERS.SPECIFIC_SCHEMA.equal(getSchema().getName()))
            .and(PARAMETERS.SPECIFIC_NAME.equal(specificName))
            .orderBy(PARAMETERS.ORDINAL_POSITION.asc())
            .fetch()) {

            String inOut = record.getValue(PARAMETERS.PARAMETER_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(PARAMETERS.DATA_TYPE),
                record.getValue(PARAMETERS.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(PARAMETERS.NUMERIC_PRECISION),
                record.getValue(PARAMETERS.NUMERIC_SCALE),
                null,
                record.getValue(PARAMETERS.PARAMETER_DEFAULT) != null,
                record.getValue(PARAMETERS.UDT_NAME)
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.getValue(PARAMETERS.PARAMETER_NAME),
                record.getValue(PARAMETERS.ORDINAL_POSITION),
                type,
                record.getValue(PARAMETERS.PARAMETER_DEFAULT) != null
            );

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }

    private boolean is94() {
        if (is94 == null) {

            // [#4254] INFORMATION_SCHEMA.PARAMETERS.PARAMETER_DEFAULT was added
            // in PostgreSQL 9.4 only
            try {
                create(true)
                    .select(PARAMETERS.PARAMETER_DEFAULT)
                    .from(PARAMETERS)
                    .where(falseCondition())
                    .fetch();

                is94 = true;
            }
            catch (DataAccessException e) {
                is94 = false;
            }
        }

        return is94;
    }
}
