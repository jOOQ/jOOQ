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
