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

import static org.jooq.impl.DSL.inline;
import static org.jooq.util.oracle.sys.Tables.ALL_ARGUMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_COL_COMMENTS;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

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
	            ALL_ARGUMENTS.DATA_LENGTH,
	            ALL_ARGUMENTS.DATA_PRECISION,
	            ALL_ARGUMENTS.DATA_SCALE,
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

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(ALL_ARGUMENTS.DATA_TYPE),
                record.getValue(ALL_ARGUMENTS.DATA_LENGTH),
                record.getValue(ALL_ARGUMENTS.DATA_PRECISION),
                record.getValue(ALL_ARGUMENTS.DATA_SCALE),
                true,
                record.getValue(defaulted, boolean.class),
                record.getValue(ALL_ARGUMENTS.TYPE_NAME)
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
            is11g = create().selectCount()
                            .from(ALL_COL_COMMENTS)
                            .where(ALL_COL_COMMENTS.OWNER.equal(ALL_ARGUMENTS.getSchema().getName()))
                            .and(ALL_COL_COMMENTS.TABLE_NAME.equal(ALL_ARGUMENTS.getName()))
                            .and(ALL_COL_COMMENTS.COLUMN_NAME.equal(ALL_ARGUMENTS.DEFAULTED.getName()))
                            .fetchOne(0, boolean.class);
        }

        return is11g;
    }
}
