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
package org.jooq.impl;

// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.Keywords.K_COLUMNS;
import static org.jooq.impl.Keywords.K_ERROR;
import static org.jooq.impl.Keywords.K_FOR;
import static org.jooq.impl.Keywords.K_JSON_TABLE;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_ORDINALITY;
import static org.jooq.impl.Keywords.K_PATH;
import static org.jooq.impl.Keywords.K_WITH;
import static org.jooq.impl.Names.N_JSON_TABLE;
import static org.jooq.impl.Names.N_OPENJSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitSubquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.JSONTableColumnPathStep;
import org.jooq.Name;
// ...
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectField;
import org.jooq.TableOptions;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class JSONTable
extends AbstractTable<Record>
implements
    JSONTableColumnPathStep {
    private static final Set<SQLDialect>         REQUIRES_COLUMN_PATH = SQLDialect.supportedBy(MYSQL);





    private final Field<String>                  path;
    private final Field<?>                       json;
    private final QueryPartList<JSONTableColumn> columns;
    private final boolean                        hasOrdinality;
    private transient FieldsImpl<Record>         fields;

    JSONTable(Field<?> json, Field<String> path) {
        this(json, path, null, false);
    }

    private JSONTable(
        Field<?> json,
        Field<String> path,
        QueryPartList<JSONTableColumn> columns,
        boolean hasOrdinality
    ) {
        super(TableOptions.expression(), N_JSON_TABLE);

        this.json = json;
        this.path = path;
        this.columns = columns == null ? new QueryPartList<>() : columns;
        this.hasOrdinality = hasOrdinality;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final JSONTable column(String name) {
        return column(DSL.name(name));
    }

    @Override
    public final JSONTable column(Name name) {
        return column(DSL.field(name));
    }

    @Override
    public final JSONTable column(Field<?> name) {
        return column(name, name.getDataType());
    }

    @Override
    public final JSONTable column(String name, DataType<?> type) {
        return column(DSL.name(name), type);
    }

    @Override
    public final JSONTable column(Name name, DataType<?> type) {
        return column(DSL.field(name), type);
    }

    @Override
    public final JSONTable column(Field<?> name, DataType<?> type) {
        QueryPartList<JSONTableColumn> c = new QueryPartList<>(columns);
        c.add(new JSONTableColumn(name, type, false, null));
        return new JSONTable(json, path, c, hasOrdinality);
    }

    @Override
    public final JSONTable forOrdinality() {
        return path0(true, null);
    }

    @Override
    public final JSONTable path(String p) {
        return path0(false, p);
    }

    private final JSONTable path0(boolean forOrdinality, String p) {
        QueryPartList<JSONTableColumn> c = new QueryPartList<>(columns);
        int i = c.size() - 1;
        JSONTableColumn last = c.get(i);
        c.set(i, new JSONTableColumn(last.field, last.type, forOrdinality, p));
        return new JSONTable(json, path, c, hasOrdinality || forOrdinality);
    }

    // -------------------------------------------------------------------------
    // XXX: Table API
    // -------------------------------------------------------------------------

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImplN.class;
    }

    @Override
    final FieldsImpl<Record> fields0() {
        if (fields == null)
            fields = new FieldsImpl<>(map(columns, c -> c.field.getDataType() == c.type ? c.field : DSL.field(c.field.getQualifiedName(), c.type)));

        return fields;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case POSTGRES:
                acceptPostgres(ctx);
                break;







            default:
                acceptStandard(ctx);
                break;
        }
    }

    private final void acceptPostgres(Context<?> ctx) {
        visitSubquery(
            ctx,
            select(map(columns, col -> col.forOrdinality
                ? DSL.field("o").as(col.field)
                : DSL.field("(jsonb_path_query_first(j, {0}::jsonpath)->>0)::{1}",
                    col.path != null ? val(col.path) : inline("$." + col.field.getName()),
                    keyword(col.type.getCastTypeName(ctx.configuration()))
                  ).as(col.field)))
            .from(hasOrdinality
                ? "jsonb_path_query({0}, {1}::jsonpath) {with} {ordinality} {as} t(j, o)"
                : "jsonb_path_query({0}, {1}::jsonpath) {as} t(j)",
                json.getType() == JSONB.class ? json : json.cast(JSONB),
                path
            )
        );
    }


































    private final void acceptStandard(Context<?> ctx) {
        ctx.visit(K_JSON_TABLE).sqlIndentStart('(');

        ctx.visit(json).sql(',').formatSeparator();
        acceptJSONPath(ctx);

        ctx.formatSeparator().visit(K_COLUMNS).sql(" (").visit(columns).sql(')');






        ctx.sqlIndentEnd(')');
    }

    private final void acceptJSONPath(Context<?> ctx) {






        ctx.visit(path);
    }

    @Override
    public boolean declaresTables() {
        return true;
    }

    private static class JSONTableColumn extends AbstractQueryPart {

        final Field<?>            field;
        final DataType<?>         type;
        final boolean             forOrdinality;
        final String              path;

        JSONTableColumn(Field<?> field, DataType<?> type, boolean forOrdinality, String path) {
            this.field = field;
            this.type = type;
            this.forOrdinality = forOrdinality;
            this.path = path;
        }

        @Override
        public final void accept(Context<?> ctx) {
            ctx.qualify(false, c -> c.visit(field)).sql(' ');

            if (forOrdinality)
                ctx.visit(K_FOR).sql(' ').visit(K_ORDINALITY);
            else
                Tools.toSQLDDLTypeDeclaration(ctx, type);

            String p =
                  path != null
                ? path
                : !forOrdinality && REQUIRES_COLUMN_PATH.contains(ctx.dialect())
                ? "$." + field.getName()
                : null;

            if (p != null)





                    ctx.sql(' ').visit(K_PATH).sql(' ').visit(inline(p));
        }
    }
}
