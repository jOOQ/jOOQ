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

import static java.util.Collections.unmodifiableList;
import static org.jooq.impl.Tools.EMPTY_CHECK;
import static org.jooq.impl.Tools.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UniqueKey;
import org.jooq.exception.DataAccessException;

/**
 * An implementation of {@code Meta} which can be used to create fully
 * self-contained copies of other {@code Meta} objects.
 *
 * @author Knut Wannheden
 */
final class Snapshot extends AbstractMeta {

    private Meta delegate;

    Snapshot(Meta meta) {
        super(meta.configuration());

        delegate = meta;
        getCatalogs();
        delegate = null;
        resolveReferences();
    }

    private final void resolveReferences() {
        for (Catalog catalog : getCatalogs())
            ((SnapshotCatalog) catalog).resolveReferences();
    }

    @Override
    final List<Catalog> getCatalogs0() throws DataAccessException {
        return map(delegate.getCatalogs(), SnapshotCatalog::new);
    }

    private class SnapshotCatalog extends CatalogImpl {
        private final List<SnapshotSchema> schemas;

        SnapshotCatalog(Catalog catalog) {
            super(catalog.getQualifiedName(), catalog.getCommentPart());
            schemas = map(catalog.getSchemas(), s -> new SnapshotSchema(this, s));
        }

        private final void resolveReferences() {
            for (SnapshotSchema schema : schemas)
                schema.resolveReferences();
        }

        @Override
        public final List<Schema> getSchemas() {
            return unmodifiableList(schemas);
        }
    }

    private class SnapshotSchema extends SchemaImpl {

        private final List<SnapshotDomain<?>>   domains;
        private final List<SnapshotTable<?>>    tables;
        private final List<SnapshotSequence<?>> sequences;
        private final List<SnapshotUDT<?>>      udts;

        SnapshotSchema(SnapshotCatalog catalog, Schema schema) {
            super(schema.getQualifiedName(), catalog, schema.getCommentPart());

            domains = map(schema.getDomains(), d -> new SnapshotDomain<>(this, d));
            tables = map(schema.getTables(), t -> new SnapshotTable<>(this, t));
            sequences = map(schema.getSequences(), s -> new SnapshotSequence<>(this, s));
            udts = map(schema.getUDTs(), u -> new SnapshotUDT<>(this, u));
        }

        final void resolveReferences() {
            for (SnapshotTable<?> table : tables)
                table.resolveReferences();
        }

        @Override
        public final List<Domain<?>> getDomains() {
            return unmodifiableList(domains);
        }

        @Override
        public final List<Table<?>> getTables() {
            return unmodifiableList(tables);
        }

        @Override
        public final List<Sequence<?>> getSequences() {
            return unmodifiableList(sequences);
        }

        @Override
        public final List<UDT<?>> getUDTs() {
            return unmodifiableList(udts);
        }
    }

    private class SnapshotDomain<T> extends DomainImpl<T> {
        SnapshotDomain(SnapshotSchema schema, Domain<T> domain) {
            super(schema, domain.getQualifiedName(), domain.getDataType(), domain.getChecks().toArray(EMPTY_CHECK));
        }
    }

    private class SnapshotTable<R extends Record> extends TableImpl<R> {
        private final List<Index>            indexes;
        private final List<UniqueKey<R>>     uniqueKeys;
        private UniqueKey<R>                 primaryKey;
        private final List<ForeignKey<R, ?>> foreignKeys;
        private final List<Check<R>>         checks;

        SnapshotTable(SnapshotSchema schema, Table<R> table) {
            super(table.getQualifiedName(), schema, null, null, table.getCommentPart(), table.getOptions());

            for (Field<?> field : table.fields())
                createField(field.getUnqualifiedName(), field.getDataType(), this, field.getComment());

            indexes = map(table.getIndexes(), index -> Internal.createIndex(
                index.getQualifiedName(), this,
                map(index.getFields(), field -> {

                    // [#10804] Use this table's field reference if possible.
                    //          Otherwise (e.g. for function based indexes), use the actual field expression
                    Field<?> f = field(field.getName());
                    return f != null ? f.sort(field.getOrder()) : field;

                    // [#9009] TODO NULLS FIRST / NULLS LAST
                }, SortField[]::new),
                index.getUnique()
            ));

            uniqueKeys = map(table.getUniqueKeys(), uk -> Internal.createUniqueKey(this, uk.getQualifiedName(), fields(uk.getFieldsArray()), uk.enforced()));

            UniqueKey<R> pk = table.getPrimaryKey();
            if (pk != null)
                primaryKey = Internal.createUniqueKey(this, pk.getQualifiedName(), fields(pk.getFieldsArray()), pk.enforced());

            foreignKeys = new ArrayList<>(table.getReferences());
            checks = new ArrayList<>(table.getChecks());
        }

        @SuppressWarnings("unchecked")
        @Deprecated
        private final TableField<R, ?>[] fields(TableField<R, ?>[] tableFields) {

            // TODO: [#9456] This auxiliary method should not be necessary
            //               We should be able to call TableLike.fields instead.
            return map(tableFields, f -> (TableField<R, ?>) field(f.getName()), TableField[]::new);
        }

        final void resolveReferences() {

            // TODO: Is there a better way than temporarily keeping the wrong
            //       ReferenceImpl in this list until we "know better"?
            for (int i = 0; i < foreignKeys.size(); i++) {
                ForeignKey<R, ?> fk = foreignKeys.get(i);
                UniqueKey<?> uk = lookupUniqueKey(fk);

                // [#10823] [#11287] There are numerous reasons why a UNIQUE
                // constraint may not be known to our meta model. Let's just
                // prevent exceptions here
                if (uk == null)
                    foreignKeys.remove(i);
                else
                    foreignKeys.set(i, copyFK(this, uk, fk));
            }
        }

        @Override
        public final List<Index> getIndexes() {
            return Collections.unmodifiableList(indexes);
        }

        @Override
        public final List<UniqueKey<R>> getUniqueKeys() {
            return Collections.unmodifiableList(uniqueKeys);
        }

        @Override
        public final UniqueKey<R> getPrimaryKey() {
            return primaryKey;
        }

        @Override
        public final List<ForeignKey<R, ?>> getReferences() {
            return Collections.unmodifiableList(foreignKeys);
        }

        @Override
        public final List<Check<R>> getChecks() {
            return Collections.unmodifiableList(checks);
        }
    }

    private class SnapshotSequence<T extends Number> extends SequenceImpl<T> {
        SnapshotSequence(SnapshotSchema schema, Sequence<T> sequence) {
            super(
                sequence.getQualifiedName(),
                schema,
                sequence.getDataType(),
                false,
                sequence.getStartWith(),
                sequence.getIncrementBy(),
                sequence.getMinvalue(),
                sequence.getMaxvalue(),
                sequence.getCycle(),
                sequence.getCache()
            );
        }
    }

    private class SnapshotUDT<R extends UDTRecord<R>> extends UDTImpl<R> {
        SnapshotUDT(SnapshotSchema schema, UDT<R> udt) {
            super(udt.getName(), schema, udt.getPackage(), udt.isSynthetic());
        }
    }
}