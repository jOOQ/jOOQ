/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
import org.jooq.util.oracle.OracleDatabase.TypeInfo;

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
                ALL_TYPE_ATTRS.ATTR_TYPE_OWNER,
                ALL_TYPE_ATTRS.ATTR_TYPE_NAME,
                ALL_TYPE_ATTRS.LENGTH,
                ALL_TYPE_ATTRS.PRECISION,
                ALL_TYPE_ATTRS.SCALE)
            .from(ALL_TYPE_ATTRS)
            .where(ALL_TYPE_ATTRS.OWNER.equal(getSchema().getName()))
            .and(ALL_TYPE_ATTRS.TYPE_NAME.equal(getName()))
            .orderBy(ALL_TYPE_ATTRS.ATTR_NO).fetch()) {

            // [#3711] Check if the reported type is really a synonym for another type
            TypeInfo info = ((OracleDatabase) getDatabase()).getTypeInfo(
                getSchema(),
                record.getValue(ALL_TYPE_ATTRS.ATTR_TYPE_OWNER),
                record.getValue(ALL_TYPE_ATTRS.ATTR_TYPE_NAME));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                info.schema,
                record.getValue(ALL_TYPE_ATTRS.ATTR_TYPE_NAME),
                record.getValue(ALL_TYPE_ATTRS.LENGTH, int.class),
                record.getValue(ALL_TYPE_ATTRS.PRECISION, int.class),
                record.getValue(ALL_TYPE_ATTRS.SCALE, int.class),
                null,
                null,
                info.name
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
