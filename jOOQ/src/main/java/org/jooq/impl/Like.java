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
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
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

        this.value = nullSafeNotNull(value, OTHER);
        this.pattern = nullSafeNotNull(pattern, VARCHAR);
        this.escape = escape;
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



    private static final Set<SQLDialect> REQUIRES_CAST_ON_LIKE = SQLDialect.supportedBy(DERBY, POSTGRES, YUGABYTE);

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
        if (arg1.getType() != String.class && REQUIRES_CAST_ON_LIKE.contains(ctx.dialect()))
            arg1 = castIfNeeded(arg1, String.class);

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
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Like) {
            return
                StringUtils.equals(value, ((Like) that).value) &&
                StringUtils.equals(pattern, ((Like) that).pattern) &&
                StringUtils.equals(escape, ((Like) that).escape)
            ;
        }
        else
            return super.equals(that);
    }
}
