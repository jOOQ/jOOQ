/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import java.sql.SQLException;
import java.util.List;

import org.jooq.ConditionProvider;
import org.jooq.Configuration;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SimpleSelectQuery;
import org.jooq.StoreQuery;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.UpdatableTable;
import org.jooq.UpdateQuery;

/**
 * A record implementation for a record holding a primary key
 *
 * @author Lukas Eder
 */
public class UpdatableRecordImpl<R extends TableRecord<R>> extends TableRecordImpl<R> implements UpdatableRecord<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -1012420583600561579L;

    public UpdatableRecordImpl(UpdatableTable<R> table) {
        this(table, null);
    }

    public UpdatableRecordImpl(UpdatableTable<R> table, Configuration configuration) {
        super(table, configuration);
    }

    /**
     * @deprecated - Use {@link #UpdatableRecordImpl(UpdatableTable, Configuration)} instead
     */
    @Deprecated
    public UpdatableRecordImpl(Configuration configuration, UpdatableTable<R> table) {
        super(table, configuration);
    }

    @Override
    public final UpdatableTable<R> getTable() {
        return (UpdatableTable<R>) super.getTable();
    }

    @Override
    @Deprecated
    public final List<TableField<R, ?>> getPrimaryKey() {
        return getTable().getPrimaryKey();
    }

    @Override
    @Deprecated
    public final List<TableField<R, ?>> getMainUniqueKey() {
        return getTable().getMainUniqueKey();
    }

    private final UniqueKey<R> getMainKey() {
        return getTable().getMainKey();
    }

    @Override
    public final int store() throws SQLException {
        boolean executeUpdate = false;

        for (TableField<R, ?> field : getMainKey().getFields()) {

            // If any primary key value is null or changed, execute an insert
            if (getValue(field) == null || getValue0(field).isChanged()) {
                executeUpdate = false;
                break;
            }

            // If primary key values are unchanged, updates are possible
            else {
                executeUpdate = true;
            }
        }

        if (executeUpdate) {
            return storeUpdate();
        }
        else {
            return storeInsert();
        }
    }

    @SuppressWarnings("unchecked")
    private final int storeInsert() throws SQLException {
        InsertQuery<R> insert = create().insertQuery(getTable());

        for (Field<?> field : getFields()) {
            if (getValue0(field).isChanged()) {
                addValue(insert, (TableField<R, ?>)field);
            }
        }

        int result = insert.execute();

        // If an insert was executed successfully try fetching the generated
        // IDENTITY value
        if (result > 0) {
            Identity<R, ? extends Number> identity = getTable().getIdentity();

            if (identity != null) {
                setValue0(identity.getField(), new Value<Number>(create().lastID(identity)));
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private final int storeUpdate() throws SQLException {
        UpdateQuery<R> update = create().updateQuery(getTable());

        for (Field<?> field : getFields()) {
            if (getValue0(field).isChanged()) {
                addValue(update, (TableField<R, ?>)field);
            }
        }

        for (Field<?> field : getMainKey().getFields()) {
            addCondition(update, field);
        }

        return update.execute();
    }

    @Override
    public final int delete() throws SQLException {
        try {
            DeleteQuery<R> delete = create().deleteQuery(getTable());

            for (Field<?> field : getMainKey().getFields()) {
                addCondition(delete, field);
            }

            return delete.execute();
        }

        // [#673] If store() is called after delete(), a new INSERT should
        // be executed and the record should be recreated
        finally {
            for (Field<?> field : getFields()) {
                getValue0(field).setChanged(true);
            }
        }
    }

    @Override
    public final void refresh() throws SQLException {
        SimpleSelectQuery<R> select = create().selectQuery(getTable());

        for (Field<?> field : getMainKey().getFields()) {
            addCondition(select, field);
        }

        if (select.execute() == 1) {
            AbstractRecord record = (AbstractRecord) select.getResult().get(0);

            for (Field<?> field : getFields()) {
                setValue0(field, record.getValue0(field));
            }
        } else {
            throw new SQLException("Exactly one row expected for refresh. Record does not exist in database.");
        }
    }

    @Override
    public final R copy() {
        R copy = create().newRecord(getTable());

        // Copy all fields. This marks them all as isChanged, which is important
        for (Field<?> field : getFields()) {
            setValue(copy, field);
        }

        // Remove key values again
        for (Field<?> field : getMainKey().getFields()) {
            copy.setValue(field, null);
        }

        return copy;
    }

    // Those generics... go figure...
    @SuppressWarnings("unchecked")
    private <T> void setValue0(Field<T> field, Value<?> value) {
        setValue(field, (Value<T>) value);
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    private final <T> void addCondition(ConditionProvider provider, Field<T> field) {
        provider.addConditions(field.equal(getValue(field)));
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    private final <T> void addValue(StoreQuery<?> store, Field<T> field) {
        store.addValue(field, getValue(field));
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    private final <T> void setValue(Record record, Field<T> field) {
        record.setValue(field, getValue(field));
    }
}
