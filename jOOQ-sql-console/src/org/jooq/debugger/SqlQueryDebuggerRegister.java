package org.jooq.debugger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Christopher Deckers
 */
public class SqlQueryDebuggerRegister {

	private SqlQueryDebuggerRegister() {}

	private static final Object LOCK = new Object();
	private static final List<SqlQueryDebugger> debuggerList = new ArrayList<SqlQueryDebugger>();

	public static void addSqlQueryDebugger(SqlQueryDebugger sqlQueryDebugger) {
		synchronized(LOCK) {
			debuggerList.add(sqlQueryDebugger);
			for(SqlQueryDebuggerRegisterListener l: debuggerRegisterListenerList) {
				l.notifySqlQueryDebuggerListenerListModified();
			}
		}
	}

	public static void removeSqlQueryDebugger(SqlQueryDebugger sqlQueryDebugger) {
		synchronized(LOCK) {
			debuggerList.remove(sqlQueryDebugger);
			for(SqlQueryDebuggerRegisterListener l: debuggerRegisterListenerList) {
				l.notifySqlQueryDebuggerListenerListModified();
			}
		}
	}

	private static final List<SqlQueryDebuggerRegisterListener> debuggerRegisterListenerList = new ArrayList<SqlQueryDebuggerRegisterListener>(1);

	public static void addSqlQueryDebuggerRegisterListener(SqlQueryDebuggerRegisterListener listener) {
		synchronized(LOCK) {
			debuggerRegisterListenerList.add(listener);
		}
	}
	
	public static void removeSqlQueryDebuggerRegisterListener(SqlQueryDebuggerRegisterListener listener) {
		synchronized(LOCK) {
			debuggerRegisterListenerList.remove(listener);
		}
	}
	
	/*
	 * @return an immutable list of all the debuggers currently registered.
	 */
	public static List<SqlQueryDebugger> getSqlQueryDebuggerList() {
		synchronized(LOCK) {
			if(debuggerList.isEmpty()) {
				// No cost when no loggers
				return Collections.emptyList();
			}
			// Small cost: copy collection and make it immutable.
			// Generally, no more than one or two listeners in the list.
			return Collections.unmodifiableList(new ArrayList<SqlQueryDebugger>(debuggerList));
		}
	}

}
