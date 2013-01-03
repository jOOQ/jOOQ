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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jooq.debug.impl.Message.NoResult;


/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
class MessagingInterface {

    private static final boolean IS_DEBUGGING_MESSAGES = Boolean.parseBoolean(System.getProperty("communication.interface.debug.printmessages"));

    private static class CommandResultMessage<S extends Serializable> extends Message<S> {

        private int originalID;
        private S result;
        private Throwable exception;

        CommandResultMessage(int originalID, S result, Throwable exception) {
            this.originalID = originalID;
            this.result = result;
            this.exception = exception;
        }

        public S getResult() {
            return result;
        }

        public Throwable getException() {
            return exception;
        }

        @Override
        public String toString() {
            return super.toString() + "(" + originalID + ")";
        }

    }

    private Object RECEIVER_LOCK = new Object();

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private boolean isAlive = true;

    public boolean isAlive() {
        return isAlive;
    }

    public void destroy() {
        isAlive = false;
        try {
            ois.close();
        } catch(Exception e) {
        }
    }

    private class MessageProcessingThread extends Thread {

        private long originatorThreadID;
        private List<Message<?>> messageList = new LinkedList<Message<?>>();

        public MessageProcessingThread(long originatorThreadID) {
            setName("Communication Interface Message Dispatcher-" + getId() + " [" + originatorThreadID + "]");
            this.originatorThreadID = originatorThreadID;
        }

        public void addMessage(Message<?> message) {
            synchronized (messageList) {
                messageList.add(message);
            }
        }

        public long getOriginatorThreadID() {
            return originatorThreadID;
        }

        private boolean isWaitingOnSyncCall;

        public boolean isWaitingOnSyncCall() {
            return isWaitingOnSyncCall;
        }

        public void setWaitingOnSyncCall(boolean isWaitingOnSyncCall) {
            this.isWaitingOnSyncCall = isWaitingOnSyncCall;
        }

        @Override
        public void run() {
            while(true) {
                Message<?> message;
                synchronized(messageList) {
                    if(messageList.isEmpty()) {
                       message = null;
                    } else {
                        message = messageList.remove(0);
                    }
                }
                // When there are no more messages to process, we try to de-register the current thread.
                // We need to make sure that no message is posted while we do this.
                if(message == null) {
                    synchronized (originatorThreadIDToThreadMap) {
                        synchronized(messageList) {
                            if(messageList.isEmpty()) {
                                originatorThreadIDToThreadMap.remove(originatorThreadID);
                            } else {
                                message = messageList.remove(0);
                            }
                        }
                    }
                }
                if(message == null) {
                    break;
                }
                runMessage(message);
            }
        }

    }

    private Map<Long, MessageProcessingThread> originatorThreadIDToThreadMap = new HashMap<Long, MessagingInterface.MessageProcessingThread>();

    private Communication comm;
    private boolean isClient;

    MessagingInterface(final Communication communication, final Socket socket, boolean isClient) {
        this.comm = communication;
        this.isClient = isClient;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()) {
                @Override
                public synchronized void write(int b) throws IOException {
                    super.write(b);
                    oosByteCount++;
                }
                @Override
                public synchronized void write(byte[] b, int off, int len) throws IOException {
                    super.write(b, off, len);
                    oosByteCount += len;
                }
            });
            oos.flush();
            ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        Thread receiverThread = new Thread("MessagingInterface Receiver (" + (isClient? "client": "server") + ")") {
            @Override
            public void run() {
                while(isAlive) {
                    Message<?> message = null;
                    try {
                        message = readMessage();
                    } catch(Exception e) {
                        if(isAlive) {
                            isAlive = false;
//                            e.printStackTrace();
                            try {
                                communication.notifyKilled();
                            } catch(Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        // Unlock all locked sync calls
                        synchronized(RECEIVER_LOCK) {
                            receivedMessageList.clear();
                            RECEIVER_LOCK.notify();
                        }
                        synchronized (idToThreadInfo) {
                            for(ThreadInfo<?> threadInfo: idToThreadInfo.values()) {
                                synchronized (threadInfo) {
                                    threadInfo.notify();
                                }
                            }
                        }
                    }
                    if(message != null) {
                        long threadID = message.getThreadID();
//                        boolean isOriginatingSide = message instanceof CM_asyncExecResponse;
                        boolean isProcessorToOriginator = message.isProcessorToOriginator();
                        if(!isProcessorToOriginator) {
                            MessageProcessingThread messageProcessingThread;
                            boolean isNew = false;
                            synchronized (originatorThreadIDToThreadMap) {
                                messageProcessingThread = originatorThreadIDToThreadMap.get(threadID);
                                if(messageProcessingThread == null) {
                                    messageProcessingThread = new MessageProcessingThread(threadID);
                                    originatorThreadIDToThreadMap.put(threadID, messageProcessingThread);
                                    isNew = true;
                                    messageProcessingThread.addMessage(message);
                                } else {
                                    if(messageProcessingThread.isWaitingOnSyncCall()) {
                                        threadID = messageProcessingThread.getId();
                                        isProcessorToOriginator = true;
                                    } else {
                                        messageProcessingThread.addMessage(message);
                                    }
                                }
                            }
                            if(isNew) {
                                messageProcessingThread.start();
                            }
                        }
                        if(isProcessorToOriginator) {
                            ThreadInfo threadInfo;
                            synchronized (idToThreadInfo) {
                                threadInfo = idToThreadInfo.get(threadID);
                            }
                            if(threadInfo != null) {
                                if(message instanceof CommandResultMessage) {
                                    throw new IllegalStateException("I need to indicate if command result message.");
                                }
                                synchronized (threadInfo) {
                                    threadInfo.setMessage(message);
                                    threadInfo.notify();
                                }
                            } else {
                                System.err.println("What to do?");
                            }
                        }
                    }
                }
                try {
                    oos.close();
                } catch(Exception e) {
                }
                try {
                    ois.close();
                } catch(Exception e) {
                }
                try {
                    socket.close();
                } catch(Exception e) {
                }
            }
        };
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    private <S extends Serializable> CommandResultMessage<S> runMessage(Message<S> message) {
        if(IS_DEBUGGING_MESSAGES) {
            System.err.println("[" + (isClient? "client": "server") + "] >RUN: " + message.getID() + ", " + message);
        }
        CommandResultMessage<S> commandResultMessage;
        if(message instanceof CommandMessage) {
            CommandMessage<S> commandMessage = (CommandMessage<S>)message;
            S result = null;
            Throwable throwable = null;
            if (message.isValid()) {
                try {
                    result = commandMessage.run(new MessageContext(comm));
                }
                catch (Throwable t) {
                    throwable = t;
                }
            }
            if (commandMessage.isSyncExec()) {
                commandResultMessage = new CommandResultMessage<S>(commandMessage.getID(), result, throwable);
                asyncSend(commandResultMessage);
            }
            else {
                if (throwable != null) {
                    throwable.printStackTrace();
                }
                commandResultMessage = new CommandResultMessage<S>(message.getID(), result, throwable);
            }
        } else {
            commandResultMessage = new CommandResultMessage<S>(message.getID(), null, null);
            if (message.isSyncExec()) {
                asyncSend(commandResultMessage);
            }
        }
        if(IS_DEBUGGING_MESSAGES) {
            System.err.println("[" + (isClient? "client": "server") + "] <RUN: " + message.getID());
        }
        return commandResultMessage;
    }

    private List<Message<?>> receivedMessageList = new LinkedList<Message<?>>();

    private static class CM_asyncExecResponse<S extends Serializable> extends CommandMessage<NoResult> {
        private final long threadID;
        private final CommandResultMessage<S> commandResultMessage;

        public CM_asyncExecResponse(long threadID, CommandResultMessage<S> commandResultMessage) {
            this.threadID = threadID;
            this.commandResultMessage = commandResultMessage;
        }

        @SuppressWarnings("unchecked")
        @Override
        public NoResult run(MessageContext context) {
            MessagingInterface messagingInterface = context.getMessagingInterface();
            ThreadInfo<S> threadInfo;
            synchronized (messagingInterface.idToThreadInfo) {
                threadInfo = (ThreadInfo<S>) messagingInterface.idToThreadInfo.get(threadID);
            }
            if(threadInfo == null) {
                System.err.println("A sync call is missing.");
                return null;
            }
            synchronized(threadInfo) {
                threadInfo.setMessage(commandResultMessage);
                threadInfo.notify();
            }
            return null;
        }
    }

    private static class CM_asyncExec<S extends Serializable> extends CommandMessage<NoResult> {
        private final long threadID;
        private final Message<S> message;

        CM_asyncExec(long threadID, Message<S> message) {
            this.threadID = threadID;
            this.message = message;
        }

        @Override
        public NoResult run(MessageContext context) {
            message.setSyncExec(false);
//            message.setCommunicationInterface(communicationInterface);
            MessagingInterface messagingInterface = context.getMessagingInterface();
            CommandResultMessage<S> commandResultMessage = messagingInterface.runMessage(message);
            CM_asyncExecResponse<S> asyncExecResponse = new CM_asyncExecResponse<S>(threadID, commandResultMessage);
            messagingInterface.asyncSend(asyncExecResponse);
            return null;
        }
    }

    private static class ThreadInfo<S extends Serializable> {

        private boolean isValuePresent;
        private Message<S> message;

        public Message<S> getMessage() {
            return message;
        }

        public void setMessage(Message<S> message) {
            isValuePresent = true;
            this.message = message;
        }

        public void clearMessage() {
            isValuePresent = false;
            message = null;
        }

        public boolean isValuePresent() {
            return isValuePresent;
        }
    }

    private Map<Long, ThreadInfo<?>> idToThreadInfo = new HashMap<Long, ThreadInfo<?>>();

    private void printFailedInvocation(Message<?> message) {
        System.err.println("[" + (isClient? "client": "server") + "] Failed messaging: " + message);
    }

    public <S extends Serializable> S syncSend(Message<S> message) {
        Thread thread = Thread.currentThread();
        long threadID = thread.getId();
        ThreadInfo<S> threadInfo = new ThreadInfo<S>();
        ThreadInfo<?> previousThreadInfo;
        synchronized (idToThreadInfo) {
            previousThreadInfo = idToThreadInfo.put(threadID, threadInfo);
        }
        if(thread instanceof MessageProcessingThread) {
            synchronized (originatorThreadIDToThreadMap) {
                ((MessageProcessingThread)thread).setWaitingOnSyncCall(true);
            }
        }
        CM_asyncExec<S> asyncExec = new CM_asyncExec<S>(threadID, message);
        asyncSend(asyncExec);
        CommandResultMessage<S> commandResultMessage = null;
        synchronized(threadInfo) {
            while(commandResultMessage == null) {
                while(!threadInfo.isValuePresent()) {
                    try {
                        threadInfo.wait();
                    } catch(Exception e) {
                    }
                    if(!isAlive()) {
                        idToThreadInfo.remove(threadID);
                        printFailedInvocation(message);
                        return null;
                    }
                }
                Message<S> value = threadInfo.getMessage();
                threadInfo.clearMessage();
                if(value instanceof CommandResultMessage) {
                    commandResultMessage = (CommandResultMessage<S>)value;
                } else {
                    runMessage(value);
                }
            }
        }
        synchronized (idToThreadInfo) {
            if(previousThreadInfo != null) {
                idToThreadInfo.put(threadID, previousThreadInfo);
            } else {
                idToThreadInfo.remove(threadID);
            }
        }
        if(previousThreadInfo == null &&thread instanceof MessageProcessingThread) {
            synchronized (originatorThreadIDToThreadMap) {
                ((MessageProcessingThread)thread).setWaitingOnSyncCall(false);
            }
        }
        return processCommandResult(commandResultMessage);
    }

    private <S extends Serializable> S processCommandResult(CommandResultMessage<S> message) {
        if(IS_DEBUGGING_MESSAGES) {
            System.err.println("[" + (isClient? "client": "server") + "] <USE: " + message.getID());
        }
        Throwable exception = message.getException();
        if(exception != null) {
//            if(exception instanceof RuntimeException) {
//                throw (RuntimeException)exception;
//            }
            throw new RuntimeException(exception);
        }
        return message.getResult();
    }

    public void asyncSend(Message<?> message) {
        message.setSyncExec(false);
        Thread thread = Thread.currentThread();
        // If the message was sent by the other side, all returning messages need to know which originating thread they are bound to.
        if(thread instanceof MessageProcessingThread) {
            message.setThreadID(((MessageProcessingThread) thread).getOriginatorThreadID());
            message.setProcessorToOriginator(true);
        } else {
            message.setThreadID(thread.getId());
            message.setProcessorToOriginator(false);
        }
        try {
            writeMessage(message);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static final int OOS_RESET_THRESHOLD;

    static {
        String maxByteCountProperty = System.getProperty("communication.interface.streamresetthreshold");
        if(maxByteCountProperty != null) {
            OOS_RESET_THRESHOLD = Integer.parseInt(maxByteCountProperty);
        } else {
            OOS_RESET_THRESHOLD = 500000;
        }
    }

    private int oosByteCount;

    private void writeMessage(Message<?> message) throws IOException {
        if(!isAlive()) {
            printFailedInvocation(message);
            return;
        }
        if(IS_DEBUGGING_MESSAGES) {
            System.err.println("[" + (isClient? "client": "server") + "] " + (message.isSyncExec()? "SENDS": "SENDA") + ": " + message.getID() + ", " + message);
        }
        synchronized(oos) {
            oos.writeUnshared(message);
            oos.flush();
            // Messages are cached, so we need to reset() from time to time to clean the cache, or else we get an OutOfMemoryError.
            if(oosByteCount > OOS_RESET_THRESHOLD) {
                oos.reset();
                oosByteCount = 0;
            }
        }
    }

    private Message<?> readMessage() throws IOException, ClassNotFoundException {
        Object o = ois.readUnshared();
        if(o instanceof Message) {
            Message<?> message = (Message<?>)o;
            if(IS_DEBUGGING_MESSAGES) {
                System.err.println("[" + (isClient? "client": "server") + "] RECV: " + message.getID() + ", " + message);
            }
//            message.setCommunicationInterface(communicationInterface);
            return message;
        }
        System.err.println("[" + (isClient? "client": "server") + "] Unknown message: " + o);
        return null;
    }

}
