/*
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

package org.jooq.util.mysql;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.name;
import static org.jooq.util.mysql.information_schema.tables.Columns.COLUMNS;
import static org.jooq.util.mysql.information_schema.tables.Columns.ORDINAL_POSITION;
import static org.jooq.util.mysql.information_schema.tables.Columns.TABLE_NAME;
import static org.jooq.util.mysql.information_schema.tables.Columns.TABLE_SCHEMA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.mysql.information_schema.tables.Columns;

/**
 * @author Lukas Eder
 */
public class MySQLTableDefinition extends AbstractTableDefinition {

    public MySQLTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                    Columns.ORDINAL_POSITION,
                    Columns.COLUMN_NAME,
                    Columns.COLUMN_COMMENT,
                    Columns.COLUMN_TYPE,
                    Columns.DATA_TYPE,
                    Columns.IS_NULLABLE,
                    Columns.COLUMN_DEFAULT,
                    Columns.CHARACTER_MAXIMUM_LENGTH,
                    Columns.NUMERIC_PRECISION,
                    Columns.NUMERIC_SCALE,
                    Columns.EXTRA)
                .from(COLUMNS)
                .where(TABLE_SCHEMA.equal(getSchema().getName()))
                .and(TABLE_NAME.equal(getName()))
                .orderBy(ORDINAL_POSITION)) {

            String dataType = record.get(Columns.DATA_TYPE);

            // [#519] Some types have unsigned versions
            if (getDatabase().supportsUnsignedTypes()) {
                if (asList("tinyint", "smallint", "mediumint", "int", "bigint").contains(dataType.toLowerCase())) {
                    if (record.get(Columns.COLUMN_TYPE).toLowerCase().contains("unsigned")) {
                        dataType += "unsigned";
                    }
                }
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                record.get(Columns.CHARACTER_MAXIMUM_LENGTH),
                record.get(Columns.NUMERIC_PRECISION),
                record.get(Columns.NUMERIC_SCALE),
                record.get(Columns.IS_NULLABLE, boolean.class),
                record.get(Columns.COLUMN_DEFAULT),
                name(getSchema().getName(), getName() + "_" + record.get(Columns.COLUMN_NAME))
            );

            ColumnDefinition column = new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                record.get(Columns.COLUMN_NAME),
                record.get(Columns.ORDINAL_POSITION, int.class),
                type,
                "auto_increment".equalsIgnoreCase(record.get(Columns.EXTRA)),
                record.get(Columns.COLUMN_COMMENT)
            );

            result.add(column);
        }

        return result;
    }
}
