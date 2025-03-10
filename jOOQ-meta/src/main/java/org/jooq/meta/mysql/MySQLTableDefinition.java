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

package org.jooq.meta.mysql;

import static java.util.Arrays.asList;
// ...
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.when;
import static org.jooq.meta.mysql.information_schema.Tables.COLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

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
        MySQLDatabase database = (MySQLDatabase) getDatabase();
        Field<String> generationExpression = database.generationExpression(COLUMNS.GENERATION_EXPRESSION);

        for (Record record : create().select(
                    COLUMNS.ORDINAL_POSITION,
                    COLUMNS.COLUMN_NAME,
                    COLUMNS.COLUMN_COMMENT,
                    COLUMNS.COLUMN_TYPE,
                    when(database.jsonCheck(COLUMNS.TABLE_SCHEMA, COLUMNS.TABLE_NAME, COLUMNS.COLUMN_NAME), inline("json"))
                        .else_(COLUMNS.DATA_TYPE)
                        .as(COLUMNS.DATA_TYPE),
                    COLUMNS.IS_NULLABLE,
                    COLUMNS.COLUMN_DEFAULT,
                    COLUMNS.EXTRA,
                    generationExpression,
                    COLUMNS.CHARACTER_MAXIMUM_LENGTH,

                    // [#10856] Some older versions of MySQL 5.7 don't have the DATETIME_PRECISION column yet
                    getDatabase().exists(COLUMNS.DATETIME_PRECISION)
                        ? coalesce(COLUMNS.NUMERIC_PRECISION, COLUMNS.DATETIME_PRECISION).as(COLUMNS.NUMERIC_PRECISION)
                        : COLUMNS.NUMERIC_PRECISION,
                    COLUMNS.NUMERIC_SCALE,
                    COLUMNS.EXTRA)
                .from(COLUMNS)
                // [#5213] Duplicate schema value to work around MySQL issue https://bugs.mysql.com/bug.php?id=86022
                .where(COLUMNS.TABLE_SCHEMA.in(getSchema().getName(), getSchema().getName()))
                .and(COLUMNS.TABLE_NAME.equal(getName()))
                .and(getDatabase().getIncludeInvisibleColumns()
                    ? noCondition()
                    : COLUMNS.EXTRA.notLike("%INVISIBLE%"))
                .orderBy(COLUMNS.ORDINAL_POSITION)
        ) {

            String dataType = record.get(COLUMNS.DATA_TYPE);

            // [#7719]
            boolean displayWidths = getDatabase().integerDisplayWidths();

            // [#6492] MariaDB supports a standard IS_GENERATED, but MySQL doesn't (yet)
            GenerationOption generationOption =
                  "VIRTUAL GENERATED".equalsIgnoreCase(record.get(COLUMNS.EXTRA))
                ? GenerationOption.VIRTUAL
                : "STORED GENERATED".equalsIgnoreCase(record.get(COLUMNS.EXTRA))
                ? GenerationOption.STORED
                : null;

            // [#13818] Some DEFAULT expressions (e.g. CURRENT_TIMESTAMP) produce a DEFAULT_GENERATED value in EXTRA
            boolean generated = generationOption != null;

            columnTypeFix:
            if (displayWidths) {
                if (asList("tinyint", "smallint", "mediumint", "int", "bigint").contains(dataType.toLowerCase())) {
                    String columnType = record.get(COLUMNS.COLUMN_TYPE).toLowerCase();






                    Matcher matcher = COLUMN_TYPE.matcher(columnType);

                    if (matcher.find()) {
                        String mType = matcher.group(1);
                        String mPrecision = matcher.group(2);
                        String mUnsigned = matcher.group(3);

                        dataType = mType
                                 + (mUnsigned != null ? mUnsigned : "")
                                 + (displayWidths && mPrecision != null ? mPrecision : "");
                    }
                }
            }

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                record.get(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.get(COLUMNS.NUMERIC_PRECISION),
                record.get(COLUMNS.NUMERIC_SCALE),
                record.get(COLUMNS.IS_NULLABLE, boolean.class),
                generated ? null : record.get(COLUMNS.COLUMN_DEFAULT),
                name(getSchema().getName(), getName() + "_" + record.get(COLUMNS.COLUMN_NAME))
            )
                .generatedAlwaysAs(generated ? record.get(COLUMNS.GENERATION_EXPRESSION) : null)
                .generationOption(generationOption);






            result.add(new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                record.get(COLUMNS.COLUMN_NAME),
                result.size() + 1,
                type,
                "auto_increment".equalsIgnoreCase(record.get(COLUMNS.EXTRA)),
                record.get(COLUMNS.COLUMN_COMMENT)
            ));
        }

        return result;
    }
}
