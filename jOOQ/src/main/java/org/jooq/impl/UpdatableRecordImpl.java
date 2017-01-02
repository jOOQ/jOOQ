/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
// ...
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.impl.RecordDelegate.delegate;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.DELETE;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.REFRESH;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.STORE;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.UPDATE;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.settings;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.exception.DataChangedException;
import org.jooq.exception.NoDataFoundException;
import org.jooq.tools.JooqLogger;
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
    private static final long       serialVersionUID = -1012420583600561579L;
    private static final JooqLogger log              = JooqLogger.getLogger(UpdatableRecordImpl.class);

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
        return Tools.filterOne(fetchChildren(key));
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
        return store(fields.fields.fields);
    }

    @Override
    public final int store(final Field<?>... storeFields) {
        final int[] result = new int[1];

        delegate(configuration(), (Record) this, STORE)
        .operate(new RecordOperation<Record, RuntimeException>() {

            @Override
            public Record operate(Record record) throws RuntimeException {
                result[0] = store0(storeFields);
                return record;
            }
        });

        return result[0];
    }

    @Override
    public final int store(Collection<? extends Field<?>> storeFields) {
        return store(storeFields.toArray(EMPTY_FIELD));
    }

    @Override
    public final int update() {
        return update(fields.fields.fields);
    }

    @Override
    public int update(Field<?>... storeFields) {
        return storeUpdate(storeFields, getPrimaryKey().getFieldsArray());
    }

    @Override
    public final int update(Collection<? extends Field<?>> storeFields) {
        return update(storeFields.toArray(EMPTY_FIELD));
    }

    private final int store0(Field<?>[] storeFields) {
        TableField<R, ?>[] keys = getPrimaryKey().getFieldsArray();
        boolean executeUpdate = false;

        // [#2764] If primary key values are allowed to be changed,
        // inserting is only possible without prior loading of pk values
        if (updatablePrimaryKeys(settings(this))) {
            executeUpdate = fetched;
        }
        else {
            for (TableField<R, ?> field : keys) {

                // If any primary key value is null or changed
                if (changed(field) ||

                // [#3237] or if a NOT NULL primary key value is null, then execute an INSERT
                   (field.getDataType().nullable() == false && get(field) == null)) {
                    executeUpdate = false;
                    break;
                }

                // Otherwise, updates are possible
                executeUpdate = true;
            }
        }

        int result = 0;

        if (executeUpdate) {
            result = storeUpdate(storeFields, keys);
        }
        else {
            result = storeInsert(storeFields);
        }

        return result;
    }

    private final int storeUpdate(final Field<?>[] storeFields, final TableField<R, ?>[] keys) {
        final int[] result = new int[1];

        delegate(configuration(), (Record) this, UPDATE)
        .operate(new RecordOperation<Record, RuntimeException>() {

            @Override
            public Record operate(Record record) throws RuntimeException {
                result[0] = storeUpdate0(storeFields, keys);
                return record;
            }
        });

        return result[0];

    }

    private final int storeUpdate0(Field<?>[] storeFields, TableField<R, ?>[] keys) {
        UpdateQuery<R> update = create().updateQuery(getTable());
        addChangedValues(storeFields, update);
        Tools.addConditions(update, this, keys);

        // Don't store records if no value was set by client code
        if (!update.isExecutable()) {
            if (log.isDebugEnabled())
                log.debug("Query is not executable", update);

            return 0;
        }

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
            // [#5384] Do this only if the exclusion flag for unversioned records is off
            else if (isExecuteWithOptimisticLockingIncludeUnversioned()) {
                checkIfChanged(keys);
            }
        }

        // [#1596] Check if the record was really changed in the database
        // [#1859] Specify the returning clause if needed
        Collection<Field<?>> key = setReturningIfNeeded(update);
        int result = update.execute();
        checkIfChanged(result, version, timestamp);

        if (result > 0) {
            for (Field<?> storeField : storeFields)
                changed(storeField, false);

            // [#1859] If an update was successful try fetching the generated
            getReturningIfNeeded(update, key);
        }

        return result;
    }

    @Override
    public final int delete() {
        final int[] result = new int[1];

        delegate(configuration(), (Record) this, DELETE)
        .operate(new RecordOperation<Record, RuntimeException>() {

            @Override
            public Record operate(Record record) throws RuntimeException {
                result[0] = delete0();
                return record;
            }
        });

        return result[0];
    }

    private final int delete0() {
        TableField<R, ?>[] keys = getPrimaryKey().getFieldsArray();

        try {
            DeleteQuery<R> delete1 = create().deleteQuery(getTable());
            Tools.addConditions(delete1, this, keys);

            if (isExecuteWithOptimisticLocking()) {

                // [#1596] Add additional conditions for version and/or timestamp columns
                if (isTimestampOrVersionAvailable()) {
                    addConditionForVersionAndTimestamp(delete1);
                }

                // [#1547] Try fetching the Record again first, and compare this
                // Record's original values with the ones in the database
                // [#5384] Do this only if the exclusion flag for unversioned records is off
                else if (isExecuteWithOptimisticLockingIncludeUnversioned()) {
                    checkIfChanged(keys);
                }
            }

            int result = delete1.execute();
            checkIfChanged(result, null, null);
            return result;
        }

        // [#673] [#3363] If store() is called after delete(), a new INSERT should
        // be executed and the record should be recreated
        finally {
            changed(true);
            fetched = false;
        }
    }

    @Override
    public final void refresh() {
        refresh(fields.fields.fields);
    }

    @Override
    public final void refresh(final Field<?>... refreshFields) {
        SelectQuery<Record> select = create().selectQuery();
        select.addSelect(refreshFields);
        select.addFrom(getTable());
        Tools.addConditions(select, this, getPrimaryKey().getFieldsArray());

        if (select.execute() == 1) {
            final AbstractRecord source = (AbstractRecord) select.getResult().get(0);

            delegate(configuration(), (Record) this, REFRESH)
                .operate(new RecordOperation<Record, RuntimeException>() {
                    @Override
                    public Record operate(Record record) throws RuntimeException {
                        setValues(refreshFields, source);
                        return record;
                    }
                });
        }
        else {
            throw new NoDataFoundException("Exactly one row expected for refresh. Record does not exist in database.");
        }
    }

    @Override
    public final void refresh(Collection<? extends Field<?>> refreshFields) {
        refresh(refreshFields.toArray(EMPTY_FIELD));
    }

    @Override
    public final R copy() {

        // [#3359] The "fetched" flag must be set to false to enforce INSERT statements on
        // subsequent store() calls - when Settings.updatablePrimaryKeys is set.
        return Tools.newRecord(false, getTable(), configuration())
                    .operate(new RecordOperation<R, RuntimeException>() {

        	@Override
            public R operate(R copy) throws RuntimeException {
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
                record.set(field, get(field));
            }
        });
    }

    private final boolean isExecuteWithOptimisticLocking() {
        Configuration configuration = configuration();

        // This can be null when the current record is detached
        return configuration != null
            ? TRUE.equals(configuration.settings().isExecuteWithOptimisticLocking())
            : false;
    }

    private final boolean isExecuteWithOptimisticLockingIncludeUnversioned() {
        Configuration configuration = configuration();

        // This can be null when the current record is detached
        return configuration != null
            ? !TRUE.equals(configuration.settings().isExecuteWithOptimisticLockingExcludeUnversioned())
            : true;
    }

    @SuppressWarnings("deprecation")
    private final void addConditionForVersionAndTimestamp(org.jooq.ConditionProvider query) {
        TableField<R, ?> v = getTable().getRecordVersion();
        TableField<R, ?> t = getTable().getRecordTimestamp();

        if (v != null) Tools.addCondition(query, this, v);
        if (t != null) Tools.addCondition(query, this, t);
    }

    /**
     * Perform an additional SELECT .. FOR UPDATE to check if the underlying
     * database record has been changed compared to this record.
     */
    private final void checkIfChanged(TableField<R, ?>[] keys) {
        SelectQuery<R> select = create().selectQuery(getTable());
        Tools.addConditions(select, this, keys);

        // [#1547] MS Access and SQLite doesn't support FOR UPDATE. CUBRID and SQL Server
        // can emulate it, though!
        if (!asList(SQLITE).contains(create().configuration().dialect().family())) {
            select.setForUpdate(true);
        }

        R record = select.fetchOne();

        if (record == null) {
            throw new DataChangedException("Database record no longer exists");
        }

        for (Field<?> field : fields.fields.fields) {
            Object thisObject = original(field);
            Object thatObject = record.original(field);

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
}
