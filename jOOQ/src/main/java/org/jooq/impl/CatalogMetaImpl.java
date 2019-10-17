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
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class CatalogMetaImpl extends AbstractMeta {

    private static final long serialVersionUID = 7582210274970452691L;
    private final Catalog[]   catalogs;

    CatalogMetaImpl(Configuration configuration, Catalog[] catalogs) {
        super(configuration);

        this.catalogs = catalogs;
    }

    @Override
    protected final List<Catalog> getCatalogs0() {
        return Arrays.asList(catalogs);
    }

    @Override
    protected final List<Schema> getSchemas0() {
        List<Schema> result = new ArrayList<>();

        for (Catalog catalog : catalogs)
            result.addAll(catalog.getSchemas());

        return result;
    }

    @Override
    protected final List<Table<?>> getTables0() {
        List<Table<?>> result = new ArrayList<>();

        for (Catalog catalog : catalogs)
            for (Schema schema : catalog.getSchemas())
                result.addAll(schema.getTables());

        return result;
    }

    @Override
    protected final List<Sequence<?>> getSequences0() {
        List<Sequence<?>> result = new ArrayList<>();

        for (Catalog catalog : catalogs)
            for (Schema schema : catalog.getSchemas())
                result.addAll(schema.getSequences());

        return result;
    }

    @Override
    protected final List<UniqueKey<?>> getPrimaryKeys0() {
        List<UniqueKey<?>> result = new ArrayList<>();

        for (Catalog catalog : catalogs)
            for (Schema schema : catalog.getSchemas())
                for (Table<?> table : schema.getTables())
                    if (table.getPrimaryKey() != null)
                        result.add(table.getPrimaryKey());

        return result;
    }
}
