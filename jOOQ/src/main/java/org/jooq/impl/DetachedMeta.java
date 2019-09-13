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
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Comment;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Package;
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
final class DetachedMeta extends AbstractMeta {

    private static final long serialVersionUID = 5561057000510740144L;
    private Meta delegate;

    private DetachedMeta() {}

    private final DetachedMeta copy(Meta meta) {
        delegate = meta;
        getCatalogs();
        delegate = null;
        resolveReferences();
        return this;
    }

    private final void resolveReferences() {
        for (Catalog catalog : getCatalogs())
            ((DetachedCatalog) catalog).resolveReferences(this);
    }

    static Meta copyOf(Meta meta) {
        return new DetachedMeta().copy(meta);
    }

    @Override
    protected final List<Catalog> getCatalogs0() throws DataAccessException {
        List<Catalog> result = new ArrayList<>();
        for (Catalog catalog : delegate.getCatalogs())
            result.add(DetachedCatalog.copyOf(catalog));
        return result;
    }

    private static class DetachedCatalog extends CatalogImpl {
        private static final long serialVersionUID = 7979890261252183486L;

        private final List<Schema> schemas = new ArrayList<>();

        private DetachedCatalog(String name, String comment) {
            super(name, comment);
        }

        private final DetachedCatalog copy(Catalog catalog) {
            for (Schema schema : catalog.getSchemas())
                schemas.add(DetachedSchema.copyOf(schema, this));
            return this;
        }

        private final void resolveReferences(Meta meta) {
            for (Schema schema : schemas)
                ((DetachedSchema) schema).resolveReferences(meta);
        }

        static DetachedCatalog copyOf(Catalog catalog) {
            return new DetachedCatalog(catalog.getName(), catalog.getComment()).copy(catalog);
        }

        @Override
        public final List<Schema> getSchemas() {
            return schemas;
        }
    }

    private static class DetachedSchema extends SchemaImpl {
        private static final long serialVersionUID = -95755926444275258L;

        private final List<Table<?>> tables = new ArrayList<>();
        private final List<Sequence<?>> sequences = new ArrayList<>();
        private final List<UDT<?>> udts = new ArrayList<>();

        private DetachedSchema(String name, Catalog owner, String comment) {
            super(name, owner, comment);
        }

        private final DetachedSchema copy(Schema schema) {
            for (Table<?> table : schema.getTables())
                tables.add(DetachedTable.copyOf(table, this));
            for (Sequence<?> sequence : schema.getSequences())
                sequences.add(DetachedSequence.copyOf(sequence, this));
            for (UDT<?> udt : schema.getUDTs())
                udts.add(DetachedUDT.copyOf(udt, this));
            return this;
        }

        static DetachedSchema copyOf(Schema schema, Catalog owner) {
            return new DetachedSchema(schema.getName(), owner, schema.getComment()).copy(schema);
        }

        public final void resolveReferences(Meta meta) {
            for (Table<?> table : tables)
                ((DetachedTable<?>) table).resolveReferences(meta);
        }

        @Override
        public final List<Table<?>> getTables() {
            return tables;
        }

        @Override
        public final List<Sequence<?>> getSequences() {
            return sequences;
        }

        @Override
        public final List<UDT<?>> getUDTs() {
            return udts;
        }
    }

    private static class DetachedTable<R extends Record> extends TableImpl<R> {
        private static final long serialVersionUID = -6070726881709997500L;

        private final List<Index> indexes = new ArrayList<>();
        private final List<UniqueKey<R>> keys = new ArrayList<>();
        private UniqueKey<R> primaryKey;
        private final List<ForeignKey<R, ?>> references = new ArrayList<>();

        private DetachedTable(Name name, Schema owner, Comment comment) {
            super(name, owner, null, null, comment);
        }

        private final DetachedTable<?> copy(Table<R> table) {
            for (Field<?> field : table.fields())
                createField(field.getName(), field.getDataType(), this, field.getComment());
            for (Index index : table.getIndexes()) {
                List<SortField<?>> indexFields = index.getFields();
                SortField<?>[] copiedFields = new SortField[indexFields.size()];
                for (int i = 0; i < indexFields.size(); i++) {
                    SortField<?> field = indexFields.get(i);
                    copiedFields[i] = field(field.getName()).sort(field.getOrder());
                    // [#9009] TODO NULLS FIRST / NULLS LAST
                }
                indexes.add(org.jooq.impl.Internal.createIndex(index.getName(), this, copiedFields, index.getUnique()));
            }
            for (UniqueKey<R> key : table.getKeys())
                keys.add(org.jooq.impl.Internal.createUniqueKey(this, key.getName(), copiedFields(key.getFieldsArray())));
            UniqueKey<R> pk = table.getPrimaryKey();
            if (pk != null)
                primaryKey = org.jooq.impl.Internal.createUniqueKey(this, pk.getName(), copiedFields(pk.getFieldsArray()));
            references.addAll(table.getReferences());
            return this;
        }

        @SuppressWarnings("unchecked")
        private final TableField<R, ?>[] copiedFields(TableField<R, ?>[] tableFields) {
            TableField<R, ?>[] result = new TableField[tableFields.length];
            for (int i = 0; i < tableFields.length; i++)
                result[i] = (TableField<R, ?>) field(tableFields[i].getName());
            return result;
        }

        @SuppressWarnings("unchecked")
        static DetachedTable<?> copyOf(Table<?> table, Schema owner) {
            return new DetachedTable<>(table.getUnqualifiedName(), owner, DSL.comment(table.getComment())).copy((Table<Record>) table);
        }

        public final void resolveReferences(Meta meta) {
            for (int i = 0; i < references.size(); i++) {
                ForeignKey<R, ?> ref = references.get(i);
                Name name = ref.getKey().getTable().getQualifiedName();
                Table<?> table = resolveTable(name, meta);
                UniqueKey<?> pk = table == null ? null : table.getPrimaryKey();
                references.set(i, org.jooq.impl.Internal.createForeignKey(pk, this, ref.getName(), copiedFields(ref.getFieldsArray())));
            }
        }

        private final Table<?> resolveTable(Name tableName, Meta meta) {
            List<Table<?>> list = meta.getTables(tableName);
            return list.isEmpty() ? null : list.get(0);
        }

        @Override
        public final List<Index> getIndexes() {
            return indexes;
        }

        @Override
        public final List<UniqueKey<R>> getKeys() {
            return keys;
        }

        @Override
        public final UniqueKey<R> getPrimaryKey() {
            return primaryKey;
        }

        @Override
        public final List<ForeignKey<R, ?>> getReferences() {
            return references;
        }
    }

    private static class DetachedSequence<T extends Number> extends SequenceImpl<T> {
        private static final long serialVersionUID = -1607062195966296849L;

        private DetachedSequence(String name, Schema owner, DataType<T> dataType) {
            super(name, owner, dataType);
        }

        private final DetachedSequence<?> copy(Sequence<?> sequence) {
            return this;
        }

        static DetachedSequence<?> copyOf(Sequence<?> sequence, Schema owner) {
            return new DetachedSequence<>(sequence.getName(), owner, sequence.getDataType()).copy(sequence);
        }
    }

    private static class DetachedUDT<R extends UDTRecord<R>> extends UDTImpl<R> {
        private static final long serialVersionUID = -5732449514562314202L;

        private DetachedUDT(String name, Schema owner, Package package_, boolean synthetic) {
            super(name, owner, package_, synthetic);
        }

        private final DetachedUDT<?> copy(UDT<?> udt) {
            return this;
        }

        static DetachedUDT<?> copyOf(UDT<?> udt, Schema owner) {
            Package package_ = null;



            return new DetachedUDT<>(udt.getName(), owner, package_, udt.isSynthetic()).copy(udt);
        }
    }
}