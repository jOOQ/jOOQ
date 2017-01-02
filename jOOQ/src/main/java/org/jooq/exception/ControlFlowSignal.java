/*
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
