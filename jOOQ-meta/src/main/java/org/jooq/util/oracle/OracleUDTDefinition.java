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
package org.jooq.util.oracle;

import static org.jooq.util.oracle.sys.Tables.ALL_ARGUMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_TYPE_ATTRS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractUDTDefinition;
import org.jooq.util.AttributeDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultAttributeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;

public class OracleUDTDefinition extends AbstractUDTDefinition {

    public OracleUDTDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    protected List<AttributeDefinition> getElements0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<AttributeDefinition>();

        for (Record record : create().select(
                ALL_TYPE_ATTRS.ATTR_NAME,
                ALL_TYPE_ATTRS.ATTR_NO,
                ALL_TYPE_ATTRS.ATTR_TYPE_NAME,
                ALL_TYPE_ATTRS.LENGTH,
                ALL_TYPE_ATTRS.PRECISION,
                ALL_TYPE_ATTRS.SCALE)
            .from(ALL_TYPE_ATTRS)
            .where(ALL_TYPE_ATTRS.OWNER.equal(getSchema().getName()))
            .and(ALL_TYPE_ATTRS.TYPE_NAME.equal(getName()))
            .orderBy(ALL_TYPE_ATTRS.ATTR_NO).fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(ALL_TYPE_ATTRS.ATTR_TYPE_NAME),
                record.getValue(ALL_TYPE_ATTRS.LENGTH, int.class),
                record.getValue(ALL_TYPE_ATTRS.PRECISION, int.class),
                record.getValue(ALL_TYPE_ATTRS.SCALE, int.class),
                null,
                null
            );

            AttributeDefinition attribute = new DefaultAttributeDefinition(
                this,
                record.getValue(ALL_TYPE_ATTRS.ATTR_NAME),
                record.getValue(ALL_TYPE_ATTRS.ATTR_NO, int.class),
                type
            );

            result.add(attribute);
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
                .selectDistinct(
                    ALL_ARGUMENTS.OBJECT_NAME,
                    ALL_ARGUMENTS.OBJECT_ID,
                    ALL_ARGUMENTS.OVERLOAD)
                .from(ALL_ARGUMENTS)
                .where(ALL_ARGUMENTS.OWNER.equal(getSchema().getName()))
                .and(ALL_ARGUMENTS.PACKAGE_NAME.equal(getName()))
                .orderBy(
                    ALL_ARGUMENTS.OBJECT_NAME,
                    ALL_ARGUMENTS.OVERLOAD)
                .fetch()) {

            result.add(new OracleRoutineDefinition(getSchema(),
                this,
                record.getValue(ALL_ARGUMENTS.OBJECT_NAME),
                "",
                record.getValue(ALL_ARGUMENTS.OBJECT_ID),
                record.getValue(ALL_ARGUMENTS.OVERLOAD)));
        }

        return result;
    }
}
