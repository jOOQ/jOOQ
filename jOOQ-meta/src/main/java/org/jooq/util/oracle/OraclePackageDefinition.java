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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractPackageDefinition;
import org.jooq.util.Database;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.oracle.sys.tables.AllArguments;

/**
 * @author Lukas Eder
 */
public class OraclePackageDefinition extends AbstractPackageDefinition {

    public OraclePackageDefinition(Database database, String packageName, String comment) {
        super(database, packageName, comment);
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
                .selectDistinct(
                    AllArguments.OBJECT_NAME,
                    AllArguments.OBJECT_ID,
                    AllArguments.OVERLOAD)
                .from(ALL_ARGUMENTS)
                .where(AllArguments.OWNER.equal(getSchemaName()))
                .and(AllArguments.PACKAGE_NAME.equal(getName()))
                .orderBy(AllArguments.OBJECT_NAME, AllArguments.OVERLOAD)
                .fetch()) {

            result.add(new OracleRoutineDefinition(getDatabase(),
                this,
                record.getValue(AllArguments.OBJECT_NAME),
                "",
                record.getValue(AllArguments.OBJECT_ID),
                record.getValue(AllArguments.OVERLOAD)));
        }

        return result;
    }
}
