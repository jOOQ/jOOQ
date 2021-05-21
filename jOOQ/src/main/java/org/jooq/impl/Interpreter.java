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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.jooq.Name.Quoted.QUOTED;
import static org.jooq.conf.SettingsTools.interpreterLocale;
import static org.jooq.impl.AbstractName.NO_NAME;
import static org.jooq.impl.Cascade.CASCADE;
import static org.jooq.impl.Cascade.RESTRICT;
import static org.jooq.impl.ConstraintType.FOREIGN_KEY;
import static org.jooq.impl.ConstraintType.PRIMARY_KEY;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.dataTypes;
import static org.jooq.impl.Tools.findAny;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.normaliseNameCase;
import static org.jooq.impl.Tools.reverseIterable;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Comment;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.DataType;
import org.jooq.Delete;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.FieldOrConstraint;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Insert;
import org.jooq.Merge;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Named;
import org.jooq.Nullability;
import org.jooq.OrderField;
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
import org.jooq.Update;
import org.jooq.conf.InterpreterNameLookupCaseSensitivity;
import org.jooq.conf.InterpreterSearchSchema;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataDefinitionException;
import org.jooq.impl.ConstraintImpl.Action;
import org.jooq.tools.JooqLogger;

@SuppressWarnings({ "rawtypes", "unchecked" })
final class Interpreter {

    private static final JooqLogger                              log                    = JooqLogger.getLogger(Interpreter.class);

    private final Configuration                                  configuration;
    private final InterpreterNameLookupCaseSensitivity           caseSensitivity;
    private final Locale                                         locale;
    private final Map<Name, MutableCatalog>                      catalogs               = new LinkedHashMap<>();
    private final MutableCatalog                                 defaultCatalog;
    private final MutableSchema                                  defaultSchema;
    private MutableSchema                                        currentSchema;
    private boolean                                              delayForeignKeyDeclarations;
    private final Deque<DelayedForeignKey>                       delayedForeignKeyDeclarations;

    // Caches
    private final Map<Name, MutableCatalog.InterpretedCatalog>   interpretedCatalogs    = new HashMap<>();
    private final Map<Name, MutableSchema.InterpretedSchema>     interpretedSchemas     = new HashMap<>();
    private final Map<Name, MutableTable.InterpretedTable>       interpretedTables      = new HashMap<>();
    private final Map<Name, UniqueKeyImpl<Record>>               interpretedUniqueKeys  = new HashMap<>();
    private final Map<Name, ReferenceImpl<Record, ?>>            interpretedForeignKeys = new HashMap<>();
    private final Map<Name, Index>                               interpretedIndexes     = new HashMap<>();
    private final Map<Name, MutableDomain.InterpretedDomain>     interpretedDomains     = new HashMap<>();
    private final Map<Name, MutableSequence.InterpretedSequence> interpretedSequences   = new HashMap<>();

    Interpreter(Configuration configuration) {
        this.configuration = configuration;
        this.delayForeignKeyDeclarations = TRUE.equals(configuration.settings().isInterpreterDelayForeignKeyDeclarations());
        this.delayedForeignKeyDeclarations = new ArrayDeque<>();
        this.caseSensitivity = caseSensitivity(configuration);
        this.locale = interpreterLocale(configuration.settings());
        this.defaultCatalog = new MutableCatalog(NO_NAME);
        this.catalogs.put(defaultCatalog.name(), defaultCatalog);
        this.defaultSchema = new MutableSchema(NO_NAME, defaultCatalog);
    }

    final Meta meta() {
        applyDelayedForeignKeys();

        return new AbstractMeta(configuration) {

            @Override
            final List<Catalog> getCatalogs0() throws DataAccessException {
                return map(catalogs.values(), c -> c.interpretedCatalog());
            }
        };
    }

    // -------------------------------------------------------------------------
    // Interpretation logic
    // -------------------------------------------------------------------------

    final void accept(Query query) {
        invalidateCaches();

        if (log.isDebugEnabled())
            log.debug(query);

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
        else if (query instanceof TruncateImpl)
            accept0((TruncateImpl<?>) query);

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

        else if (query instanceof CreateDomainImpl)
            accept0((CreateDomainImpl<?>) query);
        else if (query instanceof AlterDomainImpl)
            accept0((AlterDomainImpl<?>) query);
        else if (query instanceof DropDomainImpl)
            accept0((DropDomainImpl) query);

        else if (query instanceof CommentOnImpl)
            accept0((CommentOnImpl) query);

        // TODO: Add support for catalogs
        // else if (query instanceof SetCatalog)
        //     accept0((SetCatalog) query);
        else if (query instanceof SetSchema)
            accept0((SetSchema) query);

        // The interpreter cannot handle DML statements. We're ignoring these for now.
        else if (query instanceof Select)
            ;
        else if (query instanceof Update)
            ;
        else if (query instanceof Insert)
            ;
        else if (query instanceof Delete)
            ;
        else if (query instanceof Merge)
            ;

        else if (query instanceof SetCommand)
            accept0((SetCommand) query);

        else
            throw unsupportedQuery(query);
    }

    private final void invalidateCaches() {
        interpretedCatalogs.clear();
        interpretedSchemas.clear();
        interpretedTables.clear();
        interpretedUniqueKeys.clear();
        interpretedForeignKeys.clear();
        interpretedIndexes.clear();
        interpretedSequences.clear();
    }

    private final void accept0(CreateSchemaImpl query) {
        Schema schema = query.$schema();

        if (getSchema(schema, false) != null) {
            if (!query.$createSchemaIfNotExists())
                throw alreadyExists(schema);

            return;
        }

        getSchema(schema, true);
    }

    private final void accept0(AlterSchemaImpl query) {
        Schema schema = query.$schema();
        Schema renameTo = query.$renameTo();

        MutableSchema oldSchema = getSchema(schema);
        if (oldSchema == null) {
            if (!query.$alterSchemaIfExists())
                throw notExists(schema);

            return;
        }

        if (renameTo != null) {
            if (getSchema(renameTo, false) != null)
                throw alreadyExists(renameTo);

            oldSchema.name((UnqualifiedName) renameTo.getUnqualifiedName());
            return;
        }
        else
            throw unsupportedQuery(query);
    }

    private final void accept0(DropSchemaImpl query) {
        Schema schema = query.$schema();
        MutableSchema mutableSchema = getSchema(schema);

        if (mutableSchema == null) {
            if (!query.$dropSchemaIfExists())
                throw notExists(schema);

            return;
        }

        if (mutableSchema.isEmpty() || query.$cascade() == Cascade.CASCADE)
            mutableSchema.catalog.schemas.remove(mutableSchema);
        else
            throw schemaNotEmpty(schema);
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

        for (Constraint constraint : query.$constraints())
            addConstraint(query, (ConstraintImpl) constraint, mt);

        for (Index index : query.$indexes()) {
            IndexImpl impl = (IndexImpl) index;
            mt.indexes.add(new MutableIndex((UnqualifiedName) impl.getUnqualifiedName(), mt, mt.sortFields(asList(impl.$fields())), impl.$unique()));
        }
    }

    private final void addForeignKey(MutableTable mt, ConstraintImpl impl) {
        if (delayForeignKeyDeclarations)
            delayForeignKey(mt, impl);
        else
            addForeignKey0(mt, impl);
    }

    private static class DelayedForeignKey {
        final MutableTable   table;
        final ConstraintImpl constraint;

        DelayedForeignKey(MutableTable mt, ConstraintImpl constraint) {
            this.table = mt;
            this.constraint = constraint;
        }
    }

    private final void delayForeignKey(MutableTable mt, ConstraintImpl impl) {
        delayedForeignKeyDeclarations.add(new DelayedForeignKey(mt, impl));
    }

    private final void applyDelayedForeignKeys() {
        Iterator<DelayedForeignKey> it = delayedForeignKeyDeclarations.iterator();

        while (it.hasNext()) {
            DelayedForeignKey key = it.next();
            addForeignKey0(key.table, key.constraint);
            it.remove();
        }
    }

    private final void addForeignKey0(MutableTable mt, ConstraintImpl impl) {
        MutableSchema ms = getSchema(impl.$referencesTable().getSchema());

        if (ms == null)
            throw notExists(impl.$referencesTable().getSchema());

        MutableTable mrf = ms.table(impl.$referencesTable());
        MutableUniqueKey mu = null;

        if (mrf == null)
            throw notExists(impl.$referencesTable());

        List<MutableField> mfs = mt.fields(impl.$foreignKey(), true);
        List<MutableField> mrfs = mrf.fields(impl.$references(), true);

        if (!mrfs.isEmpty())
            mu = mrf.uniqueKey(mrfs);
        else if (mrf.primaryKey != null && mrf.primaryKey.fields.size() == mfs.size())
            mrfs = (mu = mrf.primaryKey).fields;

        if (mu == null)
            throw primaryKeyNotExists(impl.$referencesTable());

        mt.foreignKeys.add(new MutableForeignKey(
            (UnqualifiedName) impl.getUnqualifiedName(), mt, mfs, mu, mrfs, impl.$onDelete(), impl.$onUpdate(), impl.$enforced()
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
            if (it.next().nameEquals(table.name())) {
                it.remove();
                break;
            }
        }
    }

    private final void dropColumns(MutableTable table, List<MutableField> fields, Cascade cascade) {
        Iterator<MutableIndex> it1 = table.indexes.iterator();

        for (boolean check : cascade == CASCADE ? new boolean [] { false } : new boolean [] { true, false }) {
            if (table.primaryKey != null) {
                if (anyMatch(table.primaryKey.fields, t1 -> fields.contains(t1))) {
                    cascade(table.primaryKey, fields, check ? RESTRICT : CASCADE);

                    if (!check)
                        table.primaryKey = null;
                }
            }

            cascade(table.uniqueKeys, fields, check);
        }

        cascade(table.foreignKeys, fields, false);

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

            if (fields == null || anyMatch(key.fields, t1 -> fields.contains(t1))) {
                if (key instanceof MutableUniqueKey)
                    cascade((MutableUniqueKey) key, fields, check ? RESTRICT : CASCADE);

                if (!check)
                    it2.remove();
            }
        }
    }

    private final void cascade(MutableUniqueKey key, List<MutableField> fields, Cascade cascade) {
        for (MutableTable mt : tables()) {
            Iterator<MutableForeignKey> it = mt.foreignKeys.iterator();

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

    private final void accept0(AlterTableImpl query) {
        Table<?> table = query.$table();
        MutableSchema schema = getSchema(table.getSchema());

        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$ifExists())
                throw notExists(table);

            return;
        }
        else if (!existing.options.type().isTable())
            throw objectNotTable(table);

        if (query.$add() != null) {
            for (FieldOrConstraint fc : query.$add())
                if (fc instanceof Field && find(existing.fields, (Field<?>) fc) != null)
                    throw alreadyExists(fc);
                else if (fc instanceof Constraint && !fc.getUnqualifiedName().empty() && existing.constraint((Constraint) fc) != null)
                    throw alreadyExists(fc);

            // TODO: ReverseIterable is not a viable approach if we also allow constraints to be added this way
            if (query.$addFirst()) {
                for (Field<?> f : assertFields(query, reverseIterable(query.$add())))
                    addField(existing, 0, (UnqualifiedName) f.getUnqualifiedName(), ((Field<?>) f).getDataType());
            }
            else if (query.$addBefore() != null) {
                int index = indexOrFail(existing.fields, query.$addBefore());

                for (Field<?> f : assertFields(query, reverseIterable(query.$add())))
                    addField(existing, index, (UnqualifiedName) f.getUnqualifiedName(), ((Field<?>) f).getDataType());
            }
            else if (query.$addAfter() != null) {
                int index = indexOrFail(existing.fields, query.$addAfter()) + 1;

                for (Field<?> f : assertFields(query, reverseIterable(query.$add())))
                    addField(existing, index, (UnqualifiedName) f.getUnqualifiedName(), ((Field<?>) f).getDataType());
            }
            else {
                for (FieldOrConstraint fc : query.$add())
                    if (fc instanceof Field)
                        addField(existing, Integer.MAX_VALUE, (UnqualifiedName) fc.getUnqualifiedName(), ((Field<?>) fc).getDataType());
                    else if (fc instanceof Constraint)
                        addConstraint(query, (ConstraintImpl) fc, existing);
                    else
                        throw unsupportedQuery(query);
            }
        }
        else if (query.$addColumn() != null) {
            if (find(existing.fields, query.$addColumn()) != null)
                if (!query.$ifNotExistsColumn())
                    throw alreadyExists(query.$addColumn());
                else
                    return;

            UnqualifiedName name = (UnqualifiedName) query.$addColumn().getUnqualifiedName();
            DataType<?> dataType = query.$addColumnType();

            if (query.$addFirst())
                addField(existing, 0, name, dataType);
            else if (query.$addBefore() != null)
                addField(existing, indexOrFail(existing.fields, query.$addBefore()), name, dataType);
            else if (query.$addAfter() != null)
                addField(existing, indexOrFail(existing.fields, query.$addAfter()) + 1, name, dataType);
            else
                addField(existing, Integer.MAX_VALUE, name, dataType);
        }
        else if (query.$addConstraint() != null) {
            addConstraint(query, (ConstraintImpl) query.$addConstraint(), existing);
        }
        else if (query.$alterColumn() != null) {
            MutableField existingField = find(existing.fields, query.$alterColumn());

            if (existingField == null)
                if (!query.$ifExistsColumn())
                    throw notExists(query.$alterColumn());
                else
                    return;

            if (query.$alterColumnNullability() != null)
                existingField.type = existingField.type.nullability(query.$alterColumnNullability());
            else if (query.$alterColumnType() != null)
                existingField.type = query.$alterColumnType().nullability(
                      query.$alterColumnType().nullability() == Nullability.DEFAULT
                    ? existingField.type.nullability()
                    : query.$alterColumnType().nullability()
                );
            else if (query.$alterColumnDefault() != null)
                existingField.type = existingField.type.default_((Field) query.$alterColumnDefault());
            else if (query.$alterColumnDropDefault())
                existingField.type = existingField.type.default_((Field) null);
            else
                throw unsupportedQuery(query);
        }
        else if (query.$renameTo() != null && checkNotExists(schema, query.$renameTo())) {
            existing.name((UnqualifiedName) query.$renameTo().getUnqualifiedName());
        }
        else if (query.$renameColumn() != null) {
            MutableField mf = find(existing.fields, query.$renameColumn());

            if (mf == null)
                throw notExists(query.$renameColumn());
            else if (find(existing.fields, query.$renameColumnTo()) != null)
                throw alreadyExists(query.$renameColumnTo());
            else
                mf.name((UnqualifiedName) query.$renameColumnTo().getUnqualifiedName());
        }
        else if (query.$renameConstraint() != null) {
            MutableConstraint mc = existing.constraint(query.$renameConstraint(), true);

            if (existing.constraint(query.$renameConstraintTo()) != null)
                throw alreadyExists(query.$renameConstraintTo());
            else
                mc.name((UnqualifiedName) query.$renameConstraintTo().getUnqualifiedName());
        }
        else if (query.$alterConstraint() != null) {
            existing.constraint(query.$alterConstraint(), true).enforced = query.$alterConstraintEnforced();
        }
        else if (query.$dropColumns() != null) {
            List<MutableField> fields = existing.fields(query.$dropColumns().toArray(EMPTY_FIELD), false);

            if (fields.size() < query.$dropColumns().size() && !query.$ifExistsColumn())
                existing.fields(query.$dropColumns().toArray(EMPTY_FIELD), true);

            dropColumns(existing, fields, query.$dropCascade());
        }
        else if (query.$dropConstraint() != null) dropConstraint: {
            ConstraintImpl impl = (ConstraintImpl) query.$dropConstraint();

            if (impl.getUnqualifiedName().empty()) {
                if (impl.$foreignKey() != null) {
                    throw new DataDefinitionException("Cannot drop unnamed foreign key");
                }
                else if (impl.$check() != null) {
                    throw new DataDefinitionException("Cannot drop unnamed check constraint");
                }
                else if (impl.$unique() != null) {
                    Iterator<MutableUniqueKey> uks = existing.uniqueKeys.iterator();

                    while (uks.hasNext()) {
                        MutableUniqueKey key = uks.next();

                        if (key.fieldsEquals(impl.$unique())) {
                            cascade(key, null, query.$dropCascade());
                            uks.remove();
                            break dropConstraint;
                        }
                    }
                }
            }

            else {
                Iterator<MutableForeignKey> fks = existing.foreignKeys.iterator();
                while (fks.hasNext()) {
                    if (fks.next().nameEquals((UnqualifiedName) impl.getUnqualifiedName())) {
                        fks.remove();
                        break dropConstraint;
                    }
                }

                if (query.$dropConstraintType() != FOREIGN_KEY) {
                    Iterator<MutableUniqueKey> uks = existing.uniqueKeys.iterator();

                    while (uks.hasNext()) {
                        MutableUniqueKey key = uks.next();

                        if (key.nameEquals((UnqualifiedName) impl.getUnqualifiedName())) {
                            cascade(key, null, query.$dropCascade());
                            uks.remove();
                            break dropConstraint;
                        }
                    }

                    Iterator<MutableCheck> chks = existing.checks.iterator();

                    while (chks.hasNext()) {
                        MutableCheck check = chks.next();

                        if (check.nameEquals((UnqualifiedName) impl.getUnqualifiedName())) {
                            chks.remove();
                            break dropConstraint;
                        }
                    }

                    if (existing.primaryKey != null) {
                        if (existing.primaryKey.nameEquals((UnqualifiedName) impl.getUnqualifiedName())) {
                            cascade(existing.primaryKey, null, query.$dropCascade());
                            existing.primaryKey = null;
                            break dropConstraint;
                        }
                    }
                }
            }

            Iterator<DelayedForeignKey> it = delayedForeignKeyDeclarations.iterator();
            while (it.hasNext()) {
                DelayedForeignKey key = it.next();

                if (existing.equals(key.table) && key.constraint.getUnqualifiedName().equals(impl.getUnqualifiedName())) {
                    it.remove();
                    break dropConstraint;
                }
            }

            if (!query.$ifExistsConstraint())
                throw notExists(query.$dropConstraint());
        }
        else if (query.$dropConstraintType() == PRIMARY_KEY) {
            if (existing.primaryKey != null)
                existing.primaryKey = null;
            else
                throw primaryKeyNotExists(table);
        }
        else
            throw unsupportedQuery(query);
    }

    private final Iterable<Field<?>> assertFields(Query query, Iterable<FieldOrConstraint> fields) {
        return () -> new Iterator<Field<?>>() {
            final Iterator<FieldOrConstraint> it = fields.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Field<?> next() {
                FieldOrConstraint next = it.next();

                if (next instanceof Field)
                    return (Field<?>) next;
                else
                    throw unsupportedQuery(query);
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }

    private final void addField(MutableTable existing, int index, UnqualifiedName name, DataType<?> dataType) {
        MutableField field = new MutableField(name, existing, dataType);

        for (MutableField mf : existing.fields)
            if (mf.nameEquals(field.name()))
                throw columnAlreadyExists(field.qualifiedName());
            else if (mf.type.identity() && dataType.identity())
                throw new DataDefinitionException("Table can only have one identity: " + mf.qualifiedName());

        if (index == Integer.MAX_VALUE)
            existing.fields.add(field);
        else
            existing.fields.add(index, field);
    }

    private final void addConstraint(Query query, ConstraintImpl impl, MutableTable existing) {
        if (!impl.getUnqualifiedName().empty() && existing.constraint(impl) != null)
            throw alreadyExists(impl);

        if (impl.$primaryKey() != null)
            if (existing.primaryKey != null)
                throw alreadyExists(impl);
            else
                existing.primaryKey = new MutableUniqueKey((UnqualifiedName) impl.getUnqualifiedName(), existing, existing.fields(impl.$primaryKey(), true), impl.$enforced());
        else if (impl.$unique() != null)
            existing.uniqueKeys.add(new MutableUniqueKey((UnqualifiedName) impl.getUnqualifiedName(), existing, existing.fields(impl.$unique(), true), impl.$enforced()));
        else if (impl.$foreignKey() != null)
            addForeignKey(existing, impl);
        else if (impl.$check() != null)
            existing.checks.add(new MutableCheck((UnqualifiedName) impl.getUnqualifiedName(), existing, impl.$check(), impl.$enforced()));
        else
            throw unsupportedQuery(query);
    }

    private final void accept0(DropTableImpl query) {
        Table<?> table = query.$table();

        MutableSchema schema = getSchema(table.getSchema());
        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$dropTableIfExists())
                throw notExists(table);

            return;
        }
        else if (!existing.options.type().isTable())
            throw objectNotTable(table);
        else if (query.$temporary() && existing.options.type() != TableType.TEMPORARY)
            throw objectNotTemporaryTable(table);

        drop(schema.tables, existing, query.$cascade());
    }

    private final void accept0(TruncateImpl<?> query) {
        Table<?> table = query.$table();

        MutableSchema schema = getSchema(table.getSchema());
        MutableTable existing = schema.table(table);

        if (existing == null)
            throw notExists(table);
        else if (!existing.options.type().isTable())
            throw objectNotTable(table);
        else if (query.$cascade() != Cascade.CASCADE && existing.hasReferencingKeys())
            throw new DataDefinitionException("Cannot truncate table referenced by other tables. Use CASCADE: " + table);
    }

    private final void accept0(CreateViewImpl<?> query) {
        Table<?> table = query.$view();
        MutableSchema schema = getSchema(table.getSchema(), true);

        MutableTable existing = schema.table(table);
        if (existing != null) {
            if (!existing.options.type().isView())
                throw objectNotView(table);
            else if (query.$orReplace())
                drop(schema.tables, existing, RESTRICT);
            else if (!query.$ifNotExists())
                throw viewAlreadyExists(table);
            else
                return;
        }

        List<DataType<?>> columnTypes = query.$select() != null
            ? dataTypes(query.$select())
            : map(query.$fields(), f -> f.getDataType());

        newTable(table, schema, asList(query.$fields()), columnTypes, query.$select(), null, TableOptions.view(query.$select()));
    }

    private final void accept0(AlterViewImpl query) {
        Table<?> table = query.$view();
        MutableSchema schema = getSchema(table.getSchema());

        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$alterViewIfExists())
                throw viewNotExists(table);

            return;
        }
        else if (!existing.options.type().isView())
            throw objectNotView(table);

        Table<?> renameTo = query.$renameTo();
        if (renameTo != null && checkNotExists(schema, renameTo))
            existing.name((UnqualifiedName) renameTo.getUnqualifiedName());
        else
            throw unsupportedQuery(query);
    }

    private final void accept0(DropViewImpl query) {
        Table<?> table = query.$view();
        MutableSchema schema = getSchema(table.getSchema());

        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$dropViewIfExists())
                throw viewNotExists(table);

            return;
        }
        else if (!existing.options.type().isView())
            throw objectNotView(table);

        drop(schema.tables, existing, RESTRICT);
    }

    private final void accept0(CreateSequenceImpl query) {
        Sequence<?> sequence = query.$sequence();
        MutableSchema schema = getSchema(sequence.getSchema(), true);

        MutableSequence existing = schema.sequence(sequence);
        if (existing != null) {
            if (!query.$createSequenceIfNotExists())
                throw alreadyExists(sequence);

            return;
        }

        MutableSequence ms = new MutableSequence((UnqualifiedName) sequence.getUnqualifiedName(), schema);

        ms.startWith = query.$startWith();
        ms.incrementBy = query.$incrementBy();
        ms.minvalue = query.$noMinvalue() ? null : query.$minvalue();
        ms.maxvalue = query.$noMaxvalue() ? null : query.$maxvalue();
        ms.cycle = query.$cycle();
        ms.cache = query.$noCache() ? null : query.$cache();
    }

    private final void accept0(AlterSequenceImpl<?> query) {
        Sequence<?> sequence = query.$sequence();
        MutableSchema schema = getSchema(sequence.getSchema());

        MutableSequence existing = schema.sequence(sequence);
        if (existing == null) {
            if (!query.$alterSequenceIfExists())
                throw notExists(sequence);

            return;
        }

        Sequence<?> renameTo = query.$renameTo();
        if (renameTo != null) {
            if (schema.sequence(renameTo) != null)
                throw alreadyExists(renameTo);

            existing.name((UnqualifiedName) renameTo.getUnqualifiedName());
        }
        else {
            Field<? extends Number> startWith = query.$startWith();
            boolean seen = false;
            if (startWith != null && (seen |= true))
                existing.startWith = startWith;

            Field<? extends Number> incrementBy = query.$incrementBy();
            if (incrementBy != null && (seen |= true))
                existing.incrementBy = incrementBy;

            Field<? extends Number> minvalue = query.$minvalue();
            if (minvalue != null && (seen |= true))
                existing.minvalue = minvalue;
            else if (query.$noMinvalue() && (seen |= true))
                existing.minvalue = null;

            Field<? extends Number> maxvalue = query.$maxvalue();
            if (maxvalue != null && (seen |= true))
                existing.maxvalue = maxvalue;
            else if (query.$noMaxvalue() && (seen |= true))
                existing.maxvalue = null;

            Boolean cycle = query.$cycle();
            if (cycle != null && (seen |= true))
                existing.cycle = cycle;

            Field<? extends Number> cache = query.$cache();
            if (cache != null && (seen |= true))
                existing.cache = cache;
            else if (query.$noCache() && (seen |= true))
                existing.cache = null;

            if ((query.$restart() || query.$restartWith() != null) && (seen |= true))
                // ignored
                ;

            if (!seen)
                throw unsupportedQuery(query);
        }
    }

    private final void accept0(DropSequenceImpl query) {
        Sequence<?> sequence = query.$sequence();
        MutableSchema schema = getSchema(sequence.getSchema());

        MutableSequence existing = schema.sequence(sequence);
        if (existing == null) {
            if (!query.$dropSequenceIfExists())
                throw notExists(sequence);

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
            throw notExists(table);

        MutableIndex existing = find(mt.indexes, index);
        List<MutableSortField> mtf = mt.sortFields(query.$on());

        if (existing != null) {
            if (!query.$createIndexIfNotExists())
                throw alreadyExists(index);

            return;
        }

        mt.indexes.add(new MutableIndex((UnqualifiedName) index.getUnqualifiedName(), mt, mtf, query.$unique()));
    }

    private final void accept0(AlterIndexImpl query) {
        Index index = query.$index();
        Table<?> table = query.$on() != null ? query.$on() : index.getTable();
        MutableIndex existing = index(index, table, query.$alterIndexIfExists(), true);

        if (existing != null) {
            if (query.$renameTo() != null)
                if (index(query.$renameTo(), table, false, false) == null)
                    existing.name((UnqualifiedName) query.$renameTo().getUnqualifiedName());
                else
                    throw alreadyExists(query.$renameTo());
            else
                throw unsupportedQuery(query);
        }
    }

    private final void accept0(DropIndexImpl query) {
        Index index = query.$index();
        Table<?> table = query.$on() != null ? query.$on() : index.getTable();
        MutableIndex existing = index(index, table, query.$dropIndexIfExists(), true);

        if (existing != null)
            existing.table.indexes.remove(existing);
    }

    private final void accept0(CreateDomainImpl<?> query) {
        Domain<?> domain = query.$domain();
        MutableSchema schema = getSchema(domain.getSchema(), true);

        MutableDomain existing = schema.domain(domain);
        if (existing != null) {
            if (!query.$createDomainIfNotExists())
                throw alreadyExists(domain);

            return;
        }

        MutableDomain md = new MutableDomain((UnqualifiedName) domain.getUnqualifiedName(), schema, query.$dataType());

        if (query.$default_() != null)
            md.dataType = md.dataType.default_((Field) query.$default_());

        // TODO: Support NOT NULL constraints
        if (query.$constraints() != null)
            for (Constraint constraint : query.$constraints())
                if (((ConstraintImpl) constraint).$check() != null)
                    md.checks.add(new MutableCheck(constraint));
    }

    private final void accept0(AlterDomainImpl<?> query) {
        Domain<?> domain = query.$domain();
        MutableSchema schema = getSchema(domain.getSchema());

        MutableDomain existing = schema.domain(domain);
        if (existing == null) {
            if (!query.$alterDomainIfExists())
                throw notExists(domain);

            return;
        }

        if (query.$addConstraint() != null) {
            if (find(existing.checks, query.$addConstraint()) != null)
                throw alreadyExists(query.$addConstraint());

            existing.checks.add(new MutableCheck(query.$addConstraint()));
        }
        else if (query.$dropConstraint() != null) {
            MutableCheck mc = find(existing.checks, query.$dropConstraint());

            if (mc == null) {
                if (!query.$dropConstraintIfExists())
                    throw notExists(query.$dropConstraint());

                return;
            }

            existing.checks.remove(mc);
        }
        else if (query.$renameTo() != null) {
            if (schema.domain(query.$renameTo()) != null)
                throw alreadyExists(query.$renameTo());

            existing.name((UnqualifiedName) query.$renameTo().getUnqualifiedName());
        }
        else if (query.$renameConstraint() != null) {
            MutableCheck mc = find(existing.checks, query.$renameConstraint());

            if (mc == null) {
                if (!query.$renameConstraintIfExists())
                    throw notExists(query.$renameConstraint());

                return;
            }
            else if (find(existing.checks, query.$renameConstraintTo()) != null)
                throw alreadyExists(query.$renameConstraintTo());

            mc.name((UnqualifiedName) query.$renameConstraintTo().getUnqualifiedName());
        }
        else if (query.$setDefault() != null) {
            existing.dataType = existing.dataType.defaultValue((Field) query.$setDefault());
        }
        else if (query.$dropDefault()) {
            existing.dataType = existing.dataType.defaultValue((Field) null);
        }

        // TODO: Implement these
        // else if (query.$setNotNull()) {}
        // else if (query.$dropNotNull()) {}
        else
            throw unsupportedQuery(query);
    }

    private final void accept0(DropDomainImpl query) {
        Domain<?> domain = query.$domain();
        MutableSchema schema = getSchema(domain.getSchema());

        MutableDomain existing = schema.domain(domain);
        if (existing == null) {
            if (!query.$dropDomainIfExists())
                throw notExists(domain);

            return;
        }

        if (query.$cascade() != Cascade.CASCADE && !existing.fields.isEmpty())
            throw new DataDefinitionException("Domain " + domain.getQualifiedName() + " is still being referenced by fields.");

        List<MutableField> field = new ArrayList<>(existing.fields);
        for (MutableField mf : field)
            dropColumns(mf.table, existing.fields, CASCADE);

        schema.domains.remove(existing);
    }

    private final void accept0(CommentOnImpl query) {
        Table<?> table = query.$table();
        Field<?> field = query.$field();

        if (table != null)
            table(table).comment(query.$comment());
        else if (field != null)
            field(field).comment(query.$comment());
        else
            throw unsupportedQuery(query);
    }

    private final void accept0(SetSchema query) {
        MutableSchema schema = getSchema(query.$schema());
        if (schema == null)
            throw notExists(query.$schema());

        currentSchema = schema;
    }

    private final void accept0(SetCommand query) {
        if ("foreign_key_checks".equals(query.$name().last().toLowerCase(locale))) {
            delayForeignKeyDeclarations = !Convert.convert(query.$value().getValue(), boolean.class);

            if (!delayForeignKeyDeclarations)
                applyDelayedForeignKeys();
        }
        else
            throw unsupportedQuery(query);
    }

    // -------------------------------------------------------------------------
    // Exceptions
    // -------------------------------------------------------------------------

    private static final DataDefinitionException unsupportedQuery(Query query) {
        return new DataDefinitionException("Unsupported query: " + query.getSQL());
    }

    private static final DataDefinitionException schemaNotEmpty(Schema schema) {
        return new DataDefinitionException("Schema is not empty: " + schema.getQualifiedName());
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

    private static final DataDefinitionException viewNotExists(Table<?> view) {
        return new DataDefinitionException("View does not exist: " + view.getQualifiedName());
    }

    private static final DataDefinitionException viewAlreadyExists(Table<?> view) {
        return new DataDefinitionException("View already exists: " + view.getQualifiedName());
    }

    private static final DataDefinitionException columnAlreadyExists(Name name) {
        return new DataDefinitionException("Column already exists: " + name);
    }

    private static final DataDefinitionException notExists(Named named) {
        return new DataDefinitionException(named.getClass().getSimpleName() + " does not exist: " + named.getQualifiedName());
    }

    private static final DataDefinitionException alreadyExists(Named named) {
        return new DataDefinitionException(named.getClass().getSimpleName() + " already exists: " + named.getQualifiedName());
    }

    private static final DataDefinitionException primaryKeyNotExists(Named named) {
        return new DataDefinitionException("Primary key does not exist on table: " + named);
    }

    // -------------------------------------------------------------------------
    // Auxiliary methods
    // -------------------------------------------------------------------------

    private final Iterable<MutableTable> tables() {
        // TODO: Make this lazy
        List<MutableTable> result = new ArrayList<>();

        for (MutableCatalog catalog : catalogs.values())
            for (MutableSchema schema : catalog.schemas)
                result.addAll(schema.tables);

        return result;
    }

    private final MutableSchema getSchema(Schema input) {
        return getSchema(input, false);
    }

    private final MutableSchema getSchema(Schema input, boolean create) {
        if (input == null)
            return currentSchema(create);

        MutableCatalog catalog = defaultCatalog;
        if (input.getCatalog() != null) {
            Name catalogName = input.getCatalog().getUnqualifiedName();
            if ((catalog = catalogs.get(catalogName)) == null && create)
                catalogs.put(catalogName, catalog = new MutableCatalog((UnqualifiedName) catalogName));
        }

        if (catalog == null)
            return null;

        MutableSchema schema = defaultSchema;
        if ((schema = find(catalog.schemas, input)) == null && create)
            // TODO createSchemaIfNotExists should probably be configurable
            schema = new MutableSchema((UnqualifiedName) input.getUnqualifiedName(), catalog);

        return schema;
    }

    private final MutableSchema currentSchema(boolean create) {
        if (currentSchema == null)
            currentSchema = getInterpreterSearchPathSchema(create);

        return currentSchema;
    }

    private final MutableSchema getInterpreterSearchPathSchema(boolean create) {
        List<InterpreterSearchSchema> searchPath = configuration.settings().getInterpreterSearchPath();

        if (searchPath.isEmpty())
            return defaultSchema;

        InterpreterSearchSchema schema = searchPath.get(0);
        return getSchema(schema(name(schema.getCatalog(), schema.getSchema())), create);
    }

    private final MutableTable newTable(
        Table<?> table,
        MutableSchema schema,
        List<Field<?>> columns,
        List<DataType<?>> columnTypes,
        Select<?> select,
        Comment comment,
        TableOptions options
    ) {
        MutableTable t = new MutableTable((UnqualifiedName) table.getUnqualifiedName(), schema, comment, options);

        if (!columns.isEmpty())
            for (int i = 0; i < columns.size(); i++)
                addField(t, Integer.MAX_VALUE, (UnqualifiedName) columns.get(i).getUnqualifiedName(), columnTypes.get(i));
        else if (select != null)
            for (Field<?> column : select.fields())
                addField(t, Integer.MAX_VALUE, (UnqualifiedName) column.getUnqualifiedName(), column.getDataType());

        return t;
    }

    private final MutableTable table(Table<?> table) {
        return table(table, true);
    }

    private final MutableTable table(Table<?> table, boolean throwIfNotExists) {
        MutableTable result = getSchema(table.getSchema()).table(table);
        if (result == null && throwIfNotExists)
            throw notExists(table);

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
                if ((mi = find(mt1.indexes, index)) != null) {
                    mt = mt1;
                    ms = mt1.schema;
                    break;
                }
            }
        }

        if (mt != null)
            mi = find(mt.indexes, index);
        else if (table != null && throwIfNotExists)
            throw notExists(table);

        if (mi == null && !ifExists && throwIfNotExists)
            throw notExists(index);

        return mi;
    }

    private static final boolean checkNotExists(MutableSchema schema, Table<?> table) {
        MutableTable mt = schema.table(table);

        if (mt != null)
            throw alreadyExists(table, mt);

        return true;
    }

    private static final DataDefinitionException alreadyExists(Table<?> t, MutableTable mt) {
        if (mt.options.type().isView())
            return viewAlreadyExists(t);
        else
            return alreadyExists(t);
    }

    private final MutableField field(Field<?> field) {
        return field(field, true);
    }

    private final MutableField field(Field<?> field, boolean throwIfNotExists) {
        MutableTable table = table(DSL.table(field.getQualifiedName().qualifier()), throwIfNotExists);

        if (table == null)
            return null;

        MutableField result = find(table.fields, field);

        if (result == null && throwIfNotExists)
            throw notExists(field);

        return result;
    }

    private static final <M extends MutableNamed> M find(M m, UnqualifiedName name) {
        if (m == null)
            return null;

        if (m.nameEquals(name))
            return m;
        else
            return null;
    }

    private static final <M extends MutableNamed> M find(M m, Named named) {
        return find(m, (UnqualifiedName) named.getUnqualifiedName());
    }

    private static final <M extends MutableNamed> M find(List<? extends M> list, Named named) {
        UnqualifiedName n = (UnqualifiedName) named.getUnqualifiedName();

        // TODO Avoid O(N) lookups. Use Maps instead
        for (M m : list)
            if ((m = find(m, n)) != null)
                return m;

        return null;
    }

    private static final int indexOrFail(List<? extends MutableNamed> list, Named named) {
        int result = -1;

        // TODO Avoid O(N) lookups. Use Maps instead
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).nameEquals((UnqualifiedName) named.getUnqualifiedName())) {
                result = i;
                break;
            }
        }

        if (result == -1)
            throw notExists(named);

        return result;
    }

    private static final InterpreterNameLookupCaseSensitivity caseSensitivity(Configuration configuration) {
        InterpreterNameLookupCaseSensitivity result = defaultIfNull(configuration.settings().getInterpreterNameLookupCaseSensitivity(), InterpreterNameLookupCaseSensitivity.DEFAULT);

        if (result == InterpreterNameLookupCaseSensitivity.DEFAULT) {
            switch (defaultIfNull(configuration.settings().getInterpreterDialect(), configuration.family()).family()) {












                case MARIADB:
                case MYSQL:
                case SQLITE:
                    return InterpreterNameLookupCaseSensitivity.NEVER;

                case DEFAULT:
                default:
                    return InterpreterNameLookupCaseSensitivity.WHEN_QUOTED;
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Data model
    // -------------------------------------------------------------------------

    private abstract class MutableNamed {
        private UnqualifiedName                      name;
        private String                               upper;
        private Comment                              comment;

        MutableNamed(UnqualifiedName name) {
            this(name, null);
        }

        MutableNamed(UnqualifiedName name, Comment comment) {
            this.comment = comment;

            name(name);
        }

        Name qualifiedName() {
            MutableNamed parent = parent();

            if (parent == null)
                return name;
            else
                return parent.qualifiedName().append(name);
        }

        UnqualifiedName name() {
            return name;
        }

        void name(UnqualifiedName n) {
            this.name = n;
            this.upper = name.last().toUpperCase(locale);
        }

        Comment comment() {
            return comment;
        }

        void comment(Comment c) {
            this.comment = c;
        }

        boolean nameEquals(UnqualifiedName other) {
            switch (caseSensitivity) {
                case ALWAYS:
                    return name.last().equals(other.last());

                case WHEN_QUOTED:
                    return normaliseNameCase(configuration, name.last(), name.quoted() == QUOTED, locale).equals(
                           normaliseNameCase(configuration, other.last(), other.quoted() == QUOTED, locale));

                case NEVER:
                    return upper.equalsIgnoreCase(other.last().toUpperCase(locale));

                case DEFAULT:
                default:
                    throw new IllegalStateException();
            }
        }

        abstract MutableNamed parent();
        abstract void onDrop();

        @Override
        public String toString() {
            return qualifiedName().toString();
        }
    }

    private final class MutableCatalog extends MutableNamed {
        List<MutableSchema> schemas = new MutableNamedList<>();

        MutableCatalog(UnqualifiedName name) {
            super(name, null);
        }

        @Override
        final void onDrop() {
            schemas.clear();
        }

        @Override
        final MutableNamed parent() {
            return null;
        }

        final InterpretedCatalog interpretedCatalog() {
            return interpretedCatalogs.computeIfAbsent(qualifiedName(), n -> new InterpretedCatalog());
        }

        private final class InterpretedCatalog extends CatalogImpl {
            InterpretedCatalog() {
                super(MutableCatalog.this.name(), MutableCatalog.this.comment());
            }

            @Override
            public final List<Schema> getSchemas() {
                return map(schemas, s -> s.interpretedSchema());
            }
        }
    }

    private final class MutableSchema extends MutableNamed  {
        MutableCatalog        catalog;
        List<MutableTable>    tables    = new MutableNamedList<>();
        List<MutableDomain>   domains   = new MutableNamedList<>();
        List<MutableSequence> sequences = new MutableNamedList<>();

        MutableSchema(UnqualifiedName name, MutableCatalog catalog) {
            super(name);

            this.catalog = catalog;
            this.catalog.schemas.add(this);
        }

        @Override
        final void onDrop() {
            for (MutableTable table : tables)
                for (MutableForeignKey referencingKey : table.referencingKeys())
                    referencingKey.table.foreignKeys.remove(referencingKey);

            // TODO: Cascade domains?

            tables.clear();
            domains.clear();
            sequences.clear();
        }

        @Override
        final MutableNamed parent() {
            return catalog;
        }

        final InterpretedSchema interpretedSchema() {
            return interpretedSchemas.computeIfAbsent(qualifiedName(), n -> new InterpretedSchema(catalog.interpretedCatalog()));
        }

        final boolean isEmpty() {
            return tables.isEmpty();
        }

        final MutableTable table(Named t) {
            return find(tables, t);
        }

        final MutableDomain domain(Named d) {
            return find(domains, d);
        }

        final MutableSequence sequence(Named s) {
            return find(sequences, s);
        }

        private final class InterpretedSchema extends SchemaImpl {
            InterpretedSchema(MutableCatalog.InterpretedCatalog catalog) {
                super(MutableSchema.this.name(), catalog, MutableSchema.this.comment());
            }

            @Override
            public final List<Table<?>> getTables() {
                return map(tables, t -> t.interpretedTable());
            }

            @Override
            public final List<Domain<?>> getDomains() {
                return map(domains, d -> d.interpretedDomain());
            }

            @Override
            public final List<Sequence<?>> getSequences() {
                return map(sequences, s -> s.interpretedSequence());
            }
        }
    }

    private final class MutableTable extends MutableNamed  {
        MutableSchema           schema;
        List<MutableField>      fields      = new MutableNamedList<>();
        MutableUniqueKey        primaryKey;
        List<MutableUniqueKey>  uniqueKeys  = new MutableNamedList<>();
        List<MutableForeignKey> foreignKeys = new MutableNamedList<>();
        List<MutableCheck>      checks      = new MutableNamedList<>();
        List<MutableIndex>      indexes     = new MutableNamedList<>();
        TableOptions            options;

        MutableTable(UnqualifiedName name, MutableSchema schema, Comment comment, TableOptions options) {
            super(name, comment);

            this.schema = schema;
            this.options = options;
            schema.tables.add(this);
        }

        @Override
        final void onDrop() {
            if (primaryKey != null)
                primaryKey.onDrop();

            uniqueKeys.clear();
            foreignKeys.clear();
            checks.clear();
            indexes.clear();
            fields.clear();
        }

        @Override
        final MutableNamed parent() {
            return schema;
        }

        final InterpretedTable interpretedTable() {
            return interpretedTables.computeIfAbsent(qualifiedName(), n -> new InterpretedTable(schema.interpretedSchema()));
        }

        boolean hasReferencingKeys() {
            if (primaryKey != null && !primaryKey.referencingKeys.isEmpty())
                return true;
            else
                return anyMatch(uniqueKeys, uk -> !uk.referencingKeys.isEmpty());
        }

        List<MutableForeignKey> referencingKeys() {
            List<MutableForeignKey> result = new ArrayList<>();

            if (primaryKey != null)
                result.addAll(primaryKey.referencingKeys);

            for (MutableUniqueKey uk : uniqueKeys)
                result.addAll(uk.referencingKeys);

            return result;
        }

        final MutableConstraint constraint(Constraint constraint, boolean failIfNotFound) {
            MutableConstraint result;

            if ((result = find(foreignKeys, constraint)) != null)
                return result;

            if ((result = find(uniqueKeys, constraint)) != null)
                return result;

            if ((result = find(checks, constraint)) != null)
                return result;

            if ((result = find(primaryKey, constraint)) != null)
                return result;

            if (failIfNotFound)
                throw notExists(constraint);

            return null;
        }

        final MutableNamed constraint(Constraint constraint) {
            return constraint(constraint, false);
        }

        final List<MutableField> fields(Field<?>[] fs, boolean failIfNotFound) {
            List<MutableField> result = new ArrayList<>();

            for (Field<?> f : fs) {
                MutableField mf = find(fields, f);

                if (mf != null)
                    result.add(mf);
                else if (failIfNotFound)
                    throw new DataDefinitionException("Field does not exist in table: " + f.getQualifiedName());
            }

            return result;
        }

        final List<MutableSortField> sortFields(Collection<? extends OrderField<?>> ofs) {
            return map(ofs, (OrderField<?> of) -> {
                SortField<?> sf = Tools.sortField(of);
                Field<?> f = ((SortFieldImpl<?>) sf).getField();
                MutableField mf = find(fields, f);

                if (mf == null)
                    throw new DataDefinitionException("Field does not exist in table: " + f.getQualifiedName());

                return new MutableSortField(mf, sf.getOrder());
            });
        }

        final MutableUniqueKey uniqueKey(List<MutableField> mrfs) {
            Set<MutableField> set = new HashSet<>(mrfs);

            if (primaryKey != null && set.equals(new HashSet<>(primaryKey.fields)))
                return primaryKey;
            else
                return findAny(uniqueKeys, mu -> set.equals(new HashSet<>(mu.fields)));
        }

        private final class InterpretedTable extends TableImpl<Record> {
            InterpretedTable(MutableSchema.InterpretedSchema schema) {
                super(MutableTable.this.name(), schema, null, null, null, null, MutableTable.this.comment(), MutableTable.this.options);

                for (MutableField field : MutableTable.this.fields)
                    createField(field.name(), field.type, field.comment() != null ? field.comment().getComment() : null);
            }

            @Override
            public final UniqueKey<Record> getPrimaryKey() {
                return MutableTable.this.primaryKey != null
                     ? MutableTable.this.primaryKey.interpretedKey()
                     : null;
            }

            @Override
            public final List<UniqueKey<Record>> getUniqueKeys() {
                return map(MutableTable.this.uniqueKeys, uk -> uk.interpretedKey());
            }

            @Override
            public List<ForeignKey<Record, ?>> getReferences() {
                return map(MutableTable.this.foreignKeys, fk -> fk.interpretedKey());
            }

            @Override
            public List<Check<Record>> getChecks() {
                return map(MutableTable.this.checks, c -> new CheckImpl<>(this, c.name(), c.condition, c.enforced));
            }

            @Override
            public final List<Index> getIndexes() {
                return map(MutableTable.this.indexes, i -> i.interpretedIndex());
            }
        }
    }

    private final class MutableDomain extends MutableNamed {
        MutableSchema      schema;
        DataType<?>        dataType;
        List<MutableCheck> checks = new MutableNamedList<>();
        List<MutableField> fields = new MutableNamedList<>();

        MutableDomain(UnqualifiedName name, MutableSchema schema, DataType<?> dataType) {
            super(name);

            this.schema = schema;
            this.dataType = dataType;
            schema.domains.add(this);
        }

        @Override
        final void onDrop() {
            schema.domains.remove(this);
            // TODO: Cascade
        }

        @Override
        final MutableNamed parent() {
            return schema;
        }


        final InterpretedDomain interpretedDomain() {
            return interpretedDomains.computeIfAbsent(qualifiedName(), n -> new InterpretedDomain(schema.interpretedSchema()));
        }

        final Check<?>[] interpretedChecks() {
            return map(checks, c -> new CheckImpl<>(null, c.name(), c.condition, c.enforced), Check[]::new);
        }

        private final class InterpretedDomain extends DomainImpl {
            InterpretedDomain(Schema schema) {
                super(schema, MutableDomain.this.name(), dataType, interpretedChecks());
            }
        }
    }

    private final class MutableSequence extends MutableNamed {
        MutableSchema           schema;
        Field<? extends Number> startWith;
        Field<? extends Number> incrementBy;
        Field<? extends Number> minvalue;
        Field<? extends Number> maxvalue;
        boolean                 cycle;
        Field<? extends Number> cache;

        MutableSequence(UnqualifiedName name, MutableSchema schema) {
            super(name);

            this.schema = schema;
            schema.sequences.add(this);
        }

        @Override
        final void onDrop() {}

        @Override
        final MutableNamed parent() {
            return schema;
        }

        final InterpretedSequence interpretedSequence() {
            return interpretedSequences.computeIfAbsent(qualifiedName(), n -> new InterpretedSequence(schema.interpretedSchema()));
        }

        private final class InterpretedSequence extends SequenceImpl<Long> {
            InterpretedSequence(Schema schema) {
                super(MutableSequence.this.name(), schema, BIGINT, false,
                    (Field<Long>) MutableSequence.this.startWith,
                    (Field<Long>) MutableSequence.this.incrementBy,
                    (Field<Long>) MutableSequence.this.minvalue,
                    (Field<Long>) MutableSequence.this.maxvalue,
                    MutableSequence.this.cycle,
                    (Field<Long>) MutableSequence.this.cache);
            }
        }
    }

    private abstract class MutableConstraint extends MutableNamed {
        MutableTable table;
        boolean      enforced;

        MutableConstraint(UnqualifiedName name, MutableTable table, boolean enforced) {
            super(name);

            this.table = table;
            this.enforced = enforced;
        }

        @Override
        final MutableNamed parent() {
            return table;
        }
    }

    private abstract class MutableKey extends MutableConstraint {
        List<MutableField> fields;

        MutableKey(UnqualifiedName name, MutableTable table, List<MutableField> fields, boolean enforced) {
            super(name, table, enforced);

            this.fields = fields;
        }

        final boolean fieldsEquals(Field<?>[] f) {
            if (fields.size() != f.length)
                return false;
            else
                return !anyMatch(fields, (x, i) -> !x.nameEquals((UnqualifiedName) f[i].getUnqualifiedName()));
        }
    }

    private final class MutableCheck extends MutableConstraint {
        Condition condition;

        MutableCheck(Constraint constraint) {
            this(
                (UnqualifiedName) constraint.getUnqualifiedName(),
                null,
                ((ConstraintImpl) constraint).$check(),
                ((ConstraintImpl) constraint).$enforced()
            );
        }

        MutableCheck(UnqualifiedName name, MutableTable table, Condition condition, boolean enforced) {
            super(name, table, enforced);

            this.condition = condition;
        }

        @Override
        final void onDrop() {}

        @Override
        final Name qualifiedName() {

            // TODO: Find a better way to identify unnamed constraints.
            if (name().empty())
                return super.qualifiedName().append(condition.toString());
            else
                return super.qualifiedName();
        }
    }

    private final class MutableUniqueKey extends MutableKey {
        List<MutableForeignKey> referencingKeys = new MutableNamedList<>();

        MutableUniqueKey(UnqualifiedName name, MutableTable table, List<MutableField> fields, boolean enforced) {
            super(name, table, fields, enforced);
        }

        @Override
        final void onDrop() {
            // TODO Is this StackOverflowError safe?
            referencingKeys.clear();
        }

        @Override
        final Name qualifiedName() {

            // TODO: Find a better way to identify unnamed constraints.
            if (name().empty())
                return super.qualifiedName().append(fields.toString());
            else
                return super.qualifiedName();
        }

        final UniqueKeyImpl<Record> interpretedKey() {
            Name qualifiedName = qualifiedName();
            UniqueKeyImpl<Record> result = interpretedUniqueKeys.get(qualifiedName);

            if (result == null) {
                MutableTable.InterpretedTable t = table.interpretedTable();

                // Add to map before adding bi-directionality to avoid StackOverflowErrors
                interpretedUniqueKeys.put(qualifiedName, result = new UniqueKeyImpl<>(
                    t,
                    name(),
                    map(fields, f -> (TableField<Record, ?>) t.field(f.name()), TableField[]::new),
                    enforced
                ));

                for (MutableForeignKey referencingKey : referencingKeys)
                    result.references.add((ForeignKey) referencingKey.interpretedKey());
            }

            return result;
        }
    }

    private final class MutableForeignKey extends MutableKey {
        MutableUniqueKey   referencedKey;
        List<MutableField> referencedFields;

        // TODO: Support these
        Action           onDelete;
        Action           onUpdate;

        MutableForeignKey(
            UnqualifiedName name,
            MutableTable table,
            List<MutableField> fields,
            MutableUniqueKey referencedKey,
            List<MutableField> referencedFields,
            Action onDelete,
            Action onUpdate,
            boolean enforced
        ) {
            super(name, table, fields, enforced);

            this.referencedKey = referencedKey;
            this.referencedKey.referencingKeys.add(this);
            this.referencedFields = referencedFields;
            this.onDelete = onDelete;
            this.onUpdate = onUpdate;
        }

        @Override
        final void onDrop() {
            this.referencedKey.referencingKeys.remove(this);
        }

        @Override
        final Name qualifiedName() {

            // TODO: Find a better way to identify unnamed constraints.
            if (name().empty())
                return super.qualifiedName().append(referencedKey.qualifiedName());
            else
                return super.qualifiedName();
        }

        final ForeignKey<Record, ?> interpretedKey() {
            Name qualifiedName = qualifiedName();
            ReferenceImpl<Record, ?> result = interpretedForeignKeys.get(qualifiedName);

            if (result == null) {
                MutableTable.InterpretedTable t = table.interpretedTable();
                UniqueKeyImpl<Record> uk = referencedKey.interpretedKey();

                interpretedForeignKeys.put(qualifiedName, result = new ReferenceImpl<>(
                    t,
                    name(),
                    map(fields, f -> (TableField<Record, ?>) t.field(f.name()), TableField[]::new),
                    uk,
                    map(referencedFields, f -> (TableField<Record, ?>) uk.getTable().field(f.name()), TableField[]::new),
                    enforced
                ));
            }

            return result;
        }

    }

    private final class MutableIndex extends MutableNamed {
        MutableTable           table;
        List<MutableSortField> fields;
        boolean                unique;

        MutableIndex(UnqualifiedName name, MutableTable table, List<MutableSortField> fields, boolean unique) {
            super(name);

            this.table = table;
            this.fields = fields;
            this.unique = unique;
        }

        @Override
        final void onDrop() {}

        @Override
        final MutableNamed parent() {
            return table;
        }

        @Override
        final Name qualifiedName() {

            // TODO: Can we have unnamed indexes?
            return super.qualifiedName();
        }

        final Index interpretedIndex() {
            Name qualifiedName = qualifiedName();
            Index result = interpretedIndexes.get(qualifiedName);

            if (result == null) {
                Table<?> t = table.interpretedTable();
                interpretedIndexes.put(qualifiedName, result = new IndexImpl(
                    name(),
                    t,
                    map(fields, msf -> t.field(msf.name()).sort(msf.sort), SortField[]::new),
                    null,
                    unique
                ));
            }

            return result;
        }
    }

    private final class MutableField extends MutableNamed {
        MutableTable  table;
        DataType<?>   type;
        MutableDomain domain;

        MutableField(UnqualifiedName name, MutableTable table, DataType<?> type) {
            super(name);

            this.table = table;
            this.type = type;
            this.domain = table.schema.domain(type);

            if (this.domain != null)
                this.domain.fields.add(this);
        }

        @Override
        final void onDrop() {
            if (this.domain != null)
                this.domain.fields.remove(this);
        }

        @Override
        final MutableNamed parent() {
            return table;
        }
    }

    private final class MutableSortField extends MutableNamed {
        MutableField field;
        SortOrder    sort;

        MutableSortField(MutableField field, SortOrder sort) {
            super(field.name());

            this.field = field;
            this.sort = sort;
        }

        @Override
        final void onDrop() {}

        @Override
        final MutableNamed parent() {
            return field.parent();
        }
    }

    private final class MutableNamedList<N extends MutableNamed> extends AbstractList<N> {
        private final List<N> delegate = new ArrayList<>();

        @Override
        public N get(int index) {
            return delegate.get(index);
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public N set(int index, N element) {
            return delegate.set(index, element);
        }

        @Override
        public void add(int index, N element) {
            delegate.add(index, element);
        }

        @Override
        public N remove(int index) {
            N removed = delegate.remove(index);
            removed.onDrop();
            return removed;
        }
    }

    @Override
    public String toString() {
        return meta().toString();
    }
}
