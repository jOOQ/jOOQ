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

package org.jooq.meta.postgres;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.meta.postgres.information_schema.Tables.COLUMNS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_ATTRDEF;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_ATTRIBUTE;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_CLASS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_COLLATION;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_DESCRIPTION;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_NAMESPACE;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_TYPE;
import static org.jooq.tools.StringUtils.defaultString;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.postgres.information_schema.tables.Columns;
import org.jooq.meta.postgres.pg_catalog.tables.PgAttrdef;
import org.jooq.meta.postgres.pg_catalog.tables.PgAttribute;
import org.jooq.meta.postgres.pg_catalog.tables.PgClass;
import org.jooq.meta.postgres.pg_catalog.tables.PgCollation;
import org.jooq.meta.postgres.pg_catalog.tables.PgNamespace;
import org.jooq.meta.postgres.pg_catalog.tables.PgType;

/**
 * @author Lukas Eder
 */
public class PostgresMaterializedViewDefinition extends AbstractTableDefinition {

    public PostgresMaterializedViewDefinition(SchemaDefinition schema, String name, String comment) {
        this(schema, name, comment, null);
    }

    public PostgresMaterializedViewDefinition(SchemaDefinition schema, String name, String comment, String source) {
        super(schema, name, comment, TableType.MATERIALIZED_VIEW, source);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        Columns col = COLUMNS;
        PgAttribute a = PG_ATTRIBUTE.as("a");
        PgAttrdef ad = PG_ATTRDEF.as("ad");
        PgType t = PG_TYPE.as("t");
        PgType bt = PG_TYPE.as("bt");
        PgClass c = PG_CLASS.as("c");
        PgCollation co = PG_COLLATION.as("co");
        PgNamespace nt = PG_NAMESPACE.as("nt");
        PgNamespace nc = PG_NAMESPACE.as("nc");
        PgNamespace nbt = PG_NAMESPACE.as("nbt");
        PgNamespace nco = PG_NAMESPACE.as("nco");

        // [#8478] [#15414] CockroachDB can't use information_schema.sql_identifier
        Field<String> udtName = field("({0})::varchar", col.UDT_NAME.getDataType(), nvl(bt.TYPNAME, t.TYPNAME));
        Condition c1 = t.TYPTYPE.eq(inline("d"));
        Condition c1array = bt.TYPELEM.ne(inline(0L)).and(bt.TYPLEN.eq(inline((short) -1)));
        Condition c0array = t.TYPELEM.ne(inline(0L)).and(t.TYPLEN.eq(inline((short) -1)));

        for (Record record : create().select(
                field("({0})::varchar", col.COLUMN_NAME.getDataType(), a.ATTNAME).as(col.COLUMN_NAME),
                field("({0})::int", col.ORDINAL_POSITION.getDataType(), a.ATTNUM).as(col.ORDINAL_POSITION),
                field("({0})::varchar", col.DATA_TYPE.getDataType(),
                    when(c1,
                        when(c1array, substring(udtName, inline(2)).concat(inline(" ARRAY")))
                       .when(nbt.NSPNAME.eq(inline("pg_catalog")), field("format_type({0}, NULL::integer)", String.class, t.TYPBASETYPE))
                       .otherwise(inline("USER-DEFINED")))
                   .otherwise(
                        when(c0array, substring(udtName, inline(2)).concat(inline(" ARRAY")))
                       .when(nt.NSPNAME.eq(inline("pg_catalog")), field("format_type({0}, NULL::integer)", String.class, a.ATTTYPID))

                       // [#18738] Just like in PostgresTableDefinition
                       .when(udtName.eq(inline("geometry")), inline("geometry"))
                       .otherwise(inline("USER-DEFINED")))).as(col.DATA_TYPE),
                field("(information_schema._pg_char_max_length(information_schema._pg_truetypid(a.*, t.*), information_schema._pg_truetypmod(a.*, t.*)))::integer", col.CHARACTER_MAXIMUM_LENGTH.getDataType()).as(col.CHARACTER_MAXIMUM_LENGTH),
                field("(information_schema._pg_numeric_precision(information_schema._pg_truetypid(a.*, t.*), information_schema._pg_truetypmod(a.*, t.*)))::integer", col.NUMERIC_PRECISION.getDataType()).as(col.NUMERIC_PRECISION),
                field("(information_schema._pg_numeric_scale(information_schema._pg_truetypid(a.*, t.*), information_schema._pg_truetypmod(a.*, t.*)))::integer", col.NUMERIC_SCALE.getDataType()).as(col.NUMERIC_SCALE),
                field("({0})::varchar", col.IS_NULLABLE.getDataType(),
                    when(condition(a.ATTNOTNULL).or(t.TYPTYPE.eq(inline("d")).and(t.TYPNOTNULL)), inline("NO"))
                   .otherwise(inline("YES"))).as(col.IS_NULLABLE),
                field("(pg_get_expr({0}, {1}))::varchar", col.COLUMN_DEFAULT.getDataType(), ad.ADBIN, ad.ADRELID).as(col.COLUMN_DEFAULT),
                field("({0})::varchar", col.UDT_SCHEMA.getDataType(),
                    nvl(nbt.NSPNAME, nt.NSPNAME)).as(col.UDT_SCHEMA),
                when(c1.and(c1array).or(not(c1).and(c0array)), substring(udtName, inline(2)))
                    .else_(udtName)
                    .as(col.UDT_NAME),
                PG_DESCRIPTION.DESCRIPTION)
            .from(a
                .leftJoin(ad)
                    .on(a.ATTRELID.eq(ad.ADRELID))
                    .and(a.ATTNUM.eq(ad.ADNUM))
                .join(c
                    .join(nc)
                        .on(c.RELNAMESPACE.eq(nc.OID)))
                    .on(a.ATTRELID.eq(c.OID))
                .join(t
                    .join(nt)
                        .on(t.TYPNAMESPACE.eq(nt.OID)))
                    .on(a.ATTTYPID.eq(t.OID)))
                .leftJoin(bt
                    .join(nbt)
                        .on(bt.TYPNAMESPACE.eq(nbt.OID)))
                    .on(t.TYPTYPE.eq(inline("d")).and(t.TYPBASETYPE.eq(bt.OID)))
                .leftJoin(co
                    .join(nco)
                        .on(co.COLLNAMESPACE.eq(nco.OID)))
                    .on(a.ATTCOLLATION.eq(co.OID).and(
                        nco.NSPNAME.ne(inline("pg_catalog")).or(co.COLLNAME.ne(inline("default")))
                    ))
                .leftJoin(PG_DESCRIPTION)
                    .on(PG_DESCRIPTION.OBJOID.eq(c.OID))
                    .and(PG_DESCRIPTION.CLASSOID.eq(field("'pg_class'::regclass", BIGINT)))
                    .and(PG_DESCRIPTION.OBJSUBID.eq(a.ATTNUM.coerce(PG_DESCRIPTION.OBJSUBID)))
            .where(
                not(condition("pg_is_other_temp_schema({0})", nc.OID))
                .and(a.ATTNUM.gt(inline((short) 0)))
                .and(not(a.ATTISDROPPED))
                .and(c.RELKIND.eq(inline("m")))
                .and(nc.NSPNAME.in(getSchema().getName()))
                .and(c.RELNAME.eq(getName())))
            .orderBy(a.ATTNUM)
        ) {

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
            );

            result.add(new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                record.get(COLUMNS.COLUMN_NAME),
                result.size() + 1,
                type,
                defaultString(record.get(COLUMNS.COLUMN_DEFAULT)).startsWith("nextval"),
                record.get(PG_DESCRIPTION.DESCRIPTION)
            ));
        }

        return result;
    }
}
