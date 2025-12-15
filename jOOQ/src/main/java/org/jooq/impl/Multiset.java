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
import static java.util.Collections.emptyList;
// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.arrayAgg;
import static org.jooq.impl.DSL.arrayGet;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonbArray;
import static org.jooq.impl.DSL.jsonbArrayAgg;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.unnest;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlattributes;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlserializeContent;
import static org.jooq.impl.DerivedTable.NO_SUPPORT_CORRELATED_DERIVED_TABLE_FOR_MULTISET;
import static org.jooq.impl.JSONArrayAgg.patchOracleArrayAggBug;
import static org.jooq.impl.Keywords.K_ARRAY;
import static org.jooq.impl.Keywords.K_MULTISET;
import static org.jooq.impl.Names.NQ_RESULT;
import static org.jooq.impl.Names.N_ELEMENT;
import static org.jooq.impl.Names.N_JSON_QUERY;
import static org.jooq.impl.Names.N_MULTISET;
import static org.jooq.impl.Names.N_O;
import static org.jooq.impl.Names.N_RECORD;
import static org.jooq.impl.Names.N_RESULT;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.Names.N_TO_JSON_STRING;
import static org.jooq.impl.Names.N_U;
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
import static org.jooq.impl.Tools.isVal;
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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.AggregateFilterStep;
import org.jooq.ArrayAggOrderByStep;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.Formatter;
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
import org.jooq.Param;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
// ...
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SQLDialectCategory;
import org.jooq.Scope;
import org.jooq.Select;
// ...
import org.jooq.SelectForStep;
import org.jooq.SelectOrderByStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
// ...
import org.jooq.UDTPathField;
import org.jooq.XML;
import org.jooq.XMLAggOrderByStep;
import org.jooq.conf.NestedCollectionEmulation;

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
                        if (NO_SUPPORT_CORRELATED_DERIVED_TABLE_FOR_MULTISET.contains(ctx.dialect()) && isSimple(select) && !select.$distinct()) {
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
        if (NO_SUPPORT_CORRELATED_DERIVED_TABLE_FOR_MULTISET.contains(ctx.dialect()) && isSimple(select)) {
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

            if (multisetCondition && (jsonb ? NO_SUPPORT_JSONB_COMPARE : NO_SUPPORT_JSON_COMPARE).contains(ctx.dialect())) {





                ctx.visit(DSL.field((Select) s).cast(VARCHAR));
            }
            else
                visitSubquery(ctx, s);
        }
    }

    private final JSONArrayAggReturningStep<?> orderBy(JSONArrayAggOrderByStep<?> order) {
        if (!select.$orderBy().isEmpty()) {
            List<Field<?>> s = select.getSelect();
            List<Field<?>> u = unaliasedFields(s);

            if (allMatch(select.$orderBy(), o ->
                s.contains(o.$field()) ||
                u.contains(o.$field()) ||
                Tools.isVal(o.$field())
            )) {
                return order.orderBy(map(select.$orderBy(), o -> {
                    int i;

                    if (isVal(o.$field()))
                        return DSL.field(fieldName(Convert.convert(((Param<?>) o.$field()).getValue(), int.class) - 1)).sort(o.$sortOrder());
                    else if ((i = s.indexOf(o.$field())) > -1)
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

    static final Field<?> castForJSON(Context<?> ctx, Field<?> field) {
        Formatter f;

        if ((f = field.getBinding().formatter()) != null) {
            DefaultFormatterContext fctx = new DefaultFormatterContext(ctx, true, field);

            if (emulateMultiset(ctx.configuration()) == NestedCollectionEmulation.JSONB)
                f.formatJSONB(fctx);
            else
                f.formatJSON(fctx);

            return fctx.formatted;
        }

        else if (field.getDataType().isUDTRecord() || field.getDataType().isArray()) {
            return toJSONArrays(ctx, field);
        }

        return field;
    }

    static final Field<?> castForXML(Context<?> ctx, Field<?> field) {
        Formatter f;

        if ((f = field.getBinding().formatter()) != null) {
            DefaultFormatterContext fctx = new DefaultFormatterContext(ctx, true, field);
            f.formatXML(fctx);
            return fctx.formatted;
        }

        else if (field.getDataType().isUDTRecord() || field.getDataType().isArray()) {
            return toXMLElements(ctx, field);
        }

        return field;
    }

    private static final Field<?> transformNestedTypes(
        Field<?> field,
        Function<? super UDTDataType<?>, ? extends Field<?>> arrayOfUDT,
        Supplier<? extends Field<?>> arrayOfFormatted,
        Supplier<? extends Field<?>> arrayOfUnformatted,
        BiFunction<? super UDTDataType<?>, ? super UDTPathField<?, ?, ?>, ? extends Field<?>> udt
    ) {
        DataType<?> t = field.getDataType();

        if (t.isArray()) {
            DataType<?> ct = t.getArrayComponentDataType();

            // [#19067] This currently applies to arrays of UDTs only. Let's not complicate things for ordinary arrays.
            if (ConvertedDataType.delegate(ct) instanceof UDTDataType<?> ut)
                return arrayOfUDT.apply(ut);
            else if (ct.getBinding().formatter() != null)
                return arrayOfFormatted.get();
            else
                return arrayOfUnformatted.get();
        }
        else if (ConvertedDataType.delegate(t) instanceof UDTDataType<?> ut) {
            UDTPathField<?, ?, ?> up;

            if (field instanceof UDTPathField<?, ?, ?> u)
                up = u;
            else if (field instanceof TableField<?, ?> tf)
                up = new UDTPathFieldImpl<>(field.getUnqualifiedName(), t, tf.getTable(), ut.udt, null);
            else
                up = null;

            if (up != null)
                return udt.apply(ut, up);
        }

        return field;
    }

    private static final JSONArrayAggOrderByStep<?> jsonxArrayAgg(Context<?> ctx, Field<?> field) {
        switch (emulateMultiset(ctx.configuration())) {
            case JSON:
                return jsonArrayAgg(field);
            case JSONB:
                return jsonbArrayAgg(field);
            default:
                throw new IllegalStateException();
        }
    }

    private static final JSONArrayNullStep<?> jsonxArray(Context<?> ctx, List<? extends Field<?>> fields) {
        switch (emulateMultiset(ctx.configuration())) {
            case JSON:
                return DSL.jsonArray(fields);
            case JSONB:
                return DSL.jsonbArray(fields);
            default:
                throw new IllegalStateException();
        }
    }

    private static final Field<?> toJSONArrays(Context<?> ctx, Field<?> field) {
        boolean transform = false;

        switch (ctx.family()) {


            case POSTGRES:
            case YUGABYTEDB:
                transform = hasFormattedTypes(field.getDataType());
                break;
        }

        return !transform ? field : transformNestedTypes(
            field,
            toJSONArrayOfUDTs(ctx, field),
            toJSONArrayOfFormatted(ctx, field),
            () -> field,
            toJSONUDT(ctx, field)
        );
    }

    private static final boolean hasFormattedTypes(DataType<?> t) {
        if (t.isArray()) {
            DataType<?> ct = t.getArrayComponentDataType();

            // [#19067] This currently applies to arrays of UDTs only. Let's not complicate things for ordinary arrays.
            if (ConvertedDataType.delegate(ct) instanceof UDTDataType<?> ut)
                return hasFormattedTypes(ut);
            else
                return ct.getBinding().formatter() != null;
        }
        else if (ConvertedDataType.delegate(t) instanceof UDTDataType<?> ut) {
            for (Field<?> f : ut.getRow().fields())
                if (hasFormattedTypes(f.getDataType()))
                    return true;
        }

        return false;
    }

    private static final Function<? super UDTDataType<?>, ? extends Field<?>> toJSONArrayOfUDTs(Context<?> ctx, Field<?> field) {
        return ut -> {
            DataType<Object[]> t = (DataType<Object[]>) field.getDataType();
            Field<Integer> o = DSL.field(N_O, INTEGER);

            return when(udtNotNull(ctx, field),
                DSL.field(
                    select(DSL.coalesce(
                        jsonxArrayAgg(ctx,

                            // [#19067] Both NULL::UDT and (NULL, NULL)::UDT unnest as (NULL, NULL), so we need to distinguish
                            //          the cases a bit more laboriously
                            when(
                                udtNotNull(ctx, arrayGet(DSL.field(N_T, t), o)),
                                jsonxArray(ctx, map(ut.getRow().fields(),
                                    (f, i) -> castForJSON(ctx, f)
                                ))
                            )
                        ).orderBy(o),
                        jsonxArray(ctx, emptyList())
                    ))
                    .from(
                        values(row(field)).as(N_T, N_T),
                        unnest(DSL.field(N_T, t))
                            .withOrdinality()
                            .as(table(N_U), Tools.concat(asList(ut.getRow().fields()), asList(o)))
                    )
                )
            );
        };
    }

    private static final Supplier<Field<?>> toJSONArrayOfFormatted(Context<?> ctx, Field<?> field) {
        return () -> when(field.isNotNull(),
            DSL.field(
                select(jsonxArrayAgg(ctx,
                    when(
                        DSL.field(N_T).isNotNull(),
                        castForJSON(ctx, DSL.field(N_T, field.getDataType().getArrayComponentDataType()))
                    )
                ))
                .from(unnest(field).as(N_T, N_T))
            )
        );
    }

    private static final BiFunction<? super UDTDataType<?>, ? super UDTPathField<?, ?, ?>, ? extends Field<?>> toJSONUDT(
        Context<?> ctx,
        Field<?> field
    ) {
        return (ut, up) -> when(udtNotNull(ctx, field), jsonArray(
            map(field.getDataType().getRow().fields(),
                (f, i) -> castForJSON(ctx,
                    new UDTPathFieldImpl<>(
                        f.getUnqualifiedName(), f.getDataType(), up.asQualifier(), ut.udt, null
                    )
                )
            )
        ));
    }

    private static final Field<?> toXMLElements(Context<?> ctx, Field<?> field) {
        return transformNestedTypes(
            field,
            toXMLArrayOfUDT(ctx, field),
            toXMLElementsArrayOfFormatted(ctx, field),
            toXMLElementsArrayOfFormatted(ctx, field),
            toXMLUDT(ctx, field)
        );
    }

    @SuppressWarnings("unchecked")
    private static final Function<? super UDTDataType<?>, ? extends Field<?>> toXMLArrayOfUDT(Context<?> ctx, Field<?> field) {
        return ut -> {
            DataType<Object[]> t = (DataType<Object[]>) field.getDataType();
            Field<Integer> o = DSL.field(N_O, INTEGER);

            return when(udtNotNull(ctx, field),
                DSL.field(
                    select(xmlelement(nResult(ctx), xmlagg(

                        // [#19067] Both NULL::UDT and (NULL, NULL)::UDT unnest as (NULL, NULL), so we need to distinguish
                        //          the cases a bit more laboriously
                        when(
                            udtNotNull(ctx, arrayGet(DSL.field(N_T, t), o)),
                            xmlelement(N_RECORD, map(ut.getRow().fields(),
                                (f, i) -> wrapXmlelement(
                                    ctx,
                                    fieldName(i),
                                    castForXML(ctx, f),
                                    f
                                )
                            ))
                        )
                        .else_(xmlelement(N_RECORD, xmlattributes(inline("true").as(xsiNil(ctx)))))
                    ).orderBy(o)))
                    .from(
                        values(row(field)).as(N_T, N_T),
                        unnest(DSL.field(N_T, t))
                            .withOrdinality()
                            .as(table(N_U), Tools.concat(asList(ut.getRow().fields()), asList(o)))
                    )
                )
            );
        };
    }

    private static final BiFunction<? super UDTDataType<?>, ? super UDTPathField<?, ?, ?>, ? extends Field<?>> toXMLUDT(
        Context<?> ctx,
        Field<?> field
    ) {
        return (ut, up) -> when(udtNotNull(ctx, field), xmlelement(N_RECORD,
            map(field.getDataType().getRow().fields(),
                (f, i) -> {
                    Field<?> up2 = new UDTPathFieldImpl<>(
                        f.getUnqualifiedName(), f.getDataType(), up.asQualifier(), ut.udt, null
                    );

                    return wrapXmlelement(
                        ctx,
                        fieldName(i),
                        castForXML(ctx, up2),
                        up2
                    );
                }
            )
        ));
    }

    private static final Supplier<Field<?>> toXMLElementsArrayOfFormatted(Context<?> ctx, Field<?> field) {
        return () -> when(field.isNotNull(),
            DSL.field(
                select(xmlagg(
                    when(
                        DSL.field(N_T).isNotNull(),
                        wrapXmlelement(
                            ctx,
                            N_ELEMENT,
                            castForXML(ctx, DSL.field(N_T, field.getDataType().getArrayComponentDataType()))
                        )
                    )
                    .else_(xmlelement(N_ELEMENT, xmlattributes(inline("true").as(xsiNil(ctx)))))
                ))
                .from(unnest(field).as(N_T, N_T))
            )
        );
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final Condition udtNotNull(Context<?> ctx, Field<?> field) {

        // [#13872] UDTs are rows and (NULL, NULL) IS NULL is TRUE but (NULL, NULL) IS DISTINCT FROM NULL is FALSE.
        switch (ctx.family()) {


            case POSTGRES:
            case YUGABYTEDB:
                return field.isDistinctFrom((Field) inline(null, field.getDataType()));

            default:
                return field.isNotNull();
        }
    }

    static final XMLAggOrderByStep<XML> xmlaggEmulation(Context<?> ctx, Fields fields, boolean agg) {
        return xmlagg(
            xmlelement(N_RECORD,
                map(fields.fields(), (f, i) -> wrapXmlelement(
                    ctx,
                    fieldName(i),
                    castForXML(ctx, agg ? f : DSL.field(N_T.append(fieldName(i)), f.getDataType())),
                    f
                ))
            )
        );
    }

    static final Field<XML> wrapXmlelement(
        Context<?> ctx,
        Name name,
        Field<?> xmlContent
    ) {
        return wrapXmlelement(ctx, name, xmlContent, xmlContent);
    }

    static final Field<XML> wrapXmlelement(
        Context<?> ctx,
        Name name,
        Field<?> xmlContent,
        Field<?> nullCheck
    ) {
        DataType<?> t1 = xmlContent.getDataType();
        DataType<?> t2 = nullCheck.getDataType();

        // [#13181] We must make the '' vs NULL distinction explicit in XML
        // [#13872] Same with ARRAY[] vs NULL
        if (t1.isString() || t1.isArray() ||
            t2.isString() || t2.isArray()
        )
            return xmlelement(name, xmlattributes(when(nullCheck.isNull(), inline("true")).as(xsiNil(ctx))), xmlContent);
        else
            return xmlelement(name, xmlContent);
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
