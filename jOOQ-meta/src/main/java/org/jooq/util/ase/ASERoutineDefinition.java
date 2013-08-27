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
package org.jooq.util.ase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * [#1507] This was contributed by user Mark. It will be correctly implemented at a later stage
 *
 * @author Mark
 */
@SuppressWarnings("unused")
class ASERoutineDefinition extends AbstractRoutineDefinition {

    public ASERoutineDefinition(SchemaDefinition schema, String name, String dataType, Number length, Number precision, Number scale) {
        super(schema, null, name, null, null);

        if (!StringUtils.isBlank(dataType)) {
            throw new InstantiationError("datatype argument not supported for ASE routines");
        }
    }

    @Override
    protected void init0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();
        List<Result<Record>> fetchMany = create().fetchMany("sp_help '" + getName() + "'");
        if(fetchMany.size() < 2) {
            return;
        }

        for (Record record : fetchMany.get(1)) {
            String n = record.getValue("Parameter_name", String.class);
            String l = record.getValue("Length", String.class);
            String p = record.getValue("Prec", String.class);
            String s = record.getValue("Scale", String.class);
            int o = record.getValue("Param_order", int.class);
            String inOut = record.getValue("Mode", String.class);

            int length = 0;
            int precision = 0;
            int scale = 0;

            if (l != null && !"null".equalsIgnoreCase(l.trim())) {
                length = Integer.valueOf(l.trim());
            }
            if (p != null && !"null".equalsIgnoreCase(p.trim())) {
                precision = Integer.valueOf(p.trim());
            }
            if (s != null && !"null".equalsIgnoreCase(s.trim())) {
                scale = Integer.valueOf(s.trim());
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    getSchema(),
                    record.getValue("Type", String.class),
                    length,
                    precision,
                    scale,
                    null,
                    null
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                    this,
                    n.replaceAll("@", ""),
                    o,
                    type
            );

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }
}
