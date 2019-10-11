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

import static org.jooq.impl.AbstractName.NO_NAME;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Catalog;
import org.jooq.Comment;
import org.jooq.Constraint;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.Named;
import org.jooq.Nullability;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataDefinitionException;

@SuppressWarnings("serial")
final class DDLInterpreter {

    private final Map<Name, MutableCatalog> catalogs = new LinkedHashMap<>();
    private final MutableCatalog            defaultCatalog;
    private final MutableSchema             defaultSchema;
    private MutableSchema                   currentSchema;

    DDLInterpreter() {
        defaultCatalog = new MutableCatalog(NO_NAME);
        catalogs.put(defaultCatalog.name, defaultCatalog);
        defaultSchema = new MutableSchema(NO_NAME, defaultCatalog);
        currentSchema = defaultSchema;
    }

    final Meta meta() {
        return new AbstractMeta() {
            private static final long serialVersionUID = 2052806256506059701L;

            @Override
            protected List<Catalog> getCatalogs0() throws DataAccessException {
                List<Catalog> result = new ArrayList<>();

                for (MutableCatalog catalog : catalogs.values())
                    result.add(catalog.new InterpretedCatalog());

                return result;
            }
        };
    }

    // -------------------------------------------------------------------------
    // Interpretation logic
    // -------------------------------------------------------------------------

    final void accept(Query query) {
        if (query instanceof CreateSchemaImpl)
            accept0((CreateSchemaImpl) query);
        else if (query instanceof AlterSchemaImpl)
            accept0((AlterSchemaImpl) query);
        else if (query instanceof DropSchemaImpl)
            accept0((DropSchemaImpl) query);

        else if (query instanceof CreateTableImpl)
            accept0((CreateTableImpl) query);
        else if (query instanceof AlterTableImpl)
            accept0((AlterTableImpl) query);
        else if (query instanceof DropTableImpl)
            accept0((DropTableImpl) query);

        else if (query instanceof CreateViewImpl)
            accept0((CreateViewImpl<?>) query);
        else if (query instanceof AlterViewImpl)
            accept0((AlterViewImpl) query);
        else if (query instanceof DropViewImpl)
            accept0((DropViewImpl) query);

        else if (query instanceof CreateSequenceImpl)
            accept0((CreateSequenceImpl) query);
        else if (query instanceof AlterSequenceImpl)
            accept0((AlterSequenceImpl<?>) query);
        else if (query instanceof DropSequenceImpl)
            accept0((DropSequenceImpl) query);

        else if (query instanceof CommentOnImpl)
            accept0((CommentOnImpl) query);

        else
            throw unsupportedQuery(query);
    }

    private final void accept0(CreateSchemaImpl query) {
        Schema schema = query.$schema();

        if (getSchema(schema, false) != null) {
            if (!query.$ifNotExists())
                throw schemaAlreadyExists(schema);

            return;
        }

        getSchema(schema, true);
    }

    private final void accept0(AlterSchemaImpl query) {
        Schema schema = query.$schema();
        Schema renameTo = query.$renameTo();

        MutableSchema oldSchema = getSchema(schema);
        if (oldSchema == null) {
            if (!query.$ifExists())
                throw schemaNotExists(schema);

            return;
        }

        if (renameTo != null) {
            if (getSchema(renameTo, false) != null)
                throw schemaAlreadyExists(renameTo);

            oldSchema.name = (UnqualifiedName) renameTo.getUnqualifiedName();
            return;
        }
        else
            throw new UnsupportedOperationException(query.getSQL());
    }

    private final void accept0(DropSchemaImpl query) {
        Schema schema = query.$schema();
        MutableSchema mutableSchema = getSchema(schema);

        if (mutableSchema == null) {
            if (!query.$ifExists())
                throw schemaNotExists(schema);

            return;
        }

        if (mutableSchema.isEmpty() || query.$cascade())
            mutableSchema.catalog.schemas.remove(mutableSchema);
        else
            throw schemaNotEmpty(schema);

        // TODO: Is this needed?
        if (mutableSchema.equals(currentSchema))
            currentSchema = null;
    }

    private final void accept0(CreateTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema(), true);

        MutableTable existing = schema.table(table);
        if (existing != null) {
            if (!query.$ifNotExists())
                throw alreadyExists(table, existing);

            return;
        }

        MutableTable t = newTable(table, schema, query.$columnFields(), query.$columnTypes(), query.$select(), query.$comment(), false);

        for (Constraint constraint : query.$constraints()) {
            ConstraintImpl impl = (ConstraintImpl) constraint;

            // XXX handle case that primary key already exists?
            if (impl.$primaryKey() != null)
                t.primaryKey = new MutableUniqueKey((UnqualifiedName) impl.getUnqualifiedName(), t, t.fields(impl.$primaryKey(), true));

            else if (impl.$unique() != null)
                t.uniqueKeys.add(new MutableUniqueKey((UnqualifiedName) impl.getUnqualifiedName(), t, t.fields(impl.$unique(), true)));
        }

        for (Index index : query.$indexes()) {
            IndexImpl impl = (IndexImpl) index;
            t.indexes.add(new MutableIndex((UnqualifiedName) impl.getUnqualifiedName(), t, t.sortFields(impl.$fields()), impl.$unique()));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final void accept0(AlterTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema());

        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$ifExists())
                throw tableNotExists(table);

            return;
        }
        else if (existing.view)
            throw objectNotTable(table);

        Field<?> addColumn = query.$addColumn();
        Field<?> alterColumn = query.$alterColumn();
        Field<?> alterColumnDefault = query.$alterColumnDefault();
        Nullability alterColumnNullability = query.$alterColumnNullability();
        DataType<?> alterColumnType = query.$alterColumnType();
        boolean ifExistsColumn = query.$ifExistsColumn();
        boolean ifNotExistsColumn = query.$ifNotExistsColumn();
        Table<?> renameTo = query.$renameTo();
        Field<?> renameColumn = query.$renameColumn();
        Field<?> renameColumnTo = query.$renameColumnTo();
        List<Field<?>> $dropColumns = query.$dropColumns();

        if (addColumn != null) {
            if (query.$addFirst())
                existing.fields.add(0, new MutableField((UnqualifiedName) addColumn.getUnqualifiedName(), existing, query.$addColumnType()));
            else if (query.$addBefore() != null)
                existing.fields.add(indexOrFail(existing, query.$addBefore()), new MutableField((UnqualifiedName) addColumn.getUnqualifiedName(), existing, query.$addColumnType()));
            else if (query.$addAfter() != null)
                existing.fields.add(indexOrFail(existing, query.$addAfter()) + 1, new MutableField((UnqualifiedName) addColumn.getUnqualifiedName(), existing, query.$addColumnType()));
            else
                existing.fields.add(new MutableField((UnqualifiedName) addColumn.getUnqualifiedName(), existing, query.$addColumnType()));
        }
        else if (alterColumn != null) {
            MutableField existingField = existing.field(alterColumn);

            if (existingField == null) {
                if (!ifExistsColumn)
                    throw columnNotExists(alterColumn);

                return;
            }

            if (alterColumnNullability != null)
                existingField.type = existingField.type.nullability(alterColumnNullability);
            else if (alterColumnType != null)
                existingField.type = alterColumnType;
            else if (alterColumnDefault != null)
                existingField.type = existingField.type.default_((Field) alterColumnDefault);
            else
                throw unsupportedQuery(query);
        }
        else if (renameTo != null && checkNotExists(schema, renameTo)) {
            existing.name = (UnqualifiedName) renameTo.getUnqualifiedName();
        }
        else if (renameColumn != null) {
            if (existing.field(renameColumn) == null)
                throw fieldNotExists(renameColumn);
            else if (existing.field(renameColumnTo) != null)
                throw fieldAlreadyExists(renameColumnTo);
            else
                existing.field(renameColumn).name = (UnqualifiedName) renameColumnTo.getUnqualifiedName();
        }
        else if ($dropColumns != null) {
            // TODO Implement cascade
            // TODO check if any constraints or indexes reference this column.
            List<MutableField> fields = existing.fields($dropColumns.toArray(EMPTY_FIELD), false);

            if (fields.size() < $dropColumns.size() && !query.$ifExistsColumn())
                existing.fields($dropColumns.toArray(EMPTY_FIELD), true);

            existing.fields.removeAll(fields);
        }
        else
            throw unsupportedQuery(query);
    }

    private final int indexOrFail(MutableTable existing, Field<?> field) {
        int result = -1;

        for (int i = 0; i < existing.fields.size(); i++) {
            if (existing.fields.get(i).name.equals(field.getUnqualifiedName())) {
                result = i;
                break;
            }
        }

        if (result == -1)
            throw fieldNotExists(field);

        return result;
    }

    private final void accept0(DropTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema());

        // TODO schema == null
        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$ifExists())
                throw tableNotExists(table);

            return;
        }
        else if (existing.view)
            throw objectNotTable(table);

        schema.drop(table);
    }

    private final void accept0(CreateViewImpl<?> query) {
        Table<?> table = query.$view();
        MutableSchema schema = getSchema(table.getSchema());

        MutableTable existing = schema.table(table);
        if (existing != null) {
            if (!existing.view)
                throw objectNotView(table);
            else if (query.$orReplace())
                schema.drop(table);
            else if (!query.$ifNotExists())
                throw viewAlreadyExists(table);
            else
                return;
        }

        List<DataType<?>> columnTypes = new ArrayList<>();
        for (Field<?> f : query.$select().getSelect())
            columnTypes.add(f.getDataType());

        newTable(table, schema, Arrays.asList(query.$fields()), columnTypes, query.$select(), null, true);
    }

    private final void accept0(AlterViewImpl query) {
        Table<?> table = query.$view();
        MutableSchema schema = getSchema(table.getSchema());

        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$ifExists())
                throw viewNotExists(table);

            return;
        }
        else if (!existing.view)
            throw objectNotView(table);

        Table<?> renameTo = query.$renameTo();
        if (renameTo != null && checkNotExists(schema, renameTo))
            existing.name = (UnqualifiedName) renameTo.getUnqualifiedName();
        else
            throw unsupportedQuery(query);
    }

    private final void accept0(DropViewImpl query) {
        Table<?> table = query.$view();
        MutableSchema schema = getSchema(table.getSchema());

        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$ifExists())
                throw viewNotExists(table);

            return;
        }
        else if (!existing.view)
            throw objectNotView(table);

        schema.drop(table);
    }

    private final void accept0(CreateSequenceImpl query) {
        Sequence<?> sequence = query.$sequence();
        MutableSchema schema = getSchema(sequence.getSchema());

        MutableSequence existing = schema.sequence(sequence);
        if (existing != null) {
            if (!query.$ifNotExists())
                throw sequenceAlreadyExists(sequence);

            return;
        }

        MutableSequence ms = new MutableSequence((UnqualifiedName) sequence.getUnqualifiedName(), schema);

        ms.startWith = query.$startWith();
        ms.incrementBy = query.$incrementBy();
        ms.minValue = query.$minvalue();
        ms.noMinvalue = query.$noMinvalue();
        ms.maxValue = query.$maxvalue();
        ms.noMaxvalue = query.$noMaxvalue();
        ms.cycle = query.$cycle();
        ms.noCache = query.$noCycle();
        ms.cache = query.$cache();
        ms.noCache = query.$noCache();
    }

    private final void accept0(AlterSequenceImpl<?> query) {
        Sequence<?> sequence = query.$sequence();
        MutableSchema schema = getSchema(sequence.getSchema());

        MutableSequence existing = schema.sequence(sequence);
        if (existing == null) {
            if (!query.$ifExists())
                throw sequenceNotExists(sequence);

            return;
        }

        Sequence<?> renameTo = query.$renameTo();
        if (renameTo != null) {
            if (schema.sequence(renameTo) != null)
                throw sequenceAlreadyExists(renameTo);

            existing.name = (UnqualifiedName) renameTo.getUnqualifiedName();
        }
        else
            throw unsupportedQuery(query);
    }

    private final void accept0(DropSequenceImpl query) {
        Sequence<?> sequence = query.$sequence();
        MutableSchema schema = getSchema(sequence.getSchema());

        MutableSequence existing = schema.sequence(sequence);
        if (existing == null) {
            if (!query.$ifExists())
                throw sequenceNotExists(sequence);

            return;
        }

        schema.sequences.remove(existing);
    }

    private final void accept0(CommentOnImpl query) {
        Table<?> table = query.$table();
        Field<?> field = query.$field();

        if (table != null)
            table(table).comment = query.$comment();
        else if (field != null)
            field(field).comment = query.$comment();
        else
            throw unsupportedQuery(query);
    }

    // -------------------------------------------------------------------------
    // Exceptions
    // -------------------------------------------------------------------------

    private static final DataDefinitionException unsupportedQuery(Query query) {
        return new DataDefinitionException("Unsupported query: " + query.getSQL());
    }

    private static final DataDefinitionException schemaNotExists(Schema schema) {
        return new DataDefinitionException("Schema does not exist: " + schema.getQualifiedName());
    }

    private static final DataDefinitionException schemaAlreadyExists(Schema schema) {
        return new DataDefinitionException("Schema already exists: " + schema.getQualifiedName());
    }

    private static final DataDefinitionException schemaNotEmpty(Schema schema) {
        return new DataDefinitionException("Schema is not empty: " + schema.getQualifiedName());
    }

    private static final DataDefinitionException tableNotExists(Table<?> table) {
        return new DataDefinitionException("Table does not exist: " + table.getQualifiedName());
    }

    private static final DataDefinitionException objectNotTable(Table<?> table) {
        return new DataDefinitionException("Object is not a table: " + table.getQualifiedName());
    }

    private static final DataDefinitionException objectNotView(Table<?> table) {
        return new DataDefinitionException("Object is not a view: " + table.getQualifiedName());
    }

    private static final DataDefinitionException tableAlreadyExists(Table<?> table) {
        return new DataDefinitionException("Table already exists: " + table.getQualifiedName());
    }

    private static final DataDefinitionException viewNotExists(Table<?> view) {
        return new DataDefinitionException("View does not exist: " + view.getQualifiedName());
    }

    private static final DataDefinitionException viewAlreadyExists(Table<?> view) {
        return new DataDefinitionException("View already exists: " + view.getQualifiedName());
    }

    private static final DataDefinitionException columnNotExists(Field<?> field) {
        return new DataDefinitionException("Column does not exist: " + field.getQualifiedName());
    }

    private static final DataDefinitionException columnAlreadyExists(Field<?> field) {
        return new DataDefinitionException("Column already exists: " + field.getQualifiedName());
    }

    private static final DataDefinitionException sequenceNotExists(Sequence<?> sequence) {
        return new DataDefinitionException("Sequence does not exist: " + sequence.getQualifiedName());
    }

    private static final DataDefinitionException sequenceAlreadyExists(Sequence<?> sequence) {
        return new DataDefinitionException("Sequence already exists: " + sequence.getQualifiedName());
    }

    private static final DataDefinitionException fieldNotExists(Field<?> field) {
        return new DataDefinitionException("Field does not exist: " + field.getQualifiedName());
    }

    private static final DataDefinitionException fieldAlreadyExists(Field<?> field) {
        return new DataDefinitionException("Field does not exist: " + field.getQualifiedName());
    }

    // -------------------------------------------------------------------------
    // Auxiliary methods
    // -------------------------------------------------------------------------

    private final MutableSchema getSchema(Schema input) {
        return getSchema(input, false);
    }

    private final MutableSchema getSchema(Schema input, boolean create) {
        if (input == null)
            return currentSchema;

        MutableCatalog catalog = defaultCatalog;
        if (input.getCatalog() != null) {
            Name catalogName = input.getCatalog().getUnqualifiedName();
            if ((catalog = catalogs.get(catalogName)) == null && create)
                catalogs.put(catalogName, catalog = new MutableCatalog((UnqualifiedName) catalogName));
        }

        if (catalog == null)
            return null;

        MutableSchema schema = defaultSchema;
        UnqualifiedName schemaName = (UnqualifiedName) input.getUnqualifiedName();
        if ((schema = catalog.getSchema(schemaName)) == null && create)
            // TODO createSchemaIfNotExists should probably be configurable
            schema = new MutableSchema(schemaName, catalog);

        return schema;
    }

    private static final MutableTable newTable(
        Table<?> table,
        MutableSchema schema,
        List<Field<?>> columns,
        List<DataType<?>> columnTypes,
        Select<?> select,
        Comment comment,
        boolean view
    ) {
        MutableTable t = new MutableTable((UnqualifiedName) table.getUnqualifiedName(), schema, comment, view);

        if (!columns.isEmpty()) {
            for (int i = 0; i < columns.size(); i++) {
                Field<?> column = columns.get(i);
                t.fields.add(new MutableField((UnqualifiedName) column.getUnqualifiedName(), t, columnTypes.get(i)));
            }
        }
        else if (select != null) {
            for (Field<?> column : select.fields())
                t.fields.add(new MutableField((UnqualifiedName) column.getUnqualifiedName(), t, column.getDataType()));
        }

        return t;
    }

    private final MutableTable table(Table<?> table) {
        return table(table, true);
    }

    private final MutableTable table(Table<?> table, boolean throwIfNotExists) {

        // TODO: Can schema be null?
        MutableTable result = getSchema(table.getSchema()).table(table);

        if (result == null && throwIfNotExists)
            throw tableNotExists(table);

        return result;
    }

    private static final boolean checkNotExists(MutableSchema schema, Table<?> table) {
        MutableTable mt = schema.table(table);

        if (mt != null)
            throw alreadyExists(table, mt);

        return true;
    }

    private static final DataDefinitionException alreadyExists(Table<?> t, MutableTable mt) {
        if (mt.view)
            return viewAlreadyExists(t);
        else
            return tableAlreadyExists(t);
    }

    private final MutableField field(Field<?> field) {
        return field(field, true);
    }

    private final MutableField field(Field<?> field, boolean throwIfNotExists) {
        MutableTable table = table(DSL.table(field.getQualifiedName().qualifier()), throwIfNotExists);

        if (table == null)
            return null;

        MutableField result = table.field(field);

        if (result == null && throwIfNotExists)
            throw fieldNotExists(field);

        return result;
    }

    private static final UnqualifiedName normalize(Named named) {
        return normalize((UnqualifiedName) named.getUnqualifiedName());
    }

    private static final UnqualifiedName normalize(UnqualifiedName name) {
        if (name == null)
            return null;

        if (name.quoted() == Quoted.QUOTED)
            return name;

        String lowerCase = name.first().toLowerCase();
        return (UnqualifiedName) (name.first() == lowerCase ? name : unquotedName(lowerCase));
    }

    // -------------------------------------------------------------------------
    // Data model
    // -------------------------------------------------------------------------

    private static abstract class MutableNamed {
        UnqualifiedName name;
        Comment         comment;

        MutableNamed(UnqualifiedName name) {
            this(name, null);
        }

        MutableNamed(UnqualifiedName name, Comment comment) {
            this.name = normalize(name);
            this.comment = comment;
        }

        @Override
        public String toString() {
            return name.toString();
        }
    }

    private static final class MutableCatalog extends MutableNamed {
        List<MutableSchema> schemas = new ArrayList<>();

        MutableCatalog(UnqualifiedName name) {
            super(name, null);
        }

        final MutableSchema getSchema(UnqualifiedName n) {
            for (MutableSchema schema : schemas)
                if (schema.name.equals(n))
                    return schema;

            return null;
        }

        private final class InterpretedCatalog extends CatalogImpl {
            InterpretedCatalog() {
                super(MutableCatalog.this.name, MutableCatalog.this.comment);
            }

            @Override
            public final List<Schema> getSchemas() {
                List<Schema> result = new ArrayList<>(schemas.size());

                for (MutableSchema schema : schemas)
                    result.add(schema.new InterpretedSchema(this));

                return result;
            }
        }
    }

    private static final class MutableSchema extends MutableNamed  {
        MutableCatalog        catalog;
        List<MutableTable>    tables    = new ArrayList<>();
        List<MutableSequence> sequences = new ArrayList<>();

        MutableSchema(UnqualifiedName name, MutableCatalog catalog) {
            super(name);

            this.catalog = catalog;
            catalog.schemas.add(this);
        }

        final boolean isEmpty() {
            return tables.isEmpty();
        }

        final MutableTable table(Table<?> t) {
            return find(tables, t);
        }

        final MutableSequence sequence(Sequence<?> s) {
            return find(sequences, s);
        }

        final <M extends MutableNamed> M find(List<? extends M> list, Named named) {
            UnqualifiedName n = normalize(named);

            for (M m : list)
                if (m.name.equals(n))
                    return m;

            return null;
        }

        final void drop(Table<?> t) {
            UnqualifiedName n = normalize(t);

            for (int i = 0; i < tables.size(); i++) {
                if (tables.get(i).name.equals(n)) {
                    tables.remove(i);
                    break;
                }
            }
        }

        private final class InterpretedSchema extends SchemaImpl {
            InterpretedSchema(MutableCatalog.InterpretedCatalog catalog) {
                super(MutableSchema.this.name, catalog, MutableSchema.this.comment);
            }

            @Override
            public final List<Table<?>> getTables() {
                List<Table<?>> result = new ArrayList<>(tables.size());

                for (MutableTable table : tables)
                    result.add(table.new InterpretedTable(this));

                return result;
            }

            @Override
            public final List<Sequence<?>> getSequences() {
                List<Sequence<?>> result = new ArrayList<>(sequences.size());

                for (MutableSequence sequence : sequences)
                    result.add(sequence.new InterpretedSequence(this));

                return result;
            }
        }
    }

    private static final class MutableTable extends MutableNamed  {
        MutableSchema          schema;
        List<MutableField>     fields     = new ArrayList<>();
        MutableUniqueKey       primaryKey;
        List<MutableUniqueKey> uniqueKeys = new ArrayList<>();
        List<MutableIndex>     indexes    = new ArrayList<>();
        boolean                view;

        MutableTable(UnqualifiedName name, MutableSchema schema, Comment comment, boolean view) {
            super(name, comment);

            this.schema = schema;
            this.view = view;
            schema.tables.add(this);
        }

        final MutableField field(Field<?> f) {
            Name n = f.getUnqualifiedName();

            for (MutableField mf : fields)
                if (mf.name.equals(n))
                    return mf;

            return null;
        }

        final List<MutableField> fields(Field<?>[] fs, boolean failIfNotFound) {
            List<MutableField> result = new ArrayList<>();

            for (Field<?> f : fs) {
                MutableField mf = field(f);

                if (mf != null)
                    result.add(mf);
                else if (failIfNotFound)
                    throw new DataDefinitionException("Field does not exist in table: " + f.getQualifiedName());
            }

            return result;
        }

        final List<MutableSortField> sortFields(SortField<?>[] sfs) {
            List<MutableSortField> result = new ArrayList<>();

            for (SortField<?> sf : sfs) {
                Field<?> f = ((SortFieldImpl<?>) sf).getField();
                MutableField mf = field(f);

                if (mf == null)
                    throw new DataDefinitionException("Field does not exist in table: " + f.getQualifiedName());

                result.add(new MutableSortField(mf, sf.getOrder()));
            }

            return result;
        }

        private final class InterpretedTable extends TableImpl<Record> {
            InterpretedTable(MutableSchema.InterpretedSchema schema) {
                super(MutableTable.this.name, schema, null, null, MutableTable.this.comment);

                for (MutableField field : MutableTable.this.fields)
                    createField(field.name, field.type, field.comment != null ? field.comment.getComment() : null);
            }

            @Override
            public final UniqueKey<Record> getPrimaryKey() {
                return interpretedKey(MutableTable.this.primaryKey);
            }

            @Override
            public final List<UniqueKey<Record>> getKeys() {
                List<UniqueKey<Record>> result = new ArrayList<>();
                UniqueKey<Record> pk = getPrimaryKey();

                if (pk != null)
                    result.add(pk);

                for (MutableUniqueKey uk : MutableTable.this.uniqueKeys)
                    result.add(interpretedKey(uk));

                return result;
            }

            @Override
            public final List<Index> getIndexes() {
                List<Index> result = new ArrayList<>();

                for (MutableIndex i : MutableTable.this.indexes)
                    result.add(interpretedIndex(i));

                return result;
            }

            @SuppressWarnings("unchecked")
            private final UniqueKey<Record> interpretedKey(MutableUniqueKey key) {
                if (key == null)
                    return null;

                TableField<Record, ?>[] f = new TableField[key.fields.size()];

                for (int i = 0; i < f.length; i++)
                    f[i] = (TableField<Record, ?>) field(key.fields.get(i).name);

                return new UniqueKeyImpl<Record>(this, key.name.last(), f);
            }

            private final Index interpretedIndex(MutableIndex idx) {
                if (idx == null)
                    return null;

                SortField<?>[] f = new SortField[idx.fields.size()];

                for (int i = 0; i < f.length; i++) {
                    MutableSortField msf = idx.fields.get(i);
                    f[i] = field(msf.name).sort(msf.sort);
                }

                return new IndexImpl(idx.name, this, f, null, idx.unique);
            }
        }
    }

    @SuppressWarnings("unused")
    private static final class MutableSequence extends MutableNamed {
        MutableSchema schema;
        Field<?>      startWith;
        Field<?>      incrementBy;
        boolean       noMinvalue;
        Field<?>      minValue;
        boolean       noMaxvalue;
        Field<?>      maxValue;
        boolean       noCycle;
        boolean       cycle;
        boolean       noCache;
        Field<?>      cache;

        MutableSequence(UnqualifiedName name, MutableSchema schema) {
            super(name);

            this.schema = schema;
            schema.sequences.add(this);
        }

        private final class InterpretedSequence extends SequenceImpl<Long> {
            InterpretedSequence(Schema schema) {
                super(MutableSequence.this.name, schema, BIGINT, false);

                // [#7752] TODO: Pass additional flags like START WITH to
                //         SequenceImpl when this is ready.
            }
        }
    }

    private static abstract class MutableKey extends MutableNamed {
        MutableTable       table;
        List<MutableField> fields;

        MutableKey(UnqualifiedName name, MutableTable table, List<MutableField> fields) {
            super(name);

            this.table = table;
            this.fields = fields;
        }
    }

    private static final class MutableUniqueKey extends MutableKey {
        MutableUniqueKey(UnqualifiedName name, MutableTable table, List<MutableField> fields) {
            super(name, table, fields);
        }
    }

    private static final class MutableIndex extends MutableNamed {
        MutableTable           table;
        List<MutableSortField> fields;
        boolean                unique;

        MutableIndex(UnqualifiedName name, MutableTable table, List<MutableSortField> fields, boolean unique) {
            super(name);

            this.table = table;
            this.fields = fields;
            this.unique = unique;
        }
    }

    private static final class MutableField extends MutableNamed {
        MutableTable table;
        DataType<?> type;

        MutableField(UnqualifiedName name, MutableTable table, DataType<?> type) {
            super(name);

            this.table = table;
            this.type = type;
        }
    }

    private static final class MutableSortField extends MutableNamed {
        MutableField field;
        SortOrder sort;

        MutableSortField(MutableField field, SortOrder sort) {
            super(field.name);

            this.field = field;
            this.sort = sort;
        }
    }
}
