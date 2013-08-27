/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.util.postgres;

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
        super(schema, name, comment);
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
                    ATTRIBUTES.ATTRIBUTE_UDT_NAME)
                .from(ATTRIBUTES)
                .where(ATTRIBUTES.UDT_SCHEMA.equal(getSchema().getName()))
                .and(ATTRIBUTES.UDT_NAME.equal(getName()))
                .orderBy(ATTRIBUTES.ORDINAL_POSITION)
                .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(ATTRIBUTES.DATA_TYPE),
                record.getValue(ATTRIBUTES.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(ATTRIBUTES.NUMERIC_PRECISION),
                record.getValue(ATTRIBUTES.NUMERIC_SCALE),
                record.getValue(ATTRIBUTES.IS_NULLABLE, boolean.class),
                record.getValue(ATTRIBUTES.ATTRIBUTE_DEFAULT) != null,
                record.getValue(ATTRIBUTES.ATTRIBUTE_UDT_NAME)
            );

            AttributeDefinition column = new DefaultAttributeDefinition(
                this,
                record.getValue(ATTRIBUTES.ATTRIBUTE_NAME),
                record.getValue(ATTRIBUTES.ORDINAL_POSITION),
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
