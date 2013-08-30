/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
