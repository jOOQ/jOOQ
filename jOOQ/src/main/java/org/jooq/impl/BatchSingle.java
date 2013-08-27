/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.SettingsTools.executeStaticStatements;
import static org.jooq.impl.Utils.visitAll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
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
    private static final long    serialVersionUID = 3793967258181493207L;

    private final DSLContext       create;
    private final Configuration  configuration;
    private final Query          query;
    private final List<Object[]> allBindValues;

    public BatchSingle(Configuration configuration, Query query) {
        this.create = DSL.using(configuration);
        this.configuration = configuration;
        this.query = query;
        this.allBindValues = new ArrayList<Object[]>();
    }

    @Override
    public final BatchSingle bind(Object... bindValues) {
        allBindValues.add(bindValues);
        return this;
    }

    @Override
    public final BatchSingle bind(Object[][] bindValues) {
        for (Object[] v : bindValues) {
            bind(v);
        }

        return this;
    }

    @Override
    public final int size() {
        return allBindValues.size();
    }

    @Override
    public final int[] execute() {

        // [#1180] Run batch queries with BatchMultiple, if no bind variables
        // should be used...
        if (executeStaticStatements(configuration.settings())) {
            return executeStatic();
        }
        else {
            return executePrepared();
        }
    }

    private final int[] executePrepared() {
        ExecuteContext ctx = new DefaultExecuteContext(configuration, new Query[] { query });
        ExecuteListener listener = new ExecuteListeners(ctx);
        Connection connection = ctx.connection();

        // [#1371] fetch bind variables to restore them again, later
        List<Param<?>> params = new ArrayList<Param<?>>(query.getParams().values());
        List<Object> previous = new ArrayList<Object>();

        for (Param<?> param : params) {
            previous.add(param.getValue());
        }

        try {
            listener.renderStart(ctx);
            // [#1520] TODO: Should the number of bind values be checked, here?
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
                visitAll(new DefaultBindContext(configuration, ctx.statement()), params);

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
            Utils.safeClose(listener, ctx);

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

            queries.add(create.query(query.getSQL(INLINED)));
        }

        return create.batch(queries).execute();
    }
}
