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
