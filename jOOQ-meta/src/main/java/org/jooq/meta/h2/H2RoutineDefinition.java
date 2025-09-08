/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.meta.h2;


import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.meta.h2.H2Database.MAX_VARCHAR_LENGTH;
import static org.jooq.meta.h2.information_schema.Tables.FUNCTION_COLUMNS;
import static org.jooq.meta.hsqldb.information_schema.Tables.ELEMENT_TYPES;
import static org.jooq.meta.hsqldb.information_schema.Tables.PARAMETERS;
import static org.jooq.meta.hsqldb.information_schema.Tables.ROUTINES;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractRoutineDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultParameterDefinition;
import org.jooq.meta.InOutDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.tools.StringUtils;
import org.jooq.util.h2.H2DataType;

/**
 * H2 implementation of {@link AbstractRoutineDefinition}
 *
 * @author Espen Stromsnes
 * @author Lukas Eder
 */
public class H2RoutineDefinition extends AbstractRoutineDefinition {

    private final String specificName; // internal name for the function used by H2

    public H2RoutineDefinition(SchemaDefinition schema, String name, String specificName, String dataType, Number precision, Number scale, String comment, String overload) {
        super(schema, null, name, comment, overload);

        if (!StringUtils.isBlank(dataType)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
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
        if (((H2Database) getDatabase()).is2_0_202())
            init2_0();
        else
            init1_4();
    }

    private void init2_0() {
        Result<?> result = create().select(
                PARAMETERS.PARAMETER_MODE,
                PARAMETERS.PARAMETER_NAME,
                nvl(concat(ELEMENT_TYPES.DATA_TYPE, inline(" ARRAY")), PARAMETERS.DATA_TYPE).as(PARAMETERS.DATA_TYPE),
                nullif(nvl(ELEMENT_TYPES.CHARACTER_MAXIMUM_LENGTH, PARAMETERS.CHARACTER_MAXIMUM_LENGTH), inline(MAX_VARCHAR_LENGTH)).as(PARAMETERS.CHARACTER_MAXIMUM_LENGTH),
                nvl(ELEMENT_TYPES.NUMERIC_PRECISION, PARAMETERS.NUMERIC_PRECISION).as(PARAMETERS.NUMERIC_PRECISION),
                nvl(ELEMENT_TYPES.NUMERIC_SCALE, PARAMETERS.NUMERIC_SCALE).as(PARAMETERS.NUMERIC_SCALE),
                PARAMETERS.ORDINAL_POSITION)
            .from(PARAMETERS)
                .join(ROUTINES)
                    .on(PARAMETERS.SPECIFIC_SCHEMA.eq(ROUTINES.SPECIFIC_SCHEMA))
                    .and(PARAMETERS.SPECIFIC_NAME.eq(ROUTINES.SPECIFIC_NAME))
                .leftJoin(ELEMENT_TYPES)
                    .on(PARAMETERS.SPECIFIC_SCHEMA.eq(ELEMENT_TYPES.OBJECT_SCHEMA))
                    .and(PARAMETERS.SPECIFIC_NAME.eq(ELEMENT_TYPES.OBJECT_NAME))
                    .and(PARAMETERS.DTD_IDENTIFIER.eq(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER))
            .where(PARAMETERS.SPECIFIC_SCHEMA.eq(getSchema().getName()))
            .and(PARAMETERS.SPECIFIC_NAME.eq(specificName))
            .orderBy(PARAMETERS.ORDINAL_POSITION.asc()).fetch();

        for (Record record : result) {
            String inOut = record.get(PARAMETERS.PARAMETER_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get(PARAMETERS.DATA_TYPE),
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

    private void init1_4() {
        for (Record record : create()
                .select(
                    FUNCTION_COLUMNS.COLUMN_NAME,
                    FUNCTION_COLUMNS.TYPE_NAME,
                    FUNCTION_COLUMNS.PRECISION,
                    FUNCTION_COLUMNS.SCALE,
                    FUNCTION_COLUMNS.POS,
                    FUNCTION_COLUMNS.NULLABLE,
                    FUNCTION_COLUMNS.COLUMN_DEFAULT)
                .from(FUNCTION_COLUMNS)
                .where(FUNCTION_COLUMNS.ALIAS_SCHEMA.equal(getSchema().getName()))
                .and(FUNCTION_COLUMNS.ALIAS_NAME.equal(getName()))

                // [#4193] recent versions of H2 produce a row for the function
                // return value at position 0
                .and(FUNCTION_COLUMNS.POS.gt(0))
                .and(getOverload() == null
                    ? noCondition()
                    : FUNCTION_COLUMNS.COLUMN_COUNT.eq(FUNCTION_COLUMNS.COLUMN_COUNT.getDataType().convert(getOverload())))
                .orderBy(FUNCTION_COLUMNS.POS.asc()).fetch()) {

            String paramName = record.get(FUNCTION_COLUMNS.COLUMN_NAME);
            String typeName = record.get(FUNCTION_COLUMNS.TYPE_NAME);
            Integer precision = record.get(FUNCTION_COLUMNS.PRECISION);
            Short scale = record.get(FUNCTION_COLUMNS.SCALE);
            int position = record.get(FUNCTION_COLUMNS.POS);
            boolean nullable = record.get(FUNCTION_COLUMNS.NULLABLE, boolean.class);
            String defaultValue = record.get(FUNCTION_COLUMNS.COLUMN_DEFAULT);

            // VERY special case for H2 alias/function parameters. The first parameter
            // may be a java.sql.Connection object and in such cases it should NEVER be used.
            // It is only used internally by H2 to provide a connection to the current database.
            if (position == 0 && H2DataType.OTHER.getTypeName().equalsIgnoreCase(typeName)) {
                continue;
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(), typeName,
                precision,
                precision,
                scale,
                nullable,
                defaultValue
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(this, paramName, position, type);
            addParameter(InOutDefinition.IN, parameter);
        }
    }
}
