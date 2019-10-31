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
import static org.jooq.impl.Cascade.CASCADE;
import static org.jooq.impl.Cascade.RESTRICT;
import static org.jooq.impl.ConstraintType.FOREIGN_KEY;
import static org.jooq.impl.ConstraintType.PRIMARY_KEY;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.intersect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Catalog;
import org.jooq.Comment;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.Named;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.TableOptions.TableType;
import org.jooq.UniqueKey;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataDefinitionException;
import org.jooq.impl.ConstraintImpl.Action;
import org.jooq.tools.JooqLogger;

@SuppressWarnings("serial")
final class DDLInterpreter {

    private static final JooqLogger         log      = JooqLogger.getLogger(DDLInterpreter.class);

    private final Configuration             configuration;
    private final Map<Name, MutableCatalog> catalogs = new LinkedHashMap<>();
    private final MutableCatalog            defaultCatalog;
    private final MutableSchema             defaultSchema;
    private MutableSchema                   currentSchema;

    DDLInterpreter(Configuration configuration) {
        this.configuration = configuration;
        this.defaultCatalog = new MutableCatalog(NO_NAME);
        this.catalogs.put(defaultCatalog.name, defaultCatalog);
        this.defaultSchema = new MutableSchema(NO_NAME, defaultCatalog);
        this.currentSchema = defaultSchema;
    }

    final Meta meta() {
        return new AbstractMeta(configuration) {
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
        log.info(query);

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

        else if (query instanceof CreateIndexImpl)
            accept0((CreateIndexImpl) query);
        else if (query instanceof AlterIndexImpl)
            accept0((AlterIndexImpl) query);
        else if (query instanceof DropIndexImpl)
            accept0((DropIndexImpl) query);

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

        // TODO We're doing this all the time. Can this be factored out without adding too much abstraction?
        MutableTable existing = schema.table(table);
        if (existing != null) {
            if (!query.$ifNotExists())
                throw alreadyExists(table, existing);

            return;
        }

        MutableTable mt = newTable(table, schema, query.$columnFields(), query.$columnTypes(), query.$select(), query.$comment(), query.$temporary() ? TableOptions.temporaryTable(query.$onCommit()) : TableOptions.table());

        for (Constraint constraint : query.$constraints()) {
            ConstraintImpl impl = (ConstraintImpl) constraint;

            if (impl.$primaryKey() != null)
                mt.primaryKey = new MutableUniqueKey((UnqualifiedName) impl.getUnqualifiedName(), mt, mt.fields(impl.$primaryKey(), true));
            else if (impl.$unique() != null)
                mt.uniqueKeys.add(new MutableUniqueKey((UnqualifiedName) impl.getUnqualifiedName(), mt, mt.fields(impl.$unique(), true)));
            else if (impl.$foreignKey() != null)
                addForeignKey(schema, mt, impl);
            else
                throw unsupportedQuery(query);
        }

        for (Index index : query.$indexes()) {
            IndexImpl impl = (IndexImpl) index;
            mt.indexes.add(new MutableIndex((UnqualifiedName) impl.getUnqualifiedName(), mt, mt.sortFields(impl.$fields()), impl.$unique()));
        }
    }

    private final void addForeignKey(MutableSchema schema, MutableTable mt, ConstraintImpl impl) {
        MutableTable mrf = schema.table(impl.$referencesTable());
        MutableUniqueKey mu = null;

        if (mrf == null)
            throw tableNotExists(impl.$referencesTable());

        List<MutableField> mfs = mt.fields(impl.$foreignKey(), true);
        List<MutableField> mrfs = mrf.fields(impl.$references(), true);

        if (!mrfs.isEmpty())
            mu = mrf.uniqueKey(mrfs);
        else if (mrf.primaryKey != null && mrf.primaryKey.keyFields.size() == mfs.size())
            mu = mrf.primaryKey;

        if (mu == null)
            throw primaryKeyNotExists();

        mt.foreignkeys.add(new MutableForeignKey(
            (UnqualifiedName) impl.getUnqualifiedName(), mt, mfs, mu, impl.$onDelete(), impl.$onUpdate()
        ));
    }

    private final void drop(List<MutableTable> tables, MutableTable table, Cascade cascade) {
        for (boolean check : cascade == CASCADE ? new boolean [] { false } : new boolean [] { true, false }) {
            if (table.primaryKey != null)
                cascade(table.primaryKey, null, check ? RESTRICT : CASCADE);

            cascade(table.uniqueKeys, null, check);
        }

        Iterator<MutableTable> it = tables.iterator();

        while (it.hasNext()) {
            if (it.next().name.equals(table.name)) {
                it.remove();
                break;
            }
        }
    }

    private final void dropColumns(MutableTable table, List<MutableField> fields, Cascade cascade) {
        Iterator<MutableIndex> it1 = table.indexes.iterator();

        for (boolean check : cascade == CASCADE ? new boolean [] { false } : new boolean [] { true, false }) {
            if (table.primaryKey != null) {
                if (intersect(table.primaryKey.keyFields, fields)) {
                    cascade(table.primaryKey, fields, check ? RESTRICT : CASCADE);

                    if (!check)
                        table.primaryKey = null;
                }
            }

            cascade(table.uniqueKeys, fields, check);
        }

        cascade(table.foreignkeys, fields, false);

        indexLoop:
        while (it1.hasNext()) {
            for (MutableSortField msf : it1.next().fields) {
                if (fields.contains(msf.field)) {
                    it1.remove();
                    continue indexLoop;
                }
            }
        }

        // Actual removal
        table.fields.removeAll(fields);
    }

    private final void cascade(List<? extends MutableKey> keys, List<MutableField> fields, boolean check) {
        Iterator<? extends MutableKey> it2 = keys.iterator();

        while (it2.hasNext()) {
            MutableKey key = it2.next();

            if (fields == null || intersect(key.keyFields, fields)) {
                if (key instanceof MutableUniqueKey)
                    cascade((MutableUniqueKey) key, fields, check ? RESTRICT : CASCADE);

                if (!check)
                    it2.remove();
            }
        }
    }

    private final void cascade(MutableUniqueKey key, List<MutableField> fields, Cascade cascade) {
        for (MutableTable mt : tables()) {
            Iterator<MutableForeignKey> it = mt.foreignkeys.iterator();

            while (it.hasNext()) {
                MutableForeignKey mfk = it.next();

                if (mfk.referencedKey.equals(key)) {
                    if (cascade == CASCADE)
                        it.remove();
                    else if (fields == null)
                        throw new DataDefinitionException("Cannot drop constraint " + key + " because other objects depend on it");
                    else if (fields.size() == 1)
                        throw new DataDefinitionException("Cannot drop column " + fields.get(0) + " because other objects depend on it");
                    else
                        throw new DataDefinitionException("Cannot drop columns " + fields + " because other objects depend on them");
                }
            }
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
        else if (!table(existing))
            throw objectNotTable(table);

        // TODO: Multi-add statements

        if (query.$addColumn() != null) {
            if (query.$addFirst())
                existing.fields.add(0, new MutableField((UnqualifiedName) query.$addColumn().getUnqualifiedName(), existing, query.$addColumnType()));
            else if (query.$addBefore() != null)
                existing.fields.add(indexOrFail(existing, query.$addBefore()), new MutableField((UnqualifiedName) query.$addColumn().getUnqualifiedName(), existing, query.$addColumnType()));
            else if (query.$addAfter() != null)
                existing.fields.add(indexOrFail(existing, query.$addAfter()) + 1, new MutableField((UnqualifiedName) query.$addColumn().getUnqualifiedName(), existing, query.$addColumnType()));
            else
                existing.fields.add(new MutableField((UnqualifiedName) query.$addColumn().getUnqualifiedName(), existing, query.$addColumnType()));
        }
        else if (query.$addConstraint() != null) {
            ConstraintImpl impl = (ConstraintImpl) query.$addConstraint();

            if (existing.constraint(impl) != null)
                throw constraintAlreadyExists(impl);
            else if (impl.$primaryKey() != null)

                // TODO: More nuanced error messages would be good, in general.
                if (existing.primaryKey != null)
                    throw constraintAlreadyExists(impl);
                else
                    existing.primaryKey = new MutableUniqueKey((UnqualifiedName) impl.getUnqualifiedName(), existing, existing.fields(impl.$primaryKey(), true));
            else if (impl.$unique() != null)
                existing.uniqueKeys.add(new MutableUniqueKey((UnqualifiedName) impl.getUnqualifiedName(), existing, existing.fields(impl.$unique(), true)));
            else if (impl.$foreignKey() != null)
                addForeignKey(schema, existing, impl);
            else
                throw unsupportedQuery(query);
        }
        else if (query.$alterColumn() != null) {
            MutableField existingField = existing.field(query.$alterColumn());

            if (existingField == null) {
                if (!query.$ifExistsColumn())
                    throw columnNotExists(query.$alterColumn());

                return;
            }

            if (query.$alterColumnNullability() != null)
                existingField.type = existingField.type.nullability(query.$alterColumnNullability());
            else if (query.$alterColumnType() != null)
                existingField.type = query.$alterColumnType();
            else if (query.$alterColumnDefault() != null)
                existingField.type = existingField.type.default_((Field) query.$alterColumnDefault());
            else if (query.$alterColumnDropDefault())
                existingField.type = existingField.type.default_((Field) null);
            else
                throw unsupportedQuery(query);
        }
        else if (query.$renameTo() != null && checkNotExists(schema, query.$renameTo())) {
            existing.name = (UnqualifiedName) query.$renameTo().getUnqualifiedName();
        }
        else if (query.$renameColumn() != null) {
            MutableField mf = existing.field(query.$renameColumn());

            if (mf == null)
                throw fieldNotExists(query.$renameColumn());
            else if (existing.field(query.$renameColumnTo()) != null)
                throw fieldAlreadyExists(query.$renameColumnTo());
            else
                mf.name = (UnqualifiedName) query.$renameColumnTo().getUnqualifiedName();
        }
        else if (query.$renameConstraint() != null) {
            MutableKey mk = existing.constraint(query.$renameConstraint());

            if (mk == null)
                throw constraintNotExists(query.$renameConstraint());
            else if (existing.constraint(query.$renameConstraintTo()) != null)
                throw constraintAlreadyExists(query.$renameConstraintTo());
            else
                mk.name = (UnqualifiedName) query.$renameConstraintTo().getUnqualifiedName();
        }
        else if (query.$dropColumns() != null) {
            List<MutableField> fields = existing.fields(query.$dropColumns().toArray(EMPTY_FIELD), false);

            if (fields.size() < query.$dropColumns().size() && !query.$ifExistsColumn())
                existing.fields(query.$dropColumns().toArray(EMPTY_FIELD), true);

            dropColumns(existing, fields, query.$dropCascade());
        }
        else if (query.$dropConstraint() != null) {
            ConstraintImpl impl = (ConstraintImpl) query.$dropConstraint();

            removal: {
                Iterator<MutableForeignKey> fks = existing.foreignkeys.iterator();
                while (fks.hasNext()) {
                    if (fks.next().name.equals(impl.getUnqualifiedName())) {
                        fks.remove();
                        break removal;
                    }
                }

                if (query.$dropConstraintType() != FOREIGN_KEY) {
                    Iterator<MutableUniqueKey> uks = existing.uniqueKeys.iterator();

                    while (uks.hasNext()) {
                        MutableUniqueKey key = uks.next();

                        if (key.name.equals(impl.getUnqualifiedName())) {
                            cascade(key, null, query.$dropCascade());
                            uks.remove();
                            break removal;
                        }
                    }

                    if (existing.primaryKey != null) {
                        if (existing.primaryKey.name.equals(impl.getUnqualifiedName())) {
                            cascade(existing.primaryKey, null, query.$dropCascade());
                            existing.primaryKey = null;
                            break removal;
                        }
                    }
                }

                throw constraintNotExists(query.$dropConstraint());
            }
        }
        else if (query.$dropConstraintType() == PRIMARY_KEY) {
            if (existing.primaryKey != null)
                existing.primaryKey = null;
            else
                throw primaryKeyNotExists();
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
        else if (!table(existing))
            throw objectNotTable(table);
        else if (query.$temporary() && existing.options.type() != TableType.TEMPORARY)
            throw objectNotTemporaryTable(table);

        drop(schema.tables, existing, query.$cascade());
    }

    private final void accept0(CreateViewImpl<?> query) {
        Table<?> table = query.$view();
        MutableSchema schema = getSchema(table.getSchema());

        MutableTable existing = schema.table(table);
        if (existing != null) {
            if (!view(existing))
                throw objectNotView(table);
            else if (query.$orReplace())
                drop(schema.tables, existing, RESTRICT);
            else if (!query.$ifNotExists())
                throw viewAlreadyExists(table);
            else
                return;
        }

        List<DataType<?>> columnTypes = new ArrayList<>();
        for (Field<?> f : query.$select().getSelect())
            columnTypes.add(f.getDataType());

        newTable(table, schema, Arrays.asList(query.$fields()), columnTypes, query.$select(), null, TableOptions.view(query.$select()));
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
        else if (!view(existing))
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
        else if (!view(existing))
            throw objectNotView(table);

        drop(schema.tables, existing, RESTRICT);
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
        ms.minValue = query.$noMinvalue() ? null : query.$minvalue();
        ms.maxValue = query.$noMaxvalue() ? null : query.$maxvalue();
        ms.cycle = query.$cycle();
        ms.cache = query.$noCache() ? null : query.$cache();
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

    private final void accept0(CreateIndexImpl query) {
        Index index = query.$index();
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema());
        MutableTable mt = schema.table(table);

        if (mt == null)
            throw tableNotExists(table);

        MutableIndex existing = mt.index(index);
        List<MutableSortField> mtf = mt.sortFields(query.$sortFields());

        if (existing != null) {
            if (!query.$ifNotExists())
                throw indexAlreadyExists(index);

            return;
        }

        mt.indexes.add(new MutableIndex((UnqualifiedName) index.getUnqualifiedName(), mt, mtf, query.$unique()));
    }

    private final void accept0(AlterIndexImpl query) {
        Index index = query.$index();
        Table<?> table = query.$on() != null ? query.$on() : index.getTable();
        MutableIndex existing = index(index, table, query.$ifExists(), true);

        if (existing != null) {
            if (query.$renameTo() != null)
                if (index(query.$renameTo(), table, false, false) == null)
                    existing.name = (UnqualifiedName) query.$renameTo().getUnqualifiedName();
                else
                    throw indexAlreadyExists(query.$renameTo());
            else
                throw unsupportedQuery(query);
        }
    }

    private final void accept0(DropIndexImpl query) {
        Index index = query.$index();
        Table<?> table = query.$on() != null ? query.$on() : index.getTable();
        MutableIndex existing = index(index, table, query.$ifExists(), true);

        if (existing != null)
            existing.table.indexes.remove(existing);
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

    // TODO: Surely, these exception utilities can be refactored / improved?
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

    private static final DataDefinitionException objectNotTemporaryTable(Table<?> table) {
        return new DataDefinitionException("Object is not a temporary table: " + table.getQualifiedName());
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

    private static final DataDefinitionException primaryKeyNotExists() {
        return new DataDefinitionException("Primary key does not exist");
    }

    private static final DataDefinitionException constraintAlreadyExists(Constraint constraint) {
        return new DataDefinitionException("Constraint already exists: " + constraint.getQualifiedName());
    }

    private static final DataDefinitionException constraintNotExists(Constraint constraint) {
        return new DataDefinitionException("Constraint does not exist: " + constraint.getQualifiedName());
    }

    private static final DataDefinitionException indexNotExists(Index index) {
        return new DataDefinitionException("Index does not exist: " + index.getQualifiedName());
    }

    private static final DataDefinitionException indexAlreadyExists(Index index) {
        return new DataDefinitionException("Index already exists: " + index.getQualifiedName());
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

    private static final boolean view(MutableTable mt) {
        TableType type = mt.options.type();
        return type == TableType.VIEW || type == TableType.MATERIALIZED_VIEW;
    }

    private static final boolean table(MutableTable mt) {
        TableType type = mt.options.type();
        return type == TableType.TABLE || type == TableType.TEMPORARY;
    }

    private final Iterable<MutableTable> tables() {
        // TODO: Make this lazy
        List<MutableTable> result = new ArrayList<>();

        for (MutableCatalog catalog : catalogs.values())
            for (MutableSchema schema : catalog.schemas)
                for (MutableTable table : schema.tables)
                    result.add(table);

        return result;
    }

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
        TableOptions options
    ) {
        MutableTable t = new MutableTable((UnqualifiedName) table.getUnqualifiedName(), schema, comment, options);

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

    private final MutableIndex index(Index index, Table<?> table, boolean ifExists, boolean throwIfNotExists) {
        MutableSchema ms;
        MutableTable mt = null;
        MutableIndex mi = null;

        if (table != null) {
            ms = getSchema(table.getSchema());
            mt = ms.table(table);
        }
        else {
            for (MutableTable mt1 : tables()) {
                if ((mi = mt1.index(index)) != null) {
                    mt = mt1;
                    ms = mt1.schema;
                    break;
                }
            }
        }

        if (mt != null)
            mi = mt.index(index);
        else if (table != null && throwIfNotExists)
            throw tableNotExists(table);

        if (mi == null && !ifExists && throwIfNotExists)
            throw indexNotExists(index);

        return mi;
    }

    private static final boolean checkNotExists(MutableSchema schema, Table<?> table) {
        MutableTable mt = schema.table(table);

        if (mt != null)
            throw alreadyExists(table, mt);

        return true;
    }

    private static final DataDefinitionException alreadyExists(Table<?> t, MutableTable mt) {
        if (view(mt))
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

    // TODO We shouldn't need this "normalize" method, but instead, work out how
    //      we can implement equivalent logic using configuration.
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

            // TODO: I'm not sure we should let the constructor add "this" to
            //       someone else's collection. We're probably reusing code, but
            //       it seems surprising and is already inconsistent
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
        MutableSchema           schema;
        List<MutableField>      fields      = new ArrayList<>();
        MutableUniqueKey        primaryKey;
        List<MutableUniqueKey>  uniqueKeys  = new ArrayList<>();
        List<MutableForeignKey> foreignkeys = new ArrayList<>();
        List<MutableIndex>      indexes     = new ArrayList<>();
        TableOptions            options;

        MutableTable(UnqualifiedName name, MutableSchema schema, Comment comment, TableOptions options) {
            super(name, comment);

            this.schema = schema;
            this.options = options;
            schema.tables.add(this);
        }

        final MutableKey constraint(Constraint constraint) {
            for (MutableForeignKey mfk : foreignkeys)
                if (mfk.name.equals(constraint.getUnqualifiedName()))
                    return mfk;

            for (MutableUniqueKey muk : uniqueKeys)
                if (muk.name.equals(constraint.getUnqualifiedName()))
                    return muk;

            if (primaryKey != null && primaryKey.name.equals(constraint.getUnqualifiedName()))
                return primaryKey;
            else
                return null;
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

        final MutableIndex index(Index i) {
            Name n = i.getUnqualifiedName();

            for (MutableIndex mi : indexes)
                if (mi.name.equals(n))
                    return mi;

            return null;
        }

        final MutableUniqueKey uniqueKey(List<MutableField> mrfs) {
            if (primaryKey != null)
                if (primaryKey.keyFields.equals(mrfs))
                    return primaryKey;

            for (MutableUniqueKey mu : uniqueKeys)
                if (mu.keyFields.equals(mrfs))
                    return mu;

            return null;
        }

        private final class InterpretedTable extends TableImpl<Record> {
            InterpretedTable(MutableSchema.InterpretedSchema schema) {
                super(MutableTable.this.name, schema, null, null, null, null, MutableTable.this.comment, MutableTable.this.options);

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
            public List<ForeignKey<Record, ?>> getReferences() {
                List<ForeignKey<Record, ?>> result = new ArrayList<>();

                for (MutableForeignKey fk : MutableTable.this.foreignkeys)
                    result.add(interpretedKey(fk));

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

                TableField<Record, ?>[] f = new TableField[key.keyFields.size()];

                for (int i = 0; i < f.length; i++)
                    f[i] = (TableField<Record, ?>) field(key.keyFields.get(i).name);

                // TODO: Cache these?
                return new UniqueKeyImpl<Record>(this, key.name.last(), f);
            }

            @SuppressWarnings("unchecked")
            private final ForeignKey<Record, ?> interpretedKey(MutableForeignKey key) {
                if (key == null)
                    return null;

                TableField<Record, ?>[] f = new TableField[key.keyFields.size()];

                for (int i = 0; i < f.length; i++)
                    f[i] = (TableField<Record, ?>) field(key.keyFields.get(i).name);

                // TODO: Cache these?
                return new ReferenceImpl<>(key.referencedKey.keyTable.new InterpretedTable((MutableSchema.InterpretedSchema) getSchema()).interpretedKey(key.referencedKey), this, key.name.last(), f);
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
        Field<?>      minValue;
        Field<?>      maxValue;
        boolean       cycle;
        Field<?>      cache;

        MutableSequence(UnqualifiedName name, MutableSchema schema) {
            super(name);

            this.schema = schema;
            schema.sequences.add(this);
        }

        private final class InterpretedSequence extends SequenceImpl<Long> {
            @SuppressWarnings("unchecked")
            InterpretedSequence(Schema schema) {
                super(MutableSequence.this.name, schema, BIGINT, false,
                    (Field<Long>) startWith, (Field<Long>) incrementBy, (Field<Long>) minValue, (Field<Long>) maxValue, cycle, (Field<Long>) cache);

                // [#7752] TODO: Pass additional flags like START WITH to
                //         SequenceImpl when this is ready.
            }
        }
    }

    private static abstract class MutableKey extends MutableNamed {
        MutableTable       keyTable;
        List<MutableField> keyFields;

        MutableKey(UnqualifiedName name, MutableTable table, List<MutableField> fields) {
            super(name);

            this.keyTable = table;
            this.keyFields = fields;
        }
    }

    private static final class MutableUniqueKey extends MutableKey {
        MutableUniqueKey(UnqualifiedName name, MutableTable keyTable, List<MutableField> keyFields) {
            super(name, keyTable, keyFields);
        }
    }

    private static final class MutableForeignKey extends MutableKey {
        MutableUniqueKey referencedKey;
        Action onDelete;
        Action onUpdate;

        MutableForeignKey(
            UnqualifiedName name,
            MutableTable keyTable,
            List<MutableField> keyFields,
            MutableUniqueKey referencedKey,
            Action onDelete,
            Action onUpdate
        ) {
            super(name, keyTable, keyFields);

            this.referencedKey = referencedKey;
            this.onDelete = onDelete;
            this.onUpdate = onUpdate;
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
