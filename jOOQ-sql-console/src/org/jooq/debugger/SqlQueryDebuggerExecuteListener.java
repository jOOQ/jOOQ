package org.jooq.debugger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Locale;

import org.jooq.ExecuteContext;
import org.jooq.impl.DefaultExecuteListener;

public class SqlQueryDebuggerExecuteListener extends DefaultExecuteListener {

	private boolean isLogging;
	
	@Override
	public void renderStart(ExecuteContext ctx) {
		isLogging = !SqlQueryDebuggerRegister.getSqlQueryDebuggerList().isEmpty();
		startPreparationTime = 0;
		endPreparationTime = 0;
		startBindTime = 0;
		endBindTime = 0;
		startExecutionTime = 0;
		endExecutionTime = 0;
	}
	
	private long startPreparationTime;
	private long endPreparationTime;
	
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
		endPreparationTime = System.currentTimeMillis();
		PreparedStatement statement = ctx.statement();
		ctx.statement(new UsageTrackingPreparedStatement(statement));
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
		List<SqlQueryDebugger> sqlQueryDebuggerList = SqlQueryDebuggerRegister.getSqlQueryDebuggerList();
		if(sqlQueryDebuggerList.isEmpty()) {
			return;
		}
		ResultSet resultSet = ctx.resultSet();
		String sql = ctx.sql();
		SqlQueryType sqlQueryType = getSqlQueryType(sql);
		PreparedStatement statement = ctx.statement();
		if(statement instanceof UsageTrackingPreparedStatement) {
			String parameterDescription = ((UsageTrackingPreparedStatement) statement).getParameterDescription();
			if(parameterDescription != null) {
				sql += " -> " + parameterDescription;
			}
		}
		SqlQueryDebuggerData sqlQueryDebuggerData = new SqlQueryDebuggerData(sqlQueryType, new String[] {sql}, startPreparationTime == 0? null: endPreparationTime - startPreparationTime, startBindTime == 0? null: endBindTime - startBindTime, endExecutionTime - startExecutionTime);
		for(SqlQueryDebugger listener: sqlQueryDebuggerList) {
			listener.debugQueries(sqlQueryDebuggerData);
		}
		if(resultSet != null) {
			final int sqlQueryDebuggerDataID = sqlQueryDebuggerData.getID();
			ResultSet newResultSet = new UsageTrackingResultSet(resultSet) {
				@Override
				protected void notifyData(long lifeTime, int readRows, int readCount, int writeCount) {
					List<SqlQueryDebugger> sqlQueryDebuggerList = SqlQueryDebuggerRegister.getSqlQueryDebuggerList();
					if(sqlQueryDebuggerList.isEmpty()) {
						return;
					}
                    SqlQueryDebuggerResultSetData sqlQueryDebuggerResultSetData = new SqlQueryDebuggerResultSetData(lifeTime, readRows, readCount, writeCount);
					for(SqlQueryDebugger listener: sqlQueryDebuggerList) {
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

    private static SqlQueryType getSqlQueryType(String query) {
        String queryLC = query.toLowerCase(Locale.ENGLISH).trim();
        if(queryLC.startsWith("insert ") ||
                queryLC.startsWith("update ") ||
                queryLC.startsWith("delete ")) {
            return SqlQueryType.WRITE;
        }
        if(queryLC.startsWith("select ") ||
                queryLC.startsWith("with ")) {
            return SqlQueryType.READ;
        }
        return SqlQueryType.OTHER;
    }

}
