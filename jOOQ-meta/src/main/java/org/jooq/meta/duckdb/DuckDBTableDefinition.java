/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package org.jooq.meta.duckdb;

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.duckdb.system.main.Tables.DUCKDB_COLUMNS;
import static org.jooq.meta.duckdb.system.main.Tables.DUCKDB_TYPES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class DuckDBTableDefinition extends AbstractTableDefinition {

    public DuckDBTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    public DuckDBTableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType, String source) {
        super(schema, name, comment, tableType, source);
    }

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<>();

        for (Record record : create()
            .select(
                DUCKDB_COLUMNS.COLUMN_NAME,
                DUCKDB_COLUMNS.COLUMN_INDEX,
                DUCKDB_COLUMNS.DATA_TYPE,
                DUCKDB_COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                DUCKDB_COLUMNS.NUMERIC_PRECISION,
                DUCKDB_COLUMNS.NUMERIC_SCALE,
                DUCKDB_COLUMNS.IS_NULLABLE,
                DUCKDB_COLUMNS.COLUMN_DEFAULT,
                DUCKDB_COLUMNS.COLUMN_DEFAULT.like(inline("nextval('%')")).as("is_identity"),
                when(
                    DUCKDB_TYPES.LOGICAL_TYPE.eq(inline("STRUCT")),
                    DUCKDB_TYPES.SCHEMA_NAME).as(DUCKDB_TYPES.SCHEMA_NAME),
                when(
                    DUCKDB_TYPES.LOGICAL_TYPE.eq(inline("STRUCT")),
                    DUCKDB_TYPES.TYPE_NAME).as(DUCKDB_TYPES.TYPE_NAME),
                ((DuckDBDatabase) getDatabase()).is0100()
                    ? DUCKDB_COLUMNS.COMMENT
                    : inline(null, VARCHAR).as(DUCKDB_COLUMNS.COMMENT)
            )
            .from("{0}()", DUCKDB_COLUMNS)
                .leftJoin(DUCKDB_TYPES)
                .on(DUCKDB_COLUMNS.DATA_TYPE_ID.eq(DUCKDB_TYPES.TYPE_OID))
            .where(DUCKDB_COLUMNS.DATABASE_NAME.eq(getCatalog().getInputName()))
            .and(DUCKDB_COLUMNS.SCHEMA_NAME.eq(getSchema().getInputName()))
            .and(DUCKDB_COLUMNS.TABLE_NAME.eq(getInputName()))
            .orderBy(DUCKDB_COLUMNS.COLUMN_INDEX)
        ) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get(DUCKDB_COLUMNS.DATA_TYPE),
                record.get(DUCKDB_COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.get(DUCKDB_COLUMNS.NUMERIC_PRECISION),
                record.get(DUCKDB_COLUMNS.NUMERIC_SCALE),
                record.get(DUCKDB_COLUMNS.IS_NULLABLE),
                record.get(DUCKDB_COLUMNS.COLUMN_DEFAULT),
                name(
                    record.get(DUCKDB_TYPES.SCHEMA_NAME),
                    record.get(DUCKDB_TYPES.TYPE_NAME)
                )
            );

            result.add(new DefaultColumnDefinition(
                this,
                record.get(DUCKDB_COLUMNS.COLUMN_NAME),
                record.get(DUCKDB_COLUMNS.COLUMN_INDEX),
                type,
                record.get("is_identity", boolean.class),
                record.get(DUCKDB_COLUMNS.COMMENT)
            ));
		}

		return result;
	}
}
