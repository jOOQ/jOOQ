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
