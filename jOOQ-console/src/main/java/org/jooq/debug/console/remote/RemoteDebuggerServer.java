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
import java.net.ServerSocket;
import java.net.Socket;

import org.jooq.debug.Debugger;
import org.jooq.debug.DebuggerData;
import org.jooq.debug.DebuggerRegistry;
import org.jooq.debug.DebuggerResultSetData;

/**
 * @author Christopher Deckers
 */
public class RemoteDebuggerServer {

	private final Object LOCK = new Object();
	private ServerSocket serverSocket;

	public RemoteDebuggerServer(final int port) {
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
				Debugger sqlQueryDebugger = null;
				boolean isLogging = false;
				try {
					ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					sqlQueryDebugger = new Debugger() {
						@Override
						public synchronized void debugQueries(DebuggerData sqlQueryDebuggerData) {
							try {
								out.writeObject(new ClientDebugQueriesMessage(sqlQueryDebuggerData));
								out.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						@Override
						public synchronized void debugResultSet(int sqlQueryDebuggerDataID, DebuggerResultSetData sqlQueryDebuggerResultSetData) {
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
								DebuggerRegistry.addSqlQueryDebugger(sqlQueryDebugger);
							} else {
								DebuggerRegistry.removeSqlQueryDebugger(sqlQueryDebugger);
							}
						}
					}
				} catch(Exception e) {
					if(isLogging) {
						e.printStackTrace();
					}
				} finally {
					if(sqlQueryDebugger != null) {
						DebuggerRegistry.removeSqlQueryDebugger(sqlQueryDebugger);
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
