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
package org.jooq.impl;

import static org.jooq.impl.Utils.fieldArray;

import java.sql.ResultSetMetaData;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;

/**
 * A common base class for all <code>SELECT</code> statements.
 *
 * @author Lukas Eder
 */
abstract class AbstractSelect<R extends Record> extends AbstractResultQuery<R> implements Select<R> {

    private static final long       serialVersionUID = 5432006637149005588L;

    AbstractSelect(Configuration configuration) {
        super(configuration);
    }

    @Override
    public final int fetchCount() throws DataAccessException {
        return DSL.using(configuration()).fetchCount(this);
    }

    @Override
    public final Select<R> union(Select<? extends R> select) {
        return new Union<R>(configuration(), this, select, CombineOperator.UNION);
    }

    @Override
    public final Select<R> unionAll(Select<? extends R> select) {
        return new Union<R>(configuration(), this, select, CombineOperator.UNION_ALL);
    }

    @Override
    public final Select<R> except(Select<? extends R> select) {
        return new Union<R>(configuration(), this, select, CombineOperator.EXCEPT);
    }

    @Override
    public final Select<R> intersect(Select<? extends R> select) {
        return new Union<R>(configuration(), this, select, CombineOperator.INTERSECT);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> Field<T> asField() {
        if (getSelect().size() != 1) {
            throw new IllegalStateException("Can only use single-column ResultProviderQuery as a field");
        }

        return new SelectQueryAsField<T>(this, (DataType<T>) getSelect().get(0).getDataType());
    }

    @Override
    public final <T> Field<T> asField(String alias) {
        return this.<T> asField().as(alias);
    }

    @Override
    public final Row fieldsRow() {
        return asTable().fieldsRow();
    }

    @Override
    public final <T> Field<T> field(Field<T> field) {
        return asTable().field(field);
    }

    @Override
    public final Field<?> field(String string) {
        return asTable().field(string);
    }

    @Override
    public final Field<?> field(int index) {
        return asTable().field(index);
    }

    @Override
    public final Field<?>[] fields() {
        return asTable().fields();
    }

    @Override
    public final Table<R> asTable() {
        // Its usually better to alias nested selects that are used in
        // the FROM clause of a query
        return new SelectQueryAsTable<R>(this).as("alias_" + Utils.hash(this));
    }

    @Override
    public final Table<R> asTable(String alias) {
        return new SelectQueryAsTable<R>(this).as(alias);
    }

    @Override
    public final Table<R> asTable(String alias, String... fieldAliases) {
        return new SelectQueryAsTable<R>(this).as(alias, fieldAliases);
    }

    @Override
    protected final Field<?>[] getFields(ResultSetMetaData meta) {

        // [#1808] TODO: Restrict this field list, in case a restricting fetch()
        // method was called to get here
        List<Field<?>> fields = getSelect();

        // If no projection was specified explicitly, create fields from result
        // set meta data instead. This is typically the case for SELECT * ...
        if (fields.isEmpty()) {
            Configuration configuration = configuration();
            return new MetaDataFieldProvider(configuration, meta).getFields();
        }

        return fieldArray(fields);
    }

    @Override
    final boolean isSelectingRefCursor() {
        for (Field<?> field : getSelect()) {
            if (Result.class.isAssignableFrom(field.getType())) {
                return true;
            }
        }

        return false;
    }
}
