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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT;
import static org.jooq.Clause.INSERT_RETURNING;
import static org.jooq.Clause.INSERT_SELECT;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.Keywords.K_DEFAULT;
import static org.jooq.impl.Keywords.K_DEFAULT_VALUES;
import static org.jooq.impl.Keywords.K_DO_NOTHING;
import static org.jooq.impl.Keywords.K_DO_UPDATE;
import static org.jooq.impl.Keywords.K_IGNORE;
import static org.jooq.impl.Keywords.K_INSERT;
import static org.jooq.impl.Keywords.K_INTO;
import static org.jooq.impl.Keywords.K_ON_CONFLICT;
import static org.jooq.impl.Keywords.K_ON_CONSTRAINT;
import static org.jooq.impl.Keywords.K_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.impl.Keywords.K_SET;
import static org.jooq.impl.Keywords.K_VALUES;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.aliasedFields;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.flattenCollection;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.unqualified;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_CONSTRAINT_REFERENCE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST;
import static org.jooq.impl.Tools.DataKey.DATA_ON_DUPLICATE_KEY_WHERE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.Merge;
import org.jooq.MergeNotMatchedStep;
import org.jooq.MergeOnConditionStep;
import org.jooq.Name;
import org.jooq.Operator;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.conf.WriteIfReadonly;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.Tools.DataExtendedKey;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
final class InsertQueryImpl<R extends Record> extends AbstractStoreQuery<R> implements InsertQuery<R>, UNotYetImplemented {

    private static final Clause[]        CLAUSES                                       = { INSERT };
    private static final Set<SQLDialect> SUPPORT_INSERT_IGNORE                         = SQLDialect.supportedBy(MARIADB, MYSQL);
    private static final Set<SQLDialect> SUPPORTS_OPTIONAL_DO_UPDATE_CONFLICT_TARGETS  = SQLDialect.supportedBy(SQLITE);
    private static final Set<SQLDialect> NO_SUPPORT_DERIVED_COLUMN_LIST_IN_MERGE_USING = SQLDialect.supportedBy(DERBY, H2);
    private static final Set<SQLDialect> NO_SUPPORT_SUBQUERY_IN_MERGE_USING            = SQLDialect.supportedBy(DERBY);

    final FieldMapForUpdate              updateMap;
    final FieldMapsForInsert             insertMaps;
    Select<?>                            select;
    boolean                              defaultValues;
    boolean                              onDuplicateKeyUpdate;
    boolean                              onDuplicateKeyIgnore;
    Constraint                           onConstraint;
    UniqueKey<R>                         onConstraintUniqueKey;
    QueryPartList<Field<?>>              onConflict;
    final ConditionProviderImpl          onConflictWhere;
    final ConditionProviderImpl          condition;

    InsertQueryImpl(Configuration configuration, WithImpl with, Table<R> into) {
        super(configuration, with, into);

        this.updateMap = new FieldMapForUpdate(into, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT);
        this.insertMaps = new FieldMapsForInsert(into);
        this.onConflictWhere = new ConditionProviderImpl();
        this.condition = new ConditionProviderImpl();
    }

    @Override
    public final void newRecord() {
        insertMaps.newRecord();
    }

    @Override
    protected final Map<Field<?>, Field<?>> getValues() {
        return insertMaps.lastMap();
    }

    final FieldMapsForInsert getInsertMaps() {
        return insertMaps;
    }

    final Select<?> getSelect() {
        return select;
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
        this.onConflict = new QueryPartList<Field<?>>(fields).qualify(false);
    }

    @Override
    public final void onConflictWhere(Condition conditions) {
        onConflictWhere.addConditions(conditions);
    }

    @Override
    public final void onConflictOnConstraint(Constraint constraint) {
        onConflictOnConstraint0(constraint);
    }

    @Override
    public void onConflictOnConstraint(UniqueKey<R> constraint) {
        if (StringUtils.isEmpty(constraint.getName()))
            throw new IllegalArgumentException("UniqueKey's name is not specified");

        this.onConstraintUniqueKey = constraint;
        onConflictOnConstraint0(constraint(name(constraint.getName())));
    }

    @Override
    public final void onConflictOnConstraint(Name constraint) {
        onConflictOnConstraint0(constraint(constraint));
    }

    private void onConflictOnConstraint0(Constraint constraint) {
        this.onConstraint = constraint;

        if (onConstraintUniqueKey == null)
            onConstraintUniqueKey = Tools.findAny(table().getKeys(), key -> constraint.getName().equals(key.getName()));
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
    public final void setDefaultValues() {
        defaultValues = true;
    }

    private final boolean defaultValues(Configuration c) {





        return defaultValues;
    }

    @Override
    public final void setSelect(Field<?>[] f, Select<?> s) {
        setSelect(Arrays.asList(f), s);
    }

    @Override
    public final void setSelect(Collection<? extends Field<?>> f, Select<?> s) {
        insertMaps.addFields(f);
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


                case POSTGRES:
                case SQLITE:
                case YUGABYTEDB: {
                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_CONFLICT)
                       .sql(' ');

                    if (onConstraint != null ) {
                        ctx.data(DATA_CONSTRAINT_REFERENCE, true);
                        ctx.visit(K_ON_CONSTRAINT)
                           .sql(' ')
                           .visit(onConstraint);

                        ctx.data().remove(DATA_CONSTRAINT_REFERENCE);
                    }
                    else {
                        if (onConflict != null && onConflict.size() > 0)
                            ctx.sql('(').visit(onConflict).sql(')');











                        // [#13273] SQLite 3.38 has started supporting optional on conflict targets
                        else if (SUPPORTS_OPTIONAL_DO_UPDATE_CONFLICT_TARGETS.contains(ctx.dialect()) && !onConflictWhere.hasWhere())
                            ;
                        // [#6462] There is no way to emulate MySQL's ON DUPLICATE KEY UPDATE
                        //         where all UNIQUE keys are considered for conflicts. PostgreSQL
                        //         doesn't allow ON CONFLICT DO UPDATE without either a conflict
                        //         column list or a constraint reference.
                        else if (table().getPrimaryKey() == null)
                            ctx.sql("[unknown primary key]");
                        else
                            ctx.sql('(').qualify(false, c -> c.visit(new FieldsImpl<>(table().getPrimaryKey().getFields()))).sql(')');
                    }

                    if (onConflictWhere.hasWhere())
                        ctx.qualify(false, c -> c
                                .formatSeparator()
                                .visit(K_WHERE)
                                .sql(' ')
                                .visit(onConflictWhere.getWhere()));

                    ctx.formatSeparator()
                       .visit(K_DO_UPDATE)
                       .formatSeparator()
                       .visit(K_SET)
                       .formatIndentStart()
                       .formatSeparator()
                       .visit(updateMap)
                       .formatIndentEnd();

                    if (condition.hasWhere())
                        ctx.formatSeparator()
                           .visit(K_WHERE)
                           .sql(' ')
                           .visit(condition);

                    ctx.end(INSERT_ON_DUPLICATE_KEY_UPDATE);

                    break;
                }

                // Some databases allow for emulating this clause using a MERGE statement















                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB: {
                    ctx.visit(toMerge(ctx));
                    break;
                }

                // MySQL has a nice syntax for this
                default: {

                    // [#2508] In H2, this syntax is supported in MySQL MODE (we're assuming users will
                    //         set this mode in order to profit from this functionality). Up until
                    //         H2 1.4.197, qualification of columns in the ON DUPLICATE KEY UPDATE clause
                    //         wasn't supported (see https://github.com/h2database/h2database/issues/1027)
                    boolean oldQualify = ctx.qualify();
                    boolean newQualify = ctx.family() != H2 && oldQualify;

                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_DUPLICATE_KEY_UPDATE)
                       .formatIndentStart()
                       .formatSeparator()
                       .qualify(newQualify);

                    // [#8479] Emulate WHERE clause using CASE
                    if (condition.hasWhere())
                        ctx.data(DATA_ON_DUPLICATE_KEY_WHERE, condition.getWhere());

                    ctx.visit(updateMap);

                    if (condition.hasWhere())
                        ctx.data().remove(DATA_ON_DUPLICATE_KEY_WHERE);

                    ctx.qualify(oldQualify)
                       .formatIndentEnd()
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);

                    break;
                }
            }
        }

        // ON DUPLICATE KEY IGNORE clause
        // ------------------------------
        else if (onDuplicateKeyIgnore) {
            switch (ctx.dialect()) {

                // The default emulation









                case FIREBIRD:




                case IGNITE:





{
                    ctx.visit(toInsertSelect(ctx));
                    break;
                }















                case POSTGRES:
                case SQLITE:
                case YUGABYTEDB: {
                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_CONFLICT);

                    if (onConstraint != null ) {
                        ctx.data(DATA_CONSTRAINT_REFERENCE, true, c -> c
                            .sql(' ')
                            .visit(K_ON_CONSTRAINT)
                            .sql(' ')
                            .visit(onConstraint)
                        );
                    }
                    else {
                        if (onConflict != null && onConflict.size() > 0) {
                            ctx.sql(" (").visit(onConflict).sql(')');

                            if (onConflictWhere.hasWhere())
                                ctx.formatSeparator()
                                   .visit(K_WHERE)
                                   .sql(' ')
                                   .visit(onConflictWhere.getWhere());
                        }











                    }

                    ctx.formatSeparator()
                       .visit(K_DO_NOTHING)
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
                    break;
                }

                // CUBRID can emulate this using ON DUPLICATE KEY UPDATE
                case CUBRID: {
                    FieldMapForUpdate update = new FieldMapForUpdate(table(), INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT);
                    Field<?> field = table().field(0);
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



























                case H2:
                case HSQLDB: {
                    ctx.visit(toMerge(ctx));
                    break;
                }

                case DERBY: {

                    // [#10989] Cannot use MERGE with SELECT: [42XAL]: The source table of a MERGE statement must be a base table or table function.
                    if (select != null)
                        ctx.visit(toInsertSelect(ctx));
                    else
                        ctx.visit(toMerge(ctx));

                    break;
                }

                // MySQL has a nice, native syntax for this
                default: {
                    toSQLInsert(ctx);
                    ctx.start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
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
        ctx.start(INSERT_INSERT_INTO)
           .visit(K_INSERT)
           .sql(' ');

        // [#1295] MySQL dialects have native syntax for INSERT IGNORE
        // [#4376] [#8433] for SQLite render using ON CONFLICT DO NOTHING
        //                 rather than INSERT OR IGNORE
        if (onDuplicateKeyIgnore)
            if (SUPPORT_INSERT_IGNORE.contains(ctx.dialect()))
                ctx.visit(K_IGNORE).sql(' ');

        ctx.visit(K_INTO)
           .sql(' ')
           .declareTables(true, c -> c.visit(table(c)));

        List<Field<?>> fields = insertMaps.toSQLReferenceKeys(ctx);
        ctx.end(INSERT_INSERT_INTO);





        if (select != null) {

            // [#2995] Prevent the generation of wrapping parentheses around the
            //         INSERT .. SELECT statement's SELECT because they would be
            //         interpreted as the (missing) INSERT column list's parens.
            if (insertMaps.keysFlattened(ctx).size() == 0)
                ctx.data(DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST, true);












            ctx.data(DATA_INSERT_SELECT, true);

            Select<?> s = select;







            // [#8353] TODO: Support overlapping embeddables
            ctx.formatSeparator()
               .start(INSERT_SELECT)
               .visit(s)
               .end(INSERT_SELECT);

            ctx.data().remove(DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST);
            ctx.data().remove(DATA_INSERT_SELECT);
        }
        else if (defaultValues(ctx.configuration())) {
            switch (ctx.family()) {



















                case DERBY:
                case MARIADB:
                case MYSQL:
                    acceptDefaultValuesEmulation(ctx, table().fields().length);
                    break;

                default:
                    ctx.formatSeparator()
                       .visit(K_DEFAULT_VALUES);

                    break;
            }
        }




        else
            ctx.visit(insertMaps);
    }

    private final void acceptDefaultValuesEmulation(Context<?> ctx, int length) {
        ctx.formatSeparator()
           .visit(K_VALUES)
           .sql(" (")
           .visit(wrap(nCopies(length, K_DEFAULT)))
           .sql(')');
    }























    private final List<List<? extends Field<?>>> conflictingKeys(Context<?> ctx) {

        // [#7365] PostgreSQL ON CONFLICT (conflict columns) clause
        if (onConflict != null && onConflict.size() > 0)
            return singletonList(onConflict);

        // [#7409] PostgreSQL ON CONFLICT ON CONSTRAINT clause
        else if (onConstraintUniqueKey != null)
            return singletonList(onConstraintUniqueKey.getFields());

        // [#6462] MySQL ON DUPLICATE KEY UPDATE clause
        //         Flag for backwards compatibility considers only PRIMARY KEY
        else if (TRUE.equals(Tools.settings(ctx.configuration()).isEmulateOnDuplicateKeyUpdateOnPrimaryKeyOnly()))
            return singletonList(table().getPrimaryKey().getFields());

        // [#6462] MySQL ON DUPLICATE KEY UPDATE clause
        //         All conflicting keys are considered
        else
            return map(table().getKeys(), k -> k.getFields());
    }

    @SuppressWarnings("unchecked")
    private final QueryPart toInsertSelect(Context<?> ctx) {
        List<List<? extends Field<?>>> keys = conflictingKeys(ctx);

        if (!keys.isEmpty()) {
            Select<Record> rows = null;
            Set<Field<?>> fields = insertMaps.keysFlattened(ctx);

            // [#10989] INSERT .. SELECT .. ON DUPLICATE KEY IGNORE
            if (select != null) {
                Map<Field<?>, Field<?>> map = new HashMap<>();
                Field<?>[] names = Tools.fields(select.fields().length);
                List<Field<?>> f = new ArrayList<>(fields);
                for (int i = 0; i < fields.size() && i < names.length; i++)
                    map.put(f.get(i), names[i]);

                rows = (Select<Record>) selectFrom(select.asTable(DSL.table(name("t")), names))
                    .whereNotExists(
                        selectOne()
                        .from(table())
                        .where(matchByConflictingKeys(ctx, map))
                    );
            }

            // [#5089] Multi-row inserts need to explicitly generate UNION ALL
            //         here. TODO: Refactor this logic to be more generally
            //         reusable - i.e. ordinary UNION ALL emulation should be
            //         re-used.
            else {
                for (Map<Field<?>, Field<?>> map : insertMaps.maps()) {
                    Select<Record> row =
                        select(aliasedFields(map.entrySet().stream().filter(e -> fields.contains(e.getKey())).map(Entry::getValue).collect(toList())))
                        .whereNotExists(
                            selectOne()
                            .from(table())
                            .where(matchByConflictingKeys(ctx, map))
                        );

                    if (rows == null)
                        rows = row;
                    else
                        rows = rows.unionAll(row);
                }
            }

            return ctx.dsl()
                .insertInto(table())
                .columns(fields)
                .select(selectFrom(rows.asTable("t")));
        }
        else
            return DSL.sql("[ The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be emulated when inserting into tables without any known keys : " + table() + " ]");
    }

    private final QueryPart toMerge(Context<?> ctx) {
        if ((onConflict != null && onConflict.size() > 0)
            || onConstraint != null
            || !table().getKeys().isEmpty()) {

            Table<?> t = null;
            Set<Field<?>> k = insertMaps.keysFlattened(ctx);
            Collection<Field<?>> f = null;

            if (!NO_SUPPORT_SUBQUERY_IN_MERGE_USING.contains(ctx.dialect())) {
                f = k.isEmpty() ? asList(table().fields()) : k;

                // [#10461]          Multi row inserts need to be emulated using select
                // [#11770] [#11880] Single row inserts also do, in some dialects
                Select<?> s = select != null
                    ? select
                    : insertMaps.insertSelect(ctx);

                // [#8937] With DEFAULT VALUES, there is no SELECT. Create one from
                //         known DEFAULT expressions, or use NULL.
                if (s == null)
                    s = select(map(f, x -> x.getDataType().defaulted() ? x.getDataType().default_() : DSL.NULL(x)));

                // [#6375]  INSERT .. VALUES and INSERT .. SELECT distinction also in MERGE
                t = s.asTable("t", map(f, Field::getName, String[]::new));

                if (NO_SUPPORT_DERIVED_COLUMN_LIST_IN_MERGE_USING.contains(ctx.dialect()))
                    t = selectFrom(t).asTable("t");
            }

            MergeOnConditionStep<R> on = t != null
                ? ctx.dsl().mergeInto(table())
                           .using(t)
                           .on(matchByConflictingKeys(ctx, t))
                : ctx.dsl().mergeInto(table())
                           .usingDual()
                           .on(matchByConflictingKeys(ctx, insertMaps.lastMap()));

            // [#1295] Use UPDATE clause only when with ON DUPLICATE KEY UPDATE,
            //         not with ON DUPLICATE KEY IGNORE
            MergeNotMatchedStep<R> notMatched = on;
            if (onDuplicateKeyUpdate)
                notMatched = condition.hasWhere()
                    ? on.whenMatchedAnd(condition.getWhere()).thenUpdate().set(updateMap)
                    : on.whenMatchedThenUpdate().set(updateMap);

            return t != null
                ? notMatched.whenNotMatchedThenInsert(f).values(t.fields())
                : notMatched.whenNotMatchedThenInsert(k).values(insertMaps.lastMap().entrySet().stream().filter(e -> k.contains(e.getKey())).map(Entry::getValue).collect(toList()));
        }
        else
            return DSL.sql("[ The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be emulated when inserting into non-updatable tables : " + table() + " ]");
    }

    /**
     * Produce a {@link Condition} that matches existing rows by the inserted or
     * updated primary key values.
     */
    @SuppressWarnings("unchecked")
    private final Condition matchByConflictingKeys(Context<?> ctx, Map<Field<?>, Field<?>> map) {
        Condition or = null;

        // [#7365] The ON CONFLICT clause can be emulated using MERGE by joining
        //         the MERGE's target and source tables on the conflict columns
        // [#7409] The ON CONFLICT ON CONSTRAINT clause can be emulated using MERGE by
        //         joining the MERGE's target and source tables on the constraint columns
        // [#6462] The ON DUPLICATE KEY UPDATE clause is emulated using the primary key.
        //         To properly reflect MySQL behaviour, it should use all the known unique keys.
        if (onConstraint != null && onConstraintUniqueKey == null)
            return DSL.condition("[ cannot create predicate from constraint with unknown columns ]");

        for (List<? extends Field<?>> fields : conflictingKeys(ctx)) {
            Condition and = null;

            for (Field<?> field : fields) {
                Field<Object> f = (Field<Object>) field;
                Condition other = matchByConflictingKey(ctx, f, (Field<Object>) map.get(f));
                and = (and == null) ? other : and.and(other);
            }

            or = (or == null) ? and : or.or(and);
        }

        return or;
    }

    /**
     * Produce a {@link Condition} that matches existing rows by the inserted or
     * updated primary key values.
     */
    @SuppressWarnings("unchecked")
    private final Condition matchByConflictingKeys(Context<?> ctx, Table<?> s) {
        Condition or = null;

        // [#7365] The ON CONFLICT (column list) clause can be emulated using MERGE by
        //         joining the MERGE's target and source tables on the conflict columns
        // [#7409] The ON CONFLICT ON CONSTRAINT clause can be emulated using MERGE by
        //         joining the MERGE's target and source tables on the constraint columns
        // [#6462] The ON DUPLICATE KEY UPDATE clause is emulated using the primary key.
        //         To properly reflect MySQL behaviour, it should use all the known unique keys.
        if (onConstraint != null && onConstraintUniqueKey == null)
            return DSL.condition("[ cannot create predicate from constraint with unknown columns ]");

        for (List<? extends Field<?>> fields : conflictingKeys(ctx)) {
            Condition and = null;

            for (Field<?> field : fields) {
                Field<Object> f = (Field<Object>) field;
                Condition other = matchByConflictingKey(ctx, f, s.field(f));
                and = (and == null) ? other : and.and(other);
            }

            or = (or == null) ? and : or.or(and);
        }

        return or;
    }

    private final <T> Condition matchByConflictingKey(Context<?> ctx, Field<T> f, Field<T> v) {






        return f.eq(v);
    }

    @Override
    public final boolean isExecutable() {
        return insertMaps.isExecutable() || defaultValues(configuration()) || select != null;
    }















}
