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
 */
package org.jooq.impl;

import static java.util.Collections.unmodifiableList;
import static org.jooq.impl.DSL.name;
import static org.jooq.util.xml.jaxb.TableConstraintType.PRIMARY_KEY;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.ForeignKey;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.KeyColumnUsage;
import org.jooq.util.xml.jaxb.ReferentialConstraint;
import org.jooq.util.xml.jaxb.TableConstraint;

/**
 * @author Lukas Eder
 */
final class InformationSchemaMetaImpl implements Meta {

    private final Configuration                             configuration;
    private final InformationSchema                         source;

    private final List<Catalog>                             catalogs;
    private final List<Schema>                              schemas;
    private final Map<Name, Schema>                         schemasByName;
    private final Map<Catalog, List<Schema>>                schemasPerCatalog;
    private final List<InformationSchemaTable>              tables;
    private final Map<Name, InformationSchemaTable>         tablesByName;
    private final Map<Schema, List<InformationSchemaTable>> tablesPerSchema;
    private final List<Sequence<?>>                         sequences;
    private final Map<Schema, List<Sequence<?>>>            sequencesPerSchema;
    private final List<UniqueKeyImpl<Record>>               primaryKeys;
    private final Map<Name, UniqueKeyImpl<Record>>          uniqueKeysByName;
    private final Map<Name, Name>                           referentialKeys;

    InformationSchemaMetaImpl(Configuration configuration, InformationSchema source) {
        this.configuration = configuration;
        this.source = source;
        this.catalogs = new ArrayList<Catalog>();
        this.schemas = new ArrayList<Schema>();
        this.schemasByName = new HashMap<Name, Schema>();
        this.schemasPerCatalog = new HashMap<Catalog, List<Schema>>();
        this.tables = new ArrayList<InformationSchemaTable>();
        this.tablesByName = new HashMap<Name, InformationSchemaTable>();
        this.tablesPerSchema = new HashMap<Schema, List<InformationSchemaTable>>();
        this.sequences = new ArrayList<Sequence<?>>();
        this.sequencesPerSchema = new HashMap<Schema, List<Sequence<?>>>();
        this.primaryKeys = new ArrayList<UniqueKeyImpl<Record>>();
        this.uniqueKeysByName = new HashMap<Name, UniqueKeyImpl<Record>>();
        this.referentialKeys = new HashMap<Name, Name>();

        init(source);
    }
public static void main(String[] args) {
    System.out.println(String.format("x y %0$s", "abc"));
}
    private final void init(InformationSchema meta) {

        List<String> errors = new ArrayList<String>();

        // Catalogs / Schemas
        // -------------------------------------------------------------------------------------------------------------
        for (org.jooq.util.xml.jaxb.Schema xs : meta.getSchemata()) {
            InformationSchemaCatalog catalog = new InformationSchemaCatalog(xs.getCatalogName());

            if (!catalogs.contains(catalog))
                catalogs.add(catalog);

            InformationSchemaSchema is = new InformationSchemaSchema(xs.getSchemaName(), catalog);
            schemas.add(is);
            schemasByName.put(name(xs.getCatalogName(), xs.getSchemaName()), is);
        }

        // Tables
        // -------------------------------------------------------------------------------------------------------------
        tableLoop:
        for (org.jooq.util.xml.jaxb.Table xt : meta.getTables()) {
            Name schemaName = name(xt.getTableCatalog(), xt.getTableSchema());
            Schema schema = schemasByName.get(schemaName);

            if (schema == null) {
                errors.add(String.format("Schema " + schemaName + " not defined for table " + xt.getTableName()));
                continue tableLoop;
            }

            InformationSchemaTable it = new InformationSchemaTable(xt.getTableName(), schema);
            tables.add(it);
            tablesByName.put(name(xt.getTableCatalog(), xt.getTableSchema(), xt.getTableName()), it);
        }

        // Columns
        // -------------------------------------------------------------------------------------------------------------
        List<Column> columns = new ArrayList<Column>(meta.getColumns());
        Collections.sort(columns, new Comparator<Column>() {
            @Override
            public int compare(Column o1, Column o2) {
                Integer p1 = o1.getOrdinalPosition();
                Integer p2 = o2.getOrdinalPosition();

                if (p1 == p2)
                    return 0;
                if (p1 == null)
                    return -1;
                if (p2 == null)
                    return 1;

                return p1.compareTo(p2);
            }
        });

        columnLoop:
        for (Column xc : columns) {
            String typeName = xc.getDataType();
            int length = xc.getCharacterMaximumLength() == null ? 0 : xc.getCharacterMaximumLength();
            int precision = xc.getNumericPrecision() == null ? 0 : xc.getNumericPrecision();
            int scale = xc.getNumericScale() == null ? 0 : xc.getNumericScale();
            boolean nullable = xc.isIsNullable() == null ? true : xc.isIsNullable();

            // TODO: Exception handling should be moved inside SQLDataType
            Name tableName = name(xc.getTableCatalog(), xc.getTableSchema(), xc.getTableName());
            InformationSchemaTable table = tablesByName.get(tableName);

            if (table == null) {
                errors.add(String.format("Table " + tableName + " not defined for column " + xc.getColumnName()));
                continue columnLoop;
            }

            AbstractTable.createField(
                xc.getColumnName(),
                type(typeName, length, precision, scale, nullable),
                table
            );
        }

        // Constraints
        // -------------------------------------------------------------------------------------------------------------
        Map<Name, List<TableField<Record, ?>>> columnsByConstraint = new HashMap<Name, List<TableField<Record, ?>>>();
        List<KeyColumnUsage> keyColumnUsages = new ArrayList<KeyColumnUsage>(meta.getKeyColumnUsages());
        Collections.sort(keyColumnUsages, new Comparator<KeyColumnUsage>() {
            @Override
            public int compare(KeyColumnUsage o1, KeyColumnUsage o2) {
                int p1 = o1.getOrdinalPosition();
                int p2 = o2.getOrdinalPosition();

                return (p1 < p2) ? -1 : ((p1 == p2) ? 0 : 1);
            }
        });

        keyColumnLoop:
        for (KeyColumnUsage xc : keyColumnUsages) {
            Name constraintName = name(xc.getConstraintCatalog(), xc.getConstraintSchema(), xc.getConstraintName());
            List<TableField<Record, ?>> fields = columnsByConstraint.get(constraintName);

            if (fields == null) {
                fields = new ArrayList<TableField<Record, ?>>();
                columnsByConstraint.put(constraintName, fields);
            }

            Name tableName = name(xc.getTableCatalog(), xc.getTableSchema(), xc.getTableName());
            InformationSchemaTable table = tablesByName.get(tableName);

            if (table == null) {
                errors.add(String.format("Table " + tableName + " not defined for constraint " + constraintName));
                continue keyColumnLoop;
            }

            TableField<Record, ?> field = (TableField<Record, ?>) table.field(xc.getColumnName());

            if (field == null) {
                errors.add(String.format("Column " + xc.getColumnName() + " not defined for table " + tableName));
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
                        errors.add(String.format("Table " + tableName + " not defined for constraint " + constraintName));
                        continue tableConstraintLoop;
                    }

                    List<TableField<Record, ?>> c = columnsByConstraint.get(constraintName);

                    if (c == null || c.isEmpty()) {
                        errors.add(String.format("No columns defined for constraint " + constraintName));
                        continue tableConstraintLoop;
                    }

                    UniqueKeyImpl<Record> key = (UniqueKeyImpl<Record>) AbstractKeys.createUniqueKey(table, xc.getConstraintName(), c.toArray(new TableField[0]));

                    if (xc.getConstraintType() == PRIMARY_KEY) {
                        table.primaryKey = key;
                        primaryKeys.add(key);
                    }

                    table.uniqueKeys.add(key);
                    uniqueKeysByName.put(constraintName, key);
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
                        errors.add(String.format("Table " + tableName + " not defined for constraint " + constraintName));
                        continue tableConstraintLoop;
                    }

                    List<TableField<Record, ?>> c = columnsByConstraint.get(constraintName);

                    if (c == null || c.isEmpty()) {
                        errors.add(String.format("No columns defined for constraint " + constraintName));
                        continue tableConstraintLoop;
                    }

                    UniqueKeyImpl<Record> uniqueKey = uniqueKeysByName.get(referentialKeys.get(constraintName));

                    if (uniqueKey == null) {
                        errors.add(String.format("No unique key defined for foreign key " + constraintName));
                        continue tableConstraintLoop;
                    }

                    ForeignKey<Record, Record> key = AbstractKeys.createForeignKey(uniqueKey, table, xc.getConstraintName(), c.toArray(new TableField[0]));
                    table.foreignKeys.add(key);
                    break;
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
                errors.add(String.format("Schema " + schemaName + " not defined for sequence " + xs.getSequenceName()));
                continue sequenceLoop;
            }

            String typeName = xs.getDataType();
            int length = xs.getCharacterMaximumLength() == null ? 0 : xs.getCharacterMaximumLength();
            int precision = xs.getNumericPrecision() == null ? 0 : xs.getNumericPrecision();
            int scale = xs.getNumericScale() == null ? 0 : xs.getNumericScale();
            boolean nullable = true;

            @SuppressWarnings({ "rawtypes", "unchecked" })
            InformationSchemaSequence is = new InformationSchemaSequence(
                xs.getSequenceName(),
                schema,
                type(typeName, length, precision, scale, nullable)
            );

            sequences.add(is);
        }

        // Lookups
        // -------------------------------------------------------------------------------------------------------------
        for (Schema s : schemas) {
            Catalog c = s.getCatalog();
            List<Schema> list = schemasPerCatalog.get(c);

            if (list == null) {
                list = new ArrayList<Schema>();
                schemasPerCatalog.put(c, list);
            }

            list.add(s);
        }

        for (InformationSchemaTable t : tables) {
            Schema s = t.getSchema();
            List<InformationSchemaTable> list = tablesPerSchema.get(s);

            if (list == null) {
                list = new ArrayList<InformationSchemaTable>();
                tablesPerSchema.put(s, list);
            }

            list.add(t);
        }

        for (Sequence<?> q : sequences) {
            Schema s = q.getSchema();
            List<Sequence<?>> list = sequencesPerSchema.get(s);

            if (list == null) {
                list = new ArrayList<Sequence<?>>();
                sequencesPerSchema.put(s, list);
            }

            list.add(q);
        }

        if (!errors.isEmpty())
            throw new IllegalArgumentException(errors.toString());
    }

    private final DataType<?> type(String typeName, int length, int precision, int scale, boolean nullable) {
        DataType<?> type = null;

        try {
            type = DefaultDataType.getDataType(configuration.family(), typeName);
            type = type.nullable(nullable);

            if (length != 0)
                type = type.length(length);
            else if (precision != 0 || scale != 0)
                type = type.precision(precision, scale);
        }
        catch (SQLDialectNotSupportedException e) {
            type = SQLDataType.OTHER;
        }

        return type;
    }

    @Override
    public final List<Catalog> getCatalogs() {
        return Collections.<Catalog>unmodifiableList(catalogs);
    }

    @Override
    public final List<Schema> getSchemas() {
        return Collections.<Schema>unmodifiableList(schemas);
    }

    @Override
    public final List<Table<?>> getTables() {
        return Collections.<Table<?>>unmodifiableList(tables);
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return Collections.<Sequence<?>>unmodifiableList(sequences);
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys() {
        return Collections.<UniqueKey<?>>unmodifiableList(primaryKeys);
    }

    private final class InformationSchemaCatalog extends CatalogImpl {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 87038321849045492L;

        InformationSchemaCatalog(String name) {
            super(name);
        }

        @Override
        public final List<Schema> getSchemas() {
            return unmodifiableList(schemasPerCatalog.get(this));
        }
    }

    private final class InformationSchemaSchema extends SchemaImpl {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7290709749127378187L;

        public InformationSchemaSchema(String name, Catalog catalog) {
            super(name, catalog);
        }

        @Override
        public final List<Table<?>> getTables() {
            return Collections.<Table<?>>unmodifiableList(tablesPerSchema.get(this));
        }

        @Override
        public final List<Sequence<?>> getSequences() {
            return Collections.<Sequence<?>>unmodifiableList(sequencesPerSchema.get(this));
        }
    }

    private final class InformationSchemaTable extends TableImpl<Record> {

        /**
         * Generated UID
         */
        private static final long              serialVersionUID = 4314110578549768267L;

        UniqueKey<Record>                      primaryKey;
        final List<UniqueKey<Record>>          uniqueKeys       = new ArrayList<UniqueKey<Record>>();
        final List<ForeignKey<Record, Record>> foreignKeys      = new ArrayList<ForeignKey<Record, Record>>();

        public InformationSchemaTable(String name, Schema schema) {
            super(name, schema);
        }

        @Override
        public UniqueKey<Record> getPrimaryKey() {
            return primaryKey;
        }

        @Override
        public List<UniqueKey<Record>> getKeys() {
            return Collections.<UniqueKey<Record>>unmodifiableList(uniqueKeys);
        }

        @Override
        public List<ForeignKey<Record, ?>> getReferences() {
            return Collections.<ForeignKey<Record, ?>>unmodifiableList(foreignKeys);
        }
    }

    private final class InformationSchemaSequence<N extends Number> extends SequenceImpl<N> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -1246697252597049756L;

        InformationSchemaSequence(String name, Schema schema, DataType<N> type) {
            super(name, schema, type);
        }
    }

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        JAXB.marshal(source, writer);
        return writer.toString();
    }
}
