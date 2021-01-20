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

    private static final long serialVersionUID = 5561057000510740144L;
    private Meta              delegate;

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
        List<Catalog> result = new ArrayList<>();

        for (Catalog catalog : delegate.getCatalogs())
            result.add(new SnapshotCatalog(catalog));

        return result;
    }

    private class SnapshotCatalog extends CatalogImpl {
        private static final long          serialVersionUID = 7979890261252183486L;
        private final List<SnapshotSchema> schemas;

        SnapshotCatalog(Catalog catalog) {
            super(catalog.getQualifiedName(), catalog.getCommentPart());

            schemas = new ArrayList<>();

            for (Schema schema : catalog.getSchemas())
                schemas.add(new SnapshotSchema(this, schema));
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
        private static final long               serialVersionUID = -95755926444275258L;

        private final List<SnapshotDomain<?>>   domains;
        private final List<SnapshotTable<?>>    tables;
        private final List<SnapshotSequence<?>> sequences;
        private final List<SnapshotUDT<?>>      udts;

        SnapshotSchema(SnapshotCatalog catalog, Schema schema) {
            super(schema.getQualifiedName(), catalog, schema.getCommentPart());

            domains = new ArrayList<>();
            tables = new ArrayList<>();
            sequences = new ArrayList<>();
            udts = new ArrayList<>();

            for (Domain<?> domain : schema.getDomains())
                domains.add(new SnapshotDomain<>(this, domain));
            for (Table<?> table : schema.getTables())
                tables.add(new SnapshotTable<>(this, table));
            for (Sequence<?> sequence : schema.getSequences())
                sequences.add(new SnapshotSequence<>(this, sequence));
            for (UDT<?> udt : schema.getUDTs())
                udts.add(new SnapshotUDT<>(this, udt));
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
        private static final long serialVersionUID = -1607062195966296849L;

        SnapshotDomain(SnapshotSchema schema, Domain<T> domain) {
            super(schema, domain.getQualifiedName(), domain.getDataType(), domain.getChecks().toArray(EMPTY_CHECK));
        }
    }

    private class SnapshotTable<R extends Record> extends TableImpl<R> {
        private static final long serialVersionUID = -6070726881709997500L;

        private final List<Index>            indexes;
        private final List<UniqueKey<R>>     uniqueKeys;
        private UniqueKey<R>                 primaryKey;
        private final List<ForeignKey<R, ?>> foreignKeys;
        private final List<Check<R>>         checks;

        SnapshotTable(SnapshotSchema schema, Table<R> table) {
            super(table.getQualifiedName(), schema, null, null, table.getCommentPart(), table.getOptions());

            indexes = new ArrayList<>();
            uniqueKeys = new ArrayList<>();
            foreignKeys = new ArrayList<>();
            checks = new ArrayList<>();

            for (Field<?> field : table.fields())
                createField(field.getUnqualifiedName(), field.getDataType(), this, field.getComment());

            for (Index index : table.getIndexes()) {
                List<SortField<?>> indexFields = index.getFields();
                SortField<?>[] copiedFields = new SortField[indexFields.size()];

                for (int i = 0; i < indexFields.size(); i++) {
                    SortField<?> field = indexFields.get(i);

                    // [#10804] Use this table's field reference if possible.
                    //          Otherwise (e.g. for function based indexes), use the actual field expression
                    Field<?> f = field(field.getName());
                    copiedFields[i] = f != null ? f.sort(field.getOrder()) : field;

                    // [#9009] TODO NULLS FIRST / NULLS LAST
                }

                indexes.add(Internal.createIndex(index.getQualifiedName(), this, copiedFields, index.getUnique()));
            }

            for (UniqueKey<R> uk : table.getUniqueKeys())
                uniqueKeys.add(Internal.createUniqueKey(this, uk.getQualifiedName(), fields(uk.getFieldsArray()), uk.enforced()));

            UniqueKey<R> pk = table.getPrimaryKey();
            if (pk != null)
                primaryKey = Internal.createUniqueKey(this, pk.getQualifiedName(), fields(pk.getFieldsArray()), pk.enforced());

            foreignKeys.addAll(table.getReferences());
            checks.addAll(table.getChecks());
        }

        @SuppressWarnings("unchecked")
        @Deprecated
        private final TableField<R, ?>[] fields(TableField<R, ?>[] tableFields) {

            // TODO: [#9456] This auxiliary method should not be necessary
            //               We should be able to call TableLike.fields instead.
            TableField<R, ?>[] result = new TableField[tableFields.length];

            for (int i = 0; i < tableFields.length; i++)
                result[i] = (TableField<R, ?>) field(tableFields[i].getName());

            return result;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        final void resolveReferences() {

            // TODO: Is there a better way than temporarily keeping the wrong
            //       ReferenceImpl in this list until we "know better"?
            for (int i = 0; i < foreignKeys.size(); i++) {
                ForeignKey<R, ?> fk = foreignKeys.get(i);

                UniqueKey uk = lookupUniqueKey(fk);
                foreignKeys.set(i, org.jooq.impl.Internal.createForeignKey(
                    this,
                    fk.getQualifiedName(),
                    fields(fk.getFieldsArray()),
                    uk,
                    fields(uk.getFieldsArray()),
                    fk.enforced()
                ));
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
        private static final long serialVersionUID = -1607062195966296849L;

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
        private static final long serialVersionUID = -5732449514562314202L;

        SnapshotUDT(SnapshotSchema schema, UDT<R> udt) {
            super(udt.getName(), schema, udt.getPackage(), udt.isSynthetic());
        }
    }
}