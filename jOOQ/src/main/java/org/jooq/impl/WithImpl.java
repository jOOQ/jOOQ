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

import static org.jooq.Clause.WITH;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Keywords.K_RECURSIVE;
import static org.jooq.impl.Keywords.K_WITH;
import static org.jooq.impl.Tools.EMPTY_NAME;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_TOP_LEVEL_CTE;
import static org.jooq.impl.Transformations.transformInlineCTE;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.Clause;
import org.jooq.CommonTableExpression;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DerivedColumnList;
import org.jooq.Field;
import org.jooq.InsertSetStep;
import org.jooq.MergeUsingStep;
import org.jooq.Name;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Record13;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record16;
import org.jooq.Record17;
import org.jooq.Record18;
import org.jooq.Record19;
import org.jooq.Record2;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
// ...
import org.jooq.ResultQuery;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SelectSelectStep;
import org.jooq.SelectWhereStep;
import org.jooq.Table;
import org.jooq.TableLike;
// ...
import org.jooq.WithAsStep;
import org.jooq.WithAsStep1;
import org.jooq.WithAsStep10;
import org.jooq.WithAsStep11;
import org.jooq.WithAsStep12;
import org.jooq.WithAsStep13;
import org.jooq.WithAsStep14;
import org.jooq.WithAsStep15;
import org.jooq.WithAsStep16;
import org.jooq.WithAsStep17;
import org.jooq.WithAsStep18;
import org.jooq.WithAsStep19;
import org.jooq.WithAsStep2;
import org.jooq.WithAsStep20;
import org.jooq.WithAsStep21;
import org.jooq.WithAsStep22;
import org.jooq.WithAsStep3;
import org.jooq.WithAsStep4;
import org.jooq.WithAsStep5;
import org.jooq.WithAsStep6;
import org.jooq.WithAsStep7;
import org.jooq.WithAsStep8;
import org.jooq.WithAsStep9;
import org.jooq.WithStep;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.QOM.With;

import org.jetbrains.annotations.NotNull;

/**
 * This type models an intermediary DSL construction step, which leads towards
 * creating any of the other 5 DML statement types.
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class WithImpl extends AbstractQueryPart
implements
    WithAsStep,


    WithAsStep1,
    WithAsStep2,
    WithAsStep3,
    WithAsStep4,
    WithAsStep5,
    WithAsStep6,
    WithAsStep7,
    WithAsStep8,
    WithAsStep9,
    WithAsStep10,
    WithAsStep11,
    WithAsStep12,
    WithAsStep13,
    WithAsStep14,
    WithAsStep15,
    WithAsStep16,
    WithAsStep17,
    WithAsStep18,
    WithAsStep19,
    WithAsStep20,
    WithAsStep21,
    WithAsStep22,



    WithStep,
    With
{
    private static final Clause[]                                                     CLAUSES              = { WITH };







    final CommonTableExpressionList                                                   ctes;
    final boolean                                                                     recursive;
    private Configuration                                                             configuration;

    // Intermediary properties for CTE construction

    private transient Name                                                            alias;
    private transient Name[]                                                          fieldAliases;
    private transient BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction;

    WithImpl(Configuration configuration, boolean recursive) {
        this.configuration = configuration;
        this.recursive = recursive;
        this.ctes = new CommonTableExpressionList();
    }

    static final WithImpl merge(WithImpl w1, WithImpl w2) {
        if (w1 == null)
            return w2;
        else if (w2 == null)
            return w1;
        else
            return new WithImpl(w1.configuration(), w1.recursive || w2.recursive).with(w1.ctes).with(w2.ctes);
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        if (transformInlineCTE(ctx.configuration()))
            return;

        CommonTableExpressionList list;












        list = ctes;

        if (!list.isEmpty()) {











            acceptWithRecursive(ctx, recursive);

            ctx.data(DATA_LIST_ALREADY_INDENTED, true, c1 ->
                c1.formatIndentStart()
                  .formatSeparator()
                  .declareCTE(true, c2 -> c2.visit(list))
                  .formatIndentEnd()
                  .formatSeparator()
            );
        }
    }

    static final void acceptWithRecursive(Context<?> ctx, boolean recursive) {
        ctx.visit(K_WITH);

        if (recursive



        )
            ctx.sql(' ').visit(K_RECURSIVE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    // -------------------------------------------------------------------------
    // XXX With API
    // -------------------------------------------------------------------------

    private final WithStep as0(ResultQuery query, Boolean materialized) {
        DerivedColumnList dcl;

        if (fieldNameFunction != null)
            dcl = name(alias).fields(fieldNameFunction);
        else
            dcl = name(alias).fields(fieldAliases);

        CommonTableExpression cte;

        if (materialized == null)
            cte = dcl.as(query);
        else if (materialized)
            cte = dcl.asMaterialized(query);
        else
            cte = dcl.asNotMaterialized(query);

        this.ctes.add(cte);
        this.alias = null;
        this.fieldAliases = null;
        this.fieldNameFunction = null;

        return this;
    }

    @Override
    public final WithStep as(ResultQuery query) {
        return as0(query, null);
    }

    @Override
    public final WithStep asMaterialized(ResultQuery query) {
        return as0(query, true);
    }

    @Override
    public final WithStep asNotMaterialized(ResultQuery query) {
        return as0(query, false);
    }

    @Override
    public final WithImpl with(String a) {
        return with(DSL.name(a));
    }

    @Override
    public final WithImpl with(String a, String... f) {
        return with(DSL.name(a), Tools.map(f, s -> DSL.name(s), Name[]::new));
    }

    @Override
    public final WithImpl with(String a, Collection<String> f) {
        return with(DSL.name(a), Tools.map(f, s -> DSL.name(s), Name[]::new));
    }

    @Override
    public final WithImpl with(Name a) {
        return with(a, new Name[0]);
    }

    @Override
    public final WithImpl with(Name a, Name... f) {
        this.alias = a;
        this.fieldAliases = f;

        return this;
    }

    @Override
    public final WithImpl with(Name a, Collection<? extends Name> f) {
        return with(a, f.toArray(EMPTY_NAME));
    }

    @Override
    public final WithImpl with(String a, Function<? super Field<?>, ? extends String> f) {
        this.alias = DSL.name(a);
        this.fieldNameFunction = (field, i) -> f.apply(field);

        return this;
    }

    @Override
    public final WithImpl with(String a, BiFunction<? super Field<?>, ? super Integer, ? extends String> f) {
        this.alias = DSL.name(a);
        this.fieldNameFunction = f;

        return this;
    }



    @Override
    public final WithImpl with(String a, String fieldAlias1) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21 }));
    }

    @Override
    public final WithImpl with(String a, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21, String fieldAlias22) {
        return with(DSL.name(a), Tools.names(new String[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22 }));
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1) {
        return with(a, new Name[] { fieldAlias1 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21 });
    }

    @Override
    public final WithImpl with(Name a, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21, Name fieldAlias22) {
        return with(a, new Name[] { fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22 });
    }



    @Override
    public final WithImpl with(CommonTableExpression<?>... tables) {
        return with(Arrays.asList(tables));
    }

    @Override
    public final WithImpl with(Collection<? extends CommonTableExpression<?>> tables) {
        for (CommonTableExpression<?> table : tables)
            ctes.add(table);

        return this;
    }

    @Override
    public final <R extends Record> SelectWhereStep<R> selectFrom(TableLike<R> table) {
        return new SelectImpl(configuration, this).from(table);
    }

    @Override
    public final SelectWhereStep<Record> selectFrom(Name table) {
        return new SelectImpl(configuration, this).from(table);
    }

    @Override
    public final SelectWhereStep<Record> selectFrom(SQL sql) {
        return new SelectImpl(configuration, this).from(sql);
    }

    @Override
    public final SelectWhereStep<Record> selectFrom(String sql) {
        return new SelectImpl(configuration, this).from(sql);
    }

    @Override
    public final SelectWhereStep<Record> selectFrom(String sql, Object... bindings) {
        return new SelectImpl(configuration, this).from(sql, bindings);
    }

    @Override
    public final SelectWhereStep<Record> selectFrom(String sql, QueryPart... parts) {
        return new SelectImpl(configuration, this).from(sql, parts);
    }

    @Override
    public final SelectSelectStep<Record> select(Collection<? extends SelectFieldOrAsterisk> fields) {
        return new SelectImpl(configuration, this).select(fields);
    }

    @Override
    public final SelectSelectStep<Record> select(SelectFieldOrAsterisk... fields) {
        return new SelectImpl(configuration, this).select(fields);
    }



    @Override
    public final SelectSelectStep select(SelectField field1) {
        return (SelectSelectStep) select(new SelectField[] { field1 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20, SelectField field21) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final SelectSelectStep select(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20, SelectField field21, SelectField field22) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    @Override
    public final SelectSelectStep<Record> selectDistinct(Collection<? extends SelectFieldOrAsterisk> fields) {
        return new SelectImpl(configuration, this, true).select(fields);
    }

    @Override
    public final SelectSelectStep<Record> selectDistinct(SelectFieldOrAsterisk... fields) {
        return new SelectImpl(configuration, this, true).select(fields);
    }



    @Override
    public final SelectSelectStep selectDistinct(SelectField field1) {
        return selectDistinct(new SelectField[] { field1 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2) {
        return selectDistinct(new SelectField[] { field1, field2 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3) {
        return selectDistinct(new SelectField[] { field1, field2, field3 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20, SelectField field21) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final SelectSelectStep selectDistinct(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20, SelectField field21, SelectField field22) {
        return selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    @Override
    public final SelectSelectStep<Record1<Integer>> selectZero() {
        return new SelectImpl(configuration, this).select(zero());
    }

    @Override
    public final SelectSelectStep<Record1<Integer>> selectOne() {
        return new SelectImpl(configuration, this).select(one());
    }

    @Override
    public final SelectSelectStep<Record1<Integer>> selectCount() {
        return new SelectImpl(configuration, this).select(count());
    }

    @Override
    public final <R extends Record> InsertSetStep<R> insertInto(Table<R> into) {
        return new InsertImpl(configuration, this, into);
    }



    @Override
    public final InsertImpl insertInto(Table into, Field field1) {
        return insertInto(into, Arrays.asList(field1));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2) {
        return insertInto(into, Arrays.asList(field1, field2));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3) {
        return insertInto(into, Arrays.asList(field1, field2, field3));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21));
    }

    @Override
    public final InsertImpl insertInto(Table into, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21, Field field22) {
        return insertInto(into, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22));
    }



    @Override
    public final InsertImpl insertInto(Table into, Field... fields) {
        return insertInto(into, Arrays.asList(fields));
    }

    @Override
    public final InsertImpl insertInto(Table into, Collection fields) {
        return new InsertImpl(configuration, this, into, fields);
    }

    @Override
    public final <R extends Record> UpdateImpl update(Table<R> table) {
        return new UpdateImpl(configuration, this, table);
    }

    @Override
    public final <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table) {
        return new MergeImpl(configuration, this, table);
    }



    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1) {
        return mergeInto(table, Arrays.asList(field1));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2) {
        return mergeInto(table, Arrays.asList(field1, field2));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3) {
        return mergeInto(table, Arrays.asList(field1, field2, field3));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21));
    }

    @Override
    @Deprecated(forRemoval = true, since = "3.14")
    public final MergeUpsert mergeInto(Table table, Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21, Field field22) {
        return mergeInto(table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22));
    }



    @Override
    public final MergeUpsert mergeInto(Table table, Field... fields) {
        return mergeInto(table, Arrays.asList(fields));
    }

    @Override
    public final MergeUpsert mergeInto(Table table, Collection fields) {
        return new MergeUpsert(configuration, this, table, fields);
    }

    @Override
    public final <R extends Record> DeleteImpl delete(Table<R> table) {
        return new DeleteImpl(configuration, this, table);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends CommonTableExpression<?>> $commonTableExpressions() {
        return QOM.unmodifiable(ctes);
    }

    @Override
    public final WithImpl $commonTableExpressions(UnmodifiableList<? extends CommonTableExpression<?>> c) {
        return new WithImpl(configuration, recursive).with(c);
    }

    @Override
    public final boolean $recursive() {
        return recursive;
    }

    @Override
    public final WithImpl $recursive(boolean r) {
        return new WithImpl(configuration, r).with(ctes);
    }

















}
