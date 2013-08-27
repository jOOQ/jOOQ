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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
class UniqueKeyImpl<R extends Record> extends AbstractKey<R> implements UniqueKey<R> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = 162853300137140844L;

    final List<ForeignKey<?, R>> references;

    UniqueKeyImpl(Table<R> table, TableField<R, ?>... fields) {
        super(table, fields);

        this.references = new ArrayList<ForeignKey<?, R>>();
    }

    @Override
    public final List<ForeignKey<?, R>> getReferences() {
        return Collections.unmodifiableList(references);
    }

    // -------------------------------------------------------------------------
    // XXX: Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UNIQUE KEY (");

        String s1 = "";
        for (Field<?> field : getFields()) {
            sb.append(s1);
            sb.append(field);

            s1 = ", ";
        }

        sb.append(")");
        return sb.toString();
    }
}
