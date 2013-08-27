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
package org.jooq.exception;

import org.jooq.DSLContext;
import org.jooq.Cursor;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;

/**
 * An error occurred while fetching data into a user defined Java object with
 * any of these methods:
 * <ul>
 * <li> {@link ResultQuery#fetchInto(Class)}</li>
 * <li> {@link Cursor#fetchInto(Class)}</li>
 * <li> {@link Result#into(Class)}</li>
 * <li> {@link Record#into(Class)}</li>
 * </ul>
 * ... or when copying data into a {@link Record} with any of these methods
 * <ul>
 * <li> {@link DSLContext#newRecord(org.jooq.Table, Object)}</li>
 * <li> {@link Record#from(Object)}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class MappingException extends DataAccessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6460945824599280420L;

    /**
     * Constructor for MappingException.
     *
     * @param message the detail message
     */
    public MappingException(String message) {
        super(message);
    }

    /**
     * Constructor for MappingException.
     *
     * @param message the detail message
     * @param cause the root cause
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
