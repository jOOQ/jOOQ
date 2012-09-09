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
package org.jooq.debug.console.remote.messaging;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.jooq.debug.Debugger;

/**
 * The communication interface, which establishes the link between a peer VM and
 * this local side.
 *
 * @author Christopher Deckers
 */
public class CommunicationInterface {

    private final boolean IS_SYNCING_MESSAGES = Boolean.parseBoolean(System.getProperty("communication.interface.syncmessages"));

    private volatile boolean isOpen;
    private final Debugger   debugger;
    private final int        port;

    public CommunicationInterface(Debugger debugger, int port) {
        this.debugger = debugger;
        this.port = port;
    }

    public Debugger getDebugger() {
        return debugger;
    }

    public boolean isOpen() {
        return isOpen;
    }

    private void checkOpen() {
        if(!isOpen()) {
            throw new IllegalStateException("The interface is not open!");
        }
    }

    public void close() {
        isOpen = false;
        if(messagingInterface != null) {
            messagingInterface.destroy();
            messagingInterface = null;
        }
    }

    public static CommunicationInterface openClientCommunicationChannel(CommunicationInterfaceFactory communicationInterfaceFactory, String ip, int port) throws Exception {
        CommunicationInterface communicationInterface = communicationInterfaceFactory.createCommunicationInterface(port);
        communicationInterface.createClientCommunicationChannel(ip);
        return communicationInterface;
    }

    void notifyOpen() {
        isOpen = true;
        processOpened();
    }

    protected void processOpened() {
    }

    void notifyKilled() {
        isOpen = false;
        messagingInterface = null;
        processClosed();
    }

    protected void processClosed() {
    }

    private void createClientCommunicationChannel(String ip) throws Exception {
        // Create the interface to communicate with the process handling the other side
        Socket socket = null;
        // 2 attempts
        for(int i=1; i>=0; i--) {
            try {
                socket = new Socket(ip, port);
                break;
            } catch(IOException e) {
                if(i == 0) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(100);
            } catch(Exception e) {
            }
        }
        if(socket == null) {
            throw new IllegalStateException("Failed to connect to " + ip + "!");
        }
        messagingInterface = new MessagingInterface(this, socket, true);
        notifyOpen();
    }

    private volatile MessagingInterface messagingInterface;

    MessagingInterface getMessagingInterface() {
        return messagingInterface;
    }

    /**
     * Send that message synchronously, potentially returning a result if the
     * message type allows that.
     *
     * @return the result if any.
     */
    public final <S extends Serializable> S syncSend(final Message<S> message) {
        checkOpen();
        if (message instanceof LocalMessage) {
            LocalMessage<S> localMessage = (LocalMessage<S>) message;
            return localMessage.runCommand(new MessageContext(this));
        }
        return messagingInterface.syncSend(message);
    }

    /**
     * Send a message asynchronously.
     */
    public final <S extends Serializable> void asyncSend(final Message<S> message) {
        if (IS_SYNCING_MESSAGES) {
            syncSend(message);
        }
        else {
            checkOpen();
            if (message instanceof LocalMessage) {
                LocalMessage<?> localMessage = (LocalMessage<?>) message;
                localMessage.runCommand(new MessageContext(this));
                return;
            }
            messagingInterface.asyncSend(message);
        }
    }

    public static ServerSocket openServerCommunicationChannel(final CommunicationInterfaceFactory communicationInterfaceFactory, final int port) throws Exception {
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
                                CommunicationInterface communicationInterface = communicationInterfaceFactory.createCommunicationInterface(port);
                                communicationInterface.messagingInterface = new MessagingInterface(communicationInterface, socket, false);
                                communicationInterface.notifyOpen();
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
