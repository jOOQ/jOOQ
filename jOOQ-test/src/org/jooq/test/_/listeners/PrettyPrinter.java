/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.test._.listeners;

import static org.jooq.conf.ParamType.INLINED;

import java.util.concurrent.atomic.AtomicInteger;

import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.tools.StringUtils;

/**
 * An execute listener that does pretty printing of every executed statement.
 *
 * @author Lukas Eder
 */
public class PrettyPrinter extends DefaultExecuteListener {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7399239846062763212L;

    private static final AtomicInteger count = new AtomicInteger();

    /**
     * Hook into the query execution lifecycle before executing queries
     */
    @Override
    public void renderEnd(ExecuteContext ctx) {

        // Create a new factory for logging rendering purposes
        DSLContext pretty = DSL.using(new DefaultConfiguration()

            // This factory doesn't need a connection, only the SQLDialect...
            .set(ctx.configuration().dialect())

            // ... and the flag for pretty-printing
            .set(SettingsTools.clone(ctx.configuration().settings()).withRenderFormatted(true))

            // ... and visit listener providers for potential SQL transformations
            .set(ctx.configuration().visitListenerProviders()));

        DSLContext normal = DSL.using(new DefaultConfiguration()
            .set(ctx.configuration().dialect())
            .set(ctx.configuration().visitListenerProviders()));

        String n = "" + count.incrementAndGet();

        System.out.println();
        System.out.println("Executing #" + n);
        System.out.println("-----------" + StringUtils.leftPad("", n.length(), '-'));

        // If we're executing a query
        if (ctx.query() != null) {
            System.out.println(normal.renderInlined(ctx.query()));
            System.out.println();
            System.out.println(pretty.renderContext()
                                     .paramType(INLINED)
                                     .render(ctx.query()));
        }

        // If we're executing a routine
        else if (ctx.routine() != null) {
            System.out.println(normal.renderInlined(ctx.routine()));
            System.out.println();
            System.out.println(pretty.renderContext()
                                     .paramType(INLINED)
                                     .render(ctx.routine()));
        }

        // If we're executing anything else (e.g. plain SQL)
        else if (!StringUtils.isBlank(ctx.sql())) {
            System.out.println(ctx.sql());
        }
    }
}