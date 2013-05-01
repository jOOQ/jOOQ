/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.exception;

import org.jooq.ExecuteListener;
import org.jooq.UpdatableRecord;

/**
 * An exception that is used to influence control flows.
 * <p>
 * There are some specific cases, where control flows can be aborted or
 * otherwise influenced using well-defined exceptions. Some examples where this
 * can be very useful:
 * <ul>
 * <li>When generating SQL from {@link UpdatableRecord#store()} methods, without
 * actually executing the SQL</li>
 * </ul>
 * <p>
 * Typically, a <code>ControlFlowException</code> is thrown from within an
 * {@link ExecuteListener}.
 *
 * @author Lukas Eder
 * @see ExecuteListener
 */
public class ControlFlowSignal extends RuntimeException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -3269663230218616147L;

    /**
     * Create a new <code>ControlFlowException</code>.
     */
    public ControlFlowSignal() {}

    /**
     * Create a new <code>ControlFlowException</code>.
     */
    public ControlFlowSignal(String message) {
        super(message);
    }

    @SuppressWarnings("sync-override")
    @Override
    public Throwable fillInStackTrace() {

        // Prevent "expensive" operation of filling in a stack trace for signals
        return this;
    }
}
