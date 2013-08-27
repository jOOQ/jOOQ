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
class FutureResultImpl<R extends Record> implements FutureResult<R> {

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
