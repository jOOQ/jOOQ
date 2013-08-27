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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractPackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;

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
                    ALL_ARGUMENTS.OBJECT_NAME,
                    ALL_ARGUMENTS.OBJECT_ID,
                    ALL_ARGUMENTS.OVERLOAD)
                .from(ALL_ARGUMENTS)
                .where(ALL_ARGUMENTS.OWNER.in(getSchema().getName()))
                .and(ALL_ARGUMENTS.PACKAGE_NAME.equal(getName()))
                .orderBy(ALL_ARGUMENTS.OBJECT_NAME, ALL_ARGUMENTS.OVERLOAD)
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
