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

package org.jooq.meta.postgres;

import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.greatest;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.when;
import static org.jooq.meta.postgres.information_schema.Tables.COLUMNS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_ATTRIBUTE;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_CLASS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_DESCRIPTION;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_NAMESPACE;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_TYPE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.impl.DSL;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class PostgresTableDefinition extends AbstractTableDefinition {

    public PostgresTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    public PostgresTableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType, String source) {
        super(schema, name, comment, tableType, source);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        PostgresDatabase database = (PostgresDatabase) getDatabase();
        Field<String> dataType =
            when(COLUMNS.INTERVAL_TYPE.like(any(inline("%YEAR%"), inline("%MONTH%"))), inline("INTERVAL YEAR TO MONTH"))
            .when(COLUMNS.INTERVAL_TYPE.like(any(inline("%DAY%"), inline("%HOUR%"), inline("%MINUTE%"), inline("%SECOND%"))), inline("INTERVAL DAY TO SECOND"))
            .when(COLUMNS.DATA_TYPE.eq(inline("USER-DEFINED")).and(COLUMNS.UDT_NAME.eq(inline("geometry"))), inline("geometry"))
            .when(COLUMNS.DATA_TYPE.eq(inline("ARRAY")),
                concat(
                    substring(PG_TYPE.TYPNAME, inline(2)),
                    DSL.repeat(inline(" ARRAY"),
                        // [#252] Among others, it seems that UDT arrays report dimension 0 (?)
                        greatest(inline(1), PG_ATTRIBUTE.ATTNDIMS)
                    )
                ))
            .else_(COLUMNS.DATA_TYPE);
        Field<String> udtSchema = COLUMNS.UDT_SCHEMA;

        // [#8067] [#11658] [#13919]
        // A more robust / sophisticated decoding might be available via
        // - information_schema._pg_char_max_length
        // - information_schema._pg_numeric_precision
        // - information_schema._pg_numeric_scale
        // - information_schema._pg_datetime_precision
        // However, we cannot rely on all PG versions and PG clones making these available
        // The source of these functions can be looked up here:
        // https://github.com/postgres/postgres/blob/master/src/backend/catalog/information_schema.sql
        Field<Integer> length = nvl(
            COLUMNS.CHARACTER_MAXIMUM_LENGTH,
            when(
                COLUMNS.UDT_NAME.in(inline("_varchar"), inline("_bpchar"), inline("_char")),
                PG_ATTRIBUTE.ATTTYPMOD.sub(inline(4))
            )
        );
        Field<Integer> precision = coalesce(
            COLUMNS.DATETIME_PRECISION,
            COLUMNS.NUMERIC_PRECISION,
            when(
                COLUMNS.UDT_NAME.in(inline("_time"), inline("_timetz"), inline("_timestamp"), inline("_timestamptz")).and(PG_ATTRIBUTE.ATTTYPMOD.ge(inline(0))),
                PG_ATTRIBUTE.ATTTYPMOD
            )
            .when(
                COLUMNS.UDT_NAME.eq(inline("_numeric")),
                PG_ATTRIBUTE.ATTTYPMOD.sub(inline(4)).shr(inline(16)).bitAnd(inline(65535))
            )
        );
        Field<Integer> scale = nvl(
            COLUMNS.NUMERIC_SCALE,
            when(
                COLUMNS.UDT_NAME.eq(inline("_numeric")),
                PG_ATTRIBUTE.ATTTYPMOD.sub(inline(4)).bitAnd(inline(65535))
            )
        );

        Field<String> serialColumnDefault = inline("nextval('%_seq'::regclass)");
        Field<String> generationExpression = COLUMNS.GENERATION_EXPRESSION;
        Field<String> attgenerated = database.is12() ? PG_ATTRIBUTE.ATTGENERATED : inline("s");










        Condition isSerial = lower(COLUMNS.COLUMN_DEFAULT).likeIgnoreCase(serialColumnDefault);
        Condition isIdentity10 = COLUMNS.IS_IDENTITY.eq(inline("YES"));

        // [#9200] only use COLUMN_DEFAULT for ColumnDefinition#isIdentity() if
        // table has no column with IS_IDENTITY = 'YES'
        Condition isIdentity =
              database.is10()
            ? isIdentity10.or(count().filterWhere(isIdentity10).over().eq(inline(0)).and(isSerial))
            : isSerial;

        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,
                dataType.as(COLUMNS.DATA_TYPE),
                length.as(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                precision.as(COLUMNS.NUMERIC_PRECISION),
                scale.as(COLUMNS.NUMERIC_SCALE),
                (when(isIdentity, inline("YES"))).as(COLUMNS.IS_IDENTITY),
                COLUMNS.IS_NULLABLE,
                generationExpression.as(COLUMNS.GENERATION_EXPRESSION),
                attgenerated.as(PG_ATTRIBUTE.ATTGENERATED),
                (when(isIdentity, inline(null, String.class)).else_(COLUMNS.COLUMN_DEFAULT)).as(COLUMNS.COLUMN_DEFAULT),
                coalesce(COLUMNS.DOMAIN_SCHEMA, udtSchema).as(COLUMNS.UDT_SCHEMA),
                coalesce(
                    COLUMNS.DOMAIN_NAME,
                    when(COLUMNS.DATA_TYPE.eq(inline("ARRAY")), substring(PG_TYPE.TYPNAME, inline(2)))
                        .else_(COLUMNS.UDT_NAME)
                ).as(COLUMNS.UDT_NAME),
                PG_DESCRIPTION.DESCRIPTION)
            .from(COLUMNS)
            .join(PG_NAMESPACE)
                .on(COLUMNS.TABLE_SCHEMA.eq(PG_NAMESPACE.NSPNAME))
            .join(PG_CLASS)
                .on(PG_CLASS.RELNAME.eq(COLUMNS.TABLE_NAME))
                .and(PG_CLASS.RELNAMESPACE.eq(PG_NAMESPACE.OID))
            .join(PG_ATTRIBUTE)
                .on(PG_ATTRIBUTE.ATTRELID.eq(PG_CLASS.OID))
                .and(PG_ATTRIBUTE.ATTNAME.eq(COLUMNS.COLUMN_NAME))
            .join(PG_TYPE)
                .on(PG_ATTRIBUTE.ATTTYPID.eq(PG_TYPE.OID))
            .leftJoin(PG_DESCRIPTION)
                .on(PG_DESCRIPTION.OBJOID.eq(PG_CLASS.OID))
                .and(PG_DESCRIPTION.OBJSUBID.eq(COLUMNS.ORDINAL_POSITION))
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(COLUMNS.TABLE_NAME.equal(getName()))



            .orderBy(COLUMNS.ORDINAL_POSITION)) {

            SchemaDefinition typeSchema = null;

            String schemaName = record.get(COLUMNS.UDT_SCHEMA);
            if (schemaName != null)
                typeSchema = getDatabase().getSchema(schemaName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                typeSchema,
                record.get(COLUMNS.DATA_TYPE),
                record.get(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.get(COLUMNS.NUMERIC_PRECISION),
                record.get(COLUMNS.NUMERIC_SCALE),
                record.get(COLUMNS.IS_NULLABLE, boolean.class),
                record.get(COLUMNS.COLUMN_DEFAULT),
                name(
                    record.get(COLUMNS.UDT_SCHEMA),
                    record.get(COLUMNS.UDT_NAME)
                )
            )
                .generatedAlwaysAs(record.get(COLUMNS.GENERATION_EXPRESSION))
                .generationOption(
                    "s".equals(record.get(PG_ATTRIBUTE.ATTGENERATED))
                  ? GenerationOption.STORED
                  : "v".equals(record.get(PG_ATTRIBUTE.ATTGENERATED))
                  ? GenerationOption.VIRTUAL
                  : null
                );

            ColumnDefinition column = new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                record.get(COLUMNS.COLUMN_NAME),
                record.get(COLUMNS.ORDINAL_POSITION, int.class),
                type,
                record.get(COLUMNS.IS_IDENTITY, boolean.class),
                record.get(PG_DESCRIPTION.DESCRIPTION)
            );

            result.add(column);
        }

        return result;
    }
}
