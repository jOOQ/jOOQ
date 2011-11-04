/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
 * . Neither the name "jOOQ" nor the names of its contributors may be
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
package org.jooq.util.db2;

import static org.jooq.util.db2.syscat.tables.Funcparms.FUNCPARMS;
import static org.jooq.util.db2.syscat.tables.Functions.FUNCTIONS;
import static org.jooq.util.db2.syscat.tables.Procparms.PROCPARMS;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.db2.syscat.tables.Funcparms;
import org.jooq.util.db2.syscat.tables.Functions;
import org.jooq.util.db2.syscat.tables.Procparms;

/**
 * DB2 implementation of {@link AbstractRoutineDefinition}
 *
 * @author Espen Stromsnes
 * @author Lukas Eder
 */
public class DB2RoutineDefinition extends AbstractRoutineDefinition {

    private final boolean isProcedure;

    public DB2RoutineDefinition(Database database, String name, String comment, boolean isProcedure) {
        super(database, null, name, comment, null);

        this.isProcedure = isProcedure;
    }

    @Override
    protected void init0() throws SQLException {
        if (isProcedure) {
            initP();
        }
        else {
            initF();
        }
    }

    private void initF() {
        for (Record record : create()
                .select(
                    Funcparms.ROWTYPE,
                    Funcparms.TYPENAME,
                    Funcparms.LENGTH,
                    Funcparms.SCALE,
                    Funcparms.ORDINAL,
                    Funcparms.PARMNAME)
                .from(FUNCPARMS)
                .join(FUNCTIONS)
                .on(
                    Funcparms.FUNCSCHEMA.equal(Functions.FUNCSCHEMA),
                    Funcparms.FUNCNAME.equal(Functions.FUNCNAME))
                .where(
                    Funcparms.FUNCSCHEMA.equal(getSchemaName()),
                    Funcparms.FUNCNAME.equal(getName()),
                    Functions.ORIGIN.equal("Q"))
                .orderBy(
                    Funcparms.FUNCNAME.asc(),
                    Funcparms.ORDINAL.asc())
                .fetch()) {

            String rowType = record.getValue(Funcparms.ROWTYPE);
            String dataType = record.getValue(Funcparms.TYPENAME);
            Integer precision = record.getValue(Funcparms.LENGTH);
            Short scale = record.getValue(Funcparms.SCALE);
            int position = record.getValue(Funcparms.ORDINAL);
            String paramName = record.getValue(Funcparms.PARMNAME);

            // result after casting
            if ("C".equals(rowType)) {
                DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), dataType, precision, scale);
                addParameter(InOutDefinition.RETURN, new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type));
            }

            // parameter
            else if ("P".equals(rowType)) {
                DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), dataType, precision, scale);
                ParameterDefinition column = new DefaultParameterDefinition(this, paramName, position, type);

                addParameter(InOutDefinition.IN, column);
            }

            // result before casting
            else {
            }
        }
    }

    private void initP() {
        for (Record record : create().select(
                    Procparms.PARMNAME,
                    Procparms.TYPENAME,
                    Procparms.LENGTH,
                    Procparms.SCALE,
                    Procparms.ORDINAL,
                    Procparms.PARM_MODE)
                .from(PROCPARMS)
                .where(Procparms.PROCSCHEMA.equal(getSchemaName()))
                .and(Procparms.PROCNAME.equal(getName()))
                .orderBy(Procparms.ORDINAL).fetch()) {

            String paramMode = record.getValue(Procparms.PARM_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                record.getValue(Procparms.TYPENAME),
                record.getValue(Procparms.LENGTH),
                record.getValue(Procparms.SCALE));

            addParameter(
                InOutDefinition.getFromString(paramMode),
                new DefaultParameterDefinition(
                    this,
                    record.getValue(Procparms.PARMNAME),
                    record.getValue(Procparms.ORDINAL),
                    type));
        }
    }
}
