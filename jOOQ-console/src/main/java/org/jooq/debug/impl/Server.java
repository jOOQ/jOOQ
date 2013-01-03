/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.debug.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.jooq.debug.DatabaseDescriptor;

/**
 * @author Christopher Deckers
 */
public class Server {

    private final Object LOCK = new Object();
    private ServerSocket serverSocket;

    public Server(final int port) {
        this(port, null);
    }

    public Server(final int port, final DatabaseDescriptor descriptor) {
        try {
            synchronized (LOCK) {
                serverSocket = openServerCommunicationChannel(port, descriptor);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        synchronized (LOCK) {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocket = null;
            }
        }
    }

    private static ServerSocket openServerCommunicationChannel(final int port, final DatabaseDescriptor descriptor) throws Exception {
        final ServerSocket serverSocket;
        try {
          serverSocket = new ServerSocket();
//          serverSocket.setReuseAddress(true);
          serverSocket.bind(new InetSocketAddress(port));
        } catch(IOException e) {
          throw e;
        }
        Thread serverThread = new Thread("Communication channel server on port " + port) {
            @Override
            public void run() {
                while(!serverSocket.isClosed()) {
                    final Socket socket;
                    try {
                        socket = serverSocket.accept();
                        new Thread("Communication channel - client connection") {
                            @Override
                            public void run() {
                                final ServerDebugger debugger = new ServerDebugger(descriptor);
                                Communication comm = new ServerCommunication(debugger);
                                debugger.setCommunicationInterface(comm);
                                comm.setMessagingInterface(new MessagingInterface(comm, socket, false));
                                comm.notifyOpen();
                            }
                        }.start();
                    } catch(Exception e) {
                        if(!serverSocket.isClosed()) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        serverThread.setDaemon(true);
        serverThread.start();
        return serverSocket;
    }
}
