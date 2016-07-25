/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import java.util.List;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.tools.StringUtils;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.InformationSchema;

/**
 * @author Lukas Eder
 */
final class InformationSchemaExport {

    static final InformationSchema export(Configuration configuration, List<Schema> schemas) {
        InformationSchema result = new InformationSchema();

        for (Schema s : schemas) {
            org.jooq.util.xml.jaxb.Schema is = new org.jooq.util.xml.jaxb.Schema();

            if (!StringUtils.isBlank(s.getCatalog().getName()))
                is.setCatalogName(s.getCatalog().getName());

            if (!StringUtils.isBlank(s.getName())) {
                is.setSchemaName(s.getName());
                result.getSchemata().add(is);
            }

            for (Table<?> t : s.getTables()) {
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
                    ic.setColumnName(t.getName());
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
            }

            for (Sequence<?> q : s.getSequences()) {
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
        }

        return result;
    }

    private InformationSchemaExport() {}
}
