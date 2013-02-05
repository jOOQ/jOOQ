/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import static org.jooq.impl.Factory.trueCondition;
import static org.jooq.impl.Factory.vals;

import java.util.Collection;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.PivotForStep;
import org.jooq.PivotInStep;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;

/**
 * A pivot table implementation
 * <p>
 * Future versions of jOOQ could simulate Oracle's <code>PIVOT</code> clause by
 * rendering a subquery instead.
 *
 * @author Lukas Eder
 */
class Pivot<T>
extends AbstractTable<Record>
implements
    PivotForStep,
    PivotInStep<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7918219502110473521L;

    private final Table<?>    table;
    private final FieldList   aggregateFunctions;
    private Field<T>          on;
    private FieldList         in;

    Pivot(Table<?> table, Field<?>... aggregateFunctions) {
        super("pivot");

        this.table = table;
        this.aggregateFunctions = new SelectFieldList(aggregateFunctions);
    }

    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(pivot(context));
    }

    private Table<?> pivot(Configuration configuration) {
        switch (configuration.getDialect()) {

            // Oracle has native support for the PIVOT clause
            case ORACLE: {
                return new OraclePivotTable();
            }

            // Some other dialects can simulate it. This implementation is
            // EXPERIMENTAL and not officially supported
            default: {
                return new DefaultPivotTable();
            }
        }
    }

    /**
     * A simulation of Oracle's <code>PIVOT</code> table
     */
    private class DefaultPivotTable extends DialectPivotTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -5930286639571867314L;

        @Override
        public void toSQL(RenderContext context) {
            context.declareTables(true)
                   .sql(select(context))
                   .declareTables(false);
        }

        @Override
        public void bind(BindContext context) throws DataAccessException {
            context.declareTables(true)
                   .bind(select(context))
                   .declareTables(false);
        }

        private Table<Record> select(Context<?> context) {
            FieldList groupingFields = new FieldList();
            FieldList aliasedGroupingFields = new FieldList();
            FieldList aggregatedFields = new FieldList();

            Table<?> pivot = table.as("pivot_outer");

            // Clearly, the API should be improved to make this more object-
            // oriented...

            // This loop finds all fields that are used in aggregate
            // functions. They're excluded from the GROUP BY clause
            for (Field<?> field : aggregateFunctions) {
                if (field instanceof Function) {
                    for (QueryPart argument : ((Function<?>) field).getArguments()) {
                        if (argument instanceof Field) {
                            aggregatedFields.add((Field<?>) argument);
                        }
                    }
                }
            }

            // This loop finds all fields qualify for GROUP BY clauses
            for (Field<?> field : table.getFields()) {
                if (!aggregatedFields.contains(field)) {
                    if (!on.equals(field)) {
                        aliasedGroupingFields.add(pivot.getField(field));
                        groupingFields.add(field);
                    }
                }
            }

            // The product {aggregateFunctions} x {in}
            FieldList aggregationSelects = new FieldList();
            for (Field<?> inField : in) {
                for (Field<?> aggregateFunction : aggregateFunctions) {
                    Condition join = trueCondition();

                    for (Field<?> field : groupingFields) {
                        join = join.and(condition(pivot, field));
                    }

                    @SuppressWarnings("unchecked")
                    Select<?> aggregateSelect = create(context)
                        .select(aggregateFunction)
                        .from(table)
                        .where(on.equal((Field<T>) inField))
                        .and(join);

                    aggregationSelects.add(aggregateSelect.asField(inField.getName() + "_" + aggregateFunction.getName()));
                }
            }

            // This is the complete select
            Table<Record> select =
            create(context).select(aliasedGroupingFields)
                           .select(aggregationSelects)
                           .from(pivot)
                           .where(pivot.getField(on).in(in.toArray(new Field[0])))
                           .groupBy(aliasedGroupingFields)
                           .asTable();
            return select;
        }
    }

    /**
     * The Oracle-specific <code>PIVOT</code> implementation
     */
    private class OraclePivotTable extends DialectPivotTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -3451610306872726958L;

        @Override
        public void toSQL(RenderContext context) {

            // Bind variables are not allowed inside of PIVOT clause
            boolean inline = context.inline();
            boolean declareFields = context.declareFields();
            boolean declareTables = context.declareFields();

            context.declareTables(true)
                   .sql(table)
                   .declareTables(declareTables)
                   .formatSeparator()
                   .keyword("pivot (")
                   .inline(true)
                   .declareFields(true)
                   .formatIndentStart()
                   .sql(aggregateFunctions)
                   .formatSeparator()
                   .keyword("for ")
                   .literal(on.getName())
                   .formatSeparator()
                   .keyword("in (")
                   .sql(in)
                   .declareFields(declareFields)
                   .inline(inline)
                   .sql(")")
                   .formatIndentEnd()
                   .formatNewLine()
                   .sql(")");
        }

        @Override
        public void bind(BindContext context) throws DataAccessException {
            boolean declareTables = context.declareFields();

            context.declareTables(true)
                   .bind(table)
                   .declareTables(declareTables);
        }
    }

    /**
     * A base class for dialect-specific implementations of the pivot table
     */
    private abstract class DialectPivotTable extends AbstractTable<Record> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2662639259338694177L;

        DialectPivotTable() {
            super("pivot");
        }

        @Override
        public Class<? extends Record> getRecordType() {
            return RecordImpl.class;
        }

        @Override
        public Table<Record> as(String as) {
            return new TableAlias<Record>(this, as);
        }

        @Override
        protected FieldList getFieldList() {
            return Pivot.this.getFieldList();
        }
    }


    /**
     * Extracted method for type-safety
     */
    private <Z> Condition condition(Table<?> pivot, Field<Z> field) {
        return field.equal(pivot.getField(field));
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @Override
    public final void bind(BindContext context) throws DataAccessException {
        context.bind(pivot(context));
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias, true);
    }

    @Override
    protected final FieldList getFieldList() {
        return new FieldList();
    }

    // ------------------------------------------------------------------------
    // XXX: Pivot API
    // ------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final <Z> Pivot<Z> on(Field<Z> field) {
        // The previous bound of <T> is Object, but that's irrelevant
        this.on = (Field<T>) field;
        return (Pivot<Z>) this;
    }

    @Override
    public final Table<Record> in(Object... values) {
        return in(vals(values).toArray(new Field<?>[0]));
    }

    @Override
    public final Table<Record> in(Field<?>... f) {
        this.in = new SelectFieldList(f);
        return this;
    }

    @Override
    public final Table<Record> in(Collection<? extends Field<T>> f) {
        return in(f.toArray(new Field[0]));
    }
}
