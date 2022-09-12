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

        PgNamespace n = PG_NAMESPACE.as("n");
        PgClass c = PG_CLASS.as("c");
        PgAttribute a = PG_ATTRIBUTE.as("a");

        for (Record record : create().select(
                    ATTRIBUTES.ATTRIBUTE_NAME,
                    ATTRIBUTES.ORDINAL_POSITION,
                    coalesce(
                        DOMAINS.DATA_TYPE,
                        when(ATTRIBUTES.DATA_TYPE.eq(inline("USER-DEFINED")).and(ATTRIBUTES.ATTRIBUTE_UDT_NAME.eq(inline("geometry"))), inline("geometry"))
                        .else_(db.arrayDataType(ATTRIBUTES.DATA_TYPE, ATTRIBUTES.ATTRIBUTE_UDT_NAME, a.ATTNDIMS))
                    ).as(ATTRIBUTES.DATA_TYPE),
                    coalesce(DOMAINS.CHARACTER_MAXIMUM_LENGTH, ATTRIBUTES.CHARACTER_MAXIMUM_LENGTH).as(ATTRIBUTES.CHARACTER_MAXIMUM_LENGTH),
                    coalesce(DOMAINS.NUMERIC_PRECISION, ATTRIBUTES.NUMERIC_PRECISION).as(ATTRIBUTES.NUMERIC_PRECISION),
                    coalesce(DOMAINS.NUMERIC_SCALE, ATTRIBUTES.NUMERIC_SCALE).as(ATTRIBUTES.NUMERIC_SCALE),
                    ATTRIBUTES.IS_NULLABLE,
                    ATTRIBUTES.ATTRIBUTE_DEFAULT,
                    ATTRIBUTES.ATTRIBUTE_UDT_SCHEMA,
                    db.arrayUdtName(ATTRIBUTES.DATA_TYPE, ATTRIBUTES.ATTRIBUTE_UDT_NAME).as(ATTRIBUTES.ATTRIBUTE_UDT_NAME))
                .from(ATTRIBUTES)
                .join(n)
                    .on(ATTRIBUTES.UDT_SCHEMA.eq(n.NSPNAME))
                .join(c)
                    .on(ATTRIBUTES.UDT_NAME.eq(c.RELNAME))
                    .and(n.OID.eq(c.RELNAMESPACE))
                .join(a)
                    .on(ATTRIBUTES.ATTRIBUTE_NAME.eq(a.ATTNAME))
                    .and(c.OID.eq(a.ATTRELID))
                .leftJoin(DOMAINS)
                    .on(ATTRIBUTES.ATTRIBUTE_UDT_CATALOG.eq(DOMAINS.DOMAIN_CATALOG))
                    .and(ATTRIBUTES.ATTRIBUTE_UDT_SCHEMA.eq(DOMAINS.DOMAIN_SCHEMA))
                    .and(ATTRIBUTES.ATTRIBUTE_UDT_NAME.eq(DOMAINS.DOMAIN_NAME))
                .where(ATTRIBUTES.UDT_SCHEMA.equal(getSchema().getName()))
                .and(ATTRIBUTES.UDT_NAME.equal(getName()))
                .orderBy(ATTRIBUTES.ORDINAL_POSITION)) {

            SchemaDefinition typeSchema = null;

            String schemaName = record.get(ATTRIBUTES.ATTRIBUTE_UDT_SCHEMA);
            if (schemaName != null)
                typeSchema = getDatabase().getSchema(schemaName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                typeSchema == null ? getSchema() : typeSchema,
                record.get(ATTRIBUTES.DATA_TYPE),
                record.get(ATTRIBUTES.CHARACTER_MAXIMUM_LENGTH),
                record.get(ATTRIBUTES.NUMERIC_PRECISION),
                record.get(ATTRIBUTES.NUMERIC_SCALE),
                record.get(ATTRIBUTES.IS_NULLABLE, boolean.class),
                record.get(ATTRIBUTES.ATTRIBUTE_DEFAULT),
                name(
                    record.get(ATTRIBUTES.ATTRIBUTE_UDT_SCHEMA),
                    record.get(ATTRIBUTES.ATTRIBUTE_UDT_NAME)
                )
            );

            AttributeDefinition column = new DefaultAttributeDefinition(
                this,
                record.get(ATTRIBUTES.ATTRIBUTE_NAME),
                record.get(ATTRIBUTES.ORDINAL_POSITION),
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
