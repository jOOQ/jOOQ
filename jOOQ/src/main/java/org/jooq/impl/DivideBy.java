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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.selectOne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DivideByOnConditionStep;
import org.jooq.DivideByOnStep;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class DivideBy
implements
    DivideByOnStep,
    DivideByOnConditionStep {

    private final Table<?>                dividend;
    private final Table<?>                divisor;
    private final ConditionProviderImpl   condition;
    private final QueryPartList<Field<?>> returning;

    DivideBy(Table<?> dividend, Table<?> divisor) {
        this.dividend = dividend;
        this.divisor = divisor;

        this.condition = new ConditionProviderImpl();
        this.returning = new QueryPartList<Field<?>>();
    }

    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    /**
     * Transform the relational division operation into SQL.
     * <p>
     * Various nice examples of how this can be achieved are found here: <a
     * href=
     * "http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-sql-of-relational-division/"
     * >http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-
     * sql-of-relational-division/</a>
     */
    private final Table<Record> table() {
        ConditionProviderImpl selfJoin = new ConditionProviderImpl();
        List<Field<?>> select = new ArrayList<Field<?>>();
        Table<?> outer = dividend.as("dividend");

        for (Field<?> field : returning) {
            Field<?> outerField = outer.field(field);

            // Fields from the RETURNING clause are added AS-IS to the SELECT
            // statement, if they're not contained in the dividend table
            if (outerField == null) {
                select.add(field);
            }

            // Fields from the RETURNING clause need proper aliasing if they're
            // contained in the dividend table
            else {
                select.add(outerField);
                selfJoin(selfJoin, outer, dividend, field);
            }
        }

        // Apply relational division using double-nested NOT EXISTS clauses
        // There are more efficient ways to express division in SQL, but those
        // are hard to simulate with jOOQ
        return selectDistinct(select)
              .from(outer)
              .whereNotExists(
                   selectOne()
                  .from(divisor)
                  .whereNotExists(
                       selectOne()
                      .from(dividend)
                      .where(selfJoin)
                      .and(condition)))
              .asTable();
    }

    /**
     * Extracted method for type-safety
     */
    @SuppressWarnings("deprecation")
    private final <T> void selfJoin(org.jooq.ConditionProvider selfJoin, Table<?> outer, Table<?> inner, Field<T> field) {
        Field<T> outerField = outer.field(field);
        Field<T> innerField = inner.field(field);

        if (outerField == null || innerField == null) {
            return;
        }
        else {
            selfJoin.addConditions(outerField.equal(innerField));
        }
    }

    // ------------------------------------------------------------------------
    // XXX: DivideBy API
    // ------------------------------------------------------------------------

    @Override
    public final DivideByOnConditionStep on(Condition... conditions) {
        condition.addConditions(conditions);
        return this;
    }

    @Override
    public final DivideByOnConditionStep on(Field<Boolean> c) {
        return on(condition(c));
    }

    @Override
    public final DivideByOnConditionStep on(String sql) {
        and(sql);
        return this;
    }

    @Override
    public final DivideByOnConditionStep on(String sql, Object... bindings) {
        and(sql, bindings);
        return this;
    }

    @Override
    public final DivideByOnConditionStep on(String sql, QueryPart... parts) {
        and(sql, parts);
        return this;
    }

    @Override
    public final Table<Record> returning(Field<?>... fields) {
        return returning(Arrays.asList(fields));
    }

    @Override
    public final Table<Record> returning(Collection<? extends Field<?>> fields) {
        returning.addAll(fields);
        return table();
    }

    @Override
    public final DivideByOnConditionStep and(Condition c) {
        condition.addConditions(c);
        return this;
    }

    @Override
    public final DivideByOnConditionStep and(Field<Boolean> c) {
        return and(condition(c));
    }

    @Override
    public final DivideByOnConditionStep and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final DivideByOnConditionStep and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final DivideByOnConditionStep and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final DivideByOnConditionStep andNot(Condition c) {
        return and(c.not());
    }

    @Override
    public final DivideByOnConditionStep andNot(Field<Boolean> c) {
        return andNot(condition(c));
    }

    @Override
    public final DivideByOnConditionStep andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final DivideByOnConditionStep andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final DivideByOnConditionStep or(Condition c) {
        condition.addConditions(Operator.OR, c);
        return this;
    }

    @Override
    public final DivideByOnConditionStep or(Field<Boolean> c) {
        return or(condition(c));
    }

    @Override
    public final DivideByOnConditionStep or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final DivideByOnConditionStep or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final DivideByOnConditionStep or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final DivideByOnConditionStep orNot(Condition c) {
        return or(c.not());
    }

    @Override
    public final DivideByOnConditionStep orNot(Field<Boolean> c) {
        return orNot(condition(c));
    }

    @Override
    public final DivideByOnConditionStep orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final DivideByOnConditionStep orNotExists(Select<?> select) {
        return or(notExists(select));
    }
}
