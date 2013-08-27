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
package org.jooq.util.postgres;


import static org.jooq.util.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.util.postgres.information_schema.Tables.ROUTINES;

import java.sql.SQLException;
import java.util.Arrays;

import org.jooq.Record;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;

/**
 * Postgres implementation of {@link AbstractRoutineDefinition}
 *
 * @author Lukas Eder
 */
public class PostgresRoutineDefinition extends AbstractRoutineDefinition {

    private final String specificName;

    public PostgresRoutineDefinition(Database database, Record record) {
        super(database.getSchema(record.getValue(ROUTINES.ROUTINE_SCHEMA)),
            null,
            record.getValue(ROUTINES.ROUTINE_NAME),
            null,
            record.getValue("overload", String.class));

        if (!Arrays.asList("void", "record").contains(record.getValue("data_type"))) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                database.getSchema(record.getValue(ROUTINES.ROUTINE_SCHEMA)),
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
                PARAMETERS.PARAMETER_MODE)
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
                null,
                record.getValue(PARAMETERS.UDT_NAME)
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.getValue(PARAMETERS.PARAMETER_NAME),
                record.getValue(PARAMETERS.ORDINAL_POSITION),
                type
            );

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }
}
