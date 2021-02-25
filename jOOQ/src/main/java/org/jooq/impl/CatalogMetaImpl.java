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

import static org.jooq.impl.AbstractNamed.nameOrDefault;
import static org.jooq.impl.Tools.EMPTY_CATALOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
@SuppressWarnings("serial")
final class CatalogMetaImpl extends AbstractMeta {

    private static final long    serialVersionUID = 7582210274970452691L;
    private final Catalog[]      catalogs;

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

    private static final Meta filterCatalogs0(Configuration configuration, Catalog[] array, final Set<Catalog> set) {
        return new CatalogMetaImpl(configuration, array).filterCatalogs(new Predicate<Catalog>() {
            @Override
            public boolean test(Catalog catalog) {
                return set.contains(catalog);
            }
        });
    }

    static final Meta filterSchemas(Configuration configuration, Schema[] schemas) {
        return filterSchemas(configuration, new LinkedHashSet<>(Arrays.asList(schemas)));
    }

    static final Meta filterSchemas(Configuration configuration, final Set<Schema> schemas) {
        final Map<Name, Catalog> c = new LinkedHashMap<>();
        final Map<Name, List<Schema>> mapping = new LinkedHashMap<>();

        for (Schema schema : schemas) {
            Name key = nameOrDefault(schema.getCatalog());
            List<Schema> list = mapping.get(key);

            if (list == null)
                mapping.put(key, list = new ArrayList<>());

            list.add(schema);
        }

        for (Schema schema : schemas) {
            Name key = nameOrDefault(schema.getCatalog());

            if (!c.containsKey(key))
                c.put(key, new CatalogImpl(key) {
                    @Override
                    public List<Schema> getSchemas() {
                        return mapping.get(getQualifiedName());
                    }
                });
        }

        return filterCatalogs(configuration, new LinkedHashSet<>(c.values())).filterSchemas(new Predicate<Schema>() {
            @Override
            public boolean test(Schema schema) {
                return schemas.contains(schema);
            }
        });
    }

    static final Meta filterTables(Configuration configuration, Table<?>[] tables) {
        return filterTables(configuration, new LinkedHashSet<>(Arrays.asList(tables)));
    }

    static final Meta filterTables(Configuration configuration, final Set<Table<?>> tables) {
        final Map<Name, Schema> s = new LinkedHashMap<>();
        final Map<Name, List<Table<?>>> mapping = new LinkedHashMap<>();

        // TODO: [#7172] Can't use Table.getQualifiedName() here, yet
        for (Table<?> table : tables) {
            Name key = nameOrDefault(table.getCatalog()).append(nameOrDefault(table.getSchema()));
            List<Table<?>> list = mapping.get(key);

            if (list == null)
                mapping.put(key, list = new ArrayList<>());

            list.add(table);
        }

        for (Table<?> table : tables) {
            Name key = nameOrDefault(table.getCatalog()).append(nameOrDefault(table.getSchema()));

            if (!s.containsKey(key))
                s.put(key, new SchemaImpl(key, table.getCatalog()) {
                    @Override
                    public List<Table<?>> getTables() {
                        return mapping.get(getQualifiedName());
                    }
                });
        }

        return filterSchemas(configuration, new LinkedHashSet<>(s.values()))
              .filterTables(new Predicate<Table<?>>() {
                  @Override
                  public boolean test(Table<?> table) {
                      return tables.contains(table);
                  }
              })
              .filterSequences(none())
              .filterDomains(none())
              ;
    }

    static final <Q extends QueryPart> Predicate<Q> none() {
        return new Predicate<Q>() {
            @Override
            public boolean test(Q t) {
                return false;
            }
        };
    }
}
