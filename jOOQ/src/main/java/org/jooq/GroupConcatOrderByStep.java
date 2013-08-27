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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE11G;
import static org.jooq.SQLDialect.ORACLE12C;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SYBASE;

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
import org.jooq.impl.DSL;


/**
 * MySQL's <code>GROUP_CONCAT</code> function.
 *
 * @author Lukas Eder
 * @see DSL#listAgg(Field)
 */
@State
public interface GroupConcatOrderByStep extends GroupConcatSeparatorStep {

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MARIADB, MYSQL, ORACLE11G, ORACLE12C, POSTGRES, SYBASE })
    @Transition(
        name = "ORDER BY",
        args = "Field+"
    )
    GroupConcatSeparatorStep orderBy(Field<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MARIADB, MYSQL, ORACLE11G, ORACLE12C, POSTGRES, SYBASE })
    @Transition(
        name = "ORDER BY",
        args = "SortField+"
    )
    GroupConcatSeparatorStep orderBy(SortField<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, MARIADB, MYSQL, ORACLE11G, ORACLE12C, POSTGRES, SYBASE })
    @Transition(
        name = "ORDER BY",
        args = "SortField+"
    )
    GroupConcatSeparatorStep orderBy(Collection<SortField<?>> fields);
}
