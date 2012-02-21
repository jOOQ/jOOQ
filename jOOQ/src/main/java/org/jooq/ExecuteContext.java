/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A context object for {@link Query} execution passed to registered
 * {@link ExecuteListener}'s.
 * <p>
 * Expect most of this context's objects to be <code>nullable</code>!
 *
 * @author Lukas Eder
 */
public interface ExecuteContext extends Configuration {

    Configuration configuration();

    // TODO Routines?
    // Nullable!
    Query query();

    // Nullable!
    void sql(String sql);

    String sql();

    /**
     * Override the context's {@link PreparedStatement}
     * <p>
     * Use this to wrap the <code>PreparedStatement</code> executed by jOOQ with
     * your custom wrapper statement, logging bind variables and query
     * execution. Beware
     */
    void statement(PreparedStatement statement);

    PreparedStatement statement();

    // Nullable!
    void resultSet(ResultSet resultSet);

    ResultSet resultSet();

    // Nullable
    void record(Record record);

    Record record();

    void result(Result<?> result);

    Result<?> result();

}
