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
package org.jooq.util.hsqldb;

import static org.jooq.impl.DSL.nvl;
import static org.jooq.util.hsqldb.information_schema.Tables.ELEMENT_TYPES;
import static org.jooq.util.hsqldb.information_schema.Tables.PARAMETERS;
import static org.jooq.util.hsqldb.information_schema.Tables.ROUTINES;

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
 * HSQLDB implementation of {@link AbstractRoutineDefinition}
 *
 * @author Espen Stromsnes
 * @author Lukas Eder
 */
public class HSQLDBRoutineDefinition extends AbstractRoutineDefinition {

    private final String specificName; // internal name for the function used by HSQLDB

    public HSQLDBRoutineDefinition(SchemaDefinition schema, String name, String specificName, String dataType, Number precision, Number scale) {
        super(schema, null, name, null, null);

        if (!StringUtils.isBlank(dataType)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                precision,
                precision,
                scale,
                null,
                null
            );

            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }

        this.specificName = specificName;
    }

    @Override
    protected void init0() throws SQLException {
        Result<?> result = create().select(
                PARAMETERS.PARAMETER_MODE,
                PARAMETERS.PARAMETER_NAME,
                nvl(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER, PARAMETERS.DATA_TYPE).as("datatype"),
                PARAMETERS.CHARACTER_MAXIMUM_LENGTH,
                PARAMETERS.NUMERIC_PRECISION,
                PARAMETERS.NUMERIC_SCALE,
                PARAMETERS.ORDINAL_POSITION)
            .from(PARAMETERS)
            .join(ROUTINES)
            .on(PARAMETERS.SPECIFIC_SCHEMA.equal(ROUTINES.SPECIFIC_SCHEMA))
            .and(PARAMETERS.SPECIFIC_NAME.equal(ROUTINES.SPECIFIC_NAME))
            .leftOuterJoin(ELEMENT_TYPES)
            .on(ROUTINES.ROUTINE_SCHEMA.equal(ELEMENT_TYPES.OBJECT_SCHEMA))
            .and(ROUTINES.ROUTINE_NAME.equal(ELEMENT_TYPES.OBJECT_NAME))
            .and(PARAMETERS.DTD_IDENTIFIER.equal(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER))
            .where(PARAMETERS.SPECIFIC_SCHEMA.equal(getSchema().getName()))
            .and(PARAMETERS.SPECIFIC_NAME.equal(this.specificName))
            .orderBy(PARAMETERS.ORDINAL_POSITION.asc()).fetch();

        for (Record record : result) {
            String inOut = record.getValue(PARAMETERS.PARAMETER_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue("datatype", String.class),
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
