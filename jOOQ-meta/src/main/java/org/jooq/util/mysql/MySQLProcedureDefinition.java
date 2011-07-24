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

import org.jooq.impl.StringUtils;
import org.jooq.util.AbstractProcedureDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;

/**
 * @author Lukas Eder
 */
public class MySQLProcedureDefinition extends AbstractProcedureDefinition {

	private final String params;

    public MySQLProcedureDefinition(Database database, String name, String comment, String params) {
		super(database, null, name, comment, null);

		this.params = params;
	}

	@Override
    protected void init0() throws SQLException {
	    // [#738] Avoid matching commas that appear in types, for instance DECIMAL(2, 1)
		String[] split = params.split(",(?!\\s*\\d+\\s*\\))");

		for (int i = 0; i < split.length; i++) {
			String param = split[i];

			// TODO [#742] : Use the INFORMATION_SCHEMA.PARAMETERS dictionary view instead.
			// It's much more reliable, than mysql.proc pattern matching...

			param = param.trim();
			Matcher matcher = PARAMETER_PATTERN.matcher(param);
			while (matcher.find()) {
				String inOut = matcher.group(2);
				String paramName = matcher.group(3);
				String paramType = matcher.group(4);

				Number precision = 0;
				Number scale = 0;

                if (!StringUtils.isBlank(matcher.group(5))) {
                    precision = Integer.valueOf(matcher.group(5));
                }
                if (!StringUtils.isBlank(matcher.group(6))) {
                    scale = Integer.valueOf(matcher.group(6));
                }

                DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), paramType, precision, scale);
                ParameterDefinition parameter = new DefaultParameterDefinition(this, paramName, i + 1, type);

				addParameter(InOutDefinition.getFromString(inOut), parameter);
			}
		}
	}
}
