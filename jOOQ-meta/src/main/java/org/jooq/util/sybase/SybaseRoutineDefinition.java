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
 * and Maintenance Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.util.sybase;

import static org.jooq.util.sybase.sys.Tables.SYSDOMAIN;
import static org.jooq.util.sybase.sys.Tables.SYSPROCEDURE;
import static org.jooq.util.sybase.sys.Tables.SYSPROCPARM;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * Sybase implementation of {@link AbstractRoutineDefinition}
 *
 * @author Espen Stromsnes
 * @author Lukas Eder
 */
public class SybaseRoutineDefinition extends AbstractRoutineDefinition {

    public SybaseRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, String name) {
        super(schema, pkg, name, null, null);
    }

    @Override
    protected void init0() throws SQLException {
        for (Record record : create().select(
                    SYSPROCPARM.PARM_NAME,
                    SYSDOMAIN.DOMAIN_NAME,
                    SYSPROCPARM.WIDTH,
                    SYSPROCPARM.SCALE,
                    SYSPROCPARM.PARM_ID,
                    SYSPROCPARM.PARM_TYPE,
                    SYSPROCPARM.PARM_MODE_IN,
                    SYSPROCPARM.PARM_MODE_OUT,
                    SYSPROCPARM.DEFAULT)
                .from(SYSPROCPARM)
                .join(SYSDOMAIN).on(SYSPROCPARM.DOMAIN_ID.equal(SYSDOMAIN.DOMAIN_ID))
                .join(SYSPROCEDURE).on(SYSPROCPARM.PROC_ID.equal(SYSPROCEDURE.PROC_ID))
                .where(SYSPROCEDURE.PROC_NAME.equal(getName()))
                .orderBy(SYSPROCPARM.PARM_ID)
                .fetch()) {

            String paramName = record.getValue(SYSPROCPARM.PARM_NAME);
            boolean paramModeIn = record.getValue(SYSPROCPARM.PARM_MODE_IN, boolean.class);
            boolean paramModeOut = record.getValue(SYSPROCPARM.PARM_MODE_OUT, boolean.class);
            int parmType = record.getValue(SYSPROCPARM.PARM_TYPE);

            InOutDefinition inOutDefinition;
            if (parmType == 4) {
                inOutDefinition = InOutDefinition.RETURN;
                paramName = "RETURN_VALUE";
            }
            else if (paramModeIn && paramModeOut) {
                inOutDefinition = InOutDefinition.INOUT;
            }
            else if (paramModeIn) {
                inOutDefinition = InOutDefinition.IN;
            }
            else if (paramModeOut) {
                inOutDefinition = InOutDefinition.OUT;
            }
            else {
                throw new IllegalArgumentException("Stored procedure param is neither in or out mode!");
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(SYSDOMAIN.DOMAIN_NAME),
                record.getValue(SYSPROCPARM.WIDTH),
                record.getValue(SYSPROCPARM.WIDTH),
                record.getValue(SYSPROCPARM.SCALE),
                true,
                record.getValue(SYSPROCPARM.DEFAULT) != null
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(this,
                paramName,
                record.getValue(SYSPROCPARM.PARM_ID),
                type);

            addParameter(inOutDefinition, parameter);
        }
    }
}
