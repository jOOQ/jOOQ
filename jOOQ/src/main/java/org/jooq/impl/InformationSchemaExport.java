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

import static org.jooq.tools.StringUtils.isBlank;
import static org.jooq.util.xml.jaxb.TableConstraintType.CHECK;
import static org.jooq.util.xml.jaxb.TableConstraintType.FOREIGN_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.PRIMARY_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.UNIQUE;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Configuration;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Key;
import org.jooq.Param;
import org.jooq.Qualified;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.util.xml.jaxb.CheckConstraint;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.IndexColumnUsage;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.KeyColumnUsage;
import org.jooq.util.xml.jaxb.ReferentialConstraint;
import org.jooq.util.xml.jaxb.TableConstraint;
import org.jooq.util.xml.jaxb.TableConstraintType;
import org.jooq.util.xml.jaxb.TableType;

/**
 * @author Lukas Eder
 */
final class InformationSchemaExport {

    static final InformationSchema exportTables(Configuration configuration, List<Table<?>> tables) {
        InformationSchema result = new InformationSchema();

        Set<Catalog> includedCatalogs = new LinkedHashSet<>();
        Set<Schema> includedSchemas = new LinkedHashSet<>();
        Set<Table<?>> includedTables = new LinkedHashSet<>(tables);

        for (Table<?> t : tables)
            if (t.getSchema() != null)
                includedSchemas.add(t.getSchema());

        for (Schema s : includedSchemas)
            if (s.getCatalog() != null)
                includedCatalogs.add(s.getCatalog());

        for (Catalog c : includedCatalogs)
            exportCatalog0(result, c);

        for (Schema s : includedSchemas)
            exportSchema0(result, s);

        for (Table<?> t : tables)
            exportTable0(configuration, result, t, includedTables);

        return result;
    }

    static final InformationSchema exportSchemas(Configuration configuration, List<Schema> schemas) {
        InformationSchema result = new InformationSchema();

        Set<Catalog> includedCatalogs = new LinkedHashSet<>();
        Set<Table<?>> includedTables = new LinkedHashSet<>();

        for (Schema s : schemas) {
            if (s.getCatalog() != null)
                includedCatalogs.add(s.getCatalog());

            includedTables.addAll(s.getTables());
        }

        for (Catalog c : includedCatalogs)
            exportCatalog0(result, c);

        for (Schema s : schemas) {
            exportSchema0(result, s);

            for (Domain<?> d : s.getDomains())
                exportDomain0(configuration, result, d);

            for (Table<?> t : s.getTables())
                exportTable0(configuration, result, t, includedTables);

            for (Sequence<?> q : s.getSequences())
                exportSequence0(configuration, result, q);
        }

        return result;
    }

    static final InformationSchema exportCatalogs(Configuration configuration, List<Catalog> catalogs) {
        InformationSchema result = new InformationSchema();

        Set<Table<?>> includedTables = new LinkedHashSet<>();

        for (Catalog c : catalogs)
            for (Schema s : c.getSchemas())
                includedTables.addAll(s.getTables());

        for (Catalog c : catalogs) {
            exportCatalog0(result, c);

            for (Schema s : c.getSchemas()) {
                exportSchema0(result, s);

                for (Domain<?> d : s.getDomains())
                    exportDomain0(configuration, result, d);

                for (Table<?> t : s.getTables())
                    exportTable0(configuration, result, t, includedTables);

                for (Sequence<?> q : s.getSequences())
                    exportSequence0(configuration, result, q);
            }
        }

        return result;
    }

    private static final void exportDomain0(Configuration configuration, InformationSchema result, Domain<?> d) {
        org.jooq.util.xml.jaxb.Domain id = new org.jooq.util.xml.jaxb.Domain();

        String catalogName = catalogName(d);
        String schemaName = schemaName(d);
        String domainName = d.getName();

        if (!isBlank(catalogName))
            id.setDomainCatalog(catalogName);

        if (!isBlank(schemaName))
            id.setDomainSchema(schemaName);

        id.setDomainName(domainName);
        id.setDataType(d.getDataType().getTypeName(configuration));

        if (d.getDataType().lengthDefined())
            id.setCharacterMaximumLength(d.getDataType().length());

        if (d.getDataType().precisionDefined())
            id.setNumericPrecision(d.getDataType().precision());

        if (d.getDataType().scaleDefined())
            id.setNumericScale(d.getDataType().scale());

        result.getDomains().add(id);

        for (Check<?> c : d.getChecks()) {
            org.jooq.util.xml.jaxb.DomainConstraint idc = new org.jooq.util.xml.jaxb.DomainConstraint();
            org.jooq.util.xml.jaxb.CheckConstraint icc = new org.jooq.util.xml.jaxb.CheckConstraint();

            if (!isBlank(catalogName)) {
                idc.setDomainCatalog(catalogName);
                idc.setConstraintCatalog(catalogName);
                icc.setConstraintCatalog(catalogName);
            }

            if (!isBlank(schemaName)) {
                idc.setDomainSchema(schemaName);
                idc.setConstraintSchema(schemaName);
                icc.setConstraintSchema(schemaName);
            }

            idc.setDomainName(domainName);
            idc.setConstraintName(c.getName());
            icc.setConstraintName(c.getName());
            icc.setCheckClause(configuration.dsl().render(c.condition()));

            result.getDomainConstraints().add(idc);
            result.getCheckConstraints().add(icc);
        }
    }

    private static final void exportSequence0(Configuration configuration, InformationSchema result, Sequence<?> q) {
        org.jooq.util.xml.jaxb.Sequence iq = new org.jooq.util.xml.jaxb.Sequence();

        String catalogName = catalogName(q);
        String schemaName = schemaName(q);

        if (!isBlank(catalogName))
            iq.setSequenceCatalog(catalogName);

        if (!isBlank(schemaName))
            iq.setSequenceSchema(schemaName);

        iq.setSequenceName(q.getName());
        iq.setDataType(q.getDataType().getTypeName(configuration));

        if (q.getDataType().lengthDefined())
            iq.setCharacterMaximumLength(q.getDataType().length());

        if (q.getDataType().precisionDefined())
            iq.setNumericPrecision(q.getDataType().precision());

        if (q.getDataType().scaleDefined())
            iq.setNumericScale(q.getDataType().scale());

        if (q.getStartWith() != null)
            iq.setStartValue(Convert.convert(q.getStartWith() instanceof Param ? ((Param<?>) q.getStartWith()).getValue() : q.getStartWith().toString(), BigInteger.class));
        if (q.getIncrementBy() != null)
            iq.setIncrement(Convert.convert(q.getIncrementBy() instanceof Param ? ((Param<?>) q.getIncrementBy()).getValue() : q.getIncrementBy().toString(), BigInteger.class));
        if (q.getMinvalue() != null)
            iq.setMinimumValue(Convert.convert(q.getMinvalue() instanceof Param ? ((Param<?>) q.getMinvalue()).getValue() : q.getMinvalue().toString(), BigInteger.class));
        if (q.getMaxvalue() != null)
            iq.setMaximumValue(Convert.convert(q.getMaxvalue() instanceof Param ? ((Param<?>) q.getMaxvalue()).getValue() : q.getMaxvalue().toString(), BigInteger.class));
        iq.setCycleOption(q.getCycle());
        if (q.getCache() != null)
            iq.setCache(Convert.convert(q.getCache() instanceof Param ? ((Param<?>) q.getCache()).getValue() : q.getCache().toString(), BigInteger.class));

        result.getSequences().add(iq);
    }

    private static final void exportCatalog0(InformationSchema result, Catalog c) {
        org.jooq.util.xml.jaxb.Catalog ic = new org.jooq.util.xml.jaxb.Catalog();

        if (!isBlank(c.getName())) {
            ic.setCatalogName(c.getName());
            ic.setComment(c.getComment());
            result.getCatalogs().add(ic);
        }
    }

    private static final void exportSchema0(InformationSchema result, Schema s) {
        org.jooq.util.xml.jaxb.Schema is = new org.jooq.util.xml.jaxb.Schema();

        String catalogName = catalogName(s);

        if (!isBlank(catalogName))
            is.setCatalogName(catalogName);

        if (!isBlank(s.getName())) {
            is.setSchemaName(s.getName());
            is.setComment(s.getComment());
            result.getSchemata().add(is);
        }
    }

    private static final void exportTable0(Configuration configuration, InformationSchema result, Table<?> t, Set<Table<?>> includedTables) {
        org.jooq.util.xml.jaxb.Table it = new org.jooq.util.xml.jaxb.Table();

        String catalogName = catalogName(t);
        String schemaName = schemaName(t);

        if (!isBlank(catalogName))
            it.setTableCatalog(catalogName);

        if (!isBlank(schemaName))
            it.setTableSchema(schemaName);

        switch (t.getOptions().type()) {
            case MATERIALIZED_VIEW:
            case VIEW:              it.setTableType(TableType.VIEW); break;
            case TEMPORARY:         it.setTableType(TableType.GLOBAL_TEMPORARY); break;
            case FUNCTION:
            case TABLE:
            case EXPRESSION:
            case UNKNOWN:
            default:                it.setTableType(TableType.BASE_TABLE); break;
        }

        it.setTableName(t.getName());
        it.setComment(t.getComment());
        result.getTables().add(it);

        if (t.getOptions().type() == org.jooq.TableOptions.TableType.VIEW) {
            org.jooq.util.xml.jaxb.View iv = new org.jooq.util.xml.jaxb.View();

            if (!isBlank(catalogName))
                iv.setTableCatalog(catalogName);

            if (!isBlank(schemaName))
                iv.setTableSchema(schemaName);

            iv.setTableName(t.getName());
            iv.setViewDefinition(t.getOptions().source());
            result.getViews().add(iv);
        }

        Field<?>[] fields = t.fields();
        for (int i = 0; i < fields.length; i++) {
            Field<?> f = fields[i];
            Column ic = new Column();

            if (!isBlank(catalogName))
                ic.setTableCatalog(catalogName);

            if (!isBlank(schemaName))
                ic.setTableSchema(schemaName);

            ic.setTableName(t.getName());
            ic.setColumnName(f.getName());
            ic.setComment(f.getComment());
            ic.setDataType(f.getDataType().getTypeName(configuration));

            if (f.getDataType().lengthDefined())
                ic.setCharacterMaximumLength(f.getDataType().length());

            if (f.getDataType().precisionDefined())
                ic.setNumericPrecision(f.getDataType().precision());

            if (f.getDataType().scaleDefined())
                ic.setNumericScale(f.getDataType().scale());

            ic.setColumnDefault(DSL.using(configuration).render(f.getDataType().defaultValue()));
            ic.setIsNullable(f.getDataType().nullable());
            ic.setOrdinalPosition(i + 1);

            result.getColumns().add(ic);
        }

        for (UniqueKey<?> key : t.getKeys())
            exportKey0(result, t, key, key.isPrimary() ? PRIMARY_KEY : UNIQUE);

        for (ForeignKey<?, ?> fk : t.getReferences())
            if (includedTables.contains(fk.getKey().getTable()))
                exportKey0(result, t, fk, FOREIGN_KEY);

        for (Check<?> chk : t.getChecks())
            if (includedTables.contains(chk.getTable()))
                exportCheck0(configuration, result, t, chk);

        for (Index index : t.getIndexes())
            exportIndex0(result, t, index);
    }

    private static final void exportCheck0(Configuration configuration, InformationSchema result, Table<?> t, Check<?> chk) {
        exportTableConstraint(result, t, chk.getName(), CHECK);

        CheckConstraint c = new CheckConstraint();

        String catalogName = catalogName(t);
        String schemaName = schemaName(t);

        if (!isBlank(catalogName))
            c.setConstraintCatalog(catalogName);

        if (!isBlank(schemaName))
            c.setConstraintSchema(schemaName);

        c.setConstraintName(chk.getName());
        c.setCheckClause(configuration.dsl().render(chk.condition()));
        result.getCheckConstraints().add(c);
    }

    private static final void exportIndex0(InformationSchema result, Table<?> t, Index index) {
        org.jooq.util.xml.jaxb.Index i = new org.jooq.util.xml.jaxb.Index();

        String catalogName = catalogName(t);
        String schemaName = schemaName(t);

        if (!isBlank(catalogName))
            i.withIndexCatalog(catalogName)
             .withTableCatalog(catalogName);

        if (!isBlank(schemaName))
            i.withIndexSchema(schemaName)
             .withTableSchema(schemaName);

        i.setIndexName(index.getName());
        i.setTableName(t.getName());
        i.setIsUnique(index.getUnique());
        result.getIndexes().add(i);

        int position = 1;
        for (SortField<?> sortField : index.getFields()) {
            IndexColumnUsage ic = new IndexColumnUsage();

            if (!isBlank(catalogName))
                ic.withIndexCatalog(catalogName)
                  .withTableCatalog(catalogName);

            if (!isBlank(schemaName))
                ic.withIndexSchema(schemaName)
                  .withTableSchema(schemaName);

            ic.setIndexName(index.getName());
            ic.setTableName(t.getName());

            ic.setOrdinalPosition(position++);
            ic.setColumnName(sortField.getName());
            ic.setIsDescending(sortField.getOrder() == SortOrder.DESC);

            result.getIndexColumnUsages().add(ic);
        }
    }

    private static final void exportKey0(InformationSchema result, Table<?> t, Key<?> key, TableConstraintType constraintType) {
        exportTableConstraint(result, t, key.getName(), constraintType);

        String catalogName = catalogName(t);
        String schemaName = schemaName(t);

        int i = 0;
        for (Field<?> f : key.getFields()) {
            KeyColumnUsage kc = new KeyColumnUsage();

            if (!isBlank(catalogName)) {
                kc.setConstraintCatalog(catalogName);
                kc.setTableCatalog(catalogName);
            }

            if (!isBlank(schemaName)) {
                kc.setConstraintSchema(schemaName);
                kc.setTableSchema(schemaName);
            }

            kc.setColumnName(f.getName());
            kc.setTableName(t.getName());
            kc.setOrdinalPosition(++i);
            kc.setConstraintName(key.getName());

            result.getKeyColumnUsages().add(kc);
        }

        if (constraintType == FOREIGN_KEY) {
            ReferentialConstraint rc = new ReferentialConstraint();
            UniqueKey<?> uk = ((ForeignKey<?, ?>) key).getKey();
            String ukCatalogName = catalogName(uk.getTable());
            String ukSchemaName = schemaName(uk.getTable());

            if (!isBlank(catalogName))
                rc.setConstraintCatalog(catalogName);

            if (!isBlank(ukCatalogName))
                rc.setUniqueConstraintCatalog(ukCatalogName);

            if (!isBlank(schemaName))
                rc.setConstraintSchema(schemaName);

            if (!isBlank(ukSchemaName))
                rc.setUniqueConstraintSchema(ukSchemaName);

            rc.setConstraintName(key.getName());
            rc.setUniqueConstraintName(uk.getName());

            result.getReferentialConstraints().add(rc);
        }
    }

    private static final void exportTableConstraint(InformationSchema result, Table<?> t, String constraintName, TableConstraintType constraintType) {
        TableConstraint tc = new TableConstraint();

        String catalogName = catalogName(t);
        String schemaName = schemaName(t);

        tc.setConstraintName(constraintName);
        tc.setConstraintType(constraintType);

        if (!isBlank(catalogName))
            tc.withConstraintCatalog(catalogName)
              .withTableCatalog(catalogName);

        if (!isBlank(schemaName))
            tc.withConstraintSchema(schemaName)
              .withTableSchema(schemaName);

        tc.setTableName(t.getName());
        result.getTableConstraints().add(tc);
    }

    private static final String catalogName(Schema s) {
        return s.getCatalog() == null ? null : s.getCatalog().getName();
    }

    private static final String catalogName(Qualified q) {
        return q.getCatalog() == null ? null : q.getCatalog().getName();
    }

    private static final String schemaName(Qualified q) {
        return q.getSchema() == null ? null : q.getSchema().getName();
    }

    private InformationSchemaExport() {}
}
