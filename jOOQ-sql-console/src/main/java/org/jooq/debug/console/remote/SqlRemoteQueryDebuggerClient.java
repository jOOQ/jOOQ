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
package org.jooq.debug.console.remote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.jooq.debug.SqlQueryDebugger;
import org.jooq.debug.SqlQueryDebuggerData;
import org.jooq.debug.SqlQueryDebuggerRegister;
import org.jooq.debug.SqlQueryDebuggerRegisterListener;
import org.jooq.debug.SqlQueryDebuggerResultSetData;

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
