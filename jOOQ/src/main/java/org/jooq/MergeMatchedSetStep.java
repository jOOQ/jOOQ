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
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.Map;

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
public interface MergeMatchedSetStep<R extends Record> {

    /**
     * Set values for <code>UPDATE</code> in the <code>MERGE</code> statement's
     * <code>WHEN MATCHED</code> clause.
     */
    @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    @Transition(
        name = "SET",
        args = {
            "Field",
            "Object"
        }
    )
    <T> MergeMatchedSetMoreStep<R> set(Field<T> field, T value);

    /**
     * Set values for <code>UPDATE</code> in the <code>MERGE</code> statement's
     * <code>WHEN MATCHED</code> clause.
     */
    @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    @Transition(
        name = "SET",
        args = {
            "Field",
            "Field"
        }
    )
    <T> MergeMatchedSetMoreStep<R> set(Field<T> field, Field<T> value);

    /**
     * Set values for <code>UPDATE</code> in the <code>MERGE</code> statement's
     * <code>WHEN MATCHED</code> clause.
     */
    @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    @Transition(
        name = "SET",
        args = {
            "Field",
            "Select"
        }
    )
    <T> MergeMatchedSetMoreStep<R> set(Field<T> field, Select<? extends Record1<T>> value);

    /**
     * Set multiple values for <code>UPDATE</code> in the <code>MERGE</code>
     * statement's <code>WHEN MATCHED</code> clause.
     * <p>
     * Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>. jOOQ will attempt to convert values to their
     * corresponding field's type.
     */
    @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    MergeMatchedSetMoreStep<R> set(Map<? extends Field<?>, ?> map);

    /**
     * Set multiple values for <code>UPDATE</code> in the <code>MERGE</code>
     * statement's <code>WHEN MATCHED</code> clause.
     * <p>
     * This is the same as calling {@link #set(Map)} with the argument record
     * treated as a <code>Map<Field<?>, Object></code>.
     *
     * @see #set(Map)
     */
    @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    MergeMatchedSetMoreStep<R> set(Record record);
}
