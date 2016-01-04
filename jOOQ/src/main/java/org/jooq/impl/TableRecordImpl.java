/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static org.jooq.impl.RecordDelegate.delegate;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.INSERT;
import static org.jooq.impl.Utils.indexOrFail;
import static org.jooq.impl.Utils.DataKey.DATA_OMIT_RETURNING_CLAUSE;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.tools.JooqLogger;

/**
 * A record implementation for a record originating from a single table
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class TableRecordImpl<R extends TableRecord<R>> extends AbstractRecord implements TableRecord<R> {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = 3216746611562261641L;
    private static final JooqLogger log              = JooqLogger.getLogger(TableRecordImpl.class);
    private final Table<R>          table;

    public TableRecordImpl(Table<R> table) {
        super(table.fields());

        this.table = table;
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }

    /*
     * Subclasses may override this method
     */
    @Override
    public Row fieldsRow() {
        return fields;
    }

    /*
     * Subclasses may override this method
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Row valuesRow() {
        return new RowImpl(Utils.fields(intoArray(), fields.fields.fields()));
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

    @Override
    public final int insert() {
        return insert(fields.fields.fields);
    }

    @Override
    public final int insert(Field<?>... storeFields) {
        return storeInsert(storeFields);
    }

    final int storeInsert(final Field<?>[] storeFields) {
        final int[] result = new int[1];

        delegate(configuration(), (Record) this, INSERT)
        .operate(new RecordOperation<Record, RuntimeException>() {

            @Override
            public Record operate(Record record) throws RuntimeException {
                result[0] = storeInsert0(storeFields);
                return record;
            }
        });

        return result[0];
    }

    final int storeInsert0(Field<?>[] storeFields) {
        DSLContext create = create();
        InsertQuery<R> insert = create.insertQuery(getTable());
        addChangedValues(storeFields, insert);

        // Don't store records if no value was set by client code
        if (!insert.isExecutable()) {
            if (log.isDebugEnabled())
                log.debug("Query is not executable", insert);

            return 0;
        }

        // [#1596] Set timestamp and/or version columns to appropriate values
        BigInteger version = addRecordVersion(insert);
        Timestamp timestamp = addRecordTimestamp(insert);

        // [#814] Refresh identity and/or main unique key values
        // [#1002] Consider also identity columns of non-updatable records
        // [#1537] Avoid refreshing identity columns on batch inserts
        Collection<Field<?>> key = null;
        if (!TRUE.equals(create.configuration().data(DATA_OMIT_RETURNING_CLAUSE))) {
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
                        int index = indexOrFail(fieldsRow(), field);
                        Object value = insert.getReturnedRecord().getValue(field);

                        values[index] = value;
                        originals[index] = value;
                    }
                }
            }

            for (Field<?> storeField : storeFields)
                changed(storeField, false);

            fetched = true;
        }

        return result;
    }

    /**
     * Set a generated version and timestamp value onto this record after
     * successfully storing the record.
     */
    final void setRecordVersionAndTimestamp(BigInteger version, Timestamp timestamp) {
        if (version != null) {
            TableField<R, ?> field = getTable().getRecordVersion();
            int fieldIndex = indexOrFail(fieldsRow(), field);
            Object value = field.getDataType().convert(version);

            values[fieldIndex] = value;
            originals[fieldIndex] = value;
            changed.clear(fieldIndex);
        }
        if (timestamp != null) {
            TableField<R, ?> field = getTable().getRecordTimestamp();
            int fieldIndex = indexOrFail(fieldsRow(), field);
            Object value = field.getDataType().convert(timestamp);

            values[fieldIndex] = value;
            originals[fieldIndex] = value;
            changed.clear(fieldIndex);
        }
    }

    /**
     * Set all changed values of this record to a store query
     */
    final void addChangedValues(Field<?>[] storeFields, StoreQuery<R> query) {
        Fields<Record> f = new Fields<Record>(storeFields);

        for (Field<?> field : fields.fields.fields) {
            if (changed(field) && f.field(field) != null) {
                addValue(query, field);
            }
        }
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    final <T> void addValue(StoreQuery<?> store, Field<T> field, Object value) {
        store.addValue(field, Utils.field(value, field));
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    final <T> void addValue(StoreQuery<?> store, Field<T> field) {
        addValue(store, field, getValue(field));
    }

    /**
     * Set an updated timestamp value to a store query
     */
    final Timestamp addRecordTimestamp(StoreQuery<?> store) {
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
    final BigInteger addRecordVersion(StoreQuery<?> store) {
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

    final boolean isTimestampOrVersionAvailable() {
        return getTable().getRecordTimestamp() != null || getTable().getRecordVersion() != null;
    }

    final Collection<Field<?>> getReturning() {
        Collection<Field<?>> result = new LinkedHashSet<Field<?>>();

        Identity<R, ?> identity = getTable().getIdentity();
        if (identity != null)
            result.add(identity.getField());

        UniqueKey<?> key = getPrimaryKey();
        if (key != null)
            result.addAll(key.getFields());

        return result;
    }
}
