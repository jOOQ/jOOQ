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
 * The <code>CONCAT WS</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ConcatWs
extends
    AbstractField<String>
implements
    QOM.ConcatWs
{

    final Field<String>                              separator;
    final QueryPartListView<? extends Field<String>> values;

    ConcatWs(
        Field<String> separator,
        Collection<? extends Field<String>> values
    ) {
        super(
            N_CONCAT_WS,
            allNotNull(VARCHAR, separator)
        );

        this.separator = nullSafeNotNull(separator, VARCHAR);
        this.values = new QueryPartList<>(values);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {















            case DERBY:
            case FIREBIRD:
                return false;


            case H2:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {



























            case DERBY:
            case FIREBIRD:
                ctx.visit(values.isEmpty()
                ? inline(null, getDataType())
                : values.size() == 1
                ? when(separator.isNotNull(), DSL.nvl(values.get(0), inline("")))
                : when(separator.eq(inline("")), concatEmulation())
                  .when(separator.isNotNull(), DSL.ltrim(concatEmulation(), separator)));
                break;


            case H2:
                ctx.visit(values.isEmpty()
                ? inline(null, getDataType())
                : values.size() == 1
                ? when(separator.isNotNull(), DSL.nvl(values.get(0), inline("")))
                : function(N_CONCAT_WS, getDataType(), Tools.concat(Arrays.asList(separator), (List<Field<String>>) values)));
                break;

            default:
                ctx.visit(function(N_CONCAT_WS, getDataType(), Tools.concat(Arrays.asList(separator), (List<Field<String>>) values)));
                break;
        }
    }














    private final Field<String> concatEmulation() {
        return DSL.concat(map(values, v -> when(v.isNull(), inline("")).else_(separator.concat(v))).toArray(EMPTY_FIELD));
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $arg1() {
        return separator;
    }

    @Override
    public final QOM.UnmodifiableList<? extends Field<String>> $arg2() {
        return QOM.unmodifiable(values);
    }

    @Override
    public final QOM.ConcatWs $arg1(Field<String> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.ConcatWs $arg2(QOM.UnmodifiableList<? extends Field<String>> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<String>, ? super Collection<? extends Field<String>>, ? extends QOM.ConcatWs> $constructor() {
        return (a1, a2) -> new ConcatWs(a1, (Collection<? extends Field<String>>) a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ConcatWs o) {
            return
                Objects.equals($separator(), o.$separator()) &&
                Objects.equals($values(), o.$values())
            ;
        }
        else
            return super.equals(that);
    }
}
