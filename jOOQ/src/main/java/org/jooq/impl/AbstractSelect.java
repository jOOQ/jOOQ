/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
