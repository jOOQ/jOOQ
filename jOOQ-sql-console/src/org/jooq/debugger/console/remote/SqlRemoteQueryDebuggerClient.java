package org.jooq.debugger.console.remote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.jooq.debugger.SqlQueryDebugger;
import org.jooq.debugger.SqlQueryDebuggerData;
import org.jooq.debugger.SqlQueryDebuggerRegister;
import org.jooq.debugger.SqlQueryDebuggerRegisterListener;
import org.jooq.debugger.SqlQueryDebuggerResultSetData;

/**
 * @author Christopher Deckers
 */
public class SqlRemoteQueryDebuggerClient {

	private Socket socket;
	
	public SqlRemoteQueryDebuggerClient(String ip, int port) throws Exception {
		socket = new Socket(ip, port);
		Thread thread = new Thread("SQL Remote Debugger Client on port " + port) {
			@Override
			public void run() {
				SqlQueryDebuggerRegisterListener debuggerRegisterListener = null;
				try {
					final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					debuggerRegisterListener = new SqlQueryDebuggerRegisterListener() {
						@Override
						public void notifySqlQueryDebuggerListenerListModified() {
							try {
								boolean isLogging = !SqlQueryDebuggerRegister.getSqlQueryDebuggerList().isEmpty();
								out.writeObject(new ServerLoggingActivationMessage(isLogging));
								out.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					};
					SqlQueryDebuggerRegister.addSqlQueryDebuggerRegisterListener(debuggerRegisterListener);
					ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					for(Message o; (o=(Message)in.readObject()) != null; ) {
						if(o instanceof ClientDebugQueriesMessage) {
							SqlQueryDebuggerData sqlQueryDebuggerData = ((ClientDebugQueriesMessage) o).getSqlQueryDebuggerData();
							for(SqlQueryDebugger debugger: SqlQueryDebuggerRegister.getSqlQueryDebuggerList()) {
								debugger.debugQueries(sqlQueryDebuggerData);
							}
						} else if(o instanceof ClientDebugResultSetMessage) {
							ClientDebugResultSetMessage m = (ClientDebugResultSetMessage) o;
							int sqlQueryDebuggerDataID = m.getSqlQueryDebuggerDataID();
							SqlQueryDebuggerResultSetData clientDebugResultSetData = m.getSqlQueryDebuggerResultSetData();
							for(SqlQueryDebugger debugger: SqlQueryDebuggerRegister.getSqlQueryDebuggerList()) {
								debugger.debugResultSet(sqlQueryDebuggerDataID, clientDebugResultSetData);
							}
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					if(debuggerRegisterListener != null) {
						SqlQueryDebuggerRegister.removeSqlQueryDebuggerRegisterListener(debuggerRegisterListener);
					}
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
}
