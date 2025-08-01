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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.impl.Tools.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Sequence;
// ...
import org.jooq.Table;
import org.jooq.TableField;
// ...
import org.jooq.UDT;
import org.jooq.UniqueKey;

/**
 * A {@link Meta} implementation that applies filters on a delegate {@link Meta}
 * object.
 *
 * @author Lukas Eder
 */
final class FilteredMeta extends AbstractMeta {

    private final AbstractMeta                        meta;
    private final Predicate<? super Table<?>>         tableFilter;
    private final Predicate<? super UDT<?>>        udtFilter;
    private final Predicate<? super Domain<?>>        domainFilter;




    private final Predicate<? super Sequence<?>>      sequenceFilter;
    private final Predicate<? super UniqueKey<?>>     primaryKeyFilter;
    private final Predicate<? super UniqueKey<?>>     uniqueKeyFilter;
    private final Predicate<? super ForeignKey<?, ?>> foreignKeyFilter;
    private final Predicate<? super Index>            indexFilter;

    FilteredMeta(
        AbstractMeta meta,
        Predicate<? super Catalog> catalogFilter,
        Predicate<? super Schema> schemaFilter,
        Predicate<? super Table<?>> tableFilter,
        Predicate<? super UDT<?>> udtFilter,
        Predicate<? super Domain<?>> domainFilter,




        Predicate<? super Sequence<?>> sequenceFilter,
        Predicate<? super UniqueKey<?>> primaryKeyFilter,
        Predicate<? super UniqueKey<?>> uniqueKeyFilter,
        Predicate<? super ForeignKey<?, ?>> foreignKeyFilter,
        Predicate<? super Index> indexFilter
    ) {
        super(meta.configuration(), catalogFilter, schemaFilter);

        this.meta = meta.filtered0(catalogFilter, schemaFilter);
        this.tableFilter = tableFilter;
        this.udtFilter = udtFilter;
        this.domainFilter = domainFilter;




        this.sequenceFilter = sequenceFilter;
        this.primaryKeyFilter = primaryKeyFilter;
        this.uniqueKeyFilter = uniqueKeyFilter;
        this.foreignKeyFilter = foreignKeyFilter;
        this.indexFilter = indexFilter;
    }

    @Override
    final AbstractMeta filtered0(Predicate<? super Catalog> catalogFilter, Predicate<? super Schema> schemaFilter) {
        return filterCatalogs(catalogFilter).filterSchemas(schemaFilter);
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
    public final FilteredMeta filterCatalogs(Predicate<? super Catalog> filter) {
        return new FilteredMeta(
            meta,
            and(catalogFilter, filter),
            schemaFilter,
            tableFilter,
            udtFilter,
            domainFilter,




            sequenceFilter,
            primaryKeyFilter,
            uniqueKeyFilter,
            foreignKeyFilter,
            indexFilter
        );
    }

    @Override
    public final FilteredMeta filterSchemas(Predicate<? super Schema> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            and(schemaFilter, filter),
            tableFilter,
            udtFilter,
            domainFilter,




            sequenceFilter,
            primaryKeyFilter,
            uniqueKeyFilter,
            foreignKeyFilter,
            indexFilter
        );
    }

    @Override
    public final FilteredMeta filterTables(Predicate<? super Table<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            and(tableFilter, filter),
            udtFilter,
            domainFilter,




            sequenceFilter,
            primaryKeyFilter,
            uniqueKeyFilter,
            foreignKeyFilter,
            indexFilter
        );
    }

    @Override
    public final FilteredMeta filterUDTs(Predicate<? super UDT<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            and(udtFilter, filter),
            domainFilter,




            sequenceFilter,
            primaryKeyFilter,
            uniqueKeyFilter,
            foreignKeyFilter,
            indexFilter
        );
    }

    @Override
    public final FilteredMeta filterDomains(Predicate<? super Domain<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            udtFilter,
            and(domainFilter, filter),




            sequenceFilter,
            primaryKeyFilter,
            uniqueKeyFilter,
            foreignKeyFilter,
            indexFilter
        );
    }











































    @Override
    public final FilteredMeta filterSequences(Predicate<? super Sequence<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            udtFilter,
            domainFilter,




            and(sequenceFilter, filter),
            primaryKeyFilter,
            uniqueKeyFilter,
            foreignKeyFilter,
            indexFilter
        );
    }

    @Override
    public final FilteredMeta filterPrimaryKeys(Predicate<? super UniqueKey<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            udtFilter,
            domainFilter,




            sequenceFilter,
            and(primaryKeyFilter, filter),
            uniqueKeyFilter,
            foreignKeyFilter,
            indexFilter
        );
    }

    @Override
    public final FilteredMeta filterUniqueKeys(Predicate<? super UniqueKey<?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            udtFilter,
            domainFilter,




            sequenceFilter,
            primaryKeyFilter,
            and(uniqueKeyFilter, filter),
            foreignKeyFilter,
            indexFilter
        );
    }

    @Override
    public final FilteredMeta filterForeignKeys(Predicate<? super ForeignKey<?, ?>> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            udtFilter,
            domainFilter,




            sequenceFilter,
            primaryKeyFilter,
            uniqueKeyFilter,
            and(foreignKeyFilter, filter),
            indexFilter
        );
    }

    @Override
    public final FilteredMeta filterIndexes(Predicate<? super Index> filter) {
        return new FilteredMeta(
            meta,
            catalogFilter,
            schemaFilter,
            tableFilter,
            udtFilter,
            domainFilter,




            sequenceFilter,
            primaryKeyFilter,
            uniqueKeyFilter,
            foreignKeyFilter,
            and(indexFilter, filter)
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

    static <Q extends QueryPart> Predicate<? super Q> and(Predicate<? super Q> p1, Predicate<? super Q> p2) {
        if (p1 == null)
            return p2;
        else if (p2 == null)
            return p1;
        else
            return new And<>(p1, p2);
    }

    private class FilteredCatalog extends CatalogImpl {

        private final Catalog          delegate;
        private transient List<Schema> schemas;

        private FilteredCatalog(Catalog delegate) {
            super(delegate.getQualifiedName(), delegate.getCommentPart());

            this.delegate = delegate;
        }

        @Override
        public final List<Schema> getSchemas() {
            if (schemas == null) {
                schemas = new ArrayList<>();

                for (Schema s : delegate.getSchemas())
                    if (schemaFilter == null || schemaFilter.test(s))
                        schemas.add(new FilteredSchema(this, s));
            }

            return Collections.unmodifiableList(schemas);
        }
    }

    private class FilteredSchema extends SchemaImpl {
        private final Schema                delegate;
        private transient List<UDT<?>>      udts;
        private transient List<Domain<?>>   domains;
        private transient List<Table<?>>    tables;
        private transient List<Sequence<?>> sequences;





        private FilteredSchema(FilteredCatalog catalog, Schema delegate) {
            super(delegate.getQualifiedName(), catalog, delegate.getCommentPart());

            this.delegate = delegate;
        }

        @Override
        public final List<Domain<?>> getDomains() {
            if (domains == null) {
                domains = new ArrayList<>();

                for (Domain<?> d : delegate.getDomains())
                    if (domainFilter == null || domainFilter.test(d))
                        // TODO: Schema is wrong here
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
                        tables.add(new FilteredTable<>(this, t));
            }

            return Collections.unmodifiableList(tables);
        }

        @Override
        public final List<Sequence<?>> getSequences() {
            if (sequences == null) {
                sequences = new ArrayList<>();

                for (Sequence<?> t : delegate.getSequences())
                    if (sequenceFilter == null || sequenceFilter.test(t))
                        // TODO: Schema is wrong here
                        sequences.add(t);
            }

            return Collections.unmodifiableList(sequences);
        }

        @Override
        public final List<UDT<?>> getUDTs() {
            if (udts == null) {
                udts = new ArrayList<>();

                for (UDT<?> u : delegate.getUDTs())
                    if (udtFilter == null || udtFilter.test(u))
                        // TODO: Schema is wrong here
                        udts.add(u);
            }

            return Collections.unmodifiableList(udts);
        }




































    }

    private class FilteredTable<R extends Record> extends TableImpl<R> {
        private final Table<R>                   delegate;
        private transient List<Index>            indexes;
        private transient UniqueKey<R>           primaryKey;
        private transient List<UniqueKey<R>>     uniqueKeys;
        private transient List<ForeignKey<R, ?>> references;




        private FilteredTable(FilteredSchema schema, Table<R> delegate) {
            super(delegate.getQualifiedName(), schema, null, null, delegate.getCommentPart(), delegate.getOptions());

            this.delegate = delegate;

            for (Field<?> field : delegate.fieldsIncludingHidden().fields())
                createField(field.getQualifiedName(), field.getDataType(), this, field.getComment());
        }

        @Override
        public final List<Index> getIndexes() {
            if (indexes == null) {
                indexes = new ArrayList<>();

                for (Index index : delegate.getIndexes())
                    if (indexFilter == null || indexFilter.test(index))
                        indexes.add(index);
            }

            return Collections.unmodifiableList(indexes);
        }

        private final void initKeys() {
            if (uniqueKeys == null) {
                uniqueKeys = new ArrayList<>();

                for (UniqueKey<R> uk : delegate.getUniqueKeys())
                    if (uniqueKeyFilter == null || uniqueKeyFilter.test(uk))
                        uniqueKeys.add(key(uk));

                UniqueKey<R> pk = delegate.getPrimaryKey();
                if (pk != null)
                    if (primaryKeyFilter == null || primaryKeyFilter.test(pk))
                        primaryKey = key(pk);
            }
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final UniqueKey<R> key(UniqueKey<R> key) {
            return Internal.createUniqueKey(
                this,
                key.getName(),
                map(key.getFieldsArray(), f -> (TableField) field(f), TableField[]::new),
                key.enforced()
            );
        }

        @Override
        public final UniqueKey<R> getPrimaryKey() {
            initKeys();
            return primaryKey;
        }

        @Override
        public final List<UniqueKey<R>> getUniqueKeys() {
            initKeys();
            return Collections.unmodifiableList(uniqueKeys);
        }

        @Override
        public final List<ForeignKey<R, ?>> getReferences() {
            if (references == null) {
                references = new ArrayList<>();

                fkLoop:
                for (ForeignKey<R, ?> fk : delegate.getReferences()) {
                    if (foreignKeyFilter != null && !foreignKeyFilter.test(fk))
                        continue fkLoop;

                    UniqueKey<?> uk = lookupUniqueKey(fk);
                    if (uk == null)
                        continue fkLoop;
                    else if (uk.isPrimary() && primaryKeyFilter != null && !primaryKeyFilter.test(uk))
                        continue fkLoop;
                    else if (!uk.isPrimary() && uniqueKeyFilter != null && !uniqueKeyFilter.test(uk))
                        continue fkLoop;

                    references.add(copyFK(this, uk, fk));
                }
            }

            return Collections.unmodifiableList(references);
        }

        @Override
        public final List<Check<R>> getChecks() {
            return delegate.getChecks();
        }

















    }
}
