/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: http://www.jooq.org/licenses
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
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>RTRIM</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Rtrim
extends
    AbstractField<String>
{

    private final Field<String> string;
    private final Field<String> characters;

    Rtrim(
        Field<String> string
    ) {
        super(
            N_RTRIM,
            allNotNull(VARCHAR, string)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.characters = null;
    }

    Rtrim(
        Field<String> string,
        Field<String> characters
    ) {
        super(
            N_RTRIM,
            allNotNull(VARCHAR, string, characters)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.characters = nullSafeNotNull(characters, VARCHAR);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        if (characters == null) {
            switch (ctx.family()) {
                case FIREBIRD:
                    ctx.visit(N_TRIM).sql('(').visit(K_TRAILING).sql(' ').visit(K_FROM).sql(' ').visit(string).sql(')');
                    break;

                default:
                    ctx.visit(function(N_RTRIM, getDataType(), string));
                    break;
            }
        }
        else {
            switch (ctx.family()) {



                case SQLITE:
                    ctx.visit(function(N_RTRIM, getDataType(), string, characters));
                    break;

                default:
                    ctx.visit(N_TRIM).sql('(').visit(K_TRAILING).sql(' ').visit(characters).sql(' ').visit(K_FROM).sql(' ').visit(string).sql(')');
                    break;
            }
        }
    }
















    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Rtrim) {
            return
                StringUtils.equals(string, ((Rtrim) that).string) &&
                StringUtils.equals(characters, ((Rtrim) that).characters)
            ;
        }
        else
            return super.equals(that);
    }
}
