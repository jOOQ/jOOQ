/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0 (the "License"); You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
                    scale);

            ParameterDefinition parameter = new DefaultParameterDefinition(
                    this,
                    n.replaceAll("@", ""),
                    o,
                    type);

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }
}
