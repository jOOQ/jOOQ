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

import java.util.Collection;
import java.util.LinkedHashSet;

import org.jooq.ConditionProvider;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.SimpleSelectQuery;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdateQuery;
import org.jooq.exception.InvalidResultException;

/**
 * A record implementation for a record originating from a single table
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class TableRecordImpl<R extends TableRecord<R>> extends TypeRecord<Table<R>> implements TableRecord<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 3216746611562261641L;

    public TableRecordImpl(Table<R> table) {
        super(table);
    }

    /*
     * This method is overridden covariantly by UpdatableRecordImpl
     */
    @Override
    public Table<R> getTable() {
        return getType0();
    }

    @Override
    public final int storeUsing(TableField<R, ?>... keys) {
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

        for (Value<?> value : getValues()) {
            value.setChanged(false);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private final int storeInsert() {
        InsertQuery<R> insert = create().insertQuery(getTable());

        for (Field<?> field : getFields()) {
            if (getValue0(field).isChanged()) {
                addValue(insert, (TableField<R, ?>) field);
            }
        }

        // [#814] Refresh identity and/or main unique key values
        // [#1002] Consider also identity columns of non-updatable records
        Collection<Field<?>> key = getReturning();
        insert.setReturning(key);
        int result = insert.execute();

        // If an insert was successful try fetching the generated IDENTITY value
        if (!key.isEmpty() && result > 0) {
            if (insert.getReturnedRecord() != null) {
                for (Field<?> field : key) {
                    setValue0(field, new Value<Object>(insert.getReturnedRecord().getValue(field)));
                }
            }
        }

        return result;
    }

    /**
     * Subclasses may override this method to provide an identity
     */
    Collection<Field<?>> getReturning() {
        Collection<Field<?>> result = new LinkedHashSet<Field<?>>();

        Identity<R, ?> identity = getTable().getIdentity();
        if (identity != null) {
            result.add(identity.getField());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private final int storeUpdate(TableField<R, ?>... keys) {
        UpdateQuery<R> update = create().updateQuery(getTable());

        for (Field<?> field : getFields()) {
            if (getValue0(field).isChanged()) {
                addValue(update, (TableField<R, ?>) field);
            }
        }

        for (Field<?> field : keys) {
            addCondition(update, field);
        }

        return update.execute();
    }

    @Override
    public final int deleteUsing(TableField<R, ?>... keys) {
        try {
            DeleteQuery<R> delete = create().deleteQuery(getTable());

            for (Field<?> field : keys) {
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
    public final void refreshUsing(TableField<R, ?>... keys) {
        SimpleSelectQuery<R> select = create().selectQuery(getTable());

        for (Field<?> field : keys) {
            addCondition(select, field);
        }

        if (select.execute() == 1) {
            AbstractRecord record = (AbstractRecord) select.getResult().get(0);

            for (Field<?> field : getFields()) {
                setValue0(field, record.getValue0(field));
            }
        }
        else {
            throw new InvalidResultException("Exactly one row expected for refresh. Record does not exist in database.");
        }
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    @SuppressWarnings("unchecked")
    private final <T> void setValue0(Field<T> field, Value<?> value) {
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
}
