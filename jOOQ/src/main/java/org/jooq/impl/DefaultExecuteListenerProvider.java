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
package org.jooq.impl;

import java.io.Serializable;

import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;

/**
 * A default implementation for {@link ExecuteListenerProvider}.
 * <p>
 * This implementation just wraps an instance of {@link ExecuteListener}, always
 * providing the same.
 * 
 * @author Lukas Eder
 */
public class DefaultExecuteListenerProvider implements ExecuteListenerProvider, Serializable {

    /**
     * Generated UID.
     */
    private static final long     serialVersionUID = -2122007794302549679L;

    /**
     * The delegate list.
     */
    private final ExecuteListener listener;

    /**
     * Convenience method to construct an array of
     * <code>DefaultExecuteListenerProvider</code> from an array of
     * <code>ExecuteListener</code> instances.
     */
    public static ExecuteListenerProvider[] providers(ExecuteListener... listeners) {
        ExecuteListenerProvider[] result = new ExecuteListenerProvider[listeners.length];

        for (int i = 0; i < listeners.length; i++) {
            result[i] = new DefaultExecuteListenerProvider(listeners[i]);
        }

        return result;
    }

    /**
     * Create a new provider instance from an argument listener.
     *
     * @param listener The argument listener.
     */
    public DefaultExecuteListenerProvider(ExecuteListener listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ExecuteListener provide() {
        return listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return listener.toString();
    }
}
