/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
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
 * and Maintenance Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.using;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.PivotForStep;
import org.jooq.PivotInStep;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.conf.ParamType;
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
    private static final long     serialVersionUID = -7918219502110473521L;

    private final Table<?>        table;
    private final SelectFieldList aggregateFunctions;
    private Field<T>              on;
    private SelectFieldList       in;

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
        context.visit(pivot(context.configuration()));
    }

    private Table<?> pivot(Configuration configuration) {
        switch (configuration.dialect()) {

            /* [pro] */
            // Oracle has native support for the PIVOT clause
            case ORACLE:
            case ORACLE11G:
            case ORACLE12C: {
                return new OraclePivotTable();
            }

            /* [/pro] */
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
        public void toSQL(RenderContext ctx) {
            ctx.declareTables(true)
               .visit(select(ctx.configuration()))
               .declareTables(false);
        }

        @Override
        public void bind(BindContext ctx) throws DataAccessException {
            ctx.declareTables(true)
               .visit(select(ctx.configuration()))
               .declareTables(false);
        }

        private Table<Record> select(Configuration configuration) {
            List<Field<?>> groupingFields = new ArrayList<Field<?>>();
            List<Field<?>> aliasedGroupingFields = new ArrayList<Field<?>>();
            List<Field<?>> aggregatedFields = new ArrayList<Field<?>>();

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
            for (Field<?> field : table.fields()) {
                if (!aggregatedFields.contains(field)) {
                    if (!on.equals(field)) {
                        aliasedGroupingFields.add(pivot.field(field));
                        groupingFields.add(field);
                    }
                }
            }

            // The product {aggregateFunctions} x {in}
            List<Field<?>> aggregationSelects = new ArrayList<Field<?>>();
            for (Field<?> inField : in) {
                for (Field<?> aggregateFunction : aggregateFunctions) {
                    Condition join = trueCondition();

                    for (Field<?> field : groupingFields) {
                        join = join.and(condition(pivot, field));
                    }

                    @SuppressWarnings("unchecked")
                    Select<?> aggregateSelect = using(configuration)
                            .select(aggregateFunction)
                            .from(table)
                            .where(on.equal((Field<T>) inField))
                            .and(join);

                    aggregationSelects.add(aggregateSelect.asField(inField.getName() + "_" + aggregateFunction.getName()));
                }
            }

            // This is the complete select
            Table<Record> select =
            using(configuration)
                    .select(aliasedGroupingFields)
                    .select(aggregationSelects)
                    .from(pivot)
                    .where(pivot.field(on).in(in.toArray(new Field[0])))
                    .groupBy(aliasedGroupingFields)
                    .asTable();

            return select;
        }
    }

    /* [pro] */
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
            ParamType paramType = context.paramType();
            boolean declareFields = context.declareFields();
            boolean declareTables = context.declareTables();

            context.declareTables(true)
                   .visit(table)
                   .declareTables(declareTables)
                   .formatSeparator()
                   .keyword("pivot").sql(" (")
                   .paramType(INLINED)
                   .declareFields(true)
                   .formatIndentStart()
                   .visit(aggregateFunctions)
                   .formatSeparator()
                   .keyword("for").sql(" ")
                   .literal(on.getName())
                   .formatSeparator()
                   .keyword("in").sql(" (")
                   .visit(in)
                   .declareFields(declareFields)
                   .paramType(paramType)
                   .sql(")")
                   .formatIndentEnd()
                   .formatNewLine()
                   .sql(")");
        }

        @Override
        public void bind(BindContext context) throws DataAccessException {
            boolean declareTables = context.declareFields();

            context.declareTables(true)
                   .visit(table)
                   .declareTables(declareTables);
        }
    }
    /* [/pro] */

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
        public final Class<? extends Record> getRecordType() {
            return RecordImpl.class;
        }

        @Override
        public final Table<Record> as(String as) {
            return new TableAlias<Record>(this, as);
        }

        @Override
        @Support
        public final Table<Record> as(String as, String... fieldAliases) {
            return new TableAlias<Record>(this, as, fieldAliases);
        }

        @Override
        final Fields<Record> fields0() {
            return Pivot.this.fields0();
        }
    }


    /**
     * Extracted method for type-safety
     */
    private <Z> Condition condition(Table<?> pivot, Field<Z> field) {
        return field.equal(pivot.field(field));
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @Override
    public final void bind(BindContext context) throws DataAccessException {
        context.visit(pivot(context.configuration()));
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias, true);
    }

    @Override
    public final Table<Record> as(String alias, String... fieldAliases) {
        return new TableAlias<Record>(this, alias, fieldAliases, true);
    }

    @Override
    final Fields<Record> fields0() {
        return new Fields<Record>();
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
        return in(Utils.fields(values, on).toArray(new Field<?>[0]));
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
