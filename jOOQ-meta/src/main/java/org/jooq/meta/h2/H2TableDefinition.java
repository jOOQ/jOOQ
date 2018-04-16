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

import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.zero;
import static org.jooq.meta.h2.information_schema.tables.Columns.COLUMNS;
import static org.jooq.tools.StringUtils.defaultString;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Param;
import org.jooq.Record;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.h2.information_schema.tables.Columns;

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

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        // [#7206] H2 defaults to these precision/scale values when a DECIMAL/NUMERIC type
        //         does not have any precision/scale. What works in H2 works in almost no
        //         other database, which is relevant when using the DDLDatabase for instance,
        //         which is based on the H2Database
        Param<Integer> maxP = inline(65535);
        Param<Integer> maxS = inline(32767);

        for (Record record : create().select(
                Columns.COLUMN_NAME,
                Columns.ORDINAL_POSITION,
                Columns.TYPE_NAME,
                ((H2Database) getDatabase()).is1_4_197() ? Columns.COLUMN_TYPE : inline("").as(Columns.COLUMN_TYPE),
                choose().when(Columns.NUMERIC_PRECISION.eq(maxP).and(Columns.NUMERIC_SCALE.eq(maxS)), zero())
                        .otherwise(Columns.CHARACTER_MAXIMUM_LENGTH).as(Columns.CHARACTER_MAXIMUM_LENGTH),
                Columns.NUMERIC_PRECISION.decode(maxP, zero(), Columns.NUMERIC_PRECISION).as(Columns.NUMERIC_PRECISION),
                Columns.NUMERIC_SCALE.decode(maxS, zero(), Columns.NUMERIC_SCALE).as(Columns.NUMERIC_SCALE),
                Columns.IS_NULLABLE,
                Columns.COLUMN_DEFAULT,
                Columns.REMARKS,
                Columns.SEQUENCE_NAME)
            .from(COLUMNS)
            .where(Columns.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(Columns.TABLE_NAME.equal(getName()))
            .orderBy(Columns.ORDINAL_POSITION)
            .fetch()) {

            // [#5331] AUTO_INCREMENT (MySQL style)
            // [#5331] DEFAULT nextval('sequence') (PostgreSQL style)
            // [#6332] [#6339] system-generated defaults shouldn't produce a default clause
            boolean isIdentity =
                   null != record.get(Columns.SEQUENCE_NAME)
                || defaultString(record.get(Columns.COLUMN_DEFAULT)).trim().toLowerCase().startsWith("nextval");

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get(Columns.TYPE_NAME),
                record.get(Columns.CHARACTER_MAXIMUM_LENGTH),
                record.get(Columns.NUMERIC_PRECISION),
                record.get(Columns.NUMERIC_SCALE),
                record.get(Columns.IS_NULLABLE, boolean.class),
                isIdentity ? null : record.get(Columns.COLUMN_DEFAULT),
                name(getSchema().getName(), getName() + "_" + record.get(Columns.COLUMN_NAME)));

            ColumnDefinition column = new DefaultColumnDefinition(
            	getDatabase().getTable(getSchema(), getName()),
                record.get(Columns.COLUMN_NAME),
                record.get(Columns.ORDINAL_POSITION),
                type,
                isIdentity,
                record.get(Columns.REMARKS));

            result.add(column);
        }

        return result;
    }
}
