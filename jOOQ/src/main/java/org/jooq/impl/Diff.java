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

import static java.util.Arrays.asList;
import static org.jooq.impl.Comparators.CHECK_COMP;
import static org.jooq.impl.Comparators.FOREIGN_KEY_COMP;
import static org.jooq.impl.Comparators.INDEX_COMP;
import static org.jooq.impl.Comparators.KEY_COMP;
import static org.jooq.impl.Comparators.NAMED_COMP;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.defaultString;
import static org.jooq.tools.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jooq.AlterSequenceFlagsStep;
import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Configuration;
import org.jooq.DDLExportConfiguration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Key;
import org.jooq.Meta;
import org.jooq.MigrationConfiguration;
import org.jooq.Name;
import org.jooq.Named;
import org.jooq.Nullability;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableOptions.TableType;
import org.jooq.UniqueKey;

/**
 * A class producing a diff between two {@link Meta} objects.
 *
 * @author Lukas Eder
 */
final class Diff {

    private final MigrationConfiguration migrateConf;
    private final DDLExportConfiguration exportConf;
    private final DSLContext             ctx;
    private final Meta                   meta1;
    private final Meta                   meta2;
    private final DDL                    ddl;

    Diff(Configuration configuration, MigrationConfiguration migrateConf, Meta meta1, Meta meta2) {
        this.migrateConf = migrateConf;
        this.exportConf = new DDLExportConfiguration().createOrReplaceView(migrateConf.createOrReplaceView());
        this.ctx = configuration.dsl();
        this.meta1 = meta1;
        this.meta2 = meta2;
        this.ddl = new DDL(ctx, exportConf);
    }

    final Queries queries() {
        return ctx.queries(appendCatalogs(new DiffResult(), meta1.getCatalogs(), meta2.getCatalogs()).queries);
    }

    private final DiffResult appendCatalogs(DiffResult result, List<Catalog> l1, List<Catalog> l2) {
        return append(result, l1, l2, null,

            // TODO Implement this for SQL Server support.
            null,

            // TODO Implement this for SQL Server support.
            null,

            new Merge<Catalog>() {
                @Override
                public void merge(DiffResult r, Catalog c1, Catalog c2) {
                    appendSchemas(r, c1.getSchemas(), c2.getSchemas());
                }
            }
        );
    }

    private final Create<Schema> CREATE_SCHEMA = new Create<Schema>() {
        @Override
        public void create(DiffResult r, Schema s) {
            r.queries.addAll(Arrays.asList(ctx.ddl(s).queries()));
        }
    };

    private final Drop<Schema> DROP_SCHEMA = new Drop<Schema>() {
        @Override
        public void drop(DiffResult r, Schema s) {
            if (s.getTables().isEmpty() && s.getSequences().isEmpty()) {
                r.queries.add(ctx.dropSchema(s));
            }
            else if (migrateConf.dropSchemaCascade()) {

                // TODO: Can we reuse the logic from DROP_TABLE?
                for (Table<?> t : s.getTables())
                    for (UniqueKey<?> uk : t.getKeys())
                        for (ForeignKey<?, ?> fk : uk.getReferences())
                            r.droppedFks.add(fk);

                r.queries.add(ctx.dropSchema(s).cascade());
            }
            else {
                for (Table<?> t : s.getTables())
                    DROP_TABLE.drop(r, t);

                for (Sequence<?> seq : s.getSequences())
                    DROP_SEQUENCE.drop(r, seq);

                r.queries.add(ctx.dropSchema(s));
            }
        }
    };

    private final Merge<Schema> MERGE_SCHEMA = new Merge<Schema>() {
        @Override
        public void merge(DiffResult r, Schema s1, Schema s2) {
            appendTables(r, s1.getTables(), s2.getTables());
            appendSequences(r, s1.getSequences(), s2.getSequences());
        }
    };

    private final DiffResult appendSchemas(DiffResult result, List<Schema> l1, List<Schema> l2) {
        return append(result, l1, l2, null,
            CREATE_SCHEMA,
            DROP_SCHEMA,
            MERGE_SCHEMA
        );
    }

    private final Create<Sequence<?>> CREATE_SEQUENCE = new Create<Sequence<?>>() {
        @Override
        public void create(DiffResult r, Sequence<?> s) {
            r.queries.add(ddl.createSequence(s));
        }
    };

    private final Drop<Sequence<?>> DROP_SEQUENCE = new Drop<Sequence<?>>() {
        @Override
        public void drop(DiffResult r, Sequence<?> s) {
            r.queries.add(ctx.dropSequence(s));
        }
    };

    private final Merge<Sequence<?>> MERGE_SEQUENCE = new Merge<Sequence<?>>() {
        @Override
        public void merge(DiffResult r, Sequence<?> s1, Sequence<?> s2) {
            AlterSequenceFlagsStep stmt = null;
            AlterSequenceFlagsStep stmt0 = ctx.alterSequence(s1);

            if (s2.getStartWith() != null && !s2.getStartWith().equals(s1.getStartWith()))
                stmt = defaultIfNull(stmt, stmt0).startWith(s2.getStartWith());
            else if (s2.getStartWith() == null && s1.getStartWith() != null)
                stmt = defaultIfNull(stmt, stmt0).startWith(1);

            if (s2.getIncrementBy() != null && !s2.getIncrementBy().equals(s1.getIncrementBy()))
                stmt = defaultIfNull(stmt, stmt0).incrementBy(s2.getIncrementBy());
            else if (s2.getIncrementBy() == null && s1.getIncrementBy() != null)
                stmt = defaultIfNull(stmt, stmt0).incrementBy(1);

            if (s2.getMinvalue() != null && !s2.getMinvalue().equals(s1.getMinvalue()))
                stmt = defaultIfNull(stmt, stmt0).minvalue(s2.getMinvalue());
            else if (s2.getMinvalue() == null && s1.getMinvalue() != null)
                stmt = defaultIfNull(stmt, stmt0).noMinvalue();

            if (s2.getMaxvalue() != null && !s2.getMaxvalue().equals(s1.getMaxvalue()))
                stmt = defaultIfNull(stmt, stmt0).maxvalue(s2.getMaxvalue());
            else if (s2.getMaxvalue() == null && s1.getMaxvalue() != null)
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
    };

    private final DiffResult appendSequences(DiffResult result, List<? extends Sequence<?>> l1, List<? extends Sequence<?>> l2) {
        return append(result, l1, l2, null, CREATE_SEQUENCE, DROP_SEQUENCE, MERGE_SEQUENCE);
    }

    private final Create<Table<?>> CREATE_TABLE = new Create<Table<?>>() {
        @Override
        public void create(DiffResult r, Table<?> t) {
            r.queries.addAll(Arrays.asList(ctx.ddl(t, exportConf).queries()));
        }
    };

    private final Drop<Table<?>> DROP_TABLE = new Drop<Table<?>>() {
        @Override
        public void drop(DiffResult r, Table<?> t) {
            for (UniqueKey<?> uk : t.getKeys())
                for (ForeignKey<?, ?> fk : uk.getReferences())
                    if (r.droppedFks.add(fk) && !migrateConf.dropTableCascade())
                        r.queries.add(ctx.alterTable(fk.getTable()).dropForeignKey(fk.constraint()));

            if (t.getType().isView())
                r.queries.add(ctx.dropView(t));
            else if (t.getType() == TableType.TEMPORARY)
                r.queries.add(ctx.dropTemporaryTable(t));
            else
                r.queries.add(migrateConf.dropTableCascade()
                    ? ctx.dropTable(t).cascade()
                    : ctx.dropTable(t));
        }
    };

    private final Merge<Table<?>> MERGE_TABLE = new Merge<Table<?>>() {
        @Override
        public void merge(DiffResult r, Table<?> t1, Table<?> t2) {
            boolean v1 = t1.getType().isView();
            boolean v2 = t2.getType().isView();

            if (v1 && v2) {
                if (!Arrays.equals(t1.fields(), t2.fields())
                    || !t1.getOptions().select().equals(t2.getOptions().select())) {
                    replaceView(r, t1, t2);
                    return;
                }
            }
            else if (v1 != v2) {
                replaceView(r, t1, t2);
                return;
            }
            else {

                // TODO: The order of dropping / adding these objects might be incorrect
                //       as there could be inter-dependencies.
                appendColumns(r, t1, asList(t1.fields()), asList(t2.fields()));
                appendPrimaryKey(r, t1, asList(t1.getPrimaryKey()), asList(t2.getPrimaryKey()));
                appendUniqueKeys(r, t1, removePrimary(t1.getKeys()), removePrimary(t2.getKeys()));
                appendForeignKeys(r, t1, t1.getReferences(), t2.getReferences());
                appendChecks(r, t1, t1.getChecks(), t2.getChecks());
                appendIndexes(r, t1, t1.getIndexes(), t2.getIndexes());
            }

            String c1 = defaultString(t1.getComment());
            String c2 = defaultString(t2.getComment());

            if (!c1.equals(c2))
                if (v2)
                    r.queries.add(ctx.commentOnView(t2).is(c2));
                else
                    r.queries.add(ctx.commentOnTable(t2).is(c2));
        }

        private void replaceView(DiffResult r, Table<?> v1, Table<?> v2) {
            if (!migrateConf.createOrReplaceView())
                DROP_TABLE.drop(r, v1);

            CREATE_TABLE.create(r, v2);
        }
    };



    private final DiffResult appendTables(DiffResult result, List<? extends Table<?>> l1, List<? extends Table<?>> l2) {
        return append(result, l1, l2, null, CREATE_TABLE, DROP_TABLE, MERGE_TABLE);
    }

    private final List<UniqueKey<?>> removePrimary(List<? extends UniqueKey<?>> list) {
        List<UniqueKey<?>> result = new ArrayList<>();

        for (UniqueKey<?> uk : list)
            if (!uk.isPrimary())
                result.add(uk);

        return result;
    }

    private final DiffResult appendColumns(DiffResult result, final Table<?> t1, List<? extends Field<?>> l1, List<? extends Field<?>> l2) {
        final List<Field<?>> add = new ArrayList<>();
        final List<Field<?>> drop = new ArrayList<>();

        result = append(result, l1, l2, null,
            new Create<Field<?>>() {
                @Override
                public void create(DiffResult r, Field<?> f) {
                    if (migrateConf.alterTableAddMultiple())
                        add.add(f);
                    else
                        r.queries.add(ctx.alterTable(t1).add(f));
                }
            },

            new Drop<Field<?>>() {
                @Override
                public void drop(DiffResult r, Field<?> f) {
                    if (migrateConf.alterTableDropMultiple())
                        drop.add(f);
                    else
                        r.queries.add(ctx.alterTable(t1).drop(f));
                }
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
                    if (!type1.getTypeName().equals(type2.getTypeName()))
                        r.queries.add(ctx.alterTable(t1).alter(f1).set(type2.nullability(Nullability.DEFAULT)));

                    if (type1.nullable() && !type2.nullable())
                        r.queries.add(ctx.alterTable(t1).alter(f1).setNotNull());
                    else if (!type1.nullable() && type2.nullable())
                        r.queries.add(ctx.alterTable(t1).alter(f1).dropNotNull());

                    Field<?> d1 = type1.defaultValue();
                    Field<?> d2 = type2.defaultValue();

                    if (type1.defaulted() && !type2.defaulted())
                        r.queries.add(ctx.alterTable(t1).alter(f1).dropDefault());
                    else if (type2.defaulted() && (!type1.defaulted() || !d2.equals(d1)))
                        r.queries.add(ctx.alterTable(t1).alter(f1).setDefault((Field) d2));

                    if ((type1.hasLength() && type2.hasLength() && (type1.lengthDefined() != type2.lengthDefined() || type1.length() != type2.length()))
                        || (type1.hasPrecision() && type2.hasPrecision() && (type1.precisionDefined() != type2.precisionDefined() || type1.precision() != type2.precision()))
                        || (type1.hasScale() && type2.hasScale() && (type1.scaleDefined() != type2.scaleDefined() || type1.scale() != type2.scale())))
                        r.queries.add(ctx.alterTable(t1).alter(f1).set(type2));

                    // [#9656] TODO: Change collation
                    // [#9656] TODO: Change character set
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
        final Create<UniqueKey<?>> create = new Create<UniqueKey<?>>() {
            @Override
            public void create(DiffResult r, UniqueKey<?> pk) {
                r.queries.add(ctx.alterTable(t1).add(pk.constraint()));
            }
        };

        final Drop<UniqueKey<?>> drop = new Drop<UniqueKey<?>>() {
            @Override
            public void drop(DiffResult r, UniqueKey<?> pk) {
                if (isEmpty(pk.getName()))
                    r.queries.add(ctx.alterTable(t1).dropPrimaryKey());
                else
                    r.queries.add(ctx.alterTable(t1).dropPrimaryKey(pk.constraint()));
            }
        };

        return append(result, pk1, pk2, KEY_COMP,
            create,
            drop,
            keyMerge(t1, create, drop),
            true
        );
    }

    private final DiffResult appendUniqueKeys(DiffResult result, final Table<?> t1, List<? extends UniqueKey<?>> uk1, List<? extends UniqueKey<?>> uk2) {
        final Create<UniqueKey<?>> create = new Create<UniqueKey<?>>() {
            @Override
            public void create(DiffResult r, UniqueKey<?> u) {
                r.queries.add(ctx.alterTable(t1).add(u.constraint()));
            }
        };

        final Drop<UniqueKey<?>> drop = new Drop<UniqueKey<?>>() {
            @Override
            public void drop(DiffResult r, UniqueKey<?> u) {
                r.queries.add(ctx.alterTable(t1).dropUnique(u.constraint()));
            }
        };

        return append(result, uk1, uk2, KEY_COMP,
            create,
            drop,
            keyMerge(t1, create, drop),
            true
        );
    }

    private <K extends Named> Merge<K> keyMerge(final Table<?> t1, final Create<K> create, final Drop<K> drop) {
        return new Merge<K>() {
            @Override
            public void merge(DiffResult r, K k1, K k2) {
                Name n1 = k1.getUnqualifiedName();
                Name n2 = k2.getUnqualifiedName();

                if (n1.empty() ^ n2.empty()) {
                    drop.drop(r, k1);
                    create.create(r, k2);

                    return;
                }

                if (NAMED_COMP.compare(k1, k2) != 0)
                    r.queries.add(ctx.alterTable(t1).renameConstraint(n1).to(n2));











            }
        };
    }

    private final DiffResult appendForeignKeys(DiffResult result, final Table<?> t1, List<? extends ForeignKey<?, ?>> fk1, List<? extends ForeignKey<?, ?>> fk2) {
        final Create<ForeignKey<?, ?>> create = new Create<ForeignKey<?, ?>>() {
            @Override
            public void create(DiffResult r, ForeignKey<?, ?> fk) {
                r.queries.add(ctx.alterTable(t1).add(fk.constraint()));
            }
        };

        final Drop<ForeignKey<?, ?>> drop = new Drop<ForeignKey<?, ?>>() {
            @Override
            public void drop(DiffResult r, ForeignKey<?, ?> fk) {
                if (r.droppedFks.add(fk))
                    r.queries.add(ctx.alterTable(t1).dropForeignKey(fk.constraint()));
            }
        };

        return append(result, fk1, fk2, FOREIGN_KEY_COMP,
            create,
            drop,
            keyMerge(t1, create, drop),
            true
        );
    }

    private final DiffResult appendChecks(DiffResult result, final Table<?> t1, List<? extends Check<?>> c1, List<? extends Check<?>> c2) {
        final Create<Check<?>> create = new Create<Check<?>>() {
            @Override
            public void create(DiffResult r, Check<?> c) {
                r.queries.add(ctx.alterTable(t1).add(c.constraint()));
            }
        };

        final Drop<Check<?>> drop = new Drop<Check<?>>() {
            @Override
            public void drop(DiffResult r, Check<?> c) {
                r.queries.add(ctx.alterTable(t1).drop(c.constraint()));
            }
        };

        return append(result, c1, c2, CHECK_COMP,
            create,
            drop,
            keyMerge(t1, create, drop),
            true
        );
    }

    private final DiffResult appendIndexes(DiffResult result, final Table<?> t1, List<? extends Index> l1, List<? extends Index> l2) {
        final Create<Index> create = new Create<Index>() {
            @Override
            public void create(DiffResult r, Index i) {
                r.queries.add(ctx.createIndex(i).on(t1, i.getFields()));
            }
        };
        final Drop<Index> drop = new Drop<Index>() {
            @Override
            public void drop(DiffResult r, Index i) {
                r.queries.add(ctx.dropIndex(i).on(t1));
            }
        };

        return append(result, l1, l2, INDEX_COMP,
            create,
            drop,
            new Merge<Index>() {
                @Override
                public void merge(DiffResult r, Index ix1, Index ix2) {
                    if (ix1.getUnique() != ix2.getUnique()
                            || !ix1.getFields().equals(ix2.getFields())
                            || !defaultIfNull(ix1.getWhere(), noCondition()).equals(defaultIfNull(ix2.getWhere(), noCondition()))) {
                        drop.drop(r, ix1);
                        create.create(r, ix2);
                    }
                    else if (NAMED_COMP.compare(ix1, ix2) != 0)
                        r.queries.add(ctx.alterTable(t1).renameIndex(ix1).to(ix2));
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

        DiffResult dropped = dropMergeCreate ? new DiffResult(new ArrayList<>(), result.droppedFks) : result;
        DiffResult merged = dropMergeCreate ? new DiffResult(new ArrayList<>(), result.droppedFks) : result;
        DiffResult created = dropMergeCreate ? new DiffResult(new ArrayList<>(), result.droppedFks) : result;

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
        Collections.sort(result, comp);
        return result.iterator();
    }

    private static final class DiffResult {
        final List<Query>           queries;
        final Set<ForeignKey<?, ?>> droppedFks;

        DiffResult() {
            this(new ArrayList<>(), new HashSet<>());
        }

        DiffResult(List<Query> queries, Set<ForeignKey<?, ?>> droppedFks) {
            this.queries = queries;
            this.droppedFks = droppedFks;
        }

        void addAll(DiffResult other) {
            queries.addAll(other.queries);
            droppedFks.addAll(other.droppedFks);
        }

        @Override
        public String toString() {
            return queries.toString();
        }
    }
}
