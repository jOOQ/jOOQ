/*
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

import org.jooq.Cursor;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Table;
import org.jooq.TableRecord;

/**
 * A base class for custom {@link TableRecord} implementations in client code.
 * <p>
 * Client code may provide proper {@link TableRecord} implementations extending
 * this useful base class. All necessary parts of the {@link TableRecord}
 * interface are already implemented. No methods need further implementation.
 * <p>
 * Use this base class when providing custom tables to any of the following
 * methods:
 * <ul>
 * <li>{@link ResultQuery#fetchInto(Table)}</li>
 * <li>{@link Cursor#fetchInto(Table)}</li>
 * <li>{@link Result#into(Table)}</li>
 * <li>{@link Record#into(Table)}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public abstract class CustomRecord<R extends TableRecord<R>> extends TableRecordImpl<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 5287021930962460241L;

    protected CustomRecord(Table<R> table) {
        super(table);
    }
}
