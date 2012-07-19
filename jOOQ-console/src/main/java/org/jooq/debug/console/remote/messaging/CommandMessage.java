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

import java.util.Arrays;

/**
 * A type of message that executes a command with optional arguments and returns a result.
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public abstract class CommandMessage extends Message {

    public CommandMessage() {
    }

    private Object[] args;

    /**
     * Set the arguments that will be used when the message is run.
     * @param arguments the arguments, which must be serializable.
     */
    void setArgs(Object... arguments) {
        if(arguments.length == 0) {
            arguments = null;
        }
        this.args = arguments;
    }

    /**
     * Execute that message asynchronously with the given arguments.
     * @param arguments the arguments, which must be serializable.
     */
    public void asyncExec(CommunicationInterface communicationInterface, Object... arguments) {
        setArgs(arguments);
        asyncSend(communicationInterface);
    }

    /**
     * Execute that message synchronously with the given arguments and return the result.
     * @param arguments the arguments, which must be serializable.
     * @return the result of the execution.
     */
    public Object syncExec(CommunicationInterface communicationInterface, Object... arguments) {
        setArgs(arguments);
        return syncSend(communicationInterface);
    }

    private static final Object[] EMPTY_ARGS = new Object[0];

    Object runCommand() throws Exception {
        return run(args == null? EMPTY_ARGS: args);
    }

    /**
     * Run the message.
     * @param arguments the arguments that were specified for that command, or an empty array if none were specified.
     * @return the result that may be passed back to the caller.
     * @throws Exception any exception that may happen, and which would be passed back if it is a synchronous execution.
     */
    public abstract Object run(Object[] arguments) throws Exception;

    @Override
    public String toString() {
        String s = super.toString();
        if(args == null || args.length == 0) {
            return s + "()";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(s).append('(');
        for(int i=0; i<args.length; i++) {
            Object arg = args[i];
            if(i > 0) {
                sb.append(", ");
            }
            if(arg != null && arg.getClass().isArray()) {
                sb.append(Arrays.deepToString((Object[])arg));
            } else {
                sb.append(arg);
            }
        }
        sb.append(')');
        return sb.toString();
    }

}
