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

import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.exception.DataAccessException;

/**
 * This type is used for the {@link Update}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * TableRecord<?> record =
 * create.update(table)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .returning(field1)
 *       .fetchOne();
 * </pre></code>
 * <p>
 * This implemented differently for every dialect:
 * <ul>
 * <li>Firebird and Postgres have native support for
 * <code>UPDATE .. RETURNING</code> clauses</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface UpdateResultStep<R extends Record> extends Insert<R> {

    /**
     * The result holding returned values as specified by the
     * {@link UpdateReturningStep}
     * <p>
     * This currently only works well for DB2, HSQLDB, MySQL, and Postgres
     *
     * @return The returned values as specified by the
     *         {@link UpdateReturningStep}. Note:
     *         <ul>
     *         <li>Not all databases / JDBC drivers support returning several
     *         values on multi-row inserts!</li><li>This may return an empty
     *         <code>Result</code> in case jOOQ could not retrieve any generated
     *         keys from the JDBC driver.</li>
     *         </ul>
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdateQuery#getReturnedRecords()
     */
    @Support({ FIREBIRD, POSTGRES })
    Result<R> fetch() throws DataAccessException;

    /**
     * The record holding returned values as specified by the
     * {@link UpdateReturningStep}
     *
     * @return The returned value as specified by the
     *         {@link UpdateReturningStep}. This may return <code>null</code> in
     *         case jOOQ could not retrieve any generated keys from the JDBC
     *         driver.
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdateQuery#getReturnedRecord()
     */
    @Support({ FIREBIRD, POSTGRES })
    R fetchOne() throws DataAccessException;
}
