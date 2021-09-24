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
 * The <code>LIKE IGNORE CASE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class LikeIgnoreCase
extends
    AbstractCondition
implements
    LikeEscapeStep
{

    final Field<?>      value;
    final Field<String> pattern;
          Character     escape;

    LikeIgnoreCase(
        Field<?> value,
        Field<String> pattern
    ) {
        this(
            value,
            pattern,
            null
        );
    }

    LikeIgnoreCase(
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
    public final LikeIgnoreCase escape(char escape) {
        this.escape = escape;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        Like.accept0(ctx, value, org.jooq.Comparator.LIKE_IGNORE_CASE, pattern, escape);
    }












    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof LikeIgnoreCase) {
            return
                StringUtils.equals(value, ((LikeIgnoreCase) that).value) &&
                StringUtils.equals(pattern, ((LikeIgnoreCase) that).pattern) &&
                StringUtils.equals(escape, ((LikeIgnoreCase) that).escape)
            ;
        }
        else
            return super.equals(that);
    }
}
