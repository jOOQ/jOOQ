package org.jooq.debugger.console.remote;

import org.jooq.debugger.SqlQueryDebuggerData;

/**
 * @author Christopher Deckers
 */
public class ClientDebugQueriesMessage implements Message {

	private SqlQueryDebuggerData sqlQueryDebuggerData;
	
	public ClientDebugQueriesMessage(SqlQueryDebuggerData sqlQueryDebuggerData) {
		this.sqlQueryDebuggerData = sqlQueryDebuggerData;
	}
	
	public SqlQueryDebuggerData getSqlQueryDebuggerData() {
		return sqlQueryDebuggerData;
	}
	
}
