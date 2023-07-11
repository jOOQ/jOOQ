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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.QOM.GenerationOption.STORED;
import static org.jooq.impl.QOM.GenerationOption.VIRTUAL;
import static org.jooq.impl.Tools.EMPTY_CHECK;
import static org.jooq.impl.Tools.EMPTY_SORTFIELD;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.util.xml.jaxb.TableConstraintType.PRIMARY_KEY;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Comment;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
// ...
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.Statement;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.TableOptions.TableType;
// ...
// ...
// ...
// ...
import org.jooq.UniqueKey;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.tools.StringUtils;
import org.jooq.util.xml.jaxb.CheckConstraint;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.IndexColumnUsage;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.KeyColumnUsage;
import org.jooq.util.xml.jaxb.ReferentialConstraint;
import org.jooq.util.xml.jaxb.TableConstraint;
import org.jooq.util.xml.jaxb.TriggerActionOrientation;

/**
 * @author Lukas Eder
 */
final class InformationSchemaMetaImpl extends AbstractMeta {

    private final List<Catalog>                                 catalogs;
    private final Map<Name, Catalog>                            catalogsByName;
    private final List<Schema>                                  schemas;
    private final Map<Name, Schema>                             schemasByName;
    private final Map<Catalog, List<Schema>>                    schemasPerCatalog;
    private final List<InformationSchemaTable>                  tables;
    private final Map<Name, InformationSchemaTable>             tablesByName;
    private final Map<Schema, List<InformationSchemaTable>>     tablesPerSchema;
    private final List<InformationSchemaDomain<?>>              domains;
    private final Map<Name, InformationSchemaDomain<?>>         domainsByName;
    private final Map<Schema, List<InformationSchemaDomain<?>>> domainsPerSchema;
    private final List<Sequence<?>>                             sequences;
    private final Map<Schema, List<Sequence<?>>>                sequencesPerSchema;
    private final List<UniqueKeyImpl<Record>>                   primaryKeys;
    private final Map<Name, UniqueKeyImpl<Record>>              keysByName;
    private final Map<Name, Name>                               referentialKeys;
    private final Map<Name, IndexImpl>                          indexesByName;






    InformationSchemaMetaImpl(Configuration configuration, InformationSchema source) {
        super(configuration);

        this.catalogs = new ArrayList<>();
        this.catalogsByName = new HashMap<>();
        this.schemas = new ArrayList<>();
        this.schemasByName = new HashMap<>();
        this.schemasPerCatalog = new HashMap<>();
        this.tables = new ArrayList<>();
        this.tablesByName = new HashMap<>();
        this.tablesPerSchema = new HashMap<>();
        this.domains = new ArrayList<>();
        this.domainsByName = new HashMap<>();
        this.domainsPerSchema = new HashMap<>();
        this.sequences = new ArrayList<>();
        this.sequencesPerSchema = new HashMap<>();
        this.primaryKeys = new ArrayList<>();
        this.keysByName = new HashMap<>();
        this.referentialKeys = new HashMap<>();
        this.indexesByName = new HashMap<>();






        init(source);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final void init(InformationSchema meta) {
        List<String> errors = new ArrayList<>();

        // Catalogs
        // -------------------------------------------------------------------------------------------------------------
        boolean hasCatalogs = false;
        for (org.jooq.util.xml.jaxb.Catalog xc : meta.getCatalogs()) {
            InformationSchemaCatalog ic = new InformationSchemaCatalog(xc.getCatalogName(), xc.getComment());
            catalogs.add(ic);
            catalogsByName.put(name(xc.getCatalogName()), ic);
            hasCatalogs = true;
        }

        // Schemas
        // -------------------------------------------------------------------------------------------------------------
        schemaLoop:
        for (org.jooq.util.xml.jaxb.Schema xs : meta.getSchemata()) {

            // [#6662] This is kept for backwards compatibility reasons
            if (!hasCatalogs) {
                InformationSchemaCatalog ic = new InformationSchemaCatalog(xs.getCatalogName(), null);

                if (!catalogs.contains(ic)) {
                    catalogs.add(ic);
                    catalogsByName.put(name(xs.getCatalogName()), ic);
                }
            }

            Name catalogName = name(xs.getCatalogName());
            Catalog catalog = catalogsByName.get(catalogName);

            if (catalog == null) {
                errors.add("Catalog " + catalogName + " not defined for schema " + xs.getSchemaName());
                continue schemaLoop;
            }

            InformationSchemaSchema is = new InformationSchemaSchema(xs.getSchemaName(), catalog, xs.getComment());
            schemas.add(is);
            schemasByName.put(name(xs.getCatalogName(), xs.getSchemaName()), is);
        }

        // Domains
        // -------------------------------------------------------------------------------------------------------------
        domainLoop:
        for (org.jooq.util.xml.jaxb.Domain d : meta.getDomains()) {
            Name schemaName = name(d.getDomainCatalog(), d.getDomainSchema());
            Schema schema = schemasByName.get(schemaName);

            if (schema == null) {
                errors.add("Schema " + schemaName + " not defined for domain " + d.getDomainName());
                continue domainLoop;
            }

            Name domainName = name(d.getDomainCatalog(), d.getDomainSchema(), d.getDomainName());
            int length = d.getCharacterMaximumLength() == null ? 0 : d.getCharacterMaximumLength();
            int precision = d.getNumericPrecision() == null ? 0 : d.getNumericPrecision();
            int scale = d.getNumericScale() == null ? 0 : d.getNumericScale();

            // TODO [#10239] Support NOT NULL constraints
            boolean nullable = true;
            List<Check<?>> checks = new ArrayList<>();

            for (org.jooq.util.xml.jaxb.DomainConstraint dc : meta.getDomainConstraints()) {
                if (domainName.equals(name(dc.getDomainCatalog(), dc.getDomainSchema(), dc.getDomainName()))) {
                    Name constraintName = name(dc.getConstraintCatalog(), dc.getConstraintSchema(), dc.getConstraintName());

                    for (org.jooq.util.xml.jaxb.CheckConstraint cc : meta.getCheckConstraints())
                        if (constraintName.equals(name(cc.getConstraintCatalog(), cc.getConstraintSchema(), cc.getConstraintName())))
                            checks.add(new CheckImpl<>(null, constraintName, DSL.condition(cc.getCheckClause()), true));
                }
            }

            InformationSchemaDomain<?> id = new InformationSchemaDomain<Object>(
                schema,
                name(d.getDomainName()),
                (DataType) type(d.getDataType(), length, precision, scale, nullable, false, null, null),
                checks.toArray(EMPTY_CHECK)
            );
            domains.add(id);
            domainsByName.put(domainName, id);
        }

        // Tables
        // -------------------------------------------------------------------------------------------------------------
        tableLoop:
        for (org.jooq.util.xml.jaxb.Table xt : meta.getTables()) {
            Name schemaName = name(xt.getTableCatalog(), xt.getTableSchema());
            Schema schema = schemasByName.get(schemaName);

            if (schema == null) {
                errors.add("Schema " + schemaName + " not defined for table " + xt.getTableName());
                continue tableLoop;
            }

            TableType tableType;

            switch (xt.getTableType()) {
                case GLOBAL_TEMPORARY: tableType = TableType.TEMPORARY; break;
                case VIEW:             tableType = TableType.VIEW; break;
                case BASE_TABLE:
                default:               tableType = TableType.TABLE; break;
            }

            String sql = null;

            if (tableType == TableType.VIEW) {

                viewLoop:
                for (org.jooq.util.xml.jaxb.View vt : meta.getViews()) {
                    if (StringUtils.equals(defaultIfNull(xt.getTableCatalog(), ""), defaultIfNull(vt.getTableCatalog(), "")) &&
                        StringUtils.equals(defaultIfNull(xt.getTableSchema(), ""), defaultIfNull(vt.getTableSchema(), "")) &&
                        StringUtils.equals(defaultIfNull(xt.getTableName(), ""), defaultIfNull(vt.getTableName(), ""))) {

                        sql = vt.getViewDefinition();
                        break viewLoop;
                    }
                }
            }

            InformationSchemaTable it = new InformationSchemaTable(xt.getTableName(), schema, xt.getComment(), tableType, sql);
            tables.add(it);
            tablesByName.put(name(xt.getTableCatalog(), xt.getTableSchema(), xt.getTableName()), it);
        }

        // Columns
        // -------------------------------------------------------------------------------------------------------------
        List<Column> columns = new ArrayList<>(meta.getColumns());
        columns.sort((o1, o2) -> {
            Integer p1 = o1.getOrdinalPosition();
            Integer p2 = o2.getOrdinalPosition();

            if (Objects.equals(p1, p2))
                return 0;
            if (p1 == null)
                return -1;
            if (p2 == null)
                return 1;

            return p1.compareTo(p2);
        });

        columnLoop:
        for (Column xc : columns) {
            String typeName = xc.getDataType();
            int length = xc.getCharacterMaximumLength() == null ? 0 : xc.getCharacterMaximumLength();
            int precision = xc.getNumericPrecision() == null ? 0 : xc.getNumericPrecision();
            int scale = xc.getNumericScale() == null ? 0 : xc.getNumericScale();
            boolean nullable = !FALSE.equals(xc.isIsNullable());
            boolean readonly = TRUE.equals(xc.isReadonly());
            Field<?> generatedAlwaysAs = TRUE.equals(xc.isIsGenerated())
                ? DSL.field(xc.getGenerationExpression())
                : null;
            GenerationOption generationOption = TRUE.equals(xc.isIsGenerated())
                ? "STORED".equalsIgnoreCase(xc.getGenerationOption())
                    ? STORED
                    : "VIRTUAL".equalsIgnoreCase(xc.getGenerationOption())
                    ? VIRTUAL
                    : null
                : null;

            // TODO: Exception handling should be moved inside SQLDataType
            Name tableName = name(xc.getTableCatalog(), xc.getTableSchema(), xc.getTableName());
            InformationSchemaTable table = tablesByName.get(tableName);

            if (table == null) {
                errors.add("Table " + tableName + " not defined for column " + xc.getColumnName());
                continue columnLoop;
            }

            AbstractTable.createField(
                name(xc.getColumnName()),
                type(typeName, length, precision, scale, nullable, readonly, generatedAlwaysAs, generationOption),
                table,
                xc.getComment()
            );
        }

        // Indexes
        // -------------------------------------------------------------------------------------------------------------
        Map<Name, List<SortField<?>>> columnsByIndex = new HashMap<>();
        List<IndexColumnUsage> indexColumnUsages = new ArrayList<>(meta.getIndexColumnUsages());
        indexColumnUsages.sort(comparingInt(IndexColumnUsage::getOrdinalPosition));

        indexColumnLoop:
        for (IndexColumnUsage ic : indexColumnUsages) {
            Name indexName = name(ic.getIndexCatalog(), ic.getIndexSchema(), ic.getTableName(), ic.getIndexName());
            List<SortField<?>> fields = columnsByIndex.computeIfAbsent(indexName, k -> new ArrayList<>());

            Name tableName = name(ic.getTableCatalog(), ic.getTableSchema(), ic.getTableName());
            InformationSchemaTable table = tablesByName.get(tableName);

            if (table == null) {
                errors.add("Table " + tableName + " not defined for index " + indexName);
                continue indexColumnLoop;
            }

            TableField<Record, ?> field = (TableField<Record, ?>) table.field(ic.getColumnName());

            if (field == null) {
                errors.add("Column " + ic.getColumnName() + " not defined for table " + tableName);
                continue indexColumnLoop;
            }

            fields.add(Boolean.TRUE.equals(ic.isIsDescending()) ? field.desc() : field.asc());
        }


        indexLoop:
        for (org.jooq.util.xml.jaxb.Index i : meta.getIndexes()) {
            Name tableName = name(i.getTableCatalog(), i.getTableSchema(), i.getTableName());
            Name indexName = name(i.getIndexCatalog(), i.getIndexSchema(), i.getTableName(), i.getIndexName());
            InformationSchemaTable table = tablesByName.get(tableName);

            if (table == null) {
                errors.add("Table " + tableName + " not defined for index " + indexName);
                continue indexLoop;
            }

            List<SortField<?>> c = columnsByIndex.get(indexName);

            if (c == null || c.isEmpty()) {
                errors.add("No columns defined for index " + indexName);
                continue indexLoop;
            }

            IndexImpl index = (IndexImpl) Internal.createIndex(i.getIndexName(), table, c.toArray(EMPTY_SORTFIELD), Boolean.TRUE.equals(i.isIsUnique()));

            table.indexes.add(index);
            indexesByName.put(indexName, index);
        }

        // Constraints
        // -------------------------------------------------------------------------------------------------------------
        Map<Name, List<TableField<Record, ?>>> columnsByConstraint = new HashMap<>();
        List<KeyColumnUsage> keyColumnUsages = new ArrayList<>(meta.getKeyColumnUsages());
        keyColumnUsages.sort(comparing(KeyColumnUsage::getOrdinalPosition));

        keyColumnLoop:
        for (KeyColumnUsage xc : keyColumnUsages) {
            Name constraintName = name(xc.getConstraintCatalog(), xc.getConstraintSchema(), xc.getConstraintName());
            List<TableField<Record, ?>> fields = columnsByConstraint.computeIfAbsent(constraintName, k -> new ArrayList<>());

            Name tableName = name(xc.getTableCatalog(), xc.getTableSchema(), xc.getTableName());
            InformationSchemaTable table = tablesByName.get(tableName);

            if (table == null) {
                errors.add("Table " + tableName + " not defined for constraint " + constraintName);
                continue keyColumnLoop;
            }

            TableField<Record, ?> field = (TableField<Record, ?>) table.field(xc.getColumnName());

            if (field == null) {
                errors.add("Column " + xc.getColumnName() + " not defined for table " + tableName);
                continue keyColumnLoop;
            }

            fields.add(field);
        }

        tableConstraintLoop:
        for (TableConstraint xc : meta.getTableConstraints()) {
            switch (xc.getConstraintType()) {
                case PRIMARY_KEY:
                case UNIQUE: {
                    Name tableName = name(xc.getTableCatalog(), xc.getTableSchema(), xc.getTableName());
                    Name constraintName = name(xc.getConstraintCatalog(), xc.getConstraintSchema(), xc.getConstraintName());
                    InformationSchemaTable table = tablesByName.get(tableName);

                    if (table == null) {
                        errors.add("Table " + tableName + " not defined for constraint " + constraintName);
                        continue tableConstraintLoop;
                    }

                    List<TableField<Record, ?>> c = columnsByConstraint.get(constraintName);

                    if (c == null || c.isEmpty()) {
                        errors.add("No columns defined for constraint " + constraintName);
                        continue tableConstraintLoop;
                    }

                    UniqueKeyImpl<Record> key = (UniqueKeyImpl<Record>) Internal.createUniqueKey(table, xc.getConstraintName(), c.toArray(new TableField[0]));

                    if (xc.getConstraintType() == PRIMARY_KEY) {
                        table.primaryKey = key;
                        primaryKeys.add(key);
                    }
                    else
                        table.uniqueKeys.add(key);

                    keysByName.put(constraintName, key);
                    break;
                }
            }
        }

        for (ReferentialConstraint xr : meta.getReferentialConstraints()) {
            referentialKeys.put(
                name(xr.getConstraintCatalog(), xr.getConstraintSchema(), xr.getConstraintName()),
                name(xr.getUniqueConstraintCatalog(), xr.getUniqueConstraintSchema(), xr.getUniqueConstraintName())
            );
        }

        tableConstraintLoop:
        for (TableConstraint xc : meta.getTableConstraints()) {
            switch (xc.getConstraintType()) {
                case FOREIGN_KEY: {
                    Name tableName = name(xc.getTableCatalog(), xc.getTableSchema(), xc.getTableName());
                    Name constraintName = name(xc.getConstraintCatalog(), xc.getConstraintSchema(), xc.getConstraintName());
                    InformationSchemaTable table = tablesByName.get(tableName);

                    if (table == null) {
                        errors.add("Table " + tableName + " not defined for constraint " + constraintName);
                        continue tableConstraintLoop;
                    }

                    List<TableField<Record, ?>> c = columnsByConstraint.get(constraintName);

                    if (c == null || c.isEmpty()) {
                        errors.add("No columns defined for constraint " + constraintName);
                        continue tableConstraintLoop;
                    }

                    UniqueKeyImpl<Record> uniqueKey = keysByName.get(referentialKeys.get(constraintName));

                    if (uniqueKey == null) {
                        errors.add("No unique key defined for foreign key " + constraintName);
                        continue tableConstraintLoop;
                    }

                    ForeignKey<Record, Record> key = Internal.createForeignKey(uniqueKey, table, xc.getConstraintName(), c.toArray(new TableField[0]));
                    table.foreignKeys.add(key);
                    break;
                }
            }
        }

        tableConstraintLoop:
        for (TableConstraint xc : meta.getTableConstraints()) {
            switch (xc.getConstraintType()) {
                case CHECK: {
                    Name tableName = name(xc.getTableCatalog(), xc.getTableSchema(), xc.getTableName());
                    Name constraintName = name(xc.getConstraintCatalog(), xc.getConstraintSchema(), xc.getConstraintName());
                    InformationSchemaTable table = tablesByName.get(tableName);

                    if (table == null) {
                        errors.add("Table " + tableName + " not defined for constraint " + constraintName);
                        continue tableConstraintLoop;
                    }

                    for (CheckConstraint cc : meta.getCheckConstraints()) {
                        if (constraintName.equals(name(cc.getConstraintCatalog(), cc.getConstraintSchema(), cc.getConstraintName()))) {
                            table.checks.add(new CheckImpl<>(table, constraintName, DSL.condition(cc.getCheckClause()), true));
                            continue tableConstraintLoop;
                        }
                    }

                    errors.add("No check clause found for check constraint " + constraintName);
                    continue tableConstraintLoop;
                }
            }
        }

        // Sequences
        // -------------------------------------------------------------------------------------------------------------
        sequenceLoop:
        for (org.jooq.util.xml.jaxb.Sequence xs : meta.getSequences()) {
            Name schemaName = name(xs.getSequenceCatalog(), xs.getSequenceSchema());
            Schema schema = schemasByName.get(schemaName);

            if (schema == null) {
                errors.add("Schema " + schemaName + " not defined for sequence " + xs.getSequenceName());
                continue sequenceLoop;
            }

            String typeName = xs.getDataType();
            int length = xs.getCharacterMaximumLength() == null ? 0 : xs.getCharacterMaximumLength();
            int precision = xs.getNumericPrecision() == null ? 0 : xs.getNumericPrecision();
            int scale = xs.getNumericScale() == null ? 0 : xs.getNumericScale();
            boolean nullable = true;
            BigInteger startWith = xs.getStartValue();
            BigInteger incrementBy = xs.getIncrement();
            BigInteger minvalue = xs.getMinimumValue();
            BigInteger maxvalue = xs.getMaximumValue();
            Boolean cycle = xs.isCycleOption();
            BigInteger cache = xs.getCache();

            InformationSchemaSequence is = new InformationSchemaSequence(
                xs.getSequenceName(),
                schema,
                type(typeName, length, precision, scale, nullable, false, null, null),
                startWith,
                incrementBy,
                minvalue,
                maxvalue,
                cycle,
                cache
            );

            sequences.add(is);
        }




















































        // Lookups
        // -------------------------------------------------------------------------------------------------------------
        for (Schema s : schemas)
            initLookup(schemasPerCatalog, s.getCatalog(), s);

        for (InformationSchemaDomain<?> d : domains)
            initLookup(domainsPerSchema, d.getSchema(), d);

        for (InformationSchemaTable t : tables)
            initLookup(tablesPerSchema, t.getSchema(), t);

        for (Sequence<?> s : sequences)
            initLookup(sequencesPerSchema, s.getSchema(), s);






        if (!errors.isEmpty())
            throw new IllegalArgumentException(errors.toString());
    }

    private final <K, V> void initLookup(Map<K, List<V>> lookup, K key, V value) {
        lookup.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    @SuppressWarnings("unchecked")
    private final DataType<?> type(
        String typeName,
        int length,
        int precision,
        int scale,
        boolean nullable,
        boolean readonly,
        Field<?> generatedAlwaysAs,
        GenerationOption generationOption
    ) {
        DataType<?> type = null;

        try {
            type = DefaultDataType.getDataType(configuration.family(), typeName);
            type = type.nullable(nullable);
            type = type.readonly(readonly);

            if (length != 0)
                type = type.length(length);
            else if (precision != 0 || scale != 0)
                type = type.precision(precision, scale);

            if (generatedAlwaysAs != null)
                type = type.generatedAlwaysAs((Field) generatedAlwaysAs);
            if (generationOption != null)
                type = type.generationOption(generationOption);
        }
        catch (SQLDialectNotSupportedException e) {
            type = SQLDataType.OTHER;
        }

        return type;
    }

    @Override
    final List<Catalog> getCatalogs0() {
        return catalogs;
    }

    @Override
    final List<Schema> getSchemas0() {
        return schemas;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    final List<Table<?>> getTables0() {
        return (List) tables;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    final List<Domain<?>> getDomains0() {
        return (List) domains;
    }











    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    final List<UniqueKey<?>> getPrimaryKeys0() {
        return (List) primaryKeys;
    }

    private final class InformationSchemaCatalog extends CatalogImpl {

        InformationSchemaCatalog(String name, String comment) {
            super(name, comment);
        }

        @Override
        public final List<Schema> getSchemas() {
            return InformationSchemaMetaImpl.unmodifiableList(schemasPerCatalog.get(this));
        }
    }

    private final class InformationSchemaSchema extends SchemaImpl {

        InformationSchemaSchema(String name, Catalog catalog, String comment) {
            super(name, catalog, comment);
        }

        @Override
        public final List<Domain<?>> getDomains() {
            return InformationSchemaMetaImpl.unmodifiableList(domainsPerSchema.get(this));
        }

        @Override
        public final List<Table<?>> getTables() {
            return InformationSchemaMetaImpl.unmodifiableList(tablesPerSchema.get(this));
        }

        @Override
        public final List<Sequence<?>> getSequences() {
            return InformationSchemaMetaImpl.unmodifiableList(sequencesPerSchema.get(this));
        }









    }

    private static final class InformationSchemaTable extends TableImpl<Record> {

        UniqueKey<Record>                      primaryKey;
        final List<UniqueKey<Record>>          uniqueKeys       = new ArrayList<>();
        final List<ForeignKey<Record, Record>> foreignKeys      = new ArrayList<>();
        final List<Check<Record>>              checks           = new ArrayList<>();
        final List<Index>                      indexes          = new ArrayList<>();

        InformationSchemaTable(String name, Schema schema, String comment, TableType tableType, String source) {
            super(DSL.name(name), schema, null, null, DSL.comment(comment), tableType == TableType.VIEW ? TableOptions.view(source) : TableOptions.of(tableType));
        }

        @Override
        public List<Index> getIndexes() {
            return InformationSchemaMetaImpl.unmodifiableList(indexes);
        }

        @Override
        public UniqueKey<Record> getPrimaryKey() {
            return primaryKey;
        }

        @Override
        public List<UniqueKey<Record>> getUniqueKeys() {
            return InformationSchemaMetaImpl.unmodifiableList(uniqueKeys);
        }

        @Override
        public List<ForeignKey<Record, ?>> getReferences() {
            return InformationSchemaMetaImpl.unmodifiableList(foreignKeys);
        }

        @Override
        public List<Check<Record>> getChecks() {
            return InformationSchemaMetaImpl.unmodifiableList(checks);
        }
    }

    private static final class InformationSchemaDomain<T> extends DomainImpl<T> {

        InformationSchemaDomain(Schema schema, Name name, DataType<T> type, Check<?>[] checks) {
            super(schema, name, type, checks);
        }
    }

























    private static final class InformationSchemaSequence<N extends Number> extends SequenceImpl<N> {

        InformationSchemaSequence(String name, Schema schema, DataType<N> type, Number startWith, Number incrementBy, Number minvalue, Number maxvalue, Boolean cycle, Number cache) {
            super(DSL.name(name),
                schema,
                type,
                false,
                startWith != null ? Tools.field(startWith, type) : null,
                incrementBy != null ? Tools.field(incrementBy, type) : null,
                minvalue != null ? Tools.field(minvalue, type) : null,
                maxvalue != null ? Tools.field(maxvalue, type) : null,
                Boolean.TRUE.equals(cycle),
                cache != null ? Tools.field(cache, type) : null
            );
        }
    }

    private static final <T> List<T> unmodifiableList(List<? extends T> list) {
        return list == null ? emptyList() : Collections.unmodifiableList(list);
    }
}
