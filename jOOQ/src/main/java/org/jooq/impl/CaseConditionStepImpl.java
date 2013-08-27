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
import java.util.List;

import org.jooq.BindContext;
import org.jooq.CaseConditionStep;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.RenderContext;

class CaseConditionStepImpl<T> extends AbstractField<T> implements CaseConditionStep<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1735676153683257465L;

    private final List<Condition> conditions;
    private final List<Field<T>>  results;
    private Field<T>              otherwise;

    CaseConditionStepImpl(Condition condition, Field<T> result) {
        super("case", result.getDataType());

        this.conditions = new ArrayList<Condition>();
        this.results = new ArrayList<Field<T>>();

        when(condition, result);
    }

    @Override
    public final CaseConditionStep<T> when(Condition condition, T result) {
        return when(condition, Utils.field(result));
    }

    @Override
    public final CaseConditionStep<T> when(Condition condition, Field<T> result) {
        conditions.add(condition);
        results.add(result);

        return this;
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
    public final void bind(BindContext context) {
        for (int i = 0; i < conditions.size(); i++) {
            context.visit(conditions.get(i));
            context.visit(results.get(i));
        }

        if (otherwise != null) {
            context.visit(otherwise);
        }
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.formatIndentLockStart()
               .keyword("case")
               .formatIndentLockStart();

        int size = conditions.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                context.formatNewLine();
            }

            context.sql(" ").keyword("when").sql(" ").visit(conditions.get(i))
                   .sql(" ").keyword("then").sql(" ").visit(results.get(i));
        }

        if (otherwise != null) {
            context.formatNewLine()
                   .sql(" ").keyword("else").sql(" ").visit(otherwise);
        }

        context.formatIndentLockEnd();

        if (size > 1 || otherwise != null) {
            context.formatSeparator();
        }
        else {
            context.sql(" ");
        }

        context.keyword("end")
               .formatIndentLockEnd();
    }
}
