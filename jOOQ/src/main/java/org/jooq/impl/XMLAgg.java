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

// ...
import static org.jooq.impl.Names.N_XMLAGG;

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.XML;
import org.jooq.XMLAggOrderByStep;


/**
 * @author Lukas Eder
 */
final class XMLAgg
extends
    AbstractAggregateFunction<XML, XMLAgg>
implements
    XMLAggOrderByStep<XML>,
    QOM.XMLAgg
{

    XMLAgg(Field<XML> arg) {
        super(false, N_XMLAGG, SQLDataType.XML, arg);
    }

    @Override
    public void accept(Context<?> ctx) {











        ctx.visit(N_XMLAGG).sql('(');
        acceptArguments0(ctx);
        acceptOrderBy(ctx);
        ctx.sql(')');

        acceptFilterClause(ctx);
        acceptOverClause(ctx);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<XML> $arg1() {
        return (Field<XML>) getArguments().get(0);
    }

    @Override
    public final Function1<? super Field<XML>, ? extends QOM.XMLAgg> $constructor() {
        return f -> new XMLAgg(f);
    }

    @Override
    final XMLAgg copy2(Function<XMLAgg, XMLAgg> function) {
        return function.apply((XMLAgg) $constructor().apply($arg1()));
    }
}
