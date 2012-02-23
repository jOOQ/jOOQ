package org.jooq.debugger.console.remote;

import org.jooq.debugger.SqlQueryDebuggerResultSetData;

/**
 * @author Christopher Deckers
 */
public class ClientDebugResultSetMessage implements Message {

	private int sqlQueryDebuggerDataID;
	private SqlQueryDebuggerResultSetData sqlQueryDebuggerResultSetData;
	
	public ClientDebugResultSetMessage(int sqlQueryDebuggerDataID, SqlQueryDebuggerResultSetData sqlQueryDebuggerData) {
		this.sqlQueryDebuggerDataID = sqlQueryDebuggerDataID;
		this.sqlQueryDebuggerResultSetData = sqlQueryDebuggerData;
	}
	
	public int getSqlQueryDebuggerDataID() {
		return sqlQueryDebuggerDataID;
	}
	
	public SqlQueryDebuggerResultSetData getSqlQueryDebuggerResultSetData() {
		return sqlQueryDebuggerResultSetData;
	}
	
}
