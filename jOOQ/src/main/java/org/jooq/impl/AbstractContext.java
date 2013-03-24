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

import java.util.HashMap;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Context;

/**
 * @author Lukas Eder
 */
abstract class AbstractContext<C extends Context<C>> implements Context<C> {

    final Configuration       configuration;
    final Map<Object, Object> data;
    boolean                   declareFields;
    boolean                   declareTables;
    boolean                   subquery;
    int                       index;

    AbstractContext(Configuration configuration) {
        this.configuration = configuration;
        this.data = new HashMap<Object, Object>();
    }

    // ------------------------------------------------------------------------
    // XXX Context API
    // ------------------------------------------------------------------------

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @Override
    public final Map<Object, Object> data() {
        return data;
    }

    @Override
    public final Object data(Object key) {
        return data.get(key);
    }

    @Override
    public final Object data(Object key, Object value) {
        return data.put(key, value);
    }

    @Override
    public final boolean declareFields() {
        return declareFields;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final C declareFields(boolean d) {
        this.declareFields = d;
        return (C) this;
    }

    @Override
    public final boolean declareTables() {
        return declareTables;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final C declareTables(boolean d) {
        this.declareTables = d;
        return (C) this;
    }

    @Override
    public final boolean subquery() {
        return subquery;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final C subquery(boolean s) {
        this.subquery = s;
        return (C) this;
    }

    @Override
    public final int nextIndex() {
        return ++index;
    }

    @Override
    public final int peekIndex() {
        return index + 1;
    }

    // ------------------------------------------------------------------------
    // XXX Object API
    // ------------------------------------------------------------------------

    void toString(StringBuilder sb) {
        sb.append(  "bind index   [");
        sb.append(index);
        sb.append("]");
        sb.append("\ndeclaring    [");

        if (declareFields) {
            sb.append("fields");
        }
        else if (declareTables) {
            sb.append("tables");
        }
        else {
            sb.append("-");
        }

        sb.append("]\nsubquery     [");
        sb.append(subquery);
        sb.append("]");
    }
}
