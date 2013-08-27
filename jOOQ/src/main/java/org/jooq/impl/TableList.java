/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.impl;

import java.util.List;

import org.jooq.Field;
import org.jooq.RenderContext;
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
    protected void toSQLEmptyList(RenderContext context) {
        context.visit(new Dual());
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    /**
     * Get a list of names of the <code>NamedQueryParts</code> contained in this
     * list.
     */
    final void toSQLFieldNames(RenderContext context) {
        String separator = "";

        for (Table<?> table : this) {
            for (Field<?> field : table.fieldsRow().fields()) {
                context.sql(separator);
                context.literal(field.getName());

                separator = ", ";
            }
        }
    }
}
