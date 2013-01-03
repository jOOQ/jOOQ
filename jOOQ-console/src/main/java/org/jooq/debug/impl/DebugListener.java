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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.debug.Breakpoint;
import org.jooq.debug.BreakpointHit;
import org.jooq.debug.Debugger;
import org.jooq.debug.ExecutionType;
import org.jooq.debug.LoggingListener;
import org.jooq.debug.QueryInfo;
import org.jooq.debug.QueryLog;
import org.jooq.debug.QueryMatcher;
import org.jooq.debug.QueryProcessor;
import org.jooq.debug.QueryType;
import org.jooq.debug.ResultLog;
import org.jooq.debug.impl.LocalDebugger.DebuggerRegistry;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.Executor;

/**
 * @author Christopher Deckers
 */
public class DebugListener extends DefaultExecuteListener {

    private boolean    hasDebuggers;

    private long       startPreparationTime;
    private long       aggregatedPreparationDuration;

    private long       startBindTime;
    private long       endBindTime;

    private long       startExecutionTime;
    private long       endExecutionTime;
    private String     matchingSQL;
    private String     matchingParameterDescription;
    private String     effectiveSQL;
    private Breakpoint matchingBreakpoint;
    private Debugger   matchingDebugger = null;

	@Override
	public void renderStart(ExecuteContext ctx) {
		hasDebuggers = !DebuggerRegistry.get().isEmpty();
		startPreparationTime = 0;
		aggregatedPreparationDuration = 0;
		startBindTime = 0;
		endBindTime = 0;
		startExecutionTime = 0;
		endExecutionTime = 0;
	}

	@Override
	public void prepareStart(ExecuteContext ctx) {
		if(!hasDebuggers) {
			return;
		}
		startPreparationTime = System.currentTimeMillis();
	}

	@Override
	public void prepareEnd(ExecuteContext ctx) {
		if(!hasDebuggers) {
			return;
		}
		aggregatedPreparationDuration += System.currentTimeMillis() - startPreparationTime;
		PreparedStatement statement = ctx.statement();
		if (ctx.type() == ExecuteType.ROUTINE) {
		    ctx.statement(new TrackingCallableStatement((CallableStatement)statement));
		} else {
		    ctx.statement(new TrackingPreparedStatement(statement));
		}
	}

	@Override
	public void bindStart(ExecuteContext ctx) {
		if(!hasDebuggers) {
			return;
		}
		startBindTime = System.currentTimeMillis();
	}

	@Override
	public void bindEnd(ExecuteContext ctx) {
		if(!hasDebuggers) {
			return;
		}
		endBindTime = System.currentTimeMillis();
	}

	@Override
	public void executeStart(ExecuteContext ctx) {
        List<Debugger> debuggerList = DebuggerRegistry.get();
        boolean hasBreakpointHitHandler = false;
        if(!debuggerList.isEmpty()) {
            QueryInfo queryInfo = null;
            bp: for(Debugger debugger: debuggerList) {
                Breakpoint[] breakpoints = debugger.getBreakpoints();
                if(breakpoints != null) {
                    for(Breakpoint breakpoint: breakpoints) {
                        String sql_ = null;
                        String parameterDescription = null;
                        if(queryInfo == null) {
                            String[] sql = ctx.batchSQL();
                            QueryType queryType = QueryType.detectType(sql[0]);
                            if(sql.length == 1) {
                                sql_ = sql[0];
                                PreparedStatement statement = ctx.statement();
                                if(statement instanceof TrackingPreparedStatement) {
                                    parameterDescription = ((TrackingPreparedStatement) statement).getParameterDescription();
                                }
                            } else {
                                StringBuilder sb = new StringBuilder();
                                for(int i=0; i<sql.length; i++) {
                                    if(i > 0) {
                                        sb.append('\n');
                                    }
                                    sb.append(sql[i]);
                                }
                                sql_ = sb.toString();
                            }
                            queryInfo = new QueryInfo(queryType, sql, parameterDescription);
                        }
                        if(breakpoint.matches(queryInfo)) {
                            matchingSQL = sql_;
                            matchingParameterDescription = parameterDescription;
                            matchingDebugger = debugger;
                            matchingBreakpoint = breakpoint;
                            if(breakpoint.isBreaking()) {
                                hasBreakpointHitHandler = debugger.getBreakpointHitHandler() != null;
                            }
                            break bp;
                        }
                    }
                }
            }
        }
        // We consider raw SQL (not the parameters). If we want to match on parameters, this should be a separate matcher.
        // For batched execution of in-lined statements, we aggregate the statements as a multiple-line one for matching purposes.
        if(matchingBreakpoint != null) {
            QueryProcessor beforeExecutionProcessor = matchingBreakpoint.getBeforeExecutionProcessor();
            if(beforeExecutionProcessor != null) {
                String sql = beforeExecutionProcessor.processSQL(matchingSQL);
                long subStartExecutionTime = System.currentTimeMillis();
                executeSQL(ctx, sql);
                long subEndExecutionTime = System.currentTimeMillis();

                // Log result of pre-processing.
                for (Debugger debugger : debuggerList) {
                    LoggingListener listener = debugger.getLoggingListener();

                    if (listener != null) {
                        QueryType type = QueryType.detectType(sql);
                        QueryInfo info = new QueryInfo(type, new String[] { sql }, null);
                        QueryLog log = new QueryLog(info, null, null, subEndExecutionTime - subStartExecutionTime);
                        QueryMatcher[] matchers = listener.getMatchers();

                        if (matchers == null) {
                            listener.logQuery(log);
                        }
                        else {
                            for (QueryMatcher matcher : matchers) {
                                if (matcher.matches(log.getQueryInfo())) {
                                    listener.logQuery(log);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            String mainSQL = null;
            QueryProcessor replacementExecutionProcessor = matchingBreakpoint.getReplacementExecutionProcessor();
            if(replacementExecutionProcessor != null) {
                mainSQL = replacementExecutionProcessor.processSQL(matchingSQL);
                matchingParameterDescription = null;
                try {
                    ctx.statement().close();
                    ctx.sql(mainSQL);
                    ctx.statement(ctx.connection().prepareStatement(mainSQL));
                } catch(Exception e) {
                    // TODO: how to process properly breakpoint errors??
                    throw new RuntimeException(e);
                }
            }
            ExecutionType executionType = ExecutionType.RUN;
            if(hasBreakpointHitHandler) {
                effectiveSQL = mainSQL != null? mainSQL: matchingSQL;
                // TODO: find a way for the handler to replace the statement (not just step over).
                Thread currentThread = Thread.currentThread();
                long threadID = currentThread.getId();
                String threadName = currentThread.getName();
                StackTraceElement[] callerStackTraceElements = currentThread.getStackTrace();
                callerStackTraceElements = Arrays.copyOfRange(callerStackTraceElements, 2, callerStackTraceElements.length);
                BreakpointHit breakpointHit = new BreakpointHit(matchingBreakpoint.getID(), effectiveSQL, matchingParameterDescription, threadID, threadName, callerStackTraceElements, true);
                matchingDebugger.processBreakpointBeforeExecutionHit(ctx, breakpointHit);
                // Breakpoint has an answer.
                if(breakpointHit.getBreakpointID() == null) {
                    executionType = breakpointHit.getExecutionType();
                    String sql = breakpointHit.getSQL();
                    if(sql != null) {
                        effectiveSQL = sql;
                        matchingParameterDescription = null;
                        try {
                            ctx.statement().close();
                            ctx.sql(effectiveSQL);
                            ctx.statement(ctx.connection().prepareStatement(effectiveSQL));
                        } catch(Exception e) {
                            // TODO: how to process properly breakpoint errors??
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            if(executionType != ExecutionType.STEP) {
                matchingDebugger = null;
            }
            switch(executionType) {
                case FAIL: {
                    throw new DataAccessException("Failing SQL statement.");
                }
                case SKIP: {
                    try {
                        ctx.statement().close();
                        // Better return possibility? Based on originating query?
                        String sql = new Executor(ctx.getDialect()).selectZero().where("1 = 2").getSQL();
                        ctx.sql(sql);
                        ctx.statement(ctx.connection().prepareStatement(sql));
                    } catch(Exception e) {
                        // TODO: how to process properly breakpoint errors??
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
        if(!hasDebuggers) {
            return;
        }
		startExecutionTime = System.currentTimeMillis();
	}

    private void executeSQL(ExecuteContext ctx, String sql) {
        Statement statement = null;
        try {
            statement = ctx.connection().createStatement();
            statement.execute(sql);
        } catch(Exception e) {
            // TODO: how to process properly breakpoint errors??
            throw new RuntimeException(e);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch(Exception e) {
                    // No error for closing problems.
                    e.printStackTrace();
                }
            }
        }
    }

	@Override
	public void executeEnd(ExecuteContext ctx) {
		if(!hasDebuggers) {
			return;
		}
		endExecutionTime = System.currentTimeMillis();
        List<Debugger> debuggers = DebuggerRegistry.get();
		if(!debuggers.isEmpty()) {
		    boolean hasListener = false;
            for (Debugger debugger : debuggers) {
                LoggingListener listener = debugger.getLoggingListener();
                if (listener != null) {
                    hasListener = true;
                    break;
                }
            }

		    if(hasListener) {
		        String[] sql = ctx.batchSQL();
		        QueryType type = QueryType.detectType(sql[0]);
                String parameterDescription = null;
                if (sql.length == 1) {
                    PreparedStatement statement = ctx.statement();
                    if (statement instanceof TrackingPreparedStatement) {
                        parameterDescription = ((TrackingPreparedStatement) statement).getParameterDescription();
                    }
                }
		        QueryInfo info = new QueryInfo(type, sql, parameterDescription);
		        final QueryLog log = new QueryLog(info, startPreparationTime == 0? null: aggregatedPreparationDuration, startBindTime == 0? null: endBindTime - startBindTime, endExecutionTime - startExecutionTime);
		        final List<LoggingListener> listeners = new ArrayList<LoggingListener>(debuggers.size());
                for (Debugger debugger : debuggers) {
                    LoggingListener listener = debugger.getLoggingListener();

                    if (listener != null) {
                        QueryMatcher[] matchers = listener.getMatchers();
                        if (matchers == null) {
                            listeners.add(listener);
                            listener.logQuery(log);
                        }
                        else {
                            for (QueryMatcher matcher : matchers) {
                                if (matcher.matches(log.getQueryInfo())) {
                                    listeners.add(listener);
                                    listener.logQuery(log);
                                    break;
                                }
                            }
                        }
                    }
		        }
		        ResultSet resultSet = ctx.resultSet();
                if (resultSet != null && !listeners.isEmpty()) {
                    ResultSet newResultSet = new TrackingResultSet(resultSet) {
                        @Override
                        protected void notifyData(long lifeTime, int readRows, int readCount, int writeCount) {
                            ResultLog resultLog = null;
                            for (LoggingListener loggingListener : listeners) {
                                if (resultLog == null) {
                                    resultLog = new ResultLog(log.getID(), lifeTime, readRows, readCount, writeCount);
                                }
                                loggingListener.logResult(resultLog);
                            }
                        }
                    };
                    ctx.resultSet(newResultSet);
                }
		    }
		}
        if(matchingDebugger != null) {
            Thread currentThread = Thread.currentThread();
            long threadID = currentThread.getId();
            String threadName = currentThread.getName();
            StackTraceElement[] callerStackTraceElements = currentThread.getStackTrace();
            callerStackTraceElements = Arrays.copyOfRange(callerStackTraceElements, 2, callerStackTraceElements.length);
            matchingDebugger.processBreakpointAfterExecutionHit(ctx, new BreakpointHit(matchingBreakpoint.getID(), effectiveSQL, matchingParameterDescription, threadID, threadName, callerStackTraceElements, false));
        }
        if(matchingBreakpoint != null) {
            QueryProcessor afterExecutionProcessor = matchingBreakpoint.getAfterExecutionProcessor();
            matchingBreakpoint = null;
            if(afterExecutionProcessor != null) {
                String sql = afterExecutionProcessor.processSQL(matchingSQL);
                long subStartExecutionTime = System.currentTimeMillis();
                executeSQL(ctx, sql);
                long subEndExecutionTime = System.currentTimeMillis();
                // Log result of pre-processing.
                for (Debugger debugger : debuggers) {
                    LoggingListener listener = debugger.getLoggingListener();

                    if (listener != null) {
                        QueryType type = QueryType.detectType(sql);
                        QueryInfo info = new QueryInfo(type, new String[] {sql}, null);
                        QueryLog log = new QueryLog(info, null, null, subEndExecutionTime - subStartExecutionTime);
                        QueryMatcher[] matchers = listener.getMatchers();

                        if (matchers == null) {
                            listener.logQuery(log);
                        }
                        else {
                            for (QueryMatcher matcher : matchers) {
                                if (matcher.matches(log.getQueryInfo())) {
                                    listener.logQuery(log);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
	}

//	private long startFetchTime;
//	private long endFetchTime;
//
//	@Override
//	public void fetchStart(ExecuteContext ctx) {
//		if(!isLogging) {
//			return;
//		}
//		startFetchTime = System.currentTimeMillis();
//	}
//
//	@Override
//	public void fetchEnd(ExecuteContext ctx) {
//		if(!isLogging) {
//			return;
//		}
//		endFetchTime = System.currentTimeMillis();
//	}

}
