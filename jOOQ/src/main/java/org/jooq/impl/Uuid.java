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

import java.util.UUID;


/**
 * The <code>UUID</code> statement.
 */
@SuppressWarnings({ "unused" })
final class Uuid
extends
    AbstractField<UUID>
implements
    QOM.Uuid
{

    Uuid() {
        super(
            N_UUID,
            allNotNull(UUID)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {












            case POSTGRES:
                return true;






            case FIREBIRD:
                return false;






            case H2:
                return true;

            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case MYSQL:
                return true;









            case SQLITE:
                return false;









            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {





















            case POSTGRES:
                ctx.visit(function(N_GEN_RANDOM_UUID, getDataType()));
                break;












            case FIREBIRD:
                ctx.visit(function(N_UUID_TO_CHAR, getDataType(), function(N_GEN_UUID, getDataType())));
                break;











            case H2:
                ctx.visit(function(N_RANDOM_UUID, getDataType()));
                break;

            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case MYSQL:
                ctx.visit(function(N_UUID, getDataType()));
                break;















            case SQLITE: {
                // See https://stackoverflow.com/a/22725697/521799
                Field<String> u = DSL.field(name("u"), VARCHAR);

                ctx.visit(DSL.field(
                    select(
                                DSL.substring(u, inline(1), inline(8)).concat(inline('-'))
                        .concat(DSL.substring(u, inline(9), inline(4)).concat(inline('-')))
                        .concat(DSL.substring(u, inline(13), inline(4)).concat(inline('-')))
                        .concat(DSL.substring(u, inline(17), inline(4)).concat(inline('-')))
                        .concat(DSL.substring(u, inline(21)))
                    )
                    .from(select(DSL.lower(function(N_HEX, VARCHAR, function(N_RANDOMBLOB, BINARY, inline(16)))).as(u)).asTable(unquotedName("t")))
                ));
                break;
            }











            default:
                ctx.visit(function(N_UUID, getDataType()));
                break;
        }
    }










    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Function0<? extends QOM.Uuid> $constructor() {
        return () -> new Uuid();
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Uuid o) {
            return true;
        }
        else
            return super.equals(that);
    }
}
