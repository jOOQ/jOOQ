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
import java.util.Objects;
import java.util.Set;



/**
 * The <code>CURRENT SCHEMA</code> statement.
 */
@SuppressWarnings({ "unused" })
final class CurrentSchema
extends
    AbstractField<String>
implements
    QOM.CurrentSchema
{

    CurrentSchema() {
        super(
            N_CURRENT_SCHEMA,
            allNotNull(VARCHAR)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {



















            case CUBRID:
            case FIREBIRD:
            case SQLITE:
                ctx.visit(inline(""));
                break;

            case DERBY:
                ctx.visit(K_CURRENT).sql(' ').visit(K_SCHEMA);
                break;

            case H2:
                ctx.visit(K_SCHEMA).sql("()");
                break;



            case MARIADB:
            case MYSQL:
                ctx.visit(K_DATABASE).sql("()");
                break;




            case HSQLDB:
            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(K_CURRENT_SCHEMA);
                break;

            default:
                ctx.visit(K_CURRENT_SCHEMA).sql("()");
                break;
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Function0<? extends QOM.CurrentSchema> $constructor() {
        return () -> new CurrentSchema();
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.CurrentSchema o) {
            return true;
        }
        else
            return super.equals(that);
    }
}
