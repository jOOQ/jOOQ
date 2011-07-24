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
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.util.AbstractPackageDefinition;
import org.jooq.util.Database;
import org.jooq.util.FunctionDefinition;
import org.jooq.util.ProcedureDefinition;
import org.jooq.util.oracle.sys.tables.AllArguments;
import org.jooq.util.oracle.sys.tables.records.AllArgumentsRecord;

/**
 * @author Lukas Eder
 */
public class OraclePackageDefinition extends AbstractPackageDefinition {

    private Table<AllArgumentsRecord> o = ALL_ARGUMENTS.as("outer");
    private Table<AllArgumentsRecord> i = ALL_ARGUMENTS.as("inner");
    private Field<String> o_objectName = o.getField(AllArguments.OBJECT_NAME);
    private Field<String> i_objectName = i.getField(AllArguments.OBJECT_NAME);
    private Field<BigDecimal> o_objectID = o.getField(AllArguments.OBJECT_ID);
    private Field<BigDecimal> i_objectID = i.getField(AllArguments.OBJECT_ID);
    private Field<String> o_overload = o.getField(AllArguments.OVERLOAD);


    public OraclePackageDefinition(Database database, String packageName, String comment) {
        super(database, packageName, comment);
    }

    @Override
    protected List<ProcedureDefinition> getProcedures0() throws SQLException {
        List<ProcedureDefinition> result = new ArrayList<ProcedureDefinition>();

        for (Record record : getProcedures(true)) {
            String name = record.getValue(o_objectName);
            BigDecimal objectId = record.getValue(o_objectID);
            String overload = record.getValue(o_overload);
            result.add(new OracleProcedureDefinition(getDatabase(), this, name, "", objectId, overload));
        }

        return result;
    }

    @Override
    protected List<FunctionDefinition> getFunctions0() throws SQLException {
        List<FunctionDefinition> result = new ArrayList<FunctionDefinition>();

        for (Record record : getProcedures(false)) {
            String name = record.getValue(o_objectName);
            BigDecimal objectId = record.getValue(o_objectID);
            String overload = record.getValue(o_overload);
            result.add(new OracleFunctionDefinition(getDatabase(), this, name, "", objectId, overload));
        }

        return result;
    }

    private Result<Record> getProcedures(boolean procedures) throws SQLException {
        Condition existsReturnValue = create().exists(create().selectOne()
            .from(i)
            .where(i.getField(AllArguments.OWNER).equal(getSchemaName()))
            .and(i.getField(AllArguments.PACKAGE_NAME).equal(getName()))
            .and(i_objectID.equal(o_objectID))
            .and(i_objectName.equal(o_objectName))
            .and(i.getField(AllArguments.POSITION).equal(BigDecimal.ZERO)));

        Condition notExistsOUTParameters = create().notExists(create().selectOne()
            .from(i)
            .where(i.getField(AllArguments.OWNER).equal(getSchemaName()))
            .and(i.getField(AllArguments.PACKAGE_NAME).equal(getName()))
            .and(i_objectID.equal(o_objectID))
            .and(i_objectName.equal(o_objectName))
            .and(i.getField(AllArguments.POSITION).notEqual(BigDecimal.ZERO))
            .and(i.getField(AllArguments.IN_OUT).in("OUT", "IN/OUT")));

        Condition combine = existsReturnValue.and(notExistsOUTParameters);

        // Invert requirements for function callables, if we're looking
        // for procedures
        if (procedures) {
            combine = combine.not();
        }

        return create().selectDistinct(o_objectName, o_objectID, o_overload)
            .from(o)
            .where(o.getField(AllArguments.OWNER).equal(getSchemaName()))
            .and(o.getField(AllArguments.PACKAGE_NAME).equal(getName()))
            .and(combine)
            .orderBy(o_objectName, o_overload)
            .fetch();
    }
}
