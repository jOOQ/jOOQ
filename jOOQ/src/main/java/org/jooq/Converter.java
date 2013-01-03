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

import java.io.Serializable;

import org.jooq.impl.SQLDataType;

/**
 * A <code>Converter</code> for data types.
 * <p>
 * A general data type conversion interface that can be provided to jOOQ at
 * various places in order to perform custom data type conversion. Conversion is
 * directed, this means that the <code>Converter</code> is used
 * <ul>
 * <li>to load database types converting them to user types "FROM" the database.
 * Hence, {@link #fromType()} is the type as defined in the database.</li>
 * <li>to store user types converting them to database types "TO" the database.
 * Hence, {@link #toType()} is the user-defined type</li>
 * </ul>
 * <p>
 * Note: In order to avoid unwanted side-effects, it is highly recommended (yet
 * not required) for {@link #from(Object)} and {@link #to(Object)} to be
 * <strong>reciprocal</strong>. The two methods are reciprocal, if for all
 * <code>X and Y</code>, it can be said that
 * <ul>
 * <li>if <code>Y.equals(converter.from(X))</code>, then
 * <code>X.equals(converter.to(Y))</code>.</li>
 * <li>
 * <code>X.equals(converter.from(converter.to(X)))</code></li>
 * <li><code>X.equals(converter.to(converter.from(X)))</code></li>
 * </ul>
 * <p>
 * Furthermore, it is recommended (yet not required) that
 * <ul>
 * <li><code>converter.from(null) == null</code></li>
 * <li><code>converter.to(null) == null</code></li>
 * </ul>
 *
 * @author Lukas Eder
 * @param <T> The database type - i.e. any type available from
 *            {@link SQLDataType}
 * @param <U> The user type
 */
public interface Converter<T, U> extends Serializable {

    /**
     * Convert a database object to a user object
     *
     * @param databaseObject The database object
     * @return The user object
     */
    U from(T databaseObject);

    /**
     * Convert a user object to a database object
     *
     * @param userObject The user object
     * @return The database object
     */
    T to(U userObject);

    /**
     * The database type
     */
    Class<T> fromType();

    /**
     * The user type
     */
    Class<U> toType();
}
