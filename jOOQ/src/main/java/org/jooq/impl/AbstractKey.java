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

import java.util.Arrays;
import java.util.List;

import org.jooq.Key;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * Common base class for <code>Key</code>'s
 *
 * @author Lukas Eder
 */
abstract class AbstractKey<R extends Record> implements Key<R> {

    /**
     * Generated UID
     */
    private static final long        serialVersionUID = 8176874459141379340L;

    private final Table<R>           table;
    private final TableField<R, ?>[] fields;


    @SafeVarargs

    AbstractKey(Table<R> table, TableField<R, ?>... fields) {
        this.table = table;
        this.fields = fields;
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }

    @Override
    public final List<TableField<R, ?>> getFields() {
        return Arrays.asList(fields);
    }

    @Override
    public final TableField<R, ?>[] getFieldsArray() {
        return fields;
    }
}
