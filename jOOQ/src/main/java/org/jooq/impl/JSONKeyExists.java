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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
 * The <code>JSON KEY EXISTS</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class JSONKeyExists
extends
    AbstractCondition
implements
    QOM.JSONKeyExists
{

    final Field<JSON>   json;
    final Field<String> key;

    JSONKeyExists(
        Field<JSON> json,
        Field<String> key
    ) {

        this.json = nullSafeNotNull(json, JSON);
        this.key = nullSafeNotNull(key, VARCHAR);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {




            case CLICKHOUSE:
            case MARIADB:
            case MYSQL:
            case SQLITE:
            case TRINO:
                return false;







            case POSTGRES:
            case YUGABYTEDB:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {










            case CLICKHOUSE:
            case MARIADB:
            case MYSQL:
            case SQLITE:
            case TRINO:
                ctx.visit(jsonGetAttribute(json, key).isNotNull());
                break;









            case POSTGRES:
            case YUGABYTEDB: {
                ctx.sql('(').visit(json.cast(JSONB)).sql(" ?? ").visit(key).sql(')');
                break;
            }

            default:
                ctx.visit(function(N_JSON_KEY_EXISTS, BOOLEAN, json, key));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<JSON> $arg1() {
        return json;
    }

    @Override
    public final Field<String> $arg2() {
        return key;
    }

    @Override
    public final QOM.JSONKeyExists $arg1(Field<JSON> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.JSONKeyExists $arg2(Field<String> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<JSON>, ? super Field<String>, ? extends QOM.JSONKeyExists> $constructor() {
        return (a1, a2) -> new JSONKeyExists(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONKeyExists o) {
            return
                StringUtils.equals($json(), o.$json()) &&
                StringUtils.equals($key(), o.$key())
            ;
        }
        else
            return super.equals(that);
    }
}
