/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
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
package org.jooq.debug.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.debug.Breakpoint;
import org.jooq.debug.BreakpointHit;
import org.jooq.debug.BreakpointHitHandler;
import org.jooq.debug.DatabaseDescriptor;
import org.jooq.debug.Debugger;
import org.jooq.debug.LoggingListener;
import org.jooq.debug.QueryExecutorContext;


/**
 * @author Christopher Deckers
 */
class LocalDebugger implements Debugger {

    private DatabaseDescriptor databaseDescriptor;

    public LocalDebugger(DatabaseDescriptor databaseDescriptor) {
        this.databaseDescriptor = databaseDescriptor;
        DebuggerRegistry.add(this);
    }

    private LoggingListener loggingListener;
    private final Object LOGGING_LISTENER_LOCK = new Object();

    @Override
    public void close() {
        DebuggerRegistry.remove(this);
    }

    @Override
    public void setLoggingListener(LoggingListener loggingListener) {
        synchronized (LOGGING_LISTENER_LOCK) {
            this.loggingListener = loggingListener;
        }
    }

    @Override
    public LoggingListener getLoggingListener() {
        synchronized (LOGGING_LISTENER_LOCK) {
            return loggingListener;
        }
    }

    private Breakpoint[] breakpoints;
    private final Object BREAKPOINT_LOCK = new Object();

    @Override
    public void addBreakpoint(Breakpoint breakpoint) {
        synchronized (BREAKPOINT_LOCK) {
            if (this.breakpoints == null) {
                this.breakpoints = new Breakpoint[] { breakpoint };
                return;
            }

            for (int i = 0; i < breakpoints.length; i++) {
                if (breakpoints[i].equals(breakpoint)) {
                    breakpoints[i] = breakpoint;
                    return;
                }
            }

            Breakpoint[] newBreakpoints = new Breakpoint[breakpoints.length + 1];
            System.arraycopy(breakpoints, 0, newBreakpoints, 0, breakpoints.length);
            newBreakpoints[breakpoints.length] = breakpoint;
            breakpoints = newBreakpoints;
        }
    }

    @Override
    public void removeBreakpoint(Breakpoint breakpoint) {
        synchronized (BREAKPOINT_LOCK) {
            if (this.breakpoints == null) {
                return;
            }
            for (int i = 0; i < breakpoints.length; i++) {
                if (breakpoints[i].equals(breakpoint)) {
                    if (breakpoints.length == 1) {
                        breakpoints = null;
                    }
                    else {
                        Breakpoint[] newBreakpoints = new Breakpoint[breakpoints.length - 1];
                        System.arraycopy(breakpoints, 0, newBreakpoints, 0, i);
                        System.arraycopy(breakpoints, i + 1, newBreakpoints, i, newBreakpoints.length - i);
                        breakpoints = newBreakpoints;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public Breakpoint[] getBreakpoints() {
        synchronized (BREAKPOINT_LOCK) {
            return breakpoints;
        }
    }

    private BreakpointHitHandler breakpointHitHandler;
    private final Object BREAKPOINT_HIT_HANDLER_LOCK = new Object();

    @Override
    public void setBreakpointHitHandler(BreakpointHitHandler breakpointHitHandler) {
        synchronized (BREAKPOINT_HIT_HANDLER_LOCK) {
            this.breakpointHitHandler = breakpointHitHandler;
        }
    }

    @Override
    public BreakpointHitHandler getBreakpointHitHandler() {
        synchronized (BREAKPOINT_HIT_HANDLER_LOCK) {
            return breakpointHitHandler;
        }
    }

    @Override
    public String[] getExecutionContextNames() {
        // TODO: implement execution context mapping.
        return databaseDescriptor != null? new String[] {"default"}: new String[0];
    }

    @Override
    public LocalStatementExecutor createQueryExecutor(String executionContextName) {
        // TODO: implement execution context mapping: use context name.
        return new LocalStatementExecutor(new QueryExecutorContext() {
            @Override
            public boolean isReadOnly() {
                return databaseDescriptor.isReadOnly();
            }
            @Override
            public Connection getConnection() {
                return databaseDescriptor.createConnection();
            }
            @Override
            public void releaseConnection(Connection connection) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
            @Override
            public SQLDialect getSQLDialect() {
                return databaseDescriptor.getSQLDialect();
            }
            @Override
            public String[] getTableNames() {
                return LocalDebugger.this.getTableNames();
            }
            @Override
            public String[] getTableColumnNames() {
                return LocalDebugger.this.getTableColumnNames();
            }
        });
    }

    private String[] getTableNames() {
        if(databaseDescriptor == null) {
            return new String[0];
        }
        List<Table<?>> tableList = databaseDescriptor.getSchema().getTables();
        List<String> tableNameList = new ArrayList<String>();
        for(Table<? extends Record> table: tableList) {
            String tableName = table.getName();
            tableNameList.add(tableName);
        }
        Collections.sort(tableNameList, String.CASE_INSENSITIVE_ORDER);
        return tableNameList.toArray(new String[0]);
    }

    private String[] getTableColumnNames() {
        if(databaseDescriptor == null) {
            return new String[0];
        }
        Set<String> columnNameSet = new HashSet<String>();
        for(Table<?> table: databaseDescriptor.getSchema().getTables()) {
            for(Field<?> field: table.fields()) {
                String columnName = field.getName();
                columnNameSet.add(columnName);
            }
        }
        String[] columnNames = columnNameSet.toArray(new String[0]);
        Arrays.sort(columnNames, String.CASE_INSENSITIVE_ORDER);
        return columnNames;
    }

    private Map<Long, ExecuteContext> threadIDToExecuteContextMap = new HashMap<Long, ExecuteContext>();

    @Override
    public void processBreakpointBeforeExecutionHit(ExecuteContext ctx, BreakpointHit breakpointHit) {
        BreakpointHitHandler handler = getBreakpointHitHandler();
        if(handler == null) {
            return;
        }
        long threadID = breakpointHit.getThreadID();
        synchronized (threadIDToExecuteContextMap) {
            threadIDToExecuteContextMap.put(threadID, ctx);
        }
        try {
            handler.processBreakpointBeforeExecutionHit(breakpointHit);
        } finally {
            synchronized (threadIDToExecuteContextMap) {
                threadIDToExecuteContextMap.remove(threadID);
            }
            performThreadDataCleanup(threadID);
        }
    }

    /**
     * @param threadID The thread ID whose data should be cleaned up
     */
    protected void performThreadDataCleanup(long threadID) {
    }

    @Override
    public void processBreakpointAfterExecutionHit(ExecuteContext ctx, BreakpointHit breakpointHit) {
        BreakpointHitHandler handler = getBreakpointHitHandler();
        if(handler == null) {
            return;
        }
        long threadID = breakpointHit.getThreadID();
        synchronized (threadIDToExecuteContextMap) {
            threadIDToExecuteContextMap.put(threadID, ctx);
        }
        try {
            handler.processBreakpointAfterExecutionHit(breakpointHit);
        } finally {
            synchronized (threadIDToExecuteContextMap) {
                threadIDToExecuteContextMap.remove(threadID);
            }
            performThreadDataCleanup(threadID);
        }
    }

    @Override
    public LocalStatementExecutor createBreakpointHitStatementExecutor(long threadID) {
        final ExecuteContext ctx;
        synchronized (threadIDToExecuteContextMap) {
            ctx = threadIDToExecuteContextMap.get(threadID);
            if(ctx == null) {
                return null;
            }
        }
        return new LocalStatementExecutor(new QueryExecutorContext() {
            @Override
            public boolean isReadOnly() {
                return false;
            }
            @Override
            public Connection getConnection() {
                return ctx.connection();
            }
            @Override
            public void releaseConnection(Connection connection) {
                // We don't want to alter the connection.
            }
            @Override
            public SQLDialect getSQLDialect() {
                return ctx.configuration().dialect();
            }
            @Override
            public String[] getTableNames() {
                return LocalDebugger.this.getTableNames();
            }
            @Override
            public String[] getTableColumnNames() {
                return LocalDebugger.this.getTableColumnNames();
            }
        });
    }

    /**
     * A registry for local debuggers
     */
    static class DebuggerRegistry {

        private static final Object         LOCK      = new Object();
        private static final List<Debugger> debuggers = new ArrayList<Debugger>();

        private static void add(Debugger debugger) {
            synchronized (LOCK) {
                debuggers.add(debugger);
            }
        }

        private static void remove(Debugger debugger) {
            synchronized (LOCK) {
                debuggers.remove(debugger);
            }
        }

        /*
         * @return an immutable list of all the debuggers currently registered.
         */
        public static List<Debugger> get() {
            synchronized (LOCK) {

                // No cost when no loggers
                if (debuggers.isEmpty()) {
                    return Collections.emptyList();
                }

                // Small cost: copy collection and make it immutable.
                // Generally, no more than one or two listeners in the list.
                return Collections.unmodifiableList(new ArrayList<Debugger>(debuggers));
            }
        }

        /**
         * No instances
         */
        private DebuggerRegistry() {}
    }
}
