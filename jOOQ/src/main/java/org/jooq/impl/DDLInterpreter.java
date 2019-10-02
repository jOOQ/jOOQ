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

import static org.jooq.impl.DSL.unquotedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Binding;
import org.jooq.Catalog;
import org.jooq.Comment;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;

final class DDLInterpreter {

    private final Map<Name, MutableCatalog> catalogs       = new LinkedHashMap<>();
    private final Configuration             configuration;
    private final MutableCatalog            defaultCatalog;
    private final MutableSchema             defaultSchema;
    private MutableSchema                   currentSchema;
    private JooqLogger                      log            = JooqLogger.getLogger(DDLInterpreter.class);

    DDLInterpreter(Configuration configuration) {
        this.configuration = configuration;
        defaultCatalog = new MutableCatalog(null);
        catalogs.put(defaultCatalog.getUnqualifiedName(), defaultCatalog);
        defaultSchema = new MutableSchema(null, defaultCatalog);
        currentSchema = defaultSchema;
    }

    Meta meta() {
        return new AbstractMeta() {
            private static final long serialVersionUID = 2052806256506059701L;

            @Override
            protected List<Catalog> getCatalogs0() throws DataAccessException {
                return new ArrayList<>(catalogs.values());
            }
        };
    }

    final void accept(Query query) {
        if (query instanceof CreateSchemaImpl)
            accept0((CreateSchemaImpl) query);
        else if (query instanceof DropSchemaImpl)
            accept0((DropSchemaImpl) query);
        else if (query instanceof CreateTableImpl)
            accept0((CreateTableImpl) query);
        else if (query instanceof AlterTableImpl)
            accept0((AlterTableImpl) query);
        else if (query instanceof DropTableImpl)
            accept0((DropTableImpl) query);
        else
            throw new UnsupportedOperationException(query.getSQL());
    }

    private final void accept0(CreateSchemaImpl query) {
        Schema schema = query.$schema();

        if (getSchema(schema, false) != null) {
            String message = "Schema already exists: " + schema.getQualifiedName();
            if (!query.$ifNotExists())
                throw new DataAccessException(message);
            log.debug(message);
            return;
        }

        getSchema(schema, true);
    }

    private final void accept0(DropSchemaImpl query) {
        Schema schema = query.$schema();
        MutableSchema mutableSchema = getSchema(schema, false);

        if (mutableSchema == null) {
            String message = "Schema does not exist: " + schema.getQualifiedName();
            if (!query.$ifExists())
                throw new DataAccessException(message);
            log.debug(message);
            return;
        }

        if (mutableSchema.isEmpty() || query.$cascade())
            mutableSchema.catalog.schemas.remove(mutableSchema);
        else
            throw new DataAccessException("Schema is not empty: " + schema.getQualifiedName());

        // TODO: Is this needed?
        if (mutableSchema.equals(currentSchema))
            currentSchema = null;
    }

    private final void accept0(CreateTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema(), true);

        if (schema.getTable(table.getUnqualifiedName()) != null) {
            String message = "Table already exists: " + table.getQualifiedName();
            if (!query.$ifNotExists())
                throw new DataAccessException(message);
            log.debug(message);
            return;
        }

        MutableTable t = new MutableTable(table.getUnqualifiedName(), schema, query.$comment());
        List<Field<?>> columns = query.$columnFields();
        if (!columns.isEmpty())
            for (int i = 0; i < columns.size(); i++) {
                Field<?> column = columns.get(i);
                t.addColumn(column.getUnqualifiedName(), query.$columnTypes().get(i));
            }
        else if (query.$select() != null)
            for (Field<?> column : query.$select().fields())
                t.addColumn(column.getUnqualifiedName(), column.getDataType());

        for (Constraint constraint : query.$constraints())
            if (constraint instanceof ConstraintImpl) {
                ConstraintImpl impl = (ConstraintImpl) constraint;
                // XXX handle case that primary key already exists?
                if (impl.$primaryKey() != null)
                    t.addPrimaryKey(impl.getUnqualifiedName(), impl.$primaryKey());
                if (impl.$unique() != null)
                    t.addUniqueKey(impl.getUnqualifiedName(), impl.$unique());
            }
            else
                // XXX log warning?
                ;
        for (Index index : query.$indexes())
            if (index instanceof IndexImpl) {
                IndexImpl impl = (IndexImpl) index;
                t.addIndex(impl.getUnqualifiedName(), impl.$fields(), impl.$unique());
            }
    }

    private final void accept0(AlterTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema(), false);

        MutableTable existing = schema.getTable(table.getUnqualifiedName());
        if (existing == null) {
            String message = "Table does not exist: " + table.getQualifiedName();
            if (!query.$ifExists())
                throw new DataAccessException(message);
            log.debug(message);
            return;
        }

        Field<?> addColumn = query.$addColumn();
        if (addColumn != null) {
            if (query.$addFirst())
                existing.addColumn(addColumn.getUnqualifiedName(), query.$addColumnType(), 0);
            else if (query.$addBefore() != null)
                existing.addColumn(addColumn.getUnqualifiedName(), query.$addColumnType(), indexOrFail(existing, query.$addBefore()));
            else if (query.$addAfter() != null)
                existing.addColumn(addColumn.getUnqualifiedName(), query.$addColumnType(), indexOrFail(existing, query.$addAfter()) + 1);
            else
                existing.addColumn(addColumn.getUnqualifiedName(), query.$addColumnType());
        }
    }

    private int indexOrFail(MutableTable existing, Field<?> field) {
        int result = existing.indexOf(field);

        if (result == -1)
            throw new DataAccessException("Field does not exist: " + field.getQualifiedName());

        return result;
    }

    private final void accept0(DropTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema(), false);

        // TODO schema == null
        MutableTable existing = schema.dropTable(table.getUnqualifiedName());
        if (existing == null) {
            String message = "Table does not exist: " + table.getQualifiedName();
            if (!query.$ifExists())
                throw new DataAccessException(message);
            log.debug(message);
            return;
        }
    }

    private final MutableSchema getSchema(Schema input, boolean create) {
        if (input == null)
            return currentSchema;

        MutableCatalog catalog = defaultCatalog;
        if (input.getCatalog() != null) {
            Name catalogName = input.getCatalog().getUnqualifiedName();
            if ((catalog = catalogs.get(catalogName)) == null && create)
                catalogs.put(catalogName, catalog = new MutableCatalog(catalogName));
        }

        if (catalog == null)
            return null;

        MutableSchema schema = defaultSchema;
        Name schemaName = input.getUnqualifiedName();
        if ((schema = catalog.getSchema(schemaName)) == null && create)
            // TODO createSchemaIfNotExists should probably be configurable
            schema = new MutableSchema(schemaName, catalog);

        return schema;
    }

    private static Name normalize(Name name) {
        if (name == null)
            return null;
        if (name instanceof UnqualifiedName) {
            if (name.quoted() == Quoted.QUOTED)
                return name;
            String lowerCase = name.first().toLowerCase();
            return name.first() == lowerCase ? name : unquotedName(lowerCase);
        }

        Name[] parts = name.parts();
        for (int i = 0; i < parts.length; i++)
            parts[i] = normalize(parts[i]);
        return DSL.name(parts);
    }

    private static class MutableCatalog extends CatalogImpl {
        private static final long   serialVersionUID = 9061637392590064527L;

        private List<MutableSchema> schemas          = new ArrayList<>();

        MutableCatalog(Name name) {
            super(normalize(name));
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<Schema> getSchemas() {
            return Collections.unmodifiableList((List<Schema>) (List<?>) schemas);
        }

        MutableSchema getSchema(Name name) {
            for (MutableSchema schema : schemas)
                if (schema.getUnqualifiedName().equals(name))
                    return schema;
            return null;
        }
    }

    private static class MutableSchema extends SchemaImpl {
        private static final long        serialVersionUID = -6704449383643804804L;

        private final MutableCatalog     catalog;
        private final List<MutableTable> tables           = new ArrayList<>();

        MutableSchema(Name name, MutableCatalog catalog) {
            super(normalize(name), null);
            this.catalog = catalog;
            catalog.schemas.add(this);
        }

        @Override
        public Catalog getCatalog() {
            return catalog;
        }

        @Override
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public List<Table<?>> getTables() {
            return Collections.unmodifiableList((List) tables);
        }

        boolean isEmpty() {
            return tables.isEmpty();
        }

        MutableTable getTable(Name name) {
            name = normalize(name);
            for (MutableTable table : tables)
                if (table.getUnqualifiedName().equals(name))
                    return table;
            return null;
        }

        MutableTable dropTable(Name name) {
            name = normalize(name);
            for (int i = 0; i < tables.size(); i++)
                if (tables.get(i).getUnqualifiedName().equals(name))
                    return tables.remove(i);
            return null;
        }

    }

    private static class MutableTable extends TableImpl<Record> {
        private static final long       serialVersionUID = -7474225786973716638L;

        private UniqueKey<Record>       primaryKey;
        private List<UniqueKey<Record>> keys;
        private List<Index>             indexes;

        MutableTable(Name name, MutableSchema schema, Comment comment) {
            super(normalize(name), schema, null, null, comment);
            schema.tables.add(this);
        }

        void addColumn(Name name, DataType<?> dataType) {
            createField(normalize(name), dataType);
        }

        void addColumn(Name name, DataType<?> dataType, int position) {
            createField(normalize(name), dataType, this, null, null, null, position);
        }

        // TODO Remove this implementation copy from AbstractTable again, replace by new
        // mutable meta model, see https://github.com/jOOQ/jOOQ/issues/8528#issuecomment-530238124
        protected static final <R extends Record, T, X, U> TableField<R, U> createField(Name name, DataType<T> type, Table<R> table, String comment, Converter<X, U> converter, Binding<T, X> binding, int position) {
            final Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
            final DataType<U> actualType =
                converter == null && binding == null
              ? (DataType<U>) type
              : type.asConvertedDataType(actualBinding);

            // [#5999] TODO: Allow for user-defined Names
            final TableFieldImpl<R, U> tableField = new TableFieldImpl<>(name, actualType, table, DSL.comment(comment), actualBinding);

            // [#1199] The public API of Table returns immutable field lists
            if (table instanceof TableImpl) {
                ((TableImpl<?>) table).fields0().add(position, tableField);
            }

            return tableField;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        void addPrimaryKey(Name name, Field<?>[] primaryKeyFields) {
            if (primaryKeyFields != null)
                this.primaryKey = new UniqueKeyImpl(this, normalize(name).first(), copiedFields(primaryKeyFields));
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        void addUniqueKey(Name name, Field<?>[] uniqueKeyFields) {
            if (uniqueKeyFields != null) {
                if (keys == null)
                    keys = new ArrayList<>();
                keys.add(new UniqueKeyImpl(this, normalize(name).first(), copiedFields(uniqueKeyFields)));
            }
        }

        void addIndex(Name name, SortField<?>[] indexFields, boolean unique) {
            // XXX copy fields?
            if (indexes == null)
                indexes = new ArrayList<>();
            indexes.add(Internal.createIndex(normalize(name).first(), this, indexFields, unique));
        }

        private final TableField<?, ?>[] copiedFields(Field<?>[] input) {
            TableField<?, ?>[] result = new TableField[input.length];
            for (int i = 0; i < input.length; i++)
                result[i] = (TableField<?, ?>) field(normalize(input[i].getUnqualifiedName()));
            return result;
        }

        @Override
        public UniqueKey<Record> getPrimaryKey() {
            return primaryKey;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public List<UniqueKey<Record>> getKeys() {
            if (primaryKey == null)
                return keys == null ? Collections.emptyList() : Collections.unmodifiableList((List) keys);
            else if (keys == null)
                return Collections.singletonList(primaryKey);

            List<UniqueKey<Record>> result = new ArrayList<>();
            result.add(primaryKey);
            result.addAll(keys);
            return Collections.unmodifiableList(result);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public List<Index> getIndexes() {
            return indexes == null ? Collections.emptyList() : Collections.unmodifiableList((List) indexes);
        }
    }

}
