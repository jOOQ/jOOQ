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
