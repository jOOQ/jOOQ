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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Meta;
import org.jooq.Named;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Schema;
import org.jooq.Table;

/**
 * A class producing a diff between two {@link Meta} objects.
 *
 * @author Lukas Eder
 */
final class Diff {

    private static final NamedComparator COMP = new NamedComparator();
    private final DSLContext             ctx;
    private final Meta                   meta1;
    private final Meta                   meta2;

    Diff(Configuration configuration, Meta meta1, Meta meta2) {
        this.ctx = configuration.dsl();
        this.meta1 = meta1;
        this.meta2 = meta2;
    }

    final Queries queries() {
        return ctx.queries(appendCatalogs(new ArrayList<>(), sorted(meta1.getCatalogs()), sorted(meta2.getCatalogs())));
    }

    private final List<Query> appendCatalogs(final List<Query> queries, final Iterator<Catalog> i1, final Iterator<Catalog> i2) {
        return append(queries, i1, i2,
            null,
            null,
            new Merge<Catalog>() {
                @Override
                public void merge(List<Query> q, Catalog c1, Catalog c2) {
                    appendSchemas(q, sorted(c1.getSchemas()), sorted(c2.getSchemas()));
                }
            }
        );
    }

    private final List<Query> appendSchemas(final List<Query> queries, final Iterator<Schema> i1, final Iterator<Schema> i2) {
        // TODO Cascade semantics when creating and deleting
        return append(queries, i1, i2,
            new Create<Schema>() {
                @Override
                public void create(List<Query> q, Schema s) {
                    q.add(ctx.createSchema(s));
                }
            },
            new Drop<Schema>() {
                @Override
                public void drop(List<Query> q, Schema s) {
                    q.add(ctx.dropSchema(s));
                }
            },
            new Merge<Schema>() {
                @Override
                public void merge(List<Query> q, Schema s1, Schema s2) {
                    appendTables(q, sorted(s1.getTables()), sorted(s2.getTables()));
                }
            }
        );
    }

    private final List<Query> appendTables(final List<Query> queries, final Iterator<Table<?>> i1, final Iterator<Table<?>> i2) {
        // TODO Cascade semantics when creating and deleting
        return append(queries, i1, i2,
            new Create<Table<?>>() {
                @Override
                public void create(List<Query> q, Table<?> t) {
                    q.addAll(Arrays.asList(ctx.ddl(t).queries()));
                }
            },
            new Drop<Table<?>>() {
                @Override
                public void drop(List<Query> q, Table<?> t) {
                    q.add(ctx.dropTable(t));
                }
            },
            new Merge<Table<?>>() {
                @Override
                public void merge(List<Query> q, Table<?> t1, Table<?> t2) {
                    appendColumns(queries, t1, t2, sorted(t1.fields()), sorted(t2.fields()));
                }
            }
        );
    }

    private final List<Query> appendColumns(final List<Query> queries, final Table<?> t1, final Table<?> t2, final Iterator<Field<?>> i1, final Iterator<Field<?>> i2) {
        return append(queries, i1, i2,
            new Create<Field<?>>() {
                @Override
                public void create(List<Query> q, Field<?> f) {
                    q.add(ctx.alterTable(t1).add(f));
                }
            },
            new Drop<Field<?>>() {
                @Override
                public void drop(List<Query> q, Field<?> f) {
                    q.add(ctx.alterTable(t1).drop(f));
                }
            },
            null
        );
    }

    private final <N extends Named> List<Query> append(
        List<Query> queries,
        Iterator<N> i1,
        Iterator<N> i2,
        Create<N> create,
        Drop<N> drop,
        Merge<N> merge
    ) {
        N s1 = null;
        N s2 = null;

        for (;;) {
            if (s1 == null && i1.hasNext())
                s1 = i1.next();

            if (s2 == null && i2.hasNext())
                s2 = i2.next();

            if (s1 == null && s2 == null)
                break;

            int comp = s1 == null
                     ? 1
                     : s2 == null
                     ? -1
                     : s1.getQualifiedName().compareTo(s2.getQualifiedName());

            if (comp < 0) {
                if (drop != null)
                    drop.drop(queries, s1);

                s1 = null;
            }
            else if (comp > 0) {
                if (create != null)
                    create.create(queries, s2);

                s2 = null;
            }
            else {
                if (merge != null)
                    merge.merge(queries, s1, s2);

                s1 = s2 = null;
            }
        }

        return queries;
    }

    private static interface Create<N extends Named> {
        void create(List<Query> queries, N named);
    }

    private static interface Drop<N extends Named> {
        void drop(List<Query> queries, N named);
    }

    private static interface Merge<N extends Named> {
        void merge(List<Query> queries, N named1, N named2);
    }

    private static final <N extends Named> Iterator<N> sorted(N... array) {
        List<N> result = Arrays.asList(array);
        Collections.sort(result, COMP);
        return result.iterator();
    }

    private static final <N extends Named> Iterator<N> sorted(List<N> list) {
        List<N> result = new ArrayList<>(list);
        Collections.sort(result, COMP);
        return result.iterator();
    }

    private static final class NamedComparator implements Comparator<Named> {
        @Override
        public int compare(Named o1, Named o2) {
            return o1.getQualifiedName().compareTo(o2.getQualifiedName());
        }
    }
}
