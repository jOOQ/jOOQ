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
import java.util.Set;



/**
 * The <code>STRING TO ARRAY</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class StringToArray
extends
    AbstractField<String[]>
implements
    QOM.StringToArray
{

    final Field<String> string;
    final Field<String> delimiter;
    final Field<String> nullString;

    StringToArray(
        Field<String> string,
        Field<String> delimiter
    ) {
        super(
            N_STRING_TO_ARRAY,
            allNotNull(VARCHAR.array(), string, delimiter)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.delimiter = nullSafeNotNull(delimiter, VARCHAR);
        this.nullString = null;
    }

    StringToArray(
        Field<String> string,
        Field<String> delimiter,
        Field<String> nullString
    ) {
        super(
            N_STRING_TO_ARRAY,
            allNotNull(VARCHAR.array(), string, delimiter, nullString)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.delimiter = nullSafeNotNull(delimiter, VARCHAR);
        this.nullString = nullSafeNotNull(nullString, VARCHAR);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {















            default:
                if (nullString != null)
                    ctx.visit(function(N_STRING_TO_ARRAY, getDataType(), string, delimiter, nullString));
                else
                    ctx.visit(function(N_STRING_TO_ARRAY, getDataType(), string, delimiter));
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
    public final Field<String> $arg2() {
        return delimiter;
    }

    @Override
    public final Field<String> $arg3() {
        return nullString;
    }

    @Override
    public final QOM.StringToArray $arg1(Field<String> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.StringToArray $arg2(Field<String> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.StringToArray $arg3(Field<String> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<String>, ? super Field<String>, ? super Field<String>, ? extends QOM.StringToArray> $constructor() {
        return (a1, a2, a3) -> new StringToArray(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.StringToArray o) {
            return
                StringUtils.equals($string(), o.$string()) &&
                StringUtils.equals($delimiter(), o.$delimiter()) &&
                StringUtils.equals($nullString(), o.$nullString())
            ;
        }
        else
            return super.equals(that);
    }
}
