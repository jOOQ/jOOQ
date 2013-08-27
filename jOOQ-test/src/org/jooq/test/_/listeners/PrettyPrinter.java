/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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