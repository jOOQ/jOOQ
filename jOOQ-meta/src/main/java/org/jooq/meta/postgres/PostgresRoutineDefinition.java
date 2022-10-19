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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.meta.postgres;


import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.when;
import static org.jooq.meta.postgres.information_schema.Tables.COLUMNS;
import static org.jooq.meta.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.meta.postgres.information_schema.Tables.ROUTINES;

import java.sql.SQLException;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.meta.AbstractRoutineDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.Database;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultParameterDefinition;
import org.jooq.meta.InOutDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.postgres.information_schema.tables.Parameters;
import org.jooq.tools.StringUtils;

/**
 * Postgres implementation of {@link AbstractRoutineDefinition}
 *
 * @author Lukas Eder
 */
public class PostgresRoutineDefinition extends AbstractRoutineDefinition {

    private final String  specificName;
    private final boolean isProcedure;

    public PostgresRoutineDefinition(Database database, Record record) {
        super(database.getSchema(record.get(ROUTINES.ROUTINE_SCHEMA)),
            null,
            record.get(ROUTINES.ROUTINE_NAME),
            null,
            record.get("overload", String.class),
            record.get("is_agg", boolean.class)
        );

        String dataType = record.get("data_type", String.class);
        if (dataType != null && !asList("void", "record").contains(dataType)) {
            SchemaDefinition typeSchema = null;

            String schemaName = record.get(ROUTINES.TYPE_UDT_SCHEMA);
            if (schemaName != null)
                typeSchema = getDatabase().getSchema(schemaName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                typeSchema == null
                    ? database.getSchema(record.get(ROUTINES.ROUTINE_SCHEMA))
                    : typeSchema,
                dataType,
                record.get(ROUTINES.CHARACTER_MAXIMUM_LENGTH),
                record.get(ROUTINES.NUMERIC_PRECISION),
                record.get(ROUTINES.NUMERIC_SCALE),
                null,
                (String) null,
                name(
                    record.get(ROUTINES.TYPE_UDT_SCHEMA),
                    record.get(ROUTINES.TYPE_UDT_NAME)
                )
            );

            returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }

        specificName = record.get(ROUTINES.SPECIFIC_NAME);
        isProcedure = "PROCEDURE".equalsIgnoreCase(record.get(ROUTINES.ROUTINE_TYPE));
    }

    // [#3375] This internal constructor is used for table-valued functions. It should not be used otherwise
    PostgresRoutineDefinition(Database database, String schema, String name, String specificName) {
        super(database.getSchema(schema), null, name, null, null);

        this.specificName = specificName;
        this.isProcedure = false;
    }

    @Override
    protected void init0() throws SQLException {
        Parameters p = PARAMETERS;
        Field<Integer> count = count()
            .filterWhere(p.PARAMETER_NAME.ne(inline("")))
            .over(partitionBy(p.SPECIFIC_NAME, p.PARAMETER_NAME));
        Field<Integer> c = count.as("c");

        for (Record record : create().select(
                p.PARAMETER_NAME,
                when(p.DATA_TYPE.eq(inline("USER-DEFINED")).and(p.UDT_NAME.eq(inline("geometry"))), inline("geometry"))
                    .when(p.DATA_TYPE.eq(inline("ARRAY")), substring(p.UDT_NAME, inline(2)).concat(inline(" ARRAY")))
                    .else_(p.DATA_TYPE)
                    .as(p.DATA_TYPE),
                p.CHARACTER_MAXIMUM_LENGTH,
                pNumericPrecision(p).as(p.NUMERIC_PRECISION),
                p.NUMERIC_SCALE,
                p.UDT_SCHEMA,
                when(p.DATA_TYPE.eq(inline("ARRAY")), substring(p.UDT_NAME, inline(2)))
                    .else_(p.UDT_NAME).as(p.UDT_NAME),
                p.ORDINAL_POSITION,
                p.PARAMETER_MODE,
                ((PostgresDatabase) getDatabase()).is94()
                    ? p.PARAMETER_DEFAULT
                    : inline((String) null).as(p.PARAMETER_DEFAULT),
                c
            )
            .from(p)
            .where(p.SPECIFIC_SCHEMA.equal(getSchema().getName()))
            .and(p.SPECIFIC_NAME.equal(specificName))
            .orderBy(p.ORDINAL_POSITION.asc())) {

            String parameterName = record.get(p.PARAMETER_NAME);
            String inOut = record.get(p.PARAMETER_MODE);
            SchemaDefinition typeSchema = null;

            String schemaName = record.get(p.UDT_SCHEMA);
            if (schemaName != null)
                typeSchema = getDatabase().getSchema(schemaName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                typeSchema,
                record.get(p.DATA_TYPE),
                record.get(p.CHARACTER_MAXIMUM_LENGTH),
                record.get(p.NUMERIC_PRECISION),
                record.get(p.NUMERIC_SCALE),
                null,
                record.get(p.PARAMETER_DEFAULT),
                name(
                    record.get(p.UDT_SCHEMA),
                    record.get(p.UDT_NAME)
                )
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                parameterName,
                record.get(p.ORDINAL_POSITION),
                type,
                record.get(p.PARAMETER_DEFAULT) != null,
                StringUtils.isBlank(parameterName),
                "",
                record.get(c) > 1 ? record.get(p.ORDINAL_POSITION, String.class) : null
            );

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }

    static Field<Integer> pNumericPrecision(Parameters p) {
        // [#12048] [#12612] TODO: Maintain whether we know the precision or not

        return when(p.NUMERIC_PRECISION.isNull().and(p.DATA_TYPE.in(
            inline("time"),
            inline("timetz"),
            inline("time without time zone"),
            inline("time with time zone"),
            inline("timestamp"),
            inline("timestamptz"),
            inline("timestamp without time zone"),
            inline("timestamp with time zone"))), inline(6))
        .else_(p.NUMERIC_PRECISION);
    }

    public boolean isProcedure() {
        return isProcedure;
    }
}
