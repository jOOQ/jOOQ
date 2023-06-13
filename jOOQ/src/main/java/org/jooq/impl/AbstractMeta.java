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

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Tools.findAny;
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
    private Map<Name, Schema>                 cachedQualifiedSchemas;
    private Map<Name, Table<?>>               cachedQualifiedTables;
    private Map<Name, Domain<?>>              cachedQualifiedDomains;
    private Map<Name, Sequence<?>>            cachedQualifiedSequences;
    private Map<Name, UniqueKey<?>>           cachedQualifiedPrimaryKeys;
    private Map<Name, UniqueKey<?>>           cachedQualifiedUniqueKeys;
    private Map<Name, ForeignKey<?, ?>>       cachedQualifiedForeignKeys;
    private Map<Name, Index>                  cachedQualifiedIndexes;
    private Map<Name, List<Schema>>           cachedUnqualifiedSchemas;
    private Map<Name, List<Table<?>>>         cachedUnqualifiedTables;
    private Map<Name, List<Domain<?>>>        cachedUnqualifiedDomains;
    private Map<Name, List<Sequence<?>>>      cachedUnqualifiedSequences;
    private Map<Name, List<UniqueKey<?>>>     cachedUnqualifiedPrimaryKeys;
    private Map<Name, List<UniqueKey<?>>>     cachedUnqualifiedUniqueKeys;
    private Map<Name, List<ForeignKey<?, ?>>> cachedUnqualifiedForeignKeys;
    private Map<Name, List<Index>>            cachedUnqualifiedIndexes;

    AbstractMeta(Configuration configuration) {
        super(configuration);
    }

    @Override
    public final Catalog getCatalog(String name) {
        return getCatalog(name(name));
    }

    @Override
    public final Catalog getCatalog(Name name) {
        initCatalogs();
        return cachedCatalogs.get(name);
    }

    @Override
    public final List<Catalog> getCatalogs() {
        initCatalogs();
        return Collections.unmodifiableList(new ArrayList<>(cachedCatalogs.values()));
    }

    private final void initCatalogs() {
        if (cachedCatalogs == null) {
            cachedCatalogs = new LinkedHashMap<>();

            for (Catalog catalog : getCatalogs0())
                cachedCatalogs.put(catalog.getQualifiedName(), catalog);
        }
    }

    abstract List<Catalog> getCatalogs0();

    @Override
    public final List<Schema> getSchemas(String name) {
        return getSchemas(name(name));
    }

    @Override
    public final List<Schema> getSchemas(Name name) {
        initSchemas();
        return get(name, () -> getSchemas0().iterator(), cachedQualifiedSchemas, cachedUnqualifiedSchemas);
    }

    @Override
    public final List<Schema> getSchemas() {
        initSchemas();
        return Collections.unmodifiableList(new ArrayList<>(cachedQualifiedSchemas.values()));
    }

    private final void initSchemas() {
        if (cachedQualifiedSchemas == null) {
            cachedQualifiedSchemas = new LinkedHashMap<>();
            cachedUnqualifiedSchemas = new LinkedHashMap<>();
            get(name(""), () -> getSchemas0().iterator(), cachedQualifiedSchemas, cachedUnqualifiedSchemas);
        }
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
        initTables();
        return get(name, () -> getTables().iterator(), cachedQualifiedTables, cachedUnqualifiedTables);
    }

    @Override
    public final List<Table<?>> getTables() {
        initTables();
        return Collections.unmodifiableList(new ArrayList<>(cachedQualifiedTables.values()));
    }

    private final void initTables() {
        if (cachedQualifiedTables == null) {
            cachedQualifiedTables = new LinkedHashMap<>();
            cachedUnqualifiedTables = new LinkedHashMap<>();
            get(name(""), () -> getTables0().iterator(), cachedQualifiedTables, cachedUnqualifiedTables);
        }
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
        initDomains();
        return get(name, () -> getDomains().iterator(), cachedQualifiedDomains, cachedUnqualifiedDomains);
    }

    @Override
    public final List<Domain<?>> getDomains() {
        initDomains();
        return Collections.unmodifiableList(new ArrayList<>(cachedQualifiedDomains.values()));
    }

    private final void initDomains() {
        if (cachedQualifiedDomains == null) {
            cachedQualifiedDomains = new LinkedHashMap<>();
            cachedUnqualifiedDomains = new LinkedHashMap<>();
            get(name(""), () -> getDomains0().iterator(), cachedQualifiedDomains, cachedUnqualifiedDomains);
        }
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
        initSequences();
        return get(name, () -> getSequences().iterator(), cachedQualifiedSequences, cachedUnqualifiedSequences);
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        initSequences();
        return Collections.unmodifiableList(new ArrayList<>(cachedQualifiedSequences.values()));
    }

    private final void initSequences() {
        if (cachedQualifiedSequences == null) {
            cachedQualifiedSequences = new LinkedHashMap<>();
            cachedUnqualifiedSequences = new LinkedHashMap<>();
            get(name(""), () -> getSequences0().iterator(), cachedQualifiedSequences, cachedUnqualifiedSequences);
        }
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
        initPrimaryKeys();
        return get(name, () -> getPrimaryKeys().iterator(), cachedQualifiedPrimaryKeys, cachedUnqualifiedPrimaryKeys);
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys() {
        initPrimaryKeys();
        return Collections.unmodifiableList(new ArrayList<>(cachedQualifiedPrimaryKeys.values()));
    }

    private final void initPrimaryKeys() {
        if (cachedQualifiedPrimaryKeys == null) {
            cachedQualifiedPrimaryKeys = new LinkedHashMap<>();
            cachedUnqualifiedPrimaryKeys = new LinkedHashMap<>();
            get(name(""), () -> getPrimaryKeys0().iterator(), cachedQualifiedPrimaryKeys, cachedUnqualifiedPrimaryKeys);
        }
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
        initUniqueKeys();
        return get(name, () -> getUniqueKeys().iterator(), cachedQualifiedUniqueKeys, cachedUnqualifiedUniqueKeys);
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys() {
        initUniqueKeys();
        return Collections.unmodifiableList(new ArrayList<>(cachedQualifiedUniqueKeys.values()));
    }

    private final void initUniqueKeys() {
        if (cachedQualifiedUniqueKeys == null) {
            cachedQualifiedUniqueKeys = new LinkedHashMap<>();
            cachedUnqualifiedUniqueKeys = new LinkedHashMap<>();
            get(name(""), () -> getUniqueKeys0().iterator(), cachedQualifiedUniqueKeys, cachedUnqualifiedUniqueKeys);
        }
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
        initForeignKeys();
        return get(name, () -> getForeignKeys().iterator(), cachedQualifiedForeignKeys, cachedUnqualifiedForeignKeys);
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys() {
        initForeignKeys();
        return Collections.unmodifiableList(new ArrayList<>(cachedQualifiedForeignKeys.values()));
    }

    private final void initForeignKeys() {
        if (cachedQualifiedForeignKeys == null) {
            cachedQualifiedForeignKeys = new LinkedHashMap<>();
            cachedUnqualifiedForeignKeys = new LinkedHashMap<>();
            get(name(""), () -> getForeignKeys0().iterator(), cachedQualifiedForeignKeys, cachedUnqualifiedForeignKeys);
        }
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
        initIndexes();
        return get(name, () -> getIndexes().iterator(), cachedQualifiedIndexes, cachedUnqualifiedIndexes);
    }

    @Override
    public final List<Index> getIndexes() {
        initIndexes();
        return Collections.unmodifiableList(new ArrayList<>(cachedQualifiedIndexes.values()));
    }

    private final void initIndexes() {
        if (cachedQualifiedIndexes == null) {
            cachedQualifiedIndexes = new LinkedHashMap<>();
            cachedUnqualifiedIndexes = new LinkedHashMap<>();
            get(name(""), () -> getIndexes0().iterator(), cachedQualifiedIndexes, cachedUnqualifiedIndexes);
        }
    }

    List<Index> getIndexes0() {
        return flatMap(getTables(), t -> t.getIndexes());
    }

    private final <T extends Named> List<T> get(Name name, Iterable<T> i, Map<Name, T> qualified, Map<Name, List<T>> unqualified) {
        if (qualified.isEmpty()) {
            for (T object : i) {
                Name q = object.getQualifiedName();
                Name u = object.getUnqualifiedName();

                qualified.put(q, object);
                unqualified.computeIfAbsent(u, n -> new ArrayList<>()).add(object);
            }
        }

        T object = qualified.get(name);
        if (object != null)
            return Collections.singletonList(object);

        List<T> list = unqualified.get(name);
        if (list == null)
            return Collections.emptyList();
        else
            return Collections.unmodifiableList(list);
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
