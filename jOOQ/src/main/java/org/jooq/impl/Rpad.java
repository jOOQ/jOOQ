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
 * The <code>RPAD</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Rpad
extends
    AbstractField<String>
implements
    QOM.Rpad
{

    final Field<String>           string;
    final Field<? extends Number> length;
    final Field<String>           character;

    Rpad(
        Field<String> string,
        Field<? extends Number> length
    ) {
        super(
            N_RPAD,
            allNotNull(VARCHAR, string, length)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.length = nullSafeNotNull(length, INTEGER);
        this.character = null;
    }

    Rpad(
        Field<String> string,
        Field<? extends Number> length,
        Field<String> character
    ) {
        super(
            N_RPAD,
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

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {













            // This beautiful expression was contributed by "Ludo", here:
            // http://stackoverflow.com/questions/6576343/how-to-simulate-lpad-rpad-with-sqlite
            case SQLITE:
                ctx.visit(string).sql(" || ").visit(N_SUBSTR).sql('(')
                    .visit(N_REPLACE).sql('(')
                        .visit(N_HEX).sql('(')
                            .visit(N_ZEROBLOB).sql('(')
                                .visit(length)
                        .sql(")), '00', ").visit(characterOrBlank())
                    .sql("), 1, ").visit(length).sql(" - ").visit(N_LENGTH).sql('(').visit(string).sql(')')
                .sql(')');
                break;

            default:
                ctx.visit(function(N_RPAD, getDataType(), string, length, characterOrBlank()));
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
    public final QOM.Rpad $arg1(Field<String> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.Rpad $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.Rpad $arg3(Field<String> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<String>, ? super Field<? extends Number>, ? super Field<String>, ? extends QOM.Rpad> $constructor() {
        return (a1, a2, a3) -> new Rpad(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Rpad o) {
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
