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

import static org.jooq.impl.DSL.inline;
import static org.jooq.util.oracle.sys.Tables.ALL_ARGUMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_IDENTIFIERS;
import static org.jooq.util.oracle.sys.Tables.ALL_OBJECTS;
import static org.jooq.util.oracle.sys.Tables.ALL_PROCEDURES;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.util.AbstractPackageDefinition;
import org.jooq.util.AttributeDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultAttributeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.oracle.sys.tables.AllIdentifiers;

/**
 * @author Lukas Eder
 */
public class OraclePackageDefinition extends AbstractPackageDefinition {

    public OraclePackageDefinition(SchemaDefinition schema, String packageName, String comment) {
        super(schema, packageName, comment);
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
                .selectDistinct(
                    ALL_PROCEDURES.PROCEDURE_NAME,

                 // [#4224] ALL_PROCEDURES.OBJECT_ID didn't exist in 10g
                 // ALL_PROCEDURES.OBJECT_ID,
                    ALL_OBJECTS.OBJECT_ID,

                 // [#4224] ... neither did ALL_PROCEDURES.OVERLOAD
                 // ALL_PROCEDURES.OVERLOAD
                    ALL_ARGUMENTS.OVERLOAD
                )
                .from(ALL_OBJECTS)
                .join(ALL_PROCEDURES)
                    .on(ALL_OBJECTS.OWNER.eq(ALL_PROCEDURES.OWNER))
                    .and(ALL_OBJECTS.OBJECT_NAME.eq(ALL_PROCEDURES.OBJECT_NAME))
                .leftOuterJoin(ALL_ARGUMENTS)
                    .on(ALL_ARGUMENTS.OWNER.eq(ALL_PROCEDURES.OWNER))
                    .and(ALL_ARGUMENTS.PACKAGE_NAME.eq(ALL_PROCEDURES.OBJECT_NAME))
                    .and(ALL_ARGUMENTS.OBJECT_NAME.eq(ALL_PROCEDURES.PROCEDURE_NAME))
                .where(ALL_OBJECTS.OWNER.in(getSchema().getName()))

                // Exclude "PACKAGE BODY"
                .and(ALL_OBJECTS.OBJECT_TYPE.eq("PACKAGE"))

                // There is this weird entry in ALL_PROCEDURES where
                // PROCEDURE_NAME IS NULL AND SUBPROGRAM_ID = 0
                .and(ALL_PROCEDURES.PROCEDURE_NAME.isNotNull())
                .and(ALL_PROCEDURES.OBJECT_NAME.equal(getName()))
                .orderBy(
                    ALL_PROCEDURES.PROCEDURE_NAME,
                    ALL_ARGUMENTS.OVERLOAD)
                .fetch()) {

            result.add(new OracleRoutineDefinition(getSchema(),
                this,
                record.getValue(ALL_PROCEDURES.PROCEDURE_NAME),
                "",
                record.getValue(ALL_OBJECTS.OBJECT_ID),
                record.getValue(ALL_ARGUMENTS.OVERLOAD)));
        }

        return result;
    }

    @Override
    protected List<AttributeDefinition> getConstants0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<AttributeDefinition>();

        if (((OracleDatabase) getDatabase()).is11g()) {
            AllIdentifiers pName = ALL_IDENTIFIERS.as("pName");
            AllIdentifiers cName = ALL_IDENTIFIERS.as("cName");
            AllIdentifiers cType = ALL_IDENTIFIERS.as("cType");

            for (Record3<String, String, String> record : create()
                .select(pName.NAME, cName.NAME, cType.NAME)
                .from(pName)
                .join(cName)
                    .on(pName.USAGE_ID.eq(cName.USAGE_CONTEXT_ID))
                    .and(pName.OWNER.eq(cName.OWNER))
                    .and(pName.OBJECT_NAME.eq(cName.OBJECT_NAME))
                    .and(pName.OBJECT_TYPE.eq(cName.OBJECT_TYPE))
                    .and(cName.TYPE.eq("CONSTANT"))
                .join(cType)
                    .on(cName.USAGE_ID.eq(cType.USAGE_CONTEXT_ID))
                    .and(cName.OWNER.eq(cType.OWNER))
                    .and(cName.OBJECT_NAME.eq(cType.OBJECT_NAME))
                    .and(cName.OBJECT_TYPE.eq(cType.OBJECT_TYPE))
                    .and(cType.USAGE.eq("REFERENCE"))
                .where(pName.USAGE_CONTEXT_ID.eq(inline(BigDecimal.ZERO)))
                .and(pName.OWNER.eq(getSchema().getName()))
                .and(pName.OBJECT_NAME.eq(getName()))
                .and(pName.OBJECT_TYPE.eq("PACKAGE"))
            ) {

                // TODO: Support synonyms here, if this is possible at all...
                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    getSchema(),
                    record.value3(),
                    0,
                    0,
                    0,
                    true,
                    false,
                    record.value3()
                );

                // TODO: Will we need to provide more meta information here?
                AttributeDefinition attribute = new DefaultAttributeDefinition(
                    new OracleUDTDefinition(getSchema(), getName(), ""),
                    record.value2(),
                    1,
                    type
                );

                result.add(attribute);
            }
        }

        return result;
    }
}
