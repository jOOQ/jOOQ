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

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;

/**
 * A publicly available default implementation of {@link ExecuteListener}.
 * <p>
 * Use this to stay compatible with future API changes (i.e. added methods to
 * <code>ExecuteListener</code>)
 *
 * @author Lukas Eder
 */
public class DefaultExecuteListener implements ExecuteListener {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7399239846062763212L;

    @Override
    public void start(ExecuteContext ctx) {}

    @Override
    public void renderStart(ExecuteContext ctx) {}

    @Override
    public void renderEnd(ExecuteContext ctx) {}

    @Override
    public void prepareStart(ExecuteContext ctx) {}

    @Override
    public void prepareEnd(ExecuteContext ctx) {}

    @Override
    public void bindStart(ExecuteContext ctx) {}

    @Override
    public void bindEnd(ExecuteContext ctx) {}

    @Override
    public void executeStart(ExecuteContext ctx) {}

    @Override
    public void executeEnd(ExecuteContext ctx) {}

    @Override
    public void fetchStart(ExecuteContext ctx) {}

    @Override
    public void resultStart(ExecuteContext ctx) {}

    @Override
    public void recordStart(ExecuteContext ctx) {}

    @Override
    public void recordEnd(ExecuteContext ctx) {}

    @Override
    public void resultEnd(ExecuteContext ctx) {}

    @Override
    public void fetchEnd(ExecuteContext ctx) {}

    @Override
    public void end(ExecuteContext ctx) {}

    @Override
    public void exception(ExecuteContext ctx) {}

}
