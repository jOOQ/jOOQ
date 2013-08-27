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
import static org.jooq.SQLDialect.ORACLE;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * This type is used for the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.mergeInto(table)
 *       .using(select)
 *       .on(condition)
 *       .whenMatchedThenUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .whenNotMatchedThenInsert(field1, field2)
 *       .values(value1, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
@State
public interface MergeNotMatchedWhereStep<R extends Record> extends MergeFinalStep<R> {

    /**
     * Add an additional <code>WHERE</code> clause to the preceding
     * <code>WHEN NOT MATCHED THEN INSERT</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     */
    @Support({ CUBRID, ORACLE })
    @Transition(
        name = "WHERE",
        args = "Condition"
    )
    MergeFinalStep<R> where(Condition condition);

    /**
     * Add an additional <code>WHERE</code> clause to the preceding
     * <code>WHEN NOT MATCHED THEN INSERT</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     */
    @Support({ CUBRID, ORACLE })
    @Transition(
        name = "WHERE",
        args = "Condition"
    )
    MergeFinalStep<R> where(Field<Boolean> condition);
}
