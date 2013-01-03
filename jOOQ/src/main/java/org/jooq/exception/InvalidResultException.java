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
package org.jooq.exception;

import org.jooq.Query;
import org.jooq.ResultQuery;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;

/**
 * An unexpected result was encountered after executing a {@link Query}. This
 * exception indicates wrong usage of jOOQ's various fetch methods, or an
 * integrity problem in your data.
 * <p>
 * This is typically the case in the following situations:
 * <ul>
 * <li>When you call methods such as {@link ResultQuery#fetchOne()} and the
 * database returns more than one record.</li>
 * <li>When you call methods such as
 * {@link ResultQuery#fetchMap(org.jooq.Field)} and the database returns several
 * records per key.</li>
 * <li>When you refresh a {@link TableRecord} using
 * {@link UpdatableRecord#refresh()}, and the record does not exist anymore in
 * the database.</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class InvalidResultException extends DataAccessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6460945824599280420L;

    /**
     * Constructor for InvalidResultException.
     *
     * @param message the detail message
     */
    public InvalidResultException(String message) {
        super(message);
    }
}
