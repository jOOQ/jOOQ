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
import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT;
import static org.jooq.Clause.INSERT_RETURNING;
import static org.jooq.Clause.INSERT_SELECT;
// ...
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.dual;
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
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.aliasedFields;
import static org.jooq.impl.Tools.fieldNameStrings;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_CONSTRAINT_REFERENCE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST;
import static org.jooq.impl.Tools.DataKey.DATA_ON_DUPLICATE_KEY_WHERE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.InsertQuery;
import org.jooq.Merge;
import org.jooq.MergeMatchedSetMoreStep;
import org.jooq.MergeNotMatchedStep;
import org.jooq.MergeOnConditionStep;
import org.jooq.Name;
import org.jooq.Operator;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.StringUtils;

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
    private Constraint                       onConstraint;
    private UniqueKey<R>                     onConstraintUniqueKey;
    private QueryPartList<Field<?>>          onConflict;
    private final ConditionProviderImpl      condition;

    InsertQueryImpl(Configuration configuration, WithImpl with, Table<R> into) {
        super(configuration, with, into);

        this.updateMap = new FieldMapForUpdate(into, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT);
        this.insertMaps = new FieldMapsForInsert(into);
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
        this.onConflict = new QueryPartList<Field<?>>(fields);
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

        if (onConstraintUniqueKey == null) {
            for (UniqueKey<R> key : table().getKeys()) {
                if (constraint.getName().equals(key.getName())) {
                    onConstraintUniqueKey = key;
                    break;
                }
            }
        }
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
                case H2:
                case MARIADB:
                case MYSQL: {

                    // [#2508] In H2, this syntax is supported in MySQL MODE (we're assuming users will
                    //         set this mode in order to profit from this functionality). Up until
                    //         H2 1.4.197, qualification of columns in the ON DUPLICATE KEY UPDATE clause
                    //         wasn't supported (see https://github.com/h2database/h2database/issues/1027)
                    boolean oldQualify = ctx.qualify();
                    boolean newQualify = ctx.family() == H2 ? false : oldQualify;

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




                case POSTGRES:
                case SQLITE: {
                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_CONFLICT)
                       .sql(' ');

                    if (onConstraint != null) {
                        ctx.data(DATA_CONSTRAINT_REFERENCE, true);
                        ctx.visit(K_ON_CONSTRAINT)
                           .sql(' ')
                           .visit(onConstraint);

                        ctx.data().remove(DATA_CONSTRAINT_REFERENCE);
                    }
                    else {
                        boolean qualify = ctx.qualify();

                        ctx.sql('(');

                        if (onConflict != null && onConflict.size() > 0)
                            ctx.qualify(false)
                               .visit(onConflict)
                               .qualify(qualify);

                        // [#6462] There is no way to emulate MySQL's ON DUPLICATE KEY UPDATE
                        //         where all UNIQUE keys are considered for conflicts. PostgreSQL
                        //         doesn't allow ON CONFLICT DO UPDATE without either a conflict
                        //         column list or a constraint reference.
                        else if (table().getPrimaryKey() == null)
                            ctx.sql("[unknown primary key]");
                        else
                            ctx.qualify(false)
                               .visit(new Fields<Record>(table().getPrimaryKey().getFields()))
                               .qualify(qualify);

                        ctx.sql(')');
                    }

                    ctx.sql(' ')
                       .visit(K_DO_UPDATE)
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

                // Some databases allow for emulating this clause using a MERGE statement








                case DERBY:
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





                case MYSQL:
                case MARIADB: {
                    toSQLInsert(ctx);
                    ctx.start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .end(INSERT_ON_DUPLICATE_KEY_UPDATE);
                    break;
                }









                case POSTGRES:
                case SQLITE: {
                    toSQLInsert(ctx);
                    ctx.formatSeparator()
                       .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
                       .visit(K_ON_CONFLICT)
                       .sql(' ');

                    if (onConstraint != null) {
                        ctx.data(DATA_CONSTRAINT_REFERENCE, true);
                        ctx.visit(K_ON_CONSTRAINT)
                           .sql(' ')
                           .visit(onConstraint);

                        ctx.data().remove(DATA_CONSTRAINT_REFERENCE);
                    }
                    else if (onConflict != null && onConflict.size() > 0) {
                        boolean qualify = ctx.qualify();

                        ctx.sql('(')
                           .qualify(false)
                           .visit(onConflict)
                           .qualify(qualify)
                           .sql(')');
                    }

                    ctx.sql(' ')
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
























                case DERBY:
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

        // [#1295] MySQL dialects have native syntax for INSERT IGNORE
        // [#4376] [#8433] for SQLite render using ON CONFLICT DO NOTHING
        //                 rather than INSERT OR IGNORE
        if (onDuplicateKeyIgnore)
            if (SUPPORT_INSERT_IGNORE.contains(ctx.family()))
                ctx.visit(K_IGNORE).sql(' ');

        ctx.visit(K_INTO)
           .sql(' ')
           .declareTables(true)
           .visit(table(ctx))
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

                    int count = table().fields().length;
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
























    private final List<List<? extends Field<?>>> conflictingKeys(Configuration configuration) {

        // [#7365] PostgreSQL ON CONFLICT (conflict columns) clause
        if (onConflict != null && onConflict.size() > 0)
            return Collections.<List<? extends Field<?>>>singletonList(onConflict);

        // [#7409] PostgreSQL ON CONFLICT ON CONSTRAINT clause
        else if (onConstraintUniqueKey != null)
            return Collections.<List<? extends Field<?>>>singletonList(onConstraintUniqueKey.getFields());

        // [#6462] MySQL ON DUPLICATE KEY UPDATE clause
        //         Flag for backwards compatibility considers only PRIMARY KEY
        else if (TRUE.equals(Tools.settings(configuration).isEmulateOnDuplicateKeyUpdateOnPrimaryKeyOnly()))
            return Collections.<List<? extends Field<?>>>singletonList(table().getPrimaryKey().getFields());

        // [#6462] MySQL ON DUPLICATE KEY UPDATE clause
        //         All conflicting keys are considered
        List<UniqueKey<R>> keys = table().getKeys();
        List<List<? extends Field<?>>> result = new ArrayList<List<? extends Field<?>>>(keys.size());
        for (UniqueKey<R> key : keys)
            result.add(key.getFields());

        return result;
    }

    private final QueryPart toInsertSelect(Configuration configuration) {
        List<List<? extends Field<?>>> keys = conflictingKeys(configuration);

        if (!keys.isEmpty()) {

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
                        .from(table())
                        .where(matchByConflictingKeys(configuration, map))
                    );

                if (rows == null)
                    rows = row;
                else
                    rows = rows.unionAll(row);
            }

            return configuration.dsl()
                .insertInto(table())
                .columns(insertMaps.fields())
                .select(selectFrom(DSL.table(rows).as("t")));
        }
        else {
            throw new IllegalStateException("The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be emulated when inserting into tables without any known keys : " + table());
        }
    }

    private final Merge<R> toMerge(Configuration configuration) {
        if ((onConflict != null && onConflict.size() > 0)
            || onConstraint != null
            || !table().getKeys().isEmpty()) {

            // [#6375] INSERT .. VALUES and INSERT .. SELECT distinction also in MERGE
            Table<?> t = select == null
                ? dual()
                : DSL.table(select).as("t", fieldNameStrings(insertMaps.fields().toArray(EMPTY_FIELD)));

            MergeOnConditionStep<R> on = select == null
                ? configuration.dsl().mergeInto(table())
                                     .usingDual()
                                     .on(matchByConflictingKeys(configuration, insertMaps.lastMap()))
                : configuration.dsl().mergeInto(table())
                                     .using(t)
                                     .on(matchByConflictingKeys(configuration, t));

            // [#1295] Use UPDATE clause only when with ON DUPLICATE KEY UPDATE,
            //         not with ON DUPLICATE KEY IGNORE
            MergeNotMatchedStep<R> notMatched = on;
            if (onDuplicateKeyUpdate) {
                MergeMatchedSetMoreStep<R> set =
                    on.whenMatchedThenUpdate()
                      .set(updateMap);

                if (condition.hasWhere())
                    notMatched = condition != null
                        ? set.where(condition)
                        : set;
            }

            return select == null
                ? notMatched.whenNotMatchedThenInsert(insertMaps.fields())
                            .values(insertMaps.lastMap().values())
                : notMatched.whenNotMatchedThenInsert(insertMaps.fields())
                            .values(t.fields());
        }
        else {
            throw new IllegalStateException("The ON DUPLICATE KEY IGNORE/UPDATE clause cannot be emulated when inserting into non-updatable tables : " + table());
        }
    }

    /**
     * Produce a {@link Condition} that matches existing rows by the inserted or
     * updated primary key values.
     */
    @SuppressWarnings("unchecked")
    private final Condition matchByConflictingKeys(Configuration configuration, Map<Field<?>, Field<?>> map) {
        Condition or = null;

        // [#7365] The ON CONFLICT clause can be emulated using MERGE by joining
        //         the MERGE's target and source tables on the conflict columns
        // [#7409] The ON CONFLICT ON CONSTRAINT clause can be emulated using MERGE by
        //         joining the MERGE's target and source tables on the constraint columns
        // [#6462] The ON DUPLICATE KEY UPDATE clause is emulated using the primary key.
        //         To properly reflect MySQL behaviour, it should use all the known unique keys.
        if (onConstraint != null && onConstraintUniqueKey == null)
            return DSL.condition("[ cannot create predicate from constraint with unknown columns ]");

        for (List<? extends Field<?>> fields : conflictingKeys(configuration)) {
            Condition and = null;

            for (Field<?> field : fields) {
                Field<Object> f = (Field<Object>) field;
                Field<Object> v = (Field<Object>) map.get(f);

                Condition other =






                    f.eq(v)
                ;
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
    private final Condition matchByConflictingKeys(Configuration configuration, Table<?> s) {
        Condition or = null;

        // [#7365] The ON CONFLICT (column list) clause can be emulated using MERGE by
        //         joining the MERGE's target and source tables on the conflict columns
        // [#7409] The ON CONFLICT ON CONSTRAINT clause can be emulated using MERGE by
        //         joining the MERGE's target and source tables on the constraint columns
        // [#6462] The ON DUPLICATE KEY UPDATE clause is emulated using the primary key.
        //         To properly reflect MySQL behaviour, it should use all the known unique keys.
        if (onConstraint != null && onConstraintUniqueKey == null)
            return DSL.condition("[ cannot create predicate from constraint with unknown columns ]");

        for (List<? extends Field<?>> fields : conflictingKeys(configuration)) {
            Condition and = null;

            for (Field<?> field : fields) {
                Field<Object> f = (Field<Object>) field;
                Field<Object> v = s.field(f);

                Condition other =






                    f.eq(v)
                ;

                and = (and == null) ? other : and.and(other);
            }

            or = (or == null) ? and : or.or(and);
        }

        return or;
    }

    @Override
    public final boolean isExecutable() {
        return insertMaps.isExecutable() || defaultValues || select != null;
    }

    @Override
    final int estimatedRowCount() {
        if (defaultValues)
            return 1;
        else if (select != null)
            return Integer.MAX_VALUE;
        else
            return insertMaps.rows;
    }
}
