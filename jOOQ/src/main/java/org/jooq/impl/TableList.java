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

import java.util.List;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class TableList extends QueryPartList<Table<?>> {

    private static final long serialVersionUID = -8545559185481762229L;

    TableList() {
        super();
    }

    TableList(List<? extends Table<?>> wrappedList) {
        super(wrappedList);
    }

    @Override
    protected void toSQLEmptyList(Context<?> ctx) {
        ctx.visit(new Dual());
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    /**
     * Get a list of names of the <code>NamedQueryParts</code> contained in this
     * list.
     */
    final void toSQLFieldNames(Context<?> ctx) {
        String separator = "";

        for (Table<?> table : this) {
            for (Field<?> field : table.fieldsRow().fields()) {
                ctx.sql(separator);
                ctx.literal(field.getName());

                separator = ", ";
            }
        }
    }
}
