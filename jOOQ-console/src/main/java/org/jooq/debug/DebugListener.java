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
import java.util.List;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.impl.DefaultExecuteListener;

public class DebugListener extends DefaultExecuteListener {

	private boolean isLogging;

	@Override
	public void renderStart(ExecuteContext ctx) {
		isLogging = !DebuggerRegistry.getDebuggerList().isEmpty();
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
		if(!isLogging) {
			return;
		}
		startPreparationTime = System.currentTimeMillis();
	}

	@Override
	public void prepareEnd(ExecuteContext ctx) {
		if(!isLogging) {
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
		if(!isLogging) {
			return;
		}
		startBindTime = System.currentTimeMillis();
	}

	@Override
	public void bindEnd(ExecuteContext ctx) {
		if(!isLogging) {
			return;
		}
		endBindTime = System.currentTimeMillis();
	}

	private long startExecutionTime;
	private long endExecutionTime;

	@Override
	public void executeStart(ExecuteContext ctx) {
		if(!isLogging) {
			return;
		}
		startExecutionTime = System.currentTimeMillis();
	}

	@Override
	public void executeEnd(ExecuteContext ctx) {
		if(!isLogging) {
			return;
		}
		endExecutionTime = System.currentTimeMillis();
		List<Debugger> sqlQueryDebuggerList = DebuggerRegistry.getDebuggerList();
		if(sqlQueryDebuggerList.isEmpty()) {
			return;
		}
		ResultSet resultSet = ctx.resultSet();
		String[] sql = ctx.batchSQL();
		SqlQueryType sqlQueryType = SqlQueryType.detectType(sql[0]);
		String parameterDescription = null;
		if(sql.length == 1) {
		    PreparedStatement statement = ctx.statement();
		    if(statement instanceof UsageTrackingPreparedStatement) {
		        parameterDescription = ((UsageTrackingPreparedStatement) statement).getParameterDescription();
		    }
		}
		DebuggerData sqlQueryDebuggerData = new DebuggerData(sqlQueryType, sql, parameterDescription, startPreparationTime == 0? null: aggregatedPreparationDuration, startBindTime == 0? null: endBindTime - startBindTime, endExecutionTime - startExecutionTime);
		for(Debugger listener: sqlQueryDebuggerList) {
			listener.debugQueries(sqlQueryDebuggerData);
		}
		if(resultSet != null) {
			final int sqlQueryDebuggerDataID = sqlQueryDebuggerData.getID();
			ResultSet newResultSet = new UsageTrackingResultSet(resultSet) {
				@Override
				protected void notifyData(long lifeTime, int readRows, int readCount, int writeCount) {
					List<Debugger> sqlQueryDebuggerList = DebuggerRegistry.getDebuggerList();
					if(sqlQueryDebuggerList.isEmpty()) {
						return;
					}
                    DebuggerResultSetData sqlQueryDebuggerResultSetData = new DebuggerResultSetData(lifeTime, readRows, readCount, writeCount);
					for(Debugger listener: sqlQueryDebuggerList) {
						listener.debugResultSet(sqlQueryDebuggerDataID, sqlQueryDebuggerResultSetData);
					}
				}
			};
			ctx.resultSet(newResultSet);
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
