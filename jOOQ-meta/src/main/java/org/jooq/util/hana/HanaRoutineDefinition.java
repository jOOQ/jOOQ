/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.util.hana;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.util.hana.sys.Tables.FUNCTION_PARAMETERS;
import static org.jooq.util.hana.sys.Tables.PROCEDURE_PARAMETERS;

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
import org.jooq.util.hana.sys.tables.FunctionParameters;
import org.jooq.util.hana.sys.tables.ProcedureParameters;

/**
 * Hana implementation of {@link AbstractRoutineDefinition}
 *
 * @author Lukas Eder
 */
public class HanaRoutineDefinition extends AbstractRoutineDefinition {

    private final Long oid;

    public HanaRoutineDefinition(SchemaDefinition schema, String name, Long oid, String dataType, Number precision, Number scale) {
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

        this.oid = oid;
    }

    @Override
    protected void init0() throws SQLException {
        ProcedureParameters pp = PROCEDURE_PARAMETERS;
        FunctionParameters fp = FUNCTION_PARAMETERS;

        for (Record record : create()
            .select(
                pp.PARAMETER_TYPE,
                pp.PARAMETER_NAME,
                pp.DATA_TYPE_NAME,
                pp.LENGTH,
                pp.SCALE,
                pp.POSITION)
            .from(pp)
            .where(pp.PROCEDURE_OID.eq(oid))
            .and(pp.SCHEMA_NAME.eq(getSchema().getName()))
            .and(pp.PROCEDURE_NAME.eq(getName()))
            .unionAll(
                select(
                    fp.PARAMETER_TYPE,
                    fp.PARAMETER_NAME,
                    fp.DATA_TYPE_NAME,
                    fp.LENGTH,
                    fp.SCALE,
                    fp.POSITION)
                .from(fp)
                .where(fp.PARAMETER_TYPE.ne(inline("RETURN")))
                .and(fp.SCHEMA_NAME.eq(getSchema().getName()))
                .and(fp.FUNCTION_NAME.eq(getName()))
            )
            .orderBy(field(name(pp.POSITION.getName())))) {

            String inOut = record.getValue(pp.PARAMETER_TYPE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(pp.DATA_TYPE_NAME),
                record.getValue(pp.LENGTH),
                record.getValue(pp.LENGTH),
                record.getValue(pp.SCALE),
                null,
                null
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.getValue(pp.PARAMETER_NAME),
                record.getValue(pp.POSITION, int.class),
                type
            );

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }
}
