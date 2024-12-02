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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.conf.WriteIfReadonly.IGNORE;
import static org.jooq.conf.WriteIfReadonly.THROW;
import static org.jooq.conf.WriteIfReadonly.WRITE;
import static org.jooq.impl.RecordDelegate.delegate;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.INSERT;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.indexOrFail;
import static org.jooq.impl.Tools.settings;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Insert;
import org.jooq.InsertQuery;
// ...
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.Update;
import org.jooq.conf.Settings;
import org.jooq.conf.WriteIfReadonly;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.BatchCRUD.QueryCollectorSignal;
import org.jooq.tools.JooqLogger;

/**
 * A record implementation for a record originating from a single table
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class TableRecordImpl<R extends TableRecord<R>>
extends
    AbstractQualifiedRecord<R>
implements
    TableRecord<R>
{

    private static final JooqLogger      log                              = JooqLogger.getLogger(TableRecordImpl.class);
    private static final Set<SQLDialect> REFRESH_GENERATED_KEYS           = SQLDialect.supportedBy(DERBY, H2, MARIADB, MYSQL);
    private static final Set<SQLDialect> REFRESH_GENERATED_KEYS_ON_UPDATE = SQLDialect.supportedBy(HSQLDB);

    public TableRecordImpl(Table<R> table) {
        super(table);
    }

    @Override
    public final Table<R> getTable() {
        return (Table<R>) getQualifier();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final R original() {
        return (R) super.original();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <O extends UpdatableRecord<O>> O fetchParent(ForeignKey<R, O> key) {
        return key.fetchParent((R) this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <O extends UpdatableRecord<O>> Table<O> parent(ForeignKey<R, O> key) {
        return key.parent((R) this);
    }

    @Override
    public final int insert() {
        return insert(fields.fields.fields);
    }

    @Override
    public final int insert(Field<?>... storeFields) {
        return storeInsert(storeFields);
    }

    @Override
    public final int insert(Collection<? extends Field<?>> storeFields) {
        return insert(storeFields.toArray(EMPTY_FIELD));
    }

    final int storeInsert(final Field<?>[] storeFields) {
        final int[] result = new int[1];

        delegate(configuration(), (Record) this, INSERT)
        .operate(record -> {
            result[0] = storeInsert0(storeFields);
            return record;
        });

        return result[0];
    }

    final int storeInsert0(Field<?>[] storeFields) {
        DSLContext create = create();
        InsertQuery<R> insert = create.insertQuery(getTable());
        List<Field<?>> changedFields = addChangedValues(storeFields, insert, false);

        // [#1596] Set timestamp and/or version columns to appropriate values
        BigInteger version = addRecordVersion(insert, false);
        Timestamp timestamp = addRecordTimestamp(insert, false);

        // [#17708] DEFAULT VALUES applies only if we're not generating timestamp and/or version values!
        if (changedFields.isEmpty() && version == null && timestamp == null) {

            // Don't store records if no value was set by client code
            if (FALSE.equals(create.settings().isInsertUnchangedRecords())) {
                if (log.isDebugEnabled())
                    log.debug("Query is not executable", insert);

                return 0;
            }

            else
                insert.setDefaultValues();
        }

        // [#814] Refresh identity and/or main unique key values
        // [#1002] Consider also identity columns of non-updatable records
        // [#1537] Avoid refreshing identity columns on batch inserts
        Collection<Field<?>> key = setReturningIfNeeded(insert);
        try {
            int result = insert.execute();

            if (result > 0) {
                for (Field<?> changedField : changedFields)
                    changed(changedField, false);

                // [#1596] If insert was successful, update timestamp and/or version columns
                setRecordVersionAndTimestamp(version, timestamp);

                // [#1859] If an insert was successful try fetching the generated values.
                getReturningIfNeeded(insert, key);

                fetched = true;
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

    final void getReturningIfNeeded(StoreQuery<R> query, Collection<Field<?>> key) {
        if (key != null && !key.isEmpty()) {
            R record = query.getReturnedRecord();

            if (record != null) {
                for (Field<?> field : key) {
                    int index = indexOrFail(fieldsRow(), field);
                    Object value = record.get(field);

                    values[index] = value;
                    originals[index] = value;
                }
            }

            // [#1859] In some databases, not all fields can be fetched via getGeneratedKeys()
            Configuration c = configuration();
            if (TRUE.equals(c.settings().isReturnAllOnUpdatableRecord())

                    // [#11620] Refresh only if the RETURNING clause didn't run
                    //          E.g. in MySQL when there was no identity column
                    && (REFRESH_GENERATED_KEYS.contains(c.dialect()) && record == null
                    || REFRESH_GENERATED_KEYS_ON_UPDATE.contains(c.dialect()) && query instanceof Update





                    )
                    && this instanceof UpdatableRecord)
                ((UpdatableRecord<?>) this).refresh(key.toArray(EMPTY_FIELD));
        }
    }

    final Collection<Field<?>> setReturningIfNeeded(StoreQuery<R> query) {
        Collection<Field<?>> key = null;

        if (configuration() != null)

            // [#7966] Allow users to turning off the returning clause entirely
            if (!FALSE.equals(configuration().settings().isReturnIdentityOnUpdatableRecord()))

                // [#1859] Return also non-key columns
                if (TRUE.equals(configuration().settings().isReturnAllOnUpdatableRecord()))
                    key = Arrays.asList(fields());

                // [#5940] Getting the primary key mostly doesn't make sense on UPDATE statements
                else if (query instanceof InsertQuery || updatablePrimaryKeys(settings(this)))
                    key = getReturning();

        if (key != null)
            query.setReturning(key);

        return key;
    }

    /**
     * Set a generated version and timestamp value onto this record after
     * successfully storing the record.
     */
    final void setRecordVersionAndTimestamp(BigInteger version, Timestamp timestamp) {
        if (version != null) {
            TableField<R, ?> field = getTable().getRecordVersion();
            int fieldIndex = indexOrFail(fields, field);
            Object value = field.getDataType().convert(version);

            values[fieldIndex] = value;
            originals[fieldIndex] = value;
            changed.clear(fieldIndex);
        }
        if (timestamp != null) {
            TableField<R, ?> field = getTable().getRecordTimestamp();
            int fieldIndex = indexOrFail(fields, field);
            Object value = field.getDataType().convert(timestamp);

            values[fieldIndex] = value;
            originals[fieldIndex] = value;
            changed.clear(fieldIndex);
        }
    }

    /**
     * Set all changed values of this record to a store query.
     */
    final List<Field<?>> addChangedValues(Field<?>[] storeFields, StoreQuery<R> query, boolean forUpdate) {
        FieldsImpl<Record> f = new FieldsImpl<>(storeFields);
        List<Field<?>> result = new ArrayList<>();

        for (Field<?> field : fields.fields.fields) {
            if (changed(field) && f.field(field) != null && writable(field, forUpdate)) {
                addValue(query, field, forUpdate);
                result.add(field);
            }
        }

        return result;
    }

    final boolean writable(Field<?> field, boolean forUpdate) {





















        return true;
    }


    /**
     * Extracted method to ensure generic type safety.
     */
    final <T> void addValue(StoreQuery<?> store, Field<T> field, Object value, boolean forUpdate) {
        store.addValue(field, Tools.field(value, field));

        if (forUpdate)
            ((InsertQuery<?>) store).addValueForUpdate(field, DSL.excluded(field));
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    final <T> void addValue(StoreQuery<?> store, Field<T> field, boolean forUpdate) {
        addValue(store, field, get(field), forUpdate);
    }

    /**
     * Set an updated timestamp value to a store query
     */
    final Timestamp addRecordTimestamp(StoreQuery<?> store, boolean forUpdate) {
        Timestamp result = null;
        TableField<R, ?> timestamp = getTable().getRecordTimestamp();

        if (timestamp != null && isUpdateRecordTimestamp()) {

            // Use Timestamp locally, to provide maximum precision
            result = new Timestamp(configuration().clock().millis());

            // [#9933] Truncate timestamp to column precision, if needed
            addValue(store, timestamp, result = truncate(result, timestamp.getDataType()), forUpdate);
        }

        return result;
    }

    private static final long[] TRUNCATE = { 1000L, 100L, 10L, 1L };

    private static final Timestamp truncate(Timestamp ts, DataType<?> type) {
        if (type.isDate())
            return new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), 0, 0, 0, 0);
        else if (!type.precisionDefined() || type.precision() >= 3)
            return ts;
        else
            return new Timestamp((ts.getTime() / TRUNCATE[type.precision()]) * TRUNCATE[type.precision()]);
    }

    /**
     * Set an updated version value to a store query
     */
    final BigInteger addRecordVersion(StoreQuery<?> store, boolean forUpdate) {
        BigInteger result = null;
        TableField<R, ?> version = getTable().getRecordVersion();

        if (version != null && isUpdateRecordVersion()) {
            Object value = get(version);

            // Use BigInteger locally to avoid arithmetic overflows
            if (value == null)
                result = BigInteger.ONE;
            else
                result = new BigInteger(value.toString()).add(BigInteger.ONE);

            addValue(store, version, result, forUpdate);
        }

        return result;
    }

    final Object getRecordVersion() {
        TableField<R, ?> field = getTable().getRecordVersion();
        return field != null ? get(field) : null;
    }

    final Object getRecordTimestamp() {
        TableField<R, ?> field = getTable().getRecordTimestamp();
        return field != null ? get(field) : null;
    }

    final boolean isUpdateRecordVersion() {
        Configuration configuration = configuration();

        return configuration != null
            ? !FALSE.equals(configuration.settings().isUpdateRecordVersion())
            : true;
    }

    final boolean isUpdateRecordTimestamp() {
        Configuration configuration = configuration();

        return configuration != null
            ? !FALSE.equals(configuration.settings().isUpdateRecordTimestamp())
            : true;
    }

    final boolean isTimestampOrVersionAvailable() {
        return getTable().getRecordTimestamp() != null && isUpdateRecordTimestamp()
            || getTable().getRecordVersion() != null && isUpdateRecordVersion();
    }

    final Collection<Field<?>> getReturning() {
        Collection<Field<?>> result = new LinkedHashSet<>();

        Identity<R, ?> identity = getTable().getIdentity();
        if (identity != null)
            result.add(identity.getField());

        UniqueKey<?> key = getPrimaryKey();
        if (key != null)
            result.addAll(key.getFields());

        return result;
    }
}
