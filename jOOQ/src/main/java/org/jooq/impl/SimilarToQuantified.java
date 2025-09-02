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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>SIMILAR TO</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class SimilarToQuantified
extends
    AbstractCondition
implements
    QOM.SimilarToQuantified,
    LikeEscapeStep
{

    final Field<?>                                             value;
    final org.jooq.QuantifiedSelect<? extends Record1<String>> pattern;
          Character                                            escape;

    SimilarToQuantified(
        Field<?> value,
        org.jooq.QuantifiedSelect<? extends Record1<String>> pattern
    ) {
        this(
            value,
            pattern,
            null
        );
    }

    SimilarToQuantified(
        Field<?> value,
        org.jooq.QuantifiedSelect<? extends Record1<String>> pattern,
        Character escape
    ) {

        this.value = nullSafeNotNull(value, OTHER);
        this.pattern = pattern;
        this.escape = escape;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final SimilarToQuantified escape(char escape) {
        this.escape = escape;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        EqQuantified.acceptCompareCondition(ctx, this, (Field<String>) value, org.jooq.Comparator.SIMILAR_TO, pattern, escape);
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return value;
    }

    @Override
    public final org.jooq.QuantifiedSelect<? extends Record1<String>> $arg2() {
        return pattern;
    }

    @Override
    public final Character $arg3() {
        return escape;
    }

    @Override
    public final QOM.SimilarToQuantified $arg1(Field<?> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.SimilarToQuantified $arg2(org.jooq.QuantifiedSelect<? extends Record1<String>> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.SimilarToQuantified $arg3(Character newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<?>, ? super org.jooq.QuantifiedSelect<? extends Record1<String>>, ? super Character, ? extends QOM.SimilarToQuantified> $constructor() {
        return (a1, a2, a3) -> new SimilarToQuantified(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.SimilarToQuantified o) {
            return
                Objects.equals($value(), o.$value()) &&
                Objects.equals($pattern(), o.$pattern()) &&
                Objects.equals($escape(), o.$escape())
            ;
        }
        else
            return super.equals(that);
    }
}
