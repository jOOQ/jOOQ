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
 * The <code>XMLPI</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Xmlpi
extends
    AbstractField<XML>
{

    final Name     target;
    final Field<?> content;

    Xmlpi(
        Name target
    ) {
        super(
            N_XMLPI,
            allNotNull(XML)
        );

        this.target = target;
        this.content = null;
    }

    Xmlpi(
        Name target,
        Field<?> content
    ) {
        super(
            N_XMLPI,
            allNotNull(XML, content)
        );

        this.target = target;
        this.content = nullSafeNotNull(content, OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        ctx.visit(N_XMLPI).sql('(').visit(K_NAME).sql(' ').visit(target);

        if (content != null)
            ctx.sql(", ").visit(content);

        ctx.sql(')');
    }
















    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Xmlpi) {
            return
                StringUtils.equals(target, ((Xmlpi) that).target) &&
                StringUtils.equals(content, ((Xmlpi) that).content)
            ;
        }
        else
            return super.equals(that);
    }
}
