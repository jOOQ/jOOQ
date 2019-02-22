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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.LIKE;
import static org.jooq.Comparator.LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_LIKE;
import static org.jooq.Comparator.NOT_LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_SIMILAR_TO;
import static org.jooq.Comparator.SIMILAR_TO;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_ESCAPE;
import static org.jooq.impl.Keywords.K_VARCHAR;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.isEmbeddable;

import java.util.EnumSet;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.LikeEscapeStep;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class CompareCondition extends AbstractCondition implements LikeEscapeStep {

    private static final long                serialVersionUID      = -747240442279619486L;
    private static final Clause[]            CLAUSES               = { CONDITION, CONDITION_COMPARISON };
    private static final EnumSet<SQLDialect> REQUIRES_CAST_ON_LIKE = EnumSet.of(DERBY, POSTGRES);

    private final Field<?>                   field1;
    private final Field<?>                   field2;
    private final Comparator                 comparator;
    private Character                        escape;

    CompareCondition(Field<?> field1, Field<?> field2, Comparator comparator) {
        this.field1 = field1;
        this.field2 = field2;
        this.comparator = comparator;
    }

    @Override
    public final Condition escape(char c) {
        this.escape = c;
        return this;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (isEmbeddable(field1) && isEmbeddable(field2))
            ctx.visit(row(embeddedFields(field1)).compare(comparator, embeddedFields(field2)));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        SQLDialect family = ctx.family();
        Field<?> lhs = field1;
        Field<?> rhs = field2;
        Comparator op = comparator;

        // [#1159] [#1725] Some dialects cannot auto-convert the LHS operand to a
        // VARCHAR when applying a LIKE predicate
        if ((op == LIKE || op == NOT_LIKE || op == SIMILAR_TO || op == NOT_SIMILAR_TO)
                && field1.getType() != String.class
                && REQUIRES_CAST_ON_LIKE.contains(family)) {

            lhs = lhs.cast(String.class);
        }

        // [#1423] Only Postgres knows a true ILIKE operator. Other dialects
        // need to emulate this as LOWER(lhs) LIKE LOWER(rhs)
        else if ((op == LIKE_IGNORE_CASE || op == NOT_LIKE_IGNORE_CASE)



                && POSTGRES != family) {

            lhs = lhs.lower();
            rhs = rhs.lower();
            op = (op == LIKE_IGNORE_CASE ? LIKE : NOT_LIKE);
        }

        ctx.visit(lhs)
           .sql(' ');

        boolean castRhs = false;
        ParamType previousParamType = ctx.paramType();
        ParamType forcedParamType = previousParamType;














                     ctx.visit(op.toKeyword()).sql(' ');
        if (castRhs) ctx.visit(K_CAST).sql('(');
                     ctx.paramType(forcedParamType)
                        .visit(rhs)
                        .paramType(previousParamType);
        if (castRhs) ctx.sql(' ').visit(K_AS).sql(' ').visit(K_VARCHAR).sql("(4000))");

        if (escape != null) {
            ctx.sql(' ').visit(K_ESCAPE).sql(' ')
               .visit(inline(escape));
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
