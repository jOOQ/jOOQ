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
 * The <code>SUBSTRING INDEX</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class SubstringIndex
extends
    AbstractField<String>
implements
    MSubstringIndex
{

    final Field<String>           string;
    final Field<String>           delimiter;
    final Field<? extends Number> n;

    SubstringIndex(
        Field<String> string,
        Field<String> delimiter,
        Field<? extends Number> n
    ) {
        super(
            N_SUBSTRING_INDEX,
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





















            default:
                ctx.visit(function(N_SUBSTRING_INDEX, getDataType(), string, delimiter, n));
                break;
        }
    }















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $string() {
        return string;
    }

    @Override
    public final Field<String> $delimiter() {
        return delimiter;
    }

    @Override
    public final Field<? extends Number> $n() {
        return n;
    }

    @Override
    public final MSubstringIndex $string(MField<String> newValue) {
        return constructor().apply(newValue, $delimiter(), $n());
    }

    @Override
    public final MSubstringIndex $delimiter(MField<String> newValue) {
        return constructor().apply($string(), newValue, $n());
    }

    @Override
    public final MSubstringIndex $n(MField<? extends Number> newValue) {
        return constructor().apply($string(), $delimiter(), newValue);
    }

    public final Function3<? super MField<String>, ? super MField<String>, ? super MField<? extends Number>, ? extends MSubstringIndex> constructor() {
        return (a1, a2, a3) -> new SubstringIndex((Field<String>) a1, (Field<String>) a2, (Field<? extends Number>) a3);
    }

    @Override
    public final MQueryPart replace(
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        return QOM.replace(
            this,
            $string(),
            $delimiter(),
            $n(),
            constructor()::apply,
            recurse,
            replacement
        );
    }

    @Override
    public final <R> R traverse(
        R init,
        Predicate<? super R> abort,
        Predicate<? super MQueryPart> recurse,
        BiFunction<? super R, ? super MQueryPart, ? extends R> accumulate
    ) {
        return QOM.traverse(
            init, abort, recurse, accumulate, this,
            $string(),
            $delimiter(),
            $n()
        );
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof SubstringIndex) {
            return
                StringUtils.equals($string(), ((SubstringIndex) that).$string()) &&
                StringUtils.equals($delimiter(), ((SubstringIndex) that).$delimiter()) &&
                StringUtils.equals($n(), ((SubstringIndex) that).$n())
            ;
        }
        else
            return super.equals(that);
    }
}
