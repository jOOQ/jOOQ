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

import java.util.Collection;
import java.util.Map;

/**
 * This type is used for the {@link Insert}'s alternative DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.insertInto(table)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .newRecord()
 *       .set(field1, value3)
 *       .set(field2, value4)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface InsertSetStep<R extends Record> {

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, T value);

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, Field<T> value);

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, Select<? extends Record1<T>> value);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>. jOOQ will attempt to convert values to their
     * corresponding field's type.
     */
    @Support
    InsertSetMoreStep<R> set(Map<? extends Field<?>, ?> map);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * This is the same as calling {@link #set(Map)} with the argument record
     * treated as a <code>Map<Field<?>, Object></code>.
     *
     * @see #set(Map)
     */
    @Support
    InsertSetMoreStep<R> set(Record record);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Object... values);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Field<?>... values);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Collection<?> values);

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement.
     * <p>
     * This variant of the <code>INSERT .. SELECT</code> statement does not
     * allow for specifying a subset of the fields inserted into. It will insert
     * into all fields of the table specified in the <code>INTO</code> clause.
     * Use {@link DSLContext#insertInto(Table, Field...)} or
     * {@link DSLContext#insertInto(Table, Collection)} instead, to
     * define a field set for insertion.
     */
    @Support
    Insert<R> select(Select<?> select);
}
