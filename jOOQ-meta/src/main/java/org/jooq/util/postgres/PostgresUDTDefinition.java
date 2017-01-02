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
package org.jooq.util.postgres;

import static org.jooq.impl.DSL.name;
import static org.jooq.util.postgres.information_schema.Tables.ATTRIBUTES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractUDTDefinition;
import org.jooq.util.AttributeDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultAttributeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;

public class PostgresUDTDefinition extends AbstractUDTDefinition {

    public PostgresUDTDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, null, name, comment);
    }

    @Override
    protected List<AttributeDefinition> getElements0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<AttributeDefinition>();

        for (Record record : create().select(
                    ATTRIBUTES.ATTRIBUTE_NAME,
                    ATTRIBUTES.ORDINAL_POSITION,
                    ATTRIBUTES.DATA_TYPE,
                    ATTRIBUTES.CHARACTER_MAXIMUM_LENGTH,
                    ATTRIBUTES.NUMERIC_PRECISION,
                    ATTRIBUTES.NUMERIC_SCALE,
                    ATTRIBUTES.IS_NULLABLE,
                    ATTRIBUTES.ATTRIBUTE_DEFAULT,
                    ATTRIBUTES.ATTRIBUTE_UDT_SCHEMA,
                    ATTRIBUTES.ATTRIBUTE_UDT_NAME)
                .from(ATTRIBUTES)
                .where(ATTRIBUTES.UDT_SCHEMA.equal(getSchema().getName()))
                .and(ATTRIBUTES.UDT_NAME.equal(getName()))
                .orderBy(ATTRIBUTES.ORDINAL_POSITION)
                .fetch()) {

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
