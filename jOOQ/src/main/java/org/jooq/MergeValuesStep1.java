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
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.Collection;

import javax.annotation.Generated;

/**
 * This type is used for the H2-specific variant of the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
 * using(configuration)
 *       .mergeInto(table, field1)
 *       .key(id)
 *       .values(field1)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface MergeValuesStep1<R extends Record, T1> {

    /**
     * Specify a <code>VALUES</code> clause
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    Merge<R> values(T1 value1);

    /**
     * Specify a <code>VALUES</code> clause
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    Merge<R> values(Field<T1> value1);

    /**
     * Specify a <code>VALUES</code> clause
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    Merge<R> values(Collection<?> values);

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>MERGE</code> statement
     * <p>
     * This variant of the <code>MERGE .. SELECT</code> statement expects a
     * select returning exactly as many fields as specified previously in the
     * <code>INTO</code> clause:
     * {@link DSLContext#mergeInto(Table, Field)}
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    Merge<R> select(Select<? extends Record1<T1>> select);
}
