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
import static org.jooq.conf.RenderQuotedNames.NEVER;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonbArray;
import static org.jooq.impl.DSL.jsonbObject;
import static org.jooq.impl.DSL.quotedName;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.Multiset.returningClob;
import static org.jooq.impl.Multiset.wrapXmlelement;
import static org.jooq.impl.Names.N_RECORD;
import static org.jooq.impl.RowAsField.NO_NATIVE_SUPPORT;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.fieldName;
import static org.jooq.impl.Tools.fieldNameString;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.row0;
import static org.jooq.impl.Tools.sanitiseName;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_ROW_CONTENT;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SelectField;
import org.jooq.conf.NestedCollectionEmulation;
import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
abstract class AbstractRowAsField<R extends Record>
extends
    AbstractField<R>
implements
    AutoAlias<SelectField<R>>
{

    private static final JooqLogger logNamePathSeparator = JooqLogger.getLogger(AbstractRowAsField.class, 3);

    AbstractRowAsField(Name name, DataType<R> type) {
        super(name, type);
    }

    abstract Fields fields0();
    abstract Class<R> getRecordType();

    @SuppressWarnings("unchecked")
    final AbstractRow<R> emulatedFields(Configuration configuration) {
        String s = configuration.settings().getNamePathSeparator();

        // [#18173] [#18176] Most RDBMS don't support "." in unquoted identifiers, so warn the user about what they're doing wrong
        if (".".equals(s) && configuration.settings().getRenderQuotedNames() == NEVER)
            if (logNamePathSeparator.isWarnEnabled())
                logNamePathSeparator.warn("Unquoted identifiers", "Settings.renderQuotedNames disables quoting and Settings.namePathSeparator is the default (\".\"), which can cause problems when nesting records. Consider quoting identifiers or specifying a different Settings.namePathSeparator.");

        return (AbstractRow<R>) row0(map(fields0().fields(), x -> x.as(quotedName(sanitiseName(configuration, getUnqualifiedName().unquotedName() + s + x.getName()))), Field[]::new));
    }

    @Override
    final int projectionSize() {
        int result = 0;

        for (Field<?> field : fields0().fields())
            result += ((AbstractField<?>) field).projectionSize();

        return result;
    }

    @Override
    public final boolean declaresFields() {
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#12021] If a RowField is nested somewhere in MULTISET, we must apply
        //          the MULTISET emulation as well, here
        if (forceMultisetContent(ctx, () -> getDataType().getRow().size() > 1))
            acceptMultisetContent(ctx, getDataType().getRow(), this, this::acceptDefault);
        else
            acceptDefault(ctx);
    }

    static final boolean forceMultisetContent(
        Context<?> ctx,
        BooleanSupplier degreeCheck
    ) {
        // [#18870] If users want native MULTISET / ROW behaviour, we mustn't enforce emulations
        return ctx.settings().getEmulateMultiset() != NestedCollectionEmulation.NATIVE && (

            // All types of row expressions must be emulated using MULTISET
            // emulations if nested in some sort of MULTISET content
            // [#13598] Users can also override the default behaviour
            TRUE.equals(ctx.data(DATA_MULTISET_CONTENT))

            // Row expressions of degree > 1 must also be emulated using MULTISET
            // emulations if nested in scalar subqueries, except for predicand
            // subqueries, where row subqueries are usually supported, e.g.
            // (a, b) IN (SELECT x, y)
            || ctx.subquery()
                    && NO_NATIVE_SUPPORT.contains(ctx.dialect())
                    && !ctx.predicandSubquery()
                    && !ctx.derivedTableSubquery()
                    && !ctx.setOperationSubquery()
                    && degreeCheck.getAsBoolean()
        );
    }

    static final boolean forceRowContent(Context<?> ctx) {
        return

            // [#15991] All types of row expressions must be emulated using ROW
            // emulations if nested in some sort of ROW content
            TRUE.equals(ctx.data(DATA_ROW_CONTENT));
    }

    static final void acceptMultisetContent(Context<?> ctx, Row row, Field<?> field, Consumer<? super Context<?>> acceptDefault) {
        Object previous = ctx.data(DATA_MULTISET_CONTENT);

        try {
            ctx.data(DATA_MULTISET_CONTENT, true);
            Name alias = field.getUnqualifiedName();

            switch (emulateMultiset(ctx.configuration())) {
                case JSON:
                    switch (ctx.family()) {
















                        default:
                            ctx.visit(alias(ctx, alias, returningClob(ctx, jsonArray(row.fields()).nullOnNull())));
                            break;
                    }

                    break;

                case JSONB:
                    switch (ctx.family()) {
















                        default:
                            ctx.visit(alias(ctx, alias, returningClob(ctx, jsonbArray(row.fields()).nullOnNull())));
                            break;
                    }

                    break;

                case XML:
                    switch (ctx.family()) {









                        default:
                            ctx.visit(alias(ctx, alias, xmlelement(N_RECORD,
                                map(row.fields(), (f, i) -> wrapXmlelement(ctx, fieldName(i), f))
                            )));

                            break;
                    }

                    break;

                case NATIVE:
                default:
                    acceptDefault.accept(ctx);
                    break;
            }
        }
        finally {
            ctx.data(DATA_MULTISET_CONTENT, previous);
        }
    }

    @Override
    public final SelectField<R> autoAlias(Context<?> ctx, SelectField<R> s) {

        // [#13843] Re-aliasing only applies if at least ROW() projection is supported natively
        if (RowAsField.NO_NATIVE_SUPPORT.contains(ctx.dialect()))
            return s;

        // [#13843] Within MULTISET(), re-aliasing isn't required, while it leads to new edge cases
        else if (forceMultisetContent(ctx, () -> getDataType().getRow().size() > 1))
            return s;

        // [#13843] With native support, re-alias the table as field projection
        else
            return new FieldAlias<>(DSL.field(s), getUnqualifiedName());
    }

    private static final Field<?> alias(Context<?> ctx, Name alias, Field<?> field) {
        return ctx.declareFields() ? field.as(alias) : field;
    }

    abstract void acceptDefault(Context<?> ctx);
}
