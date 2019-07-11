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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class TableMetaImpl extends AbstractMeta {

    private static final long   serialVersionUID = 2910000827304539796L;

    @SuppressWarnings("unused")
    private final Configuration configuration;
    private final Table<?>[]    tables;

    TableMetaImpl(Configuration configuration, Table<?>[] tables) {
        this.configuration = configuration;
        this.tables = tables;
    }

    @Override
    public final List<Catalog> getCatalogs() {
        Set<Catalog> result = new LinkedHashSet<>();

        for (Table<?> table : tables)
            if (table.getSchema() != null)
                if (table.getSchema().getCatalog() != null)
                    result.add(table.getSchema().getCatalog());

        return new ArrayList<>(result);
    }

    @Override
    public final List<Schema> getSchemas() {
        Set<Schema> result = new LinkedHashSet<>();

        for (Table<?> table : tables)
            if (table.getSchema() != null)
                result.add(table.getSchema());

        return new ArrayList<>(result);
    }

    @Override
    public final List<Table<?>> getTables() {
        return Collections.unmodifiableList(Arrays.asList(tables));
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return Collections.emptyList();
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys() {
        List<UniqueKey<?>> result = new ArrayList<>();

        for (Table<?> table : tables)
            if (table.getPrimaryKey() != null)
                result.add(table.getPrimaryKey());

        return result;
    }
}
