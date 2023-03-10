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

package org.jooq.meta.trino;

import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.when;
import static org.jooq.meta.hsqldb.information_schema.Tables.SCHEMATA;
import static org.jooq.meta.hsqldb.information_schema.Tables.TABLES;
import static org.jooq.meta.hsqldb.information_schema.Tables.VIEWS;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record12;
import org.jooq.Record6;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.TableOptions.TableType;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.XMLSchemaCollectionDefinition;
import org.jooq.meta.hsqldb.HSQLDBDatabase;

/**
 * The Trino database
 *
 * @author Lukas Eder
 */
public class TrinoDatabase extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.TRINO);
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Record record : create()
                .select(
                    TABLES.TABLE_SCHEMA,
                    TABLES.TABLE_NAME,
                    when(TABLES.TABLE_TYPE.eq(inline("VIEW")), inline(TableType.VIEW.name()))
                        .else_(inline(TableType.TABLE.name())).trim().as("table_type"),
                    when(VIEWS.VIEW_DEFINITION.lower().like(inline("create%")), VIEWS.VIEW_DEFINITION)
                        .else_(inline("create view \"").concat(TABLES.TABLE_NAME).concat("\" as ").concat(VIEWS.VIEW_DEFINITION)).as(VIEWS.VIEW_DEFINITION)
                )
                .from(TABLES)
                .leftJoin(VIEWS)
                    .on(TABLES.TABLE_SCHEMA.eq(VIEWS.TABLE_SCHEMA))
                    .and(TABLES.TABLE_NAME.eq(VIEWS.TABLE_NAME))
                .where(TABLES.TABLE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    TABLES.TABLE_SCHEMA,
                    TABLES.TABLE_NAME)
        ) {
            SchemaDefinition schema = getSchema(record.get(TABLES.TABLE_SCHEMA));
            String name = record.get(TABLES.TABLE_NAME);
            String comment = "";
            TableType tableType = record.get("table_type", TableType.class);
            String source = record.get(VIEWS.VIEW_DEFINITION);

            result.add(new TrinoTableDefinition(schema, name, comment, tableType, source));
        }

        return result;
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        return
        create().select(SCHEMATA.SCHEMA_NAME)
                .from(SCHEMATA)
                .fetch(mapping(s -> new SchemaDefinition(this, s, "")));
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    protected List<XMLSchemaCollectionDefinition> getXMLSchemaCollections0() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        return new ArrayList<>();
    }
}
