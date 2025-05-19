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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
// ...
import static org.jooq.SQLDialect.IGNITE;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.TableOptions.TableType.MATERIALIZED_VIEW;
import static org.jooq.TableOptions.TableType.VIEW;
import static org.jooq.impl.Comparators.CHECK_COMP;
import static org.jooq.impl.Comparators.FOREIGN_KEY_COMP;
import static org.jooq.impl.Comparators.INDEX_COMP;
import static org.jooq.impl.Comparators.KEY_COMP;
import static org.jooq.impl.Comparators.NAMED_COMP;
import static org.jooq.impl.Comparators.UNQUALIFIED_COMP;
import static org.jooq.impl.ConstraintType.CHECK;
import static org.jooq.impl.ConstraintType.FOREIGN_KEY;
import static org.jooq.impl.ConstraintType.PRIMARY_KEY;
import static org.jooq.impl.ConstraintType.UNIQUE;
import static org.jooq.impl.CreateTableImpl.SUPPORT_NULLABLE_PRIMARY_KEY;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.Tools.NO_SUPPORT_TIMESTAMP_PRECISION;
import static org.jooq.impl.Tools.NO_SUPPORT_TIME_PRECISION;
import static org.jooq.impl.Tools.allMatch;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.autoAlias;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.findAny;
import static org.jooq.impl.Tools.flatMap;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.isVal1;
import static org.jooq.impl.Tools.map;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.defaultString;
import static org.jooq.tools.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

import org.jooq.AlterSequenceFlagsStep;
import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Configuration;
import org.jooq.DDLExportConfiguration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Function3;
import org.jooq.Index;
import org.jooq.Key;
import org.jooq.Meta;
import org.jooq.MigrationConfiguration;
import org.jooq.Name;
import org.jooq.Named;
import org.jooq.Nullability;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableOptions.TableType;
import org.jooq.UniqueKey;
import org.jooq.conf.Settings;
import org.jooq.tools.StringUtils;

/**
 * A class producing a diff between two {@link Meta} objects.
 *
 * @author Lukas Eder
 */
final class Diff {

    private static final Set<SQLDialect> NO_SUPPORT_PK_NAMES = SQLDialect.supportedBy(IGNITE, MARIADB, MYSQL);

    private final MigrationConfiguration migrateConf;
    private final DDLExportConfiguration exportConf;
    private final DSLContext             ctx;
    private final Meta                   meta1;
    private final Meta                   meta2;
    private final DDL                    ddl;

    Diff(Configuration configuration, MigrationConfiguration migrateConf, Meta meta1, Meta meta2) {
        this.migrateConf = migrateConf;
        this.exportConf = new DDLExportConfiguration()
            .createOrReplaceView(migrateConf.createOrReplaceView())
            .createOrReplaceMaterializedView(migrateConf.createOrReplaceMaterializedView());

        this.ctx = configuration.dsl();
        this.meta1 = meta1;
        this.meta2 = meta2;
        this.ddl = new DDL(ctx, exportConf);
    }

    final Queries queries() {
        return ctx.queries(patch(appendCatalogs(new DiffResult(), meta1.getCatalogs(), meta2.getCatalogs())).queries);
    }

    private final DiffResult patch(DiffResult result) {

        // [#18388] The final outcome of a diff may have to be patched with additional statements, for example,
        //          when a child table is dropped and its parent PK or UK is dropped as well, we must drop
        //          the child table's FK explicitly in order to ensure that happens first, before the PK or UK is dropped.
        //          A better solution would be to detect an "ideal" drop order among tables, but that would require a more
        //          sophisticated dependency anylsis, and it would still not always be possible.
        if (anyMatch(result.queries, q -> droppingPKorUK(q)) && anyMatch(result.queries, q -> q instanceof QOM.DropTable)) {
            List<ForeignKey<?, ?>> fks = flatMap(
                filter(result.queries, q -> q instanceof QOM.DropTable),
                q -> ((QOM.DropTable) q).$table().getReferences()
            );

            Set<UniqueKey<?>> uks = new HashSet<>(map(
                filter(result.queries, q -> droppingPKorUK(q)),
                q -> droppedPKorUK((AlterTableImpl) q)
            ));

            boolean sort = false;
            for (ForeignKey<?, ?> x : filter(fks, fk -> uks.contains(fk.getKey()) && !result.droppedFks.contains(fk))) {
                result.queries.add(ctx.alterTable(x.getTable()).dropForeignKey(x.constraint()));
                sort = true;
            }

            if (sort)
                result.queries.sort(Diff::sortOrder);
        }

        return result;
    }

    private final boolean droppingPKorUK(Query q) {
        if (q instanceof AlterTableImpl a) {
            if (a.$dropConstraintType() == PRIMARY_KEY)
                return true;
            else if (a.$dropConstraintType() == UNIQUE)
                return true;
            else
                return droppedPKorUK(a) != null;
        }

        return false;
    }

    private final UniqueKey<?> droppedPKorUK(AlterTableImpl a) {

        // [#18388] TODO: Is there a case where meta data isn't available on the Table?
        if (a.$dropConstraintType() == PRIMARY_KEY)
            return a.$table().getPrimaryKey();

        // [#18388] TODO: Is there a case where we're comparing structural and nominal constraints, which should match?
        else
            return findAny(a.$table().getUniqueKeys(), u -> u.constraint().equals(a.$dropConstraint()));
    }

    private final DiffResult appendCatalogs(DiffResult result, List<Catalog> l1, List<Catalog> l2) {
        return append(result, l1, l2, null,

            // TODO Implement this for SQL Server support.
            null,

            // TODO Implement this for SQL Server support.
            null,

            (r, c1, c2) -> appendSchemas(r, c1.getSchemas(), c2.getSchemas())
        );
    }

    private final DiffResult appendSchemas(DiffResult result, List<Schema> l1, List<Schema> l2) {
        return append(result, l1, l2, null,
            (r, s) -> r.queries.addAll(Arrays.asList(ctx.ddl(s).queries())),
            (r, s) -> {
                if (s.getTables().isEmpty() && s.getSequences().isEmpty()) {
                    if (!StringUtils.isEmpty(s.getName()))
                        r.queries.add(ctx.dropSchema(s));
                }
                else if (migrateConf.dropSchemaCascade()) {

                    // TODO: Can we reuse the logic from DROP_TABLE?
                    for (Table<?> t1 : s.getTables())
                        for (UniqueKey<?> uk : t1.getKeys())
                            r.droppedFks.addAll(uk.getReferences());

                    if (!StringUtils.isEmpty(s.getName()))
                        r.queries.add(ctx.dropSchema(s).cascade());
                }
                else {
                    for (Table<?> t2 : s.getTables())
                        dropTable().drop(r, t2);

                    for (Sequence<?> seq : s.getSequences())
                        dropSequence().drop(r, seq);

                    if (!StringUtils.isEmpty(s.getName()))
                        r.queries.add(ctx.dropSchema(s));
                }
            },
            (r, s1, s2) -> {
                appendDomains(r, s1.getDomains(), s2.getDomains());
                appendTables(r, s1.getTables(), s2.getTables());
                appendSequences(r, s1.getSequences(), s2.getSequences());
            }
        );
    }

    private final Drop<Sequence<?>> dropSequence() {
        return (r, s) -> r.queries.add(ctx.dropSequence(s));
    }

    private final DiffResult appendSequences(DiffResult result, List<? extends Sequence<?>> l1, List<? extends Sequence<?>> l2) {
        return append(result, l1, l2, null,
            (r, s) -> r.queries.add(ddl.createSequence(s)),
            dropSequence(),
            (r, s1, s2) -> {
                AlterSequenceFlagsStep stmt = null;
                AlterSequenceFlagsStep stmt0 = ctx.alterSequence(s1);

                if (s2.getStartWith() != null && !equivalentSequenceFlag(s2, s1, Sequence::getStartWith, this::defaultStartWithValue))
                    stmt = defaultIfNull(stmt, stmt0).startWith(s2.getStartWith());
                else if (s2.getStartWith() == null && s1.getStartWith() != null && !equivalentSequenceFlag(s1, s2, Sequence::getStartWith, this::defaultStartWithValue))
                    stmt = defaultIfNull(stmt, stmt0).startWith(defaultStartWithValue(s2));

                if (s2.getIncrementBy() != null && !equivalentSequenceFlag(s2, s1, Sequence::getIncrementBy, this::defaultIncrementByValue))
                    stmt = defaultIfNull(stmt, stmt0).incrementBy(s2.getIncrementBy());
                else if (s2.getIncrementBy() == null && s1.getIncrementBy() != null && !equivalentSequenceFlag(s1, s2, Sequence::getIncrementBy, this::defaultIncrementByValue))
                    stmt = defaultIfNull(stmt, stmt0).incrementBy(defaultIncrementByValue(s2));

                if (s2.getMinvalue() != null && !equivalentSequenceFlag(s2, s1, Sequence::getMinvalue, this::defaultMinValue))
                    stmt = defaultIfNull(stmt, stmt0).minvalue(s2.getMinvalue());
                else if (s2.getMinvalue() == null && s1.getMinvalue() != null && !equivalentSequenceFlag(s1, s2, Sequence::getMinvalue, this::defaultMinValue))
                    stmt = defaultIfNull(stmt, stmt0).noMinvalue();

                if (s2.getMaxvalue() != null && !equivalentSequenceFlag(s2, s1, Sequence::getMaxvalue, this::defaultMaxValue))
                    stmt = defaultIfNull(stmt, stmt0).maxvalue(s2.getMaxvalue());
                else if (s2.getMaxvalue() == null && s1.getMaxvalue() != null && !equivalentSequenceFlag(s1, s2, Sequence::getMaxvalue, this::defaultMaxValue))
                    stmt = defaultIfNull(stmt, stmt0).noMaxvalue();

                if (s2.getCache() != null && !s2.getCache().equals(s1.getCache()))
                    stmt = defaultIfNull(stmt, stmt0).cache(s2.getCache());
                else if (s2.getCache() == null && s1.getCache() != null)
                    stmt = defaultIfNull(stmt, stmt0).noCache();

                if (s2.getCycle() && !s1.getCycle())
                    stmt = defaultIfNull(stmt, stmt0).cycle();
                else if (!s2.getCycle() && s1.getCycle())
                    stmt = defaultIfNull(stmt, stmt0).noCycle();

                if (stmt != null)
                    r.queries.add(stmt);
            }
        );
    }

    private final boolean equivalentSequenceFlag(
        Sequence<?> s2,
        Sequence<?> s1,
        Function<? super Sequence<?>, ? extends Field<?>> flag,
        ToLongFunction<? super Sequence<?>> defaultValue
    ) {
        Field<?> sw2 = flag.apply(s2);
        Field<?> sw1 = flag.apply(s1);

        if (Objects.equals(sw2, sw1))
            return true;
        else
            return equivalentSequenceFlagValue(sw2, sw1, defaultValue.applyAsLong(s2))
                || equivalentSequenceFlagValue(sw1, sw2, defaultValue.applyAsLong(s1));
    }

    private final boolean equivalentSequenceFlagValue(
        Field<?> sw2,
        Field<?> sw1,
        Long defaultValue
    ) {
        return sw1 == null && isVal1(sw2, v -> Objects.equals(Convert.convert(v.getValue(), Long.class), defaultValue));
    }

    private final Long defaultStartWithValue(Sequence<?> s) {
        switch (ctx.family()) {
            case HSQLDB:
                return 0L;
            default:
                return 1L;
        }
    }

    private final Long defaultIncrementByValue(Sequence<?> s) {
        return 1L;
    }

    private final Long defaultMinValue(Sequence<?> s) {
        if (s.getDataType().getFromType() == Byte.class)
            return (long) Byte.MIN_VALUE;
        else if (s.getDataType().getFromType() == Short.class)
            return (long) Short.MIN_VALUE;
        else if (s.getDataType().getFromType() == Integer.class)
            return (long) Integer.MIN_VALUE;
        else if (s.getDataType().getFromType() == Long.class)
            return Long.MIN_VALUE;
        else
            return null;
    }

    private final Long defaultMaxValue(Sequence<?> s) {
        if (s.getDataType().getFromType() == Byte.class)
            return (long) Byte.MAX_VALUE;
        else if (s.getDataType().getFromType() == Short.class)
            return (long) Short.MAX_VALUE;
        else if (s.getDataType().getFromType() == Integer.class)
            return (long) Integer.MAX_VALUE;
        else if (s.getDataType().getFromType() == Long.class)
            return Long.MAX_VALUE;
        else
            return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final DiffResult appendDomains(DiffResult result, List<? extends Domain<?>> l1, List<? extends Domain<?>> l2) {
        return append(result, l1, l2, null,
            (r, d) -> r.queries.add(ddl.createDomain(d)),
            (r, d) -> r.queries.add(ctx.dropDomain(d)),
            (r, d1, d2) -> {
                if (!d1.getDataType().getSQLDataType().equals(d2.getDataType().getSQLDataType())) {
                    r.queries.addAll(Arrays.asList(ctx.dropDomain(d1), ddl.createDomain(d2)));
                }
                else {
                    if (d1.getDataType().defaulted() && !d2.getDataType().defaulted())
                        r.queries.add(ctx.alterDomain(d1).dropDefault());
                    else if (d2.getDataType().defaulted() && !d2.getDataType().defaultValue().equals(d1.getDataType().defaultValue()))
                        r.queries.add(ctx.alterDomain(d1).setDefault((Field) d2.getDataType().defaultValue()));

                    appendChecks(r, d1, d1.getChecks(), d2.getChecks());
                }
            }
        );
    }

    private final Create<Table<?>> createTable() {
        return (r, t) -> r.queries.addAll(Arrays.asList(ddl.queries(t).queries()));
    }

    private final Drop<Table<?>> dropTable() {
        return (r, t) -> {
            for (UniqueKey<?> uk : t.getKeys())
                for (ForeignKey<?, ?> fk : uk.getReferences())
                    if (r.droppedFks.add(fk) && !migrateConf.dropTableCascade())
                        r.queries.add(ctx.alterTable(fk.getTable()).dropForeignKey(fk.constraint()));

            if (t.getTableType() == VIEW)
                r.queries.add(ctx.dropView(t));
            else if (t.getTableType() == MATERIALIZED_VIEW)
                r.queries.add(ctx.dropMaterializedView(t));
            else if (t.getTableType() == TableType.TEMPORARY)
                r.queries.add(ctx.dropTemporaryTable(t));
            else
                r.queries.add(migrateConf.dropTableCascade()
                    ? ctx.dropTable(t).cascade()
                    : ctx.dropTable(t));
        };
    }

    private final Merge<Table<?>> MERGE_TABLE = new Merge<Table<?>>() {
        @Override
        public void merge(DiffResult r, Table<?> t1, Table<?> t2) {
            boolean m1 = t1.getTableType() == MATERIALIZED_VIEW;
            boolean m2 = t2.getTableType() == MATERIALIZED_VIEW;
            boolean v1 = t1.getTableType() == VIEW;
            boolean v2 = t2.getTableType() == VIEW;

            if (v1 && v2 || m1 && m2) {
                if (!Arrays.equals(t1.fields(), t2.fields())
                      || t2.getOptions().select() != null && !t2.getOptions().select().equals(t1.getOptions().select())
                      || t2.getOptions().source() != null && !t2.getOptions().source().equals(t1.getOptions().source())) {
                    replaceView(r, t1, t2, true);
                    return;
                }
            }
            else if (v1 != v2 || m1 != m2) {
                replaceView(r, t1, t2, false);
                return;
            }
            else {

                // [#18044] [#18327] Ensure constraint / column drop / add order
                DiffResult temp = new DiffResult(new ArrayList<>(), new ArrayList<>(), r.addedFks, r.droppedFks);

                appendColumns(temp, t1, t2, asList(t1.fields()), asList(t2.fields()));
                appendPrimaryKey(temp, t1, asList(t1.getPrimaryKey()), asList(t2.getPrimaryKey()));
                appendUniqueKeys(temp, t1, removePrimary(t1.getKeys()), removePrimary(t2.getKeys()));
                appendForeignKeys(temp, t1, t1.getReferences(), t2.getReferences());
                appendChecks(temp, t1, t1.getChecks(), t2.getChecks());
                appendIndexes(temp, t1, t1.getIndexes(), t2.getIndexes());

                temp.queries.sort(Diff::sortOrder);
                r.addAll(temp);
            }

            String c1 = defaultString(t1.getComment());
            String c2 = defaultString(t2.getComment());

            if (!c1.equals(c2))
                if (v2)
                    r.queries.add(ctx.commentOnView(t2).is(c2));
                else if (m2)
                    r.queries.add(ctx.commentOnMaterializedView(t2).is(c2));
                else
                    r.queries.add(ctx.commentOnTable(t2).is(c2));
        }

        private void replaceView(DiffResult r, Table<?> v1, Table<?> v2, boolean canReplace) {
            if (!canReplace
                    || v2.getTableType() == VIEW && !migrateConf.createOrReplaceView()
                    || v2.getTableType() == MATERIALIZED_VIEW && !migrateConf.createOrReplaceMaterializedView())
                dropTable().drop(r, v1);

            createTable().create(r, v2);
        }
    };

    private final DiffResult appendTables(DiffResult result, List<? extends Table<?>> l1, List<? extends Table<?>> l2) {
        return append(result, l1, l2, null, createTable(), dropTable(), MERGE_TABLE);
    }

    private final List<UniqueKey<?>> removePrimary(List<? extends UniqueKey<?>> list) {
        List<UniqueKey<?>> result = new ArrayList<>();

        for (UniqueKey<?> uk : list)
            if (!uk.isPrimary())
                result.add(uk);

        return result;
    }

    private final boolean isSynthetic(Field<?> f) {
        switch (ctx.family()) {









        }

        return false;
    }

    private final boolean isSynthetic(UniqueKey<?> pk) {
        switch (ctx.family()) {







        }

        return false;
    }

    private final DiffResult appendColumns(
        DiffResult result,
        Table<?> t1,
        Table<?> t2,
        List<? extends Field<?>> l1,
        List<? extends Field<?>> l2
    ) {
        final List<Field<?>> add = new ArrayList<>();
        final List<Field<?>> drop = new ArrayList<>();

        result = append(result, l1, l2, null,
            (r, f) -> {

                // Ignore synthetic columns
                if (isSynthetic(f))
                    ;
                else if (migrateConf.alterTableAddMultiple())
                    add.add(f);
                else
                    r.queries.add(ctx.alterTable(t1).add(f));
            },

            (r, f) -> {

                // Ignore synthetic columns
                if (isSynthetic(f))
                    ;
                else if (migrateConf.alterTableDropMultiple())
                    drop.add(f);
                else
                    r.queries.add(ctx.alterTable(t1).drop(f));
            },

            new Merge<Field<?>>() {
                @SuppressWarnings({ "unchecked", "rawtypes" })
                @Override
                public void merge(DiffResult r, Field<?> f1, Field<?> f2) {
                    DataType<?> type1 = f1.getDataType();
                    DataType<?> type2 = f2.getDataType();

                    // TODO: Some dialects support changing nullability and types in one statement
                    //       We should produce a single statement as well, and handle derived things
                    //       like nullability through emulations
                    if (typeNameDifference(type1, type2))
                        r.queries.add(ctx.alterTable(t1).alter(f1).set(type2.nullability(Nullability.DEFAULT)));

                    if (type1.nullable() && !type2.nullable() && respectPkNullability(f1, f2))
                        r.queries.add(ctx.alterTable(t1).alter(f1).setNotNull());
                    else if (!type1.nullable() && type2.nullable() && respectPkNullability(f1, f2))
                        r.queries.add(ctx.alterTable(t1).alter(f1).dropNotNull());

                    Field<?> d1 = type1.defaultValue();
                    Field<?> d2 = type2.defaultValue();

                    if (type1.defaulted() && !type2.defaulted())
                        r.queries.add(ctx.alterTable(t1).alter(f1).dropDefault());
                    else if (type2.defaulted() && (!type1.defaulted() || !equivalent(d2, d1)))
                        r.queries.add(ctx.alterTable(t1).alter(f1).setDefault((Field) d2));

                    if (type1.identity() && !type2.identity())
                        r.queries.add(ctx.alterTable(t1).alter(f1).dropIdentity());
                    else if (type2.identity() && !type1.identity())
                        r.queries.add(ctx.alterTable(t1).alter(f1).setGeneratedByDefaultAsIdentity());

                    if ((type1.hasLength() && type2.hasLength() && (type1.lengthDefined() != type2.lengthDefined() || type1.length() != type2.length()))
                        || (type1.hasPrecision() && type2.hasPrecision() && precisionDifference(type1, type2))
                        || (type1.hasScale() && type2.hasScale() && (type1.scaleDefined() != type2.scaleDefined() || type1.scale() != type2.scale())))
                        r.queries.add(ctx.alterTable(t1).alter(f1).set(type2));

                    // [#9656] TODO: Change collation
                    // [#9656] TODO: Change character set
                }

                private final boolean equivalent(Field<?> d2, Field<?> d1) {
                    if (d2.equals(d1))
                        return true;

                    // [#17688] Some expressions can be considered "equivalent," even if not exactly identical.
                    //          CAST('a' AS TEXT) is equivalent to 'a'
                    if (Objects.equals(d2.getDataType().getSQLDataType(), d1.getDataType().getSQLDataType())) {
                        if (d2 instanceof QOM.Cast<?> c)
                            d2 = c.$field();
                        if (d1 instanceof QOM.Cast<?> c)
                            d1 = c.$field();

                        Val<?> v2 = Tools.extractVal(d2);
                        Val<?> v1 = Tools.extractVal(d1);

                        if (v2 != null && v1 != null)
                            return Objects.equals(v2.getValue(), v1.getValue());
                    }

                    return false;
                }

                private final boolean respectPkNullability(Field<?> f1, Field<?> f2) {
                    if (FALSE.equals(ctx.settings().isMigrationIgnoreImplicitPrimaryKeyNotNullConstraints())
                            || SUPPORT_NULLABLE_PRIMARY_KEY.contains(ctx.dialect()))
                        return true;

                    UniqueKey<?> pk1 = t1.getPrimaryKey();
                    UniqueKey<?> pk2 = t2.getPrimaryKey();

                    return pk1 == null
                        || pk2 == null
                        || !pk1.getFields().contains(f1)
                        || !pk2.getFields().contains(f2);
                }

                private final boolean typeNameDifference(DataType<?> type1, DataType<?> type2) {
                    if (type1.getTypeName().equals(type2.getTypeName()))
                        return false;

                    // [#10864] In most dialects, DECIMAL and NUMERIC are aliases and don't need to be changed into each other
                    else
                        return type1.getType() != BigDecimal.class || type2.getType() != BigDecimal.class;
                }

                private final boolean precisionDifference(DataType<?> type1, DataType<?> type2) {

                    // [#10807] Only one type has a default precision defined
                    boolean d1 = defaultPrecision(type1);
                    boolean d2 = defaultPrecision(type2);

                    if (d1 || d2)
                        return d1 != d2;
                    else
                        return type1.precision() != type2.precision();
                }

                private final boolean defaultPrecision(DataType<?> type) {
                    if (!type.isDateTime())
                        return false;

                    if (!type.precisionDefined())
                        return true;

                    if ((type.isTime() || type.isTimeWithTimeZone()) && NO_SUPPORT_TIME_PRECISION.contains(ctx.dialect()))
                        return true;

                    if (!type.isTime() && !type.isTimeWithTimeZone() && NO_SUPPORT_TIMESTAMP_PRECISION.contains(ctx.dialect()))
                        return true;

                    if (FALSE.equals(ctx.settings().isMigrationIgnoreDefaultTimestampPrecisionDiffs()))
                        return false;

                    switch (ctx.family()) {
                        case MARIADB:
                            return type.precision() == 0;

                        // [#10807] TODO: Alternative defaults will be listed here as they are discovered
                        default:
                            return type.precision() == 6;
                    }
                }
            }
        );

        if (!drop.isEmpty())
            result.queries.add(0, ctx.alterTable(t1).drop(drop));

        if (!add.isEmpty())
            result.queries.add(ctx.alterTable(t1).add(add));

        return result;
    }

    private final DiffResult appendPrimaryKey(DiffResult result, final Table<?> t1, List<? extends UniqueKey<?>> pk1, List<? extends UniqueKey<?>> pk2) {
        final Create<UniqueKey<?>> create = (r, pk) -> {
            if (isSynthetic(pk))
                ;
            else
                r.queries.add(ctx.alterTable(t1).add(pk.constraint()));
        };

        final Drop<UniqueKey<?>> drop = (r, pk) -> {
            if (isSynthetic(pk))
                ;
            else if (isEmpty(pk.getName()))
                r.queries.add(ctx.alterTable(t1).dropPrimaryKey());
            else
                r.queries.add(ctx.alterTable(t1).dropPrimaryKey(pk.constraint()));
        };

        return append(result, pk1, pk2, KEY_COMP,
            create,
            drop,
            keyMerge(t1, create, drop, PRIMARY_KEY),
            true
        );
    }

    private final DiffResult appendUniqueKeys(DiffResult result, final Table<?> t1, List<? extends UniqueKey<?>> uk1, List<? extends UniqueKey<?>> uk2) {
        final Create<UniqueKey<?>> create = (r, u) -> r.queries.add(ctx.alterTable(t1).add(u.constraint()));
        final Drop<UniqueKey<?>> drop = (r, u) -> r.queries.add(ctx.alterTable(t1).dropUnique(u.constraint()));

        return append(result, uk1, uk2, KEY_COMP,
            create,
            drop,
            keyMerge(t1, create, drop, UNIQUE),
            true
        );
    }

    private final boolean allowRenames(Function<Settings, Boolean> setting) {
        return
            !FALSE.equals(ctx.settings().isMigrationAllowRename())
         && !FALSE.equals(setting.apply(ctx.settings()));
    }

    private final <K extends Named> Merge<K> keyMerge(Table<?> t1, Create<K> create, Drop<K> drop, ConstraintType type) {
        return (r, k1, k2) -> {
            Name n1 = k1.getUnqualifiedName();
            Name n2 = k2.getUnqualifiedName();
            boolean ignoreRenames = false;
            boolean allowRenames = allowRenames(Settings::isMigrationAllowRenameConstraints);

            if (n1.empty() ^ n2.empty()) {
                if (!TRUE.equals(ctx.settings().isMigrationIgnoreUnnamedConstraintDiffs())) {
                    drop.drop(r, k1);
                    create.create(r, k2);

                    return;
                }
                else
                    allowRenames = !(ignoreRenames = true);
            }

            if (UNQUALIFIED_COMP.compare(k1, k2) != 0) {
                if (allowRenames) {

                    // [#10813] Don't rename constraints in MySQL
                    if (type != PRIMARY_KEY || !NO_SUPPORT_PK_NAMES.contains(ctx.dialect())) {
                        rename(r, type == CHECK ? t1.getChecks() : t1.getKeys(), n1, n2,
                            (_n1, _n2) -> ctx.alterTable(t1).renameConstraint(_n1).to(_n2)
                        );
                    }
                }
                else if (!ignoreRenames) {
                    drop.drop(r, k1);
                    create.create(r, k2);
                }
            }











        };
    }

    private final void rename(
        DiffResult r,
        List<? extends Named> existing,
        Name n1,
        Name n2,
        Function2<? super Name, ? super Name, ? extends Query> renameQuery
    ) {

        // [#18441] Handle name swaps
        // [#18454] TODO: Handle case sensitivity according to dialect
        if (anyMatch(existing, k -> k.getName().equals(n2.last()))) {
            Name temp = unquotedName(autoAlias(ctx.configuration(), n1.append(n2)));

            if (n1.qualified())
                temp = n1.qualifier().append(temp);

            r.queries.add(renameQuery.apply(n1, temp));
            r.cleanup.add(renameQuery.apply(temp, n2));
        }
        else
            r.queries.add(renameQuery.apply(n1, n2));
    }

    private final <K extends Named> Merge<K> keyMerge(Domain<?> d1, Create<K> create, Drop<K> drop) {
        return (r, k1, k2) -> {
            Name n1 = k1.getUnqualifiedName();
            Name n2 = k2.getUnqualifiedName();

            if (n1.empty() ^ n2.empty()) {
                drop.drop(r, k1);
                create.create(r, k2);

                return;
            }

            if (UNQUALIFIED_COMP.compare(k1, k2) != 0)
                r.queries.add(ctx.alterDomain(d1).renameConstraint(n1).to(n2));
        };
    }

    private final DiffResult appendForeignKeys(DiffResult result, final Table<?> t1, List<? extends ForeignKey<?, ?>> fk1, List<? extends ForeignKey<?, ?>> fk2) {
        final Create<ForeignKey<?, ?>> create = (r, fk) -> {
            if (r.addedFks.add(fk))
                r.queries.add(ctx.alterTable(t1).add(fk.constraint()));
        };
        final Drop<ForeignKey<?, ?>> drop = (r, fk) -> {
            if (r.droppedFks.add(fk))
                r.queries.add(ctx.alterTable(t1).dropForeignKey(fk.constraint()));
        };

        return append(result, fk1, fk2, FOREIGN_KEY_COMP,
            create,
            drop,
            keyMerge(t1, create, drop, FOREIGN_KEY),
            true
        );
    }

    private final DiffResult appendChecks(DiffResult result, Table<?> t1, List<? extends Check<?>> c1, List<? extends Check<?>> c2) {
        final Create<Check<?>> create = (r, c) -> r.queries.add(ctx.alterTable(t1).add(c.constraint()));
        final Drop<Check<?>> drop = (r, c) -> r.queries.add(ctx.alterTable(t1).drop(c.constraint()));

        return append(result, c1, c2, CHECK_COMP,
            create,
            drop,
            keyMerge(t1, create, drop, CHECK),
            true
        );
    }

    private final DiffResult appendChecks(DiffResult result, Domain<?> d1, List<? extends Check<?>> c1, List<? extends Check<?>> c2) {
        final Create<Check<?>> create = (r, c) -> r.queries.add(ctx.alterDomain(d1).add(c.constraint()));
        final Drop<Check<?>> drop = (r, c) -> r.queries.add(ctx.alterDomain(d1).dropConstraint(c.constraint()));

        return append(result, c1, c2, CHECK_COMP,
            create,
            drop,
            keyMerge(d1, create, drop),
            true
        );
    }

    private final DiffResult appendIndexes(DiffResult result, Table<?> t1, List<? extends Index> l1, List<? extends Index> l2) {
        final Create<Index> create = (r, i) -> r.queries.add((i.getUnique() ? ctx.createUniqueIndex(i) : ctx.createIndex(i)).on(t1, i.getFields()));
        final Drop<Index> drop = (r, i) -> r.queries.add(ctx.dropIndex(i).on(t1));

        return append(result, l1, l2, INDEX_COMP,
            create,
            drop,
            (r, ix1, ix2) -> {
                if (INDEX_COMP.compare(ix1, ix2) != 0) {
                    drop.drop(r, ix1);
                    create.create(r, ix2);
                }
                else if (UNQUALIFIED_COMP.compare(ix1, ix2) != 0) {
                    if (allowRenames(Settings::isMigrationAllowRenameIndexes)) {
                        rename(r, t1.getIndexes(), ix1.getUnqualifiedName(), ix2.getUnqualifiedName(),
                            (_i1, _i2) -> ctx.alterTable(t1).renameIndex(_i1).to(_i2)
                        );
                    }
                    else {
                        drop.drop(r, ix1);
                        create.create(r, ix2);
                    }
                }
            },
            true
        );
    }

    private final <N extends Named> DiffResult append(
        DiffResult result,
        List<? extends N> l1,
        List<? extends N> l2,
        Comparator<? super N> comp,
        Create<N> create,
        Drop<N> drop,
        Merge<N> merge
    ) {
        return append(result, l1, l2, comp, create, drop, merge, false);
    }

    private final <N extends Named> DiffResult append(
        DiffResult result,
        List<? extends N> l1,
        List<? extends N> l2,
        Comparator<? super N> comp,
        Create<N> create,
        Drop<N> drop,
        Merge<N> merge,
        boolean dropMergeCreate
    ) {
        if (comp == null)
            comp = NAMED_COMP;

        N s1 = null;
        N s2 = null;

        Iterator<? extends N> i1 = sorted(l1, comp);
        Iterator<? extends N> i2 = sorted(l2, comp);

        DiffResult dropped = dropMergeCreate ? new DiffResult(new ArrayList<>(), new ArrayList<>(), result.addedFks, result.droppedFks) : result;
        DiffResult merged = dropMergeCreate ? new DiffResult(new ArrayList<>(), new ArrayList<>(), result.addedFks, result.droppedFks) : result;
        DiffResult created = dropMergeCreate ? new DiffResult(new ArrayList<>(), new ArrayList<>(), result.addedFks, result.droppedFks) : result;

        for (;;) {
            if (s1 == null && i1.hasNext())
                s1 = i1.next();

            if (s2 == null && i2.hasNext())
                s2 = i2.next();

            if (s1 == null && s2 == null)
                break;

            int c = s1 == null
                  ? 1
                  : s2 == null
                  ? -1
                  : comp.compare(s1, s2);

            if (c < 0) {
                if (drop != null)
                    drop.drop(dropped, s1);

                s1 = null;
            }
            else if (c > 0) {
                if (create != null)
                    create.create(created, s2);

                s2 = null;
            }
            else {
                if (merge != null)
                    merge.merge(merged, s1, s2);

                s1 = s2 = null;
            }
        }

        if (dropMergeCreate) {
            result.addAll(dropped);
            result.addAll(merged);
            result.addAll(created);
        }

        result.queries.sort(Diff::sortOrder);
        return result;
    }

    private static interface Create<N extends Named> {
        void create(DiffResult result, N named);
    }

    private static interface Drop<N extends Named> {
        void drop(DiffResult result, N named);
    }

    private static interface Merge<N extends Named> {
        void merge(DiffResult result, N named1, N named2);
    }

    private static final <N extends Named> Iterator<N> sorted(List<N> list, Comparator<? super N> comp) {
        List<N> result = new ArrayList<>(list);
        result.sort(comp);
        return result.iterator();
    }

    private static final record DiffResult(
        List<Query> queries,
        List<Query> cleanup,
        Set<ForeignKey<?, ?>> addedFks,
        Set<ForeignKey<?, ?>> droppedFks
    ) {
        DiffResult() {
            this(new ArrayList<>(), new ArrayList<>(), new HashSet<>(), new HashSet<>());
        }

        void addAll(DiffResult other) {
            queries.addAll(other.queries);
            queries.addAll(other.cleanup);
            addedFks.addAll(other.addedFks);
            droppedFks.addAll(other.droppedFks);
        }

        @Override
        public String toString() {
            return Tools.concat(queries, cleanup).toString();
        }
    }

    static final int sortOrder(Query q1, Query q2) {
        return sortIndex(q1) - sortIndex(q2);
    }

    static final int sortIndex(Query q) {
        final int VIEW = 5;
        final int FKEY = 4;
        final int CONS = 3;
        final int NULL = 2;
        final int COL = 1;

        if (q instanceof AlterTableImpl a) {
            return

                // [#18383] FOREIGN KEY must be dropped before other constraints, or added after other constraints
                  a.$dropConstraint() instanceof QOM.ForeignKey || a.$dropConstraintType() == FOREIGN_KEY
                ? -FKEY
                : a.$addConstraint() instanceof QOM.ForeignKey || a.$dropConstraintType() == FOREIGN_KEY
                ? FKEY

                // [#18044] DROP CONSTRAINT / INDEX before everything, ADD CONSTRAINT / INDEX after everything
                : a.$dropConstraint() != null || a.$dropConstraintType() != null
                ? -CONS
                : a.$addConstraint() != null
                ? CONS

                // [#18450] DROP NOT NULL must happen after dropping constraints, SET NOT NULL before adding constraints
                : a.$alterColumnNullability() == Nullability.NULL
                ? -NULL
                : a.$alterColumnNullability() == Nullability.NOT_NULL
                ? NULL

                // [#18462] To prevent tables from being without columns, adding columns must happen before dropping them
                : a.$addColumn() != null || anyMatch(a.$add(), c -> c instanceof Field)
                ? -COL
                : !isEmpty(a.$dropColumns())
                ? COL

                : 0;
        }
        else if (q instanceof QOM.DropIndex)
            return -CONS;
        else if (q instanceof QOM.CreateIndex)
            return CONS;
        else if (q instanceof QOM.DropView)
            return -VIEW;
        else if (q instanceof QOM.CreateView)
            return VIEW;
        else
            return 0;
    }
}
