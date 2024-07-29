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
package org.jooq.meta.clickhouse;

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.regexpReplaceFirst;
import static org.jooq.meta.clickhouse.information_schema.Tables.COLUMNS;
import static org.jooq.meta.clickhouse.system.System.SYSTEM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Name;
import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.clickhouse.system.System;

/**
 * @author Lukas Eder
 */
public class ClickHouseTableDefinition extends AbstractTableDefinition {

    public ClickHouseTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    public ClickHouseTableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType, String source) {
        super(schema, name, comment, tableType, source);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,
                regexpReplaceFirst(
                    COLUMNS.DATA_TYPE.coerce(String.class),
                    inline("Nullable\\((.*)\\)"),
                    inline("\\1")).as(COLUMNS.DATA_TYPE).as(COLUMNS.DATA_TYPE),
                COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                COLUMNS.NUMERIC_PRECISION,
                COLUMNS.NUMERIC_SCALE,
                COLUMNS.IS_NULLABLE,
                nullif(COLUMNS.COLUMN_DEFAULT, inline("")).as(COLUMNS.COLUMN_DEFAULT),
                SYSTEM.COLUMNS.COMMENT
            )
            .from(COLUMNS)
            .join(SYSTEM.COLUMNS)
                .on(COLUMNS.TABLE_SCHEMA.eq(SYSTEM.COLUMNS.DATABASE))
                .and(COLUMNS.TABLE_NAME.eq(SYSTEM.COLUMNS.TABLE))
                .and(COLUMNS.COLUMN_NAME.eq(SYSTEM.COLUMNS.NAME))
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(COLUMNS.TABLE_NAME.equal(getName()))
            .orderBy(COLUMNS.ORDINAL_POSITION)
        ) {
            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get(COLUMNS.DATA_TYPE, String.class),
                record.get(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.get(COLUMNS.NUMERIC_PRECISION),
                record.get(COLUMNS.NUMERIC_SCALE),
                record.get(COLUMNS.IS_NULLABLE, boolean.class),
                record.get(COLUMNS.COLUMN_DEFAULT),
                (Name) null
            );

            result.add(new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                record.get(COLUMNS.COLUMN_NAME),
                result.size() + 1,
                type,
                false,
                record.get(SYSTEM.COLUMNS.COMMENT)
            ));
        }

        return result;
    }
}
