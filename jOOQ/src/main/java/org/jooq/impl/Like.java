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
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>LIKE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class Like
extends
    AbstractCondition
implements
    QOM.Like,
    LikeEscapeStep
{

    final Field<?>      value;
    final Field<String> pattern;
          Character     escape;

    Like(
        Field<?> value,
        Field<String> pattern
    ) {
        this(
            value,
            pattern,
            null
        );
    }

    Like(
        Field<?> value,
        Field<String> pattern,
        Character escape
    ) {

        this.value = nullableIf(false, Tools.nullSafe(value, pattern.getDataType()));
        this.pattern = nullableIf(false, Tools.nullSafe(pattern, value.getDataType()));
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final Like escape(char escape) {
        this.escape = escape;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> REQUIRES_CAST_ON_LIKE = SQLDialect.supportedBy(DERBY, POSTGRES, YUGABYTEDB);
    private static final Set<SQLDialect> NO_SUPPORT_ILIKE      = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL, SQLITE);

    @Override
    public final void accept(Context<?> ctx) {








        accept0(ctx, value, org.jooq.Comparator.LIKE, pattern, escape);
    }

    static final boolean castRhs(Context<?> ctx, Field<?> arg2) {
        boolean castRhs = false;









        return castRhs;
    }

    static final ParamType forcedParamType(Context<?> ctx, Character escape) {
        ParamType forcedParamType = ctx.paramType();








        return forcedParamType;
    }

    static final void accept0(Context<?> ctx, Field<?> arg1, org.jooq.Comparator op, Field<?> arg2, Character escape) {

        // [#1159] [#1725] Some dialects cannot auto-convert the LHS operand to a
        // VARCHAR when applying a LIKE predicate
        switch (op) {
            case LIKE:
            case SIMILAR_TO:
            case NOT_LIKE:
            case NOT_SIMILAR_TO:
                if (arg1.getType() != String.class && REQUIRES_CAST_ON_LIKE.contains(ctx.dialect()))
                    arg1 = castIfNeeded(arg1, String.class);
                if (arg2.getType() != String.class && REQUIRES_CAST_ON_LIKE.contains(ctx.dialect()))
                    arg2 = castIfNeeded(arg2, String.class);

                break;

            case LIKE_IGNORE_CASE:
            case NOT_LIKE_IGNORE_CASE:
                if (arg1.getType() != String.class)
                    arg1 = castIfNeeded(arg1, String.class);
                if (arg2.getType() != String.class)
                    arg2 = castIfNeeded(arg2, String.class);

                break;
        }

        // [#1423] [#9889] PostgreSQL and H2 support ILIKE natively. Other dialects
        // need to emulate this as LOWER(lhs) LIKE LOWER(rhs)
        switch (op) {
            case LIKE_IGNORE_CASE:
            case NOT_LIKE_IGNORE_CASE:
                if (NO_SUPPORT_ILIKE.contains(ctx.dialect())) {
                    arg1 = DSL.lower((Field) arg1);
                    arg2 = DSL.lower((Field) arg2);
                    op = (op == org.jooq.Comparator.LIKE_IGNORE_CASE ? org.jooq.Comparator.LIKE : org.jooq.Comparator.NOT_LIKE);
                }
        }

        boolean castRhs = castRhs(ctx, arg2);

        ctx.visit(arg1).sql(' ').visit(op.toKeyword()).sql(' ');

        if (castRhs)
            ctx.visit(K_CAST).sql('(');

        ctx.visit(arg2, forcedParamType(ctx, escape));

        if (castRhs)
            ctx.sql(' ').visit(K_AS).sql(' ').visit(K_VARCHAR).sql("(4000))");

        if (escape != null) {
            ctx.sql(' ').visit(K_ESCAPE).sql(' ')
               .visit(inline(escape));
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return value;
    }

    @Override
    public final Field<String> $arg2() {
        return pattern;
    }

    @Override
    public final Character $arg3() {
        return escape;
    }

    @Override
    public final QOM.Like $arg1(Field<?> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.Like $arg2(Field<String> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.Like $arg3(Character newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<?>, ? super Field<String>, ? super Character, ? extends QOM.Like> $constructor() {
        return (a1, a2, a3) -> new Like(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Like o) {
            return
                StringUtils.equals($value(), o.$value()) &&
                StringUtils.equals($pattern(), o.$pattern()) &&
                StringUtils.equals($escape(), o.$escape())
            ;
        }
        else
            return super.equals(that);
    }
}
