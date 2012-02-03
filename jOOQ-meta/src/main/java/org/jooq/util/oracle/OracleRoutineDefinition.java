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

package org.jooq.util.oracle;

import static org.jooq.util.oracle.sys.Tables.ALL_ARGUMENTS;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class OracleRoutineDefinition extends AbstractRoutineDefinition {

    private final BigDecimal objectId;

	public OracleRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, String comment, BigDecimal objectId, String overload) {
		super(schema, pkg, name, comment, overload);

		this.objectId = objectId;
	}

	@Override
    protected void init0() throws SQLException {
	    Result<Record> result = create().select(
	            ALL_ARGUMENTS.IN_OUT,
	            ALL_ARGUMENTS.ARGUMENT_NAME,
	            ALL_ARGUMENTS.DATA_TYPE,
	            ALL_ARGUMENTS.DATA_PRECISION,
	            ALL_ARGUMENTS.DATA_SCALE,
	            ALL_ARGUMENTS.TYPE_NAME,
	            ALL_ARGUMENTS.POSITION)
	        .from(ALL_ARGUMENTS)
            .where(ALL_ARGUMENTS.OWNER.equal(getSchema().getName()))
            .and(ALL_ARGUMENTS.OBJECT_NAME.equal(getName()))
            .and(ALL_ARGUMENTS.OBJECT_ID.equal(objectId))
            .and(ALL_ARGUMENTS.OVERLOAD.equal(getOverload()))
            .and(ALL_ARGUMENTS.DATA_LEVEL.equal(BigDecimal.ZERO))

            // [#284] In packages, procedures without arguments may have a
            // single data type entry that does not mean anything...?
            .and(ALL_ARGUMENTS.DATA_TYPE.isNotNull())
            .orderBy(ALL_ARGUMENTS.POSITION.asc()).fetch();

	    for (Record record : result) {
	        InOutDefinition inOut =
                InOutDefinition.getFromString(record.getValue(ALL_ARGUMENTS.IN_OUT));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(ALL_ARGUMENTS.DATA_TYPE),
                record.getValue(ALL_ARGUMENTS.DATA_PRECISION),
                record.getValue(ALL_ARGUMENTS.DATA_SCALE),
                record.getValue(ALL_ARGUMENTS.TYPE_NAME));

            String name = record.getValue(ALL_ARGUMENTS.ARGUMENT_NAME);
            Integer position = record.getValueAsInteger(ALL_ARGUMENTS.POSITION);

            // [#378] Oracle supports stored functions with OUT parameters.
            // They are mapped to procedures in jOOQ
            if (StringUtils.isBlank(name) && position == 0) {
                inOut = InOutDefinition.RETURN;
                name = "RETURN_VALUE";
            }

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                name,
                position,
                type);

            addParameter(inOut, parameter);
	    }
	}
}
