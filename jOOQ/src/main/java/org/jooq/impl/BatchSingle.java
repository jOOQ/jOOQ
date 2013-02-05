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
package org.jooq.impl;

import static org.jooq.conf.SettingsTools.executeStaticStatements;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.BatchBindStep;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Param;
import org.jooq.Query;

/**
 * @author Lukas Eder
 */
class BatchSingle implements BatchBindStep {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = 3793967258181493207L;

    private final Factory           create;
    private final Query             query;
    private final List<Object[]>    allBindValues;

    public BatchSingle(Factory create, Query query) {
        this.create = create;
        this.query = query;
        this.allBindValues = new ArrayList<Object[]>();
    }

    @Override
    public final BatchSingle bind(Object... bindValues) {
        allBindValues.add(bindValues);
        return this;
    }

    @Override
    public final int[] execute() {

        // [#1180] Run batch queries with BatchMultiple, if no bind variables
        // should be used...
        if (executeStaticStatements(create.getSettings())) {
            return executeStatic();
        }
        else {
            return executePrepared();
        }
    }

    private final int[] executePrepared() {
        Connection connection = create.getConnection();

        ExecuteContext ctx = new DefaultExecuteContext(create, new Query[] { query });
        ExecuteListener listener = new ExecuteListeners(ctx);

        // [#1371] fetch bind variables to restore them again, later
        List<Param<?>> params = new ArrayList<Param<?>>(query.getParams().values());
        List<Object> previous = new ArrayList<Object>();

        for (Param<?> param : params) {
            previous.add(param.getValue());
        }

        try {
            listener.renderStart(ctx);
            ctx.sql(create.render(query));
            listener.renderEnd(ctx);

            listener.prepareStart(ctx);
            ctx.statement(connection.prepareStatement(ctx.sql()));
            listener.prepareEnd(ctx);

            for (Object[] bindValues : allBindValues) {
                listener.bindStart(ctx);

                // [#1371] [#2139] Don't bind variables directly onto statement,
                // bind them through the collected params list to preserve type
                // information
                for (int i = 0; i < params.size(); i++) {
                    params.get(i).setConverted(bindValues[i]);
                }
                new DefaultBindContext(create, ctx.statement()).bind(params);

                listener.bindEnd(ctx);
                ctx.statement().addBatch();
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
            Util.safeClose(listener, ctx);

            // Restore bind variables to values prior to batch execution
            for (int i = 0; i < params.size(); i++) {
                params.get(i).setConverted(previous.get(i));
            }
        }
    }

    private final int[] executeStatic() {
        List<Query> queries = new ArrayList<Query>();

        for (Object[] bindValues : allBindValues) {
            for (int i = 0; i < bindValues.length; i++) {
                query.bind(i + 1, bindValues[i]);
            }

            queries.add(create.query(query.getSQL(true)));
        }

        return create.batch(queries).execute();
    }
}
