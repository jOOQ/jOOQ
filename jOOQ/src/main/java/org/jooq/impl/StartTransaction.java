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
 * The <code>START TRANSACTION</code> statement.
 */
@SuppressWarnings({ "unused" })
final class StartTransaction
extends
    AbstractRowCountQuery
implements
    QOM.StartTransaction
{

    StartTransaction(Configuration configuration) {
        super(configuration);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {


            case POSTGRES:
            case YUGABYTEDB: {
                // [#7106] PostgreSQL blocks can't start new transactions
                if (ctx.data(DATA_BLOCK_NESTING) == null)
                    ctx.visit(K_START).sql(' ').visit(K_TRANSACTION);
                break;
            }



            case FIREBIRD: {
                // [#7106] Blocks can't start new transactions
                if (ctx.data(DATA_BLOCK_NESTING) == null)
                    ctx.visit(begin());
                break;
            }



            case H2:
            case SQLITE: {
                ctx.visit(K_BEGIN).sql(' ').visit(K_TRANSACTION);
                break;
            }

            case HSQLDB: {
                ctx.visit(K_START).sql(' ').visit(K_TRANSACTION).sql(' ').visit(K_READ).sql(' ').visit(K_WRITE);
                break;
            }

            default:
                ctx.visit(K_START).sql(' ').visit(K_TRANSACTION);
                break;
        }
    }
}
