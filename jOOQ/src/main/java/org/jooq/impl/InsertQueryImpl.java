/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.ConditionProviderImpl.extractCondition;
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.FieldMapsForInsert.toSQLInsertSelect;
import static org.jooq.impl.Keywords.K_AS;
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
import static org.jooq.impl.Tools.aliasedFields;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.degree;
import static org.jooq.impl.Tools.flattenCollection;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.orElse;
import static org.jooq.impl.Tools.qualify;
import static org.jooq.impl.Tools.unqualified;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_CONSTRAINT_REFERENCE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MANDATORY_WHERE_CLAUSE;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_ON_DUPLICATE_KEY_WHERE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.Set;

import org.jooq.CheckReturnValue;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
import org.jooq.GeneratorStatementType;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.MergeNotMatchedStep;
import org.jooq.MergeOnConditionStep;
import org.jooq.Name;
import org.jooq.Operator;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
// ...
import org.jooq.UniqueKey;
import org.jooq.conf.ParamType;
import org.jooq.conf.WriteIfReadonly;
import org.jooq.impl.FieldMapForUpdate.SetClause;
import org.jooq.impl.QOM.Insert;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.QOM.UnmodifiableMap;
import org.jooq.impl.QOM.With;
import org.jooq.impl.Tools.BooleanDataKey;
import org.jooq.impl.Tools.ExtendedDataKey;
import org.jooq.tools.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Eder
 */
final class InsertQueryImpl<R extends Record>
extends
    AbstractStoreQuery<R, Field<?>, Field<?>>
implements
    InsertQuery<R>,
    QOM.Insert<R>
{

    static final Clause[]        CLAUSES                                       = { INSERT };
    static final Set<SQLDialect> SUPPORT_INSERT_IGNORE                         = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect> SUPPORTS_OPTIONAL_DO_UPDATE_CONFLICT_TARGETS  = SQLDialect.supportedBy(SQLITE);
    static final Set<SQLDialect> NO_SUPPORT_DERIVED_COLUMN_LIST_IN_MERGE_USING = SQLDialect.supportedBy(DERBY, H2);
    static final Set<SQLDialect> NO_SUPPORT_SUBQUERY_IN_MERGE_USING            = SQLDialect.supportedBy(DERBY);
    static final Set<SQLDialect> REQUIRE_NEW_MYSQL_EXCLUDED_EMULATION          = SQLDialect.supportedBy(MYSQL);

    final FieldMapsForInsert     insertMaps;
    Select<?>                    select;
    boolean                      defaultValues;
    boolean                      onDuplicateKeyUpdate;
    boolean                      onDuplicateKeyIgnore;
    Constraint                   onConstraint;
    UniqueKey<R>                 onConstraintUniqueKey;
    QueryPartList<Field<?>>      onConflict;
    final ConditionProviderImpl  onConflictWhere;
    final FieldMapForUpdate      updateMap;
    final ConditionProviderImpl  updateWhere;

    InsertQueryImpl(Configuration configuration, WithImpl with, Table<R> into) {
        super(configuration, with, into);

        this.insertMaps = new FieldMapsForInsert(into);
        this.onConflictWhere = new ConditionProviderImpl();
        this.updateMap = new FieldMapForUpdate(into, SetClause.INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT);
        this.updateWhere = new ConditionProviderImpl();
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

    private final void onConflictOnConstraint0(Constraint constraint) {
        this.onConstraint = constraint;

        if (onConstraintUniqueKey == null)
            onConstraintUniqueKey = Tools.findAny(table().getKeys(), key -> constraint.getName().equals(key.getName()));
    }

    @Override
    public final void onDuplicateKeyUpdate(boolean flag) {
        onDuplicateKeyUpdate = flag;

        if (flag) {
            onDuplicateKeyIgnore = false;
        }
    }

    @Override
    public final void onDuplicateKeyIgnore(boolean flag) {
        onDuplicateKeyIgnore = flag;

        if (flag) {
            onDuplicateKeyUpdate = false;
            updateMap.clear();
            updateWhere.setWhere(null);
        }
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
        updateWhere.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        updateWhere.addConditions(conditions);
    }

    @Override
    public final void addConditions(Collection<? extends Condition> conditions) {
        updateWhere.addConditions(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition conditions) {
        updateWhere.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        updateWhere.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Collection<? extends Condition> conditions) {
        updateWhere.addConditions(operator, conditions);
    }

    @Override
    public final void setDefaultValues() {
        defaultValues = true;
        select = null;
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
        defaultValues = false;
        insertMaps.clear();
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


                case DUCKDB:
                case POSTGRES:
                case SQLITE:
                case YUGABYTEDB: {

                    // [#7552] Dialects supporting both MERGE and ON CONFLICT should
                    //         generate MERGE to emulate MySQL's ON DUPLICATE KEY UPDATE
                    //         if there are multiple known unique constraints.
                    if (ctx.dialect().supports(POSTGRES)
                            && onConstraint == null
                            && onConflict == null
                            && returning.isEmpty()
                            && table().getKeys().size() > 1) {
                        acceptMerge(ctx);
                    }
                    else {
                        ctx.data(DATA_MANDATORY_WHERE_CLAUSE, ctx.family() == SQLITE, c -> toSQLInsert(c, false));

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

                        acceptOnConflictWhere(ctx);

                        ctx.formatSeparator()
                           .visit(K_DO_UPDATE)
                           .formatSeparator()
                           .visit(K_SET)
                           .formatIndentStart()
                           .formatSeparator()
                           .visit(updateMapComputedOnClientStored(ctx))
                           .formatIndentEnd();

                        if (updateWhere.hasWhere())
                            ctx.formatSeparator()
                               .visit(K_WHERE)
                               .sql(' ')
                               .visit(updateWhere);

                        ctx.end(INSERT_ON_DUPLICATE_KEY_UPDATE);
                    }

                    break;
                }

                // Some databases allow for emulating this clause using a MERGE statement









                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB: {
                    acceptMerge(ctx);
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
                    FieldMapForUpdate um = updateMapComputedOnClientStored(ctx);
                    boolean requireNewMySQLExcludedEmulation = REQUIRE_NEW_MYSQL_EXCLUDED_EMULATION.contains(ctx.dialect()) && anyMatch(um.values(), v -> v instanceof Excluded);

                    Set<Field<?>> keys = toSQLInsert(ctx, requireNewMySQLExcludedEmulation);

                    // [#5214] The alias only applies with INSERT .. VALUES
                    if (requireNewMySQLExcludedEmulation && select == null)
                        ctx.formatSeparator()
                           .visit(K_AS).sql(' ').visit(name("t"));

                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_DUPLICATE_KEY_UPDATE)
                       .formatIndentStart()
                       .formatSeparator()
                       .qualify(newQualify);

                    // [#8479] Emulate WHERE clause using CASE
                    if (updateWhere.hasWhere())
                        ctx.data(DATA_ON_DUPLICATE_KEY_WHERE, updateWhere.getWhere());

                    if (requireNewMySQLExcludedEmulation) {
                        um.replaceAll((k, v) -> {
                            if (v instanceof Excluded<?> e) {
                                return keys.contains(e.$field()) ? v : qualify(table(), e.$field());
                            }
                            else
                                return v;
                        });
                    }

                    ctx.visit(um);

                    if (updateWhere.hasWhere())
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
            switch (ctx.family()) {

                // The default emulation











                case FIREBIRD:
                case IGNITE:
                case TRINO: {
                    acceptInsertSelect(ctx);
                    break;
                }



                case DUCKDB:
                case POSTGRES:
                case SQLITE:
                case YUGABYTEDB: {








                    ctx.data(DATA_MANDATORY_WHERE_CLAUSE, ctx.family() == SQLITE, c -> toSQLInsert(c, false));

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
                            acceptOnConflictWhere(ctx);
                        }











                    }

                    ctx.formatSeparator()
                       .visit(K_DO_NOTHING)
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
                    break;
                }

                // CUBRID can emulate this using ON DUPLICATE KEY UPDATE
                case CUBRID: {
                    FieldMapForUpdate update = new FieldMapForUpdate(table(), SetClause.INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT);
                    Field<?> field = table().field(0);
                    update.put(field, field);

                    toSQLInsert(ctx, false);
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
                    acceptMerge(ctx);
                    break;
                }

                case DERBY: {

                    // [#10989] Cannot use MERGE with SELECT: [42XAL]: The source table of a MERGE statement must be a base table or table function.
                    if (select != null)
                        acceptInsertSelect(ctx);
                    else
                        acceptMerge(ctx);

                    break;
                }

                // MySQL has a nice, native syntax for this
                default: {
                    toSQLInsert(ctx, false);
                    ctx.start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
                    break;
                }
            }
        }

        // Default mode
        // ------------
        else {
            toSQLInsert(ctx, false);
            ctx.start(INSERT_ON_DUPLICATE_KEY_UPDATE)
               .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
        }

        ctx.start(INSERT_RETURNING);
        toSQLReturning(ctx);
        ctx.end(INSERT_RETURNING);
    }

    private final void acceptOnConflictWhere(Context<?> ctx) {
        if (onConflictWhere.hasWhere())

            // [#11732] [#13660] Avoid qualification, which wasn't supported in older PG versions
            // [#12531]          In this very particular case, bind values aren't desirable to avoid
            //                   mismatches with index specifications
            ctx.paramType(INLINED,
                c1 -> c1.qualify(false,
                    c2 -> c2
                        .formatSeparator()
                        .visit(K_WHERE)
                        .sql(' ')
                        .visit(onConflictWhere.getWhere())
                )
            );
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    private final Set<Field<?>> toSQLInsert(Context<?> ctx, boolean requireNewMySQLExcludedEmulation) {
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

        Set<Field<?>> fields = insertMaps.toSQLReferenceKeys(ctx);
        ctx.end(INSERT_INSERT_INTO);





        if (select != null) {
            Set<Field<?>> keysFlattened = insertMaps.keysFlattened(ctx, GeneratorStatementType.INSERT);

            // [#2995] Prevent the generation of wrapping parentheses around the
            //         INSERT .. SELECT statement's SELECT because they would be
            //         interpreted as the (missing) INSERT column list's parens.
            if (keysFlattened.size() == 0)
                ctx.data(DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST, true);












            ctx.data(DATA_INSERT_SELECT, true);

            Select<?> s = select;











            if (requireNewMySQLExcludedEmulation)
                s = selectFrom(s.asTable(DSL.table(name("t")), keysFlattened));

            // [#8353] TODO: Support overlapping embeddables
            toSQLInsertSelect(ctx, s);

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

        return fields;
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
    private final void acceptInsertSelect(Context<?> ctx) {
        List<List<? extends Field<?>>> keys = conflictingKeys(ctx);

        if (!keys.isEmpty()) {
            Select<Record> rows = null;
            Set<Field<?>> fields = insertMaps.keysFlattened(ctx, GeneratorStatementType.INSERT);

            // [#10989] INSERT .. SELECT .. ON DUPLICATE KEY IGNORE
            if (select != null) {
                Map<Field<?>, Field<?>> map = new HashMap<>();
                Field<?>[] names = Tools.fields(degree(select));
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

            ctx.visit(ctx.dsl()
                .insertInto(table())
                .columns(fields)
                .select(selectFrom(rows.asTable("t")))
            );
        }
        else
            ctx.sql("[ The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be emulated when inserting into tables without any known keys : " + table() + " ]");
    }

    private final void acceptMerge(Context<?> ctx) {
        ctx.data(ExtendedDataKey.DATA_INSERT_ON_DUPLICATE_KEY_UPDATE, this, c -> acceptMerge0(c));
    }

    private final void acceptMerge0(Context<?> ctx) {
        if ((onConflict != null && onConflict.size() > 0)
            || onConstraint != null
            || !table().getKeys().isEmpty()) {

            Table<?> t;
            Set<Field<?>> k = insertMaps.keysFlattened(ctx, null);
            Collection<Field<?>> f = null;

            if (!NO_SUPPORT_SUBQUERY_IN_MERGE_USING.contains(ctx.dialect())) {
                f = k.isEmpty() ? asList(table().fields()) : k;

                // [#10461]          Multi row inserts need to be emulated using select
                // [#11770] [#11880] Single row inserts also do, in some dialects
                Select<?> s = select != null
                    ? select
                    : insertMaps.insertSelect(ctx, null);

                // [#8937] With DEFAULT VALUES, there is no SELECT. Create one from
                //         known DEFAULT expressions, or use NULL.
                if (s == null)
                    s = select(map(f, x -> x.getDataType().defaulted() ? x.getDataType().default_() : DSL.inline(null, x)));










                // [#6375] INSERT .. VALUES and INSERT .. SELECT distinction also in MERGE
                if (NO_SUPPORT_DERIVED_COLUMN_LIST_IN_MERGE_USING.contains(ctx.dialect()))
                    t = new AliasedSelect<Record>((Select<Record>) s, true, true, false, map(f, Field::getUnqualifiedName, Name[]::new)).as("t");
                else
                    t = s.asTable("t", map(f, Field::getName, String[]::new));
            }
            else
                t = null;

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
            if (onDuplicateKeyUpdate) {
                FieldMapForUpdate um = new FieldMapForUpdate(updateMap, SetClause.INSERT);

                // [#5214] [#13571] PostgreSQL EXCLUDED pseudo table emulation
                //                  The InsertQueryImpl uses "t" as table name
                um.replaceAll((key, v) -> {
                    if (v instanceof Excluded<?> e) {
                        if (t != null)

                            // If the field isn't part of the USING clause, just
                            // set the field to itself to maintain the user's
                            // intended (?) touch semantics, which might affect
                            // triggers
                            return orElse(t.field(e.$field()), () -> qualify(table(), e.$field()));
                        else
                            return orElse(insertMaps.lastMap().get(e.$field()), () -> qualify(table(), e.$field()));
                    }
                    else
                        return v;
                });

                // [#9879] EXCLUDED emulation must happen before client side
                //         computed column emulation
                um = updateMapComputedOnClientStored(ctx, um);

                notMatched = updateWhere.hasWhere()
                    ? on.whenMatchedAnd(updateWhere.getWhere()).thenUpdate().set(um)
                    : on.whenMatchedThenUpdate().set(um);
            }

            ctx.visit(t != null
                ? notMatched.whenNotMatchedThenInsert(f).values(t.fields())
                : notMatched.whenNotMatchedThenInsert(k).values(insertMaps.lastMap().entrySet().stream().filter(e -> k.contains(e.getKey())).map(Entry::getValue).collect(toList()))
            );
        }
        else
            ctx.sql("[ The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be emulated when inserting into non-updatable tables : " + table() + " ]");
    }

    private final FieldMapForUpdate updateMapComputedOnClientStored(Context<?> ctx) {

        // [#5214] Always make a copy to benefit other emulations
        return updateMapComputedOnClientStored(ctx, new FieldMapForUpdate(updateMap, SetClause.INSERT));
    }

    private final FieldMapForUpdate updateMapComputedOnClientStored(Context<?> ctx, FieldMapForUpdate um) {












        return um;
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






















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    final InsertQueryImpl<R> copy(Consumer<? super InsertQueryImpl<R>> finisher) {
        return copy(finisher, table);
    }

    final <O extends Record> InsertQueryImpl<O> copy(Consumer<? super InsertQueryImpl<O>> finisher, Table<O> t) {
        InsertQueryImpl<O> i = new InsertQueryImpl<>(configuration(), with, t);

        if (!returning.isEmpty())
            i.setReturning(returning);

        i.insertMaps.empty.putAll(insertMaps.empty);
        for (Entry<Field<?>, List<Field<?>>> e : insertMaps.values.entrySet())
            i.insertMaps.values.put(e.getKey(), new ArrayList<>(e.getValue()));
        i.insertMaps.rows = insertMaps.rows;
        i.insertMaps.nextRow = insertMaps.nextRow;
        i.defaultValues = defaultValues;
        i.select = select;

        if (onConflict != null)
            i.onConflict(onConflict);

        if (onConflictWhere.hasWhere())
            i.onConflictWhere.setWhere(extractCondition(onConflictWhere));

        i.onConstraint = onConstraint;
        i.onConstraintUniqueKey = (UniqueKey) onConstraintUniqueKey;
        i.onDuplicateKeyIgnore = onDuplicateKeyIgnore;
        i.onDuplicateKeyUpdate = onDuplicateKeyUpdate;
        i.updateWhere.setWhere(updateWhere.getWhere());
        i.updateMap.putAll(updateMap);
        finisher.accept(i);
        return i;
    }

    @Override
    public final WithImpl $with() {
        return with;
    }

    @Override
    public final Table<R> $into() {
        return table;
    }

    @Override
    public final Insert<?> $into(Table<?> newInto) {
        if ($into() == newInto)
            return this;
        else
            return copy(i -> {}, newInto);
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $columns() {
        return QOM.unmodifiable(new ArrayList<>(insertMaps.values.keySet()));
    }

    @Override
    public final Insert<?> $columns(Collection<? extends Field<?>> columns) {
        return copy(i -> {
            Map<Field<?>, List<Field<?>>> v = new LinkedHashMap<>();

            for (Field<?> c : columns)
                if (i.insertMaps.values.get(c) == null)
                    v.put(c, new ArrayList<>(nCopies(i.insertMaps.rows, inline(null, c))));
                else
                    v.put(c, i.insertMaps.values.get(c));

            i.insertMaps.values.clear();
            i.insertMaps.values.putAll(v);
        });
    }

    @Override
    public final Select<?> $select() {
        return select;
    }

    @Override
    public final Insert<?> $select(Select<?> newSelect) {
        if ($select() == newSelect)
            return this;
        else
            return copy(i -> {
                i.setSelect($columns(), newSelect);
            });
    }

    @Override
    public final boolean $defaultValues() {
        return defaultValues;
    }

    @Override
    public final Insert<?> $defaultValues(boolean newDefaultValues) {
        if ($defaultValues() == newDefaultValues)
            return this;
        else
            return copy(i -> {
                if (newDefaultValues)
                    i.setDefaultValues();
                else
                    i.defaultValues = false;
            });
    }

    @Override
    public final UnmodifiableList<? extends Row> $values() {
        return QOM.unmodifiable(insertMaps.rows());
    }

    @Override
    public final Insert<?> $values(Collection<? extends Row> values) {
        return copy(i -> {
            i.insertMaps.rows = values.size();
            Iterator<Entry<Field<?>, List<Field<?>>>> it = i.insertMaps.values.entrySet().iterator();
            int index = 0;

            while (it.hasNext()) {
                int c = index;
                Entry<Field<?>, List<Field<?>>> e = it.next();
                Field<?> n = inline(null, e.getKey());
                e.getValue().clear();
                e.getValue().addAll(map(values, v -> (Field<?>) StringUtils.defaultIfNull(v.field(c), n)));
                index++;
            }
        });
    }

    @Override
    public final boolean $onDuplicateKeyIgnore() {
        return onDuplicateKeyIgnore;
    }

    @Override
    public final Insert<?> $onDuplicateKeyIgnore(boolean newOnDuplicateKeyIgnore) {
        if ($onDuplicateKeyIgnore() == newOnDuplicateKeyIgnore)
            return this;
        else
            return copy(i -> i.onDuplicateKeyIgnore(newOnDuplicateKeyIgnore));
    }

    @Override
    public final boolean $onDuplicateKeyUpdate() {
        return onDuplicateKeyUpdate;
    }

    @Override
    public final Insert<?> $onDuplicateKeyUpdate(boolean newOnDuplicateKeyUpdate) {
        if ($onDuplicateKeyUpdate() == newOnDuplicateKeyUpdate)
            return this;
        else
            return copy(i -> i.onDuplicateKeyUpdate(newOnDuplicateKeyUpdate));
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $onConflict() {
        return QOM.unmodifiable(onConflict == null ? new ArrayList<>() : onConflict);
    }

    @Override
    public final Insert<?> $onConflict(Collection<? extends Field<?>> newOnConflict) {
        if ($onConflict() == newOnConflict)
            return this;
        else
            return copy(i -> i.onConflict(newOnConflict));
    }

    @Override
    public final Condition $onConflictWhere() {
        return onConflictWhere.getWhereOrNull();
    }

    @Override
    public final Insert<?> $onConflictWhere(Condition newWhere) {
        if ($onConflictWhere() == newWhere)
            return this;
        else
            return copy(i -> i.onConflictWhere.setWhere(newWhere));
    }

    @Override
    public final UnmodifiableMap<? extends FieldOrRow, ? extends FieldOrRowOrSelect> $updateSet() {
        return QOM.unmodifiable(updateMap);
    }

    @Override
    public final Insert<?> $updateSet(Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> newUpdateSet) {
        if ($updateSet() == newUpdateSet)
            return this;
        else
            return copy(i -> i.addValuesForUpdate(newUpdateSet));
    }

    @Override
    public final Condition $updateWhere() {
        return updateWhere.getWhereOrNull();
    }

    @Override
    public final Insert<?> $updateWhere(Condition newWhere) {
        if ($updateWhere() == newWhere)
            return this;
        else
            return copy(i -> i.updateWhere.setWhere(newWhere));
    }


































































}
