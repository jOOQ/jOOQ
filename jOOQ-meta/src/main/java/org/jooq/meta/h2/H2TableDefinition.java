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
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.meta.h2.information_schema.Tables.COLUMNS;
import static org.jooq.tools.StringUtils.defaultString;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import org.jooq.meta.h2.H2Database.ElementType;
import org.jooq.meta.h2.H2Database.ElementTypeLookupKey;
import org.jooq.meta.hsqldb.information_schema.Tables;

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

    final H2Database getH2Database() {
        return (H2Database) super.getDatabase();
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        if (getH2Database().is2_0_202())
            return getElements2_0();
        else
            return getElements1_4();
    }

    public List<ColumnDefinition> getElements2_0() {
        List<ColumnDefinition> result = new ArrayList<>();

        H2Database db = (H2Database) getDatabase();


        // [#252] While recursing on ELEMENT_TYPES to detect multi dimensional
        //        arrays looks like the cleanest approach, H2's recursive SQL
        //        has been troubled by a ton of bugs in the past. Hard-coded
        //        left joins are an ugly option, which is why we opt for doing
        //        this calculation in Java, for once.
        //        See also: https://github.com/jOOQ/jOOQ/issues/252#issuecomment-1240484853
        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,

                // [#2230] [#11733] Translate INTERVAL_TYPE to supported types
                when(COLUMNS.INTERVAL_TYPE.like(any(inline("%YEAR%"), inline("%MONTH%"))), inline("INTERVAL YEAR TO MONTH"))
                .when(COLUMNS.INTERVAL_TYPE.like(any(inline("%DAY%"), inline("%HOUR%"), inline("%MINUTE%"), inline("%SECOND%"))), inline("INTERVAL DAY TO SECOND"))
                .else_(Tables.COLUMNS.DATA_TYPE).as(COLUMNS.TYPE_NAME),
                COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                coalesce(
                    COLUMNS.DATETIME_PRECISION.coerce(COLUMNS.NUMERIC_PRECISION),
                    COLUMNS.NUMERIC_PRECISION).as(COLUMNS.NUMERIC_PRECISION),
                COLUMNS.NUMERIC_SCALE,
                COLUMNS.IS_NULLABLE,
                Tables.COLUMNS.IS_GENERATED.eq(inline("ALWAYS")).as(COLUMNS.IS_COMPUTED),
                Tables.COLUMNS.GENERATION_EXPRESSION,
                COLUMNS.COLUMN_DEFAULT,
                COLUMNS.REMARKS,
                Tables.COLUMNS.IS_IDENTITY.eq(inline("YES")).as(Tables.COLUMNS.IS_IDENTITY),
                COLUMNS.DOMAIN_SCHEMA,
                COLUMNS.DOMAIN_NAME,
                Tables.COLUMNS.DTD_IDENTIFIER
            )
            .from(COLUMNS)
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(COLUMNS.TABLE_NAME.equal(getName()))
            .and(!getDatabase().getIncludeInvisibleColumns()
                ? condition(COLUMNS.IS_VISIBLE.coerce(BOOLEAN))
                : noCondition())
            .orderBy(COLUMNS.ORDINAL_POSITION)
        ) {

            // [#5331] AUTO_INCREMENT (MySQL style)
            // [#5331] DEFAULT nextval('sequence') (PostgreSQL style)
            // [#6332] [#6339] system-generated defaults shouldn't produce a default clause
            boolean isIdentity =
                   record.get(Tables.COLUMNS.IS_IDENTITY, boolean.class)
                || defaultString(record.get(COLUMNS.COLUMN_DEFAULT)).trim().toLowerCase().startsWith("nextval");

            boolean isComputed = record.get(COLUMNS.IS_COMPUTED, boolean.class);

            // [#681] Domain name if available
            Name userType = record.get(COLUMNS.DOMAIN_NAME) != null
                ? name(record.get(COLUMNS.DOMAIN_SCHEMA), record.get(COLUMNS.DOMAIN_NAME))
                : name(getSchema().getName(), getName() + "_" + record.get(COLUMNS.COLUMN_NAME));

            SchemaDefinition typeSchema = record.get(COLUMNS.DOMAIN_NAME) != null
                ? getDatabase().getSchema(record.get(COLUMNS.DOMAIN_SCHEMA))
                : getSchema();

            ElementType et = db.elementTypeLookup(new ElementTypeLookupKey(getSchema().getName(), getName(), record.get(Tables.COLUMNS.DTD_IDENTIFIER)));

            if (et == null)
                et = new ElementType(record.get(COLUMNS.TYPE_NAME), record.get(COLUMNS.CHARACTER_MAXIMUM_LENGTH), record.get(COLUMNS.NUMERIC_PRECISION), record.get(COLUMNS.NUMERIC_SCALE), null, 0);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                typeSchema == null ? getSchema() : typeSchema,
                et.dataType() + IntStream.range(0, et.dimension()).mapToObj(i -> " ARRAY").collect(Collectors.joining()),
                et.length(),
                et.precision(),
                et.scale(),
                record.get(COLUMNS.IS_NULLABLE, boolean.class),
                isIdentity || isComputed ? null : record.get(COLUMNS.COLUMN_DEFAULT),
                userType
            ).generatedAlwaysAs(isComputed ? record.get(Tables.COLUMNS.GENERATION_EXPRESSION) : null);

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

    public List<ColumnDefinition> getElements1_4() {
        List<ColumnDefinition> result = new ArrayList<>();

        // [#7206] H2 defaults to these precision/scale values when a DECIMAL/NUMERIC type
        //         does not have any precision/scale. What works in H2 works in almost no
        //         other database, which is relevant when using the DDLDatabase for instance,
        //         which is based on the H2Database
        Param<Long> maxP = inline(65535L);
        Param<Long> maxS = inline(32767L);

        H2Database db = (H2Database) getDatabase();

        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,

                // [#2230] [#11733] Translate INTERVAL_TYPE to supported types
                (db.is1_4_198()
                    ? (when(COLUMNS.INTERVAL_TYPE.like(any(inline("%YEAR%"), inline("%MONTH%"))), inline("INTERVAL YEAR TO MONTH"))
                      .when(COLUMNS.INTERVAL_TYPE.like(any(inline("%DAY%"), inline("%HOUR%"), inline("%MINUTE%"), inline("%SECOND%"))), inline("INTERVAL DAY TO SECOND"))
                      .else_(COLUMNS.TYPE_NAME))
                    : COLUMNS.TYPE_NAME
                ).as(COLUMNS.TYPE_NAME),
                choose().when(COLUMNS.NUMERIC_PRECISION.eq(maxP).and(COLUMNS.NUMERIC_SCALE.eq(maxS)), inline(0L))
                        .otherwise(COLUMNS.CHARACTER_MAXIMUM_LENGTH).as(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                COLUMNS.NUMERIC_PRECISION.decode(maxP, inline(0L), COLUMNS.NUMERIC_PRECISION).as(COLUMNS.NUMERIC_PRECISION),
                COLUMNS.NUMERIC_SCALE.decode(maxS, inline(0L), COLUMNS.NUMERIC_SCALE).as(COLUMNS.NUMERIC_SCALE),
                COLUMNS.IS_NULLABLE,
                COLUMNS.IS_COMPUTED.as(COLUMNS.IS_COMPUTED),
                COLUMNS.COLUMN_DEFAULT.as("GENERATION_EXPRESSION"),
                COLUMNS.COLUMN_DEFAULT,
                COLUMNS.REMARKS,
                COLUMNS.SEQUENCE_NAME.isNotNull().as("IS_IDENTITY"),
                db.is1_4_198() ? COLUMNS.DOMAIN_SCHEMA : inline("").as(COLUMNS.DOMAIN_SCHEMA),
                db.is1_4_198() ? COLUMNS.DOMAIN_NAME : inline("").as(COLUMNS.DOMAIN_NAME)
            )
            .from(COLUMNS)
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(COLUMNS.TABLE_NAME.equal(getName()))
            .and(!getDatabase().getIncludeInvisibleColumns()
                ? db.is1_4_198()
                    ? COLUMNS.IS_VISIBLE.eq(inline("TRUE"))
                    : COLUMNS.COLUMN_TYPE.notLike(inline("%INVISIBLE%"))
                : noCondition())
            .orderBy(COLUMNS.ORDINAL_POSITION)
        ) {

            // [#5331] AUTO_INCREMENT (MySQL style)
            // [#5331] DEFAULT nextval('sequence') (PostgreSQL style)
            // [#6332] [#6339] system-generated defaults shouldn't produce a default clause
            boolean isIdentity =
                   record.get("IS_IDENTITY", boolean.class)
                || defaultString(record.get(COLUMNS.COLUMN_DEFAULT)).trim().toLowerCase().startsWith("nextval");

            boolean isComputed = record.get(COLUMNS.IS_COMPUTED, boolean.class);

            // [#7644] H2 1.4.200 puts DATETIME_PRECISION in NUMERIC_SCALE column
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
                isIdentity || isComputed ? null : record.get(COLUMNS.COLUMN_DEFAULT),
                userType
            ).generatedAlwaysAs(isComputed ? record.get("GENERATION_EXPRESSION", String.class) : null);

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
