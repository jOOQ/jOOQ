/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

import static org.jooq.impl.DSL.row;
import static org.jooq.impl.Utils.filterOne;
import static org.jooq.impl.Utils.first;
import static org.jooq.impl.Utils.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.AttachableInternal;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.RowN;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.DetachedException;

/**
 * @author Lukas Eder
 */
class ReferenceImpl<R extends Record, O extends Record> extends AbstractKey<R> implements ForeignKey<R, O> {

    /**
     * Generated UID
     */
    private static final long  serialVersionUID = 3636724364192618701L;

    private final UniqueKey<O> key;

    /* [java-8] */
    @SafeVarargs
    /* [/java-8] */
    ReferenceImpl(UniqueKey<O> key, Table<R> table, TableField<R, ?>... fields) {
        super(table, fields);

        this.key = key;
    }

    @Override
    public final UniqueKey<O> getKey() {
        return key;
    }

    @Override
    public final O fetchParent(R record) {
        return filterOne(fetchParents(record));
    }

    @Override
    /* [java-8] */
    @SafeVarargs
    /* [/java-8] */
    public final Result<O> fetchParents(R... records) {
        return fetchParents(list(records));
    }

    @Override
    public final Result<R> fetchChildren(O record) {
        return fetchChildren(list(record));
    }

    @Override
    /* [java-8] */
    @SafeVarargs
    /* [/java-8] */
    public final Result<R> fetchChildren(O... records) {
        return fetchChildren(list(records));
    }

    @Override
    public final Result<O> fetchParents(Collection<? extends R> records) {
        if (records == null || records.size() == 0) {
            return new ResultImpl<O>(new DefaultConfiguration(), key.getFields());
        }
        else {
            return fetch(records, key.getTable(), key.getFieldsArray(), getFieldsArray());
        }
    }

    @Override
    public final Result<R> fetchChildren(Collection<? extends O> records) {
        if (records == null || records.size() == 0) {
            return new ResultImpl<R>(new DefaultConfiguration(), getFields());
        }
        else {
            return fetch(records, getTable(), getFieldsArray(), key.getFieldsArray());
        }
    }

    /**
     * Do the actual fetching
     */
    @SuppressWarnings("unchecked")
    private static <R1 extends Record, R2 extends Record> Result<R1> fetch(
        Collection<? extends R2> records,
        Table<R1> table,
        TableField<R1, ?>[] fields1,
        TableField<R2, ?>[] fields2) {

        // Use regular predicates
        if (fields1.length == 1) {
            return extractDSLContext(records)
                .selectFrom(table)
                .where(((Field<Object>) fields1[0]).in(extractValues(records, fields2[0])))
                .fetch();
        }

        // Use row value expressions
        else {
            return extractDSLContext(records)
                .selectFrom(table)
                .where(row(fields1).in(extractRows(records, fields2)))
                .fetch();
        }

    }

    /**
     * Extract a list of values from a set of records given some fields
     */
    private static <R extends Record> List<Object> extractValues(Collection<? extends R> records, TableField<R, ?> field2) {
        List<Object> result = new ArrayList<Object>();

        for (R record : records) {
            result.add(record.getValue(field2));
        }

        return result;
    }

    /**
     * Extract a list of row value expressions from a set of records given some fields
     */
    private static <R extends Record> List<RowN> extractRows(Collection<? extends R> records, TableField<R, ?>[] fields) {
        List<RowN> rows = new ArrayList<RowN>();

        for (R record : records) {
            Object[] values = new Object[fields.length];

            for (int i = 0; i < fields.length; i++) {
                values[i] = record.getValue(fields[i]);
            }

            rows.add(row(values));
        }

        return rows;
    }

    /**
     * Extract a configuration from the first record of a collection of records
     */
    private static <R extends Record> DSLContext extractDSLContext(Collection<? extends R> records)
        throws DetachedException {
        R first = first(records);

        if (first instanceof AttachableInternal) {
            return DSL.using(((AttachableInternal) first).configuration());
        }
        else {
            throw new DetachedException("Supply at least one attachable record");
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FOREIGN KEY (");

        String s1 = "";
        for (Field<?> field : getFields()) {
            sb.append(s1);
            sb.append(DSL.name(field.getName()));

            s1 = ", ";
        }

        sb.append(") REFERENCES ");
        sb.append(key.getTable());
        sb.append("(");

        String s2 = "";
        for (Field<?> field : key.getFields()) {
            sb.append(s2);
            sb.append(DSL.name(field.getName()));

            s2 = ", ";
        }

        sb.append(")");
        return sb.toString();
    }
}
