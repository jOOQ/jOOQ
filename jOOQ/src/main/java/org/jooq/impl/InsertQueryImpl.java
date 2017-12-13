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
 *
 *
 *
 */

package org.jooq.impl;

import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT;
import static org.jooq.Clause.INSERT_RETURNING;
import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.impl.DSL.dual;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_DEFAULT;
import static org.jooq.impl.Keywords.K_DEFAULT_VALUES;
import static org.jooq.impl.Keywords.K_DO_NOTHING;
import static org.jooq.impl.Keywords.K_DO_UPDATE;
import static org.jooq.impl.Keywords.K_IGNORE;
import static org.jooq.impl.Keywords.K_INSERT;
import static org.jooq.impl.Keywords.K_INTO;
import static org.jooq.impl.Keywords.K_ON_CONFLICT;
import static org.jooq.impl.Keywords.K_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.impl.Keywords.K_OR;
import static org.jooq.impl.Keywords.K_SET;
import static org.jooq.impl.Keywords.K_VALUES;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.aliasedFields;
import static org.jooq.impl.Tools.fieldNameStrings;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.DataKey.DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.Keyword;
import org.jooq.Merge;
import org.jooq.MergeNotMatchedStep;
import org.jooq.MergeOnConditionStep;
import org.jooq.Name;
import org.jooq.OnConflict;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
final class InsertQueryImpl<R extends Record> extends AbstractStoreQuery<R> implements InsertQuery<R> {

    private static final long                serialVersionUID      = 4466005417945353842L;
    private static final Clause[]            CLAUSES               = { INSERT };
    private static final EnumSet<SQLDialect> SUPPORT_INSERT_IGNORE = EnumSet.of(MARIADB, MYSQL);

    private final FieldMapForUpdate          updateMap;
    private final FieldMapsForInsert         insertMaps;
    private Select<?>                        select;
    private boolean                          defaultValues;
    private boolean                          onDuplicateKeyUpdate;
    private boolean                          onDuplicateKeyIgnore;
    private final OnConflictProviderImpl     onConflict;
    private final ConditionProviderImpl      condition;
    private final ConditionProviderImpl      conditionOnTarget;

    InsertQueryImpl(Configuration configuration, WithImpl with, Table<R> into) {
        super(configuration, with, into);

        this.updateMap = new FieldMapForUpdate(into, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT);
        this.insertMaps = new FieldMapsForInsert(into);
        this.condition = new ConditionProviderImpl();
        this.conditionOnTarget = new ConditionProviderImpl();
        this.onConflict = new OnConflictProviderImpl();
    }

    @Override
    public final void newRecord() {
        insertMaps.newRecord();
    }

    @Override
    protected final Map<Field<?>, Field<?>> getValues() {
        return insertMaps.lastMap();
    }

    @Override
    public final void addRecord(R record) {
        newRecord();
        setRecord(record);
    }

    @Override
    public final void onConflict(Field<?>... fields) {
        onConflict(Arrays.asList(fields));
    }

    @Override
    public final void onConflict(Collection<? extends Field<?>> fields) {
        for(Field<?> field: fields)
            onConflict(field, null, null);
    }

    @Override
    public final void onConflict(Field<?> field, Name collation) {
        onConflict(field, collation, null);
    }

    @Override
    public final void onConflict(Field<?> field, Keyword opclass) {
        onConflict(field, null, opclass);
    }

    @Override
    public final void onConflict(Field<?> field, Name collation, Keyword opclass) {
        addOnConflict(field, collation, opclass);
    }

    @Override
    public final void addOnConflict(Field<?> field, Name collation) {
        addOnConflict(field, collation, null);
    }

    @Override
    public final void addOnConflict(Field<?> field, Keyword opclass) {
        addOnConflict(field, null, opclass);
    }

    @Override
    public final void addOnConflict(Field<?> field, Name collation, Keyword opclass) {
        addOnConflict(DSL.onConflict(field, collation, opclass));
    }

    @Override
    public final void addOnConflict(OnConflict onConflict) {
        this.onConflict.addOnConflict(onConflict);
    }

    @Override
    public final void onDuplicateKeyUpdate(boolean flag) {
        this.onDuplicateKeyIgnore = false;
        this.onDuplicateKeyUpdate = flag;
    }

    @Override
    public final void onDuplicateKeyIgnore(boolean flag) {
        this.onDuplicateKeyUpdate = false;
        this.onDuplicateKeyIgnore = flag;
    }

    @Override
    public final <T> void addValueForUpdate(Field<T> field, T value) {
        updateMap.put(field, Tools.field(value, field));
    }

    @Override
    public final <T> void addValueForUpdate(Field<T> field, Field<T> value) {
        updateMap.put(field, Tools.field(value, field));
    }

    @Override
    public final void addValuesForUpdate(Map<?, ?> map) {
        updateMap.set(map);
    }

    @Override
    public final void addConditions(Condition conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Collection<? extends Condition> conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Collection<? extends Condition> conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addIndexConditions(Condition conditions) {
        conditionOnTarget.addConditions(conditions);
    }

    @Override
    public final void addIndexConditions(Condition... conditions) {
        conditionOnTarget.addConditions(conditions);
    }

    @Override
    public final void addIndexConditions(Collection<? extends Condition> conditions) {
        conditionOnTarget.addConditions(conditions);
    }

    @Override
    public final void addIndexConditions(Operator operator, Condition condition) {
        conditionOnTarget.addConditions(operator, condition);
    }

    @Override
    public final void addIndexConditions(Operator operator, Condition... conditions) {
        conditionOnTarget.addConditions(operator, conditions);
    }

    @Override
    public final void addIndexConditions(Operator operator, Collection<? extends Condition> conditions) {
        conditionOnTarget.addConditions(operator, conditions);
    }

    @Override
    public final void setDefaultValues() {
        defaultValues = true;
    }

    @Override
    public final void setSelect(Field<?>[] f, Select<?> s) {
        insertMaps.addFields(Arrays.asList(f));
        select = s;
    }

    @Override
    public final void addValues(Map<?, ?> map) {
        insertMaps.set(map);
    }

    @Override
    final void accept0(Context<?> ctx) {

        // ON DUPLICATE KEY UPDATE clause
        // ------------------------------
        if (onDuplicateKeyUpdate) {
            switch (ctx.family()) {

                // MySQL has a nice syntax for this
                case CUBRID:
                case MARIADB:
                case MYSQL: {
                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_DUPLICATE_KEY_UPDATE)
                       .formatIndentStart()
                       .formatSeparator()
                       .visit(updateMap)
                       .formatIndentEnd()
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);

                    break;
                }

                case POSTGRES: {
                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_CONFLICT)
                       .sql(" (");

                    if (onConflict != null && onConflict.isNotEmpty()) {
                        boolean qualify = ctx.qualify();

                        ctx.qualify(false)
                           .visit(onConflict)
                           .qualify(qualify);
                    }
                    else if (table.getPrimaryKey() == null) {
                        ctx.sql("[unknown primary key]");
                    }
                    else {
                        boolean qualify = ctx.qualify();

                        ctx.qualify(false)
                           .visit(new Fields<Record>(table.getPrimaryKey().getFields()))
                           .qualify(qualify);
                    }

                    ctx.sql(") ");

                    if (conditionOnTarget.hasWhere())
                        ctx.formatSeparator()
                           .visit(K_WHERE)
                           .sql(' ')
                           .visit(conditionOnTarget)
                           .sql(' ');

                    ctx.visit(K_DO_UPDATE)
                       .formatSeparator()
                       .visit(K_SET)
                       .sql(' ')
                       .formatIndentLockStart()
                       .visit(updateMap)
                       .formatIndentLockEnd();

                    if (condition.hasWhere())
                        ctx.formatSeparator()
                           .visit(K_WHERE)
                           .sql(' ')
                           .visit(condition);

                    ctx.end(INSERT_ON_DUPLICATE_KEY_UPDATE);

                    break;
                }

                // Some dialects can't really handle this clause. Emulation should be done in two steps
                case H2: {
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be emulated for " + ctx.dialect());
                }

                // Some databases allow for emulating this clause using a MERGE statement







                case HSQLDB: {
                    ctx.visit(toMerge(ctx.configuration()));
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("The ON DUPLICATE KEY UPDATE clause cannot be emulated for " + ctx.dialect());
            }
        }

        // ON DUPLICATE KEY IGNORE clause
        // ------------------------------
        else if (onDuplicateKeyIgnore) {
            switch (ctx.dialect()) {

                // MySQL has a nice, native syntax for this
                case MARIADB:
                case MYSQL_5_7:
                case MYSQL_8_0:
                case MYSQL:
                case SQLITE: {
                    toSQLInsert(ctx);
                    ctx.start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
                    break;
                }

                case POSTGRES_9_5:
                case POSTGRES_10:
                case POSTGRES: {
                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_CONFLICT)
                       .sql(' ');

                    if (onConflict != null && onConflict.isNotEmpty()) {
                        boolean qualify = ctx.qualify();

                        ctx.sql('(')
                           .qualify(false)
                           .visit(onConflict)
                           .qualify(qualify)
                           .sql(") ");
                    }

                    ctx.visit(K_DO_NOTHING)
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
                    break;
                }

                // CUBRID can emulate this using ON DUPLICATE KEY UPDATE
                case CUBRID: {
                    FieldMapForUpdate update = new FieldMapForUpdate(table, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT);
                    Field<?> field = table.field(0);
                    update.put(field, field);

                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_DUPLICATE_KEY_UPDATE)
                       .sql(' ')
                       .visit(update)
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);

                    break;
                }

                // Some databases allow for emulating this clause using a MERGE statement




















                case HSQLDB: {
                    ctx.visit(toMerge(ctx.configuration()));
                    break;
                }

                default: {
                    ctx.visit(toInsertSelect(ctx.configuration()));
                    break;
                }
            }
        }

        // Default mode
        // ------------
        else {
            toSQLInsert(ctx);
            ctx.start(INSERT_ON_DUPLICATE_KEY_UPDATE)
               .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
        }

        ctx.start(INSERT_RETURNING);
        toSQLReturning(ctx);
        ctx.end(INSERT_RETURNING);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    private final void toSQLInsert(Context<?> ctx) {
        boolean declareTables = ctx.declareTables();

        ctx.start(INSERT_INSERT_INTO)
           .visit(K_INSERT)
           .sql(' ');

        // [#1295] [#4376] MySQL and SQLite have native syntaxes for
        //                 INSERT [ OR ] IGNORE
        if (onDuplicateKeyIgnore)
            if (SUPPORT_INSERT_IGNORE.contains(ctx.family()))
                ctx.visit(K_IGNORE).sql(' ');
            else if (SQLDialect.SQLITE == ctx.family())
                ctx.visit(K_OR).sql(' ').visit(K_IGNORE).sql(' ');

        ctx.visit(K_INTO)
           .sql(' ')
           .declareTables(true)
           .visit(table)
           .declareTables(declareTables);

        insertMaps.toSQLReferenceKeys(ctx);
        ctx.end(INSERT_INSERT_INTO);

        if (select != null) {

            // [#2995] Prevent the generation of wrapping parentheses around the
            //         INSERT .. SELECT statement's SELECT because they would be
            //         interpreted as the (missing) INSERT column list's parens.
            if (insertMaps.fields().size() == 0)
                ctx.data(DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST, true);








            ctx.formatSeparator()
               .start(INSERT_SELECT)
               .visit(select)
               .end(INSERT_SELECT);

            ctx.data().remove(DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST);
        }
        else if (defaultValues) {
            switch (ctx.family()) {






                case DERBY:
                case MARIADB:
                case MYSQL:
                    ctx.formatSeparator()
                       .visit(K_VALUES)
                       .sql('(');

                    int count = table.fields().length;
                    String separator = "";

                    for (int i = 0; i < count; i++) {
                        ctx.sql(separator);
                        ctx.visit(K_DEFAULT);
                        separator = ", ";
                    }

                    ctx.sql(')');
                    break;

                default:
                    ctx.formatSeparator()
                       .visit(K_DEFAULT_VALUES);
                    break;
            }
        }









        else {
            ctx.visit(insertMaps);
        }
    }























    private final QueryPart toInsertSelect(Configuration configuration) {
        if (table.getPrimaryKey() != null) {

            // [#5089] Multi-row inserts need to explicitly generate UNION ALL
            //         here. TODO: Refactor this logic to be more generally
            //         reusable - i.e. ordinary UNION ALL emulation should be
            //         re-used.

            Select<Record> rows = null;
            Name[] aliases = fieldNames(insertMaps.fields().toArray(EMPTY_FIELD));

            for (Map<Field<?>, Field<?>> map : insertMaps.maps()) {
                Select<Record> row =
                    select(aliasedFields(map.values().toArray(EMPTY_FIELD), aliases))
                    .whereNotExists(
                        selectOne()
                        .from(table)
                        .where(matchByPrimaryKey(map))
                    );

                if (rows == null)
                    rows = row;
                else
                    rows = rows.unionAll(row);
            }

            return create(configuration)
                .insertInto(table)
                .columns(insertMaps.fields())
                .select(selectFrom(table(rows).as("t")));
        }
        else {
            throw new IllegalStateException("The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be emulated when inserting into non-updatable tables : " + table);
        }
    }

    private final Merge<R> toMerge(Configuration configuration) {
        if (table.getPrimaryKey() != null) {

            // [#6375] INSERT .. VALUES and INSERT .. SELECT distinction also in MERGE
            Table<?> t = select == null
                ? dual()
                : table(select).as("t", fieldNameStrings(insertMaps.fields().toArray(EMPTY_FIELD)));

            MergeOnConditionStep<R> on = select == null
                ? create(configuration).mergeInto(table)
                                       .usingDual()
                                       .on(matchByPrimaryKey(insertMaps.lastMap()))
                : create(configuration).mergeInto(table)
                                       .using(t)
                                       .on(matchByPrimaryKey(t));

            // [#1295] Use UPDATE clause only when with ON DUPLICATE KEY UPDATE,
            //         not with ON DUPLICATE KEY IGNORE
            MergeNotMatchedStep<R> notMatched = on;
            if (onDuplicateKeyUpdate)
                notMatched = on.whenMatchedThenUpdate()
                               .set(updateMap);

            return select == null
                ? notMatched.whenNotMatchedThenInsert(insertMaps.fields())
                            .values(insertMaps.lastMap().values())
                : notMatched.whenNotMatchedThenInsert(insertMaps.fields())
                            .values(t.fields());
        }
        else {
            throw new IllegalStateException("The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be emulated when inserting into non-updatable tables : " + table);
        }
    }

    /**
     * Produce a {@link Condition} that matches existing rows by the inserted or
     * updated primary key values.
     */
    @SuppressWarnings("unchecked")
    private final Condition matchByPrimaryKey(Map<Field<?>, Field<?>> map) {
        Condition result = null;

        for (Field<?> f : table.getPrimaryKey().getFields()) {
            Field<Object> field = (Field<Object>) f;
            Field<Object> value = (Field<Object>) map.get(field);

            Condition other = field.eq(value);
            result = (result == null) ? other : result.and(other);
        }

        return result;
    }

    /**
     * Produce a {@link Condition} that matches existing rows by the inserted or
     * updated primary key values.
     */
    @SuppressWarnings("unchecked")
    private final Condition matchByPrimaryKey(Table<?> s) {
        Condition result = null;

        for (Field<?> f : table.getPrimaryKey().getFields()) {
            Field<Object> field = (Field<Object>) f;
            Field<Object> value = s.field(field);

            Condition other = field.eq(value);
            result = (result == null) ? other : result.and(other);
        }

        return result;
    }

    @Override
    public final boolean isExecutable() {
        return insertMaps.isExecutable() || defaultValues || select != null;
    }
}
