package org.jooq.debugger.console.remote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.jooq.debugger.SqlQueryDebugger;
import org.jooq.debugger.SqlQueryDebuggerData;
import org.jooq.debugger.SqlQueryDebuggerRegister;
import org.jooq.debugger.SqlQueryDebuggerResultSetData;

/**
 * @author Christopher Deckers
 */
public class SqlRemoteQueryDebuggerServer {

	private final Object LOCK = new Object();
	private ServerSocket serverSocket;
	
	public SqlRemoteQueryDebuggerServer(final int port) {
		Thread serverThread = new Thread("SQL Remote Debugger Server on port " + port) {
			@Override
			public void run() {
				try {
					synchronized(LOCK) {
						serverSocket = new ServerSocket(port);
					}
					while(true) {
						ServerSocket serverSocket_;
						synchronized(LOCK) {
							serverSocket_ = serverSocket;
						}
						if(serverSocket_ != null) {
							Socket socket = serverSocket_.accept();
							startServerToClientThread(socket, port);
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		serverThread.setDaemon(true);
		serverThread.start();
	}
	
	private void startServerToClientThread(final Socket socket, int port) {
		Thread clientThread = new Thread("SQL Remote Debugger Server on port " + port) {
			@Override
			public void run() {
				SqlQueryDebugger sqlQueryDebugger = null;
				boolean isLogging = false;
				try {
					ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					sqlQueryDebugger = new SqlQueryDebugger() {
						@Override
						public synchronized void debugQueries(SqlQueryDebuggerData sqlQueryDebuggerData) {
							try {
								out.writeObject(new ClientDebugQueriesMessage(sqlQueryDebuggerData));
								out.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						@Override
						public synchronized void debugResultSet(int sqlQueryDebuggerDataID, SqlQueryDebuggerResultSetData sqlQueryDebuggerResultSetData) {
							try {
								out.writeObject(new ClientDebugResultSetMessage(sqlQueryDebuggerDataID, sqlQueryDebuggerResultSetData));
								out.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					};
					for(Message o; (o=(Message)in.readObject()) != null; ) {
						if(o instanceof ServerLoggingActivationMessage) {
							isLogging = ((ServerLoggingActivationMessage) o).isLogging();
							if(isLogging) {
								SqlQueryDebuggerRegister.addSqlQueryDebugger(sqlQueryDebugger);
							} else {
								SqlQueryDebuggerRegister.removeSqlQueryDebugger(sqlQueryDebugger);
							}
						}
					}
				} catch(Exception e) {
					if(isLogging) {
						e.printStackTrace();
					}
				} finally {
					if(sqlQueryDebugger != null) {
						SqlQueryDebuggerRegister.removeSqlQueryDebugger(sqlQueryDebugger);
					}
				}
			}
		};
		clientThread.setDaemon(true);
		clientThread.start();
	}
	
	public void close() {
		synchronized(LOCK) {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				serverSocket = null;
			}
		}
	}
	
}
