/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name of the "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
                    SYSPROCPARM.PARM_MODE_OUT)
                .from(SYSPROCPARM)
                .join(SYSDOMAIN).on(SYSPROCPARM.DOMAIN_ID.equal(SYSDOMAIN.DOMAIN_ID))
                .join(SYSPROCEDURE).on(SYSPROCPARM.PROC_ID.equal(SYSPROCEDURE.PROC_ID))
                .where(SYSPROCEDURE.PROC_NAME.equal(getName()))
                .orderBy(SYSPROCPARM.PARM_ID)
                .fetch()) {

            String paramName = record.getValue(SYSPROCPARM.PARM_NAME);
            Boolean paramModeIn = record.getValueAsBoolean(SYSPROCPARM.PARM_MODE_IN, false);
            Boolean paramModeOut = record.getValueAsBoolean(SYSPROCPARM.PARM_MODE_OUT, false);
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
                record.getValue(SYSPROCPARM.SCALE));

            ParameterDefinition parameter = new DefaultParameterDefinition(this,
                paramName,
                record.getValue(SYSPROCPARM.PARM_ID),
                type);

            addParameter(inOutDefinition, parameter);
        }
    }
}
