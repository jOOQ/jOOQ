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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>UNIQUE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Unique
extends
    AbstractCondition
implements
    QOM.Unique
{

    final Select<?> query;

    Unique(
        Select<?> query
    ) {

        this.query = query;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            case H2:
                ctx.visit(K_UNIQUE).sql(' ');
                visitSubquery(ctx, query, SubqueryCharacteristics.PREDICAND);
                break;

            default:
                Table<?> queryTable = query.asTable("t");
                Field<?>[] queryFields = queryTable.fields();
                Select<?> subquery = select(one())
                    .from(queryTable)
                    .where(row(queryFields).isNotNull())
                    .groupBy(queryFields)
                    .having(DSL.count().gt(one()));

                // TODO: [#7362] [#10304] Find a better way to prevent double negation and unnecessary parentheses
                ctx.visit(notExists(subquery));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Select<?> $arg1() {
        return query;
    }

    @Override
    public final QOM.Unique $arg1(Select<?> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Select<?>, ? extends QOM.Unique> $constructor() {
        return (a1) -> new Unique(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Unique o) {
            return
                StringUtils.equals($query(), o.$query())
            ;
        }
        else
            return super.equals(that);
    }
}
