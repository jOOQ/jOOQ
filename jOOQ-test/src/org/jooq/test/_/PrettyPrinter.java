/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test._;

import java.util.concurrent.atomic.AtomicInteger;

import org.jooq.ExecuteContext;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.Factory;
import org.jooq.tools.StringUtils;

/**
 * An execute listener that does pretty printing of every executed statement
 *
 * @author Lukas Eder
 */
public class PrettyPrinter extends DefaultExecuteListener {

    private static final AtomicInteger count = new AtomicInteger();

    /**
     * Hook into the query execution lifecycle before executing queries
     */
    @Override
    public void executeStart(ExecuteContext ctx) {

        // Create a new factory for logging rendering purposes
        // This factory doesn't need a connection, only the SQLDialect...
        Factory pretty = new Factory(ctx.getDialect(),

        // ... and the flag for pretty-printing
            SettingsTools.clone(ctx.getSettings()).withRenderFormatted(true));

        Factory normal = new Factory(ctx.getDialect());

        String n = "" + count.incrementAndGet();

        System.out.println();
        System.out.println("Executing #" + n);
        System.out.println("-----------" + StringUtils.leftPad("", n.length(), '-'));

        // If we're executing a query
        if (ctx.query() != null) {
            System.out.println(normal.renderInlined(ctx.query()));
            System.out.println();
            System.out.println(pretty.renderContext()
                                     .inline(true)
                                     .render(ctx.query()));
        }

        // If we're executing a routine
        else if (ctx.routine() != null) {
            System.out.println(normal.renderInlined(ctx.routine()));
            System.out.println();
            System.out.println(pretty.renderContext()
                                     .inline(true)
                                     .render(ctx.routine()));
        }

        // If we're executing anything else (e.g. plain SQL)
        else if (!StringUtils.isBlank(ctx.sql())) {
            System.out.println(ctx.sql());
        }
    }
}