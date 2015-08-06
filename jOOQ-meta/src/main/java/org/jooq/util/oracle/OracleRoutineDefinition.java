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

import java.math.BigDecimal;
import java.sql.SQLException;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.oracle.OracleDatabase.TypeInfo;

/**
 * @author Lukas Eder
 */
public class OracleRoutineDefinition extends AbstractRoutineDefinition {

    private static Boolean   is11g;
    private final BigDecimal objectId;

    public OracleRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, String comment, BigDecimal objectId, String overload) {
        this(schema, pkg, name, comment, objectId, overload, false);
    }

	public OracleRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, String comment, BigDecimal objectId, String overload, boolean aggregate) {
		super(schema, pkg, name, comment, overload, aggregate);

		this.objectId = objectId;
	}

	@Override
    protected void init0() throws SQLException {

	    // [#1324] The ALL_ARGUMENTS.DEFAULTED column is available in Oracle 11g
	    // only. This feature should thus be deactivated for older versions
	    Field<String> defaulted = is11g()
	        ? ALL_ARGUMENTS.DEFAULTED
            : inline("N");

        Result<?> result = create().select(
	            ALL_ARGUMENTS.IN_OUT,
	            ALL_ARGUMENTS.ARGUMENT_NAME,
	            ALL_ARGUMENTS.DATA_TYPE,
	            ALL_ARGUMENTS.CHAR_LENGTH,
	            ALL_ARGUMENTS.DATA_PRECISION,
	            ALL_ARGUMENTS.DATA_SCALE,
	            ALL_ARGUMENTS.TYPE_OWNER,
	            ALL_ARGUMENTS.TYPE_NAME,
	            ALL_ARGUMENTS.POSITION,
	            defaulted)
	        .from(ALL_ARGUMENTS)
            .where(ALL_ARGUMENTS.OWNER.equal(getSchema().getName()))
            .and(ALL_ARGUMENTS.OBJECT_NAME.equal(getName()))
            .and(ALL_ARGUMENTS.OBJECT_ID.equal(objectId))
            .and(ALL_ARGUMENTS.OVERLOAD.isNotDistinctFrom(getOverload()))
            .and(ALL_ARGUMENTS.DATA_LEVEL.equal(BigDecimal.ZERO))

            // [#284] In packages, procedures without arguments may have a
            // single data type entry that does not mean anything...?
            .and(ALL_ARGUMENTS.DATA_TYPE.isNotNull())
            .orderBy(ALL_ARGUMENTS.POSITION.asc()).fetch();

	    for (Record record : result) {
	        InOutDefinition inOut =
                InOutDefinition.getFromString(record.getValue(ALL_ARGUMENTS.IN_OUT));

	        // [#3552] Check if the reported type is really a synonym for another type
	        TypeInfo info = ((OracleDatabase) getDatabase()).getTypeInfo(
                getSchema(),
                record.getValue(ALL_ARGUMENTS.TYPE_OWNER),
                record.getValue(ALL_ARGUMENTS.TYPE_NAME));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                info.schema,
                record.getValue(ALL_ARGUMENTS.DATA_TYPE),
                record.getValue(ALL_ARGUMENTS.CHAR_LENGTH),
                record.getValue(ALL_ARGUMENTS.DATA_PRECISION),
                record.getValue(ALL_ARGUMENTS.DATA_SCALE),
                true,
                record.getValue(defaulted, boolean.class),
                info.name
            );

            String name = record.getValue(ALL_ARGUMENTS.ARGUMENT_NAME);
            int position = record.getValue(ALL_ARGUMENTS.POSITION, int.class);

            // [#378] Oracle supports stored functions with OUT parameters.
            // They are mapped to procedures in jOOQ
            if (StringUtils.isBlank(name) && position == 0) {
                inOut = InOutDefinition.RETURN;
                name = "RETURN_VALUE";
            }

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                name,
                position,
                type,
                record.getValue(defaulted, boolean.class));

            addParameter(inOut, parameter);
	    }
	}

    private boolean is11g() {
        if (is11g == null) {

            // [#2866] The ALL_ARGUMENTS.DEFAULTED column was introduced in Oracle 11g
            try {
                create().selectCount()
                        .from(ALL_ARGUMENTS)
                        .where(ALL_ARGUMENTS.DEFAULTED.eq("defaulted"))
                        .fetch();

                is11g = true;
            }
            catch (DataAccessException e) {
                is11g = false;
            }
        }

        return is11g;
    }
}
