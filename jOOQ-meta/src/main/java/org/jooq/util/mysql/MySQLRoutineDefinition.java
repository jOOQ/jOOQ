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

package org.jooq.util.mysql;

import java.sql.SQLException;
import java.util.regex.Matcher;

import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class MySQLRoutineDefinition extends AbstractRoutineDefinition {

    private final String params;
    private final String returns;

	public MySQLRoutineDefinition(SchemaDefinition schema, String name, String comment, String params, String returns) {
		super(schema, null, name, comment, null);

		this.params = params;
		this.returns = returns;
	}

    @Override
    protected void init0() throws SQLException {
        // [#738] Avoid matching commas that appear in types, for instance DECIMAL(2, 1)
        String[] split = params.split(",(?!\\s*\\d+\\s*\\))");

        Matcher matcher = TYPE_PATTERN.matcher(returns);
        if (matcher.find()) {
            addParameter(InOutDefinition.RETURN, createParameter(matcher, 0, -1, "RETURN_VALUE"));
        }

        for (int i = 0; i < split.length; i++) {
            String param = split[i];

            // TODO [#742] : Use the INFORMATION_SCHEMA.PARAMETERS dictionary view instead.
            // It's much more reliable, than mysql.proc pattern matching...

            param = param.trim();
            matcher = PARAMETER_PATTERN.matcher(param);
            while (matcher.find()) {
                InOutDefinition inOut = InOutDefinition.getFromString(matcher.group(2));
                addParameter(inOut, createParameter(matcher, 3, i + 1));
            }
        }
    }

    private ParameterDefinition createParameter(Matcher matcher, int group, int columnIndex) {
        return createParameter(matcher, group, columnIndex, matcher.group(group));
    }

    private ParameterDefinition createParameter(Matcher matcher, int group, int columnIndex, String paramName) {
		String paramType = matcher.group(group + 1);

		Number precision = 0;
        Number scale = 0;

        if (!StringUtils.isBlank(matcher.group(group + 2))) {
            precision = Integer.valueOf(matcher.group(group + 2));
        }
        if (!StringUtils.isBlank(matcher.group(group + 3))) {
            scale = Integer.valueOf(matcher.group(group + 3));
        }

        DataTypeDefinition type = new DefaultDataTypeDefinition(
            getDatabase(),
            getSchema(),
            paramType,
            precision,
            scale);

        return new DefaultParameterDefinition(this, paramName, columnIndex, type);
	}
}
