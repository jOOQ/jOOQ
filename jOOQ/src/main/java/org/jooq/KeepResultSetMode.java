/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.ResultSet;

/**
 * A {@link ResultQuery}'s execution mode with respect to keeping open JDBC
 * {@link ResultSet} references after fetching.
 * <p>
 * This mode type is used with
 * {@link ResultQuery#keepResultSet(KeepResultSetMode)} to indicate how to deal
 * with JDBC {@link ResultSet} references after fetching them into jOOQ
 * {@link Result} objects.
 * <p>
 * See the various modes for details.
 *
 * @author Lukas Eder
 */
public enum KeepResultSetMode {

    /**
     * Close the JDBC {@link ResultSet} after consuming it (this is the
     * default).
     */
    CLOSE_AFTER_FETCH,

    /**
     * Keep the JDBC {@link ResultSet} after consuming it.
     * <p>
     * Client code must assure that the {@link ResultSet} is closed explicitly
     * to free database resources. Closing can be done through any of these
     * methods:
     * <ul>
     * <li>{@link ResultSet#close()}</li>
     * <li>{@link Result#close()}</li>
     * <li>{@link Cursor#close()}</li>
     * </ul>
     */
    KEEP_AFTER_FETCH,

    /**
     * Keep the JDBC {@link ResultSet} after consuming it, updating the
     * <code>ResultSet</code> at every change of a {@link Record}.
     * <p>
     * TODO: More details here
     * <p>
     * Client code must assure that the {@link ResultSet} is closed explicitly
     * to free database resources. Closing can be done through any of these
     * methods:
     * <ul>
     * <li>{@link ResultSet#close()}</li>
     * <li>{@link Result#close()}</li>
     * <li>{@link Cursor#close()}</li>
     * </ul>
     */
    UPDATE_ON_CHANGE,

    /**
     * Keep the JDBC {@link ResultSet} after consuming it, updating the
     * <code>ResultSet</code> at every call to {@link Record#store()}, or
     * {@link Result#store()} (<strong>This is not yet supported</strong>).
     * <p>
     * TODO: More details here
     * <p>
     * Client code must assure that the {@link ResultSet} is closed explicitly
     * to free database resources. Closing can be done through any of these
     * methods:
     * <ul>
     * <li>{@link ResultSet#close()}</li>
     * <li>{@link Result#close()}</li>
     * <li>{@link Cursor#close()}</li>
     * </ul>
     */
    UPDATE_ON_STORE
}
