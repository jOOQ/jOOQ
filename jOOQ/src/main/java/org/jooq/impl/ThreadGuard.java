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
 *
 *
 *
 */
package org.jooq.impl;

import java.util.function.Supplier;

/**
 * This API acts as a "guard" to prevent the same code from being executed
 * recursively within the same thread.
 */
final class ThreadGuard {

    static final class Guard {
        final ThreadLocal<Object> tl = new ThreadLocal<>();
    }

    static final ThreadGuard.Guard RECORD_TOSTRING = new Guard();

    /**
     * A guarded operation.
     */
    static interface GuardedOperation<V> {

        /**
         * This callback is executed only once on the current stack.
         */
        V unguarded();

        /**
         * This callback is executed if {@link #unguarded()} has already been executed on the current stack.
         */
        V guarded();
    }

    /**
     * Run an operation using a guard.
     */
    static final void run(ThreadGuard.Guard guard, Runnable unguardedOperation, Runnable guardedOperation) {
        run(guard, () -> { unguardedOperation.run(); return null; }, () -> { guardedOperation.run(); return null; });
    }

    /**
     * Run an operation using a guard.
     */
    static final <V> V run(ThreadGuard.Guard guard, Supplier<V> unguardedOperation, Supplier<V> guardedOperation) {
        boolean unguarded = (guard.tl.get() == null);
        if (unguarded)
            guard.tl.set(ThreadGuard.Guard.class);

        try {
            if (unguarded)
                return unguardedOperation.get();
            else
                return guardedOperation.get();
        }
        finally {
            if (unguarded)
                guard.tl.remove();
        }
    }
}