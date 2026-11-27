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
 * The <code>LPAD</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Lpad
extends
    AbstractField<String>
implements
    QOM.Lpad
{

    final Field<String>           string;
    final Field<? extends Number> length;
    final Field<String>           character;

    Lpad(
        Field<String> string,
        Field<? extends Number> length
    ) {
        super(
            N_LPAD,
            allNotNull(VARCHAR, string, length)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.length = nullSafeNotNull(length, INTEGER);
        this.character = null;
    }

    Lpad(
        Field<String> string,
        Field<? extends Number> length,
        Field<String> character
    ) {
        super(
            N_LPAD,
            allNotNull(VARCHAR, string, length, character)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.length = nullSafeNotNull(length, INTEGER);
        this.character = nullSafeNotNull(character, VARCHAR);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private final Field<String> characterOrBlank() {
        return character == null ? inline(" ") : character;
    }

    /**
     * [#20577] Whether the padding string is known to consist of a single
     * character.
     * <p>
     * This can only be checked in the client if the padding string is a
     * {@link Param}. Any other expression must be assumed to produce a padding
     * string of length <code>&gt; 1</code>.
     */
    static final boolean isSingleCharacterPadding(Field<String> character) {
        if (character instanceof Param<String> p) {
            String c = p.getValue();

            // NULL padding strings produce NULL in both the native function
            // and the emulation, so there's no need to emulate them
            return c == null || c.length() == 1;
        }
        else
            return false;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {













            // [#20561] SQLite doesn't support LPAD(). The emulation must truncate
            //          the string if it is longer than the length argument, and
            //          produce an empty string if the length argument is <= 0
            case SQLITE:
                if (character == null) {

                    // substr(printf('%*s', max(n - length(s), 0), '') || s, 1, n)
                    ctx.visit(N_SUBSTR).sql('(')
                        .visit(N_PRINTF).sql("('%*s', ")
                            .visit(N_MAX).sql('(').visit(length).sql(" - ").visit(N_LENGTH).sql('(').visit(string).sql("), 0)")
                        .sql(", '') || ").visit(string)
                    .sql(", 1, ").visit(length).sql(')');
                }
                else {

                    // substr(substr(replace(printf('%*s', n, ''), ' ', c), 1, n - length(s)) || s, 1, n)
                    ctx.visit(N_SUBSTR).sql('(')
                        .visit(N_SUBSTR).sql('(')
                            .visit(N_REPLACE).sql('(')
                                .visit(N_PRINTF).sql("('%*s', ").visit(length).sql(", '')")
                            .sql(", ' ', ").visit(character)
                        .sql("), 1, ").visit(length).sql(" - ").visit(N_LENGTH).sql('(').visit(string)
                    .sql(")) || ").visit(string)
                    .sql(", 1, ").visit(length).sql(')');
                }

                break;

            // [#20577] H2 only uses the first character of the padding string
            case H2:
                if (isSingleCharacterPadding(characterOrBlank()))
                    ctx.visit(function(N_LPAD, getDataType(), string, length, characterOrBlank()));

                // left(repeat(c, n - length(s)), n - length(s)) || left(s, n)
                else {
                    Field<? extends Number> l = isub(length, DSL.length(string));
                    ctx.visit(DSL.concat(DSL.left(DSL.repeat(characterOrBlank(), l), l), DSL.left(string, length)));
                }

                break;

            case CLICKHOUSE:
                ctx.visit(function(N_leftPad, getDataType(), string, length, characterOrBlank()));
                break;

            default:
                ctx.visit(function(N_LPAD, getDataType(), string, length, characterOrBlank()));
                break;
        }
    }

















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $arg1() {
        return string;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return length;
    }

    @Override
    public final Field<String> $arg3() {
        return character;
    }

    @Override
    public final QOM.Lpad $arg1(Field<String> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.Lpad $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.Lpad $arg3(Field<String> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<String>, ? super Field<? extends Number>, ? super Field<String>, ? extends QOM.Lpad> $constructor() {
        return (a1, a2, a3) ->
              a3 == null
            ? new Lpad(a1, a2)
            : new Lpad(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Lpad o) {
            return
                Objects.equals($string(), o.$string()) &&
                Objects.equals($length(), o.$length()) &&
                Objects.equals($character(), o.$character())
            ;
        }
        else
            return super.equals(that);
    }
}

