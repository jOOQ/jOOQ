/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.util.sybase.sys.tables.Sysdomain.SYSDOMAIN;
import static org.jooq.util.sybase.sys.tables.Sysprocedure.SYSPROCEDURE;
import static org.jooq.util.sybase.sys.tables.Sysprocparm.SYSPROCPARM;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.util.AbstractFunctionDefinition;
import org.jooq.util.AbstractProcedureDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.sybase.sys.tables.Sysdomain;
import org.jooq.util.sybase.sys.tables.Sysprocedure;
import org.jooq.util.sybase.sys.tables.Sysprocparm;

/**
 * Sybase implementation of {@link AbstractProcedureDefinition}
 *
 * @author Espen Stromsnes
 */
public class SybaseFunctionDefinition extends AbstractFunctionDefinition {

    public SybaseFunctionDefinition(Database database, PackageDefinition pkg, String name) {
        super(database, pkg, name, null, null);
    }

    @Override
    protected void init0() throws SQLException {
        for (Record record : create().select(
                    Sysprocparm.PARM_NAME,
                    Sysdomain.DOMAIN_NAME,
                    Sysprocparm.WIDTH,
                    Sysprocparm.SCALE,
                    Sysprocparm.PARM_ID,
                    Sysprocparm.PARM_TYPE)
                .from(SYSPROCPARM)
                .join(SYSDOMAIN).on(Sysprocparm.DOMAIN_ID.equal(Sysdomain.DOMAIN_ID))
                .join(SYSPROCEDURE).on(Sysprocparm.PROC_ID.equal(Sysprocedure.PROC_ID))
                .where(Sysprocedure.PROC_NAME.equal(getName()))
                .orderBy(Sysprocparm.PARM_ID)
                .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                record.getValue(Sysdomain.DOMAIN_NAME),
                record.getValue(Sysprocparm.WIDTH),
                record.getValue(Sysprocparm.SCALE));

            String parmName = record.getValue(Sysprocparm.PARM_NAME);

            int parmId = record.getValue(Sysprocparm.PARM_ID);
            int parmType = record.getValue(Sysprocparm.PARM_TYPE);

            if (parmType == 0) {
                ParameterDefinition parameter = new DefaultParameterDefinition(this, parmName, parmId, type);
                getInParameters().add(parameter);
            } else if (parmType == 4) {
                this.returnValue = new DefaultParameterDefinition(this, parmName, -1, type);
            } else {
                throw new IllegalArgumentException("Unsupported parmtype: " + parmType);
            }
        }
    }
}
