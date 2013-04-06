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
package org.jooq.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.Batch;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Query;

/**
 * @author Lukas Eder
 */
class BatchMultiple implements Batch {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -7337667281292354043L;

    private final Configuration configuration;
    private final Query[]       queries;

    public BatchMultiple(Configuration configuration, Query... queries) {
        this.configuration = configuration;
        this.queries = queries;
    }

    @Override
    public final int size() {
        return queries.length;
    }

    @Override
    public final int[] execute() {
        ExecuteContext ctx = new DefaultExecuteContext(configuration, queries);
        ExecuteListener listener = new ExecuteListeners(ctx);
        Connection connection = ctx.connection();

        try {
            ctx.statement(new SettingsEnabledPreparedStatement(connection));

            String[] batchSQL = ctx.batchSQL();
            for (int i = 0; i < queries.length; i++) {
                listener.renderStart(ctx);
                batchSQL[i] = DSL.using(configuration).renderInlined(queries[i]);
                listener.renderEnd(ctx);
            }

            for (String sql : batchSQL) {
                ctx.sql(sql);
                listener.prepareStart(ctx);
                ctx.statement().addBatch(sql);
                listener.prepareEnd(ctx);
                ctx.sql(null);
            }

            listener.executeStart(ctx);
            int[] result = ctx.statement().executeBatch();
            listener.executeEnd(ctx);

            return result;
        }
        catch (SQLException e) {
            ctx.sqlException(e);
            listener.exception(ctx);
            throw ctx.exception();
        }
        finally {
            Utils.safeClose(listener, ctx);
        }
    }
}
