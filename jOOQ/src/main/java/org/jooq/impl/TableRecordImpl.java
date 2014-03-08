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

import org.jooq.ForeignKey;
import org.jooq.Row;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;

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
    private static final long serialVersionUID = 3216746611562261641L;
    private final Table<R>    table;

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
}
