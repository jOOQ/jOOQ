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

import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.util.postgres.PostgresDSL.oid;
import static org.jooq.util.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.util.postgres.information_schema.Tables.ROUTINES;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_NAMESPACE;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_PROC;

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
import org.jooq.util.postgres.information_schema.tables.Parameters;
import org.jooq.util.postgres.information_schema.tables.Routines;
import org.jooq.util.postgres.pg_catalog.tables.PgNamespace;
import org.jooq.util.postgres.pg_catalog.tables.PgProc;

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

        Routines r = ROUTINES;
        Parameters p = PARAMETERS;
        PgNamespace pg_n = PG_NAMESPACE;
        PgProc pg_p = PG_PROC;

        for (Record record : create().select(
                r.ROUTINE_NAME,
                p.PARAMETER_NAME,
                rowNumber().over(partitionBy(p.SPECIFIC_NAME).orderBy(p.ORDINAL_POSITION)).as(p.ORDINAL_POSITION.getName()),
                p.DATA_TYPE,
                p.CHARACTER_MAXIMUM_LENGTH,
                p.NUMERIC_PRECISION,
                p.NUMERIC_SCALE
            )
            .from(r)
            .join(p).on(row(r.SPECIFIC_CATALOG, r.SPECIFIC_SCHEMA, r.SPECIFIC_NAME)
                        .eq(p.SPECIFIC_CATALOG, p.SPECIFIC_SCHEMA, p.SPECIFIC_NAME))
            .join(pg_n).on(r.SPECIFIC_SCHEMA.eq(pg_n.NSPNAME))
            .join(pg_p).on(pg_p.PRONAMESPACE.eq(oid(pg_n)))
                       .and(pg_p.PRONAME.eq(r.ROUTINE_NAME))
            .where(r.SPECIFIC_NAME.eq(specificName))
            .and(p.PARAMETER_MODE.ne("IN"))
            .and(pg_p.PRORETSET)
            .fetch()
        ) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(p.DATA_TYPE),
                record.getValue(p.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(p.NUMERIC_PRECISION),
                record.getValue(p.NUMERIC_SCALE),
                true,
                false,
                null
            );

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.getValue(p.PARAMETER_NAME),
			    record.getValue(p.ORDINAL_POSITION),
			    type,
			    false,
			    null
		    );

			result.add(column);
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
