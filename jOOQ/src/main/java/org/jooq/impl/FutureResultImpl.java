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
package org.jooq.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jooq.FutureResult;
import org.jooq.Record;
import org.jooq.Result;

/**
 * @author Lukas Eder
 */
@Deprecated
final class FutureResultImpl<R extends Record> implements FutureResult<R> {

    private final Future<Result<R>> future;
    private final ExecutorService executor;

    FutureResultImpl(Future<Result<R>> future) {
        this(future, null);
    }

    FutureResultImpl(Future<Result<R>> future, ExecutorService executor) {
        this.future = future;
        this.executor = executor;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        try {
            return future.cancel(mayInterruptIfRunning);
        }
        finally {
            if (executor != null) {
                executor.shutdownNow();
            }
        }
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public Result<R> get() throws InterruptedException, ExecutionException {
        try {
            return future.get();
        }
        finally {
            if (executor != null) {
                executor.shutdownNow();
            }
        }
    }

    @Override
    public Result<R> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            return future.get(timeout, unit);
        }
        finally {
            if (executor != null) {
                executor.shutdownNow();
            }
        }
    }

}
