/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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

package org.jooq.util.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class PostgresTableValuedFunction extends AbstractTableDefinition {

    private final PostgresRoutineDefinition routine;
    private final String                    specificName;

    public PostgresTableValuedFunction(SchemaDefinition schema, String name, String specificName, String comment) {
		super(schema, name, comment);

		this.routine = new PostgresRoutineDefinition(schema.getDatabase(), schema.getInputName(), name, specificName);
		this.specificName = specificName;
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();
		Long oid = null;

		try {
		    oid = Long.valueOf(specificName.substring(specificName.lastIndexOf("_") + 1));
		}
		catch (Exception ignore) {}

		if (oid != null) {
            for (Record record : create().fetch(

                // [#3375] This query mimicks what SQL Server knows as INFORMATION_SCHEMA.COLUMNS for table-valued
                // functions, which are really tables with result COLUMNS.

                  " SELECT columns.proargname AS column_name,"
                + "        ROW_NUMBER() OVER(PARTITION BY p.oid ORDER BY o.ordinal) AS ordinal_position,"
                + "        format_type(t.oid, t.typtypmod) AS data_type,"
                + "        information_schema._pg_char_max_length(t.oid, t.typtypmod) AS character_maximum_length,"
                + "        information_schema._pg_numeric_precision(t.oid, t.typtypmod) AS numeric_precision,"
                + "        information_schema._pg_numeric_scale(t.oid, t.typtypmod) AS numeric_scale,"
                + "        not(t.typnotnull) AS is_nullable"
                + " FROM pg_proc p,"
                + " LATERAL generate_series(1, array_length(p.proargmodes, 1)) o(ordinal),"
                + " LATERAL (SELECT p.proargnames[o.ordinal], p.proargmodes[o.ordinal], p.proallargtypes[o.ordinal]) columns(proargname, proargmode, proargtype),"
                + " LATERAL ("
                + "   SELECT pg_type.oid oid, pg_type.* FROM pg_type WHERE pg_type.oid = columns.proargtype"
                + " ) t"
                + " WHERE p.proretset"
                + " AND columns.proargmode = 't'"
                + " AND p.oid = ?", oid)
            ) {

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    getSchema(),
                    record.getValue("data_type", String.class),
                    record.getValue("character_maximum_length", Integer.class),
                    record.getValue("numeric_precision", Integer.class),
                    record.getValue("numeric_scale", Integer.class),
                    record.getValue("is_nullable", boolean.class),
                    false,
                    null
                );

    			ColumnDefinition column = new DefaultColumnDefinition(
    			    getDatabase().getTable(getSchema(), getName()),
    			    record.getValue("column_name", String.class),
    			    record.getValue("ordinal_position", int.class),
    			    type,
    			    false,
    			    null
    		    );

    			result.add(column);
    		}
		}

		return result;
	}

    @Override
    protected List<ParameterDefinition> getParameters0() {
        return routine.getInParameters();
    }

    @Override
    public boolean isTableValuedFunction() {
        return true;
    }
}
