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
package org.jooq.meta.postgres;

import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.when;
import static org.jooq.meta.postgres.information_schema.Tables.ATTRIBUTES;
import static org.jooq.meta.postgres.information_schema.Tables.COLUMNS;
import static org.jooq.meta.postgres.information_schema.Tables.DOMAINS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_ATTRIBUTE;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_CLASS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_NAMESPACE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jooq.Record;
import org.jooq.meta.AbstractUDTDefinition;
import org.jooq.meta.AttributeDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.Database;
import org.jooq.meta.DefaultAttributeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.postgres.information_schema.tables.Attributes;
import org.jooq.meta.postgres.information_schema.tables.Domains;
import org.jooq.meta.postgres.pg_catalog.Tables;
import org.jooq.meta.postgres.pg_catalog.tables.PgAttribute;
import org.jooq.meta.postgres.pg_catalog.tables.PgClass;
import org.jooq.meta.postgres.pg_catalog.tables.PgNamespace;

public class PostgresUDTDefinition extends AbstractUDTDefinition {

    public PostgresUDTDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, null, name, comment);
    }

    @Override
    protected List<AttributeDefinition> getElements0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<>();
        PostgresDatabase db = (PostgresDatabase) getDatabase();

        Attributes a = ATTRIBUTES.as("a");
        Domains d = DOMAINS.as("d");
        PgAttribute pg_a = PG_ATTRIBUTE.as("pg_a");

        for (Record record : create().select(
                    a.ATTRIBUTE_NAME,
                    a.ORDINAL_POSITION,
                    coalesce(
                        d.DATA_TYPE,
                        when(a.DATA_TYPE.eq(inline("USER-DEFINED")).and(a.ATTRIBUTE_UDT_NAME.eq(inline("geometry"))), inline("geometry"))
                        .else_(db.arrayDataType(a.DATA_TYPE, a.ATTRIBUTE_UDT_NAME, pg_a.ATTNDIMS))
                    ).as(a.DATA_TYPE),
                    coalesce(d.CHARACTER_MAXIMUM_LENGTH, a.CHARACTER_MAXIMUM_LENGTH).as(a.CHARACTER_MAXIMUM_LENGTH),
                    coalesce(d.NUMERIC_PRECISION, a.NUMERIC_PRECISION).as(a.NUMERIC_PRECISION),
                    coalesce(d.NUMERIC_SCALE, a.NUMERIC_SCALE).as(a.NUMERIC_SCALE),
                    a.IS_NULLABLE,
                    a.ATTRIBUTE_DEFAULT,
                    a.ATTRIBUTE_UDT_SCHEMA,
                    db.arrayUdtName(a.DATA_TYPE, a.ATTRIBUTE_UDT_NAME).as(a.ATTRIBUTE_UDT_NAME))
                .from(a)
                .join(pg_a)
                    .on(a.ATTRIBUTE_NAME.eq(pg_a.ATTNAME))
                    .and(a.UDT_NAME.eq(pg_a.pgClass().RELNAME))
                    .and(a.UDT_SCHEMA.eq(pg_a.pgClass().pgNamespace().NSPNAME))
                .leftJoin(d)
                    .on(a.ATTRIBUTE_UDT_CATALOG.eq(d.DOMAIN_CATALOG))
                    .and(a.ATTRIBUTE_UDT_SCHEMA.eq(d.DOMAIN_SCHEMA))
                    .and(a.ATTRIBUTE_UDT_NAME.eq(d.DOMAIN_NAME))
                .where(a.UDT_SCHEMA.equal(getSchema().getName()))
                .and(a.UDT_NAME.equal(getName()))
                .orderBy(a.ORDINAL_POSITION)) {

            SchemaDefinition typeSchema = null;

            String schemaName = record.get(a.ATTRIBUTE_UDT_SCHEMA);
            if (schemaName != null)
                typeSchema = getDatabase().getSchema(schemaName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                typeSchema == null ? getSchema() : typeSchema,
                record.get(a.DATA_TYPE),
                record.get(a.CHARACTER_MAXIMUM_LENGTH),
                record.get(a.NUMERIC_PRECISION),
                record.get(a.NUMERIC_SCALE),
                record.get(a.IS_NULLABLE, boolean.class),
                record.get(a.ATTRIBUTE_DEFAULT),
                name(
                    record.get(a.ATTRIBUTE_UDT_SCHEMA),
                    record.get(a.ATTRIBUTE_UDT_NAME)
                )
            );

            AttributeDefinition column = new DefaultAttributeDefinition(
                this,
                record.get(a.ATTRIBUTE_NAME),
                record.get(a.ORDINAL_POSITION),
                type);

            result.add(column);
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() {
        return Collections.emptyList();
    }
}
