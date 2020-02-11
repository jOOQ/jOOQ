/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
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

package org.jooq.meta.mysql;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.name;
import static org.jooq.meta.mysql.information_schema.tables.Columns.COLUMNS;
import static org.jooq.meta.mysql.information_schema.tables.Columns.ORDINAL_POSITION;
import static org.jooq.meta.mysql.information_schema.tables.Columns.TABLE_NAME;
import static org.jooq.meta.mysql.information_schema.tables.Columns.TABLE_SCHEMA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.mysql.information_schema.tables.Columns;

/**
 * @author Lukas Eder
 */
public class MySQLTableDefinition extends AbstractTableDefinition {

    private static final Pattern COLUMN_TYPE = Pattern.compile("(\\w+)\\s*(\\(\\d+\\))?\\s*(unsigned)?");

    public MySQLTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    public MySQLTableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType, String source) {
        super(schema, name, comment, tableType, source);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

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
                // [#5213] Duplicate schema value to work around MySQL issue https://bugs.mysql.com/bug.php?id=86022
                .where(TABLE_SCHEMA.in(getSchema().getName(), getSchema().getName()))
                .and(TABLE_NAME.equal(getName()))
                .orderBy(ORDINAL_POSITION)) {

            String dataType = record.get(Columns.DATA_TYPE);

            // [#519] Some types have unsigned versions
            boolean unsigned = getDatabase().supportsUnsignedTypes();

            // [#7719]
            boolean displayWidths = getDatabase().integerDisplayWidths();

            if (unsigned || displayWidths) {
                if (asList("tinyint", "smallint", "mediumint", "int", "bigint").contains(dataType.toLowerCase())) {
                    Matcher matcher = COLUMN_TYPE.matcher(record.get(Columns.COLUMN_TYPE).toLowerCase());

                    if (matcher.find()) {
                        String mType = matcher.group(1);
                        String mPrecision = matcher.group(2);
                        String mUnsigned = matcher.group(3);

                        dataType = mType
                                 + (unsigned && mUnsigned != null ? mUnsigned : "")
                                 + (displayWidths && mPrecision != null ? mPrecision : "");
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
