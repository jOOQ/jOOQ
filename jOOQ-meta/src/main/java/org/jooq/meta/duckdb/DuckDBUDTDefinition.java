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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
package org.jooq.meta.duckdb;

import static org.jooq.impl.DSL.currentSchema;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.meta.duckdb.system.main.Tables.DUCKDB_COLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.QOM;
import org.jooq.meta.AbstractUDTDefinition;
import org.jooq.meta.AttributeDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultAttributeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;

public class DuckDBUDTDefinition extends AbstractUDTDefinition {

    public DuckDBUDTDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, null, name, comment);
    }

    @Override
    protected List<AttributeDefinition> getElements0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<>();

        // A current limitation of DuckDB 0.8.1 requires a workaround where we create
        // a dummy table containing a reference to the UDT in order to reverse engineer
        // its structure, see https://github.com/duckdb/duckdb/discussions/8832
        // [#17251] Cross schema references still don't work: https://github.com/duckdb/duckdb/issues/13981
        String currentSchema = create().fetchValue(currentSchema());
        Name name = unquotedName("dummy_" + Math.abs(new Random().nextInt()));

        try {
            create().setSchema(getSchema().getName()).execute();
            create().createTable(name)
                    .column("dummy", new DefaultDataType<>(null, Object.class, getName()))
                    .execute();

            String struct =
            create()
                .select(DUCKDB_COLUMNS.DATA_TYPE)
                .from(DUCKDB_COLUMNS)
                .where(DUCKDB_COLUMNS.TABLE_NAME.eq(name.last()))
                .fetchSingle()
                .value1();

            QOM.CreateType ct = (QOM.CreateType)
            create().parser()
                    .parseQuery("create type t as " + struct);

            int i = 1;
            for (Field<?> a : ct.$attributes()) {
                SchemaDefinition typeSchema = null;

                Name qualifiedName = a.getDataType().getQualifiedName();
                if (qualifiedName.qualified())
                    typeSchema = getDatabase().getSchema(qualifiedName.qualifier().last());

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    typeSchema == null ? getSchema() : typeSchema,
                    qualifiedName.last(),
                    a.getDataType().length(),
                    a.getDataType().precision(),
                    a.getDataType().scale(),
                    a.getDataType().nullable(),
                    a.getDataType().defaulted() ? a.getDataType().default_().toString() : null,
                    name(
                        qualifiedName.qualified() ? qualifiedName.qualifier().last() : null,
                        qualifiedName.last()
                    )
                );

                result.add(new DefaultAttributeDefinition(
                    this,
                    a.getName(),
                    i++,
                    type
                ));
            }
        }
        finally {
            create().setSchema(currentSchema).execute();
            create().dropTableIfExists(name).execute();
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() {
        return Collections.emptyList();
    }
}
