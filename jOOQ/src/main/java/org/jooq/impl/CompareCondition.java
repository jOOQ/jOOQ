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

import static java.lang.Boolean.TRUE;
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.LIKE;
import static org.jooq.Comparator.LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.Comparator.NOT_LIKE;
import static org.jooq.Comparator.NOT_LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_SIMILAR_TO;
import static org.jooq.Comparator.SIMILAR_TO;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_ESCAPE;
import static org.jooq.impl.Keywords.K_VARCHAR;
import static org.jooq.impl.Tools.castIfNeeded;
import static org.jooq.impl.Tools.characterLiteral;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.nullSafe;
import static org.jooq.impl.Tools.nullableIf;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONDITION;
import static org.jooq.impl.Transformations.transformInConditionSubqueryWithLimitToDerivedTable;
import static org.jooq.impl.Transformations.subqueryWithLimit;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.LikeEscapeStep;
// ...
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.conf.ParamType;
import org.jooq.impl.Tools.BooleanDataKey;

/**
 * @author Lukas Eder
 */
final class CompareCondition extends AbstractCondition implements LikeEscapeStep {

    private static final Clause[]        CLAUSES               = { CONDITION, CONDITION_COMPARISON };
    private static final Set<SQLDialect> REQUIRES_CAST_ON_LIKE = SQLDialect.supportedBy(DERBY, POSTGRES);
    private static final Set<SQLDialect> NO_SUPPORT_ILIKE      = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL, SQLITE);

    final Field<?>                       field1;
    final Field<?>                       field2;
    final Comparator                     comparator;
    private Character                    escape;

    CompareCondition(Field<?> field1, Field<?> field2, Comparator comparator) {
        this.field1 = nullableIf(comparator.supportsNulls(), nullSafe(field1, field2.getDataType()));
        this.field2 = nullableIf(comparator.supportsNulls(), nullSafe(field2, field1.getDataType()));
        this.comparator = comparator;
    }

    @Override
    public final Condition escape(char c) {
        this.escape = c;
        return this;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void accept(Context<?> ctx) {
        boolean field1Embeddable = field1.getDataType().isEmbeddable();
        SelectQueryImpl<?> s;

        if (field1Embeddable && field2 instanceof ScalarSubquery)
            ctx.visit(row(embeddedFields(field1)).compare(comparator, ((ScalarSubquery<?>) field2).query));
        else if (field1Embeddable && field2.getDataType().isEmbeddable())
            ctx.visit(row(embeddedFields(field1)).compare(comparator, embeddedFields(field2)));
        else if ((comparator == IN || comparator == NOT_IN)
                && (s = subqueryWithLimit(field2)) != null
                && transformInConditionSubqueryWithLimitToDerivedTable(ctx.configuration())) {



        }
        else if (field1.getDataType().isMultiset()
                && field2.getDataType().isMultiset()
                && !TRUE.equals(ctx.data(DATA_MULTISET_CONDITION)))
            ctx.data(DATA_MULTISET_CONDITION, true, c -> c.visit(this));
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
                && REQUIRES_CAST_ON_LIKE.contains(ctx.dialect())) {

            lhs = castIfNeeded(lhs, String.class);
        }

        // [#1423] [#9889] PostgreSQL and H2 support ILIKE natively. Other dialects
        // need to emulate this as LOWER(lhs) LIKE LOWER(rhs)
        else if ((op == LIKE_IGNORE_CASE || op == NOT_LIKE_IGNORE_CASE) && NO_SUPPORT_ILIKE.contains(ctx.dialect())) {
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
                     ctx.visit(rhs, forcedParamType);
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
