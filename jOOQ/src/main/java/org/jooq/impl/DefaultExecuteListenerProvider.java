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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;

/**
 * A default implementation for {@link ExecuteListenerProvider}.
 * <p>
 * This implementation just wraps a <code>List</code> of {@link ExecuteListener}
 * instances, always providing the same.
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
    private List<ExecuteListener> listeners;

    /**
     * Create a new provider instance with an empty <code>ArrayList</code>
     * argument.
     */
    public DefaultExecuteListenerProvider() {
        this(new ArrayList<ExecuteListener>());
    }

    /**
     * Create a new provider instance from an argument <code>List</code>.
     *
     * @param listeners The argument list.
     */
    public DefaultExecuteListenerProvider(ExecuteListener... listeners) {
        this.listeners = new ArrayList<ExecuteListener>(Arrays.asList(listeners));
    }

    /**
     * Create a new provider instance from an argument <code>List</code>.
     *
     * @param listeners The argument list.
     */
    public DefaultExecuteListenerProvider(List<ExecuteListener> listeners) {
        this.listeners = listeners;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<ExecuteListener> provide() {
        return listeners;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "" + listeners;
    }
}
