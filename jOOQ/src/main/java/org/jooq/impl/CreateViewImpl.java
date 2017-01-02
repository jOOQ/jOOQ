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
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.CREATE_VIEW;
import static org.jooq.Clause.CREATE_VIEW_AS;
import static org.jooq.Clause.CREATE_VIEW_NAME;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.table;

import java.util.List;
import java.util.function.BiFunction;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateViewAsStep;
import org.jooq.CreateViewFinalStep;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class CreateViewImpl<R extends Record> extends AbstractQuery implements

    // Cascading interface implementations for CREATE VIEW behaviour
    CreateViewAsStep<R>,
    CreateViewFinalStep {


    /**
     * Generated UID
     */
    private static final long                                                       serialVersionUID = 8904572826501186329L;
    private static final Clause[]                                                   CLAUSES          = { CREATE_VIEW };

    private final boolean                                                           ifNotExists;
    private final Table<?>                                                          view;

    private final BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction;

    private Field<?>[]                                                              fields;
    private Select<?>                                                               select;

    CreateViewImpl(Configuration configuration, Table<?> view, Field<?>[] fields, boolean ifNotExists) {
        super(configuration);

        this.view = view;
        this.fields = fields;

        this.fieldNameFunction = null;

        this.ifNotExists = ifNotExists;
    }


    CreateViewImpl(Configuration configuration, Table<?> view, BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction, boolean ifNotExists) {
        super(configuration);

        this.view = view;
        this.fields = null;
        this.fieldNameFunction = fieldNameFunction;
        this.ifNotExists = ifNotExists;
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

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !asList(DERBY, FIREBIRD).contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.executeImmediateBegin(ctx, DDLStatementType.CREATE_VIEW);
            accept0(ctx);
            Tools.executeImmediateEnd(ctx, DDLStatementType.CREATE_VIEW);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {

        // [#3835] SQLite doesn't like renaming columns at the view level
        boolean rename = fields != null && fields.length > 0;
        boolean renameSupported = ctx.family() != SQLITE;

        // [#4806] CREATE VIEW doesn't accept parameters in most databases
        ParamType paramType = ctx.paramType();

        ctx.start(CREATE_VIEW_NAME)
           .keyword("create view")
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.keyword("if not exists")
               .sql(' ');

        ctx.visit(view);

        if (rename && renameSupported) {
            boolean qualify = ctx.qualify();

            ctx.sql('(')
               .qualify(false)
               .visit(new QueryPartList<Field<?>>(fields))
               .qualify(qualify)
               .sql(')');
        }

        ctx.end(CREATE_VIEW_NAME)
           .formatSeparator()
           .keyword("as")
           .formatSeparator()
           .start(CREATE_VIEW_AS)
           .paramType(INLINED)
           .visit(
               rename && !renameSupported
             ? selectFrom(table(select).as("t", Tools.fieldNames(fields)))
             : select)
           .paramType(paramType)
           .end(CREATE_VIEW_AS);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
