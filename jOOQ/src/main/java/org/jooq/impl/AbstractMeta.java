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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Tools.flatMap;
import static org.jooq.impl.Tools.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.DDLExportConfiguration;
import org.jooq.Domain;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Named;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.util.xml.jaxb.InformationSchema;

/**
 * @author Lukas Eder
 */
abstract class AbstractMeta extends AbstractScope implements Meta, Serializable {

    // [#9010] TODO: Allow for opting out of this cache
    private Map<Name, Catalog>                cachedCatalogs;
    private Cached<Schema>                    cachedSchemas;
    private Cached<Table<?>>                  cachedTables;
    private Cached<Domain<?>>                 cachedDomains;
    private Cached<Sequence<?>>               cachedSequences;
    private Cached<UniqueKey<?>>              cachedPrimaryKeys;
    private Cached<UniqueKey<?>>              cachedUniqueKeys;
    private Cached<ForeignKey<?, ?>>          cachedForeignKeys;
    private Cached<Index>                     cachedIndexes;
    final Predicate<? super Catalog>          catalogFilter;
    final Predicate<? super Schema>           schemaFilter;

    AbstractMeta(Configuration configuration) {
        this(configuration, null, null);
    }

    AbstractMeta(Configuration configuration, Predicate<? super Catalog> catalogFilter, Predicate<? super Schema> schemaFilter) {
        super(configuration);

        this.catalogFilter = catalogFilter;
        this.schemaFilter = schemaFilter;
    }

    abstract AbstractMeta filtered0(Predicate<? super Catalog> catalogFilter, Predicate<? super Schema> schemaFilter);

    @Override
    public final Catalog getCatalog(String name) {
        return getCatalog(name(name));
    }

    @Override
    public final Catalog getCatalog(Name name) {
        return getCachedCatalogs().get(name);
    }

    @Override
    public final List<Catalog> getCatalogs() {
        return Collections.unmodifiableList(new ArrayList<>(getCachedCatalogs().values()));
    }

    private final Map<Name, Catalog> getCachedCatalogs() {
        Map<Name, Catalog> c = cachedCatalogs;

        if (c == null) {
            c = new LinkedHashMap<>();

            for (Catalog catalog : getCatalogs0())
                c.put(catalog.getQualifiedName(), catalog);
        }

        if (caching())
            cachedCatalogs = c;

        return c;
    }

    abstract List<Catalog> getCatalogs0();

    private static final record Cached<N extends Named>(Map<Name, N> qualified, Map<Name, List<N>> partiallyQualified, Map<Name, List<N>> unqualified) {
        Cached() {
            this(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
        }

        final void init(Iterable<N> i) {
            if (qualified().isEmpty()) {
                for (N object : i) {
                    Name q = object.getQualifiedName();
                    Name u = object.getUnqualifiedName();

                    qualified().put(q, object);
                    unqualified().computeIfAbsent(u, n -> new ArrayList<>()).add(object);

                    if (q.qualified() && q.qualifierQualified())
                        partiallyQualified().computeIfAbsent(q.qualifier().unqualifiedName().append(u), n -> new ArrayList<>()).add(object);
                }
            }
        }

        final List<N> get(Name name) {
            N object = qualified().get(name);
            if (object != null)
                return Collections.singletonList(object);

            List<N> list;

            list = partiallyQualified().get(name);
            if (list != null)
                return Collections.unmodifiableList(list);

            list = unqualified().get(name);
            if (list == null)
                return Collections.emptyList();
            else
                return Collections.unmodifiableList(list);
        }
    }

    @Override
    public final List<Schema> getSchemas(String name) {
        return getSchemas(name(name));
    }

    @Override
    public final List<Schema> getSchemas(Name name) {
        return getCachedSchemas().get(name);
    }

    @Override
    public final List<Schema> getSchemas() {
        return Collections.unmodifiableList(new ArrayList<>(getCachedSchemas().qualified().values()));
    }

    private final Cached<Schema> getCachedSchemas() {
        Cached<Schema> s = cachedSchemas;

        if (s == null) {
            s = new Cached<>();
            s.init(schemaFilter != null
                ? () -> Tools.filter(getSchemas0().iterator(), schemaFilter)
                : () -> getSchemas0().iterator()
            );
        }

        if (caching())
            cachedSchemas = s;

        return s;
    }

    List<Schema> getSchemas0() {
        return flatMap(getCatalogs(), c -> c.getSchemas());
    }

    @Override
    public final List<Table<?>> getTables(String name) {
        return getTables(name(name));
    }

    @Override
    public final List<Table<?>> getTables(Name name) {
        return getCachedTables().get(name);
    }

    @Override
    public final List<Table<?>> getTables() {
        return Collections.unmodifiableList(new ArrayList<>(getCachedTables().qualified().values()));
    }

    private final Cached<Table<?>> getCachedTables() {
        Cached<Table<?>> t = cachedTables;

        if (t == null) {
            t = new Cached<>();
            t.init(() -> getTables0().iterator());
        }

        if (caching())
            cachedTables = t;

        return t;
    }

    List<Table<?>> getTables0() {
        return flatMap(getSchemas(), s -> s.getTables());
    }

    @Override
    public final List<Domain<?>> getDomains(String name) {
        return getDomains(name(name));
    }

    @Override
    public final List<Domain<?>> getDomains(Name name) {
        return getCachedDomains().get(name);
    }

    @Override
    public final List<Domain<?>> getDomains() {
        getCachedDomains();
        return Collections.unmodifiableList(new ArrayList<>(getCachedDomains().qualified().values()));
    }

    private final Cached<Domain<?>> getCachedDomains() {
        Cached<Domain<?>> d = cachedDomains;

        if (d == null) {
            d = new Cached<>();
            d.init(() -> getDomains0().iterator());
        }

        if (caching())
            cachedDomains = d;

        return d;
    }

    List<Domain<?>> getDomains0() {
        return flatMap(getSchemas(), s -> s.getDomains());
    }

    @Override
    public final List<Sequence<?>> getSequences(String name) {
        return getSequences(name(name));
    }

    @Override
    public final List<Sequence<?>> getSequences(Name name) {
        return getCachedSequences().get(name);
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return Collections.unmodifiableList(new ArrayList<>(getCachedSequences().qualified().values()));
    }

    private final Cached<Sequence<?>> getCachedSequences() {
        Cached<Sequence<?>> s = cachedSequences;

        if (s == null) {
            s = new Cached<>();
            s.init(() -> getSequences0().iterator());
        }

        if (caching())
            cachedSequences = s;

        return s;
    }

    final List<Sequence<?>> getSequences0() {
        return flatMap(getSchemas(), s -> s.getSequences());
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys(String name) {
        return getPrimaryKeys(name(name));
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys(Name name) {
        return getCachedPrimaryKeys().get(name);
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys() {
        return Collections.unmodifiableList(new ArrayList<>(getCachedPrimaryKeys().qualified().values()));
    }

    private final Cached<UniqueKey<?>> getCachedPrimaryKeys() {
        Cached<UniqueKey<?>> k = cachedPrimaryKeys;

        if (k == null) {
            k = new Cached<>();
            k.init(() -> getPrimaryKeys0().iterator());
        }

        if (caching())
            cachedPrimaryKeys = k;

        return k;
    }

    List<UniqueKey<?>> getPrimaryKeys0() {
        List<UniqueKey<?>> result = new ArrayList<>();

        for (Table<?> table : getTables())
            if (table.getPrimaryKey() != null)
                result.add(table.getPrimaryKey());

        return result;
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys(String name) {
        return getUniqueKeys(name(name));
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys(Name name) {
        return getCachedUniqueKeys().get(name);
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys() {
        return Collections.unmodifiableList(new ArrayList<>(getCachedUniqueKeys().qualified().values()));
    }

    private final Cached<UniqueKey<?>> getCachedUniqueKeys() {
        Cached<UniqueKey<?>> k = cachedUniqueKeys;

        if (k == null) {
            k = new Cached<>();
            k.init(() -> getUniqueKeys0().iterator());
        }

        if (caching())
            cachedUniqueKeys = k;

        return k;
    }

    List<UniqueKey<?>> getUniqueKeys0() {
        return flatMap(getTables(), t -> t.getUniqueKeys());
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys(String name) {
        return getForeignKeys(name(name));
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys(Name name) {
        return getCachedForeignKeys().get(name);
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys() {
        return Collections.unmodifiableList(new ArrayList<>(getCachedForeignKeys().qualified().values()));
    }

    private final Cached<ForeignKey<?, ?>> getCachedForeignKeys() {
        Cached<ForeignKey<?, ?>> k = cachedForeignKeys;

        if (k == null) {
            k = new Cached<>();
            k.init(() -> getForeignKeys0().iterator());
        }

        if (caching())
            cachedForeignKeys = k;

        return k;
    }

    List<ForeignKey<?, ?>> getForeignKeys0() {
        return flatMap(getTables(), (Table<?> t) -> t.getReferences());
    }

    @Override
    public final List<Index> getIndexes(String name) {
        return getIndexes(name(name));
    }

    @Override
    public final List<Index> getIndexes(Name name) {
        return getCachedIndexes().get(name);
    }

    @Override
    public final List<Index> getIndexes() {
        return Collections.unmodifiableList(new ArrayList<>(getCachedIndexes().qualified().values()));
    }

    private final Cached<Index> getCachedIndexes() {
        Cached<Index> i = cachedIndexes;

        if (i == null) {
            i = new Cached<>();
            i.init(() -> getIndexes0().iterator());
        }

        if (caching())
            cachedIndexes = i;

        return i;
    }

    List<Index> getIndexes0() {
        return flatMap(getTables(), t -> t.getIndexes());
    }

    private boolean caching() {
        return true;
    }

    @Override
    public Meta filterCatalogs(Predicate<? super Catalog> filter) {
        return new FilteredMeta(
            this,
            filter,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Meta filterSchemas(Predicate<? super Schema> filter) {
        return new FilteredMeta(
            this,
            null,
            filter,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Meta filterTables(Predicate<? super Table<?>> filter) {
        return new FilteredMeta(
            this,
            null,
            null,
            filter,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Meta filterDomains(Predicate<? super Domain<?>> filter) {
        return new FilteredMeta(
            this,
            null,
            null,
            null,
            filter,
            null,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Meta filterSequences(Predicate<? super Sequence<?>> filter) {
        return new FilteredMeta(
            this,
            null,
            null,
            null,
            null,
            filter,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Meta filterPrimaryKeys(Predicate<? super UniqueKey<?>> filter) {
        return new FilteredMeta(
            this,
            null,
            null,
            null,
            null,
            null,
            filter,
            null,
            null,
            null
        );
    }

    @Override
    public Meta filterUniqueKeys(Predicate<? super UniqueKey<?>> filter) {
        return new FilteredMeta(
            this,
            null,
            null,
            null,
            null,
            null,
            null,
            filter,
            null,
            null
        );
    }

    @Override
    public Meta filterForeignKeys(Predicate<? super ForeignKey<?, ?>> filter) {
        return new FilteredMeta(
            this,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            filter,
            null
        );
    }

    @Override
    public Meta filterIndexes(Predicate<? super Index> filter) {
        return new FilteredMeta(
            this,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            filter
        );
    }

    @Override
    public final Meta snapshot() {
        return new Snapshot(this);
    }

    @Override
    public final Queries ddl() {
        return ddl(new DDLExportConfiguration());
    }

    // [#9396] TODO Fix this. Subclasses should not need to override this to get
    //         correct results
    @Override
    public /* non-final */ Queries ddl(DDLExportConfiguration exportConfiguration) {
        return new DDL(dsl(), exportConfiguration).queries(this);
    }

    @Override
    public final Meta apply(String migration) {
        return apply(dsl().parser().parse(migration));
    }

    @Override
    public final Meta apply(Query... migration) {
        return apply(dsl().queries(migration));
    }

    @Override
    public final Meta apply(Collection<? extends Query> migration) {
        return apply(dsl().queries(migration));
    }

    @Override
    public final Meta apply(Queries migration) {
        return dsl().meta(ddl().concat(migration).queries());
    }

    @Override
    public final Queries migrateTo(Meta other) {
        return migrateTo(other, new org.jooq.MigrationConfiguration());
    }

    @Override
    public final Queries migrateTo(Meta other, org.jooq.MigrationConfiguration c) {
        return new Diff(configuration(), c, this, other).queries();
    }

    // [#9396] TODO Fix this. Subclasses should not need to override this to get
    //         correct results
    @Override
    public /* non-final */ InformationSchema informationSchema() {
        return InformationSchemaExport.exportCatalogs(configuration(), getCatalogs());
    }

    final Table<?> lookupTable(Table<?> table) {
        Catalog c = table.getCatalog();
        Schema s = table.getSchema();

        // TODO: This is a re-occurring pattern in Meta implementations. Should we have a more generic way to look up objects in a Catalog/Schema?
        Catalog catalog = getCatalog(c == null ? "" : c.getName());
        if (catalog == null)
            return null;

        Schema schema = catalog.getSchema(s == null ? "" : s.getName());
        if (schema == null)
            return null;

        return schema.getTable(table.getName());
    }

    final <R extends Record> UniqueKey<R> lookupKey(Table<R> in, UniqueKey<?> uk) {
        Set<?> ukFields = new HashSet<>(uk.getFields());

        // [#10279] [#10281] Cannot use Key::equals here, because that is
        // name-based. 1) The name is irrelevant for this lookup, 2) some
        // key implementations (e.g. MetaPrimaryKey for H2) don't produce
        // the correct key name, but the index name.
        // [#11258] Also, we need position agnostic comparison, using sets
        return Tools.findAny(in.getKeys(), k -> ukFields.equals(new HashSet<>(k.getFields())));
    }

    final UniqueKey<?> lookupUniqueKey(ForeignKey<?, ?> fk) {
        Table<?> table = lookupTable(fk.getKey().getTable());

        if (table == null)
            return null;

        return lookupKey(table, fk.getKey());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    static final <R extends Record> ForeignKey<R, ?> copyFK(Table<R> fkTable, UniqueKey<?> uk, ForeignKey<R, ?> oldFk) {
        Table<?> ukTable = uk.getTable();

        return Internal.createForeignKey(
            fkTable,
            oldFk.getQualifiedName(),
            map(oldFk.getFieldsArray(), f -> (TableField) fkTable.field(f), TableField[]::new),
            uk,
            map(oldFk.getKeyFieldsArray(), f -> (TableField) ukTable.field(f), TableField[]::new),
            oldFk.enforced()
        );
    }

    @Override
    public int hashCode() {
        return ddl().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Meta m)
            return ddl().equals(m.ddl());

        return false;
    }

    @Override
    public String toString() {
        return ddl().toString();
    }
}
