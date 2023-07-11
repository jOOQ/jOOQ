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

import static org.jooq.impl.AbstractNamed.nameOrDefault;
import static org.jooq.impl.Tools.EMPTY_CATALOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Schema;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class CatalogMetaImpl extends AbstractMeta {

    private final Catalog[] catalogs;

    private CatalogMetaImpl(Configuration configuration, Catalog[] catalogs) {
        super(configuration);

        this.catalogs = catalogs;
    }

    @Override
    final List<Catalog> getCatalogs0() {
        return Arrays.asList(catalogs);
    }

    static final Meta filterCatalogs(Configuration configuration, Catalog[] catalogs) {
        return filterCatalogs0(configuration, catalogs, new LinkedHashSet<>(Arrays.asList(catalogs)));
    }

    static final Meta filterCatalogs(Configuration configuration, Set<Catalog> catalogs) {
        return filterCatalogs0(configuration, catalogs.toArray(EMPTY_CATALOG), catalogs);
    }

    private static final Meta filterCatalogs0(Configuration configuration, Catalog[] array, Set<Catalog> set) {
        return new CatalogMetaImpl(configuration, array).filterCatalogs(set::contains);
    }

    static final Meta filterSchemas(Configuration configuration, Schema[] schemas) {
        return filterSchemas(configuration, new LinkedHashSet<>(Arrays.asList(schemas)));
    }

    static final Meta filterSchemas(Configuration configuration, Set<Schema> schemas) {
        Map<Name, Catalog> c = new LinkedHashMap<>();
        Map<Name, List<Schema>> mapping = new LinkedHashMap<>();

        for (Schema schema : schemas)
            mapping.computeIfAbsent(nameOrDefault(schema.getCatalog()), k -> new ArrayList<>()).add(schema);

        for (Schema schema : schemas)
            c.computeIfAbsent(nameOrDefault(schema.getCatalog()), k -> new CatalogImpl(k) {
                @Override
                public List<Schema> getSchemas() {
                    return mapping.get(getQualifiedName());
                }
            });

        return filterCatalogs(configuration, new LinkedHashSet<>(c.values())).filterSchemas(schemas::contains);
    }

    static final Meta filterTables(Configuration configuration, Table<?>[] tables) {
        return filterTables(configuration, new LinkedHashSet<>(Arrays.asList(tables)));
    }

    static final Meta filterTables(Configuration configuration, Set<Table<?>> tables) {
        Map<Name, Schema> s = new LinkedHashMap<>();
        Map<Name, List<Table<?>>> mapping = new LinkedHashMap<>();

        // TODO: [#7172] Can't use Table.getQualifiedName() here, yet
        for (Table<?> table : tables)
            mapping.computeIfAbsent(nameOrDefault(table.getCatalog()).append(nameOrDefault(table.getSchema())), k -> new ArrayList<>()).add(table);

        for (Table<?> table : tables)
            s.computeIfAbsent(nameOrDefault(table.getCatalog()).append(nameOrDefault(table.getSchema())), k -> new SchemaImpl(k, table.getCatalog()) {
                @Override
                public List<Table<?>> getTables() {
                    return mapping.get(getQualifiedName());
                }
            });

        return filterSchemas(configuration, new LinkedHashSet<>(s.values()))
              .filterTables(tables::contains)
              .filterSequences(none())
              .filterDomains(none())



              ;
    }

    static final <Q extends QueryPart> Predicate<Q> none() {
        return t -> false;
    }
}
