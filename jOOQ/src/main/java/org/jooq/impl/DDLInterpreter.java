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

import static org.jooq.Name.Quoted.QUOTED;
import static org.jooq.conf.SettingsTools.renderLocale;
import static org.jooq.impl.AbstractName.NO_NAME;
import static org.jooq.impl.Cascade.CASCADE;
import static org.jooq.impl.Cascade.RESTRICT;
import static org.jooq.impl.ConstraintType.FOREIGN_KEY;
import static org.jooq.impl.ConstraintType.PRIMARY_KEY;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.intersect;
import static org.jooq.impl.Tools.normaliseNameCase;
import static org.jooq.impl.Tools.reverseIterable;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Comment;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.DataType;
import org.jooq.Delete;
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
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataDefinitionException;
import org.jooq.impl.ConstraintImpl.Action;
import org.jooq.tools.JooqLogger;

@SuppressWarnings("serial")
final class DDLInterpreter {

    private static final JooqLogger                    log      = JooqLogger.getLogger(DDLInterpreter.class);

    private final Configuration                        configuration;
    private final InterpreterNameLookupCaseSensitivity caseSensitivity;
    private final Map<Name, MutableCatalog>            catalogs = new LinkedHashMap<>();
    private final MutableCatalog                       defaultCatalog;
    private final MutableSchema                        defaultSchema;
    private MutableSchema                              currentSchema;

    DDLInterpreter(Configuration configuration) {
        this.configuration = configuration;
        this.caseSensitivity = caseSensitivity(configuration);
        this.defaultCatalog = new MutableCatalog(NO_NAME);
        this.catalogs.put(defaultCatalog.name(), defaultCatalog);
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

        else if (query instanceof CommentOnImpl)
            accept0((CommentOnImpl) query);

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
                addForeignKey(getSchema(impl.$referencesTable().getSchema(), false), mt, impl);
            else if (impl.$check() != null)
                mt.checks.add(new MutableCheck((UnqualifiedName) impl.getUnqualifiedName(), mt, impl.$check()));
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

        mt.foreignKeys.add(new MutableForeignKey(
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
                if (intersect(table.primaryKey.keyFields, fields)) {
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
        else if (!existing.options.type().isTable())
            throw objectNotTable(table);

        if (query.$add() != null) {
            for (FieldOrConstraint fc : query.$add())
                if (fc instanceof Field && find(existing.fields, (Field<?>) fc) != null)
                    throw fieldAlreadyExists((Field<?>) fc);
                else if (fc instanceof Constraint && !fc.getUnqualifiedName().empty() && existing.constraint((Constraint) fc) != null)
                    throw constraintAlreadyExists((Constraint) fc);

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
                        addConstraint(query, (ConstraintImpl) fc, schema, existing);
                    else
                        throw unsupportedQuery(query);
            }
        }
        else if (query.$addColumn() != null) {
            if (find(existing.fields, query.$addColumn()) != null)
                if (!query.$ifNotExistsColumn())
                    throw fieldAlreadyExists(query.$addColumn());
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
            addConstraint(query, (ConstraintImpl) query.$addConstraint(), schema, existing);
        }
        else if (query.$alterColumn() != null) {
            MutableField existingField = find(existing.fields, query.$alterColumn());

            if (existingField == null)
                if (!query.$ifExistsColumn())
                    throw columnNotExists(query.$alterColumn());
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
                throw fieldNotExists(query.$renameColumn());
            else if (find(existing.fields, query.$renameColumnTo()) != null)
                throw fieldAlreadyExists(query.$renameColumnTo());
            else
                mf.name((UnqualifiedName) query.$renameColumnTo().getUnqualifiedName());
        }
        else if (query.$renameConstraint() != null) {
            MutableNamed mk = existing.constraint(query.$renameConstraint());

            if (mk == null)
                throw constraintNotExists(query.$renameConstraint());
            else if (existing.constraint(query.$renameConstraintTo()) != null)
                throw constraintAlreadyExists(query.$renameConstraintTo());
            else
                mk.name((UnqualifiedName) query.$renameConstraintTo().getUnqualifiedName());
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
                Iterator<MutableForeignKey> fks = existing.foreignKeys.iterator();
                while (fks.hasNext()) {
                    if (fks.next().nameEquals((UnqualifiedName) impl.getUnqualifiedName())) {
                        fks.remove();
                        break removal;
                    }
                }

                if (query.$dropConstraintType() != FOREIGN_KEY) {
                    Iterator<MutableUniqueKey> uks = existing.uniqueKeys.iterator();

                    while (uks.hasNext()) {
                        MutableUniqueKey key = uks.next();

                        if (key.nameEquals((UnqualifiedName) impl.getUnqualifiedName())) {
                            cascade(key, null, query.$dropCascade());
                            uks.remove();
                            break removal;
                        }
                    }

                    Iterator<MutableCheck> chks = existing.checks.iterator();

                    while (chks.hasNext()) {
                        MutableCheck check = chks.next();

                        if (check.nameEquals((UnqualifiedName) impl.getUnqualifiedName())) {
                            chks.remove();
                            break removal;
                        }
                    }

                    if (existing.primaryKey != null) {
                        if (existing.primaryKey.nameEquals((UnqualifiedName) impl.getUnqualifiedName())) {
                            cascade(existing.primaryKey, null, query.$dropCascade());
                            existing.primaryKey = null;
                            break removal;
                        }
                    }
                }

                if (!query.$ifExistsConstraint())
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

    private final Iterable<Field<?>> assertFields(final Query query, final Iterable<FieldOrConstraint> fields) {
        return new Iterable<Field<?>>() {
            @Override
            public Iterator<Field<?>> iterator() {
                return new Iterator<Field<?>>() {
                    Iterator<FieldOrConstraint> it = fields.iterator();

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
        };
    }

    private final void addField(MutableTable existing, int index, UnqualifiedName name, DataType<?> dataType) {
        if (index == Integer.MAX_VALUE)
            existing.fields.add(       new MutableField(name, existing, dataType));
        else
            existing.fields.add(index, new MutableField(name, existing, dataType));
    }

    private final void addConstraint(Query query, ConstraintImpl impl, MutableSchema schema, MutableTable existing) {
        if (!impl.getUnqualifiedName().empty() && existing.constraint(impl) != null)
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
        else if (impl.$check() != null)
            existing.checks.add(new MutableCheck((UnqualifiedName) impl.getUnqualifiedName(), existing, impl.$check()));
        else
            throw unsupportedQuery(query);
    }

    private final void accept0(DropTableImpl query) {
        Table<?> table = query.$table();

        MutableSchema schema = getSchema(table.getSchema());
        MutableTable existing = schema.table(table);
        if (existing == null) {
            if (!query.$ifExists())
                throw tableNotExists(table);

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
            throw tableNotExists(table);
        else if (!existing.options.type().isTable())
            throw objectNotTable(table);
        else if (!query.$cascade() && existing.hasReferencingTables())
            throw new DataDefinitionException("Cannot truncate table referenced by other tables. Use CASCADE: " + table);
    }

    private final void accept0(CreateViewImpl<?> query) {
        Table<?> table = query.$view();
        MutableSchema schema = getSchema(table.getSchema());

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
            if (!query.$ifExists())
                throw viewNotExists(table);

            return;
        }
        else if (!existing.options.type().isView())
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
            if (!query.$ifExists())
                throw sequenceNotExists(sequence);

            return;
        }

        Sequence<?> renameTo = query.$renameTo();
        if (renameTo != null) {
            if (schema.sequence(renameTo) != null)
                throw sequenceAlreadyExists(renameTo);

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

        MutableIndex existing = find(mt.indexes, index);
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
                    existing.name((UnqualifiedName) query.$renameTo().getUnqualifiedName());
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
            table(table).comment(query.$comment());
        else if (field != null)
            field(field).comment(query.$comment());
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
        return new DataDefinitionException("Field already exists: " + field.getQualifiedName());
    }

    private static final DataDefinitionException objectNotExists(Named named) {
        return new DataDefinitionException("Object does not exist: " + named.getQualifiedName());
    }

    // -------------------------------------------------------------------------
    // Auxiliary methods
    // -------------------------------------------------------------------------

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

        // TODO It does not appear we should auto-create schema in the interpreter. Why is this being done?
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
        if ((schema = find(catalog.schemas, input)) == null && create)
            // TODO createSchemaIfNotExists should probably be configurable
            schema = new MutableSchema((UnqualifiedName) input.getUnqualifiedName(), catalog);

        return schema;
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
        if (mt.options.type().isView())
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

        MutableField result = find(table.fields, field);

        if (result == null && throwIfNotExists)
            throw fieldNotExists(field);

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
            throw objectNotExists(named);

        return result;
    }

    private static final InterpreterNameLookupCaseSensitivity caseSensitivity(Configuration configuration) {
        InterpreterNameLookupCaseSensitivity result = defaultIfNull(configuration.settings().getInterpreterNameLookupCaseSensitivity(), InterpreterNameLookupCaseSensitivity.DEFAULT);

        if (result == InterpreterNameLookupCaseSensitivity.DEFAULT) {
            switch (defaultIfNull(configuration.settings().getInterpreterDialect(), configuration.family()).family()) {












                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB:
                case POSTGRES:
                    return InterpreterNameLookupCaseSensitivity.WHEN_QUOTED;











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

        UnqualifiedName name() {
            return name;
        }

        void name(UnqualifiedName n) {
            this.name = n;

            // TODO: Use Settings.renderLocale()
            this.upper = name.last().toUpperCase(renderLocale(configuration.settings()));
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
                    return normaliseNameCase(configuration, name.last(), name.quoted() == QUOTED).equals(
                           normaliseNameCase(configuration, other.last(), other.quoted() == QUOTED));

                case NEVER:
                    return upper.equalsIgnoreCase(other.last().toUpperCase(renderLocale(configuration.settings())));

                case DEFAULT:
                default:
                    throw new IllegalStateException();
            }
        }

        abstract void drop();

        @Override
        public String toString() {
            return name.toString();
        }
    }

    private final class MutableCatalog extends MutableNamed {
        List<MutableSchema> schemas = new MutableNamedList<>();

        MutableCatalog(UnqualifiedName name) {
            super(name, null);
        }

        @Override
        final void drop() {
            schemas.clear();
        }

        private final class InterpretedCatalog extends CatalogImpl {
            InterpretedCatalog() {
                super(MutableCatalog.this.name(), MutableCatalog.this.comment());
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

    private final class MutableSchema extends MutableNamed  {
        MutableCatalog        catalog;
        List<MutableTable>    tables    = new MutableNamedList<>();
        List<MutableSequence> sequences = new MutableNamedList<>();

        MutableSchema(UnqualifiedName name, MutableCatalog catalog) {
            super(name);

            this.catalog = catalog;
            this.catalog.schemas.add(this);
        }

        @Override
        final void drop() {
            tables.clear();
            sequences.clear();
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

        private final class InterpretedSchema extends SchemaImpl {
            InterpretedSchema(MutableCatalog.InterpretedCatalog catalog) {
                super(MutableSchema.this.name(), catalog, MutableSchema.this.comment());
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
        final void drop() {
            if (primaryKey != null)
                primaryKey.drop();

            uniqueKeys.clear();
            foreignKeys.clear();
            checks.clear();
            indexes.clear();
            fields.clear();
        }

        boolean hasReferencingTables() {
            if (primaryKey != null && !primaryKey.referencingKeys.isEmpty())
                return true;

            for (MutableUniqueKey uk : uniqueKeys)
                if (!uk.referencingKeys.isEmpty())
                    return true;

            return false;
        }

        final MutableNamed constraint(Constraint constraint) {
            MutableNamed result;

            if ((result = find(foreignKeys, constraint)) != null)
                return result;

            if ((result = find(uniqueKeys, constraint)) != null)
                return result;

            if ((result = find(checks, constraint)) != null)
                return result;

            return find(primaryKey, constraint);
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

        final List<MutableSortField> sortFields(SortField<?>[] sfs) {
            List<MutableSortField> result = new ArrayList<>();

            for (SortField<?> sf : sfs) {
                Field<?> f = ((SortFieldImpl<?>) sf).getField();
                MutableField mf = find(fields, f);

                if (mf == null)
                    throw new DataDefinitionException("Field does not exist in table: " + f.getQualifiedName());

                result.add(new MutableSortField(mf, sf.getOrder()));
            }

            return result;
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
                super(MutableTable.this.name(), schema, null, null, null, null, MutableTable.this.comment(), MutableTable.this.options);

                for (MutableField field : MutableTable.this.fields)
                    createField(field.name(), field.type, field.comment() != null ? field.comment().getComment() : null);
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

                for (MutableForeignKey fk : MutableTable.this.foreignKeys)
                    result.add(interpretedKey(fk));

                return result;
            }

            @Override
            public List<Check<Record>> getChecks() {
                List<Check<Record>> result = new ArrayList<>();

                for (MutableCheck c : MutableTable.this.checks)
                    result.add(new CheckImpl<>(this, c.name(), c.condition));

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

                // TODO: Refactor these constructor calls
                InterpretedTable t = key.keyTable.new InterpretedTable(key.keyTable.schema.new InterpretedSchema(key.keyTable.schema.catalog.new InterpretedCatalog()));

                for (int i = 0; i < f.length; i++)
                    f[i] = (TableField<Record, ?>) t.field(key.keyFields.get(i).name());

                // TODO: Cache these?
                return new UniqueKeyImpl<Record>(t, key.name().last(), f);
            }

            @SuppressWarnings("unchecked")
            private final ForeignKey<Record, ?> interpretedKey(MutableForeignKey key) {
                if (key == null)
                    return null;

                TableField<Record, ?>[] f = new TableField[key.keyFields.size()];

                for (int i = 0; i < f.length; i++)
                    f[i] = (TableField<Record, ?>) field(key.keyFields.get(i).name());

                // TODO: Refactor these constructor calls
                // TODO: Cache these?
                return new ReferenceImpl<>(key.referencedKey.keyTable.new InterpretedTable((MutableSchema.InterpretedSchema) getSchema()).interpretedKey(key.referencedKey), this, key.name().last(), f);
            }

            private final Index interpretedIndex(MutableIndex idx) {
                if (idx == null)
                    return null;

                SortField<?>[] f = new SortField[idx.fields.size()];

                for (int i = 0; i < f.length; i++) {
                    MutableSortField msf = idx.fields.get(i);
                    f[i] = field(msf.name()).sort(msf.sort);
                }

                return new IndexImpl(idx.name(), this, f, null, idx.unique);
            }
        }
    }

    @SuppressWarnings("unused")
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
        final void drop() {}

        private final class InterpretedSequence extends SequenceImpl<Long> {
            @SuppressWarnings("unchecked")
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

    private abstract class MutableKey extends MutableNamed {
        MutableTable       keyTable;
        List<MutableField> keyFields;

        MutableKey(UnqualifiedName name, MutableTable table, List<MutableField> fields) {
            super(name);

            this.keyTable = table;
            this.keyFields = fields;
        }
    }

    private final class MutableCheck extends MutableNamed {
        MutableTable table;
        Condition    condition;

        MutableCheck(UnqualifiedName name, MutableTable table, Condition condition) {
            super(name);

            this.table = table;
            this.condition = condition;
        }

        @Override
        final void drop() {}
    }

    private final class MutableUniqueKey extends MutableKey {
        List<MutableForeignKey> referencingKeys = new MutableNamedList<>();

        MutableUniqueKey(UnqualifiedName name, MutableTable keyTable, List<MutableField> keyFields) {
            super(name, keyTable, keyFields);
        }

        @Override
        final void drop() {
            // TODO Is this StackOverflowError safe?
            referencingKeys.clear();
        }
    }

    private final class MutableForeignKey extends MutableKey {
        MutableUniqueKey referencedKey;
        Action           onDelete;
        Action           onUpdate;

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
            this.referencedKey.referencingKeys.add(this);
            this.onDelete = onDelete;
            this.onUpdate = onUpdate;
        }

        @Override
        final void drop() {
            this.referencedKey.referencingKeys.remove(this);
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
        final void drop() {}
    }

    private final class MutableField extends MutableNamed {
        MutableTable table;
        DataType<?>  type;

        MutableField(UnqualifiedName name, MutableTable table, DataType<?> type) {
            super(name);

            this.table = table;
            this.type = type;
        }

        @Override
        final void drop() {}
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
        final void drop() {}
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
            removed.drop();
            return removed;
        }
    }

    @Override
    public String toString() {
        return meta().toString();
    }
}
