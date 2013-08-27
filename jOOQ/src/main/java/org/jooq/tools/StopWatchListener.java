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
package org.jooq.tools;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;

/**
 * A default {@link ExecuteListener} that just logs events to java.util.logging,
 * log4j, or slf4j using the {@link JooqLogger}
 *
 * @author Lukas Eder
 */
public class StopWatchListener implements ExecuteListener {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7399239846062763212L;

    private final StopWatch watch = new StopWatch();

    @Override
    public void start(ExecuteContext ctx) {
        watch.splitTrace("Initialising");
    }

    @Override
    public void renderStart(ExecuteContext ctx) {
        watch.splitTrace("Rendering query");
    }

    @Override
    public void renderEnd(ExecuteContext ctx) {
        watch.splitTrace("Query rendered");
    }

    @Override
    public void prepareStart(ExecuteContext ctx) {
        watch.splitTrace("Preparing statement");
    }

    @Override
    public void prepareEnd(ExecuteContext ctx) {
        watch.splitTrace("Statement prepared");
    }

    @Override
    public void bindStart(ExecuteContext ctx) {
        watch.splitTrace("Binding variables");
    }

    @Override
    public void bindEnd(ExecuteContext ctx) {
        watch.splitTrace("Variables bound");
    }

    @Override
    public void executeStart(ExecuteContext ctx) {
        watch.splitTrace("Executing query");
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        watch.splitDebug("Query executed");
    }

    @Override
    public void fetchStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching results");
    }

    @Override
    public void resultStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching result");
    }

    @Override
    public void recordStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching record");
    }

    @Override
    public void recordEnd(ExecuteContext ctx) {
        watch.splitTrace("Record fetched");
    }

    @Override
    public void resultEnd(ExecuteContext ctx) {
        watch.splitTrace("Result fetched");
    }

    @Override
    public void fetchEnd(ExecuteContext ctx) {
        watch.splitTrace("Results fetched");
    }

    @Override
    public void end(ExecuteContext ctx) {
        watch.splitDebug("Finishing");
    }

    @Override
    public void exception(ExecuteContext ctx) {
        watch.splitDebug("Exception");
    }
}
