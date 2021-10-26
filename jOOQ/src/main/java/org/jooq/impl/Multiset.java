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

import static java.lang.Boolean.TRUE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonbArray;
import static org.jooq.impl.DSL.jsonbArrayAgg;
import static org.jooq.impl.DSL.jsonbObject;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlserializeContent;
import static org.jooq.impl.JSONArrayAgg.patchOracleArrayAggBug;
import static org.jooq.impl.Keywords.K_MULTISET;
import static org.jooq.impl.Names.N_MULTISET;
import static org.jooq.impl.Names.N_RECORD;
import static org.jooq.impl.Names.N_RESULT;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.CLOB;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.fieldName;
import static org.jooq.impl.Tools.fieldNameString;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitSubquery;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONDITION;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;

import java.util.List;
import java.util.Set;

import org.jooq.AggregateFilterStep;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.JSON;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.JSONArrayAggReturningStep;
import org.jooq.JSONArrayReturningStep;
import org.jooq.JSONB;
import org.jooq.JSONObjectReturningStep;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.XML;
import org.jooq.XMLAggOrderByStep;

/**
 * @author Lukas Eder
 */
final class Multiset<R extends Record> extends AbstractField<Result<R>> {

    static final Set<SQLDialect> NO_SUPPORT_JSON_COMPARE  = SQLDialect.supportedBy(POSTGRES);
    static final Set<SQLDialect> NO_SUPPORT_JSONB_COMPARE = SQLDialect.supportedBy();
    static final Set<SQLDialect> NO_SUPPORT_XML_COMPARE   = SQLDialect.supportedBy(POSTGRES);

    final Select<R>              select;

    @SuppressWarnings("unchecked")
    Multiset(Select<R> select) {
        // [#12100] Can't use select.fieldsRow() here.
        super(N_MULTISET, new MultisetDataType<>((AbstractRow<R>) DSL.row(select.getSelect()), select.getRecordType()));

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

    private final void accept0(Context<?> ctx, boolean multisetCondition) {
        switch (emulateMultiset(ctx.configuration())) {
            case JSON: {
                Table<?> t = new AliasedSelect<>(select, true, false, fieldNames(select.getSelect().size())).as(DSL.name("t"), (Name[]) null);

                switch (ctx.family()) {









                    default: {
                        JSONArrayAggOrderByStep<JSON> order;
                        JSONArrayAggReturningStep<JSON> returning;

                        returning = order = jsonArrayaggEmulation(ctx, select, false);

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
                            visitSubquery(ctx, s, true);

                        break;
                    }
                }

                break;
            }

            case JSONB: {
                Table<?> t = new AliasedSelect<>(select, true, false, fieldNames(select.getSelect().size())).as(DSL.name("t"), (Name[]) null);

                switch (ctx.family()) {









                    default: {
                        JSONArrayAggOrderByStep<JSONB> order;
                        JSONArrayAggReturningStep<JSONB> returning;

                        returning = order = jsonbArrayaggEmulation(ctx, select, false);

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
                            visitSubquery(ctx, s, true);

                        break;
                    }
                }

                break;
            }

            case XML: {
                List<Field<?>> fields = select.getSelect();
                Table<?> t = new AliasedSelect<>(select, true, false, fieldNames(fields.size())).as(DSL.name("t"), (Name[]) null);

                switch (ctx.family()) {


















                    default: {
                        XMLAggOrderByStep<XML> order;
                        AggregateFilterStep<XML> filter;

                        filter = order = xmlaggEmulation(select, false);

                        // TODO: Re-apply derived table's ORDER BY clause as aggregate ORDER BY
                        if (multisetCondition)
                            filter = order.orderBy(t.fields());

                        Select<Record1<XML>> s = select(xmlelement(N_RESULT, filter)).from(t);

                        if (multisetCondition && NO_SUPPORT_XML_COMPARE.contains(ctx.dialect()))
                            ctx.visit(xmlserializeContent(DSL.field(s), VARCHAR));
                        else
                            visitSubquery(ctx, s, true);

                        break;
                    }
                }

                break;
            }

            case NATIVE:
                visitSubquery(ctx.visit(K_MULTISET), select, true);
                break;
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

    static final JSONArrayAggOrderByStep<JSON> jsonArrayaggEmulation(Scope ctx, Fields fields, boolean agg) {
        switch (ctx.family()) {













            default:
                return jsonArrayAgg(
                    returningClob(ctx, jsonArray(
                        map(fields.fields(), (f, i) -> JSONEntryImpl.unescapeNestedJSON(ctx,
                            (Field<?>) (agg ? f : DSL.field(fieldName(i), f.getDataType()))
                        ))
                    ).nullOnNull())
                );
        }
    }

    static final JSONArrayAggOrderByStep<JSONB> jsonbArrayaggEmulation(Scope ctx, Fields fields, boolean agg) {
        switch (ctx.family()) {













            default:
                return jsonbArrayAgg(
                    returningClob(ctx, jsonbArray(
                        map(fields.fields(), (f, i) -> JSONEntryImpl.unescapeNestedJSON(ctx,
                            (Field<?>) (agg ? f : DSL.field(fieldName(i), f.getDataType()))
                        ))
                    ).nullOnNull())
                );
        }
    }

    static final XMLAggOrderByStep<XML> xmlaggEmulation(Fields fields, boolean agg) {
        return xmlagg(
            xmlelement(N_RECORD,
                map(fields.fields(), (f, i) -> xmlelement(
                    fieldNameString(i),
                    agg ? f : DSL.field(fieldName(i), f.getDataType())
                ))
            )
        );
    }
}
