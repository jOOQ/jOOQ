/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
