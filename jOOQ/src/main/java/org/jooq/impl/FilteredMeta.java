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
import java.util.Collections;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Domain;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.QueryPart;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UDT;
import org.jooq.UniqueKey;

/**
 * A {@link Meta} implementation that applies filters on a delegate {@link Meta}
 * object.
 *
 * @author Lukas Eder
 */
final class FilteredMeta extends AbstractMeta {

    /**
     * Generated UID
     */
    private static final long                     serialVersionUID = 2589476339574534267L;

    private final AbstractMeta                    meta;
    private final Predicate<? super Catalog>      catalogFilter;
    private final Predicate<? super Schema>       schemaFilter;
    private final Predicate<? super Table<?>>     tableFilter;
    private final Predicate<? super Domain<?>>    domainFilter;
    private final Predicate<? super Sequence<?>>  sequenceFilter;
    private final Predicate<? super UniqueKey<?>> primaryKeyFilter;
    private final Predicate<? super Index>        indexFilter;

    FilteredMeta(
        AbstractMeta meta,
        Predicate<? super Catalog> catalogFilter,
        Predicate<? super Schema> schemaFilter,
        Predicate<? super Table<?>> tableFilter,
        Predicate<? super Domain<?>> domainFilter,
        Predicate<? super Sequence<?>> sequenceFilter,
        Predicate<? super UniqueKey<?>> primaryKeyFilter,
        Predicate<? super Index> indexFilter
    ) {
        super(meta.configuration());

        this.meta = meta;
        this.catalogFilter = catalogFilter;
        this.schemaFilter = schemaFilter;
        this.tableFilter = tableFilter;
        this.domainFilter = domainFilter;
        this.sequenceFilter = sequenceFilter;
        this.primaryKeyFilter = primaryKeyFilter;
        this.indexFilter = indexFilter;
    }

    @Override
    final List<Catalog> getCatalogs0() {
        List<Catalog> result = new ArrayList<>();

        for (Catalog c : meta.getCatalogs())
            if (catalogFilter == null || catalogFilter.test(c))
                result.add(new FilteredCatalog(c));

        return result;
    }

    @Override
    public final Meta filterCatalogs(Predicate<? super Catalog> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter != null ? new And<>(catalogFilter, filter) : filter,
            schemaFilter,
            tableFilter,
            domainFilter,
            sequenceFilter,
            primaryKeyFilter,
            indexFilter
        );
    }

    @Override
    public final Meta filterSchemas(Predicate<? super Schema> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter != null ? new And<>(schemaFilter, filter) : filter,
            tableFilter,
            domainFilter,
            sequenceFilter,
            primaryKeyFilter,
            indexFilter
        );
    }

    @Override
    public final Meta filterTables(Predicate<? super Table<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter != null ? new And<>(tableFilter, filter) : filter,
            domainFilter,
            sequenceFilter,
            primaryKeyFilter,
            indexFilter
        );
    }

    @Override
    public final Meta filterDomains(Predicate<? super Domain<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            domainFilter != null ? new And<>(domainFilter, filter) : filter,
            sequenceFilter,
            primaryKeyFilter,
            indexFilter
        );
    }

    @Override
    public final Meta filterSequences(Predicate<? super Sequence<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            domainFilter,
            sequenceFilter != null ? new And<>(sequenceFilter, filter) : filter,
            primaryKeyFilter,
            indexFilter
        );
    }

    @Override
    public final Meta filterPrimaryKeys(Predicate<? super UniqueKey<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            domainFilter,
            sequenceFilter,
            primaryKeyFilter != null ? new And<>(primaryKeyFilter, filter) : filter,
            indexFilter
        );
    }

    @Override
    public final Meta filterIndexes(Predicate<? super Index> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            domainFilter,
            sequenceFilter,
            primaryKeyFilter,
            indexFilter != null ? new And<>(indexFilter, filter) : filter
        );
    }

    private static class And<Q extends QueryPart> implements Predicate<Q> {
        private final Predicate<? super Q> p1;
        private final Predicate<? super Q> p2;

        And(Predicate<? super Q> p1, Predicate<? super Q> p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public final boolean test(Q q) {
            return p1.test(q) && p2.test(q);
        }
    }

    private class FilteredCatalog extends CatalogImpl {
        private static final long      serialVersionUID = 7979890261252183486L;

        private final Catalog          delegate;
        private transient List<Schema> schemas;

        private FilteredCatalog(Catalog delegate) {
            super(delegate.getQualifiedName(), DSL.comment(delegate.getComment()));

            this.delegate = delegate;
        }

        @Override
        public final List<Schema> getSchemas() {
            if (schemas == null) {
                schemas = new ArrayList<>();

                for (Schema s : delegate.getSchemas())
                    if (schemaFilter == null || schemaFilter.test(s))
                        schemas.add(new FilteredSchema(s));
            }

            return Collections.unmodifiableList(schemas);
        }
    }

    private class FilteredSchema extends SchemaImpl {
        private static final long serialVersionUID = -95755926444275258L;

        private final Schema                delegate;
        private transient List<Domain<?>>   domains;
        private transient List<Table<?>>    tables;
        private transient List<Sequence<?>> sequences;

        private FilteredSchema(Schema delegate) {
            super(delegate.getQualifiedName(), delegate.getCatalog(), DSL.comment(delegate.getComment()));

            this.delegate = delegate;
        }

        @Override
        public final List<Domain<?>> getDomains() {
            if (domains == null) {
                domains = new ArrayList<>();

                for (Domain<?> d : delegate.getDomains())
                    if (domainFilter == null || domainFilter.test(d))
                        domains.add(d);
            }

            return Collections.unmodifiableList(domains);
        }

        @Override
        public final List<Table<?>> getTables() {
            if (tables == null) {
                tables = new ArrayList<>();

                for (Table<?> t : delegate.getTables())
                    if (tableFilter == null || tableFilter.test(t))
                        // TODO create a FilteredTable and filter out primary key and indexes
                        tables.add(t);
            }

            return Collections.unmodifiableList(tables);
        }

        @Override
        public final List<Sequence<?>> getSequences() {
            if (sequences == null) {
                sequences = new ArrayList<>();

                for (Sequence<?> t : delegate.getSequences())
                    if (sequenceFilter == null || sequenceFilter.test(t))
                        sequences.add(t);
            }

            return Collections.unmodifiableList(sequences);
        }

        @Override
        public final List<UDT<?>> getUDTs() {
            return delegate.getUDTs();
        }
    }
}
