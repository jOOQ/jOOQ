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

import static org.jooq.SQLDialect.H2;
import static org.jooq.impl.Factory.condition;
import static org.jooq.impl.Factory.exists;
import static org.jooq.impl.Factory.notExists;
import static org.jooq.impl.Factory.nullSafe;
import static org.jooq.impl.Factory.val;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.MergeMatchedDeleteStep;
import org.jooq.MergeMatchedSetMoreStep;
import org.jooq.MergeNotMatchedSetMoreStep;
import org.jooq.MergeNotMatchedValuesStep;
import org.jooq.MergeOnConditionStep;
import org.jooq.MergeOnStep;
import org.jooq.MergeUsingStep;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.UniqueKey;
import org.jooq.UpdatableTable;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.StringUtils;

/**
 * The SQL standard MERGE statement
 *
 * @author Lukas Eder
 */
class MergeImpl<R extends Record> extends AbstractQuery
implements

    // Cascading interface implementations for Merge behaviour
    MergeUsingStep<R>,
    MergeOnStep<R>,
    MergeOnConditionStep<R>,
    MergeMatchedSetMoreStep<R>,
    MergeMatchedDeleteStep<R>,
    MergeNotMatchedSetMoreStep<R>,
    MergeNotMatchedValuesStep<R> {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = -8835479296876774391L;

    private final Table<R>              table;
    private final ConditionProviderImpl on;
    private TableLike<?>                using;

    // [#998] Oracle extensions to the MERGE statement
    private Condition                   matchedWhere;
    private Condition                   matchedDeleteWhere;
    private Condition                   notMatchedWhere;

    // Flags to keep track of DSL object creation state
    private boolean                     matchedClause;
    private FieldMapForUpdate           matchedUpdate;
    private boolean                     notMatchedClause;
    private FieldMapForInsert           notMatchedInsert;

    // Objects for the H2-specific syntax
    private boolean                     h2Style;
    private FieldList                   h2Fields;
    private FieldList                   h2Keys;
    private FieldList                   h2Values;
    private Select<?>                   h2Select;

    MergeImpl(Configuration configuration, Table<R> table) {
        this(configuration, table, null);
    }

    MergeImpl(Configuration configuration, Table<R> table, Collection<? extends Field<?>> fields) {
        super(configuration);

        this.table = table;
        this.on = new ConditionProviderImpl();

        if (fields != null) {
            h2Style = true;
            h2Fields = new FieldList(fields);
        }
    }

    // -------------------------------------------------------------------------
    // H2-specific MERGE API
    // -------------------------------------------------------------------------

    FieldList getH2Fields() {
        if (h2Fields == null) {
            h2Fields = new FieldList();
            h2Fields.addAll(table.getFields());
        }

        return h2Fields;
    }

    FieldList getH2Keys() {
        if (h2Keys == null) {
            h2Keys = new FieldList();
        }

        return h2Keys;
    }

    FieldList getH2Values() {
        if (h2Values == null) {
            h2Values = new FieldList();
        }

        return h2Values;
    }

    @Override
    public final MergeImpl<R> select(Select<?> select) {
        h2Style = true;
        h2Select = select;
        return this;
    }

    @Override
    public final MergeImpl<R> key(Field<?>... k) {
        return key(Arrays.asList(k));
    }

    @Override
    public final MergeImpl<R> key(Collection<? extends Field<?>> keys) {
        h2Style = true;
        getH2Keys().addAll(keys);
        return this;
    }

    // -------------------------------------------------------------------------
    // Shared MERGE API
    // -------------------------------------------------------------------------

    @Override
    public final MergeImpl<R> values(Object... values) {

        // [#1541] The VALUES() clause is also supported in the H2-specific
        // syntax, in case of which, the USING() was not added
        if (using == null) {
            h2Style = true;
            getH2Values().addAll(Utils.fields(values, getH2Fields().toArray(new Field[0])));
        }
        else {
            Field<?>[] fields = notMatchedInsert.keySet().toArray(new Field[0]);
            notMatchedInsert.putValues(Utils.fields(values, fields));
        }

        return this;
    }

    @Override
    public final MergeImpl<R> values(Field<?>... values) {
        return values((Object[]) values);
    }

    @Override
    public final MergeImpl<R> values(Collection<?> values) {
        return values(values.toArray());
    }

    // -------------------------------------------------------------------------
    // Merge API
    // -------------------------------------------------------------------------

    @Override
    public final MergeImpl<R> using(TableLike<?> u) {
        this.using = u;
        return this;
    }

    @Override
    public final MergeImpl<R> usingDual() {
        this.using = create().selectOne();
        return this;
    }

    @Override
    public final MergeImpl<R> on(Condition... conditions) {
        on.addConditions(conditions);
        return this;
    }

    @Override
    public final MergeImpl<R> on(String sql) {
        return on(condition(sql));
    }

    @Override
    public final MergeImpl<R> on(String sql, Object... bindings) {
        return on(condition(sql, bindings));
    }

    @Override
    public final MergeImpl<R> on(String sql, QueryPart... parts) {
        return on(condition(sql, parts));
    }

    @Override
    public final MergeImpl<R> and(Condition condition) {
        on.addConditions(condition);
        return this;
    }

    @Override
    public final MergeImpl<R> and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final MergeImpl<R> and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final MergeImpl<R> and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final MergeImpl<R> andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final MergeImpl<R> andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final MergeImpl<R> andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final MergeImpl<R> or(Condition condition) {
        on.addConditions(Operator.OR, condition);
        return this;
    }

    @Override
    public final MergeImpl<R> or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final MergeImpl<R> or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final MergeImpl<R> or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final MergeImpl<R> orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final MergeImpl<R> orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final MergeImpl<R> orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final MergeImpl<R> whenMatchedThenUpdate() {
        matchedClause = true;
        matchedUpdate = new FieldMapForUpdate();

        notMatchedClause = false;
        return this;
    }

    @Override
    public final <T> MergeImpl<R> set(Field<T> field, T value) {
        return set(field, val(value, field));
    }

    @Override
    public final <T> MergeImpl<R> set(Field<T> field, Field<T> value) {
        if (matchedClause) {
            matchedUpdate.put(field, nullSafe(value));
        }
        else if (notMatchedClause) {
            notMatchedInsert.put(field, nullSafe(value));
        }
        else {
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");
        }

        return this;
    }

    @Override
    public final MergeImpl<R> set(Map<? extends Field<?>, ?> map) {
        if (matchedClause) {
            matchedUpdate.set(map);
        }
        else if (notMatchedClause) {
            notMatchedInsert.set(map);
        }
        else {
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");
        }

        return this;
    }

    @Override
    public final MergeImpl<R> whenNotMatchedThenInsert() {
        return whenNotMatchedThenInsert(Collections.<Field<?>>emptyList());
    }

    @Override
    public final MergeImpl<R> whenNotMatchedThenInsert(Field<?>... fields) {
        return whenNotMatchedThenInsert(Arrays.asList(fields));
    }

    @Override
    public final MergeImpl<R> whenNotMatchedThenInsert(Collection<? extends Field<?>> fields) {
        notMatchedClause = true;
        notMatchedInsert = new FieldMapForInsert();
        notMatchedInsert.putFields(fields);

        matchedClause = false;
        return this;
    }

    @Override
    public final MergeImpl<R> where(Condition condition) {
        if (matchedClause) {
            matchedWhere = condition;
        }
        else if (notMatchedClause) {
            notMatchedWhere = condition;
        }
        else {
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");
        }

        return this;
    }

    @Override
    public final MergeImpl<R> deleteWhere(Condition condition) {
        matchedDeleteWhere = condition;
        return this;
    }

    // -------------------------------------------------------------------------
    // QueryPart API
    // -------------------------------------------------------------------------

    /**
     * Return a standard MERGE statement simulating the H2-specific syntax
     */
    private final QueryPart getStandardMerge(Configuration config) {
        switch (config.getDialect()) {
            case DB2:
            case HSQLDB:
            case ORACLE:
            case SQLSERVER:
            case SYBASE: {

                // The SRC for the USING() clause:
                // ------------------------------
                Table<?> src;
                if (h2Select != null) {
                    FieldList v = new FieldList();

                    for (int i = 0; i < h2Select.getFields().size(); i++) {
                        v.add(h2Select.getField(i).as("s" + (i + 1)));
                    }

                    // [#579] TODO: Currently, this syntax may require aliasing
                    // on the call-site
                    src = create().select(v).from(h2Select).asTable("src");
                }
                else {
                    FieldList v = new FieldList();

                    for (int i = 0; i < getH2Values().size(); i++) {
                        v.add(getH2Values().get(i).as("s" + (i + 1)));
                    }

                    src = create().select(v).asTable("src");
                }

                // The condition for the ON clause:
                // --------------------------------
                Set<Field<?>> onFields = new HashSet<Field<?>>();
                Condition condition = null;
                if (getH2Keys().isEmpty()) {
                    if (table instanceof UpdatableTable) {
                        UniqueKey<?> key = ((UpdatableTable<?>) table).getMainKey();
                        onFields.addAll(key.getFields());

                        for (int i = 0; i < key.getFields().size(); i++) {

                            @SuppressWarnings({ "unchecked", "rawtypes" })
                            Condition rhs = key.getFields().get(i).equal((Field) src.getField(i));

                            if (condition == null) {
                                condition = rhs;
                            }
                            else {
                                condition = condition.and(rhs);
                            }
                        }
                    }

                    // This should probably execute an INSERT statement
                    else {
                        throw new IllegalStateException("Cannot omit KEY() clause on a non-Updatable Table");
                    }
                }
                else {
                    for (int i = 0; i < getH2Keys().size(); i++) {
                        int matchIndex = getH2Fields().indexOf(getH2Keys().get(i));
                        if (matchIndex == -1) {
                            throw new IllegalStateException("Fields in KEY() clause must be part of the fields specified in MERGE INTO table (...)");
                        }

                        onFields.addAll(getH2Keys());

                        @SuppressWarnings({ "unchecked", "rawtypes" })
                        Condition rhs = getH2Keys().get(i).equal((Field) src.getField(matchIndex));

                        if (condition == null) {
                            condition = rhs;
                        }
                        else {
                            condition = condition.and(rhs);
                        }
                    }
                }

                // INSERT and UPDATE clauses
                // -------------------------
                Map<Field<?>, Field<?>> update = new LinkedHashMap<Field<?>, Field<?>>();
                Map<Field<?>, Field<?>> insert = new LinkedHashMap<Field<?>, Field<?>>();

                for (int i = 0; i < src.getFields().size(); i++) {

                    // Oracle does not allow to update fields from the ON clause
                    if (!onFields.contains(getH2Fields().get(i))) {
                        update.put(getH2Fields().get(i), src.getField(i));
                    }

                    insert.put(getH2Fields().get(i), src.getField(i));
                }

                return create().mergeInto(table)
                               .using(src)
                               .on(condition)
                               .whenMatchedThenUpdate()
                               .set(update)
                               .whenNotMatchedThenInsert()
                               .set(insert);
            }
            default:
                throw new SQLDialectNotSupportedException("The H2-specific MERGE syntax is not supported in dialect : " + config.getDialect());
        }
    }

    @Override
    public final void toSQL(RenderContext context) {
        if (h2Style) {
            if (context.getDialect() == H2) {
                toSQLH2(context);
            }
            else {
                context.sql(getStandardMerge(context));
            }
        }
        else {
            toSQLStandard(context);
        }
    }

    private final void toSQLH2(RenderContext context) {
        context.keyword("merge into ")
               .declareTables(true)
               .sql(table)
               .formatSeparator();

        context.sql("(");
        Utils.fieldNames(context, getH2Fields());
        context.sql(")");

        if (!getH2Keys().isEmpty()) {
            context.keyword(" key (");
            Utils.fieldNames(context, getH2Keys());
            context.sql(")");
        }

        if (h2Select != null) {
            context.sql(" ")
                   .sql(h2Select);
        }
        else {
            context.keyword(" values (")
                   .sql(getH2Values())
                   .sql(")");
        }
    }

    private final void toSQLStandard(RenderContext context) {
        context.keyword("merge into ")
               .declareTables(true)
               .sql(table)
               .formatSeparator()
               .keyword("using ")
               .formatIndentStart()
               .formatNewLine()
               .sql(Utils.wrapInParentheses(context.render(using)))
               .formatIndentEnd()
               .declareTables(false);

        switch (context.getDialect()) {
            case SQLSERVER:
            case SYBASE: {
                if (using instanceof Select) {
                    int hash = Utils.hash(using);

                    context.keyword(" as ")
                           .sql("dummy_")
                           .sql(hash)
                           .sql("(");

                    String separator = "";
                    for (Field<?> field : ((Select<?>) using).getFields()) {

                        // Some fields are unnamed
                        // [#579] Correct this
                        String name = StringUtils.isBlank(field.getName())
                            ? "dummy_" + hash + "_" + Utils.hash(field)
                            : field.getName();

                        context.sql(separator).literal(name);
                        separator = ", ";
                    }

                    context.sql(")");
                }

                break;
            }
        }

        context.formatSeparator()
               .keyword("on ")
               .sql(Utils.wrapInParentheses(context.render(on)));

        // [#999] WHEN MATCHED clause is optional
        if (matchedUpdate != null) {
            context.formatSeparator()
                   .keyword("when matched then update set ")
                   .sql(matchedUpdate);
        }

        // [#998] Oracle MERGE extension: WHEN MATCHED THEN UPDATE .. WHERE
        if (matchedWhere != null) {
            context.formatSeparator()
                   .keyword("where ")
                   .sql(matchedWhere);
        }

        // [#998] Oracle MERGE extension: WHEN MATCHED THEN UPDATE .. DELETE WHERE
        if (matchedDeleteWhere != null) {
            context.formatSeparator()
                   .keyword("delete where ")
                   .sql(matchedDeleteWhere);
        }

        // [#999] WHEN NOT MATCHED clause is optional
        if (notMatchedInsert != null) {
            context.formatSeparator()
                   .sql("when not matched then insert ")
                   .sql(notMatchedInsert);
        }

        // [#998] Oracle MERGE extension: WHEN NOT MATCHED THEN INSERT .. WHERE
        if (notMatchedWhere != null) {
            context.formatSeparator()
                   .keyword("where ")
                   .sql(notMatchedWhere);
        }

        switch (context.getDialect()) {
            case SQLSERVER:
                context.sql(";");
                break;
        }
    }

    @Override
    public final void bind(BindContext context) {
        if (h2Style) {
            if (context.getDialect() == H2) {
                bindH2(context);
            }
            else {
                context.bind(getStandardMerge(context));
            }
        }
        else {
            bindStandard(context);
        }
    }

    private final void bindH2(BindContext context) {
        context.declareTables(true)
               .bind(table)
               .declareTables(false)
               .bind((QueryPart) getH2Fields())
               .bind((QueryPart) getH2Keys())
               .bind(h2Select)
               .bind((QueryPart) getH2Values());
    }

    private final void bindStandard(BindContext context) {
        context.declareTables(true)
               .bind(table)
               .bind(using)
               .declareTables(false)
               .bind(on)
               .bind(matchedUpdate)
               .bind(matchedWhere)
               .bind(matchedDeleteWhere)
               .bind(notMatchedInsert)
               .bind(notMatchedWhere);
    }
}
