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
 */
package org.jooq.util.h2;


import static org.jooq.util.h2.information_schema.tables.FunctionColumns.FUNCTION_COLUMNS;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.h2.information_schema.tables.FunctionColumns;

/**
 * H2 implementation of {@link AbstractRoutineDefinition}
 *
 * @author Espen Stromsnes
 * @author Lukas Eder
 */
public class H2RoutineDefinition extends AbstractRoutineDefinition {

    public H2RoutineDefinition(SchemaDefinition schema, String name, String comment, String typeName, Number precision, Number scale) {
        super(schema, null, name, comment, null);

        if (!StringUtils.isBlank(typeName)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
                typeName,
                precision,
                precision,
                scale,
                null,
                (String) null
            );

            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }
    }

    @Override
    protected void init0() throws SQLException {
        for (Record record : create()
                .select(
                    FunctionColumns.COLUMN_NAME,
                    FunctionColumns.TYPE_NAME,
                    FunctionColumns.PRECISION,
                    FunctionColumns.SCALE,
                    FunctionColumns.POS,
                    FunctionColumns.NULLABLE,
                    FunctionColumns.COLUMN_DEFAULT)
                .from(FUNCTION_COLUMNS)
                .where(FunctionColumns.ALIAS_SCHEMA.equal(getSchema().getName()))
                .and(FunctionColumns.ALIAS_NAME.equal(getName()))

                // [#4193] recent versions of H2 produce a row for the function
                // return value at position 0
                .and(FunctionColumns.POS.gt(0))
                .orderBy(FunctionColumns.POS.asc()).fetch()) {

            String paramName = record.get(FunctionColumns.COLUMN_NAME);
            String typeName = record.get(FunctionColumns.TYPE_NAME);
            Integer precision = record.get(FunctionColumns.PRECISION);
            Short scale = record.get(FunctionColumns.SCALE);
            int position = record.get(FunctionColumns.POS);
            boolean nullable = record.get(FunctionColumns.NULLABLE, boolean.class);
            String defaultValue = record.get(FunctionColumns.COLUMN_DEFAULT);

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
