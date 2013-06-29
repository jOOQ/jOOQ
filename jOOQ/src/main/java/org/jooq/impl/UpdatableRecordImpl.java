/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import static java.lang.Boolean.TRUE;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectQuery;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.exception.DataChangedException;
import org.jooq.exception.InvalidResultException;
import org.jooq.tools.StringUtils;

/**
 * A record implementation for a record holding a primary key
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class UpdatableRecordImpl<R extends UpdatableRecord<R>> extends TableRecordImpl<R> implements UpdatableRecord<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID      = -1012420583600561579L;

    public UpdatableRecordImpl(Table<R> table) {
        super(table);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Record key() {
        RecordImpl result = new RecordImpl(getPrimaryKey().getFields());
        result.setValues(result.fields.fields.fields, this);
        return result;
    }

    @Override
    public final <O extends TableRecord<O>> O fetchChild(ForeignKey<O, R> key) {
        return Utils.filterOne(fetchChildren(key));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <O extends TableRecord<O>> Result<O> fetchChildren(ForeignKey<O, R> key) {
        return key.fetchChildren((R) this);
    }

    @Override
    final UniqueKey<R> getPrimaryKey() {
        return getTable().getPrimaryKey();
    }

    @Override
    public final int store() {
        TableField<R, ?>[] keys = getPrimaryKey().getFieldsArray();
        boolean executeUpdate = false;

        for (TableField<R, ?> field : keys) {

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

        int result = 0;

        if (executeUpdate) {
            result = storeUpdate(keys);
        }
        else {
            result = storeInsert();
        }

        return result;
    }

    @Override
    public final int insert() {
        return storeInsert();
    }

    @Override
    public final int update() {
        return storeUpdate(getPrimaryKey().getFieldsArray());
    }

    private final int storeInsert() {
        DSLContext create = create();
        InsertQuery<R> insert = create.insertQuery(getTable());
        addChangedValues(insert);

        // Don't store records if no value was set by client code
        if (!insert.isExecutable()) return 0;

        // [#1596] Set timestamp and/or version columns to appropriate values
        BigInteger version = addRecordVersion(insert);
        Timestamp timestamp = addRecordTimestamp(insert);

        // [#814] Refresh identity and/or main unique key values
        // [#1002] Consider also identity columns of non-updatable records
        // [#1537] Avoid refreshing identity columns on batch inserts
        Collection<Field<?>> key = null;
        if (!TRUE.equals(create.configuration().data(Utils.DATA_OMIT_RETURNING_CLAUSE))) {
            key = getReturning();
            insert.setReturning(key);
        }

        int result = insert.execute();

        if (result > 0) {

            // [#1596] If insert was successful, update timestamp and/or version columns
            setRecordVersionAndTimestamp(version, timestamp);

            // If an insert was successful try fetching the generated IDENTITY value
            if (key != null && !key.isEmpty()) {
                if (insert.getReturnedRecord() != null) {
                    for (Field<?> field : key) {
                        setValue(field, new Value<Object>(insert.getReturnedRecord().getValue(field)));
                    }
                }
            }

            changed(false);
        }

        return result;
    }

    private final int storeUpdate(TableField<R, ?>[] keys) {
        UpdateQuery<R> update = create().updateQuery(getTable());
        addChangedValues(update);
        Utils.addConditions(update, this, keys);

        // Don't store records if no value was set by client code
        if (!update.isExecutable()) return 0;

        // [#1596] Set timestamp and/or version columns to appropriate values
        BigInteger version = addRecordVersion(update);
        Timestamp timestamp = addRecordTimestamp(update);

        if (isExecuteWithOptimisticLocking()) {

            // [#1596] Add additional conditions for version and/or timestamp columns
            if (isTimestampOrVersionAvailable()) {
                addConditionForVersionAndTimestamp(update);
            }

            // [#1547] Try fetching the Record again first, and compare this
            // Record's original values with the ones in the database
            else {
                checkIfChanged(keys);
            }
        }

        // [#1596] Check if the record was really changed in the database
        int result = update.execute();
        checkIfChanged(result, version, timestamp);

        if (result > 0) {
            changed(false);
        }

        return result;
    }

    /**
     * Set all changed values of this record to a store query
     */
    private final void addChangedValues(StoreQuery<R> query) {
        for (Field<?> field : fields.fields.fields) {
            if (getValue0(field).isChanged()) {
                addValue(query, field);
            }
        }
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    private final <T> void addValue(StoreQuery<?> store, Field<T> field, Object value) {
        store.addValue(field, Utils.field(value, field));
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    private final <T> void addValue(StoreQuery<?> store, Field<T> field) {
        addValue(store, field, getValue(field));
    }

    /**
     * Set an updated timestamp value to a store query
     */
    private final Timestamp addRecordTimestamp(StoreQuery<?> store) {
        Timestamp result = null;

        if (isTimestampOrVersionAvailable()) {
            TableField<R, ? extends java.util.Date> timestamp = getTable().getRecordTimestamp();

            if (timestamp != null) {

                // Use Timestamp locally, to provide maximum precision
                result = new Timestamp(System.currentTimeMillis());
                addValue(store, timestamp, result);
            }
        }

        return result;
    }

    /**
     * Set an updated version value to a store query
     */
    private final BigInteger addRecordVersion(StoreQuery<?> store) {
        BigInteger result = null;

        if (isTimestampOrVersionAvailable()) {
            TableField<R, ? extends Number> version = getTable().getRecordVersion();

            if (version != null) {
                Number value = getValue(version);

                // Use BigInteger locally to avoid arithmetic overflows
                if (value == null) {
                    result = BigInteger.ONE;
                }
                else {
                    result = new BigInteger(value.toString()).add(BigInteger.ONE);
                }

                addValue(store, version, result);
            }
        }

        return result;
    }

    @Override
    public final int delete() {
        TableField<R, ?>[] keys = getPrimaryKey().getFieldsArray();

        try {
            DeleteQuery<R> delete1 = create().deleteQuery(getTable());
            Utils.addConditions(delete1, this, keys);

            if (isExecuteWithOptimisticLocking()) {

                // [#1596] Add additional conditions for version and/or timestamp columns
                if (isTimestampOrVersionAvailable()) {
                    addConditionForVersionAndTimestamp(delete1);
                }

                // [#1547] Try fetching the Record again first, and compare this
                // Record's original values with the ones in the database
                else {
                    checkIfChanged(keys);
                }
            }

            int result = delete1.execute();
            checkIfChanged(result, null, null);
            return result;
        }

        // [#673] If store() is called after delete(), a new INSERT should
        // be executed and the record should be recreated
        finally {
            changed(true);
        }
    }

    @Override
    public final void refresh() {
        refresh(fields.fields.fields);
    }

    @Override
    public final void refresh(Field<?>... f) {
        SelectQuery<?> select = create().selectQuery();
        select.addSelect(f);
        select.addFrom(getTable());
        Utils.addConditions(select, this, getPrimaryKey().getFieldsArray());

        if (select.execute() == 1) {
            AbstractRecord record = (AbstractRecord) select.getResult().get(0);
            setValues(f, record);
        }
        else {
            throw new InvalidResultException("Exactly one row expected for refresh. Record does not exist in database.");
        }
    }

    private final Collection<Field<?>> getReturning() {
        Collection<Field<?>> result = new LinkedHashSet<Field<?>>();

        Identity<R, ?> identity = getTable().getIdentity();
        if (identity != null) {
            result.add(identity.getField());
        }

        result.addAll(getPrimaryKey().getFields());
        return result;
    }

    @Override
    public final R copy() {
        R copy = create().newRecord(getTable());

        // Copy all fields. This marks them all as isChanged, which is important
        List<TableField<R, ?>> key = getPrimaryKey().getFields();
        for (Field<?> field : fields.fields.fields) {

            // Don't copy key values
            if (!key.contains(field)) {
                setValue(copy, field);
            }
        }

        return copy;
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    private final <T> void setValue(Record record, Field<T> field) {
        record.setValue(field, getValue(field));
    }

    private final boolean isExecuteWithOptimisticLocking() {
        Configuration configuration = configuration();

        // This can be null when the current record is detached
        if (configuration != null) {
            return TRUE.equals(configuration.settings().isExecuteWithOptimisticLocking());
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    private final void addConditionForVersionAndTimestamp(org.jooq.ConditionProvider query) {
        TableField<R, ?> v = getTable().getRecordVersion();
        TableField<R, ?> t = getTable().getRecordTimestamp();

        if (v != null) Utils.addCondition(query, this, v);
        if (t != null) Utils.addCondition(query, this, t);
    }

    private final boolean isTimestampOrVersionAvailable() {
        return getTable().getRecordTimestamp() != null || getTable().getRecordVersion() != null;
    }

    /**
     * Perform an additional SELECT .. FOR UPDATE to check if the underlying
     * database record has been changed compared to this record.
     */
    private final void checkIfChanged(TableField<R, ?>[] keys) {
        SelectQuery<R> select = create().selectQuery(getTable());
        Utils.addConditions(select, this, keys);

        // [#1547] SQLite doesn't support FOR UPDATE. CUBRID and SQL Server
        // can simulate it, though!
        if (create().configuration().dialect() != SQLDialect.SQLITE) {
            select.setForUpdate(true);
        }

        R record = select.fetchOne();

        if (record == null) {
            throw new DataChangedException("Database record no longer exists");
        }

        for (Field<?> field : fields.fields.fields) {
            Value<?> thisValue = getValue0(field);
            Value<?> thatValue = ((AbstractRecord) record).getValue0(field);

            Object thisObject = thisValue.getOriginal();
            Object thatObject = thatValue.getOriginal();

            if (!StringUtils.equals(thisObject, thatObject)) {
                throw new DataChangedException("Database record has been changed");
            }
        }
    }

    /**
     * Check if a database record was changed in the database.
     */
    private final void checkIfChanged(int result, BigInteger version, Timestamp timestamp) {

        // [#1596] If update/delete was successful, update version and/or
        // timestamp columns.
        // [#673] Do this also for deletions, in case a deleted record is re-added
        if (result > 0) {
            setRecordVersionAndTimestamp(version, timestamp);
        }

        // [#1596] No records were updated due to version and/or timestamp change
        else if (isExecuteWithOptimisticLocking()) {
            throw new DataChangedException("Database record has been changed or doesn't exist any longer");
        }
    }

    /**
     * Set a generated version and timestamp value onto this record after
     * successfully storing the record.
     */
    private final void setRecordVersionAndTimestamp(BigInteger version, Timestamp timestamp) {
        if (version != null) {
            TableField<R, ?> field = getTable().getRecordVersion();
            setValue(field, new Value<Object>(field.getDataType().convert(version)));
        }
        if (timestamp != null) {
            TableField<R, ?> field = getTable().getRecordTimestamp();
            setValue(field, new Value<Object>(field.getDataType().convert(timestamp)));
        }
    }
}
