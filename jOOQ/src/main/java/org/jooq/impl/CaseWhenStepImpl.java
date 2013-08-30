/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.impl;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.CaseWhenStep;
import org.jooq.Field;
import org.jooq.RenderContext;

class CaseWhenStepImpl<V, T> extends AbstractField<T> implements CaseWhenStep<V, T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -3817194006479624228L;

    private final Field<V>       value;
    private final List<Field<V>> compareValues;
    private final List<Field<T>> results;
    private Field<T>             otherwise;

    CaseWhenStepImpl(Field<V> value, Field<V> compareValue, Field<T> result) {
        super("case", result.getDataType());

        this.value = value;
        this.compareValues = new ArrayList<Field<V>>();
        this.results = new ArrayList<Field<T>>();

        when(compareValue, result);
    }

    @Override
    public final Field<T> otherwise(T result) {
        return otherwise(Utils.field(result));
    }

    @Override
    public final Field<T> otherwise(Field<T> result) {
        this.otherwise = result;

        return this;
    }

    @Override
    public final CaseWhenStep<V, T> when(V compareValue, T result) {
        return when(Utils.field(compareValue), Utils.field(result));
    }

    @Override
    public final CaseWhenStep<V, T> when(V compareValue, Field<T> result) {
        return when(Utils.field(compareValue), result);
    }

    @Override
    public final CaseWhenStep<V, T> when(Field<V> compareValue, T result) {
        return when(compareValue, Utils.field(result));
    }

    @Override
    public final CaseWhenStep<V, T> when(Field<V> compareValue, Field<T> result) {
        compareValues.add(compareValue);
        results.add(result);

        return this;
    }

    @Override
    public final void bind(BindContext ctx) {
        switch (ctx.configuration().dialect()) {

            // The DERBY dialect doesn't support the simple CASE clause
            case DERBY: {
                for (int i = 0; i < compareValues.size(); i++) {
                    ctx.visit(value);
                    ctx.visit(compareValues.get(i));
                    ctx.visit(results.get(i));
                }

                break;
            }

            default: {
                ctx.visit(value);

                for (int i = 0; i < compareValues.size(); i++) {
                    ctx.visit(compareValues.get(i));
                    ctx.visit(results.get(i));
                }

                break;
            }
        }

        if (otherwise != null) {
            ctx.visit(otherwise);
        }
    }

    @Override
    public final void toSQL(RenderContext ctx) {
        ctx.formatIndentLockStart()
           .keyword("case");

        int size = compareValues.size();
        switch (ctx.configuration().dialect()) {

            // The DERBY dialect doesn't support the simple CASE clause
            case DERBY: {
                ctx.formatIndentLockStart();

                for (int i = 0; i < size; i++) {
                    if (i > 0) {
                        ctx.formatNewLine();
                    }

                    ctx.sql(" ").keyword("when").sql(" ");
                    ctx.visit(value.equal(compareValues.get(i)));
                    ctx.sql(" ").keyword("then").sql(" ");
                    ctx.visit(results.get(i));
                }

                break;
            }

            default: {
                ctx.sql(" ")
                   .visit(value)
                   .formatIndentLockStart();

                for (int i = 0; i < size; i++) {
                    if (i > 0) {
                        ctx.formatNewLine();
                    }

                    ctx.sql(" ").keyword("when").sql(" ");
                    ctx.visit(compareValues.get(i));
                    ctx.sql(" ").keyword("then").sql(" ");
                    ctx.visit(results.get(i));
                }

                break;
            }
        }

        if (otherwise != null) {
            ctx.formatNewLine()
               .sql(" ").keyword("else").sql(" ").visit(otherwise);
        }

        ctx.formatIndentLockEnd();

        if (size > 1 || otherwise != null) {
            ctx.formatSeparator();
        }
        else {
            ctx.sql(" ");
        }

        ctx.keyword("end")
           .formatIndentLockEnd();
    }
}
