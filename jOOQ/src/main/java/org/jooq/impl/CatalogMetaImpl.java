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

import static org.jooq.impl.Tools.EMPTY_CATALOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Meta;
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
        return filterCatalogs0(configuration, catalogs, new HashSet<>(Arrays.asList(catalogs)));
    }

    static final Meta filterCatalogs(Configuration configuration, Set<Catalog> catalogs) {
        return filterCatalogs0(configuration, catalogs.toArray(EMPTY_CATALOG), catalogs);
    }

    private static final Meta filterCatalogs0(Configuration configuration, Catalog[] array, Set<Catalog> set) {
        return new CatalogMetaImpl(configuration, array).filterCatalogs(set::contains);
    }

    static final Meta filterSchemas(Configuration configuration, Schema[] schemas) {
        return filterSchemas(configuration, new HashSet<>(Arrays.asList(schemas)));
    }

    static final Meta filterSchemas(Configuration configuration, Set<Schema> schemas) {

        // TODO: Some schemas may belong to another catalog
        Catalog defaultCatalog = new CatalogImpl("") {
            @Override
            public List<Schema> getSchemas() {
                return new ArrayList<>(schemas);
            }
        };

        Set<Catalog> c = new HashSet<>();
        for (Schema schema : schemas)
            c.add(schema.getCatalog() != null ? schema.getCatalog() : defaultCatalog);

        return filterCatalogs(configuration, c).filterSchemas(schemas::contains);
    }

    static final Meta filterTables(Configuration configuration, Table<?>[] tables) {
        return filterTables(configuration, new HashSet<>(Arrays.asList(tables)));
    }

    static final Meta filterTables(Configuration configuration, Set<Table<?>> tables) {

        // TODO: Some tables may belong to another schema
        Schema defaultSchema = new SchemaImpl("") {
            @Override
            public List<Table<?>> getTables() {
                return new ArrayList<>(tables);
            }
        };

        Set<Schema> s = new HashSet<>();
        for (Table<?> table : tables)
            s.add(table.getSchema() != null ? table.getSchema() : defaultSchema);

        return filterSchemas(configuration, s)
              .filterTables(tables::contains)
              .filterSequences(none())
              .filterDomains(none())
              ;
    }

    static final <Q extends QueryPart> Predicate<Q> none() {
        return t -> false;
    }
}
