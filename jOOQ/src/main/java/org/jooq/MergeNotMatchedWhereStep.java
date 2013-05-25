/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq;

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.ORACLE;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * This type is used for the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.mergeInto(table)
 *       .using(select)
 *       .on(condition)
 *       .whenMatchedThenUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .whenNotMatchedThenInsert(field1, field2)
 *       .values(value1, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
@State
public interface MergeNotMatchedWhereStep<R extends Record> extends MergeFinalStep<R> {

    /**
     * Add an additional <code>WHERE</code> clause to the preceding
     * <code>WHEN NOT MATCHED THEN INSERT</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     */
    @Support({ CUBRID, ORACLE })
    @Transition(
        name = "WHERE",
        args = "Condition"
    )
    MergeFinalStep<R> where(Condition condition);

    /**
     * Add an additional <code>WHERE</code> clause to the preceding
     * <code>WHEN NOT MATCHED THEN INSERT</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     */
    @Support({ CUBRID, ORACLE })
    @Transition(
        name = "WHERE",
        args = "Condition"
    )
    MergeFinalStep<R> where(Field<Boolean> condition);
}
