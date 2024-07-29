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

import org.jooq.Context;
import org.jooq.Lambda1;
import org.jooq.QueryPart;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class LambdaImpl1<Q1 extends QueryPart, QR extends QueryPart>
extends
    AbstractQueryPart
implements
    Lambda1<Q1, QR>
{

    final Q1 arg1;
    final QR result;

    LambdaImpl1(Q1 arg1, QR result) {
        this.arg1 = arg1;
        this.result = result;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(arg1).sql(" -> ").visit(result);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Q1 $arg1() {
        return arg1;
    }

    @Override
    public final QR $result() {
        return result;
    }















    // -------------------------------------------------------------------------
    // XXX: Object API
    // -------------------------------------------------------------------------

}
