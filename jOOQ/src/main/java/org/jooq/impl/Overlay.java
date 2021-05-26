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
 * The <code>OVERLAY</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Overlay
extends
    AbstractField<String>
{

    private final Field<String>           in;
    private final Field<String>           placing;
    private final Field<? extends Number> startIndex;
    private final Field<? extends Number> length;

    Overlay(
        Field<String> in,
        Field<String> placing,
        Field<? extends Number> startIndex
    ) {
        super(
            N_OVERLAY,
            allNotNull(VARCHAR, in, placing, startIndex)
        );

        this.in = nullSafeNotNull(in, VARCHAR);
        this.placing = nullSafeNotNull(placing, VARCHAR);
        this.startIndex = nullSafeNotNull(startIndex, INTEGER);
        this.length = null;
    }

    Overlay(
        Field<String> in,
        Field<String> placing,
        Field<? extends Number> startIndex,
        Field<? extends Number> length
    ) {
        super(
            N_OVERLAY,
            allNotNull(VARCHAR, in, placing, startIndex, length)
        );

        this.in = nullSafeNotNull(in, VARCHAR);
        this.placing = nullSafeNotNull(placing, VARCHAR);
        this.startIndex = nullSafeNotNull(startIndex, INTEGER);
        this.length = nullSafeNotNull(length, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT     = SQLDialect.supportedBy(DERBY, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> SUPPORT_INSERT = SQLDialect.supportedBy(H2, MARIADB, MYSQL);

    @Override
    public final void accept(Context<?> ctx) {
        Field<? extends Number> l = length;











        if (l != null) {
            if (SUPPORT_INSERT.contains(ctx.dialect()))
                ctx.visit(function(N_INSERT, getDataType(), in, startIndex, l, placing));
            else if (NO_SUPPORT.contains(ctx.dialect()))
                ctx.visit(
                    DSL.substring(in, inline(1), isub(startIndex, inline(1)))
                       .concat(placing)
                       .concat(DSL.substring(in, iadd(startIndex, l)))
                );
            else
                ctx.visit(N_OVERLAY).sql('(').visit(in).sql(' ')
                   .visit(K_PLACING).sql(' ').visit(placing).sql(' ')
                   .visit(K_FROM).sql(' ').visit(startIndex).sql(' ')
                   .visit(K_FOR).sql(' ').visit(l).sql(')');
        }
        else {
            if (SUPPORT_INSERT.contains(ctx.dialect()))
                ctx.visit(function(N_INSERT, getDataType(), in, startIndex, DSL.length(placing), placing));
            else if (NO_SUPPORT.contains(ctx.dialect()))
                ctx.visit(
                    DSL.substring(in, inline(1), isub(startIndex, inline(1)))
                       .concat(placing)
                       .concat(DSL.substring(in, iadd(startIndex, DSL.length(placing))))
                );
            else
                ctx.visit(N_OVERLAY).sql('(').visit(in).sql(' ')
                   .visit(K_PLACING).sql(' ').visit(placing).sql(' ')
                   .visit(K_FROM).sql(' ').visit(startIndex).sql(')');
        }
    }


















    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Overlay) {
            return
                StringUtils.equals(in, ((Overlay) that).in) &&
                StringUtils.equals(placing, ((Overlay) that).placing) &&
                StringUtils.equals(startIndex, ((Overlay) that).startIndex) &&
                StringUtils.equals(length, ((Overlay) that).length)
            ;
        }
        else
            return super.equals(that);
    }
}
