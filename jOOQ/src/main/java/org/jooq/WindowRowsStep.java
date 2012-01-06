/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SYBASE;

/**
 * This type is used for the window function DSL API.
 * <p>
 * Example: <code><pre>
 * field.firstValue()
 *      .ignoreNulls()
 *      .over()
 *      .partitionBy(AUTHOR_ID)
 *      .orderBy(PUBLISHED_IN.asc())
 *      .rowsBetweenUnboundedPreceding()
 *      .andUnboundedFollowing()
 * </pre></code>
 * <p>
 * Note, that the functionality provided by this interface is not supported in
 * {@link SQLDialect#SQLSERVER}
 *
 * @param <T> The function return type
 * @author Lukas Eder
 */
public interface WindowRowsStep<T> extends WindowFinalStep<T> {

    /**
     * Add a <code>ROWS UNBOUNDED PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowFinalStep<T> rowsUnboundedPreceding();

    /**
     * Add a <code>ROWS [number] PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowFinalStep<T> rowsPreceding(int number);

    /**
     * Add a <code>ROWS CURRENT ROW</code> frame clause to the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowFinalStep<T> rowsCurrentRow();

    /**
     * Add a <code>ROWS UNBOUNDED FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowFinalStep<T> rowsUnboundedFollowing();

    /**
     * Add a <code>ROWS [number] FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowFinalStep<T> rowsFollowing(int number);

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowRowsAndStep<T> rowsBetweenUnboundedPreceding();

    /**
     * Add a <code>ROWS BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowRowsAndStep<T> rowsBetweenPreceding(int number);

    /**
     * Add a <code>ROWS BETWEEN CURRENT ROW ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowRowsAndStep<T> rowsBetweenCurrentRow();

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowRowsAndStep<T> rowsBetweenUnboundedFollowing();

    /**
     * Add a <code>ROWS BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowRowsAndStep<T> rowsBetweenFollowing(int number);
}
