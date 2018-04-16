/*
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
package org.jooq.meta.hsqldb;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.val;
import static org.jooq.meta.hsqldb.information_schema.Tables.ELEMENT_TYPES;
import static org.jooq.meta.hsqldb.information_schema.Tables.PARAMETERS;
import static org.jooq.meta.hsqldb.information_schema.Tables.ROUTINES;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.meta.AbstractRoutineDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultParameterDefinition;
import org.jooq.meta.InOutDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.tools.StringUtils;

/**
 * HSQLDB implementation of {@link AbstractRoutineDefinition}
 *
 * @author Espen Stromsnes
 * @author Lukas Eder
 */
public class HSQLDBRoutineDefinition extends AbstractRoutineDefinition {

    private final String specificName; // internal name for the function used by HSQLDB

    public HSQLDBRoutineDefinition(SchemaDefinition schema, String name, String specificName, String dataType, Number precision, Number scale) {
        this(schema, name, specificName, dataType, precision, scale, false);
    }

    public HSQLDBRoutineDefinition(SchemaDefinition schema, String name, String specificName, String dataType, Number precision, Number scale, boolean aggregate) {
        super(schema, null, name, null, null, aggregate);

        if (!StringUtils.isBlank(dataType)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                precision,
                precision,
                scale,
                null,
                (String) null
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

            // [#3015] HSQLDB user-defined AGGREGATE functions have four parameters, but only one
            // is relevant to client code
            .and(condition(val(!isAggregate())).or(PARAMETERS.ORDINAL_POSITION.eq(1L)))
            .orderBy(PARAMETERS.ORDINAL_POSITION.asc()).fetch();

        for (Record record : result) {
            String inOut = record.get(PARAMETERS.PARAMETER_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get("datatype", String.class),
                record.get(PARAMETERS.CHARACTER_MAXIMUM_LENGTH),
                record.get(PARAMETERS.NUMERIC_PRECISION),
                record.get(PARAMETERS.NUMERIC_SCALE),
                null,
                (String) null
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.get(PARAMETERS.PARAMETER_NAME).replaceAll("@", ""),
                record.get(PARAMETERS.ORDINAL_POSITION, int.class),
                type
            );

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }
}
