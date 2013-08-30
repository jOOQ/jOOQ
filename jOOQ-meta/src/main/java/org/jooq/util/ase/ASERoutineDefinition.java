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
