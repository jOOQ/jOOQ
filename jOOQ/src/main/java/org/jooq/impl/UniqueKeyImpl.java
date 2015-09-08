/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

    /* [java-8] */
    @SafeVarargs
    /* [/java-8] */
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
