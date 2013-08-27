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
package org.jooq.exception;

import org.jooq.ExecuteListener;
import org.jooq.UpdatableRecord;

/**
 * An exception that is used to influence control flows.
 * <p>
 * There are some specific cases, where control flows can be aborted or
 * otherwise influenced using well-defined exceptions. Some examples where this
 * can be very useful:
 * <ul>
 * <li>When generating SQL from {@link UpdatableRecord#store()} methods, without
 * actually executing the SQL</li>
 * </ul>
 * <p>
 * Typically, a <code>ControlFlowException</code> is thrown from within an
 * {@link ExecuteListener}.
 *
 * @author Lukas Eder
 * @see ExecuteListener
 */
public class ControlFlowSignal extends RuntimeException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -3269663230218616147L;

    /**
     * Create a new <code>ControlFlowException</code>.
     */
    public ControlFlowSignal() {}

    /**
     * Create a new <code>ControlFlowException</code>.
     */
    public ControlFlowSignal(String message) {
        super(message);
    }

    @SuppressWarnings("sync-override")
    @Override
    public Throwable fillInStackTrace() {

        // Prevent "expensive" operation of filling in a stack trace for signals
        return this;
    }
}
