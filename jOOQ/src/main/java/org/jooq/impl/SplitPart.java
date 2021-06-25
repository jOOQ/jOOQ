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


/**
 * The <code>SPLIT PART</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class SplitPart
extends
    AbstractField<String>
{

    private final Field<String>           string;
    private final Field<String>           delimiter;
    private final Field<? extends Number> n;

    SplitPart(
        Field<String> string,
        Field<String> delimiter,
        Field<? extends Number> n
    ) {
        super(
            N_SPLIT_PART,
            allNotNull(VARCHAR, string, delimiter, n)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.delimiter = nullSafeNotNull(delimiter, VARCHAR);
        this.n = nullSafeNotNull(n, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {











































            case MARIADB:
            case MYSQL:
                ctx.visit(DSL.substring(
                    substringIndex(string, delimiter, n),
                    case_((Field) n).when(one(), one()).else_(
                        DSL.length(substringIndex(string, delimiter, n.minus(one())))
                        .plus(DSL.length(delimiter))
                        .plus(one())
                    )
                ));
                break;

            case HSQLDB: {
                Field<String> rS = DSL.field(name("s"), String.class);
                Field<Integer> rN = DSL.field(name("n"), int.class);
                Field<String> rD = DSL.field(name("d"), String.class);
                Field<Integer> rPos = DSL.position(rS, rD);
                Field<Integer> rLen = DSL.length(rD);
                Field<Integer> rPosN = DSL.nullif(rPos, zero());
                Field<String> rStr = DSL.substring(rS, rPosN.plus(rLen));
                Field<String> rRes = DSL.coalesce(DSL.substring(rS, one(), rPosN.minus(one())), rS);

                CommonTableExpression<?> s1 = name("s1")
                    .fields("s", "d")
                    .as(select(string, delimiter));
                CommonTableExpression<?> s2 = name("s2")
                    .fields("s", "d", "x", "n")
                    .as(select(rStr, rD, rRes, one()).from(s1)
                        .unionAll(
                        select(rStr, rD, rRes, rN.plus(one())).from(name("s2")).where(rS.isNotNull())
                    ));

                visitSubquery(
                    ctx,
                    withRecursive(s1, s2).select(DSL.coalesce(DSL.max(DSL.field(name("x"))), inline(""))).from(s2).where(s2.field("n").eq((Field) n))
                );
                break;
            }

            default:
                ctx.visit(function(N_SPLIT_PART, getDataType(), string, delimiter, n));
                break;
        }
    }















    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof SplitPart) {
            return
                StringUtils.equals(string, ((SplitPart) that).string) &&
                StringUtils.equals(delimiter, ((SplitPart) that).delimiter) &&
                StringUtils.equals(n, ((SplitPart) that).n)
            ;
        }
        else
            return super.equals(that);
    }
}
