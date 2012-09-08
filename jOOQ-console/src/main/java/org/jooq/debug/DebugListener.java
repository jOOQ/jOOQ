/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.debug;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.debug.BreakpointHit.ExecutionType;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.Factory;

/**
 * @author Christopher Deckers
 */
public class DebugListener extends DefaultExecuteListener {

	private boolean hasDebuggers;

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

	private long startPreparationTime;
	private long aggregatedPreparationDuration;

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
		    ctx.statement(new UsageTrackingCallableStatement((CallableStatement)statement));
		} else {
		    ctx.statement(new UsageTrackingPreparedStatement(statement));
		}
	}

	private long startBindTime;
	private long endBindTime;

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

	private long startExecutionTime;
	private long endExecutionTime;
	private String matchingSQL;
	private String matchingParameterDescription;
	private String effectiveSQL;
	private Breakpoint matchingBreakpoint;
    private Debugger matchingDebugger = null;

	@Override
	public void executeStart(ExecuteContext ctx) {
        List<Debugger> debuggerList = DebuggerRegistry.get();
        boolean hasBreakpointHitHandler = false;
        if(!debuggerList.isEmpty()) {
            StatementInfo statementInfo = null;
            bp: for(Debugger debugger: debuggerList) {
                Breakpoint[] breakpoints = debugger.getBreakpoints();
                if(breakpoints != null) {
                    for(Breakpoint breakpoint: breakpoints) {
                        String sql_ = null;
                        String parameterDescription = null;
                        if(statementInfo == null) {
                            String[] sql = ctx.batchSQL();
                            SqlQueryType sqlQueryType = SqlQueryType.detectType(sql[0]);
                            if(sql.length == 1) {
                                sql_ = sql[0];
                                PreparedStatement statement = ctx.statement();
                                if(statement instanceof UsageTrackingPreparedStatement) {
                                    parameterDescription = ((UsageTrackingPreparedStatement) statement).getParameterDescription();
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
                            statementInfo = new StatementInfo(sqlQueryType, sql, parameterDescription);
                        }
                        if(breakpoint.matches(statementInfo, true)) {
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
            StatementProcessor beforeExecutionProcessor = matchingBreakpoint.getBeforeExecutionProcessor();
            if(beforeExecutionProcessor != null) {
                String sql = beforeExecutionProcessor.processSQL(matchingSQL);
                long subStartExecutionTime = System.currentTimeMillis();
                executeSQL(ctx, sql);
                long subEndExecutionTime = System.currentTimeMillis();
                // Log result of pre-processing.
                for(Debugger debugger: debuggerList) {
                    LoggingListener loggingListener = debugger.getLoggingListener();
                    if(loggingListener != null) {
                        SqlQueryType sqlQueryType = SqlQueryType.detectType(sql);
                        QueryLoggingData queryLoggingData = new QueryLoggingData(sqlQueryType, new String[] {sql}, null, null, null, subEndExecutionTime - subStartExecutionTime);
                        StatementMatcher[] loggingStatementMatchers = debugger.getLoggingStatementMatchers();
                        if(loggingStatementMatchers == null) {
                            loggingListener.logQueries(queryLoggingData);
                        } else for(StatementMatcher statementMatcher: loggingStatementMatchers) {
                            if(statementMatcher.matches(queryLoggingData)) {
                                loggingListener.logQueries(queryLoggingData);
                                break;
                            }
                        }
                    }
                }
            }
            String mainSQL = null;
            StatementProcessor replacementExecutionProcessor = matchingBreakpoint.getReplacementExecutionProcessor();
            if(replacementExecutionProcessor != null) {
                mainSQL = replacementExecutionProcessor.processSQL(matchingSQL);
                matchingParameterDescription = null;
                try {
                    ctx.statement().close();
                    ctx.sql(mainSQL);
                    ctx.statement(ctx.getConnection().prepareStatement(mainSQL));
                } catch(Exception e) {
                    // TODO: how to process properly breakpoint errors??
                    throw new RuntimeException(e);
                }
            }
            ExecutionType executionType = BreakpointHit.ExecutionType.RUN;
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
                    String sql = breakpointHit.getSql();
                    if(sql != null) {
                        effectiveSQL = sql;
                        matchingParameterDescription = null;
                        try {
                            ctx.statement().close();
                            ctx.sql(effectiveSQL);
                            ctx.statement(ctx.getConnection().prepareStatement(effectiveSQL));
                        } catch(Exception e) {
                            // TODO: how to process properly breakpoint errors??
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            if(executionType != ExecutionType.STEP_THROUGH) {
                matchingDebugger = null;
            }
            switch(executionType) {
                case FAIL: {
                    throw new DataAccessException("Failing SQL statement.");
                }
                case RUN_OVER: {
                    try {
                        ctx.statement().close();
                        // Better return possibility? Based on originating query?
                        String sql = new Factory(ctx.getDialect()).selectZero().where("1 = 2").getSQL();
                        ctx.sql(sql);
                        ctx.statement(ctx.getConnection().prepareStatement(sql));
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
            statement = ctx.getConnection().createStatement();
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
        List<Debugger> debuggerList = DebuggerRegistry.get();
		if(!debuggerList.isEmpty()) {
		    boolean hasListener = false;
		    for(Debugger debugger: debuggerList) {
		        LoggingListener loggingListener = debugger.getLoggingListener();
		        if(loggingListener != null) {
		            hasListener = true;
		            break;
		        }
		    }
		    if(hasListener) {
		        String[] sql = ctx.batchSQL();
		        SqlQueryType sqlQueryType = SqlQueryType.detectType(sql[0]);
		        String parameterDescription = null;
		        if(sql.length == 1) {
		            PreparedStatement statement = ctx.statement();
		            if(statement instanceof UsageTrackingPreparedStatement) {
		                parameterDescription = ((UsageTrackingPreparedStatement) statement).getParameterDescription();
		            }
		        }
		        QueryLoggingData queryLoggingData = new QueryLoggingData(sqlQueryType, sql, parameterDescription, startPreparationTime == 0? null: aggregatedPreparationDuration, startBindTime == 0? null: endBindTime - startBindTime, endExecutionTime - startExecutionTime);
		        final List<LoggingListener> loggingListenerList = new ArrayList<LoggingListener>(debuggerList.size());
		        for(Debugger listener: debuggerList) {
		            LoggingListener loggingListener = listener.getLoggingListener();
		            if(loggingListener != null) {
		                StatementMatcher[] loggingStatementMatchers = listener.getLoggingStatementMatchers();
		                if(loggingStatementMatchers == null) {
		                    loggingListenerList.add(loggingListener);
		                    loggingListener.logQueries(queryLoggingData);
		                } else for(StatementMatcher statementMatcher: loggingStatementMatchers) {
		                    if(statementMatcher.matches(queryLoggingData)) {
		                        loggingListenerList.add(loggingListener);
		                        loggingListener.logQueries(queryLoggingData);
		                        break;
		                    }
		                }
		            }
		        }
		        ResultSet resultSet = ctx.resultSet();
		        if(resultSet != null && !loggingListenerList.isEmpty()) {
		            final int queryLoggingDataID = queryLoggingData.getID();
		            ResultSet newResultSet = new UsageTrackingResultSet(resultSet) {
		                @Override
		                protected void notifyData(long lifeTime, int readRows, int readCount, int writeCount) {
		                    ResultSetLoggingData resultSetLoggingData = null;
		                    for(LoggingListener loggingListener: loggingListenerList) {
		                        if(resultSetLoggingData == null) {
		                            resultSetLoggingData = new ResultSetLoggingData(lifeTime, readRows, readCount, writeCount);
		                        }
		                        loggingListener.logResultSet(queryLoggingDataID, resultSetLoggingData);
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
            StatementProcessor afterExecutionProcessor = matchingBreakpoint.getAfterExecutionProcessor();
            matchingBreakpoint = null;
            if(afterExecutionProcessor != null) {
                String sql = afterExecutionProcessor.processSQL(matchingSQL);
                long subStartExecutionTime = System.currentTimeMillis();
                executeSQL(ctx, sql);
                long subEndExecutionTime = System.currentTimeMillis();
                // Log result of pre-processing.
                for(Debugger listener: debuggerList) {
                    LoggingListener loggingListener = listener.getLoggingListener();
                    if(loggingListener != null) {
                        SqlQueryType sqlQueryType = SqlQueryType.detectType(sql);
                        QueryLoggingData queryLoggingData = new QueryLoggingData(sqlQueryType, new String[] {sql}, null, null, null, subEndExecutionTime - subStartExecutionTime);
                        StatementMatcher[] loggingStatementMatchers = listener.getLoggingStatementMatchers();
                        if(loggingStatementMatchers == null) {
                            loggingListener.logQueries(queryLoggingData);
                        } else for(StatementMatcher statementMatcher: loggingStatementMatchers) {
                            if(statementMatcher.matches(queryLoggingData)) {
                                loggingListener.logQueries(queryLoggingData);
                                break;
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
