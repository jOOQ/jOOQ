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
 * The <code>SIMILAR TO</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class SimilarTo
extends
    AbstractCondition
implements
    QOM.SimilarTo,
    LikeEscapeStep
{

    final Field<?>      value;
    final Field<String> pattern;
          Character     escape;

    SimilarTo(
        Field<?> value,
        Field<String> pattern
    ) {
        this(
            value,
            pattern,
            null
        );
    }

    SimilarTo(
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
    public final SimilarTo escape(char escape) {
        this.escape = escape;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        Like.accept0(ctx, value, org.jooq.Comparator.SIMILAR_TO, pattern, escape);
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
    public final QOM.SimilarTo $arg1(Field<?> newValue) {
        return constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.SimilarTo $arg2(Field<String> newValue) {
        return constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.SimilarTo $arg3(Character newValue) {
        return constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<?>, ? super Field<String>, ? super Character, ? extends QOM.SimilarTo> constructor() {
        return (a1, a2, a3) -> new SimilarTo(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.SimilarTo) { QOM.SimilarTo o = (QOM.SimilarTo) that;
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
