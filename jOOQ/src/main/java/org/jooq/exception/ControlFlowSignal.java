/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
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
