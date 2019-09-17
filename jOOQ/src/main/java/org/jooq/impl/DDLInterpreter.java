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

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.DataAccessException;

final class DDLInterpreter {

    private final Map<Name, MutableCatalog> catalogs = new LinkedHashMap<>();
    private final Configuration             configuration;
    private final MutableCatalog            defaultCatalog;
    private final MutableSchema             defaultSchema;
    private MutableSchema                   currentSchema;

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
        if (query instanceof CreateTableImpl)
            accept0((CreateTableImpl) query);
        else if (query instanceof AlterTableImpl)
            accept0((AlterTableImpl) query);
        else if (query instanceof DropTableImpl)
            accept0((DropTableImpl) query);
        else
            throw new UnsupportedOperationException(query.getSQL());
    }

    private final void accept0(CreateTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema(), true);

        // TODO ifNotExists
        MutableTable t = new MutableTable(table.getUnqualifiedName(), schema);
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
                t.primaryKey(impl.$primaryKey());
            }
            else
                // XXX log warning?
                ;
    }

    private final void accept0(AlterTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema(), false);

        Field<?> addColumn = query.$addColumn();
        if (addColumn != null) {
            MutableTable existing = schema.getTable(table.getUnqualifiedName());
            existing.addColumn(addColumn.getUnqualifiedName(), query.$addColumnType());
        }
    }

    private final void accept0(DropTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema(), false);

        // TODO schema == null
        MutableTable oldTable = schema.dropTable(table.getUnqualifiedName());
        // TODO oldTable == null
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

        MutableTable getTable(Name name) {
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
        private static final long serialVersionUID = -7474225786973716638L;

        private UniqueKey<Record> primaryKey;

        MutableTable(Name name, MutableSchema schema) {
            super(normalize(name), schema);
            schema.tables.add(this);
        }

        void addColumn(Name name, DataType<?> dataType) {
            createField(normalize(name), dataType);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void primaryKey(Field<?>[] primaryKeyFields) {
            if (primaryKeyFields != null)
                this.primaryKey = new UniqueKeyImpl(this, copiedFields(primaryKeyFields));
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
    }

}
