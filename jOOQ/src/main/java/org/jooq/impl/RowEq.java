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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>ROW EQ</code> statement.
 */
@SuppressWarnings({ "unused" })
final class RowEq
extends
    AbstractCondition
implements
    QOM.RowEq
{

    final Row arg1;
    final Row arg2;

    RowEq(
        Row arg1,
        Row arg2
    ) {

        this.arg1 = ((AbstractRow) arg1).convertTo(arg2);
        this.arg2 = ((AbstractRow) arg2).convertTo(arg1);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------







    private static final Set<SQLDialect> EMULATE_EQ_AND_NE  = SQLDialect.supportedBy(DERBY, FIREBIRD);
    private static final Set<SQLDialect> EMULATE_RANGES     = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD);

    @Override
    public final void accept(Context<?> ctx) {







        RowEq.acceptCompareCondition(ctx, this, arg1, org.jooq.Comparator.EQUALS, arg2);
    }

    /**
     * @deprecated - [#12425] After the QOM refactoring, this should be improved
     */
    @Deprecated
    static final <T extends Row> void acceptCompareCondition(
        Context<?> ctx,
        AbstractCondition condition,
        T arg1,
        org.jooq.Comparator op,
        T arg2
    ) {






        // Regular comparison predicate emulation
        if ((op == org.jooq.Comparator.EQUALS || op == org.jooq.Comparator.NOT_EQUALS) &&

            // TODO: Re-implement Informix forceEmulation semantics
            EMULATE_EQ_AND_NE.contains(ctx.dialect())
        ) {

            Field<?>[] arg2Fields = arg2.fields();
            Condition result = DSL.and(map(arg1.fields(), (f, i) -> f.equal((Field) arg2Fields[i])));

            if (op == org.jooq.Comparator.NOT_EQUALS)
                result = result.not();

            ctx.visit(result);
        }

        // Ordering comparison predicate emulation
        else if ((op == org.jooq.Comparator.GREATER || op == org.jooq.Comparator.GREATER_OR_EQUAL || op == org.jooq.Comparator.LESS || op == org.jooq.Comparator.LESS_OR_EQUAL) &&

            // TODO: Re-implement Informix forceEmulation semantics
            EMULATE_RANGES.contains(ctx.dialect())
        ) {

            // The order component of the comparator (stripping the equal component)
            org.jooq.Comparator order
                = (op == org.jooq.Comparator.GREATER) ? org.jooq.Comparator.GREATER
                : (op == org.jooq.Comparator.GREATER_OR_EQUAL) ? org.jooq.Comparator.GREATER
                : (op == org.jooq.Comparator.LESS) ? org.jooq.Comparator.LESS
                : (op == org.jooq.Comparator.LESS_OR_EQUAL) ? org.jooq.Comparator.LESS
                : null;

            // [#2658] The factored order component of the comparator (enforcing the equal component)
            org.jooq.Comparator factoredOrder
                = (op == org.jooq.Comparator.GREATER) ? org.jooq.Comparator.GREATER_OR_EQUAL
                : (op == org.jooq.Comparator.GREATER_OR_EQUAL) ? org.jooq.Comparator.GREATER_OR_EQUAL
                : (op == org.jooq.Comparator.LESS) ? org.jooq.Comparator.LESS_OR_EQUAL
                : (op == org.jooq.Comparator.LESS_OR_EQUAL) ? org.jooq.Comparator.LESS_OR_EQUAL
                : null;

            // Whether the comparator has an equal component
            boolean equal
                = (op == org.jooq.Comparator.GREATER_OR_EQUAL)
                ||(op == org.jooq.Comparator.LESS_OR_EQUAL);

            Field<?>[] arg1Fields = arg1.fields();
            Field<?>[] arg2Fields = arg2.fields();

            // The following algorithm emulates the equivalency of these expressions:
            // (A, B, C) > (X, Y, Z)
            // (A > X) OR (A = X AND B > Y) OR (A = X AND B = Y AND C > Z)
            List<Condition> outer = new ArrayList<>(1 + arg1Fields.length);

            for (int i = 0; i < arg1Fields.length; i++) {
                List<Condition> inner = new ArrayList<>(1 + i);

                for (int j = 0; j < i; j++)
                    inner.add(arg1Fields[j].equal((Field) arg2Fields[j]));

                inner.add(arg1Fields[i].compare(
                    equal && i == arg1Fields.length - 1 ? op : order,
                    (Field) arg2Fields[i])
                );

                outer.add(DSL.and(inner));
            }

            Condition result = DSL.or(outer);

            // [#2658] For performance reasons, an additional, redundant
            // predicate is factored out to favour the application of range
            // scans as the topmost predicate is AND-connected, not
            // OR-connected:
            // (A, B, C) > (X, Y, Z)
            // (A >= X) AND ((A > X) OR (A = X AND B > Y) OR (A = X AND B = Y AND C > Z))
            if (arg1Fields.length > 1)
                result = arg1Fields[0].compare(factoredOrder, (Field) arg2Fields[0]).and(result);

            ctx.visit(result);
        }







        else {

            // Some dialects do not support != comparison with rows








            {
                // Some databases need extra parentheses around the RHS
                boolean extraParentheses = false



                    ;

                ctx.visit(arg1)
                   .sql(' ')
                   .sql(op.toSQL())
                   .sql(' ')
                   .sql(extraParentheses ? "(" : "")
                   .visit(arg2)
                   .sql(extraParentheses ? ")" : "");
            }
        }
    }

    /**
     * @deprecated - [#12425] After the QOM refactoring, this should be improved
     */
    @Deprecated
    static final org.jooq.Comparator comparator(Condition condition) {
        if (condition instanceof RowEq)
            return org.jooq.Comparator.EQUALS;
        else if (condition instanceof RowNe)
            return org.jooq.Comparator.NOT_EQUALS;
        else if (condition instanceof RowGt)
            return org.jooq.Comparator.GREATER;
        else if (condition instanceof RowGe)
            return org.jooq.Comparator.GREATER_OR_EQUAL;
        else if (condition instanceof RowLt)
            return org.jooq.Comparator.LESS;
        else if (condition instanceof RowLe)
            return org.jooq.Comparator.LESS_OR_EQUAL;
        else
            return null;
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Row $arg1() {
        return arg1;
    }

    @Override
    public final Row $arg2() {
        return arg2;
    }

    @Override
    public final QOM.RowEq $arg1(Row newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.RowEq $arg2(Row newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Row, ? super Row, ? extends QOM.RowEq> $constructor() {
        return (a1, a2) -> new RowEq(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.RowEq o) {
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
