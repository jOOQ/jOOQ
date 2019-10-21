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

// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.impl.DSL.comment;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Keywords.K_ALTER_TABLE;
import static org.jooq.impl.Keywords.K_BEGIN_CATCH;
import static org.jooq.impl.Keywords.K_BEGIN_TRY;
import static org.jooq.impl.Keywords.K_COLUMN;
import static org.jooq.impl.Keywords.K_COMMENT;
import static org.jooq.impl.Keywords.K_DECLARE;
import static org.jooq.impl.Keywords.K_DEFAULT;
import static org.jooq.impl.Keywords.K_END_CATCH;
import static org.jooq.impl.Keywords.K_END_TRY;
import static org.jooq.impl.Keywords.K_EXEC;
import static org.jooq.impl.Keywords.K_IS;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.Keywords.K_VIEW;

import java.util.Set;

import org.jooq.Comment;
import org.jooq.CommentOnFinalStep;
import org.jooq.CommentOnIsStep;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
// ...
import org.jooq.SQLDialect;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class CommentOnImpl extends AbstractRowCountQuery
implements
    CommentOnIsStep,
    CommentOnFinalStep {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID         = 2665659331902435568L;
    private static final Set<SQLDialect>     SUPPORTS_COMMENT_ON_VIEW = SQLDialect.supported(FIREBIRD, POSTGRES);

    private final Table<?>                   table;
    private final boolean                    isView;
    private final Field<?>                   field;
    private Comment                          comment;

    CommentOnImpl(Configuration configuration, Table<?> table, boolean isView) {
        super(configuration);

        this.table = table;
        this.isView = isView;
        this.field = null;
    }

    CommentOnImpl(Configuration configuration, Field<?> field) {
        super(configuration);

        this.table = null;
        this.isView = false;
        this.field = field;
    }

    final Table<?> $table()   { return table; }
    final boolean  $isView()  { return isView; }
    final Field<?> $field()   { return field; }
    final Comment  $comment() { return comment; }

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
            ctx.visit(isView && SUPPORTS_COMMENT_ON_VIEW.contains(ctx.family())
                        ? K_VIEW
                        : K_TABLE)
               .sql(' ')
               .visit(table);
        else if (field != null)
            ctx.visit(K_COLUMN).sql(' ').visit(field);
        else
            throw new IllegalStateException();

        ctx.sql(' ').visit(K_IS).sql(' ').visit(comment);
    }

    @Override
    public final CommentOnImpl is(String c) {
        return is(comment(c));
    }

    @Override
    public final CommentOnImpl is(Comment c) {
        this.comment = c;
        return this;
    }
}
