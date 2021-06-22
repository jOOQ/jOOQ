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
package org.jooq.meta.h2;

import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.DSL.zero;
import static org.jooq.meta.h2.information_schema.Tables.COLUMNS;
import static org.jooq.tools.StringUtils.defaultString;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Name;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * H2 table definition
 *
 * @author Espen Stromsnes
 * @author Oliver Flege
 */
public class H2TableDefinition extends AbstractTableDefinition {

    public H2TableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    public H2TableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType, String source) {
        super(schema, name, comment, tableType, source);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        // [#7206] H2 defaults to these precision/scale values when a DECIMAL/NUMERIC type
        //         does not have any precision/scale. What works in H2 works in almost no
        //         other database, which is relevant when using the DDLDatabase for instance,
        //         which is based on the H2Database
        Param<Integer> maxP = inline(65535);
        Param<Integer> maxS = inline(32767);

        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,

                // [#2230] [#11733] Translate INTERVAL_TYPE to supported types
                (((H2Database) getDatabase()).is1_4_198()
                    ? (when(COLUMNS.INTERVAL_TYPE.like(any(inline("%YEAR%"), inline("%MONTH%"))), inline("INTERVAL YEAR TO MONTH"))
                      .when(COLUMNS.INTERVAL_TYPE.like(any(inline("%DAY%"), inline("%HOUR%"), inline("%MINUTE%"), inline("%SECOND%"))), inline("INTERVAL DAY TO SECOND"))
                      .else_(COLUMNS.TYPE_NAME))
                    : COLUMNS.TYPE_NAME
                ).as(COLUMNS.TYPE_NAME),
                (((H2Database) getDatabase()).is1_4_197()
                    ? COLUMNS.COLUMN_TYPE
                    : COLUMNS.TYPE_NAME
                ).as(COLUMNS.COLUMN_TYPE),
                choose().when(COLUMNS.NUMERIC_PRECISION.eq(maxP).and(COLUMNS.NUMERIC_SCALE.eq(maxS)), zero())
                        .otherwise(COLUMNS.CHARACTER_MAXIMUM_LENGTH).as(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                COLUMNS.NUMERIC_PRECISION.decode(maxP, zero(), COLUMNS.NUMERIC_PRECISION).as(COLUMNS.NUMERIC_PRECISION),
                COLUMNS.NUMERIC_SCALE.decode(maxS, zero(), COLUMNS.NUMERIC_SCALE).as(COLUMNS.NUMERIC_SCALE),
                COLUMNS.IS_NULLABLE,
                COLUMNS.COLUMN_DEFAULT,
                COLUMNS.REMARKS,
                COLUMNS.SEQUENCE_NAME,
                ((H2Database) getDatabase()).is1_4_198() ? COLUMNS.DOMAIN_SCHEMA : inline("").as(COLUMNS.DOMAIN_SCHEMA),
                ((H2Database) getDatabase()).is1_4_198() ? COLUMNS.DOMAIN_NAME : inline("").as(COLUMNS.DOMAIN_NAME)
            )
            .from(COLUMNS)
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(COLUMNS.TABLE_NAME.equal(getName()))
            .and(!getDatabase().getIncludeInvisibleColumns()
                ? ((H2Database) getDatabase()).is1_4_198()
                    ? COLUMNS.IS_VISIBLE.eq(inline("TRUE"))
                    : COLUMNS.COLUMN_TYPE.notLike(inline("%INVISIBLE%"))
                : noCondition())
            .orderBy(COLUMNS.ORDINAL_POSITION)) {

            // [#5331] AUTO_INCREMENT (MySQL style)
            // [#5331] DEFAULT nextval('sequence') (PostgreSQL style)
            // [#6332] [#6339] system-generated defaults shouldn't produce a default clause
            boolean isIdentity =
                   null != record.get(COLUMNS.SEQUENCE_NAME)
                || defaultString(record.get(COLUMNS.COLUMN_DEFAULT)).trim().toLowerCase().startsWith("nextval");


            // [#7644] H2 puts DATETIME_PRECISION in NUMERIC_SCALE column
            boolean isTimestamp = record.get(COLUMNS.TYPE_NAME).trim().toLowerCase().startsWith("timestamp");

            // [#681] Domain name if available
            Name userType = record.get(COLUMNS.DOMAIN_NAME) != null
                ? name(record.get(COLUMNS.DOMAIN_SCHEMA), record.get(COLUMNS.DOMAIN_NAME))
                : name(getSchema().getName(), getName() + "_" + record.get(COLUMNS.COLUMN_NAME));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get(COLUMNS.TYPE_NAME),
                record.get(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                isTimestamp
                    ? record.get(COLUMNS.NUMERIC_SCALE)
                    : record.get(COLUMNS.NUMERIC_PRECISION),
                isTimestamp
                    ? 0
                    : record.get(COLUMNS.NUMERIC_SCALE),
                record.get(COLUMNS.IS_NULLABLE, boolean.class),
                isIdentity ? null : record.get(COLUMNS.COLUMN_DEFAULT),
                userType
            );

            result.add(new DefaultColumnDefinition(
            	getDatabase().getTable(getSchema(), getName()),
                record.get(COLUMNS.COLUMN_NAME),
                result.size() + 1,
                type,
                isIdentity,
                record.get(COLUMNS.REMARKS))
            );
        }

        return result;
    }
}
