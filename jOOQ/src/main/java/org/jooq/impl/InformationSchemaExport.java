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

import static org.jooq.util.xml.jaxb.TableConstraintType.FOREIGN_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.PRIMARY_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.UNIQUE;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Key;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.tools.StringUtils;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.KeyColumnUsage;
import org.jooq.util.xml.jaxb.ReferentialConstraint;
import org.jooq.util.xml.jaxb.TableConstraint;
import org.jooq.util.xml.jaxb.TableConstraintType;

/**
 * @author Lukas Eder
 */
final class InformationSchemaExport {

    static final InformationSchema exportTables(Configuration configuration, List<Table<?>> tables) {
        InformationSchema result = new InformationSchema();

        Set<Schema> includedSchemas = new LinkedHashSet<Schema>();
        Set<Table<?>> includedTables = new LinkedHashSet<Table<?>>(tables);

        for (Table<?> t : tables)
            includedSchemas.add(t.getSchema());

        for (Schema s : includedSchemas)
            exportSchema0(result, s);

        for (Table<?> t : tables)
            exportTable0(configuration, result, t, includedTables);

        return result;
    }

    static final InformationSchema exportSchemas(Configuration configuration, List<Schema> schemas) {
        InformationSchema result = new InformationSchema();

        Set<Table<?>> includedTables = new LinkedHashSet<Table<?>>();
        for (Schema s : schemas)
            for (Table<?> t : s.getTables())
                includedTables.add(t);

        for (Schema s : schemas) {
            exportSchema0(result, s);

            for (Table<?> t : s.getTables())
                exportTable0(configuration, result, t, includedTables);

            for (Sequence<?> q : s.getSequences())
                exportSequences0(configuration, result, q);
        }

        return result;
    }

    private static final void exportSequences0(Configuration configuration, InformationSchema result, Sequence<?> q) {
        org.jooq.util.xml.jaxb.Sequence iq = new org.jooq.util.xml.jaxb.Sequence();

        if (!StringUtils.isBlank(q.getCatalog().getName()))
            iq.setSequenceCatalog(q.getCatalog().getName());

        if (!StringUtils.isBlank(q.getSchema().getName()))
            iq.setSequenceSchema(q.getSchema().getName());

        iq.setSequenceName(q.getName());
        iq.setDataType(q.getDataType().getTypeName(configuration));

        if (q.getDataType().hasLength())
            iq.setCharacterMaximumLength(q.getDataType().length());

        if (q.getDataType().hasPrecision())
            iq.setNumericPrecision(q.getDataType().precision());

        if (q.getDataType().hasScale())
            iq.setNumericScale(q.getDataType().scale());

        result.getSequences().add(iq);
    }

    private static final void exportSchema0(InformationSchema result, Schema s) {
        org.jooq.util.xml.jaxb.Schema is = new org.jooq.util.xml.jaxb.Schema();

        if (!StringUtils.isBlank(s.getCatalog().getName()))
            is.setCatalogName(s.getCatalog().getName());

        if (!StringUtils.isBlank(s.getName())) {
            is.setSchemaName(s.getName());
            result.getSchemata().add(is);
        }
    }

    private static final void exportTable0(Configuration configuration, InformationSchema result, Table<?> t, Set<Table<?>> includedTables) {
        org.jooq.util.xml.jaxb.Table it = new org.jooq.util.xml.jaxb.Table();

        if (!StringUtils.isBlank(t.getCatalog().getName()))
            it.setTableCatalog(t.getCatalog().getName());

        if (!StringUtils.isBlank(t.getSchema().getName()))
            it.setTableSchema(t.getSchema().getName());

        it.setTableName(t.getName());
        result.getTables().add(it);

        Field<?>[] fields = t.fields();
        for (int i = 0; i < fields.length; i++) {
            Field<?> f = fields[i];
            Column ic = new Column();

            if (!StringUtils.isBlank(t.getCatalog().getName()))
                ic.setTableCatalog(t.getCatalog().getName());

            if (!StringUtils.isBlank(t.getSchema().getName()))
                ic.setTableSchema(t.getSchema().getName());

            ic.setTableName(t.getName());
            ic.setColumnName(f.getName());
            ic.setDataType(f.getDataType().getTypeName(configuration));

            if (f.getDataType().hasLength())
                ic.setCharacterMaximumLength(f.getDataType().length());

            if (f.getDataType().hasPrecision())
                ic.setNumericPrecision(f.getDataType().precision());

            if (f.getDataType().hasScale())
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
    }

    private static final void exportKey0(InformationSchema result, Table<?> t, Key<?> key, TableConstraintType constraintType) {
        TableConstraint tc = new TableConstraint();

        String catalogName = t.getCatalog().getName();
        String schemaName = t.getSchema().getName();

        tc.setConstraintName(key.getName());
        tc.setConstraintType(constraintType);

        if (!StringUtils.isBlank(catalogName)) {
            tc.setConstraintCatalog(catalogName);
            tc.setTableCatalog(catalogName);
        }

        if (!StringUtils.isBlank(schemaName)) {
            tc.setConstraintSchema(schemaName);
            tc.setTableSchema(schemaName);
        }

        tc.setTableName(t.getName());
        result.getTableConstraints().add(tc);

        int i = 0;
        for (Field<?> f : key.getFields()) {
            KeyColumnUsage kc = new KeyColumnUsage();

            if (!StringUtils.isBlank(catalogName)) {
                kc.setConstraintCatalog(catalogName);
                kc.setTableCatalog(catalogName);
            }

            if (!StringUtils.isBlank(schemaName)) {
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
            String ukCatalogName = uk.getTable().getCatalog().getName();
            String ukSchemaName = uk.getTable().getSchema().getName();

            if (!StringUtils.isBlank(catalogName))
                rc.setConstraintCatalog(catalogName);

            if (!StringUtils.isBlank(ukCatalogName))
                rc.setUniqueConstraintCatalog(ukCatalogName);

            if (!StringUtils.isBlank(schemaName))
                rc.setConstraintSchema(schemaName);

            if (!StringUtils.isBlank(ukSchemaName))
                rc.setUniqueConstraintSchema(ukSchemaName);

            rc.setConstraintName(key.getName());
            rc.setUniqueConstraintName(uk.getName());

            result.getReferentialConstraints().add(rc);
        }
    }

    private InformationSchemaExport() {}
}
