/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util.postgres;


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
import org.jooq.util.postgres.information_schema.tables.Parameters;
import org.jooq.util.postgres.information_schema.tables.Routines;

/**
 * Postgres implementation of {@link AbstractRoutineDefinition}
 *
 * @author Lukas Eder
 */
public class PostgresRoutineDefinition extends AbstractRoutineDefinition {

    private final String specificName;

    public PostgresRoutineDefinition(Database database, Record record) {
        super(database,
            null,
            record.getValue(Routines.ROUTINE_NAME),
            null,
            record.getValueAsString("overload"));

        if (!Arrays.asList("void", "record").contains(record.getValue("data_type"))) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                record.getValueAsString("data_type"),
                record.getValue(Routines.NUMERIC_PRECISION),
                record.getValue(Routines.NUMERIC_SCALE),
                record.getValue(Routines.TYPE_UDT_NAME));

            returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }

        specificName = record.getValue(Routines.SPECIFIC_NAME);
    }

    @Override
    protected void init0() throws SQLException {
        for (Record record : create().select(
                Parameters.PARAMETER_NAME,
                Parameters.DATA_TYPE,
                Parameters.NUMERIC_PRECISION,
                Parameters.NUMERIC_SCALE,
                Parameters.UDT_NAME,
                Parameters.ORDINAL_POSITION,
                Parameters.PARAMETER_MODE)
            .from(Parameters.PARAMETERS)
            .where(Parameters.SPECIFIC_SCHEMA.equal(getSchemaName()))
            .and(Parameters.SPECIFIC_NAME.equal(specificName))
            .orderBy(Parameters.ORDINAL_POSITION.asc())
            .fetch()) {

            String inOut = record.getValue(Parameters.PARAMETER_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                record.getValue(Parameters.DATA_TYPE),
                record.getValue(Parameters.NUMERIC_PRECISION),
                record.getValue(Parameters.NUMERIC_SCALE),
                record.getValue(Parameters.UDT_NAME));

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.getValue(Parameters.PARAMETER_NAME),
                record.getValue(Parameters.ORDINAL_POSITION),
                type);

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }
}
