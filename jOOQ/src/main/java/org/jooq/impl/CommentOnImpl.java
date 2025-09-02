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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>COMMENT ON TABLE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class CommentOnImpl
extends
    AbstractDDLQuery
implements
    QOM.CommentOn,
    CommentOnIsStep,
    CommentOnFinalStep
{

    final Table<?> table;
    final boolean  isView;
    final Field<?> field;
          Comment  comment;

    CommentOnImpl(
        Configuration configuration,
        Table<?> table,
        boolean isView,
        Field<?> field
    ) {
        this(
            configuration,
            table,
            isView,
            field,
            null
        );
    }

    CommentOnImpl(
        Configuration configuration,
        Table<?> table,
        boolean isView
    ) {
        this(
            configuration,
            table,
            isView,
            null
        );
    }

    CommentOnImpl(
        Configuration configuration,
        Table<?> table,
        boolean isView,
        Field<?> field,
        Comment comment
    ) {
        super(configuration);

        this.table = table;
        this.isView = isView;
        this.field = field;
        this.comment = comment;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final CommentOnImpl is(String comment) {
        return is(DSL.comment(comment));
    }

    @Override
    public final CommentOnImpl is(Comment comment) {
        this.comment = comment;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> SUPPORTS_COMMENT_ON_VIEW = SQLDialect.supportedBy(FIREBIRD, POSTGRES, YUGABYTEDB);

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
















            case MARIADB:
            case MYSQL: {
                if (table != null)
                    acceptMySQL(ctx);
                else
                    acceptDefault(ctx);

                break;
            }

            default: {
                acceptDefault(ctx);
                break;
            }
        }
    }




























































    private final void acceptMySQL(Context<?> ctx) {
        ctx.visit(K_ALTER_TABLE).sql(' ').visit(table).sql(' ').visit(K_COMMENT).sql(" = ").visit(comment);
    }

    private final void acceptDefault(Context<?> ctx) {
        ctx.visit(K_COMMENT).sql(' ').visit(K_ON).sql(' ');

        if (table != null)
            ctx.visit(isView && SUPPORTS_COMMENT_ON_VIEW.contains(ctx.dialect()) ? K_VIEW : K_TABLE)
               .sql(' ')
               .visit(table);
        else if (field != null)
            ctx.visit(K_COLUMN).sql(' ').visit(field);
        else
            throw new IllegalStateException();

        ctx.sql(' ').visit(K_IS).sql(' ').visit(comment);
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<?> $table() {
        return table;
    }

    @Override
    public final boolean $isView() {
        return isView;
    }

    @Override
    public final Field<?> $field() {
        return field;
    }

    @Override
    public final Comment $comment() {
        return comment;
    }

    @Override
    public final QOM.CommentOn $table(Table<?> newValue) {
        return $constructor().apply(newValue, $isView(), $field(), $comment());
    }

    @Override
    public final QOM.CommentOn $isView(boolean newValue) {
        return $constructor().apply($table(), newValue, $field(), $comment());
    }

    @Override
    public final QOM.CommentOn $field(Field<?> newValue) {
        return $constructor().apply($table(), $isView(), newValue, $comment());
    }

    @Override
    public final QOM.CommentOn $comment(Comment newValue) {
        return $constructor().apply($table(), $isView(), $field(), newValue);
    }

    public final Function4<? super Table<?>, ? super Boolean, ? super Field<?>, ? super Comment, ? extends QOM.CommentOn> $constructor() {
        return (a1, a2, a3, a4) -> new CommentOnImpl(configuration(), a1, a2, a3, a4);
    }


























}
