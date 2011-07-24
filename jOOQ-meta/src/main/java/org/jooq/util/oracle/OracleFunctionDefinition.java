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

import static org.jooq.util.oracle.sys.tables.AllArguments.ALL_ARGUMENTS;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.util.AbstractFunctionDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.oracle.sys.tables.AllArguments;

/**
 * @author Lukas Eder
 */
public class OracleFunctionDefinition extends AbstractFunctionDefinition {

    private final BigDecimal objectId;

	public OracleFunctionDefinition(Database database, PackageDefinition pkg, String name, String comment, BigDecimal objectId, String overload) {
		super(database, pkg, name, comment, overload);

		this.objectId = objectId;
	}

	@Override
    protected void init0() throws SQLException {
	    Result<Record> result = create().select(
	            AllArguments.IN_OUT,
	            AllArguments.ARGUMENT_NAME,
	            AllArguments.DATA_TYPE,
	            AllArguments.DATA_PRECISION,
	            AllArguments.DATA_SCALE,
	            AllArguments.TYPE_NAME,
	            AllArguments.POSITION)
	        .from(ALL_ARGUMENTS)
            .where(AllArguments.OWNER.equal(getSchemaName()))
            .and(AllArguments.OBJECT_NAME.equal(getName()))
            .and(AllArguments.OBJECT_ID.equal(objectId))
            .and(AllArguments.OVERLOAD.equal(getOverload()))
            .and(AllArguments.DATA_LEVEL.equal(BigDecimal.ZERO))

            // [#284] In packages, procedures without arguments may have a
            // single data type entry that does not mean anything...?
            .and(AllArguments.DATA_TYPE.isNotNull())
            .orderBy(AllArguments.POSITION.asc()).fetch();

	    for (Record record : result) {
	        String inOut = record.getValue(AllArguments.IN_OUT);

            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                record.getValue(AllArguments.DATA_TYPE),
                record.getValue(AllArguments.DATA_PRECISION),
                record.getValue(AllArguments.DATA_SCALE),
                record.getValue(AllArguments.TYPE_NAME));

	        if (InOutDefinition.getFromString(inOut) == InOutDefinition.OUT) {
	            returnValue = new DefaultParameterDefinition(
	                this,
                    "RETURN_VALUE",
                    record.getValueAsInteger(AllArguments.POSITION),
                    type);
	        } else {
	            inParameters.add(new DefaultParameterDefinition(
	                this,
	                record.getValue(AllArguments.ARGUMENT_NAME),
	                record.getValueAsInteger(AllArguments.POSITION),
	                type));
	        }
	    }
	}
}
