package org.jooq.util.postgresplus;

import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.jooq.util.*;
import org.jooq.util.postgres.information_schema.tables.Parameters;

import java.sql.SQLException;
import java.util.Arrays;

import static org.jooq.util.postgresplus.pg_catalog.Tables.*;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.util.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.util.postgres.information_schema.Tables.ROUTINES;

/**
 * Created by rbellamy on 1/4/16.
 */
public class PostgresPlusRoutineDefinition extends AbstractRoutineDefinition {

    private final String specificName;
    private Boolean is94;

    public PostgresPlusRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, Record record) {
        super(schema,
                pkg,
                record.getValue(ROUTINES.ROUTINE_NAME),
                null,
                record.getValue("overload", String.class),
                record.getValue(PG_PROC.PROISAGG));

        if (!Arrays.asList("void", "record").contains(record.getValue("data_type", String.class))) {
            SchemaDefinition typeSchema = null;

            String schemaName = record.getValue(ROUTINES.TYPE_UDT_SCHEMA);
            if (schemaName != null)
                typeSchema = getDatabase().getSchema(schemaName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    typeSchema == null
                            ? getDatabase().getSchema(record.getValue(PG_NAMESPACE.NSPNAME))
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

        this.specificName = record.getValue(ROUTINES.SPECIFIC_NAME);
    }

    @Override
    protected void init0() throws SQLException {

        Parameters p = PARAMETERS.as("p");

        for (Record record : create().select(
                p.PARAMETER_NAME,
                p.DATA_TYPE,
                p.CHARACTER_MAXIMUM_LENGTH,
                p.NUMERIC_PRECISION,
                p.NUMERIC_SCALE,
                p.UDT_NAME,
                p.ORDINAL_POSITION,
                p.PARAMETER_MODE,
                is94()
                        ? p.PARAMETER_DEFAULT
                        : inline((String) null).as(p.PARAMETER_DEFAULT))
                .from(p)
                .where(p.SPECIFIC_NAME.equal(specificName))
                .orderBy(p.ORDINAL_POSITION.asc())
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
    }}
