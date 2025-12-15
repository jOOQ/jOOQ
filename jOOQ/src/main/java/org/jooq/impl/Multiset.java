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
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.arrayAgg;
import static org.jooq.impl.DSL.arrayGet;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonbArray;
import static org.jooq.impl.DSL.noField;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
// ...
// ...
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlattributes;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlserializeContent;
import static org.jooq.impl.DerivedTable.NO_SUPPORT_CORRELATED_DERIVED_TABLE;
import static org.jooq.impl.JSONArrayAgg.patchOracleArrayAggBug;
import static org.jooq.impl.Keywords.K_ARRAY;
import static org.jooq.impl.Keywords.K_MULTISET;
import static org.jooq.impl.Names.NQ_RESULT;
import static org.jooq.impl.Names.N_HEX;
import static org.jooq.impl.Names.N_JSON_QUERY;
import static org.jooq.impl.Names.N_MULTISET;
import static org.jooq.impl.Names.N_RECORD;
import static org.jooq.impl.Names.N_RESULT;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.CLOB;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.allMatch;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.fieldName;
import static org.jooq.impl.Tools.fieldNameString;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.impl.Tools.sortable;
import static org.jooq.impl.Tools.unaliasedFields;
import static org.jooq.impl.Tools.unqualified;
import static org.jooq.impl.Tools.visitSubquery;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONDITION;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.jooq.AggregateFilterStep;
import org.jooq.ArrayAggOrderByStep;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.JSON;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.JSONArrayAggReturningStep;
import org.jooq.JSONArrayNullStep;
import org.jooq.JSONArrayReturningStep;
import org.jooq.JSONB;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectNullStep;
import org.jooq.JSONObjectReturningStep;
import org.jooq.Name;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
// ...
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
// ...
import org.jooq.SelectForStep;
import org.jooq.SelectOrderByStep;
import org.jooq.SortField;
import org.jooq.Spatial;
import org.jooq.Table;
import org.jooq.TableLike;
// ...
import org.jooq.XML;
import org.jooq.XMLAggOrderByStep;

/**
 * @author Lukas Eder
 */
final class Multiset<R extends Record> extends AbstractField<Result<R>> implements QOM.Multiset<R> {

    static final Set<SQLDialect> NO_SUPPORT_JSON_COMPARE           = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect> NO_SUPPORT_JSONB_COMPARE          = SQLDialect.supportedBy();
    static final Set<SQLDialect> NO_SUPPORT_XML_COMPARE            = SQLDialect.supportedBy(POSTGRES);
    static final Set<SQLDialect> NO_SUPPORT_DERIVED_TABLE_ORDERING = SQLDialect.supportedBy(DUCKDB);
    static final Set<SQLDialect> FORCE_LIMIT_IN_DERIVED_TABLE      = SQLDialect.supportedBy(MARIADB, MYSQL, TRINO);

    final TableLike<R>           table;
    final Select<R>              select;

    Multiset(TableLike<R> table) {
        this(table, table instanceof Select<R> s ? s : selectFrom(table));
    }

    @SuppressWarnings("unchecked")
    private Multiset(TableLike<R> table, Select<R> select) {
        // [#12100] Can't use select.fieldsRow() here.
        super(N_MULTISET, new MultisetDataType<>((AbstractRow<R>) DSL.row(select.getSelect()), select.getRecordType()));

        this.table = table;
        this.select = select;
    }

    @Override
    public final void accept(Context<?> ctx) {











        if (TRUE.equals(ctx.data(DATA_MULTISET_CONDITION))) {
            ctx.data().remove(DATA_MULTISET_CONDITION);
            ctx.data(DATA_MULTISET_CONTENT, true, c -> accept0(c, true));
            ctx.data(DATA_MULTISET_CONDITION, true);
        }
        else
            ctx.data(DATA_MULTISET_CONTENT, true, c -> accept0(c, false));
    }






































    @SuppressWarnings("unchecked")
    private final void accept0(Context<?> ctx, boolean multisetCondition) {
        switch (emulateMultiset(ctx.configuration())) {
            case JSON: {
                List<Field<?>> fields = select.getSelect();
                Table<?> t = new AliasedSelect<>(select, true, false, FORCE_LIMIT_IN_DERIVED_TABLE.contains(ctx.dialect()), fieldNames(fields.size())).as(N_T, (Name[]) null);

                switch (ctx.family()) {








                    default: {
                        acceptDefaultJSONEmulation(ctx, multisetCondition, t, false);
                        break;
                    }
                }

                break;
            }

            case JSONB: {
                List<Field<?>> fields = select.getSelect();
                Table<?> t = new AliasedSelect<>(select, true, false, FORCE_LIMIT_IN_DERIVED_TABLE.contains(ctx.dialect()), fieldNames(fields.size())).as(N_T, (Name[]) null);

                switch (ctx.family()) {








                    default: {
                        acceptDefaultJSONEmulation(ctx, multisetCondition, t, true);
                        break;
                    }
                }

                break;
            }

            case XML: {
                List<Field<?>> fields = select.getSelect();
                Table<?> t = new AliasedSelect<>(select, true, false, FORCE_LIMIT_IN_DERIVED_TABLE.contains(ctx.dialect()), fieldNames(fields.size())).as(N_T, (Name[]) null);

                switch (ctx.family()) {


















                    default: {
                        if (NO_SUPPORT_CORRELATED_DERIVED_TABLE.contains(ctx.dialect()) && isSimple(select) && !select.$distinct()) {
                            List<Field<?>> l = map(select.getSelect(), f -> Tools.unalias(f));

                            acceptMultisetSubqueryForXMLEmulation(ctx, multisetCondition, (Select<Record1<XML>>)
                                select
                                .$select(asList(xmlelement(
                                    nResult(ctx),
                                    xmlaggEmulation(ctx, row(l), true)
                                        .orderBy(multisetCondition
                                            ? map(filter(l, f -> sortable(f)), f -> f.sortDefault())
                                            : select.$orderBy()
                                        )
                                )))
                                .$orderBy(asList())
                            );
                        }
                        else {
                            XMLAggOrderByStep<XML> order;
                            AggregateFilterStep<XML> filter;

                            filter = order = xmlaggEmulation(ctx, t, false);

                            // TODO: Re-apply derived table's ORDER BY clause as aggregate ORDER BY
                            if (multisetCondition)
                                filter = order.orderBy(t.fields());

                            acceptMultisetSubqueryForXMLEmulation(ctx, multisetCondition,
                                select(xmlelement(nResult(ctx), filter)).from(t)
                            );
                        }

                        break;
                    }
                }

                break;
            }

            case NATIVE:
                switch (ctx.family()) {
                    case DUCKDB: {
                        SelectOrderByStep<?> s = select(DSL.field(N_T)).from(select.asTable(N_T));
                        visitSubquery(ctx.visit(K_ARRAY), multisetCondition
                            ? s.orderBy(DSL.field(N_T))
                            : s
                        );
                        break;
                    }

                    default:
                        visitSubquery(ctx.visit(K_MULTISET), select);
                        break;
                }

                break;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final void acceptDefaultJSONEmulation(
        Context<?> ctx,
        boolean multisetCondition,
        Table<?> t,
        boolean jsonb
    ) {
        if (NO_SUPPORT_CORRELATED_DERIVED_TABLE.contains(ctx.dialect()) && isSimple(select)) {
            List<Field<?>> l = map(select.getSelect(), f -> Tools.unalias(f));

            JSONArrayAggReturningStep<?> returning =
                (jsonb ? jsonbArrayaggEmulation(ctx, row(l), true, select.$distinct()) : jsonArrayaggEmulation(ctx, row(l), true, select.$distinct()))
                    .orderBy(multisetCondition
                        ? map(filter(l, f -> sortable(f)), f -> f.sortDefault())
                        : select.$orderBy()
                    );

            Select<?> s = select
                .$select(Arrays.asList(DSL.coalesce(
                    returningClob(ctx, returning),
                    returningClob(ctx, (JSONArrayReturningStep<?>) (jsonb ? jsonbArray() : jsonArray()))
                )))
                .$distinct(false)
                .$orderBy(Arrays.asList());

            visitSubquery(ctx, s);
        }
        else {
            JSONArrayAggOrderByStep<?> order;
            JSONArrayAggReturningStep<?> returning;

            returning = order = jsonb ? jsonbArrayaggEmulation(ctx, t, true, false) : jsonArrayaggEmulation(ctx, t, true, false);

            // TODO: Re-apply derived table's ORDER BY clause as aggregate ORDER BY
            if (multisetCondition)
                returning = order.orderBy(t.fields());
            else if (NO_SUPPORT_DERIVED_TABLE_ORDERING.contains(ctx.dialect()))
                returning = orderBy(order);

            Select<? extends Record1<?>> s = patchOracleArrayAggBug(
                ctx,
                select(DSL.coalesce(
                    returningClob(ctx, returning),
                    returningClob(ctx, (JSONArrayReturningStep<?>) (jsonb ? jsonbArray() : jsonArray()))
                )).from(t)
            );

            if (multisetCondition && (jsonb ? NO_SUPPORT_JSONB_COMPARE : NO_SUPPORT_JSON_COMPARE).contains(ctx.dialect()))
                ctx.visit(DSL.field((Select) s).cast(VARCHAR));
            else
                visitSubquery(ctx, s);
        }
    }

    private final JSONArrayAggReturningStep<?> orderBy(JSONArrayAggOrderByStep<?> order) {
        if (!select.$orderBy().isEmpty()) {
            List<Field<?>> s = select.getSelect();
            List<Field<?>> u = unaliasedFields(s);

            if (allMatch(select.$orderBy(), o -> s.contains(o.$field()) || u.contains(o.$field()))) {
                return order.orderBy(map(select.$orderBy(), o -> {
                    int i;

                    if ((i = s.indexOf(o.$field())) > -1)
                        return DSL.field(fieldName(i)).sort(o.$sortOrder());
                    else if ((i = u.indexOf(o.$field())) > -1)
                        return DSL.field(fieldName(i)).sort(o.$sortOrder());
                    else
                        return o;
                }));
            }
        }

        return order;
    }





























    private static final void acceptMultisetSubqueryForXMLEmulation(Context<?> ctx, boolean multisetCondition, Select<Record1<XML>> s) {
        if (multisetCondition && NO_SUPPORT_XML_COMPARE.contains(ctx.dialect()))
            ctx.visit(xmlserializeContent(DSL.field(s), VARCHAR));
        else
            visitSubquery(ctx, s);
    }

    // [#12045] Only simple selects can profit from the simplified MULTISET emulation
    private static final boolean isSimple(Select<?> s) {
        return s.$groupBy().isEmpty()
            && s.$having() == null
            && s.$window().isEmpty()
            && s.$qualify() == null
            && !selectQueryImpl(s).hasUnions()
            && s.$offset() == null
            && s.$limit() == null
        ;
    }

    static final Name nResult(Scope ctx) {
        switch (ctx.family()) {




            default:
                return N_RESULT;
        }
    }

    static final Name xsiNil(Context<?> ctx) {
        switch (ctx.family()) {





            // [#18728] Always quote this name because of the : character
            default:
                return DSL.quotedName("xsi:nil");
        }
    }

    static final <J> Field<J> returningClob(Scope ctx, JSONObjectReturningStep<J> j) {
        switch (ctx.family()) {








            default:
                return j;
        }
    }

    static final <J> Field<J> returningClob(Scope ctx, JSONArrayReturningStep<J> j) {
        switch (ctx.family()) {








            default:
                return j;
        }
    }

    static final <J> Field<J> returningClob(Scope ctx, JSONArrayAggReturningStep<J> j) {
        switch (ctx.family()) {








            default:
                return j;
        }
    }

    // The emulations use the less intuitive JSONFormat.RecordFormat.ARRAY encoding:
    // - It uses less bandwidth
    // - It is column name agnostic (supporting ambiguous column names)
    // - The JSON never leaks outside of the emulation into user code

    static final JSONArrayAggOrderByStep<JSON> jsonArrayaggEmulation(Context<?> ctx, Fields fields, boolean agg, boolean distinct) {
        return jsonxArrayaggEmulation(ctx, fields, agg, distinct ? DSL::jsonArrayAggDistinct : DSL::jsonArrayAgg, DSL::jsonObject, DSL::jsonArray);
    }

    static final JSONArrayAggOrderByStep<JSONB> jsonbArrayaggEmulation(Context<?> ctx, Fields fields, boolean agg, boolean distinct) {
        return jsonxArrayaggEmulation(ctx, fields, agg, distinct ? DSL::jsonbArrayAggDistinct : DSL::jsonbArrayAgg, DSL::jsonbObject, DSL::jsonbArray);
    }

    static final <J> JSONArrayAggOrderByStep<J> jsonxArrayaggEmulation(
        Context<?> ctx,
        Fields fields,
        boolean agg,
        Function<? super Field<?>, ? extends JSONArrayAggOrderByStep<J>> jsonxArrayAgg,
        Function<? super Collection<? extends JSONEntry<?>>, ? extends JSONObjectNullStep<J>> jsonxObject,
        Function<? super Collection<? extends Field<?>>, ? extends JSONArrayNullStep<J>> jsonxArray
    ) {
        switch (ctx.family()) {













            default:
                return jsonxArrayAgg.apply(
                    returningClob(ctx, jsonxArray.apply(
                        map(fields.fields(), (f, i) -> JSONEntryImpl.unescapeNestedJSON(ctx,
                            castForJSON(ctx, agg ? f : DSL.field(fieldName(i), f.getDataType()))
                        ))
                    ).nullOnNull())
                );
        }
    }

    @SuppressWarnings("unchecked")
    static final Field<?> castForJSON(Context<?> ctx, Field<?> field) {
        DataType<?> t = field.getDataType();

        // [#10880] [#17067] Many dialects don't support NaN and other float values in JSON documents as numbers
        if (t.isFloat()) {
            switch (ctx.family()) {

                case H2:
                    return field.cast(VARCHAR);
            }
        }

        // [#18955] MySQL does this out of the box, but MariaDB doesn't
        else if (t.isBinary()) {
            switch (ctx.family()) {
                case MARIADB:
                    return function(Names.N_TO_BASE64, CLOB, field);
            }
        }





























        return field;
    }

    @SuppressWarnings("unchecked")
    static final Field<?> castForXML(Context<?> ctx, Field<?> field) {














        return field;
    }

    static final XMLAggOrderByStep<XML> xmlaggEmulation(Context<?> ctx, Fields fields, boolean agg) {
        return xmlagg(
            xmlelement(N_RECORD,
                map(fields.fields(), (f, i) -> {
                    Field<?> v = castForXML(ctx, agg ? f : DSL.field(fieldName(i), f.getDataType()));
                    String n = fieldNameString(i);
                    return wrapXmlelement(ctx, v, n);
                })
            )
        );
    }

    static final Field<XML> wrapXmlelement(Context<?> ctx, Field<?> v, String n) {
        DataType<?> t = v.getDataType();

        // [#13181] We must make the '' vs NULL distinction explicit in XML
        // [#13872] Same with ARRAY[] vs NULL
        if (t.isString() || t.isArray())
            return xmlelement(n, xmlattributes(when(v.isNull(), inline("true")).as(xsiNil(ctx))), v);
        else
            return xmlelement(n, v);
    }

    static final ArrayAggOrderByStep<?> arrayAggEmulation(Fields fields, boolean agg) {
        return arrayAgg(
            new RowAsField<>(row(
                map(fields.fields(), (f, i) -> agg ? f : DSL.field(fieldName(i), f.getDataType()))
            ))
        );
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final TableLike<R> $table() {
        return table;
    }














}
