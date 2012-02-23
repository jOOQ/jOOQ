package org.jooq.debugger;


/**
 * @author Christopher Deckers
 */
public interface SqlQueryDebugger {

    public void debugQueries(SqlQueryDebuggerData sqlQueryDebuggerData);

    public void debugResultSet(int sqlQueryDebuggerDataID, SqlQueryDebuggerResultSetData sqlQueryDebuggerResultSetData);

}
