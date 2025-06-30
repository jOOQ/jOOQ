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
import org.jooq.impl.QOM.CommentObjectType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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

    final CommentObjectType objectType;
    final Table<?>          table;
    final Field<?>          field;
          Comment           comment;

    CommentOnImpl(
        Configuration configuration,
        CommentObjectType objectType,
        Table<?> table,
        Field<?> field
    ) {
        this(
            configuration,
            objectType,
            table,
            field,
            null
        );
    }

    CommentOnImpl(
        Configuration configuration,
        CommentObjectType objectType,
        Table<?> table
    ) {
        this(
            configuration,
            objectType,
            table,
            null
        );
    }

    CommentOnImpl(
        Configuration configuration,
        CommentObjectType objectType,
        Table<?> table,
        Field<?> field,
        Comment comment
    ) {
        super(configuration);

        this.objectType = objectType;
        this.table = table;
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



    private static final Set<SQLDialect> NO_SUPPORT_COMMENT_ON_VIEW              = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> NO_SUPPORT_COMMENT_ON_MATERIALIZED_VIEW = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE, TRINO);

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

            case CLICKHOUSE: {
                if (table != null)
                    ctx.visit(K_ALTER_TABLE).sql(' ').visit(table).sql(' ')
                       .visit(K_MODIFY).sql(' ').visit(K_COMMENT).sql(' ').visit(comment);
                else
                    ctx.visit(K_ALTER_TABLE).sql(' ').visit(table(field.getQualifiedName().qualifier())).sql(' ')
                       .visit(K_COMMENT).sql(' ').visit(K_COLUMN).sql(' ').visit(DSL.field(field.getUnqualifiedName())).sql(' ').visit(comment);

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

        if (table != null) {
            if (objectType == CommentObjectType.VIEW && !NO_SUPPORT_COMMENT_ON_VIEW.contains(ctx.dialect()))
                ctx.visit(K_VIEW).sql(' ');
            else if (objectType == CommentObjectType.MATERIALIZED_VIEW && !NO_SUPPORT_COMMENT_ON_MATERIALIZED_VIEW.contains(ctx.dialect()))
                ctx.visit(K_MATERIALIZED).sql(' ').visit(K_VIEW).sql(' ');




            else
                ctx.visit(K_TABLE).sql(' ');

            ctx.visit(table);
        }
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
    public final CommentObjectType $objectType() {
        return objectType;
    }

    @Override
    public final Table<?> $table() {
        return table;
    }

    @Override
    public final Field<?> $field() {
        return field;
    }

    @Override
    public final boolean $isView() {
        return $objectType() == CommentObjectType.VIEW;
    }

    @Override
    public final boolean $isMaterializedView() {
        return $objectType() == CommentObjectType.MATERIALIZED_VIEW;
    }

    @Override
    public final Comment $comment() {
        return comment;
    }

    @Override
    public final QOM.CommentOn $objectType(CommentObjectType newValue) {
        return $constructor().apply(newValue, $table(), $field(), $comment());
    }

    @Override
    public final QOM.CommentOn $table(Table<?> newValue) {
        return $constructor().apply($objectType(), newValue, $field(), $comment());
    }

    @Override
    public final QOM.CommentOn $field(Field<?> newValue) {
        return $constructor().apply($objectType(), $table(), newValue, $comment());
    }

    @Override
    public final QOM.CommentOn $comment(Comment newValue) {
        return $constructor().apply($objectType(), $table(), $field(), newValue);
    }

    @Override
    public final QOM.CommentOn $isView(boolean newValue) {
        return $objectType(newValue ? CommentObjectType.VIEW : null);
    }

    @Override
    public final QOM.CommentOn $isMaterializedView(boolean newValue) {
        return $objectType(newValue ? CommentObjectType.MATERIALIZED_VIEW : CommentObjectType.TABLE);
    }

    public final Function4<? super CommentObjectType, ? super Table<?>, ? super Field<?>, ? super Comment, ? extends QOM.CommentOn> $constructor() {
        return (a1, a2, a3, a4) -> new CommentOnImpl(configuration(), a1, a2, a3, a4);
    }


























}
