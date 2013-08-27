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
package org.jooq.util.db2;

import static org.jooq.util.db2.syscat.tables.Funcparms.FUNCPARMS;
import static org.jooq.util.db2.syscat.tables.Functions.FUNCTIONS;
import static org.jooq.util.db2.syscat.tables.Procparms.PROCPARMS;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;
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

    public DB2RoutineDefinition(SchemaDefinition schema, String name, String comment, boolean isProcedure) {
        super(schema, null, name, comment, null);

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
                    Funcparms.FUNCSCHEMA.equal(getSchema().getName()),
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

                addParameter(InOutDefinition.RETURN, new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type));
            }

            // parameter
            else if ("P".equals(rowType)) {
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
                .where(Procparms.PROCSCHEMA.equal(getSchema().getName()))
                .and(Procparms.PROCNAME.equal(getName()))
                .orderBy(Procparms.ORDINAL).fetch()) {

            String paramMode = record.getValue(Procparms.PARM_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(Procparms.TYPENAME),
                record.getValue(Procparms.LENGTH),
                record.getValue(Procparms.LENGTH),
                record.getValue(Procparms.SCALE),
                null,
                null
            );

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
