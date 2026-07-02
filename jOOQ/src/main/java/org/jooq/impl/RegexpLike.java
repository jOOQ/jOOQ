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
 * The <code>REGEXP LIKE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class RegexpLike
extends
    AbstractCondition
implements
    QOM.RegexpLike
{

    final Field<?>      search;
    final Field<String> pattern;

    RegexpLike(
        Field<?> search,
        Field<String> pattern
    ) {

        this.search = nullableIf(false, Tools.nullSafe(search, pattern.getDataType()));
        this.pattern = nullableIf(false, Tools.nullSafe(pattern, search.getDataType()));
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            // [#620] These databases are compatible with the MySQL syntax




            case CUBRID:
            case H2:
            case MARIADB:
            case MYSQL:
            case SQLITE:
                ctx.visit(search)
                   .sql(' ')
                   .visit(K_REGEXP)
                   .sql(' ')
                   .visit(pattern);

                break;

            // [#620] HSQLDB has its own syntax
            case HSQLDB:
                ctx.visit(keyword("regexp_matches")).sql('(').visit(search).sql(", ").visit(pattern).sql(')');
                break;

            // [#620] Postgres has its own syntax



            case DUCKDB:
            case POSTGRES:
            case YUGABYTEDB:
                ctx.sql('(').visit(search).sql(" ~ ").visit(pattern).sql(')');
                break;



















            // Render the SQL standard for those databases that do not support
            // regular expressions
            default: {
                ctx.sql('(')
                   .visit(search)
                   .sql(' ')
                   .visit(K_LIKE_REGEX)
                   .sql(' ')
                   .visit(pattern)
                   .sql(')');

                break;
            }
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return search;
    }

    @Override
    public final Field<String> $arg2() {
        return pattern;
    }

    @Override
    public final QOM.RegexpLike $arg1(Field<?> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.RegexpLike $arg2(Field<String> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<?>, ? super Field<String>, ? extends QOM.RegexpLike> $constructor() {
        return (a1, a2) -> new RegexpLike(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.RegexpLike o) {
            return
                Objects.equals($search(), o.$search()) &&
                Objects.equals($pattern(), o.$pattern())
            ;
        }
        else
            return super.equals(that);
    }
}
