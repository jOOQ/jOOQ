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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import static org.jooq.impl.Tools.map;

import java.util.Arrays;
import java.util.function.BiFunction;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateViewAsStep;
import org.jooq.CreateViewFinalStep;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.Table;

/**
 * This is a legacy implementation of the deprecated overloads like
 * {@link DSL#createView(Table, java.util.function.Function)}, where we accepted
 * "convenience" functions for the field names.
 *
 * @author Lukas Eder
 */
final class CreateViewImplWithFieldNameFunctionImpl<R extends Record> extends AbstractDDLQuery implements

    // Cascading interface implementations for CREATE VIEW behaviour
    CreateViewAsStep<R>,
    CreateViewFinalStep,
    QOM.UTransient

{

    private final boolean                                                           ifNotExists;
    private final boolean                                                           orReplace;
    private final Table<?>                                                          view;
    private final BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction;
    private Field<?>[]                                                              fields;
    private ResultQuery<?>                                                          select;
    private CreateViewImpl<?>                                                       delegate;

    CreateViewImplWithFieldNameFunctionImpl(
        Configuration configuration,
        Table<?> view,
        BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction,
        boolean ifNotExists,
        boolean orReplace
    ) {
        super(configuration);

        this.view = view;
        this.fields = null;
        this.fieldNameFunction = fieldNameFunction;
        this.ifNotExists = ifNotExists;
        this.orReplace = orReplace;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final CreateViewFinalStep as(ResultQuery<? extends R> as) {
        this.select = as;

        if (fieldNameFunction != null) {
            if (as instanceof Select<?> s)
                fields = map(s.getSelect(), fieldNameFunction::apply, Field[]::new);
            else
                fields = map(as.fields(), fieldNameFunction::apply, Field[]::new);
        }

        return delegate();
    }

    @Override
    public final CreateViewFinalStep as(SQL sql) {
        this.select = DSL.resultQuery(sql);

        if (fieldNameFunction != null)
            fields = map(delegate().parsed().getSelect(), fieldNameFunction::apply, Field[]::new);

        return delegate();
    }

    private final CreateViewImpl<?> delegate() {
        if (delegate == null)
            delegate = new CreateViewImpl<>(configuration(), view, Arrays.asList(fields), orReplace, ifNotExists, false, select);

        return delegate;
    }

    @Override
    public final CreateViewFinalStep as(String sql) {
        return as(DSL.sql(sql));
    }

    @Override
    public final CreateViewFinalStep as(String sql, Object... bindings) {
        return as(DSL.sql(sql, bindings));
    }

    @Override
    public final CreateViewFinalStep as(String sql, QueryPart... parts) {
        return as(DSL.sql(sql, parts));
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate());
    }
}
