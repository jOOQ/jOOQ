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

package org.jooq.meta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Meta;
import org.jooq.MetaProvider;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;

/**
 * The base implementation for {@link Meta} based databases.
 *
 * @author Lukas Eder
 */
public abstract class AbstractMetaDatabase extends AbstractDatabase {

    private List<Catalog> catalogs;
    private List<Schema>  schemas;

    @Override
    protected DSLContext create0() {
        DSLContext ctx = DSL.using(getConnection());
        ctx.configuration().set(new MetaProvider() {
            @Override
            public Meta provide() {
                return AbstractMetaDatabase.this.getMeta0();
            }
        });

        return ctx;
    }

    abstract protected Meta getMeta0();

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Schema schema : getSchemasFromMeta()) {
            SchemaDefinition s = getSchema(schema.getName());

            if (s != null) {
                for (Table<?> table : schema.getTables()) {
                    TableDefinition t = getTable(s, table.getName());

                    if (t != null) {
                        UniqueKey<?> key = table.getPrimaryKey();

                        if (key != null)
                            for (Field<?> field : key.getFields())
                                relations.addPrimaryKey("PK_" + key.getTable().getName(), t, t.getColumn(field.getName()));
                    }
                }
            }
        }
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<>();

        for (Catalog catalog : getCatalogsFromMeta())
            result.add(new CatalogDefinition(this, catalog.getName(), ""));

        Collections.sort(result, COMP);
        return result;
    }

    private List<Catalog> getCatalogsFromMeta() {
        if (catalogs == null) {
            catalogs = new ArrayList<>();

            for (Catalog catalog : getMeta0().getCatalogs())
                catalogs.add(catalog);
        }

        return catalogs;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<>();

        for (Schema schema : getSchemasFromMeta()) {
            if (schema.getCatalog() != null) {
                CatalogDefinition catalog = getCatalog(schema.getCatalog().getName());

                if (catalog != null)
                    result.add(new SchemaDefinition(this, schema.getName(), "", catalog));
            }
            else
                result.add(new SchemaDefinition(this, schema.getName(), ""));
        }

        Collections.sort(result, COMP);
        return result;
    }

    private List<Schema> getSchemasFromMeta() {
        if (schemas == null) {
            schemas = new ArrayList<>();

            for (Schema schema : getMeta0().getSchemas())
                schemas.add(schema);
        }

        return schemas;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();

        for (Schema schema : getSchemasFromMeta()) {
            for (Sequence<?> sequence : schema.getSequences()) {
                SchemaDefinition sd = getSchema(schema.getName());

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    sd,
                    sequence.getDataType().getTypeName()
                );

                result.add(new DefaultSequenceDefinition(
                    sd, sequence.getName(), type));
            }
        }

        Collections.sort(result, COMP);
        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Schema schema : getSchemasFromMeta()) {
            SchemaDefinition sd = getSchema(schema.getName());

            if (sd != null)
                for (Table<?> table : schema.getTables())
                    result.add(new DefaultMetaTableDefinition(sd, table));
        }

        Collections.sort(result, COMP);
        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        List<DomainDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<>();
        return result;
    }

    private static final Comparator<Definition> COMP = new Comparator<Definition>() {
        @Override
        public int compare(Definition o1, Definition o2) {
            return o1.getQualifiedInputName().compareTo(o2.getQualifiedInputName());
        }
    };
}
