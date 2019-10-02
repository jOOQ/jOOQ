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

import static org.jooq.Clause.CREATE_VIEW;
import static org.jooq.Clause.CREATE_VIEW_AS;
import static org.jooq.Clause.CREATE_VIEW_NAME;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_ALTER;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;
import static org.jooq.impl.Keywords.K_OR;
import static org.jooq.impl.Keywords.K_REPLACE;
import static org.jooq.impl.Keywords.K_VIEW;
import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import org.jooq.Clause;
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
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class CreateViewImpl<R extends Record> extends AbstractRowCountQuery implements

    // Cascading interface implementations for CREATE VIEW behaviour
    CreateViewAsStep<R>,
    CreateViewFinalStep {


    /**
     * Generated UID
     */
    private static final long                                                       serialVersionUID         = 8904572826501186329L;
    private static final Clause[]                                                   CLAUSES                  = { CREATE_VIEW };
    private static final Set<SQLDialect>                                            NO_SUPPORT_IF_NOT_EXISTS = SQLDialect.supported(DERBY, FIREBIRD, POSTGRES);

    private final boolean                                                           ifNotExists;
    private final boolean                                                           orReplace;
    private final Table<?>                                                          view;

    private final BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction;

    private Field<?>[]                                                              fields;
    private ResultQuery<?>                                                          select;
    private transient Select<?>                                                     parsed;

    CreateViewImpl(Configuration configuration, Table<?> view, Field<?>[] fields, boolean ifNotExists, boolean orReplace) {
        super(configuration);

        this.view = view;
        this.fields = fields;

        this.fieldNameFunction = null;

        this.ifNotExists = ifNotExists;
        this.orReplace = orReplace;
    }


    CreateViewImpl(Configuration configuration, Table<?> view, BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction, boolean ifNotExists, boolean orReplace) {
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
    public final CreateViewFinalStep as(Select<? extends R> s) {
        this.select = s;


        if (fieldNameFunction != null) {
            List<Field<?>> source = s.getSelect();
            fields = new Field[source.size()];
            for (int i = 0; i < fields.length; i++)
                fields[i] = fieldNameFunction.apply(source.get(i), i);
        }


        return this;
    }

    @Override
    public final CreateViewFinalStep as(SQL sql) {
        this.select = DSL.resultQuery(sql);


        if (fieldNameFunction != null) {
            Select<?> s = parsed();
            List<Field<?>> source = s.getSelect();
            fields = new Field[source.size()];
            for (int i = 0; i < fields.length; i++)
                fields[i] = fieldNameFunction.apply(source.get(i), i);
        }


        return this;
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

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.CREATE_VIEW);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.CREATE_VIEW);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        Field<?>[] f = fields;

        // [#2059] MemSQL doesn't support column aliases at the view level
        boolean rename = f != null && f.length > 0;
        boolean renameSupported = true;
        boolean replaceSupported = false;









        // [#4806] CREATE VIEW doesn't accept parameters in most databases
        ParamType paramType = ctx.paramType();

        ctx.start(CREATE_VIEW_NAME)
           .visit(replaceSupported && orReplace ? K_REPLACE : K_CREATE);

        if (orReplace && !replaceSupported) {
            ctx.sql(' ').visit(K_OR);

            switch (ctx.family()) {







                default:
                    ctx.sql(' ').visit(K_REPLACE);
                    break;
            }
        }

        ctx.sql(' ').visit(K_VIEW)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');

        ctx.visit(view);

        if (rename && renameSupported) {
            boolean qualify = ctx.qualify();

            ctx.sql('(')
               .qualify(false)
               .visit(new QueryPartList<>(f))
               .qualify(qualify)
               .sql(')');
        }

        ctx.end(CREATE_VIEW_NAME)
           .formatSeparator()
           .visit(K_AS)
           .formatSeparator()
           .start(CREATE_VIEW_AS)
           .paramType(INLINED)
           .visit(
               rename && !renameSupported
             ? selectFrom(table(parsed()).as(name("t"), Tools.fieldNames(f)))
             : select)
           .paramType(paramType)
           .end(CREATE_VIEW_AS);
    }

    private final Select<?> parsed() {
        if (parsed != null)
            return parsed;

        if (select instanceof Select)
            return parsed = (Select<?>) select;

        DSLContext dsl = configuration().dsl();
        return dsl.parser().parseSelect(dsl.renderInlined(select));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
