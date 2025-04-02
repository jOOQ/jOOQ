/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.impl.RecordDelegate.delegate;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.DELETE;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.MERGE;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.REFRESH;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.STORE;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.UPDATE;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.recordDirtyTrackingPredicate;
import static org.jooq.impl.Tools.settings;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jooq.ConditionProvider;
import org.jooq.Configuration;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.ForeignKey;
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
import org.jooq.conf.UpdateUnchangedRecords;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.exception.DataChangedException;
import org.jooq.exception.NoDataFoundException;
import org.jooq.impl.BatchCRUD.QueryCollectorSignal;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * A record implementation for a record holding a primary key
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class UpdatableRecordImpl<R extends UpdatableRecord<R>>
extends TableRecordImpl<R>
implements
    UpdatableRecord<R>
{

    private static final JooqLogger      log                        = JooqLogger.getLogger(UpdatableRecordImpl.class);
    private static final Set<SQLDialect> NO_SUPPORT_FOR_UPDATE      = SQLDialect.supportedBy(SQLITE);
    private static final Set<SQLDialect> NO_SUPPORT_MERGE_RETURNING = SQLDialect.supportedBy(DERBY, IGNITE);

    public UpdatableRecordImpl(Table<R> table) {
        super(table);
    }

    @Override
    public Record key() {
        AbstractRecord result = Tools.newRecord(
            fetched,
            configuration(),
            AbstractRecord.class,
            (AbstractRow<AbstractRecord>) Tools.row0(getPrimaryKey().getFieldsArray())
        ).operate(null);

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

    @SuppressWarnings("unchecked")
    @Override
    public final <O extends TableRecord<O>> Table<O> children(ForeignKey<O, R> key) {
        return key.children((R) this);
    }

    @Override
    final UniqueKey<R> getPrimaryKey() {
        if (getTable() instanceof AbstractTable<R> t)
            return t.getPrimaryKeyWithEmbeddables();
        else
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
        .operate(record -> {
            result[0] = store0(storeFields);
            return record;
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

    @Override
    public final int merge() {
        return merge(fields.fields.fields);
    }

    @Override
    public int merge(Field<?>... storeFields) {
        return storeMerge(storeFields, getPrimaryKey().getFieldsArray());
    }

    @Override
    public final int merge(Collection<? extends Field<?>> storeFields) {
        return merge(storeFields.toArray(EMPTY_FIELD));
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
            ObjIntPredicate<Record> dirty = recordDirtyTrackingPredicate(this);
            for (TableField<R, ?> field : keys) {

                // If any primary key value is null or touched
                if (dirty.test(this, indexOf(field)) ||

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

        if (executeUpdate)
            result = storeUpdate(storeFields, keys);
        else
            result = storeInsert(storeFields);

        return result;
    }

    private final int storeUpdate(final Field<?>[] storeFields, final TableField<R, ?>[] keys) {
        final int[] result = new int[1];

        delegate(configuration(), (Record) this, UPDATE)
        .operate(record -> {
            result[0] = storeUpdate0(storeFields, keys);
            return record;
        });

        return result[0];
    }

    private final int storeUpdate0(Field<?>[] storeFields, TableField<R, ?>[] keys) {
        return storeMergeOrUpdate0(storeFields, keys, create().updateQuery(getTable()), false);
    }

    private final int storeMerge(final Field<?>[] storeFields, final TableField<R, ?>[] keys) {
        final int[] result = new int[1];

        delegate(configuration(), (Record) this, MERGE)
        .operate(record -> {
            result[0] = storeMerge0(storeFields, keys);
            return record;
        });

        // MySQL returns 0 when nothing was updated, 1 when something was inserted, and 2 if something was updated
        return Math.min(result[0], 1);
    }

    private final int storeMerge0(Field<?>[] storeFields, TableField<R, ?>[] keys) {

        // [#10050] No need for MERGE with optimistic locking being active.
        if (lockingActive()) {
            if (lockValuePresent())
                return storeUpdate0(storeFields, keys);
            else
                return storeInsert0(storeFields);
        }
        else {
            InsertQuery<R> merge = create().insertQuery(getTable());
            merge.onDuplicateKeyUpdate(true);
            return storeMergeOrUpdate0(storeFields, keys, merge, true);
        }
    }

    private final boolean lockingActive() {
        return isExecuteWithOptimisticLocking() && (isTimestampOrVersionAvailable() || isExecuteWithOptimisticLockingIncludeUnversioned());
    }

    private final boolean lockValuePresent() {

        // [#10050] A lock value is present if we either have locking columns or if the record was fetched from the database
        return getRecordVersion() != null
            || getRecordTimestamp() != null
            || getTable().getRecordVersion() == null && getTable().getRecordTimestamp() == null && fetched;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final <Q extends StoreQuery<R> & ConditionProvider> int storeMergeOrUpdate0(
        Field<?>[] storeFields,
        TableField<R, ?>[] keys,
        Q query,
        boolean merge
    ) {
        List<Field<?>> touchedFields = addTouchedValues(storeFields, query, merge);

        // [#11552] These conditions should be omitted in the MERGE case
        if (!merge)
            Tools.addConditions(query, this, keys);

        if (touchedFields.isEmpty()) {
            switch (StringUtils.defaultIfNull(create().settings().getUpdateUnchangedRecords(), UpdateUnchangedRecords.NEVER)) {

                // Don't store records if no value was set by client code
                case NEVER:
                    if (log.isDebugEnabled())
                        log.debug("Query is not executable", query);

                    return 0;

                case SET_PRIMARY_KEY_TO_ITSELF:
                    for (TableField<R, ?> key : keys)
                        query.addValue(key, (Field) key);

                    break;

                case SET_NON_PRIMARY_KEY_TO_THEMSELVES:
                    for (Field<?> field : storeFields)
                        if (!asList(keys).contains(field))
                            query.addValue(field, (Field) field);

                    break;

                case SET_NON_PRIMARY_KEY_TO_RECORD_VALUES:
                    for (Field<?> field : storeFields)
                        if (!asList(keys).contains(field))
                            touched(field, true);

                    addTouchedValues(storeFields, query, merge);
                    break;
            }
        }

        // [#1596] Set timestamp and/or version columns to appropriate values
        // [#8924] Allow for overriding this using a setting
        BigInteger version = addRecordVersion(query, merge);
        Timestamp timestamp = addRecordTimestamp(query, merge);

        if (isExecuteWithOptimisticLocking())

            // [#1596] Add additional conditions for version and/or timestamp columns
            if (isTimestampOrVersionAvailable())
                addConditionForVersionAndTimestamp(query);

            // [#1547] Try fetching the Record again first, and compare this
            // Record's original values with the ones in the database
            // [#5384] Do this only if the exclusion flag for unversioned records is off
            else if (isExecuteWithOptimisticLockingIncludeUnversioned())
                checkIfChanged(keys);

        // [#1596]  Check if the record was really changed in the database
        // [#1859]  Specify the returning clause if needed
        // [#10051] Not all dialects support RETURNING on MERGE
        Collection<Field<?>> key = merge && NO_SUPPORT_MERGE_RETURNING.contains(create().dialect())
            ? null
            : setReturningIfNeeded(query);

        try {
            int result = query.execute();
            checkIfChanged(result, version, timestamp);

            if (result > 0) {
                for (Field<?> touchedField : touchedFields)
                    touched(touchedField, false);

                // [#1859] If an update was successful try fetching the generated
                getReturningIfNeeded(query, key);
            }

            return result;
        }

        // [#8283] Pass optimistic locking information on to BatchCRUD, if applicable
        catch (QueryCollectorSignal e) {
            e.version = version;
            e.timestamp = timestamp;
            throw e;
        }
    }

    @Override
    public final int delete() {
        final int[] result = new int[1];

        delegate(configuration(), (Record) this, DELETE)
        .operate(record -> {
            result[0] = delete0();
            return record;
        });

        return result[0];
    }

    private final int delete0() {
        TableField<R, ?>[] keys = getPrimaryKey().getFieldsArray();
        Throwable t = null;

        try {
            DeleteQuery<R> delete1 = create().deleteQuery(getTable());
            Tools.addConditions(delete1, this, keys);

            if (isExecuteWithOptimisticLocking())

                // [#1596] Add additional conditions for version and/or timestamp columns
                if (isTimestampOrVersionAvailable())
                    addConditionForVersionAndTimestamp(delete1);

                // [#1547] Try fetching the Record again first, and compare this
                // Record's original values with the ones in the database
                // [#5384] Do this only if the exclusion flag for unversioned records is off
                else if (isExecuteWithOptimisticLockingIncludeUnversioned())
                    checkIfChanged(keys);

            int result = delete1.execute();
            checkIfChanged(result, null, null);
            return result;
        }

        catch (Throwable t0) {
            t = t0;
            throw t0;
        }

        // [#673] [#3363] If store() is called after delete(), a new INSERT should
        // be executed and the record should be recreated
        finally {

            // [#18261] These state changes must happen only on successful deletion
            if (t == null || t instanceof ControlFlowSignal) {
                touched(true);
                asList(originals).replaceAll(e -> null);
                fetched = false;
            }
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
                .operate(record -> {
                    setValues(refreshFields, source);
                    return record;
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final R copy() {

        // [#3359] The "fetched" flag must be set to false to enforce INSERT statements on
        // subsequent store() calls - when Settings.updatablePrimaryKeys is set.
        // R vs Record casting is needed in Java 8 it seems
        return (R) Tools.newRecord(false, configuration(), (Table<Record>) (Table) getTable())
                    .operate((Record copy) -> {

                        // Copy all fields. This marks them all as isChanged, which is important
                        List<TableField<R, ?>> key = getPrimaryKey().getFields();
                        for (Field<?> field : fields.fields.fields)

                            // Don't copy key values
                            if (!key.contains(field))
                                copy.set((Field) field, get(field));

                        return copy;
                    });
    }

    private final boolean isExecuteWithOptimisticLocking() {
        Configuration configuration = configuration();
        return configuration != null && TRUE.equals(configuration.settings().isExecuteWithOptimisticLocking());
    }

    private final boolean isExecuteWithOptimisticLockingIncludeUnversioned() {
        Configuration configuration = configuration();
        return configuration == null || !TRUE.equals(configuration.settings().isExecuteWithOptimisticLockingExcludeUnversioned());
    }

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
        if (!NO_SUPPORT_FOR_UPDATE.contains(create().dialect()))
            select.setForUpdate(true);

        R record = select.fetchOne();

        if (record == null)
            throw new DataChangedException("Database record no longer exists");

        for (Field<?> field : fields.fields.fields) {
            Object thisObject = original(field);
            Object thatObject = record.original(field);

            if (!StringUtils.equals(thisObject, thatObject))
                if (thisObject == null && !fetched)
                    throw new DataChangedException("Cannot detect whether unversioned record has been changed. Either make sure the record is fetched from the database, or use a version or timestamp column to version the record.");
                else
                    throw new DataChangedException("Database record has been changed");
        }
    }

    /**
     * Check if a database record was changed in the database.
     */
    private final void checkIfChanged(int result, BigInteger version, Timestamp timestamp) {

        // [#1596] If update/delete was successful, update version and/or
        // timestamp columns.
        // [#673] Do this also for deletions, in case a deleted record is re-added
        if (result > 0)
            setRecordVersionAndTimestamp(version, timestamp);

        // [#1596] No records were updated due to version and/or timestamp change
        else if (isExecuteWithOptimisticLocking())
            throw new DataChangedException("Database record has been changed or doesn't exist any longer");
    }
}
