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

import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * A {@link Query} that can truncate a table in the database.
 *
 * @author Lukas Eder
 */
@State
public interface TruncateCascadeStep<R extends Record> extends TruncateFinalStep<R> {

    /**
     * Add the <code>CASCADE</code> clause to the <code>TRUNCATE</code>
     * statement.
     */
    @Support(POSTGRES)
    @Transition(
        name = "CASCADE"
    )
    TruncateFinalStep<R> cascade();

    /**
     * Add the <code>RESTRICT</code> clause to the <code>TRUNCATE</code>
     * statement.
     */
    @Support(POSTGRES)
    @Transition(
        name = "RESTRICT"
    )
    TruncateFinalStep<R> restrict();
}
