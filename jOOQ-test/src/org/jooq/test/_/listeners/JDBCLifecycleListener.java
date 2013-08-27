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
package org.jooq.test._.listeners;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.jooq.ExecuteContext;
import org.jooq.tools.jdbc.DefaultCallableStatement;
import org.jooq.tools.jdbc.DefaultPreparedStatement;
import org.jooq.tools.jdbc.DefaultResultSet;

/**
 * An <code>ExecuteListener</code> that collects data about the lifecycle of
 * JDBC objects in all integration tests.
 *
 * @author Lukas Eder
 */
public class JDBCLifecycleListener extends AbstractLifecycleListener {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID = -2283264126211556442L;

    public static final Map<Method, Integer> STMT_START_COUNT = new TreeMap<Method, Integer>(METHOD_COMPARATOR);
    public static final Map<Method, Integer> STMT_CLOSE_COUNT = new TreeMap<Method, Integer>(METHOD_COMPARATOR);

    public static final Map<Method, Integer> RS_START_COUNT = new TreeMap<Method, Integer>(METHOD_COMPARATOR);
    public static final Map<Method, Integer> RS_CLOSE_COUNT = new TreeMap<Method, Integer>(METHOD_COMPARATOR);

    @Override
    public void executeStart(ExecuteContext ctx) {
        super.executeStart(ctx);
        increment(STMT_START_COUNT);

        if (ctx.statement() instanceof CallableStatement) {
            ctx.statement(new DefaultCallableStatement((CallableStatement) ctx.statement()) {

                @Override
                public void close() throws SQLException {
                    increment(STMT_CLOSE_COUNT);
                    super.close();
                }
            });

        }
        else {
            ctx.statement(new DefaultPreparedStatement(ctx.statement()) {

                @Override
                public void close() throws SQLException {
                    increment(STMT_CLOSE_COUNT);
                    super.close();
                }
            });
        }
    }

    @Override
    public void fetchStart(ExecuteContext ctx) {
        super.fetchStart(ctx);
        increment(RS_START_COUNT);

        ctx.resultSet(new DefaultResultSet(ctx.resultSet()) {

            @Override
            public void close() throws SQLException {
                increment(RS_CLOSE_COUNT);
                super.close();
            }
        });
    }
}
