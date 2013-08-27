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

import java.util.HashMap;
import java.util.Map;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.impl.DefaultExecuteListener;

/**
 * An <code>ExecuteListener</code> that counts the number of executions in all
 * integration tests.
 *
 * @author Lukas Eder
 */
public class TestStatisticsListener extends DefaultExecuteListener {

    /**
     * Generated UID
     */
    private static final long               serialVersionUID = 7399239846062763212L;

    public static Map<ExecuteType, Integer> STATISTICS       = new HashMap<ExecuteType, Integer>();

    @Override
    public synchronized void start(ExecuteContext ctx) {
        Integer count = STATISTICS.get(ctx.type());

        if (count == null) {
            count = 0;
        }

        STATISTICS.put(ctx.type(), count + 1);
    }

}
