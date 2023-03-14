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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.*;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonbArray;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
// ...
// ...
import static org.jooq.impl.DSL.toHex;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlattributes;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlserializeContent;
import static org.jooq.impl.DerivedTable.NO_SUPPORT_CORRELATED_DERIVED_TABLE;
import static org.jooq.impl.JSONArrayAgg.patchOracleArrayAggBug;
import static org.jooq.impl.Keywords.K_MULTISET;
import static org.jooq.impl.Names.NQ_RESULT;
import static org.jooq.impl.Names.N_HEX;
import static org.jooq.impl.Names.N_JSON_QUERY;
import static org.jooq.impl.Names.N_MULTISET;
import static org.jooq.impl.Names.N_RECORD;
import static org.jooq.impl.Names.N_RESULT;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.CLOB;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.fieldName;
import static org.jooq.impl.Tools.fieldNameString;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.impl.Tools.visitSubquery;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONDITION;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.jooq.AggregateFilterStep;
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
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
// ...
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
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

    static final Set<SQLDialect> NO_SUPPORT_JSON_COMPARE      = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect> NO_SUPPORT_JSONB_COMPARE     = SQLDialect.supportedBy();
    static final Set<SQLDialect> NO_SUPPORT_XML_COMPARE       = SQLDialect.supportedBy(POSTGRES);
    static final Set<SQLDialect> FORCE_LIMIT_IN_DERIVED_TABLE = SQLDialect.supportedBy(MARIADB, MYSQL, TRINO);

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
                Table<?> t = new AliasedSelect<>(select, true, false, FORCE_LIMIT_IN_DERIVED_TABLE.contains(ctx.dialect()), fieldNames(fields.size())).as(DSL.name("t"), (Name[]) null);

                switch (ctx.family()) {



















                    default: {
                        if (NO_SUPPORT_CORRELATED_DERIVED_TABLE.contains(ctx.dialect()) && isSimple(select)) {
                            JSONArrayAggReturningStep<JSON> returning =
                                jsonArrayaggEmulation(ctx, row(map(select.getSelect(), f -> Tools.unalias(f))), true, select.$distinct()).orderBy(select.$orderBy());

                            Select<?> s = select
                                .$select(Arrays.asList(DSL.coalesce(
                                    returningClob(ctx, returning),
                                    returningClob(ctx, jsonArray())
                                )))
                                .$distinct(false)
                                .$orderBy(Arrays.asList());

                            visitSubquery(ctx, s);
                        }
                        else {
                            JSONArrayAggOrderByStep<JSON> order;
                            JSONArrayAggReturningStep<JSON> returning;

                            returning = order = jsonArrayaggEmulation(ctx, t, true, false);

                            // TODO: Re-apply derived table's ORDER BY clause as aggregate ORDER BY
                            if (multisetCondition)
                                returning = order.orderBy(t.fields());

                            Select<Record1<JSON>> s = patchOracleArrayAggBug(
                                ctx,
                                select(DSL.coalesce(
                                    returningClob(ctx, returning),
                                    returningClob(ctx, jsonArray())
                                )).from(t)
                            );

                            if (multisetCondition && NO_SUPPORT_JSON_COMPARE.contains(ctx.dialect()))
                                ctx.visit(DSL.field(s).cast(VARCHAR));
                            else
                                visitSubquery(ctx, s);
                        }

                        break;
                    }
                }

                break;
            }

            case JSONB: {
                List<Field<?>> fields = select.getSelect();
                Table<?> t = new AliasedSelect<>(select, true, false, FORCE_LIMIT_IN_DERIVED_TABLE.contains(ctx.dialect()), fieldNames(fields.size())).as(DSL.name("t"), (Name[]) null);

                switch (ctx.family()) {




















                    default: {
                        if (NO_SUPPORT_CORRELATED_DERIVED_TABLE.contains(ctx.dialect()) && isSimple(select)) {
                            JSONArrayAggReturningStep<JSONB> returning =
                                jsonbArrayaggEmulation(ctx, row(map(select.getSelect(), f -> Tools.unalias(f))), true, select.$distinct()).orderBy(select.$orderBy());

                            Select<?> s = select
                                .$select(Arrays.asList(DSL.coalesce(
                                    returningClob(ctx, returning),
                                    returningClob(ctx, jsonbArray())
                                )))
                                .$distinct(false)
                                .$orderBy(Arrays.asList());

                            visitSubquery(ctx, s);
                        }
                        else {
                            JSONArrayAggOrderByStep<JSONB> order;
                            JSONArrayAggReturningStep<JSONB> returning;

                            returning = order = jsonbArrayaggEmulation(ctx, t, false, false);

                            // TODO: Re-apply derived table's ORDER BY clause as aggregate ORDER BY
                            if (multisetCondition)
                                returning = order.orderBy(t.fields());

                            Select<Record1<JSONB>> s = patchOracleArrayAggBug(
                                ctx,
                                select(DSL.coalesce(
                                    returningClob(ctx, returning),
                                    returningClob(ctx, jsonbArray())
                                )).from(t)
                            );

                            if (multisetCondition && NO_SUPPORT_JSONB_COMPARE.contains(ctx.dialect()))
                                ctx.visit(DSL.field(s).cast(VARCHAR));
                            else
                                visitSubquery(ctx, s);
                        }

                        break;
                    }
                }

                break;
            }

            case XML: {
                List<Field<?>> fields = select.getSelect();
                Table<?> t = new AliasedSelect<>(select, true, false, FORCE_LIMIT_IN_DERIVED_TABLE.contains(ctx.dialect()), fieldNames(fields.size())).as(DSL.name("t"), (Name[]) null);

                switch (ctx.family()) {


















                    default: {
                        if (NO_SUPPORT_CORRELATED_DERIVED_TABLE.contains(ctx.dialect()) && isSimple(select) && !select.$distinct()) {
                            acceptMultisetSubqueryForXMLEmulation(ctx, multisetCondition, (Select<Record1<XML>>)
                                select
                                .$select(asList(xmlelement(
                                    nResult(ctx),
                                    xmlaggEmulation(ctx, row(map(select.getSelect(), f -> Tools.unalias(f))), true).orderBy(select.$orderBy())
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
                visitSubquery(ctx.visit(K_MULTISET), select);
                break;
        }
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




            default:
                return DSL.name("xsi:nil");
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
                    DataType<?> t = v.getDataType();

                    // [#13181] We must make the '' vs NULL distinction explicit in XML
                    // [#13872] Same with ARRAY[] vs NULL
                    if (t.isString() || t.isArray())
                        return xmlelement(n, xmlattributes(when(v.isNull(), inline("true")).as(xsiNil(ctx))), v);
                    else
                        return xmlelement(n, v);
                })
            )
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
