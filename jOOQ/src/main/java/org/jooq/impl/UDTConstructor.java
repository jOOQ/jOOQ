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

import static org.jooq.impl.Keywords.K_ROW;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
// ...
// ...
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.impl.QOM.UnmodifiableList;

final class UDTConstructor<R extends UDTRecord<R>>
extends
    AbstractField<R>
implements
    QOM.UDTConstructor<R>
{

    final UDT<R>                  udt;
    final QueryPartList<Field<?>> args;

    UDTConstructor(UDT<R> udt, Field<?>[] args) {
        this(udt, Arrays.asList(args));
    }

    UDTConstructor(UDT<R> udt, Collection<? extends Field<?>> args) {
        super(udt.getQualifiedName(), udt.getDataType());

        this.udt = udt;
        this.args = new QueryPartList<>(args);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {



            case DUCKDB:
            case POSTGRES:
            case YUGABYTEDB:
                Cast.renderCast(ctx,
                    c -> c.visit(K_ROW).sql('(').visit(args).sql(')'),
                    c -> c.visit(udt.getDataType())
                );
                break;

            default:
                ctx.visit(udt).sql('(').visit(args).sql(')');
                break;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UDT<R> $udt() {
        return udt;
    }

    @Override
    public final UnmodifiableList<Field<?>> $args() {
        return QOM.unmodifiable(args);
    }














}
