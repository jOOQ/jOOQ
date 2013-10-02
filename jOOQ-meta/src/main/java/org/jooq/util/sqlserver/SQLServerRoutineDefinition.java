/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.util.sqlserver;

import static org.jooq.util.sqlserver.information_schema.Tables.PARAMETERS;
import static org.jooq.util.sqlserver.information_schema.Tables.ROUTINES;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class SQLServerRoutineDefinition extends AbstractRoutineDefinition {

    /**
     * internal name for the function used by HSQLDB / SQL Server
     */
    private final String specificName;

    public SQLServerRoutineDefinition(SchemaDefinition schema, String name, String specificName, String dataType, Number length, Number precision, Number scale) {
        super(schema, null, name, null, null);

        if (!StringUtils.isBlank(dataType)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), schema, dataType, length, precision, scale, null, null);
            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }

        this.specificName = specificName;
    }

    @Override
    protected void init0() throws SQLException {
        Result<?> result = create().selectDistinct(
                PARAMETERS.PARAMETER_MODE,
                PARAMETERS.PARAMETER_NAME,
                PARAMETERS.DATA_TYPE,
                PARAMETERS.CHARACTER_MAXIMUM_LENGTH,
                PARAMETERS.NUMERIC_PRECISION,
                PARAMETERS.NUMERIC_SCALE,
                PARAMETERS.ORDINAL_POSITION,
                PARAMETERS.IS_RESULT)
            .from(PARAMETERS)
            .join(ROUTINES)
            .on(PARAMETERS.SPECIFIC_SCHEMA.equal(ROUTINES.SPECIFIC_SCHEMA))
            .and(PARAMETERS.SPECIFIC_NAME.equal(ROUTINES.SPECIFIC_NAME))
            .where(PARAMETERS.SPECIFIC_SCHEMA.equal(getSchema().getName()))
            .and(PARAMETERS.SPECIFIC_NAME.equal(this.specificName))
            .and(PARAMETERS.IS_RESULT.isFalse())
            .orderBy(PARAMETERS.ORDINAL_POSITION.asc()).fetch();

        for (Record record : result) {
            String inOut = record.getValue(PARAMETERS.PARAMETER_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(PARAMETERS.DATA_TYPE),
                record.getValue(PARAMETERS.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(PARAMETERS.NUMERIC_PRECISION),
                record.getValue(PARAMETERS.NUMERIC_SCALE),
                null,
                null
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.getValue(PARAMETERS.PARAMETER_NAME).replaceAll("@", ""),
                record.getValue(PARAMETERS.ORDINAL_POSITION, int.class),
                type
            );

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }
}
