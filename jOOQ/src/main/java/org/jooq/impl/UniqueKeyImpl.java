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
 */
package org.jooq.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jooq.Constraint;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class UniqueKeyImpl<R extends Record> extends AbstractKey<R> implements UniqueKey<R> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 162853300137140844L;

    final List<ForeignKey<?, R>> references;


    @SafeVarargs

    UniqueKeyImpl(Table<R> table, TableField<R, ?>... fields) {
        this(table, null, fields);
    }


    @SafeVarargs

    UniqueKeyImpl(Table<R> table, String name, TableField<R, ?>... fields) {
        super(table, name, fields);

        this.references = new ArrayList<ForeignKey<?, R>>();
    }

    @Override
    public final boolean isPrimary() {
        return equals(getTable().getPrimaryKey());
    }

    @Override
    public final List<ForeignKey<?, R>> getReferences() {
        return Collections.unmodifiableList(references);
    }

    @Override
    public Constraint constraint() {
        if (isPrimary())
            return DSL.constraint(getName()).primaryKey(getFieldsArray());
        else
            return DSL.constraint(getName()).unique(getFieldsArray());
    }
}
