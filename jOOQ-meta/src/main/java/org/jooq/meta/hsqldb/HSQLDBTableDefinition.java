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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

package org.jooq.meta.hsqldb;

import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.when;
import static org.jooq.meta.hsqldb.information_schema.Tables.COLUMNS;
import static org.jooq.meta.hsqldb.information_schema.Tables.ELEMENT_TYPES;
import static org.jooq.meta.hsqldb.information_schema.Tables.SYSTEM_COLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class HSQLDBTableDefinition extends AbstractTableDefinition {

    public HSQLDBTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    public HSQLDBTableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType, String source) {
        super(schema, name, comment, tableType, source);
    }

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,
                nvl(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER,

                    // [#2230] Translate INTERVAL_TYPE to supported types
                    when(COLUMNS.INTERVAL_TYPE.like(any(inline("%YEAR%"), inline("%MONTH%"))), inline("INTERVAL YEAR TO MONTH"))
                    .when(COLUMNS.INTERVAL_TYPE.like(any(inline("%DAY%"), inline("%HOUR%"), inline("%MINUTE%"), inline("%SECOND%"))), inline("INTERVAL DAY TO SECOND"))
                    .else_(COLUMNS.DATA_TYPE)
                ).as(COLUMNS.DATA_TYPE),
                COLUMNS.IDENTITY_GENERATION,
                COLUMNS.IS_NULLABLE,
                COLUMNS.COLUMN_DEFAULT,
                COLUMNS.GENERATION_EXPRESSION,
                COLUMNS.IS_SYSTEM_TIME_PERIOD_START,
                COLUMNS.IS_SYSTEM_TIME_PERIOD_END,
                nvl(ELEMENT_TYPES.CHARACTER_MAXIMUM_LENGTH, COLUMNS.CHARACTER_MAXIMUM_LENGTH).as(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                coalesce(
                    ELEMENT_TYPES.DATETIME_PRECISION,
                    ELEMENT_TYPES.NUMERIC_PRECISION,
                    COLUMNS.DATETIME_PRECISION,
                    COLUMNS.NUMERIC_PRECISION).as(COLUMNS.NUMERIC_PRECISION),
                nvl(ELEMENT_TYPES.NUMERIC_SCALE, COLUMNS.NUMERIC_SCALE).as(COLUMNS.NUMERIC_SCALE),

                coalesce(COLUMNS.DOMAIN_SCHEMA, COLUMNS.UDT_SCHEMA).as(COLUMNS.UDT_SCHEMA),
                coalesce(COLUMNS.DOMAIN_NAME, COLUMNS.UDT_NAME).as(COLUMNS.UDT_NAME),
                SYSTEM_COLUMNS.REMARKS)
            .from(COLUMNS)
                .leftOuterJoin(SYSTEM_COLUMNS)
                    .on(COLUMNS.TABLE_CATALOG.eq(SYSTEM_COLUMNS.TABLE_CAT))
                    .and(COLUMNS.TABLE_SCHEMA.eq(SYSTEM_COLUMNS.TABLE_SCHEM))
                    .and(COLUMNS.TABLE_NAME.eq(SYSTEM_COLUMNS.TABLE_NAME))
                    .and(COLUMNS.COLUMN_NAME.eq(SYSTEM_COLUMNS.COLUMN_NAME))
                .leftOuterJoin(ELEMENT_TYPES)
                    .on(COLUMNS.TABLE_SCHEMA.equal(ELEMENT_TYPES.OBJECT_SCHEMA))
                    .and(COLUMNS.TABLE_NAME.equal(ELEMENT_TYPES.OBJECT_NAME))
                    .and(COLUMNS.DTD_IDENTIFIER.equal(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER))
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
                .and(COLUMNS.TABLE_NAME.equal(getName()))
            .orderBy(COLUMNS.ORDINAL_POSITION)
        ) {
            String generated =
                  record.get(COLUMNS.IS_SYSTEM_TIME_PERIOD_START, boolean.class)
                ? "ROW START"
                : record.get(COLUMNS.IS_SYSTEM_TIME_PERIOD_END, boolean.class)
                ? "ROW END"
                : record.get(COLUMNS.GENERATION_EXPRESSION);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get(COLUMNS.DATA_TYPE),
                record.get(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.get(COLUMNS.NUMERIC_PRECISION),
                record.get(COLUMNS.NUMERIC_SCALE),
                record.get(COLUMNS.IS_NULLABLE, boolean.class),
                generated != null ? null : record.get(COLUMNS.COLUMN_DEFAULT),
                DSL.name(record.get(COLUMNS.UDT_SCHEMA), record.get(COLUMNS.UDT_NAME))
            ).generatedAlwaysAs(generated);

            result.add(new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.get(COLUMNS.COLUMN_NAME),
                result.size() + 1,
			    type,
			    null != record.get(COLUMNS.IDENTITY_GENERATION),
			    record.get(SYSTEM_COLUMNS.REMARKS)
		    ));
		}

		return result;
	}
}
